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

    private static Combat plugin;
    private static final HashMap<UUID, Integer> kills = new HashMap<>();
    private static final HashMap<UUID, Integer> deaths = new HashMap<>();
    private static final HashMap<UUID, Integer> selfDeaths = new HashMap<>();

    public ScoreboardUtils(Combat plugin) {
        ScoreboardUtils.plugin = plugin;
    }

    public static void startUpdatingScoreboard(Player player, Combat instance) {
        plugin = instance;
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

    public static void updateScoreboard(Player player) {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard scoreboard = manager.getNewScoreboard();
        Objective objective = scoreboard.getObjective(DisplaySlot.SIDEBAR);

        if (objective == null) {
            String title = ColorUtils.colorize("  &e&lPROTOTYPE  ");
            objective = scoreboard.registerNewObjective("scoreboard", "dummy", title);
            objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        }
        clearExistingScores(scoreboard);

        // int ping = player.getPing();
        // String currentTime = ColorUtils.colorize("&7" + TimeUtils.getCurrentFormattedTime() + " &7(" + (String.format("&3"+ping+"ms")+"&7)"));
        // setScore(objective, currentTime, 12);
        String currentTime = ColorUtils.colorize("&7" + TimeUtils.getCurrentFormattedTime());
        setScore(objective, currentTime, 12);

        setScore(objective, "&a ", 11);
        int onlinePlayers = Bukkit.getOnlinePlayers().size();
        String totalPlayers = String.valueOf(onlinePlayers);
        String online = ColorUtils.colorize("  &fPlayers &a" + totalPlayers);
        setScore(objective, online, 10);

        UUID uuid = player.getUniqueId();
        int playtimeSeconds = playTimes.getOrDefault(uuid, 0);

        int hours = playtimeSeconds / 3600;
        int minutes = (playtimeSeconds % 3600) / 60;
        // int seconds = playtimeSeconds % 60;

        String playtimeString = formatPlaytime(hours, minutes);
        setScore(objective, playtimeString, 9);

        int playerSelfDeaths = selfDeaths.getOrDefault(uuid, 0);
        setScore(objective, "  &fSelf Deaths &a" + playerSelfDeaths, 8);
        int playerKills = kills.getOrDefault(uuid, 0);
        int playerDeaths = deaths.getOrDefault(uuid, 0);
        setScore(objective, "  &fDeaths &6" + playerDeaths, 7);
        setScore(objective, "  &fKills &a" + playerKills, 6);

        setScore(objective, "&8 ", 5);

        player.setScoreboard(scoreboard);
    }

    public static void addKill(UUID playeruuid) {
        kills.put(playeruuid, deaths.getOrDefault(playeruuid, 0) + 1);
    }

    public static void addDeath(UUID playeruuid) {
        deaths.put(playeruuid, deaths.getOrDefault(playeruuid, 0) + 1);
    }
    public static void addSelfDeath(UUID playeruuid) {
        selfDeaths.put(playeruuid, selfDeaths.getOrDefault(playeruuid, 0) + 1);
    }

    private static String formatPlaytime(int hours, int minutes) {
        if (minutes >= 60) {
            hours += minutes / 60;
            minutes %= 60;
        }
        return String.format("  &fPlaytime &e%dh %dm", hours, minutes);
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