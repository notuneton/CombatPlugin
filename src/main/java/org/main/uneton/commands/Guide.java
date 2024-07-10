package org.main.uneton.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.main.uneton.utils.ColorUtils;

public class Guide implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Only players can execute this command!");
            return true;
        }
        switch (args.length) {
            case 1:
                if ("komennot".equals(args[0]) || "k".equals(args[0])) {
                    player.sendMessage(ChatColor.GRAY.toString() + ChatColor.BOLD + "-------------------------------------");
                    player.sendMessage(ColorUtils.colorize("&x&2&E&2&E&2&E&l>&x&1&8&8&D&2&7&l>&x&2&8&E&E&4&2&l> &8[i] &7/spawn | Teleport to the spawnpoint."));
                    player.sendMessage(ColorUtils.colorize("&x&2&E&2&E&2&E&l>&x&1&8&8&D&2&7&l>&x&2&8&E&E&4&2&l> &8[i] &7/ec | Opens your personal Enderchest."));
                    player.sendMessage(ColorUtils.colorize("&x&2&E&2&E&2&E&l>&x&1&8&8&D&2&7&l>&x&2&8&E&E&4&2&l> &8[i] &7/guide <text> | Show this list"));
                    player.sendMessage(ColorUtils.colorize("&x&2&E&2&E&2&E&l>&x&1&8&8&D&2&7&l>&x&2&8&E&E&4&2&l> &8[i] &7/kys | Kill Yourself."));
                    player.sendMessage(ColorUtils.colorize("&x&2&E&2&E&2&E&l>&x&1&8&8&D&2&7&l>&x&2&8&E&E&4&2&l> &8[i] &7/ping <player> | Check player ping."));
                    player.sendMessage(ColorUtils.colorize("&x&2&E&2&E&2&E&l>&x&1&8&8&D&2&7&l>&x&2&8&E&E&4&2&l> &8[i] &7/puu | Spawns an oak tree at the player's location."));
                    player.sendMessage(ColorUtils.colorize("&x&2&E&2&E&2&E&l>&x&1&8&8&D&2&7&l>&x&2&8&E&E&4&2&l> &8[i] &7/repair | Repairs the durability of your armor."));
                    player.sendMessage(ColorUtils.colorize("&x&2&E&2&E&2&E&l>&x&1&8&8&D&2&7&l>&x&2&8&E&E&4&2&l> &8[i] &7/rules | Rules of the Server."));
                    player.sendMessage(ColorUtils.colorize("&x&2&E&2&E&2&E&l>&x&1&8&8&D&2&7&l>&x&2&8&E&E&4&2&l> &8[i] &7/sign | Spawns you a sign. (Can be clicked!)"));
                    player.sendMessage(ColorUtils.colorize("&x&2&E&2&E&2&E&l>&x&1&8&8&D&2&7&l>&x&2&8&E&E&4&2&l> &8[i] &7/trashcan | Put your stuff in the menu and close it!"));
                    player.sendMessage(ChatColor.GRAY.toString() + ChatColor.BOLD + "-------------------------------------");
                    break;
                }

            case 2:
                //todo Only if they have permission it can execute this
                if ("admin".equals(args[0])) {
                    if (player.hasPermission("combat.view.commands")) {
                        player.sendMessage(ChatColor.GRAY.toString() + ChatColor.BOLD + "-------------------------------------");
                        player.sendMessage(ColorUtils.colorize("&x&2&E&2&E&2&E&l>&x&1&8&8&D&2&7&l>&x&2&8&E&E&4&2&l> &8[i] &7/crash <player> | Crashes the specified player's game"));
                        player.sendMessage(ColorUtils.colorize("&x&2&E&2&E&2&E&l>&x&1&8&8&D&2&7&l>&x&2&8&E&E&4&2&l> &8[i] &7/heal <player> | Heals the player's hearts."));
                        player.sendMessage(ColorUtils.colorize("&x&2&E&2&E&2&E&l>&x&1&8&8&D&2&7&l>&x&2&8&E&E&4&2&l> &8[i] &7/invsee <player> | See the inventory of other players."));
                        player.sendMessage(ColorUtils.colorize("&x&2&E&2&E&2&E&l>&x&1&8&8&D&2&7&l>&x&2&8&E&E&4&2&l> &8[i] &7/setspawn | Sets the server spawn point!"));
                        player.sendMessage(ColorUtils.colorize("&x&2&E&2&E&2&E&l>&x&1&8&8&D&2&7&l>&x&2&8&E&E&4&2&l> &8[i] &7/slippery <player> | Drops Items from the player."));
                        player.sendMessage(ColorUtils.colorize("&x&2&E&2&E&2&E&l>&x&1&8&8&D&2&7&l>&x&2&8&E&E&4&2&l> &8[i] &7/trap <player> | Create a cube around the player that the player cannot break."));
                        player.sendMessage(ColorUtils.colorize("&x&2&E&2&E&2&E&l>&x&1&8&8&D&2&7&l>&x&2&8&E&E&4&2&l> &8[i] &7/sudo <target> <message> | Executes another player perform a task."));
                        player.sendMessage(ColorUtils.colorize("&x&2&E&2&E&2&E&l>&x&1&8&8&D&2&7&l>&x&2&8&E&E&4&2&l> &8[i] &7/freeze <player> | Cancels the player's movement."));
                        player.sendMessage(ColorUtils.colorize("&x&2&E&2&E&2&E&l>&x&1&8&8&D&2&7&l>&x&2&8&E&E&4&2&l> &8[i] &7/gm <player> | Makes the player invulnerable to all faults."));
                        player.sendMessage(ChatColor.GRAY.toString() + ChatColor.BOLD + "-------------------------------------");
                        break;
                    }
                }

            default:
                player.sendMessage(ChatColor.GRAY.toString() + ChatColor.BOLD + "-------------------------------------");
                player.sendMessage(ChatColor.RED + "Error: " + ChatColor.DARK_RED + "No valid argument found! Try /guide <komennot>");
                player.sendMessage(ChatColor.GRAY.toString() + ChatColor.BOLD + "-------------------------------------");
                break;
        }
        return true;
    }
}
