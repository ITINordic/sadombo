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
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.http.HttpStatus;
import org.junit.*;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.openhim.mediator.engine.MediatorConfig;
import org.openhim.mediator.engine.messages.FinishRequest;
import org.openhim.mediator.engine.messages.MediatorHTTPRequest;
import org.openhim.mediator.engine.messages.MediatorHTTPResponse;
import org.openhim.mediator.engine.testing.MockLauncher;
import org.openhim.mediator.engine.testing.TestingUtils;
import scala.concurrent.duration.Duration;
import zw.org.mohcc.sadombo.DhisOrchestratorTestHelper.MockDhisChannelHttpConnector;
import zw.org.mohcc.sadombo.mapper.RequestHeaderMapper;
import zw.org.mohcc.sadombo.mapper.RequestTarget;
import zw.org.mohcc.sadombo.mapper.RequestTargetMapper;
import zw.org.mohcc.sadombo.security.SecurityManager;
import zw.org.mohcc.sadombo.transformer.RequestBodyTransformer;
import zw.org.mohcc.sadombo.transformer.ResponseTransformer;
import zw.org.mohcc.sadombo.utils.AdxUtility;
import static zw.org.mohcc.sadombo.utils.AdxUtility.isAdxResponseMessage;
import zw.org.mohcc.sadombo.utils.ConfigUtility;
import zw.org.mohcc.sadombo.utils.SampleDataUtility;
import zw.org.mohcc.sadombo.validator.RequestValidator;
import zw.org.mohcc.sadombo.validator.Validation;

public class DhisOrchestratorTest {

    private static ActorSystem system;

    private MediatorConfig testConfig;

    @Before
    public void setUp() throws Exception {
        system = ActorSystem.create();
        testConfig = new MediatorConfig("sadombo", "localhost", 3000);
        //TODO: replace with mocks pleases
        SadomboBeanFactory sadomboBeanFactory = mock(SadomboBeanFactory.class);
        testConfig.getDynamicConfig().put("sadomboBeanFactory", sadomboBeanFactory);
        SecurityManager securityManager = mock(SecurityManager.class);
        RequestValidator requestValidator = mock(RequestValidator.class);
        RequestBodyTransformer requestBodyTransformer = mock(RequestBodyTransformer.class);
        RequestHeaderMapper requestHeaderMapper = mock(RequestHeaderMapper.class);
        RequestTargetMapper requestTargetMapper = mock(RequestTargetMapper.class);
        ResponseTransformer responseTransformer = mock(ResponseTransformer.class);
        Channels channels = mock(Channels.class);

        when(sadomboBeanFactory.getSecurityManager()).thenReturn(securityManager);
        when(sadomboBeanFactory.getRequestValidator()).thenReturn(requestValidator);
        when(sadomboBeanFactory.getRequestBodyTransformer()).thenReturn(requestBodyTransformer);
        when(sadomboBeanFactory.getRequestHeaderMapper()).thenReturn(requestHeaderMapper);
        when(sadomboBeanFactory.getRequestTargetMapper()).thenReturn(requestTargetMapper);
        when(sadomboBeanFactory.getResponseTransformer()).thenReturn(responseTransformer);
        when(sadomboBeanFactory.getChannels()).thenReturn(channels);

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
    public void testMediatorNoAuth() throws Exception {
        new JavaTestKit(system) {
            {
                //This is the mocked security manager we mocked recently
                SecurityManager securityManager = ConfigUtility.getSecurityManager(testConfig);

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
                        Collections.<String, String>singletonMap("content-type", AdxUtility.ADX_CONTENT_TYPE),
                        Collections.<Pair<String, String>>emptyList()
                );

                when(securityManager.isUserAllowed(POST_Request, testConfig)).thenReturn(false);
                dhisOrchestratorActorRef.tell(POST_Request, getRef());

                FinishRequest response = expectMsgClass(Duration.create(3, TimeUnit.SECONDS), FinishRequest.class);

                assertNotNull(response);
                assertTrue(response.getResponseStatus() == HttpStatus.SC_FORBIDDEN);

            }
        };
    }

    @Test
    public void testMediatorWrongAdxHTTPRequest() throws Exception {
        new JavaTestKit(system) {
            {
                //This is the mocked security manager we mocked recently
                SecurityManager securityManager = ConfigUtility.getSecurityManager(testConfig);
                RequestValidator requestValidator = ConfigUtility.getRequestValidator(testConfig);

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
                        Collections.<String, String>singletonMap("content-type", AdxUtility.ADX_CONTENT_TYPE),
                        Collections.<Pair<String, String>>emptyList()
                );

                when(securityManager.isUserAllowed(POST_Request, testConfig)).thenReturn(true);
                when(requestValidator.validate(POST_Request)).thenReturn(new Validation(false, "Unable to process", HttpStatus.SC_UNPROCESSABLE_ENTITY));

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
                //This is the mocked security manager we mocked recently
                SecurityManager securityManager = ConfigUtility.getSecurityManager(testConfig);
                RequestValidator requestValidator = ConfigUtility.getRequestValidator(testConfig);
                RequestBodyTransformer requestBodyTransformer = ConfigUtility.getRequestBodyTransformer(testConfig);
                RequestHeaderMapper requestHeaderMapper = ConfigUtility.getRequestHeaderMapper(testConfig);
                RequestTargetMapper requestTargetMapper = ConfigUtility.getRequestTargetMapper(testConfig);
                ResponseTransformer responseTransformer = ConfigUtility.getResponseTransformer(testConfig);

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
                        Collections.<String, String>singletonMap("content-type", AdxUtility.ADX_CONTENT_TYPE),
                        Collections.<Pair<String, String>>emptyList()
                );

                when(securityManager.isUserAllowed(POST_Request, testConfig)).thenReturn(true);
                when(requestValidator.validate(POST_Request)).thenReturn(Validation.getValidationWithNoErrors());
                when(requestBodyTransformer.transformRequestBody(POST_Request)).thenReturn(SampleDataUtility.getAdxSampleData());
                when(requestHeaderMapper.mapHeaders(POST_Request)).thenReturn(new HashMap<>());
                when(requestTargetMapper.getRequestTarget(POST_Request)).thenReturn(new RequestTarget("/develop/api/dataValueSets", null, "post"));
                when(responseTransformer.transform(any(MediatorHTTPResponse.class), eq(POST_Request))).thenReturn(new FinishRequest(SampleDataUtility.getAdxSampleResponse(), AdxUtility.ADX_CONTENT_TYPE, HttpStatus.SC_OK));

                dhisOrchestratorActorRef.tell(POST_Request, getRef());

                FinishRequest response = expectMsgClass(Duration.create(3, TimeUnit.SECONDS), FinishRequest.class);
                assertNotNull(response);
                assertTrue(isAdxResponseMessage(response.getResponse()));
                assertTrue(response.getResponseStatus() == HttpStatus.SC_OK);

            }
        };
    }

}
