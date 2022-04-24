package src;

import java.util.*;
import IG.Grille;

enum Element {
    WATER,
    EARTH,
    FIRE,
    AIR,
}

enum Direction {
    UP,
    DOWN,
    RIGHT,
    LEFT,
}

class Plateau extends Grille {
    private Case[][] grid; 
    private Coord heliport;
    private ArrayList<Coord> artifacts; // WATER EARTH FIRE AIR
    private ArrayList<Player> players;
    private ArrayList<Case> not_submerged;
    private ArrayList<Case> submerged;
    private int turn;

    public Plateau(int taille, int heli_x, int heli_y) {
        super(taille, taille);
        this.not_submerged = new ArrayList<Case>();
        this.submerged = new ArrayList<Case>();
        
        this.grid = new Case[taille][taille]; 
        for (int i = 0; i < taille; i++) {
            for (int j = 0; j < taille; j++) {
                Case new_case = new Case(this, false, i, j, 0);
                this.grid[i][j] = new_case;
                this.ajouteElement(new_case);
                if (new_case.is_in_bound())
                    this.not_submerged.add(new_case);
            }
        }

        this.heliport = new Coord(heli_x, heli_y);

        this.artifacts = new ArrayList<Coord>();
        this.artifacts.add(new Coord(1, 1));
        this.artifacts.add(new Coord(1, 4));
        this.artifacts.add(new Coord(4, 1));
        this.artifacts.add(new Coord(4, 4));
        this.players = new ArrayList<Player>();
        this.turn = 0;
    }
    
    public Plateau(int taille, int heli_x, int heli_y, ArrayList<Player> players) {
        this(taille, heli_x, heli_y);
        
        for (int i = 0; i < taille; i++)
            for (int j = 0; j < taille; j++)
                if(i == players.get(0).coord().x() && j == players.get(0).coord().y())
                    this.grid[i][j].add_pion();
    
        this.players = players;
    }

    public Case get_case(int x, int y) { return grid[x][y]; }

    public Case get_case(Coord coord) { return grid[coord.x()][coord.y()]; }

    public Coord get_heliport() { return this.heliport; }

    public ArrayList<Coord> get_artifacts() { return this.artifacts; }

    public ArrayList<Player> get_players() { return this.players; }

    public Element artifact_element(Coord c) {
        if (c.equal(new Coord(1, 1)))
            return Element.WATER;
        if (c.equal(new Coord(1, 4)))
            return Element.EARTH;
        if (c.equal(new Coord(4, 1)))
            return Element.FIRE;
        if (c.equal(new Coord(4, 4)))
            return Element.AIR;
        return Element.WATER;
    }

    public Player get_player(int idx) { return this.players.get(idx); }

    public int get_turn() { return this.turn; }

    public void add_player(Player new_player) {
        this.players.add(new_player);
        this.get_player_case(new_player).add_pion();
    }

    public Case get_player_case(Player player) { return this.get_case(player.coord()); }

    public void kill(Player player) {
        this.players.remove(player);
        Case death_case = this.get_player_case(player);
        death_case.remove_pion();
        death_case.repaint();
        this.repaint();
    }

    public void next_turn() {
        this.turn = (this.turn + 1) % this.players.size();
        for (Player p : this.players)
            p.reset_actions();
    }

    private Case next_case(Coord c, Direction d) {
        return this.get_case(c.next_coord(d));
    }

    public void move_player(Player player, Case next_case) {
        if (!next_case.is_submerged() && next_case.is_adjacent(player.coord())) {
            this.get_case(player.coord()).remove_pion();
            player.move(next_case.coord());
            this.get_case(player.coord()).add_pion();
            player.has_moved = true;
            player.is_drowning = false;
        } else if (!next_case.is_submerged() && player.has_helico) {
            this.get_case(player.coord()).remove_pion();
            player.move(next_case.coord());
            this.get_case(player.coord()).add_pion();
            player.has_helico = false;
            player.is_drowning = false;

            for (Player p : this.get_players()) {
                if (!player.coord().equal(this.get_heliport())) {
                    this.get_case(p.coord()).remove_pion();
                    p.move(next_case.coord());
                    this.get_case(p.coord()).add_pion();
                    p.is_drowning = false;
                }
            }
        }
    }

    public void dry_out(Player player, Case target) {
        Coord position = player.coord();
        if (target.is_flooded() && target.is_adjacent(position)) {
            target.dry_out();
            player.has_dried_out = true;
        } else if (target.is_flooded() && player.has_sandbag) {
            target.dry_out();
            player.has_sandbag = false;
        }
    }

    public void pick_up_artifact(Player player) {
        Case position = this.get_player_case(player);
        if (position.has_artifact()) {
            Element e = this.artifact_element(position.coord());
            if (player.has_key(e))
                player.pick_up_artifact(e);
        }
    }

    public void search_key(Player player) {
        Random rand = new Random();
        int roll = rand.nextInt(100) + 1;
        if (90 < roll) { // helico
            player.has_helico = true;
        } else if (80 < roll && roll <= 90) { // sandbag
            player.has_sandbag = true;
        } else if (50 < roll) { // find key
            Element random_elem = Element.values()[rand.nextInt(4)];
            player.pick_up_key(random_elem);
        } else if (roll <= 10) { // water rising
            Case position = this.get_case(player.coord());
            position.flood();
        }
    }

    private Case random_not_submerged_case() {
        Random rand = new Random();
        Case random_case = this.not_submerged.get(rand.nextInt(this.not_submerged.size()));
        return random_case;
    }

    public void flood() {
        for (int i = 0; i < Math.min(3, this.not_submerged.size()); i++) {
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

    public boolean has_won() {
        ArrayList<Player> players = this.get_players();

        // if all players are at the heliport
        for (Player player : players) {
            Coord position = player.coord();
            if (!position.equal(this.get_heliport()))
                return false;
        }

        // if all artifacts are gathered
        for (Element e : Element.values()) {
            boolean fetched = false;
            for (Player player : players) {
                if (player.has_artifact(e)) {
                    fetched = true;
                    break;
                }
            }
            if (!fetched)
                return false;
        }

        return true;
    }

    public boolean is_winnable() {
        ArrayList<Player> players = this.get_players();
        // if all players are dead
        if (players.isEmpty())
            return false;
        
        // if the heliport is inaccessible
        if (this.is_inaccessible(this.get_case(heliport)))
            return false;

        // an artifact zone is inaccessible and the corresponding artifact didn't get
        // fetched
        for (Coord coord : this.get_artifacts()) {
            Case c = this.get_case(coord);
            if (this.is_inaccessible(c)) {
                Element elem = this.artifact_element(c.coord());
                boolean fetched = false;
                for (Player player : players) {
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
}
