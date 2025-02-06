# P6-Full-Stack-reseau-dev

## Base de données et Back-end
1. Créer le fichier ".env" dans le dossier "back" pour les credentials de la base de données avec les lignes suivantes :
    DB_URL=jdbc:mysql://localhost:3306/[NOM_BD]
    DB_USERNAME=[NOM_UTILISATEUR_BD]
    DB_PASSWORD=[MDP_UTILISATEUR_BD]

2. Dans MySQL, créer une nouvelle base de données nommée comme ce que vous avez indiqué pour "DB_URL".
3. Dans votre base de données fraîchement créée, exécuter le fichier SQL suivant pour créer les tables et les données en même temps : "back\src\main\resources\script.sql"
4. Lancer l'application Java sur votre IDE.

## Front-end
1. Se rendre dans le répertoire "front".
2. Lancer la commande "npm install" pour installer les dépendances.
3. Lancer la commande "ng serve" pour lancer l'application et se rendre à l'url donnée par le terminal (par défaut: localhost:4200).

## Swagger pour l'API
Lien : http://localhost:3001/swagger-ui/index.html
Avant d'utiliser toutes les routes, vous devez vous identifier avec la route "/api/auth/login" grâce à vos identifiants habituels.

## Identifiants déjà existants
Login : "admin" ou "admin@admin.com"
Mot de passe : "Test123!"
