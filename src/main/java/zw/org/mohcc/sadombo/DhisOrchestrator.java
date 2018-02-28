/* 
 * Copyright (c) 2018, ITINordic
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package zw.org.mohcc.sadombo;

import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import java.io.IOException;
import java.util.Map;
import org.apache.http.HttpStatus;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.openhim.mediator.engine.MediatorConfig;
import org.openhim.mediator.engine.messages.ExceptError;
import org.openhim.mediator.engine.messages.FinishRequest;
import org.openhim.mediator.engine.messages.MediatorHTTPRequest;
import org.openhim.mediator.engine.messages.MediatorHTTPResponse;
import zw.org.mohcc.sadombo.data.CompletionInput;
import zw.org.mohcc.sadombo.data.DataSet;
import zw.org.mohcc.sadombo.data.Group;
import zw.org.mohcc.sadombo.data.OrganisationUnit;
import zw.org.mohcc.sadombo.mapper.RequestHeaderMapper;
import zw.org.mohcc.sadombo.mapper.RequestTarget;
import zw.org.mohcc.sadombo.mapper.RequestTargetMapper;
import zw.org.mohcc.sadombo.security.SecurityManager;
import zw.org.mohcc.sadombo.transformer.RequestBodyTransformer;
import zw.org.mohcc.sadombo.transformer.ResponseTransformer;
import zw.org.mohcc.sadombo.utils.AdxUtility;
import zw.org.mohcc.sadombo.utils.ConfigUtility;
import zw.org.mohcc.sadombo.utils.GeneralUtility;
import static zw.org.mohcc.sadombo.utils.GeneralUtility.getBasicAuthorization;
import zw.org.mohcc.sadombo.validator.RequestValidator;
import zw.org.mohcc.sadombo.validator.Validation;

public class DhisOrchestrator extends UntypedActor {

    LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    private final MediatorConfig config;
    private MediatorHTTPRequest originalRequest;
    private final Channels channels;
    private final RequestBodyTransformer requestBodyTransformer;
    private final RequestValidator requestValidator;
    private final RequestTargetMapper requestTargetMapper;
    private final ResponseTransformer responseTransformer;
    private final SecurityManager securityManager;
    private final RequestHeaderMapper requestHeaderMapper;

    private DataSet resolvedDataSet;
    private OrganisationUnit resolvedOrganisationUnit;
    private String completionResponse;
    private Group group;
    private FinishRequest processDhisFinishRequest;

    public DhisOrchestrator(MediatorConfig config) throws IOException {
        this.config = config;
        this.channels = ConfigUtility.getChannels(config);
        this.requestBodyTransformer = ConfigUtility.getRequestBodyTransformer(config);
        this.requestValidator = ConfigUtility.getRequestValidator(config);
        this.requestTargetMapper = ConfigUtility.getRequestTargetMapper(config);
        this.responseTransformer = ConfigUtility.getResponseTransformer(config);
        this.securityManager = ConfigUtility.getSecurityManager(config);
        this.requestHeaderMapper = ConfigUtility.getRequestHeaderMapper(config);
    }

    @Override
    public void onReceive(Object msg) throws Exception {
        if (msg instanceof MediatorHTTPRequest) {
            MediatorHTTPRequest request = (MediatorHTTPRequest) msg;
            if (securityManager.isUserAllowed(request, config)) {
                try {
                    processRequest((MediatorHTTPRequest) msg);
                } catch (SadomboException ex) {
                    log.error(ex, "Error occurred when querying ");
                    request.getRequestHandler().tell(new ExceptError(ex), getSelf());
                }
            } else {
                FinishRequest finishRequest = new FinishRequest("Not allowed", "text/plain", HttpStatus.SC_FORBIDDEN);
                request.getRequestHandler().tell(finishRequest, getSelf());
            }
        } else if (msg instanceof MediatorHTTPResponse) {
            processDhisResponse((MediatorHTTPResponse) msg);
        } else if (msg instanceof DataSetOrchestrator.ResolveDataSetResponse) {
            log.info("DataSetOrchestrator.ResolveDataSetResponse");
            resolvedDataSet = ((DataSetOrchestrator.ResolveDataSetResponse) msg).getResponseObject();
            completeRequest();
        } else if (msg instanceof FacilityOrchestrator.ResolveFacilityResponse) {
            log.info("FacilityOrchestrator.ResolveFacilityResponse");
            resolvedOrganisationUnit = ((FacilityOrchestrator.ResolveFacilityResponse) msg).getResponseObject();
            completeRequest();
        } else if (msg instanceof CompletionOrchestrator.ResolveCompletionResponse) {
            log.info("CompletionOrchestrator.ResolveCompletionResponse");
            completionResponse = ((CompletionOrchestrator.ResolveCompletionResponse) msg).getResponseObject();
            finalizeRequest();
        } else {
            unhandled(msg);
        }
    }

    private void processRequest(MediatorHTTPRequest request) {
        log.info("Querying the DHIS service");
        originalRequest = request;
        Validation validation = requestValidator.validate(request);
        if (validation.isValid()) {
            String requestBody = requestBodyTransformer.transformRequestBody(request);
            Map<String, String> headers = requestHeaderMapper.mapHeaders(request);
            RequestTarget requestTarget = requestTargetMapper.getRequestTarget(request);
            addAuthorizationHeader(headers, request);
            sendRequestToDhisChannel(requestTarget, headers, requestBody, request.getRequestHandler());
        } else {
            FinishRequest finishRequest = new FinishRequest(validation.getErrorMessage(), validation.getErrorHeaders(), validation.getHttpStatus());
            request.getRequestHandler().tell(finishRequest, getSelf());
        }
    }

    private void sendRequestToDhisChannel(RequestTarget requestTarget, Map<String, String> headers, String requestBody, ActorRef actorRef) {
        MediatorHTTPRequest serviceRequest = new MediatorHTTPRequest(
                actorRef,
                getSelf(),
                "DHIS Service",
                requestTarget.getMethod(),
                channels.getDhisChannelScheme(),
                channels.getDhisChannelHost(),
                channels.getDhisChannelPort(),
                channels.getDhisChannelContextPath() + requestTarget.getRelativePath(),
                requestBody,
                headers,
                requestTarget.getParams()
        );
        ActorSelection httpConnector = getContext().actorSelection(config.userPathFor("http-connector"));
        httpConnector.tell(serviceRequest, getSelf());
    }

    private void processDhisResponse(MediatorHTTPResponse response) {
        log.info("Received response from DHIS service");
        processDhisFinishRequest = responseTransformer.transform(response, originalRequest);

        if (AdxUtility.hasAdxContentType(originalRequest) && !GeneralUtility.hasEmptyRequestBody(originalRequest)) {
            group = getGroup(originalRequest);
            //Get data set id
            DataSetOrchestrator.ResolveDataSetRequest dataSetRequest = new DataSetOrchestrator.ResolveDataSetRequest(
                    originalRequest.getRequestHandler(), getSelf(), group.getDataSet());
            ActorRef dataSetOrchestrator = getContext().actorOf(Props.create(DataSetOrchestrator.class, config));
            dataSetOrchestrator.tell(dataSetRequest, getSelf());

            //Get facility id
            FacilityOrchestrator.ResolveFacilityRequest facilityRequest = new FacilityOrchestrator.ResolveFacilityRequest(
                    originalRequest.getRequestHandler(), getSelf(), group.getOrgUnit());
            ActorRef facilityOrchestrator = getContext().actorOf(Props.create(FacilityOrchestrator.class, config));
            facilityOrchestrator.tell(facilityRequest, getSelf());
        } else {
            finalizeRequest();
        }

    }

    private void completeRequest() {
        if (resolvedDataSet != null && resolvedOrganisationUnit != null) {
            CompletionOrchestrator.ResolveCompletionRequest completionRequest = new CompletionOrchestrator.ResolveCompletionRequest(
                    originalRequest.getRequestHandler(), getSelf(), new CompletionInput(resolvedOrganisationUnit.getId(), resolvedDataSet.getId(), group.getPeriod()));
            ActorRef providerResolver = getContext().actorOf(Props.create(CompletionOrchestrator.class,
                    config));
            providerResolver.tell(completionRequest, getSelf());
        }
    }

    private void finalizeRequest() {
        originalRequest.getRespondTo().tell(processDhisFinishRequest, getSelf());
    }

    private void addAuthorizationHeader(Map<String, String> headers, MediatorHTTPRequest request) {
        String dhisAuthorization = null;
        if (request.getHeaders() != null & !request.getHeaders().isEmpty()) {
            dhisAuthorization = request.getHeaders().get("x-dhis-authorization");
        }

        if (dhisAuthorization != null && !dhisAuthorization.trim().isEmpty()) {
            headers.put("Authorization", dhisAuthorization);
            config.getDynamicConfig().put("dhisAuthorization", dhisAuthorization);
        } else {
            String dhisChannelUser = channels.getDhisChannelUser();
            String dhisChannelPassword = channels.getDhisChannelPassword();
            if (dhisChannelUser != null && dhisChannelPassword != null && !dhisChannelUser.trim().isEmpty() && !dhisChannelPassword.trim().isEmpty()) {
                dhisAuthorization = getBasicAuthorization(dhisChannelUser, dhisChannelPassword);
                headers.put("Authorization", dhisAuthorization);
                config.getDynamicConfig().put("dhisAuthorization", dhisAuthorization);
            }
        }
    }

    private Group getGroup(MediatorHTTPRequest request) {
        Namespace namespace = Namespace.getNamespace("urn:ihe:qrph:adx:2015");
        try {
            Document doc = GeneralUtility.getJDom2Document(request.getBody());
            Element rootNode = doc.getRootElement();
            Element groupElement = rootNode.getChild("group", namespace);
            group = new Group();
            group.setOrgUnit(groupElement.getAttribute("orgUnit").getValue());
            group.setDataSet(groupElement.getAttribute("dataSet").getValue());
            group.setPeriod(groupElement.getAttribute("period").getValue());
            return group;
        } catch (IOException | JDOMException exception) {
            throw new SadomboException(exception);
        }
    }

}
