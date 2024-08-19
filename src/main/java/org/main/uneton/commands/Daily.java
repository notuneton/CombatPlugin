package org.main.uneton.commands;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.main.uneton.utils.ColorUtils;

import java.util.Random;
import java.util.UUID;

import static org.main.uneton.Combat.cooldowns;

public class Daily implements CommandExecutor {

    private ItemStack rewardItem() {
        Random random = new Random();
        int index = random.nextInt(blocksList.length);
        return blocksList[index];
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Only players can execute this command!");
            return true;
        }

        UUID playerUUID = player.getUniqueId();
        if (isOnCooldown(playerUUID)) {
            long timeLeft = getTimeLeft(playerUUID, 86400);
            String formattedTimeLeft = formatTime(timeLeft);
            player.sendMessage(ColorUtils.colorize("&c&lCAN'T! &7You must wait " + formattedTimeLeft + "&7 before using this command again."));
        } else {
            updateCooldown(playerUUID);
            ItemStack raffledItem = rewardItem();
            player.getInventory().addItem(raffledItem);
            String success = ColorUtils.colorize("&2>&a> &8+ &a");
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 3.0f);
            player.sendMessage(success + ColorUtils.colorize("You have successfully claimed &3" + raffledItem.getType()+"&a!"));
        }

        return true;
    }

    private boolean isOnCooldown(UUID playerUUID) {
        if (cooldowns.containsKey(playerUUID)) {
            long lastUsed = cooldowns.get(playerUUID);
            long currentTime = System.currentTimeMillis();
            long cooldownTime = 86400 * 1000L; // Convert to milliseconds

            return (currentTime - lastUsed) < cooldownTime;
        }
        return false;
    }

    private long getTimeLeft(UUID playerUUID, int cooldownSeconds) {
        if (cooldowns.containsKey(playerUUID)) {
            long lastUsed = cooldowns.get(playerUUID);
            long currentTime = System.currentTimeMillis();
            long cooldownTime = cooldownSeconds * 1000L;

            return (cooldownTime - (currentTime - lastUsed)) / 1000;
        }
        return 0;
    }

    private void updateCooldown(UUID playerUUID) {
        cooldowns.put(playerUUID, System.currentTimeMillis());
    }

    private String formatTime(long totalSeconds) {
        long hours = totalSeconds / 3600;
        long minutes = (totalSeconds % 3600) / 60;
        long seconds = totalSeconds % 60;

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    private final ItemStack[] blocksList = new ItemStack[]{
            new ItemStack(Material.EMERALD),
            new ItemStack(Material.AMETHYST_SHARD),
            new ItemStack(Material.IRON_NUGGET),
            new ItemStack(Material.GOLD_NUGGET),
            new ItemStack(Material.COPPER_INGOT),
            new ItemStack(Material.STRING)
    };
}
