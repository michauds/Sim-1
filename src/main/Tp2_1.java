package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * La classe Tp2 simule un jeu de d�s.  L'utilisateur doit parier sur le r�sultat
 * du lancer de trois d�s effectu�s par l'ordinateur.  Ce montant mis� est d�duit 
 * de ce qu'il a en main.  L'ordinateur lance les d�s.  L'utilisateur � le choix 
 * de relancer chacun des d�s une seule fois.  Ensuite, si le r�sultat final du 
 * lancer des d�s correspond � ce que l'utilisateur a pari�, il empoche un certain
 * nombre de cr�dits.  La partie termine lorsque le joueur n'a plus de cr�dit en
 * main ou lorsqu'il manifeste son d�sir de mettre fin � la partie.
 * 
 */
public class Tp2_1
{

    public static AffichageFrame unAffichage; 
    
    /**
     * Une question est affich�e � l'�cran et l'utilisateur doit y r�pondre par un
     * nombre entier.
     * 
     * @param  question   question qui sera affich�e � l'�cran
     * @return            le nombre entier lu
     */
    public static int questionRepInt (String question)
    {
        unAffichage.TextArea.append ( question );
       
        return Clavier.lireInt ();
        
    } // questionRepInt

    
    /**
     * Une question est affich�e � l'�cran et l'utilisateur doit y r�pondre par une
     * cha�ne de caract�res.
     * 
     * @param  question   question qui sera affich�e � l'�cran
     * @return            la chaine de caract�re lue    
     */
    public static String questionRepString (String question)
    {
        unAffichage.TextArea.append ( question );
       
        return Clavier.lireString ();
        
    } // questionRepString
    
    
    /**
     * Afficher les choix des paris possibles (menu) et saisir le pari.  Valider le pari
     * pour qu'il corresponde � un choix valide (1, 2, 3 ou 4) et retourner le pari.
     * 
     * @param  menu   le menu qui montre les choix possible
     * @return pari   le pari que l'utilisateur a entr� 
     */
    public static int lireLePari (String menu)
    {
        int pari;                   // Num�ro du pari entr� par l'utilisateur
        final int NO_PARI_MIN = 1;  // Num�ros de pari valides sont de 1 � 4, donc min 1
        final int NO_PARI_MAX = 4;  // Num�ros de pari valides sont de 1 � 4, donc max 4
        
        // Afficher le menu et demander � l'utilisateur d'entrer son pari.
        //
        pari = questionRepInt ( menu );
        
        // Valider le pari pour qu'il corresponde � un choix valide (1 � 4)
        //
        while ( pari < NO_PARI_MIN || pari > NO_PARI_MAX ) {
            unAffichage.AfficheErreur ( MessagesTp2.MESS_ERREUR_PARI );
            pari = questionRepInt ( menu );    
        } // while

        return pari;
        
    } // lireLePari

    
    /**
     * Demander � l'utilisateur d'entrer sa mise (nombre de cr�dits).  Valider la mise 
     * pour qu'elle soit sup�rieur � 0 et inf�rieur ou �gale au nombre de cr�dits en main
     * et retourner la mise.
     * 
     * @param  combienMise   question pour conna�tre la mise du joueur
     * @param  max           nombre de cr�dits maximum que le joueur peut miser (cr�dits en main)
     * @return mise          nombre de cr�dits mis�s par le joueur pour le prochain pari
     */
    public static int lireLaMise (String combienMise, int max)
    {
        int mise;                // Mise que le joueur a entr�e
        final int MISE_MIN = 1;  // Nombre minimum de cr�dit � miser est 1
        
        // Demander � l'utilisateur d'entrer sa mise.
        //
        mise = questionRepInt ( combienMise );
        
        // Valider la mise pour qu'elle soit sup�rieure � 0 et inf�rieure ou �gale 
        // au nombre maximum de cr�dits pouvant �tre mis�s
        //
        while ( mise < MISE_MIN || mise > max ) {
            unAffichage.AfficheErreur ( MessagesTp2.MESS_ERREUR_MISE );
            mise = questionRepInt ( combienMise );    
        } // while

        return mise;
        
    } // lireLaMise
    
    
    /**
     * Une question est pos�e au joueur et le joueur doit r�pondre soit par oui ou
     * par non.  La m�thode valide la r�ponse et retourne une valeur bool�enne pour 
     * indiquer si la r�ponse est affirmative ou n�gative.
     * 
     * @param  question     question qui sera pos�e au joueur 
     * @return repOui       true si le joueur a r�pondu par l'affirmative � la question
     */    
    public static boolean reponseEstOui (String question)
    {
        boolean repOui;
        String reponse;
           
        reponse = questionRepString ( question ).toUpperCase();
            
        while ( !(reponse.equals("O") || reponse.equals("OUI") || 
                  reponse.equals("N")  || reponse.equals("NON")) ) {
            unAffichage.AfficheErreur ( MessagesTp2.MESS_ERREUR_OUI_NON );
            reponse = questionRepString ( question ).toUpperCase();
        } // while
        
        if ( reponse.equals("O") || reponse.equals("OUI") ) {
            repOui = true;
        } else {
            repOui = false;
        }
		
        return repOui;
    } // reponseEstOui
    
 
    /**
     * M�thode qui affiche le r�sultat des 3 d�s lanc�s.
     *
     * @param  de1      r�sultat du d�1
     * @param  de2      r�sultat du d�2
     * @param  de3      r�sultat du d�3
     * @return 
     */    
    public static void afficherLesDes (int de1, int de2, int de3, int choix)
    {
        final String MESS_VOICI_LES_DES = "\nVoici les trois d�s : ";
        if(choix == 4)  {
        unAffichage.TextArea.append ( MESS_VOICI_LES_DES + de1 + " + " + de2 + " + " + de3 + " = " + sommeDes(de1,de2,de3) + "\n ");
        }else{
            unAffichage.TextArea.append ( MESS_VOICI_LES_DES + de1 + "  " + de2 + "  " + de3 + " \n ");
        }
    } // afficherLesDes
    

    /**
     * Cette m�thode d�termine s'il y a gain.  Si c'est le cas, elle �value
     * � combien de fois la mise le gain correspond.
     * 
     * @param  de1      r�sultat du d� 1
     * @param  de2      r�sultat du d� 2
     * @param  de3      r�sultat du d� 3
     * @param  pari     le num�ro du pari que le joueur a choisi
     * @return nbFoisMise   le nombre de fois la mise selon le gain
     */    
    public static int determineNbFoisMise (int de1, int de2, int de3, int pari)
    {
        int nbFoisMise = 0;             // Nombre de fois la mise
        final int GAIN_PARI_1 = 10;     // Gain en cr�dits pour pari 1
        final int GAIN_PARI_2 = 2;      // Gain en cr�dits pour pari 2
        final int GAIN_PARI_3 = 5;      // Gain en cr�dits pour pari 3
        final int GAIN_PARI_4 = 3;		// Gain en cr�dits pour pari 4
        final int NO_PARI_PAREILS = 1;  // Num�ro du pari pour les d�s pareils
        final int NO_PARI_DIFF = 2;     // Num�ro du pari pour les d�s diff�rents
        
        if ( pari == NO_PARI_PAREILS ) {
            if ( sontPareils ( de1, de2, de3 ) ) {
                nbFoisMise = GAIN_PARI_1;
            }
        } else if ( pari == NO_PARI_DIFF ) {
            if ( sontDifferents ( de1, de2, de3 ) ) {
                nbFoisMise = GAIN_PARI_2;
            }
        } else if ( sommeDes( de1, de2, de3) <= 7 ) {
        	nbFoisMise = GAIN_PARI_4;
        } else if ( sontUneSuite ( de1, de2, de3 ) ) {
            nbFoisMise = GAIN_PARI_3;
        }
        
        return nbFoisMise;
        
    } // determineNbFoisMise
    
    
    /**
     * M�thode qui d�termine si les d�s sont tous pareils.  Si c'est le cas,
     * la m�thode retourne 'true', sinon elle retourne 'false'.
     * 
     * @param  de1      r�sultat du d� 1
     * @param  de2      r�sultat du d� 2
     * @param  de3      r�sultat du d� 3
     * @return          true si les d�s sont pareils, sinon false est retourn� 
     */
    public static boolean sontPareils ( int de1, int de2, int de3 )
    {
        if ( de1 == de2 && de1 == de3 ) {
            return true;
        } else {
            return false;
        }
        
    } // sontPareils

    
    /**
     * M�thode qui d�termine si les d�s sont tous diff�rents.  Si c'est le cas,
     * la m�thode retourne 'true', sinon elle retourne 'false'.
     * 
     * @param  de1      r�sultat du d� 1
     * @param  de2      r�sultat du d� 2
     * @param  de3      r�sultat du d� 3
     * @return          true si les d�s sont diff�rents, sinon false est retourn� 
     */
    public static boolean sontDifferents ( int de1, int de2, int de3 )
    {
        if ( de1 != de2 && de1 != de3 && de2 != de3 ) {
            return true;
        } else {
            return false;
        }
        
    } // sontDifferents

    /**
     * M�thode retournant la somme de la valeur des 3 d�s
     * @param de1	valeur de d� 1
     * @param de2	valeur de d� 2
     * @param de3	valeur de d� 3
     * @return		la somme de la valeur des 3 d�s
     */
    public static int sommeDes ( int de1, int de2, int de3 ) 
    {
    	return de1 + de2 + de3;
    }
    
    /**
     * M�thode qui d�termine si les d�s correspondent � une suite.  Si c'est le cas,
     * la m�thode retourne 'true', sinon elle retourne 'false'.
     * 
     * @param  de1      r�sultat du d� 1
     * @param  de2      r�sultat du d� 2
     * @param  de3      r�sultat du d� 3
     * @return          true si les d�s correspondent � une suite, sinon false est retourn� 
     */
    public static boolean sontUneSuite ( int de1, int de2, int de3 )
    {
        
        int premChiffre = 0;    // Le d� ayant le plus petit chiffre
        
        // Trouver le chiffre le plus petit des 3 d�s
        //
        premChiffre = Math.min ( de1, de2 );
        premChiffre = Math.min ( premChiffre, de3 );
        
        // V�rifier si un d� correspond au chiffre suivant du plus petit trouv� pr�c�demment
        //
        if ( premChiffre + 1 == de1 || premChiffre + 1 == de2 || premChiffre + 1 == de3 ) {
            // V�rifier si un d� correspond au 2i�me chiffre suivant le plus petit trouv� pr�c�demment
            //
            if ( premChiffre + 2 == de1 || premChiffre + 2 == de2 || premChiffre + 2 == de3 ) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
        
    } // sontUneSuite

    
    /**
     * M�thode qui r��value le nombre de cr�dits en main selon le nombre de cr�dits
     * mis�s et le gain (nombre de fois la mise).  Le nombre de cr�dits en main 
     * r��valu� est retourn�.
     * 
     * @param  enMain        le nombre de cr�dits dont dispose le joueur
     * @param  crMises       le nombre de cr�dits que le joueur a mis�
     * @param  nbFoisLaMise  gain repr�sent� en nombre de fois la mise
     * @return               le nombre de cr�dits en main r��valu�
     */
    public static int calculerCreditsEnMain ( int enMain, int crMises, int nbFoisLaMise )
    {
        int gainEnCredits;          // Gain calcul� en nombre de cr�dits
        final String MESS_CREDITS = " cr�dits."; 

        //  Si le nombre de fois la mise est �gale � 0, �a indique que le joueur
        //  a perdu et un message en ce sens est affich�.  Sinon, le nombre de
        //  cr�dits en main est r��valu�.
        //
        if ( nbFoisLaMise == 0 ) {
            afficherResultPari ( MessagesTp2.MESS_PERDU, enMain );
        } else {
            gainEnCredits = nbFoisLaMise * crMises;
            enMain = enMain + gainEnCredits;
            afficherResultPari ( MessagesTp2.MESS_GAGNE + gainEnCredits + MESS_CREDITS, enMain );
        }        
        
        return enMain;
        
    } // calculerCreditsEnMain
        
    
    /**
     * M�thode qui affiche s'il s'agit d'un gain ou non et qui affiche le nombre
     * de cr�dits dont le joueur dispose.
     * 
     * @param  message      message � afficher
     * @param  enMain       nombre de cr�dits en main
     * @return     
     */
    public static void afficherResultPari ( String message, int enMain )
    {
        final String MESS_CREDITS_EN_MAIN = "\nVous disposez maintenant de ";
        final String MESS_CREDIT = " cr�dit.";   
        final String MESS_CREDITS = " cr�dits."; 
        
        unAffichage.TextArea.append ( message );
        
        unAffichage.TextArea.append ( MESS_CREDITS_EN_MAIN + enMain );
        if ( enMain > 1 ) {
            unAffichage.TextArea.append ( MESS_CREDITS );
        } else {
            unAffichage.TextArea.append ( MESS_CREDIT );
        }
        
    } // afficherResultPari
    
   
    /**
     * M�thode principale du jeu.  Les d�s sont lanc�s, le r�sultat des d�s est �valu�
     * pour d�terminer s'il y a gain.  Si c'est le cas, le gain est calcul� en nombre
     * de cr�dits et le nombre de cr�dits en main est ajust�.
     * 
     * @param  creditsEnMain    cr�dits dont dispose le joueur
     * @param  pari             num�ro du pari que le joueur a choisi
     * @param  creditsMises     le nombre de cr�dits mis�s par le joueur
     * @return credit en main   le nombre de cr�dits dont le joueur dispose apr�s le jeu
     */    
    public static int determinerResultPari (int creditsEnMain, int pari, int creditsMises)
    {
        int de1;
        int de2;
        int de3;
        boolean deRelance = false;          // Indique si un des d�s a �t� relanc�
        final int CREDIT_PAR_LANCER = 3;   // Il en co�te 3 cr�dits pour relancer un d�
                
        // Lancer les 3 d�s et afficher le r�sultat
        //
        de1 = Aleatoire.lancerUnDe6();
        de2 = Aleatoire.lancerUnDe6();
        de3 = Aleatoire.lancerUnDe6();
        afficherLesDes ( de1, de2, de3, pari );
        
        // Pour chaque d�, demander si le joueur d�sire relancer le d� une deuxi�me fois.
        // Il doit avoir au moins 3 cr�dits en main pour avoir la possibilit� de relancer un d�.
        //
        if ( creditsEnMain >= CREDIT_PAR_LANCER && 
             reponseEstOui ( MessagesTp2.MESS_RELANCER + "1 ? " ) ) {
            de1 = Aleatoire.lancerUnDe6();
            creditsEnMain = creditsEnMain - CREDIT_PAR_LANCER;
            deRelance = true;
        }
        if ( creditsEnMain >= CREDIT_PAR_LANCER && 
             reponseEstOui ( MessagesTp2.MESS_RELANCER + "2 ? " ) ) {
            de2 = Aleatoire.lancerUnDe6();
            creditsEnMain = creditsEnMain - CREDIT_PAR_LANCER;
            deRelance = true;
        }        
        if ( creditsEnMain >= CREDIT_PAR_LANCER && 
             reponseEstOui ( MessagesTp2.MESS_RELANCER + "3 ? " ) ) {
            de3 = Aleatoire.lancerUnDe6();
            creditsEnMain = creditsEnMain - CREDIT_PAR_LANCER;
            deRelance = true;
        }        
        // Afficher le r�sultat des d�s si au moins un des d�s a �t� relanc�
        //
        if ( deRelance ) {
            afficherLesDes ( de1, de2, de3 , pari);
        } else {
            unAffichage.TextArea.append ("");
        }
        
        // Selon le r�sultat du pari (determineNbFoisMise), calculer et retourner la nouvelle 
        // valeur des cr�dits en main
        //
        return calculerCreditsEnMain ( creditsEnMain, creditsMises, 
                                       determineNbFoisMise ( de1, de2, de3, pari ) );
        
    } // determinerResultPari
    
    
    /**
     * M�thode qui affiche le nombre de cr�dits en main � la fin de la partie.
     * 
     * @param  creditEnMain     nombre de cr�dits dont le joueur dispose
     * @return  
     */
    public static void afficherFinPartie ( int creditEnMain )
    {
        final String MESS_FIN_PARTIE = "\nVous avez termin� la partie avec ";
        final String MESS_CREDIT = " cr�dit";   
        final String MESS_CREDITS = " cr�dits"; 
        
        unAffichage.TextArea.append ( MESS_FIN_PARTIE + creditEnMain );
        if ( creditEnMain <= 1 ) {
            unAffichage.TextArea.append ( MESS_CREDIT );
        } else {
            unAffichage.TextArea.append ( MESS_CREDITS );
        }
        
    } // afficherFinPartie    
    
    
    /**
     * M�thode qui affiche le nom du jeu.
     * 
     * @param  
     * @return  
     */
    public static void afficherNomJeu ()
    {
        final String MESS_DEBUT_PARTIE = "\nJEU DU LANCER DES D�S\n";
        final String MESS_DEBUT_SOULIGN = "=====================\n";

        unAffichage.TextArea.append ( MESS_DEBUT_PARTIE );
        unAffichage.TextArea.append ( MESS_DEBUT_SOULIGN );
        
    } // afficherNomJeu    
    
    //TODO JavaDoc
    public static String determinerEtat ( String question ) 
    {
        String etat;
           
        etat = questionRepString ( question ).toUpperCase();
            
        while ( !( etat.equals("P") 
        		|| etat.equals("E") 
        		|| etat.equals("Q") ) ) {
        	
            unAffichage.AfficheErreur( MessagesTp2.MESS_ERREUR_PEQ );
            etat = questionRepString ( question ).toUpperCase();
        } // while
        		
        return etat;
    }
    
    
    // TODO JavaDoc
    public static void enregistrerPartie ( int credits, String fichier )
    {    	
    	try {
    		BufferedWriter out = new BufferedWriter(new FileWriter(fichier), Integer.SIZE);
    		out.write(String.valueOf(credits));
    		out.close();
    	}
    	catch ( IOException e ) {   		
    		unAffichage.AfficheErreur(e.getMessage());
    	} 
    }
    
    // TODO Javadoc
    public static int initCredits ( String fichier )
    {   
    	int credits = -1;
    	try {    		
    		BufferedReader in = new BufferedReader(new FileReader(fichier), Integer.SIZE);
    		File chemin = new File( fichier );
    		credits = Integer.parseInt(in.readLine());
			in.close();
			chemin.delete();
    	}
    	catch ( IOException e ) {
    		unAffichage.AfficheErreur(e.getMessage());
    	}
    	
    	return credits;
    }
    
    // TODO Javadoc
    public static boolean fichierExiste ( String fichier )
    {
    	File f = new File(fichier);
    	return f.exists();   	
    }
    
    
    public static void main ( String[] params ) {
        
        // D�claration des variables
        //
        int pari;                   // Le num�ro du pari, peut �tre 1, 2 ou 3. Voir menu
        int creditsMises;           // Le nombre de cr�dits que le joueur d�sire miser
        int creditsEnMain;    // Le joueur d�bute la partie avec 100 cr�dits en main
        String etatJeu;
        final String JOUER = "P";
        final String ENREGISTRER = "E";
        final String CHEMIN = "save/credits.txt";

        unAffichage = new AffichageFrame();
        unAffichage.show(true);
        unAffichage.TextArea.setEditable(false);
        
        // Afficher le nom du jeu
        //
        afficherNomJeu ();
        
        // Initialiser le processus al�atoire � l'aide d'un nombre saisi par l'utilisateur
        //
        Aleatoire.initialiserLesDes ( questionRepInt ( MessagesTp2.MESS_INITIALISER ) );
        
        // Si un fichier de persistance existe, demander � l'utilisateur 
        // s'il veut reprendre la partie
        //
        if ( fichierExiste( CHEMIN )
        		&& reponseEstOui( MessagesTp2.MESS_LOAD ) ) {
        	
        	creditsEnMain = initCredits(CHEMIN);
        	
        	if (creditsEnMain < 0) {
        	
        		creditsEnMain = 100;
        	
        	}
        	
        } else {
        	
        	creditsEnMain = 100;
        }
        
        // D�terminer d'abord si le joueur veut jouer
        //
        etatJeu = determinerEtat( MessagesTp2.MESS_VEUT_JOUER );
        
        // Boucle principale du jeu.
        // L'utilisateur peut jouer tant qu'il a suffisamment de cr�dit en main et qu'il
        // manifeste son d�sir de jouer
        //
        while ( creditsEnMain > 0 && etatJeu.equals(JOUER) ) {
            
            // Lire et valider le pari
            //
            pari = lireLePari ( "\n" + MessagesTp2.MENU );
            
            // Lire et valider la mise.  La mise doit �tre sup�rieur � 0 et inf�rieur
            // ou �gale au nombre de cr�dits en main.
            //
            creditsMises = lireLaMise ( "\n" + MessagesTp2.MESS_COMBIEN_MISE, creditsEnMain );
            
            // D�duire les cr�dits mis�s du nombre de cr�dits en main
            //
            creditsEnMain = creditsEnMain - creditsMises;
            
            // Jouer les d�s et d�terminer le r�sultat du pari.  R�-�valuer les cr�dits
            // en main selon le r�sultat du pari.
            //
            creditsEnMain = determinerResultPari ( creditsEnMain, pari, creditsMises );
            
            // V�rifier si le joueur d�sire continuer la partie
            //
            etatJeu = determinerEtat( MessagesTp2.MESS_VEUT_JOUER );
            
        } // while
        
        afficherFinPartie ( creditsEnMain );
        
        // Si l'option de sauvegarde est choisie, le nombre de cr�dits que 
        // l'utilisateur d�tient sera sauvegard�
        //
        if ( etatJeu.equals( ENREGISTRER ) ) {
        	
        	enregistrerPartie( creditsEnMain, CHEMIN );
        	
        } // if
        
    } // main
}
