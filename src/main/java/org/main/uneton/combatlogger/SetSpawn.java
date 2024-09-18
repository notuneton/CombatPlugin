package org.main.uneton.combatlogger;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.main.uneton.utils.ColorUtils;
import org.main.uneton.utils.ConfigManager;

import static org.main.uneton.utils.SoundsUtils.playCancerSound;

public class SetSpawn implements CommandExecutor {

    private final String success = ColorUtils.colorize("&2>&a> &8+ &7");

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Only players can execute this command!");
            return true;
        }

        if (!player.hasPermission("combat.setspawn.sv")) {
            player.sendMessage(ColorUtils.colorize("&c&lCAN'T! &7You do not have permission to run /" + command.getName() + "."));
            playCancerSound(player);
            return true;
        }

        Location location = player.getLocation();
        ConfigManager.get().set("spawn-location.world", location.getWorld().getName());
        ConfigManager.get().set("spawn-location.x", location.getX());
        ConfigManager.get().set("spawn-location.y", location.getY());
        ConfigManager.get().set("spawn-location.z", location.getZ());
        ConfigManager.get().set("spawn-location.yaw", location.getYaw());
        ConfigManager.get().set("spawn-location.pitch", location.getPitch());
        ConfigManager.save();

        player.sendMessage(success + ColorUtils.colorize("Successfully set the &aspawn&7 to: X " + location.getBlockX() + ", Y: " + location.getBlockY() + ", Z: " + location.getBlockZ()));
        return true;
    }
}
