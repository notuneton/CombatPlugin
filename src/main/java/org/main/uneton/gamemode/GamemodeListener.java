package org.main.uneton.gamemode;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class GamemodeListener implements Listener {

    private String inv = ChatColor.DARK_GRAY.toString() + ChatColor.BOLD +"Gamemodes";

    @EventHandler
    public void onInvClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        if (e.getView().getTitle().contains(inv)) {
            ItemStack clickedItem = e.getCurrentItem();
            if (clickedItem != null) {

                switch (e.getCurrentItem().getType()) {
                    case STONE:
                        e.setCancelled(true);
                        player.setGameMode(GameMode.CREATIVE);
                        break;
                    case GRASS_BLOCK:
                        e.setCancelled(true);
                        player.setGameMode(GameMode.SURVIVAL);
                        break;
                    case STRING:
                        e.setCancelled(true);
                        player.setGameMode(GameMode.SPECTATOR);
                        break;
                }
            }
        }
    }
}
