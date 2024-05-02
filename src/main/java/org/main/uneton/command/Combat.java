package org.main.uneton.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.main.uneton.admin.ClearChat;

import java.util.HashMap;
import java.util.Map;

public class Combat implements CommandExecutor {

    private final JavaPlugin plugin;
    private final Map<String, CommandExecutor> commandExecutors;

    public Combat(JavaPlugin plugin, Map<String, CommandExecutor> commandExecutors) {
        this.plugin = plugin;
        this.commandExecutors = new HashMap<>();
        commandExecutors.put("clearchat", new ClearChat());
    }


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Only players can execute this command!");
            return true;
        }

        return true;
    }
}
