package org.main.uneton.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.main.uneton.Combat;

public class AfkCheckTask extends BukkitRunnable {

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            long lastActivity = ((Combat) Bukkit.getPluginManager().getPlugin("Combat")).getLastActivityTime(player);
            if (System.currentTimeMillis() - lastActivity > 5 * 60 * 1000) {
                ((Combat) Bukkit.getPluginManager().getPlugin("Combat")).kickPlayerForAFK(player);
            }
        }
    }
}
