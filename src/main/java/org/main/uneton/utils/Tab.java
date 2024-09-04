package org.main.uneton.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Tab {

    public static void updateTab() {
        String headerColor = "\n &x&4&D&9&8&F&B[&x&6&6&A&6&F&B[ &x&F&F&F&F&F&F&lQ&x&F&F&F&F&F&F&lu&x&F&F&F&F&F&F&lo&x&F&F&F&F&F&F&ll&x&F&F&F&F&F&F&ll&x&F&F&F&F&F&F&le&x&F&F&F&F&F&F&le&x&F&F&F&F&F&F&lt&x&F&F&F&F&F&F&l &x&4&D&9&8&F&B]&x&6&6&A&6&F&B] \n";
        String footerColor = "\n &7Olet palvelimella&8: &x&2&6&6&9&F&F&ld&x&2&6&6&9&F&F&le&x&2&6&6&9&F&F&lv&x&2&6&6&9&F&F&l-&x&2&6&6&9&F&F&ls&x&2&6&6&9&F&F&le&x&2&6&6&9&F&F&lr&x&2&6&6&9&F&F&lv&x&2&6&6&9&F&F&le&x&2&6&6&9&F&F&lr \n";
        for (Player player : Bukkit.getOnlinePlayers()) {
            String header = headerColor.replace("&D", "&d").replace("&A", "&a").replace("&B", "&b")
                    .replace("&F", "&f").replace("&Q", "&q").replace("&S", "&s");
            String footer = footerColor.replace("&D", "&d").replace("&A", "&a").replace("&B", "&b")
                    .replace("&F", "&f").replace("&O", "&o").replace("&E", "&e");
            player.setPlayerListHeaderFooter(ColorUtils.colorize(header), ColorUtils.colorize(footer));
        }
    }
}
