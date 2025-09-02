package fr.rudy.economy;

import fr.rudy.databaseapi.DatabaseAPI;
import fr.rudy.economy.commands.CoinsCommand;
import fr.rudy.economy.commands.PayCommand;
import fr.rudy.economy.manager.EconomyManager;
import fr.rudy.economy.vault.VaultEconomy;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

public class Main extends JavaPlugin implements Listener {

    private VaultEconomy vaultEconomy;
    private EconomyManager economyManager;
    private Connection connection;

    @Override
    public void onEnable() {
        Plugin plugin = getServer().getPluginManager().getPlugin("DataBaseAPI");
        if (!(plugin instanceof DatabaseAPI db)) {
            getLogger().severe("❌ DataBaseAPI introuvable ou invalide");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        try {
            connection = db.getDatabaseManager().getConnection();
            connection.createStatement().executeUpdate(
                    "CREATE TABLE IF NOT EXISTS economy (uuid TEXT PRIMARY KEY, money DOUBLE DEFAULT 0.0)"
            );
        } catch (SQLException | NullPointerException e) {
            getLogger().severe("❌ Erreur DB: " + e.getMessage());
            e.printStackTrace();
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        economyManager = new EconomyManager(connection);
        vaultEconomy = new VaultEconomy(economyManager);
        Bukkit.getServicesManager().register(Economy.class, vaultEconomy, this, ServicePriority.Normal);

        Plugin vaultPlugin = Bukkit.getPluginManager().getPlugin("Vault");
        if (vaultPlugin != null && vaultPlugin.isEnabled()) {
            Economy registered = Bukkit.getServicesManager().getRegistration(Economy.class).getProvider();
            if (registered == null) {
                getLogger().warning("⚠ Vault Provider introuvable après enregistrement");
            }
        } else {
            getLogger().warning("⚠ Vault non trouvé ou inactif");
        }

        getCommand("coins").setExecutor(new CoinsCommand(economyManager));
        getCommand("pay").setExecutor(new PayCommand(economyManager));

        Bukkit.getPluginManager().registerEvents(this, this);
        getLogger().info("✅ Plugin Economy activé !");
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        if (economyManager == null || connection == null || vaultEconomy == null) {
            return;
        }
        try (PreparedStatement ps = connection.prepareStatement(
                "INSERT OR IGNORE INTO economy (uuid, money) VALUES (?, 0.0)"
        )) {
            ps.setString(1, e.getPlayer().getUniqueId().toString());
            ps.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
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
}
