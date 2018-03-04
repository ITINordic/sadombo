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
import zw.org.mohcc.sadombo.data.OrganisationUnit;
import zw.org.mohcc.sadombo.data.OrganisationUnitInput;
import zw.org.mohcc.sadombo.data.OrganisationUnitOutput;
import zw.org.mohcc.sadombo.data.OrganisationUnitsWrapper;
import zw.org.mohcc.sadombo.utils.ConfigUtility;

/**
 *
 * @author Charles Chigoriwa
 */
public class GetOrganisationUnitActor extends UntypedActor {

    private final MediatorConfig config;
    private ResolveOrganisationUnitRequest originalRequest;
    private final Channels channels;

    public static class ResolveOrganisationUnitRequest extends SimpleMediatorRequest<OrganisationUnitInput> {

        public ResolveOrganisationUnitRequest(ActorRef requestHandler, ActorRef respondTo, OrganisationUnitInput facilityRequestInput) {
            super(requestHandler, respondTo, facilityRequestInput);
        }
    }

    public static class ResolveOrganisationUnitResponse extends SimpleMediatorResponse<OrganisationUnitOutput> {

        public ResolveOrganisationUnitResponse(MediatorRequestMessage originalRequest, OrganisationUnitOutput organisationUnitOutput) {
            super(originalRequest, organisationUnitOutput);
        }
    }

    LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    public GetOrganisationUnitActor(MediatorConfig config) {
        this.config = config;
        this.channels = ConfigUtility.getChannels(config);
    }

    private void sendFacilityRequest(ResolveOrganisationUnitRequest request) {
        String requestPath = "/api/organisationUnits";
        originalRequest = request;

        OrganisationUnitInput facilityRequestInput = request.getRequestObject();
        String facilityCode = facilityRequestInput.getFacilityCode();
        String dhisAuthorization = facilityRequestInput.getDhisAuthorization();
        String parentOpenHIMTranId = facilityRequestInput.getParentOpenHIMTranId();

        List<Pair<String, String>> params = new ArrayList<>();
        params.add(new ImmutablePair<>("filter", "code:eq:" + facilityCode));
        params.add(new ImmutablePair<>("fields", "id,code,name"));
        params.add(new ImmutablePair<>("paging", "false"));

        Map<String, String> headers = new LinkedHashMap<>();
        headers.put("Authorization", dhisAuthorization);
        headers.put("x-parent-openhim-transaction-id", parentOpenHIMTranId);

        MediatorHTTPRequest serviceRequest = new MediatorHTTPRequest(
                request.getRequestHandler(),
                getSelf(),
                "Get Facility/OrganizationUnit (DHIS)",
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
        OrganisationUnitsWrapper dataSetWrapper = mapper.readValue(response.getBody(), OrganisationUnitsWrapper.class);
        OrganisationUnit organisationUnit = null;
        List<OrganisationUnit> organisationUnits = dataSetWrapper.getOrganisationUnits();
        if (organisationUnits != null && !organisationUnits.isEmpty()) {
            organisationUnit = organisationUnits.get(0);
        }
        ResolveOrganisationUnitResponse resolveOrganisationUnitResponse = new ResolveOrganisationUnitResponse(originalRequest, new OrganisationUnitOutput(organisationUnit));
        originalRequest.getRespondTo().tell(resolveOrganisationUnitResponse, getSelf());

    }

    @Override
    public void onReceive(Object msg) throws Exception {
        if (msg instanceof ResolveOrganisationUnitRequest) {
            sendFacilityRequest((ResolveOrganisationUnitRequest) msg);
        } else if (msg instanceof MediatorHTTPResponse) {
            processFacilityServiceResponse((MediatorHTTPResponse) msg);
        } else {
            unhandled(msg);
        }
    }
}
