package org.main.uneton.comvanilla;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.main.uneton.utils.ColorUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Time implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Only players can execute this command!");
            return true;
        }

        if (!player.hasPermission("combat.time.sv")) {
            player.sendMessage(ChatColor.RED + "You do not have permission to run /" + command.getName() + ".");
            return true;
        }

        if (args.length == 1) {
            World world = player.getWorld();
            String time = args[0].toLowerCase();

            switch (time) {
                case "day":
                    world.setTime(1000);  // Set time to day
                    player.sendMessage(ColorUtils.colorize("&x&5&B&5&B&5&B&l>&x&1&C&7&A&1&7&l>&x&3&3&D&D&2&A&l> &7world's time has been set to &fDay"));
                    break;

                case "night":
                    world.setTime(13000);  // Set time to night
                    player.sendMessage(ColorUtils.colorize("&x&5&B&5&B&5&B&l>&x&1&C&7&A&1&7&l>&x&3&3&D&D&2&A&l> &7world's time has been set to &fNight"));
                    break;

                default:
                    String usage = ColorUtils.colorize("&3>&b> &8+ &7usage: &f/time <day, night>");
                    player.sendMessage(usage);
                    break;
            }
            return true;
        }
        return true;
    }
}
