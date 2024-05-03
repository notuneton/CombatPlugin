package org.main.uneton.events;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.main.uneton.Combat;

import java.util.HashMap;

public class MessageHolder implements Listener {

    private final HashMap<Player, String> lastmsg = new HashMap<>(); // blocker repeat messages
    private final HashMap<Player, Boolean> antispam = new HashMap<>();

    @EventHandler
    public void onChat(PlayerChatEvent e) {
        Player player = e.getPlayer();
        String message = e.getMessage();
        String blocked_message = ChatColor.GRAY + player.getName() + ChatColor.ITALIC + " " + message;

        if(!lastmsg.containsKey(player)) {
            lastmsg.put(player, message);
        } else {
            if(message.equalsIgnoreCase(lastmsg.get(player))) {
                e.setCancelled(true);
                player.sendMessage(blocked_message);
                return;
            }
        }

        if (!antispam.containsKey(player)) {
            antispam.put(player, true);
            Bukkit.getScheduler().scheduleSyncDelayedTask(Combat.getInstance(), () -> {
                if (antispam.containsKey(player)) antispam.remove(player);
            }, 20L); // 20 ticks = 1 second

        } else {
            e.setCancelled(true);
            player.sendMessage(blocked_message);

        }

    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        e.setCancelled(true);
        Player sender = e.getPlayer();
        String message = e.getMessage();
        String playerMsg = ChatColor.WHITE + sender.getName() + ChatColor.GRAY + " [ALL] " + ChatColor.WHITE + message;
        Bukkit.getServer().broadcastMessage(playerMsg);
    }
}
