package org.main.uneton.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Tab {

    public static void updateTab() {
        int onlinePlayers = Bukkit.getOnlinePlayers().size();

        String headercolor = "\n &x&4&D&9&8&F&B[&x&6&6&A&6&F&B[ &x&F&F&F&F&F&F&lQ&x&F&F&F&F&F&F&lu&x&F&F&F&F&F&F&lo&x&F&F&F&F&F&F&ll&x&F&F&F&F&F&F&ll&x&F&F&F&F&F&F&le&x&F&F&F&F&F&F&le&x&F&F&F&F&F&F&lt&x&F&F&F&F&F&F&l.&x&F&F&F&F&F&F&lc&x&F&F&F&F&F&F&lo &x&4&D&9&8&F&B]&x&6&6&A&6&F&B] \n"; // +ChatColor.WHITE + onlinePlayers + " Players online \n";
        String footercolor = "\n &7Olet palvelimella&8: &x&4&3&7&1&F&B&lS&x&4&7&6&D&F&C&lu&x&4&A&6&9&F&C&lr&x&4&E&6&5&F&D&lv&x&5&2&6&0&F&D&li&x&5&6&5&C&F&E&lv&x&5&9&5&8&F&E&la&x&5&D&5&4&F&F&ll \n";

        String header = headercolor.replace("&D", "&d").replace("&A", "&a").replace("&B", "&b")
                .replace("&F", "&f").replace("&Q", "&q").replace("&S", "&s");
        String footer = footercolor.replace("&D", "&d").replace("&A", "&a").replace("&B", "&b")
                .replace("&F", "&f").replace("&O", "&o").replace("&E", "&e");

        for (Player user : Bukkit.getOnlinePlayers()) {
            user.setPlayerListHeaderFooter(ChatColor.translateAlternateColorCodes('&', header), ChatColor.translateAlternateColorCodes('&', footer));
        }
    }
}
