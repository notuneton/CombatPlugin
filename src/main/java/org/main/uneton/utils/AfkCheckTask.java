package org.main.uneton.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.main.uneton.Combat;

public class AfkCheckTask extends BukkitRunnable {

    private static final int AFK_TIME_TICKS = 1200;

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            long lastActivity = ((Combat) Bukkit.getPluginManager().getPlugin("CombatV3")).getLastActivityTime(player);
            long currentTime = System.currentTimeMillis();
            long timeSinceLastActivity = currentTime - lastActivity;

            if (timeSinceLastActivity >= AFK_TIME_TICKS * 50) { // Convert ticks back to milliseconds
                ((Combat) Bukkit.getPluginManager().getPlugin("CombatV3")).kickPlayerForAFK(player);
            }
        }
    }
}
