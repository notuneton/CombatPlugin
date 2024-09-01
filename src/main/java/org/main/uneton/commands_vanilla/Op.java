package org.main.uneton.commands_vanilla;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.main.uneton.utils.ColorUtils;

import static org.main.uneton.utils.SoundsUtils.playCancerSound;

public class Op implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Only players can execute this command!");
            return true;
        }

        if (!player.hasPermission("combat.op.sv")) {
            player.sendMessage(ColorUtils.colorize("&c&lCAN'T! &7You do not have permission to run /" + command.getName() + "."));
            playCancerSound(player);
            return true;
        }

        if (args.length == 1) {
            Player target = Bukkit.getPlayer(args[0]);
            if (target == null || !player.isOnline()) {
                String warn = ColorUtils.colorize("&4>&c> &8+ &7");
                player.sendMessage(warn + "That player does not exist.");
                return true;
            }
            target.setOp(true);
            Bukkit.broadcastMessage(ColorUtils.colorize("" +player.getName() + " made " + target.getName() + " server new operator!"));
            player.sendMessage(ColorUtils.colorize("&7&o" + player.getName() +": " + target.getName() + " were made server operator!"));
        }


        return true;
    }
}
