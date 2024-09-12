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

    private BukkitRunnable getBukkitRunnable(Player player, Location initial_location) {
        int countdownSeconds = 5;
        return new BukkitRunnable() {
            private int secondsPassed = 0;
            @Override
            public void run() {
                if (secondsPassed >= countdownSeconds) {
                    this.cancel();
                    if (!teleportPlayer(player)) {
                        player.sendMessage(ColorUtils.colorize("&x&2&C&0&9&1&6&l>&x&5&C&1&2&2&F&l>&x&C&7&5&3&4&7&l> &7[i] &cTeleportation cancelled."));
                    }
                    return;
                }

                if (player.getLocation().distance(initial_location) > 1) {
                    player.sendMessage(ColorUtils.colorize("&x&2&C&0&9&1&6&l>&x&5&C&1&2&2&F&l>&x&C&7&5&3&4&7&l> &x&2&E&2&E&2&E&l- &cTeleport failed, You were moved!"));
                    this.cancel();
                    return;
                }

                player.sendActionBar(ColorUtils.colorize("&7Teleporting in &3" + (countdownSeconds - secondsPassed) + "&7 seconds..."));
                secondsPassed++;
            }
        };
    }

    public boolean teleportPlayer(Player player) {
        if (combat_tagged.containsKey(player)) {
            player.sendMessage(ColorUtils.colorize("&x&2&C&0&9&1&6&l>&x&5&C&1&2&2&F&l>&x&C&7&5&3&4&7&l> &x&2&E&2&E&2&E&l- &cTeleport failed, You are combat tagged!"));
            return false;
        }

        Location spawnLoc = plugin.getConfig().getLocation("spawn");
        if (spawnLoc != null) {
            player.teleport(spawnLoc);
            player.sendMessage(ColorUtils.colorize("&cYou were spawned in &estart-zone&c!"));
            return true;
        }

        player.sendMessage(ColorUtils.colorize("&x&2&C&0&9&1&6&l>&x&5&C&1&2&2&F&l>&x&C&7&5&3&4&7&l> &x&2&E&2&E&2&E&l- &cLocation not found! Please contact an admin."));
        return false;
    }
}

