/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: jalopy.xml,v 1.1 2009/03/17 16:30:44 ent_breyton Exp $
 */

package org.crlr.metier.business;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.crlr.dto.application.admin.GenerateurDTO;
import org.crlr.dto.application.base.UtilisateurDTO;
import org.crlr.dto.application.importEDT.CaracEtabImportDTO;
import org.crlr.dto.application.importEDT.PeriodeEdtQO;
import org.crlr.exception.base.CrlrRuntimeException;
import org.crlr.exception.metier.MetierException;
import org.crlr.importEDT.DTO.AlternanceSemaineDTO;
import org.crlr.importEDT.DTO.CoursDTO;
import org.crlr.importEDT.DTO.DivisionDTO;
import org.crlr.importEDT.DTO.EmploiDTO;
import org.crlr.importEDT.DTO.EnumJourSemaine;
import org.crlr.importEDT.DTO.ErreurEDT;
import org.crlr.importEDT.DTO.GroupeDTO;
import org.crlr.importEDT.DTO.HoraireDTO;
import org.crlr.importEDT.DTO.IndividuDTO;
import org.crlr.importEDT.DTO.MatiereDTO;
import org.crlr.importEDT.DTO.NomPrenomDTO;
import org.crlr.importEDT.DTO.ServiceDTO;
import org.crlr.log.Log;
import org.crlr.log.LogFactory;
import org.crlr.web.dto.TypeSemaine;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Classe qui permet de parser du XML et de faire correspondre son contenu à d'autres classes.
 *
 * @author jp.mandrick
 * @version $Revision: 1.1 $
 */
@Component
public class ImportEDTBusiness implements ImportEDTBusinessService  {
    @Autowired
    private EmploiHibernateBusinessService emploiHibernateBusinessService;

    @Autowired
    private SequenceHibernateBusinessService sequenceHibernateBusinessService;

    @Autowired
    private EnseignantHibernateBusinessService enseignantHibernateBusinessService;

    @Autowired
    private ClasseHibernateBusinessService classeHibernateBusinessService;

    @Autowired
    private GroupeHibernateBusinessService groupeHibernateBusinessService;

    @Autowired
    private EnseignementHibernateBusinessService enseignementHibernateBusinessService;

    @Autowired
    private EtablissementHibernateBusinessService etablissementHibernateBusinessService;

    @Autowired
    private LdapBusinessService ldapBusinessService;

    /**
     * Fichier de log pour les cas d'erreur.
     */
    private static final Log log = LogFactory.getLog(ImportEDTBusiness.class);


    // VARIABLES "CONSTANTES"
    public final static String DTDSTSEDT = "DTD_STS_EDT.dtd";   // Le nom du fichier DTD pour valider le STS_EDT.
    public final static String DTDEDTSTS = "DTD_EDT_STS.dtd";   // Le nom du fichier DTD pour valider le EDT_STS.
    /** numéro de semaine minimum du début de l'année scolaire. 
     * Permet d'identifier les types d'alternances qui comment au début de l'année (T1, S1,...). */
    public final static Integer NUMSEMAINEDEBUTANNEE = 34;    
    /** numéro de semaine de la date limite pour déterminer un début d'année scolaire. */
    public final static Integer NUMSEMAINEFINDEBUTANNEE = 44;
    
    private Map<String, String> mapJour;
    /**
     * Permet de mapper un numéro de jour avec son libellé.
     * @return le libellé du jour de la semaine.
     */
    private Map<String, String> getMapJour(){
        if(mapJour == null){
            mapJour = new HashMap<String, String>();
            for(EnumJourSemaine jour : EnumJourSemaine.values()){
                mapJour.put(jour.getNumero(), jour.getJour());
            }
        }
        return mapJour;
    }

    /**
     * Modificateur de emploiHibernateBusinessService.
     * @param emploiHibernateBusinessService le emploiHibernateBusinessService à modifier
     */
    public void setEmploiHibernateBusinessService(
            EmploiHibernateBusinessService emploiHibernateBusinessService) {
        this.emploiHibernateBusinessService = emploiHibernateBusinessService;
    }

    /**
     * Modificateur de sequenceHibernateBusinessService.
     * @param sequenceHibernateBusinessService le sequenceHibernateBusinessService à modifier
     */
    public void setSequenceHibernateBusinessService(
            SequenceHibernateBusinessService sequenceHibernateBusinessService) {
        this.sequenceHibernateBusinessService = sequenceHibernateBusinessService;
    }

    /**
     * Modificateur de enseignantHibernateBusinessService.
     * @param enseignantHibernateBusinessService le enseignantHibernateBusinessService à modifier
     */
    public void setEnseignantHibernateBusinessService(
            EnseignantHibernateBusinessService enseignantHibernateBusinessService) {
        this.enseignantHibernateBusinessService = enseignantHibernateBusinessService;
    }

    /**
     * Modificateur de classeHibernateBusinessService.
     * @param classeHibernateBusinessService le classeHibernateBusinessService à modifier
     */
    public void setClasseHibernateBusinessService(
            ClasseHibernateBusinessService classeHibernateBusinessService) {
        this.classeHibernateBusinessService = classeHibernateBusinessService;
    }

    /**
     * Modificateur de groupeHibernateBusinessService.
     * @param groupeHibernateBusinessService le groupeHibernateBusinessService à modifier
     */
    public void setGroupeHibernateBusinessService(
            GroupeHibernateBusinessService groupeHibernateBusinessService) {
        this.groupeHibernateBusinessService = groupeHibernateBusinessService;
    }

    /**
     * Modificateur de enseignementHibernateBusinessService.
     * @param enseignementHibernateBusinessService le enseignementHibernateBusinessService à modifier
     */
    public void setEnseignementHibernateBusinessService(
            EnseignementHibernateBusinessService enseignementHibernateBusinessService) {
        this.enseignementHibernateBusinessService = enseignementHibernateBusinessService;
    }

    /**
     * Modificateur de etablissementHibernateBusinessService.
     * @param etablissementHibernateBusinessService le etablissementHibernateBusinessService à modifier
     */
    public void setEtablissementHibernateBusinessService(
            EtablissementHibernateBusinessService etablissementHibernateBusinessService) {
        this.etablissementHibernateBusinessService = etablissementHibernateBusinessService;
    }

    /**
     * Modificateur de ldapBusinessService.
     * @param ldapBusinessService le ldapBusinessService à modifier
     */
    public void setLdapBusinessService(LdapBusinessService ldapBusinessService) {
        this.ldapBusinessService = ldapBusinessService;
    }

    /**
     * {@inheritDoc}
     */
    public CaracEtabImportDTO saveTraitementEDTSTS(CaracEtabImportDTO caracEtabImportDTO, 
            String pathSTS, String pathEDT, String pathAppli) throws MetierException {
        log.info("Début de la méthode saveTraitementEDTSTS !");
        final SAXBuilder sxb = new SAXBuilder(true);
        Document documentStsEdt = new Document();
        Document documentEdtSts = new Document();

        // remplis le fichier fichierSTS_EDT
        final String pathFichierSTScree = recreeFichierAvecEntete(1, new File(pathSTS), pathAppli); 
        log.info("le fichier {0} a été crée.", pathFichierSTScree);
        new File(pathSTS).delete();     // supprimer le fichier STS temporaire uploadé.

        // remplis le fichier fichierEDT_STS
        final String pathFichierEDTcree = recreeFichierAvecEntete(2, new File(pathEDT), pathAppli);
        log.info("le fichier {0} a été crée.", pathFichierEDTcree);
        new File(pathEDT).delete();     // supprimer le fichier EDT temporaire uploadé.

        try {
            documentStsEdt = sxb.build(new File(pathFichierSTScree));        
            documentEdtSts = sxb.build(new File(pathFichierEDTcree));
            new File(pathFichierSTScree).delete();
            new File(pathFichierEDTcree).delete();
        } catch (JDOMException e) {
            throw new MetierException("Le fichier fourni ne correspond pas au format attendu, " +
            "veillez à ne pas inverser les fichiers de type STS_EDT et EDT_STS. ");
        } catch (IOException e) {
            throw new MetierException("Exception métier pour la construction du document XML : erreur de lecture du fichier fourni.");
        } 
        log.info("La validation des fichiers XML a réussi.");
        if(! verifieCorrespondanceFichiers(documentStsEdt, documentEdtSts)){
            throw new MetierException("Les fichiers fournis ne décrivent pas le même établissement. " +
            "Veuillez ne fournir que les paires de fichiers qui désignent le même établissement. ");
        }else{
            log.info("Les deux fichiers décrivent le même établissement. Le traitement continue...");

            final int idEtab = executeTraitementSTSEDT(documentStsEdt, caracEtabImportDTO);
            if(caracEtabImportDTO.getIdEtablissement() != idEtab){ 
                throw new MetierException("Les fichiers XML fournis ne correspondent pas à l'établissement " + 
                        "que vous avez sélectionné à l'accueil." +
                " Vous ne pouvez faire d'import que sur l'établissement sélectionné à l'accueil.");
            }
            log.info("Le traitement sur le fichier STS est terminé, id de l'établissement : {0}", idEtab);
            caracEtabImportDTO.setLesIdClasses(classeHibernateBusinessService.findListeClassePourEtab(idEtab));
            caracEtabImportDTO.setLesIdGroupes(groupeHibernateBusinessService.findListeGroupePourEtab(idEtab));

            executeTraitementEDTSTS(documentEdtSts, idEtab, caracEtabImportDTO);
            log.info("Fin du traitement de génération des cases d'emploi du temps ------------------");
        } 
        return caracEtabImportDTO;
    }

    /**
     * Liste les méthodes qui seront appelées pour le traitement d'un fichier XML STS_EDT.
     * @param document : Le document XML.
     * @param caracEtabImportDTO : le DTO qui contient les données de l'établissement.
     * @return idEtablissement.
     * @throws MetierException l'exception métier qui peut être levée.
     */
    @Transactional(readOnly = true)
    private int executeTraitementSTSEDT(Document document, CaracEtabImportDTO caracEtabImportDTO) throws MetierException{
        final Element racine = document.getRootElement();
        final String uajEtablissement = recupereUAJSTSEDT(racine);
        String sirenEtab;
        //TEST pour Chateauneuf
        //String sirenEtab = "19360003800015";
        try{
            sirenEtab = ldapBusinessService.getSirenEtablissement("(ENTStructureUAI="+uajEtablissement+")");
        }catch(Exception e){
            throw new MetierException("Il semble que l'établissement décrit par le fichier XML et ayant l'UAJ {0}" +
                    " ne soit pas répertorié dans notre base.", uajEtablissement);
        }
        final int idEtablissement = etablissementHibernateBusinessService.findIdEtablissementParCode(sirenEtab);
        log.info("etablissement uaj : "+uajEtablissement+", siren : "+sirenEtab+", id : " + idEtablissement);
        caracEtabImportDTO.setLesIndividusDTO(recupereListeIndividus(racine));
        caracEtabImportDTO.setLesMatieres(recupereListeMatieres(racine));
        return idEtablissement;
    }

    /**
     * Contruit la liste des semaines de vancances.
     * @param utilisateurDTO : utilisateur sur lequel est lue la chaine anneeScolaire.periodeVacance.
     * au format : "DateDeb1:DateFin1|DateDeb2:DateFin2|DateDeb2:DateFin2|...|" 
     * @return La liste des numeros de semaine de vancances
     */
    private Set<Integer> construitNumSemaineVacances(UtilisateurDTO utilisateurDTO) {
        String periodeVacance;
        try {
            periodeVacance = utilisateurDTO.getAnneeScolaireDTO().getPeriodeVacances();    
        } catch (Exception e) {
            periodeVacance = "";
        }
        final Set<Integer> setVacance = GenerateurDTO.generateSetPeriodeVaqueFromDb(periodeVacance);
        return setVacance;
    }
    
    /**
     * Liste les méthodes qui seront appelées pour le traitement d'un fichier XML EDT_STS.
     * @param document : Le document XML.
     * @param idEtab : id de l'établissement.
     * @param caracEtabImportDTO le DTO qui contient les informations sur l'établissement.
     * @throws MetierException exception métier.
     */
    @SuppressWarnings("unchecked")
    private void executeTraitementEDTSTS(Document document, int idEtab, CaracEtabImportDTO caracEtabImportDTO) throws MetierException{

        final Element racine = document.getRootElement();
        int totalEmploi = 0;
        caracEtabImportDTO.getPrintEDTDTO().setDateDebutPeriode(caracEtabImportDTO.getDateDebutPeriode());
        caracEtabImportDTO.setLesHoraires(recupereListeHoraireEtablissement(idEtab));
        final String altSemaines = etablissementHibernateBusinessService.findAlternanceSemaine(idEtab);
        if(StringUtils.isEmpty(altSemaines) || altSemaines.equals("''")){
            //On a pas d'alternance.
            caracEtabImportDTO.setEdtAlternance(false);
        }
        if((! StringUtils.isEmpty(altSemaines)) && altSemaines.contains("PAIR")){
            //C'est de l'alternance
            caracEtabImportDTO.setEdtAlternance(true);
        }
        
        // Construit la liste des semaines de vacances (qui sont considérées comme PAIR et IMPAIR
        final Set<Integer> listeVacance = construitNumSemaineVacances(caracEtabImportDTO.getUtilisateurDTO());
        
        //Récupérer la liste des semaines et donner une signification aux codes d'alternances.
        final Element donnees = racine.getChild("DONNEES");
        final Element alternances = donnees.getChild("ALTERNANCES");
        final List<Element> lesAlternances = alternances.getChildren("ALTERNANCE");
        for(Element alternance : lesAlternances){
            final String codeAlt = alternance.getAttributeValue("CODE");
            final int result = compareSemainesAlternance(alternance.getChild("SEMAINES"), idEtab, altSemaines, listeVacance, caracEtabImportDTO);
            caracEtabImportDTO.getSignificationAlternance().add(new AlternanceSemaineDTO(result,codeAlt));
        }
        //!!if(caracEtabImportDTO.getMargeMinute() == 0){ correction = false; } else{ correction = true; }
        Integer compteurValide = 0;
        final List<DivisionDTO> mesDivs = recupereListeDivisions(racine, caracEtabImportDTO);
        if(! CollectionUtils.isEmpty(mesDivs)){
            for(DivisionDTO div : mesDivs){
                final List<EmploiDTO> desEmploisDTO = extraitInfosDivision(div, idEtab, caracEtabImportDTO);
                totalEmploi += desEmploisDTO.size();
                //Séparer les emploi à fusionner des autres : 2 stockages différents.
                //!!final List<EmploiDTO> casesAFusionner = new ArrayList<EmploiDTO>();
                for(EmploiDTO caseEmploi : desEmploisDTO){
                    caracEtabImportDTO.getCasesSimples().add(caseEmploi);
                    compteurValide++; 
                    
                }
            }
            log.info("Il y a " + mesDivs.size() + " divisions !");
        } 
        final List<GroupeDTO> mesGroups = recupereListeGroupes(racine, caracEtabImportDTO);
        if(! CollectionUtils.isEmpty(mesGroups)){
            for(GroupeDTO groupe : mesGroups){
                final List<EmploiDTO> desEmploisDTO = extraitInfosGroupe(groupe, idEtab, caracEtabImportDTO);
                totalEmploi += desEmploisDTO.size();
                //Séparer les emploi à fusionner des autres -> 2 stockages différents.
                //!!final List<EmploiDTO> casesAFusionner = new ArrayList<EmploiDTO>();
                for(EmploiDTO caseEmploi : desEmploisDTO){
                    caracEtabImportDTO.getCasesSimples().add(caseEmploi);
                    compteurValide++; 
                }
            } 
            log.info("Il y a " + mesGroups.size() + " groupes !");
            log.info("Il y a en tout " + totalEmploi + " cases d'emploi du temps !");
        } 
        caracEtabImportDTO.getPrintEDTDTO().setTotalCase(totalEmploi);
        log.info("Statistiques sur un total de "+totalEmploi+" cases : ");
    }

    /**
     * Permet de récupérer l'UAJ pour un fichier EDT_STS.
     * @param racine l'élément racine du document.
     * @return l'UAJ.
     */
    private String recupereUAJEDTSTS(Element racine){
        final Element parametres = racine.getChild("PARAMETRES");
        return parametres.getChildText("UAJ");
    }

    /**
     * Permet de récupérer l'UAJ pour un fichier STS_EDT.
     * @param racine l'élément racine du document.
     * @return l'UAJ.
     */
    private String recupereUAJSTSEDT(Element racine){
        final Element parametres = racine.getChild("PARAMETRES");
        return parametres.getChild("UAJ").getAttributeValue("CODE");
    }

    /**
     * Permet de récupérer la liste des individus répertoriés dans le fichier XML STS_EDT.
     * @param racine : l'élément racine du document XML.
     * @return la liste des individus du document.
     */
    @SuppressWarnings("unchecked")
    private List<IndividuDTO> recupereListeIndividus(Element racine){
        final List<IndividuDTO> individusDTO = new ArrayList<IndividuDTO>();
        final Element donnees = racine.getChild("DONNEES");
        final Element individus = donnees.getChild("INDIVIDUS");
        if(individus.getChildren("INDIVIDU") != null){
            final List<Element> lesIndividus = individus.getChildren("INDIVIDU");
            for(Element indi : lesIndividus){
                final IndividuDTO unIndividu = new IndividuDTO();
                unIndividu.setId(indi.getAttributeValue("ID"));
                unIndividu.setNom(indi.getChildText("NOM_USAGE")); 
                unIndividu.setPrenom(indi.getChildText("PRENOM"));
                individusDTO.add(unIndividu);
            }
        }
        return individusDTO;
    }

    /**
     * Permet de récupérer la liste des matières répertoriées dans le fichier XML STS_EDT.
     * @param racine : l'élément racine du document XML.
     * @return la liste des matières du document.
     */
    @SuppressWarnings("unchecked")
    private List<MatiereDTO> recupereListeMatieres(Element racine){
        final List<MatiereDTO> matieresDTO = new ArrayList<MatiereDTO>();
        final Element nomenclatures = racine.getChild("NOMENCLATURES");
        final Element matieres = nomenclatures.getChild("MATIERES");
        if(!CollectionUtils.isEmpty(matieres.getChildren("MATIERE"))){
            final List<Element> lesMatieres = matieres.getChildren("MATIERE");
            for(Element matiere : lesMatieres){
                //On ne récupère que le nécessaire.
                final MatiereDTO uneMatiere = new MatiereDTO();
                uneMatiere.setCode(matiere.getAttributeValue("CODE"));
                uneMatiere.setLibelleLong(org.crlr.utils.StringUtils.stripSpacesSentence(matiere.getChildText("LIBELLE_LONG"))); 
                matieresDTO.add(uneMatiere);
            }
        }
        return matieresDTO;
    }

    /**
     * Permet de récupérer la liste des divisions répertoriées dans le fichier XML STS_EDT.
     * @param racine : l'élément racine du document XML.
     * @param caracEtabImportDTO : Le DTO contenant les informations pour l'établissemement.
     * @return la liste des divisions du document.
     */
    @SuppressWarnings("unchecked")
    private List<DivisionDTO> recupereListeDivisions(Element racine, CaracEtabImportDTO caracEtabImportDTO){
        final List<DivisionDTO> divisionsDTO = new ArrayList<DivisionDTO>();
        final Element donnees = racine.getChild("DONNEES");
        final Element structure = donnees.getChild("STRUCTURE");
        if(structure.getChild("DIVISIONS") != null){
            final Element divisions = structure.getChild("DIVISIONS");
            if(!CollectionUtils.isEmpty(divisions.getChildren("DIVISION"))){
                final List<Element> lesDivisions = divisions.getChildren("DIVISION");
                for(Element division : lesDivisions){
                    //On ne récupère que le nécessaire.
                    final DivisionDTO uneDivision = new DivisionDTO();
                    final String codeDiv = division.getAttributeValue("CODE").replace("/", "-");
                    uneDivision.setCode(codeDiv);
                    uneDivision.setServices(recupereListeServices(division, caracEtabImportDTO)); 
                    divisionsDTO.add(uneDivision);
                }
            }
        }
        return divisionsDTO;
    }

    /**
     * Permet de récupérer la liste des groupes répertoriés dans le fichier XML STS_EDT.
     * @param racine : l'élément racine du document XML.
     * @param caracEtabImportDTO : Le DTO contenant les informations pour l'établissemement.
     * @return la liste des groupes du document.
     */
    @SuppressWarnings("unchecked")
    private List<GroupeDTO> recupereListeGroupes(Element racine, CaracEtabImportDTO caracEtabImportDTO){
        final List<GroupeDTO> groupesDTO = new ArrayList<GroupeDTO>();
        final Element donnees = racine.getChild("DONNEES");
        final Element structure = donnees.getChild("STRUCTURE");
        if(structure.getChild("GROUPES") != null){
            final Element groupes = structure.getChild("GROUPES");
            if(!CollectionUtils.isEmpty(groupes.getChildren("GROUPE"))){
                final List<Element> lesGroupes = groupes.getChildren("GROUPE");
                for(Element groupe : lesGroupes){
                    //On ne récupère que le nécessaire.
                    final GroupeDTO unGroupe = new GroupeDTO();
                    final String codeGroupe = groupe.getAttributeValue("CODE").replace("/", "-");
                    unGroupe.setCode(codeGroupe);
                    unGroupe.setServices(recupereListeServices(groupe, caracEtabImportDTO)); 
                    groupesDTO.add(unGroupe);
                }
            }
        }
        return groupesDTO;
    }

    /**
     * Récupère la liste des services d'une division ou d'un groupe.
     * @param division : la division (ou le groupe) dont on veut les services.
     * @param caracEtabImportDTO : Le DTO contenant les informations pour l'établissemement.
     * @return la liste des services.
     */
    @SuppressWarnings("unchecked")
    private List<ServiceDTO> recupereListeServices(Element division, CaracEtabImportDTO caracEtabImportDTO){
        final List<ServiceDTO> servicesDTO = new ArrayList<ServiceDTO>();
        if(division.getChild("SERVICES") != null){
            final Element services = division.getChild("SERVICES");
            if(!CollectionUtils.isEmpty(services.getChildren("SERVICE"))){
                final List<Element> lesServices = services.getChildren("SERVICE");
                for(Element service : lesServices){
                    //On ne récupère que le nécessaire.
                    final ServiceDTO unService = new ServiceDTO();
                    unService.setCodeMatiere(service.getAttributeValue("CODE_MATIERE"));
                    unService.setEnseignants(recupereListeEnseignantsDuService(service, caracEtabImportDTO));
                    servicesDTO.add(unService);
                }
            }
        }
        return servicesDTO;
    }

    /**
     * Récupère la liste des enseignants d'un service.
     * @param service : le service.
     * @param caracEtabImportDTO : Le DTO contenant les informations pour l'établissemement.
     * @return la liste des enseignants.
     */
    @SuppressWarnings("unchecked")
    private List<IndividuDTO> recupereListeEnseignantsDuService(Element service, CaracEtabImportDTO caracEtabImportDTO){
        final List<IndividuDTO> individusDTO = new ArrayList<IndividuDTO>();
        if(service.getChild("ENSEIGNANTS") != null){
            final Element enseignants = service.getChild("ENSEIGNANTS");
            if(!CollectionUtils.isEmpty(enseignants.getChildren("ENSEIGNANT"))){
                final List<Element> lesEnsei = enseignants.getChildren("ENSEIGNANT");
                for(Element ens : lesEnsei){
                    final IndividuDTO unEnseignant = new IndividuDTO();
                    unEnseignant.setId(ens.getAttributeValue("ID"));
                    unEnseignant.setCours(recupereListeCoursDeEnseignant(ens, caracEtabImportDTO));
                    individusDTO.add(unEnseignant);
                }
            }
        }
        return individusDTO;
    }

    /**
     * Permet de récupérer la liste des cours d'un enseignant.
     * @param enseignant : l'enseignant.
     * @param caracEtabImportDTO : Le DTO contenant les informations pour l'établissemement.
     * @return la liste des cours.
     */
    @SuppressWarnings("unchecked")
    private List<CoursDTO> recupereListeCoursDeEnseignant(Element enseignant, CaracEtabImportDTO caracEtabImportDTO){
        final List<CoursDTO> coursDTO = new ArrayList<CoursDTO>();
        if(enseignant.getChild("COURS_RATTACHES") != null){
            final Element coursRattaches = enseignant.getChild("COURS_RATTACHES");
            if(!CollectionUtils.isEmpty(coursRattaches.getChildren("COURS"))){
                final List<Element> lesCours = coursRattaches.getChildren("COURS");
                for(Element cours : lesCours){
                    String codeSalle = null;
                    if(cours.getChild("CODE_SALLE") != null){
                        codeSalle = cours.getChildText("CODE_SALLE");
                    }
                    
                    final CoursDTO unCours = new CoursDTO();
                    unCours.setCodeSalle(codeSalle);
                    unCours.setCodeAlternance(caracEtabImportDTO.getMapAlternance().get(cours.getChildText("CODE_ALTERNANCE")));
                    unCours.setJour(getMapJour().get(cours.getChildText("JOUR")));
                    unCours.setHeureDebut(cours.getChildText("HEURE_DEBUT"));
                    unCours.setDuree(cours.getChildText("DUREE"));
                    if(unCours.getCodeAlternance().equals("3")){
                        unCours.setCodeAlternance("1");
                        //Création d'un clone avec codeAlternance 2 + ajout du clone à la liste coursDTO.
                        if(caracEtabImportDTO.getEdtAlternance()){
                            final CoursDTO secondCours = new CoursDTO(unCours);
                            secondCours.setCodeAlternance("2");
                            coursDTO.add(secondCours);
                        }
                    }
                    coursDTO.add(unCours);
                }
            }
        }
        return coursDTO;
    }

    /**
     * Permet d'obtenir les heures et minutes de début et de fin.
     * @param heureDebut : l'indication d'heure de début du cours du XML (1500 = 15h00).
     * @param duree : la durée du cours indiquée dans le XML (0100 = 1h).
     * @return un objet HoraireDTO.
     */
    private HoraireDTO extraitHoraire(String heureDebut, String duree){
        final String hd = heureDebut.substring(0, 2);
        final String md = heureDebut.substring(2);
        //additionner deux chaines de caractère du style 1340 + 0100 = 1440
        String heureFin = "";
        final Integer total = Integer.valueOf(heureDebut) + Integer.valueOf(duree);
        if(total<1000){ 
            heureFin = "0"+total.toString();
        }else{
            heureFin = total.toString();
        }
        String hf = heureFin.substring(0, 2);
        String mf = heureFin.substring(2, 4);
        if(Integer.valueOf(mf)>59){
            //si on a 60 minutes, on doit rajouter une heure et retirer 60 minutes.
            hf = String.valueOf((Integer.valueOf(hf)+1));
            if(Integer.valueOf(hf)<10){ hf = "0"+hf; }
            mf = String.valueOf(Integer.valueOf(mf)-60);
            if(Integer.valueOf(mf)<10){ mf = "0"+mf; }
        }
        final HoraireDTO horaire = new HoraireDTO();
        horaire.setHeureDebut(hd);
        horaire.setHeureFin(hf);
        horaire.setMinuteDebut(md);
        horaire.setMinuteFin(mf);
        return horaire;
    }

    /**
     * Permet d'extraire toutes les informations nécessaires depuis une division pour remplir les champs de l'emploi du temps.
     * @param uneDivision : la classe étudiée.
     * @param idEtab : l'établissement auquel appartient la division.
     * @param caracEtabImportDTO : Le DTO contenant les informations pour l'établissemement.
     * @return la liste de toutes les emploiDTO.
     */
    @Transactional(readOnly = true)
    private List<EmploiDTO> extraitInfosDivision(DivisionDTO uneDivision, int idEtab, CaracEtabImportDTO caracEtabImportDTO){
        final List<EmploiDTO> lesEmplois = new ArrayList<EmploiDTO>();
        final int idClasse = classeHibernateBusinessService.findIdClasseByCodeAndEtab(uneDivision.getCode(), idEtab);
        if(idClasse==0){ 
            caracEtabImportDTO.getPrintEDTDTO().getClasseErreur().add(new ErreurEDT(uneDivision.getCode())); 
        }else{
            for(ServiceDTO service : uneDivision.getServices()){
                final String codeMatiere = service.getCodeMatiere();
                final Map<String,String> mapMatiere = caracEtabImportDTO.getMapMatiere(); 
                final String libelleMatiere = mapMatiere.get(codeMatiere);
                final int idEnseignement = enseignementHibernateBusinessService.findIdEnseignementByLibelle(libelleMatiere);
                if(idEnseignement==0){
                    final ErreurEDT erreurEDT;
                    if (libelleMatiere != null) { 
                        erreurEDT = new ErreurEDT(libelleMatiere);
                    } else { 
                        erreurEDT = new ErreurEDT(codeMatiere);
                    }
                    caracEtabImportDTO.getPrintEDTDTO().getEnseignementErreur().add(erreurEDT);
                }else{
                    final Integer idSeq = sequenceHibernateBusinessService.findIdSequenceByEnsEtClasseGroupe(idEnseignement, idClasse);
                    for(IndividuDTO enseignant : service.getEnseignants()){
                        if(caracEtabImportDTO.getMapEnseignant().get(enseignant.getId()) != null){
                            final NomPrenomDTO nomPrenom = caracEtabImportDTO.getMapEnseignant().get(enseignant.getId());
                            final int idEnseignant = enseignantHibernateBusinessService.findIdEnseignantParEtabNomPrenom(idEtab, nomPrenom.getNom(),
                                    nomPrenom.getPrenom());
                            if(idEnseignant==0){ 
                                caracEtabImportDTO.getPrintEDTDTO().getEnseignantErreurBD().add(new ErreurEDT(nomPrenom.getPrenom() 
                                        + " " + nomPrenom.getNom())); 
                            }else{
                                if(verifieAutorisationEnseignantClasse(idEtab, idEnseignant, idEnseignement, idClasse, caracEtabImportDTO)){
                                    for(CoursDTO cours : enseignant.getCours()){
                                        final String jour = cours.getJour();
                                        final String typeSemaine = cours.getCodeAlternance();
                                        if(! typeSemaine.equals("0")){
                                            final HoraireDTO horaire = extraitHoraire(cours.getHeureDebut(), cours.getDuree());
                                            final EmploiDTO unEmploi = new EmploiDTO();
                                            unEmploi.setCodeSalle(cours.getCodeSalle());
                                            unEmploi.setHeureDebut(horaire.getHeureDebut());
                                            unEmploi.setHeureFin(horaire.getHeureFin());
                                            unEmploi.setIdClasse(idClasse);
                                            unEmploi.setIdGroupe(null);
                                            unEmploi.setIdEnseignant(idEnseignant);
                                            unEmploi.setIdEnseignement(idEnseignement);
                                            unEmploi.setIdEtablissement(idEtab);
                                            unEmploi.setIdSequence(idSeq);
                                            unEmploi.setJour(jour);
                                            unEmploi.setMinuteDebut(horaire.getMinuteDebut());
                                            unEmploi.setMinuteFin(horaire.getMinuteFin());
                                            unEmploi.setTypeSemaine(TypeSemaine.getTypeSemaine(typeSemaine));
                                            lesEmplois.add(unEmploi);
                                        }
                                    }
                                }else{
                                    log.info("Une autorisation refusée pour la classe "+idClasse+" l'enseignant "
                                            +idEnseignant+" pour la matière "+idEnseignement);
                                }
                            }
                        }else{
                            // Aucune correspondance trouvée pour cet enseignant. On passe au suivant.
                            caracEtabImportDTO.getPrintEDTDTO().getEnseignantErreurEDT().add(new ErreurEDT(enseignant.getId()));
                        }
                    }
                }
            }
        }
        return lesEmplois;
    }

    /**
     * Permet d'extraire toutes les informations nécessaires depuis un groupe pour remplir les champs de l'emploi du temps.
     * @param unGroupe : le groupe étudié.
     * @param idEtab : l'établissement auquel appartient le groupe.
     * @param caracEtabImportDTO : Le DTO contenant les informations pour l'établissemement.
     * @return la liste de toutes les emploiDTO.
     */
    @Transactional(readOnly = true)
    private List<EmploiDTO> extraitInfosGroupe(GroupeDTO unGroupe, int idEtab, CaracEtabImportDTO caracEtabImportDTO){
        final List<EmploiDTO> lesEmplois = new ArrayList<EmploiDTO>();
        final int idGroupe = groupeHibernateBusinessService.findIdGroupeByCodeAndEtab(unGroupe.getCode(), idEtab);
        if(idGroupe==0){ 
            caracEtabImportDTO.getPrintEDTDTO().getGroupeErreur().add(new ErreurEDT(unGroupe.getCode())); 
        }else{
            for(ServiceDTO service : unGroupe.getServices()){
                final String libelleMatiere = caracEtabImportDTO.getMapMatiere().get(service.getCodeMatiere());
                final int idEnseignement = enseignementHibernateBusinessService.findIdEnseignementByLibelle(libelleMatiere);
                if(idEnseignement==0){ 
                    caracEtabImportDTO.getPrintEDTDTO().getEnseignementErreur().add(new ErreurEDT(libelleMatiere));
                }else{
                    final Integer idSeq = sequenceHibernateBusinessService.findIdSequenceByEnsEtClasseGroupe(idEnseignement, idGroupe);
                    for(IndividuDTO enseignant : service.getEnseignants()){
                        if(caracEtabImportDTO.getMapEnseignant().get(enseignant.getId()) != null){
                            final NomPrenomDTO nomPrenom = caracEtabImportDTO.getMapEnseignant().get(enseignant.getId());
                            final int idEnseignant = enseignantHibernateBusinessService.findIdEnseignantParEtabNomPrenom(idEtab, nomPrenom.getNom(),
                                    nomPrenom.getPrenom());
                            if(idEnseignant==0){ 
                                caracEtabImportDTO.getPrintEDTDTO().getEnseignantErreurBD().add(new ErreurEDT(nomPrenom.getPrenom() 
                                        + " " + nomPrenom.getNom())); 
                            }else{
                                if(verifieAutorisationEnseignantGroupe(idEtab, idEnseignant, idEnseignement, idGroupe, caracEtabImportDTO)){
                                    for(CoursDTO cours : enseignant.getCours()){
                                        final String jour = cours.getJour();
                                        final String typeSemaine = cours.getCodeAlternance();
                                        if(! typeSemaine.equals("0")){
                                            final HoraireDTO horaire = extraitHoraire(cours.getHeureDebut(), cours.getDuree());
                                            final EmploiDTO unEmploi = new EmploiDTO();
                                            unEmploi.setCodeSalle(cours.getCodeSalle());
                                            unEmploi.setHeureDebut(horaire.getHeureDebut());
                                            unEmploi.setHeureFin(horaire.getHeureFin());
                                            unEmploi.setIdGroupe(idGroupe);
                                            unEmploi.setIdClasse(null);
                                            unEmploi.setIdEnseignant(idEnseignant);
                                            unEmploi.setIdEnseignement(idEnseignement);
                                            unEmploi.setIdEtablissement(idEtab);
                                            unEmploi.setIdSequence(idSeq);
                                            unEmploi.setJour(jour);
                                            unEmploi.setMinuteDebut(horaire.getMinuteDebut());
                                            unEmploi.setMinuteFin(horaire.getMinuteFin());
                                            unEmploi.setTypeSemaine(TypeSemaine.getTypeSemaine(typeSemaine));
                                            lesEmplois.add(unEmploi);
                                        }
                                    }
                                }else{
                                    log.info("Une autorisation refusée pour le groupe "+idGroupe+" l'enseignant "
                                            +idEnseignant+" pour la matière "+idEnseignement);
                                }
                            }
                        }else{
                            // Aucune correspondance trouvée pour cet enseignant. On passe au suivant.
                            caracEtabImportDTO.getPrintEDTDTO().getEnseignantErreurEDT().add(new ErreurEDT(enseignant.getId()));
                        }
                    }
                }
            }
        }
        return lesEmplois;
    }

    /**
     * Permet de comparer le contenu d'alternance du XML avec ce qui a été configuré en base de données.
     * @param semaines : le noeud pour explorer les semaines d'alternance.
     * @param idEtab : l'id de l'établissement.
     * @param altSemaines : contenu du champs alternance_semaine de la base de données.
     * @param listeVacance : liste des numeros de semaines correspondant aux vacances
     * @param caracEtabImportDTO : Le DTO contenant les informations pour l'établissemement.
     * @return resultat : 0 -> pas encore configuré en bdd, 1->alternance semaine 1 (PAIR), 2 -> alternance semaine 2 (IMPAIR), 
     * 3 -> hebdomadaire.
     */
    @SuppressWarnings({ "unchecked", "static-access" })
    private int compareSemainesAlternance(Element semaines, int idEtab, String altSemaines, 
            final Set<Integer> listeVacance, CaracEtabImportDTO caracEtabImportDTO){
        //requête qui récupère la string des semaines d'alternances.
        if(!caracEtabImportDTO.getEdtAlternance()){
            final List<Element> debSemaines = semaines.getChildren("DATE_DEBUT_SEMAINE");
            final int tailleSem = debSemaines.size();
            if(tailleSem >= 30){
                return 3;
            }else{
                if(tailleSem>8){
                    //c'est peut-être un trimestre
                    final Calendar gc = (Calendar)Calendar.getInstance();
                    final List<AlternanceSemaineDTO> semAltDuXML = new ArrayList<AlternanceSemaineDTO>();
                    for(Element debS : debSemaines){
                        //splitter ce qu'il y a dans la balise avec le "-"
                        final String[] laDate = debS.getTextTrim().split("-");
                        gc.set(Integer.valueOf(laDate[0]), Integer.valueOf(laDate[1]), Integer.valueOf(laDate[2]));
                        final AlternanceSemaineDTO altsDTO = new AlternanceSemaineDTO(gc.get(gc.WEEK_OF_YEAR), "");
                        semAltDuXML.add(altsDTO);
                    }
                    return verifieTypeAlternance(semAltDuXML);
                }
                //Pas d'alternance et trop petit pour représenter l'année entière.
                return 0;
            }
        }

        //récupération des semaines du XML.
        final Calendar gc = (Calendar)Calendar.getInstance();
        final List<AlternanceSemaineDTO> semAltDuXML = new ArrayList<AlternanceSemaineDTO>();
        final List<Element> debSemaines = semaines.getChildren("DATE_DEBUT_SEMAINE");
        for(Element debS : debSemaines){
            //splitter ce qu'il y a dans la balise avec le "-"
            final String[] laDate = debS.getTextTrim().split("-");
            gc.set(Integer.valueOf(laDate[0]), Integer.valueOf(laDate[1]) - 1, Integer.valueOf(laDate[2]));
            final AlternanceSemaineDTO altsDTO = new AlternanceSemaineDTO(gc.get(gc.WEEK_OF_YEAR), "");
            semAltDuXML.add(altsDTO);
        }
        log.info("On a récupéré " + semAltDuXML.size() + " semaines");
        if(StringUtils.isEmpty(altSemaines)){
            if(semAltDuXML.size() >= 30){
                return 3;
            }else{
                log.info("Pas d'alternance configurée, trop peu pour représenter l'année entière, on teste le premier trimestre.");
                if (semAltDuXML.size() > 8){
                    return verifieTypeAlternance(semAltDuXML);
                } else {
                    return 0;
                }
            }
        }
        final List<AlternanceSemaineDTO> listeSemainesAltDTO = new ArrayList<AlternanceSemaineDTO>();
        final String[] lesSemaines = altSemaines.split("\\|");    //sera de la forme 45:PAIR
        for (String s : lesSemaines ){
            final String[] uneSem = s.split(":");
            final AlternanceSemaineDTO altSemDTO = new AlternanceSemaineDTO(Integer.valueOf(uneSem[0]),uneSem[1]);
            listeSemainesAltDTO.add(altSemDTO);
        }
        // COMPARATIF DES DEUX LISTES POUR SAVOIR QUEL RESULTAT RENVOYER
        final List<Integer> lesPairs = new ArrayList<Integer>();
        for(AlternanceSemaineDTO as : listeSemainesAltDTO){
            if(as.getTypeSemaine().equals("PAIR")){ lesPairs.add(as.getNumeroSemaine()); }
        }
        final List<Integer> lesImpairs = new ArrayList<Integer>();
        for(AlternanceSemaineDTO as : listeSemainesAltDTO){
            if(as.getTypeSemaine().equals("IMPAIR")){ lesImpairs.add(as.getNumeroSemaine()); }
        }
        // Ajoute aux pairs et impairs les numeros de semaine de vacances
        for (final Integer numeroSemaine:listeVacance) {
            lesPairs.add(numeroSemaine);
            lesImpairs.add(numeroSemaine);
        }
        
        if((lesPairs.size()  >= (semAltDuXML.size())) || 
           (lesImpairs.size() >= (semAltDuXML.size())) 
        && (semAltDuXML.size() < 30)){
            //Il y a une chance pour que ça soit les semaines paires ou impaires, on vérifie d'abord pour les semaines paires.
            int comptePair = 0;
            boolean estPair = false;
            for(AlternanceSemaineDTO alt : semAltDuXML){
                estPair = false;
                for(int pair : lesPairs){
                    
                    final Integer ipair = pair;
                    final Integer ialt = alt.getNumeroSemaine();
                    
                    if(ipair.equals(ialt)){
                        estPair = true; 
                        comptePair++; 
                        break;
                    }
                }
                if(!estPair){   // si tous les numéros de semaine du XML sont pairs, alors estPair sera toujours true ici.
                    break;
                }
            }
            if(estPair){
                return 1; 
            } //C'est PAIR et pas autre chose !
            
            boolean estImpair = false;
            if((!estPair)&&(comptePair==0)){    //Il n'y a aucun pair, cela peut donc être IMPAIR.
                estImpair = false;
                for(AlternanceSemaineDTO alt : semAltDuXML){
                    estImpair = false;
                    for(int impair : lesImpairs){
                        
                        final Integer iimpair = impair;
                        final Integer ialt = alt.getNumeroSemaine();
                        
                        if(iimpair.equals(ialt)){
                            estImpair = true; break;
                        }
                    }
                    if(!estImpair){
                        break;
                    }
                }
            }
            //Si estImpair == true, c'est que toute la liste XML contient des impairs et rien d'autre.
            if(estImpair){
                return 2;
            }else{
                // peut-être un trimestre ou autre ?
                if (semAltDuXML.size() > 8){
                    return verifieTypeAlternance(semAltDuXML);
                } else {
                    return 0;
                }
            }
        }else{
            //la liste des semaines du XML est plus grande que la liste des alternances, 
            if(semAltDuXML.size() >= 30){   // lorsque c'est supérieur à 30, on considère que c'est hebdomadaire.
                return 3;
            }else{
                if (semAltDuXML.size() > 8){
                    return verifieTypeAlternance(semAltDuXML);
                } else {
                    return 0;
                }
            }
        }
    }

    /**
     * Récupère le contenu des horaires configurés en base de données pour l'établissement.
     * @param idEtab : l'établissement dont on extrait les horaires.
     * @return horaires la liste des horaires.
     * @throws MetierException : l'exception métier.
     */
    @Transactional(readOnly = true)
    private List<HoraireDTO> recupereListeHoraireEtablissement(Integer idEtab) throws MetierException{
        final String horaireBdd = etablissementHibernateBusinessService.findHorairesCoursEtablissement(idEtab).getValeurDTO();
        final String[] listePlages = horaireBdd.split("\\|"); // on se retrouve avec 08:00#09:00#true
        final List<HoraireDTO> horaires = new ArrayList<HoraireDTO>();
        for(String s : listePlages){
            final HoraireDTO h = new HoraireDTO();
            final String[] heures = s.split("#");
            final String[] debut = heures[0].split(":");
            final String[] fin = heures[1].split(":");
            h.setHeureDebut(debut[0]);
            h.setMinuteDebut(debut[1]);
            h.setHeureFin(fin[0]);
            h.setMinuteFin(fin[1]);
            horaires.add(h);
        }
        return horaires;
    }

    /**
     * Méthode qui permet de s'assurer de la correspondance entre les deux fichiers XML (basé sur leur UAJ).
     * @param stsedt : le document stsedt.
     * @param edtsts : le document edtsts.
     * @return un booléen qui indique true si les deux fichiers correspondent entre eux.
     */
    private Boolean verifieCorrespondanceFichiers(Document stsedt, Document edtsts){
        final Element racinestsedt = stsedt.getRootElement();
        if(racinestsedt.getName().equals("STS_EDT")){
            final Element racineedtsts = edtsts.getRootElement();
            if(racineedtsts.getName().equals("EDT_STS")){
                return recupereUAJSTSEDT(racinestsedt).equals(recupereUAJEDTSTS(racineedtsts));
            }else{
                return false;
            }
        }else{
            return false;
        }
    }

    /**
     * {@inheritDoc}
     */
    public void deleteEDTEtablissement(PeriodeEdtQO periodeEdtQO){
        emploiHibernateBusinessService.deleteEmploiDuTempsEtablissement(periodeEdtQO);
    }

    /**
     * Permet de savoir si l'alternance ressemble à un trimestre ou un semestre de départ.
     * @param alts : la liste des semaines d'alternance.
     * @return 3 si c'est une alternance continue de début d'année, 0 sinon.
     */
    private Integer verifieTypeAlternance(List<AlternanceSemaineDTO> alts){
        final List<Integer> mesNumSemaine = new ArrayList<Integer>();
        Integer compteur = 0;
        for(AlternanceSemaineDTO alt : alts){
            mesNumSemaine.add(alt.getNumeroSemaine());
            if(compteur==10){ break; }
            compteur++;
        }
        Collections.sort(mesNumSemaine);
        if(mesNumSemaine.get(0)>= NUMSEMAINEDEBUTANNEE && mesNumSemaine.get(0)<= NUMSEMAINEFINDEBUTANNEE){
            Integer total = 0;
            for(int i = 0; i<7; i++){
                total += (mesNumSemaine.get(i+1) - mesNumSemaine.get(i)) - 1;
            }
            if( total < 3 ){
                //Avec les possibles semaines de vacances qu'il pourrait y avoir... on considère que c'est continu.
                return 3;
            }else{
                return 0;
            }
        }
        return 0;
    }

    /**
     * Retourne le nombre de case emploi présentes en bdd pour cet établissement.
     * @param periodeEdtQO :
     *    idEtablissement : id de l'établissement.
     *    dateDebut de la periode
     * @return le nombre d'enregistrements présents.
     */
    @Transactional(readOnly = true)
    public Integer checkNombreCaseEmploiPourEtablissement(PeriodeEdtQO periodeEdtQO){
        return emploiHibernateBusinessService.checkNombreCaseEmploiPourEtablissement(periodeEdtQO);
    }

    /**
     * Méthode qui va écrire dans le fichier prévu à cet effet en rajoutant l'entête DOCTYPE qui fait référence au DTD.
     * @param codeType : permet de determiner si c'est un STS_EDT ou un EDT_STS.
     * @param fichierUpload le fichier uploadé.
     * @param pathAppli le chemin de l'application.
     * @return le chemin du fichier crée.
     */
    private String recreeFichierAvecEntete(Integer codeType, File fichierUpload, String pathAppli) {
        BufferedReader buff = null;
        BufferedWriter bw = null;
        String pathFichierCree = null;
        try {
            if(codeType==1){    //Code pour un fichier STS_EDT.
                log.info("Création d'un fichier fichierSTS_EDTxxx....xml");

                final Date da = new Date();
                final File monFichier = new File(pathAppli + "importEDT" + java.io.File.separator + "fichierSTS_EDT"+da.getTime()+".xml");
                pathFichierCree = monFichier.getPath();
                final Boolean vraiOuFauxFichierCree = monFichier.createNewFile();

                if (vraiOuFauxFichierCree) {
                    log.info("Le fichier est créé ! " + monFichier.getAbsolutePath());
                    final String docType = "<!DOCTYPE STS_EDT SYSTEM '" + DTDSTSEDT + "'>";
                    final String prologue = "<?xml version=\"1.0\" encoding=\"ISO-8859-15\" ?>";

                    final InputStreamReader isr = new InputStreamReader(new FileInputStream(fichierUpload), "ISO-8859-15");
                    buff = new BufferedReader(isr);
                    bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(monFichier), "ISO-8859-15"));

                    String maLigne;
                    boolean faireTest = true;
                    String newligne = "";
                    while ((maLigne = buff.readLine()) != null) {
                        if(faireTest){
                            if(maLigne.contains("<STS_EDT>")){
                                if(maLigne.length()>9){
                                    final String[] theLigne = maLigne.split("<STS_EDT>");
                                    if(theLigne.length==1){
                                        newligne = prologue + docType + "<STS_EDT>";
                                    }else{
                                        newligne = prologue + docType + "<STS_EDT>" + theLigne[1];
                                    }
                                }else{
                                    newligne = prologue + docType + "<STS_EDT>";
                                }
                                bw.write(newligne);
                                faireTest = false;
                            }
                        }else{
                            bw.newLine(); bw.write(maLigne); 
                        }
                    }
                    bw.flush();
                } else {
                    throw new CrlrRuntimeException("Echec de création du fichier de traitement");
                }
            } else if(codeType==2){  //Code pour un fichier EDT_STS.
                log.info("Création d'un fichierEDT_STSxxx....xml");

                final Date da = new Date();
                final File monFichier = new File(pathAppli + "importEDT" + java.io.File.separator + "fichierEDT_STS"+da.getTime()+".xml");
                pathFichierCree = monFichier.getPath();
                final Boolean vraiOuFauxFichierCree = monFichier.createNewFile();

                if (vraiOuFauxFichierCree) {

                    log.info("Le fichier est créé ! " + monFichier.getAbsolutePath());
                    final String docType = "<!DOCTYPE EDT_STS SYSTEM '" + DTDEDTSTS + "'>";
                    final String prologue = "<?xml version=\"1.0\" encoding=\"ISO-8859-15\" ?>";

                    final InputStreamReader isr = new InputStreamReader(new FileInputStream(fichierUpload), "ISO-8859-15");
                    buff = new BufferedReader(isr);
                    bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(monFichier), "ISO-8859-15"));

                    String maLigne;
                    boolean faireTest = true;
                    String newligne = "";
                    while ((maLigne = buff.readLine()) != null) {
                        if(faireTest){
                            if(maLigne.contains("<EDT_STS>")){
                                if(maLigne.length()>9){
                                    final String[] theLigne = maLigne.split("<EDT_STS>");
                                    if(theLigne.length==1){
                                        newligne = prologue + docType + "<EDT_STS>";
                                    }else{
                                        newligne = prologue + docType + "<EDT_STS>" + theLigne[1];
                                    }
                                }else{
                                    newligne = prologue + docType + "<EDT_STS>";
                                }
                                bw.write(newligne);
                                faireTest = false;
                            }
                        }else{
                            bw.newLine(); bw.write(maLigne); 
                        }
                    }
                    bw.flush();
                } else {
                    throw new CrlrRuntimeException("Echec de création du fichier de traitement");
                }
            } else {
                throw new CrlrRuntimeException("Le code type spécifié n'existe pas ! ");
            }
        } catch (IOException e1) {               
            throw new CrlrRuntimeException("Echec de la lecture des fichiers xml");
        } finally{
            if (bw != null) {
                try {
                    bw.close();
                } catch (IOException e) {
                    throw new CrlrRuntimeException("Echec de fermeture du flux ");
                }
            }
            if (buff != null) {
                try {
                    buff.close();
                } catch (IOException e) {
                    throw new CrlrRuntimeException("Echec de fermeture du flux ");
                }
            }
        }    
        return pathFichierCree;
    }

    /**
     * Vérifie les autorisations de l'enseignant sur la classe et l'enseignement pour cet établissement.
     * @param idEtab : id de l'établissement.
     * @param idEns : id de l'enseignant.
     * @param idMatiere : id de l'enseignement.
     * @param idClasse : id de la classe.
     * @param caracEtabImportDTO : Le DTO contenant les informations pour l'établissemement.
     * @return un booléen pour dire s'il est autorisé ou non.
     */
    @Transactional(readOnly = true)
    private boolean verifieAutorisationEnseignantClasse(Integer idEtab, Integer idEns, Integer idMatiere, Integer idClasse, 
            CaracEtabImportDTO caracEtabImportDTO){
        // l'enseignant est-il autorisé à enseigner cette matière dans l'établissement ?
        if(enseignantHibernateBusinessService.verifieAutorisationEnseignement(idEtab, idEns, idMatiere)){ 
            if(enseignantHibernateBusinessService.verifieEnseignantClasse(idEns, idClasse)){  // L'enseignant enseigne à cette classe ?
                if(caracEtabImportDTO.getMapDivision().get(idClasse) != null){     // La classe appartient bien à l'établissement ?
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Vérifie les autorisations de l'enseignant sur le groupe et l'enseignement pour cet établissement.
     * @param idEtab : id de l'établissement.
     * @param idEns : id de l'enseignant.
     * @param idMatiere : id de l'enseignement.
     * @param idGroupe : id du groupe.
     * @param caracEtabImportDTO : Le DTO contenant les informations pour l'établissemement.
     * @return un booléen pour dire s'il est autorisé ou non.
     */
    @Transactional(readOnly = true)
    private boolean verifieAutorisationEnseignantGroupe(Integer idEtab, Integer idEns, Integer idMatiere, Integer idGroupe, 
            CaracEtabImportDTO caracEtabImportDTO){
        // l'enseignant est-il autorisé à enseigner cette matière dans l'établissement ?
        if(enseignantHibernateBusinessService.verifieAutorisationEnseignement(idEtab, idEns, idMatiere)){ 
            if(enseignantHibernateBusinessService.verifieEnseignantGroupe(idEns, idGroupe)){  // L'enseignant enseigne à ce groupe ?
                if(caracEtabImportDTO.getMapGroupe().get(idGroupe) != null){     // Le groupe appartient bien à l'établissement ?
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */    
    public void insertionCases(List<EmploiDTO> casesSimples, Date dateDebut){
        try{
            // insertion pour les cases simples
            emploiHibernateBusinessService.saveCasesEmploiSansFusion(casesSimples, dateDebut);
            log.info("Insertion des {0} cases simples terminées !", casesSimples.size());

        }finally{   //Si une erreur survient, on veut être certain de remettre le flag import à FALSE.
            Integer idEtab; // Une fois l'insertion terminée, on remet le flag import à FALSE.
            if(!casesSimples.isEmpty()){
                idEtab = casesSimples.get(0).getIdEtablissement();
                etablissementHibernateBusinessService.modifieStatutImportEtablissement(idEtab, false);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public Boolean findEtatImportEtablissement(Integer idEtablissement) {
        return etablissementHibernateBusinessService.findEtatImportEtablissement(idEtablissement);
    }

    /**
     * {@inheritDoc}
     */
    public void modifieStatutImportEtablissement(Integer idEtablissement,
            Boolean statut) {
        etablissementHibernateBusinessService.modifieStatutImportEtablissement(idEtablissement, statut);
    }

    /**
     * {@inheritDoc}
     */
    public String checkDateImportEtablissement(Integer idEtablissement) {
        return etablissementHibernateBusinessService.checkDateImportEtablissement(idEtablissement);
    }    
}
