# Hackathon - Ynov Toulouse 2025 : Babyfoot du futur - Cloud & Infrastructure

## Equipe
- Cloud & Infra : BARREDA Clément
- Cloud & Infra : SAIDYOUSSOUFA Michael
- Cloud & Infra : ESCOFFIER Alexandre

Et si on réinventait l’expérience babyfoot à Ynov ? L’objectif de ce hackathon est de moderniser et digitaliser l’usage des babyfoots présents dans le Souk pour créer un service _next-gen_, pensé pour près de 1000 étudiants !

Que ce soit via des gadgets connectés, un système de réservation intelligent, des statistiques en temps réel ou des fonctionnalités robustes pour une utilisation massive, nous cherchons des solutions innovantes qui allient créativité et technologie.

Toutes les filières sont invitées à contribuer : Dev, Data, Infra, IoT, Systèmes embarqués… chaque idée compte pour rendre le babyfoot plus fun, plus pratique et plus connecté.

Votre mission : transformer le babyfoot classique en expérience high-tech pour Ynov !

---

> Ce fichier contient les informations spécifiques au Cloud & Infra de votre projet. Il suffit d'en remplir une seule fois, même si vous êtes plusieurs Cloud & Infra dans l'équipe.

# Requis

Ce README contient les requis fonctionnels de la partie Cloud & Infra de votre projet. Il doit compléter le README principal à la racine du projet, et servira la partie de votre note propre à votre spécialité.

Basez-vous sur les spécifications dans [SPECIFICATIONS.md](../SPECIFICATIONS.md) pour remplir ce document.

Décrivez ici les actions que vous avez menées, votre démarche, les choix techniques que vous avez faits, les difficultés rencontrées, etc. Précisez également dans quelle mesure vous avez pu collaborer avec les autres spécialités.

Autrement, il n'y a pas de format imposé, mais essayez de rester clair et concis, je ne vous demande pas de rédiger un roman, passez à l'essentiel, et épargnez-moi de longues pages générées par IA (malusée).

En conclusion, cela doit résumer votre travail en tant qu'expert.e infra, et vous permettre de garder un trace écrite de votre contribution au projet.

Merci de votre participation, et bon courage pour la suite du hackathon !

---

### 🚀 Fonctionnalités implémentées

- **Hébergement complet de l’infra** sur un **Raspberry Pi 5 (4 Go de RAM)** fourni par Ynov.  
- **Déploiement de la base de données MariaDB**, du **serveur Nginx** (servant le front Angular), ainsi que d’un **stack de supervision Grafana / Prometheus**.  
- **Portainer** pour la gestion et le suivi des conteneurs via une interface web.  
- **phpMyAdmin** pour l’administration de la base de données (majoritairement utilisé pour du debug).  
- **Certificats SSL** installés dans nginx pour sécuriser l’accès au front-end.  
- **Tâche système automatisée** au démarrage du Raspberry, permettant de relancer l’API et les services critiques après un redémarrage ou une coupure d’alimentation (sous forme de service).  
- **Infrastructure docker packagée via `docker-compose.yml`** pour permettre un redéploiement complet en une seule commande.

---

### ⚙️ Choix techniques

- **Docker Compose** comme socle d’infrastructure pour isoler les composants, simplifier les déploiements et garantir la portabilité.  
- **MariaDB** pour la compatibilité avec le backend Spring Boot et la légèreté sur un environnement ARM.  
- **Nginx** utilisé comme **serveur web** statique pour héberger les fichiers Angular générés par `ng build`, et reverse proxy.  
- **Prometheus** et **Grafana** pour le **monitoring temps réel** des conteneurs et de l’état du Raspberry.  
- **Portainer** pour administrer visuellement les conteneurs Docker sans devoir intervenir en ligne de commande.

---

### 🤝 Démarche et collaboration

Chaque filière a travaillé indépendamment sur sa partie dans un premier temps.  
Une fois l’infrastructure stabilisée et opérationnelle, **l’ensemble du projet (API, front, base de données)** a été migré dessus pour permettre la mise en fonctionnement complète du prototype.  

Nous nous sommes concentrés sur la **mise en place, la configuration et la fiabilisation des services**, en veillant à la compatibilité des dépendances et à la bonne intégration du front et du back.  

---

### ⚠️ Difficultés rencontrées

- **Manque de matériel** : le second Raspberry Pi demandé pour tester le load balancing a été refusé par manque de stocks, limitant les possibilités de redondance.  
- **Temps restreint** pour mettre en place une véritable **automatisation de déploiement (CI/CD)**.  
- **Optimisation des ressources** : nécessité d’adapter les conteneurs pour ne pas saturer les 4 Go de RAM du Raspberry.  
- **Passage du dev à la prod** : le transfert de la webapp sur le Raspberry nous a posé quelques difficultés (l'API ne voulait pas se lancer, puis nous avons eu des difficultés à build le front-end dues au manque de RAM.

---

### 🧠 Conclusion

Cette partie du projet nous a permis de consolider nos compétences en **déploiement d’infrastructures conteneurisées** et d’approfondir notre maîtrise de **Docker** et de la **mise en production d’applications web** dans un environnement restreint.  
Nous avons également découvert les contraintes réelles du **fullstack** (intégration front/back/DB) et les enjeux d’un **déploiement fiable, autonome et maintenable**.

---

### 📝 Retours d'expérience

**Clément :** j'ai particulièrement apprécié travailler sur ce projet. Le début était assez compliqué, sans cadre clair ni consignes précises (volontairement), j'étais un peu perdu et je ne savais pas par ou commencer. Il nous fallait bâtir une infra complète, mais avec quoi ? Quelle technologie ? Quel matériel ? tout ça était à définir. Après avoir échangé avec Michael et Alexandre, nous avons décidé de partir donc sur une infrastructure principalement Docker. La suite a été bien plus plaisante, mettre en place tout ce que nous avons immaginé, et voir petit à petit le tout fonctionner est une vraie satisfaction. De voir aussi que le travail de nos camarades fullstack et IA/Data fonctionnait sur notre infra est, je trouve, une vraie réussite. Je ressors de ce projet satisfait de nous, content d'avoir pu y participer, et enthousiaste pour la suite.

**Michael :** Pour ma part, je suis satisfait du résultat de notre projet. Le début a été assez complexe, je ne savais pas vraiment par où commencer, mais l’avantage, c’est qu’il s’agissait d’un travail d’équipe. Les échanges avec Clément et Alexandre ont été très fluides, et nous avons beaucoup partagé nos connaissances, aussi bien entre nous trois qu’avec les autres filières. Malheureusement, le projet n’a pas abouti comme nous l’aurions souhaité, mais je suis vraiment heureux d’avoir travaillé avec cette équipe. C’est juste un peu dommage de ne pas avoir pu réaliser la seconde partie sur la redondance. J'ai hâte de voir les autres projets avec eux.

**Alexandre :** Pour commencer et pour être entièrement franc, il s'agissait de mon premier hackathon et de mon premier projet en réseau (je suis un ancien dev en reconversion) J'ai eu quelques cours de réseau mais ça remonte à 4 , 5 ans. Cela a donc été très difficile pour moi au début, surtout le premier jour où il n’y avait avait rien de technique à montrer. Il s'agissait de beaucoup de discussions pour déterminer les choix techniques, les choix de technologies... Heureusement petit à petit la situation s'est arrangée, et je suis passé d'un état de confusion totale, à une démarche d'apprentissage. Un apprentissage sur le terrain. Je remercie chaleureusement Clément et Michael pour m'avoir expliqué beaucoup de notions qui m'étais inconnues alors que je suivais attentivement leur travail. En général j'ai donc plutôt servi de pont entre l'équipe dév et l'équipe réseau, par exemple pour la base de données ou j'ai grandement participé à sa conception/création. Je suis satisfait de ce projet, notre équipe était communicative et dans l'entraide. J'ai pu voir concrètement comment se déroule un projet alliant les dév et les infra c'était très intéressant et instructif. J'ai hâte de voir la suite, ce projet a attisé ma curiosité et mon envie de me former. 
