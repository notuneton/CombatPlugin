package org.main.uneton.admin;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.main.uneton.Combat;

public class Trap implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Only players can execute this command!");
            return true;
        }

        if (!player.hasPermission("combat.trap.sv")) {
            player.sendMessage(ChatColor.RED + "Permission Denied: You do not have permission to do this task.");
            return false;
        }

        if (args.length == 0) {
            player.sendMessage(ChatColor.RED + "> /trap <player>");
            return true;
        }

        if (args.length == 1) {
            Player target = Bukkit.getPlayerExact(args[0]);
            if (target == null || !target.isOnline()) {
                player.sendActionBar(ChatColor.DARK_RED + "That user is offline.");
                player.sendMessage(ChatColor.DARK_RED + "That user is offline.");
                return true;
            }

            // if there is a player then spawn ->
            spawnTrap(player);
        }
        return true;
    }

    public void spawnTrap(Player player) {
        Location loc = player.getLocation();
        Location bottomCorner = loc.clone().add(0, 0, 0);
        player.setGameMode(GameMode.ADVENTURE);
        for (int x = 0; x < 5; x++) {
            for (int y = 0; y < 5; y++) {
                for (int z = 0; z < 5; z++) {
                    if (x > 0 && x < 4 && y > 0 && y < 4 && z > 0 && z < 4) {
                        continue;
                    }
                    bottomCorner.clone().add(x, y, z).getBlock().setType(Material.SPAWNER);
                }
            }
        }

        Location middleLocation = bottomCorner.clone().add(2, 2, 2);
        player.teleport(middleLocation);
        player.sendTitle(ChatColor.RED.toString() + ChatColor.BOLD + "Trapped!", ChatColor.RED + "You have been trapped in a box!", 10, 70, 20);
        applyBlindness(player, 10, 1); // Applies blindness for 10 seconds at level 1


        new BukkitRunnable() {
            @Override
            public void run() {
                if (player.isOnline() && !player.isDead()) {
                    player.setGameMode(GameMode.SURVIVAL);
                    player.sendMessage(ChatColor.GREEN + "You have been released from the trap!");
                    removeTrapBox(bottomCorner);
                }
            }
        }.runTaskLater(Combat.getInstance(), 400); // 72000 ticks = 1 hour | 20 ticks = 1 second
    }

    private static void removeTrapBox(Location bottomCorner) {
        for (int x = 0; x < 5; x++) {
            for (int y = 0; y < 5; y++) {
                for (int z = 0; z < 5; z++) {
                    if (x > 0 && x < 4 && y > 0 && y < 4 && z > 0 && z < 4) {
                        continue;
                    }
                    bottomCorner.clone().add(x, y, z).getBlock().setType(Material.AIR);
                }
            }
        }
    }

    public void applyBlindness(Player player, int duration, int amplifier) {
        PotionEffect blindnessEffect = new PotionEffect(PotionEffectType.BLINDNESS, (int) (duration * 20L), amplifier);

        // Apply the effect to the player
        player.addPotionEffect(blindnessEffect);
    }

}

