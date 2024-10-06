package org.main.uneton.vanillaCmds;

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
            String usage = ColorUtils.colorize("&3>&b> &x&8&8&8&3&A&4- &f/msg <player> <message> ");
            player.sendMessage(usage);
            return true;
        }

        Player recipient = Bukkit.getPlayer(args[0]);
        if (recipient == null) {
            if (!recipient.isOnline()) {
                player.sendMessage(ColorUtils.colorize("&x&2&C&0&9&1&6&l>&x&5&C&1&2&2&F&l>&x&C&7&5&3&4&7&l> &cYou cannot message this player!"));
            }
            player.sendMessage(ColorUtils.colorize("&c&lWHO?! &7Couldn't find a player with username " + recipient.getName() +"!"));
            return true;
        }

        StringBuilder msgBuilder = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            msgBuilder.append(args[i]).append(" ");
        }

        String message = msgBuilder.toString().trim();
        player.sendMessage(ColorUtils.colorize("&x&0&0&0&9&2&E&l>&x&2&1&4&0&6&9&l>&x&6&7&A&6&C&E&l> &8? &dTo &7" + recipient.getName() + ": " + message));
        recipient.sendMessage(ColorUtils.colorize("&x&0&0&0&9&2&E&l>&x&2&1&4&0&6&9&l>&x&6&7&A&6&C&E&l> &8? &dFrom &7" + player.getName() + ": " + message));

        player.setMetadata("lastMsg", new FixedMetadataValue(Combat.getInstance(), recipient.getName()));
        recipient.setMetadata("lastMsg", new FixedMetadataValue(Combat.getInstance(), player.getName()));
        return true;
    }
}
