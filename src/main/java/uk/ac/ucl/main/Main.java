package uk.ac.ucl.main;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.*;

import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;

import static uk.ac.ucl.main.Config.*;

/*
Skeleton code copied mainly from
UCLComputerScience (R. Graham), “GitHub - UCLComputerScience/COMP0004JavaWebAppExample,” GitHub, 2025.
https://github.com/UCLComputerScience/COMP0004JavaWebAppExample (accessed Mar. 16, 2026).
 */
public class Main {

    public static Thread addShutdown(final Tomcat tomcat, final Logger logger) {
        Thread shutdownHook = new Thread(() -> {
            try {
                if (tomcat != null) {
                    tomcat.stop();
                    tomcat.destroy();
                    logger.info("Tomcat has shut down normally.");
                }
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Error shutting down Tomcat", e);
            }
        });
        Runtime.getRuntime().addShutdownHook(shutdownHook);
        return shutdownHook;
    }

    public static void main(String[] args) {
        final Logger logger = initialiseLogger();
        final int port = getPort();
        final Path webappDirectory = Paths.get(getWebappDir());
        final Path targetClassesDirectory = Paths.get(getClassesDir());
        final Tomcat tomcat = new Tomcat();

        try {
            tomcat.setPort(port);
            tomcat.getConnector();
            addShutdown(tomcat, logger);

            Context context = getContext(webappDirectory, tomcat);
            setResources(context, targetClassesDirectory);

            tomcat.start();
            logger.info("Server started successfully on port " + port);
            tomcat.getServer().await();
        } catch (IllegalArgumentException e) {
            logger.log(Level.SEVERE, "Configuration error", e);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error occurred while starting the server", e);
        }
    }
}