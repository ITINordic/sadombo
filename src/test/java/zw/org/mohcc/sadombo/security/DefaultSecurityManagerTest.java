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
import org.openhim.mediator.engine.MediatorConfig;
import org.openhim.mediator.engine.messages.MediatorHTTPRequest;
import zw.org.mohcc.sadombo.Channels;
import zw.org.mohcc.sadombo.DefaultSadomboBeanFactory;
import zw.org.mohcc.sadombo.EnhancedChannels;
import zw.org.mohcc.sadombo.utils.ConfigUtility;
import zw.org.mohcc.sadombo.utils.GeneralUtility;

/**
 *
 * @author Charles Chigoriwa
 */
public class DefaultSecurityManagerTest {

    private MediatorConfig testConfig;
    private DefaultSecurityManager mediatorSecurityService;

    @Before
    public void setUp() throws Exception {
        testConfig = new MediatorConfig("sadombo", "localhost", 3000);
        //TODO: replace with mocks pleases
        testConfig.getDynamicConfig().put("sadomboBeanFactory", DefaultSadomboBeanFactory.getInstance(null, (String[]) null));
        mediatorSecurityService = new DefaultSecurityManager();
    }

    @After
    public void tearDown() throws Exception {
        testConfig = null;
        mediatorSecurityService = null;
    }

    @Test
    public void test() {
        MediatorHTTPRequest request = getMediatorHTTPRequest(null, null);
        Channels channels = ConfigUtility.getChannels(testConfig);
        //TODO: Replace with mocking here please
        ((EnhancedChannels) channels).setSadomboAuthenticationEnabled(false);
        //Replace the above code with mocking please
        assertTrue(mediatorSecurityService.isUserAllowed(request, testConfig));
    }

    @Test
    public void testWithAuthEnabledButNoAuthorization() {
        Channels channels = ConfigUtility.getChannels(testConfig);
        //TODO: Replace with mocking here please :: Mock both SadomboBeanFactory and Channels
        ((EnhancedChannels) channels).setSadomboAuthenticationEnabled(true);
        //Replace the above code with mocking please

        MediatorHTTPRequest request = getMediatorHTTPRequest(null, null);
        assertFalse(mediatorSecurityService.isUserAllowed(request, testConfig));
    }

    @Test
    public void testWithCorrectCredentials() {

        Channels channels = ConfigUtility.getChannels(testConfig);
        //TODO: Replace with mocking here please
        ((EnhancedChannels) channels).setSadomboAuthenticationEnabled(true);
        ((EnhancedChannels) channels).getSadomboCredentials().setUsername("user");
        ((EnhancedChannels) channels).getSadomboCredentials().setPassword("password");
        //Replace the above code with mocking please
        MediatorHTTPRequest request = getMediatorHTTPRequest("user", "password");
        assertTrue(mediatorSecurityService.isUserAllowed(request, testConfig));
    }

    @Test
    public void testWithWrongCredentials() {
        Channels channels = ConfigUtility.getChannels(testConfig);
        //TODO: Replace with mocking here please
        ((EnhancedChannels) channels).setSadomboAuthenticationEnabled(true);
        ((EnhancedChannels) channels).getSadomboCredentials().setUsername("user");
        ((EnhancedChannels) channels).getSadomboCredentials().setPassword("password");
        //Replace the above code with mocking please
        MediatorHTTPRequest request = getMediatorHTTPRequest("user1", "password");
        assertFalse(mediatorSecurityService.isUserAllowed(request, testConfig));
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
