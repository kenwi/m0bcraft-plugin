package services.m0b.m0bcraft;

import org.bukkit.advancement.Advancement;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDropItemEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import java.util.logging.Logger;

public class EventListener implements Listener {
    Logger logger;
    public EventListener(Logger logger) {
        this.logger = logger;

        File directory = new File("mc-outbound/");
        if(!directory.exists()) {
            directory.mkdirs();
            logger.info("Outgoing message directory does not exist. Creating " + Paths.get("mc-outbound"));
        }

        directory = new File("mc-inbound/");
        if(!directory.exists()) {
            directory.mkdirs();
            logger.info("Incoming message directory does not exist. Creating " + Paths.get("mc-inbound"));
        }

        directory = new File("mc-logs/");
        if(!directory.exists()) {
            directory.mkdirs();
            logger.info("Log directory does not exist. Creating " + Paths.get("mc-logs"));
        }
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
        } catch (IOException ex){
            System.out.println("An error occurred.");
            ex.printStackTrace();
        }
    }

    @EventHandler
    public void OnBlockBreakEvent(BlockBreakEvent event) {
        try {
            String playerName = event.getPlayer().getDisplayName();
            String message = event.getEventName() + " "  + event.getBlock().getType().name();

            writeLog(playerName, message);
        } catch (Exception ex){
            System.out.println("An error occurred.");
            ex.printStackTrace();
        }
    }

    @EventHandler
    public void OnEntityPickupItemEvent(EntityPickupItemEvent event) {
        try {
            if(event.getEntity().getType() == EntityType.PLAYER) {
                 LivingEntity entity = event.getEntity();
                 String playerName = entity.getName();
                 String message = event.getEventName() + " " + event.getItem().getName();

                 writeLog(playerName, message);
            }
        }
        catch (Exception ex){
            System.out.println("An error occurred.");
            ex.printStackTrace();
        }
    }

    @EventHandler
    public void OnEntityDropItemEvent(EntityDropItemEvent event) {
        try {
            if(event.getEntity().getType() == EntityType.PLAYER) {
                String playerName = event.getEntity().getName();
                String message = event.getEventName() + " " + event.getItemDrop().getName();

                writeLog(playerName, message);
            }
        }
        catch (Exception ex){
            System.out.println("An error occurred.");
            ex.printStackTrace();
        }
    }

    @EventHandler
    public void OnPlayerInteractEvent(PlayerInteractEvent event) {
        try {
            String playerName = event.getPlayer().getDisplayName();
            String message = event.getAction().name();

            Block block;
            if((block = event.getClickedBlock()) != null) {
                message += " " + block.getState().getLocation();
            }

            writeLog(playerName, message);
        } catch (Exception ex){
            System.out.println("An error occurred.");
            ex.printStackTrace();
        }
    }

    private void writeLog(String name, String message) {
        try {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("[dd/MM/yyyy] [HH:mm:ss]");
            LocalDateTime now = LocalDateTime.now();
            String formattedDate = dtf.format(now);

            FileWriter file = new FileWriter("mc-logs/" + name + ".txt", true);
            file.append("[" + formattedDate + "] " + message + "\n");
            file.close();

        } catch (Exception ex){
            System.out.println("An error occurred.");
            ex.printStackTrace();
        }
    }

    @EventHandler
    public void OnPlayerAdvancementDone(PlayerAdvancementDoneEvent event) {
        try {
            String playerName = event.getPlayer().getDisplayName();
            String random = UUID.randomUUID().toString();
            String fileName = playerName + "-advancement-" + random;
            FileWriter file = new FileWriter("mc-outbound/" + fileName + ".txt");

            Advancement advancement = event.getAdvancement();
            String message = playerName + " has made the advancement " + advancement.toString();

            // file.write(message);
            // file.close();
        } catch (IOException ex){
            System.out.println("An error occurred.");
            ex.printStackTrace();
        }
    }

    @EventHandler
    public void OnPlayerJoin(PlayerJoinEvent event) {
        try {
            boolean isPlayerOperator = event.getPlayer().isOp();
            if(isPlayerOperator) {
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
        } catch (IOException ex){
            System.out.println("An error occurred.");
            ex.printStackTrace();
        }
    }

    @EventHandler
    public void OnPlayerQuit(PlayerQuitEvent event) {
        try {
            boolean isPlayerOperator = event.getPlayer().isOp();
            if(isPlayerOperator) {
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
        } catch (IOException ex){
            System.out.println("An error occurred.");
            ex.printStackTrace();
        }
    }

    @EventHandler
    public void OnInventoryEvent(InventoryEvent event) {
        String message =  event.getEventName();

        event.getViewers().forEach(viewer -> {
            String playerName = viewer.getName();
            writeLog(playerName, message);
        });
    }

    @EventHandler
    public void OnInventoryOpenEvent(InventoryOpenEvent event) {
        try {
            String playerName = event.getPlayer().getName();
            String message = event.getEventName() + " " + event.getInventory();

            writeLog(playerName, message);
        } catch (Exception ex){
            System.out.println("An error occurred.");
            ex.printStackTrace();
        }
    }
}
