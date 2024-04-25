package org.main.uneton.admin;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Trap implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Only players can execute this command!");
            return true;
        }

        if(!player.hasPermission("op")) {
            player.sendMessage(ChatColor.RED + "Permission Denied: You do not have permission to do this task.");
            return true;
        }

        if(args.length == 0){
            player.sendMessage(ChatColor.RED + "usage: /box <player>");
            return true;
        }

        if (args.length == 1) {
            Player target = Bukkit.getPlayerExact(args[0]);
            if(target != null){
                spawnTrap(player);
            }
        }
        return true;
    }

    public static void spawnTrap(Player player){
        Location loc = player.getLocation();
        Location bottomCorner = loc.clone().add(-2, -2, -2); // Muutettu -1:stä -2:ksi, koska haluamme 4x4 kuution

        for (int x = 0; x < 4; x++) { // Muutettu 3:sta 4:ksi
            for (int y = 0; y < 4; y++) { // Tässä on jo 4, joten ei muutosta tarvittu
                for (int z = 0; z < 4; z++) { // Muutettu 3:sta 4:ksi
                    if ((x == 1 || x == 2) && (y == 1 || y == 2) && (z == 1 || z == 2)) { // 2x2 ontto keskellä
                        continue;
                    }
                    if (((y == 0 || y == 3) && z == 1 & x == 1)) { // roof/bottom block
                        bottomCorner.clone().add(x, y, z).getBlock().setType(Material.SPAWNER);
                        // player.addPotionEffect(mining_fatigue);
                    } else { // Walls
                        bottomCorner.clone().add(x, y, z).getBlock().setType(Material.SPAWNER);
                        removeBlocksInsideHollow(player);
                        // player.addPotionEffect(mining_fatigue);

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