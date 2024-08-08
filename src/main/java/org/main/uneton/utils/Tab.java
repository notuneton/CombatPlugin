package org.main.uneton.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Tab {

    public static void updateTab() {
        String headercolor = "\n &x&4&D&9&8&F&B[&x&6&6&A&6&F&B[ &x&F&F&F&F&F&F&lQ&x&F&F&F&F&F&F&lu&x&F&F&F&F&F&F&lo&x&F&F&F&F&F&F&ll&x&F&F&F&F&F&F&ll&x&F&F&F&F&F&F&le&x&F&F&F&F&F&F&le&x&F&F&F&F&F&F&lt&x&F&F&F&F&F&F&l &x&4&D&9&8&F&B]&x&6&6&A&6&F&B] \n";
        for (Player player : Bukkit.getOnlinePlayers()) {
            int ping = player.getPing();
            double tps = getServerTPS();
            String footercolor = "\n &7ping&8: " + "&3" + ping + "ms &d&k| &7tps&8: &3" + String.format("%.2f", tps) + "\n";;

            String header = headercolor.replace("&D", "&d").replace("&A", "&a").replace("&B", "&b")
                    .replace("&F", "&f").replace("&Q", "&q").replace("&S", "&s");
            String footer = footercolor.replace("&D", "&d").replace("&A", "&a").replace("&B", "&b")
                    .replace("&F", "&f").replace("&O", "&o").replace("&E", "&e");

            player.setPlayerListHeaderFooter(ColorUtils.colorize(header), ColorUtils.colorize(footer));
        }
    }

    private static double getServerTPS() {
        return Bukkit.getServer().getTPS()[0];
    }
}
