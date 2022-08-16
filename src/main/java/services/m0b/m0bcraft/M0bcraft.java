package services.m0b.m0bcraft;

import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class M0bcraft extends JavaPlugin {
    LogService logService;
    ApplicationState state;
    ScheduledEventsService scheduledEventsService;

    @Override
    public void onEnable() {
        state = new ApplicationState();
        logService = new LogService(this, state);
        scheduledEventsService = new ScheduledEventsService(logService, this, state);

        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new PlayerEventListener(logService), this);
        pluginManager.registerEvents(new BlockEventListener(logService), this);
        pluginManager.registerEvents(new InventoryEventListener(logService, this, state), this);
        scheduledEventsService.registerEvents();

        PluginCommand command;
        if((command = this.getCommand("map")) != null) {
            command.setExecutor(new MapCommand());
        }
        if((command = getCommand("resetcounters")) != null){
            command.setExecutor(new ResetCountersCommand(logService, state));
        }
        if((command = getCommand("togglerelay")) != null){
            command.setExecutor(new ToggleRelayCommand(logService, state));
        }
        getLogger().info("Started");
    }

    @Override
    public void onDisable() {
        state = null;
        logService = null;

        getLogger().info("Stopped");
    }
}
