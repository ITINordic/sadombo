package zw.org.mohcc.sadombo.security;

import org.openhim.mediator.engine.MediatorConfig;
import org.openhim.mediator.engine.messages.MediatorHTTPRequest;
import zw.org.mohcc.sadombo.Channels;
import static zw.org.mohcc.sadombo.utils.ConfigUtility.getChannels;
import zw.org.mohcc.sadombo.utils.Credentials;
import static zw.org.mohcc.sadombo.utils.GeneralUtility.getCredentials;

/**
 *
 * @author Charles Chigoriwa
 */
public class DefaultSecurityManager extends SecurityManager {

    @Override
    public boolean isUserAllowed(MediatorHTTPRequest request, MediatorConfig config) {
        Channels channels = getChannels(config);
        if (!channels.isSadomboAuthenticationEnabled()) {
            return true;
        } else {
            Credentials sadomboCredentials = channels.getSadomboCredentials();
            Credentials userCredentials = getCredentials(request);
            return userCredentials != null && userCredentials.equals(sadomboCredentials);
        }
    }

}
