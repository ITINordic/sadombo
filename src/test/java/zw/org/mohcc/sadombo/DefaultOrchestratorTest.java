/* 
 * Copyright (c) 2018, ITINordic
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
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
                //TODO: Mock this please
                testConfig.getDynamicConfig().put("sadomboBeanFactory", DefaultSadomboBeanFactory.getInstance(null, (String[]) null));
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
