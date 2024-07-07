package org.main.uneton.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Tab {

    public static void updateTab() {
        String headercolor = "\n &x&4&D&9&8&F&B[&x&6&6&A&6&F&B[ &x&F&F&F&F&F&F&lQ&x&F&F&F&F&F&F&lu&x&F&F&F&F&F&F&lo&x&F&F&F&F&F&F&ll&x&F&F&F&F&F&F&ll&x&F&F&F&F&F&F&le&x&F&F&F&F&F&F&le&x&F&F&F&F&F&F&lt&x&F&F&F&F&F&F&l.&x&F&F&F&F&F&F&lc&x&F&F&F&F&F&F&lo &x&4&D&9&8&F&B]&x&6&6&A&6&F&B] \n";
        String footercolor = "\n &7Olet palvelimella&8: &x&8&B&B&D&F&F&lS&x&7&0&A&9&E&9&lu&x&5&5&9&4&D&2&lr&x&3&A&8&0&B&C&lv&x&2&F&7&A&B&7&li&x&3&2&8&3&C&4&lv&x&3&6&8&B&D&1&la&x&3&9&9&4&D&E&ll \n";

        String header = headercolor.replace("&D", "&d").replace("&A", "&a").replace("&B", "&b")
                .replace("&F", "&f").replace("&Q", "&q").replace("&S", "&s");
        String footer = footercolor.replace("&D", "&d").replace("&A", "&a").replace("&B", "&b")
                .replace("&F", "&f").replace("&O", "&o").replace("&E", "&e");

        // Update all player's tab lists
        for (Player user : Bukkit.getOnlinePlayers()) {
            user.setPlayerListHeaderFooter(ChatColor.translateAlternateColorCodes('&', header), ChatColor.translateAlternateColorCodes('&', footer));
        }
    }
}