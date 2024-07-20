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
        player.sendActionBar(ColorUtils.colorize("&fPing: " ) + "&b" + String.format("%d ms", playerPing));

        Player target = Bukkit.getServer().getPlayer(args[0]);
        if (args.length == 1) {
            int ping = target.getPing();
            player.sendActionBar(ColorUtils.colorize("&f"+ target.getName()) + "'s ping: " + ChatColor.AQUA + String.format("%d ms", ping));
        } else {
            String warn = ColorUtils.colorize("&4>&c> &x&2&E&2&E&2&E&l- &7");
            player.sendMessage(warn + ColorUtils.colorize("That player does not exist."));
        }

        return true;
    }
}
