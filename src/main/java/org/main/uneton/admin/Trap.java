package org.main.uneton.admin;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

public class Trap implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Only players can execute this command!");
            return true;
        }

        if(!player.hasPermission("combat.trap.sv")) {
            player.sendMessage(ChatColor.RED + "Permission Denied: You do not have permission to do this task.");
            return true;
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
            spawnTrap(player);
        }
        return true;
    }

    public static void spawnTrap(Player player) {
        Location loc = player.getLocation();
        Location bottomCorner = loc.clone().add(-1, -1, -1);
        PotionEffect mining_fatigue = new PotionEffect(PotionEffectType.SLOW_DIGGING, 216000, 3);

        // Create the 5x5x5 outer shell with a hollow 3x3x3 inside
        for (int x = 0; x < 5; x++) {
            for (int y = 0; y < 5; y++) {
                for (int z = 0; z < 5; z++) {
                    // Skip the inside space
                    if (x > 0 && x < 4 && y > 0 && y < 4 && z > 0 && z < 4) {
                        continue;
                    }

                    // Set the block type to SPAWNER
                    bottomCorner.clone().add(x, y, z).getBlock().setType(Material.SPAWNER);
                }
            }
        }

        // Apply mining fatigue effect to the player
        Location middleLocation = bottomCorner.clone().add(2, 2, 2);
        player.teleport(middleLocation);

        player.addPotionEffect(mining_fatigue);
        player.sendTitle(ChatColor.RED.toString()+ChatColor.BOLD+ "Trapped!", ChatColor.RED+"You have been trapped in a box!", 10, 70, 20);
    }


    public static void spawnrap(Player player) {
        Location loc = player.getLocation();
        Location bottomCorner = loc.clone().add(-1, -1, -1);
        PotionEffect mining_fatigue = new PotionEffect(PotionEffectType.SLOW_DIGGING, 216000, 3);

        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 4; y++) {
                for (int z = 0; z < 3; z++) {
                    if (((y == 1 || y == 2) && z == 1 & x == 1)) {  // empty space
                        continue;
                    }

                    if ((y == 0 || y == 3) && z == 1 & x == 1) {  // roof/bottom block(s)
                        bottomCorner.clone().add(x, y, z).getBlock().setType(Material.SPAWNER);
                        player.addPotionEffect(mining_fatigue);
                    } else { // Walls
                        bottomCorner.clone().add(x, y, z).getBlock().setType(Material.SPAWNER);
                        player.addPotionEffect(mining_fatigue);
                    }
                }
            }
        }
    }
}