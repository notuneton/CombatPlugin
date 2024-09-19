package org.main.uneton.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;
import org.main.uneton.Combat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import static org.main.uneton.Combat.playTimes;
import static org.main.uneton.utils.ConfigManager.*;
import static org.main.uneton.utils.NumberFormatter.formatBigNumber;

public class ScoreboardUtils {

    public static void updateScoreboard(Player player, Combat plugin) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (player.isOnline()) {
                    createScoreboard(player);
                } else {
                    this.cancel();
                }
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }

    public static String getCurrentFormattedTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yy");
        LocalDateTime now = LocalDateTime.now();
        return now.format(formatter);
    }

    public static void createScoreboard(Player player) {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard scoreboard = manager.getNewScoreboard();
        Objective objective = scoreboard.getObjective(DisplaySlot.SIDEBAR);
        if (objective == null) {
            objective = scoreboard.registerNewObjective("scoreboard", "owo", ColorUtils.colorize("    "));
            objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        }
        clearExistingScores(scoreboard);

        String clockSymbol = "\u23F0";
        String currentTime = ColorUtils.colorize("&7" + clockSymbol + " " + getCurrentFormattedTime());
        setScore(objective, currentTime, 11);
        setScore(objective, "&1 ", 10);
        setScore(objective, "  &fYou: &6" + player.getName(), 8);

        UUID uuid = player.getUniqueId();
        int playerKills = kills.getOrDefault(uuid, 0);
        int playerDeaths = deaths.getOrDefault(uuid, 0);
        setScore(objective, "  &fDeaths: &a" + formatBigNumber(playerDeaths), 7);
        setScore(objective, "  &fKills: &a" + formatBigNumber(playerKills), 6);

        String kdRatio;
        if (playerDeaths > 0) {
            double ratio = (double) playerKills / playerDeaths;
            kdRatio = String.format("  &fKDR: &c%.2f", ratio);
        } else {
            kdRatio = "  &fKDR: &7&oNaN";
        }
        setScore(objective, kdRatio,5);

        setScore(objective, "&2 ", 4);
        int ping = player.getPing();
        setScore(objective, "  &fPing: &d " + String.format(ping + "ms"), 3);

        int onlinePlayers = Bukkit.getOnlinePlayers().size();
        String totalPlayers = String.valueOf(onlinePlayers);
        String online = ColorUtils.colorize("  &fPlayers: &a" + totalPlayers);
        setScore(objective, online, 2);

        int playtimeSeconds = playTimes.getOrDefault(uuid, 0);
        int hours = playtimeSeconds / 3600;
        int minutes = (playtimeSeconds % 3600) / 60;
        int seconds = playtimeSeconds % 60;
        String playtimeString = formatPlaytime(hours, minutes, seconds);
        setScore(objective, playtimeString, 1);

        setScore(objective, "&3 ", 0);
        player.setScoreboard(scoreboard);
    }

    public static String formatPlaytime(int hours, int minutes, int seconds) {
        if (seconds >= 60) {
            hours += minutes / 60;
            seconds %= 60;
        }
        if (minutes >= 60) {
            hours += minutes / 60;
            minutes %= 60;
        }
        return String.format("  &fPlaytime: &e%dh %dm %ds", hours, minutes, seconds);
    }

    private static void clearExistingScores(Scoreboard scoreboard) {
        for (String entry : scoreboard.getEntries()) {
            scoreboard.resetScores(entry);
        }
    }
    private static void setScore(Objective objective, String text, int score) {
        Score line = objective.getScore(ColorUtils.colorize(text));
        line.setScore(score);
    }
}
