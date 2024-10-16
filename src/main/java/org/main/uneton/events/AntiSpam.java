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
    private final ConcurrentHashMap<Player, Boolean> spammed_message = new ConcurrentHashMap<>();

    @EventHandler
    @Deprecated
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();
        String previousMessage = lastMessage.put(player, message);
        if (message.equalsIgnoreCase(previousMessage)) {
            player.sendMessage("\n");
            player.sendMessage(ColorUtils.colorize("&cYou cannot say the same message twice!"));
            player.sendMessage("\n");
            event.setCancelled(true);
            return;
        }

        if (spammed_message.putIfAbsent(player, true) != null) {
            event.setCancelled(true);
            player.sendMessage("\n");
            player.sendMessage(ColorUtils.colorize("&cYou're sending messages too quickly!"));
            player.sendMessage("\n");
        } else {
            Bukkit.getScheduler().runTaskLater(JavaPlugin.getPlugin(Combat.class), () -> spammed_message.remove(player), 30L);
        }
    }
}