# Hackathon - Ynov Toulouse 2025 : Babyfoot du futur - IA & Data

## Equipe

- IA & Data 1 : LEMAIRE Cesar
- IA & Data 2 : ALBALAT Noah

DOCUMENTATION
Projet Babyfoot Analytics
École: Ynov Campus
Dataset: babyfoot_dataset.csv
Lignes initiales: 100 200
Lignes finales après nettoyage: 60 075
________________________________________
1. Contexte du projet
Ce projet vise à analyser les performances des joueurs de babyfoot à travers trois axes principaux :
•	Top 10 des buteurs : Identifier les meilleurs attaquants
•	Top 5 des défenseurs : Classer les meilleurs gardiens selon leurs arrêts (saves)
•	Influence du camp : Tester si le choix de l'équipe (rouge/bleu) affecte les chances de victoire
Le dataset présentait plusieurs problèmes de qualité nécessitant un nettoyage approfondi :
•	40% de doublons identifiés via la colonne duplicate_flag
•	Formats de dates multiples (6 formats différents détectés)
•	Variantes orthographiques dans winner, team_color, player_role
•	Valeurs textuelles dans les colonnes numériques (ex: "two" au lieu de 2)
•	Scores mal structurés (parfois combinés dans une seule colonne au format "7-0")

2. Processus de nettoyage réalisé
2.1 Suppression des doublons 
Action : Filtrage sur la colonne duplicate_flag pour exclure les valeurs 1 et yes
Impact : Réduction de 100 200 à 60 075 lignes (-40%)
Justification : Élimination des observations redondantes pour garantir l'unicité des données et éviter les biais statistiques.


2.2 Restructuration des scores 
Problème détecté : Certaines lignes avaient le score complet dans final_score_red au format "7-0", avec final_score_blue vide.
Solution appliquée :
Création de deux colonnes propres via Power Query :
// score_red_clean
if Text.Contains([final_score_red], "-") then 
    Text.BeforeDelimiter([final_score_red], "-") 
else 
    [final_score_red]

// score_blue_clean
if Text.Contains([final_score_red], "-") then 
    Text.AfterDelimiter([final_score_red], "-") 
else 
    [final_score_blue]
Impact : Récupération de nombreuses parties avec scores exploitables.

2.3 Recalcul de la colonne winner 
Problème initial :
•	15 variantes détectées (ex: "RED", "Red", "red", "rouge")
•	4,7% de valeurs manquantes
Solution : Recalcul complet basé sur les scores nettoyés
if [score_blue_clean] > [score_red_clean] then "Blue"
else if [score_red_clean] > [score_blue_clean] then "Red"
else if [score_blue_clean] = [score_red_clean] then "Draw"
else null
Impact : Uniformisation à 3 valeurs (Red, Blue, Draw) et récupération des winners manquants.

2.4 Normalisation de team_color 
Problème : 10 variantes (Red, red, R, 🔴, Blue, blue, B, 🔵, etc.)


Solution :
let
    upper = Text.Upper([team_color])
in
    if Text.Contains(upper, "R") or Text.Contains(upper, "ROUGE") or [team_color] = "🔴" then 
        "Red"
    else if Text.Contains(upper, "B") or Text.Contains(upper, "BLEU") or [team_color] = "🔵" then 
        "Blue"
    else null
Impact : Passage de 10 variantes à 2 valeurs standardisées.

2.5 Normalisation de player_role 
Problème : 8 variantes (defence, defense, def, ATTACK, attack, attck, Defense, etc.)
Solution :
let
    upper = Text.Upper([player_role])
in
    if Text.Contains(upper, "ATT") then "Attack"
    else if Text.Contains(upper, "DEF") then "Defense"
    else null
Impact : Passage de 8 variantes à 2 valeurs (Attack, Defense).

2.6 Conversion des valeurs textuelles en numériques 
Problème : Colonne player_goals contenait des valeurs comme "one", "two", "three" au lieu de 1, 2, 3.
Solution : Création de player_goals_clean 
let
    value = Text.Lower(Text.Trim([player_goals]))
in
    if value = "one" then 1
    else if value = "two" then 2
    else if value = "three" then 3
    // ... (jusqu'à ten)
    else try Number.From([player_goals]) otherwise null
Impact : Conversion réussie de toutes les valeurs textuelles en nombres.

2.7 Normalisation des dates 
Problème : 6 formats de dates différents détectés :
•	2025/01/15
•	12/27/2024
•	Oct 04 2025
•	07 Dec 24
•	2023-02-11
•	Apr 20st 2024 (avec suffixes ordinaux "st", "nd", "rd", "th")
Solution :
let
    // Nettoyage des suffixes ordinaux
    cleaned = Text.Replace(Text.Replace(Text.Replace(Text.Replace(
        [game_date], "st ", " "), "nd ", " "), "rd ", " "), "th ", " "),
    
    // Tentative conversion automatique
    auto = try Date.FromText(cleaned, "en-US") otherwise null,
    
    // Parsing manuel des formats dd-mm-yyyy
    manual1 = if auto = null and Text.Contains(cleaned, "-") then
        // ... logique de parsing
    else auto,
    
    // Parsing manuel des formats dd/mm/yy
    manual2 = if manual1 = null and Text.Contains(cleaned, "/") then
        // ... logique avec ajout de 2000 si année < 100
    else manual1
in
    manual2
Impact : Conversion réussie de tous les formats vers un type Date standardisé.

2.8 Nettoyage de game_duration 
Problème : 3 formats détectés :
•	20min (avec texte)
•	15.52 (décimal)
•	00:20:11 (format hh:mm:ss)
Solution : Conversion en minutes décimales
let
    value = Text.Trim([game_duration]),
    
    result = if Text.Contains(value, "min") then
        try Number.From(Text.Replace(value, "min", "")) otherwise null
    
    else if Text.Contains(value, ":") then
        let
            parts = Text.Split(value, ":"),
            count = List.Count(parts),
            hours = if count = 3 then (try Number.From(parts{0}) otherwise 0) else 0,
            minutes = if count = 3 then (try Number.From(parts{1}) otherwise 0) 
                      else (try Number.From(parts{0}) otherwise 0),
            seconds = if count = 3 then (try Number.From(parts{2}) otherwise 0) 
                      else (try Number.From(parts{1}) otherwise 0)
        in
            (hours * 60) + minutes + (seconds / 60)
    
    else
        try Number.FromText(value, [Culture="en-US"]) otherwise null
in
    result
Impact : Uniformisation en minutes pour permettre les calculs de durée moyenne.

2.9 Suppression des colonnes non essentielles
Colonnes supprimées :
•	music_playing, mood, ping_ms, misc (métadonnées non analytiques)
•	duplicate_flag (déjà utilisé pour le filtrage)
•	ball_type, referee, rating_raw, attendance_count (pas d'analyse prévue)
•	player_assists, possession_time (non requis pour les objectifs)
Colonnes conservées :
•	Game : game_id, game_date_clean, location, table_id, table_condition, game_duration_minutes
•	Scores : score_red_clean, score_blue_clean, winner_clean
•	Player : player_id, player_canonical_name, player_role_clean, player_goals_clean, player_own_goals, player_saves, team_color_clean
•	Maintenance : notes, player_comment
Justification : Réduction de 35 à 18 colonnes pour améliorer les performances et la clarté de l'analyse.
________________________________________
3. Mesures DAX créées
3.1 Mesures de base
Mesure	Formule DAX	Utilité
Total Goals	SUM('Table'[Goals])	Top 10 buteurs
Total Saves	SUM('Table'[Saves])	Top 5 défenseurs
Total Games	DISTINCTCOUNT('Table'[game_id])	Comptage des matchs uniques
Durée Moyenne	AVERAGE('Table'[DurationMinutes])	Analyse de l'utilisation des tables
3.2 Mesures pour l'analyse d'influence du camp
Wins Red = 
CALCULATE(
    DISTINCTCOUNT('Table'[game_id]),
    'Table'[Winner] = "Red"
)

Wins Blue = 
CALCULATE(
    DISTINCTCOUNT('Table'[game_id]),
    'Table'[Winner] = "Blue"
)

Explication :
•	CALCULATE modifie le contexte de filtre pour ne garder que les victoires du camp spécifié
•	DISTINCTCOUNT évite les doublons si plusieurs joueurs d'une même partie sont dans le dataset
•	DIVIDE avec 0 comme troisième paramètre évite les erreurs de division par zéro
________________________________________


Conclusion
Ce projet a permis de transformer un dataset brut de 100k lignes avec de nombreux problèmes de qualité en une base de données exploitable de 60k lignes, permettant de répondre aux trois questions analytiques posées.
La méthodologie de nettoyage adoptée garantit la fiabilité des insights produits, au prix d'une réduction du volume de données, ce qui est un compromis acceptable dans un contexte de hackathon où la qualité prime sur la quantité.
Les visuels Power BI créés permettent une analyse interactive et complète des performances des joueurs, de l'influence du choix de camp, et de l'utilisation des équipements.

