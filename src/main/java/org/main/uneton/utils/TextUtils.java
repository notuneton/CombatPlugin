package org.main.uneton.utils;

import org.bukkit.ChatColor;

public class TextUtils {
    public TextUtils() {
    }

    public static String formatText(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }
}
