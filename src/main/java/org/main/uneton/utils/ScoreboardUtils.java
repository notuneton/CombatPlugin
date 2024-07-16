package org.main.uneton.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

public class ScoreboardUtils {

    public static void updateScoreboard(Player player) {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard board = manager.getNewScoreboard();

        Objective objective = board.registerNewObjective("scoreboard", "dummy", ChatColor.translateAlternateColorCodes('&', "&4&d9&8f&b[&6&6a&6f&b[ &f&f&f&f&f&f&lQ&f&f&f&f&f&f&lu&f&f&f&f&f&f&lo&f&f&f&f&f&f&ll&f&f&f&f&f&f&ll&f&f&f&f&f&f&le&f&f&f&f&f&f&le&f&f&f&f&f&f&lt&f&f&f&f&f&f&l.&f&f&f&f&f&f&lc&f&f&f&f&f&f&lo &4&d9&8f&b]&6&6a&6f&b]"));
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        // set scoreboard lines
        setScore(objective, "8", 15);
        setScore(objective, "&8", 14);

        player.setScoreboard(board);

    }

    private static void setScore(Objective objective, String text, int score) {
        Score line = objective.getScore(ChatColor.translateAlternateColorCodes('&', text));
        line.setScore(score);
    }
}
