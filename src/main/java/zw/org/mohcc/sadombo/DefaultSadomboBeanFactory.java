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
