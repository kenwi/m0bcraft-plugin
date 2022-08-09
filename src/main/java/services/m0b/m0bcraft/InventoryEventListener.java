package services.m0b.m0bcraft;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;

public class InventoryEventListener implements Listener {
    LogService logService;

    public InventoryEventListener(LogService logService) {
        this.logService = logService;
        logService.info("Listening to InventoryEvents");
    }

    private void writeLog(String name, String message) {
        logService.writeLog(name, message);
    }

    @EventHandler
    public void OnInventoryOpenEvent(InventoryOpenEvent event) {
        try {
            String playerName = event.getPlayer().getName();
            String message = event.getEventName() + " " + event.getInventory();

            writeLog(playerName, message);
        } catch (Exception ex){
            logService.info("An error occurred.");
            ex.printStackTrace();
        }
    }
}
