package fr.rudy.economy;

import fr.rudy.databaseapi.DatabaseAPI;
import fr.rudy.economy.commands.CoinsCommand;
import fr.rudy.economy.manager.EconomyManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.*;
import java.util.UUID;

public class main extends JavaPlugin implements Listener {

    private Connection connection;

    @Override
    public void onEnable() {
        try {
            Plugin plugin = getServer().getPluginManager().getPlugin("DataBaseAPI");
            if (!(plugin instanceof DatabaseAPI)) {
                getLogger().severe("❌ DataBaseAPI introuvable ou désactivée !");
                getServer().getPluginManager().disablePlugin(this);
                return;
            }
            connection = ((DatabaseAPI) plugin).getDatabaseManager().getConnection();

            Statement stmt = connection.createStatement();
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS economy (uuid TEXT PRIMARY KEY, money DOUBLE DEFAULT 0.0)");
        } catch (SQLException e) {
            e.printStackTrace();
            getLogger().severe("Impossible de créer ou d'accéder à la base de données !");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        // Initialisation du gestionnaire d'économie
        EconomyManager economyManager = new EconomyManager(connection);

        // Enregistrement des événements et des commandes
        getServer().getPluginManager().registerEvents(this, this);
        getCommand("coins").setExecutor(new CoinsCommand(economyManager));

        getLogger().info("✅ Economy activée !");
    }

    @Override
    public void onDisable() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        try (PreparedStatement ps = connection.prepareStatement("INSERT OR IGNORE INTO economy (uuid, money) VALUES (?, 0.0)")) {
            ps.setString(1, uuid.toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}