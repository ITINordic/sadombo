package zw.org.mohcc.sadombo.transformer;

import javax.xml.parsers.DocumentBuilderFactory;
import org.openhim.mediator.engine.messages.MediatorHTTPRequest;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import zw.org.mohcc.sadombo.SadomboException;
import zw.org.mohcc.sadombo.utils.AdxUtility;
import zw.org.mohcc.sadombo.utils.GeneralUtility;
import static zw.org.mohcc.sadombo.utils.GeneralUtility.documentToString;

/**
 *
 * @author Charles Chigoriwa
 */
public class DefaultRequestBodyTransformer extends RequestBodyTransformer {

    @Override
    public String transformRequestBody(MediatorHTTPRequest request) {
        if (AdxUtility.hasAdxContentType(request) && !GeneralUtility.hasEmptyRequestBody(request)) {
            return transformAdxRequestBody(request);
        } else {
            return request.getBody();
        }
    }

    private String transformAdxRequestBody(MediatorHTTPRequest request) {
        String openHIMTransactionId = request.getHeaders().get("x-openhim-transactionid");
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setValidating(false);
        try {
            Document doc = GeneralUtility.getDomDocument(request.getBody(), false);
            Element element = (Element) doc.getElementsByTagName("group").item(0);
            element.setAttribute("comment", "{source: 'OpenHIM', transactionId: '" + openHIMTransactionId + "'}");
            return documentToString(doc);
        } catch (Exception exception) {
            throw new SadomboException(exception);
        }
    }

}
