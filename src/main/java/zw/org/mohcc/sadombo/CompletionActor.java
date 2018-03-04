package zw.org.mohcc.sadombo;

import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import org.openhim.mediator.engine.MediatorConfig;
import org.openhim.mediator.engine.messages.MediatorHTTPRequest;
import org.openhim.mediator.engine.messages.MediatorHTTPResponse;
import org.openhim.mediator.engine.messages.MediatorRequestMessage;
import org.openhim.mediator.engine.messages.SimpleMediatorRequest;
import org.openhim.mediator.engine.messages.SimpleMediatorResponse;
import zw.org.mohcc.sadombo.data.CompletionInput;
import zw.org.mohcc.sadombo.data.CompletionOutput;
import zw.org.mohcc.sadombo.utils.ConfigUtility;

/**
 *
 * @author Charles Chigoriwa
 */
public class CompletionActor extends UntypedActor {

    private final MediatorConfig config;
    private CompletionRequest originalRequest;
    private final Channels channels;

    public static class CompletionRequest extends SimpleMediatorRequest<CompletionInput> {

        public CompletionRequest(ActorRef requestHandler, ActorRef respondTo, CompletionInput completionInput) {
            super(requestHandler, respondTo, completionInput);
        }
    }

    public static class CompletionResponse extends SimpleMediatorResponse<CompletionOutput> {

        public CompletionResponse(MediatorRequestMessage originalRequest, CompletionOutput completionOutput) {
            super(originalRequest, completionOutput);
        }
    }

    LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    public CompletionActor(MediatorConfig config) {
        this.config = config;
        this.channels = ConfigUtility.getChannels(config);
    }

    private void sendCompletionRequest(CompletionRequest request) {

        String requestPath = "/api/completeDataSetRegistrations";
        originalRequest = request;

        CompletionInput completionInput = request.getRequestObject();
        String dataSetId = completionInput.getDataSetId();
        String period = completionInput.getPeriod();
        String organisationUnitId = completionInput.getFacilityId();

        String dhisAuthorization = completionInput.getDhisAuthorization();
        String parentOpenHIMTranId = completionInput.getParentOpenHIMTranId();

        String requestBody
                = "<completeDataSetRegistrations xmlns=\"http://dhis2.org/schema/dxf/2.0\">\n"
                + "  <completeDataSetRegistration dataSet=\"" + dataSetId + "\" period=\"" + period + "\"  organisationUnit=\"" + organisationUnitId + "\" />\n"
                + "</completeDataSetRegistrations>";

        Map<String, String> headers = new LinkedHashMap<>();
        headers.put("Authorization", dhisAuthorization);
        headers.put("x-parent-openhim-transaction-id", parentOpenHIMTranId);
        headers.put("Content-Type", "application/xml");

        MediatorHTTPRequest serviceRequest = new MediatorHTTPRequest(
                request.getRequestHandler(),
                getSelf(),
                "Complete DataSet Registration (DHIS)",
                "POST",
                channels.getDhisChannelScheme(),
                channels.getDhisChannelHost(),
                channels.getDhisChannelPort(),
                channels.getDhisChannelContextPath() + requestPath,
                requestBody,
                headers,
                null
        );

        ActorSelection httpConnector = getContext().actorSelection(config.userPathFor("http-connector"));
        httpConnector.tell(serviceRequest, getSelf());

    }

    private void processCompletionServiceResponse(MediatorHTTPResponse response) throws IOException {
        CompletionResponse completionResponse = new CompletionResponse(originalRequest, new CompletionOutput());
        originalRequest.getRespondTo().tell(completionResponse, getSelf());
    }

    @Override
    public void onReceive(Object msg) throws Exception {
        if (msg instanceof CompletionRequest) {
            CompletionRequest completionRequest = (CompletionRequest) msg;
            sendCompletionRequest(completionRequest);
        } else if (msg instanceof MediatorHTTPResponse) {
            processCompletionServiceResponse((MediatorHTTPResponse) msg);
        } else {
            unhandled(msg);
        }
    }
}
