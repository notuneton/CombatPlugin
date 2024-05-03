package org.main.uneton.suicide;

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

public class Suicide implements CommandExecutor {

    private String guimenu = ChatColor.BOLD + "Kys menu :)";

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Only players can execute this command!");
            return true;
        }

        Inventory suicide = Bukkit.createInventory(player, 9*3, guimenu);
        ItemStack yesButton = new ItemStack(Material.GREEN_CONCRETE);
        setItemStackName(yesButton, ChatColor.GREEN.toString() + ChatColor.BOLD+ "Confirm");
        suicide.setItem(15, yesButton);

        ItemStack noButton = new ItemStack(Material.RED_CONCRETE);
        setItemStackName(noButton, ChatColor.RED.toString() + ChatColor.BOLD+ "Deny");
        suicide.setItem(11, noButton);

        player.openInventory(suicide);
        return true;
    }

    public static void setItemStackName(ItemStack item, String customName) {
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(customName);
        item.setItemMeta(meta);
    }
}
