package org.main.uneton.gamemode;

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

import java.util.ArrayList;

public class Gamemodes implements CommandExecutor {

    private String inv = ChatColor.DARK_GRAY.toString() + ChatColor.BOLD +"Gamemodes";
    Inventory gamemodeSwitcher;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Only players can execute this command!");
            return true;
        }

        if(!player.hasPermission("combat.gamemode.sv")) {
            player.sendMessage(ChatColor.RED + "Permission Denied: You do not have permission to do this task.");
            return true;
        }

        // create virtual chest inventory with size 3*9
        gamemodeSwitcher = Bukkit.createInventory(player, 9*3, inv);

        // format slotting
        ItemStack creative = new ItemStack(Material.STONE);
        setItemFormatSlot(creative, ChatColor.GOLD + "Creative.");

        ItemStack survival = new ItemStack(Material.GRASS_BLOCK);
        setItemFormatSlot(survival, ChatColor.GOLD + "Survival.");

        ItemStack spectator = new ItemStack(Material.STRING);
        setItemFormatSlot(spectator, ChatColor.GOLD + "Spectator.");

        // set items to item index
        gamemodeSwitcher.setItem(11, creative);
        gamemodeSwitcher.setItem(13, survival);
        gamemodeSwitcher.setItem(15, spectator);
        player.openInventory(gamemodeSwitcher);
        return true;
    }

    public static void setItemFormatSlot(ItemStack item, String customName) {
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(customName);
        item.setItemMeta(meta);

        // lore
        ArrayList<String> lore = new ArrayList<>();
        lore.add(" ");
        lore.add(ChatColor.YELLOW + "Click to switch your " + ChatColor.GREEN + "Gamemode.");
        item.setLore(lore);
    }
}