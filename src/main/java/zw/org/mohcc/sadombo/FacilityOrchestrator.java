package zw.org.mohcc.sadombo;

import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.openhim.mediator.engine.MediatorConfig;
import org.openhim.mediator.engine.messages.MediatorHTTPRequest;
import org.openhim.mediator.engine.messages.MediatorHTTPResponse;
import org.openhim.mediator.engine.messages.MediatorRequestMessage;
import org.openhim.mediator.engine.messages.SimpleMediatorRequest;
import org.openhim.mediator.engine.messages.SimpleMediatorResponse;
import zw.org.mohcc.sadombo.data.FacilityRequestInput;
import zw.org.mohcc.sadombo.data.OrganisationUnit;
import zw.org.mohcc.sadombo.data.OrganisationUnitWrapper;
import zw.org.mohcc.sadombo.utils.ConfigUtility;

/**
 *
 * @author Charles Chigoriwa
 */
public class FacilityOrchestrator extends UntypedActor {

    private final MediatorConfig config;
    private ResolveFacilityRequest originalRequest;
    private final Channels channels;

    public static class ResolveFacilityRequest extends SimpleMediatorRequest<FacilityRequestInput> {

        public ResolveFacilityRequest(ActorRef requestHandler, ActorRef respondTo, FacilityRequestInput facilityRequestInput) {
            super(requestHandler, respondTo, facilityRequestInput);
        }
    }

    public static class ResolveFacilityResponse extends SimpleMediatorResponse<OrganisationUnit> {

        public ResolveFacilityResponse(MediatorRequestMessage originalRequest, OrganisationUnit organisationUnit) {
            super(originalRequest, organisationUnit);
        }
    }

    LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    public FacilityOrchestrator(MediatorConfig config) {
        this.config = config;
        this.channels = ConfigUtility.getChannels(config);
    }

    private void sendFacilityRequest(ResolveFacilityRequest request) {
        String requestPath = "/api/organisationUnits";
        originalRequest = request;

        List<Pair<String, String>> params = new ArrayList<>();
        params.add(new ImmutablePair<>("filter", "code:eq:" + request.getRequestObject().getFacilityCode()));
        params.add(new ImmutablePair<>("fields", "id,code"));
        params.add(new ImmutablePair<>("paging", "false"));

        Map<String, String> headers = new LinkedHashMap<>();
        headers.put("Authorization", request.getRequestObject().getDhisAuthorization());
        headers.put("x-parent-openhim-transaction-id", request.getRequestObject().getParentOpenHIMTranId());

        MediatorHTTPRequest serviceRequest = new MediatorHTTPRequest(
                request.getRequestHandler(),
                getSelf(),
                "DHIS Service",
                "GET",
                channels.getDhisChannelScheme(),
                channels.getDhisChannelHost(),
                channels.getDhisChannelPort(),
                channels.getDhisChannelContextPath() + requestPath,
                null,
                headers,
                params
        );
        ActorSelection httpConnector = getContext().actorSelection(config.userPathFor("http-connector"));
        httpConnector.tell(serviceRequest, getSelf());

    }

    private void processFacilityServiceResponse(MediatorHTTPResponse response) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        OrganisationUnitWrapper dataSetWrapper = mapper.readValue(response.getBody(), OrganisationUnitWrapper.class);
        OrganisationUnit organisation = null;
        List<OrganisationUnit> organisations = dataSetWrapper.getOrganisationUnits();
        if (organisations != null && !organisations.isEmpty()) {
            organisation = organisations.get(0);
        }

        ResolveFacilityResponse resolveFacilityResponse = new ResolveFacilityResponse(originalRequest, organisation);
        originalRequest.getRespondTo().tell(resolveFacilityResponse, getSelf());

    }

    @Override
    public void onReceive(Object msg) throws Exception {
        if (msg instanceof ResolveFacilityRequest) {
            log.info("ResolveFacilityRequest");
            sendFacilityRequest((ResolveFacilityRequest) msg);
        } else if (msg instanceof MediatorHTTPResponse) {
            processFacilityServiceResponse((MediatorHTTPResponse) msg);
        } else {
            unhandled(msg);
        }
    }
}
