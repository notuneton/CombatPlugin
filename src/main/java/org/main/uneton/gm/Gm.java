package org.main.uneton.gm;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

public class Gm implements CommandExecutor {

    public static Set<Player> godmodePlayers = new HashSet<>();
    public static String guiName = ChatColor.GOLD + "God mode >";

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Only players can execute this command!");
            return true;
        }

        Inventory gui = Bukkit.createInventory(player, 9*3, guiName);
        ItemStack onButton = new ItemStack(Material.GREEN_CONCRETE);
        setItemStackName(onButton, "Enable Godmode");
        gui.setItem(15, onButton);

        ItemStack offButton = new ItemStack(Material.RED_CONCRETE);
        setItemStackName(offButton, "Disable Godmode");
        gui.setItem(11, offButton);

        player.openInventory(gui);

        return true;
    }

    public static void setItemStackName(ItemStack item, String customName) {
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(customName);
        item.setItemMeta(meta);
    }

}
