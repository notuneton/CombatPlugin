package org.main.uneton.events;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.main.uneton.utils.ColorUtils;

public class TrashEvent implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (event.getView().getTitle().equals(ColorUtils.colorize("&lTrashcan menu :-)"))) {
            ItemStack clickedItem = event.getCurrentItem();
            if (clickedItem != null) {

                switch (event.getCurrentItem().getType()) {
                    case BLACK_STAINED_GLASS_PANE:
                        event.setCancelled(true);
                        break;

                    case BARRIER:
                        event.setCancelled(true);
                        player.closeInventory();
                        break;
                }
            }
        }
    }
}
