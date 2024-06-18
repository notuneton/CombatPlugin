package org.main.uneton.ignore;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

import static org.main.uneton.ignore.Ignore.getIgnoredPlayers;

public class Ignorelist implements CommandExecutor {


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Only players can execute this command!");
            return true;
        }

        Set<String> ignoredSet = getIgnoredPlayers(player.getName());

        if (ignoredSet.isEmpty()) {
            player.sendMessage(ChatColor.YELLOW + "You don't have any ignored players.");
        } else {
            player.sendMessage(ChatColor.YELLOW + "You are ignoring:");
            for (String ignored : ignoredSet) {
                player.sendMessage(ChatColor.YELLOW + "- " + ignored);
            }
        }

        return true;
    }
}
