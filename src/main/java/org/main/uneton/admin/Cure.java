package org.main.uneton.admin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.main.uneton.utils.ColorUtils;

public class Cure implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player = (Player) sender;
        if (!player.hasPermission("combat.heal.sv")) {
            player.sendMessage(ChatColor.RED + "You do not have permission to run /" + command.getName() + ".");
            return true;
        }

        if (args.length == 0) {
            player.sendMessage(ColorUtils.colorize("&aYou healed yourself."));
            player.setHealth(20.0);
            return true;
        }

        if (args.length == 1) {
            Player target = Bukkit.getPlayerExact(args[0]);
            if (target == null || !target.isOnline()) {
                String warn = ColorUtils.colorize("&x&2&C&0&9&1&6&l>&x&5&C&1&2&2&F&l>&x&C&7&5&3&4&7&l> &8+ ");
                player.sendMessage(warn + ColorUtils.colorize("&4That player does not exist."));
                return true;
            }

            target.setHealth(20.0);
            String success = ColorUtils.colorize("&x&2&E&2&E&2&E&l>&x&2&0&8&1&8&A&l>&x&3&6&D&D&E&E&l> &8+");
            target.sendMessage(success + ColorUtils.colorize("&7You have been healed by " + "&6" + player.getName() + "&7."));
            player.sendMessage(success + ColorUtils.colorize("&7You have healed " + "&6" + target.getName() + "&7."));
            return true;
        }
        return false;
    }
}
