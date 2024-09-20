package org.main.uneton.admin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.main.uneton.utils.ColorUtils;

import java.util.HashMap;
import java.util.UUID;

import static org.main.uneton.utils.ConfigManager.deaths;
import static org.main.uneton.utils.ConfigManager.kills;
import static org.main.uneton.utils.SoundsUtils.playCancerSound;

public class Wipe implements CommandExecutor {

    public static HashMap<UUID, Integer> playTimes = new HashMap<>();


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Only players can execute this command!");
            return true;
        }

        if (!player.hasPermission("combat.wipe.sv")) {
            player.sendMessage(ColorUtils.colorize("&c&lCAN'T! &7You do not have permission to run /" + command.getName() + "."));
            playCancerSound(player);
            return true;
        }

        if (args.length == 0) {
            String usage = ColorUtils.colorize("&x&2&C&0&9&1&6&l>&x&5&C&1&2&2&F&l>&x&C&7&5&3&4&7&l> &8+ /wipeprofile <player> ");
            player.sendMessage(usage);
            return true;
        }

        Player target = Bukkit.getServer().getPlayer(args[0]);
        if (target == null || !target.isOnline()) {
            String warn = ColorUtils.colorize("&4>&c> &8+ &7");
            player.sendMessage(warn + "That player does not exist.");
            return true;
        }

        UUID uuid = target.getUniqueId();
        playTimes.remove(uuid);
        kills.remove(uuid);
        deaths.remove(uuid);
        player.sendMessage("\n");
        target.sendMessage(ColorUtils.colorize("&f[IMPORTANT] &cYour uuid has been wiped by &e" + player.getName() + "&c."));
        player.sendMessage("\n");
        player.sendMessage(ColorUtils.colorize("&eSuccessfully wiped the uuid player of &c" + target.getName() + "&e."));
        return true;
    }
}
