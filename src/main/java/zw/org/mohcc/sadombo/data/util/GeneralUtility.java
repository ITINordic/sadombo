package zw.org.mohcc.sadombo.data.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author Charles Chigoriwa
 */
public class GeneralUtility {

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

}
