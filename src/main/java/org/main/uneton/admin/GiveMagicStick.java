package org.main.uneton.admin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.main.uneton.utils.ColorUtils;

import java.util.List;

public class GiveMagicStick implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Only players can execute this command!");
            return true;
        }

        if (!player.hasPermission("combat.givestick.sv")) {
            player.sendMessage(ChatColor.RED + "You do not have permission to run /" + command.getName() + ".");
            return true;
        }

        if (args.length == 0) {
            String usage = ColorUtils.colorize("&x&2&C&0&9&1&6&l>&x&5&C&1&2&2&F&l>&x&C&7&5&3&4&7&l> &8+ &x&A&B&A&B&A&B/givestick <player>");
            player.sendMessage(usage);
            return true;
        }

        if (args.length == 1) {
            Player target = Bukkit.getPlayerExact(args[0]);
            if (target == null || !target.isOnline()) {
                String warn = ColorUtils.colorize("&x&2&C&0&9&1&6&l>&x&5&C&1&2&2&F&l>&x&C&7&5&3&4&7&l> &8+ ");
                player.sendMessage(warn + ColorUtils.colorize("&4That player does not exist."));
                return true;
            }

            ItemStack magicToyStick = new ItemStack(Material.STICK);
            ItemMeta meta = magicToyStick.getItemMeta();

            if (meta != null) {
                meta.setDisplayName("Magic Toy Stick");
                meta.setLore(List.of(
                        ChatColor.GRAY + "Do not leave with an",
                        ChatColor.GRAY + "unsupervised magician."
                ));
                meta.addEnchant(Enchantment.KNOCKBACK, 3, true);
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                magicToyStick.setItemMeta(meta);
            }

            target.getInventory().addItem(magicToyStick);
            player.sendMessage(ColorUtils.colorize("&aGave " + target.getName() + "&a item &eMAGIC_TOY_STICK"));
        }
        return true;
    }
}
