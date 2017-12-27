package zw.org.mohcc.sadombo.mapper;

import akka.actor.ActorRef;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.tuple.Pair;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.openhim.mediator.engine.messages.MediatorHTTPRequest;
import static zw.org.mohcc.sadombo.utils.GeneralUtility.ADX_CONTENT_TYPE;

/**
 *
 * @author Charles Chigoriwa
 */
public class DefaultRequestHeaderMapperTest {

    @Test
    public void test() {
        DefaultRequestHeaderMapper defaultRequestHeaderMapper = new DefaultRequestHeaderMapper();
        MediatorHTTPRequest request = getMediatorHTTPRequest(ADX_CONTENT_TYPE);
        Map<String, String> newHeaders = defaultRequestHeaderMapper.mapHeaders(request);
        String contentType = newHeaders.get("content-type");
        assertTrue(contentType != null && !contentType.trim().isEmpty());
    }

    private MediatorHTTPRequest getMediatorHTTPRequest(String contentType) {
        String path = "/dhis-mediator/api/dataSets";
        String body = "defaultBody";
        List<Pair<String, String>> params = null;
        ActorRef actorRef = null;
        Map<String, String> headers = new HashMap<>();
        headers.put("content-type", contentType);
        return new MediatorHTTPRequest(actorRef, actorRef, "my-orch", "get", "https", "localhost", 3000, path, body, headers, params);
    }

}
