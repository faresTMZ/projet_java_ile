package IG;

import javax.swing.JComponent;
import javax.swing.JPanel;
import java.awt.GridLayout;


public class Button extends JPanel {
    public Button(int hauteur) {
        setLayout(new GridLayout(11, 4, 5, 5));
    }

    public void ajouteElement(JComponent element) {
	    this.add(element);
    }
}
