package zw.org.mohcc.sadombo.validator;

import java.io.IOException;
import java.io.InputStream;
import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import org.apache.commons.io.IOUtils;
import org.xml.sax.SAXException;
import zw.org.mohcc.sadombo.MediatorMain;
import zw.org.mohcc.sadombo.utils.ClasspathResourceResolver;

/**
 *
 * @author daniel
 */
public class AdxXsdValidator {

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

    public static void validateXml(InputStream xsdInputStream, InputStream xmlInputStream) throws SAXException, IOException {

        SchemaFactory factory
                = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        factory.setResourceResolver(new ClasspathResourceResolver());
        Schema schema = factory.newSchema(new StreamSource(xsdInputStream));
        Validator validator = schema.newValidator();
        validator.validate(new StreamSource(xmlInputStream));
    }

}
