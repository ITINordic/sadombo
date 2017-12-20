package zw.org.mohcc.sadombo.data.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Base64;
import java.util.Properties;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.openhim.mediator.engine.MediatorConfig;
import org.openhim.mediator.engine.messages.MediatorHTTPRequest;
import zw.org.mohcc.sadombo.Channels;

/**
 *
 * @author Charles Chigoriwa
 */
public class GeneralUtility {

    public final static String ADX_CONTENT_TYPE = "application/adx+xml";

    public static String getPath(String contextPath) {
        String requestPath = "/";
        int firstIndexOf = contextPath.indexOf("/");
        if (firstIndexOf >= 0 && contextPath.length() >= firstIndexOf + 2) {
            int secondIndexOf = contextPath.indexOf("/", firstIndexOf + 1);
            if (secondIndexOf > firstIndexOf) {
                requestPath = contextPath.substring(secondIndexOf);
            }
        }
        return requestPath;
    }

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
        int argsLength = args.length;
        for (int i = 0; i < argsLength; i++) {
            if (args[i].equals(paramName) && i + 1 < argsLength) {
                paramValue = args[i + 1];
                break;
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
        String authorization = request.getHeaders().get("Authorization");
        authorization = authorization != null ? authorization : request.getHeaders().get("authorization");
        if (authorization == null || authorization.trim().isEmpty()) {
            return null;
        } else {
            return getCredentials(authorization);
        }
    }

    public static Channels getChannels(MediatorConfig config) {
        return (Channels) config.getDynamicConfig().get("channels");
    }

    public static boolean isUserAllowed(MediatorHTTPRequest request, MediatorConfig config) {
        Channels channels = getChannels(config);
        if (!channels.isSadomboAuthenticationEnabled()) {
            return true;
        } else {
            Credentials sadomboCredentials = channels.getSadomboCredentials();
            Credentials userCredentials = getCredentials(request);
            return userCredentials != null && userCredentials.equals(sadomboCredentials);
        }
    }

    public static boolean hasAdxContentType(MediatorHTTPRequest request) {
        String contentType = request.getHeaders().get("content-type");
        return contentType != null && contentType.trim().equalsIgnoreCase(ADX_CONTENT_TYPE);
    }

    public static String getBasicAuthorization(String username, String password) {
        return "Basic " + Base64.getEncoder().encodeToString((username + ":" + password).getBytes());
    }

}
