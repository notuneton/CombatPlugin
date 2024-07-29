package org.main.uneton.events;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.main.uneton.Combat;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class MessageHolder implements Listener {

    private final ConcurrentHashMap<Player, String> lastMessage = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Player, Boolean> antiSpam = new ConcurrentHashMap<>();

    @EventHandler
    public void onChatSpam(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();

        // Check for repeated messages
        String previousMessage = lastMessage.put(player, message);
        if (message.equalsIgnoreCase(previousMessage)) {
            event.setCancelled(true);
            sendWarnMessage(player, message);
            return;
        }

        // Check for spamming
        if (antiSpam.putIfAbsent(player, true) != null) {
            event.setCancelled(true);
        } else {
            Bukkit.getScheduler().runTaskLater(JavaPlugin.getPlugin(Combat.class), () -> {
                antiSpam.remove(player);
            }, 20L); // 20 ticks = 1 second
        }
    }

    private void sendWarnMessage(Player player, String message) {
        String blockedMessage = ChatColor.translateAlternateColorCodes('&',
                ChatColor.GRAY + "" + ChatColor.ITALIC + player.getName() + " " + message);
        player.sendMessage(blockedMessage);
    }
}


// This can't be posted because it contains content blocked by this server. This may also be viewed by server owners.