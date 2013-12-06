/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: ChargeTravailDTO.java,v 1.8 2010/04/21 09:04:38 ent_breyton Exp $
 */

package org.crlr.dto.application.devoir;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.crlr.dto.application.base.TypeGroupe;

/**
 * Un DTO pour contenir la charge de travail (devois à rendre le même jours pour la même classe/groupe).
 *
 * @author $author$
 * @version $Revision: 1.8 $
 */
public class ChargeTravailDTO implements Serializable {
    

    /** Serial de la classe.      */
    private static final long serialVersionUID = -7190232035111793134L;

    /** Classe ou Groupe. */
    private TypeGroupe groupeOuClasse;
    
    /** Indicateur de surcharge de devoir de à rendre. */
    private Boolean affichageCharge;

    /** Liste des devoirs pour le meme jour affecte au meme groupe ou classe. */
    private List<DevoirEnteteDTO> listeDevoirPrincipal;

    /** Liste des devoirs pour le meme jour affecte au meme groupe ou classe. */
    private List<DevoirEnteteDTO> listeDevoirComplement;
    
    /**
     * Constructeur.
     */
    public ChargeTravailDTO() {
        groupeOuClasse = TypeGroupe.CLASSE;
        affichageCharge = Boolean.FALSE;
        listeDevoirPrincipal = new ArrayList<DevoirEnteteDTO>();
        listeDevoirComplement = new ArrayList<DevoirEnteteDTO>();
        
    }

    /**
     * Accesseur groupeOuClasse.
     * @return the groupeOuClasse
     */
    public TypeGroupe getGroupeOuClasse() {
        return groupeOuClasse;
    }

    /**
     * Mutateur groupeOuClasse.
     * @param groupeOuClasse the groupeOuClasse to set
     */
    public void setGroupeOuClasse(TypeGroupe groupeOuClasse) {
        this.groupeOuClasse = groupeOuClasse;
    }

    /**
     * Accesseur listeDevoirPrincipal.
     * @return the listeDevoirPrincipal
     */
    public List<DevoirEnteteDTO> getListeDevoirPrincipal() {
        return listeDevoirPrincipal;
    }

    /**
     * Mutateur listeDevoirPrincipal.
     * @param listeDevoirPrincipal the listeDevoirPrincipal to set
     */
    public void setListeDevoirPrincipal(List<DevoirEnteteDTO> listeDevoirPrincipal) {
        this.listeDevoirPrincipal = listeDevoirPrincipal;
    }

    /**
     * Accesseur listeDevoirComplement.
     * @return the listeDevoirComplement
     */
    public List<DevoirEnteteDTO> getListeDevoirComplement() {
        return listeDevoirComplement;
    }

    /** Mutateur listeDevoirComplement.
     * @param listeDevoirComplement the listeDevoirComplement to set
     */
    public void setListeDevoirComplement(List<DevoirEnteteDTO> listeDevoirComplement) {
        this.listeDevoirComplement = listeDevoirComplement;
    }

    /**
     * @return the affichageCharge
     */
    public Boolean getAffichageCharge() {
        return affichageCharge;
    }

    /**
     * @param affichageCharge the affichageCharge to set
     */
    public void setAffichageCharge(Boolean affichageCharge) {
        this.affichageCharge = affichageCharge;
    }


    
}
