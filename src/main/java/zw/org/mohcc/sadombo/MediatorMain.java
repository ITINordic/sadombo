package zw.org.mohcc.sadombo;

import akka.actor.ActorSystem;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import java.io.IOException;
import java.io.InputStream;
import org.openhim.mediator.engine.*;
import static zw.org.mohcc.sadombo.utils.ConfigUtility.findConfigPath;
import static zw.org.mohcc.sadombo.utils.ConfigUtility.loadChannels;
import zw.org.mohcc.sadombo.utils.GeneralUtility;

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
        if (configPath != null) {
            config.setProperties(GeneralUtility.loadProperties(configPath));
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

    public static void main(String... args) throws Exception {
        //setup actor system
        final ActorSystem system = ActorSystem.create("mediator");
        //setup logger for main
        final LoggingAdapter log = Logging.getLogger(system, "main");

        //setup actors
        log.info("Initializing mediator actors...");

        String configPath = findConfigPath(args, log);
        MediatorConfig config = loadConfig(configPath);

        Channels channels = loadChannels(args, log);
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
