package zw.org.mohcc.sadombo.utils;

import java.io.InputStream;
import java.io.InputStreamReader;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;

/**
 *
 * @author Charles Chigoriwa
 *
 * Hint: https://dzone.com/articles/xml-validation
 */
public class ClasspathResourceResolver implements LSResourceResolver {

    @Override
    public LSInput resolveResource(String type, String namespaceURI, String publicId, String systemId, String baseURI) {

        LSInputImpl input = new LSInputImpl();

        //System.out.println("SystemId=" + systemId);
        InputStream stream = getClass().getClassLoader().getResourceAsStream(systemId);

        input.setPublicId(publicId);
        input.setSystemId(systemId);
        input.setBaseURI(baseURI);
        input.setCharacterStream(new InputStreamReader(stream));

        return input;
    }
}
