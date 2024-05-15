package org.main.uneton.otherPackets;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import static org.main.uneton.otherPackets.Ignore.ignoredPlayers;

public class IgnoreListener implements Listener {

    @EventHandler
    public void onChatEvent(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String senderName = player.getName();
        if (ignoredPlayers.contains(senderName)) {
            event.setCancelled(true); // Prevent the ignored player from sending the message
        }
    }
}
