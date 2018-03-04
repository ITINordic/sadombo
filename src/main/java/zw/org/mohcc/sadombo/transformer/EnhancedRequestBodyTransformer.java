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

import java.io.IOException;
import java.io.StringWriter;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.openhim.mediator.engine.messages.MediatorHTTPRequest;
import zw.org.mohcc.sadombo.SadomboException;
import zw.org.mohcc.sadombo.utils.AdxUtility;
import zw.org.mohcc.sadombo.utils.GeneralUtility;

/**
 *
 * @author Charles Chigoriwa
 */
public class EnhancedRequestBodyTransformer extends RequestBodyTransformer {

    @Override
    public String transformRequestBody(MediatorHTTPRequest request) {
        if (AdxUtility.hasAdxContentType(request) && !GeneralUtility.hasEmptyRequestBody(request)) {
            return transformAdxRequestBody(request);
        } else {
            return request.getBody();
        }
    }

    private String transformAdxRequestBody(MediatorHTTPRequest request) {
        Namespace namespace = Namespace.getNamespace(AdxUtility.ADX_NAMESPACE);
        String openHIMTransactionId = request.getHeaders().get("x-openhim-transactionid");
        try {
            Document doc = GeneralUtility.getJDom2Document(request.getBody());
            Element rootNode = doc.getRootElement();
            Element groupElement = rootNode.getChild("group", namespace);
            groupElement.setAttribute("comment", "{source: 'OpenHIM', transactionId: '" + openHIMTransactionId + "'}");
            StringWriter stringWriter = new StringWriter();
            XMLOutputter xmlOutput = new XMLOutputter();
            xmlOutput.setFormat(Format.getPrettyFormat());
            xmlOutput.output(doc, stringWriter);
            return stringWriter.toString();
        } catch (IOException | JDOMException exception) {
            throw new SadomboException(exception);
        }
    }

}
