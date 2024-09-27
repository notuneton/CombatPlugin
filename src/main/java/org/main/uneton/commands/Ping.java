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
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Only players can execute this command!");
            return true;
        }

        if (args.length == 0) {
            String usage = ColorUtils.colorize("&3>&b> &7usage: &f/ping <player>");
            player.sendMessage(usage);
            return true;
        }

        if (args.length == 1) {
            Player target = Bukkit.getServer().getPlayer(args[0]);
            if (target == null || !target.isOnline()) {
                player.sendMessage(ColorUtils.colorize("&c&lWHO?! &7Couldn't find a player with username "+ target.getName() +"!"));
                return true;
            }
            int ping = target.getPing();
            player.sendActionBar(ColorUtils.colorize("&f" + target.getName()) + "'s ping: " + ChatColor.AQUA + String.format("%dms", ping));
        }
        return true;
    }
}
