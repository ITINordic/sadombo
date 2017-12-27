package zw.org.mohcc.sadombo.transformer;

import java.io.IOException;
import java.io.StringWriter;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.openhim.mediator.engine.messages.MediatorHTTPRequest;
import zw.org.mohcc.sadombo.SadomboException;
import zw.org.mohcc.sadombo.utils.GeneralUtility;

/**
 *
 * @author Charles Chigoriwa
 */
public class EnhancedRequestBodyTransformer extends RequestBodyTransformer {

    @Override
    public String transformRequestBody(MediatorHTTPRequest request) {
        if (GeneralUtility.hasAdxContentType(request) && !GeneralUtility.hasEmptyRequestBody(request)) {
            return transformAdxRequestBody(request);
        } else {
            return request.getBody();
        }
    }

    private String transformAdxRequestBody(MediatorHTTPRequest request) {
        Namespace namespace = Namespace.getNamespace("urn:ihe:qrph:adx:2015");
        String openHIMTransactionId = request.getHeaders().get("x-openhim-transactionid");
        try {
            Document doc = GeneralUtility.getJDom2Document(request.getBody());
            Element rootNode = doc.getRootElement();
            Element groupElement = rootNode.getChild("group", namespace);
            groupElement.setAttribute("comment", "{source: 'OpenHIM', transactionId: '" + openHIMTransactionId + "'}");
            StringWriter stringWriter = new StringWriter();
            XMLOutputter xmlOutput = new XMLOutputter();
            xmlOutput.setFormat(Format.getPrettyFormat());
            xmlOutput.output(doc, stringWriter);
            return stringWriter.toString();
        } catch (IOException | JDOMException exception) {
            throw new SadomboException(exception);
        }
    }

}
