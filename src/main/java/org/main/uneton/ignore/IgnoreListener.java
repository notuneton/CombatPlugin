package org.main.uneton.ignore;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import static org.main.uneton.ignore.Ignore.isPlayerIgnored;

public class IgnoreListener implements Listener {

    @EventHandler
    public void onChatEvent(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String senderName = player.getName();

        event.getRecipients().removeIf(recipient -> isPlayerIgnored(recipient.getName(), senderName));
    }
}
