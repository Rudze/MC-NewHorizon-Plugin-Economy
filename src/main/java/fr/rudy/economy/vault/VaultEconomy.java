package fr.rudy.economy.vault;

import fr.rudy.economy.manager.EconomyManager;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.List;

public class VaultEconomy implements Economy {
    private final EconomyManager economyManager;

    public VaultEconomy(EconomyManager economyManager) {
        this.economyManager = economyManager;
    }

    @Override public boolean isEnabled() { return true; }
    @Override public String getName() { return "VaultEconomy"; }
    @Override public boolean hasBankSupport() { return false; }
    @Override public int fractionalDigits() { return 2; }
    @Override public String format(double amount) { return String.format("%.2f coins", amount); }
    @Override public String currencyNamePlural() { return "coins"; }
    @Override public String currencyNameSingular() { return "coin"; }

    @Override
    public boolean hasAccount(OfflinePlayer player) {
        return true;
    }

    @Override public double getBalance(OfflinePlayer player) {
        double bal = economyManager.getMoney(player.getUniqueId());
        return bal;
    }

    @Override public boolean has(OfflinePlayer player, double amount) {
        boolean ok = getBalance(player) >= amount;
        return ok;
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer player, double amount) {
        double current = getBalance(player);
        if (current < amount) {
            return new EconomyResponse(amount, current, EconomyResponse.ResponseType.FAILURE, "Not enough money");
        }
        economyManager.setMoney(player.getUniqueId(), current - amount);
        return new EconomyResponse(amount, current - amount, EconomyResponse.ResponseType.SUCCESS, null);
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer player, double amount) {
        double current = getBalance(player);
        economyManager.setMoney(player.getUniqueId(), current + amount);
        return new EconomyResponse(amount, current + amount, EconomyResponse.ResponseType.SUCCESS, null);
    }

    @Override public boolean hasAccount(String s) { return false; }
    @Override public boolean hasAccount(String s, String s1) { return false; }
    @Override public boolean hasAccount(OfflinePlayer player, String world) { return true; }
    @Override public double getBalance(String s) { return 0; }
    @Override public double getBalance(String s, String s1) { return 0; }
    @Override public double getBalance(OfflinePlayer player, String world) { return getBalance(player); }
    @Override public boolean has(String s, double v) { return false; }
    @Override public boolean has(String s, String s1, double v) { return false; }
    @Override public boolean has(OfflinePlayer player, String w, double a) { return has(player, a); }
    @Override public EconomyResponse withdrawPlayer(String s, double v) { return null; }
    @Override public EconomyResponse withdrawPlayer(String s, String s1, double v) { return null; }
    @Override public EconomyResponse withdrawPlayer(OfflinePlayer p, String w, double v) { return withdrawPlayer(p, v); }
    @Override public EconomyResponse depositPlayer(String s, double v) { return null; }
    @Override public EconomyResponse depositPlayer(String s, String s1, double v) { return null; }
    @Override public EconomyResponse depositPlayer(OfflinePlayer p, String w, double v) { return depositPlayer(p, v); }
    @Override public EconomyResponse createBank(String s, String s1) { return null; }
    @Override public EconomyResponse createBank(String n, OfflinePlayer p) { return null; }
    @Override public EconomyResponse deleteBank(String n) { return null; }
    @Override public EconomyResponse bankBalance(String n) { return null; }
    @Override public EconomyResponse bankHas(String n, double a) { return null; }
    @Override public EconomyResponse bankWithdraw(String n, double a) { return null; }
    @Override public EconomyResponse bankDeposit(String n, double a) { return null; }
    @Override public EconomyResponse isBankOwner(String b, String p) { return null; }
    @Override public EconomyResponse isBankOwner(String b, OfflinePlayer p) { return null; }
    @Override public EconomyResponse isBankMember(String b, String p) { return null; }
    @Override public EconomyResponse isBankMember(String b, OfflinePlayer p) { return null; }
    @Override public List<String> getBanks() { return null; }
    @Override public boolean createPlayerAccount(String s) { return false; }
    @Override public boolean createPlayerAccount(OfflinePlayer p) { return true; }
    @Override public boolean createPlayerAccount(String s, String s1) { return false; }
    @Override public boolean createPlayerAccount(OfflinePlayer p, String w) { return true; }
}
