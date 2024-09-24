package org.main.uneton.limbo;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerActivityListener implements Listener {

    private final LimboManager limboManager;
    public PlayerActivityListener(LimboManager limboManager) {
        this.limboManager = limboManager;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        limboManager.updatePlayerActivity(event.getPlayer());
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        limboManager.updatePlayerActivity(event.getPlayer());
    }
}
