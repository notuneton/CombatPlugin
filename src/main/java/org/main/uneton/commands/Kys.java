package org.main.uneton.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.main.uneton.utils.ColorUtils;

public class Kys implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Only players can execute this command!");
            return true;
        }

        player.setHealth(0.0);
        player.sendMessage(ColorUtils.colorize("&x&7&3&1&7&3&BY&x&7&4&2&3&4&2o&x&7&6&2&F&4&9u &x&7&7&3&C&5&0t&x&7&9&4&8&5&7o&x&7&A&5&4&5&Eo&x&7&C&6&0&6&5k &x&7&D&6&C&6&Ct&x&7&F&7&9&7&3h&x&8&0&8&5&7&Ae &x&8&2&9&1&8&0e&x&8&3&9&D&8&7a&x&8&5&A&A&8&Es&x&8&6&B&6&9&5y &x&8&8&C&2&9&Cw&x&8&9&C&E&A&3a&x&8&B&D&A&A&Ay &x&8&C&E&7&B&1o&x&8&E&F&3&B&8u&x&8&F&F&F&B&Ft"));
        return true;
    }
}