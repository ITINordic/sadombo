package zw.org.mohcc.sadombo.transformer;

import akka.actor.ActorRef;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.tuple.Pair;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;
import org.openhim.mediator.engine.messages.MediatorHTTPRequest;
import zw.org.mohcc.sadombo.utils.GeneralUtility;
import static zw.org.mohcc.sadombo.utils.GeneralUtility.ADX_CONTENT_TYPE;
import zw.org.mohcc.sadombo.utils.SampleDataUtility;

/**
 *
 * @author Charles Chigoriwa
 */
public class EnhancedRequestBodyTransformerTest {

    @Test
    public void test() throws JDOMException, IOException {

        EnhancedRequestBodyTransformer requestBodyTransformer = new EnhancedRequestBodyTransformer();
        MediatorHTTPRequest request = getMediatorHTTPRequest(SampleDataUtility.getAdxSampleData(), ADX_CONTENT_TYPE);
        String enrichedAdxData = requestBodyTransformer.transformRequestBody(request);
        Namespace namespace = Namespace.getNamespace("urn:ihe:qrph:adx:2015");
        Document doc = GeneralUtility.getJDom2Document(enrichedAdxData);
        Element rootNode = doc.getRootElement();
        Element groupElement = rootNode.getChild("group", namespace);
        assertNotNull(groupElement.getAttribute("comment"));
        assertFalse(groupElement.getAttribute("comment").getValue().isEmpty());

    }

    private MediatorHTTPRequest getMediatorHTTPRequest(String content, String contentType) {
        String path = "/dhis-mediator/api/dataSets";
        String body = content;
        List<Pair<String, String>> params = null;
        ActorRef actorRef = null;
        Map<String, String> headers = new HashMap<>();
        headers.put("content-type", contentType);
        return new MediatorHTTPRequest(actorRef, actorRef, "my-orch", "get", "https", "localhost", 3000, path, body, headers, params);
    }

}
