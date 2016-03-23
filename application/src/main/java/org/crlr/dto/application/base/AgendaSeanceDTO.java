/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: SeanceDTO.java,v 1.2 2009/05/26 08:06:30 ent_breyton Exp $
 */

package org.crlr.dto.application.base;

import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.crlr.dto.application.emploi.DetailJourEmploiDTO;
import org.crlr.utils.DateUtils;
import org.apache.commons.lang.StringUtils;
import org.crlr.web.dto.TypeCouleur;


/**
 * Un DTO pour contenir une seance.
 *
 * @author $author$
 * @version $Revision: 1.2 $
 */
public class AgendaSeanceDTO implements Serializable {
    
    /** Serial. */
    private static final long serialVersionUID = -1900509129856609927L;

    /** Une seance. */
    private SeanceDTO seance;
    
    /** Une case de l'emploi du temps sans seance correspondante. */
    private DetailJourEmploiDTO detail;

    /** Date de la case. */
    private Date date;
    
     /** Couleur de la case au format String. */
    private String couleurCase;
    
    /** Couleur de la bordure. */
    private String couleurBordure;

    /** Couleur du texte. */
    private String couleurTexte;
    
    /** Description de la case prevue pour etre affiche dans le tool tip.*/
    private String description;
    
    /** Liste des sequence possible pour la case. */
    private List<SequenceDTO> listeSequence;
    
    /** Indique si la sélection de séquence doit être faite ou non. */
    private Boolean afficheSelectionSequence;

    /** Indique si la case est celle qui est selectionnee. */
    private Boolean selected;
    
    /** 
     * Constructeur par defaut.
     */
    public AgendaSeanceDTO() {
        super();
        selected=false;
        setTypeCouleur(TypeCouleur.Blanc);
    }
    
    /** 
     * Constructeur avec la seance.
     * @param seance la seance.
     */
    public AgendaSeanceDTO(final SeanceDTO seance) {
        super();
        this.date = seance.getDate();
        this.seance = seance;
        this.detail = null;
        this.listeSequence = new ArrayList<SequenceDTO>();
        this.afficheSelectionSequence = false;
        this.description = 
            seance.getHeureDebut().toString() + ":" +
            seance.getMinuteDebut().toString() + " - " +
            seance.getHeureFin().toString() + ":" +
            seance.getMinuteFin().toString() + "<br/>";
        if (seance.getDesignationClasse() != null) {
            this.description += seance.getDesignationClasse();
        } else if (seance.getDesignationGroupe() != null) {
            this.description += seance.getDesignationGroupe();
        }
        setTypeCouleur(TypeCouleur.Blanc);
    }
    
    /** 
     * Constructeur avec la case de EDT.
     * @param detail la case EDT.
     * @param lundi la date du lundi 
     */
    public AgendaSeanceDTO(final Date lundi, final DetailJourEmploiDTO detail) {
        super();
        this.seance = null;
        this.detail = detail;
        this.date = DateUtils.setJourSemaine(lundi, detail.getJour().getDayOfWeek());
        this.description = 
            detail.getHeureDebut().toString() + ":" +
            detail.getMinuteDebut().toString() + " - " +
            detail.getHeureFin().toString() + ":" +
            detail.getMinuteFin().toString() + "<br/>" +
            detail.getGroupeOuClasse().getIntitule();
        if (StringUtils.trimToNull(detail.getCodeSalle()) != null) {
            this.description += " / " + detail.getCodeSalle();
        }
        this.description += "<br/>";
        if (StringUtils.trimToNull(detail.getDescription()) != null) {
            this.description += detail.getDescription() + "<br/>";
        }
        if (detail.getMatiere() != null) {
            this.description += detail.getMatiere().getIntitule() + "<br/>";
        }
        setTypeCouleur(detail.getTypeCouleur());
    }
    
   
    
    /**
     * Complete la plage agenda sur laquelle une seance existe dejà et qui
     * correspond parfaitement avec une case de l'EDT.
     * @param detail le detail EDT.
     */
    public void completeWithDetail(final DetailJourEmploiDTO detail) {
        this.detail = detail;
        this.description = 
            detail.getHeureDebut().toString() + ":" +
            detail.getMinuteDebut().toString() + " - " +
            detail.getHeureFin().toString() + ":" +
            detail.getMinuteFin().toString() + "<br/>" +
            detail.getGroupeOuClasse().getIntitule();
        if (StringUtils.trimToNull(detail.getCodeSalle()) != null) {
            this.description += " / " + detail.getCodeSalle();
        }
        this.description += "<br/>";
        if (StringUtils.trimToNull(detail.getDescription()) != null) {
            this.description += detail.getDescription() + "<br/>";
        }
        if (detail.getMatiere() != null) {
            this.description += detail.getMatiere().getIntitule() + "<br/>";
        }
        setTypeCouleur(detail.getTypeCouleur());
    }
    

    /**
     * Accesseur de detail {@link #detail}.
     * @return retourne detail
     */
    public DetailJourEmploiDTO getDetail() {
        return detail;
    }

    /**
     * Mutateur de detail {@link #detail}.
     * @param detail le detail to set
     */
    public void setDetail(DetailJourEmploiDTO detail) {
        this.detail = detail;
    }

    /**
     * Accesseur de date {@link #date}.
     * @return retourne date
     */
    public Date getDate() {
        return date;
    }

    /**
     * Mutateur de date {@link #date}.
     * @param date le date to set
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * Accesseur de couleurCase {@link #couleurCase}.
     * @return retourne couleurCase
     */
    public String getCouleurCase() {
        return couleurCase;
    }

    /**
     * Mutateur de couleurCase {@link #couleurCase}.
     * @param couleurCase le couleurCase to set
     */
    public void setCouleurCase(String couleurCase) {
        this.couleurCase = couleurCase;
        
    }

    /**
     * Accesseur de description {@link #description}.
     * @return retourne description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Mutateur de description {@link #description}.
     * @param description le description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Accesseur de listeSequence {@link #listeSequence}.
     * @return retourne listeSequence
     */
    public List<SequenceDTO> getListeSequence() {
        return listeSequence;
    }

    /**
     * Mutateur de listeSequence {@link #listeSequence}.
     * @param listeSequence le listeSequence to set
     */
    public void setListeSequence(List<SequenceDTO> listeSequence) {
        this.listeSequence = listeSequence;
    }

    /**
     * Accesseur de afficheSelectionSequence {@link #afficheSelectionSequence}.
     * @return retourne afficheSelectionSequence
     */
    public Boolean getAfficheSelectionSequence() {
        return afficheSelectionSequence;
    }

    /**
     * Mutateur de afficheSelectionSequence {@link #afficheSelectionSequence}.
     * @param afficheSelectionSequence le afficheSelectionSequence to set
     */
    public void setAfficheSelectionSequence(Boolean afficheSelectionSequence) {
        this.afficheSelectionSequence = afficheSelectionSequence;
    }

    /**
     * Accesseur de couleurBordure {@link #couleurBordure}.
     * @return retourne couleurBordure
     */
    public String getCouleurBordure() {
        return couleurBordure;
    }
 
    /**
     * Accesseur de couleurTexte {@link #couleurTexte}.
     * @return retourne couleurTexte
     */
    public String getCouleurTexte() {
        return couleurTexte;
    }

    /**
     * Accesseur de selected {@link #selected}.
     * @return retourne selected
     */
    public Boolean getSelected() {
        return selected;
    }

    /**
     * Mutateur de selected {@link #selected}.
     * @param selected le selected to set
     */
    public void setSelected(Boolean selected) {
        this.selected = selected;
    }

    /**
     * Positione le champ couleurCase à partir d'un objet TypeCouleur.
     * @param typeCouleur la couleur.
     */
    private void setTypeCouleur(final TypeCouleur typeCouleur) {
        final Color couleurBordure;
        final Color couleurFond;
        final Color couleurTexte;
        
        // Cas particulier du blanc
        if (TypeCouleur.Blanc.equals(typeCouleur)) {
            if (seance == null) {
                couleurFond = typeCouleur.getColor();
                couleurBordure = couleurFond.darker();
            } else {
                couleurFond = TypeCouleur.Gris.getColor();
                couleurBordure = couleurFond.darker();
            }
            couleurTexte = new Color(0x000000);
        } else {
            if (seance == null) {
                couleurFond = TypeCouleur.Blanc.getColor();
                couleurBordure = typeCouleur.getColor();
                couleurTexte = couleurBordure.darker().darker(); 
            } else {
                couleurBordure = typeCouleur.getColor();
                if (typeCouleur.getVraiOuFauxCouleurClaire()) {
                    couleurFond = couleurBordure.darker();
                    couleurTexte = couleurBordure.brighter();
                } else {
                    couleurFond = couleurBordure.brighter();
                    couleurTexte = couleurBordure.darker();
                }
            }
        }
        this.couleurCase    = org.crlr.utils.StringUtils.colorToString(couleurFond);
        this.couleurBordure = org.crlr.utils.StringUtils.colorToString(couleurBordure);
        this.couleurTexte = org.crlr.utils.StringUtils.colorToString(couleurTexte);
    }



    /**
     * @return the seance
     */
    public SeanceDTO getSeance() {
        return seance;
    }

    /**
     * @param seance the seance to set
     */
    public void setSeance(SeanceDTO seance) {
        this.seance = seance;
    }
    
    
}
