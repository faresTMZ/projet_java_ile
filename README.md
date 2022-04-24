## Commandes
- Se déplacer : clique gauche sur une case non submergée adjacente
- Assécher : clique droit sur une case innondée adjacente
- Sac de sable : clique droit sur n'importe quelle case innondée
- Helicoptère : clique gauche sur n'importe quelle case non submergée


## Rapport

### 1. Les parties du sujet traitées.

Nous avons traités entièrement les parties 10.1, 10.2, et 10.3 du sujet.\
Pour les question bonus (∞), nous avons fait celle de la partie 10.1 à propos des zones à innonder, en partie celle du 10.2 car nous indiquons le tour du joueur actif mais pas les actions restantes et celle du 10.3 avec comme critère additionels la mort de tous les joueurs, la submersion de l'héliport et la submersion d'un zone d'artéfact s'il dernier n'avait pas encore été récupéré.\
Comme ajout pour la partie 10.4 nous avons choisis d'implémenter les actions spéciales (sac de sable et hélicoptère).


### 2. Choix d’architecture.

Nous avons organisation des classes en 2 grandes parties : celles qui était reliés à l'affichage et celles pour le jeux en lui même.\
Pour l'affichage, nous avons réutilisé les squelettes du TP1 du jeux NReines comme base que nous avons adaptés aux spécificités de notre code.\
Pour le code du jeux en lui même, nous avons fait des classes pour chacune des composantes principales du jeux (la partie, le plateau, les cases et les joueurs).

Nous avons fait tous ce projet en live share et en avons profité pour nous partager toutes les tâches en fonction de ce qui devait être fait sur le moment. Nous avons donc chacun participé à chaque aspect du projet bien qu'en fin de projet Fares a plus travaillé sur l'affichage et Martin sur l'implémentation des bonus.


### 3. Les problèmes présents et que nous n’avons pas pu éliminer.

Hormis la question bonus du 10.2 que nous n'avons pas eu le temps de finir, nous n'avons pas pu faire un meilleur affichage des textes plus organisé et lisible faute de temps.


### 4. Morceaux de code écrits à plusieurs ou empruntés ailleurs.

Comme mentionnée dans la partie 2, nous avons réutilisé les squelettes du TP1 du jeux NReines pour l'affichage. Hormis cela, nous n'avons n'avons emprunté de code nul


## Diagramme de classe

![Diagramme de Classe](/diagramme-de-classe.png)
