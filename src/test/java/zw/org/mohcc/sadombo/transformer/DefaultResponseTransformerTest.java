package zw.org.mohcc.sadombo.transformer;

import java.util.Collections;
import org.apache.http.HttpStatus;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.openhim.mediator.engine.messages.FinishRequest;
import org.openhim.mediator.engine.messages.MediatorHTTPResponse;

/**
 *
 * @author Charles Chigoriwa
 */
public class DefaultResponseTransformerTest {

    @Test
    public void testTranform() {
        DefaultResponseTransformer defaultResponseTransformer = new DefaultResponseTransformer();
        MediatorHTTPResponse response = new MediatorHTTPResponse("Noted with thanks", HttpStatus.SC_OK, Collections.<String, String>singletonMap("Content-Type", "text/plain"));
        FinishRequest finishRequest = defaultResponseTransformer.transform(response, null);
        assertNotNull(finishRequest);
        assertTrue(finishRequest.getResponse().equals("Noted with thanks"));
        assertTrue(finishRequest.getResponseHeaders().get("Content-Type").equals("text/plain"));
        assertTrue(finishRequest.getResponseStatus() == HttpStatus.SC_OK);
    }

}
