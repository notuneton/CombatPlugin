package org.main.uneton.utils;

import org.bukkit.ChatColor;

public class ChatUtils {

    public static String translateHexColorCodes(String message) {
        StringBuilder translated = new StringBuilder();
        char[] b = message.toCharArray();
        for (int i = 0; i < b.length; i++) {
            if (b[i] == '&' && i + 1 < b.length && b[i + 1] == '#') {
                StringBuilder hex = new StringBuilder();
                if (i + 7 < b.length) {
                    for (int j = 2; j < 8; j++) {
                        hex.append(b[i + j]);
                    }
                    try {
                        translated.append(ChatColor.valueOf(hex.toString()));
                        i += 7;
                        continue;
                    } catch (Exception ignored) {
                    }
                }
            }
            translated.append(b[i]);
        }
        return translated.toString();
    }
}
