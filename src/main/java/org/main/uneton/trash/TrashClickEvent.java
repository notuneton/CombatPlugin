package org.main.uneton.trash;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class TrashClickEvent implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (event.getView().getTitle().equals(ChatColor.RED + "Trashcan :-)")) {
            ItemStack clickedItem = event.getCurrentItem();
            if(clickedItem != null) {
                if(clickedItem.getType() == Material.BLACK_STAINED_GLASS_PANE) {
                    event.setCancelled(true);

                } else if (clickedItem.getType() == Material.BARRIER) {
                    event.setCancelled(true);
                    player.closeInventory();
                }
            }
        }
    }
}
