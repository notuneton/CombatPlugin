package org.main.uneton.comvanilla;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.jetbrains.annotations.NotNull;
import org.main.uneton.Combat;
import org.main.uneton.utils.ColorUtils;

public class Msg implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Only players can execute this command!");
            return true;
        }

        if (args.length < 2) {
            String usage = ColorUtils.colorize("&3>&b> &8+ &7usage: &f/msg <player> <message> ");
            player.sendMessage(usage);
            return true;
        }

        Player recipient = Bukkit.getPlayer(args[0]);
        if (recipient == null || !recipient.isOnline()) {
            String warn = ColorUtils.colorize("&4>&c> &x&2&E&2&E&2&E&l+ &7");
            player.sendMessage(warn + ColorUtils.colorize("&7That player does not exist."));
            return true;
        }

        StringBuilder msgBuilder = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            msgBuilder.append(args[i]).append(" ");
        }
        String message = msgBuilder.toString().trim();

        player.sendMessage(ColorUtils.colorize("&dTo &7" + recipient.getName() + ": " + message));
        recipient.sendMessage(ColorUtils.colorize("&dFrom &7" + player.getName() + ": " + message));

        player.setMetadata("lastMsg", new FixedMetadataValue(Combat.getInstance(), recipient.getName()));
        recipient.setMetadata("lastMsg", new FixedMetadataValue(Combat.getInstance(), player.getName()));
        return true;
    }
}
