package services.m0b.m0bcraft;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ToggleRelayCommand implements CommandExecutor {
    boolean isEnabled;
    private final LogService logService;
    ApplicationState state;

    public ToggleRelayCommand(LogService logService, ApplicationState state) {
        this.logService = logService;
        this.state = state;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.isOp()) {
                Execute();
                String message = "Relay set to " + isEnabled;
                logService.writeLog(player.getName(), message);
                player.sendMessage(message);
                return true;
            }
            player.sendMessage("Sorry Dave, I can't let you do that.");
            return false;
        }
        Execute();
        String message = "Relay set to " + isEnabled;
        logService.writeLog("Server", message);
        return true;
    }

    void Execute() {
    }
}
