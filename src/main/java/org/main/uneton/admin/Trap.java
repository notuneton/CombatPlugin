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

        if(!player.hasPermission("combat.trapcage.use")) {
            player.sendMessage(ChatColor.RED + "Permission Denied: You do not have permission to do this task.");
            return true;
        }

        if(args.length == 0){
            player.sendMessage(ChatColor.RED + "usage: /trapcage <player>");
            return true;
        }

        if (args.length == 1) {
            Player target = Bukkit.getPlayerExact(args[0]);
            if (target == null || !target.isOnline()) {
                player.sendActionBar(ChatColor.RED + "That player does not exist.");
                return true;
            }

            spawnTrap(player);
        }
        return true;
    }

    public static void spawnTrap(Player player){
        Location loc = player.getLocation();
        Location bottomCorner = loc.clone().add(-1, -1, -1);
        PotionEffect mining_fatigue = new PotionEffect(PotionEffectType.SLOW_DIGGING, 72000, 3); // 259,200 seconds

        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 4; y++) {
                for (int z = 0; z < 3; z++) {
                    if (((y == 1 || y == 2) && z == 1 & x == 1)) {  // empty space
                        continue;
                    }
                    if (((y == 0 || y == 3) && z == 1 & x == 1)) {  // roof/bottom block
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

    public static void removeBlocksInsideHollow(Player player){
        Location loc = player.getLocation();
        Location bottomCorner = loc.clone().add(-2, -2, -2); // 4x4 kuution alakulma

        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                for (int z = 0; z < 4; z++) {
                    if ((x == 1 || x == 2) && (y == 1 || y == 2) && (z == 1 || z == 2)) { // 2x2 ontto keskellä
                        bottomCorner.clone().add(x, y, z).getBlock().setType(Material.AIR); // Poistetaan palikat
                    }
                }
            }
        }
    }
}