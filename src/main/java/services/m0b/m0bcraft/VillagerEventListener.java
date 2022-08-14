package services.m0b.m0bcraft;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.VillagerAcquireTradeEvent;
import org.bukkit.event.entity.VillagerReplenishTradeEvent;

public class VillagerEventListener implements Listener {
    LogService logService;

    public VillagerEventListener(LogService logService) {
        this.logService = logService;
    }

    @EventHandler
    public void OnVillagerReplenishTradeEvent(VillagerReplenishTradeEvent event) {
        try {
            String name = event.getEntity().getName();
            String message = event.getEventName() + " "
                    + event.getEntity().getType() + " "
                    + name;

            logService.writeLog(name, message);
        } catch (Exception ex) {
            logService.info("An error occurred.");
            ex.printStackTrace();
        }
    }

    @EventHandler
    public void OnVillagerAcquireTradeEvent(VillagerAcquireTradeEvent event) {
        try {
            String name = event.getEntity().getName();
            String message = event.getEventName() + " "
                    + event.getEntity().getType() + " "
                    + name;

            logService.writeLog(name, message);
        } catch (Exception ex) {
            logService.info("An error occurred.");
            ex.printStackTrace();
        }
    }

}
