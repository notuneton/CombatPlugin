package org.main.uneton.combatlogger;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.main.uneton.utils.ColorUtils;
import org.main.uneton.utils.ConfigManager;

import static org.main.uneton.utils.MessageHolder.perm;
import static org.main.uneton.utils.MessageHolder.success_bold;
import static org.main.uneton.utils.SoundsUtils.playCancerSound;

public class SetSpawn implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Only players can execute this command!");
            return true;
        }

        if (!player.hasPermission("combat.setspawn.sv")) {
            player.sendMessage(ColorUtils.colorize(perm + command.getName()));
            playCancerSound(player);
            return true;
        }

        if (args.length == 1 && "remove".equals(args[0])) {
            ConfigManager.get().set("spawn-location.world", null);
            ConfigManager.get().set("spawn-location.world", null);
            ConfigManager.get().set("spawn-location.x", null);
            ConfigManager.get().set("spawn-location.y", null);
            ConfigManager.get().set("spawn-location.z", null);
            ConfigManager.get().set("spawn-location.yaw", null);
            ConfigManager.get().set("spawn-location.pitch", null);
            player.sendMessage(ColorUtils.colorize(success_bold + "Successfully erased spawn location!"));
            ConfigManager.save();
            return true;
        }

        Location location = player.getLocation();
        World world = location.getWorld();
        ConfigManager.get().set("spawn-location.world", location.getWorld().getName());
        ConfigManager.get().set("spawn-location.x", location.getX());
        ConfigManager.get().set("spawn-location.y", location.getY());
        ConfigManager.get().set("spawn-location.z", location.getZ());
        ConfigManager.get().set("spawn-location.yaw", location.getYaw());
        ConfigManager.get().set("spawn-location.pitch", location.getPitch());
        if (world != null) {
            world.setSpawnLocation(location.getBlockX(), location.getBlockY(), location.getBlockZ());
        }

        ConfigManager.save();
        player.sendMessage(ColorUtils.colorize(success_bold + " Successfully set the &aspawn&7 to: XYZ: &e" + location.getBlockX() + ", " + location.getBlockY() + ", " + location.getBlockZ()+ "!"));
        return true;
    }
}
