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
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players can execute this command!");
            return true;
        }

        if (args.length > 0) {
            Player player = (Player) sender;
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayerIfCached(args[0]);
            if (offlinePlayer != null && offlinePlayer.hasPlayedBefore()) {
                sendPlaytime(sender, offlinePlayer.getName(), plugin.playTimes.getOrDefault(offlinePlayer.getUniqueId(), 0));
            } else {
                player.sendActionBar(ChatColor.RED + "Player not found.");
            }
        } else {
            Player player2 = (Player) sender;
            sendPlaytime(sender, player2.getName(), plugin.playTimes.getOrDefault(player2.getUniqueId(), 0));
        }
        return true;
    }

    private void sendPlaytime(CommandSender sender, String playerName, int playTime) {
        // Convert playtime to minutes, hours, days, and send a message
        if (playTime >= 60 && playTime <= 3600) {
            sender.sendMessage(String.format(ChatColor.WHITE + "%s'You've Played %d" + ChatColor.GRAY + " m", playTime / 60));
        } else if (playTime <= 86400) {
            sender.sendMessage(String.format(ChatColor.WHITE + "%s'You've Played %.2f" + ChatColor.GRAY + " h", playTime / 3600.0));
        }

        if (playTime <= 86400) {
            sender.sendMessage(ChatColor.RED + "Your playtime is not enough!");
        }
    }
}
