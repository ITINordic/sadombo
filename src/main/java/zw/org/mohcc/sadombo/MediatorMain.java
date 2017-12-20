package zw.org.mohcc.sadombo;

import akka.actor.ActorSystem;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import org.openhim.mediator.engine.*;
import zw.org.mohcc.sadombo.data.util.GeneralUtility;

public class MediatorMain {

    private static RoutingTable buildRoutingTable() throws RoutingTable.RouteAlreadyMappedException {
        RoutingTable routingTable = new RoutingTable();
        routingTable.addRoute("/mediator", DefaultOrchestrator.class);
        routingTable.addRegexRoute("/dhis-mediator/.*", DhisOrchestrator.class);
        return routingTable;
    }

    private static StartupActorsConfig buildStartupActorsConfig() {
        StartupActorsConfig startupActors = new StartupActorsConfig();
        return startupActors;
    }

    private static MediatorConfig loadConfig(String configPath) throws IOException, RoutingTable.RouteAlreadyMappedException {
        MediatorConfig config = new MediatorConfig();
        String mediatorHomeFilePath = System.getProperty("user.home") + File.separator + ".sadombo" + File.separator + "mediator.properties";
        if (configPath != null) {
            config.setProperties(GeneralUtility.loadProperties(configPath));
        } else if (new File(mediatorHomeFilePath).exists()) {
            config.setProperties(GeneralUtility.loadProperties(mediatorHomeFilePath));
        } else {
            config.setProperties("mediator.properties");
        }

        config.setName(config.getProperty("mediator.name"));
        config.setServerHost(config.getProperty("mediator.host"));
        config.setServerPort(Integer.parseInt(config.getProperty("mediator.port")));
        config.setRootTimeout(Integer.parseInt(config.getProperty("mediator.timeout")));

        config.setCoreHost(config.getProperty("core.host"));
        config.setCoreAPIUsername(config.getProperty("core.api.user"));
        config.setCoreAPIPassword(config.getProperty("core.api.password"));
        if (config.getProperty("core.api.port") != null) {
            config.setCoreAPIPort(Integer.parseInt(config.getProperty("core.api.port")));
        }

        config.setRoutingTable(buildRoutingTable());
        config.setStartupActors(buildStartupActorsConfig());

        InputStream regInfo = MediatorMain.class.getClassLoader().getResourceAsStream("mediator-registration-info.json");
        RegistrationConfig regConfig = new RegistrationConfig(regInfo);
        config.setRegistrationConfig(regConfig);

        if (config.getProperty("mediator.heartbeats") != null && "true".equalsIgnoreCase(config.getProperty("mediator.heartbeats"))) {
            config.setHeartbeatsEnabled(true);
        }

        return config;
    }

    private static Channels loadChannels(String channelConfigPath) throws IOException {
        Channels channels = new Channels();
        String channelHomeFilePath = System.getProperty("user.home") + File.separator + ".sadombo" + File.separator + "openhim-channels.properties";
        if (channelConfigPath != null) {
            channels.setProperties(GeneralUtility.loadProperties(channelConfigPath));
        } else if (new File(channelHomeFilePath).exists()) {
            channels.setProperties(GeneralUtility.loadProperties(channelHomeFilePath));
        } else {
            channels.setProperties("openhim-channels.properties");
        }
        return channels;

    }

    public static void main(String... args) throws Exception {
        //setup actor system
        final ActorSystem system = ActorSystem.create("mediator");
        //setup logger for main
        final LoggingAdapter log = Logging.getLogger(system, "main");

        //setup actors
        log.info("Initializing mediator actors...");

        String configPath = GeneralUtility.getParamValue(args, "--conf");
        if (configPath != null) {
            log.info("Loading mediator configuration from '" + configPath + "'...");
        } else {
            log.info("No configuration specified. Using default properties...");
        }

        MediatorConfig config = loadConfig(configPath);

        String channelConfigPath = GeneralUtility.getParamValue(args, "--chan-conf");
        if (channelConfigPath != null) {
            log.info("Loading channels configuration from '" + channelConfigPath + "'...");
        } else {
            log.info("No channels configuration specified. Using default properties...");
        }
        Channels channels = loadChannels(channelConfigPath);
        config.getDynamicConfig().put("channels", channels);

        //Added by Charles Chigoriwa
        final MediatorServer server = new MediatorServer(system, config);

        //setup shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                log.info("Shutting down mediator");
                server.stop();
                system.shutdown();
            }
        });

        log.info("Starting mediator server...");
        server.start();

        log.info(String.format("%s listening on %s:%s", config.getName(), config.getServerHost(), config.getServerPort()));
        Thread.currentThread().join();
    }
}
