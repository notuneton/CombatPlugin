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
        if (!(sender instanceof Player)) {
            return completions;
        }

        if (args.length == 1) {
            for (Player loopPlayer : Bukkit.getOnlinePlayers()) {
                completions.add(loopPlayer.getName());
            }
            completions.add("ground_items");
            completions.add("living_entities");
        }
        if (args.length == 2) {
            String firstArg = args[0].toLowerCase();
            if (firstArg.equals("ground_items") || firstArg.equals("living_entities")) {
                completions.add("100");
                completions.add("80");
                completions.add("60");
                completions.add("40");
                completions.add("30");
                completions.add("25");
                completions.add("10");
            }
        }

        if (args.length >= 3) {
            return completions;
        }

        String currentArg = args[args.length - 1].toLowerCase();
        completions.removeIf(option -> !option.toLowerCase().startsWith(currentArg));
        return completions;
    }
}
