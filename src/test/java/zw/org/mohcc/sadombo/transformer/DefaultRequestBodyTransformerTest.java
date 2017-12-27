package zw.org.mohcc.sadombo.transformer;

import akka.actor.ActorRef;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.lang3.tuple.Pair;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;
import org.openhim.mediator.engine.messages.MediatorHTTPRequest;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import zw.org.mohcc.sadombo.utils.GeneralUtility;
import static zw.org.mohcc.sadombo.utils.GeneralUtility.ADX_CONTENT_TYPE;
import zw.org.mohcc.sadombo.utils.SampleDataUtility;

/**
 *
 * @author Charles Chigoriwa
 */
public class DefaultRequestBodyTransformerTest {

    @Test
    public void test() throws IOException, ParserConfigurationException, SAXException {
        DefaultRequestBodyTransformer requestBodyTransformer = new DefaultRequestBodyTransformer();
        MediatorHTTPRequest request = getMediatorHTTPRequest(SampleDataUtility.getAdxSampleData(), ADX_CONTENT_TYPE);
        String enrichedAdxData = requestBodyTransformer.transformRequestBody(request);
        Document doc = GeneralUtility.getDomDocument(enrichedAdxData, false);
        Element element = (Element) doc.getElementsByTagName("group").item(0);
        assertNotNull(element.getAttribute("comment"));
        assertFalse(element.getAttribute("comment").isEmpty());
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
