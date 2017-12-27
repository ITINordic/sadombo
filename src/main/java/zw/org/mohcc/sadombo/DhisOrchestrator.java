package zw.org.mohcc.sadombo;

import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import java.io.IOException;
import java.util.Map;
import org.apache.http.HttpStatus;
import org.openhim.mediator.engine.MediatorConfig;
import org.openhim.mediator.engine.messages.ExceptError;
import org.openhim.mediator.engine.messages.FinishRequest;
import org.openhim.mediator.engine.messages.MediatorHTTPRequest;
import org.openhim.mediator.engine.messages.MediatorHTTPResponse;
import zw.org.mohcc.sadombo.mapper.RequestHeaderMapper;
import zw.org.mohcc.sadombo.mapper.RequestTarget;
import zw.org.mohcc.sadombo.mapper.RequestTargetMapper;
import zw.org.mohcc.sadombo.security.SecurityManager;
import zw.org.mohcc.sadombo.transformer.RequestBodyTransformer;
import zw.org.mohcc.sadombo.transformer.ResponseTransformer;
import zw.org.mohcc.sadombo.utils.ConfigUtility;
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
            sendRequestToDhisChannel(requestTarget, headers, requestBody, request.getRequestHandler());
        } else {
            FinishRequest finishRequest = new FinishRequest(validation.getErrorMessage(), validation.getErrorHeaders(), validation.getHttpStatus());
            request.getRequestHandler().tell(finishRequest, getSelf());
        }
    }

    private void sendRequestToDhisChannel(RequestTarget requestTarget, Map<String, String> headers, String requestBody, ActorRef actorRef) {
        addDhisAuthorization(headers);
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

    private void addDhisAuthorization(Map<String, String> headers) {
        String dhisChannelUser = channels.getDhisChannelUser();
        String dhisChannelPassword = channels.getDhisChannelPassword();
        if (dhisChannelUser != null && dhisChannelPassword != null && !dhisChannelUser.trim().isEmpty() && !dhisChannelPassword.trim().isEmpty()) {
            headers.put("Authorization", getBasicAuthorization(dhisChannelUser, dhisChannelPassword));
        }
    }

    private void processDhisResponse(MediatorHTTPResponse response) {
        log.info("Received response from DHIS service");
        FinishRequest finishRequest = responseTransformer.transform(response, originalRequest);
        originalRequest.getRespondTo().tell(finishRequest, getSelf());
    }

}
