package org.main.uneton.ignore;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static org.main.uneton.ignore.Ignore.ignoredPlayers;

public class Ignorelist implements CommandExecutor {


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Only players can execute this command!");
            return true;
        }

        if (args.length == 0) {
            player.sendMessage(ChatColor.RED +"usage: /ignorelist <player>");
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
