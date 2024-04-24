package org.main.uneton.events;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import org.main.uneton.Combat;

import java.util.HashMap;


public class MessageHolder implements Listener {

    private final HashMap<Player, String> lastmsg = new HashMap<>(); // blocker repeat messages
    private final HashMap<Player, Boolean> antispam = new HashMap<>();

    @EventHandler
    public void onChatEvent(PlayerChatEvent e) {
        Player player = e.getPlayer();
        String message = e.getMessage();
        String blocked = ChatColor.GRAY + player.getName() + ChatColor.ITALIC + " " + message;

        if(!lastmsg.containsKey(player)) {
            lastmsg.put(player, message);
        } else {
            if(message.equalsIgnoreCase(lastmsg.get(player))) {
                e.setCancelled(true);
                player.sendMessage(blocked);
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
            player.sendMessage(blocked);

        }

    }
}
