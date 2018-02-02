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

import akka.event.LoggingAdapter;
import java.io.IOException;
import zw.org.mohcc.sadombo.mapper.DefaultRequestHeaderMapper;
import zw.org.mohcc.sadombo.mapper.DefaultRequestTargetMapper;
import zw.org.mohcc.sadombo.mapper.RequestHeaderMapper;
import zw.org.mohcc.sadombo.mapper.RequestTargetMapper;
import zw.org.mohcc.sadombo.security.DefaultSecurityManager;
import zw.org.mohcc.sadombo.security.SecurityManager;
import zw.org.mohcc.sadombo.transformer.DefaultRequestBodyTransformer;
import zw.org.mohcc.sadombo.transformer.DefaultResponseTransformer;
import zw.org.mohcc.sadombo.transformer.RequestBodyTransformer;
import zw.org.mohcc.sadombo.transformer.ResponseTransformer;
import static zw.org.mohcc.sadombo.utils.ConfigUtility.loadChannels;
import zw.org.mohcc.sadombo.validator.DefaultRequestValidator;
import zw.org.mohcc.sadombo.validator.RequestValidator;

/**
 *
 * @author Charles Chigoriwa
 */
public final class DefaultSadomboBeanFactory extends SadomboBeanFactory {

    private Channels channels;
    private SecurityManager securityManager;
    private RequestValidator requestValidator;
    private RequestBodyTransformer requestBodyTransformer;
    private RequestHeaderMapper requestHeaderMapper;
    private RequestTargetMapper requestTargetMapper;
    private ResponseTransformer responseTransformer;

    @Override
    public synchronized SecurityManager getSecurityManager() {
        if (securityManager == null) {
            securityManager = new DefaultSecurityManager();
        }
        return securityManager;
    }

    @Override
    public synchronized RequestValidator getRequestValidator() {
        if (requestValidator == null) {
            requestValidator = new DefaultRequestValidator();
        }
        return requestValidator;
    }

    @Override
    public synchronized RequestBodyTransformer getRequestBodyTransformer() {
        if (requestBodyTransformer == null) {
            requestBodyTransformer = new DefaultRequestBodyTransformer();
        }
        return requestBodyTransformer;
    }

    @Override
    public synchronized RequestHeaderMapper getRequestHeaderMapper() {
        if (requestHeaderMapper == null) {
            requestHeaderMapper = new DefaultRequestHeaderMapper();
        }
        return requestHeaderMapper;
    }

    @Override
    public synchronized RequestTargetMapper getRequestTargetMapper() {
        if (requestTargetMapper == null) {
            requestTargetMapper = new DefaultRequestTargetMapper();
        }
        return requestTargetMapper;
    }

    @Override
    public synchronized ResponseTransformer getResponseTransformer() {
        if (responseTransformer == null) {
            responseTransformer = new DefaultResponseTransformer();
        }
        return responseTransformer;
    }

    @Override
    public synchronized Channels getChannels() {
        if (channels == null) {
            try {
                channels = loadChannels(args, log);
            } catch (IOException ex) {
                throw new SadomboException(ex);
            }
        }
        return channels;
    }

    private DefaultSadomboBeanFactory(LoggingAdapter log, String[] args) {
        this.log = log;
        this.args = args;
    }

    public static DefaultSadomboBeanFactory getInstance(LoggingAdapter log, String... args) {
        DefaultSadomboBeanFactory sadomboFactory = new DefaultSadomboBeanFactory(log, args);
        return sadomboFactory;
    }

    private final LoggingAdapter log;
    private final String[] args;

}
