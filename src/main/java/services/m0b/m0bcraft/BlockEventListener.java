package services.m0b.m0bcraft;

import org.bukkit.block.data.Ageable;
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

    @EventHandler
    public void OnBlockGrowEvent(BlockGrowEvent event) {
        try {
            boolean isAgeable = event.getBlock().getBlockData() instanceof Ageable;
            if(!isAgeable)
                return;

            Ageable blockData = (Ageable) event.getBlock().getBlockData();
            Ageable newBlockData = (Ageable) event.getNewState().getBlockData();

            String message = event.getEventName() + " "
                    + event.getNewState().getType().name() + " "
                    + "Old Age: " + blockData.getAge() + " "
                    + "New Age: " + newBlockData.getAge() + " "
                    + logService.LocationToString(event.getNewState().getLocation());

            logService.writeLog("BlockGrow", message);
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

            logService.writeLog(playerName, message);
        } catch (Exception ex) {
            logService.info("An error occurred.");
            ex.printStackTrace();
        }
    }
}
