package org.main.uneton.limbo;

import org.bukkit.Bukkit;
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
        if (limboManager != null) {
            limboManager.updatePlayerActivity(event.getPlayer());
        } else {
            Bukkit.getLogger().warning("[CombatV3]: limboManager is null!");
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (limboManager != null) {
            limboManager.updatePlayerActivity(event.getPlayer());
        } else {
            Bukkit.getLogger().warning("[CombatV3]: limboManager is null!");
        }
    }
}
