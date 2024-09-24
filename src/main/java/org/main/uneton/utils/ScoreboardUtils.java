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
import static org.main.uneton.utils.NumberFormatter.*;

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

        setScore(objective, "&1 ", 10);
        UUID uuid = player.getUniqueId();

        int playerDeaths = deaths.getOrDefault(uuid, 0);
        setScore(objective, "&x&6&3&9&2&B&8D&x&7&0&9&3&C&4e&x&7&D&9&4&D&0a&x&8&A&9&6&D&Ct&x&9&6&9&7&E&7h&x&A&3&9&8&F&3s&x&B&0&9&9&F&F: &f" + formatBigNumber(playerDeaths), 7);
        int playerKills = kills.getOrDefault(uuid, 0);
        setScore(objective, "&x&6&3&9&2&B&8K&x&7&2&9&3&C&6i&x&8&2&9&5&D&4l&x&9&1&9&6&E&3l&x&A&1&9&8&F&1s&x&B&0&9&9&F&F: &f" + formatBigNumber(playerKills), 6);

        String kdRatio;
        if (playerDeaths > 0) {
            double ratio = (double) playerKills / playerDeaths;
            kdRatio = String.format("&x&6&3&9&2&B&8K&x&7&D&9&4&D&0D&x&9&6&9&7&E&7R&x&B&0&9&9&F&F: &f%.2f", ratio);
        } else {
            kdRatio = "&x&6&3&9&2&B&8K&x&7&D&9&4&D&0D&x&9&6&9&7&E&7R&x&B&0&9&9&F&F: &f&oNaN";
        }
        setScore(objective, kdRatio,5);

        int playerCoins = some_coins.getOrDefault(uuid, 0);
        setScore(objective, "&x&6&3&9&2&B&8C&x&7&2&9&3&C&6o&x&8&2&9&5&D&4i&x&9&1&9&6&E&3n&x&A&1&9&8&F&1s&x&B&0&9&9&F&F: &f" + formatLargeCoinAmount(playerCoins), 3);

        int onlinePlayers = Bukkit.getOnlinePlayers().size();
        String totalPlayers = String.valueOf(onlinePlayers);
        String online = ColorUtils.colorize("&x&6&3&9&2&B&8P&x&6&E&9&3&C&2l&x&7&9&9&4&C&Ca&x&8&4&9&5&D&6y&x&8&F&9&6&E&1e&x&9&A&9&7&E&Br&x&A&5&9&8&F&5s&x&B&0&9&9&F&F: &f" + totalPlayers);
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
