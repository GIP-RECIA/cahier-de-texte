/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: ConfidentialiteFacade.java,v 1.16 2010/05/20 08:24:50 ent_breyton Exp $
 */

package org.crlr.metier.facade;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.crlr.alimentation.Archive;
import org.crlr.dto.Environnement;
import org.crlr.dto.ResultatDTO;
import org.crlr.dto.UserDTO;
import org.crlr.dto.application.base.AnneeScolaireDTO;
import org.crlr.dto.application.base.CheckSaisieSimplifieeQO;
import org.crlr.dto.application.base.ElevesParentDTO;
import org.crlr.dto.application.base.EtablissementAccessibleQO;
import org.crlr.dto.application.base.EtablissementDTO;
import org.crlr.dto.application.base.GenericDetailDTO;
import org.crlr.dto.application.base.Profil;
import org.crlr.dto.application.base.UtilisateurDTO;
import org.crlr.dto.application.sequence.SaveSequenceSimplifieeQO;
import org.crlr.dto.securite.AuthentificationQO;
import org.crlr.dto.securite.TypeAuthentification;
import org.crlr.dto.securite.TypeReglesSecurite;
import org.crlr.exception.metier.MetierException;
import org.crlr.exception.metier.MetierRuntimeException;
import org.crlr.log.Log;
import org.crlr.log.LogFactory;
import org.crlr.message.ConteneurMessage;
import org.crlr.message.Message;
import org.crlr.metier.business.AnneeScolaireHibernateBusinessService;
import org.crlr.metier.business.EleveHibernateBusinessService;
import org.crlr.metier.business.EnseignantHibernateBusinessService;
import org.crlr.metier.business.EtablissementHibernateBusinessService;
import org.crlr.metier.business.InspectionHibernateBusinessService;
import org.crlr.metier.business.LdapBusinessService;
import org.crlr.metier.business.PreferencesHibernateBusinessService;
import org.crlr.metier.business.SchemaHibernateBusinessService;
import org.crlr.metier.entity.EleveBean;
import org.crlr.utils.Assert;
import org.crlr.utils.BooleanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.crlr.utils.ComparateurUtils;
import org.apache.commons.lang.StringUtils;
import org.crlr.utils.TriComparateur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Façade concernant les fonctionnalités d'authentification.
 * 
 * @author breytond.
 * @version $Revision: 1.16 $
 */
@Component
@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class ConfidentialiteFacade implements ConfidentialiteFacadeService {

    protected final Log                           log = LogFactory.getLog( getClass() );

    /** Métier de élève. */
    private EleveHibernateBusinessService         eleveHibernateBusinessService;

    /** Métier d'établissement. */
    private EtablissementHibernateBusinessService etablissementHibernateBusinessService;

    /** Métier d'enseigant. */
    private EnseignantHibernateBusinessService    enseignantHibernateBusinessService;

    /** Métier inspecteur. */
    private InspectionHibernateBusinessService    inspectionHibernateBusinessService;

    /** Métier des préférences. */
    private PreferencesHibernateBusinessService   preferencesHibernateBusinessService;

    /** Service d'annuaire. */
    private LdapBusinessService                   ldapBusinessService;

    /** Service d'année scolaire. */
    private AnneeScolaireHibernateBusinessService anneeScolaireHibernateBusinessService;

    /** Service de schéma de la base de données. */
    private SchemaHibernateBusinessService        schemaHibernateBusinessService;

    /** facade de séquence. */
    private SequenceFacadeService                 sequenceFacadeService;

    /**
     * Injection du service.
     * 
     * @param schemaHibernateBusinessService
     *            le schemaHibernateBusinessService à modifier.
     */
    @Autowired
    public void setSchemaHibernateBusinessService(SchemaHibernateBusinessService schemaHibernateBusinessService) {
        this.schemaHibernateBusinessService = schemaHibernateBusinessService;
    }

    /**
     * Injection du service.
     * 
     * @param eleveHibernateBusinessService
     *            userHibernateBusinessService.
     */
    @Autowired
    public void setEleveHibernateBusinessService(EleveHibernateBusinessService eleveHibernateBusinessService) {
        this.eleveHibernateBusinessService = eleveHibernateBusinessService;
    }

    /**
     * Injection du service.
     * 
     * @param inspectionHibernateBusinessService
     *            inspectionHibernateBusinessService.
     */
    @Autowired
    public void setInspectionHibernateBusinessService(InspectionHibernateBusinessService inspectionHibernateBusinessService) {
        this.inspectionHibernateBusinessService = inspectionHibernateBusinessService;
    }

    /**
     * Injection du service.
     * 
     * @param enseignantHibernateBusinessService
     *            the enseignantHibernateBusinessService to set
     */
    @Autowired
    public void setEnseignantHibernateBusinessService(EnseignantHibernateBusinessService enseignantHibernateBusinessService) {
        this.enseignantHibernateBusinessService = enseignantHibernateBusinessService;
    }

    /**
     * Mutateur de preferencesHibernateBusinessService.
     * 
     * @param preferencesHibernateBusinessService
     *            le preferencesHibernateBusinessService à modifier.
     */
    @Autowired
    public void setPreferencesHibernateBusinessService(PreferencesHibernateBusinessService preferencesHibernateBusinessService) {
        this.preferencesHibernateBusinessService = preferencesHibernateBusinessService;
    }

    /**
     * Injection du service.
     * 
     * @param etablissementHibernateBusinessService
     *            etablissementHibernateBusinessService.
     */
    @Autowired
    public void setEtablissementHibernateBusinessService(EtablissementHibernateBusinessService etablissementHibernateBusinessService) {
        this.etablissementHibernateBusinessService = etablissementHibernateBusinessService;
    }

    /**
     * Injection du service.
     * 
     * @param ldapBusinessService
     *            ldapBusinessService.
     */
    @Autowired
    public void setLdapBusinessService(LdapBusinessService ldapBusinessService) {
        this.ldapBusinessService = ldapBusinessService;
    }

    /**
     * Injection du service.
     * 
     * @param anneeScolaireHibernateBusinessService
     *            the anneeScolaireHibernateBusinessService to set
     */
    @Autowired
    public void setAnneeScolaireHibernateBusinessService(AnneeScolaireHibernateBusinessService anneeScolaireHibernateBusinessService) {
        this.anneeScolaireHibernateBusinessService = anneeScolaireHibernateBusinessService;
    }

    /**
     * Injection de la facade.
     * 
     * @param sequenceFacadeService
     *            the sequenceFacadeService to set
     */
    @Autowired
    public void setSequenceFacadeService(SequenceFacadeService sequenceFacadeService) {
        this.sequenceFacadeService = sequenceFacadeService;
    }

    /**
     * {@inheritDoc}
     */
    public ResultatDTO<UtilisateurDTO> initialisationAuthentification(final AuthentificationQO authentificationQO) throws MetierException {

        final TypeAuthentification typeAuthentification = authentificationQO.getTypeAuthentification();
        Assert.isNotNull( "typeAuthentification", typeAuthentification );
        final ConteneurMessage conteneurMessage = new ConteneurMessage();
        final String identifiant = StringUtils.trimToNull( authentificationQO.getIdentifiant() );

        final Environnement environnement = authentificationQO.getEnvironnement();
        Assert.isNotNull( "environnement", environnement );

        Boolean vraiOuFauxEnvCRLR = Environnement.CRLR.equals( environnement );

        final UtilisateurDTO utilisateurDTO;
        try {
            switch (typeAuthentification) {
            case CAS:
                Assert.isNotNull( "uid cas", identifiant );
                // 1 Requête LDAP de recherche de l'individu en fonction de son
                // uid.
                utilisateurDTO = ldapBusinessService.getUser( identifiant, environnement, authentificationQO.getMapProfil(),
                        authentificationQO.getGroupsADMCentral(), authentificationQO.getRegexpAdmLocal() );
            break;
            case LDAP:
                if (identifiant == null) {
                    conteneurMessage.add( new Message( TypeReglesSecurite.SECU_01.name() ) );
                }
                final String password = StringUtils.trimToNull( authentificationQO.getPassword() );
                if (password == null) {
                    conteneurMessage.add( new Message( TypeReglesSecurite.SECU_02.name() ) );
                }

                if (conteneurMessage.contientMessageBloquant()) {
                    throw new MetierException( conteneurMessage, "Erreur avant l'authentification LDAP." );
                }
                // 1 Requête LDAP de recherche de l'individu en fonction de son
                // login et mot de passe.
                utilisateurDTO = ldapBusinessService.getUser( identifiant, password );
            break;
            default:
                throw new MetierRuntimeException( null, "Le type d'authentification n'existe pas." );
            }
        } catch (Exception e) {
            log.error( e, "Exception" );
            conteneurMessage.add( new Message( TypeReglesSecurite.SECU_04.name(), identifiant ) );
            throw new MetierRuntimeException( conteneurMessage, "Erreur durant l'authentification LDAP." );
        }

        final UserDTO userDTO = utilisateurDTO.getUserDTO();

        final String uid = userDTO.getUid();

        // Dans l'environement CRC, les sirens ne sont pas disponibles
        // directement.
        // Un second appel LDAP permet de trouver les sirens.
        if (!vraiOuFauxEnvCRLR) {
            log.debug( "Traitement SIREN-UAI" );
            final Set<String> uais = new HashSet<String>();
            uais.addAll( utilisateurDTO.getSirensEtablissement() );
            uais.add( utilisateurDTO.getSirenEtablissement() );
            uais.addAll( utilisateurDTO.getAdminLocalSiren() );
            final Map<String, String> mapUAISiren = ldapBusinessService.findSirenByUAI( uais );
            utilisateurDTO.setSirenEtablissement( mapUAISiren.get( utilisateurDTO.getSirenEtablissement() ) );
            final Set<String> sirens = new HashSet<String>();
            for (String uai : utilisateurDTO.getSirensEtablissement()) {
                sirens.add( mapUAISiren.get( uai ) );
            }
            utilisateurDTO.setSirensEtablissement( sirens );
            final Set<String> sirensAdmin = new HashSet<String>();
            for (String uai : utilisateurDTO.getAdminLocalSiren()) {
                sirensAdmin.add( mapUAISiren.get( uai ) );
            }
            utilisateurDTO.setAdminLocalSiren( sirensAdmin );

            // utilisateurDTO.setAdminLocalSiren(sirensAdmin);
            log.debug( "Fin du traitement SIREN-UAI" );
        }

        // En fonction du profil de l'utilisateur on va contrôler l'existence en
        // bdd et le créer.
        final Profil profilIndividu = utilisateurDTO.getProfil();

        // gestion de la draaf et du rectorat
        if (!etablissementHibernateBusinessService.etablissementExist( utilisateurDTO.getSirenEtablissement() )) {
            utilisateurDTO.setSirenEtablissement( null );
            utilisateurDTO.setSirensEtablissement( new HashSet<String>() );
            if (utilisateurDTO.getAdminLocalSiren().size() > 0) {
                utilisateurDTO.getSirensEtablissement().addAll( utilisateurDTO.getAdminLocalSiren() );
            }
            if (utilisateurDTO.getAdminRessourceSiren().size() > 0) {
                utilisateurDTO.getSirensEtablissement().addAll( utilisateurDTO.getAdminRessourceSiren() );
            }
            if (utilisateurDTO.getSirensEtablissement().size() > 0 && utilisateurDTO.getSirenEtablissement() == null) {
                utilisateurDTO.setSirenEtablissement( utilisateurDTO.getSirensEtablissement().iterator().next() );
            }
        }
        // fin gestion de la draaf et du rectorat

        // Identifiant de l'individu en base de données.
        Integer id = null;
        log.debug( "Profil utilisateur : {0}", profilIndividu.toString() );
        switch (profilIndividu) {
        case PARENT:
            if (!vraiOuFauxEnvCRLR) {
                // Seuls les enfants qui ont le parent pour responsable legal
                // doivent être utilisés dans le cahier de texte du CRC
                Set<String> uidsEnfantsUtilisablesCDT = ldapBusinessService.getAutoriteParentale( identifiant,utilisateurDTO.getListeUidEnfant() );
            
                // RECIA - Modifications pour les maitres d'apprentissage
                if (CollectionUtils.isEmpty(uidsEnfantsUtilisablesCDT)) {
                    uidsEnfantsUtilisablesCDT = ldapBusinessService.getAutoriteTuteur(identifiant, utilisateurDTO.getListeUidEnfant());
                }
                
                if (CollectionUtils.isEmpty( uidsEnfantsUtilisablesCDT )) {
                    throw new MetierRuntimeException( new ConteneurMessage(), "Le parent (ou maitre d'apprentissage) n'est responsable d'aucun élève." );
                } else {
                    utilisateurDTO.setListeUidEnfant( uidsEnfantsUtilisablesCDT );
                }
            }

        break;
        case DIRECTION_ETABLISSEMENT:
        case DOCUMENTALISTE:
        break;
        case ELEVE:
            // 3 Recherche de l'existence de l'individu en base de données.
            id = eleveHibernateBusinessService.exist( uid );
            if (id == null) {
                throw new MetierRuntimeException( new ConteneurMessage(), "L'élève n'est pas inscrit dans l'application Cahier de texte." );
            }
        break;
        case ENSEIGNANT:
            final Map<String, Object> mapResult = enseignantHibernateBusinessService.exist( uid );

            if (vraiOuFauxEnvCRLR) {
                userDTO.setDepotStockage( (String) mapResult.get( "depot" ) );
            }
            id = (Integer) mapResult.get( "id" );
            if (id == null) {
                throw new MetierRuntimeException( new ConteneurMessage(), "L'enseignant n'est pas inscrit dans l'application Cahier de texte." );
            }
        break;
        case INSPECTION_ACADEMIQUE:
            id = inspectionHibernateBusinessService.exist( uid );
            utilisateurDTO.setSirenEtablissement( null );
            utilisateurDTO.setSirensEtablissement( new HashSet<String>() );
            if (id == null) {
                throw new MetierRuntimeException( new ConteneurMessage(), "L'inspecteur n'est pas inscrit dans l'application Cahier de texte." );
            }
        break;
        case AUTRE:
            // uniquement en temps qu'administrateur
            final Set<String> listeSirenAdm = utilisateurDTO.getAdminLocalSiren();
            final Set<String> listeSirenRes = utilisateurDTO.getAdminRessourceSiren();
            if (!CollectionUtils.isEmpty( listeSirenAdm ) || !CollectionUtils.isEmpty( listeSirenRes )
                    || BooleanUtils.isTrue( utilisateurDTO.getVraiOuFauxAdmCentral() )
                    || BooleanUtils.isTrue( utilisateurDTO.getVraiOuFauxAdmRessourceENT() )) {
                break;
            }
        default:
            throw new MetierRuntimeException( new ConteneurMessage(), "Le type profil n'existe pas ou n'est pas autorisé." );
        }
        log.debug( "Utilisateur trouvé avec l'id : {0}", id );
        userDTO.setIdentifiant( id );

        // Recherche de l'id de l'année scolaire, et date de début et date de
        // fin.
        final AnneeScolaireDTO anneeScolaireDTO = anneeScolaireHibernateBusinessService.findAnneeScolaire();
        utilisateurDTO.setAnneeScolaireDTO( anneeScolaireDTO );

        final String sirenEtablissement = utilisateurDTO.getSirenEtablissement();
        if (sirenEtablissement != null) {
            log.debug( "SIREN utilisateur : {0}", sirenEtablissement );
            // Recherche des innformations de l'établissement.
            final GenericDetailDTO<Integer, String, String, Boolean> dtoEtab = etablissementHibernateBusinessService
                    .findIdDescJourOuvreEtablissementParCode( sirenEtablissement, false, null );
            final Integer idEtablissement = dtoEtab.getValeur1();
            utilisateurDTO.setIdEtablissement( idEtablissement );
            utilisateurDTO.setDesignationEtablissement( dtoEtab.getValeur2() );
            utilisateurDTO.setJoursOuvresEtablissement( dtoEtab.getValeur3() );

            final Boolean vraiOuFauxEtabAccessible = dtoEtab.getValeur4();
            utilisateurDTO.setVraiOuFauxCahierOuvertEtab( vraiOuFauxEtabAccessible );

            // si l'utilisateur est enseignant on contrôle l'activation ou non
            // de la saisie simplifiée
            if (Profil.ENSEIGNANT.equals( profilIndividu )) {
                final CheckSaisieSimplifieeQO checkSaisieSimplifieeQO = new CheckSaisieSimplifieeQO( idEtablissement, id, anneeScolaireDTO,
                        vraiOuFauxEtabAccessible, vraiOuFauxEnvCRLR );
                Boolean vraiOuFauxSaisieSimplifiee = true;
                vraiOuFauxSaisieSimplifiee = saveCheckAutiomatisationActivationSaisieSimplifiee( checkSaisieSimplifieeQO );
                utilisateurDTO.setVraiOuFauxEtabSaisieSimplifiee( vraiOuFauxSaisieSimplifiee );
            }
        }

        utilisateurDTO.setArchiveUniquementDisponible( false );

        return new ResultatDTO<UtilisateurDTO>( utilisateurDTO, conteneurMessage );
    }

    /**
     * {@inheritDoc}
     */
    public Boolean saveCheckAutiomatisationActivationSaisieSimplifiee(final CheckSaisieSimplifieeQO checkSaisieSimplifieeQO) throws MetierException {
        Assert.isNotNull( "checkSaisieSimplifieeQO", checkSaisieSimplifieeQO );

        final Integer idEnseignant = checkSaisieSimplifieeQO.getIdEnseignant();
        final Integer idEtablissement = checkSaisieSimplifieeQO.getIdEtablissement();
        final AnneeScolaireDTO anneeScolaireDTO = checkSaisieSimplifieeQO.getAnneeScolaireDTO();

        Assert.isNotNull( "idEnseigant", idEnseignant );
        Assert.isNotNull( "idEtablissement", idEtablissement );
        Assert.isNotNull( "anneeScolaireDTO", anneeScolaireDTO );

        final Boolean vraiOuFauxEtabAccessible = BooleanUtils.isTrue( checkSaisieSimplifieeQO.getVraiOuFauxEtabAccessible() );

        Boolean vraiOuFauxSaisieSimplifiee = etablissementHibernateBusinessService.checkSaisieSimplifieeEtablissement( idEtablissement, idEnseignant );
        // si l'établissemzent est accessible.
        if (vraiOuFauxEtabAccessible) {
        	
        	if (vraiOuFauxSaisieSimplifiee != false ) {
        		 final SaveSequenceSimplifieeQO saveSequenceSimplifieeQO = new SaveSequenceSimplifieeQO();
                 saveSequenceSimplifieeQO.setAnneeScolaireDTO( anneeScolaireDTO );
                 saveSequenceSimplifieeQO.setIdEnseignant( idEnseignant );
                 saveSequenceSimplifieeQO.setIdEtablissement( idEtablissement );
	            // si la valeur vaut null il faut initialiser la saisie simplifiée à
	            // active
	            if (vraiOuFauxSaisieSimplifiee == null) {
	                vraiOuFauxSaisieSimplifiee = true;
	                // vérification que les séquences n'existent pas déjà
	                saveSequenceSimplifieeQO.setVraiOuFauxSaisieSimplifiee( vraiOuFauxSaisieSimplifiee );
	                if (!sequenceFacadeService.checkExistenceSequenceEnseignant( idEnseignant, idEtablissement )) {
	                    sequenceFacadeService.saveSequenceSaisieSimplifiee( saveSequenceSimplifieeQO );
	                }
	            } else  {
	            	// si on est en mode simplifier il faut compléter les sequences pour les nouveaux groupes (ou classes)
	                saveSequenceSimplifieeQO.setVraiOuFauxSaisieSimplifiee( vraiOuFauxSaisieSimplifiee );
	                sequenceFacadeService.ajoutSequenceManquanteSaisieSimplifiee(saveSequenceSimplifieeQO);
	            }
        	}
        } else {
            vraiOuFauxSaisieSimplifiee = BooleanUtils.isTrue( vraiOuFauxSaisieSimplifiee );
        }

        return vraiOuFauxSaisieSimplifiee;
    }

    /**
     * {@inheritDoc}
     */
    public ResultatDTO<ElevesParentDTO> findEleveDuParent(final Set<String> listeUidEleve) {
        Assert.isNotNull( "listeUidEleve", listeUidEleve );

        final ElevesParentDTO enfantsParentDTO = new ElevesParentDTO();
        final Set<UserDTO> liste = enfantsParentDTO.getListeEnfant();

        for (final String uidEnfant : listeUidEleve) {
            final EleveBean eleveBean = eleveHibernateBusinessService.find( uidEnfant );
            final UserDTO userEnfantDTO = new UserDTO();
            userEnfantDTO.setIdentifiant( eleveBean.getId() );
            userEnfantDTO.setNom( eleveBean.getNom() );
            userEnfantDTO.setPrenom( eleveBean.getPrenom() );
            liste.add( userEnfantDTO );
        }

        final ResultatDTO<ElevesParentDTO> resultatDTO = new ResultatDTO<ElevesParentDTO>();
        resultatDTO.setValeurDTO( enfantsParentDTO );

        if (CollectionUtils.isEmpty( liste )) {
            final ConteneurMessage conteneurMessage = new ConteneurMessage();
            conteneurMessage.add( new Message( TypeReglesSecurite.SECU_05.name() ) );
            resultatDTO.setConteneurMessage( conteneurMessage );
        }

        return resultatDTO;
    }

    /**
     * {@inheritDoc}
     */
    public ResultatDTO<List<EtablissementDTO>> findListeEtablissement() throws MetierException {
        final ResultatDTO<List<EtablissementDTO>> resultatDTO = new ResultatDTO<List<EtablissementDTO>>();
        final Map<String, Object[]> mapCodeDesignationResultat = new HashMap<String, Object[]>();

        // recherche des établissements courants
        final Map<String, Object[]> mapCodeDesignationCourant = etablissementHibernateBusinessService.executeQueryListeEtablissement( null );

        // recherche de l'ensemble des codes quelques soit le schéma de la base
        // de données.
        // 1- recherche des schémas
        final ResultatDTO<List<AnneeScolaireDTO>> listeAnneeScolaire = anneeScolaireHibernateBusinessService.findListeAnneeScolaire( false );

        final List<AnneeScolaireDTO> listeAnnees = listeAnneeScolaire.getValeurDTO();

        // 2- recherche des établissements dans ces schémas.
        for (final AnneeScolaireDTO anneeScolaireDTO : listeAnnees) {
            final String schemaArchive = Archive.PREFIX_SCHEMA_ARCHIVE + anneeScolaireDTO.getExercice();
            if (schemaHibernateBusinessService.checkExisenceSchema( schemaArchive )) {
                mapCodeDesignationResultat.putAll( etablissementHibernateBusinessService.executeQueryListeEtablissement( schemaArchive ) );
            }
        }

        // 3- ajoute les établissements du schéma courant dans la map de
        // resultat,
        // contenant à cet instant uniquement les établissements archivés.
        mapCodeDesignationResultat.putAll( mapCodeDesignationCourant );

        // 4- Construction des DTOs de resultat.
        final List<EtablissementDTO> listeEtablissementResultat = new ArrayList<EtablissementDTO>();
        for (final Entry<String, Object[]> entry : mapCodeDesignationResultat.entrySet()) {
            final EtablissementDTO etablissementDTO = new EtablissementDTO();
            etablissementDTO.setCode( entry.getKey() );
            etablissementDTO.setDesignation( (String) entry.getValue()[0] );
            etablissementDTO.setJoursOuvres( (String) entry.getValue()[1] );
            etablissementDTO.setVraiOuFauxOuvert( (Boolean) entry.getValue()[2] );
            etablissementDTO.setId( (Integer) entry.getValue()[3] );
            listeEtablissementResultat.add( etablissementDTO );
        }

        // 5- tri les résultat pas code puis par libellé.
        final LinkedHashMap<String, TriComparateur> mapTri = new LinkedHashMap<String, TriComparateur>();
        mapTri.put( "designation", TriComparateur.CROISSANT );
        resultatDTO.setValeurDTO( ComparateurUtils.sort( listeEtablissementResultat, mapTri ) );

        return resultatDTO;
    }

    /**
     * {@inheritDoc}
     */
    public ResultatDTO<List<EtablissementDTO>> findListeEtablissementEnseignantAdmRessources(final EtablissementAccessibleQO etablissementAccessibleQO) {
        final ResultatDTO<List<EtablissementDTO>> resultatDTO = new ResultatDTO<List<EtablissementDTO>>();
        // 1 recherche des établissements courants
        final Map<String, Object[]> mapCodeDesignationCourant = etablissementHibernateBusinessService.executeQueryListeEtablissement( null );

        // 2 recherche de l'activation de la saisie simplifié.
        final List<EtablissementDTO> listeEtablissementEnseignant = etablissementHibernateBusinessService
                .findListeEtablissementEnseignant( etablissementAccessibleQO );

        // 3- Construction des DTOs de resultat.
        final List<EtablissementDTO> listeEtablissementResultat = new ArrayList<EtablissementDTO>();
        for (final Entry<String, Object[]> entry : mapCodeDesignationCourant.entrySet()) {
            final Integer idEtab = (Integer) entry.getValue()[3];
            EtablissementDTO etablissementDTO = null;

            for (final EtablissementDTO etabDTO : listeEtablissementEnseignant) {
                if (idEtab.equals( etabDTO.getId() )) {
                    etablissementDTO = etabDTO;
                    break;
                }
            }

            if (etablissementDTO == null) {
                final EtablissementDTO etablissementAdminDTO = new EtablissementDTO();
                etablissementAdminDTO.setCode( entry.getKey() );
                etablissementAdminDTO.setDesignation( (String) entry.getValue()[0] );
                etablissementAdminDTO.setJoursOuvres( (String) entry.getValue()[1] );
                etablissementAdminDTO.setVraiOuFauxOuvert( (Boolean) entry.getValue()[2] );
                etablissementAdminDTO.setId( idEtab );
                listeEtablissementResultat.add( etablissementAdminDTO );
            } else {
                listeEtablissementResultat.add( etablissementDTO );
            }
        }

        // 4- tri les résultat pas code puis par libellé.
        final LinkedHashMap<String, TriComparateur> mapTri = new LinkedHashMap<String, TriComparateur>();
        mapTri.put( "designation", TriComparateur.CROISSANT );
        resultatDTO.setValeurDTO( ComparateurUtils.sort( listeEtablissementResultat, mapTri ) );

        return resultatDTO;
    }

    /**
     * {@inheritDoc}
     */
    public ResultatDTO<List<EtablissementDTO>> findListeEtablissementEnseignant(final EtablissementAccessibleQO etablissementAccessibleQO) {
        Assert.isNotNull( "etablissementAccessibleQO", etablissementAccessibleQO );
        final ResultatDTO<List<EtablissementDTO>> resultatDTO = new ResultatDTO<List<EtablissementDTO>>();

        final Integer idEnseignant = etablissementAccessibleQO.getIdEnseignant();
        final List<EtablissementDTO> listeEtablissementResultat;
        // cas des inspecteurs
        if (idEnseignant == null) {
            listeEtablissementResultat = etablissementHibernateBusinessService.findListeEtablissementInspecteur( etablissementAccessibleQO
                    .getListeSiren() );
        } else {
            listeEtablissementResultat = etablissementHibernateBusinessService.findListeEtablissementEnseignant( etablissementAccessibleQO );
        }

        // 5- tri les résultat pas code puis par libellé.
        final LinkedHashMap<String, TriComparateur> mapTri = new LinkedHashMap<String, TriComparateur>();
        mapTri.put( "designation", TriComparateur.CROISSANT );
        resultatDTO.setValeurDTO( ComparateurUtils.sort( listeEtablissementResultat, mapTri ) );

        return resultatDTO;
    }

    /**
     * {@inheritDoc}
     */
    public String findUtilisateurPreferences(final String uid) {
        return preferencesHibernateBusinessService.findUtilisateurPreferences( uid );
    }
}
