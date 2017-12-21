package zw.org.mohcc.sadombo;

import java.io.IOException;
import java.io.InputStream;
import org.junit.Test;
import org.xml.sax.SAXException;
import zw.org.mohcc.sadombo.validator.AdxXsdValidator;

/**
 *
 * @author Charles Chigoriwa
 */
public class DataValidatorTest {

    @Test
    public void testWithBasicAxdXsd() throws SAXException, IOException {
        InputStream xsdInputStream = MediatorMain.class.getClassLoader().getResourceAsStream("basic_adx.xsd");
        InputStream xmlInputStream = MediatorMain.class.getClassLoader().getResourceAsStream("sample_data.xml");
        AdxXsdValidator.validateXml(xsdInputStream, xmlInputStream);
    }

    @Test
    public void testWithFullAxdXsd() throws SAXException, IOException {
        InputStream xsdInputStream = MediatorMain.class.getClassLoader().getResourceAsStream("ATB_005.xsd");
        InputStream xmlInputStream = MediatorMain.class.getClassLoader().getResourceAsStream("sample_data.xml");
        AdxXsdValidator.validateXml(xsdInputStream, xmlInputStream);
    }

}
