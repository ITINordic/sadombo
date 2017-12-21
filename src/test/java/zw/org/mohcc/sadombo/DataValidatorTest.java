package zw.org.mohcc.sadombo;

import java.io.IOException;
import java.io.InputStream;
import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import org.junit.Test;
import org.xml.sax.SAXException;
import zw.org.mohcc.sadombo.utils.ClasspathResourceResolver;

/**
 *
 * @author Charles Chigoriwa
 */
public class DataValidatorTest {

    @Test
    public void testWithBasicAxdXsd() throws SAXException, IOException {

        InputStream xsdInputStream = MediatorMain.class.getClassLoader().getResourceAsStream("basic_adx.xsd");
        InputStream xmlInputStream = MediatorMain.class.getClassLoader().getResourceAsStream("sample_data.xml");

        SchemaFactory factory
                = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        factory.setResourceResolver(new ClasspathResourceResolver());
        Schema schema = factory.newSchema(new StreamSource(xsdInputStream));
        Validator validator = schema.newValidator();
        validator.validate(new StreamSource(xmlInputStream));

    }

    @Test
    public void testWithFullAxdXsd() throws SAXException, IOException {

        InputStream xsdInputStream = MediatorMain.class.getClassLoader().getResourceAsStream("ATB_005.xsd");
        InputStream xmlInputStream = MediatorMain.class.getClassLoader().getResourceAsStream("sample_data.xml");

        SchemaFactory factory
                = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        factory.setResourceResolver(new ClasspathResourceResolver());
        Schema schema = factory.newSchema(new StreamSource(xsdInputStream));
        Validator validator = schema.newValidator();
        validator.validate(new StreamSource(xmlInputStream));

    }

}
