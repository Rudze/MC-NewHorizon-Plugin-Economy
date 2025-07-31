package fr.rudy.economy.manager;

import java.sql.*;
import java.util.UUID;
import org.bukkit.Bukkit;

public class EconomyManager {
    private final Connection database;

    public EconomyManager(Connection database) {
        this.database = database;
    }

    private void ensurePlayerExists(UUID player) {
        try (PreparedStatement ps = database.prepareStatement(
                "INSERT OR IGNORE INTO economy (uuid, money) VALUES (?, 0.0);")) {
            ps.setString(1, player.toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public double getMoney(UUID player) {
        ensurePlayerExists(player);
        try (PreparedStatement statement = database.prepareStatement(
                "SELECT money FROM economy WHERE uuid = ?;")) {
            statement.setString(1, player.toString());
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                double balance = rs.getDouble("money");
                return balance;
            }
        } catch (SQLException e) {
            Bukkit.getLogger().severe("❌ getMoney: " + e.getMessage());
            e.printStackTrace();
        }
        return 0.0;
    }

    public void setMoney(UUID player, double amount) {
        ensurePlayerExists(player);
        try (PreparedStatement statement = database.prepareStatement(
                "UPDATE economy SET money = ? WHERE uuid = ?;")) {
            statement.setDouble(1, amount);
            statement.setString(2, player.toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addMoney(UUID player, double amount) {
        double newAmount = getMoney(player) + amount;
        setMoney(player, newAmount);
    }
}
