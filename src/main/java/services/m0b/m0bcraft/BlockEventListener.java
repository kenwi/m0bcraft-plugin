package services.m0b.m0bcraft;

import org.bukkit.block.data.Ageable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockRedstoneEvent;

public class BlockEventListener implements Listener {
    LogService logService;
    int counter;

    public BlockEventListener(LogService logService) {
        this.logService = logService;
        logService.info("Listening to BlockEvents");
    }

    @EventHandler
    public void OnBlockGrowEvent(BlockGrowEvent event) {
        try {
            boolean isAging = event.getBlock().getBlockData() instanceof Ageable;
            if(!isAging)
                return;

            Ageable blockData = (Ageable) event.getBlock().getBlockData();
            Ageable newBlockData = (Ageable) event.getNewState().getBlockData();
            String message = String.format("%s %s Old Age: %s New Age: %s %s",
                    event.getEventName(),
                    event.getNewState().getType().name(),
                    blockData.getAge(),
                    newBlockData.getAge(),
                    logService.LocationToString(event.getNewState().getLocation()));

            logService.writeLog("BlockGrow", message);
        } catch (Exception ex) {
            logService.info("An error occurred.");
            ex.printStackTrace();
        }
    }

    @EventHandler
    public void OnBlockPlaceEvent(BlockPlaceEvent event) {
        try {
            String playerName = event.getPlayer().getDisplayName();
            String location = logService.LocationToString(event.getBlock().getLocation());
            String message = String.format("%s %s %s %s",
                    event.getEventName(),
                    playerName,
                    event.getBlock().getType().name(),
                    location);
            logService.writeLog(playerName, message);
        } catch (Exception ex) {
            logService.info("An error occurred.");
            ex.printStackTrace();
        }
    }

    @EventHandler
    public void OnBlockBreakEvent(BlockBreakEvent event) {
        try {
            String playerName = event.getPlayer().getDisplayName();
            String location = logService.LocationToString(event.getBlock().getLocation());
            String message = String.format("%s %s %s %s",
                    event.getEventName(),
                    playerName,
                    event.getBlock().getType().name(),
                    location);
            logService.writeLog(playerName, message);
        } catch (Exception ex) {
            logService.info("An error occurred.");
            ex.printStackTrace();
        }
    }

    @EventHandler
    public void OnBlockRedstoneEvent(BlockRedstoneEvent event) {
        try {
            if(counter % 100 == 0) {
                String location = logService.LocationToString(event.getBlock().getLocation());
                String message = String.format("%s Count: %s %s",
                        event.getEventName(),
                        counter,
                        location);
                logService.writeLog("Redstone", message);
            }
            counter++;
        } catch (Exception ex) {
            logService.info("An error occurred.");
            ex.printStackTrace();
        }
    }
}
