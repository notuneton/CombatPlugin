package org.main.uneton.events;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class CustomTabList {

    public static void updateTabList() {
        String headerTemplate = "\n &x&4&D&9&8&F&B[&x&6&6&A&6&F&B[ &x&F&F&F&F&F&F&lQ&x&F&F&F&F&F&F&lu&x&F&F&F&F&F&F&lo&x&F&F&F&F&F&F&ll&x&F&F&F&F&F&F&ll&x&F&F&F&F&F&F&le&x&F&F&F&F&F&F&le&x&F&F&F&F&F&F&lt&x&F&F&F&F&F&F&l.&x&F&F&F&F&F&F&lc&x&F&F&F&F&F&F&lo &x&4&D&9&8&F&B]&x&6&6&A&6&F&B] \n";
        String footerTemplate = "\n &7Olet palvelimella&8: &x&B&7&F&B&6&B&lS&x&A&8&F&C&8&3&lu&x&9&A&F&D&9&C&lr&x&8&B&F&E&B&4&lv&x&8&6&F&C&B&0&li&x&8&9&F&6&9&0&lv&x&8&C&E&F&7&1&la&x&8&F&E&9&5&1&ll \n";

        String header = headerTemplate.replace("&D", "&d").replace("&A", "&a").replace("&B", "&b")
                .replace("&F", "&f").replace("&Q", "&q").replace("&S", "&s");
        String footer = footerTemplate.replace("&D", "&d").replace("&A", "&a").replace("&B", "&b")
                .replace("&F", "&f").replace("&O", "&o").replace("&E", "&e");

        // Update all players' tab lists
        for (Player user : Bukkit.getOnlinePlayers()) {
            user.setPlayerListHeaderFooter(ChatColor.translateAlternateColorCodes('&', header), ChatColor.translateAlternateColorCodes('&', footer));
        }
    }
}