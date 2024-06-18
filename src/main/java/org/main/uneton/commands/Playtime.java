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

    private final Combat plugin;
    public Playtime(Combat plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Only players can execute this command!");
            return true;
        }

        if (args.length > 0) {
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayerIfCached(args[0]);
            if (offlinePlayer != null && offlinePlayer.hasPlayedBefore()) {
                sendPlaytime(sender, offlinePlayer.getName(), plugin.playTimes.getOrDefault(offlinePlayer.getUniqueId(), 0));
            } else {
                player.sendActionBar(ChatColor.RED + "Player not found.");
            }
        } else {
            Player user = (Player) sender;
            sendPlaytime(sender, user.getName(), plugin.playTimes.getOrDefault(user.getUniqueId(), 0));
        }
        return true;
    }

    private void sendPlaytime(CommandSender sender, String playerName, int playTime) {
        String prefix = ChatColor.WHITE + "'"; // Combine prefix into one string
        String suffix = ChatColor.YELLOW + " minutes";

        if (playTime <= 3600) {
            sender.sendMessage(String.format(prefix + "%s' have played %d" + suffix, playerName, playTime / 60));
        } else if (playTime <= 86400) {
            sender.sendMessage(String.format(prefix + "%s' have played %.2f" + suffix, playerName, playTime / 3600.0));
        }
    }

}
