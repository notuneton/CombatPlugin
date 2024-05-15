package org.main.uneton.otherPackets;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static org.main.uneton.otherPackets.Ignore.ignoredPlayers;

public class Ignorelist implements CommandExecutor {


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Only players can execute this command!");
            return true;
        }

        if (args.length == 0) {
            player.sendMessage(ChatColor.RED + "Usage: /ignorelist <player>");
            return true;
        }

        Player target = Bukkit.getPlayerExact(args[0]);
        if (target == null || !target.isOnline()) {
            player.sendActionBar(ChatColor.DARK_RED + "That user is offline.");
            return true;
        }

        if(ignoredPlayers.isEmpty()){
            player.sendMessage(ChatColor.YELLOW + "You don't have any ignored player");
        } else {
            player.sendMessage(ChatColor.YELLOW + "You are ignoring: ");
            for (String user : ignoredPlayers){
                player.sendMessage(ChatColor.YELLOW + "- " + user);
            }
        }

        return true;
    }
}
