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
