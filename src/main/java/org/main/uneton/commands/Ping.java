package org.main.uneton.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
        player.sendActionBar(ColorUtils.colorize("&fPing: " ) + ChatColor.AQUA + String.format("%d ms", playerPing));

        if (args.length == 0) {
            String usage = ColorUtils.colorize("&x&2&C&0&9&1&6&l>&x&5&C&1&2&2&F&l>&x&C&7&5&3&4&7&l> &x&2&E&2&E&2&E&l- &7Usage: &x&A&B&A&B&A&B/&x&A&B&A&B&A&Bp&x&A&B&A&B&A&Bi&x&A&B&A&B&A&Bn&x&A&B&A&B&A&Bg &x&A&B&A&B&A&B<&x&A&B&A&B&A&Bp&x&A&B&A&B&A&Bl&x&A&B&A&B&A&Ba&x&A&B&A&B&A&By&x&A&B&A&B&A&Be&x&A&B&A&B&A&Br&x&A&B&A&B&A&B> &x&A&B&A&B&A&B<&x&A&B&A&B&A&Bc&x&A&B&A&B&A&Bo&x&A&B&A&B&A&Bm&x&A&B&A&B&A&Ba&x&A&B&A&B&A&Bn&x&A&B&A&B&A&Bd&x&A&B&A&B&A&B>\n");
            player.sendMessage(usage);
            return true;
        }

        Player target = Bukkit.getServer().getPlayer(args[0]);
        if (target == null || !target.isOnline()) {
            String warn = ColorUtils.colorize("&x&2&C&0&9&1&6&l>&x&5&C&1&2&2&F&l>&x&C&7&5&3&4&7&l> &x&2&E&2&E&2&E&l- ");
            player.sendMessage(warn + ColorUtils.colorize("&4That player does not exist."));
            return true;
        }

        if (args.length == 1) {
            int ping = target.getPing();
            player.sendActionBar(ColorUtils.colorize("&f"+ target.getName()) + "'s ping: " + ChatColor.AQUA + String.format("%d ms", ping));
        } else {
            String warn = ColorUtils.colorize("&x&2&C&0&9&1&6&l>&x&5&C&1&2&2&F&l>&x&C&7&5&3&4&7&l> &x&2&E&2&E&2&E&l- ");
            player.sendActionBar(warn + ColorUtils.colorize("&4That player does not exist."));
        }

        return true;
    }
}
