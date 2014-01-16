/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: PiecesJointesSeancesBean.java,v 1.2 2009/04/28 12:45:50 ent_breyton Exp $
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
@Table(name = "cahier_piece_jointe_cycle_seance", schema="cahier_courant")
public class PiecesJointesCycleSeancesBean implements Serializable {

    /** Serial. */
    private static final long serialVersionUID = -1555063237131529282L;

    /** Identifiant. */
    @Id
    private PiecesJointesCycleSeancesPK pk;

    /** Objet PieceJointe. */
    @ManyToOne(cascade =  { } )
    @JoinColumns(
            {
                @JoinColumn(name = "id_piece_jointe", referencedColumnName = "id", insertable = false, updatable = false)
            }
    )
    private PieceJointeBean pieceJointe;
    
    /** Objet CycleSeance. */
    @ManyToOne(cascade =  { } )
    @JoinColumns(
            {
                @JoinColumn(name = "id_cycle_seance", referencedColumnName = "id", insertable = false, updatable = false)
            }
    )
    private CycleSeanceBean cycleSeance;
    
    /**
     * 
     * Constructeur.
     */
    public PiecesJointesCycleSeancesBean() {
    }

    /**
     * Accesseur pk.
     * @return pk
     */
    public PiecesJointesCycleSeancesPK getPk() {
        return pk;
    }

    /**
     * Mutateur pk.
     * @param pk Le pk à modifier
     */
    public void setPk(PiecesJointesCycleSeancesPK pk) {
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
     * Accesseur seance.
     * @return seance
     */
    public CycleSeanceBean getCycleSeance() {
        return cycleSeance;
    }

    /**
     * Mutateur seance.
     * @param seance La séance à modifier
     */
    public void setCycleSeance(CycleSeanceBean seance) {
        this.cycleSeance = seance;
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
            this.pk = new PiecesJointesCycleSeancesPK();
        }
        this.pk.idPieceJointe = idPieceJointe;
    }
    
    /**
     * idSeance.
     *
     * @return the idSeance attribute
     */
    public Integer getIdCycleSeance() {
        return (this.pk != null) ? pk.idCycleSeance : null;
    }

    /**
     * idCycleSeance.
     *
     * @param idCycleSeance id de la seance.
     */
    public void setIdCycleSeance(Integer idCycleSeance) {
        if (this.pk == null) {
            this.pk = new PiecesJointesCycleSeancesPK();
        }
        this.pk.idCycleSeance = idCycleSeance;
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
        str.append("pk=" + getIdCycleSeance() + ", ");

        str.append("{[PiecesJointesCycleSeances]");
        return (str.toString());
    }
}
