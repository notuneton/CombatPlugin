package org.main.uneton.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.main.uneton.Combat;

public class RecipeManager {

    public static void registerCustomRecipes() {
        // Custom Totem of Undying recipe
        ItemStack customTotem = new ItemStack(Material.TOTEM_OF_UNDYING, 1);
        ItemMeta customTotem_meta = customTotem.getItemMeta();
        customTotem_meta.setDisplayName(ChatColor.LIGHT_PURPLE + "Another Heart");
        customTotem.setItemMeta(customTotem_meta);

        ShapedRecipe totemOfUndying = new ShapedRecipe(new NamespacedKey(Combat.getInstance(), "totemOfUndying"), customTotem);
        totemOfUndying.shape("HR");
        totemOfUndying.setIngredient('H', Material.HEART_OF_THE_SEA);
        totemOfUndying.setIngredient('R', Material.GOLD_INGOT);
        Bukkit.addRecipe(totemOfUndying);

        // Notch Apple recipe
        ItemStack notch_apple = new ItemStack(Material.ENCHANTED_GOLDEN_APPLE, 1);
        ItemMeta notch_apple_meta = notch_apple.getItemMeta();
        notch_apple_meta.setDisplayName(ChatColor.LIGHT_PURPLE + "Notch Apple");
        notch_apple.setItemMeta(notch_apple_meta);

        ShapedRecipe godAppleRecipe = new ShapedRecipe(new NamespacedKey(Combat.getInstance(), "god_apple_recipe"), notch_apple);
        godAppleRecipe.shape("GGG", "GAG", "GGG");
        godAppleRecipe.setIngredient('G', Material.GOLD_BLOCK);
        godAppleRecipe.setIngredient('A', Material.APPLE);
        Bukkit.addRecipe(godAppleRecipe);

        // Elytra recipe
        ItemStack elytra = new ItemStack(Material.ELYTRA, 1);
        ItemMeta elytra_meta = elytra.getItemMeta();
        elytra_meta.setDisplayName(ChatColor.LIGHT_PURPLE + "Elytra");
        elytra_meta.setUnbreakable(true);
        elytra.setItemMeta(elytra_meta);

        ShapedRecipe elytraRecipe = new ShapedRecipe(new NamespacedKey(Combat.getInstance(), "elytra_recipe"), elytra);
        elytraRecipe.shape("FSF", "PDP", "P P");
        elytraRecipe.setIngredient('F', Material.STRING);
        elytraRecipe.setIngredient('S', Material.FEATHER);
        elytraRecipe.setIngredient('P', Material.PHANTOM_MEMBRANE);
        elytraRecipe.setIngredient('D', Material.DRAGON_BREATH);
        Bukkit.addRecipe(elytraRecipe);
    }
}
