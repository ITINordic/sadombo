package zw.org.mohcc.sadombo.utils;

import java.io.IOException;
import java.io.InputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.apache.commons.io.IOUtils;
import org.openhim.mediator.engine.messages.MediatorHTTPRequest;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import zw.org.mohcc.sadombo.MediatorMain;
import static zw.org.mohcc.sadombo.utils.GeneralUtility.validateXml;

/**
 *
 * @author Charles Chigoriwa
 */
public class AdxUtility {

    public final static String ADX_CONTENT_TYPE = "application/adx+xml";

    public static boolean hasAdxContentType(MediatorHTTPRequest request) {
        String contentType = request.getHeaders().get("content-type");
        return contentType != null && contentType.trim().equalsIgnoreCase(ADX_CONTENT_TYPE);
    }

    public static boolean isConformingToBasicAdxXsd(String adxContent) {
        try {
            return isConformingToBasicAdxXsd(IOUtils.toInputStream(adxContent, "UTF-8"));
        } catch (IOException ex) {
            return false;
        }
    }

    public static boolean isConformingToBasicAdxXsd(InputStream adxDataInputStream) {
        try {
            InputStream basicAdxXsdInputStream = MediatorMain.class.getClassLoader().getResourceAsStream("basic_adx.xsd");
            validateXml(basicAdxXsdInputStream, adxDataInputStream);
            return true;
        } catch (IOException | SAXException ex) {
            return false;
        }
    }

    public static boolean isAdxResponseMessage(String msg) throws ParserConfigurationException, IOException, XPathExpressionException {
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.parse(IOUtils.toInputStream(msg));
            XPath xpath = XPathFactory.newInstance().newXPath();
            String pathResult = xpath.compile("/importSummary[1]").evaluate(doc);
            return pathResult != null && !pathResult.isEmpty();
        } catch (SAXException ex) {
            return false;
        }
    }

}
