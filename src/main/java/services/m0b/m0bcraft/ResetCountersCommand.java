package services.m0b.m0bcraft;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ResetCountersCommand implements CommandExecutor {
    LogService logService;
    ApplicationState state;

    public ResetCountersCommand(LogService logService, ApplicationState state) {
        this.logService = logService;
        this.state = state;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if(!player.isOp()) {
                Execute(player.getName());
                return true;
            }
            return false;
        }
        Execute("Server");
        return true;
    }

    void Execute(String executingEntity) {
        //counter.clear();
        logService.writeLog(executingEntity, "Reset counters");
    }
}
