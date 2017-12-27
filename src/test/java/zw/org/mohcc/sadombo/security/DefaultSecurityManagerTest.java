package zw.org.mohcc.sadombo.security;

import akka.actor.ActorRef;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.After;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.openhim.mediator.engine.MediatorConfig;
import org.openhim.mediator.engine.messages.MediatorHTTPRequest;
import zw.org.mohcc.sadombo.Channels;
import zw.org.mohcc.sadombo.SadomboBeanFactory;
import zw.org.mohcc.sadombo.utils.ConfigUtility;
import zw.org.mohcc.sadombo.utils.Credentials;
import zw.org.mohcc.sadombo.utils.GeneralUtility;

/**
 *
 * @author Charles Chigoriwa
 */
public class DefaultSecurityManagerTest {

    private MediatorConfig testConfig;
    private DefaultSecurityManager defaultSecurityManager;

    @Before
    public void setUp() throws Exception {
        testConfig = new MediatorConfig("sadombo", "localhost", 3000);
        SadomboBeanFactory sadomboBeanFactory = mock(SadomboBeanFactory.class);
        testConfig.getDynamicConfig().put("sadomboBeanFactory", sadomboBeanFactory);
        Channels channels = mock(Channels.class);
        when(sadomboBeanFactory.getChannels()).thenReturn(channels);
        defaultSecurityManager = new DefaultSecurityManager();
    }

    @After
    public void tearDown() throws Exception {
        testConfig = null;
        defaultSecurityManager = null;
    }

    @Test
    public void test() {
        MediatorHTTPRequest request = getMediatorHTTPRequest(null, null);
        Channels channels = ConfigUtility.getChannels(testConfig);
        when(channels.isSadomboAuthenticationEnabled()).thenReturn(false);
        assertTrue(defaultSecurityManager.isUserAllowed(request, testConfig));
    }

    @Test
    public void testWithAuthEnabledButNoAuthorization() {
        Channels channels = ConfigUtility.getChannels(testConfig);
        when(channels.isSadomboAuthenticationEnabled()).thenReturn(true);
        MediatorHTTPRequest request = getMediatorHTTPRequest(null, null);
        assertFalse(defaultSecurityManager.isUserAllowed(request, testConfig));
    }

    @Test
    public void testWithCorrectCredentials() {
        Channels channels = ConfigUtility.getChannels(testConfig);
        when(channels.isSadomboAuthenticationEnabled()).thenReturn(true);
        when(channels.getSadomboCredentials()).thenReturn(new Credentials("user", "password"));
        MediatorHTTPRequest request = getMediatorHTTPRequest("user", "password");
        assertTrue(defaultSecurityManager.isUserAllowed(request, testConfig));
    }

    @Test
    public void testWithWrongCredentials() {
        Channels channels = ConfigUtility.getChannels(testConfig);
        when(channels.isSadomboAuthenticationEnabled()).thenReturn(true);
        when(channels.getSadomboCredentials()).thenReturn(new Credentials("user", "password"));
        MediatorHTTPRequest request = getMediatorHTTPRequest("user1", "password");
        assertFalse(defaultSecurityManager.isUserAllowed(request, testConfig));
    }

    private MediatorHTTPRequest getMediatorHTTPRequest(String username, String password) {
        String path = "/dhis-mediator/api/dataSets";
        String body = null;
        List<Pair<String, String>> params = null;
        ActorRef actorRef = null;
        Map<String, String> headers = null;
        if (username != null && password != null) {
            headers = new HashMap<>();
            headers.put("Authorization", GeneralUtility.getBasicAuthorization(username, password));
        }

        return new MediatorHTTPRequest(actorRef, actorRef, "my-orch", "get", "https", "localhost", 3000, path, body, headers, params);
    }

}
