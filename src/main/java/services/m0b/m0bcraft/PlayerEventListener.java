package services.m0b.m0bcraft;

import org.bukkit.advancement.Advancement;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDropItemEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.*;

import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;

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
            logService.info("An error occurred.");
            ex.printStackTrace();
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
            logService.info("An error occurred.");
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
            logService.info("An error occurred.");
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
        } catch (Exception ex){
            logService.info("An error occurred.");
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
            logService.info("An error occurred.");
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
            logService.info("An error occurred.");
            ex.printStackTrace();
        }
    }


}
