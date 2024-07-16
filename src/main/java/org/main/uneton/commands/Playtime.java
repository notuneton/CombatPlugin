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
            String usage = ColorUtils.colorize("&x&2&C&0&9&1&6&l>&x&5&C&1&2&2&F&l>&x&C&7&5&3&4&7&l> &x&2&E&2&E&2&E&l- &7Usage: &x&A&B&A&B&A&B/&x&A&B&A&B&A&Bp&x&A&B&A&B&A&Bl&x&A&B&A&B&A&Ba&x&A&B&A&B&A&By&x&A&B&A&B&A&Bt&x&A&B&A&B&A&Bi&x&A&B&A&B&A&Bm&x&A&B&A&B&A&Be &x&A&B&A&B&A&B<&x&A&B&A&B&A&Bp&x&A&B&A&B&A&Bl&x&A&B&A&B&A&Ba&x&A&B&A&B&A&By&x&A&B&A&B&A&Be&x&A&B&A&B&A&Br&x&A&B&A&B&A&B>");
            player.sendMessage(usage);
            return true;
        }

        if (args.length == 1) {
            Player target = Bukkit.getPlayerExact(args[0]);
            if (target == null || !target.isOnline()) {
                player.sendActionBar(ChatColor.DARK_RED + "That user is offline.");
                return true;
            }

            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayerIfCached(args[0]);
            if (offlinePlayer != null && offlinePlayer.hasPlayedBefore()) {
                sendPlaytime(sender, offlinePlayer.getName(), plugin.playTimes.getOrDefault(offlinePlayer.getUniqueId(), 0));
            } else {
                Player user = (Player) sender;
                sendPlaytime(sender, user.getName(), plugin.playTimes.getOrDefault(user.getUniqueId(), 0));
            }
        }
        return true;
    }

    private void sendPlaytime(CommandSender sender, String playerName, int playTime) {
        ChatColor blue = ChatColor.BLUE;
        if (playTime <= 3600) {
            sender.sendMessage(String.format(blue + "%s have played %d" + blue+"h", playerName, playTime / 60));
        } else if (playTime <= 86400) {
            sender.sendMessage(String.format(blue + "%s have played %.2f" + blue +"m", playerName, playTime / 3600.0));
        }
    }
}
