package org.main.uneton.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;
import org.main.uneton.Combat;

import static org.main.uneton.combatlogger.CombatLog.combat_tagged;

public class ScoreboardUtils {

    private static Combat plugin;
    public ScoreboardUtils(Combat plugin) {
        ScoreboardUtils.plugin = plugin;
    }

    public static void updateScoreboard(Player player) {
        if (plugin == null) {
            throw new IllegalStateException("ScoreboardUtils not initialized with plugin instance.");
        }

        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard board = manager.getNewScoreboard();

        Objective objective = board.registerNewObjective("scoreboard", "dummy", ColorUtils.colorize("  &x&F&F&F&F&F&F&lQ&x&F&F&F&F&F&F&lu&x&F&F&F&F&F&F&lo&x&F&F&F&F&F&F&ll&x&F&F&F&F&F&F&ll&x&F&F&F&F&F&F&le&x&F&F&F&F&F&F&le&x&F&F&F&F&F&F&lt&x&F&F&F&F&F&F&l.&x&F&F&F&F&F&F&lc&x&F&F&F&F&F&F&lo  "));
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        // set scoreboard lines
        setScore(objective, ChatColor.GRAY+ player.getName(), 15);
        if (combat_tagged.containsKey(player)) {
            setScore(objective, " ", 14);
        }
        // Get player's playtime in hours and minutes
        int hours = plugin.getConfig().getInt("hour." + player.getUniqueId());
        int minutes = plugin.getConfig().getInt("minute." + player.getUniqueId());

        // Format playtime string
        String playtimeString = ChatColor.WHITE + "  &fPlaytime &9" + hours + "h " + minutes + " minutes";
        setScore(objective, playtimeString, 13);

        // this.getConfig().getInt("hour." + player.getUniqueId()) + "h " + this.getConfig().getInt("minute." + player.getUniqueId()) + " minutes&f."

        // Add blank line
        setScore(objective," ", 12);

        player.setScoreboard(board);
    }

    private static void setScore(Objective objective, String text, int score) {
        Score line = objective.getScore(ChatColor.translateAlternateColorCodes('&', text));
        line.setScore(score);
    }
}
