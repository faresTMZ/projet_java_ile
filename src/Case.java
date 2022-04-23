package src;

import java.awt.Dimension;
import java.awt.Color;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;


class Case extends JPanel implements MouseListener {
    private int water_level;
    private Coord coord;
    private boolean pion;
    private Color color;

    public Case(boolean p, int x, int y, int water_level) {
        this.pion = p;
        this.coord = new Coord(x, y);
        this.water_level = water_level;
        this.color = new Color(0, 154, 23);
        this.setPreferredSize(new Dimension(40, 40));   
        addMouseListener(this);
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

    public boolean is_normal() {
        return this.water_level == 0;
    }

    public boolean is_flooded() {
        return this.water_level == 1;
    }

    public boolean is_submerged() {
        return this.water_level == 2;
    }

    public boolean has_pion() {
        return this.pion;
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
        if (this.coord().equal(other.coord()))
            return true;
        int dx = Math.abs(this.x() - other.x());
        int dy = Math.abs(this.y() - other.y());
        return (dx + dy == 1);
    }

    public boolean is_adjacent(Coord c) {
        if (this.coord().equal(c))
            return true;
        int dx = Math.abs(this.x() - c.x());
        int dy = Math.abs(this.y() - c.y());
        return (dx + dy == 1);
    }

    public Coord coord() {
        return this.coord;
    }

    public int x() {
        return this.coord.x();
    }

    public int y() {
        return this.coord.y();
    }

    public int water_level() {
        return this.water_level;
    }

    public void set_coord(int x, int y) {
        this.coord.set_coord(x, y);
    }

    public void set_coord(Coord coord) {
        this.coord = coord;
    }

    public void set_water_level(int water_level) {
        this.water_level = water_level;
    }

    public void dry_out() {
        this.water_level = Math.max(0, this.water_level - 1);
    }

    public void flood() {
        this.water_level = Math.min(2, this.water_level + 1);
    }

    public void clicGauche() {
        if (this.pion) {
            this.color = Color.YELLOW;
            repaint();
        }
    }

    public void clicDroit() {}

    public void paintComponent(java.awt.Graphics g) {
        if (this.is_in_bound()) {
            g.setColor(color); // green
            g.fillRect(0, 0, 99, 99);
        } else {
            g.setColor(new Color(0, 68, 129)); //dark blue
            g.fillRect(0, 0, 99, 99);
        }
        
        if (water_level == 1) {
            g.setColor(new Color(0, 138, 216)); // light blue
            g.fillRect(0, 0, 99, 99);
        } else if (water_level == 2) {
            g.setColor(new Color(0, 68, 129)); // dark blue
            g.fillRect(0, 0, 99, 99);
        }

        //if (this.color)

        if (pion) {
            g.setColor(Color.RED);
            g.fillOval(0, 0, 10, 10);
        }
    }
}
