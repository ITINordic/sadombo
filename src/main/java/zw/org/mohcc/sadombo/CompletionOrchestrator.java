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
    private ResolveCompletionRequest originalRequest;
    private final Channels channels;

    public static class ResolveCompletionRequest extends SimpleMediatorRequest<CompletionInput> {

        public ResolveCompletionRequest(ActorRef requestHandler, ActorRef respondTo, CompletionInput completionInput) {
            super(requestHandler, respondTo, completionInput);
        }
    }

    public static class ResolveCompletionResponse extends SimpleMediatorResponse<String> {

        public ResolveCompletionResponse(MediatorRequestMessage originalRequest, String id) {
            super(originalRequest, id);
        }
    }

    LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    public CompletionOrchestrator(MediatorConfig config) {
        this.config = config;
        this.channels = ConfigUtility.getChannels(config);
    }

    private void sendCompletionRequest(ResolveCompletionRequest request) {

        String requestPath = "/api/completeDataSetRegistrations";
        originalRequest = request;

        String period = request.getRequestObject().getPeriod().substring(0, 7).replaceAll("-", "");

        String requestBody = "<completeDataSetRegistrations \n"
                + "  xmlns=\"http://dhis2.org/schema/dxf/2.0\">\n"
                + "  <completeDataSetRegistration dataSet=\"" + request.getRequestObject().getDataSetId() + "\" period=\"" + period + "\"  organisationUnit=\"" + request.getRequestObject().getFacilityId() + "\"  storedBy=\"imported\" />\n"
                + "</completeDataSetRegistrations>";

        Map<String, String> headers = new LinkedHashMap<>();
        headers.put("Authorization", (String) config.getDynamicConfig().get("dhisAuthorization"));

        MediatorHTTPRequest serviceRequest = new MediatorHTTPRequest(
                request.getRequestHandler(),
                getSelf(),
                "DHIS Service",
                "POST",
                channels.getDhisChannelScheme(),
                channels.getDhisChannelHost(),
                channels.getDhisChannelPort(),
                channels.getDhisChannelContextPath() + requestPath,
                requestBody,
                headers,
                null
        );

        serviceRequest.getHeaders().put("Authorization", (String) config.getDynamicConfig().get("dhisAuthorization"));
        serviceRequest.getHeaders().put("Content-Type", "application/xml");

        ActorSelection httpConnector = getContext().actorSelection(config.userPathFor("http-connector"));
        httpConnector.tell(serviceRequest, getSelf());

    }

    private void processCompletionServiceResponse(MediatorHTTPResponse response) throws IOException {
        ResolveCompletionResponse resolveCompletionResponse = new ResolveCompletionResponse(originalRequest, "complete");
        originalRequest.getRespondTo().tell(resolveCompletionResponse, getSelf());

    }

    @Override
    public void onReceive(Object msg) throws Exception {
        if (msg instanceof ResolveCompletionRequest) {
            log.info("ResolveCompletionRequest");
            sendCompletionRequest((ResolveCompletionRequest) msg);
        } else if (msg instanceof MediatorHTTPResponse) {
            processCompletionServiceResponse((MediatorHTTPResponse) msg);
        } else {
            unhandled(msg);
        }
    }
}
