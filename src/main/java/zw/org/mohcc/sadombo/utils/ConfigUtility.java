package zw.org.mohcc.sadombo.utils;

import akka.event.LoggingAdapter;
import java.io.File;
import java.io.IOException;
import zw.org.mohcc.sadombo.Channels;

/**
 *
 * @author Charles Chigoriwa
 */
public class ConfigUtility {

    public static Channels loadChannels(String[] args, LoggingAdapter log) throws IOException {
        String channelConfigPath = findChannelConfigPath(args, log);
        Channels channels = loadChannels(channelConfigPath);
        return channels;
    }

    public static Channels loadChannels(String channelConfigPath) throws IOException {
        Channels channels = new Channels();
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
        if (configPath != null) {
            basicInfoLog(log, "Loading mediator configuration from '" + configPath + "'...");
        } else if (new File(mediatorHomeFilePath).exists()) {
            configPath = mediatorHomeFilePath;
            basicInfoLog(log, "Loading channels configuration from '" + mediatorHomeFilePath + "'...");
        } else {
            basicInfoLog(log, "No configuration specified. Using default properties...");
        }
        return configPath;
    }

    public static String findChannelConfigPath(String[] args, LoggingAdapter log) {
        String channelConfigPath = GeneralUtility.getParamValue(args, "--chan-conf");
        String channelHomeFilePath = System.getProperty("user.home") + File.separator + ".sadombo" + File.separator + "openhim-channels.properties";
        if (channelConfigPath != null) {
            basicInfoLog(log, "Loading channels configuration from '" + channelConfigPath + "'...");
        } else if (new File(channelHomeFilePath).exists()) {
            channelConfigPath = channelHomeFilePath;
            basicInfoLog(log, "Loading channels configuration from '" + channelHomeFilePath + "'...");

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
