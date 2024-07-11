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
                        String warn1 = ColorUtils.colorize("&x&2&C&0&9&1&6&l>&x&5&C&1&2&2&F&l>&x&C&7&5&3&4&7&l> &x&2&E&2&E&2&E&l- ");
                        player.sendMessage(warn1 + ChatColor.RED + "Teleportation cancelled.");
                    }
                } else {
                    player.sendActionBar(ChatColor.GRAY + "Teleporting in " + ChatColor.DARK_AQUA + (countdownSeconds - secondsPassed) + ChatColor.GRAY + " seconds.");
                    player.sendMessage(ChatColor.GRAY + "Teleporting in " + ChatColor.DARK_AQUA + (countdownSeconds - secondsPassed) + ChatColor.GRAY + " seconds.");
                    secondsPassed++;
                }
            }
        };
    }

    private boolean teleportPlayer(Player player, Location initialLocation) {
        if (combat_tagged.containsKey(player)) {
            player.sendMessage(ChatColor.RED + "You cannot do this in combat.");
            return false;
        }

        if (player.getLocation().distance(initialLocation) > 0) {
            String warn2 = ColorUtils.colorize("&x&2&C&0&9&1&6&l>&x&5&C&1&2&2&F&l>&x&C&7&5&3&4&7&l> &x&2&E&2&E&2&E&l- ");
            player.sendMessage(warn2 + ChatColor.RED + "Teleport cancelled because you moved!");
            return false;
        } else {
            Location spawnLoc = plugin.getConfig().getLocation("spawn");
            if (spawnLoc != null) {
                player.teleport(spawnLoc);
                String success = ColorUtils.colorize("&x&2&E&2&E&2&E&l>&x&2&0&8&1&8&A&l>&x&3&6&D&D&E&E&l> ");
                player.sendMessage(success + ChatColor.GRAY + "You teleported to " + ChatColor.DARK_AQUA + "spawn" + ChatColor.GRAY +".");
                return true;
            } else {
                String warn3 = ColorUtils.colorize("&x&2&C&0&9&1&6&l>&x&5&C&1&2&2&F&l>&x&C&7&5&3&4&7&l> &x&2&E&2&E&2&E&l- ");
                player.sendMessage(warn3 +ChatColor.RED + "Teleport failed : Spawn location not found.");
                return false;
            }

            //TODO return false; == Teleportation should not proceed
            // return true; == Teleportation proceeded successfully
            // return stops code
        }
    }
}
