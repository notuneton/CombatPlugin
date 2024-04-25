package org.main.uneton.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Guide implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Only players can execute this command!");
            return true;
        }

        ChatColor b = ChatColor.AQUA;
        ChatColor f = ChatColor.WHITE;
        if(args.length == 0) {
            player.sendMessage(ChatColor.RED + "usage: /guide <info>");
        }

        if(args.length == 1 && "admin".equals(args[0])) {
            player.sendMessage(ChatColor.GRAY.toString() + ChatColor.BOLD + "-------------------------------------");
            player.sendMessage(b + " /crash <player>: " + f + " | Spawns a white ball around you.");
            player.sendMessage(b + " /gm: " + f + " | Toggles god mode for a specified player, making them immune to all forms of damage.");
            player.sendMessage(b + " /heal <player>: " + f + " | Heals the player's hearts.");
            player.sendMessage(b + " /invSee <player>: " + f + " | See and/or edit the inventory of other players.");
            player.sendMessage(b + " /repair <all>: " + f + " | Repairs the durability of one or all items.");
            player.sendMessage(b + " /slippery <player>: " + f + " | Drops Items from the player.");
            player.sendMessage(b + " /sudo <text> <player>: " + f + " | Make another user perform a command.");
            player.sendMessage(b + " /trapCage <player>: " + f + " | Create a cube around the player that the player cannot break.");
            player.sendMessage(ChatColor.GRAY.toString() + ChatColor.BOLD + "-------------------------------------");
        }

        if(args.length == 1 && "info".equals(args[0])) {
            player.sendMessage(ChatColor.GRAY.toString() + ChatColor.BOLD + "-------------------------------------");
            player.sendMessage(b + " /enderChest: " + f + " | Opens your personal Enderchest.");
            player.sendMessage(b + " /guide: " + f + " | Plugin commands.");
            player.sendMessage(b + " /ignore <player>: " + f + " | Ignore a player.");
            player.sendMessage(b + " /ignoreList <player>: " + f + " | Checks all ignored players.");
            player.sendMessage(b + " /unIgnore <player> <list>: " + f + " | Un-ignores a player.");
            player.sendMessage(b + " /playtime <player>: " + f + " | Time that you have played in total on the server.");
            player.sendMessage(b + " /rules: " + f + " | Rules of the Server.");
            player.sendMessage(b + " /stuck: " + f + " | Used if you are stuck.");
            player.sendMessage(b + " /suicide: " + f + " | Causes you to perish.");
            player.sendMessage(b + " /disposal: " + f + " | You can put here items that you want to deleted permanently!");
            player.sendMessage(ChatColor.GRAY.toString() + ChatColor.BOLD + "-------------------------------------");
        }

        return true;
    }
}

