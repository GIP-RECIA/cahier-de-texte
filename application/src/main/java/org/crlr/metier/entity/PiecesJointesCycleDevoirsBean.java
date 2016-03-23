/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: PiecesJointesDevoirsBean.java,v 1.2 2009/04/28 12:45:50 ent_breyton Exp $
 */

package org.crlr.metier.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Proxy;

/**
 * DOCUMENTATION INCOMPLETE!
 *
 * @author $author$
 * @version $Revision: 1.2 $
  */
@Proxy(lazy = false)
@Entity
@Table(name = "cahier_piece_jointe_cycle_devoir", schema="cahier_courant")
public class PiecesJointesCycleDevoirsBean implements Serializable {
    
    /**
     * 
     */
    private static final long serialVersionUID = 1039662851323471542L;

    /** Identifiant. */
    @Id
    private PiecesJointesCycleDevoirsPK pk;

    /** Objet PieceJointe. */
    @ManyToOne(cascade =  { } )
    @JoinColumns(
            {
                @JoinColumn(name = "id_piece_jointe", referencedColumnName = "id", insertable = false, updatable = false)
            }
    )
    private PieceJointeBean pieceJointe;
    
    /** Objet CycleDevoir. */
    @ManyToOne(cascade =  { } )
    @JoinColumns(
            {
                @JoinColumn(name = "id_cycle_devoir", referencedColumnName = "id", insertable = false, updatable = false)
            }
    )
    private CycleDevoirBean cycleDevoir;
    
    /**
     * 
     * Constructeur.
     */
    public PiecesJointesCycleDevoirsBean() {
    }

    /**
     * Accesseur de pk {@link #pk}.
     * @return retourne pk
     */
    public PiecesJointesCycleDevoirsPK getPk() {
        return pk;
    }

    /**
     * Mutateur de pk {@link #pk}.
     * @param pk le pk to set
     */
    public void setPk(PiecesJointesCycleDevoirsPK pk) {
        this.pk = pk;
    }



    /**
     * Accesseur pieceJointe.
     * @return pieceJointe
     */
    public PieceJointeBean getPieceJointe() {
        return pieceJointe;
    }

    /**
     * Mutateur pieceJointe.
     * @param pieceJointe Le pieceJointe à modifier
     */
    public void setPieceJointe(PieceJointeBean pieceJointe) {
        this.pieceJointe = pieceJointe;
    }

    /**
     * Accesseur de cycleDevoir {@link #cycleDevoir}.
     * @return retourne cycleDevoir
     */
    public CycleDevoirBean getCycleDevoir() {
        return cycleDevoir;
    }

    /**
     * Mutateur de cycleDevoir {@link #cycleDevoir}.
     * @param cycleDevoir le cycleDevoir to set
     */
    public void setCycleDevoir(CycleDevoirBean cycleDevoir) {
        this.cycleDevoir = cycleDevoir;
    }

    /**
     * idPieceJointe.
     *
     * @return the idPieceJointe attribute
     */
    public Integer getIdPieceJointe() {
        return (this.pk != null) ? pk.idPieceJointe : null;
    }

    /**
     * idPieceJointe.
     *
     * @param idPieceJointe id de la pièce jointe.
     */
    public void setIdPieceJointe(Integer idPieceJointe) {
        if (this.pk == null) {
            this.pk = new PiecesJointesCycleDevoirsPK();
        }
        this.pk.idPieceJointe = idPieceJointe;
    }
    
    /**
     * idDevoir.
     *
     * @return the idDevoir attribute
     */
    public Integer getIdCycleDevoir() {
        return (this.pk != null) ? pk.idCycleDevoir : null;
    }

    /**
     * idDevoir.
     *
     * @param idCycleDevoir id du devoir.
     */
    public void setIdCycleDevoir(Integer idCycleDevoir) {
        if (this.pk == null) {
            this.pk = new PiecesJointesCycleDevoirsPK();
        }
        this.pk.idCycleDevoir = idCycleDevoir;
    }
    
    /**
     * ToString.
     *
     * @return chaîne.
     */
    public String toString() {
        final StringBuffer str = new StringBuffer("{");

        str.append("{[AvisAptitudeAptitudeClasseCritere], ");
        str.append("pk=" + getIdPieceJointe() + ", ");
        str.append("pk=" + getIdCycleDevoir() + ", ");

        str.append("{[PiecesJointesCycleDevoirs]");
        return (str.toString());
    }
}
