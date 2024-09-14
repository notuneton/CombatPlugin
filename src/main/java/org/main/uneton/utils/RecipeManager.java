package org.main.uneton.utils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.main.uneton.Combat;

public class RecipeManager {

    public RecipeManager(Combat plugin) {
    }

    public static void createEnchantedAppleRecipe() {
        ItemStack notchApple = new ItemStack(Material.ENCHANTED_GOLDEN_APPLE, 1);
        ItemMeta notchAppleMeta = notchApple.getItemMeta();
        if (notchAppleMeta != null) {
            notchAppleMeta.setDisplayName(ColorUtils.colorize("&cEnchanted Apple"));
            notchApple.setItemMeta(notchAppleMeta);
        }

        ShapedRecipe godAppleRecipe = new ShapedRecipe(new NamespacedKey(Combat.getInstance(), "god_apple_recipe"), notchApple);
        godAppleRecipe.shape("GGG", "GAG", "GGG");
        godAppleRecipe.setIngredient('G', Material.GOLD_BLOCK);
        godAppleRecipe.setIngredient('A', Material.APPLE);
        Bukkit.addRecipe(godAppleRecipe);
    }

    public static void createChainableArmorRecipe() {
        ItemStack chainmailBoots = new ItemStack(Material.CHAINMAIL_BOOTS, 1);
        ItemStack chainmailLeggings = new ItemStack(Material.CHAINMAIL_LEGGINGS, 1);
        ItemStack chainmailChestplate = new ItemStack(Material.CHAINMAIL_CHESTPLATE, 1);
        ItemStack chainmailHelmet = new ItemStack(Material.CHAINMAIL_HELMET, 1);

        ShapedRecipe bootsRecipe = new ShapedRecipe(new NamespacedKey(Combat.getInstance(), "boots_recipe"), chainmailBoots);
        bootsRecipe.shape("C C");
        bootsRecipe.shape("C C");
        bootsRecipe.setIngredient('C', Material.CHAIN);



        Bukkit.addRecipe(bootsRecipe);
    }

    public static void createElytraRecipe() {
        ItemStack elytra = new ItemStack(Material.ELYTRA, 1);
        ItemMeta elytraMeta = elytra.getItemMeta();
        if (elytraMeta != null) {
            elytraMeta.setDisplayName(ColorUtils.colorize("&Elytra's"));
            elytra.setItemMeta(elytraMeta);
        }

        ShapedRecipe elytraRecipe = new ShapedRecipe(new NamespacedKey(Combat.getInstance(), "elytra_recipe"), elytra);
        elytraRecipe.shape("PDP", "P P", "F F");
        elytraRecipe.setIngredient('F', Material.FEATHER);
        elytraRecipe.setIngredient('P', Material.PHANTOM_MEMBRANE);
        elytraRecipe.setIngredient('D', Material.DIAMOND);
        Bukkit.addRecipe(elytraRecipe);
    }
}
