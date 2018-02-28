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
package zw.org.mohcc.sadombo;

import zw.org.mohcc.sadombo.mapper.RequestHeaderMapper;
import zw.org.mohcc.sadombo.mapper.RequestTargetMapper;
import zw.org.mohcc.sadombo.security.SecurityManager;
import zw.org.mohcc.sadombo.transformer.RequestBodyTransformer;
import zw.org.mohcc.sadombo.transformer.ResponseTransformer;
import zw.org.mohcc.sadombo.validator.RequestValidator;

/**
 *
 * @author Charles Chigoriwa
 */
public abstract class SadomboBeanFactory {

    public abstract SecurityManager getSecurityManager();

    public abstract RequestValidator getRequestValidator();

    public abstract RequestBodyTransformer getRequestBodyTransformer();

    public abstract RequestHeaderMapper getRequestHeaderMapper();

    public abstract RequestTargetMapper getRequestTargetMapper();

    public abstract ResponseTransformer getResponseTransformer();

    public abstract Channels getChannels();

}
