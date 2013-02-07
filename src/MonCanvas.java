/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

/**
 *
 * @author naj
 */
import javax.swing.*;
import java.awt.*;

public class MonCanvas extends JComponent {

    private Image img = null;
    public String Nom;

    MonCanvas(String NomFichier) {
        setPreferredSize(new Dimension(120, 120));
        //img=Toolkit.getDefaultToolkit().createImage("des1.jpg");
        Nom = NomFichier;
        img = new ImageIcon(Nom).getImage();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(img, 0, 0, this);
    }

    public void setImage(String img) {
        this.img = new ImageIcon(img).getImage();//setImage(new ImageIcon(img).getImage());
    }

    public void setImage(Image img) {
        int width = this.getWidth();
        int height = (int) (((double) img.getHeight(null) / img.getWidth(null)) * width);
        this.img = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
    }
}
