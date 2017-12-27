package zw.org.mohcc.sadombo.validator;

import org.apache.http.HttpStatus;
import org.openhim.mediator.engine.messages.MediatorHTTPRequest;
import zw.org.mohcc.sadombo.utils.AdxUtility;
import static zw.org.mohcc.sadombo.utils.AdxUtility.isConformingToBasicAdxXsd;
import zw.org.mohcc.sadombo.utils.GeneralUtility;

/**
 *
 * @author Charles Chigoriwa
 */
public class DefaultRequestValidator extends RequestValidator {

    @Override
    public Validation validate(MediatorHTTPRequest request) {
        if (AdxUtility.hasAdxContentType(request)) {
            return validateAdxRequest(request);
        } else {
            return Validation.getValidationWithNoErrors();
        }
    }

    protected Validation validateAdxRequest(MediatorHTTPRequest request) {
        Validation validation = new Validation();
        String requestBody = request.getBody();
        String openHIMTransactionId = request.getHeaders().get("x-openhim-transactionid");
        boolean isDataValid = false;
        if (requestBody != null && !requestBody.trim().isEmpty()) {
            isDataValid = isConformingToBasicAdxXsd(requestBody);
        }

        if (!isDataValid) {
            validation.putErrorHeader("content-type", "application/xml");
            validation.setErrorMessage(GeneralUtility.getMessageForNonAdxContent(openHIMTransactionId));
            validation.setHttpStatus(HttpStatus.SC_UNPROCESSABLE_ENTITY);
        }

        validation.setValid(isDataValid);
        return validation;

    }

}
