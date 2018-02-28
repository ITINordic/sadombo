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

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.apache.commons.io.IOUtils;
import zw.org.mohcc.sadombo.utils.Credentials;

/**
 *
 * @author Charles Chigoriwa
 */
public class EnhancedChannels extends Channels {

    private String dhisChannelScheme;
    private String dhisChannelHost;
    private Integer dhisChannelPort;
    private String dhisChannelContextPath;
    private String dhisChannelUser;
    private String dhisChannelPassword;

    private boolean sadomboAuthenticationEnabled = false;
    private Credentials sadomboCredentials = new Credentials();

    private Properties properties;

    public EnhancedChannels() {
    }

    @Override
    public String getDhisChannelScheme() {
        return dhisChannelScheme;
    }

    public void setDhisChannelScheme(String dhisChannelScheme) {
        this.dhisChannelScheme = dhisChannelScheme;
    }

    @Override
    public String getDhisChannelHost() {
        return dhisChannelHost;
    }

    public void setDhisChannelHost(String dhisChannelHost) {
        this.dhisChannelHost = dhisChannelHost;
    }

    @Override
    public Integer getDhisChannelPort() {
        return dhisChannelPort;
    }

    public void setDhisChannelPort(Integer dhisChannelPort) {
        this.dhisChannelPort = dhisChannelPort;
    }

    @Override
    public String getDhisChannelContextPath() {
        return dhisChannelContextPath;
    }

    public void setDhisChannelContextPath(String dhisChannelContextPath) {
        this.dhisChannelContextPath = dhisChannelContextPath;
    }

    @Override
    public String getDhisChannelUser() {
        return dhisChannelUser;
    }

    public void setDhisChannelUser(String dhisChannelUser) {
        this.dhisChannelUser = dhisChannelUser;
    }

    @Override
    public String getDhisChannelPassword() {
        return dhisChannelPassword;
    }

    public void setDhisChannelPassword(String dhisChannelPassword) {
        this.dhisChannelPassword = dhisChannelPassword;
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
        syncFields();
    }

    @Override
    public Credentials getSadomboCredentials() {
        return sadomboCredentials;
    }

    public void setSadomboCredentials(Credentials sadomboCredentials) {
        this.sadomboCredentials = sadomboCredentials;
    }

    @Override
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
        this.sadomboAuthenticationEnabled = Boolean.valueOf(properties.getProperty("sadombo.authentication.enabled"));
        this.sadomboCredentials.setUsername(properties.getProperty("sadombo.authentication.user"));
        this.sadomboCredentials.setPassword(properties.getProperty("sadombo.authentication.password"));
    }

}
