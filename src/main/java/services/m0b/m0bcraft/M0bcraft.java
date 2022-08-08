package services.m0b.m0bcraft;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;

public final class M0bcraft extends JavaPlugin {

    @Override
    public void onEnable() {
        this.getCommand("map").setExecutor(new MapCommand());

        Bukkit.getPluginManager().registerEvents(new EventListener(getLogger()), this);

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
            Bukkit.getOnlinePlayers().forEach(player -> {
                String playerName = player.getDisplayName();
                String location = player.getLocation().toString();

                writeLog(playerName, location);
            });
        }, 0L, 20 * 10);

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
            try (Stream<Path> paths = Files.walk(Paths.get("mc-inbound"))) {
                paths.filter(Files::isRegularFile).forEach(file -> {
                    try {
                        byte[] bytes = Files.readAllBytes(file.toAbsolutePath());
                        String content = new String(bytes);

                        int pos = content.indexOf(">");
                        String message = content.substring(pos + 1);
                        String sender = content.substring(1, pos);
                        Bukkit.getOnlinePlayers().forEach(player -> {
                            player.sendMessage(content);
                        });
                        Files.deleteIfExists(file.toAbsolutePath());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });

            } catch (IOException ex) {
                System.out.println("An error occurred.");
                ex.printStackTrace();
            }
        }, 0L, 20);

        getLogger().info("Started");
    }

    private void writeLog(String name, String message) {
        try {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("[dd/MM/yyyy] [HH:mm:ss]");
            LocalDateTime now = LocalDateTime.now();
            String formattedDate = dtf.format(now);

            FileWriter file = new FileWriter("mc-logs/" + name + ".txt", true);
            file.append("[" + formattedDate + "] " + message + "\n");
            file.close();

        } catch (Exception ex) {
            System.out.println("An error occurred.");
            ex.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
        getLogger().info("Stopped");
    }
}
