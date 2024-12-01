# Paint Appix ✨

Bienvenue dans **Paint Appix**, un projet Java simple mais puissant qui permet de dessiner, de modifier et de personnaliser des formes – avec des outils comme le pinceau, la gomme, et bien plus encore ! ✔️

## 📖 Description du projet

Ce projet a été conçu comme une application de dessin intuitive en Java Swing. Il s'adresse à ceux qui souhaitent créer et manipuler des formes, importer des images, ou tout simplement explorer les bases de la création graphique en Java.

### Fonctionnalités principales 🎨

1. **Outils de dessin** :
   - Pinceau (ajustable en taille).
   - Gomme pour effacer les parties non désirées.
   - Dessin de formes (rectangle, cercle, triangle).

2. **Modification et édition des formes** :
   - Sélectionner et déplacer une forme.
   - Redimensionner les formes grâce à des poignées de redimensionnement.
   - Supprimer des formes préalablement dessinées.

3. **Importation d'images** :
   - Importez une image et ajustez-la sur le canevas pour créer des designs personnalisés.

4. **Sauvegarde et chargement** :
   - Enregistrez votre création en format image (PNG).
   - Réouvrez vos œuvres enregistrées.

5. **Personnalisation des couleurs** : Choisissez une couleur via un sélecteur de couleur.

---

## 🎢 Utilisation de l'application

### Prérequis
- **Java 8+** : Assurez-vous d'avoir la version 8 ou supérieure installée.
- Un IDE comme IntelliJ IDEA ou Eclipse pour exécuter le projet.

### Lancer l'application
1. Clonez le projet depuis le dépôt GitHub :
   ```bash
   git clone <url_du_depot>
   ```
2. Ouvrez le projet dans votre IDE.
3. Compilez et exécutez la classe `PaintApplication`.

### Interface utilisateur
1. **Menu** :
   - Fichier : Sauvegarder ou charger une image.
   - Formes : Choisir un outil de dessin.
   - Couleurs : Modifier la couleur active.
   - Dimensions : Redimensionner les formes sélectionnées.
2. **Zone de dessin** : Cliquez et faites glisser pour dessiner avec le pinceau.

---

## 🔧 Fonctionnalités techniques

### Architecture
Le projet est composé de deux fichiers principaux :

1. **`DrawingPanel.java`** :
   - Gère le canevas et les éléments graphiques comme les formes et les images.
   - Contient les méthodes pour dessiner, redimensionner, déplacer et supprimer des formes.

2. **`PaintApplication.java`** :
   - Fournit l'interface utilisateur via les menus et les actions.
   - Connecte les éléments graphiques du `DrawingPanel` avec les actions utilisateur.

### Classes et méthodes principales
- **`ColoredShape`** : Classe interne qui associe une forme à sa couleur.
- **`drawShape()`** : Ajoute une nouvelle forme au canevas.
- **`moveShape()`** : Permet de déplacer une forme.
- **`resizeShape()`** : Modifie les dimensions d'une forme.
- **`deleteShape()`** : Supprime une forme sélectionnée.

---

## 📚 Documentation utilisateur

### Dessiner une forme
1. Sélectionnez une forme dans le menu "Formes".
2. Cliquez sur la zone de dessin pour placer votre forme.

### Utiliser le pinceau
1. Sélectionnez l'outil "Pinceau".
2. Cliquez et maintenez le bouton gauche de la souris pour dessiner.
3. Ajustez la taille du pinceau dans le menu des dimensions.

### Supprimer une forme
1. Activez l'outil "Sélection et édition".
2. Cliquez sur une forme pour la sélectionner.
3. Utilisez le menu ou appuyez sur la touche Suppr pour la supprimer.

### Importer une image
1. Dans le menu "Fichier", cliquez sur "Importer une image".
2. Choisissez une image depuis votre ordinateur.
3. Ajustez-la sur le canevas en cliquant et en glissant.

---

## 🚀 Conclusion
**Paint Application** est une manière ludique d’explorer les bases du dessin et de la manipulation graphique avec Java Swing. N'hésitez pas à explorer, modifier et améliorer ce projet ! ✨

**Bon codage et amusez-vous bien !** 🎨

