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
@Table(name = "cahier_piece_jointe_devoir", schema="cahier_courant")
public class PiecesJointesDevoirsBean implements Serializable {
    
    /**
     * 
     */
    private static final long serialVersionUID = 1039662851323471542L;

    /** DOCUMENTATION INCOMPLETE! */
    @Id
    private PiecesJointesDevoirsPK pk;

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
                name = "id_devoir", referencedColumnName = "id", insertable = false, updatable = false
            )
    }
        )
    private DevoirBean devoir;
    
    /**
     * 
     * Constructeur.
     */
    public PiecesJointesDevoirsBean() {
    }

    /**
     * Accesseur pk.
     * @return pk
     */
    public PiecesJointesDevoirsPK getPk() {
        return pk;
    }

    /**
     * Mutateur pk.
     * @param pk Le pk à modifier
     */
    public void setPk(PiecesJointesDevoirsPK pk) {
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
     * Accesseur devoir.
     * @return devoir
     */
    public DevoirBean getDevoir() {
        return devoir;
    }

    /**
     * Mutateur devoir.
     * @param devoir Le devoir à modifier
     */
    public void setDevoir(DevoirBean devoir) {
        this.devoir = devoir;
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
            this.pk = new PiecesJointesDevoirsPK();
        }
        this.pk.idPieceJointe = idPieceJointe;
    }
    
    /**
     * idDevoir.
     *
     * @return the idDevoir attribute
     */
    public Integer getIdDevoir() {
        return (this.pk != null) ? pk.idDevoir : null;
    }

    /**
     * idDevoir.
     *
     * @param idDevoir id du devoir.
     */
    public void setIdDevoir(Integer idDevoir) {
        if (this.pk == null) {
            this.pk = new PiecesJointesDevoirsPK();
        }
        this.pk.idDevoir = idDevoir;
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
        str.append("pk=" + getIdDevoir() + ", ");

        str.append("{[PiecesJointesDevoirs]");
        return (str.toString());
    }
}
