package services.m0b.m0bcraft;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class EntityListener implements Listener {
    LogService logService;

    public EntityListener(LogService logService) {
        this.logService = logService;
    }

    @EventHandler
    public void OnEntityDeathEvent(EntityDeathEvent event) {
        try {
            int id = event.getEntity().getEntityId();
            EntityType entityType = event.getEntity().getType();
            String message = event.getEventName() + " "
                    + entityType.name() + "(" + id + ")";

            logService.writeLog(entityType.name(), message);
        } catch (Exception ex) {
            logService.info("An error occurred.");
            ex.printStackTrace();
        }
    }
}
