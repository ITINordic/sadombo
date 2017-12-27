package zw.org.mohcc.sadombo;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Map;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpStatus;
import org.openhim.mediator.engine.messages.MediatorHTTPRequest;
import org.openhim.mediator.engine.testing.MockHTTPConnector;

/**
 *
 * @author Charles Chigoriwa
 */
public class DhisOrchestratorTestHelper {

    public static class MockDhisChannelHttpConnector extends MockHTTPConnector {

        String response;
        String expectedMessage;

        public MockDhisChannelHttpConnector() throws IOException {
            InputStream in = getClass().getClassLoader().getResourceAsStream("sample_adx_response.xml");
            response = IOUtils.toString(in);

            InputStream expected = getClass().getClassLoader().getResourceAsStream("sample_data.xml");
            expectedMessage = IOUtils.toString(expected);
        }

        @Override
        public String getResponse() {
            return response;
        }

        @Override
        public Integer getStatus() {
            return HttpStatus.SC_OK;
        }

        @Override
        public Map<String, String> getHeaders() {
            return Collections.emptyMap();
        }

        @Override
        public void executeOnReceive(MediatorHTTPRequest msg) {
            //Will add this when mock services are available
            // assertEquals("Expected adx message", trimXML(expectedMessage), trimXML(msg.getBody()));
        }
    }

}
