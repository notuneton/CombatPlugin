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
            String usage = ColorUtils.colorize("&3>&b> &8+ &7usage: &f/playtime <player>");
            player.sendMessage(usage);
            return true;
        }

        if (args.length == 1) {
            Player target = Bukkit.getPlayerExact(args[0]);
            if (target == null || !target.isOnline()) {
                String warn = ColorUtils.colorize("&4>&c> &x&2&E&2&E&2&E&l+ &7");
                player.sendMessage(warn + ColorUtils.colorize("That player does not exist."));
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
        if (playTime <= 3600) {
            sender.sendMessage(ColorUtils.colorize(String.format("&3&l%s &bon pelannut &6%d &bminuuttia!\n", playerName, playTime / 60)));
        } else if (playTime <= 86400) {
            sender.sendMessage(ColorUtils.colorize(String.format("&3&l%s &bon pelannut &6%.2f &btuntia!\n", playerName, playTime / 3600.0)));
        }
    }
}
