package org.main.uneton.comvanilla;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.main.uneton.Combat;
import org.main.uneton.utils.ColorUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Sell implements CommandExecutor {

    private final Combat plugin;
    private final Map<UUID, Double> playerBalances = new HashMap<>();
    public Sell(Combat plugin) {
        this.plugin = plugin;
    }


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player = (Player) sender;
        double profit = 0.0;

        // Example for selling dirt
        ItemStack dirt = player.getInventory().getItemInMainHand();
        if (dirt!= null && dirt.getType() == Material.DIRT) {
            double dirtAmount = dirt.getAmount();
            player.getInventory().removeItem(dirt);
            profit += dirtAmount * 0.10; // Assuming 10% profit for dirt
        }


        // Update player's balance
        playerBalances.put(player.getUniqueId(), playerBalances.getOrDefault(player.getUniqueId(), 0.0) + profit);

        // Send profit message
        player.sendMessage(ColorUtils.colorize("&aYou have earned &e" + profit + "&a coins for selling!"));

        return true;
    }
}
