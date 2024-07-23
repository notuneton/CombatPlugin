package org.main.uneton.events;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class ChestBreak implements Listener {

    private static final double SPAWN_CHEST_CHANCE = 0.5; // 50% chance

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();

        if (Math.random() < SPAWN_CHEST_CHANCE) {
            block.setType(Material.CHEST);
            Chest chest = (Chest) block.getState();

            Random random = new Random();
            ItemStack[] possibleItems = {
                    new ItemStack(Material.DIAMOND, 1),
                    new ItemStack(Material.IRON_INGOT, 1),
                    new ItemStack(Material.GOLD_INGOT, 1),
                    new ItemStack(Material.EMERALD, 1),
                    new ItemStack(Material.APPLE, 1),
                    new ItemStack(Material.BREAD, 1)
            };

            for (int i = 0; i < possibleItems.length; i++) {
                if (random.nextBoolean()) {
                    int slot = random.nextInt(chest.getBlockInventory().getSize());
                    chest.getBlockInventory().setItem(slot, possibleItems[i]);
                }
            }

            chest.update();
        }
    }
}
