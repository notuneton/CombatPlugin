package org.main.uneton.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.main.uneton.Combat;

public class Playtime implements CommandExecutor {


    private Combat plugin;
    public Playtime(Combat plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Only players can execute this command!");
            return true;
        }

        if(args.length == 0) {
            player.sendMessage(ChatColor.RED + "Usage: /playtime <player>");
        }

        if (args.length > 0) {
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayerIfCached(args[0]);
            if (offlinePlayer != null && offlinePlayer.hasPlayedBefore()) {
                getPlaytime(sender, offlinePlayer.getName(), plugin.playTimes.getOrDefault(offlinePlayer.getUniqueId(), 0));
            } else {
                player.sendActionBar(ChatColor.RED + "That player does not exist.");
            }
        } else {
            Player user = (Player) sender;
            getPlaytime(sender, user.getName(), plugin.playTimes.getOrDefault(user.getUniqueId(), 0));
        }
        return true;
    }

    private void getPlaytime(CommandSender sender, String user, int playTime) {
        ChatColor gray = ChatColor.GRAY;
        ChatColor white = ChatColor.WHITE;
        if (playTime >= 60 && playTime <= 3600) {
            sender.sendMessage(String.format(white + "%s's You have played : %d" + gray + " minutes", user, playTime / 60));
        } else if (playTime <= 86400) {
            sender.sendMessage(String.format(white + "%s's You have played : %.2f" + gray + " hours", user, playTime / 3600.0));
        // } else if (playTime <= 31536000) {
        //    sender.sendMessage(String.format(white + "%s's playtime: %.2f" + gray + " days", playerName, playTime / 86400.0));
        }
    }
}