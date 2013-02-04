/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import javax.swing.*;
import java.awt.*;


/**
 *
 * @author Administrateur
 */

public class desPanel extends javax.swing.JPanel {
    
    private String nomDePhoto;
    
    public desPanel(String nom){
    setFocusable(true);
    setFocusTraversalKeysEnabled(false);
    nomDePhoto = nom;
    }
    
    public void paintComponent( Graphics g )
    {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        ImageIcon imageIcon = new ImageIcon("des1.jpg");
        Image image = imageIcon.getImage();
        g2.drawImage(image, 100, 100, this);
        g2.drawImage(image, 90, 0, 300, 62, this);
        g2.setPaint(Color.BLUE);
        g2.dispose();
        g2.drawString("TEST", 24, 24);
    }
}
