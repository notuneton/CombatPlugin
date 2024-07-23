package org.main.uneton.events;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.main.uneton.Combat;

import java.util.Random;

public class ChestBreak implements Listener {

    private static final double SPAWN_CHEST_CHANCE = 0.9; // 90% chance

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        System.out.println("Block broken: " + block.getType());

        if (Math.random() < SPAWN_CHEST_CHANCE) {
            System.out.println("Spawning chest at: " + block.getLocation());
            block.setType(Material.CHEST);

            // Delay to ensure chest state is updated correctly
            Bukkit.getScheduler().runTaskLater(Combat.getInstance(), () -> {
                if (block.getState() instanceof Chest) {
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

                    for (ItemStack possibleItem : possibleItems) {
                        if (random.nextBoolean()) {
                            int slot = random.nextInt(chest.getBlockInventory().getSize());
                            chest.getBlockInventory().setItem(slot, possibleItem);
                            System.out.println("Added " + possibleItem.getType() + " to slot " + slot);
                        }
                    }

                    chest.update();
                    System.out.println("Chest updated at: " + block.getLocation());
                } else {
                    System.out.println("Block is not a chest at: " + block.getLocation());
                }
            }, 1L); // 1 tick delay
        }
    }
}