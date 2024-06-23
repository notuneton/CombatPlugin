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

    @Deprecated
    @EventHandler
    public void onChatSpam(PlayerChatEvent e) {
        Player player = e.getPlayer();
        String message = e.getMessage();
        if (!lastmsg.containsKey(player)) {
            lastmsg.put(player, message);
        } else {
            if (message.equalsIgnoreCase(lastmsg.get(player))) {
                e.setCancelled(true);
                sendWarnMessage(player, message);
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
            sendWarnMessage(player, message);
            return;
        }

        String[] array = message.split(" ");
        int actions = 0;
        for (int i = 0; i < array.length; i++) {
            if (array[i].equals(array[i].toUpperCase())) {
                actions++;
            }
        }
        if (actions >= 3) {
            e.setCancelled(true);
        }
    }

    private void sendWarnMessage(Player player, String message) {
        String blocked_message = ChatColor.translateAlternateColorCodes('&', "" + ChatColor.GRAY + ChatColor.ITALIC + player.getName() + " " + message);
        player.sendMessage(blocked_message);
    }

    @EventHandler
    public void onChat(PlayerChatEvent e) {
        String message = e.getMessage();
        if (message.contains(")")) {
            // Replace ")" with "ツ"
            String replacedMessage = message.replace(")", "ツ");

            // Set the new message
            e.setMessage(replacedMessage);
        }
    }


    // &8[] &7%player% &8: &7%message%
}
