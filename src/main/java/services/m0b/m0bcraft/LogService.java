package services.m0b.m0bcraft;

import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Logger;

public class LogService {
    Logger logger;
    ApplicationState state;

    public LogService(JavaPlugin plugin, ApplicationState state) {
        this.logger = plugin.getLogger();
        this.state = state;

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

    public void writeLog(String message) {
        try {
            FileWriter file = new FileWriter("mc-logs/all.txt", true);
            file.append(message + "\n");
            file.close();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public String LocationToString(Location location) {
        return location.getBlockX() + " "
                + location.getBlockY() + " "
                + location.getBlockZ();
    }

    public void writeLog(String name, String message) {
        try {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            String formattedDate = dtf.format(now);

            String logLine = "[" + formattedDate + "] " + "[" + name + "] "  + message;
            FileWriter file = new FileWriter("mc-logs/" + name + ".txt", true);
            file.append(logLine + "\n");
            file.close();

            /*if(state.containsKey("isRelayEnabled")) {
                if(state.get("isRelayEnabled") == 1) {

                    // Bukkit.broadcastMessage("[" + name + "] "  + message);
                }
            }*/

            writeLog(logLine);

        } catch (Exception ex){
            System.out.println("An error occurred.");
            ex.printStackTrace();
        }
    }
}
