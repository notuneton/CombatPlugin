package org.main.uneton.ignore;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Ignore implements CommandExecutor {

    private static final Map<String, Set<String>> ignoredPlayers = new HashMap<>();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Only players can execute this command!");
            return true;
        }

        if (args.length == 0) {
            player.sendMessage(ChatColor.RED + "> /ignore <player>");
            return true;
        }

        String targetName = args[0];
        Player target = Bukkit.getPlayer(targetName);
        if (target == null || !target.isOnline()) {
            player.sendActionBar(ChatColor.DARK_RED + "That user is offline.");
            return true;
        }

        String playerName = player.getName();
        ignoredPlayers.putIfAbsent(playerName, new HashSet<>());
        Set<String> ignoredSet = ignoredPlayers.get(playerName);

        if (ignoredSet.add(targetName)) {
            player.sendMessage(ChatColor.YELLOW + "Successfully ignored player " + targetName);
        } else {
            player.sendMessage(ChatColor.YELLOW + "Player " + targetName + " is already ignored");
        }

        return true;
    }

    private void sendHelpMessage(Player player) {
        player.sendMessage(ChatColor.YELLOW + "Ignore Commands:");
        player.sendMessage(ChatColor.GOLD + "/ignore help" + ChatColor.AQUA + " - Prints this help message");
        player.sendMessage(ChatColor.GOLD + "/ignore list" + ChatColor.AQUA + " - List ignored players");
        player.sendMessage(ChatColor.GOLD + "/ignore add <player>" + ChatColor.AQUA + " - Ignore a player");
        player.sendMessage(ChatColor.GOLD + "/ignore remove <player>" + ChatColor.AQUA + " - Unignore a player");
    }

    public static boolean isPlayerIgnored(String playerName, String ignoredName) {
        return ignoredPlayers.getOrDefault(playerName, new HashSet<>()).contains(ignoredName);
    }

    public static Set<String> getIgnoredPlayers(String playerName) {
        return ignoredPlayers.getOrDefault(playerName, new HashSet<>());
    }
}