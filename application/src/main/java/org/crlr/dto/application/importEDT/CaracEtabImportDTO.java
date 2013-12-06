/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: jalopy.xml,v 1.1 2009/03/17 16:30:44 ent_breyton Exp $
 */

package org.crlr.dto.application.importEDT;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.crlr.dto.application.base.UtilisateurDTO;
import org.crlr.importEDT.DTO.AlternanceSemaineDTO;
import org.crlr.importEDT.DTO.EmploiDTO;
import org.crlr.importEDT.DTO.HoraireDTO;
import org.crlr.importEDT.DTO.IndividuDTO;
import org.crlr.importEDT.DTO.MatiereDTO;
import org.crlr.importEDT.DTO.NomPrenomDTO;
import org.crlr.importEDT.DTO.PrintEDTDTO;

/**
 * DTO qui permet de stocker les informations qui concernent l'import EDT pour un établissement.
 *
 * @author jp.mandrick
 * @version $Revision: 1.1 $
  */
public class CaracEtabImportDTO implements Serializable {
    /** serial id généré. */
    private static final long serialVersionUID = 3951095836363610744L;
    
    /** Répertorie les différentes alternances présentées dans le XML et leur attribut une valeur de 0 à 3.
     * Signification des valeurs : 0 pas configuré en base de données. 1 et 2 pour les alternances. 
     * 3 pour hebdomadaire, donc en semaine 1 + semaine 2.  */
    private List<AlternanceSemaineDTO> significationAlternance = new ArrayList<AlternanceSemaineDTO>();
    
    /** permet de savoir si oui on non on est en alternance (et donc H = 1 et 2). */
    private Boolean edtAlternance;
    
    /** Date de debut de la periode. */
    private Date dateDebutPeriode;
    
    /** La grille horaire configurée pour l'établissement. */
    private List<HoraireDTO> lesHoraires = new ArrayList<HoraireDTO>();
    
    /** Id de l'établissement sur lequel on navigue. */
    private Integer idEtablissement;
    
    /** les id des groupes de l'etablissement. */
    private List<Integer> lesIdGroupes = new ArrayList<Integer>();
    
    /** les id des classes de l'etablissement. */
    private List<Integer> lesIdClasses = new ArrayList<Integer>();
    
    /** le DTO qui permet de stocker toutes les anomalies. */
    private PrintEDTDTO printEDTDTO = new PrintEDTDTO();
    
    /** La liste des individus. */
    private List<IndividuDTO> lesIndividusDTO = new ArrayList<IndividuDTO>();
    
    /** La liste des matières. */
    private List<MatiereDTO> lesMatieres = new ArrayList<MatiereDTO>();
    
    /** Liste des cases emplois du temps à insérer sans fusion. */
    private List<EmploiDTO> casesSimples = new ArrayList<EmploiDTO>();
    
    /** map des divisions. */
    private Map<Integer, Integer> mapDivision;
    
    /** map des groupes. */
    private Map<Integer, Integer> mapGroupes;
    
    /** la map des matières de l'établissement. */
    private Map<String, String> mapMatiere;
    
    /** map des enseignants. */
    private Map<String, NomPrenomDTO> mapEnseignant;
    
    /** map des alternances. */
    private Map<String, String> mapAlternance;
    
    /** map des horaires configurés pour l'établissement. */
    private Map<HoraireDTO, String> mapHoraire;
    
    /** map des horaires de début face aux horaires de fin. */
    private Map<String, String> mapHeureDebutHeureFin;
    
    /** Utilisateur administrateur qui lance l'import. */
    private UtilisateurDTO utilisateurDTO;
    
    /**
     * Accesseur de MapDivision.
     * @return mapDivision
     */
    public Map<Integer, Integer> getMapDivision(){
        if(mapDivision == null){
            mapDivision = new HashMap<Integer, Integer>();
            for(Integer id : this.getLesIdClasses()){
                mapDivision.put(id, id);
            }
        }
        return mapDivision;
    }
    
    /**
     * Accesseur de mapGroupes.
     * @return mapGroupes
     */
    public Map<Integer, Integer> getMapGroupe(){
        if(mapGroupes == null){
            mapGroupes = new HashMap<Integer, Integer>();
            for(Integer id : this.getLesIdGroupes()){
                mapGroupes.put(id, id);
            }
        }
        return mapGroupes;
    }
    
    /**
     * Accesseur de mapEnseignant.
     * @return mapEnseignant
     */
    public Map<String, NomPrenomDTO> getMapEnseignant(){
        if(mapEnseignant == null){
            mapEnseignant = new HashMap<String, NomPrenomDTO>();
            for(IndividuDTO i : this.getLesIndividusDTO()){
                mapEnseignant.put(i.getId(), new NomPrenomDTO(i.getNom(), i.getPrenom()));
            }
        }
        return mapEnseignant;
    }
    
    /**
     * Accesseur de mapMatiere.
     * @return mapMatiere.
     */
    public Map<String, String> getMapMatiere(){
        if(mapMatiere == null){
            mapMatiere = new HashMap<String, String>();
            for(MatiereDTO mat : this.getLesMatieres()){
                mapMatiere.put(mat.getCode(), mat.getLibelleLong());
            }
        }
        return mapMatiere;
    }
    
    /**
     * Accesseur de mapAlternance.
     * @return mapAlternance.
     */
    public Map<String, String> getMapAlternance(){
        if(mapAlternance == null){
            mapAlternance = new HashMap<String, String>();
            for(AlternanceSemaineDTO a : this.getSignificationAlternance()){
                mapAlternance.put(a.getTypeSemaine(), String.valueOf(a.getNumeroSemaine()));
            }
        }
        return mapAlternance;
    }
    
    /**
     * Accesseur de mapHoraire.
     * @return l'heure de début pour voir si elle est présente.
     */
    public Map<HoraireDTO, String> getMapHoraire(){
        if(mapHoraire == null){
            mapHoraire = new HashMap<HoraireDTO, String>();
            for(HoraireDTO horaire : this.getLesHoraires()){
                mapHoraire.put(horaire, horaire.getHeureDebut());
            }
        }
        return mapHoraire;
    }
    
    /**
     * Accesseur de mapHeureDebutHeureFin.
     * @return une map qui permet de connaitre l'heure de fin en fonction de l'heure de début.
     */
    public Map<String, String> getMapHeureDebutHeureFin(){
        if(mapHeureDebutHeureFin == null){
            mapHeureDebutHeureFin = new HashMap<String, String>();
            for(HoraireDTO horaire : this.getLesHoraires()){
                mapHeureDebutHeureFin.put(horaire.getHeureDebut()+horaire.getMinuteDebut(), horaire.getHeureFin()+horaire.getMinuteFin());
            }
        }
        return mapHeureDebutHeureFin;
    }
    
    /**
     * Accesseur de printEDTDTO.
     * @return le printEDTDTO
     */
    public PrintEDTDTO getPrintEDTDTO() {
        return printEDTDTO;
    }
    /**
     * Modificateur de printEDTDTO.
     * @param printEDTDTO le printEDTDTO à modifier
     */
    public void setPrintEDTDTO(PrintEDTDTO printEDTDTO) {
        this.printEDTDTO = printEDTDTO;
    }
    /**
     * Accesseur de significationAlternance.
     * @return le significationAlternance
     */
    public List<AlternanceSemaineDTO> getSignificationAlternance() {
        return significationAlternance;
    }
    /**
     * Modificateur de significationAlternance.
     * @param significationAlternance le significationAlternance à modifier
     */
    public void setSignificationAlternance(
            List<AlternanceSemaineDTO> significationAlternance) {
        this.significationAlternance = significationAlternance;
    }
    /**
     * Accesseur de edtAlternance.
     * @return le edtAlternance
     */
    public Boolean getEdtAlternance() {
        return edtAlternance;
    }
    /**
     * Modificateur de edtAlternance.
     * @param edtAlternance le edtAlternance à modifier
     */
    public void setEdtAlternance(Boolean edtAlternance) {
        this.edtAlternance = edtAlternance;
    }
    /**
     * Accesseur de lesHoraires.
     * @return le lesHoraires
     */
    public List<HoraireDTO> getLesHoraires() {
        return lesHoraires;
    }
    /**
     * Modificateur de lesHoraires.
     * @param lesHoraires le lesHoraires à modifier
     */
    public void setLesHoraires(List<HoraireDTO> lesHoraires) {
        this.lesHoraires = lesHoraires;
    }
    /**
     * Accesseur de idEtablissement.
     * @return le idEtablissement
     */
    public Integer getIdEtablissement() {
        return idEtablissement;
    }

    /**
     * Modificateur de idEtablissement.
     * @param idEtablissement le idEtablissement à modifier
     */
    public void setIdEtablissement(Integer idEtablissement) {
        this.idEtablissement = idEtablissement;
    }

    /**
     * Accesseur de lesIdGroupes.
     * @return le lesIdGroupes
     */
    public List<Integer> getLesIdGroupes() {
        return lesIdGroupes;
    }
    /**
     * Modificateur de lesIdGroupes.
     * @param lesIdGroupes le lesIdGroupes à modifier
     */
    public void setLesIdGroupes(List<Integer> lesIdGroupes) {
        this.lesIdGroupes = lesIdGroupes;
    }
    /**
     * Accesseur de lesIdClasses.
     * @return le lesIdClasses
     */
    public List<Integer> getLesIdClasses() {
        return lesIdClasses;
    }
    /**
     * Modificateur de lesIdClasses.
     * @param lesIdClasses le lesIdClasses à modifier
     */
    public void setLesIdClasses(List<Integer> lesIdClasses) {
        this.lesIdClasses = lesIdClasses;
    }
    /**
     * Accesseur de lesIndividusDTO.
     * @return le lesIndividusDTO
     */
    public List<IndividuDTO> getLesIndividusDTO() {
        return lesIndividusDTO;
    }
    /**
     * Modificateur de lesIndividusDTO.
     * @param lesIndividusDTO le lesIndividusDTO à modifier
     */
    public void setLesIndividusDTO(List<IndividuDTO> lesIndividusDTO) {
        this.lesIndividusDTO = lesIndividusDTO;
    }
    /**
     * Accesseur de lesMatieres.
     * @return le lesMatieres
     */
    public List<MatiereDTO> getLesMatieres() {
        return lesMatieres;
    }
    /**
     * Modificateur de lesMatieres.
     * @param lesMatieres le lesMatieres à modifier
     */
    public void setLesMatieres(List<MatiereDTO> lesMatieres) {
        this.lesMatieres = lesMatieres;
    }

    /**
     * Accesseur de casesSimples.
     * @return le casesSimples
     */
    public List<EmploiDTO> getCasesSimples() {
        return casesSimples;
    }

    /**
     * Modificateur de casesSimples.
     * @param casesSimples le casesSimples à modifier
     */
    public void setCasesSimples(List<EmploiDTO> casesSimples) {
        this.casesSimples = casesSimples;
    }

    /**
     * Accesseur de utilisateurDTO {@link #utilisateurDTO}.
     * @return retourne utilisateurDTO 
     */
    public UtilisateurDTO getUtilisateurDTO() {
        return utilisateurDTO;
    }

    /**
     * Mutateur de utilisateurDTO {@link #utilisateurDTO}.
     * @param utilisateurDTO the utilisateurDTO to set
     */
    public void setUtilisateurDTO(UtilisateurDTO utilisateurDTO) {
        this.utilisateurDTO = utilisateurDTO;
    }

    /**
     * Accesseur de dateDebutPeriode {@link #dateDebutPeriode}.
     * @return retourne dateDebutPeriode
     */
    public Date getDateDebutPeriode() {
        return dateDebutPeriode;
    }

    /**
     * Mutateur de dateDebutPeriode {@link #dateDebutPeriode}.
     * @param dateDebutPeriode le dateDebutPeriode to set
     */
    public void setDateDebutPeriode(Date dateDebutPeriode) {
        this.dateDebutPeriode = dateDebutPeriode;
    }
    
}
