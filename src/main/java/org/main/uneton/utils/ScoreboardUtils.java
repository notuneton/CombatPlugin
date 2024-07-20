package org.main.uneton.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;
import org.main.uneton.Combat;

import java.util.UUID;

public class ScoreboardUtils {

    private static Combat plugin;
    public ScoreboardUtils(Combat plugin) {
        ScoreboardUtils.plugin = plugin;
    }

    public static void updateScoreboard(Player player) {
        Scoreboard scoreboard = player.getScoreboard();
        Objective objective = scoreboard.getObjective(DisplaySlot.SIDEBAR);

        if (objective == null) {
            String title = ColorUtils.colorize("  &x&4&D&9&8&F&B[&x&6&6&A&6&F&B[ &x&F&F&F&F&F&F&lQ&x&F&F&F&F&F&F&lu&x&F&F&F&F&F&F&lo&x&F&F&F&F&F&F&ll&x&F&F&F&F&F&F&ll&x&F&F&F&F&F&F&le&x&F&F&F&F&F&F&le&x&F&F&F&F&F&F&lt&x&F&F&F&F&F&F&l &x&4&D&9&8&F&B]&x&6&6&A&6&F&B]  ");
            objective = scoreboard.registerNewObjective("scoreboard", "dummy", title);
            objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        }
        clearExistingScores(scoreboard);

        String currentTime = ColorUtils.colorize("&7" + TimeUtils.getCurrentFormattedTime());
        setScore(objective, currentTime, 12);

        setScore(objective, "  &9", 11);

        int onlinePlayers = Bukkit.getOnlinePlayers().size();
        String totalPlayers = String.valueOf(onlinePlayers);
        String online = ColorUtils.colorize("  &fPlayers &a" + totalPlayers);
        setScore(objective, online, 10);

        UUID uuid = player.getUniqueId();
        int hours = plugin.getConfig().getInt("playtime." + uuid + ".hours", 0);
        int minutes = plugin.getConfig().getInt("playtime." + uuid + ".minutes", 0);
        int seconds = plugin.getConfig().getInt("playtime." + uuid + ".seconds", 0);

        String playtimeString = formatPlaytime(hours, minutes, seconds);
        setScore(objective, playtimeString, 9);

        setScore(objective, "  &7", 8);
        player.setScoreboard(scoreboard);
    }

    private static void clearExistingScores(Scoreboard scoreboard) {
        for (String entry : scoreboard.getEntries()) {
            scoreboard.resetScores(entry);
        }
    }

    private static String formatPlaytime(int hours, int minutes, int seconds) {
        if (seconds >= 60) {
            minutes += seconds / 60;
            seconds %= 60;
        }
        if (minutes >= 60) {
            hours += minutes / 60;
            minutes %= 60;
        }
        return String.format("%s  &fPlaytime &e%dh %dm %ds",
                ChatColor.WHITE, hours, minutes, seconds);
    }

    private static void setScore(Objective objective, String text, int score) {
        Score line = objective.getScore(ColorUtils.colorize(text));
        line.setScore(score);
    }

    public static void startUpdatingScoreboard(Player player) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (player.isOnline()) {
                    updateScoreboard(player);
                } else {
                    this.cancel();
                }
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }
}