package org.main.uneton.admin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Heal implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Only players can execute this command!");
            return true;
        }

        if(!player.hasPermission("combat.heal.sv")) {
            return false;
        }

        if (args.length == 0) {
            Player target = Bukkit.getPlayerExact(args[0]);
            if (target == null) {
                player.sendMessage(ChatColor.GREEN + "You healed yourself.");
                player.setHealth(20);
                return true;
            }
        }

        if (args.length == 1) {
            Player target = Bukkit.getPlayerExact(args[0]);
            if (target == null || !target.isOnline()) {
                player.sendActionBar(ChatColor.DARK_RED + "That user is offline.");
                player.sendMessage(ChatColor.DARK_RED + "That user is offline.");
                return true;
            }

            player.sendMessage(ChatColor.GREEN + "You have been healed by "+ ChatColor.GOLD + sender.getName());
            player.setHealth(20);
        }


        return true;
    }
}
