<table width="100%" border="0" cellspacing="0" cellpadding="0">
<tr>
<td align="left"><h1>Hackathon - Ynov Toulouse 2025</h1></td>
<td align="right"><img src="ressources/logo.png" alt="Hackathon Ynov Toulouse 2025" width="100"/></td>
</tr>
</table>

> Ce repository contient les ressources ainsi que le code source développé lors du hackathon Ynov Toulouse 2025.

Cette template de README est un guide pour vous aider à structurer votre rendu de projet. N'hésitez pas à l'adapter ou surtout à le compléter avec des sections supplémentaires si nécessaire.

## Contexte

Et si on réinventait l’expérience babyfoot à Ynov ? L’objectif de ce hackathon est de moderniser et digitaliser l’usage des babyfoots présents dans le Souk pour créer un service _next-gen_, pensé pour près de 1000 étudiants !

Que ce soit via des gadgets connectés, un système de réservation intelligent, des statistiques en temps réel ou des fonctionnalités robustes pour une utilisation massive, nous cherchons des solutions innovantes qui allient créativité et technologie.

Toutes les filières sont invitées à contribuer : Dev, Data, Infra, IoT, Systèmes embarqués… chaque idée compte pour rendre le babyfoot plus fun, plus pratique et plus connecté.

Votre mission : transformer le babyfoot classique en expérience high-tech pour Ynov !

Bienvenue dans le Hackathon Ynov Toulouse 2025 !

> Retrouvez vos guidelines techniques dans le fichier [SPECIFICATIONS.md](./SPECIFICATIONS.md).

> P.S C'est un projet de groupe, pas autant de sous-projets que de filières dans votre équipe. Travaillez ensemble pour un seul et même projet au nom de votre équipe toute entière. Les guidelines sont là pour vous aider, pas pour vous diviser. Profitez de ce moment pour apprendre à travailler ensemble, partager vos compétences, et créer quelque chose d'unique.

## Equipe

- Dev' FullStack 1 : FALLEK Maxime
- Dev' FullStack 2 : MOREL Oskar
- Dev' FullStack 3 : QUERON Raphaël
- Cloud & Infra 1 : BARREDA Clément
- Cloud & Infra 2 : SAIDYOUSSOUFA Michael
- Cloud & Infra 3 : ESCOFFIER Alexandre
- IA & Data 1 : LEMAIRE César
- IA & Data 2 : ALBALAT Noah


> Préciser qui est le porte parole de l'équipe, c'est lui qui répondra aux questions si nécessaire.
-> Porte parole : ALBALAT Noah

## Table des matières

- [Contexte](#contexte)
- [Equipe](#equipe)
- [Contenu du projet](#contenu-du-projet)
- [Technologies utilisées](#technologies-utilisées)
- [Architecture](#architecture)
- [Guide de déploiement](#guide-de-déploiement)
- [Etat des lieux](#etat-des-lieux)

## Contenu du projet

> Décrivez brièvement le projet, son objectif. Utilisez une vue business pour décrire ce que votre produit/service apporte à vos utilisateurs.

Notre projet a pour ambition de **digitaliser et moderniser la gestion des babyfoots** présents sur le campus **Ynov Toulouse**.  
Nous avons conçu une application web complète permettant :

- Aux **étudiants** de **réserver un babyfoot**, consulter son **état en temps réel** et gérer leur compte utilisateur.
- Aux **administrateurs** de disposer d’un **tableau de bord centralisé** pour suivre l’état des babyfoots, gérer les réservations, signaler des pannes et superviser les tournois.

###  Objectif business

> Offrir une expérience fluide, moderne et ludique pour la gestion des babyfoots du campus, en rendant les réservations simples, les maintenances transparentes et l’expérience plus engageante pour les 1000 étudiants d’Ynov Toulouse.

---


## Technologies utilisées

> Ici, listez les principales technologies, en expliquant pourquoi vous les avez choisies. Tout choix technique, langages, frameworks doit être justifié. (Parce que vous maîtrisez déjà la techno, parce que c'est la plus adaptée au besoin, parce que c'est la plus innovante, etc.)

| Couche                    | Technologie | Justification |
|:--------------------------|:-------------|:--------------|
| **Backend API**           | **Spring Boot (Java 21)** | Framework robuste, rapide à mettre en œuvre, et maîtrisé par l’équipe. Parfait pour créer une API REST fiable. |
| **ORM & Base de données** | **Spring Data JPA + MySQL** | Simplifie la persistance des données tout en assurant la compatibilité avec le modèle relationnel. |
| **Sécurité**              | **Spring Security + JWT** | Garantit une authentification sécurisée et adaptable selon le rôle (user/admin). |
| **Documentation**         | **Swagger / OpenAPI 3.0** | Fournit une documentation interactive pour tester et comprendre l’API. |
| **Frontend**              | **Angular** | Interface utilisateur moderne et dynamique, adaptée aux besoins d’administration et de réservation. |

## Architecture

> Faite un schéma simple de l'architecture technique de votre solution. Chaque service/composant est un bloc, et les interactions entre les blocs sont des flèches. Vous pouvez utiliser des outils comme [draw.io](https://app.diagrams.net/), ou encore [Excalidraw](https://excalidraw.com/) pour créer vos schémas. C'est une vue d'ensemble, pas un détail de chaque composant. Chacun d'entre vous doit être capable d'expliquer cette architecture.

L’architecture suit une séparation claire entre les couches :

```text
+---------------------------+
|        Frontend (Angular) |
|  - Interface de gestion   |
|  - Authentification user  |
|  - Modification user      |
|  - Réservation babyfoots  |
|  - Tableau admin          |
|  - Chatbot IA             |
+-------------+-------------+
              |
              v
+-------------+-------------+
|     API Backend (Spring Boot) |
|  - Authentification JWT       |
|  - CRUD Utilisateurs          |
|  - CRUD Babyfoots             |
|  - CRUD Réservations          |
   - CRUD Tournois
|  - Documentation Swagger UI   |
+-------------+-------------+
              |
              v
+-------------+-------------+
|     MySQL Database         |
|  - Tables :                |
|    • user_babyfoot         |
|    • babyfoot              |
|    • booking               |
|    • repairs               |
|    • tournament            |
+-----------------------------+
```
Chaque entité (User, Babyfoot, Booking) est reliée via des clés étrangères et gérée par JPA.
Le frontend Angular interagit avec l’API pour récupérer ou modifier les données, tandis que le backend assure la logique métier et la sécurité.

## Guide de déploiement

> Expliquez comment déployer votre application **EN MOINS DE LIGNES DE COMMANDE POSSIBLES**. Docker, Ansible, Terraform, Scripts Shell... Le but est de pouvoir déployer votre application en une seule commande ou presque.

Exemple de lancement en **une seule commande**:

[Références Proxmox HelperScripts](https://github.com/community-scripts/ProxmoxVE/tree/main/install)

> /!\ IMPORTANT /!\ : Votre projet sera déployé sur une machine **LINUX** (Debian/Ubuntu), avec 4Go de RAM et 2 CPU (x86_64). Assurez-vous que votre application peut fonctionner dans ces conditions. Il n'y aura pas de "Ca marche sur mon Mac." ou encore "Si on alligne les astres sur Windows XP ça passe.".


Dans le dossier Docker se trouve le fichier **install.sh**, ce script correspond à notre approche du one command install.
En théorie, il doit installer toutes les dépendences, créer toute l'arborescene de fichiers, cloner le GitHub, installer l'API, les Dockers et, il aurait pu aussi compiler le front-end.

Dans la pratique, il parvient à installer toutes les dépendances, télécharger, configurer et déployer tous les conteneurs, mais il bute sur la partie API (back-end) avec Maven, java, Angular. C'est la partie qui nous à donné le plus de mal, qui a fonctionné sur le Raspberry Pi5 après une matinée entière à cherher comment y parvenir, mais qui n'arrive pas à fonctionner avec le script. Plus de temps aurait été nécessaire pour réellement éprouver ce programm, identifier tous les bugs, les corriger, et rendre ce projet vraiment installable en une seule commande.

## Etat des lieux

> Section d'honnêteté, décrivez ce qui n'a pas été fait, ce qui aurait pu être amélioré, les limitations de votre solution actuelle. Montrez que vous avez une vision critique de votre travail, de ce qui a été accompli durant ces deux demi-journées.
Le but n'est pas de faire un produit fini, mais de montrer vos compétences techniques, votre capacité à travailler en équipe, à gérer un projet, et à livrer quelque chose de fonctionnel dans un temps limité.

| Élément                   | État   |
|:--------------------------|:-------|
| **Authentification JWT**  | **✅**  |
| **CRUD**                  | **✅**  |
| **Sécurité**              | **✅**  | 
| **Documentation Swagger** | **✅**  | 
| **Frontend**              | **🚧** |


Points perfectibles :
- Amélioration de la gestion des erreurs et des messages utilisateurs.

- Intégration du frontend Angular avec l’API (connexion en cours).

- Ajout de statistiques et visualisation des données en temps réel.

En l’espace de deux journées, notre équipe a su concevoir une solution fonctionnelle, sécurisée et évolutive, illustrant une véritable collaboration entre les pôles FullStack, Infra et Data.

Ce projet démontre notre capacité à :
-Structurer une architecture technique claire,
-Mettre en place une API REST documentée et sécurisée,
-Travailler en équipe efficacement,
-Livrer une application utilisable dans un contexte réel.
-Une belle démonstration de créativité, de rigueur et d’efficacité collective. 
