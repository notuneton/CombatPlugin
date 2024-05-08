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

        if (args.length > 0) {
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
            if (offlinePlayer.hasPlayedBefore()) {
                getPlaytime(sender, offlinePlayer.getName(), plugin.playTimes.getOrDefault(offlinePlayer.getUniqueId(), 0));
                player.sendActionBar(ChatColor.DARK_RED + "That user hasn't played at all yet.");
                return true;
            } else {
                player.sendActionBar(ChatColor.DARK_RED + "That user is offline.");
                return true;
            }
        } else {
            getPlaytime(sender, player.getName(), plugin.playTimes.getOrDefault(player.getUniqueId(), 0));
            return true;
        }
    }

    private void getPlaytime(CommandSender player, String user, int playTime) {
        ChatColor gray = ChatColor.GRAY;

        if (playTime >= 60 && playTime <= 3600) {
            player.sendMessage(gray +String.format("%s's | played time : %d minute%s", user, playTime / 60, playTime / 60 == 1? "" : "s"));
        } else if (playTime <= 86400) {
            player.sendMessage(gray +String.format("%s's | played time : %.2f hour%s", user, playTime / 3600.0, playTime / 3600.0 == 1? "" : "s"));
        }
        // Additional conditions can be added here for days, weeks, etc., if needed
    }
}
