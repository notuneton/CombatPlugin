package org.main.uneton.freeze;

import io.papermc.paper.event.entity.EntityMoveEvent;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import static org.main.uneton.freeze.Freeze.freeze_list;

public class FreezeListener implements Listener {

    @EventHandler
    public void onEntityMovement(EntityMoveEvent event) {
        Entity player = event.getEntity();
        if (freeze_list.contains(player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event){
        Player player = event.getPlayer();
        if (freeze_list.contains(player)){
            freeze_list.remove(player);
        }
    }

}
