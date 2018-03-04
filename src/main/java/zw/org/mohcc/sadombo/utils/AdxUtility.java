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
package zw.org.mohcc.sadombo.utils;

import java.io.IOException;
import java.io.InputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.apache.commons.io.IOUtils;
import org.openhim.mediator.engine.messages.MediatorHTTPRequest;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import zw.org.mohcc.sadombo.MediatorMain;
import static zw.org.mohcc.sadombo.utils.GeneralUtility.validateXml;

/**
 *
 * @author Charles Chigoriwa
 */
public class AdxUtility {

    public final static String ADX_CONTENT_TYPE = "application/adx+xml";
    public final static String ADX_NAMESPACE = "urn:ihe:qrph:adx:2015";

    public static boolean hasAdxContentType(MediatorHTTPRequest request) {
        String contentType = request.getHeaders().get("content-type");
        return contentType != null && contentType.trim().equalsIgnoreCase(ADX_CONTENT_TYPE);
    }

    public static boolean isConformingToBasicAdxXsd(String adxContent) {
        try {
            return isConformingToBasicAdxXsd(IOUtils.toInputStream(adxContent, "UTF-8"));
        } catch (IOException ex) {
            return false;
        }
    }

    public static boolean isConformingToBasicAdxXsd(InputStream adxDataInputStream) {
        try {
            InputStream basicAdxXsdInputStream = MediatorMain.class.getClassLoader().getResourceAsStream("basic_adx.xsd");
            validateXml(basicAdxXsdInputStream, adxDataInputStream);
            return true;
        } catch (IOException | SAXException ex) {
            return false;
        }
    }

    public static boolean isAdxResponseMessage(String msg) throws ParserConfigurationException, IOException, XPathExpressionException {
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.parse(IOUtils.toInputStream(msg));
            XPath xpath = XPathFactory.newInstance().newXPath();
            String pathResult = xpath.compile("/importSummary[1]").evaluate(doc);
            return pathResult != null && !pathResult.isEmpty();
        } catch (SAXException ex) {
            return false;
        }
    }

}
