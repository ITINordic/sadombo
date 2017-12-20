package zw.org.mohcc.sadombo;

import akka.actor.ActorSelection;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.apache.http.HttpStatus;
import org.openhim.mediator.engine.MediatorConfig;
import org.openhim.mediator.engine.messages.FinishRequest;
import org.openhim.mediator.engine.messages.MediatorHTTPRequest;
import org.openhim.mediator.engine.messages.MediatorHTTPResponse;
import zw.org.mohcc.sadombo.data.util.GeneralUtility;
import static zw.org.mohcc.sadombo.data.util.GeneralUtility.getBasicAuthorization;
import zw.org.mohcc.sadombo.enricher.ADXDataEnricher;

public class DhisOrchestrator extends UntypedActor {

    LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    private final MediatorConfig config;
    private MediatorHTTPRequest originalRequest;
    private final Channels channels;

    public DhisOrchestrator(MediatorConfig config) throws IOException {
        this.config = config;
        this.channels = GeneralUtility.getChannels(config);
    }

    @Override
    public void onReceive(Object msg) throws Exception {
        if (msg instanceof MediatorHTTPRequest) {
            MediatorHTTPRequest request = (MediatorHTTPRequest) msg;
            if (GeneralUtility.isUserAllowed(request, config)) {
                queryDhisService((MediatorHTTPRequest) msg);
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

    private void queryDhisService(MediatorHTTPRequest request) {
        log.info("Querying the DHIS service");
        originalRequest = request;
        String openHIMTransactionId = request.getHeaders().get("x-openhim-transactionid");
        ActorSelection httpConnector = getContext().actorSelection(config.userPathFor("http-connector"));
        Map<String, String> headers = new HashMap<>();
        String basicAuthorization = getBasicAuthorization(channels.getDhisChannelUser(), channels.getDhisChannelPassword());
        headers.putAll(copyHeaders(request.getHeaders()));
        headers.put("Authorization", basicAuthorization);

        String requestBody = request.getBody();
        if (GeneralUtility.hasAdxContentType(request) && requestBody != null && !requestBody.trim().isEmpty()) {
            requestBody = ADXDataEnricher.enrich(requestBody, openHIMTransactionId, true);
        }

        MediatorHTTPRequest serviceRequest = new MediatorHTTPRequest(
                request.getRequestHandler(),
                getSelf(),
                "DHIS Service",
                request.getMethod(),
                channels.getDhisChannelScheme(),
                channels.getDhisChannelHost(),
                channels.getDhisChannelPort(),
                channels.getDhisChannelContextPath() + DhisUrlMapper.getDhisPath(request.getPath()),
                requestBody,
                headers,
                request.getParams()
        );

        httpConnector.tell(serviceRequest, getSelf());
    }

    private void processDhisResponse(MediatorHTTPResponse response) {
        log.info("Received response from DHIS service");
        log.info(response.getBody());
        originalRequest.getRespondTo().tell(response.toFinishRequest(), getSelf());
    }

    private Map<String, String> copyHeaders(Map<String, String> headers) {
        Map<String, String> copy = new HashMap<>();
        copy.put("content-type", headers.get("content-type"));
        copy.put("x-openhim-transactionid", headers.get("x-openhim-transactionid"));
        copy.put("x-forwarded-for", headers.get("x-forwarded-for"));
        copy.put("x-forwarded-host", headers.get("x-forwarded-host"));
        return copy;
    }

}
