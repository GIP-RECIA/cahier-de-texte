/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: jalopy.xml,v 1.1 2010/06/15 12:20:58 ent_breyton Exp $
 */

package org.crlr.importEDT.DTO;

/**
 * Une classe qui permet d'utiliser les heure au sein de l'import EDT.
 * ATTENTION : ne gère pas les changement de jours.
 *
 * @author $author$
 * @version $Revision: 1.1 $
  */
public class HeureDTO {
    /**
     * L'heure.
     */
    private Integer heure;

    /**
     * Les minutes.
     */
    private Integer minute;

    /**
     * Constructeur.
     * @param heure hh
     * @param minute mm.
     */
    public HeureDTO(String heure, String minute) {
        this.heure = Integer.parseInt(heure);
        this.minute = Integer.parseInt(minute);
    }

    /**
     * Constructeur.
     * @param heureEtMinute hhmm
     */
    public HeureDTO(String heureEtMinute) {
        this.heure = Integer.parseInt(heureEtMinute.substring(0, 2));
        this.minute = Integer.parseInt(heureEtMinute.substring(2, 4));
    }

    /**
     * Constructeur.
     * @param heureDTO le DTO à dupliquer.
     */
    public HeureDTO(HeureDTO heureDTO) {
        this.heure = heureDTO.heure;
        this.minute = heureDTO.minute;
    }

    /**
     * Ajoute des minutes à l'heure courante.
     * @param ajoutMinute le nombre de minutes à ajouter.
     */
    public void add(Integer ajoutMinute) {
        this.minute += ajoutMinute;
        while (this.minute >= 60) {
            this.minute -= 60;
            this.heure++;
        }
    }

    /**
     * Soustrait des minutes à l'heure courante.
     * @param subMinute le nombre de minutes à soustraire. 
     */
    public void sub(Integer subMinute) {
        while (subMinute > this.minute) {
            this.minute += 60;
            this.heure--;
        }
        this.minute -= subMinute;
    }

    /**
     * Ajoute une heure à l'heure courante.
     * @param ajout l'heure à ajouter.
     */
    public void add(HeureDTO ajout) {
        this.minute += ajout.getMinute();
        if (this.minute >= 60) {
            this.minute -= 60;
            this.heure++;
        }
        this.heure += ajout.getHeure();
        if (this.heure > 23) {
            this.heure -= 24;
        }
    }

    /**
     * Soustrait une heure à l'heure courante.
     * @param sub L'heure à soustraire.
     */
    public void sub(HeureDTO sub) {
        Integer heureSub = sub.getHeure();
        if (sub.getMinute() > this.minute) {
            this.minute += 60;
            heureSub++;
        }
        this.minute -= sub.minute;
        this.heure -= heureSub;
    }

    /**
     * Accesseur de heure.
     *
     * @return le heure
     */
    public Integer getHeure() {
        return heure;
    }

    /**
     * Mutateur de heure.
     *
     * @param heure le heure à modifier.
     */
    public void setHeure(Integer heure) {
        this.heure = heure;
    }

    /**
     * Accesseur de minute.
     *
     * @return le minute
     */
    public Integer getMinute() {
        return minute;
    }

    /**
     * Mutateur de minute.
     *
     * @param minute le minute à modifier.
     */
    public void setMinute(Integer minute) {
        this.minute = minute;
    }

    /**
     * test <=.
     * @param heure l'heure à comparer.
     * @return True sss this <= heure
     */
    public boolean isBeforeOrEquals(HeureDTO heure) {
        if (this.getHeure() < heure.getHeure()) {
            return true;
        } else if (this.getHeure() == heure.getHeure()) {
            return this.getMinute() <= heure.getMinute();
        } else {
            return false;
        }
    }

    /**
     * test >.
     *
     * @param heure l'heure à tester.
     *
     * @return True sss this > heure.
     */
    public boolean isAfter(HeureDTO heure) {
        if (this.getHeure() > heure.getHeure()) {
            return true;
        } else if (this.getHeure() == heure.getHeure()) {
            return this.getMinute() > heure.getMinute();
        } else {
            return false;
        }
    }

    /**
     * Test si l'heure est comprise au sens large entre les deux autres heures.
     *
     * @param heureDebut l'heure de début.
     * @param heureFin l'heure de fin.
     *
     * @return True sss heureDebut <= this <= heureFin
     */
    public boolean isBetweenOrEquals(HeureDTO heureDebut, HeureDTO heureFin) {
        return heureDebut.isBeforeOrEquals(this) && this.isBeforeOrEquals(heureFin);
    }

    /**
     * Méthode hashCode.
     *
     * @return un int.
     */
    public int hashCode() {
        return heure.hashCode() + minute.hashCode();
    }

    /** 
     * Méthode equals.
     *
     * @param other l'élement à tester.
     *
     * @return true si les heures et minutes coincident.
     */
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (other instanceof HeureDTO) {
            final HeureDTO heureDTO = (HeureDTO) other;
            return this.heure.equals(heureDTO.getHeure()) &&
                   this.minute.equals(heureDTO.getMinute());
        }
        return false;
    }

    /**
     * Methode d'affichage.
     *
     * @return l'heure telle que présentée dans EDT (hhmm).
     */
    public String toString() {
        String res = "";
        if (heure < 10) {
            res += ("0" + heure);
        } else {
            res += heure;
        }
        if (minute < 10) {
            res += ("0" + minute);
        } else {
            res += minute;
        }
        return res;
    }
}
