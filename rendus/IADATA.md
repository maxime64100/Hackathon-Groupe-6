# 🏆 Babyfoot Analytics - Hackathon Ynov 2025


> **Analyse des performances des joueurs de babyfoot avec nettoyage de données, visualisations Power BI et intégration web temps réel.**

---

## Équipe IA & Data

- **LEMAIRE César** 
- **ALBALAT Noah** 

---

##  Vue d'ensemble du projet

### Objectifs analytiques

Ce projet analyse les performances des joueurs de babyfoot à travers **trois axes principaux** :

1. **Top 10 Buteurs** - Identifier les meilleurs attaquants
2. **Top 5 Défenseurs** - Classer les meilleurs gardiens (saves)
3. **Influence du camp** - Analyser si le choix Rouge/Bleu affecte les chances de victoire

### Dataset

- **Lignes initiales :** 100 200
- **Lignes après nettoyage :** 60 075 (réduction de 40%)
- **Colonnes initiales :** 35
- **Colonnes finales :** 18 colonnes essentielles
- **Format source :** CSV (`babyfoot_dataset.csv`)

---

##  Nettoyage des données

### État initial du dataset

Le dataset présentait de **nombreux problèmes de qualité** :

-  **40% de doublons** identifiés via `duplicate_flag`
-  **6 formats de dates différents** (ISO, US, texte, avec suffixes ordinaux)
-  **Variantes orthographiques** multiples (RED/Red/rouge/R, etc.)
-  **Valeurs textuelles** dans colonnes numériques (`"two"` au lieu de `2`)
-  **Scores mal structurés** (format combiné `"7-0"`)

---

### Processus de nettoyage (Power Query)

#### 1️⃣ Suppression des doublons

```m
// Filtrage sur duplicate_flag
= Table.SelectRows(Source, each [duplicate_flag] <> "1" and [duplicate_flag] <> "yes")
```

**Impact :** -40% de lignes (100 200 → 60 075)

---

#### 2️⃣ Restructuration des scores

**Problème :** Scores au format `"7-0"` dans une seule colonne.

```m
// Séparation des scores
score_red_clean = 
    if Text.Contains([final_score_red], "-") then 
        Text.BeforeDelimiter([final_score_red], "-") 
    else 
        [final_score_red]

score_blue_clean = 
    if Text.Contains([final_score_red], "-") then 
        Text.AfterDelimiter([final_score_red], "-") 
    else 
        [final_score_blue]
```

---

#### 3️⃣ Recalcul du gagnant

**Problème :** 15 variantes (RED, Red, rouge, R...) + 4,7% valeurs manquantes

```m
winner_clean = 
    if [score_blue_clean] > [score_red_clean] then "Blue"
    else if [score_red_clean] > [score_blue_clean] then "Red"
    else if [score_blue_clean] = [score_red_clean] then "Draw"
    else null
```

**Impact :** Uniformisation à 3 valeurs (`Red`, `Blue`, `Draw`)

---

#### 4️⃣ Normalisation de team_color

**Problème :** 10 variantes (Red, red, R, 🔴, Blue, blue, B, 🔵...)

```m
team_color_clean = 
    let upper = Text.Upper([team_color])
    in
        if Text.Contains(upper, "R") or [team_color] = "🔴" then "Red"
        else if Text.Contains(upper, "B") or [team_color] = "🔵" then "Blue"
        else null
```

---

#### 5️⃣ Normalisation de player_role

**Problème :** 8 variantes (defence, defense, def, ATTACK, attck...)

```m
player_role_clean = 
    let upper = Text.Upper([player_role])
    in
        if Text.Contains(upper, "ATT") then "Attack"
        else if Text.Contains(upper, "DEF") then "Defense"
        else null
```

---

#### 6️⃣ Conversion valeurs textuelles → numériques

**Problème :** `player_goals` contenait `"one"`, `"two"`, `"three"`...

```m
player_goals_clean = 
    let value = Text.Lower(Text.Trim([player_goals]))
    in
        if value = "one" then 1
        else if value = "two" then 2
        else if value = "three" then 3
        // ... jusqu'à "ten"
        else try Number.From([player_goals]) otherwise null
```

---

#### 7️⃣ Normalisation des dates

**Problème :** 6 formats détectés (`2025/01/15`, `Oct 04 2025`, `Apr 20st 2024`...)

```m
game_date_clean = 
    let
        cleaned = Text.Replace(Text.Replace(Text.Replace(Text.Replace(
            [game_date], "st ", " "), "nd ", " "), "rd ", " "), "th ", " "),
        auto = try Date.FromText(cleaned, "en-US") otherwise null,
        manual = // Parsing manuel si échec
    in result
```

---

#### 8️⃣ Nettoyage game_duration

**Problème :** 3 formats (`20min`, `15.52`, `00:20:11`)

```m
game_duration_minutes = 
    if Text.Contains(value, "min") then
        Number.From(Text.Replace(value, "min", ""))
    else if Text.Contains(value, ":") then
        // Conversion hh:mm:ss → minutes
    else
        Number.FromText(value, [Culture="en-US"])
```

---

#### 9️⃣ Suppression des colonnes non essentielles

**Supprimées :** `music_playing`, `mood`, `ping_ms`, `misc`, `duplicate_flag`, `ball_type`, `referee`, `rating_raw`, `attendance_count`, `player_assists`, `possession_time`

**Conservées (18 colonnes) :**

| Catégorie | Colonnes |
|-----------|----------|
| **Game** | `game_id`, `game_date_clean`, `location`, `table_id`, `table_condition`, `game_duration_minutes` |
| **Scores** | `score_red_clean`, `score_blue_clean`, `winner_clean` |
| **Player** | `player_id`, `player_canonical_name`, `player_role_clean`, `player_goals_clean`, `player_own_goals`, `player_saves`, `team_color_clean` |
| **Maintenance** | `notes`, `player_comment` |

---

## 📈 Mesures DAX (Power BI)

### Mesures de base

```dax
Total Goals = SUM('Table'[Goals])

Total Saves = SUM('Table'[Saves])

Total Games = DISTINCTCOUNT('Table'[game_id])

Durée Moyenne = AVERAGE('Table'[DurationMinutes])
```

---

### Mesures d'analyse Camp Rouge vs Bleu

```dax
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

Win Rate Red = 
DIVIDE(
    [Wins Red],
    [Total Games],
    0
) * 100

---



## Intégration Web (Bonus IA)

### Composant Angular - Top 10 Buteurs

Développement d'un **composant Angular standalone** pour afficher le Top 10 buteurs en temps réel sur le site web.

#### Technologies utilisées

- **Angular 17+** (Standalone Components)
- **Bootstrap 5.3** (Design responsive)
- **Bootstrap Icons** (Icônes)
- **RxJS** (Gestion asynchrone)

#### Fonctionnalités

✅ **Design moderne** avec podium visuel (1er, 2ème, 3ème)  
✅ **Actualisation automatique** toutes les 30 secondes  
✅ **Responsive** (mobile-first)    
✅ **Prêt pour API REST** (connexion backend)



##  Résultats 

### Qualité des données

✅ **60 075 lignes exploitables** (vs 100 200 initiales)  
✅ **0 valeur manquante** dans les colonnes critiques  
✅ **Formats uniformisés** pour toutes les colonnes analytiques  
✅ **Réduction de 35 à 18 colonnes** (-48%)




##  Conclusion

Ce projet a permis de **transformer un dataset brut** de 100k lignes avec de nombreux problèmes de qualité en une **base de données exploitable** de 60k lignes, permettant de répondre aux trois questions analytiques posées.

La **méthodologie de nettoyage stricte** adoptée garantit la **fiabilité des insights** produits, au prix d'une réduction du volume de données - un compromis acceptable dans un contexte de hackathon où **la qualité prime sur la quantité**.

Les **visuels Power BI** créés permettent une **analyse interactive et complète** des performances des joueurs, de l'influence du choix de camp, et de l'utilisation des équipements.



---



