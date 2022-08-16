package services.m0b.m0bcraft;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;
import java.util.stream.Stream;

public class ScheduledEventsService {
    LogService logService;
    ApplicationState state;
    JavaPlugin plugin;
    Random rng;

    public ScheduledEventsService(LogService logService, JavaPlugin plugin, ApplicationState state) {
        this.logService = logService;
        this.plugin = plugin;
        this.state = state;
    }

    private boolean isDay() {
        if(rng == null)
            rng = new Random();

        long time = Bukkit.getWorld("world").getTime();
        return time > 0 && time < (12300 -rng.nextInt(1000));
    }

    public void registerEvents() {

        logService.info("Registering location service");
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this.plugin, () -> Bukkit.getOnlinePlayers().forEach(player -> {
            String playerName = player.getDisplayName();
            String location = "Loc: " + logService.LocationToString(player.getLocation());
            logService.writeLog(playerName, location);
        }), 0L, 20 * 10);

        /*Bukkit.getScheduler().scheduleSyncRepeatingTask(this.plugin, () -> {
            if (isDay()) {
                return;
            }

            String output = "Cow density: ";
            Collection<Entity> cows = Bukkit.getWorld("world").getEntitiesByClasses(Cow.class);

            String target = "";
            for (Entity cow : cows) {
                List<Entity> nearby = cow.getNearbyEntities(1, 1, 1);
                int cowDensity = 0;
                for (Entity other : nearby) {
                    if (other.getName().contains("Cow")) {
                        cowDensity++;
                    }
                }
                output += cowDensity;

                if (cowDensity > 10) {
                    //if (new Random().nextDouble() > 0.5)
                    {
                        Cow livingCow = (Cow) cow;
                        //Block target = livingCow.getTargetBlockExact(10);
                        //String targetLocation = logService.LocationToString(target.getLocation());

                        livingCow.damage(0.001, nearby.get(0));
                        //livingCow.playEffect(EntityEffect.HURT);


                        output += "*"; // Tag for damage
                        //output += " " + targetLocation;
                    }
                }

                Mob mob = (Mob)cow;
                Block targetBlock = ((Mob) cow).getTargetBlockExact(10);
                if(targetBlock != null) {
                    target += targetBlock.getType().name() + " ";
                }

                output += " ";
            }
            logService.info(output);
            logService.info(target);

            output = "Sheep density: ";
            Collection<Entity> sheep = Bukkit.getWorld("world").getEntitiesByClasses(Sheep.class);
            for (Entity lamb : sheep) {
                List<Entity> nearby = lamb.getNearbyEntities(1, 1, 2);
                int lambDensity = 0;
                for (Entity other : nearby) {
                    if (other.getName().contains("Sheep")) {
                        lambDensity++;
                    }
                }
                output += lambDensity;

                if (lambDensity > 10) {
                    if (new Random().nextDouble() > 0.5) {
                        Sheep livingSheep = (Sheep) lamb;
                        livingSheep.damage(1, nearby.get(0));
                        livingSheep.playEffect(EntityEffect.HURT);
                        output += "*";
                    }
                }
                output += " ";
            }
            logService.info(output);
        }, 0L, 20 * 10);
        */

        logService.info("Registering discord service");
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this.plugin, () -> {
            try (Stream<Path> paths = Files.walk(Paths.get("mc-inbound"))) {
                paths.filter(Files::isRegularFile).forEach(file -> {
                    try {
                        byte[] bytes = Files.readAllBytes(file.toAbsolutePath());
                        String content = new String(bytes);

                        if (content.contains("Kremfjes")) content = content.replace("Kremfjes", "WaspFigBig");
                        if (content.contains("m0b")) content = content.replace("m0b", "m0b539");

                        String finalContent = content;
                        Bukkit.getOnlinePlayers().forEach(player -> player.sendMessage(finalContent));
                        logService.info("[Discord] " + content);
                        Files.deleteIfExists(file.toAbsolutePath());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });

            } catch (IOException ex) {
                System.out.println("An error occurred.");
                ex.printStackTrace();
            }
        }, 0L, 20);

        logService.info("Started ScheduledEventsService");
    }
}
