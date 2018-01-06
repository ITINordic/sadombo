package zw.org.mohcc.sadombo.utils;

import akka.event.LoggingAdapter;
import java.io.File;
import java.io.IOException;
import org.openhim.mediator.engine.MediatorConfig;
import zw.org.mohcc.sadombo.Channels;
import zw.org.mohcc.sadombo.EnhancedChannels;
import zw.org.mohcc.sadombo.SadomboBeanFactory;
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
public class ConfigUtility {

    public static SadomboBeanFactory getSadomboBeanFactory(MediatorConfig config) {
        return (SadomboBeanFactory) config.getDynamicConfig().get("sadomboBeanFactory");
    }

    public static Channels getChannels(MediatorConfig config) {
        return getSadomboBeanFactory(config).getChannels();
    }

    public static RequestBodyTransformer getRequestBodyTransformer(MediatorConfig config) {
        return getSadomboBeanFactory(config).getRequestBodyTransformer();
    }

    public static RequestValidator getRequestValidator(MediatorConfig config) {
        return getSadomboBeanFactory(config).getRequestValidator();
    }

    public static RequestHeaderMapper getRequestHeaderMapper(MediatorConfig config) {
        return getSadomboBeanFactory(config).getRequestHeaderMapper();
    }

    public static RequestTargetMapper getRequestTargetMapper(MediatorConfig config) {
        return getSadomboBeanFactory(config).getRequestTargetMapper();
    }

    public static ResponseTransformer getResponseTransformer(MediatorConfig config) {
        return getSadomboBeanFactory(config).getResponseTransformer();
    }

    public static SecurityManager getSecurityManager(MediatorConfig config) {
        return getSadomboBeanFactory(config).getSecurityManager();
    }

    public static EnhancedChannels loadChannels(String[] args, LoggingAdapter log) throws IOException {
        String channelConfigPath = findChannelConfigPath(args, log);
        EnhancedChannels channels = loadChannels(channelConfigPath);
        return channels;
    }

    public static EnhancedChannels loadChannels(String channelConfigPath) throws IOException {
        EnhancedChannels channels = new EnhancedChannels();
        if (channelConfigPath != null) {
            channels.setProperties(GeneralUtility.loadProperties(channelConfigPath));
        } else {
            channels.setProperties("openhim-channels.properties");
        }
        return channels;

    }

    public static String findConfigPath(String[] args, LoggingAdapter log) {
        String configPath = GeneralUtility.getParamValue(args, "--conf");
        String mediatorHomeFilePath = System.getProperty("user.home") + File.separator + ".sadombo" + File.separator + "mediator.properties";
        String mediatorEtcFilePath = File.separator + "etc" + File.separator + "sadombo" + File.separator + "mediator.properties";
        if (configPath != null) {
            basicInfoLog(log, "Loading mediator configuration from '" + configPath + "'...");
        } else if (new File(mediatorHomeFilePath).exists()) {
            configPath = mediatorHomeFilePath;
            basicInfoLog(log, "Loading mediator configuration from '" + mediatorHomeFilePath + "'...");
        } else if (new File(mediatorEtcFilePath).exists()) {
            configPath = mediatorEtcFilePath;
            basicInfoLog(log, "Loading mediator configuration from '" + mediatorEtcFilePath + "'...");
        } else {
            basicInfoLog(log, "No configuration specified. Using default properties...");
        }
        return configPath;
    }

    public static String findChannelConfigPath(String[] args, LoggingAdapter log) {
        String channelConfigPath = GeneralUtility.getParamValue(args, "--chan-conf");
        String channelHomeFilePath = System.getProperty("user.home") + File.separator + ".sadombo" + File.separator + "openhim-channels.properties";
        String channelEtcFilePath = File.separator + "etc" + File.separator + "sadombo" + File.separator + "openhim-channels.properties";
        if (channelConfigPath != null) {
            basicInfoLog(log, "Loading channels configuration from '" + channelConfigPath + "'...");
        } else if (new File(channelHomeFilePath).exists()) {
            channelConfigPath = channelHomeFilePath;
            basicInfoLog(log, "Loading channels configuration from '" + channelHomeFilePath + "'...");
        } else if (new File(channelEtcFilePath).exists()) {
            channelConfigPath = channelEtcFilePath;
            basicInfoLog(log, "Loading channels configuration from '" + channelEtcFilePath + "'...");

        } else {
            basicInfoLog(log, "No channels configuration specified. Using default properties...");
        }
        return channelConfigPath;
    }

    private static void basicInfoLog(LoggingAdapter log, String message) {
        if (log != null) {
            log.info(message);
        }
    }

}
