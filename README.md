# TP 3 – MapReduce avec Akka
## Présentation du TP

Le sujet  vise à explorer l'utilisation du paradigme MapReduce pour le comptage des mots dans un fichier texte, en employant le framework Akka pour implémenter une architecture basée sur le modèle d'acteurs dans un environnement Spring. Le but est de montrer comment la programmation réactive peut être utilisée pour réaliser des calculs distribués de façon efficace.

## Architecture 

Le système est structuré autour de composants clés qui collaborent pour accomplir le traitement MapReduce :

### AkkaService

Responsable de l'initialisation des acteurs Mapper et Reducer, de la répartition des lignes de texte et de la compilation des résultats de comptage.

### Acteurs Mapper

Ils analysent les lignes de texte, identifient les mots et les envoient aux acteurs Reducer appropriés, en se basant sur une fonction de partitionnement.

### Acteurs Reducer

Ces acteurs reçoivent les mots des Mapper et calculent le nombre d'occurrences de chaque mot, contribuant au résultat final du comptage.

## Fonctionnalités Principales

### Gestion des acteurs : 
AkkaService configure et orchestre les interactions entre les acteurs Mapper et Reducer.
## Partitionnement efficace : 
Une fonction de partitionnement spécifique assigne les mots aux Reducer pour pouvoir optimiser la répartition des tâches.