package zw.org.mohcc.sadombo;

import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.xml.sax.SAXException;

/**
 *
 * @author Charles Chigoriwa
 */
public class MiscellaneousTest {

    @Test
    public void testXsdDataLoad() throws SAXException, IOException {
        InputStream xsdInputStream = MediatorMain.class.getClassLoader().getResourceAsStream("zw/org/mohcc/sadombo/data/ATB_005.xsd");
        String xsdContent = IOUtils.toString(xsdInputStream);
        System.out.println(xsdContent);
        assertTrue(xsdContent != null && !xsdContent.isEmpty());
    }

    @Test
    public void testXmlDataLoad() throws SAXException, IOException {
        InputStream xmlInputStream = MediatorMain.class.getClassLoader().getResourceAsStream("zw/org/mohcc/sadombo/data/sample_data.xml");
        String xmlContent = IOUtils.toString(xmlInputStream);
        System.out.println(xmlContent);
        assertTrue(xmlContent != null && !xmlContent.isEmpty());

    }

}
