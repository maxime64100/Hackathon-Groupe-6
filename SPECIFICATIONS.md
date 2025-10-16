# Hackathon - Ynov Toulouse 2025 : Babyfoot du futur

Et si on réinventait l’expérience babyfoot à Ynov ? L’objectif de ce hackathon est de moderniser et digitaliser l’usage des babyfoots présents dans le Souk pour créer un service _next-gen_, pensé pour près de 1000 étudiants !

Que ce soit via des gadgets connectés, un système de réservation intelligent, des statistiques en temps réel ou des fonctionnalités robustes pour une utilisation massive, nous cherchons des solutions innovantes qui allient créativité et technologie.

Toutes les filières sont invitées à contribuer : Dev, Data, Infra, IoT, Systèmes embarqués… chaque idée compte pour rendre le babyfoot plus fun, plus pratique et plus connecté.

Votre mission : transformer le babyfoot classique en expérience high-tech pour Ynov !

## Préambule

Ce hackathon n'a pas pour but de produire un projet parfait ou fini, mais de laisser votre créativité à l'oeuvre, de vous challenger techniquement et humainement, et de vous faire découvrir d'autres domaines que le vôtre. Considérer ce hackathon comme une réussite si vous parvenez à mixer vos compétences, collaborer efficacement, et profiter de cette occasion pour toucher à des technologies ou domaines que vous ne connaissez pas encore.

> Un projet hautement collaboratif sera toujours bien mieux considéré qu'un projet techniquement parfait mais réalisé en silo.

## Sommaire

- [Dev FullStack](#dev-fullstack)
- [Cloud & Infrastructure](#cloud--infrastructure)
- [IA & Data](#ia--data)

## Dev FullStack

> Le but du hackathon n'est pas de produire une application parfaite, mais de démontrer votre capacité à concevoir et développer une solution complète en un temps limité. Concentrez-vous sur les fonctionnalités clés, la robustesse et l'expérience utilisateur.

### Objectif général

Créer une application web complète pour gérer et moderniser l’utilisation des babyfoots de Ynov Toulouse, incluant une API robuste et un frontend interactif pour les étudiants et les administrateurs.

Exemple:

- Site web de réservation et gestion des babyfoots
- Site de suivi en ligne en temps réel des parties en cours
- Site pour organiser des tournois (bracket, scores, etc.)
- Site d'administration des babyfoots (maintenance, état, etc.)

### Requis fonctionnels

- **Page d'accueil** :

  - Accueil pour les utilisateurs avec une présentation du service.
  - Attrait visuel pour encourager l'utilisation.
  - Soyez créatifs !

- **Authentification et autorisation** :

  - Inscription et connexion des utilisateurs (étudiants et administrateurs).
  - Gestion des rôles (utilisateur standard, administrateur).

- **Dashboard administrateur** :

  - Vue d’ensemble des babyfoots (état, disponibilité, statistiques d’utilisation).
  - Gestion des babyfoots (ajout, modification, suppression).
  - Gestion des utilisateurs (visualisation, modification des rôles, suppression).

> **Pensez à travailler avec vos collègues en IA/Data, IoT, Systèmes embarqués, etc. pour intégrer leurs fonctionnalités dans votre dashboard.**

- **API RESTful** :

  - Au moins un Endpoint CRUD complet (Create, Read, Update, Delete)
  - Des codes retours HTTP appropriés (200, 201, 400, 401, 403, 404, 500, etc.)
  - API Documentée (Swagger, Postman, etc.)

- **Lisibilité et maintenabilité du code** :

  - Code propre et bien structuré.
  - Utilisation de bonnes pratiques de développement (naming conventions, modularité, etc.).
  - Commentaires et documentation du code.

> Du code généré automatiquement (par exemple via des outils comme ChatGPT) est autorisé, mais il doit être revu, compris et adapté par vos soins. Tout code sortant de ces critères vaudra -5 sur votre note individuelle.

### Supplémentaires (bonus)

Vous êtes encouragés à implémenter autant de fonctionnalités supplémentaires que possible. Voici quelques idées :

- **Notifications** : Système de notifications pour les utilisateurs (email, SMS, push).
- **Statistiques avancées** : Analyse des données d’utilisation (temps de jeu, joueurs les plus actifs, etc.).
- **Intégration avec d’autres services** : Connexion avec des services externes (calendrier, réseaux sociaux, etc.).
- **Responsive Design** : Application optimisée pour les mobiles et tablettes.
- **Tests automatisés** : Couverture de tests unitaires et d’intégration.
- **Accessibilité** : Respect des normes d’accessibilité web (WCAG).
- **Internationalisation** : Support de plusieurs langues.
  ...

Une application bien pensée, même avec des fonctionnalités non terminées, sera valuée positivement. Ce cadrage et ces guidelines ne doivent surtout pas vous brider dans votre créativité, ça n'est pas le but de l'épreuve.

---

## Cloud & Infrastructure

> Le but du hackathon n'est pas de produire une infrastructure parfaite, mais de démontrer votre capacité à concevoir et déployer une solution complète en un temps limité. Concentrez-vous sur les fonctionnalités clés, la robustesse et la scalabilité.

### Objectif général

Déployer une infrastructure cloud complète pour héberger et gérer l’application web de gestion des babyfoots, incluant des services de base de données, de mise à l’échelle automatique et de surveillance.

Exemple:

- Hébergement de l’application web (frontend et backend)
- Base de données pour stocker les informations des babyfoots et des utilisateurs
- Services de surveillance et de journalisation
- Mise en place de la sécurité (firewalls, gestion des accès, etc.)

### Requis fonctionnels

- **Simplicité de déploiement** :

  - Utilisation d’outils d’automatisation (Terraform, Ansible, Scripts shell...) pour déployer l’infrastructure en une seule commande.
  - Documentation claire pour le déploiement.

- **Host sécurisé et protégé** :

  - Mise en place de règles de sécurité (firewalls, groupes de sécurité).
  - Gestion des accès (IAM, rôles, permissions).

- **Base de données** :

  - Déploiement d’une base de données relationnelle ou NoSQL.
  - Sauvegardes régulières et restauration des données.

- **Scalabilité** :

  - Mise en place de la mise à l’échelle automatique pour gérer les pics de trafic.

- **Surveillance et journalisation** :

  - Mise en place de services de surveillance (CPU, mémoire, latence, etc.).
  - Journalisation des événements importants (logs d’accès, erreurs, etc.).

================================================================================
============================== SI VOUS ÊTES PLUS DE 2 ==========================
================================================================================

Essayer de mettre en place une architecture plus complexe, par exemple :

- **Haute disponibilité** :

  - Mise en place de la redondance pour assurer la disponibilité du service. (2 serveurs minimum)
  - Utilisation de plusieurs zones de disponibilité si possible.

### Supplémentaires (bonus)

Vous êtes encouragés à implémenter autant de fonctionnalités supplémentaires que possible. Voici quelques idées :

- **Réplication de la base de données** : Mise en place de la réplication pour assurer la haute disponibilité.
- **Guide de récupération après sinistre** : Documentation et procédures pour la récupération en cas de panne majeure.
- **Optimisation des coûts** : Stratégies pour minimiser les coûts d’infrastructure
- **Tests de charge** : Simulation de trafic pour tester la robustesse de l’infrastructure.
  ...

Vous n'avez aucunement besoin de tout faire, mais une infrastructure bien pensée, même avec des fonctionnalités non terminées, sera valuée positivement. Ce cadrage et ces guidelines ne doivent surtout pas vous brider dans votre créativité, ça n'est pas le but de l'épreuve.

---

## IA & Data

> Le but du hackathon n'est pas de fournir une analyse parfaite (surtout au vu du dataset très aléatoire), mais de démontrer votre capacité à nettoyer, analyser, visualiser et utiliser des données en un temps limité. Concentrez-vous sur les fonctionnalités clés, la robustesse et l'impact business.

Vous trouverez le [dataset](data/babyfoot_dataset.csv) de référence.

### Objectif général

Analyser les données d’utilisation des babyfoots pour fournir des insights précieux aux administrateurs et améliorer l’expérience utilisateur, ainsi que fournir du concret, de la donnée à vos collègues pour votre projet. Des données provenant de votre analyse du dataset dans les autres départements sera grandement apprécié. (Section "Meilleur joueur", "Babyfoot le plus utilisé", "Heures de pointes", etc.)

### Requis fonctionnels

- **Nettoyage et préparation des données** :

  - Nettoyer les données brutes pour éliminer les valeurs manquantes, les doublons et les incohérences.
  - Eliminer des valeurs corrompues, des valeurs encodées de manière incorrecte, etc.
  - Préparer les données pour l’analyse (normalisation, transformation, etc.).
  - Justifier les choix de nettoyage et de préparation.

> Chaque groupe aura un nettoyage, et donc une interprétation différente des données. Il n'y a pas de "bonne" ou "mauvaise" manière de faire, mais vous devez être capable de justifier vos choix.

- **Analyse exploratoire des données (EDA)** :

  - Analyser les données pour identifier les tendances, les corrélations et les anomalies de votre choix.
  - Utiliser des techniques statistiques et de visualisation pour explorer les données (plots, graphiques, etc.).
  - Documenter les insights clés découverts lors de l’EDA.

- **Participation à l'élaboration de la base de données de votre projet**

  - Fournir des recommandations sur la structure de la base de données en fonction des analyses effectuées.
  - Collaborer avec les développeurs pour intégrer les insights dans l’application.
  - Travailler avec les IoT et/ou systèmes embarqués pour intégrer des données en temps réel si applicable.
  - Votre collaboration doit être visible dans le projet final, votre travail est important pour toute l'équipe.

- **Défi Data Science** :

  - Dans un rapport (format de votre choix), donne et justifiez les résultats suivants:
  - Top10 des buteurs
  - Top5 des meilleurs défenseurs (saves)
  - Est-ce que le fait de choisir un camp (bleu/rouge) influence le résultat d'une partie ?

### Supplémentaires (bonus)

Quelconques implémentations de l'IA de pair avec vos collègues Dev FullStack, IoT, Systèmes embarqués, etc. sera grandement apprécié.
ChatBot, Recommandation de babyfoot, Analyse d'images/vidéos, etc.

> Les étudiants en IoT et Systèmes embarqués seront votre source de nouvelles données hors dataset, n'hésitez pas à collaborer avec eux pour enrichir votre analyse.

Votre analyse de données et vos résultats n'ont aucunement besoin d'être parfaits, une démarche détaillée, même avec des résultats faux sera valorisée positivement. Profitez de cette occasion pour partager votre travail, vous êtes le concret de ce que développe votre équipe autour.

---

## IoT/Mobile

### Objectif général

Réaliser un dispositif IoT ou une application mobile pour améliorer l'expérience utilisateur autour des babyfoots, en intégrant des capteurs, des notifications ou des fonctionnalités interactives.

### Requis fonctionnels

- **Dispositif IoT** :

  - Intégration de capteurs pour suivre l'état des babyfoots (ex: capteurs de mouvement, capteurs de score, etc.).
  - Transmission des données en temps réel à l'application web.
  - Alimentation autonome (batterie, énergie solaire, etc.).
  - Analyse de la robustesse et de la fiabilité du dispositif.
  - Mise en place d'une communication ESPnow permettant de centraliser les données de plusieurs babyfoots sur un seul et même point d'accès.
  - Export des données collectées par le dispositif IoT pour une analyse ultérieure par un membre de l'équipe Data

- **Matériel rendu** :

  - Le matériel utilisé pour le prototype doit être listé et documenté (composants, capteurs, microcontrôleurs, etc.).
  - Justification des choix matériels en fonction des besoins du projet.
  - Analyse des coûts et de la disponibilité des composants.
  - Le matériel doit être rendu à la fin du hackathon

### Supplémentaires (bonus)

Vous êtes encouragés à implémenter autant de fonctionnalités supplémentaires que possible. Voici quelques idées :

- **Intégration avec des services externes** : Connexion avec des services de messagerie, réseaux sociaux, etc.
- **Fonctionnalités avancées** : Statistiques en temps réel, analyse des performances, etc.
- **Tests de robustesse** : Tests en conditions réelles pour évaluer la durabilité du dispositif ou de l'application.
  ...

---

## Systèmes Embarqués

### Objectif général

Réaliser un système embarqué pour automatiser certaines fonctionnalités des babyfoots, comme le comptage des scores, la détection des parties en cours, ou la gestion de l'état des babyfoots.

### Requis fonctionnels

- **Prototype fonctionnel** :

  - Développement d'un prototype embarqué capable de réaliser une ou plusieurs fonctionnalités spécifiques (ex: comptage des scores, détection des parties, etc.).
  - Intégration avec des capteurs et actionneurs appropriés.
  - Analyse de la consommation énergétique et de l'autonomie du système.
  - Documentation technique du prototype (schémas, code source, etc.).
  - Mise en valeur de votre prototype sur le support de votre choix (Affiche, Site Web...)

- **Matériel rendu** :

  - Le matériel utilisé pour le prototype doit être listé et documenté (composants, capteurs, microcontrôleurs, etc.).
  - Justification des choix matériels en fonction des besoins du projet.
  - Analyse des coûts et de la disponibilité des composants.
  - Le matériel doit être rendu à la fin du hackathon

### Supplémentaires (bonus)

Vous êtes encouragés à implémenter autant de fonctionnalités supplémentaires que possible. Voici quelques idées :

- **Connectivité** : Intégration avec des réseaux sans fil (Wi-Fi, Bluetooth, etc.) pour la communication avec l'application web ou mobile.
- **Robustesse** : Tests en conditions réelles pour évaluer la durabilité du système embarqué.

---

## Commun

A la fin de ce Hackathon, vous devrez fournir:

- Le GitHub de votre projet contenant:
- - Le code source de votre application (frontend, backend, scripts d'infrastructure, notebooks, etc.)
- - Tous les fichiers rapports présents dans [rendus/](rendus/) (Parsemez le fichier d'illustrations, schémas de votre choix, etc. pour le rendre plus vivant)
- - Votre présentation au format de votre choix (PDF, PPTX, Lien Canva/Google Slides, etc.)
- Le README principal à la racine du projet entièrement complété

Dernier délai pour rendre votre projet: **Vendredi 17 octobre 2025 à 21h00**.
