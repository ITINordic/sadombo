package zw.org.mohcc.sadombo;

import java.io.IOException;
import java.io.InputStream;
import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import org.apache.commons.io.IOUtils;
import org.junit.Ignore;
import org.junit.Test;
import org.xml.sax.SAXException;

/**
 *
 * @author Charles Chigoriwa
 */
public class DataValidatorTest {

    @Ignore
    @Test
    public void test() throws SAXException, IOException {

        InputStream xsdInputStream = MediatorMain.class.getClassLoader().getResourceAsStream("zw/org/mohcc/sadombo/data/ATB_005.xsd");
        InputStream xmlInputStream = MediatorMain.class.getClassLoader().getResourceAsStream("zw/org/mohcc/sadombo/data/sample_data.xml");

        System.out.println(IOUtils.toString(xmlInputStream));

        SchemaFactory factory
                = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = factory.newSchema(new StreamSource(xsdInputStream));
        Validator validator = schema.newValidator();
        validator.validate(new StreamSource(xmlInputStream));

    }

}
