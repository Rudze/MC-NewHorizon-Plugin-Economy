# 💰 Economy Plugin - Spigot/Bukkit

**Economy** est un plugin Minecraft simple et efficace pour gérer l’argent des joueurs. Il repose sur `DatabaseAPI` pour enregistrer les données dans une base MySQL ou SQLite, et s’intègre avec **Vault** pour une compatibilité maximale avec d'autres plugins économiques.

---

## 🔧 Fonctionnalités

- Commande `/coins` pour consulter son solde.
- `/coins give <joueur> <montant>` pour ajouter de l'argent à un joueur.
- `/coins set <joueur> <montant>` pour définir un solde précis.
- Support **Vault** entièrement fonctionnel.
- Les données sont stockées de manière sécurisée en base de données.

---

## 📁 Structure du plugin

### 📦 `fr.rudy.economy`

#### 🔹 `main.java`
Classe principale (`JavaPlugin`) :
- Initialise la connexion via `DatabaseAPI`.
- Crée la table `economy` si elle n'existe pas.
- Enregistre les commandes et listeners.
- Initialise les comptes à la connexion des joueurs.

---

### 📂 `commands`

#### 🔹 `CoinsCommand.java`
Commande `/coins` :
- Sans argument, affiche le solde du joueur.
- `give` permet d’ajouter de l’argent.
- `set` permet de définir un montant précis.
- Vérifie les permissions (`economy.give` et `economy.set`).
- Interagit avec `EconomyManager`.

#### 🔹 `PayCommand.java`
Commande `/pay` :
- Permet aux joueurs d’envoyer de l’argent à d’autres joueurs.
- Vérifie que le montant est valide et que le joueur a assez de fonds.
- Empêche l’auto-paiement.
- Vérifie que le joueur cible est en ligne.
- Utilise `EconomyManager` pour transférer l'argent.

---

### 📂 `manager`

#### 🔹 `EconomyManager.java`
Gère l’accès aux données économiques :
- `getMoney(UUID)` : retourne le solde.
- `setMoney(UUID, double)` : définit un solde.
- `addMoney(UUID, double)` : ajoute de l’argent.

---

### 📂 `vault`

#### 🔹 `VaultEconomy.java`
Adaptateur Vault :
- Implemente l’API Vault `Economy`.
- Assure la compatibilité avec les plugins comme Jobs, ChestShop, etc.
- Pas de support des banques (non implémenté).

---

## ✅ Prérequis

- Minecraft **Paper 1.21.4**
- Plugin **DatabaseAPI** installé
- Plugin **Vault** recommandé

---

## 🚀 Installation

1. Compile le plugin avec **Maven/Gradle** ou exporte-le en `.jar`.
2. Dépose le fichier `.jar` dans le dossier `plugins/`.
3. Vérifie que **DatabaseAPI** et **Vault** sont installés.
4. Redémarre le serveur.

---

## 👨‍💻 Auteur

Développé par **Rudy** – pour une gestion économique modulaire et robuste dans Minecraft.
