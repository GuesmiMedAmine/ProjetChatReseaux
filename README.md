# Application Client-Serveur en Java

Ce projet est une application client-serveur simple en Java qui permet à plusieurs clients de se connecter à un serveur et d'échanger des messages.

## Prérequis

- Java Development Kit (JDK) installé sur votre machine.
- Un terminal ou une invite de commande pour exécuter les commandes.

## Compilation et exécution

Voici les étapes pour compiler et exécuter l'application dans un seul bloc de commandes Bash :

```bash
# Naviguer vers le dossier src
cd src

# Compiler les fichiers Java
javac -d . client/*.java server/*.java

# Démarrer le serveur dans un terminal
gnome-terminal -- bash -c "java server.ServerApp; exec bash"

# Démarrer deux clients dans des terminaux séparés
gnome-terminal -- bash -c "java client.ClientApp; exec bash"
gnome-terminal -- bash -c "java client.ClientApp; exec bash"