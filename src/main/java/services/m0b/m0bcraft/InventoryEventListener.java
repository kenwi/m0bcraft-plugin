package services.m0b.m0bcraft;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

public class InventoryEventListener implements Listener {
    LogService logService;
    JavaPlugin plugin;
    HashMap<String, Integer> itemCounter;

    public InventoryEventListener(LogService logService, JavaPlugin plugin, HashMap<String, Integer> counter) {
        this.logService = logService;
        this.plugin = plugin;
        this.itemCounter = counter;

        logService.info("Listening to InventoryEvents");
        itemCounter = new HashMap<>();

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this.plugin, () -> {
            int count = itemCounter.size();
            if(count > 0) {
                itemCounter.clear();
                logService.writeLog("HOPPER", "Counters cleared. Hoppers: " + count);
            }
        }, 0L, 20 * 60 * 30);

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
                    + logService.LocationToString(source.getLocation());

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

                String location = logService.LocationToString(source.getLocation())
                        .replace(" ", "-");
                String name = source.getType().name() + " " + location;

                logService.writeLog(name, message);
                return;
            }

            if (event.getSource().getType() == InventoryType.HOPPER) {
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss.SSS");
                LocalDateTime now = LocalDateTime.now();
                String formattedDate = dtf.format(now);

                String[] lore = new String[]{
                        "",
                        ChatColor.RED + "Tainted by",
                        ChatColor.RED + "mass production ;)",
                        ChatColor.GRAY + formattedDate,
                        ChatColor.GRAY + logService.LocationToString(source.getLocation())
                };

                String location = logService.LocationToString(source.getLocation())
                        .replace(" ", "-");
                String itemKey = itemName + "-" + location;
                if(!itemCounter.containsKey(itemKey))
                    itemCounter.put(itemKey, 0);

                int tainted = itemCounter.get(itemKey);
                int oneStack = 64;
                int numberOfStacks = 2; // 128
                int replaceDestinationLimit = 2; // 256
                int replaceSourceLimit = 2; // 512

                message += " Count: " + ++tainted
                        + " Limits: "
                        + "[" + oneStack * numberOfStacks + "] "
                        + "[" + oneStack * numberOfStacks * replaceDestinationLimit + "] "
                        + "[" + oneStack * numberOfStacks * replaceDestinationLimit * replaceSourceLimit + "]";

                itemCounter.put(itemKey, tainted);

                ItemMeta meta = event.getItem().getItemMeta();
                if (!meta.hasLore() && tainted > oneStack * numberOfStacks && destination.getType() == InventoryType.CHEST) {
                    if(new Random().nextDouble() < 0.80)
                    {
                        meta.setLore(Arrays.asList(lore));
                        event.getItem().setItemMeta(meta);
                        message += " TAINTED";
                        logService.writeLog(itemName, message);
                    }

                    if(tainted > oneStack * numberOfStacks * replaceDestinationLimit) {
                        Location blockLocation = event.getDestination().getLocation();
                        Block block = Bukkit.getWorld("world").getBlockAt(blockLocation);
                        block.setType(Material.VOID_AIR);

                        String blockType = block.getType().name();
                        message += " REPLACED " + blockType;
                    }

                    if(tainted > oneStack * numberOfStacks * replaceDestinationLimit * replaceSourceLimit) {
                        Location blockLocation = event.getSource().getLocation();;
                        Block block = Bukkit.getWorld("world").getBlockAt(blockLocation);
                        block.setType(Material.VOID_AIR);

                        String blockType = block.getType().name();
                        message += " REPLACED " + blockType;
                    }
                }
            }

            String location = logService.LocationToString(source.getLocation())
                    .replace(" ", "-");
            String name = source.getType().name() + "-" + location;
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

            logService.writeLog("Inventory", message);
        } catch (Exception ex) {
            logService.info("An error occurred.");
            ex.printStackTrace();
        }
    }

    @EventHandler
    public void OnInventoryOpenEvent(InventoryOpenEvent event) {
        try {
            String name = event.getPlayer().getName();

            Inventory inventory = event.getInventory();
            inventory.getType();

            String message = event.getEventName() + " "
                    + inventory.getType() + " "
                    + logService.LocationToString(inventory.getLocation());

            logService.writeLog(name, message);
        } catch (Exception ex) {
            logService.info("An error occurred.");
            ex.printStackTrace();
        }
    }

    @EventHandler
    public void OnInventoryCloseEvent(InventoryCloseEvent event) {
        try {
            String name = event.getPlayer().getName();

            Inventory inventory = event.getInventory();
            inventory.getType();

            String message = event.getEventName() + " "
                    + inventory.getType() + " "
                    + logService.LocationToString(inventory.getLocation());

            logService.writeLog(name, message);
        } catch (Exception ex) {
            logService.info("An error occurred.");
            ex.printStackTrace();
        }
    }

    @EventHandler
    public void OnInventoryInteractEvent(InventoryInteractEvent event) {
        try {
            String name = event.getInventory().getType().name();
            String message = event.getEventName() + " ";

            logService.writeLog(name, message);
        } catch (Exception ex) {
            logService.info("An error occurred.");
            ex.printStackTrace();
        }
    }

    @EventHandler
    public void OnInventoryDragEvent(InventoryDragEvent event) {
        try {
            String name = event.getWhoClicked().getName();
            String message = event.getEventName() + " "
                    + event.getInventory().getType().name();

            logService.writeLog(name, message);
        } catch (Exception ex) {
            logService.info("An error occurred.");
            ex.printStackTrace();
        }
    }
}
