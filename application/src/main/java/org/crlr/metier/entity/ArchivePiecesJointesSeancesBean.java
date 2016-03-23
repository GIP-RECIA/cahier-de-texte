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
@Table(name = "cahier_archive_piece_jointe_seance", schema="cahier_courant")
public class ArchivePiecesJointesSeancesBean implements Serializable {
    
    /**
     * 
     */
    private static final long serialVersionUID = 1039662851323471542L;

    /** DOCUMENTATION INCOMPLETE! */
    @Id
    private ArchivePiecesJointesSeancesPK pk;

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
            {
                @JoinColumn(name = "id_archive_seance", referencedColumnName = "id_archive_seance", insertable = false, updatable = false)
            }
        )
    private ArchiveSeanceBean seance;
    
    /**
     * 
     * Constructeur.
     */
    public ArchivePiecesJointesSeancesBean() {
    }

    /**
     * Accesseur pk.
     * @return pk
     */
    public ArchivePiecesJointesSeancesPK getPk() {
        return pk;
    }

    /**
     * Mutateur pk.
     * @param pk Le pk à modifier
     */
    public void setPk(ArchivePiecesJointesSeancesPK pk) {
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
    public ArchiveSeanceBean getSeance() {
        return seance;
    }

    /**
     * Mutateur seance.
     * @param seance La séance à modifier
     */
    public void setDevoir(ArchiveSeanceBean seance) {
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
            this.pk = new ArchivePiecesJointesSeancesPK();
        }
        this.pk.idPieceJointe = idPieceJointe;
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
        str.append("pk=" + getIdPieceJointe() + ", ");

        str.append("{[ArchivePiecesJointesSeances]");
        return (str.toString());
    }
}
