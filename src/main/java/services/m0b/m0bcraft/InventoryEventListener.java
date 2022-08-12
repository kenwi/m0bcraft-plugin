package services.m0b.m0bcraft;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

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
    public void OnInventoryMoveItemEvent(InventoryMoveItemEvent event) {
        try {
            Inventory source = event.getInitiator();
            Inventory destination = event.getDestination();

            String message = event.getEventName() + " "
                    + source.getType().name() + " MOVES "
                    + event.getItem().getType().name() + " TO "
                    + destination.getType().name() + " AT "
                    + logService.LocationToString(source.getLocation())
                    + event.isCancelled();

            String itemName = event.getItem().getType().name().toLowerCase();
            if (!itemName.contains("ore")
                    && !itemName.contains("ingot")
                    && !itemName.contains("raw")
                    && !itemName.contains("nugget")
                    && !itemName.contains("bars")
                    && !itemName.contains("diamond block")
                    && !itemName.contains("lapis")
                    && !itemName.contains("diamond block")
                    && !itemName.contains("redstone")
                    && !itemName.contains("emerald")
                    && !itemName.contains("nether")
                    && !itemName.contains("quartz")
                    && !itemName.contains("gold block")) {
                String name = source.getType().name() + "-"
                        + source.getLocation().getBlockX() + "-"
                        + source.getLocation().getBlockY() + "-"
                        + source.getLocation().getBlockZ();
                logService.writeLog(name, message);
                return;
            }

            if (event.getSource().getType() == InventoryType.HOPPER) {
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
                LocalDateTime now = LocalDateTime.now();
                String formattedDate = dtf.format(now);

                String[] lore = new String[]{
                        "",
                        ChatColor.RED + "Tainted by",
                        ChatColor.RED + "mass production",
                        ChatColor.GRAY + formattedDate,
                        ChatColor.GRAY + logService.LocationToString(source.getLocation())
                };

                ItemMeta meta = event.getItem().getItemMeta();
                if (!meta.hasLore()) {
                    meta.setLore(Arrays.asList(lore));
                    event.getItem().setItemMeta(meta);
                    message += " TAINTED";
                }
            }

            String name = source.getType().name() + "-"
                    + source.getLocation().getBlockX() + "-"
                    + source.getLocation().getBlockY() + "-"
                    + source.getLocation().getBlockZ();
            logService.writeLog(name, message);
        } catch (Exception ex) {
            logService.info("An error occurred.");
            ex.printStackTrace();
        }
    }

    @EventHandler
    public void OnInventoryAction(InventoryEvent event) {
        try {
            String message = event.getEventName() + " "
                    + event.getInventory().getType().name();

            writeLog("Inventory", message);
        } catch (Exception ex) {
            logService.info("An error occurred.");
            ex.printStackTrace();
        }
    }

    @EventHandler
    public void OnInventoryOpenEvent(InventoryOpenEvent event) {
        try {
            String playerName = event.getPlayer().getName();

            Inventory inventory = event.getInventory();
            inventory.getType();

            String message = playerName + " "
                    + event.getEventName() + " "
                    + inventory.getType() + " "
                    + logService.LocationToString(inventory.getLocation());

            writeLog(playerName, message);
        } catch (Exception ex) {
            logService.info("An error occurred.");
            ex.printStackTrace();
        }
    }

    @EventHandler
    public void OnInventoryCloseEvent(InventoryCloseEvent event) {
        try {
            String playerName = event.getPlayer().getName();

            Inventory inventory = event.getInventory();
            inventory.getType();

            String message = event.getEventName() + " "
                    + event.getInventory() + " "
                    + inventory.getType() + " "
                    + logService.LocationToString(inventory.getLocation());

            writeLog(playerName, message);
        } catch (Exception ex) {
            logService.info("An error occurred.");
            ex.printStackTrace();
        }
    }
}
