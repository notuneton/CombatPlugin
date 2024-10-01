package org.main.uneton.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.main.uneton.utils.ColorUtils;

import java.util.Set;

import static org.main.uneton.commands.Blockplayer.getBlockedPlayers;
import static org.main.uneton.utils.MessageHolder.unblocked;
import static org.main.uneton.utils.MessageHolder.unknown;

public class Unblock implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Only players can execute this command!");
            return true;
        }

        if (args.length == 0) {
            String usage = ColorUtils.colorize("&3>&b> &x&8&8&8&3&A&4- &f/unblock <player> ");
            player.sendMessage(usage);
            return true;
        }

        String targetName = args[0];
        Player target = Bukkit.getPlayerExact(targetName);
        if (target == null || !target.isOnline()) {
            player.sendMessage(unknown);
            return true;
        }

        Set<String> blockSet = getBlockedPlayers(player.getName());
        if (blockSet.remove(targetName)) {
            player.sendMessage(ColorUtils.colorize(unblocked));
        } else {
            player.sendMessage(ColorUtils.colorize("&cThis player isn't blocked!"));
        }

        return true;
    }
}
