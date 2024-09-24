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

    public static String getCurrentTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm a");
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
        clearExistingLines(scoreboard);

        String now = ColorUtils.colorize("&7" + getCurrentTime());
        setScore(objective, now, 13);

        setScore(objective, "&fThis game is under ", 11);
        setScore(objective, "&fHeavy development! ", 10);

        UUID uuid = player.getUniqueId();
        int playerDeaths = deaths.getOrDefault(uuid, 0);
        setScore(objective, "&fDeaths &a" + formatLargeNumberIntoChar(playerDeaths), 8);
        int playerKills = kills.getOrDefault(uuid, 0);
        setScore(objective, "&fKills: &a" + formatLargeNumberIntoChar(playerKills), 7);

        int playtimeSeconds = playTimes.getOrDefault(uuid, 0);
        int hours = playtimeSeconds / 3600;
        int minutes = (playtimeSeconds % 3600) / 60;
        int seconds = playtimeSeconds % 60;
        String playtimeString = formatPlaytime(hours, minutes, seconds);
        setScore(objective, playtimeString, 5);

        int onlinePlayers = Bukkit.getOnlinePlayers().size();
        String totalPlayers = String.valueOf(onlinePlayers);
        String online = ColorUtils.colorize("&fPlayers: &2" + totalPlayers);
        setScore(objective, online, 4);

        int playerCoins = some_coins.getOrDefault(uuid, 0);
        setScore(objective, "&fCoins: &6" + formatWithComma(playerCoins), 1);

        setScore(objective, "&5 ", 12);
        setScore(objective, "&4 ", 9);
        setScore(objective, "&3 ", 6);
        setScore(objective, "&2 ", 3);
        setScore(objective, "&1 ", 0);
        player.setScoreboard(scoreboard);
    }

    private static void clearExistingLines(Scoreboard scoreboard) {
        for (String entry : scoreboard.getEntries()) {
            scoreboard.resetScores(entry);
        }
    }
    private static void setScore(Objective objective, String text, int score) {
        Score line = objective.getScore(ColorUtils.colorize(text));
        line.setScore(score);
    }
}
