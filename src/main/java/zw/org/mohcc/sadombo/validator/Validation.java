package zw.org.mohcc.sadombo.validator;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Charles Chigoriwa
 */
public class Validation {

    private boolean valid;
    private String errorMessage;
    private Map<String, String> errorHeaders = new HashMap<>();
    private Integer httpStatus;

    public Validation() {
    }

    public Validation(boolean valid, String errorMessage, Integer httpStatus) {
        this.valid = valid;
        this.errorMessage = errorMessage;
        this.httpStatus = httpStatus;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Map<String, String> getErrorHeaders() {
        return errorHeaders;
    }

    public void setErrorHeaders(Map<String, String> errorHeaders) {
        this.errorHeaders = errorHeaders;
    }

    public Integer getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(Integer httpStatus) {
        this.httpStatus = httpStatus;
    }

    public void putErrorHeader(String key, String value) {
        this.errorHeaders.put(key, value);
    }

    public static Validation getValidationWithNoErrors() {
        Validation validation = new Validation();
        validation.setValid(true);
        return validation;
    }

}
