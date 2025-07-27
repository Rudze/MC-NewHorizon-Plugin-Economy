package fr.rudy.economy.commands;

import fr.rudy.economy.manager.EconomyManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CoinsCommand implements CommandExecutor {

    private final EconomyManager economyManager;

    public CoinsCommand(EconomyManager economyManager) {
        this.economyManager = economyManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0 && sender instanceof Player player) {
            double balance = economyManager.getMoney(player.getUniqueId());
            String formatted = String.format("%.2f", balance);
            player.sendMessage("§eVous avez §6" + formatted + " pièces§e.");
            return true;

        } else if (args.length == 3 && args[0].equalsIgnoreCase("set")) {
            if (!sender.hasPermission("economy.set")) {
                sender.sendMessage("§cVous n'avez pas la permission.");
                return true;
            }

            Player target = Bukkit.getPlayer(args[1]);
            if (target == null) {
                sender.sendMessage("§cJoueur introuvable.");
                return true;
            }

            try {
                int amount = Integer.parseInt(args[2]);
                economyManager.setMoney(target.getUniqueId(), amount);
                sender.sendMessage("§aArgent mis à jour pour " + target.getName() + " : " + amount + " pièces.");
                target.sendMessage("§aVotre solde a été mis à jour : " + amount + " pièces.");
            } catch (NumberFormatException e) {
                sender.sendMessage("§cMontant invalide.");
            }
            return true;

        } else if (args.length == 3 && args[0].equalsIgnoreCase("give")) {
            if (!sender.hasPermission("economy.give")) {
                sender.sendMessage("§cVous n'avez pas la permission.");
                return true;
            }

            Player target = Bukkit.getPlayer(args[1]);
            if (target == null) {
                sender.sendMessage("§cJoueur introuvable.");
                return true;
            }

            try {
                int amount = Integer.parseInt(args[2]);
                economyManager.addMoney(target.getUniqueId(), amount);
                sender.sendMessage("§aVous avez donné " + amount + " pièces à " + target.getName() + ".");
                target.sendMessage("§aVous avez reçu " + amount + " pièces !");
            } catch (NumberFormatException e) {
                sender.sendMessage("§cMontant invalide.");
            }
            return true;
        }

        sender.sendMessage("§cUtilisation : /coins [give|set] <joueur> <montant>");
        return false;
    }
}
