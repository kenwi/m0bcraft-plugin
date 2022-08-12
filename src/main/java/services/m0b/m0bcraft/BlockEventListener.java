package services.m0b.m0bcraft;

import org.bukkit.block.BlockState;
import org.bukkit.entity.Ageable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockGrowEvent;

public class BlockEventListener implements Listener {
    LogService logService;

    public BlockEventListener(LogService logService) {
        this.logService = logService;
        logService.info("Listening to BlockEvents");
    }

    private void writeLog(String name, String message) {
        logService.writeLog(name, message);
    }

    @EventHandler
    public void OnBlockGrowEvent(BlockGrowEvent event) {
        try {
            String message = event.getEventName() + " "
                    + event.getNewState().getType().name() + " "
                    + logService.LocationToString(event.getNewState().getLocation());

            writeLog("BlockGrow", message);
        } catch (Exception ex) {
            logService.info("An error occurred.");
            ex.printStackTrace();
        }
    }

    @EventHandler
    public void OnBlockBreakEvent(BlockBreakEvent event) {
        try {
            String playerName = event.getPlayer().getDisplayName();
            String message = event.getEventName() + " " + event.getBlock().getType().name();

            writeLog(playerName, message);
        } catch (Exception ex) {
            logService.info("An error occurred.");
            ex.printStackTrace();
        }
    }
}
