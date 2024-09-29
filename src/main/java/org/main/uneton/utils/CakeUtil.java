package org.main.uneton.utils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class CakeUtil {

    static Inventory gui;
    static ArrayList<ItemStack> cakes = new ArrayList<>();
    static Material[] matrials = new Material[]{Material.CAKE};

    public static void gui(Player p) {
        createGui(p, 9 * 3, "Cakes");
        for (Material m : matrials.clone()) {
            createItems(m, ColorUtils.colorize("&6An cake."));
        }
        for (int i = 0; i <= cakes.size(); i++) {
            setItemsInGUI(i, cakes.get(i));
        }
        p.openInventory(gui);
    }

    public static void createGui(InventoryHolder holder, int size, String name) {
        gui = Bukkit.createInventory(holder, size, name);
    }

    public static void createItems(Material mat, String s) {
        ItemStack i = new ItemStack(mat);
        ItemMeta meth = i.getItemMeta();
        meth.setDisplayName(s);
        i.setItemMeta(meth);
        cakes.add(i);
    }

    public static void setItemsInGUI(int index, ItemStack item) {
        gui.setItem(index, item);
    }
}
