package org.main.uneton.packets;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import static org.main.uneton.packets.Freeze.freeze_list;

public class FreezeListener implements Listener {

    @EventHandler
    public void onEntityMovement(PlayerMoveEvent event) {
        Player player = event.getPlayer();
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
