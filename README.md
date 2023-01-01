# Projet Java - Duel Invaders

Ce projet est un projet collaboratif dans le cadre de nos études de deuxième année à l'ENSTA Bretagne.

Le projet a pour objectif d'apprendre Java par la pratique et de réaliser un jeu vidéo classique : Duel Invaders.

On peut retrouver toute la documentation de la logique du jeu et du code dans le dossier `docs` du projet. Vous pouvez commencer votre lecture par [ici](docs/README.md).

Les attentes du projet et le sujet venant des professeurs de l'ENSTA Bretagne est disponible [ici](docs/Projet%20Java%20-%20Sujet.pdf).

Le rendu du projet est prévu pour le 4 et 5 janvier 2023. (selon planification AURION)

## Installation

### Prérequis

- Java 11 ou plus
- [Maven](https://maven.apache.org/)

### Build

Pour compiler le projet, il suffit d'exécuter la commande suivante :

```bash
mvn clean package
```

> **Note**
> Vous devez exécuter cette commande dans le dossier du projet contenant le fichier `pom.xml`. Ici, le dossier racine du projet est `duelinvaders`.

### Exécution

> **Warning**
> Vous devez, au préalable, vérifier que vous avez la bonne version de Java configuré sur votre machine.
> Pour cela, exécutez la commande `java -version` dans un terminal. Si vous avez une version inférieure à 11, vous devez changer la version de Java utilisée par défaut sur votre machine.
> Pour cela, vous pouvez vous référer à [ce tutoriel](docs/installation.md) de la documentation du projet.

Pour exécuter le projet avec le terminal, il suffit d'exécuter la commande suivante :

```bash
java -jar target/duelinvaders-0.1.0.jar
```

> **Note**
> Si vous avez une version de Java supérieure à 11, vous pouvez exécuter le projet en double-cliquant sur le fichier produit par la commande `mvn clean package` : `target/duelinvaders-0.1.0.jar`.

## Utilisation

Une fois le jeu lancé, vous arriverez sur l'écran d'accueil du jeu. Vous pouvez alors choisir de jouer à une nouvelle partie ou explorer les options du jeu.

Par défaut, le jeu est configuré pour jouer avec un clavier. Vous pouvez changer les touches utilisées pour jouer en cliquant sur le bouton `Options` dans le menu principal.

## Dépendances

Ce projet utilise [FXGL](https://github.com/almasb/FXGL) pour la partie graphique afin de simplifier l'implémentation de la logique du jeu. Cette librairie basée sur JavaFX permet de créer des jeux vidéo 2D plus facilement que JavaFX seul.

## Références

- [Introduction to FXGL](https://github.com/AlmasB/IntroductionToFXGL) : projet simple de démonstration de FXGL
- [SpaceInvaders example](https://github.com/AlmasB/FXGLGames/tree/master/SpaceInvaders) : exemple de jeu vidéo 2D avec FXGL, qui reproduit le jeu Space Invaders.

## Auteurs

- [@jufch](https://github.com/jufch)
- [@MathieuDFS](https://github.com/MathieuDFS)
- [@LBF38](https://github.com/LBF38)
