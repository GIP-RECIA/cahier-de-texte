/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: AbstractControl.java,v 1.2 2009/03/25 10:49:26 vibertd Exp $
 */

package org.crlr.web.application.control;

import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.crlr.log.Log;
import org.crlr.log.LogFactory;
import org.crlr.utils.DateUtils;
import org.crlr.web.application.form.AbstractForm;
import org.crlr.web.utils.FacesUtils;

/**
 * Contrôleur JSF de base. Tous les contrôleurs JSF de l'application
 * <strong>doivent</strong> hériter de cette classe.
 *
 * @author romana
 *
 * @param <F> le formulaire spécifique
 */
public abstract class AbstractControl<F extends AbstractForm> {
    /** Instance de {@link Log} accessible pour les sous-classes. */
    protected final Log log = LogFactory.getLog(getClass());

    /** Formulaire lié au contrôleur, qui étend AbstractForm. */
    protected F form ;

/**
     * Contructeur d'AbstractControl qui valorise l'attribut form du controleur. 
     * @param form le formulaire
     */
     public AbstractControl(F form) {
        this.setForm(form);
    }

    /**
     * Accesseur en lecture du formulaire associé au contrôleur.
     *
     * @return le formulaire
     */
    public final F getForm() {
        return this.form;
    }

    /**
     * Accesseur en écriture du formulaire associé au contrôleur.
     *
     * @param form le nouveau formulaire
     */
    public final void setForm(F form) {
        this.form = form;
    }






    /**
     * Récupération d'un paramètre en entrée de page.
     *
     * @param id du paramètre
     *
     * @return la valeur d'un paramètre
     */
    public final String getParametre(String id) {
        final String param = FacesUtils.getRequest().getParameter(id);
        String retour = StringUtils.trimToNull(param);
        if (retour != null) {
            retour = retour.toUpperCase();
        }
        return retour;
    }

   

    /**
     * Récupère la valeur sous forme de Date du paramètre indiqué.
     *
     * @param nomParametre L'identifiant du controle contenant la valeur
     *
     * @return la valeur sous forme de Date
     */
    protected final Date getParametreEnDate(String nomParametre) {
        return DateUtils.parse(getParametre(nomParametre));
    }

    /**
     * Retourne le numéro de la page à utiliser pour l'objet navigation.
     *
     * @return le numéro de la page à utiliser pour l'objet navigation.
     */
    protected final Integer getNavigationNumeroPage() {
        final String numeroPage = getParametre("numeroPage");
        try {
            return Integer.valueOf(numeroPage);
        } catch (NumberFormatException nfe) {
            return 1;
        }
    }

    /**
     * Méthode utilisé lorsque le composant supporter n'a pas de cible
     * spécifique. Fonctionne nativement.
     */
    public final void empty() {
    }
}
