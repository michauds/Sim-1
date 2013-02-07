package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;

/**
 * La classe Tp2 simule un jeu de dï¿½s. L'utilisateur doit parier sur le
 * rï¿½sultat du lancer de trois dï¿½s effectuï¿½s par l'ordinateur. Ce montant
 * misï¿½ est dï¿½duit de ce qu'il a en main. L'ordinateur lance les dï¿½s.
 * L'utilisateur ï¿½ le choix de relancer chacun des dï¿½s une seule fois.
 * Ensuite, si le rï¿½sultat final du lancer des dï¿½s correspond ï¿½ ce que
 * l'utilisateur a pariï¿½, il empoche un certain nombre de crï¿½dits. La partie
 * termine lorsque le joueur n'a plus de crï¿½dit en main ou lorsqu'il manifeste
 * son dï¿½sir de mettre fin ï¿½ la partie.
 *
 */
public class MontrealSim1 {

    static JeuxDeDesGui monGui;
    // Dï¿½claration des variables
    //
    static int pari;                   // Le numï¿½ro du pari, peut ï¿½tre 1, 2 ou 3. Voir menu
    static int creditsMises;           // Le nombre de crï¿½dits que le joueur dï¿½sire miser
    static int creditsEnMain;    // Le joueur dï¿½bute la partie avec 100 crï¿½dits en main
    static final String CHEMIN = "save/credits.txt";
    static int de1;
    static int de2;
    static int de3;
    static boolean deRelance = false;
    static int deARelance = 1;

    /**
     * Afficher les choix des paris possibles (menu) et saisir le pari. Valider
     * le pari pour qu'il corresponde ï¿½ un choix valide (1, 2, 3 ou 4) et
     * retourner le pari.
     *
     * @param menu le menu qui montre les choix possible
     * @return pari le pari que l'utilisateur a entrï¿½
     */
    public static void lireLeChoixDeParis() {

        final int NO_PARI_MIN = 1;  // Numï¿½ros de pari valides sont de 1 ï¿½ 4, donc min 1
        final int NO_PARI_MAX = 4;  // Numï¿½ros de pari valides sont de 1 ï¿½ 4, donc max 4

        try {

            // Afficher le menu et demander ï¿½ l'utilisateur d'entrer son pari.
            //
            pari = Integer.parseInt(monGui.Reponse.getText());

            // Valider le pari pour qu'il corresponde ï¿½ un choix valide (1 ï¿½ 4)
            //
            if (pari >= NO_PARI_MIN && pari <= NO_PARI_MAX) {
                monGui.lecture = JeuxDeDesGui.ChoixDelecture.LireMise;
                monGui.TextArea.append(MessagesTp2.MESS_COMBIEN_MISE);
            } else {
                monGui.AfficherErreur(MessagesTp2.MESS_ERREUR_PARI);
            }
        } catch (Exception e) {
            monGui.AfficherErreur(MessagesTp2.MESS_ERREUR_PARI);
        }
    } // lireLeChoixDeParis

    /**
     * Demander ï¿½ l'utilisateur d'entrer sa mise (nombre de crï¿½dits).
     * Valider la mise pour qu'elle soit supï¿½rieur ï¿½ 0 et infï¿½rieur ou
     * ï¿½gale au nombre de crï¿½dits en main et retourner la mise.
     *
     * @param combienMise question pour connaï¿½tre la mise du joueur
     * @param max nombre de crï¿½dits maximum que le joueur peut miser
     * (crï¿½dits en main)
     * @return mise nombre de crï¿½dits misï¿½s par le joueur pour le prochain
     * pari
     */
    public static void lireLaMise() {
        // Mise que le joueur a entrï¿½e
        final int MISE_MIN = 1;  // Nombre minimum de crï¿½dit ï¿½ miser est 1

        // Demander ï¿½ l'utilisateur d'entrer sa mise.
        //

        try {
            creditsMises = Integer.parseInt(monGui.Reponse.getText());

            if (creditsMises >= MISE_MIN && creditsMises <= creditsEnMain) {
                creditsEnMain = creditsEnMain - creditsMises;
                monGui.lecture = JeuxDeDesGui.ChoixDelecture.LancerDes;
            } else {
                monGui.AfficherErreur(MessagesTp2.MESS_ERREUR_MISE);
            }
        } catch (Exception e) {
            monGui.AfficherErreur(MessagesTp2.MESS_ERREUR_MISE);
        }
    } // lireLaMise

    /**
     * Une question est posï¿½e au joueur et le joueur doit rï¿½pondre soit par
     * oui ou par non. La mï¿½thode valide la rï¿½ponse et retourne une valeur
     * boolï¿½enne pour indiquer si la rï¿½ponse est affirmative ou nï¿½gative.
     *
     * @param question question qui sera posï¿½e au joueur
     * @return repOui true si le joueur a rï¿½pondu par l'affirmative ï¿½ la
     * question
     */
    public static boolean reponseEstOui(String reponse) {
        if (reponse.equals("O") || reponse.equals("OUI")) {
            return true;
        } else {
            return false;
        }
    } // reponseEstOui

    public static void initCredits() {
        // Si un fichier de persistance existe, demander ï¿½ l'utilisateur 
        // s'il veut reprendre la partie
        //
        String reponse = monGui.Reponse.getText().toUpperCase();
        if (reponse.equals("O") || reponse.equals("OUI") || reponse.equals("N")
                || reponse.equals("NON")) {

            if (reponseEstOui(reponse)) {

                creditsEnMain = initCreditsDepuisFichier(CHEMIN);

                if (creditsEnMain < 0) {
                    creditsEnMain = 100;
                }

            } else {
                creditsEnMain = 100;
            }

            monGui.TextArea.setText(null);;
            monGui.TextArea.append("\nVous avez " + creditsEnMain + " Crédits.");
            monGui.lecture = JeuxDeDesGui.ChoixDelecture.ChoixDuLoad;
            // Dï¿½terminer d'abord si le joueur veut jouer
            //
            monGui.TextArea.append(MessagesTp2.MESS_VEUT_JOUER);


        } else {
            monGui.AfficherErreur(MessagesTp2.MESS_ERREUR_INVALID);
        }
    }

    /**
     * Mï¿½thode qui affiche le rï¿½sultat des 3 dï¿½s lancï¿½s.
     *
     * @param de1 rï¿½sultat du dï¿½1
     * @param de2 rï¿½sultat du dï¿½2
     * @param de3 rï¿½sultat du dï¿½3
     * @return
     */
    public static void afficherLesDes(int de1, int de2, int de3, int choix) {
        monGui.TextArea.setText(null);;
        final String MESS_VOICI_LES_DES = "\nVoici les trois dés : ";
     
        monGui.TextArea.append(MESS_VOICI_LES_DES + de1 + " + " + de2 + " + " + de3 + " = " + sommeDes(de1, de2, de3) + "\n ");

        monGui.AfficherLesImages("Image/des" + de1 + ".jpg", "Image/des" + de2 + ".jpg", "Image/des" + de3 + ".jpg");
    } // afficherLesDes

    /**
     * Cette mï¿½thode dï¿½termine s'il y a gain. Si c'est le cas, elle ï¿½value
     * ï¿½ combien de fois la mise le gain correspond.
     *
     * @param de1 rï¿½sultat du dï¿½ 1
     * @param de2 rï¿½sultat du dï¿½ 2
     * @param de3 rï¿½sultat du dï¿½ 3
     * @param pari le numï¿½ro du pari que le joueur a choisi
     * @return nbFoisMise le nombre de fois la mise selon le gain
     */
    public static int determineNbFoisMise(int de1, int de2, int de3, int pari) {
        int nbFoisMise = 0;             // Nombre de fois la mise
        final int GAIN_PARI_1 = 10;     // Gain en crï¿½dits pour pari 1
        final int GAIN_PARI_2 = 2;      // Gain en crï¿½dits pour pari 2
        final int GAIN_PARI_3 = 5;      // Gain en crï¿½dits pour pari 3
        final int GAIN_PARI_4 = 3;		// Gain en crï¿½dits pour pari 4
        final int NO_PARI_PAREILS = 1;  // Numï¿½ro du pari pour les dï¿½s pareils
        final int NO_PARI_DIFF = 2;     // Numï¿½ro du pari pour les dï¿½s diffï¿½rents

        if (pari == NO_PARI_PAREILS) {
            if (sontPareils(de1, de2, de3)) {
                nbFoisMise = GAIN_PARI_1;
            }
        } else if (pari == NO_PARI_DIFF) {
            if (sontDifferents(de1, de2, de3)) {
                nbFoisMise = GAIN_PARI_2;
            }
        } else if (sommeDes(de1, de2, de3) <= 7) {
            nbFoisMise = GAIN_PARI_4;
        } else if (sontUneSuite(de1, de2, de3)) {
            nbFoisMise = GAIN_PARI_3;
        }

        return nbFoisMise;

    } // determineNbFoisMise

    /**
     * Mï¿½thode qui dï¿½termine si les dï¿½s sont tous pareils. Si c'est le
     * cas, la mï¿½thode retourne 'true', sinon elle retourne 'false'.
     *
     * @param de1 rï¿½sultat du dï¿½ 1
     * @param de2 rï¿½sultat du dï¿½ 2
     * @param de3 rï¿½sultat du dï¿½ 3
     * @return true si les dï¿½s sont pareils, sinon false est retournï¿½
     */
    public static boolean sontPareils(int de1, int de2, int de3) {
        if (de1 == de2 && de1 == de3) {
            return true;
        } else {
            return false;
        }

    } // sontPareils

    /**
     * Mï¿½thode qui dï¿½termine si les dï¿½s sont tous diffï¿½rents. Si c'est
     * le cas, la mï¿½thode retourne 'true', sinon elle retourne 'false'.
     *
     * @param de1 rï¿½sultat du dï¿½ 1
     * @param de2 rï¿½sultat du dï¿½ 2
     * @param de3 rï¿½sultat du dï¿½ 3
     * @return true si les dï¿½s sont diffï¿½rents, sinon false est retournï¿½
     */
    public static boolean sontDifferents(int de1, int de2, int de3) {
        if (de1 != de2 && de1 != de3 && de2 != de3) {
            return true;
        } else {
            return false;
        }

    } // sontDifferents

    /**
     * Mï¿½thode retournant la somme de la valeur des 3 dï¿½s
     *
     * @param de1	valeur de dï¿½ 1
     * @param de2	valeur de dï¿½ 2
     * @param de3	valeur de dï¿½ 3
     * @return	la somme de la valeur des 3 dï¿½s
     */
    public static int sommeDes(int de1, int de2, int de3) {
        return de1 + de2 + de3;
    }

    /**
     * Mï¿½thode qui dï¿½termine si les dï¿½s correspondent ï¿½ une suite. Si
     * c'est le cas, la mï¿½thode retourne 'true', sinon elle retourne 'false'.
     *
     * @param de1 rï¿½sultat du dï¿½ 1
     * @param de2 rï¿½sultat du dï¿½ 2
     * @param de3 rï¿½sultat du dï¿½ 3
     * @return true si les dï¿½s correspondent ï¿½ une suite, sinon false est
     * retournï¿½
     */
    public static boolean sontUneSuite(int de1, int de2, int de3) {

        int premChiffre = 0;    // Le dï¿½ ayant le plus petit chiffre

        // Trouver le chiffre le plus petit des 3 dï¿½s
        //
        premChiffre = Math.min(de1, de2);
        premChiffre = Math.min(premChiffre, de3);

        // Vï¿½rifier si un dï¿½ correspond au chiffre suivant du plus petit trouvï¿½ prï¿½cï¿½demment
        //
        if (premChiffre + 1 == de1 || premChiffre + 1 == de2 || premChiffre + 1 == de3) {
            // Vï¿½rifier si un dï¿½ correspond au 2iï¿½me chiffre suivant le plus petit trouvï¿½ prï¿½cï¿½demment
            //
            if (premChiffre + 2 == de1 || premChiffre + 2 == de2 || premChiffre + 2 == de3) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }

    } // sontUneSuite

    /**
     * Mï¿½thode qui rï¿½ï¿½value le nombre de crï¿½dits en main selon le nombre
     * de crï¿½dits misï¿½s et le gain (nombre de fois la mise). Le nombre de
     * crï¿½dits en main rï¿½ï¿½valuï¿½ est retournï¿½.
     *
     * @param enMain le nombre de crï¿½dits dont dispose le joueur
     * @param crMises le nombre de crï¿½dits que le joueur a misï¿½
     * @param nbFoisLaMise gain reprï¿½sentï¿½ en nombre de fois la mise
     * @return le nombre de crï¿½dits en main rï¿½ï¿½valuï¿½
     */
    public static int calculerCreditsEnMain(int enMain, int crMises, int nbFoisLaMise) {
        int gainEnCredits;          // Gain calculï¿½ en nombre de crï¿½dits
        final String MESS_CREDITS = " crédits.";

        //  Si le nombre de fois la mise est ï¿½gale ï¿½ 0, ï¿½a indique que le joueur
        //  a perdu et un message en ce sens est affichï¿½.  Sinon, le nombre de
        //  crï¿½dits en main est rï¿½ï¿½valuï¿½.
        //
        if (nbFoisLaMise == 0) {
            afficherResultPari(MessagesTp2.MESS_PERDU, enMain);
        } else {
            gainEnCredits = nbFoisLaMise * crMises;
            enMain = enMain + gainEnCredits;
            afficherResultPari(MessagesTp2.MESS_GAGNE + gainEnCredits + MESS_CREDITS, enMain);
        }

        return enMain;

    } // calculerCreditsEnMain

    /**
     * Mï¿½thode qui affiche s'il s'agit d'un gain ou non et qui affiche le
     * nombre de crï¿½dits dont le joueur dispose.
     *
     * @param message message ï¿½ afficher
     * @param enMain nombre de crï¿½dits en main
     * @return
     */
    public static void afficherResultPari(String message, int enMain) {
        final String MESS_CREDITS_EN_MAIN = "\nVous disposez maintenant de ";
        final String MESS_CREDIT = " crédit. \n";
        final String MESS_CREDITS = " crédits. \n";

        monGui.TextArea.append(message);

        monGui.TextArea.append(MESS_CREDITS_EN_MAIN + enMain);
        if (enMain > 1) {
            monGui.TextArea.append(MESS_CREDITS);
        } else {
            monGui.TextArea.append(MESS_CREDIT);
        }

    } // afficherResultPari

    /**
     * Mï¿½thode principale du jeu. Les dï¿½s sont lancï¿½s, le rï¿½sultat des
     * dï¿½s est ï¿½valuï¿½ pour dï¿½terminer s'il y a gain. Si c'est le cas, le
     * gain est calculï¿½ en nombre de crï¿½dits et le nombre de crï¿½dits en
     * main est ajustï¿½.
     *
     * @param creditsEnMain crï¿½dits dont dispose le joueur
     * @param pari numï¿½ro du pari que le joueur a choisi
     * @param creditsMises le nombre de crï¿½dits misï¿½s par le joueur
     * @return credit en main le nombre de crï¿½dits dont le joueur dispose
     * aprï¿½s le jeu
     */
    public static void determinerResultPari(int creditsEnMain, int pari, int creditsMises) {

        boolean deRelance = false;          // Indique si un des dï¿½s a ï¿½tï¿½ relancï¿½


        // Lancer les 3 dï¿½s et afficher le rï¿½sultat
        //
        de1 = Aleatoire.lancerUnDe6();
        de2 = Aleatoire.lancerUnDe6();
        de3 = Aleatoire.lancerUnDe6();
        afficherLesDes(de1, de2, de3, pari);

        monGui.lecture = JeuxDeDesGui.ChoixDelecture.ReLancerDes;
        monGui.TextArea.append(MessagesTp2.MESS_RELANCER + deARelance + " ?");


    } // determinerResultPari

    public static void relancer() {
        final int CREDIT_PAR_LANCER = 3;   // Il en coï¿½te 3 crï¿½dits pour relancer un dï¿½

        String reponse = monGui.Reponse.getText().toUpperCase();
        if (reponse.equals("O") || reponse.equals("OUI") || reponse.equals("N")
                || reponse.equals("NON")) {

            if (deARelance == 1) {
                deARelance = 2;
                if (creditsEnMain >= CREDIT_PAR_LANCER
                        && reponseEstOui(reponse)) {
                    de1 = Aleatoire.lancerUnDe6();
                    creditsEnMain = creditsEnMain - CREDIT_PAR_LANCER;
                    deRelance = true;
                }
                monGui.TextArea.append(MessagesTp2.MESS_RELANCER + deARelance + " ?");
            } else if (deARelance == 2) {
                deARelance = 3;
                if (creditsEnMain >= CREDIT_PAR_LANCER
                        && reponseEstOui(reponse)) {
                    de2 = Aleatoire.lancerUnDe6();
                    creditsEnMain = creditsEnMain - CREDIT_PAR_LANCER;
                    deRelance = true;

                }
                monGui.TextArea.append(MessagesTp2.MESS_RELANCER + deARelance + " ?");
            } else if (deARelance == 3) {
                if (creditsEnMain >= CREDIT_PAR_LANCER
                        && reponseEstOui(reponse)) {
                    de3 = Aleatoire.lancerUnDe6();
                    creditsEnMain = creditsEnMain - CREDIT_PAR_LANCER;
                    deRelance = true;
                    monGui.TextArea.append("");
                }

                // Afficher le rï¿½sultat des dï¿½s si au moins un des dï¿½s a ï¿½tï¿½ relancï¿½
                //
                if (deRelance) {
                    afficherLesDes(de1, de2, de3, pari);

                }
                creditsEnMain = calculerCreditsEnMain(creditsEnMain, creditsMises,
                        determineNbFoisMise(de1, de2, de3, pari));

                monGui.lecture = JeuxDeDesGui.ChoixDelecture.ChoixDuLoad;
                monGui.TextArea.append(MessagesTp2.MESS_VEUT_JOUER);
            }

        } else {
            monGui.AfficherErreur(MessagesTp2.MESS_ERREUR_OUI_NON);
        }
    }

    /**
     * Mï¿½thode qui affiche le nombre de crï¿½dits en main ï¿½ la fin de la
     * partie.
     *
     * @param creditEnMain nombre de crï¿½dits dont le joueur dispose
     * @return
     */
    public static void afficherFinPartie(int creditEnMain) {
        final String MESS_FIN_PARTIE = "\nVous avez terminé la partie avec ";
        final String MESS_CREDIT = " crédit";
        final String MESS_CREDITS = " crédits";

        monGui.TextArea.append(MESS_FIN_PARTIE + creditEnMain);
        if (creditEnMain <= 1) {
            monGui.TextArea.append(MESS_CREDIT);
        } else {
            monGui.TextArea.append(MESS_CREDITS);
        }

    } // afficherFinPartie    

    /**
     * Mï¿½thode qui affiche le nom du jeu.
     *
     * @param
     * @return
     */
    public static void afficherNomJeu() {
        final String MESS_DEBUT_SOULIGN = "=====================================\n";
        monGui.TextArea.append(MESS_DEBUT_SOULIGN);
        monGui.lecture = JeuxDeDesGui.ChoixDelecture.InitSystem;
        monGui.TextArea.append(MessagesTp2.MESS_INITIALISER);
    } // afficherNomJeu    

    //TODO JavaDoc
    public static void determinerEtat() {
        String etat;

        etat = monGui.Reponse.getText().toUpperCase();

        if (etat.equals("P") || etat.equals("E") || etat.equals("Q")) {
            if (creditsEnMain > 0 && etat.equals("P")) {
                monGui.TextArea.setText(null);;
                monGui.lecture = JeuxDeDesGui.ChoixDelecture.ChoixDeParis;
                monGui.TextArea.append(MessagesTp2.MENU);

            } else {
                afficherFinPartie(creditsEnMain);

                // Si l'option de sauvegarde est choisie, le nombre de crï¿½dits que 
                // l'utilisateur dï¿½tient sera sauvegardï¿½
                //
                if (etat.equals("E")) {

                    enregistrerPartie(creditsEnMain, CHEMIN);

                } // if
                monGui.lecture = JeuxDeDesGui.ChoixDelecture.Fin;
            }
        } else {
            monGui.AfficherErreur(MessagesTp2.MESS_ERREUR_PEQ);
        }
        monGui.AfficherLesImages("des.jpg", "des.jpg", "des.jpg");
    }

    // TODO JavaDoc
    public static void enregistrerPartie(int credits, String fichier) {
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(fichier), Integer.SIZE);
            out.write(String.valueOf(credits));
            out.close();
        } catch (IOException e) {
            monGui.AfficherErreur(e.getMessage());
            monGui.TextArea.setText(null);;
        }
    }

    // TODO Javadoc
    public static int initCreditsDepuisFichier(String fichier) {
        int credits = -1;
        try {
            BufferedReader in = new BufferedReader(new FileReader(fichier), Integer.SIZE);
            File chemin = new File(fichier);
            credits = Integer.parseInt(in.readLine());
            in.close();
            chemin.delete();
        } catch (IOException e) {
            monGui.AfficherErreur(e.getMessage());
            monGui.TextArea.setText(null);;
        }

        return credits;
    }

    // TODO Javadoc
    public static boolean fichierExiste(String fichier) {
        File f = new File(fichier);
        return f.exists();
    }

    public static void jouer() {

        deRelance = false;
        deARelance = 1;
        // Jouer les dï¿½s et dï¿½terminer le rï¿½sultat du pari.  Rï¿½-ï¿½valuer les crï¿½dits
        // en main selon le rï¿½sultat du pari.
        //
        determinerResultPari(creditsEnMain, pari, creditsMises);

        // Vï¿½rifier si le joueur dï¿½sire continuer la partie
        //
        //lecture = ChoixDelecture.ChoixDuLoad;
        //TextArea.append(MessagesTp2.MESS_VEUT_JOUER);
    }

    public static void initialisation() {
        int resultat = 0;
        try {
            resultat = Integer.parseInt(monGui.Reponse.getText());
            Aleatoire.initialiserLesDes(resultat);
            if (fichierExiste(CHEMIN)) {
                monGui.TextArea.setText(null);;
                monGui.lecture = JeuxDeDesGui.ChoixDelecture.InitCredit;
                monGui.TextArea.append(MessagesTp2.MESS_LOAD);

            } else {
                creditsEnMain = 100;
                monGui.TextArea.setText(null);;
                monGui.lecture = JeuxDeDesGui.ChoixDelecture.ChoixDuLoad;
                monGui.TextArea.append(MessagesTp2.MESS_VEUT_JOUER);
            }
        } catch (Exception e) {
            monGui.AfficherErreur(MessagesTp2.MESS_ERREUR_INVALID);
        }
    }

    public static void main(String[] params) {
        monGui = new JeuxDeDesGui("JEU DU LANCER DES DES");
        afficherNomJeu();
    } // main
}
