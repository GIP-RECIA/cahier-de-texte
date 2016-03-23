/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: PiecesJointesDevoirsPK.java,v 1.1 2009/04/03 07:04:38 vibertd Exp $
 */

package org.crlr.metier.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * DOCUMENTATION INCOMPLETE!
 * 
 * @author $author$
 * @version $Revision: 1.1 $
 */
@Embeddable
public class ArchivePiecesJointesDevoirsPK implements Serializable {
    /** DOCUMENTATION INCOMPLETE! */
    private static final long serialVersionUID = 1L;
    @Column(name = "id_piece_jointe", unique = true, nullable = false)
    public Integer idPieceJointe;
    @Column(name = "id_archive_devoir", unique = true, nullable = false)
    public Integer idArchiveDevoir;

    /**
     * Constructeur.
     */
    public ArchivePiecesJointesDevoirsPK() {
    }

    /**
     * Constructeur.
     * 
     * @param idPieceJointe l'identifiant de la piece jointe.
     * @param idDevoir identifiant du devoir.
     */
    public ArchivePiecesJointesDevoirsPK(final Integer idPieceJointe,
            final Integer idDevoir) {
        this.idPieceJointe = idPieceJointe;
        this.idArchiveDevoir = idDevoir;
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
     * 
     * 
     * @return DOCUMENTATION INCOMPLETE!
     */
    public int hashCode() {
        return new HashCodeBuilder().append(idArchiveDevoir)
                .append(idPieceJointe).hashCode();

    }

    /**
     * 
     * 
     * @param obj DOCUMENTATION INCOMPLETE!
     * 
     * @return DOCUMENTATION INCOMPLETE!
     */
    public boolean equals(final Object obj) {
        if (!(obj instanceof ArchivePiecesJointesDevoirsPK)) {
            return false;
        }

        final ArchivePiecesJointesDevoirsPK rhs = (ArchivePiecesJointesDevoirsPK) obj;

        return new EqualsBuilder()
                .append(getIdArchiveDevoir(), rhs.getIdArchiveDevoir())
                .append(getIdPieceJointe(), rhs.getIdPieceJointe()).isEquals();

    }

    /**
     * 
     DOCUMENT ME!
     * 
     * @return String representation of this pk in the form of [.field1.field2].
     */
    public String toString() {
        final StringBuffer toStringValue = new StringBuffer("[.");
        toStringValue.append(this.idArchiveDevoir).append('.');
        toStringValue.append(this.idPieceJointe);
        toStringValue.append(']');
        return toStringValue.toString();
    }

    /**
     * @return the idArchiveDevoir
     */
    public Integer getIdArchiveDevoir() {
        return idArchiveDevoir;
    }

    /**
     * @param idArchiveDevoir the idArchiveDevoir to set
     */
    public void setIdArchiveDevoir(Integer idArchiveDevoir) {
        this.idArchiveDevoir = idArchiveDevoir;
    }
}
