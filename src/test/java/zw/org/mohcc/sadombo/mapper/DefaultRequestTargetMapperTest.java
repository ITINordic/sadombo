package zw.org.mohcc.sadombo.mapper;

import akka.actor.ActorRef;
import java.util.List;
import org.apache.commons.lang3.tuple.Pair;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.openhim.mediator.engine.messages.MediatorHTTPRequest;

/**
 *
 * @author Charles Chigoriwa
 */
public class DefaultRequestTargetMapperTest {

    @Test
    public void test() {
        DefaultRequestTargetMapper defaultRequestUrlMapper = new DefaultRequestTargetMapper();
        String path = "/dhis-mediator/api/dataSets";
        List<Pair<String, String>> params = null;
        ActorRef actorRef = null;
        MediatorHTTPRequest request = new MediatorHTTPRequest(actorRef, actorRef, "my-orch", "get", "https", "localhost", 3000, path, null, null, params);
        RequestTarget requestUrl = defaultRequestUrlMapper.getRequestTarget(request);
        assertNotNull(requestUrl);
        assertTrue(requestUrl.getRelativePath().equals("/api/dataSets"));
        assertNull(requestUrl.getParams());
    }

}
