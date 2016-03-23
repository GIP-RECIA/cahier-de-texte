/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: jalopy.xml,v 1.1 2010/06/15 12:20:58 ent_breyton Exp $
 */

package org.crlr.metier.facade;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.crlr.alimentation.DTO.EnseignantDTO;
import org.crlr.dto.ResultatDTO;
import org.crlr.dto.application.base.ArchiveSeanceDTO;
import org.crlr.dto.application.base.Profil;
import org.crlr.dto.application.base.TypeReglesAcquittement;
import org.crlr.dto.application.base.UtilisateurDTO;
import org.crlr.dto.application.inspection.DroitInspecteurDTO;
import org.crlr.dto.application.inspection.RechercheDroitInspecteurQO;
import org.crlr.dto.application.seance.RechercheVisaSeanceQO;
import org.crlr.dto.application.visa.DateListeVisaSeanceDTO;
import org.crlr.dto.application.visa.RechercheVisaQO;
import org.crlr.dto.application.visa.ResultatRechercheVisaSeanceDTO;
import org.crlr.dto.application.visa.TypeVisa;
import org.crlr.dto.application.visa.VisaDTO;
import org.crlr.dto.application.visa.VisaEnseignantDTO;
import org.crlr.exception.metier.MetierException;
import org.crlr.message.ConteneurMessage;
import org.crlr.message.Message;
import org.crlr.message.Message.Nature;
import org.crlr.metier.business.VisaHibernateBusinessService;
import org.crlr.services.InspectionService;
import org.crlr.utils.PropertiesUtils;
import org.crlr.web.dto.TypeCouleur;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.io.Resources;

/**
 * DOCUMENTATION INCOMPLETE!
 *
 * @author $author$
 * @version $Revision: 1.1 $
  */
@Component
@Transactional(
        readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class
    )
public class VisaFacade implements VisaFacadeService {
    /** DOCUMENTATION INCOMPLETE! */
    @Autowired
    private VisaHibernateBusinessService visaHibernateBusinessService;
    
    @Autowired
    private SeanceFacadeService seanceFacade;
    
    @Autowired
    private InspectionService inspectionService;

    protected final Logger log = LoggerFactory.getLogger(getClass());
    
    /**
     * Renvoie la liste des enseignants autorisé.
     * 
     * Dans le case du profil d'Inspection Academique, on ajoute les
     * enseignants remplacé / absents des les enseignants remplaçants
     * directement autorisés.  
     * 
     * 
     */
    public ResultatDTO<List<EnseignantDTO>> findListeEnseignant(final UtilisateurDTO utilisateurDTO) {

        final Profil profilUser = utilisateurDTO.getProfil();

        final Integer idEtablissement = utilisateurDTO.getIdEtablissement();
       
        List<EnseignantDTO> listeEnseignant = Lists.newArrayList();
        
        // Le directeur voit touts les enseignants de son etablissement 
        if (Profil.DIRECTION_ETABLISSEMENT == profilUser) {
            listeEnseignant = inspectionService.findListeEnseignants(idEtablissement);
            
        } else if (Profil.INSPECTION_ACADEMIQUE == profilUser) {
            
            listeEnseignant=   findListeEnseignantAutoriseInspecteur(utilisateurDTO).getValeurDTO();
            
        } else {
            listeEnseignant = new ArrayList<EnseignantDTO>(); 
        }
        
        ResultatDTO<List<EnseignantDTO>> ret = 
                new ResultatDTO<List<EnseignantDTO>>();
        ret.setValeurDTO(listeEnseignant);
        return ret;
    }
    
    public ResultatDTO<List<EnseignantDTO>> findListeEnseignantAutoriseInspecteur(final UtilisateurDTO utilisateurDTO) 
    {
        List<EnseignantDTO> listeEnseignant = new ArrayList<EnseignantDTO>();    
        final RechercheDroitInspecteurQO rechercheDroitInspecteurQO = new RechercheDroitInspecteurQO();
        rechercheDroitInspecteurQO.setIdEtablissement(utilisateurDTO.getIdEtablissement());
        rechercheDroitInspecteurQO.setIdInspecteur(utilisateurDTO.getUserDTO().getIdentifiant());
        rechercheDroitInspecteurQO.setVraiOuFauxRechercheFromDirecteur(false);
        
        final ResultatDTO<List<DroitInspecteurDTO>> droitsInspecteurs = inspectionService.findDroitsInspection(rechercheDroitInspecteurQO);
        final List<DroitInspecteurDTO> listeDroit = droitsInspecteurs.getValeurDTO();
        for (final DroitInspecteurDTO droit : listeDroit) {
            listeEnseignant.add(droit.getEnseignant());
        }
        
        ResultatDTO<List<EnseignantDTO>> ret = 
                new ResultatDTO<List<EnseignantDTO>>();
        ret.setValeurDTO(listeEnseignant);
        return ret;
    }
    /**
     * {@inheritDoc}
     */
    public ResultatDTO<List<VisaEnseignantDTO>> findListeVisaEnseignant(final Profil profilUser, 
            Integer idEtablissement, final List<EnseignantDTO> listeEnseignant) {
        
        List<EnseignantDTO> listeReel = Lists.newArrayList();
        listeReel.addAll(listeEnseignant);
        
       
        
        ResultatDTO<List<VisaEnseignantDTO>> resultat =
                visaHibernateBusinessService.findListeVisaEnseignant(listeReel);
        return resultat;
        
    }
    
    /**
     * {@inheritDoc}
     */
    public ResultatDTO<List<VisaDTO>> findListeVisa(final RechercheVisaQO rechercheVisa) {
        return visaHibernateBusinessService.findListeVisa(rechercheVisa);
    }
    
    /**
     * {@inheritDoc}
     */
    public ResultatDTO<Boolean> saveListeVisa(final Collection<VisaDTO> listeVisa) throws MetierException {

        final ConteneurMessage conteneurMessage = new ConteneurMessage();
        final ResultatDTO<Boolean> resultatDTO = new ResultatDTO<Boolean>();
        resultatDTO.setValeurDTO(false);
        
        log.debug("saveListeVisa");
        
        // Traite la sauvegarde de tous les visa de la liste
        for (final VisaDTO visa : listeVisa) {
            
            log.debug("saveListeVisa visa: {}", visa);
            
            /*
            if (null != visa.getTypeVisa() && null == visa.getDateVisa()) {
                conteneurMessage.add(new Message(Message.Code.ERREUR_INCONNUE.getId(),
                        Nature.BLOQUANT));
                throw new MetierException(conteneurMessage,
                        "Une visa crée sans une date de création");
                
            }*/
            if (null == visa.getIdVisa()) {
                conteneurMessage.add(new Message(Message.Code.ERREUR_INCONNUE.getId(),
                        Nature.BLOQUANT));
                throw new MetierException(conteneurMessage,
                        "Une erreur interne");
            }
        
            // Supprime les eventuelle seances archive précédentes
            visaHibernateBusinessService.supprimerArchiveVisa(visa);
            
            // Si MEMO, archive les seances correspondante au visa
            if (TypeVisa.MEMO == visa.getTypeVisa()) {
                visaHibernateBusinessService.archiverVisa(visa);
            }
            
            // Persiste l'objet VISA
            visaHibernateBusinessService.saveVisa(visa);
        }
        if (listeVisa.size()>0) {
            conteneurMessage.add(new Message(TypeReglesAcquittement.ACQUITTEMENT_01.name(),
                    Nature.INFORMATIF, "Tous les modifications effectuées sur les visas", "enregistrées"));
        } else {
            conteneurMessage.add(new Message(TypeReglesAcquittement.ACQUITTEMENT_02.name(),
                    Nature.INFORMATIF, "Aucune modification", "enregistrée"));
        }
        resultatDTO.setConteneurMessage(conteneurMessage);
        resultatDTO.setValeurDTO(true);
        return resultatDTO;
    }

    /**
     * {@inheritDoc}
     */
    public ResultatDTO<List<DateListeVisaSeanceDTO>> findVisaSeance(RechercheVisaSeanceQO rechercheSeanceQO) 
        throws MetierException {
        
        ResultatDTO<List<DateListeVisaSeanceDTO>> resultat = 
                visaHibernateBusinessService.findVisaSeance(rechercheSeanceQO);
        
        Map<Integer, TypeCouleur> sequenceCouleurMap = new HashMap<Integer, TypeCouleur>();
        
        List<TypeCouleur> listeCouleur = new ArrayList<TypeCouleur>(Arrays.asList(TypeCouleur.values()));
        
        int listeCouleurIndex = 0;
        
       /* List<EnseignantDTO> listeEnseigantsAutorises = null;
        if (rechercheSeanceQO.getProfil() == Profil.INSPECTION_ACADEMIQUE) {
            
            listeEnseigantsAutorises = findListeEnseignantAutoriseInspecteur(rechercheSeanceQO.getUtilisateurConnecte()).getValeurDTO();
        }*/
        
        ListIterator<DateListeVisaSeanceDTO> li = resultat.getValeurDTO().listIterator();
        
        while(li.hasNext()) {
            DateListeVisaSeanceDTO dlvSeance = li.next();
        
            ListIterator<ResultatRechercheVisaSeanceDTO> liSeance = dlvSeance.getListeVisaSeance().listIterator();
            
            boolean auMoinsUnSeance = false;
            
            while(liSeance.hasNext()) {
                
                ResultatRechercheVisaSeanceDTO seanceDTO = liSeance.next();
                
                seanceFacade.completerInfoSeance(seanceDTO,  false);
                
                /**
                 * Une vérification si l'inspecteur a le droit de regarder la séance
                 * 
                 * Il est nécessaire due a la possibilité que la seance était créer par une remplaçant.
                 */
                /*
                if (rechercheSeanceQO.getProfil() == Profil.INSPECTION_ACADEMIQUE) {
                    boolean isAutorise = false;
                    
                    for(EnseignantDTO enseignantAutorise : listeEnseigantsAutorises) {
                        if (seanceDTO.getIdEnseignant().equals(enseignantAutorise.getId())) {
                            isAutorise = true;
                            break;
                        }
                    }
                    
                    if (!isAutorise) {
                        liSeance.remove();
                        continue;
                    }
                }*/
                
                
                
                //Si nous ne sommes pas en mode remplaçant, id enseignant dans la séance doit être égale à l'id de enseignant dans la séquence
              //Nous ne voulons que les séances qui corréspondent au enseignant choisi, par ceux saisie par les remplaçants
                if (rechercheSeanceQO.getIdEnseignant() != null && //id cahier de texte / id 
                        rechercheSeanceQO.getIdEnseignantRemplacant() == null &&
                        !seanceDTO.getIdEnseignant().equals(rechercheSeanceQO.getIdEnseignant()) //id enseignant dans la séance
                        ) {
                    liSeance.remove();
                    continue;
                }
                
                //En mode remplaçement, nous ne voulons que les séances saisies par le remplaçeant
                if (rechercheSeanceQO.getIdEnseignantRemplacant() != null && 
                        !seanceDTO.getIdEnseignant().equals(rechercheSeanceQO.getIdEnseignantRemplacant())) {
                    liSeance.remove();
                    continue;
                }
                
                auMoinsUnSeance = true;
                
                Integer sequenceId = seanceDTO.getSequenceDTO().getId();
                if (!sequenceCouleurMap.containsKey(sequenceId)) {
                    listeCouleurIndex++;
                    if (listeCouleurIndex >= listeCouleur.size()) {
                        listeCouleurIndex = 0;
                    }
                    sequenceCouleurMap.put(sequenceId, listeCouleur.get(listeCouleurIndex));                    
                }
                
                seanceDTO.setTypeCouleur(sequenceCouleurMap.get(sequenceId));
            }
            
            //Si il n'y a pas de séance, on enléve la dateSeanceVisa DTO
            if (!auMoinsUnSeance) {
                li.remove();
            }
        }
        
        return resultat;
    }
    
    /**
     * {@inheritDoc}
     * @return null si il n'y a pas d'archive correspondant
     */
    public ResultatDTO<ArchiveSeanceDTO> findArchiveSeance(final Integer idSeance, final Integer idVisa) throws MetierException {
        ResultatDTO<ArchiveSeanceDTO> resultat = visaHibernateBusinessService.findArchiveSeance(idSeance, idVisa);
        
        if (resultat.getValeurDTO() != null) {
            seanceFacade.completerInfoSeance(resultat.getValeurDTO(), true);
        }
        
        return resultat;
    }
    
    /**
     * {@inheritDoc}
     */
    public ResultatDTO<Boolean> saveListeVisaSeance(final List<ResultatRechercheVisaSeanceDTO> listeVisaSeance) throws MetierException {

        final ConteneurMessage conteneurMessage = new ConteneurMessage();
        final ResultatDTO<Boolean> resultatDTO = new ResultatDTO<Boolean>();
        resultatDTO.setValeurDTO(false);
        
        
        
        Map<Integer, VisaDTO> visaASauvegarder = new HashMap<Integer, VisaDTO>();
        
        for(ResultatRechercheVisaSeanceDTO visaSeance : listeVisaSeance) {
            Profil profil = visaSeance.getProfil();
            if (profil == null) {
                ConteneurMessage cm = new ConteneurMessage();
                cm.add(new Message(Message.Code.ERREUR_INCONNUE.getId(), Nature.BLOQUANT));
                throw new MetierException(cm, "Erreur interne");
            }
            
            VisaDTO visa = profil == Profil.DIRECTION_ETABLISSEMENT ? visaSeance.getVisaDirecteur() : visaSeance.getVisaInspecteur();
            
            // Gestion du cas des remplacements : 
            // idEnseignant sur la seance correspond a l'enseignant createur de la seance (le remplacant)
            // Qui peut etre different de idEnseignant de la sequence  (le remplacé)
            if (visaSeance.getIdEnseignant() != null && !visaSeance.getIdEnseignant().equals(visaSeance.getSequenceDTO().getIdEnseignant())) {
                visa.setIdEnseignantVisa(visaSeance.getIdEnseignant());
            }
            visaASauvegarder.put(visa.getIdVisa(), visa);
        }
        
        saveListeVisa(visaASauvegarder.values());
        
        
        if (listeVisaSeance.size()>0) {
            conteneurMessage.add(new Message(TypeReglesAcquittement.ACQUITTEMENT_01.name(),
                    Nature.INFORMATIF, "Tous les modifications effectuées sur les visas", "enregistrées"));
        } else {
            
            conteneurMessage.add(new Message(TypeReglesAcquittement.ACQUITTEMENT_02.name(),
                    Nature.INFORMATIF, "Aucune modification", "enregistrée"));
        }
        resultatDTO.setConteneurMessage(conteneurMessage);
        resultatDTO.setValeurDTO(true);
        return resultatDTO;
    }

   
    /** Texte d'aide HTML affiche dans la popup d'aide de l'écran visaListe .*/
    private String aideContextuelleListe;
    
    /** Texte d'aide HTML affiche dans la popup d'aide de l'écran visaSeance .*/
    private String aideContextuelleSeance;

    
    
    
    /**
     * Accesseur de aideContextuelleListe {@link #aideContextuelleListe}.
     * @return retourne aideContextuelleListe
     */
    public String getAideContextuelleListe() {
        if (!StringUtils.isEmpty(aideContextuelleListe)){
            return aideContextuelleListe;
        }
        
        URL url = Resources.getResource("/visaListAide.html");
        try {
            String aide = Resources.toString(url, Charsets.UTF_8);
            aideContextuelleListe = aide;
        } catch (IOException ex) {
            log.warn("ex", ex);
        }
            
        return aideContextuelleListe;
    }

    /**
     * Accesseur de aideContextuelleSeance {@link #aideContextuelleSeance}.
     * @return retourne aideContextuelleSeance
     */
    public String getAideContextuelleSeance() {
        if (StringUtils.isEmpty(aideContextuelleSeance)){
            final Properties properties= PropertiesUtils.load("/aideContextuelle.properties");
            aideContextuelleSeance =properties.getProperty("visaSeance.aide"); 
        }
        return aideContextuelleSeance;
    }
    
    
}

