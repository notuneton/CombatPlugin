package org.main.uneton.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Tab {

    public static void updateTab() {
        String headercolor = "\n &x&2&A&3&9&F&B[&x&3&C&9&A&F&B[ &x&F&F&F&F&F&F&lQ&x&F&F&F&F&F&F&lu&x&F&F&F&F&F&F&lo&x&F&F&F&F&F&F&ll&x&F&F&F&F&F&F&ll&x&F&F&F&F&F&F&le&x&F&F&F&F&F&F&le&x&F&F&F&F&F&F&lt&x&F&F&F&F&F&F&l &x&3&C&9&A&F&B]&x&2&A&3&9&F&B] \n";
        for (Player player : Bukkit.getOnlinePlayers()) {

            //todo &x&2&B&7&9&F&F&lt&x&2&B&7&9&F&F&le&x&2&B&7&9&F&F&ls&x&2&B&7&9&F&F&lt&x&2&B&7&9&F&F&l1 hex how i can make it into a variable that can be used later there?
            String footercolor = "\n &7Olet palvelimella&8: &x&2&B&7&9&F&F&lt&x&2&B&7&9&F&F&le&x&2&B&7&9&F&F&ls&x&2&B&7&9&F&F&lti&x&2&B&7&9&F&F&l1 \n";
            String header = headercolor.replace("&D", "&d").replace("&A", "&a").replace("&B", "&b")
                    .replace("&F", "&f").replace("&Q", "&q").replace("&S", "&s");
            String footer = footercolor.replace("&D", "&d").replace("&A", "&a").replace("&B", "&b")
                    .replace("&F", "&f").replace("&O", "&o").replace("&E", "&e");

            player.setPlayerListHeaderFooter(ColorUtils.colorize(header), ColorUtils.colorize(footer));
        }
    }

    private static double getServerTPS() {
        return Bukkit.getServer().getTPS()[0];
        // "&7tps&8: &3" + String.format("%.2f", tps);  double tps = getServerTPS();
    }
    private static int getPing(Player player) {
        int ping = player.getPing();
        return ping;
    }
}
