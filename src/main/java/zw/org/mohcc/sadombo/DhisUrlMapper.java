package zw.org.mohcc.sadombo;

import zw.org.mohcc.sadombo.data.util.GeneralUtility;

/**
 *
 * @author Charles
 */
public class DhisUrlMapper {

    public static String getDhisPath(String contextPath) {

        return GeneralUtility.getPath(contextPath);
    }

}
