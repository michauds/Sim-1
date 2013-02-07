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
 * La classe Tp2 simule un jeu de d�s. L'utilisateur doit parier sur le
 * r�sultat du lancer de trois d�s effectu�s par l'ordinateur. Ce montant
 * mis� est d�duit de ce qu'il a en main. L'ordinateur lance les d�s.
 * L'utilisateur � le choix de relancer chacun des d�s une seule fois.
 * Ensuite, si le r�sultat final du lancer des d�s correspond � ce que
 * l'utilisateur a pari�, il empoche un certain nombre de cr�dits. La partie
 * termine lorsque le joueur n'a plus de cr�dit en main ou lorsqu'il manifeste
 * son d�sir de mettre fin � la partie.
 *
 */
public class MontrealSim1 {

    static JeuxDeDesGui monGui;
    // D�claration des variables
    //
    static int pari;                   // Le num�ro du pari, peut �tre 1, 2 ou 3. Voir menu
    static int creditsMises;           // Le nombre de cr�dits que le joueur d�sire miser
    static int creditsEnMain;    // Le joueur d�bute la partie avec 100 cr�dits en main
    static final String CHEMIN = "save/credits.txt";
    static int de1;
    static int de2;
    static int de3;
    static boolean deRelance = false;
    static int deARelance = 1;

    /**
     * Afficher les choix des paris possibles (menu) et saisir le pari. Valider
     * le pari pour qu'il corresponde � un choix valide (1, 2, 3 ou 4) et
     * retourner le pari.
     *
     * @param menu le menu qui montre les choix possible
     * @return pari le pari que l'utilisateur a entr�
     */
    public static void lireLeChoixDeParis() {

        final int NO_PARI_MIN = 1;  // Num�ros de pari valides sont de 1 � 4, donc min 1
        final int NO_PARI_MAX = 4;  // Num�ros de pari valides sont de 1 � 4, donc max 4

        try {

            // Afficher le menu et demander � l'utilisateur d'entrer son pari.
            //
            pari = Integer.parseInt(monGui.Reponse.getText());

            // Valider le pari pour qu'il corresponde � un choix valide (1 � 4)
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
     * Demander � l'utilisateur d'entrer sa mise (nombre de cr�dits).
     * Valider la mise pour qu'elle soit sup�rieur � 0 et inf�rieur ou
     * �gale au nombre de cr�dits en main et retourner la mise.
     *
     * @param combienMise question pour conna�tre la mise du joueur
     * @param max nombre de cr�dits maximum que le joueur peut miser
     * (cr�dits en main)
     * @return mise nombre de cr�dits mis�s par le joueur pour le prochain
     * pari
     */
    public static void lireLaMise() {
        // Mise que le joueur a entr�e
        final int MISE_MIN = 1;  // Nombre minimum de cr�dit � miser est 1

        // Demander � l'utilisateur d'entrer sa mise.
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
     * Une question est pos�e au joueur et le joueur doit r�pondre soit par
     * oui ou par non. La m�thode valide la r�ponse et retourne une valeur
     * bool�enne pour indiquer si la r�ponse est affirmative ou n�gative.
     *
     * @param question question qui sera pos�e au joueur
     * @return repOui true si le joueur a r�pondu par l'affirmative � la
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
        // Si un fichier de persistance existe, demander � l'utilisateur 
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
            monGui.TextArea.append("\nVous avez " + creditsEnMain + " Cr�dits.");
            monGui.lecture = JeuxDeDesGui.ChoixDelecture.ChoixDuLoad;
            // D�terminer d'abord si le joueur veut jouer
            //
            monGui.TextArea.append(MessagesTp2.MESS_VEUT_JOUER);


        } else {
            monGui.AfficherErreur(MessagesTp2.MESS_ERREUR_INVALID);
        }
    }

    /**
     * M�thode qui affiche le r�sultat des 3 d�s lanc�s.
     *
     * @param de1 r�sultat du d�1
     * @param de2 r�sultat du d�2
     * @param de3 r�sultat du d�3
     * @return
     */
    public static void afficherLesDes(int de1, int de2, int de3, int choix) {
        monGui.TextArea.setText(null);;
        final String MESS_VOICI_LES_DES = "\nVoici les trois d�s : ";
     
        monGui.TextArea.append(MESS_VOICI_LES_DES + de1 + " + " + de2 + " + " + de3 + " = " + sommeDes(de1, de2, de3) + "\n ");

        monGui.AfficherLesImages("Image/des" + de1 + ".jpg", "Image/des" + de2 + ".jpg", "Image/des" + de3 + ".jpg");
    } // afficherLesDes

    /**
     * Cette m�thode d�termine s'il y a gain. Si c'est le cas, elle �value
     * � combien de fois la mise le gain correspond.
     *
     * @param de1 r�sultat du d� 1
     * @param de2 r�sultat du d� 2
     * @param de3 r�sultat du d� 3
     * @param pari le num�ro du pari que le joueur a choisi
     * @return nbFoisMise le nombre de fois la mise selon le gain
     */
    public static int determineNbFoisMise(int de1, int de2, int de3, int pari) {
        int nbFoisMise = 0;             // Nombre de fois la mise
        final int GAIN_PARI_1 = 10;     // Gain en cr�dits pour pari 1
        final int GAIN_PARI_2 = 2;      // Gain en cr�dits pour pari 2
        final int GAIN_PARI_3 = 5;      // Gain en cr�dits pour pari 3
        final int GAIN_PARI_4 = 3;		// Gain en cr�dits pour pari 4
        final int NO_PARI_PAREILS = 1;  // Num�ro du pari pour les d�s pareils
        final int NO_PARI_DIFF = 2;     // Num�ro du pari pour les d�s diff�rents

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
     * M�thode qui d�termine si les d�s sont tous pareils. Si c'est le
     * cas, la m�thode retourne 'true', sinon elle retourne 'false'.
     *
     * @param de1 r�sultat du d� 1
     * @param de2 r�sultat du d� 2
     * @param de3 r�sultat du d� 3
     * @return true si les d�s sont pareils, sinon false est retourn�
     */
    public static boolean sontPareils(int de1, int de2, int de3) {
        if (de1 == de2 && de1 == de3) {
            return true;
        } else {
            return false;
        }

    } // sontPareils

    /**
     * M�thode qui d�termine si les d�s sont tous diff�rents. Si c'est
     * le cas, la m�thode retourne 'true', sinon elle retourne 'false'.
     *
     * @param de1 r�sultat du d� 1
     * @param de2 r�sultat du d� 2
     * @param de3 r�sultat du d� 3
     * @return true si les d�s sont diff�rents, sinon false est retourn�
     */
    public static boolean sontDifferents(int de1, int de2, int de3) {
        if (de1 != de2 && de1 != de3 && de2 != de3) {
            return true;
        } else {
            return false;
        }

    } // sontDifferents

    /**
     * M�thode retournant la somme de la valeur des 3 d�s
     *
     * @param de1	valeur de d� 1
     * @param de2	valeur de d� 2
     * @param de3	valeur de d� 3
     * @return	la somme de la valeur des 3 d�s
     */
    public static int sommeDes(int de1, int de2, int de3) {
        return de1 + de2 + de3;
    }

    /**
     * M�thode qui d�termine si les d�s correspondent � une suite. Si
     * c'est le cas, la m�thode retourne 'true', sinon elle retourne 'false'.
     *
     * @param de1 r�sultat du d� 1
     * @param de2 r�sultat du d� 2
     * @param de3 r�sultat du d� 3
     * @return true si les d�s correspondent � une suite, sinon false est
     * retourn�
     */
    public static boolean sontUneSuite(int de1, int de2, int de3) {

        int premChiffre = 0;    // Le d� ayant le plus petit chiffre

        // Trouver le chiffre le plus petit des 3 d�s
        //
        premChiffre = Math.min(de1, de2);
        premChiffre = Math.min(premChiffre, de3);

        // V�rifier si un d� correspond au chiffre suivant du plus petit trouv� pr�c�demment
        //
        if (premChiffre + 1 == de1 || premChiffre + 1 == de2 || premChiffre + 1 == de3) {
            // V�rifier si un d� correspond au 2i�me chiffre suivant le plus petit trouv� pr�c�demment
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
     * M�thode qui r��value le nombre de cr�dits en main selon le nombre
     * de cr�dits mis�s et le gain (nombre de fois la mise). Le nombre de
     * cr�dits en main r��valu� est retourn�.
     *
     * @param enMain le nombre de cr�dits dont dispose le joueur
     * @param crMises le nombre de cr�dits que le joueur a mis�
     * @param nbFoisLaMise gain repr�sent� en nombre de fois la mise
     * @return le nombre de cr�dits en main r��valu�
     */
    public static int calculerCreditsEnMain(int enMain, int crMises, int nbFoisLaMise) {
        int gainEnCredits;          // Gain calcul� en nombre de cr�dits
        final String MESS_CREDITS = " cr�dits.";

        //  Si le nombre de fois la mise est �gale � 0, �a indique que le joueur
        //  a perdu et un message en ce sens est affich�.  Sinon, le nombre de
        //  cr�dits en main est r��valu�.
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
     * M�thode qui affiche s'il s'agit d'un gain ou non et qui affiche le
     * nombre de cr�dits dont le joueur dispose.
     *
     * @param message message � afficher
     * @param enMain nombre de cr�dits en main
     * @return
     */
    public static void afficherResultPari(String message, int enMain) {
        final String MESS_CREDITS_EN_MAIN = "\nVous disposez maintenant de ";
        final String MESS_CREDIT = " cr�dit. \n";
        final String MESS_CREDITS = " cr�dits. \n";

        monGui.TextArea.append(message);

        monGui.TextArea.append(MESS_CREDITS_EN_MAIN + enMain);
        if (enMain > 1) {
            monGui.TextArea.append(MESS_CREDITS);
        } else {
            monGui.TextArea.append(MESS_CREDIT);
        }

    } // afficherResultPari

    /**
     * M�thode principale du jeu. Les d�s sont lanc�s, le r�sultat des
     * d�s est �valu� pour d�terminer s'il y a gain. Si c'est le cas, le
     * gain est calcul� en nombre de cr�dits et le nombre de cr�dits en
     * main est ajust�.
     *
     * @param creditsEnMain cr�dits dont dispose le joueur
     * @param pari num�ro du pari que le joueur a choisi
     * @param creditsMises le nombre de cr�dits mis�s par le joueur
     * @return credit en main le nombre de cr�dits dont le joueur dispose
     * apr�s le jeu
     */
    public static void determinerResultPari(int creditsEnMain, int pari, int creditsMises) {

        boolean deRelance = false;          // Indique si un des d�s a �t� relanc�


        // Lancer les 3 d�s et afficher le r�sultat
        //
        de1 = Aleatoire.lancerUnDe6();
        de2 = Aleatoire.lancerUnDe6();
        de3 = Aleatoire.lancerUnDe6();
        afficherLesDes(de1, de2, de3, pari);

        monGui.lecture = JeuxDeDesGui.ChoixDelecture.ReLancerDes;
        monGui.TextArea.append(MessagesTp2.MESS_RELANCER + deARelance + " ?");


    } // determinerResultPari

    public static void relancer() {
        final int CREDIT_PAR_LANCER = 3;   // Il en co�te 3 cr�dits pour relancer un d�

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

                // Afficher le r�sultat des d�s si au moins un des d�s a �t� relanc�
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
     * M�thode qui affiche le nombre de cr�dits en main � la fin de la
     * partie.
     *
     * @param creditEnMain nombre de cr�dits dont le joueur dispose
     * @return
     */
    public static void afficherFinPartie(int creditEnMain) {
        final String MESS_FIN_PARTIE = "\nVous avez termin� la partie avec ";
        final String MESS_CREDIT = " cr�dit";
        final String MESS_CREDITS = " cr�dits";

        monGui.TextArea.append(MESS_FIN_PARTIE + creditEnMain);
        if (creditEnMain <= 1) {
            monGui.TextArea.append(MESS_CREDIT);
        } else {
            monGui.TextArea.append(MESS_CREDITS);
        }

    } // afficherFinPartie    

    /**
     * M�thode qui affiche le nom du jeu.
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

                // Si l'option de sauvegarde est choisie, le nombre de cr�dits que 
                // l'utilisateur d�tient sera sauvegard�
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
        // Jouer les d�s et d�terminer le r�sultat du pari.  R�-�valuer les cr�dits
        // en main selon le r�sultat du pari.
        //
        determinerResultPari(creditsEnMain, pari, creditsMises);

        // V�rifier si le joueur d�sire continuer la partie
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
