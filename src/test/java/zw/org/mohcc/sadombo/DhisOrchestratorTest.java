package zw.org.mohcc.sadombo;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.JavaTestKit;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.http.HttpStatus;
import org.junit.*;
import static org.junit.Assert.*;
import org.openhim.mediator.engine.MediatorConfig;
import org.openhim.mediator.engine.messages.FinishRequest;
import org.openhim.mediator.engine.messages.MediatorHTTPRequest;
import org.openhim.mediator.engine.testing.MockLauncher;
import org.openhim.mediator.engine.testing.TestingUtils;
import scala.concurrent.duration.Duration;
import zw.org.mohcc.sadombo.DhisOrchestratorTestHelper.MockDhisChannelHttpConnector;
import static zw.org.mohcc.sadombo.utils.AdxUtility.isAdxResponseMessage;
import zw.org.mohcc.sadombo.utils.ConfigUtility;
import zw.org.mohcc.sadombo.utils.GeneralUtility;
import zw.org.mohcc.sadombo.utils.SampleDataUtility;

public class DhisOrchestratorTest {

    private static ActorSystem system;

    private MediatorConfig testConfig;

    @Before
    public void setUp() throws Exception {
        system = ActorSystem.create();
        testConfig = new MediatorConfig("sadombo", "localhost", 3000);
        //TODO: replace with mocks pleases
        testConfig.getDynamicConfig().put("sadomboBeanFactory", DefaultSadomboBeanFactory.getInstance(null, (String[]) null));

        List<MockLauncher.ActorToLaunch> toLaunch = new LinkedList<>();
        toLaunch.add(new MockLauncher.ActorToLaunch("http-connector", MockDhisChannelHttpConnector.class));
        TestingUtils.launchActors(system, testConfig.getName(), toLaunch);
    }

    @After
    public void tearDown() throws Exception {
        TestingUtils.clearRootContext(system, testConfig.getName());
        JavaTestKit.shutdownActorSystem(system);
        system = null;
    }

    @Test
    public void testMediatorWrongAdxHTTPRequest() throws Exception {
        new JavaTestKit(system) {
            {
                Channels channels = ConfigUtility.getChannels(testConfig);
                //TODO: Replace with mocking here please
                ((EnhancedChannels) channels).setSadomboAuthenticationEnabled(false);
                //Replace the above code with mocking please

                final ActorRef dhisOrchestratorActorRef = system.actorOf(Props.create(DhisOrchestrator.class, testConfig));

                MediatorHTTPRequest POST_Request = new MediatorHTTPRequest(
                        getRef(),
                        getRef(),
                        "unit-test",
                        "POST",
                        "http",
                        null,
                        null,
                        "/dhis-mediator/develop/api/dataValueSets",
                        "test message",
                        Collections.<String, String>singletonMap("content-type", GeneralUtility.ADX_CONTENT_TYPE),
                        Collections.<Pair<String, String>>emptyList()
                );

                dhisOrchestratorActorRef.tell(POST_Request, getRef());

                FinishRequest response = expectMsgClass(Duration.create(3, TimeUnit.SECONDS), FinishRequest.class);

                assertNotNull(response);
                assertTrue(response.getResponseStatus() == HttpStatus.SC_UNPROCESSABLE_ENTITY);

            }
        };
    }

    @Test
    public void testMediatorCorrectAdxHTTPRequest() throws Exception {
        new JavaTestKit(system) {
            {
                Channels channels = ConfigUtility.getChannels(testConfig);
                //TODO: Replace with mocking here please
                ((EnhancedChannels) channels).setSadomboAuthenticationEnabled(false);
                //Replace the above code with mocking please

                final ActorRef dhisOrchestratorActorRef = system.actorOf(Props.create(DhisOrchestrator.class, testConfig));

                MediatorHTTPRequest POST_Request = new MediatorHTTPRequest(
                        getRef(),
                        getRef(),
                        "unit-test",
                        "POST",
                        "http",
                        null,
                        null,
                        "/dhis-mediator/develop/api/dataValueSets",
                        SampleDataUtility.getAdxSampleData(),
                        Collections.<String, String>singletonMap("content-type", GeneralUtility.ADX_CONTENT_TYPE),
                        Collections.<Pair<String, String>>emptyList()
                );

                dhisOrchestratorActorRef.tell(POST_Request, getRef());

                FinishRequest response = expectMsgClass(Duration.create(3, TimeUnit.SECONDS), FinishRequest.class);
                assertNotNull(response);
                assertTrue(isAdxResponseMessage(response.getResponse()));
                assertTrue(response.getResponseStatus() == HttpStatus.SC_OK);

            }
        };
    }

}
