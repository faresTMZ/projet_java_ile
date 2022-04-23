package src;

import java.util.*;
import IG.ZoneCliquable;
import IG.Fenetre;
import IG.Texte;


public class Game {

    public static void main(String[] args) {
        int nb = 6;
        Fenetre fenetre = new Fenetre(nb + " reines");
        ArrayList<Player> players = new ArrayList<Player>();
        Player p1 = new Player(new Coord(3, 3));
        Player p2 = new Player(new Coord(3, 3));
        players.add(p1);
        players.add(p2);
        Texte text = new Texte("Tour : Player " + String.valueOf(1));
        Plateau plateau = new Plateau(nb, 3, 3, players);
        Pass pass = new Pass(plateau, text);
        fenetre.ajouteElement(plateau);
        fenetre.ajouteElement(pass);
        fenetre.ajouteElement(text);
        fenetre.dessineFenetre();
        System.out.println();
    }
}


class Pass extends ZoneCliquable {
    private Plateau plateau;
    private Texte text;

    public Pass(Plateau plateau, Texte text) {
        super(90, 25, "Fin de tour");
        this.plateau = plateau;
        this.text = text;
    }

    public void clicGauche() {
        plateau.flood();
        //plateau.search_key(plateau.get_player(plateau.get_turn()));
        plateau.next_turn();
        text.changeTexte( "Tour : Player " + String.valueOf(plateau.get_turn() + 1));
        // System.out.println(plateau.get_turn());
        plateau.repaint();
    }

    public void clicDroit() {}
}
