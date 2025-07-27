package fr.rudy.economy.manager;

import java.sql.*;
import java.util.UUID;

public class EconomyManager {
    private final Connection database;

    public EconomyManager(Connection database) {
        this.database = database;
    }

    public double getMoney(UUID player) {
        try (PreparedStatement statement = database.prepareStatement(
                "SELECT money FROM economy WHERE uuid = ?;")) {
            statement.setString(1, player.toString());
            ResultSet rs = statement.executeQuery();
            if (rs.next()) return rs.getDouble("money");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    public void setMoney(UUID player, double amount) {
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
        setMoney(player, getMoney(player) + amount);
    }
}
