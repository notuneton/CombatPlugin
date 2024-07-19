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
            String title = ColorUtils.colorize("  &x&4&5&9&2&A&E&lQ&x&4&4&8&B&A&6&lu&x&4&3&8&4&9&E&lo&x&4&2&7&D&9&6&ll&x&4&1&7&6&8&E&ll&x&4&1&7&0&8&7&le&x&4&0&6&9&7&F&le&x&3&F&6&2&7&7&lt&x&3&E&5&B&6&F&l  ");
            objective = scoreboard.registerNewObjective("scoreboard", "dummy", title);
            objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        }
        clearExistingScores(scoreboard);

        String currentTime = ColorUtils.colorize("  &7" + TimeUtils.getCurrentFormattedTime());
        setScore(objective, currentTime, 12);

        setScore(objective, "", 11);

        int onlinePlayers = Bukkit.getOnlinePlayers().size();
        String online = ColorUtils.colorize("  &9Online &7" + onlinePlayers);
        setScore(objective, online, 10);

        UUID uuid = player.getUniqueId();
        int hours = plugin.getConfig().getInt("hour." + uuid);
        int minutes = plugin.getConfig().getInt("minutes." + uuid);
        int seconds = plugin.getConfig().getInt("seconds." + uuid);

        String playtimeString = formatPlaytime(hours, minutes, seconds);
        setScore(objective, playtimeString, 9);

        setScore(objective, "&7  ", 8);
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
        return String.format("%s  &9Playtime &7%dh %dm %ds",
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

    public static void savePlaytime(Player player) {
        UUID uuid = player.getUniqueId();
        int hours = plugin.getConfig().getInt("hour." + uuid);
        int minutes = plugin.getConfig().getInt("minutes." + uuid);
        int seconds = plugin.getConfig().getInt("seconds." + uuid);

        if (seconds >= 60) {
            minutes += seconds / 60;
            seconds %= 60;
        }
        if (minutes >= 60) {
            hours += minutes / 60;
            minutes %= 60;
        }

        plugin.getConfig().set("hour." + uuid, hours);
        plugin.getConfig().set("minutes." + uuid, minutes);
        plugin.getConfig().set("seconds." + uuid, seconds);
        plugin.saveConfig();
    }
}
