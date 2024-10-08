package org.main.uneton.events;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeaths implements Listener {

    @EventHandler
    @Deprecated
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player victim = event.getPlayer();
        Player attacker = victim.getKiller();
        if (attacker != null && attacker.getType() == EntityType.PLAYER) {

        }
    }
}
