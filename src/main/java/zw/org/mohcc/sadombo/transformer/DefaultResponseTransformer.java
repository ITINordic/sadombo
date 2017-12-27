package zw.org.mohcc.sadombo.transformer;

import org.openhim.mediator.engine.messages.FinishRequest;
import org.openhim.mediator.engine.messages.MediatorHTTPRequest;
import org.openhim.mediator.engine.messages.MediatorHTTPResponse;

/**
 *
 * @author Charles Chigoriwa
 */
public class DefaultResponseTransformer extends ResponseTransformer {

    @Override
    public FinishRequest transform(MediatorHTTPResponse response, MediatorHTTPRequest request) {
        return response.toFinishRequest();
    }

}
