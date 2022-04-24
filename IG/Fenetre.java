package IG;

import java.awt.Dimension;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.GridLayout;


public class Fenetre extends JFrame {
    private JPanel elements;

    public Fenetre(String nom) {
	    super(nom);
	    this.elements = new JPanel();
	    this.add(elements);
        this.setLayout(new GridLayout(1,2));
    }
    public Fenetre(String nom, int size) {
	    this(nom);
        this.setPreferredSize(new Dimension(400, 300));
    }

    public void ajouteElement(JComponent element) {
	    elements.add(element);
    }
    
    public void dessineFenetre() {
	    this.pack();
	    this.setVisible(true);
	    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
