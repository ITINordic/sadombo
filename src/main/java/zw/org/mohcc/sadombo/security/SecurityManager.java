package zw.org.mohcc.sadombo.security;

import org.openhim.mediator.engine.MediatorConfig;
import org.openhim.mediator.engine.messages.MediatorHTTPRequest;

/**
 *
 * @author Charles Chigoriwa
 */
public abstract class SecurityManager {

    public abstract boolean isUserAllowed(MediatorHTTPRequest request, MediatorConfig config);

}
