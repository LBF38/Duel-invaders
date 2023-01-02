# Comment installer la bonne version de Java sur ma machine ?

Afin de pouvoir exécuter le jeu, vous devez avoir une version de Java 11 ou plus installée sur votre machine. Si vous n'avez pas la bonne version de Java, vous pouvez suivre les étapes suivantes pour installer la bonne version de Java sur votre machine.

## Installer Java

Pour installer Java sur n'importe quel système d'exploitation, vous devez installer le JDK (Java Development Kit) de Java 11. Vous pouvez trouver différentes versions d'installeurs sur internet. Nous conseillons d'utiliser les versions maintenus par Eclipse Adoptium. Vous pouvez télécharger l'installeur qui correspond à votre système sur [cette page](https://adoptium.net/temurin/releases/).

## Configurer Java

Une fois l'installeur téléchargé, vous devez l'exécuter pour installer Java sur votre machine. Vous pouvez ensuite configurer la version de Java utilisée par défaut sur votre machine. Pour cela, vous devez suivre les étapes suivantes :

1. Ouvrez un terminal
2. Exécutez la commande `java -version` pour vérifier la version de Java utilisée par défaut sur votre machine
3. Si la version de Java utilisée est inférieure à 11, vous devez configurer la variable d'environnement pour Java.
   1. En fonction de votre système, vous devez changer la variable `JAVA_HOME` pour configurer la version de Java utilisée par défaut, en insérant le chemin vers le dossier contenant le JDK de la version que vous voulez utiliser.

> **Exemple**
> Par exemple, avec l'installation du JDK via Eclipse Adoptium pour Java 11, votre variable d'environnement `JAVA_HOME` doit ressembler à :
>
> ```console
>  C:\Program Files\Eclipse Adoptium\jdk-11.0.17.8-hotspot\
> ```
>
> Ce chemin peut changer selon votre OS et votre installation.

## Vérifier l'installation

Pour vérifier l'installation, vous devez relancer tout terminal ouvert et exécuter la commande `java -version` pour vérifier que la version de Java utilisée est bien la version que vous avez configurée.
