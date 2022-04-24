package src;

import java.util.*;


public class Player {
    Plateau plateau;
    Coord coord;
    HashMap<Element, Integer> keys;
    HashMap<Element, Boolean> artifacts;
    boolean has_moved;
    boolean has_dried_out;
    boolean is_drowning;
    boolean has_sandbag;
    boolean has_helico;

    public Player(Plateau plateau, Coord coord) {
        this.plateau = plateau;
        this.coord = coord;
        this.keys = new HashMap<Element, Integer>(4);
        this.artifacts = new HashMap<Element, Boolean>(4);
        for (Element e : Element.values()) {
            this.keys.put(e, 0);
            this.artifacts.put(e, false);
        }
        this.has_moved = false;
        this.has_dried_out = false;
        this.is_drowning = false;
        this.has_sandbag = false;
        this.has_helico = false;
    }

    public boolean has_key(Element e) { return this.keys.get(e) > 0; }

    public boolean has_artifact(Element e) { return this.artifacts.get(e); }

    public Coord coord() { return this.coord; }

    public int x() { return this.coord.x(); }

    public int y() { return this.coord.y(); }

    public HashMap<Element, Integer> keys() { return this.keys; }

    public int keys_of_element(Element e) { return this.keys.get(e); }

    public HashMap<Element, Boolean> artifacts() { return this.artifacts; }

    public boolean artifact_of_element(Element e) { return this.artifacts.get(e); }

    public void move(int x, int y) {
        this.coord.set_coord(x, y);
        this.has_moved = true;
    }

    public void move(Coord coord) {
        this.coord = coord;
    }

    public void pick_up_key(Element e) {
        this.keys.replace(e, this.keys.get(e) + 1);
    }

    public void loose_key(Element e) {
        this.keys.replace(e, Math.min(0, this.keys.get(e) - 1));
    }

    public void pick_up_artifact(Element e) {
        this.artifacts.replace(e, true);
        this.loose_key(e);
    }

    public void reset_actions() {
        this.has_moved = false;
        this.has_dried_out = false;
    }
}
