package org.main.uneton.utils;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class SoundsUtils {

    public static void playCancerSound(Player player) {
        player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
    }

    public static void playNoteBlockPlingSound(Player player) {
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 3.0f);
    }
}
