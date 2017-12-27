package zw.org.mohcc.sadombo;

import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Charles Chigoriwa
 */
public class DefaultSadomboBeanFactoryTest {

    @Test
    public void test() {
        DefaultSadomboBeanFactory sadomboBeanFactory = DefaultSadomboBeanFactory.getInstance(null);
        Assert.assertNotNull(sadomboBeanFactory.getChannels());
        Assert.assertNotNull(sadomboBeanFactory.getSecurityManager());
        Assert.assertNotNull(sadomboBeanFactory.getRequestValidator());
        Assert.assertNotNull(sadomboBeanFactory.getRequestBodyTransformer());
        Assert.assertNotNull(sadomboBeanFactory.getRequestHeaderMapper());
        Assert.assertNotNull(sadomboBeanFactory.getRequestTargetMapper());
        Assert.assertNotNull(sadomboBeanFactory.getResponseTransformer());
    }

}
