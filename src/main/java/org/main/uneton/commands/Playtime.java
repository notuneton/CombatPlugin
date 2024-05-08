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
        Player player = (Player) sender;
        if (args.length == 1) {
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayerIfCached(args[0]);
            if (offlinePlayer != null && offlinePlayer.hasPlayedBefore()) {
                getPlaytime(sender, offlinePlayer.getName(), plugin.playTimes.getOrDefault(offlinePlayer.getUniqueId(), 0));
                player.sendActionBar(ChatColor.DARK_RED + "That user hasn't played at all yet.");

            } else {
                player.sendActionBar(ChatColor.DARK_RED + "That user is offline.");
            }

        } else {
            Player user = (Player) sender;
            getPlaytime(sender, user.getName(), plugin.playTimes.getOrDefault(user.getUniqueId(), 0));

        }
        return true;
    }


    private void getPlaytime(CommandSender player, String user, int playTime) {
        ChatColor gray = ChatColor.GRAY;
        ChatColor aqua = ChatColor.AQUA;

        if (playTime >= 60 && playTime <= 3600) {
            player.sendMessage(String.format(gray + "%s's  | Time played : %d" + aqua , playTime / 60 + " minutes"));

        } else if (playTime <= 86400) {
            player.sendMessage(String.format(gray + "%s's  | Time played : %.2f" + aqua, user, playTime / 3600.0 + " hours"));


            // } else if (playTime <= 31536000) {
            // sender.sendMessage(String.format(white + "%s's playtime: %.2f" + gray + " days", playerName, playTime / 86400.0));
        }
    }
}