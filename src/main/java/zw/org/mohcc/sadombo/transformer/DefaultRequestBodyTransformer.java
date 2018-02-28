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
package zw.org.mohcc.sadombo.transformer;

import javax.xml.parsers.DocumentBuilderFactory;
import org.openhim.mediator.engine.messages.MediatorHTTPRequest;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import zw.org.mohcc.sadombo.SadomboException;
import zw.org.mohcc.sadombo.utils.AdxUtility;
import zw.org.mohcc.sadombo.utils.GeneralUtility;
import static zw.org.mohcc.sadombo.utils.GeneralUtility.documentToString;

/**
 *
 * @author Charles Chigoriwa
 */
public class DefaultRequestBodyTransformer extends RequestBodyTransformer {

    @Override
    public String transformRequestBody(MediatorHTTPRequest request) {
        if (AdxUtility.hasAdxContentType(request) && !GeneralUtility.hasEmptyRequestBody(request)) {
            return transformAdxRequestBody(request);
        } else {
            return request.getBody();
        }
    }

    private String transformAdxRequestBody(MediatorHTTPRequest request) {
        String openHIMTransactionId = request.getHeaders().get("x-openhim-transactionid");
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setValidating(false);
        try {
            Document doc = GeneralUtility.getDomDocument(request.getBody(), false);
            Element element = (Element) doc.getElementsByTagName("group").item(0);
            element.setAttribute("comment", "{source: 'OpenHIM', transactionId: '" + openHIMTransactionId + "'}");
            return documentToString(doc);
        } catch (Exception exception) {
            throw new SadomboException(exception);
        }
    }

}
