package zw.org.mohcc.sadombo;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.JavaTestKit;
import java.util.Collections;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.*;
import static org.junit.Assert.*;
import org.openhim.mediator.engine.MediatorConfig;
import org.openhim.mediator.engine.messages.FinishRequest;
import org.openhim.mediator.engine.messages.MediatorHTTPRequest;
import scala.concurrent.duration.Duration;

public class DefaultOrchestratorTest {

    static ActorSystem system;

    @BeforeClass
    public static void setup() {
        system = ActorSystem.create();
    }

    @AfterClass
    public static void teardown() {
        JavaTestKit.shutdownActorSystem(system);
        system = null;
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testMediatorHTTPRequest() throws Exception {
        new JavaTestKit(system) {
            {
                final MediatorConfig testConfig = new MediatorConfig("sadombo", "localhost", 3000);
                testConfig.getDynamicConfig().put("channels", new Channels());
                final ActorRef defaultOrchestrator = system.actorOf(Props.create(DefaultOrchestrator.class, testConfig));

                MediatorHTTPRequest POST_Request = new MediatorHTTPRequest(
                        getRef(),
                        getRef(),
                        "unit-test",
                        "POST",
                        "http",
                        null,
                        null,
                        "/mediator",
                        "test message",
                        Collections.<String, String>singletonMap("Content-Type", "text/plain"),
                        Collections.<Pair<String, String>>emptyList()
                );

                defaultOrchestrator.tell(POST_Request, getRef());

                final Object[] out
                        = new ReceiveWhile<Object>(Object.class, Duration.create(1, TimeUnit.SECONDS)) {
                            @Override
                            protected Object match(Object msg) throws Exception {
                                if (msg instanceof FinishRequest) {
                                    return msg;
                                }
                                throw noMatch();
                            }
                        }.get();

                boolean foundResponse = false;

                for (Object o : out) {
                    if (o instanceof FinishRequest) {
                        FinishRequest finishRequest = (FinishRequest) o;
                        System.out.println(finishRequest.getResponse());
                        foundResponse = true;
                    }
                }

                assertTrue("Must send FinishRequest", foundResponse);
            }
        };
    }
}
