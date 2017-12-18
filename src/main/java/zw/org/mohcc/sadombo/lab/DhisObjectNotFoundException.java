package zw.org.mohcc.sadombo.lab;

/**
 *
 * @author Charles Chigoriwa
 */
public class DhisObjectNotFoundException extends RuntimeException {

    public DhisObjectNotFoundException() {
    }

    public DhisObjectNotFoundException(String message) {
        super(message);
    }

    public DhisObjectNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public DhisObjectNotFoundException(Throwable cause) {
        super(cause);
    }

    public DhisObjectNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
