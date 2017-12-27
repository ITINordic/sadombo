package zw.org.mohcc.sadombo.transformer;

import org.openhim.mediator.engine.messages.FinishRequest;
import org.openhim.mediator.engine.messages.MediatorHTTPRequest;
import org.openhim.mediator.engine.messages.MediatorHTTPResponse;

/**
 *
 * @author Charles Chigoriwa
 */
public abstract class ResponseTransformer {

    public abstract FinishRequest transform(MediatorHTTPResponse response, MediatorHTTPRequest request);
}
