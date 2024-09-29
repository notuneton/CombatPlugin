package org.main.uneton.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Tab {

    public static void updateTab() {
        int online = Bukkit.getOnlinePlayers().size();
        String headerColor = "\n &x&F&B&B&7&F&2&lL&x&F&C&A&6&F&5&lO&x&F&D&9&5&F&8&lC&x&F&E&8&4&F&B&lA&x&F&F&7&3&F&E&lL \n";
        String footerColor = "\n &7&x&E&1&A&4&D&9o&x&D&D&A&4&D&An&x&D&9&A&5&D&Bl&x&D&6&A&5&D&Ci&x&D&2&A&6&D&En&x&C&E&A&6&D&Fe &x&C&A&A&6&E&0p&x&C&7&A&7&E&1l&x&C&3&A&7&E&2a&x&B&F&A&7&E&3y&x&B&B&A&8&E&5e&x&B&8&A&8&E&6r&x&B&4&A&9&E&7s&x&B&0&A&9&E&8: " + online + "\n";

        for (Player player : Bukkit.getOnlinePlayers()) {
            String header = headerColor.replace("&D", "&d").replace("&A", "&a").replace("&B", "&b")
                    .replace("&F", "&f").replace("&Q", "&q").replace("&S", "&s");
            String footer = footerColor.replace("&D", "&d").replace("&A", "&a").replace("&B", "&b")
                    .replace("&F", "&f").replace("&O", "&o").replace("&E", "&e");
            player.setPlayerListHeaderFooter(ColorUtils.colorize(header), ColorUtils.colorize(footer));
        }
    }
}
