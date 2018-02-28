/* 
 * Copyright (c) 2018, ITINordic
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
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
