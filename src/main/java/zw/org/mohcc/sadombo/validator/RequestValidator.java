package zw.org.mohcc.sadombo.validator;

import org.openhim.mediator.engine.messages.MediatorHTTPRequest;

/**
 *
 * @author Charles Chigoriwa
 */
public abstract class RequestValidator {

    public abstract Validation validate(MediatorHTTPRequest request);

}
