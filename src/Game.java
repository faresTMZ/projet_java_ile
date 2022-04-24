package src;

import java.util.*;
import IG.ZoneCliquable;
import IG.Fenetre;
import IG.Texte;
import IG.Button;


public class Game {
    public Fenetre game_window;
    final int nb;

    public Game(int nb, int nb_players) {
        this.game_window = new Fenetre("The Forbidden Isle");
        Button button = new Button(nb_players);
        

        this.nb = nb;

        Plateau plateau = new Plateau(nb, 3, 3);
        for (int i = 0; i < nb_players; i++)
            plateau.add_player(new Player(plateau, new Coord(3, 3)));
        
        Texte turn_title = new Texte("Turn : Player " + String.valueOf(0));

        ArrayList<ArrayList<Texte>> textes_keys = new ArrayList<ArrayList<Texte>>();
        for (Player p : plateau.get_players()){
            ArrayList<Texte> textes_player = new ArrayList<Texte>();
            for (Element e : Element.values()) {
                Texte text_e = new Texte(e.toString() + " KEYS : " + String.valueOf(p.keys_of_element(e)));
                textes_player.add(text_e);
                button.ajouteElement(text_e);
            }
            textes_keys.add(textes_player);
        }

        ArrayList<ArrayList<Texte>> textes_artifacts = new ArrayList<ArrayList<Texte>>();
        for (Player p : plateau.get_players()){
            ArrayList<Texte> textes_player = new ArrayList<Texte>();
            for (Element e : Element.values()) {
                Texte text_e = new Texte(e.toString() + " ARTIFACTS : " + String.valueOf(p.artifact_of_element(e) ? 1 : 0));
                textes_player.add(text_e);
                button.ajouteElement(text_e);
            }
            textes_keys.add(textes_player);
        }

        ArrayList<Texte> textes_sandbag = new ArrayList<Texte>();
        for (Player p : plateau.get_players()){
            Texte text = new Texte("SANDBAG : " + String.valueOf(p.has_sandbag ? 1 : 0));
            textes_sandbag.add(text);
            button.ajouteElement(text);
        }

        ArrayList<Texte> textes_helico = new ArrayList<Texte>();
        for (Player p : plateau.get_players()){
            Texte text = new Texte("HELICO : " + String.valueOf(p.has_helico ? 1 : 0));
            textes_helico.add(text);
            button.ajouteElement(text);
        }

        Pass pass = new Pass(game_window, plateau, turn_title, textes_keys, textes_artifacts, textes_sandbag, textes_helico);

        
        button.ajouteElement(turn_title);
        button.ajouteElement(pass);

        this.game_window.ajouteElement(plateau);
        this.game_window.ajouteElement(button);
        this.game_window.dessineFenetre();
    }

    public static void main(String[] args) {
        Game new_game = new Game(6, 4);
    }
}


class Pass extends ZoneCliquable {
    private Fenetre window;
    private Plateau plateau;
    private Texte turn_title;
    private ArrayList<ArrayList<Texte>> textes_keys;
    private ArrayList<ArrayList<Texte>> textes_artifacts;
    private ArrayList<Texte> textes_sandbag;
    private ArrayList<Texte> textes_helico;

    public Pass(Fenetre window, Plateau plateau, Texte turn_title, ArrayList<ArrayList<Texte>> textes_keys, ArrayList<ArrayList<Texte>> textes_artifacts, ArrayList<Texte> textes_sandbag, ArrayList<Texte> textes_helico) {
        super(90, 25, "End turn");
        this.window = window;
        this.plateau = plateau;
        this.turn_title = turn_title;
        this.textes_keys = textes_keys;
        this.textes_artifacts = textes_artifacts;
        this.textes_sandbag = textes_sandbag;
        this.textes_helico = textes_helico;
    }

    public void end_game(String message) {
        this.window.dispose();
        Fenetre game_over = new Fenetre(message, 400);
        Texte text2 = new Texte(message, true);
        game_over.ajouteElement(text2);
        game_over.dessineFenetre();
        this.window.dispose();
    }

    public void clicGauche() {
        if (plateau.has_won()) {
            this.end_game("YOU WIN !");
        } else {
            Plateau plateau = this.plateau;
            int turn = plateau.get_turn();
            Player active_player = plateau.get_player(turn);
            ArrayList<Player> players = plateau.get_players();

            for (int i = 0; i < players.size(); i++) {
                Player p = players.get(i);
                if (p.is_drowning && p == active_player)
                    plateau.kill(p);
            }
            
            if (!plateau.is_winnable()) {
                this.end_game("GAME OVER");
            } else {
                plateau.search_key(active_player);
                for (int i = 0; i < textes_keys.size(); i++) {
                    for (int j = 0; j < textes_keys.get(i).size(); j++) {   
                        if( i < plateau.get_players().size())
                        textes_keys.get(i).get(j).changeTexte(Element.values()[j].toString() + " KEYS : " + String.valueOf(plateau.get_players().get(i).keys_of_element(Element.values()[j])));
                    }
                }
                for (int i = 0; i < textes_artifacts.size(); i++) {
                    for (int j = 0; j < textes_artifacts.get(i).size(); j++) {
                        if( i < plateau.get_players().size())
                        textes_artifacts.get(i).get(j).changeTexte(Element.values()[j].toString() + " ARTIFACTS : " + String.valueOf(plateau.get_players().get(i).artifact_of_element(Element.values()[j]) ? 1 : 0));
                    }
                }
                plateau.flood();

                for (int i = 0; i < players.size(); i++) {
                    Player p = players.get(i);
                    if (plateau.get_player_case(p).is_submerged())
                        p.is_drowning = true;
                }
                for (int i = 0; i < textes_sandbag.size(); i++) {
                    if( i < plateau.get_players().size())
                        textes_sandbag.get(i).changeTexte("SANDBAG : " + String.valueOf(plateau.get_players().get(i).has_sandbag ? 1 : 0));
                }
                for (int i = 0; i < textes_helico.size(); i++) {
                    if( i < plateau.get_players().size())
                    textes_helico.get(i).changeTexte("HELICO : " + String.valueOf(plateau.get_players().get(i).has_helico ? 1 : 0));
                }


                plateau.next_turn();
                turn_title.changeTexte("Turn : Player " + String.valueOf((turn + 1) % players.size()));
                
                plateau.repaint();
            }
        }
    }

    public void clicDroit() {}
}
