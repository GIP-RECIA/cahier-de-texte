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
@Table(name = "cahier_piece_jointe_seance", schema="cahier_courant")
public class PiecesJointesSeancesBean implements Serializable {
    
    /**
     * 
     */
    private static final long serialVersionUID = 1039662851323471542L;

    /** DOCUMENTATION INCOMPLETE! */
    @Id
    private PiecesJointesSeancesPK pk;

    /** DOCUMENTATION INCOMPLETE! */
    @ManyToOne(cascade =  {
    }
    )
    @JoinColumns(
            {@JoinColumn(
                name = "id_piece_jointe", referencedColumnName = "id", insertable = false, updatable = false
            )
    }
        )
    private PieceJointeBean pieceJointe;
    
    /** DOCUMENTATION INCOMPLETE! */
    @ManyToOne(cascade =  {
    }
    )
    @JoinColumns(
            {@JoinColumn(
                name = "id_seance", referencedColumnName = "id", insertable = false, updatable = false
            )
    }
        )
    private SeanceBean seance;
    
    /**
     * 
     * Constructeur.
     */
    public PiecesJointesSeancesBean() {
    }

    /**
     * Accesseur pk.
     * @return pk
     */
    public PiecesJointesSeancesPK getPk() {
        return pk;
    }

    /**
     * Mutateur pk.
     * @param pk Le pk à modifier
     */
    public void setPk(PiecesJointesSeancesPK pk) {
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
    public SeanceBean getSeance() {
        return seance;
    }

    /**
     * Mutateur seance.
     * @param seance La séance à modifier
     */
    public void setSeance(SeanceBean seance) {
        this.seance = seance;
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
            this.pk = new PiecesJointesSeancesPK();
        }
        this.pk.idPieceJointe = idPieceJointe;
    }
    
    /**
     * idSeance.
     *
     * @return the idSeance attribute
     */
    public Integer getIdSeance() {
        return (this.pk != null) ? pk.idSeance : null;
    }

    /**
     * idSeance.
     *
     * @param idSeance id de la seance.
     */
    public void setIdSeance(Integer idSeance) {
        if (this.pk == null) {
            this.pk = new PiecesJointesSeancesPK();
        }
        this.pk.idSeance = idSeance;
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
        str.append("pk=" + getIdSeance() + ", ");

        str.append("{[PiecesJointesSeances]");
        return (str.toString());
    }
}
