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
public class PiecesJointesSeancesPK implements Serializable {
    /** DOCUMENTATION INCOMPLETE! */
    private static final long serialVersionUID = 1L;
    @Column(name = "id_piece_jointe", unique = true, nullable = false)
    public Integer idPieceJointe;
    @Column(name = "id_seance", unique = true, nullable = false)
    public Integer idSeance;

/**
     * Constructeur.
     */
    public PiecesJointesSeancesPK() {
    }

/**
     * Constructeur.
     * @param idPieceJointe l'identifiant de la piece jointe.
     * @param idSeance identifiant de la séance.
     */
    public PiecesJointesSeancesPK(final Integer idPieceJointe, final Integer idSeance) {
        this.idPieceJointe = idPieceJointe;
        this.idSeance = idSeance;
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
     * Accesseur idSeance.
     *
     * @return idSeance
     */
    public Integer getIdSeance() {
        return idSeance;
    }

    /**
     * Mutateur idSeance.
     *
     * @param idSeance Le idSeance à modifier
     */
    public void setIdSeance(Integer idSeance) {
        this.idSeance = idSeance;
    }

    /**
     * 
    DOCUMENT ME!
     *
     * @return DOCUMENTATION INCOMPLETE!
     */
    public int hashCode() {
        int _hashCode = 0;
        if (this.idSeance != null) {
            _hashCode += this.idSeance.hashCode();
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
        if ((obj == null) || !(obj instanceof PiecesJointesSeancesPK)) {
            return false;
        }

        final PiecesJointesSeancesPK pk = (PiecesJointesSeancesPK) obj;
        boolean eq = true;

        if (this.idSeance != null) {
            eq = eq && this.idSeance.equals(pk.getIdSeance());
        } else {
            eq = eq && (pk.getIdSeance() == null);
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
        toStringValue.append(this.idSeance).append('.');
        toStringValue.append(this.idPieceJointe);
        toStringValue.append(']');
        return toStringValue.toString();
    }
}
