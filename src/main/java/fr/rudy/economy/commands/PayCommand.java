package fr.rudy.economy.commands;

import fr.rudy.economy.manager.EconomyManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PayCommand implements CommandExecutor {

    private final EconomyManager economyManager;

    public PayCommand(EconomyManager economyManager) {
        this.economyManager = economyManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("<glyph:error> §cCommande uniquement pour les joueurs.");
            return true;
        }

        if (args.length != 2) {
            player.sendMessage("<glyph:error> §cUtilisation : /pay <joueur> <montant>");
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null || !target.isOnline()) {
            player.sendMessage("<glyph:error> §cJoueur introuvable ou hors-ligne.");
            return true;
        }

        if (target.equals(player)) {
            player.sendMessage("<glyph:error> §cVous ne pouvez pas vous payer vous-même.");
            return true;
        }

        try {
            double amount = Double.parseDouble(args[1]);
            if (amount <= 0) {
                player.sendMessage("<glyph:error> §cLe montant doit être positif.");
                return true;
            }

            double balance = economyManager.getMoney(player.getUniqueId());
            if (balance < amount) {
                player.sendMessage("<glyph:error> §cFonds insuffisants.");
                return true;
            }

            economyManager.setMoney(player.getUniqueId(), balance - amount);
            economyManager.addMoney(target.getUniqueId(), amount);

            player.sendMessage("<glyph:info> §bVous avez envoyé §b" + amount + "§b à " + target.getName());
            target.sendMessage("<glyph:info> §bVous avez reçu §b" + amount + "§b de " + player.getName());
        } catch (NumberFormatException e) {
            player.sendMessage("<glyph:error> §cMontant invalide.");
        }

        return true;
    }
}
