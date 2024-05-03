package org.main.uneton.trash;

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
import org.main.uneton.utils.TextUtils;

import java.util.ArrayList;

public class Trash implements CommandExecutor {

    public static final int[] fillerStats = new int[]{0,  1,  2,  3,  4,  5,  6,  7,  8,  9,  17,  18,  26,  27,  35,  36,  37,  38, 39 , 41,  42,  43,  44};
    private String guimenu = ChatColor.GOLD + "Disposal menu :-)";

    Inventory trashcan;
    public void createAndAddFiller(){
        for(int slot : fillerStats) {

            ItemStack pane = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
            setItemStackName(pane, " ");
            trashcan.setItem(slot, pane);

            ItemStack barrier = new ItemStack(Material.BARRIER);
            setItemStackName(barrier, ChatColor.RED + "Close");
            ArrayList<String> lore = new ArrayList<>();
            lore.add(TextUtils.formatText(""));
            lore.add(TextUtils.formatText("&eClick to close."));
            barrier.lore();
            trashcan.setItem(40, barrier); // Set the barrier in the 40th slot
        }
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Only players can execute this command!");
            return true;
        }

        trashcan = Bukkit.createInventory(player, 9*5, guimenu);
        createAndAddFiller();
        player.openInventory(trashcan);
        return true;
    }

    public static void setItemStackName(ItemStack item, String customName) {
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(customName);
        item.setItemMeta(meta);
    }
}