package org.main.uneton.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;
import org.main.uneton.Combat;

import java.util.HashMap;
import java.util.UUID;

import static org.main.uneton.Combat.playTimes;

public class ScoreboardUtils {

    public static final HashMap<UUID, Integer> kills = new HashMap<>();
    public static final HashMap<UUID, Integer> deaths = new HashMap<>();

    public static void startUpdatingScoreboard(Player player, Combat plugin) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (player.isOnline()) {
                    createScoreboard(player);
                } else {
                    this.cancel();
                }
            }
        }.runTaskTimer(plugin, 0L, 10L);
    }

    public static void createScoreboard(Player player) {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard scoreboard = manager.getNewScoreboard();
        Objective objective = scoreboard.getObjective(DisplaySlot.SIDEBAR);
        if (objective == null) {
            objective = scoreboard.registerNewObjective("scoreboard", "dummy", "");
            objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        }

        clearExistingScores(scoreboard);
        String currentTime = ColorUtils.colorize("&7" + TimeUtils.getCurrentFormattedTime());
        setScore(objective, currentTime, 12);
        setScore(objective, "&9 ", 11);

        int onlinePlayers = Bukkit.getOnlinePlayers().size();
        String totalPlayers = String.valueOf(onlinePlayers);
        String online = ColorUtils.colorize("  &fPlayers: &a" + totalPlayers);
        setScore(objective, online, 10);

        int ping = player.getPing();
        double tps = getServerTPS();
        //  setScore(objective , "  &fTPS &9" + String.format("%.2f", tps), 9);
        setScore(objective, "  &fPing: &9" + String.format(ping + "ms"), 8);

        setScore(objective, "&7 ", 8);
        UUID uuid = player.getUniqueId();
        int playtimeSeconds = playTimes.getOrDefault(uuid, 0);
        int hours = playtimeSeconds / 3600;
        int minutes = (playtimeSeconds % 3600) / 60;
        int seconds = playtimeSeconds % 60;

        String playtimeString = formatPlaytime(hours, minutes, seconds);
        setScore(objective, playtimeString, 7);
        int playerKills = kills.getOrDefault(uuid, 0);
        int playerDeaths = deaths.getOrDefault(uuid, 0);
        setScore(objective, "  &fDeaths: &a" + playerDeaths, 6);
        setScore(objective, "  &fKills: &a" + playerKills, 5);

        setScore(objective, "&8 ", 0);
        player.setScoreboard(scoreboard);
    }

    public static void addKill(UUID playeruuid) {
        kills.put(playeruuid, kills.getOrDefault(playeruuid, 0) + 1);
    }
    public static void addDeath(UUID playeruuid) {
        deaths.put(playeruuid, deaths.getOrDefault(playeruuid, 0) + 1);
    }
    private static double getServerTPS() {
        return Bukkit.getServer().getTPS()[0];
    }

    private static String formatPlaytime(int hours, int minutes, int seconds) {
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
