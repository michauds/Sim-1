package main;
import java.io.*;

/**********************************************************
 * U Q A M   -   I N F 1 1 2 0   e t   I N F 2 1 2 0
 *
 * Classe permettant l'entree simple au clavier (console).
 *
 * Louise Laforest (laforest.louise@uqam.ca)
 * Creation      : 2004/08/18
 * Modifications : 2004/10/07 - ajout de viderTampon()
 *               : 2005/08/25 - ajout des methodes ...Ln()
 **********************************************************/
 
/*
 * separateur : (utilise par isWhitespace)
 * ----------
 *   voir http://java.sun.com/j2se/1.4.2/docs/api/java/lang/Character.html#isWhitespace(char)
 * 
 *   Un caractere est un separateur en Java si et seulement si l'un des
 *   criteres suivants est rencontre :
 *
 *   - C'est un caractere Unicode espace (SPACE_SEPARATOR, LINE_SEPARATOR,
 *      ou PARAGRAPH_SEPARATOR) mais n'est pas aussi un caractere espace
 *      non-sécable ('\u00A0', '\u2007', '\u202F'). 
 *   - C'est '\u0009', HORIZONTAL TABULATION.
 *   - C'est '\u000A', LINE FEED.                 LF
 *   - C'est '\u000B', VERTICAL TABULATION.
 *   - C'est '\u000C', FORM FEED.
 *   - C'est '\u000D', CARRIAGE RETURN.           CR
 *   - C'est '\u001C', FILE SEPARATOR.
 *   - C'est '\u001D', GROUP SEPARATOR.
 *   - C'est '\u001E', RECORD SEPARATOR.
 *   - C'est '\u001F', UNIT SEPARATOR.
 * 
 *   voir aussi http://www.unicode.org
 *
 * fins de lignes
 * --------------
 *
 *   Les fins de lignes sont codees de facon differente selon la plateforme
 *
 *     UNIX, Linux et Mac OS X			LF			\n
 *     Windows                          CR LF		\r \n
 *     Mac OS Classique                 CR			\r
 *
 *  La presente classe gere les fins de lignes pour UNIX, Linux, Mac OS X et Windows
 */
 
 
public class Clavier {


    public static void viderTampon() {
       
    /* antecedent : -
     * consequent : Vide le tampon d'entree associe a la console.
     */
    
        try {
            System.in.skip( System.in.available() );
        } catch( IOException e ) {
            System.out.println ( "Le tampon d'entree ne peut etre vide." );
        }

    } // viderTampon
    



    public static String lireString() {

    /*  antecedent : 
     *  consequent : Retourne la chaine lue, a partir du curseur.
     *               La fin de la chaine est determinee par :
     *                  LF (line feed) ou par CR (carriage return)
     *                  suivi de LF.
     *               Ni LF ni CR ne seront presents dans la chaine mais
     *               auront ete lus.
     */
    
        return lireMot();
    } // lireString

    
    public static int lireInt() { 
    
    /* antecedent : -
     * consequent : retourne le nombre entier lu, a partir du
     *              curseur. La fin du nombre est determinee par un
     *              separateur (voir ci-haut). Le separateur est lu.
     *              Le nombre peut commencer par - (negatif) mais ne
     *              peut commencer par +.
     */
    
        return Integer.parseInt( lireMot() );
        
    } // lireInt
    
    private static String lireMot() {
    
    /* antecedent : -
     * consequent : Retourne le mot lu, a partir du curseur.
     *              Les separateurs (voir plus haut) de debut sont
     *              ignores. La fin du mot est determinee par un sepa-
     *              rateur. Le separateur qui suit le mot est lu.
     *              Le curseur est rendu sur le premier caractere suiv-
     *              ant le separateur ayant determine la fin du mot.
     */
        maReponse = "";
        while (maReponse.isEmpty())
        {
        
        }
        return maReponse;
        
    } // lireMot
    
    public static String maReponse; // modifie par lireMot
                
}  // Clavier
