package src;

import java.awt.Dimension;
import java.awt.Color;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;


class Case extends JPanel implements MouseListener {
    private Plateau plateau;
    private Coord coord;
    private int water_level;
    private boolean pion;
    private Color color;

    public Case(Plateau plateau, boolean p, int x, int y, int water_level) {
        this.plateau = plateau;
        this.pion = p;
        this.coord = new Coord(x, y);
        this.water_level = water_level;
        this.color = new Color(0, 154, 23);
        this.setPreferredSize(new Dimension(40, 40));   
        addMouseListener(this);
    }

    public Plateau get_plateau() { return this.plateau; }

    public Coord coord() { return this.coord; }

    public int x() { return this.coord.x(); }

    public int y() { return this.coord.y(); }

    public int water_level() { return this.water_level; }

    public boolean is_normal() { return this.water_level == 0; }

    public boolean is_flooded() { return this.water_level == 1; }

    public boolean is_submerged() { return this.water_level == 2; }

    public boolean has_pion() { return this.pion; }

    public boolean has_artifact() {
        for (Coord c : this.get_plateau().get_artifacts())
            if (this.coord().equal(c))
                return true;
        return false;
    }

    public boolean is_in_bound() {
        boolean output = false;
        int i = this.x();
        int j = this.y();
        if (i == 2 || i == 3) {
            output = true;
        } else if ((i == 1 || i == 4) && 1 <= j && j <= 4) {
            output = true;
        } else if ((i == 0 || i == 5) && 2 <= j && j <= 3) {
            output = true;
        }
        return output;
    }

    public boolean is_adjacent(Case other) {
        if (!other.is_in_bound())
            return false;
        if (this.coord().equal(other.coord()))
            return true;
        int dx = Math.abs(this.x() - other.x());
        int dy = Math.abs(this.y() - other.y());
        return (dx + dy == 1);
    }

    public boolean is_adjacent(Coord c) {
        if (!c.is_in_bound())
            return false;
        if (this.coord().equal(c))
            return true;
        int dx = Math.abs(this.x() - c.x());
        int dy = Math.abs(this.y() - c.y());
        return (dx + dy == 1);
    }

    public void set_coord(int x, int y) { this.coord.set_coord(x, y); }

    public void set_coord(Coord coord) { this.coord = coord; }

    public void set_water_level(int water_level) { this.water_level = water_level; }

    public void add_pion() { this.pion = true; }

    public void remove_pion() { this.pion = false; }

    public void dry_out() {
        this.water_level = Math.max(0, this.water_level - 1);
    }

    public void flood() {
        this.water_level = Math.min(2, this.water_level + 1);
    }

    public void mouseClicked(MouseEvent e) {
      if (SwingUtilities.isRightMouseButton(e)) {
          this.clicDroit();
      } else {
          this.clicGauche();
      }
    }

    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void mousePressed(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}

    public void clicGauche() {
        Plateau plateau = this.get_plateau();
        Player active_player = plateau.get_player(plateau.get_turn());

        if (!active_player.has_moved) {
            plateau.move_player(active_player, this);
            this.repaint();
            plateau.repaint();
        }

        plateau.pick_up_artifact(active_player);
    }

    public void clicDroit() {
        Plateau plateau = this.get_plateau();
        Player active_player = plateau.get_player(plateau.get_turn());

        if (!active_player.has_dried_out) {
            plateau.dry_out(active_player, this);
            this.repaint();
            plateau.repaint();
        }
    }

    public void paintComponent(java.awt.Graphics g) {
        Plateau plateau = this.get_plateau();
        Player active_player = plateau.get_player(plateau.get_turn());
        Coord coord = this.coord();
        Case position = plateau.get_case(coord);

        if (this.is_in_bound()) {
            g.setColor(color); // green
            g.fillRect(0, 0, 100, 100);
        } else {
            g.setColor(new Color(0, 68, 129)); //dark blue
            g.fillRect(0, 0, 100, 100);
        }
        
        if (water_level == 1) {
            g.setColor(new Color(0, 138, 216)); // light blue
            g.fillRect(0, 0, 100, 100);
        } else if (water_level == 2) {
            g.setColor(new Color(0, 68, 129)); // dark blue
            g.fillRect(0, 0, 100, 100);
        }

        if (pion) {
            if (active_player.coord().equal(coord))
                g.setColor(Color.YELLOW);
            else
                g.setColor(Color.RED);
            g.fillOval(0, 0, 10, 10);
        }

        if (coord.equal(plateau.get_heliport())) {
            int [] x = {8, 13, 13, 27, 27, 32, 32, 27, 27, 13, 13, 8};
            int [] y = {8, 8, 18, 18, 8, 8, 32, 32, 23, 23, 32, 32};
            g.setColor(Color.BLACK);
            g.fillPolygon(x, y, 12);
        }

        if (position.has_artifact()) {
            Element elem = plateau.artifact_element(coord);
            int [] x = {20, 0, 20, 40};
            int [] y = {0, 20, 40, 20};

            if (elem == Element.WATER) {
                g.setColor(new Color(0, 190, 242));
            } else if (elem == Element.EARTH) {
                g.setColor(new Color(25, 170, 75));
            } else if (elem == Element.FIRE) {
                g.setColor(new Color(237, 27, 36));
            } else if (elem == Element.AIR) {
                g.setColor(new Color(254, 242, 0));
            }

            g.fillPolygon(x, y, 4);
        }
    }
}
