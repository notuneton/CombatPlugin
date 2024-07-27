package org.main.uneton.admin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.main.uneton.utils.ColorUtils;

public class FlySped implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Only players can execute this command!");
            return true;
        }

        if (!player.hasPermission("combat.flyspeed.sv")) {
            player.sendMessage(ChatColor.RED + "You do not have permission to run /" + command.getName() + ".");
            return true;
        }

        if (args.length == 0) {
            String usage = ColorUtils.colorize("&x&2&C&0&9&1&6&l>&x&5&C&1&2&2&F&l>&x&C&7&5&3&4&7&l> &x&2&E&2&E&2&E&l- &7/flyspeed <speed>");
            player.sendMessage(usage);
            return true;
        }

        Player target = Bukkit.getServer().getPlayer(args[0]);
        String speedArg = args[1];
        float speed = Float.parseFloat(speedArg);

        if (speed < 0 || speed > 10) {
            String warn = ColorUtils.colorize("&x&2&C&0&9&1&6&l>&x&5&C&1&2&2&F&l>&x&C&7&5&3&4&7&l> &x&2&E&2&E&2&E&l- ");
            player.sendMessage(warn +ColorUtils.colorize("&7Speed must be between 0 and 1."));
            return false;
        }

        if (target.isFlying()) {
            target.setFlySpeed(speed);
            String success = ColorUtils.colorize("&x&2&E&2&E&2&E&l>&x&2&0&8&1&8&A&l>&x&3&6&D&D&E&E&l> ");

            player.sendMessage(success +ColorUtils.colorize("&7Fly speed for " + player.getName() + " set to " + speed));
            target.sendMessage(success +ColorUtils.colorize("&7Your fly speed has been set to " + speed + " by " + player.getName()));
        } else {
            String warn = ColorUtils.colorize("&x&2&C&0&9&1&6&l>&x&5&C&1&2&2&F&l>&x&C&7&5&3&4&7&l> &x&2&E&2&E&2&E&l- ");
            player.sendMessage(warn +ColorUtils.colorize("&7" +player.getName() + " must be flying to set fly speed."));
        }

        return true;
    }
}
