package org.main.uneton.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.main.uneton.Combat;

public class PlayerAfkMove implements Listener {

    private final Combat plugin;

    public PlayerAfkMove(Combat plugin) {
        this.plugin = plugin;
    }
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        plugin.updatePlayerActivity(event.getPlayer());
    }
    @EventHandler
    public void onPlayerMovement(PlayerMoveEvent event) {
        plugin.updatePlayerActivity(event.getPlayer());
    }
}