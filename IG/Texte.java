package IG;

import javax.swing.JLabel;


public class Texte extends JLabel {
    public Texte(String texte) {
	    super(texte);
    }

    public Texte(String texte, boolean center) {
	    super(texte);
    }

    public void changeTexte(String texte) {
	    this.setText(texte);
	    this.repaint();
    }
}
