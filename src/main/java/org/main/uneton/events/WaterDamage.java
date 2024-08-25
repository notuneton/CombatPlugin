package org.main.uneton.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.main.uneton.Combat;

public class WaterDamage implements Listener {

    private static final double MIN_HEALTH = 0.0;
    private static final double DAMAGE = 1.0;
    private boolean isRunning = false;

    @EventHandler
    public void onWater(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        if (player.isInWater() && !isRunning) {
            isRunning = true;

            new BukkitRunnable() {

                @Override
                public void run() {
                    if (!player.isInWater()) {
                        isRunning = false;
                        cancel();
                        return;
                    }

                    double playerHealth = player.getHealth();
                    if (playerHealth <= MIN_HEALTH) {
                        isRunning = false;
                        player.setHealth(MIN_HEALTH);
                        cancel();
                    } else {
                        double newHealth = playerHealth - DAMAGE;
                        player.setHealth(Math.max(newHealth, MIN_HEALTH));

                        if (newHealth <= MIN_HEALTH) {
                            isRunning = false;
                            cancel();
                        }
                    }
                }
            }.runTaskTimer(Combat.getInstance(), 0L, 5L);
        }
    }
}
