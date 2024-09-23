package org.main.uneton.events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.main.uneton.Combat;
import org.main.uneton.utils.ColorUtils;

import java.util.concurrent.ConcurrentHashMap;

public class AntiSpam implements Listener {

    private final ConcurrentHashMap<Player, String> lastMessage = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Player, Boolean> spam = new ConcurrentHashMap<>();

    @EventHandler
    @Deprecated
    public void onChatSpam(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();
        String previousMessage = lastMessage.put(player, message);
        if (message.equalsIgnoreCase(previousMessage)) {
            player.sendMessage("\n");
            player.sendMessage(ColorUtils.colorize("&x&2&C&0&9&1&6&l>&x&5&C&1&2&2&F&l>&x&C&7&5&3&4&7&l> &7You cannot say the same message twice&f."));
            player.sendMessage("\n");
            event.setCancelled(true);
            return;
        }

        if (spam.putIfAbsent(player, true) != null) {
            event.setCancelled(true);
            player.sendMessage("\n");
            player.sendMessage(ColorUtils.colorize("&x&2&C&0&9&1&6&l>&x&5&C&1&2&2&F&l>&x&C&7&5&3&4&7&l> &7You're sending messages too quickly!"));
            player.sendMessage("\n");
        } else {
            Bukkit.getScheduler().runTaskLater(JavaPlugin.getPlugin(Combat.class), () -> spam.remove(player), 30L);
        }
    }
}