package org.main.uneton.gm;

import org.bukkit.Bukkit;
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
            Bukkit.getLogger().info("[CombatV3] 'gm_players' HashMap was cleared");
        }
    }
}