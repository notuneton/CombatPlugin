package org.main.uneton.magicStick;

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

import java.util.ArrayList;

public class MagicStick implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Only players can execute this command!");
            return true;
        }

        if (args.length == 0) {
            String usage = ColorUtils.colorize("&x&2&C&0&9&1&6&l>&x&5&C&1&2&2&F&l>&x&C&7&5&3&4&7&l> &x&2&E&2&E&2&E&l- &7&x&A&B&A&B&A&B/&x&A&B&A&B&A&Bm&x&A&B&A&B&A&Ba&x&A&B&A&B&A&Bg&x&A&B&A&B&A&Bi&x&A&B&A&B&A&Bc&x&A&B&A&B&A&Bs&x&A&B&A&B&A&Bt&x&A&B&A&B&A&Bi&x&A&B&A&B&A&Bc&x&A&B&A&B&A&Bk &x&A&B&A&B&A&B<&x&A&B&A&B&A&Bp&x&A&B&A&B&A&Bl&x&A&B&A&B&A&Ba&x&A&B&A&B&A&By&x&A&B&A&B&A&Be&x&A&B&A&B&A&Br&x&A&B&A&B&A&B>");
            player.sendMessage(usage);
            return true;
        }

        if (args.length == 1) {
            Player target = Bukkit.getPlayerExact(args[0]);
            if (target == null || !target.isOnline()) {
                String warn = ColorUtils.colorize("&x&2&C&0&9&1&6&l>&x&5&C&1&2&2&F&l>&x&C&7&5&3&4&7&l> &x&2&E&2&E&2&E&l- ");
                player.sendMessage(warn + ColorUtils.colorize("&7That player does not exist."));
                return true;
            }

            magic(player);
        }
        return true;
    }

    private void magic(Player player) {
        ItemStack stick = new ItemStack(Material.STICK);
        ItemMeta meta = stick.getItemMeta();
        meta.setDisplayName("Magic Toy Stick");
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY+ "Do not leave with an");
        lore.add(ChatColor.GRAY+ "unsupervised magician.");
        meta.setLore(lore);
        stick.setItemMeta(meta);
        player.getInventory().addItem(stick);
    }
}
