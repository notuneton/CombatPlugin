package org.main.uneton.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Ping implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Only players can execute this command!");
            return true;
        }

        int playerPing = player.getPing();
        player.sendActionBar(ChatColor.WHITE +  "ping: " + ChatColor.AQUA + playerPing);
        if (args.length > 0) {
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayerIfCached(args[0]);
            if (offlinePlayer != null && offlinePlayer.hasPlayedBefore()) {
                int ping = player.getPing();
                player.sendActionBar(ChatColor.WHITE + player.getName() + "'s ping: " + ChatColor.AQUA + ping);
            } else {
                player.sendActionBar(ChatColor.RED + "That player does not exist.");
            }
        }

        return true;
    }
}
