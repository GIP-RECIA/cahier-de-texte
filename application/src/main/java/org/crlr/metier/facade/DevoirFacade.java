/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: DevoirFacade.java,v 1.14 2010/04/21 09:04:38 ent_breyton Exp $
 */

package org.crlr.metier.facade;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.crlr.dto.ResultatDTO;
import org.crlr.dto.application.base.TypeGroupe;
import org.crlr.dto.application.base.TypeReglesAdmin;
import org.crlr.dto.application.base.TypeReglesClasse;
import org.crlr.dto.application.base.TypeReglesDevoir;
import org.crlr.dto.application.base.TypeReglesGroupe;
import org.crlr.dto.application.devoir.ChargeDevoirQO;
import org.crlr.dto.application.devoir.ChargeTravailDTO;
import org.crlr.dto.application.devoir.DevoirDTO;
import org.crlr.dto.application.devoir.RechercheDevoirQO;
import org.crlr.dto.application.devoir.RecherchePieceJointeDevoirQO;
import org.crlr.dto.application.devoir.ResultatRechercheDevoirDTO;
import org.crlr.dto.application.devoir.SaveDevoirQO;
import org.crlr.dto.application.devoir.TypeDevoirDTO;
import org.crlr.dto.application.devoir.TypeDevoirQO;
import org.crlr.dto.application.seance.RechercheSeanceQO;
import org.crlr.exception.metier.MetierException;
import org.crlr.message.ConteneurMessage;
import org.crlr.message.Message;
import org.crlr.message.Message.Nature;
import org.crlr.metier.business.ClasseHibernateBusinessService;
import org.crlr.metier.business.DevoirHibernateBusinessService;
import org.crlr.metier.business.GroupeHibernateBusinessService;
import org.crlr.metier.business.PieceJointeHibernateBusinessService;
import org.crlr.metier.entity.ClasseBean;
import org.crlr.metier.entity.GroupeBean;
import org.crlr.utils.Assert;
import org.crlr.utils.DateUtils;
import org.crlr.web.dto.FileUploadDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Façade concernant les fonctionnalités du module devoir.
 *
 * @author breytond
 * @version $Revision: 1.14 $
 */
@Component
@Transactional(
        readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class
    )
public class DevoirFacade implements DevoirFacadeService {
    /** DOCUMENTATION INCOMPLETE! */
    @Autowired
    private DevoirHibernateBusinessService devoirHibernateBusinessService;

    /** DOCUMENTATION INCOMPLETE! */
    @Autowired
    private GroupeHibernateBusinessService groupeHibernateBusinessService;
    

    @Autowired
    private SequenceFacadeService sequenceFacadeService;
    
    @Autowired
    private SeanceFacadeService seeanceFacadeService;

    /** DOCUMENTATION INCOMPLETE! */
    @Autowired
    private ClasseHibernateBusinessService classeHibernateBusinessService;
    
    @Autowired
    private PieceJointeHibernateBusinessService pieceJointeHibernateBusinessService;

    /**
     * Mutateur devoirHibernateBusinessService.
     *
     * @param devoirHibernateBusinessService devoirHibernateBusinessService
     */
    public void setDevoirHibernateBusinessService(DevoirHibernateBusinessService devoirHibernateBusinessService) {
        this.devoirHibernateBusinessService = devoirHibernateBusinessService;
    }

    /**
     * Mutateur groupeHibernateBusinessService.
     *
     * @param groupeHibernateBusinessService Le groupeHibernateBusinessService à modifier
     */
    public void setGroupeHibernateBusinessService(GroupeHibernateBusinessService groupeHibernateBusinessService) {
        this.groupeHibernateBusinessService = groupeHibernateBusinessService;
    }

    /**
     * Mutateur classeHibernateBusinessService.
     *
     * @param classeHibernateBusinessService Le classeHibernateBusinessService à modifier
     */
    public void setClasseHibernateBusinessService(ClasseHibernateBusinessService classeHibernateBusinessService) {
        this.classeHibernateBusinessService = classeHibernateBusinessService;
    }

    /**
     * Mutateur de pieceJointeHibernateBusinessService.
     * @param pieceJointeHibernateBusinessService le pieceJointeHibernateBusinessService à modifier.
     */
    public void setPieceJointeHibernateBusinessService(
            PieceJointeHibernateBusinessService pieceJointeHibernateBusinessService) {
        this.pieceJointeHibernateBusinessService = pieceJointeHibernateBusinessService;
    }

    /**
     * {@inheritDoc}
     */
    public ResultatDTO<List<TypeDevoirDTO>> findListeTypeDevoir(final Integer idEtablissement) {
        return devoirHibernateBusinessService.findListeTypeDevoir(idEtablissement);
    }

    /**
     * Retourne la liste des devoirs pour un enseignant.
     * @param rechercheDevoirQO rechercheDevoirQO
     * @return La liste des devoirs
     * @throws MetierException Exception
     */
    private ResultatDTO<List<ResultatRechercheDevoirDTO>> listeDevoirAffichageEnseignant(RechercheDevoirQO rechercheDevoirQO)
        throws MetierException {
        Assert.isNotNull("rechercheDevoirQO", rechercheDevoirQO);

        final ConteneurMessage conteneurMessage = new ConteneurMessage();

        final TypeGroupe typeClasseGroupe = rechercheDevoirQO.getTypeGroupeSelectionne();
          
        final Integer idClasseGroupe =
                sequenceFacadeService.findClasseGroupeId(rechercheDevoirQO.getGroupeClasseSelectionne(), null, null);
        
        final boolean existsClasseGroupe = idClasseGroupe != null;
        
        if (TypeGroupe.CLASSE == (typeClasseGroupe)) {
            // CLASSE
            if (existsClasseGroupe) {
                final boolean droitClasse = classeHibernateBusinessService
                        .checkDroitClasse(rechercheDevoirQO.getIdEnseignant(),
                                idClasseGroupe, false, null);
                if (!droitClasse) {
                    conteneurMessage.add(new Message(TypeReglesClasse.CLASSE_01
                            .name(), Nature.BLOQUANT));
                    throw new MetierException(conteneurMessage,
                            "La classe existe mais vous n'avez pas les droits dessus.");

                }
            } else {

                conteneurMessage.add(new Message(TypeReglesClasse.CLASSE_02
                        .name(), Nature.BLOQUANT));
                throw new MetierException(conteneurMessage,
                        "Vous devez sélectionner ou saisir une classe.");
            }

        } else if (TypeGroupe.GROUPE == (typeClasseGroupe)) {
            // GROUPE
            if (existsClasseGroupe) {
                final boolean droitGroupe = groupeHibernateBusinessService
                        .checkDroitGroupe(rechercheDevoirQO.getIdEnseignant(),
                                idClasseGroupe, false, null);
                if (!droitGroupe) {
                    conteneurMessage.add(new Message(TypeReglesGroupe.GROUPE_01
                            .name(), Nature.BLOQUANT));
                    throw new MetierException(conteneurMessage,
                            "Le groupe existe mais vous n'avez pas les droits dessus.");
                }
            } else {
                conteneurMessage.add(new Message(TypeReglesGroupe.GROUPE_02
                        .name(), Nature.BLOQUANT));
                throw new MetierException(conteneurMessage,
                        "Vous devez sélectionner ou saisir un groupe.");
            }
        }
    

        ResultatDTO<List<ResultatRechercheDevoirDTO>> resultat =
                devoirHibernateBusinessService.listeDevoirAffichage(rechercheDevoirQO);
        
        return resultat;
    }
    
    /**
     * Retourne la liste des devoirs pour un inspecteur.
     * @param rechercheDevoirQO rechercheDevoirQO
     * @return La liste des devoirs
     * @throws MetierException Exception
     */
    private ResultatDTO<List<ResultatRechercheDevoirDTO>> listeDevoirAffichageInspecteur(RechercheDevoirQO rechercheDevoirQO)
        throws MetierException {
        Assert.isNotNull("rechercheDevoirQO", rechercheDevoirQO);

        final ConteneurMessage conteneurMessage = new ConteneurMessage();

        final TypeGroupe typeClasseGroupe = rechercheDevoirQO.getTypeGroupeSelectionne();
        
        final Integer idClasseGroupe =
                sequenceFacadeService.findClasseGroupeId(rechercheDevoirQO.getGroupeClasseSelectionne(), null, null);
        
        final boolean existsClasseGroupe = idClasseGroupe != null;
        
        
        if (TypeGroupe.CLASSE  == (typeClasseGroupe)) {
            //CLASSE
            if (existsClasseGroupe) {
                final boolean droitClasse =
                    classeHibernateBusinessService.checkDroitClasseInspecteur(rechercheDevoirQO.getIdInspecteur(),
                                                                    idClasseGroupe, false, null, rechercheDevoirQO.getIdEtablissement());
                if (!droitClasse) {
                    conteneurMessage.add(new Message(TypeReglesClasse.CLASSE_01.name(),
                                                     Nature.BLOQUANT));
                    throw new MetierException(conteneurMessage,
                                              "La classe existe mais vous n'avez pas les droits dessus.");
                }
            } else {
                conteneurMessage.add(new Message(TypeReglesClasse.CLASSE_02.name(),
                                                 Nature.BLOQUANT));
                throw new MetierException(conteneurMessage,
                                          "Vous devez sélectionner ou saisir une classe.");
            }
        } else if (TypeGroupe.GROUPE  == (typeClasseGroupe)) {
            //GROUPE
            if (existsClasseGroupe) {
                final boolean droitGroupe =
                    groupeHibernateBusinessService.checkDroitGroupeInspecteur(rechercheDevoirQO.getIdInspecteur(),
                                                                    idClasseGroupe, false, null, rechercheDevoirQO.getIdEtablissement());
                if (!droitGroupe) {
                    conteneurMessage.add(new Message(TypeReglesGroupe.GROUPE_01.name(),
                                                     Nature.BLOQUANT));
                    throw new MetierException(conteneurMessage,
                                              "Le groupe existe mais vous n'avez pas les droits dessus.");
                }
            } else {
                conteneurMessage.add(new Message(TypeReglesGroupe.GROUPE_02.name(),
                                                 Nature.BLOQUANT));
                throw new MetierException(conteneurMessage,
                                          "Vous devez sélectionner ou saisir un groupe.");
            }
        }
    

        return devoirHibernateBusinessService.listeDevoirAffichage(rechercheDevoirQO);
    }
    

    /**
     * Retourne la liste des devoirs pour un eleve ou une famille.
     * @param rechercheDevoirQO rechercheDevoirQO
     * @return La liste des devoirs
     * @throws MetierException Exception
     */
    public ResultatDTO<List<ResultatRechercheDevoirDTO>> listeDevoirAffichageEleve(RechercheDevoirQO rechercheDevoirQO)
        throws MetierException {
        Assert.isNotNull("rechercheDevoirQO", rechercheDevoirQO);

        final Integer idEleve = rechercheDevoirQO.getIdEleve();
        final List<GroupeBean> listeGroupeBean = groupeHibernateBusinessService.findGroupesEleve(idEleve);
        final List<ClasseBean> listeClasseBean = classeHibernateBusinessService.findClassesEleve(idEleve);

        rechercheDevoirQO.setListeGroupeBean(listeGroupeBean);
        rechercheDevoirQO.setListeClasseBean(listeClasseBean);
        
        return devoirHibernateBusinessService.listeDevoirAffichage(rechercheDevoirQO);
    }

    /**
     * {@inheritDoc}
     */
    public ResultatDTO<List<ResultatRechercheDevoirDTO>> listeDevoirAffichage(RechercheDevoirQO rechercheDevoirQO)
        throws MetierException {
        Assert.isNotNull("rechercheDevoirQO", rechercheDevoirQO);
        
        final Integer idInspecteur = rechercheDevoirQO.getIdInspecteur();
        final Integer idEnseignant = rechercheDevoirQO.getIdEnseignant();
        final Integer idEleve = rechercheDevoirQO.getIdEleve();
        
        final ResultatDTO<List<ResultatRechercheDevoirDTO>> resultat;
        
        if(idInspecteur != null){
            resultat = listeDevoirAffichageInspecteur(rechercheDevoirQO);
        } else if(idEnseignant != null) {
            resultat = listeDevoirAffichageEnseignant(rechercheDevoirQO);
        } else if(idEleve != null) {
            resultat = listeDevoirAffichageEleve(rechercheDevoirQO);
        } else {
            resultat = null;
        }
        
        if (resultat != null) {
            for (ResultatRechercheDevoirDTO devoir : resultat.getValeurDTO()) {
                seeanceFacadeService.mettreDroitsAccess(rechercheDevoirQO.getIdEnseignantConnecte(), devoir.getSeance());
            }
        }
        
        return resultat;
    }
    
    /**
     * {@inheritDoc}
     */
    public ResultatDTO<List<DevoirDTO>> findDevoirForPlanning(
            RechercheSeanceQO rechercheSeanceQO)
        throws MetierException {
        Assert.isNotNull("rechercheSeanceQO", rechercheSeanceQO);
        if (rechercheSeanceQO.getIdEleve() != null){
            final List<ClasseBean> classes = classeHibernateBusinessService.findClassesEleve(rechercheSeanceQO.getIdEleve());
            rechercheSeanceQO.setListeClasseBean(classes);
            final List<GroupeBean> groupes = groupeHibernateBusinessService.findGroupesEleve(rechercheSeanceQO.getIdEleve());
            rechercheSeanceQO.setListeGroupeBean(groupes);
        }
        
        ResultatDTO<List<DevoirDTO>> resultat =
                devoirHibernateBusinessService.findDevoirForPlanning(rechercheSeanceQO);
        
        for (DevoirDTO devoir : resultat.getValeurDTO()) {
            seeanceFacadeService.mettreDroitsAccess(rechercheSeanceQO.getIdEnseignantConnecte(), devoir.getSeance());
        }
        
        return resultat;
        
    }


   

    /**
     * {@inheritDoc}
     */
    public List<FileUploadDTO> findPieceJointeDevoir(RecherchePieceJointeDevoirQO recherchePJDevoir)
                                              throws MetierException {
        return devoirHibernateBusinessService.findPieceJointeDevoir(recherchePJDevoir);
    }
    
    /**
     * {@inheritDoc}
     */
    public void saveTypeDevoir(final TypeDevoirQO typeDevoirQO) throws MetierException {
        Assert.isNotNull("typeDevoirQO", typeDevoirQO);
        
        final String libelle = StringUtils.trimToNull(typeDevoirQO.getLibelle());
        if (libelle == null) {
            final ConteneurMessage cm = new ConteneurMessage();
            cm.add(new Message(TypeReglesAdmin.ADMIN_10.name()));
            throw new MetierException(cm, "Echec de l'ajout ou de la mise à jour d'un type de devoir");
        }
        
        this.devoirHibernateBusinessService.saveTypeDevoir(typeDevoirQO);
    }

    /**
     * {@inheritDoc}
     */
    public void deleteTypeDevoir(Integer idTypeDevoir) throws MetierException {
        this.devoirHibernateBusinessService.deleteTypeDevoir(idTypeDevoir);
    }

    /**
     * {@inheritDoc}
     */
    public DevoirDTO findDetailDevoir(Integer idDevoir) {
        
        final DevoirDTO devoirDTO = this.devoirHibernateBusinessService.findDetailDevoir(idDevoir);
        return devoirDTO;
    }
    
    /**
     * {@inheritDoc}
     */
    public void deleteDevoir(Integer id) throws MetierException {
        //suppression des pièces jointes       
        final List<Integer> listeIdPieceJointeDevoir = pieceJointeHibernateBusinessService.deletePieceJointeDevoir(id);
        
        //suppression definitive de piece jointe
        for (final Integer idPieceJointe : listeIdPieceJointeDevoir) {
            // Avant de supprimer la piece jointe definitivement, on verifie si elle n'est 
            // pas utilisée par un autre élément
            if (!pieceJointeHibernateBusinessService.findUsed(idPieceJointe)) {
                pieceJointeHibernateBusinessService.deletePieceJointe(idPieceJointe);
            }
        }
        devoirHibernateBusinessService.delete(id);
    }

    /**
     * {@inheritDoc}
     */
    public void saveDevoir(final SaveDevoirQO saveDevoirQO) throws MetierException {    
        final DevoirDTO devoirDTO = saveDevoirQO.getDevoirDTO();
        final Date dateFinAnneeSco = saveDevoirQO.getDateFinAnneeScolaire();
        final Date dateSeance = saveDevoirQO.getDateSeance();
        
        Assert.isNotNull("devoirDTO", devoirDTO);
        Assert.isNotNull("dateFinAnneeSco", dateFinAnneeSco);
        Assert.isNotNull("dateSeance", dateSeance);
        
        final Integer idDevoir = devoirDTO.getId();
        Assert.isNotNull("idDevoir", idDevoir);
        
        final Date dateRemise = devoirDTO.getDateRemise();
        final ConteneurMessage cm = new ConteneurMessage();
        
        if (dateRemise == null) {
            //DEVOIR_02
            cm.add(new Message(TypeReglesDevoir.DEVOIR_02.name()));            
        } else if (DateUtils.compare(dateSeance, dateRemise, false) > 0) {
            cm.add(new Message(TypeReglesDevoir.DEVOIR_05.name()));
        } else if (DateUtils.compare(dateRemise, dateFinAnneeSco, false) > 0) {
            cm.add(new Message(TypeReglesDevoir.DEVOIR_06.name()));
        }
        
        if (cm.contientMessageBloquant()) {
            throw new MetierException(cm, "Echec de la sauvegarde du devoir");
        }
        
        //mise à jour du devoir
        devoirHibernateBusinessService.updateDevoir(devoirDTO);
        
        //suppression des pièces jointes puis création      
        final List<Integer> listeIdPieceJointeDevoir =
                pieceJointeHibernateBusinessService.deletePieceJointeDevoir(idDevoir);       
          
        //création des pièces jointes       
        for (final FileUploadDTO fileUploadDTO : devoirDTO.getFiles()) {
            
            fileUploadDTO.setIdDevoir(idDevoir);
            pieceJointeHibernateBusinessService.savePieceJointeDevoir(fileUploadDTO);
            
        }
        
        //suppression definitive de piece jointe
        for (final Integer idPieceJointe : listeIdPieceJointeDevoir) {
            // Avant de supprimer la piece jointe definitivement, on verifie si elle n'est 
            // pas utilisée par un autre élément
            if (!pieceJointeHibernateBusinessService.findUsed(idPieceJointe)) {
                pieceJointeHibernateBusinessService.deletePieceJointe(idPieceJointe);
            }
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public ChargeTravailDTO findChargeTravail(final ChargeDevoirQO chargeDevoir) {
        return devoirHibernateBusinessService.findChargeTravail(chargeDevoir);
    }
    
}
