/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: PieceJointeHibernateBusiness.java,v 1.12 2010/04/21 09:04:38 ent_breyton Exp $
 */

package org.crlr.metier.business;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.apache.commons.lang.StringUtils;
import org.crlr.dto.application.base.TypeReglesPieceJointe;
import org.crlr.exception.metier.MetierException;
import org.crlr.message.ConteneurMessage;
import org.crlr.message.Message;
import org.crlr.message.Message.Nature;
import org.crlr.metier.entity.ArchivePiecesJointesDevoirsBean;
import org.crlr.metier.entity.ArchivePiecesJointesSeancesBean;
import org.crlr.metier.entity.PieceJointeBean;
import org.crlr.metier.entity.PiecesJointesDevoirsBean;
import org.crlr.metier.entity.PiecesJointesSeancesBean;
import org.crlr.web.dto.FileUploadDTO;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

/**
 * PieceJointeHibernateBusiness.
 * 
 * @author vibertd
 * @version $Revision: 1.12 $
 */
@Repository
public class PieceJointeHibernateBusiness extends AbstractBusiness implements
        PieceJointeHibernateBusinessService {
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public PieceJointeBean find(Integer id) {
        final PieceJointeBean pieceJointe;

        final String query = "SELECT p FROM " + PieceJointeBean.class.getName()
                + " p WHERE p.id = :id";

        final List<PieceJointeBean> liste = getEntityManager().createQuery(
                query).setParameter("id", id).getResultList();

        if (!CollectionUtils.isEmpty(liste)) {
            pieceJointe = liste.get(0);
        } else {
            pieceJointe = null;
        }

        return pieceJointe;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public Boolean findUsed(Integer id) {
        
        // Verifie dans seance
        String query = " SELECT PJ.pk.idSeance " + "FROM "
            + PiecesJointesSeancesBean.class.getName() + " PJ "
            + " WHERE PJ.pk.idPieceJointe = :id"; 
        List<Integer> resultat = getEntityManager().createQuery(query).setParameter("id", id).getResultList();
        
        if (!CollectionUtils.isEmpty(resultat)) { 
            return Boolean.TRUE;
        }
        
        // Verifie dans devoir
        query = " SELECT PJ.pk.idDevoir " + "FROM "
            + PiecesJointesDevoirsBean.class.getName() + " PJ "
            + " WHERE PJ.pk.idPieceJointe = :id"; 
        resultat = getEntityManager().createQuery(query).setParameter("id", id).getResultList();
        
        if (!CollectionUtils.isEmpty(resultat)) { 
            return Boolean.TRUE;
        }

        // Verifie dans archive_seance
        query = " SELECT PJ.pk.idArchiveSeance " + "FROM "
            + ArchivePiecesJointesSeancesBean.class.getName() + " PJ "
            + " WHERE PJ.pk.idPieceJointe = :id"; 
        resultat = getEntityManager().createQuery(query).setParameter("id", id).getResultList();
        
        if (!CollectionUtils.isEmpty(resultat)) { 
            return Boolean.TRUE;
        }
        
        // Verifie dans archive_devoir
        query = " SELECT PJ.pk.idArchiveDevoir " + "FROM "
            + ArchivePiecesJointesDevoirsBean.class.getName() + " PJ "
            + " WHERE PJ.pk.idPieceJointe = :id"; 
        resultat = getEntityManager().createQuery(query).setParameter("id", id).getResultList();
        
        if (!CollectionUtils.isEmpty(resultat)) { 
            return Boolean.TRUE;
        }
        
        return Boolean.FALSE;
    }
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public Integer exist(String id) {
        final Integer idPieceJointe;

        final String query = "SELECT p.id FROM "
                + PieceJointeBean.class.getName() + " p WHERE p.id = :id";

        final List<Integer> liste = getEntityManager().createQuery(query)
                .setParameter("id", id).getResultList();

        if (!CollectionUtils.isEmpty(liste)) {
            idPieceJointe = liste.get(0);
        } else {
            idPieceJointe = null;
        }

        return idPieceJointe;
    }
    
    /**
     * @param idPieceJointe .
     * @param idDevoir .
     * @return true s'il existe
     */
    public boolean existsPJDevoir(Integer idPieceJointe, Integer idDevoir) {

        final String query = " SELECT 1 FROM "
                + PiecesJointesDevoirsBean.class.getName() + " PJD WHERE "
                + " PJD.pk.idPieceJointe = :idPieceJointe AND "
                + " PJD.pk.idDevoir = :idDevoir";

        @SuppressWarnings("unchecked")
        final List<Integer> liste1 = getEntityManager().createQuery(query)
                .setParameter("idPieceJointe", idPieceJointe)
                .setParameter("idDevoir", idDevoir).getResultList();

        final Integer sizeListe1 = liste1.size();

        return sizeListe1 > 0;

    }
    
    /**
     * @param idPieceJointe .
     * @param idSeance .
     * @return true si le lien existe
     */
    public boolean existsPJSeance(Integer idPieceJointe, Integer idSeance) {

        final String query = " SELECT 1 FROM "
                + PiecesJointesSeancesBean.class.getName() + " PJS WHERE "
                + " PJS.pk.idPieceJointe = :idPieceJointe AND "
                + " PJS.pk.idSeance = :idSeance";

        @SuppressWarnings("unchecked")
        final List<Integer> liste1 = getEntityManager().createQuery(query)
                .setParameter("idPieceJointe", idPieceJointe)
                .setParameter("idSeance", idSeance).getResultList();

        final Integer sizeListe1 = liste1.size();

        return sizeListe1 > 0;

    }

    /**
     * {@inheritDoc}
     */
    public Integer save(PieceJointeBean pieceJointe) {
        getEntityManager().persist(pieceJointe);
        getEntityManager().flush();

        return pieceJointe.getId();
    }

  
    /**
     * @param pieceJointeSeanceBean .
     * @throws MetierException ex
     */
    private void savePieceJointeSeance(
            PiecesJointesSeancesBean pieceJointeSeanceBean)
            throws MetierException {
        if (existsPJSeance(pieceJointeSeanceBean.getIdPieceJointe(), pieceJointeSeanceBean.getIdSeance())) {
            final ConteneurMessage conteneurMessage = new ConteneurMessage();
            conteneurMessage.add(new Message(TypeReglesPieceJointe.PIECEJOINTE_07.name(),
                                                 Nature.BLOQUANT));
            throw new MetierException(conteneurMessage,"Avertissement");
           
        }
        getEntityManager().persist(pieceJointeSeanceBean);
        getEntityManager().flush();
    }

    /**
     * {@inheritDoc}
     */
    public void savePieceJointeDevoir (
            PiecesJointesDevoirsBean pieceJointeDevoirsBean) throws MetierException {
        if (existsPJDevoir(pieceJointeDevoirsBean.getIdPieceJointe(), pieceJointeDevoirsBean.getIdDevoir())) {
            final ConteneurMessage conteneurMessage = new ConteneurMessage();
            conteneurMessage.add(new Message(TypeReglesPieceJointe.PIECEJOINTE_07.name(),
                                                 Nature.BLOQUANT));
            throw new MetierException(conteneurMessage,"Avertissement");
           
        }
        
        getEntityManager().persist(pieceJointeDevoirsBean);
        getEntityManager().flush();
    }

    /**
     * {@inheritDoc}
     */
    public Integer savePieceJointeSeance(FileUploadDTO fileUploadDTO)
            throws MetierException {
        
        Integer idPieceJointe = savePieceJointe(fileUploadDTO);

        final PiecesJointesSeancesBean piecesJointesSeancesBean = new PiecesJointesSeancesBean();
        piecesJointesSeancesBean.setIdPieceJointe(idPieceJointe);
        piecesJointesSeancesBean.setIdSeance(fileUploadDTO.getIdSeance());
        
        this.savePieceJointeSeance(piecesJointesSeancesBean);

        return null;
    }

    /**
     * Méthode utilitaire pour PJ séance et devoir.
     * @param fileUploadDTO 
     * @return l'id pièce jointe
     * @throws MetierException ex
     */
    private Integer savePieceJointe(FileUploadDTO fileUploadDTO) throws MetierException {
        
        
        //Le cas où quelqu'un rattache le même fichier non dans la base deux ou plus fois implique qu'il faut
        //vérifier si c'est dans la base ou non.  Les deux auront false pour enBase pourtant, en sauvegardant
        //le deuxième, il est déjà dans la base.
        
        FileUploadDTO fileEnBase = findFile(fileUploadDTO.getUid(), fileUploadDTO.getPathDB(), fileUploadDTO.getNom());

        final Integer idPieceJointe;
        
        if (fileEnBase == null) {
            final PieceJointeBean pieceJointeBean = new PieceJointeBean();
            final BaseHibernateBusiness baseHibernateBusiness = new BaseHibernateBusiness(
                    this.getEntityManager());
            idPieceJointe = baseHibernateBusiness
                    .getIdInsertion("cahier_piece_jointe");
            pieceJointeBean.setId(idPieceJointe);
            pieceJointeBean.setCode("PJ" + idPieceJointe);
            pieceJointeBean.setNomFichier(fileUploadDTO.getNom());
            pieceJointeBean.setUid(fileUploadDTO.getUid());
            pieceJointeBean.setChemin(fileUploadDTO.getPathDB());

            this.save(pieceJointeBean);
        } else {
            idPieceJointe = fileEnBase.getId();
        }
        
        return idPieceJointe;
    }
    
    /**
     * {@inheritDoc}
     */
    public Integer savePieceJointeDevoir(FileUploadDTO fileUploadDTO) throws MetierException {
        Integer idPieceJointe = savePieceJointe(fileUploadDTO);

        final PiecesJointesDevoirsBean piecesJointesDevoirsBean = new PiecesJointesDevoirsBean();
        piecesJointesDevoirsBean.setIdPieceJointe(idPieceJointe);
        piecesJointesDevoirsBean.setIdDevoir(fileUploadDTO.getIdDevoir());

        this.savePieceJointeDevoir(piecesJointesDevoirsBean);

        return null;
    }

    /**
     * {@inheritDoc}
     */
    public void deletePieceJointe(Integer idPieceJointe) throws MetierException {
        final String query = "DELETE FROM " + PieceJointeBean.class.getName()
                + " PJ WHERE PJ.id = :id";

        getEntityManager().createQuery(query)
                .setParameter("id", idPieceJointe).executeUpdate();
        getEntityManager().flush();
    }

    /**
     * Récupere la liste des pieces jointes d'un devoir.
     * 
     * @param idDevoir
     *            l'id du devoir
     * @return La lsite des pieces jointes  
     */
    @SuppressWarnings("unchecked")
    private List<Integer> selectPieceJointeDevoir(Integer idDevoir) {
        final String query = " SELECT PJ.pk.idPieceJointe " + "FROM "
                + PiecesJointesDevoirsBean.class.getName() + " PJ "
                + " WHERE PJ.pk.idDevoir = :id";

        final List<Integer> resultat = getEntityManager().createQuery(query)
                .setParameter("id", idDevoir).getResultList();

        return resultat;
    }

    /**
     * {@inheritDoc}
     */
    public List<Integer> deletePieceJointeDevoir(Integer idDevoir) {
        final List<Integer> listeIdPieceJointe = selectPieceJointeDevoir(idDevoir);

        final String query = "DELETE FROM "
                + PiecesJointesDevoirsBean.class.getName()
                + " PJ WHERE PJ.pk.idDevoir = :id";
        @SuppressWarnings("unused")
        final Integer resultat = getEntityManager().createQuery(query)
                .setParameter("id", idDevoir).executeUpdate();

        return listeIdPieceJointe;
    }

    /**
     * {@inheritDoc}
     */
    public void deletePieceJointeSeance(Integer idSeance)
            throws MetierException {
        final String query = "DELETE FROM "
                + PiecesJointesSeancesBean.class.getName()
                + " PJ WHERE PJ.pk.idSeance = :id";
        @SuppressWarnings("unused")
        final Integer resultat = getEntityManager().createQuery(query)
                .setParameter("id", idSeance).executeUpdate();      
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public boolean verifieAppartenancePieceJointe(FileUploadDTO fileUploadDTO)
            throws MetierException {
        boolean existe = false;

        final String nomPieceJointe = fileUploadDTO.getNom();
        final String chemin = fileUploadDTO.getPathDB();

        final String query1 = " SELECT 1 FROM "
                + PiecesJointesDevoirsBean.class.getName()
                + " PJD INNER JOIN PJD.pieceJointe PJ " + " WHERE "
                + " PJ.nomFichier = :nomPieceJointe AND " 
                + " PJ.chemin = :chemin ";

        final String query2 = " SELECT 1 FROM "
                + PiecesJointesSeancesBean.class.getName()
                + " PJS INNER JOIN PJS.pieceJointe PJ " + " WHERE "
                + " PJ.nomFichier = :nomPieceJointe AND "
                + " PJ.chemin = :chemin ";

        final List<Integer> liste1 = getEntityManager().createQuery(
                query1).setParameter("nomPieceJointe", nomPieceJointe)
                .setParameter("chemin", chemin)
                .getResultList();

        final List<Integer> liste2 = getEntityManager().createQuery(
                query2).setParameter("nomPieceJointe", nomPieceJointe)
                .setParameter("chemin", chemin)
                .getResultList();

        final Integer sizeListe1 = liste1.size();
        final Integer sizeListe2 = liste2.size();

        if ((sizeListe1 + sizeListe2) > 0) {
            existe = true;
        }

        return existe;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public boolean fileIsUsed(final String uid, final String nomDossier, final String nomFichier) {

        final String query = " SELECT PJ.id FROM "
            + PieceJointeBean.class.getName() + " PJ "  
            + " WHERE " 
            + " PJ.nomFichier = :nomFichier AND " 
            + " PJ.chemin = :nomDossier AND " 
            + " PJ.uid = :uid ";
        
        final EntityManager entity = getEntityManager();
        final javax.persistence.Query requete = entity.createQuery(query);
        requete.setParameter("nomFichier", nomFichier);
        requete.setParameter("nomDossier", nomDossier);
        requete.setParameter("uid", uid);
        
        final List<Integer> liste = requete.getResultList();
        final Integer sizeListe = liste.size(); 
        
        if ((sizeListe) > 0) {
            for (final Integer id : liste) {
                if (this.findUsed(id)) {
                    return true;
                }
                
            }
        }
        return false; 
    }
    
    /**
     * Recherche un FileUploadDTO à parir des infos permettant de l'identifier. 
     * Le champ listIdPJ peut eventuellement contenir plusieurs id de pj si le meme 
     * fichier est utilisé par plusieurs pj. 
     * @param uid uid de l'utilisateur connecté
     * @param nomDossier nom du dossier contenant le fichier a rechercher ex: "/Mes documents/SousDossier" 
     * @param nomFichier nom du fichier a rechercher ex: "monFichier.doc" 
     * @return FileUploadDTO si le fichier a été trouvé dans la table cahier_piece_jointe sinon NULL
     * @throws MetierException Exception 
     */
    @SuppressWarnings("unchecked")
    public FileUploadDTO findFile(final String uid, final String nomDossier, final String nomFichier) 
        throws MetierException {

        final String query = "SELECT PJ FROM " 
            + PieceJointeBean.class.getName() + " PJ "  
            + " WHERE " 
            + " PJ.nomFichier = :nomFichier AND " 
            + " PJ.chemin = :nomDossier AND " 
            + " PJ.uid = :uid ";

        final List<PieceJointeBean> liste = getEntityManager()
                .createQuery(query)
                .setParameter("nomFichier", nomFichier)
                .setParameter("nomDossier", nomDossier)
                .setParameter("uid", uid)
                .getResultList();

        if (CollectionUtils.isEmpty(liste)) {
            return null;
        }
        
        final PieceJointeBean pieceJointe = liste.get(0);
        final FileUploadDTO fileUploadDTO = new FileUploadDTO();
        
        fileUploadDTO.setEnBase(false);
        fileUploadDTO.setId(pieceJointe.getId());
        fileUploadDTO.setNom(pieceJointe.getNomFichier());
        fileUploadDTO.setUid(pieceJointe.getUid());
        fileUploadDTO.setPathDB(pieceJointe.getChemin());
        
        // Ajoute les id de chaque PJ correspondant au meme fichier.
        final List<Integer> listeId = new ArrayList<Integer>();
        for (PieceJointeBean pieceJointeBean : liste) {
            // Meme si la PJ est dans la table, il faut verifier si elle est utilisée
            // pour une seance ou un devoir
            if (findUsed(pieceJointeBean.getId())) {
                fileUploadDTO.setEnBase(true);
            }
            listeId.add(pieceJointeBean.getId());
        }
        fileUploadDTO.setListeIdPJ(listeId);
        

        return fileUploadDTO;
        
    }

    
    /**
     * {@inheritDoc}
     */
    public boolean moveFile(final FileUploadDTO fichierSrc, final String dossierDes) {

        if (fichierSrc.getListeIdPJ() == null || fichierSrc.getListeIdPJ().isEmpty()) {
            return true;
        }
        
        // Construit la liste des id de PJ recherchées
        String listeIdPJ = "";
        for (Integer id : fichierSrc.getListeIdPJ()) {
            if (!StringUtils.isEmpty(listeIdPJ)) {
                listeIdPJ += ",";
            }
            listeIdPJ += id.toString();
        }

        final String query = "UPDATE "
            + PieceJointeBean.class.getName()  
            + " SET chemin = :chemin "
            + " WHERE id in (" + listeIdPJ + ")"; 
        
        final Integer resultat = getEntityManager().createQuery(query)
            .setParameter("chemin", dossierDes)
            .executeUpdate();
        
        return (resultat > 0); 
    }
}
