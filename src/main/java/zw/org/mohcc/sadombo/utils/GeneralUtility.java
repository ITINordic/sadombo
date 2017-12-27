package zw.org.mohcc.sadombo.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.Base64;
import java.util.Properties;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.openhim.mediator.engine.messages.MediatorHTTPRequest;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 *
 * @author Charles Chigoriwa
 */
public class GeneralUtility {

    public static Properties loadProperties(String filePath) throws IOException {
        Properties props = new Properties();
        File file = new File(filePath);
        InputStream in = FileUtils.openInputStream(file);
        props.load(in);
        IOUtils.closeQuietly(in);
        return props;
    }

    public static String getParamValue(String[] args, String paramName) {
        String paramValue = null;
        if (args != null) {
            int argsLength = args.length;
            for (int i = 0; i < argsLength; i++) {
                if (args[i].equals(paramName) && i + 1 < argsLength) {
                    paramValue = args[i + 1];
                    break;
                }
            }
        }
        return paramValue;
    }

    public static Credentials getCredentials(String authorization) {
        // Authorization: Basic base64credentials
        Credentials credentials = new Credentials();
        String base64Credentials = authorization.substring("Basic".length()).trim();
        String strCredentials = new String(Base64.getDecoder().decode(base64Credentials), Charset.forName("UTF-8"));
        // credentials = username:password
        final String[] values = strCredentials.split(":", 2);
        credentials.setUsername(values[0]);
        credentials.setPassword(values[1]);
        return credentials;
    }

    public static Credentials getCredentials(MediatorHTTPRequest request) {
        String authorization = null;
        if (request.getHeaders() != null && !request.getHeaders().isEmpty()) {
            authorization = request.getHeaders().get("Authorization");
            authorization = authorization != null ? authorization : request.getHeaders().get("authorization");
        }
        if (authorization == null || authorization.trim().isEmpty()) {
            return null;
        } else {
            return getCredentials(authorization);
        }
    }

    public static boolean hasMethod(MediatorHTTPRequest request, String method) {
        return request.getMethod().equalsIgnoreCase(method);
    }

    public static boolean hasPostMethod(MediatorHTTPRequest request) {
        return hasMethod(request, "post");
    }

    public static boolean hasPutMethod(MediatorHTTPRequest request) {
        return hasMethod(request, "post");
    }

    public static boolean hasPostOrPutMethod(MediatorHTTPRequest request) {
        return hasPostMethod(request) || hasPutMethod(request);
    }

    public static String getBasicAuthorization(String username, String password) {
        return "Basic " + Base64.getEncoder().encodeToString((username + ":" + password).getBytes());
    }

    public static Document getDomDocument(String xmlContent, boolean validating) throws IOException, ParserConfigurationException, SAXException {
        InputStream inputStream = IOUtils.toInputStream(xmlContent, "UTF-8");
        return getDomDocument(inputStream, validating);
    }

    public static Document getDomDocument(InputStream inputStream, boolean validating) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setValidating(validating);
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document document = db.parse(inputStream);
        return document;
    }

    public static org.jdom2.Document getJDom2Document(String xmlContent) throws JDOMException, IOException {
        InputStream inputStream = IOUtils.toInputStream(xmlContent, "UTF-8");
        return getJDom2Document(inputStream);
    }

    public static org.jdom2.Document getJDom2Document(InputStream inputStream) throws JDOMException, IOException {
        SAXBuilder builder = new SAXBuilder();
        org.jdom2.Document doc = (org.jdom2.Document) builder.build(inputStream);
        return doc;
    }

    public static void validateXml(InputStream xsdInputStream, InputStream xmlInputStream) throws SAXException, IOException {

        SchemaFactory factory
                = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        factory.setResourceResolver(new ClasspathResourceResolver());
        Schema schema = factory.newSchema(new StreamSource(xsdInputStream));
        Validator validator = schema.newValidator();
        validator.validate(new StreamSource(xmlInputStream));
    }

    public static String documentToString(Document xml) throws Exception {
        Transformer tf = TransformerFactory.newInstance().newTransformer();
        tf.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        tf.setOutputProperty(OutputKeys.INDENT, "yes");
        Writer out = new StringWriter();
        tf.transform(new DOMSource(xml), new StreamResult(out));
        return out.toString();
    }

    public static boolean hasEmptyRequestBody(MediatorHTTPRequest request) {
        String requestBody = request.getBody();
        return requestBody == null || requestBody.trim().isEmpty();
    }

    public static String trimXML(String xml) {
        return xml.replace("\n", "").replaceAll(">\\s*<", "><");
    }

    public static String getMessageForNonAdxContent(String transactionId) {
        return "<?xml version='1.0' encoding='UTF-8'?>\n"
                + "<importSummary \n"
                + "  xmlns=\"http://dhis2.org/schema/dxf/2.0\" responseType=\"ImportSummary\" source=\"OpenHIM\" transactionId=\"" + transactionId + "\">\n"
                + " <status>FAILED_BASIC_ADX_VALIDATION</status>\n"
                + " <description>Failed Basic ADX xsd validation. Message not passed to the upstream server</description>\n"
                + "</importSummary>";
    }

}
