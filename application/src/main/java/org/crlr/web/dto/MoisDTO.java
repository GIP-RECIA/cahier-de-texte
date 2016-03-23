/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: BarreSemaineDTO.java,v 1.2 2010/04/12 04:30:13 jerome.carriere Exp $
 */

package org.crlr.web.dto;

import java.awt.Color;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.crlr.utils.DateUtils;

/**
 * BarreMoisDTO.
 *
 * @author breytond.
 * @version $Revision: 1.2 $
 */
public class MoisDTO implements Serializable {    

    /** Serial. */
    private static final long serialVersionUID = 8676380773095666086L;

    /** Libelle Mois .*/
    private String libelle;
    
    /** Code du mois. */ 
    private Integer code;
    
    /** Date de debut du mois. */
    private Date debut;
    
    /** Date de fin. */
    private Date fin;

    /** Rapport du nombre de semaine. */
    private Float nbrSemaine; 
    
    /** Couleur du mois. */
    private Color color;
    
    /** Indique si on est sur le mois courant. */
    private Boolean moisCourant;
    
    private Float positionLeft;
    
    private Float positionRight;
    
    
    /**
     * Constructeur special pour creer le premier faux mois correspondant au decalage de debut d'annee.
     * @param decalageDebutAnnee : rapport de decalage a appliquer.
     */
    public MoisDTO(final Float decalageDebutAnnee) {
        this.code = null;
        this.color = TypeCouleurJour.MOIS.getColor();
        this.debut = null;
        this.fin = null;
        this.libelle = " ";
        this.nbrSemaine = decalageDebutAnnee;
        this.moisCourant = false;
    }
    
    /**
     * Constructeur. 
     * @param debut : date de debut du mois
     */
    public MoisDTO(Date debut) {
        
        // Date du jour
        final Date toDay = DateUtils.getAujourdhui();
        final Integer moisToDay = DateUtils.getChamp(toDay, Calendar.YEAR)*100 + (DateUtils.getChamp(toDay, Calendar.MONTH) + 1);
        
        this.debut = debut;
        
        // Fin du mois
        Date date;
        final Calendar cal = new GregorianCalendar();
        cal.setTime(debut);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        date = cal.getTime();
        date = DateUtils.ajouter(date, Calendar.MONTH, 1);
        
        
        //final Date date = DateUtils.ajouter(debut, Calendar.MONTH, 1);
        this.fin = DateUtils.getDateMoins1(date);
        this.libelle = DateUtils.format(debut, "MMMMM");
        this.code = DateUtils.getChamp(debut, Calendar.MONTH) + 1;
        
        // Verifie s'il s'agit du mois courant
        final Integer codemois = DateUtils.getChamp(debut, Calendar.YEAR)*100 + this.code;
        if (codemois.equals(moisToDay)) {
            this.color = TypeCouleurJour.SILVERTODAY.getColor();
            this.moisCourant = true;
        } else {
            this.color = TypeCouleurJour.MOIS.getColor();
            this.moisCourant = false;
        }
        
        // Calcule le rapport / semaine
        setNbrSemaine(calculerNbrSemaine());
    }

    /** Détermine le nombre de jour du mois. 
     * @return retourne le nombre de jour du mois
     * */
    public Integer getNbrJour() {
        return DateUtils.calculerNombreJours(debut, fin);
    }
    
    /** Determine le nombre de semaines du mois. 
     * @return retourne le nombre (real) de semaines pour le mois
     * */
    private Float calculerNbrSemaine() {
        
        final Integer nbrJour = DateUtils.calculerNombreJours(this.debut, this.fin) + 1; 
        Float rapport = (nbrJour / 7.0f); 
        return rapport;
        
        // Premiere semaine
        // Le lundi correspondant à la premiere semaine
        /*!!final Date lundi = DateUtils.setJourSemaine(debut,2); 
        
        // Difference entre ce lundi et le 1er jour du mois
        Integer nbrJourEnMoins = DateUtils.calculerNombreJours(lundi, debut);
        Float rapport = (7.0f -  nbrJourEnMoins) / 7.0f;
        
        // Ajout les semaines entières
        Date dimanche = DateUtils.setJourSemaine(debut,1);
        dimanche = DateUtils.ajouter(dimanche, Calendar.DAY_OF_MONTH, 7);
        //while (numMois.equals(DateUtils.getChamp(dimanche, Calendar.MONTH))) {
        while (DateUtils.lessStrict(dimanche, fin)) {
            rapport += 1.0f;
            dimanche = DateUtils.ajouter(dimanche, Calendar.DAY_OF_MONTH, 7);
        }
        
        // On traite la derniere semaine : le dimanche n'est plus dans le mois
        nbrJourEnMoins = DateUtils.calculerNombreJours(fin, dimanche);
        rapport += (7.0f -  nbrJourEnMoins) / 7.0f;
        
        // Retourne le nombre Real de semaine pour le mois
        return rapport;*/
    }
    
    /**
     * Accesseur de nbrSemaine {@link #nbrSemaine}.
     * @return retourne nbrSemaine
     */
    public Float getNbrSemaine() {
        return nbrSemaine;
    }

    /**
     * Mutateur de nbrSemaine {@link #nbrSemaine}.
     * @param nbrSemaine le nbrSemaine to set
     */
    public void setNbrSemaine(Float nbrSemaine) {
        this.nbrSemaine = nbrSemaine;
    }

    /**
     * Calcule le declage en proportion de semaine qu'il faut appliquer sur le premier mois.
     * @param premierJourAnneeScolaire : date de la rentree
     * @return retourne le rapport de decalage
     */
    public static Float getDecalageDebutAnnee(final Date premierJourAnneeScolaire) {
        // Le lundi correspondant à la premiere semaine de la rentree
        final Date lundi = DateUtils.setJourSemaine(premierJourAnneeScolaire,2); 
        
        // Le premier jour de la rentree est dans le mois suivant correspondant au lundi de la semaine
        // L M M J V S D 
        // 08 | 09        (rentree = Mer 09)
        // => Difference entre ce lundi et le 1er jour de la rentrée
        if (DateUtils.getChamp(lundi, Calendar.MONTH) < DateUtils.getChamp(premierJourAnneeScolaire, Calendar.MONTH)) {
            final Integer nbrJourDecalage = DateUtils.calculerNombreJours(lundi, premierJourAnneeScolaire);
            final Float rapport = (nbrJourDecalage) / 7.0f;
            return rapport;
            
        // Le premier jour de la rentree correspond au lundi de la semaine de la rentree sur une fin de mois 
        // L M M J V S D
        // 08 | 09         (rentree = Lun 08)
        // On ne calcule pas de decalage. On commence directement sur un bon mois.
        } else {
            return 0.0f;
        }
    }
    
    /**
     * Accesseur de libelle {@link #libelle}.
     * @return retourne libelle
     */
    public String getLibelle() {
        return libelle;
    }

    /**
     * Mutateur de libelle {@link #libelle}.
     * @param libelle le libelle to set
     */
    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    /**
     * Accesseur de code {@link #code}.
     * @return retourne code
     */
    public Integer getCode() {
        return code;
    }

    /**
     * Mutateur de code {@link #code}.
     * @param code le code to set
     */
    public void setCode(Integer code) {
        this.code = code;
    }

    /**
     * Accesseur de debut {@link #debut}.
     * @return retourne debut
     */
    public Date getDebut() {
        return debut;
    }

    /**
     * Mutateur de debut {@link #debut}.
     * @param debut le debut to set
     */
    public void setDebut(Date debut) {
        this.debut = debut;
    }

    /**
     * Accesseur de fin {@link #fin}.
     * @return retourne fin
     */
    public Date getFin() {
        return fin;
    }

    /**
     * Mutateur de fin {@link #fin}.
     * @param fin le fin to set
     */
    public void setFin(Date fin) {
        this.fin = fin;
        setNbrSemaine(calculerNbrSemaine()); 
    }

    /**
     * Accesseur de color {@link #color}.
     * @return retourne color
     */
    public Color getColor() {
        return color;
    }

    /**
     * Mutateur de color {@link #color}.
     * @param color le color to set
     */
    public void setColor(Color color) {
        this.color = color;
    } 
    
 
    /**
     * Accesseur couleur au format String.
     * @return le couleur
     */
    public String getCouleur() {
        final String colorStr = org.crlr.utils.StringUtils.colorToString(color);
        return colorStr;
    }

    /** Retourne la couleur du texte.
     * @return la couleur du texte (en fonction de la couleur du fond)
     */
    public String getCouleurTexte() {
        if (this.color.equals(TypeCouleurJour.SILVERTODAY.getColor())) {
            return "#ffffff";
        } else {
            return "#000000"; 
        }
    }
    
    /**
     * Couleur de la bordure.
     * @return la couleur de la bordure de la case
     */
    public String getCouleurBordureTop() {
        if (color==null) { return ""; }
        return org.crlr.utils.StringUtils.colorToString(color.brighter());
    }

    /**
     * Couleur de la bordure.
     * @return la couleur de la bordure de la case
     */
    public String getCouleurBordureBottom() {
        if (color==null) { return ""; }
        return org.crlr.utils.StringUtils.colorToString(color.darker());
    }

    /**
     * Accesseur de moisCourant {@link #moisCourant}.
     * @return retourne moisCourant
     */
    public Boolean getMoisCourant() {
        return moisCourant;
    }

    /**
     * Mutateur de moisCourant {@link #moisCourant}.
     * @param moisCourant le moisCourant to set
     */
    public void setMoisCourant(Boolean moisCourant) {
        this.moisCourant = moisCourant;
    }

    /**
     * Accesseur de positionLeft {@link #positionLeft}.
     * @return retourne positionLeft
     */
    public Float getPositionLeft() {
        return positionLeft;
    }

    /**
     * Mutateur de positionLeft {@link #positionLeft}.
     * @param positionLeft le positionLeft to set
     */
    public void setPositionLeft(Float positionLeft) {
        this.positionLeft = positionLeft;
    }

    /**
     * Accesseur de positionRight {@link #positionRight}.
     * @return retourne positionRight
     */
    public Float getPositionRight() {
        return positionRight;
    }

    /**
     * Mutateur de positionRight {@link #positionRight}.
     * @param positionRight le positionRight to set
     */
    public void setPositionRight(Float positionRight) {
        this.positionRight = positionRight;
    }
    
    
}