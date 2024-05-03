package org.main.uneton.suicide;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class SuicideEvent implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (event.getView().getTitle().equals(ChatColor.BOLD + "Kys menu :)")) {
            ItemStack clickedItem = event.getCurrentItem();
            if(clickedItem != null) {

                switch (event.getCurrentItem().getType()) {
                    case GREEN_CONCRETE:
                        player.setHealth(0);
                        event.setCancelled(true);
                        break;

                    case RED_CONCRETE:
                        player.closeInventory();
                        event.setCancelled(true);
                        break;
                }
            }
        }
    }
}
