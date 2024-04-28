package org.main.uneton.spawn;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

public class SpawnTp implements CommandExecutor {

    private final Plugin plugin;

    public SpawnTp(Plugin plugin) {
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

        countdownTask.runTaskTimer(plugin, 0L, 20L); // Start immediately, repeat every 20 ticks (1 second)
        return true;
    }

    @NotNull
    private BukkitRunnable getBukkitRunnable(Player player, Location initialLocation) {
        int countdownSeconds = 5;
        return new BukkitRunnable() {
            private int secondsPassed = 0;
            @Override
            public void run() {
                if (secondsPassed >= countdownSeconds) {
                    this.cancel(); // Stop the countdown task
                    if (!teleportPlayer(player, initialLocation)) {
                        player.sendMessage(ChatColor.RED + "Teleportation cancelled.");
                    }
                } else {
                    player.sendMessage(ChatColor.GRAY + "Teleporting in " + ChatColor.DARK_AQUA + (countdownSeconds - secondsPassed) + ChatColor.GRAY + " seconds.");
                    secondsPassed++;
                }
            }
        };
    }

    private boolean teleportPlayer(Player player, Location initialLocation) {
        if (org.main.uneton.events.Combatlogger.combatCooldown.containsKey(player)) {
            player.sendMessage(ChatColor.RED + "You cannot teleport to spawn during combat.");
            return false; // Teleportation should not proceed
        }

        if (player.getLocation().distance(initialLocation) > 0) {
            player.sendMessage(ChatColor.RED + "Teleport cancelled because you moved.");
            return false; // Teleportation should not proceed
        } else {
            Location spawnLocation = plugin.getConfig().getLocation("spawn");
            if (spawnLocation != null) {
                player.teleport(spawnLocation);
                player.sendMessage(ChatColor.GRAY + "You have teleported to the spawn!");
                return true; // Teleportation proceeded successfully
            } else {
                player.sendMessage(ChatColor.RED + "Teleport failed: Spawn location not found.");
                return false; // Teleportation should not proceed
            }
        }
    }
}
