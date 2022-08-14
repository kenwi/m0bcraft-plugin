package services.m0b.m0bcraft;

import org.bukkit.Location;
import org.bukkit.advancement.Advancement;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDropItemEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.*;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class PlayerEventListener implements Listener {
    LogService logService;

    public PlayerEventListener(LogService logService) {
        this.logService = logService;
        logService.info("Listening to PlayerEvents");
    }

    private void writeLog(String name, String message) {
        logService.writeLog(name, message);
    }

    @EventHandler
    public void OnPlayerChatEvent(AsyncPlayerChatEvent event) {
        try {
            String playerName = event.getPlayer().getDisplayName();
            String random = UUID.randomUUID().toString();
            String fileName = playerName + "-chatevent-" + random;

            FileWriter file = new FileWriter("mc-outbound/" + fileName + ".txt");
            String message = playerName + ": " + event.getMessage();
            file.write(message);
            file.close();
        } catch (IOException ex) {
            logService.info("An error occurred.");
            ex.printStackTrace();
        }
    }

    @EventHandler
    public void OnEntityDropItemEvent(EntityDropItemEvent event) {
        try {
            int id = event.getEntity().getEntityId();
            String entityName = event.getEntity().getName();
            String message = event.getEventName() + " "
                    + entityName + "(" + id + ") " + "DROPS "
                    + event.getItemDrop().getName() + " AT "
                    + logService.LocationToString(event.getItemDrop().getLocation());

            if (entityName.contains("Chicken")) {
                Location location = event.getItemDrop().getLocation();
                Collection<Entity> nearby = event.getItemDrop()
                        .getWorld()
                        .getNearbyEntities(location, 1, 1, 1);
                int count = nearby.size();

                AtomicInteger nearbyChickens = new AtomicInteger();
                nearby.forEach(entity -> {
                    if (entity.getName().contains("Chicken")) {
                        nearbyChickens.getAndIncrement();
                    }
                });

                if (nearbyChickens.get() > 10) {
                    LivingEntity chicken = (LivingEntity) event.getEntity();
                    chicken.damage(50);
                    logService.info("Damage Chicken");
                }

                logService.info(String.valueOf(nearbyChickens) + " / " + count);
            }

            writeLog(entityName, message);
        } catch (Exception ex) {
            logService.info("An error occurred.");
            ex.printStackTrace();
        }
    }

    @EventHandler
    public void OnEntityPickupItemEvent(EntityPickupItemEvent event) {
        try {
            int id = event.getEntity().getEntityId();
            String entityName = event.getEntity().getName();
            String message = event.getEventName() + " "
                    + entityName + "(" + id + ") " + "PICKUP "
                    + event.getItem().getName() + " AT "
                    + logService.LocationToString(event.getItem().getLocation());

            writeLog(entityName, message);
        } catch (Exception ex) {
            logService.info("An error occurred.");
            ex.printStackTrace();
        }
    }


    @EventHandler
    public void OnPlayerInteractEvent(PlayerInteractEvent event) {
        try {
            /*String playerName = event.getPlayer().getDisplayName();
            String message = event.getAction().name();

            Block block;
            if ((block = event.getClickedBlock()) != null) {
                message += " " + logService.LocationToString(block.getState().getLocation());
            }*/
            String name = event.getPlayer().getName();
            String message = event.getEventName() + " "
                    + event.getAction().name() + " ";

            if(event.getClickedBlock() != null)
                    message += event.getClickedBlock().getType().name();

            logService.writeLog(name, message);
        } catch (Exception ex) {
            logService.info("An error occurred.");
            ex.printStackTrace();
        }
    }

    @EventHandler
    public void OnPlayerAdvancementDone(PlayerAdvancementDoneEvent event) {
        try {
            String playerName = event.getPlayer().getDisplayName();
            Advancement advancement = event.getAdvancement();
            String message = " made the advancement " + advancement;

            writeLog(playerName, message);
        } catch (Exception ex) {
            logService.info("An error occurred.");
            ex.printStackTrace();
        }
    }

    @EventHandler
    public void OnPlayerJoin(PlayerJoinEvent event) {
        try {
            boolean isPlayerOperator = event.getPlayer().isOp();
            if (isPlayerOperator) {
                event.setJoinMessage("");
                return;
            }

            String playerName = event.getPlayer().getDisplayName();
            writeLog(playerName, playerName + " joined");

            String random = UUID.randomUUID().toString();
            String fileName = playerName + "-join-" + random;

            FileWriter file = new FileWriter("mc-outbound/" + fileName + ".txt");

            int messageLength = event.getJoinMessage()
                    .length();
            String message = event.getJoinMessage()
                    .substring(2, messageLength);

            file.write(message);
            file.close();
        } catch (IOException ex) {
            logService.info("An error occurred.");
            ex.printStackTrace();
        }
    }

    @EventHandler
    public void OnPlayerQuit(PlayerQuitEvent event) {
        try {
            boolean isPlayerOperator = event.getPlayer().isOp();
            if (isPlayerOperator) {
                event.setQuitMessage("");
                return;
            }

            String playerName = event.getPlayer().getDisplayName();
            writeLog(playerName, playerName + " quit");

            String random = UUID.randomUUID().toString();
            String fileName = playerName + "-quit-" + random;
            FileWriter file = new FileWriter("mc-outbound/" + fileName + ".txt");

            int messageLength = event.getQuitMessage()
                    .length();
            String message = event.getQuitMessage()
                    .substring(2, messageLength);

            file.write(message);
            file.close();
        } catch (IOException ex) {
            logService.info("An error occurred.");
            ex.printStackTrace();
        }
    }


}
