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
import org.main.uneton.utils.ColorUtils;

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

        if (args.length == 0) {
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayerIfCached(args[0]);
            if (offlinePlayer != null && offlinePlayer.hasPlayedBefore()) {
                sendPlaytime(sender, offlinePlayer.getName(), plugin.playTimes.getOrDefault(offlinePlayer.getUniqueId(), 0));
            } else {
                player.sendActionBar(ColorUtils.colorize("&x&2&C&0&9&1&6&l>&x&5&C&1&2&2&F&l>&x&C&7&5&3&4&7&l> " + ChatColor.RED + "Player not found."));
            }
        } else {
            Player user = (Player) sender;
            sendPlaytime(sender, user.getName(), plugin.playTimes.getOrDefault(user.getUniqueId(), 0));
        }
        return true;
    }

    private void sendPlaytime(CommandSender sender, String playerName, int playTime) {
        String white = ChatColor.WHITE + "h";
        String blue = ChatColor.BLUE + "m";

        if (playTime <= 3600) {
            sender.sendMessage(String.format(white + "%s' have played %d" + blue, playerName, playTime / 60));
        } else if (playTime <= 86400) {
            sender.sendMessage(String.format(white + "%s' have played %.2f" + blue, playerName, playTime / 3600.0));
        }
    }
}
