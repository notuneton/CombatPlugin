package org.main.uneton.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.main.uneton.utils.ColorUtils;

public class Rules implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Only players can execute this command!");
            return true;
        }

        if (args.length == 1) {
            String usage = ColorUtils.colorize("&3>&b> &8+ &7usage: &f/rules ");
            player.sendMessage(usage);
            return true;
        }

        String message = ChatColor.GOLD + "\nThere are no rules. Are you some kind of retard?\n";
        player.kickPlayer(message);
        return true;
    }
}
