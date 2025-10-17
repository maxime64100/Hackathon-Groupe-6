# Hackathon - Ynov Toulouse 2025 : Babyfoot du futur - FullStack

## Equipe

- Dev' FullStack 1 : FALLEK Maxime
- Dev' FullStack 2 : MOREL Oskar
- Dev' FullStack 3 : QUERON Raphaël

Et si on réinventait l’expérience babyfoot à Ynov ? L’objectif de ce hackathon est de moderniser et digitaliser l’usage des babyfoots présents dans le Souk pour créer un service _next-gen_, pensé pour près de 1000 étudiants !

Que ce soit via des gadgets connectés, un système de réservation intelligent, des statistiques en temps réel ou des fonctionnalités robustes pour une utilisation massive, nous cherchons des solutions innovantes qui allient créativité et technologie.

Toutes les filières sont invitées à contribuer : Dev, Data, Infra, IoT, Systèmes embarqués… chaque idée compte pour rendre le babyfoot plus fun, plus pratique et plus connecté.

Votre mission : transformer le babyfoot classique en expérience high-tech pour Ynov !

---

> Ce fichier contient les informations spécifiques au développement FullStack de votre projet. Il suffit d'en remplir une seule fois, même si vous êtes plusieurs développeurs FullStack dans l'équipe.

# Requis

Ce README contient les requis fonctionnels de la partie FullStack de votre projet. Il doit compléter le README principal à la racine du projet, et servira la partie de votre note propre à votre spécialité.

Basez-vous sur les spécifications dans [SPECIFICATIONS.md](../SPECIFICATIONS.md) pour remplir ce document.

Décrivez ici les fonctionnalités que vous avez implémentées, votre démarche, les choix techniques que vous avez faits, les difficultés rencontrées, etc. Précisez également dans quelle mesure vous avez pu collaborer avec les autres spécialités.

Autrement, il n'y a pas de format imposé, mais essayez de rester clair et concis, je ne vous demande pas de rédiger un roman, passez à l'essentiel, et épargnez-moi de longues pages générées par IA (malusée).

En conclusion, cela doit résumer votre travail en tant que développeur.se FullStack, et vous permettre de garder un trace écrite de votre contribution au projet.

Merci de votre participation, et bon courage pour la suite du hackathon !

## Fonctionnalités implémentées
**Back-end:**
- API complète développée avec Spring Boot.
- Authentification sécurisée avec JWT (login, inscription, gestion des rôles).
- Gestion des réservations de babyfoot, des utilisateurs et des ressources du site.
- Tests unitaires pour assurer la fiabilité du code et la cohérence des endpoints.
**Front-end:**
- Application responsive et multilingue, interface moderne et épurée.
- Pages principales :
  - Page d’accueil
  - Page de réservation de babyfoot
  - Dashboard administrateur (gestion des babyfoots, utilisateurs et réservations)
  - Espace utilisateur connecté
  - Connexion / Inscription / Déconnexion
- Respect partiel des normes RGAA pour l’accessibilité.
## Choix techniques
- Spring Boot pour la rapidité de mise en place, la robustesse et la gestion des dépendances.
- JWT pour une authentification stateless et sécurisée.
- Angular pour une interface dynamique et fluide.
- ngx-translate pour la gestion i18n

## Démarche et collaboration
Chacun sur sa branche. Pull Requests sur github puis merge sur la branche 'dev'. Ensuite gros merge sur la branche 'main' pour le déloiement. 
Travail avec les autres filières:
- Data/IA pour la partie dashboard (admin et stats) et le chat bot IA
- Infra pour le déploiement de la bd, back et front sur un serveur

## Difficultés rencontrées
- Gestion du temps (deux jours c'est pas beaucoup)
- Gestion des droits/rôles

# Conclusion 
### Oskar Morel
Ce travail m'a permis de mettre en place une stack FullStack complète, de l'API à l'interface. Je me suis surtout occupé de quelques interfaces, du module multilangue, du RGAA, de la gestion des utilsateurs et du dashboard.

### Fallek Maxime
C'est la première fois pour ma part que j'utilisais Angular (et 2ème fois pour SpringBoot donc peu de base). Cela m'a permis de monter en compétence et d'apprendre un nouveau langage. J'ai réalisé des interfaces en front, le branchement entre le front et le back de la création de réservations et la liste des babyfoots et j'ai également intégré le chatbot ce qui était une première pour moi.

### Raphaël Queron
Ce projet bien qu'intense m'a permis de monter en compétences techniques pour ma part. Je suis la personne qui avait principalement la charge de m'occuper de la partie Backend de l'application, donc j'ai pu revoir des notions que j'avais oubliés (comme JWT) mais aussi apprendre de nouvelles choses commme la documentation avec swagger.
