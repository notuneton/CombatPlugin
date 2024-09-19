package org.main.uneton.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.main.uneton.utils.ColorUtils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Blockplayer implements CommandExecutor {

    public static final Map<String, Set<String>> blockedPlayers = new HashMap<>();

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

        String argumentOne = args[0].toLowerCase();
        switch (argumentOne) {
            case "list":
                listBlockedPlayers(player);
                break;

            case "add":
                if (args.length < 2) {
                    String usage = ColorUtils.colorize("&3>&b> &8+ &7usage: &f/block add <player> ");
                    player.sendMessage(usage);
                    return true;
                }
                blockPlayer(player, args[1]);
                break;

            case "remove":
                if (args.length < 2) {
                    String usage = ColorUtils.colorize("&3>&b> &8+ &7usage: &f/block remove <player> ");
                    player.sendMessage(usage);
                    return true;
                }
                unblockPlayer(player, args[1]);
                break;

            default:
                String usage = ColorUtils.colorize("&3>&b> &8+ &7usage: &f/block help ");
                player.sendMessage(usage);
                break;
        }
        return true;
    }

    private void sendHelpMessage(Player player) {
        player.sendMessage(ChatColor.GREEN + "Block Commands: ");
        player.sendMessage(ChatColor.YELLOW + "/block help" + ChatColor.GRAY + " - " + ChatColor.AQUA + " Prints this help message");
        player.sendMessage(ChatColor.YELLOW + "/block list" + ChatColor.GRAY + " - " + ChatColor.AQUA + " List blocked players");
        player.sendMessage(ChatColor.YELLOW + "/block add <player>" + ChatColor.GRAY + " - " + ChatColor.AQUA + " Block a player");
        player.sendMessage(ChatColor.YELLOW + "/block remove <player>" + ChatColor.GRAY + " - " + ChatColor.AQUA + " Unblock a player");
    }

    public static Set<String> getBlockedPlayers(String playerName) {
        return blockedPlayers.getOrDefault(playerName, new HashSet<>());
    }

    private void listBlockedPlayers(Player player) {
        Set<String> blockedSet = getBlockedPlayers(player.getName());

        if (blockedSet.isEmpty()) {
            player.sendMessage(ChatColor.RED + "BLOCKED - 0");
        } else {
            player.sendMessage(ChatColor.GREEN + "Blocked: ");
            for (String blocked : blockedSet) {
                player.sendMessage(ChatColor.YELLOW + "- " + ChatColor.GRAY + blocked);
            }
        }
    }

    private void blockPlayer(Player player, String targetName) {
        Player target = Bukkit.getPlayer(targetName);
        if (target == null || !target.isOnline()) {
            String warn = ColorUtils.colorize("&4>&c> &8+ &7");
            player.sendMessage(warn + "That player does not exist.");
            return;
        }

        String playerName = player.getName();
        blockedPlayers.putIfAbsent(playerName, new HashSet<>());
        Set<String> ignoredSet = blockedPlayers.get(playerName);

        if (ignoredSet.add(targetName)) {
            player.sendMessage(ColorUtils.colorize("&c&lBLOCKED! &7You blocked " + targetName +"!"));
        } else {
            player.sendMessage(ColorUtils.colorize("&cMmh... You already blocked that player!"));
            player.sendMessage(ColorUtils.colorize("&cUse /unblock to remove it."));
        }
    }

    private void unblockPlayer(Player player, String targetName) {
        Set<String> blockdSet = getBlockedPlayers(player.getName());
        if (blockdSet.remove(targetName)) {
            player.sendMessage(ColorUtils.colorize("&a&lUNBLOCKED! &7You unblocked " + targetName +"!"));
        } else {
            player.sendMessage(ColorUtils.colorize("&cThis player isn't blocked!"));
        }
    }

    public static boolean isPlayerBlocked(String playerName, String blockedName) {
        Set<String> blockedNames = blockedPlayers.getOrDefault(playerName, new HashSet<>());
        return blockedNames.contains(blockedName);
    }
}