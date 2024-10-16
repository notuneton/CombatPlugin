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
    
    public static void createScoreboard(Player player) {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard scoreboard = manager.getNewScoreboard();
        Objective objective = scoreboard.getObjective(DisplaySlot.SIDEBAR);
        if (objective == null) {
            objective = scoreboard.registerNewObjective("scoreboard", "owo", ColorUtils.colorize("    "));
            objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        }
        clearExistingLines(scoreboard);

        UUID uuid = player.getUniqueId();
        int playerDeaths = deaths.getOrDefault(uuid, 0);
        setScore(objective, "&fDeaths &a" + formatLargeNumberIntoChar(playerDeaths), 9);
        int playerKills = kills.getOrDefault(uuid, 0);
        setScore(objective, "&fKills &a" + formatLargeNumberIntoChar(playerKills), 8);

        int playtimeSeconds = playTimes.getOrDefault(uuid, 0);
        int hours = playtimeSeconds / 3600;
        int minutes = (playtimeSeconds % 3600) / 60;
        int seconds = playtimeSeconds % 60;
        String playtimeString = formatPlaytime(hours, minutes, seconds); // Format string to show hours and minutes
        setScore(objective, playtimeString, 6);

        int ping = player.getPing();
        setScore(objective, "&fLatency &d" + String.format(ping + "ms"), 5);
        int onlinePlayers = Bukkit.getOnlinePlayers().size();
        String totalPlayers = String.valueOf(onlinePlayers);
        String online = ColorUtils.colorize("&fOnline players &2" + totalPlayers);
        setScore(objective, online, 4);

        setScore(objective, "&1 ", 10);
        setScore(objective, "&7 ", 7);
        setScore(objective, "&3 ", 3);
        String current = ColorUtils.colorize("&7" + getCurrentTime());
        setScore(objective, current, 0);
        player.setScoreboard(scoreboard);
    }

    public static String getCurrentTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy"); //  hh:mm a = clock time
        LocalDateTime now = LocalDateTime.now();
        return now.format(formatter);
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
