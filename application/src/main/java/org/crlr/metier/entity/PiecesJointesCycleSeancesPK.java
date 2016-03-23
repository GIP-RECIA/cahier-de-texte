/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: jalopy.xml,v 1.1 2010/06/15 12:20:58 ent_breyton Exp $
 */

package org.crlr.metier.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * DOCUMENTATION INCOMPLETE!
 *
 * @author $author$
 * @version $Revision: 1.1 $
 */
@Embeddable
public class PiecesJointesCycleSeancesPK implements Serializable {
    /** DOCUMENTATION INCOMPLETE! */
    private static final long serialVersionUID = 1L;
    @Column(name = "id_piece_jointe", unique = true, nullable = false)
    public Integer idPieceJointe;
    @Column(name = "id_cycle_seance", unique = true, nullable = false)
    public Integer idCycleSeance;

/**
     * Constructeur.
     */
    public PiecesJointesCycleSeancesPK() {
    }

/**
     * Constructeur.
     * @param idPieceJointe l'identifiant de la piece jointe.
     * @param idCycleSeance identifiant de la séance.
     */
    public PiecesJointesCycleSeancesPK(final Integer idPieceJointe, final Integer idCycleSeance) {
        this.idPieceJointe = idPieceJointe;
        this.idCycleSeance = idCycleSeance;
    }

    /**
     * Accesseur idPieceJointe.
     *
     * @return idPieceJointe
     */
    public Integer getIdPieceJointe() {
        return idPieceJointe;
    }

    /**
     * Mutateur idPieceJointe.
     *
     * @param idPieceJointe Le idPieceJointe à modifier
     */
    public void setIdPieceJointe(Integer idPieceJointe) {
        this.idPieceJointe = idPieceJointe;
    }

    /**
     * Accesseur idCycleSeance.
     *
     * @return idCycleSeance
     */
    public Integer getIdCycleSeance() {
        return idCycleSeance;
    }

    /**
     * Mutateur idCycleSeance.
     *
     * @param idCycleSeance Le idCycleSeance à modifier
     */
    public void setIdCycleSeance(Integer idCycleSeance) {
        this.idCycleSeance = idCycleSeance;
    }

    /**
     * 
    DOCUMENT ME!
     *
     * @return DOCUMENTATION INCOMPLETE!
     */
    public int hashCode() {
        int _hashCode = 0;
        if (this.idCycleSeance != null) {
            _hashCode += this.idCycleSeance.hashCode();
        }
        if (this.idPieceJointe != null) {
            _hashCode += this.idPieceJointe.hashCode();
        }
        return _hashCode;
    }

    /**
     * 
    DOCUMENT ME!
     *
     * @param obj DOCUMENTATION INCOMPLETE!
     *
     * @return DOCUMENTATION INCOMPLETE!
     */
    public boolean equals(final Object obj) {
        if ((obj == null) || !(obj instanceof PiecesJointesCycleSeancesPK)) {
            return false;
        }

        final PiecesJointesCycleSeancesPK pk = (PiecesJointesCycleSeancesPK) obj;
        boolean eq = true;

        if (this.idCycleSeance != null) {
            eq = eq && this.idCycleSeance.equals(pk.getIdCycleSeance());
        } else {
            eq = eq && (pk.getIdCycleSeance() == null);
        }
        if (this.idPieceJointe != null) {
            eq = eq && this.idPieceJointe.equals(pk.getIdPieceJointe());
        } else {
            eq = eq && (pk.getIdPieceJointe() == null);
        }

        return eq;
    }

    /**
     * DOCUMENT ME!
     *
     * @return String representation of this pk in the form of [.field1.field2].
     */
    public String toString() {
        final StringBuffer toStringValue = new StringBuffer("[.");
        toStringValue.append(this.idCycleSeance).append('.');
        toStringValue.append(this.idPieceJointe);
        toStringValue.append(']');
        return toStringValue.toString();
    }
}
