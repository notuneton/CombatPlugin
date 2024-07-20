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
            String usage = ColorUtils.colorize("&3>&b> &8+ &7Käytä komentoa näin: &f/playtime <player>");
            player.sendMessage(usage);
            return true;
        }

        if (args.length == 1) {
            Player target = Bukkit.getPlayerExact(args[0]);
            if (target == null || !target.isOnline()) {
                String warn = ColorUtils.colorize("&x&2&C&0&9&1&6&l>&x&5&C&1&2&2&F&l>&x&C&7&5&3&4&7&l> &x&2&E&2&E&2&E&l- ");
                player.sendMessage(warn + ColorUtils.colorize("&4That player does not exist."));
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
            sender.sendMessage(ColorUtils.colorize(String.format("&3%s &bolet pelannut &6%d &bminuuttia", playerName, playTime / 60)));
        } else if (playTime <= 86400) {
            sender.sendMessage(ColorUtils.colorize(String.format("&3%s &bhave played &6%.2f &btuntia", playerName, playTime / 3600.0)));
        }
    }
}
