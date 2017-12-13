package zw.org.mohcc.sadombo;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author Charles Chigoriwa
 */
public class OpenhimChannels {

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

    private Properties properties;

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
    }

    public void setProperties(String resourceName) throws IOException {
        InputStream propsStream = getClass().getClassLoader().getResourceAsStream(resourceName);
        try {
            Properties props = new Properties();
            props.load(propsStream);
            setProperties(props);

            this.dhisChannelScheme = props.getProperty("dhis.channel.scheme");
            this.dhisChannelHost = props.getProperty("dhis.channel.host");
            this.dhisChannelPort = Integer.parseInt(props.getProperty("dhis.channel.port"));
            this.dhisChannelContextPath = props.getProperty("dhis.channel.context.path");
            this.dhisChannelUser = props.getProperty("dhis.channel.user");
            this.dhisChannelPassword = props.getProperty("dhis.channel.password");

            this.epmsChannelScheme = props.getProperty("epms.channel.scheme");
            this.epmsChannelHost = props.getProperty("epms.channel.host");
            this.epmsChannelPort = Integer.parseInt(props.getProperty("epms.channel.port"));
            this.epmsChannelContextPath = props.getProperty("epms.channel.context.path");
            this.epmsChannelUser = props.getProperty("epms.channel.user");
            this.epmsChannelPassword = props.getProperty("epms.channel.password");

        } finally {
            IOUtils.closeQuietly(propsStream);
        }
    }

}
