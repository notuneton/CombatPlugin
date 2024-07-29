package org.main.uneton.admin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.main.uneton.Combat;
import org.main.uneton.utils.ColorUtils;

import java.util.UUID;

import static org.main.uneton.commands.Playtime.getPlaytime;

public class Wipeprofile implements CommandExecutor {

    private final Combat plugin;
    public Wipeprofile(Combat plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Only players can execute this command!");
            return true;
        }

        if (!player.hasPermission("combat.wipe.sv")) {
            player.sendMessage(ChatColor.RED + "You do not have permission to run " + command.getName() + ".");
            return true;
        }

        if (args.length == 0) {
            String usage = ColorUtils.colorize("&x&2&C&0&9&1&6&l>&x&5&C&1&2&2&F&l>&x&C&7&5&3&4&7&l> &x&2&E&2&E&2&E&l+ &x&A&B&A&B&A&B/wipeprofile");
            player.sendMessage(usage);
            return true;
        }

        if (args.length == 1) {
            Player target = Bukkit.getPlayerExact(args[0]);
            if (target == null || !target.isOnline()) {
                String warn = ColorUtils.colorize("&x&2&C&0&9&1&6&l>&x&5&C&1&2&2&F&l>&x&C&7&5&3&4&7&l> &x&2&E&2&E&2&E&l+ ");
                player.sendMessage(warn + ColorUtils.colorize("&7That player does not exist."));
                return true;
            }

            //todo remove all playtime
            UUID playerUUID = player.getUniqueId();
            getPlaytime(player, player.getName(), plugin.playTimes.getOrDefault(playerUUID, 0));
            player.sendMessage(ColorUtils.colorize("&aProfile successfully wiped!"));
        }

        return true;
    }

}
