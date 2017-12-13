package com.zim.company.util;

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

}
