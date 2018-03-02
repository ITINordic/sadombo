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
import zw.org.mohcc.sadombo.utils.ConfigUtility;

/**
 *
 * @author Charles Chigoriwa
 */
public class CompletionOrchestrator extends UntypedActor {

    private final MediatorConfig config;
    private CompletionRequest originalRequest;
    private final Channels channels;

    public static class CompletionRequest extends SimpleMediatorRequest<CompletionInput> {

        public CompletionRequest(ActorRef requestHandler, ActorRef respondTo, CompletionInput completionInput) {
            super(requestHandler, respondTo, completionInput);
        }
    }

    public static class CompletionResponse extends SimpleMediatorResponse<String> {

        public CompletionResponse(MediatorRequestMessage originalRequest, String id) {
            super(originalRequest, id);
        }
    }

    LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    public CompletionOrchestrator(MediatorConfig config) {
        this.config = config;
        this.channels = ConfigUtility.getChannels(config);
    }

    private void sendCompletionRequest(CompletionRequest request) {

        String requestPath = "/api/completeDataSetRegistrations";
        originalRequest = request;
        String requestBody
                = "<completeDataSetRegistrations xmlns=\"http://dhis2.org/schema/dxf/2.0\">\n"
                + "  <completeDataSetRegistration dataSet=\"" + request.getRequestObject().getDataSetId() + "\" period=\"" + request.getRequestObject().getPeriod() + "\"  organisationUnit=\"" + request.getRequestObject().getFacilityId() + "\" />\n"
                + "</completeDataSetRegistrations>";

        Map<String, String> headers = new LinkedHashMap<>();
        headers.put("Authorization", request.getRequestObject().getDhisAuthorization());
        headers.put("x-parent-openhim-transaction-id", request.getRequestObject().getParentOpenHIMTranId());
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
        CompletionResponse completionResponse = new CompletionResponse(originalRequest, "complete");
        originalRequest.getRespondTo().tell(completionResponse, getSelf());
    }

    @Override
    public void onReceive(Object msg) throws Exception {
        if (msg instanceof CompletionRequest) {
            log.info("ResolveCompletionRequest");
            sendCompletionRequest((CompletionRequest) msg);
        } else if (msg instanceof MediatorHTTPResponse) {
            processCompletionServiceResponse((MediatorHTTPResponse) msg);
        } else {
            unhandled(msg);
        }
    }
}
