package zw.org.mohcc.sadombo.enricher;

import java.io.InputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.util.UUID;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.commons.io.IOUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import zw.org.mohcc.sadombo.SadomboException;
import zw.org.mohcc.sadombo.data.util.SampleDataUtility;

/**
 *
 * @author Charles Chigoriwa
 */
public class DefaultADXDataEnricher {

    public static void main(String[] args) throws Exception {

        System.out.println(enrich(SampleDataUtility.getAXDSampleData(), UUID.randomUUID().toString(), true));

    }

    public static String enrich(String adxData, String transactionId, boolean indent) {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setValidating(false);
        try {
            InputStream inputStream = IOUtils.toInputStream(adxData, "UTF-8");
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(inputStream);
            Element element = (Element) doc.getElementsByTagName("group").item(0);
            element.setAttribute("comment", "{source: 'OpenHIM', transactionId: '" + transactionId + "'}");
            return documentToString(doc, indent);
        } catch (Exception exception) {
            throw new SadomboException(exception);
        }
    }

    private static String documentToString(Document xml, boolean indent) throws Exception {
        Transformer tf = TransformerFactory.newInstance().newTransformer();
        tf.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        if (indent) {
            tf.setOutputProperty(OutputKeys.INDENT, "yes");
        }
        Writer out = new StringWriter();
        tf.transform(new DOMSource(xml), new StreamResult(out));
        return out.toString();
    }

}
