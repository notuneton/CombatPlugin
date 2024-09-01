package org.main.uneton.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class TeleportUtils {

    public static void teleportPlayerTo(Player player, double x, double y, double z) {
        World world = Bukkit.getWorld("world");
        if (world != null) {
            Location loc = new Location(world, x, y, z);
            player.teleport(loc);

        } else {
            System.out.println("[CombatV3]: this world is not available");
        }
    }
}
