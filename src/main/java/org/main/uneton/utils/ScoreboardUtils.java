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
        this.plugin = plugin;
    }

    public static void updateScoreboard(Player player) {
        Scoreboard scoreboard = player.getScoreboard();
        Objective objective = scoreboard.getObjective(DisplaySlot.SIDEBAR);

        if (objective == null) {
            objective = scoreboard.registerNewObjective("scoreboard", "dummy", ColorUtils.colorize("  &x&4&5&9&2&A&E&lQ&x&4&4&8&B&A&6&lu&x&4&3&8&4&9&E&lo&x&4&2&7&D&9&6&ll&x&4&1&7&6&8&E&ll&x&4&1&7&0&8&7&le&x&4&0&6&9&7&F&le&x&3&F&6&2&7&7&lt&x&3&E&5&B&6&F&l  "));
            objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        }

        for (String entry : scoreboard.getEntries()) {
            scoreboard.resetScores(entry);
        }

        String currentTime = "  &7" + TimeUtils.getCurrentFormattedTime();
        setScore(objective, currentTime, 6);

        setScore(objective, "ei", 5);

        int onlinePlayers = Bukkit.getOnlinePlayers().size();
        String online = ChatColor.WHITE + "  &9Online: &7" + onlinePlayers;
        setScore(objective, online, 3);

        UUID uuid = player.getUniqueId();
        int hours = plugin.getConfig().getInt("hour." + uuid);
        int minutes = plugin.getConfig().getInt("minutes." + uuid);
        int seconds = plugin.getConfig().getInt("seconds." + uuid);

        String playtimeString = getString(hours, minutes, seconds);
        setScore(objective, playtimeString, 2);

        setScore(objective, "kyllä", 1);
        player.setScoreboard(scoreboard);
    }


    private static String getString(int hours, int minutes, int seconds) {
        boolean hoursExceed60 = hours > 60;
        boolean minutesExceed60 = minutes > 60;
        if (seconds > 60) {
            minutes += seconds / 60;
            seconds %= 60;
        }
        String playtimeString = ChatColor.WHITE + "  &9Playtime: &7";
        if (hoursExceed60) {
            playtimeString += hours + " h1 ";
        } else {
            playtimeString += hours + "h ";
        }
        if (minutesExceed60) {
            playtimeString += minutes + " m1 ";
        } else {
            playtimeString += minutes + "m ";
        }
        playtimeString += seconds + "s";
        return playtimeString;
    }

    private static void setScore(Objective objective, String text, int score) {
        Score line = objective.getScore(ChatColor.translateAlternateColorCodes('&', text));
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

