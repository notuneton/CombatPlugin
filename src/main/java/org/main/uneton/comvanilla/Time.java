package org.main.uneton.comvanilla;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.main.uneton.utils.ColorUtils;

import static org.bukkit.Bukkit.getServer;

public class Time implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Only players can execute this command!");
            return true;
        }

        if (!player.hasPermission("combat.day.sv")) {
            player.sendMessage(ChatColor.RED + "You do not have permission to run /" + command.getName() + ".");
            return true;
        }

        if (args.length == 0) {
            String usage = ColorUtils.colorize("&3>&b> &8+ &7usage: &f/time <number>");
            player.sendMessage(usage);
            return true;
        }

        if (args.length == 1) {
            World world = getServer().getWorld("world");
            int time = 0;
            switch (time) {
                case 1000:
                    world.setTime(1000);
                    player.sendMessage(ColorUtils.colorize("&x&5&B&5&B&5&B&l>&x&1&C&7&A&1&7&l>&x&3&3&D&D&2&A&l> &fworld's time has been set to &fDay"));
                    break;
                case 10000:
                    world.setTime(13000);
                    player.sendMessage(ColorUtils.colorize("&x&5&B&5&B&5&B&l>&x&1&C&7&A&1&7&l>&x&3&3&D&D&2&A&l> &fworld's time has been set to &fNight"));
                    break;
            }
        }
        return true;
    }
}
