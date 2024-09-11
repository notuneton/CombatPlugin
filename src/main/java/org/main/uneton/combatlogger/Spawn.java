package org.main.uneton.combatlogger;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.main.uneton.utils.ColorUtils;

import static org.main.uneton.combatlogger.CombatLog.combat_tagged;

public class Spawn implements CommandExecutor {

    private final Plugin plugin;
    public Spawn(Plugin plugin) {
        this.plugin = plugin;
    }

    public static String warned = ColorUtils.colorize("&4>&c> &8+ &7");
    public static String success = ColorUtils.colorize("&3>&b> &8+ &7");

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Only players can execute this command!");
            return true;
        }

        Location initialLocation = player.getLocation();
        BukkitRunnable countdownTask = getBukkitRunnable(player, initialLocation);
        countdownTask.runTaskTimer(plugin, 0L, 20L);
        return true;
    }

    private BukkitRunnable getBukkitRunnable(Player player, Location initialLocation) {
        int countdownSeconds = 5;
        return new BukkitRunnable() {
            private int secondsPassed = 0;
            @Override
            public void run() {
                if (secondsPassed >= countdownSeconds) {
                    this.cancel();
                    if (!teleportPlayer(player, initialLocation)) {
                        player.sendMessage(warned + "Cancelled!");
                    }
                } else {
                    if (player.getLocation().distance(initialLocation) > 1) {
                        player.sendActionBar(warned + "Teleport failed : you were moved!");
                        this.cancel();
                    }
                    player.sendActionBar(ColorUtils.colorize("&7Teleporting in &3" + (countdownSeconds - secondsPassed) + "&7 seconds..."));
                    secondsPassed++;
                }
            }
        };
    }

    private boolean teleportPlayer(Player player, Location initialLocation) {
        if (combat_tagged.containsKey(player)) {
            player.sendMessage(warned + "Teleport failed : you are combat tagged!");
            return false;
        }
        if (player.getLocation().distance(initialLocation) > 1) {
            player.sendMessage(warned + "Teleport failed : you were moved!");
            return false;
        } else {
            Location spawnLoc = plugin.getConfig().getLocation("spawn");
            if (spawnLoc != null) {
                player.teleport(spawnLoc);
                player.sendMessage(success + ColorUtils.colorize("You have been teleported to &a" + "spawn"+"&7!"));
                return true;
            } else {
                player.sendMessage(warned + "Teleport failed : location not found!");
                return false;
            }
        }
    }
}
