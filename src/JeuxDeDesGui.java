/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.awt.Dimension;
import java.awt.event.KeyEvent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 *
 * @author naddou
 */
public class JeuxDeDesGui {

    public static enum ChoixDelecture {

        InitSystem,
        InitCredit,
        ChoixDuLoad,
        ChoixDeParis,
        LireMise,
        LancerDes,
        ReLancerDes,
        Fin
    };
    public JFrame jf;
    public JScrollPane jScrollPane1;
    public JTextArea TextArea;
    public JTextField Reponse;
    public JLabel jLabel1;
    public JPanel jp1;
    public JPanel jp2;
    public JPanel jp3;
    public static ChoixDelecture lecture;

    public void AfficherErreur(String erreur) {
        JOptionPane.showMessageDialog(jf, erreur, "Erreur", JOptionPane.ERROR_MESSAGE);
    }

    public void initComponents() {
        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(jf.getContentPane());
        jf.getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jLabel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(Reponse, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 68, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(jp1, 120, 120, 120)
                .add(jp2, 120, 120, 120)
                .add(jp3, 120, 120, 120)
                .add(438, 438, 438))
                .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 823, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(115, 115, 115)));
        layout.setVerticalGroup(
                layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 138, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(18, 18, 18)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                .add(jLabel1)
                .add(Reponse, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                .add(jp1, 120, 120, 120)
                .add(jp2, 120, 120, 120)
                .add(jp3, 120, 120, 120))
                .add(122, 122, 122)));


    }

    public void ReponseKeyPressed(java.awt.event.KeyEvent evt) {
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (lecture == ChoixDelecture.InitSystem) {
                MontrealSim1.initialisation();
            } else if (lecture == ChoixDelecture.InitCredit) {
                MontrealSim1.initCredits();
            } else if (lecture == ChoixDelecture.ChoixDuLoad) {
                MontrealSim1.determinerEtat();
            } else if (lecture == ChoixDelecture.ChoixDeParis) {
                MontrealSim1.lireLeChoixDeParis();
            } else if (lecture == ChoixDelecture.LireMise) {
                MontrealSim1.lireLaMise();
            }
            if (lecture == ChoixDelecture.LancerDes) {
                MontrealSim1.jouer();
            } else if (lecture == ChoixDelecture.ReLancerDes) {
                MontrealSim1.relancer();
            }
            Reponse.setText(null);
        }
    }

    public void AfficherLesImages(String image1, String image2, String image3) {
        jp1.removeAll();
        jp2.removeAll();
        jp3.removeAll();


        jp1.add(new MonCanvas(image1));
        jp1.setVisible(true);
        jp2.add(new MonCanvas(image2));
        jp2.setVisible(true);
        jp3.add(new MonCanvas(image3));
        jp3.setVisible(true);
        jf.pack();
    }

    JeuxDeDesGui(String nom) {
        jf = new JFrame();
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.setTitle(nom);
        jf.setPreferredSize(new Dimension(880, 350));
        jScrollPane1 = new javax.swing.JScrollPane();
        TextArea = new javax.swing.JTextArea();
        Reponse = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jp1 = new JPanel();
        jp2 = new JPanel();
        jp3 = new JPanel();
        TextArea.setEditable(false);

        TextArea.setColumns(30);
        TextArea.setRows(7);
        jScrollPane1.setViewportView(TextArea);

        Reponse.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                ReponseKeyPressed(evt);
            }
        });
        jLabel1.setText("Votre Choix :");

        initComponents();
        
        jf.setVisible(true);
        jf.pack();
    }
}
