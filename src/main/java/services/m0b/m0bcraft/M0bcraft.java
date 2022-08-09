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
    LogService logService;
    ScheduledEventsService scheduledEventsService;

    @Override
    public void onEnable() {
        logService = new LogService(this);
        scheduledEventsService = new ScheduledEventsService(logService, this);
        scheduledEventsService.registerEvents();

        this.getCommand("map")
                .setExecutor(new MapCommand());

        Bukkit.getPluginManager()
                .registerEvents(new EventListener(logService), this);

        getLogger().info("Started");
    }

    @Override
    public void onDisable() {
        logService = null;
        scheduledEventsService = null;

        getLogger().info("Stopped");
    }
}
