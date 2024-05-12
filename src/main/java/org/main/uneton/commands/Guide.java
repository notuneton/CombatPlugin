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
            player.sendMessage(ChatColor.RED + "Usage: /guide <help>");
        }

        switch (args.length) {
            case 1:
                if ("combat.*".equals(args[0])) {
                    player.sendMessage(ChatColor.GRAY.toString() + ChatColor.BOLD + "-------------------------------------");
                    player.sendMessage(b + "> /crash <player> " + f + " | Crashes the specified player's game");
                    player.sendMessage(b + "> /heal <player> " + f + " | Heals the player's hearts.");
                    player.sendMessage(b + "> /invsee <player> " + f + " | See the inventory of other players.");
                    player.sendMessage(b + "> /setspawn " + f + " | Sets the server spawn point!");
                    player.sendMessage(b + "> /slippery <player> " + f + " | Drops Items from the player.");
                    player.sendMessage(b + "> /trap <player> " + f + " | Create a cube around the player that the player cannot break.");
                    player.sendMessage(b + "> /sudo <player> <message> " + f + " | Executes another player perform a task.");
                    player.sendMessage(b + "> /freeze <player>" + f + " | Cancels the player's movement.");
                    player.sendMessage(b + "> /gm <player> " + f + " | Makes the player invulnerable to all faults.");
                    player.sendMessage(ChatColor.GRAY.toString() + ChatColor.BOLD + "-------------------------------------");
                    break;
                }

            case 2:
                if ("help".equals(args[0])) {
                    player.sendMessage(ChatColor.GRAY.toString() + ChatColor.BOLD + "-------------------------------------");
                    player.sendMessage(b + "> /ec " + f + " | Opens your personal Enderchest.");
                    player.sendMessage(b + "> /guide <help> " + f + " | Show this list");
                    player.sendMessage(b + "> /kys " + f + " | Kill Yourself.");
                    player.sendMessage(b + "> /repair " + f + " | Repairs the durability of your armor.");
                    player.sendMessage(b + "> /rules " + f + " | Rules of the Server.");
                    player.sendMessage(b + "> /sign " + f + " | Spawns you a sign. (Can be clicked!)");

                    player.sendMessage(b + "> /ignore <player> " + f + " | Ignore a player.");
                    player.sendMessage(b + "> /ignorelist <player> " + f + " | Checks all ignored players.");
                    player.sendMessage(b + "> /unignore <player> " + f + " | Un ignores a player.");
                    player.sendMessage(b + "> /trashcan " + f + " | Items that you want to deleted permanently!");
                    player.sendMessage(ChatColor.GRAY.toString() + ChatColor.BOLD + "-------------------------------------");
                    break;
                }
        }
        return true;
    }
}

