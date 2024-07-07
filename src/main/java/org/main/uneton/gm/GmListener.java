package org.main.uneton.gm;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import static org.main.uneton.gm.Gm.gm_list;

public class GmListener implements Listener {

    @EventHandler
    public void onEntityDamaged(EntityDamageEvent event) {
        Entity player = event.getEntity();
        if (gm_list.contains(player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event){
        Player player = event.getPlayer();
        if (gm_list.contains(player)){
            gm_list.remove(player);
        }
    }

    @EventHandler
    public void onEntityAggro(EntityDamageEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof Player && ((Player) entity).isOnline()) {
            Player player = (Player) entity;
            if (gm_list.contains(player)) {
                // Instead of cancelling the event, let it proceed normally
                // This allows mobs to still aggro towards the player
                return;
            }
        }
        // Handle other cases outside of God Mode players
        event.setCancelled(false); // Ensure other damage checks are handled correctly
    }
}