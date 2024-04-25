package org.main.uneton.admin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Particle;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Crash implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Only players can execute this command!");
            return true;
        }

        if(!player.hasPermission("combat.crash.use")) {
            sender.sendMessage(ChatColor.RED + "You do not have access to this command!");
            return true;
        }

        if (args.length == 0) {
            player.sendMessage(ChatColor.RED + "usage: /crash <player>");
            return true;
        }

        if (args.length > 0) {
            Player target = Bukkit.getPlayerExact(args[0]);
            if (target == null) {
                player.sendActionBar(ChatColor.RED + "That player does not exist.");
                return true;
            }

            // Crash their game
            player.spawnParticle(Particle.WHITE_ASH, player.getLocation(), Integer.MAX_VALUE); // The max value of int : 2147483647
        }
        return true;
    }
}