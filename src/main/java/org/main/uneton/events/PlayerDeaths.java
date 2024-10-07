package org.main.uneton.events;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.main.uneton.utils.ColorUtils;

public class PlayerDeaths implements Listener {

    @EventHandler
    @Deprecated
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player victim = event.getPlayer();
        Player attacker = victim.getKiller();
        if (attacker != null && attacker.getType() == EntityType.PLAYER) {

            if (Math.random() < 0.1) {
                event.setDeathMessage(ColorUtils.colorize("&c" + victim.getName() + " took the L to " + attacker.getName()));
            } else if (Math.random() < 0.2) {
                event.setDeathMessage(ColorUtils.colorize("&c" + victim.getName() + " got rekt by " + attacker.getName()));
            } else if (Math.random() < 0.3) {
                event.setDeathMessage(ColorUtils.colorize("&c" + victim.getName() + " was oinked down by " + attacker.getName()));
            } else if (Math.random() < 0.4) {
                event.setDeathMessage(ColorUtils.colorize("&c" + victim.getName() + " was hunted down by " + attacker.getName()));
            } else if (Math.random() < 0.5) {
                event.setDeathMessage(ColorUtils.colorize("&c" + victim.getName() + " got clapped by " + attacker.getName()));
            } else if (Math.random() < 0.6) {
                event.setDeathMessage(ColorUtils.colorize("&c" + victim.getName() + " got eliminated by " + attacker.getName()));
            } else if (Math.random() < 0.7) {
                event.setDeathMessage(ColorUtils.colorize("&c" + victim.getName() + " was destroyed by " + attacker.getName()));
            } else if (Math.random() < 0.8) {
                event.setDeathMessage(ColorUtils.colorize("&c" + victim.getName() + " was yeeted by " + attacker.getName()));
            } else if (Math.random() < 0.9) {
                event.setDeathMessage(ColorUtils.colorize("&c" + victim.getName() + " got jumped by " + attacker.getName()));
            }
        }
    }
}
