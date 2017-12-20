package zw.org.mohcc.sadombo;

/**
 *
 * @author Charles Chigoriwa
 */
public class SadomboException extends RuntimeException {

    public SadomboException() {
    }

    public SadomboException(String message) {
        super(message);
    }

    public SadomboException(String message, Throwable cause) {
        super(message, cause);
    }

    public SadomboException(Throwable cause) {
        super(cause);
    }

    public SadomboException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
