package zw.org.mohcc.sadombo;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import org.apache.http.HttpStatus;
import org.openhim.mediator.engine.MediatorConfig;
import org.openhim.mediator.engine.messages.FinishRequest;
import org.openhim.mediator.engine.messages.MediatorHTTPRequest;
import zw.org.mohcc.sadombo.utils.GeneralUtility;

public class DefaultOrchestrator extends UntypedActor {

    LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    private final MediatorConfig config;

    public DefaultOrchestrator(MediatorConfig config) {
        this.config = config;
    }

    @Override
    public void onReceive(Object msg) throws Exception {
        if (msg instanceof MediatorHTTPRequest) {
            MediatorHTTPRequest request = (MediatorHTTPRequest) msg;
            FinishRequest finishRequest;
            if (GeneralUtility.isUserAllowed(request, config)) {
                finishRequest = new FinishRequest("A message from my new mediator!", "text/plain", HttpStatus.SC_OK);
            } else {
                finishRequest = new FinishRequest("Not allowed", "text/plain", HttpStatus.SC_UNAUTHORIZED);
            }
            request.getRequestHandler().tell(finishRequest, getSelf());
        } else {
            unhandled(msg);
        }
    }
}
