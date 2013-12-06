/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: jalopy.xml,v 1.1 2010/06/15 12:20:58 ent_breyton Exp $
 */

package org.crlr.metier.facade;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.crlr.dto.ResultatDTO;
import org.crlr.dto.application.admin.GenerateurDTO;
import org.crlr.dto.application.base.EtablissementComplementDTO;
import org.crlr.dto.application.base.EtablissementDTO;
import org.crlr.dto.application.base.GenericDetailDTO;
import org.crlr.dto.application.base.OuvertureQO;
import org.crlr.dto.application.base.TypeReglesAcquittement;
import org.crlr.dto.application.base.TypeReglesAdmin;
import org.crlr.exception.metier.MetierException;
import org.crlr.message.ConteneurMessage;
import org.crlr.message.Message;
import org.crlr.message.Message.Nature;
import org.crlr.metier.business.EtablissementHibernateBusinessService;
import org.crlr.utils.DateUtils;
import org.crlr.web.dto.GrilleHoraireDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * EtablissementFacade.
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
@Component
@Transactional(
        readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class
    )
public class EtablissementFacade implements EtablissementFacadeService {
    /** DOCUMENTATION INCOMPLETE! */
    @Autowired
    private EtablissementHibernateBusinessService etablissementHibernateBusinessService;

    /**
     * Mutateur de etablissementHibernateBusinessService.
     *
     * @param etablissementHibernateBusinessService le
     *        etablissementHibernateBusinessService à modifier.
     */
    public void setEtablissementHibernateBusinessService(EtablissementHibernateBusinessService etablissementHibernateBusinessService) {
        this.etablissementHibernateBusinessService = etablissementHibernateBusinessService;
    }

    /**
     * {@inheritDoc}
     */
    public ResultatDTO<Integer> saveEtablissementJoursOuvres(EtablissementDTO etablissementQO)
        throws MetierException {
        return etablissementHibernateBusinessService.saveEtablissementJoursOuvres(etablissementQO);
    }

    /**
     * {@inheritDoc}
     */
    public EtablissementComplementDTO findDonneeComplementaireEtablissement(Integer id) {
        return etablissementHibernateBusinessService.findDonneeComplementaireEtablissement(id);
    }

    
    private static class GrilleHoraireTemps implements Comparable<GrilleHoraireTemps> {
        Integer temps;
        GrilleHoraireDTO grille;
        /* (non-Javadoc)
         * @see java.lang.Comparable#compareTo(java.lang.Object)
         */
        @Override
        public int compareTo(GrilleHoraireTemps o) {
            return temps.compareTo(o.temps);
        }
        
        /**
         * @param grille g
         * @param debut d
         */
        public GrilleHoraireTemps(GrilleHoraireDTO grille, boolean debut) {
            this.temps = debut ? (grille.getHeureDebut() * 60 + grille.getMinuteDebut()) :
                (grille.getHeureFin() * 60 + grille.getMinuteFin())
                ;
            this.grille = grille;
        }
        
    }
    
    
    /**
     * @param conteneurMessage c
     * @param listeGrille l
     */
    public static void verifierGrilleHoraireDebutFin(
            ConteneurMessage conteneurMessage,
            List<GrilleHoraireDTO> listeGrille) {
        for (GrilleHoraireDTO grille : listeGrille) {
            if (grille.getFinMinutes() <= grille.getDebutMinutes()) {
                conteneurMessage.add(new Message(TypeReglesAdmin.ADMIN_23
                        .name(), Nature.BLOQUANT, DateUtils.formatTime(
                        grille.getHeureDebut(), grille.getMinuteDebut()),
                        DateUtils.formatTime(grille.getHeureFin(),
                                grille.getMinuteFin())));
            }
        }
    }
    
    /**
     * @param conteneurMessage c
     * @param listeGrille l 
     * @param etablissement e
     */
    private static void verifierGrilleHoraireHorairesEtablissement(ConteneurMessage conteneurMessage, 
            List<GrilleHoraireDTO> listeGrille, EtablissementDTO etablissement) {
        
        for (GrilleHoraireDTO grille : listeGrille) {
            
        
        boolean horsHeuresEtab = false;
        
        if ((etablissement.getHeureDebut() * 60 + etablissement
                .getMinuteDebut()) > grille.getDebutMinutes()) {
            horsHeuresEtab = true;
        }
        
        if ( ( (etablissement.getHeureFin() + 1) * 60 ) <
            grille.getFinMinutes()) {
            horsHeuresEtab = true;
        }
        
        if (horsHeuresEtab) {
            conteneurMessage.add(new Message(TypeReglesAdmin.ADMIN_24
                    .name(), Nature.BLOQUANT, 
                    DateUtils.formatTime(grille.getHeureDebut(), grille.getMinuteDebut()),
                    DateUtils.formatTime(grille.getHeureFin(),   grille.getMinuteFin()),
                    DateUtils.formatTime(etablissement.getHeureDebut(), etablissement.getMinuteDebut()),
                    DateUtils.formatTime(etablissement.getHeureFin() + 1, 0)
                    ));
            continue;
        }
        
        }
    }
    
    /**
     * @param conteneurMessage c
     * @param grille g 
     */
    private static void verifierGrilleHoraireChevauchement(
            ConteneurMessage conteneurMessage, List<GrilleHoraireDTO> grille

    ) {
        
        List<GrilleHoraireTemps> listeTemps = new ArrayList<EtablissementFacade.GrilleHoraireTemps>();
        
        for(GrilleHoraireDTO grilleObj : grille) {
            listeTemps.add(new GrilleHoraireTemps(grilleObj, true));
            listeTemps.add(new GrilleHoraireTemps(grilleObj, false));
        }
        
        Collections.sort(listeTemps);
        
        int i = 0;
        
        /*
         * L'idée est de trier les debuts et les fins de la liste de grille horaire afin de trouver
         * si il y a un incohérence.  Si la liste est OK, la liste trié va être 
         * 
         * debut grille 0
         * fin grille 0
         * debut grille 1
         * fin grille 1
         * debut grille 2
         * fin grille 2
         * 
         * si c'est pas le cas, il y a un chevauchement comme 
         * 
         * debut grille 0
         * fin grille 1
         * debut grille 1
         * fin grille 0
         * debut grille 2
         * fin grille 2
         */
        
        while ((i + 1) < listeTemps.size()) {
            
            GrilleHoraireTemps tempsA = listeTemps.get(i);
            GrilleHoraireDTO grilleA = listeTemps.get(i).grille;

            GrilleHoraireTemps tempsB = listeTemps.get(i+1);
            GrilleHoraireDTO grilleB = tempsB.grille;
            
            i += 2;

            
            if (grilleA.getTitre().equals(grilleB.getTitre())) {
                // cas normal, grilleA == grilleB
                continue;
            } else if (tempsA.temps == grilleA.getFinMinutes() && tempsB.temps == grilleB.getDebutMinutes()) {
                //Pas de chevauchement
                continue;
            } else {
                //Cas de chevauchement
                conteneurMessage.add(new Message(TypeReglesAdmin.ADMIN_22
                        .name(), Nature.BLOQUANT, 
                        DateUtils.formatTime(grilleA.getHeureDebut(), grilleA.getMinuteDebut()),
                        DateUtils.formatTime(grilleA.getHeureFin(),   grilleA.getMinuteFin()),
                        DateUtils.formatTime(grilleB.getHeureDebut(), grilleB.getMinuteDebut()),
                        DateUtils.formatTime(grilleB.getHeureFin(),   grilleB.getMinuteFin())
                        ));                
            }
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public ResultatDTO<Integer> saveComplementEtablissement(EtablissementDTO etablissementQO)
        throws MetierException {
        
        /*
         * ADMIN_22 = La plage du {0} au {1} chevauche celle du {2} au {3}.
ADMIN_23 = La plage du {0} au {1} n'est pas valide.  Le début doit être postérieur à la fin.   
ADMIN_24 = La plage du {0} au {1} n'est pas comprise dans l'heure de début et l'heure de fin de l'établissement.
         */
        final ConteneurMessage conteneurMessage = new ConteneurMessage();
        
        if (null == etablissementQO.getFractionnement()) {
            conteneurMessage.add(new Message(TypeReglesAcquittement.ACQUITTEMENT_02.name(),
                    Nature.BLOQUANT, "Jours de fractionnement"));
        }
        
        
        List<GrilleHoraireDTO> listeGrilleHoraire = 
                GenerateurDTO.generateGrilleHoraireFromDb(etablissementQO.getHoraireCours());
        
        verifierGrilleHoraireDebutFin(conteneurMessage, listeGrilleHoraire);
        
        if (conteneurMessage.contientMessageBloquant()) {
            throw new MetierException(conteneurMessage, "Erreur pendant sauvegarde de l'établissement");
        }
        
        verifierGrilleHoraireHorairesEtablissement(conteneurMessage, listeGrilleHoraire, etablissementQO);
        verifierGrilleHoraireChevauchement(conteneurMessage, listeGrilleHoraire);

        if (conteneurMessage.contientMessageBloquant()) {
            throw new MetierException(conteneurMessage, "Erreur pendant sauvegarde de l'établissement");
        }
        
        ResultatDTO<Integer> resultat = etablissementHibernateBusinessService.saveComplementEtablissement(etablissementQO);
        resultat.setConteneurMessage(conteneurMessage);
        
        
        
        return resultat;
    }

    /**
     * {@inheritDoc}
     */
    public String findAlternanceSemaine(Integer idEtablissement) {
        return etablissementHibernateBusinessService.findAlternanceSemaine(idEtablissement);
    }

    /**
     * {@inheritDoc}
     */
    public ResultatDTO<Integer> saveEtablissementAlternance(EtablissementDTO etablissementQO)
        throws MetierException {
        return etablissementHibernateBusinessService.saveEtablissementAlternance(etablissementQO);
    }

    /**
     * {@inheritDoc}
     */
    public ResultatDTO<Integer> saveEtablissementOuverture(OuvertureQO ouvertureQO)
        throws MetierException {
        return etablissementHibernateBusinessService.saveEtablissementOuverture(ouvertureQO);
    }

    /**
     * {@inheritDoc}
     */
    public ResultatDTO<String> findHorairesCoursEtablissement(final Integer id)
        throws MetierException {
        return etablissementHibernateBusinessService.findHorairesCoursEtablissement(id);
    }

    /**
     * {@inheritDoc}
     */
    public String findJourOuvreEtablissementParCode(final String siren,
                                                    final Boolean archive,
                                                    final String exercice) {
        final GenericDetailDTO<Integer, String, String, Boolean> result = 
            etablissementHibernateBusinessService.findIdDescJourOuvreEtablissementParCode(siren,
                archive,
                exercice);
        
        if (result != null) {
            return result.getValeur3();
        } else {
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    public Integer findIdEtablissementParCode(final String siren,
                                             final Boolean archive,
                                             final String exercice) {
        final GenericDetailDTO<Integer, String, String, Boolean> result = 
            etablissementHibernateBusinessService.findIdDescJourOuvreEtablissementParCode(siren,
                archive,
                exercice);
        
        if (result != null) {
            return result.getValeur1();
        } else {
            return null;
        }
    }
    
    /**
     * 
     * {@inheritDoc}
     */
    public ResultatDTO<EtablissementDTO> findEtablissement(final Integer id) {
            ResultatDTO<EtablissementDTO> result = new ResultatDTO<EtablissementDTO>();
            result.setValeurDTO( etablissementHibernateBusinessService.findEtablissement(id) );
            
            return result;
        }
    
    public ResultatDTO<Boolean> checkSaisieSimplifieeEtablissement(final Integer idEtablissement,
            final Integer idEnseignant) {
        ResultatDTO<Boolean> result = new ResultatDTO<Boolean>();
        result.setValeurDTO(etablissementHibernateBusinessService.checkSaisieSimplifieeEtablissement(idEtablissement, idEnseignant));
        return result;
    }
}
