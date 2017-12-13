package zw.org.mohcc.sadombo;

import akka.actor.ActorSelection;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.zim.company.util.GeneralUtility;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import org.openhim.mediator.engine.MediatorConfig;
import org.openhim.mediator.engine.messages.MediatorHTTPRequest;
import org.openhim.mediator.engine.messages.MediatorHTTPResponse;

public class DhisOrchestrator extends UntypedActor {

    LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    private final MediatorConfig config;
    private MediatorHTTPRequest originalRequest;
    private OpenhimChannels openhimChannels;

    public DhisOrchestrator(MediatorConfig config) throws IOException {
        this.config = config;
        this.openhimChannels = new OpenhimChannels();
        this.openhimChannels.setProperties("openhim-channels.properties");
    }

    @Override
    public void onReceive(Object msg) throws Exception {
        if (msg instanceof MediatorHTTPRequest) {
            queryDhisService((MediatorHTTPRequest) msg);
        } else if (msg instanceof MediatorHTTPResponse) {
            processDhisResponse((MediatorHTTPResponse) msg);
        } else {
            unhandled(msg);
        }
    }

    private void queryDhisService(MediatorHTTPRequest request) {
        log.info("Querying the DHIS service");
        originalRequest = request;

        ActorSelection httpConnector = getContext().actorSelection(config.userPathFor("http-connector"));
        Map<String, String> headers = new HashMap<>();
        //TODO: remove hard coding
        String authorization = Base64.getEncoder().encodeToString((openhimChannels.getDhisChannelUser() + ":" + openhimChannels.getDhisChannelPassword()).getBytes());
        headers.put("Authorization", "Basic " + authorization);

        log.info("Querying the DHIS service");

        log.info("request.getMethod()=" + request.getMethod());

        log.info("request.getPath()=" + request.getPath());

        log.info("GeneralUtility.getRequestPath(request.getPath())=" + GeneralUtility.getPath(request.getPath()));

        //TODO: Remove hard coding
        MediatorHTTPRequest serviceRequest = new MediatorHTTPRequest(
                request.getRequestHandler(),
                getSelf(),
                "DHIS Service",
                request.getMethod(),
                openhimChannels.getDhisChannelScheme(),
                openhimChannels.getDhisChannelHost(),
                openhimChannels.getDhisChannelPort(),
                openhimChannels.getDhisChannelContextPath() + DhisUrlMapper.getDhisPath(request.getPath()),
                null,
                headers,
                null
        );

        httpConnector.tell(serviceRequest, getSelf());
    }

    private void processDhisResponse(MediatorHTTPResponse response) {
        log.info("Received response from DHIS service");
        originalRequest.getRespondTo().tell(response.toFinishRequest(), getSelf());
    }

}
