/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: ContexteOutilControl.java,v 1.1 2009/03/17 16:30:44 ent_breyton Exp $
 */

package org.crlr.web.application.control;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;

import org.crlr.web.application.form.AbstractForm;
import org.crlr.web.contexte.ContexteOutil;

/**
 * OutilControl est un contrôleur commun à tous les outils du SI@D/RH. Il permet la
 * gestion d'un contexte d'outil : stockage/récupération/suppression de données. Ce
 * mécanisme (paliant à la portée request des contrôleurs d'outils) a pour but de
 * permettre aux utilisateurs de défiler entre plusieurs pages d'un outil, et de
 * conserver les données saisies pour chacun d'entre eux, sans pour autant persister en
 * base.
 *
 * @author burnichonj
 * @author breytond
 */
@ManagedBean(name="outil")
@RequestScoped
public class ContexteOutilControl extends AbstractControl<AbstractForm> {
    
    /** The Constant ID. */
    public static final String ID = "outil";

    /** The contexte outil. */
    @ManagedProperty(value="#{contexteOutil}")
    private ContexteOutil contexteOutil;

/**
 * The Constructor.
 */
    public ContexteOutilControl() {
        super(null);
        this.log.debug("Constructeur de OutilControl");
        this.reset();
    }

    /**
     * Reset.
     */
    private void reset() {
    }

    /**
     * Retourne contexte outil.
     *
     * @return the contexte outil
     */
    public ContexteOutil getContexteOutil() {
        return this.contexteOutil;
    }

    /**
     * Affecte contexte outil.
     *
     * @param contexteOutil the contexte outil
     */
    public void setContexteOutil(ContexteOutil contexteOutil) {
        this.contexteOutil = contexteOutil;
    }

    /**
     * Sauvegarde d'un objet dans le contexte outil. Si un objet associé à cette
     * clé est déjà placé dans le contexte, alors il sera ecrasé.
     *
     * @param cle la clé associée à cet objet
     * @param objet l'objet à stocker
     */
    public void mettreAJourObjet(String cle, Object objet) {
        this.log.debug("OutilControl - Demande de mise à jour d'un objet avec la clé \"{0}\"",
                       cle);
        this.contexteOutil.add(cle, objet);
    }

    /**
     * Restitution d'un objet préalablement stocké dans le contexte.
     *
     * @param cle the cle
     *
     * @return l'objet associé à la clé, ou null si cet objet n'existe pas dans le
     *         contexte
     */
    public Object recupererObjet(String cle) {
        this.log.debug("OutilControl - Demande de récupération de l'objet avec la clé \"{0}\"",
                       cle);
        return this.contexteOutil.get(cle);
    }

    /**
     * Tester la présence d'un objet sur la clé fournie dans le contexte d'outil.
     *
     * @param cle clé à tester
     *
     * @return true, if objet present
     */
    public boolean objetPresent(String cle) {
        return this.contexteOutil.contains(cle);
    }

    /**
     * Suppression d'un objet prélablement stocké dans le contexte.
     *
     * @param cle la clé associée à cet objet
     */
    public void supprimerObjet(String cle) {
        this.log.debug("OutilControl - Demande de suppression de l'objet avec la clé \"{0}\"",
                       cle);
        this.contexteOutil.remove(cle);
    }

    /**
     * Récupération d'un objet puis suppression du contexte d'outil.
     *
     * @param cle the cle
     *
     * @return l'objet associé à la clé
     */
    public Object recupererEtSupprimerObjet(String cle) {
        final Object obj = this.recupererObjet(cle);
        this.supprimerObjet(cle);
        return obj;
    }

    /**
     * Suppression de l'ensemble des données stockées dans le contexte sans tenir
     * compte de la portée des objets.
     */
    public void viderContexte() {
        this.log.debug("Vidage du contexte d'outil");
        this.contexteOutil.reset();
    }

   
}
