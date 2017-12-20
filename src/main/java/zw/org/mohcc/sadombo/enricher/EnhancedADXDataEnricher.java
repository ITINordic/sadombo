package zw.org.mohcc.sadombo.enricher;

import java.io.IOException;
import java.io.StringWriter;
import java.util.UUID;
import org.apache.commons.io.IOUtils;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import zw.org.mohcc.sadombo.SadomboException;
import zw.org.mohcc.sadombo.data.util.SampleDataUtility;

/**
 *
 * @author Charles Chigoriwa
 */
public class EnhancedADXDataEnricher {

    public static void main(String[] args) throws Exception {

        System.out.println(enrich(SampleDataUtility.getAXDSampleData(), UUID.randomUUID().toString(), true));

    }

    public static String enrich(String adxData, String transactionId, boolean format) {
        Namespace namespace = Namespace.getNamespace("urn:ihe:qrph:adx:2015");
        try {
            SAXBuilder builder = new SAXBuilder();
            Document doc = (Document) builder.build(IOUtils.toInputStream(adxData, "UTF-8"));
            Element rootNode = doc.getRootElement();
            Element groupElement = rootNode.getChild("group", namespace);
            groupElement.setAttribute("comment", "{source: 'OpenHIM', transactionId: '" + transactionId + "'}");
            StringWriter stringWriter = new StringWriter();
            XMLOutputter xmlOutput = new XMLOutputter();
            if (format) {
                xmlOutput.setFormat(Format.getPrettyFormat());
            }
            xmlOutput.output(doc, stringWriter);
            return stringWriter.toString();
        } catch (IOException | JDOMException exception) {
            throw new SadomboException(exception);
        }
    }

}
