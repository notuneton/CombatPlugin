package org.main.uneton.gm;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import static org.main.uneton.gm.Gm.godmodePlayers;
import static org.main.uneton.gm.Gm.guiName;

public class GmListener implements Listener {

    @EventHandler
    public void onEntityDamaged(EntityDamageEvent event) {
        Entity player = event.getEntity();
        if (godmodePlayers.contains(player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event){
        Player player = event.getPlayer();
        if (godmodePlayers.contains(player)){
            godmodePlayers.remove(player);
            Bukkit.getLogger().info("[CombatV3] 'gm_players' HashMap was cleared");
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        if (!event.getView().getTitle().equals(guiName)) return;

        event.setCancelled(true); // Prevents the GUI from closing

        ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem == null) return;

        if (clickedItem.getType() == Material.GREEN_CONCRETE) {
            godmodePlayers.add(player);
            player.sendMessage(ChatColor.GREEN + "Godmode enabled!");
            // Implement Godmode logic here
        } else if (clickedItem.getType() == Material.RED_CONCRETE) {
            godmodePlayers.remove(player);
            player.sendMessage(ChatColor.RED + "Godmode disabled!");
            // Implement logic to disable Godmode here
        }
    }

    /*
    @EventHandler
    public void onClick(InventoryClickEvent event){
        Player target = (Player) event.getWhoClicked();
        if (event.getView().getTitle().equals(ChatColor.GOLD + "God mode >")){
            if (event.getCurrentItem() == null) return; // Added null check

            switch (event.getCurrentItem().getType()){
                case GREEN_CONCRETE:
                    if (godmodePlayers.contains(target)){
                        godmodePlayers.remove(target);
                        // Uncomment the following line to notify the player
                        // target.sendMessage(ChatColor.GREEN + target.getName() + " is no longer in god mode.");
                    }
                    event.setCancelled(true);
                    break;

                case RED_CONCRETE:
                    if (!godmodePlayers.contains(target)){
                        godmodePlayers.add(target);
                        // Uncomment the following line to notify the player
                        // target.sendMessage(ChatColor.GREEN + target.getName() + " is now in god mode.");
                    }
                    event.setCancelled(true);
                    break;
            }
        }
    }
     */
}