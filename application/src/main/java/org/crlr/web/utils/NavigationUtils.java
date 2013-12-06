/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: NavigationUtils.java,v 1.1 2009/03/17 16:30:44 ent_breyton Exp $
 */

package org.crlr.web.utils;

import org.crlr.dto.Outil;

import org.crlr.web.application.MenuNavigationItem;
import org.crlr.web.application.MenuNodeAction;
import org.crlr.web.contexte.utils.ContexteUtils;

/**
 * Classe utilitaire de navigation, au sein de l'arbre de navigation.
 *
 * @author breytond
 * @version $Revision: 1.1 $
 */
public final class NavigationUtils {
    /**
     * Le Constructeur.
     */
    private NavigationUtils() {
    }
    
    /**
     * Retourne la cible JSF permettant de retourner à l'écran précédemment
     * appelé, tout en supprimant la dernière navigation.
     *
     * @return outcome
     */
    public static String retourOutilPrecedentEnDepilant() {
        String outCome = null;       
        final org.crlr.web.contexte.ContexteUtilisateur contexteUtilisateur = ContexteUtils.getContexteUtilisateur();          

        if (contexteUtilisateur != null) {
            final MenuNavigationItem menuNavigationItem =
                contexteUtilisateur.deleteAndBeforeLastNavigationItem();
            if (menuNavigationItem != null) {
                outCome = menuNavigationItem.getOutcome();
            }
        }
        return outCome;
    }


    
    /**
     * Navigation vers l'outil spécifié.
     * @param identifiantOutilDestination l'identifiant de destination.
     * @return l'action composée de l'outcome et du suffixe @menu, spécifiant que l'outil
     * est autorisé, mais que la navigation n'est pas effectuée directement via l'arbre.
     */
    public static String navigationVersOutil(final String identifiantOutilDestination) {
        final MenuNodeAction nAction = new MenuNodeAction();
        nAction.setIdentifier(identifiantOutilDestination);
        return nAction.action();
    }
    
    /**
     * Permet de naviguer vers l'outil spécifié.
     *
     * @param outil outil vers lequel naviguer.
     *
     * @return identifiant de l'outil à retourner (outcome).
     */
    public static String navigationVersOutil(Outil outil) {         
        return navigationVersOutil(outil.name());
    }
    
    /**
     * Permet de naviguer vers l'outil spécifié et sauvegarde une valeur.
     * 
     * @param <T> le type de la valeur.
     * @param identifiantOutilDestination outil vers lequel naviguer.
     * @param clef la clef de l'objet à conserver.
     * @param valeur la valeur à conserver.
     * @return identifiant de l'outil à retourner (outcome).
     */
    public static <T> String navigationVersOutilAvecSauvegardeDonnees(final String identifiantOutilDestination, final String clef, final T valeur) {
        ContexteUtils.getContexteOutilControl().mettreAJourObjet(clef, valeur);
        return navigationVersOutil(identifiantOutilDestination);
    }

    /**
     * Permet de naviguer vers l'outil spécifié et sauvegarde une valeur.
     * 
     * @param <T> le type de la valeur.
     * @param outil outil vers lequel naviguer.
     * @param clef la clef de l'objet à conserver.
     * @param valeur la valeur à conserver.
     * @return identifiant de l'outil à retourner (outcome).
     */
    public static <T> String navigationVersOutilAvecSauvegardeDonnees(Outil outil, final String clef, final T valeur) {
        return navigationVersOutilAvecSauvegardeDonnees(outil.name(), clef, valeur);
    }

    /**
     * Navigation vers l'outil spécifié en détruisant la map de données présente dans le contexte outil.
     * @param identifiantOutilDestination l'identifiant de destination.
     * @return l'action composée de l'outcome, du suffixe @menu, et du @arbre, spécifiant que l'outil
     * est autorisé, et provoque une simulation d'une navigation effectuée directement via l'arbre.
     */
    public static String navigationVersOutilSimulationClique(final String identifiantOutilDestination) {
        final MenuNodeAction nAction = new MenuNodeAction();
        nAction.setIdentifier(identifiantOutilDestination);
        return nAction.actionArbre();
    }
    
    /**
    * Permet de naviguer vers l'outil spécifié en détruisant la map de données présente dans le contexte outil.
    *
    * @param outil outil vers lequel naviguer.
    *
    * @return identifiant de l'outil à retourner (outcome composé).
    */
   public static String navigationVersOutilSimulationClique(Outil outil) {         
       return navigationVersOutilSimulationClique(outil.name());
   }
   
   /**
    * Navigation vers le sous outil spécifié.
    * @param outil l'outil de destination.
    * @return l'action composée de l'outcome, du suffixe @sousEcran, spécifiant que l'outil
    * est un écran secondaire.
    */
   public static String navigationVersSousEcran(final Outil outil) {
      return navigationVersSousEcran(outil.name());     
   }
    
   /**
   * Navigation vers le sous outil spécifié.
   * @param identifiantOutilDestination l'identifiant de destination.
   * @return l'action composée de l'outcome, du suffixe @sousEcran, spécifiant que l'outil
   * est un écran secondaire.
   */
  public static String navigationVersSousEcran(final String identifiantOutilDestination) {
      final MenuNodeAction nAction = new MenuNodeAction();
      nAction.setIdentifier(identifiantOutilDestination);
      return nAction.actionSousEcran();     
  }
  
  /**
   * Permet de naviguer vers le sous outil spécifié et sauvegarde une valeur.
   * 
   * @param <T> le type de la valeur.
   * @param identifiantOutilDestination outil vers lequel naviguer.
   * @param clef la clef de l'objet à conserver.
   * @param valeur la valeur à conserver. 
   * @return identifiant de l'outil à retourner (outcome).
   */
  public static <T> String navigationVersSousEcranAvecSauvegardeDonnees(final String identifiantOutilDestination, final String clef, final T valeur) {
      ContexteUtils.getContexteOutilControl().mettreAJourObjet(clef, valeur);
      return navigationVersSousEcran(identifiantOutilDestination);
  }

  /**
   * Permet de naviguer vers le sous outil spécifié et sauvegarde une valeur.
   * 
   * @param <T> le type de la valeur.
   * @param outil l'outil de destination.
   * @param clef la clef de l'objet à conserver.
   * @param valeur la valeur à conserver.
   * @return identifiant de l'outil à retourner (outcome).
   */
  public static <T> String navigationVersSousEcranAvecSauvegardeDonnees(final Outil outil, final String clef, final T valeur) {
      return navigationVersSousEcranAvecSauvegardeDonnees(outil.name(), clef, valeur);
  }
}
