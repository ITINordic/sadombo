package zw.org.mohcc.sadombo;

import zw.org.mohcc.sadombo.utils.Credentials;

/**
 *
 * @author Charles Chigoriwa
 */
public abstract class Channels {

    public abstract String getDhisChannelScheme();

    public abstract String getDhisChannelHost();

    public abstract Integer getDhisChannelPort();

    public abstract String getDhisChannelContextPath();

    public abstract String getDhisChannelUser();

    public abstract String getDhisChannelPassword();

    public abstract Credentials getSadomboCredentials();

    public abstract boolean isSadomboAuthenticationEnabled();

    public String getDhisBaseUrl() {
        return getDhisChannelScheme() + "://" + getDhisChannelHost() + ":" + getDhisChannelPort() + getDhisChannelContextPath();
    }

}
