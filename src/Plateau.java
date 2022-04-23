package src;

import java.util.*;
import IG.Grille;


class Plateau extends Grille {
    private Case[][] grid; 
    private Coord heliport;
    private HashMap<Coord, Element> artifacts;
    private ArrayList<Player> players;
    private ArrayList<Case> not_submerged;
    private ArrayList<Case> submerged;
    private int turn;
    
    public Plateau(int taille, int heli_x, int heli_y, ArrayList<Player> players) {
        super(taille, taille);
        this.not_submerged = new ArrayList<Case>();
        this.submerged = new ArrayList<Case>();
        
        this.grid = new Case[taille][taille]; 
        for (int i = 0; i < taille; i++) {
            for (int j = 0; j < taille; j++) {
                boolean pion = false;
                if(i == heli_x && j == heli_y) {
                    pion = true;
                }
                Case new_case = new Case(pion, i, j, 0);
                this.grid[i][j] = new_case;
                this.ajouteElement(new_case);
                if (new_case.is_in_bound())
                    this.not_submerged.add(new_case);
            }
        }

        this.heliport = new Coord(heli_x, heli_y);

        this.artifacts = new HashMap<Coord, Element>();
        this.artifacts.put(new Coord(1, 1), Element.WATER);
        this.artifacts.put(new Coord(1, 4), Element.EARTH);
        this.artifacts.put(new Coord(4, 1), Element.FIRE);
        this.artifacts.put(new Coord(4, 4), Element.AIR);
        this.players = players;
        this.turn = 0;      
    }

    public Case get_case(Coord coord) {
        return grid[coord.x()][coord.y()];
    }

    public Coord get_heliport() {
        return this.heliport;
    }

    public HashMap<Coord, Element> get_artifacts() {
        return this.artifacts;
    }

    public Element get_element(Coord c) {
        return this.artifacts.get(c);
    }

    public Player get_player(int idx) {
        return this.players.get(idx);
    }

    public int get_turn() {
        return this.turn;
    }

    public void next_turn() {
        this.turn = (this.turn + 1) % this.players.size();
        System.out.println(this.turn);
    }

    public void kill(Player player) {
        this.players.remove(player);
    }

    public Case next_case(Coord c, Direction d) {
        return this.get_case(c.next_coord(d));
    }

    public void move_player(Player player, Direction d) {
        Case next_case = this.next_case(player.coord(), d);
        if (!next_case.is_submerged() && next_case.is_in_bound())
            player.move(next_case.coord());
    }

    public void dry_out(Player player, Coord c) {
        Coord position = player.coord();
        Case target = this.get_case(c);
        if (!target.is_submerged() && target.is_adjacent(position))
            target.dry_out();
    }

    public void pick_up_artifact(Player player) {
        Coord position = player.coord();
        if (this.artifacts.containsKey(position)) {
            Element e = this.get_element(position);
            if (player.has_key(e))
                player.pick_up_artifact(e);
        }
    }

    public void search_key(Player player) {
        Random rand = new Random();
        int roll = rand.nextInt(100) + 1;
        if (roll > 75) {
            Element random_elem = Element.values()[rand.nextInt(4)];
            player.pick_up_key(random_elem);
        } else if (roll <= 10) {
            Case position = this.get_case(player.coord());
            position.flood();
        }
    }

    public Case random_not_submerged_case() {
        Random rand = new Random();
        Case random_case = this.not_submerged.get(rand.nextInt(this.not_submerged.size()));
        return random_case;
    }

    public void flood() {
        for (int i = 0; i < 3; i++) {
            Case random_case = this.random_not_submerged_case();
            random_case.flood();
            if (random_case.is_submerged()) {
                this.not_submerged.remove(random_case);
                this.submerged.add(random_case);
            }
        }
    }

    public boolean is_inaccessible(Case c) {
        if (c.is_submerged())
            return true;
        else
            for (Direction d : Direction.values())
                if (!this.next_case(c.coord(), d).is_submerged())
                    return false;
        return true;
    }

    public boolean is_winnable() {
        // if all players are dead
        if (players.isEmpty())
            return false;

        // if the heliport is inaccessible
        if (this.is_inaccessible(this.get_case(heliport)))
            return false;

        // an artifact zone is inaccessible and the corresponding artifact didn't get
        // fetched
        for (Coord coord : this.artifacts.keySet()) {
            Case c = this.get_case(coord);
            if (this.is_inaccessible(c)) {
                Element elem = this.get_element(c.coord());
                boolean fetched = false;
                for (Player player : this.players) {
                    if (player.has_artifact(elem)) {
                        fetched = true;
                        break;
                    }
                }
                if (!fetched)
                    return false;
            }
        }

        return true;
    }

    public void play() {
        /* 
            1. Effectuer jusqu’a trois actions a choisir parmi :
            — se deplacer vers une zone adjacente non submergee

            — assecher la zone sur laquelle il se trouve ou une zone
            adjacente si la zone visee est inondee mais pas submergee

            — recuperer un artefact s’il se trouve sur une zone
            d’artefact et possede une cle correspondante.
        */
    }
}
