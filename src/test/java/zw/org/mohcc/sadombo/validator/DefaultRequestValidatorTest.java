package zw.org.mohcc.sadombo.validator;

import akka.actor.ActorRef;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Assert;
import org.junit.Test;
import org.openhim.mediator.engine.messages.MediatorHTTPRequest;
import zw.org.mohcc.sadombo.MediatorMain;
import zw.org.mohcc.sadombo.utils.GeneralUtility;
import zw.org.mohcc.sadombo.utils.SampleDataUtility;

/**
 *
 * @author Charles Chigoriwa
 */
public class DefaultRequestValidatorTest {

    @Test
    public void emptyBody() {
        DefaultRequestValidator requestValidator = new DefaultRequestValidator();
        MediatorHTTPRequest request = getMediatorHTTPRequest("", GeneralUtility.ADX_CONTENT_TYPE);
        Assert.assertFalse(requestValidator.validate(request).isValid());
    }

    @Test
    public void validStringBody() {
        DefaultRequestValidator requestValidator = new DefaultRequestValidator();
        MediatorHTTPRequest request = getMediatorHTTPRequest(SampleDataUtility.getAdxSampleData(), GeneralUtility.ADX_CONTENT_TYPE);
        Assert.assertTrue(requestValidator.validate(request).isValid());
    }

    @Test
    public void validInputStream() throws IOException {
        DefaultRequestValidator requestValidator = new DefaultRequestValidator();
        InputStream xmlInputStream = MediatorMain.class.getClassLoader().getResourceAsStream("sample_data.xml");
        MediatorHTTPRequest request = getMediatorHTTPRequest(IOUtils.toString(xmlInputStream), GeneralUtility.ADX_CONTENT_TYPE);
        Assert.assertTrue(requestValidator.validate(request).isValid());
    }

    @Test
    public void nonAxdCompliantStringBody() {
        DefaultRequestValidator requestValidator = new DefaultRequestValidator();
        MediatorHTTPRequest request = getMediatorHTTPRequest(SampleDataUtility.getAdxSampleDataNonXsdCompliant(), GeneralUtility.ADX_CONTENT_TYPE);
        Assert.assertFalse(requestValidator.validate(request).isValid());
    }

    @Test
    public void nonXmlBody() {
        DefaultRequestValidator requestValidator = new DefaultRequestValidator();
        MediatorHTTPRequest request = getMediatorHTTPRequest("I would like to learn about Adx", GeneralUtility.ADX_CONTENT_TYPE);
        Assert.assertFalse(requestValidator.validate(request).isValid());
    }

    @Test
    public void nonAxdCompliantInputStream() throws IOException {
        DefaultRequestValidator requestValidator = new DefaultRequestValidator();
        InputStream xmlInputStream = MediatorMain.class.getClassLoader().getResourceAsStream("sample_data_non_compliant.xml");
        MediatorHTTPRequest request = getMediatorHTTPRequest(IOUtils.toString(xmlInputStream), GeneralUtility.ADX_CONTENT_TYPE);
        Assert.assertFalse(requestValidator.validate(request).isValid());
    }

    private MediatorHTTPRequest getMediatorHTTPRequest(String content, String contentType) {
        String path = "/dhis-mediator/api/dataSets";
        String body = content;
        List<Pair<String, String>> params = null;
        ActorRef actorRef = null;
        Map<String, String> headers = new HashMap<>();
        headers.put("content-type", contentType);
        return new MediatorHTTPRequest(actorRef, actorRef, "my-orch", "get", "https", "localhost", 3000, path, body, headers, params);
    }

}
