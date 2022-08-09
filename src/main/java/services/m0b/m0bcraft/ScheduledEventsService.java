package services.m0b.m0bcraft;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class ScheduledEventsService {
    LogService logService;
    JavaPlugin plugin;

    public ScheduledEventsService(LogService logService, JavaPlugin plugin) {
        this.logService = logService;
        this.plugin = plugin;
    }

    public void registerEvents() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this.plugin, () -> {
            Bukkit.getOnlinePlayers().forEach(player -> {
                String playerName = player.getDisplayName();
                String location = player.getLocation().toString();

                logService.writeLog(playerName, location);
            });
        }, 0L, 20 * 10);

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this.plugin, () -> {
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
    }
}
