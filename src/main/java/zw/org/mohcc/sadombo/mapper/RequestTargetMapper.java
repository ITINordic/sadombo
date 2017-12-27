package zw.org.mohcc.sadombo.mapper;

import org.openhim.mediator.engine.messages.MediatorHTTPRequest;

/**
 *
 * @author Charles
 */
public abstract class RequestTargetMapper {

    public abstract RequestTarget getRequestTarget(MediatorHTTPRequest request);

}
