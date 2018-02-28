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

import akka.actor.ActorRef;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.tuple.Pair;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.openhim.mediator.engine.messages.MediatorHTTPRequest;
import static zw.org.mohcc.sadombo.utils.AdxUtility.ADX_CONTENT_TYPE;

/**
 *
 * @author Charles Chigoriwa
 */
public class DefaultRequestHeaderMapperTest {

    @Test
    public void test() {
        DefaultRequestHeaderMapper defaultRequestHeaderMapper = new DefaultRequestHeaderMapper();
        Map<String, String> headers = new HashMap<>();
        headers.put("content-type", ADX_CONTENT_TYPE);
        MediatorHTTPRequest request = getMediatorHTTPRequest(headers);
        Map<String, String> newHeaders = defaultRequestHeaderMapper.mapHeaders(request);
        String contentType = newHeaders.get("content-type");
        assertTrue(contentType != null && !contentType.trim().isEmpty());
    }

    private MediatorHTTPRequest getMediatorHTTPRequest(Map<String, String> headers) {
        String path = "/dhis-mediator/api/dataSets";
        String body = "defaultBody";
        List<Pair<String, String>> params = null;
        ActorRef actorRef = null;
        return new MediatorHTTPRequest(actorRef, actorRef, "my-orch", "get", "https", "localhost", 3000, path, body, headers, params);
    }

}
