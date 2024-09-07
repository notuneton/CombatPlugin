package org.main.uneton.vanillaCmds;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.main.uneton.utils.ColorUtils;

import static org.main.uneton.utils.SoundsUtils.playCancerSound;

public class Time implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Only players can execute this command!");
            return true;
        }

        if (!player.hasPermission("combat.time.sv")) {
            player.sendMessage(ColorUtils.colorize("&c&lCAN'T! &7You do not have permission to run /" + command.getName() + "."));
            playCancerSound(player);
            return true;
        }

        if (args.length == 1) {
            World world = player.getWorld();
            String time = args[0].toLowerCase();

            switch (time) {
                case "noon":
                    world.setTime(6000);
                    player.sendMessage(ColorUtils.colorize("&x&5&B&5&B&5&B&l>&x&1&C&7&A&1&7&l>&x&3&3&D&D&2&A&l> &7world's time has been set to &fDay&7. &8&o(6000)"));
                    break;

                case "day":
                    world.setTime(1000);
                    player.sendMessage(ColorUtils.colorize("&x&5&B&5&B&5&B&l>&x&1&C&7&A&1&7&l>&x&3&3&D&D&2&A&l> &7world's time has been set to &fDay&7. &8&o(1000)"));
                    break;

                case "night":
                    world.setTime(13000);
                    player.sendMessage(ColorUtils.colorize("&x&5&B&5&B&5&B&l>&x&1&C&7&A&1&7&l>&x&3&3&D&D&2&A&l> &7world's time has been set to &fNight&7. &8&o(13000)"));
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
