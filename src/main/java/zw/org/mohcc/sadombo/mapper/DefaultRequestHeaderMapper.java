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
package zw.org.mohcc.sadombo.mapper;

import java.util.HashMap;
import java.util.Map;
import org.openhim.mediator.engine.messages.MediatorHTTPRequest;
import static zw.org.mohcc.sadombo.utils.GeneralUtility.copyHeader;

/**
 *
 * @author Charles Chigoriwa
 */
public class DefaultRequestHeaderMapper extends RequestHeaderMapper {

    @Override
    public Map<String, String> mapHeaders(MediatorHTTPRequest request) {
        Map<String, String> headers = request.getHeaders();
        Map<String, String> newHeaders = new HashMap<>();
        String[] headerNames = {"accept", "content-type", "x-openhim-transactionid", "x-forwarded-for", "x-forwarded-host", "x-dhis-authorization"};
        for (String headerName : headerNames) {
            copyHeader(headerName, headers, newHeaders);
        }
        return newHeaders;
    }

}
