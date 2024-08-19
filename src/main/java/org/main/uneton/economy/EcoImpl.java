package org.main.uneton.economy;
/*
import net.milkbowl.vault.economy.AbstractEconomy;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.main.uneton.Combat;

import java.util.List;
import java.util.UUID;

public class EcoImpl extends AbstractEconomy {

    private Combat plugin = Combat.getInstance();

    @Override
    public boolean isEnabled() {
        return false;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public boolean hasBankSupport() {
        return false;
    }

    @Override
    public int fractionalDigits() {
        return 0;
    }

    @Override
    public String format(double amount) {
        return null;
    }

    @Override
    public String currencyNamePlural() {
        return null;
    }

    @Override
    public String currencyNameSingular() {
        return null;
    }

    @Override
    public boolean hasAccount(String playerName) {
        return false;
    }

    @Override
    public boolean hasAccount(OfflinePlayer player) {
        return false;
    }

    @Override
    public boolean hasAccount(String playerName, String worldName) {
        return false;
    }

    @Override
    public boolean hasAccount(OfflinePlayer player, String worldName) {
        return false;
    }

    public double getBalance(String s) {
        Player player = Bukkit.getPlayer(s);
        UUID uuid = player.getUniqueId();
        return Combat.economy.get(uuid);
    }

    @Override
    public double getBalance(OfflinePlayer player) {
        UUID uuid = player.getUniqueId();
        return Combat.economy.get(uuid);
    }

    @Override
    public double getBalance(String str, String s1) {
        Player player = Bukkit.getPlayer(str);
        UUID uuid = player.getUniqueId();
        return Combat.economy.get(uuid);
    }

    @Override
    public double getBalance(OfflinePlayer offlinePlayer, String s) {
        UUID uuid = offlinePlayer.getUniqueId();
        return Combat.economy.get(uuid);
    }

    @Override
    public boolean has(String playerName, double amount) {
        return false;
    }

    @Override
    public boolean has(OfflinePlayer player, double amount) {
        return false;
    }

    @Override
    public boolean has(String playerName, String worldName, double amount) {
        return false;
    }

    @Override
    public boolean has(OfflinePlayer player, String worldName, double amount) {
        return false;
    }

    @Override
    public EconomyResponse withdrawPlayer(String playerName, double amount) {
        Player player = Bukkit.getPlayer(playerName);
        UUID uuid = player.getUniqueId();
        double removeAmount = Combat.economy.get(uuid);
        if (amount >= removeAmount){
            Combat.economy.remove(uuid, removeAmount);
        }
        return null;
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer offlinePlayer, double amount) {
        UUID uuid = offlinePlayer.getUniqueId();
        double removeAmount = Combat.economy.get(uuid);
        if (amount >= removeAmount){
            Combat.economy.remove(uuid, removeAmount);
        }
        return null;
    }

    @Override
    public EconomyResponse withdrawPlayer(String playerName, String worldName, double amount) {
        Player player = Bukkit.getPlayer(playerName);
        UUID uuid = player.getUniqueId();
        double removeAmount = Combat.economy.get(uuid);
        if (amount >= removeAmount){
            Combat.economy.remove(uuid, removeAmount);
        }
        return null;
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer player, String worldName, double amount) {
        UUID uuid = player.getUniqueId();
        double removeAmount = Combat.economy.get(uuid);
        if (amount >= removeAmount){
            Combat.economy.remove(uuid, removeAmount);
        }
        return null;
    }

    @Override
    public EconomyResponse depositPlayer(String user, double amount) {
        Player player = Bukkit.getPlayer(user);
        UUID uuid = player.getUniqueId();
        double oldBalance = Combat.economy.get(uuid);
        Combat.economy.put(uuid, oldBalance + amount);
        return null;
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer offlinePlayer, double amount) {
        UUID uuid = offlinePlayer.getUniqueId();
        Combat.economy.putIfAbsent(uuid, 0.0);
        double oldBalance = Combat.economy.get(uuid);
        Combat.economy.put(uuid, oldBalance + amount);
        return null;
    }

    @Override
    public EconomyResponse depositPlayer(String playerName, String worldName, double amount) {
        Player player = Bukkit.getPlayer(playerName);
        UUID uuid = player.getUniqueId();
        double oldBalance = Combat.economy.get(uuid);
        Combat.economy.put(uuid, oldBalance + amount);
        return null;
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer player, String s, double v) {
        UUID uuid = player.getUniqueId();
        double oldBalance = Combat.economy.get(uuid);
        Combat.economy.put(uuid, oldBalance + v);
        return null;
    }

    @Override
    public EconomyResponse createBank(String name, String player) {
        return null;
    }

    @Override
    public EconomyResponse createBank(String name, OfflinePlayer player) {
        return null;
    }

    @Override
    public EconomyResponse deleteBank(String name) {
        return null;
    }

    @Override
    public EconomyResponse bankBalance(String name) {
        return null;
    }

    @Override
    public EconomyResponse bankHas(String name, double amount) {
        return null;
    }

    @Override
    public EconomyResponse bankWithdraw(String name, double amount) {
        return null;
    }

    @Override
    public EconomyResponse bankDeposit(String name, double amount) {
        return null;
    }

    @Override
    public EconomyResponse isBankOwner(String name, String playerName) {
        return null;
    }

    @Override
    public EconomyResponse isBankOwner(String name, OfflinePlayer player) {
        return null;
    }

    @Override
    public EconomyResponse isBankMember(String name, String playerName) {
        return null;
    }

    @Override
    public EconomyResponse isBankMember(String name, OfflinePlayer player) {
        return null;
    }

    @Override
    public List<String> getBanks() {
        return null;
    }

    @Override
    public boolean createPlayerAccount(String playerName) {
        return false;
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer player) {
        return false;
    }

    @Override
    public boolean createPlayerAccount(String playerName, String worldName) {
        return false;
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer player, String worldName) {
        return false;
    }
}

 */