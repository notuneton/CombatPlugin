package org.main.uneton.events;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class MagicalBucketPlace implements Listener {

    @EventHandler
    public void onPlayerUseBucket(PlayerBucketEmptyEvent event) {
        ItemStack item = event.getItemStack();
        if (item == null || item.getType() != Material.WATER_BUCKET) {
            return;
        }
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return;
        }
        if (!meta.hasDisplayName()) {
            return;
        }
        String displayName = meta.getDisplayName();
        if (!displayName.equals("Magical Water Bucket")) {
            return;
        }
        if (!meta.hasLore() || meta.getLore().size() != 5) {
            return;
        }
        List<String> lore = meta.getLore();
        if (!lore.get(0).equals(ChatColor.GRAY + "This Magical Water Bucket will never") ||
                !lore.get(1).equals(ChatColor.GRAY + "run out of water, no matter how") ||
                !lore.get(2).equals(ChatColor.GRAY + "many times it is emptied.") ||
                !lore.get(4).equals(ChatColor.WHITE + "" + ChatColor.BOLD + "COMMON")) {
            return;
        }

        event.setCancelled(true);
        event.getBlock().setType(Material.WATER);
    }
}
