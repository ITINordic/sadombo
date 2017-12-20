package zw.org.mohcc.sadombo;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.apache.commons.io.IOUtils;
import zw.org.mohcc.sadombo.data.util.Credentials;

/**
 *
 * @author Charles Chigoriwa
 */
public class Channels {

    private String dhisChannelScheme;
    private String dhisChannelHost;
    private Integer dhisChannelPort;
    private String dhisChannelContextPath;
    private String dhisChannelUser;
    private String dhisChannelPassword;

    private String epmsChannelScheme;
    private String epmsChannelHost;
    private Integer epmsChannelPort;
    private String epmsChannelContextPath;
    private String epmsChannelUser;
    private String epmsChannelPassword;

    private boolean sadomboAuthenticationEnabled = false;
    private Credentials sadomboCredentials = new Credentials();

    private Properties properties;

    public Channels() {
    }

    public String getDhisChannelScheme() {
        return dhisChannelScheme;
    }

    public void setDhisChannelScheme(String dhisChannelScheme) {
        this.dhisChannelScheme = dhisChannelScheme;
    }

    public String getDhisChannelHost() {
        return dhisChannelHost;
    }

    public void setDhisChannelHost(String dhisChannelHost) {
        this.dhisChannelHost = dhisChannelHost;
    }

    public Integer getDhisChannelPort() {
        return dhisChannelPort;
    }

    public void setDhisChannelPort(Integer dhisChannelPort) {
        this.dhisChannelPort = dhisChannelPort;
    }

    public String getDhisChannelContextPath() {
        return dhisChannelContextPath;
    }

    public void setDhisChannelContextPath(String dhisChannelContextPath) {
        this.dhisChannelContextPath = dhisChannelContextPath;
    }

    public String getDhisChannelUser() {
        return dhisChannelUser;
    }

    public void setDhisChannelUser(String dhisChannelUser) {
        this.dhisChannelUser = dhisChannelUser;
    }

    public String getDhisChannelPassword() {
        return dhisChannelPassword;
    }

    public void setDhisChannelPassword(String dhisChannelPassword) {
        this.dhisChannelPassword = dhisChannelPassword;
    }

    public String getEpmsChannelScheme() {
        return epmsChannelScheme;
    }

    public void setEpmsChannelScheme(String epmsChannelScheme) {
        this.epmsChannelScheme = epmsChannelScheme;
    }

    public String getEpmsChannelHost() {
        return epmsChannelHost;
    }

    public void setEpmsChannelHost(String epmsChannelHost) {
        this.epmsChannelHost = epmsChannelHost;
    }

    public Integer getEpmsChannelPort() {
        return epmsChannelPort;
    }

    public void setEpmsChannelPort(Integer epmsChannelPort) {
        this.epmsChannelPort = epmsChannelPort;
    }

    public String getEpmsChannelContextPath() {
        return epmsChannelContextPath;
    }

    public void setEpmsChannelContextPath(String epmsChannelContextPath) {
        this.epmsChannelContextPath = epmsChannelContextPath;
    }

    public String getEpmsChannelUser() {
        return epmsChannelUser;
    }

    public void setEpmsChannelUser(String epmsChannelUser) {
        this.epmsChannelUser = epmsChannelUser;
    }

    public String getEpmsChannelPassword() {
        return epmsChannelPassword;
    }

    public void setEpmsChannelPassword(String epmsChannelPassword) {
        this.epmsChannelPassword = epmsChannelPassword;
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
        syncFields();
    }

    public Credentials getSadomboCredentials() {
        return sadomboCredentials;
    }

    public void setSadomboCredentials(Credentials sadomboCredentials) {
        this.sadomboCredentials = sadomboCredentials;
    }

    public boolean isSadomboAuthenticationEnabled() {
        return sadomboAuthenticationEnabled;
    }

    public void setSadomboAuthenticationEnabled(boolean sadomboAuthenticationEnabled) {
        this.sadomboAuthenticationEnabled = sadomboAuthenticationEnabled;
    }

    public void setProperties(String resourceName) throws IOException {
        InputStream propsStream = getClass().getClassLoader().getResourceAsStream(resourceName);
        try {
            Properties props = new Properties();
            properties.load(propsStream);
            setProperties(props);

        } finally {
            IOUtils.closeQuietly(propsStream);
        }
    }

    private void syncFields() {
        this.dhisChannelScheme = properties.getProperty("dhis.channel.scheme");
        this.dhisChannelHost = properties.getProperty("dhis.channel.host");
        this.dhisChannelPort = Integer.parseInt(properties.getProperty("dhis.channel.port"));
        this.dhisChannelContextPath = properties.getProperty("dhis.channel.context.path");
        this.dhisChannelUser = properties.getProperty("dhis.channel.user");
        this.dhisChannelPassword = properties.getProperty("dhis.channel.password");

        this.epmsChannelScheme = properties.getProperty("epms.channel.scheme");
        this.epmsChannelHost = properties.getProperty("epms.channel.host");
        this.epmsChannelPort = Integer.parseInt(properties.getProperty("epms.channel.port"));
        this.epmsChannelContextPath = properties.getProperty("epms.channel.context.path");
        this.epmsChannelUser = properties.getProperty("epms.channel.user");
        this.epmsChannelPassword = properties.getProperty("epms.channel.password");

        this.sadomboAuthenticationEnabled = Boolean.valueOf(properties.getProperty("sadombo.authentication.enabled"));
        this.sadomboCredentials.setUsername(properties.getProperty("sadombo.authentication.user"));
        this.sadomboCredentials.setPassword(properties.getProperty("sadombo.authentication.password"));
    }

}
