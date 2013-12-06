/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: jalopy.xml,v 1.1 2010/06/15 12:20:58 ent_breyton Exp $
 */

package org.crlr.metier.business;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.apache.commons.collections.CollectionUtils;
import org.crlr.alimentation.DTO.EnseignantDTO;
import org.crlr.dto.ResultatDTO;
import org.crlr.dto.application.base.TypeReglesAdmin;
import org.crlr.dto.application.base.UserDTOForList;
import org.crlr.dto.application.inspection.DroitInspecteurDTO;
import org.crlr.dto.application.inspection.RechercheDroitInspecteurQO;
import org.crlr.dto.application.inspection.SaveDroitInspecteurQO;
import org.crlr.exception.metier.MetierException;
import org.crlr.message.ConteneurMessage;
import org.crlr.message.Message;
import org.crlr.message.Message.Nature;
import org.crlr.metier.entity.EnseignantBean;
import org.crlr.metier.entity.EtablissementsEnseignantsBean;
import org.crlr.metier.entity.InspecteurBean;
import org.crlr.metier.entity.OuvertureInspecteurBean;
import org.crlr.metier.entity.OuvertureInspecteurPK;
import org.crlr.utils.Assert;
import org.crlr.utils.ObjectUtils;
import org.springframework.stereotype.Repository;

/**
 * DOCUMENTATION INCOMPLETE!
 *
 * @author $author$
 * @version $Revision: 1.1 $
  */
@Repository
public class InspectionHibernateBusiness extends AbstractBusiness
    implements InspectionHibernateBusinessService {
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public ResultatDTO<List<DroitInspecteurDTO>> findDroitsInspection(RechercheDroitInspecteurQO rechercheDroitInspecteurQO) {
        final ResultatDTO<List<DroitInspecteurDTO>> droits =
            new ResultatDTO<List<DroitInspecteurDTO>>();

        Assert.isNotNull("rechercheDroitInspecteurQO", rechercheDroitInspecteurQO);
        Assert.isNotNull("idEtablissement", rechercheDroitInspecteurQO.getIdEtablissement());
        final Boolean vraiOuFauxRechercheFromDirecteur = rechercheDroitInspecteurQO.getVraiOuFauxRechercheFromDirecteur();
        Assert.isNotNull("VraiOuFauxRechercheFromDirecteur", vraiOuFauxRechercheFromDirecteur);
        if (! vraiOuFauxRechercheFromDirecteur){
            Assert.isNotNull("idInspecteur", rechercheDroitInspecteurQO.getIdInspecteur());
        }
        

        String requete =
            "SELECT distinct new map( " + " J.dateDebut as dateDebut, J.dateFin as dateFin, " +
            " J.uidDirecteur as uidDirecteur ," + " J.nomDirecteur as nomDirecteur ," +
            " ENS as enseignant, INS.id as idInspecteur ," +
            " INS.nom as nomInspecteur ," + " INS.prenom as prenomInspecteur ," +
            " INS.civilite as civiliteInspecteur )" + " FROM " +
            OuvertureInspecteurBean.class.getName() + " J " +
            " INNER JOIN J.etablissement ETAB " + " INNER JOIN J.enseignant ENS " +
            " INNER JOIN J.inspecteur INS " + " WHERE " + " ETAB.id = :idEtab " ;
        if (! vraiOuFauxRechercheFromDirecteur){
            requete += 
                "AND INS.id = :idInd " +
                "AND J.dateDebut <= :dateToday " +
                "AND (J.dateFin is null OR J.dateFin >= :dateToday) ";
        }

        requete += " ORDER BY J.dateDebut ASC, INS.nom ASC, J.nomDirecteur ASC";

        final Query query =
            getEntityManager().createQuery(requete)
            .setParameter("idEtab", rechercheDroitInspecteurQO.getIdEtablissement());
        
        if (! vraiOuFauxRechercheFromDirecteur){
            query.setParameter("idInd", rechercheDroitInspecteurQO.getIdInspecteur());
            query.setParameter("dateToday", new Date());
        }

        
        final List<Map<String, ?>> liste = query.getResultList();

        final List<DroitInspecteurDTO> resultat = new ArrayList<DroitInspecteurDTO>();
        
        for (final Map<String, ?> result : liste) {
            final DroitInspecteurDTO droitInspecteurDTO = new DroitInspecteurDTO();
            droitInspecteurDTO.setDateDebut((Date) result.get("dateDebut"));
            droitInspecteurDTO.setDateFin((Date) result.get("dateFin"));

            final EnseignantDTO enseignant = new EnseignantDTO();
            final EnseignantBean enseignantBean = (EnseignantBean) result.get("enseignant");
            ObjectUtils.copyProperties(enseignant, enseignantBean);
            
            droitInspecteurDTO.setEnseignantDTO(enseignant);

            final UserDTOForList inspecteur = new UserDTOForList();
            final Integer idInspecteurTrouve = (Integer) result.get("idInspecteur");
            inspecteur.setIdentifiant(idInspecteurTrouve.toString());
            inspecteur.setNom((String) result.get("civiliteInspecteur") + " " +
                    (String) result.get("nomInspecteur") + " " +
                    (String) result.get("prenomInspecteur"));
            droitInspecteurDTO.setInspecteur(inspecteur);

            final UserDTOForList directeur = new UserDTOForList();
            directeur.setIdentifiant((String) result.get("idDirecteur"));
            directeur.setNom((String) result.get("nomDirecteur"));
            droitInspecteurDTO.setDirecteur(directeur);

            droitInspecteurDTO.setIdEtablissement(rechercheDroitInspecteurQO.getIdEtablissement());
            resultat.add(droitInspecteurDTO);
        }

        droits.setValeurDTO(resultat);

        return droits;
    }
    
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public List<UserDTOForList> findListeInspecteurs() {
        final String requete =
            "SELECT INS FROM " +
            InspecteurBean.class.getName() + " INS ORDER BY INS.nom ASC, INS.prenom ASC ";

        final Query query =
            getEntityManager().createQuery(requete);
        final List<InspecteurBean> liste = query.getResultList();

        final List<UserDTOForList> resultat = new ArrayList<UserDTOForList>();
        for (final InspecteurBean inspecteurBean : liste) {
            final UserDTOForList userDTO = new UserDTOForList(inspecteurBean);
            resultat.add(userDTO);
        }

        return resultat;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public List<EnseignantDTO> findListeEnseignants(Integer idEtablissement) {
        final String requete =
            "SELECT ENS FROM " +
            EtablissementsEnseignantsBean.class.getName() + " ENSETAB " +
            " INNER JOIN ENSETAB.enseignant ENS  " +
            " WHERE ENSETAB.etablissement.id = :idEtab ORDER BY ENS.nom ASC, ENS.prenom ASC ";

        final Query query =
            getEntityManager().createQuery(requete).setParameter("idEtab", idEtablissement);
        final List<EnseignantBean> liste = query.getResultList();

        final List<EnseignantDTO> resultat = new ArrayList<EnseignantDTO>();
        for (final EnseignantBean enseignantBean : liste) {
            final EnseignantDTO userDTO = new EnseignantDTO();
            ObjectUtils.copyProperties(userDTO,  enseignantBean);
            resultat.add(userDTO);
        }

        return resultat;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public void saveDroit(SaveDroitInspecteurQO saveDroitInspecteurQO) {
        Assert.isNotNull("saveDroitInspecteurQO", saveDroitInspecteurQO);
        Assert.isNotNull("idEnseignant", saveDroitInspecteurQO.getIdsEnseignant());
        Assert.isNotNull("idEtablissement", saveDroitInspecteurQO.getIdEtablissement());
        Assert.isNotNull("idInspecteur", saveDroitInspecteurQO.getIdsInspecteur());
        Assert.isNotNull("vraiOufauxAjout", saveDroitInspecteurQO.getVraiOuFauxAjout());
        
        
        if (saveDroitInspecteurQO.getVraiOuFauxAjout()){
            
            Assert.isNotNull("directeur", saveDroitInspecteurQO.getDirecteur());
            Assert.isNotNull("uidDirecteur", saveDroitInspecteurQO.getDirecteur().getIdentifiant());
            Assert.isNotNull("nomDirecteur", saveDroitInspecteurQO.getDirecteur().getNom());
            
            for (Integer idEnseignant : saveDroitInspecteurQO.getIdsEnseignant()){
                for (Integer idInspecteur : saveDroitInspecteurQO.getIdsInspecteur()){
                    final OuvertureInspecteurBean ouvertureInspecteurBean = new OuvertureInspecteurBean();
                    
                    ouvertureInspecteurBean.setDateDebut(Calendar.getInstance().getTime());
                    ouvertureInspecteurBean.setIdEtablissement(saveDroitInspecteurQO.getIdEtablissement());
                    ouvertureInspecteurBean.setIdEnseignant(idEnseignant);
                    ouvertureInspecteurBean.setIdInspecteur(idInspecteur);
                    ouvertureInspecteurBean.setUidDirecteur(saveDroitInspecteurQO.getDirecteur().getIdentifiant());
                    ouvertureInspecteurBean.setNomDirecteur(saveDroitInspecteurQO.getDirecteur().getNom());
                    
                    final String query =
                        "SELECT 1 FROM " + OuvertureInspecteurBean.class.getName() + " OUV WHERE "+
                        "OUV.etablissement.id = :idEtab AND " +
                        "OUV.enseignant.id = :idEns AND " +
                        "OUV.inspecteur.id = :idIns  ";

                    final List<Object> res = getEntityManager().createQuery(query)
                        .setParameter("idEtab", saveDroitInspecteurQO.getIdEtablissement())
                        .setParameter("idEns", idEnseignant)
                        .setParameter("idIns", idInspecteur)
                        .getResultList();
                    
                    if (CollectionUtils.isEmpty(res)){
                        try {
                        getEntityManager().persist(ouvertureInspecteurBean);
                        } catch (Exception ex) {
                            log.error(ex, "ex");
                        }
                    }
                }   
            }
            
        } else {
            final String query =
                "DELETE FROM " + OuvertureInspecteurBean.class.getName() + " OUV WHERE "+
                "OUV.etablissement.id = :idEtab AND " +
                "OUV.enseignant.id = :idEns AND " +
                "OUV.inspecteur.id = :idIns  ";

            getEntityManager().createQuery(query)
                .setParameter("idEtab", saveDroitInspecteurQO.getIdEtablissement())
                .setParameter("idEns", saveDroitInspecteurQO.getIdsEnseignant().get(0))
                .setParameter("idIns", saveDroitInspecteurQO.getIdsInspecteur().get(0))
                .executeUpdate();
        }
        
    }
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public Integer exist(String uid) {
        final Integer id;

        final String query =
            "SELECT e.id FROM " + InspecteurBean.class.getName() +
            " e WHERE e.uid = :uid";

        final List<Integer> liste =
            getEntityManager().createQuery(query).setParameter("uid", uid).getResultList();

        if (!CollectionUtils.isEmpty(liste)) {
            id = liste.get(0);
        } else {
            id = null;
        }

        return id;
    }

    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public void ajouter(DroitInspecteurDTO droit) throws MetierException {
        
        Assert.isNotNull("DroitInspecteurQO", droit);
        Assert.isNotNull("IdEtablissement",  droit.getIdEtablissement());
        Assert.isNotNull("Enseignant.Id", droit.getEnseignant().getId());
        Assert.isNotNull("Inspecteur.Identifiant", droit.getInspecteur().getIdentifiant());
        
        final ConteneurMessage conteneurMessage = new ConteneurMessage();
        if (droit.getDateDebut()==null) {
            conteneurMessage.add(new Message(TypeReglesAdmin.ADMIN_25.name(), Nature.BLOQUANT,
            droit.getInspecteur().getNom(),
            droit.getEnseignant().getNom()));
            throw new MetierException(conteneurMessage,  "Vous devez saisir la date de début.");            
        }
        if (droit.getDateFin()!=null && droit.getDateDebut().after(droit.getDateFin())) {
            conteneurMessage.add(new Message(TypeReglesAdmin.ADMIN_26.name(), Nature.BLOQUANT,
            droit.getInspecteur().getNom(),
            droit.getEnseignant().getNom()));
            throw new MetierException(conteneurMessage,  "Vous devez saisir la date de début avant la date de fin.");            
        }
        
        
        final String query =
            "SELECT 1 FROM " + OuvertureInspecteurBean.class.getName() + " OUV WHERE "+
            "OUV.etablissement.id = :idEtab AND " +
            "OUV.enseignant.id = :idEns AND " +
            "OUV.inspecteur.id = :idIns  ";

        final List<Object> res = getEntityManager().createQuery(query)
            .setParameter("idEtab", droit.getIdEtablissement())
            .setParameter("idEns", droit.getEnseignant().getId())
            .setParameter("idIns", Integer.parseInt(droit.getInspecteur().getIdentifiant()))
            .getResultList();
        
        if (CollectionUtils.isEmpty(res)){
            try {
                final OuvertureInspecteurBean ouvertureInspecteurBean = new OuvertureInspecteurBean();
                
                ouvertureInspecteurBean.setIdEtablissement(droit.getIdEtablissement());
                ouvertureInspecteurBean.setIdEnseignant(droit.getEnseignant().getId());
                ouvertureInspecteurBean.setIdInspecteur(Integer.parseInt(droit.getInspecteur().getIdentifiant()));
                ouvertureInspecteurBean.setUidDirecteur(droit.getDirecteur().getIdentifiant());
                ouvertureInspecteurBean.setNomDirecteur(droit.getDirecteur().getNom());
                ouvertureInspecteurBean.setDateDebut(droit.getDateDebut());
                ouvertureInspecteurBean.setDateFin(droit.getDateFin());
                
                
                
                getEntityManager().persist(ouvertureInspecteurBean);
                getEntityManager().flush();
            } catch (Exception ex) {
                log.error(ex, "ex");
            }            
        }
    }

    /**
     * {@inheritDoc}
     */
    public void modifier(DroitInspecteurDTO droit) throws MetierException {
        
        Assert.isNotNull("DroitInspecteurQO", droit);
        Assert.isNotNull("IdEtablissement",  droit.getIdEtablissement());
        Assert.isNotNull("Enseignant.Id", droit.getEnseignant().getId());
        Assert.isNotNull("Inspecteur.Identifiant", droit.getInspecteur().getIdentifiant());
        
        
        final ConteneurMessage conteneurMessage = new ConteneurMessage();
        if (droit.getDateDebut()==null) {
            conteneurMessage.add(new Message(TypeReglesAdmin.ADMIN_25.name(), Nature.BLOQUANT,
            droit.getInspecteur().getNom(),
            droit.getEnseignant().getNom()));
            throw new MetierException(conteneurMessage,  "Vous devez saisir la date de début.");            
        }
        if (droit.getDateFin()!=null && droit.getDateDebut().after(droit.getDateFin())) {
            conteneurMessage.add(new Message(TypeReglesAdmin.ADMIN_26.name(), Nature.BLOQUANT,
            droit.getInspecteur().getNom(),
            droit.getEnseignant().getNom()));
            throw new MetierException(conteneurMessage,  "Vous devez saisir la date de début avant la date de fin.");            
        }
        
        final OuvertureInspecteurPK pk = new OuvertureInspecteurPK(
                droit.getEnseignant().getId(),
                droit.getIdEtablissement(), 
                Integer.parseInt(droit.getInspecteur().getIdentifiant()));
        
        final OuvertureInspecteurBean ouvertureInspecteurBean = getEntityManager().find(OuvertureInspecteurBean.class, pk); 
        if (ouvertureInspecteurBean != null) {
            ouvertureInspecteurBean.setDateDebut(droit.getDateDebut());
            ouvertureInspecteurBean.setDateFin(droit.getDateFin());
            getEntityManager().flush();
        }
        
    }
    
    
    /**
     * {@inheritDoc}
     */
    public void supprimer(DroitInspecteurDTO droit) {
        Assert.isNotNull("DroitInspecteurQO", droit);
        Assert.isNotNull("IdEtablissement",  droit.getIdEtablissement());
        Assert.isNotNull("Enseignant.Id", droit.getEnseignant().getId());
        Assert.isNotNull("Inspecteur.Identifiant", droit.getInspecteur().getIdentifiant());
        
        final OuvertureInspecteurPK pk = new OuvertureInspecteurPK(
                droit.getEnseignant().getId(),
                droit.getIdEtablissement(), 
                Integer.parseInt(droit.getInspecteur().getIdentifiant()));
        
        final OuvertureInspecteurBean ouvertureInspecteurBean = getEntityManager().find(OuvertureInspecteurBean.class, pk);
        if (ouvertureInspecteurBean != null) {
            getEntityManager().remove(ouvertureInspecteurBean);
            getEntityManager().flush();
        }
    }
}
