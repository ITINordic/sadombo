package zw.org.mohcc.sadombo.transformer;

import org.openhim.mediator.engine.messages.MediatorHTTPRequest;

/**
 *
 * @author Charles Chigoriwa
 */
public abstract class RequestBodyTransformer {

    public abstract String transformRequestBody(MediatorHTTPRequest request);

}
