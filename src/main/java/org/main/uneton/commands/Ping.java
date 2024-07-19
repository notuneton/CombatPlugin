package org.main.uneton.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.main.uneton.utils.ColorUtils;

public class Ping implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player = (Player) sender;
        int playerPing = player.getPing();
        player.sendActionBar(ColorUtils.colorize("&fPing: " ) + ChatColor.AQUA + String.format("%dms", playerPing));
        if (args.length > 0) {
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayerIfCached(args[0]);
            if (offlinePlayer != null && offlinePlayer.hasPlayedBefore()) {
                int ping = player.getPing();
                player.sendActionBar(ColorUtils.colorize("&f"+ player.getName()) + "'s ping: " + ChatColor.AQUA + String.format("%dms", ping));
            } else {
                String warn = ColorUtils.colorize("&x&2&C&0&9&1&6&l>&x&5&C&1&2&2&F&l>&x&C&7&5&3&4&7&l> &x&2&E&2&E&2&E&l- ");
                player.sendActionBar(warn + ColorUtils.colorize("&4That player does not exist."));
            }
        }

        return true;
    }
}
