package services.m0b.m0bcraft;

import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class M0bcraft extends JavaPlugin {
    LogService logService;
    ScheduledEventsService scheduledEventsService;

    @Override
    public void onEnable() {
        logService = new LogService(this);
        scheduledEventsService = new ScheduledEventsService(logService, this);
        scheduledEventsService.registerEvents();

        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new PlayerEventListener(logService), this);
        pluginManager.registerEvents(new BlockEventListener(logService), this);
        pluginManager.registerEvents(new InventoryEventListener(logService), this);

        PluginCommand command = this.getCommand("map");
        if(command != null) {
            command.setExecutor(new MapCommand());
        }

        getLogger().info("Started");
    }

    @Override
    public void onDisable() {
        logService = null;
        scheduledEventsService = null;

        getLogger().info("Stopped");
    }
}
