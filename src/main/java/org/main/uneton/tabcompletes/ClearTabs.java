package org.main.uneton.tabcompletes;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ClearTabs implements TabCompleter {

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            for (Player loopPlayer : Bukkit.getOnlinePlayers()) {
                completions.add(loopPlayer.getName());
            }
        } else if (args.length == 2) {
            completions.add("10");
            completions.add("6");
        }
        return completions;
    }
}
