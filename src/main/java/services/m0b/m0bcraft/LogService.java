package services.m0b.m0bcraft;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Logger;

public class LogService {
    Logger logger;

    public LogService(JavaPlugin plugin) {
        logger = plugin.getLogger();

        File directory = new File("mc-outbound/");
        if(!directory.exists()) {
            directory.mkdirs();
            logger.info("Outgoing message directory does not exist. Creating " + Paths.get("mc-outbound"));
        }

        directory = new File("mc-inbound/");
        if(!directory.exists()) {
            directory.mkdirs();
            logger.info("Incoming message directory does not exist. Creating " + Paths.get("mc-inbound"));
        }

        directory = new File("mc-logs/");
        if(!directory.exists()) {
            directory.mkdirs();
            logger.info("Log directory does not exist. Creating " + Paths.get("mc-logs"));
        }
    }

    public void info(String message) {
        logger.info(message);
    }

    public void writeLog(String name, String message) {
        try {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            String formattedDate = dtf.format(now);

            String logLine = "[" + formattedDate + "] " + "[" + name + "] "  + message + "\n";
            FileWriter file = new FileWriter("mc-logs/" + name + ".txt", true);
            file.append(logLine);
            file.close();

            file = new FileWriter("mc-logs/all.txt", true);
            file.append(logLine);
            file.close();
        } catch (Exception ex){
            System.out.println("An error occurred.");
            ex.printStackTrace();
        }
    }
}