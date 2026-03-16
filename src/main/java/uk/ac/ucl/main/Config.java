package uk.ac.ucl.main;

import org.apache.catalina.Context;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;
import uk.ac.ucl.model.HospitalDataType;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Map;
import java.util.logging.*;

/*
Skeleton code copied mainly from
UCLComputerScience (R. Graham), “GitHub - UCLComputerScience/COMP0004JavaWebAppExample,” GitHub, 2025.
https://github.com/UCLComputerScience/COMP0004JavaWebAppExample (accessed Mar. 16, 2026).
 */
public class Config {

    protected static final int DEFAULT_PORT = 8080;
    protected static final String DEFAULT_WEBAPP_DIR = "src/main/webapp/";
    protected static final String DEFAULT_TARGET_CLASSES = "target/classes";
    protected static final String WEB_INF_CLASSES = "/WEB-INF/classes";
    protected static final String LOGFILE = "logfile.txt";

    public static final Map<HospitalDataType, Path> DATA_TYPE_PATH_MAP = Collections.synchronizedMap(Map.of(
            HospitalDataType.GENERAL, Path.of("data", "patients100.csv"),
            HospitalDataType.ALLERGIES, Path.of("data", "allergies100.csv"),
            HospitalDataType.CAREPLANS, Path.of("data", "careplans100.csv"),
            HospitalDataType.CONDITIONS, Path.of("data", "conditions100.csv"),
            HospitalDataType.ENCOUNTERS, Path.of("data", "encounters100.csv"),
            HospitalDataType.IMAGINGSTUDIES, Path.of("data", "imaging_studies100.csv"),
            HospitalDataType.IMMUNIZATIONS, Path.of("data", "immunizations100.csv"),
            HospitalDataType.MEDICATIONS, Path.of("data", "medications100.csv"),
            HospitalDataType.OBSERVATIONS, Path.of("data", "observations100.csv"),
            HospitalDataType.PROCEDURES, Path.of("data", "procedures100.csv")
    ));

    public static int SEARCH_CACHE_SIZE = 1000;
    public static int RESULTS_PER_PAGE = 20;

    protected static int getPort() {
        String port = System.getProperty("SERVER_PORT", System.getenv("SERVER_PORT"));
        return (port != null) ? Integer.parseInt(port) : DEFAULT_PORT;
    }

    protected static String getWebappDir() {
        String dir = System.getProperty("WEBAPP_DIR", System.getenv("WEBAPP_DIR"));
        return (dir != null) ? dir : DEFAULT_WEBAPP_DIR;
    }

    protected static String getClassesDir() {
        String dir = System.getProperty("CLASSES_DIR", System.getenv("CLASSES_DIR"));
        return (dir != null) ? dir : DEFAULT_TARGET_CLASSES;
    }

    protected static Logger initialiseLogger() {
        Logger logger = Logger.getLogger(Main.class.getName());
        logger.setUseParentHandlers(false);

        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setLevel(Level.INFO);
        logger.addHandler(consoleHandler);

        try {
            FileHandler fileHandler = new FileHandler(LOGFILE);
            fileHandler.setFormatter(new SimpleFormatter());
            fileHandler.setLevel(Level.INFO);
            logger.addHandler(fileHandler);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to create log file", e);
        }

        logger.setLevel(Level.INFO);
        return logger;
    }

    protected static Context getContext(Path webappDirectory, Tomcat tomcat) {
        if (!Files.exists(webappDirectory) || !Files.isDirectory(webappDirectory)) {
            throw new IllegalArgumentException("Webapp directory does not exist: " + webappDirectory);
        }
        return tomcat.addWebapp("/", webappDirectory.toAbsolutePath().toString());
    }

    protected static void setResources(Context context, Path targetClassesDirectory) {
        WebResourceRoot resources = new StandardRoot(context);
        resources.addPreResources(new DirResourceSet(resources, WEB_INF_CLASSES,
                targetClassesDirectory.toAbsolutePath().toString(), "/"));
        context.setResources(resources);
    }
}
