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

        if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
            sendHelpMessage(player);
            return true;
        }

        String subCommand = args[0].toLowerCase();

        switch (subCommand) {
            case "list":
                listIgnoredPlayers(player);
                break;
            case "add":
                if (args.length < 2) {
                    player.sendMessage(ChatColor.RED + "> /ignore add <player>");
                    return true;
                }
                ignorePlayer(player, args[1]);
                break;
            case "remove":
                if (args.length < 2) {
                    player.sendMessage(ChatColor.RED + "> /ignore remove <player>");
                    return true;
                }
                unignorePlayer(player, args[1]);
                break;
            case "removeall":
                unignoreAllPlayers(player);
                break;
            default:
                player.sendMessage(ChatColor.RED + "> /ignore help");
                break;
        }

        return true;
    }

    private void sendHelpMessage(Player player) {
        player.sendMessage(ChatColor.GREEN + "Ignore Commands:");
        player.sendMessage(ChatColor.YELLOW + "/ignore help" + ChatColor.GRAY + " - " + ChatColor.AQUA + "Prints this help message");
        player.sendMessage(ChatColor.YELLOW + "/ignore list" + ChatColor.GRAY + " - " + ChatColor.AQUA + "List ignored players");
        player.sendMessage(ChatColor.YELLOW + "/ignore add <player>" + ChatColor.GRAY + " - " + ChatColor.AQUA + "Ignore a player");
        player.sendMessage(ChatColor.YELLOW + "/ignore remove <player>" + ChatColor.GRAY + " - " + ChatColor.AQUA + "Unignore a player");
    }

    private void listIgnoredPlayers(Player player) {
        Set<String> ignoredSet = getIgnoredPlayers(player.getName());

        if (ignoredSet.isEmpty()) {
            player.sendMessage(ChatColor.YELLOW + "You don't have any ignored players.");
        } else {
            player.sendMessage(ChatColor.YELLOW + "You are ignoring:");
            for (String ignored : ignoredSet) {
                player.sendMessage(ChatColor.YELLOW + "- " + ChatColor.GRAY + ignored);
            }
        }
    }

    private void ignorePlayer(Player player, String targetName) {
        Player target = Bukkit.getPlayer(targetName);
        if (target == null || !target.isOnline()) {
            player.sendActionBar(ChatColor.DARK_RED + "That user is offline.");
            return;
        }

        String playerName = player.getName();
        ignoredPlayers.putIfAbsent(playerName, new HashSet<>());
        Set<String> ignoredSet = ignoredPlayers.get(playerName);

        if (ignoredSet.add(targetName)) {
            player.sendMessage(ChatColor.YELLOW + "Successfully ignored player " + targetName);
        } else {
            player.sendMessage(ChatColor.YELLOW + "Player " + targetName + " is already ignored");
        }
    }

    private void unignorePlayer(Player player, String targetName) {
        Set<String> ignoredSet = getIgnoredPlayers(player.getName());
        if (ignoredSet.remove(targetName)) {
            player.sendMessage(ChatColor.YELLOW + "Successfully unignored player " + targetName);
        } else {
            player.sendMessage(ChatColor.RED + "Player " + targetName + " is not ignored");
        }
    }

    private void unignoreAllPlayers(Player player) {
        ignoredPlayers.remove(player.getName());
        player.sendMessage(ChatColor.YELLOW + "Successfully unignored all players.");
    }

    public static boolean isPlayerIgnored(String playerName, String ignoredName) {
        return ignoredPlayers.getOrDefault(playerName, new HashSet<>()).contains(ignoredName);
    }

    public static Set<String> getIgnoredPlayers(String playerName) {
        return ignoredPlayers.getOrDefault(playerName, new HashSet<>());
    }
}