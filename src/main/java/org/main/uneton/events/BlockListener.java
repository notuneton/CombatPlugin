package org.main.uneton.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import static org.main.uneton.commands.Blockplayer.isPlayerBlocked;

public class BlockListener implements Listener {

    @EventHandler
    public void onChatEvent(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String senderName = player.getName();

        event.getRecipients().removeIf(recipient -> isPlayerBlocked(recipient.getName(), senderName));
    }
}
