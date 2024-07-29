package org.main.uneton.admin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.main.uneton.utils.ColorUtils;

import java.util.Arrays;
import java.util.List;

public class MagicalBucket implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Only players can execute this command!");
            return true;
        }

        if (!player.hasPermission("combat.givebucket.sv")) {
            player.sendMessage(ChatColor.RED + "You do not have permission to run /" + command.getName() + ".");
            return true;
        }

        if (args.length == 0) {
            String usage = ColorUtils.colorize("&x&2&C&0&9&1&6&l>&x&5&C&1&2&2&F&l>&x&C&7&5&3&4&7&l> &x&2&E&2&E&2&E&l+ &7/givemagicalbucket <player>");
            player.sendMessage(usage);
        }

        if (args.length == 1) {
            Player target = Bukkit.getPlayerExact(args[0]);
            if (target == null || !target.isOnline()) {
                String warn = ColorUtils.colorize("&x&2&C&0&9&1&6&l>&x&5&C&1&2&2&F&l>&x&C&7&5&3&4&7&l> &x&2&E&2&E&2&E&l+ ");
                player.sendMessage(warn + ColorUtils.colorize("&7That player does not exist."));
                return true;
            }

            ItemStack magicalBucket = createCustomInfiniteWaterBucket();
            player.getInventory().addItem(magicalBucket);
            player.sendMessage(ColorUtils.colorize("&aGave " + target.getName() + "&aitem &eMAGICAL BUCKET"));
        }
        return true;
    }

    @Deprecated
    public boolean isMagicalWaterBucket(ItemStack item) {
        if (item == null || item.getType() != Material.WATER_BUCKET) {
            return false;
        }
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return false;
        }
        if (!meta.hasDisplayName() || !meta.getDisplayName().equals(ChatColor.RESET + "Magical Water Bucket")) {
            return false;
        }
        if (!meta.hasLore() || meta.getLore().size() != 5) {
            return false;
        }
        List<String> lore = meta.getLore();
        if (!lore.get(0).equals(ChatColor.GRAY + "This Magical Water Bucket will never") ||
                !lore.get(1).equals(ChatColor.GRAY + "run out of water, no matter how") ||
                !lore.get(2).equals(ChatColor.GRAY + "many times it is emptied.") ||
                !lore.get(4).equals(ChatColor.WHITE + "" + ChatColor.BOLD + "COMMON")) {
            return false;
        }
        return true;
    }

    private ItemStack createCustomInfiniteWaterBucket() {
        ItemStack waterBucket = new ItemStack(Material.WATER_BUCKET);
        ItemMeta meta = waterBucket.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(ChatColor.RESET + "Magical Water Bucket");
            meta.setLore(Arrays.asList(
                    ChatColor.GRAY + "This Magical Water Bucket will never",
                    ChatColor.GRAY + "run out of water, no matter how",
                    ChatColor.GRAY + "many times it is emptied.",
                    "",
                    ChatColor.WHITE + "" + ChatColor.BOLD + "COMMON"
            ));
            waterBucket.setItemMeta(meta);
        }

        return waterBucket;
    }
}