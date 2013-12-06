/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: DevoirDTO.java,v 1.8 2010/04/21 09:04:38 ent_breyton Exp $
 */

package org.crlr.dto.application.devoir;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.crlr.dto.application.base.DateDTO;
import org.crlr.dto.application.base.SeanceDTO;
import org.crlr.dto.application.seance.ImageDTO;
import org.crlr.utils.DateUtils;
import org.crlr.web.dto.FileUploadDTO;

/**
 * DOCUMENTATION INCOMPLETE!
 *
 * @author $author$
 * @version $Revision: 1.8 $
 */
public class DevoirDTO implements Serializable {
    /**  */
    private static final long serialVersionUID = -3563623034560658131L;

    /** Id du devoir. */
    private Integer id;
    
    /** Id du devoir qui est en cours de modification. */
    private Integer idAncien; 

    /** Le code du devoir. */
    private String code;

    /** la date de remise du devoir. */
    private Date dateRemise;

    /** la date de remise du devoir. */
    //private DateDTO dateRemiseDTO;
    
    /** L'intitulé du devoir. */
    private String intitule;

    /** La description du devoir. */
    private String description;
    
    /** La description abregée sans balise pour un affichage en colonne (tableau). */
    private String descriptionSansBaliseAbrege;

    /** La description sans balise pour un affichage info bulle. */
    private String descriptionSansBalise;
    
    /** La description en HTML avec des images dragmath. */
    private String descriptionHTML;
    
    /** Liste des images DragMath. **/
    private List<ImageDTO> listeImages;
    
    /** Le type de devoir correspondant. */
    private TypeDevoirDTO typeDevoirDTO;
    
    /**
     * la séance du devoir.
     */
    private SeanceDTO seance;
    
    /** Id de la classe. */
    private Integer idClasse;
    
    /** Id du groupe. */
    private Integer idGroupe;
    

    /**
     * La liste des pièces jointes du devoir.
     */
    private List<FileUploadDTO> files;
    
    /** la ligne de resultat séléctionnée dans la liste des pièces jointes. */
    private FileUploadDTO pieceJointeSelectionne;
    
    
    /** Nom de l'enseignement. */
    private String nomEnseignant;
    
    /** Civilité de l'enseignement. */
    private String civiliteEnseignant;
    
    /** Designation de la classe (exclusif avec designationGroupe). */
    private String designationClasse;
    
    /** Designation de la classe (exclusif avec designationClasse). */
    private String designationGroupe;
    
    /** Libelle de la matiere correspondant à la seance. */
    private String matiere;
    
    /** Charge de travail à rendre pour le meme jour.*/ 
    private ChargeTravailDTO chargeTravail;
    
    private boolean open;
    
    
    /**
     * Génère la description abrégée visible dans le listing de devoir.
     *
     */
    public void generateDescriptionAbrege() {
        setDescriptionSansBaliseAbrege(org.crlr.utils.StringUtils.generateDescriptionSansBaliseAbrege(getDescription()));
        setDescriptionSansBalise(org.crlr.utils.StringUtils.generateDescriptionSansBalise(getDescription()));
    }

    /**
     * Constructeur par defaut.
     */
    public DevoirDTO() {
        super();
        chargeTravail = new ChargeTravailDTO();
        typeDevoirDTO = new TypeDevoirDTO();
        seance = new SeanceDTO();
        files = new ArrayList<FileUploadDTO>();
    }

    /**
     * Accesseur code.
     * @return code
     */
    public String getCode() {
        return code;
    }

    /**
     * Mutateur code.
     * @param code Le code à modifier
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * Accesseur dateRemise.
     * @return dateRemise
     */
    public Date getDateRemise() {
        return dateRemise;
    }

    /**
     * Mutateur dateRemiseCalendar.
     * @param dateRemise Le dateRemise à modifier
     */
    public void setDateRemiseCalendar(Date dateRemise) {
    }

    /** Accesseur dateRemise.
    * @return dateRemise
    */
   public Date getDateRemiseCalendar() {
       return dateRemise;
   }

   /**
    * Mutateur dateRemise.
    * @param dateRemise Le dateRemise à modifier
    */
   public void setDateRemise(Date dateRemise) {
       this.dateRemise = dateRemise;
   }
    
    /**
     * Accesseur dateRemiseDTO.
     * @return dateRemiseDTO
     */
    public DateDTO getDateRemiseDTO() {
        return new DateDTO(dateRemise);
    }

    /**
     * Mutateur dateRemiseDTO.
     * @param dateRemiseDTO Le dateRemise à modifier
     */
    public void setDateRemiseDTO(DateDTO dateRemiseDTO) {
    }
    
    /**
     * Accesseur de dateRemiseString {@link #dateRemiseString}.
     * @return retourne dateRemiseString
     */
    public String getDateRemiseString() {
        return DateUtils.format(dateRemise);
    }

    /**
     * Mutateur de dateRemiseString {@link #dateRemiseString}.
     * @param dateRemiseString le dateRemiseString to set
     */
    public void setDateRemiseString(String dateRemiseString) {
        if (dateRemiseString != null && dateRemiseString .length()>0) {
            this.dateRemise = DateUtils.parse(dateRemiseString);
        } else {
            this.dateRemise = null;
        }
    }

    /**
     * Accesseur intitule.
     * @return intitule
     */
    public String getIntitule() {
        return intitule;
    }

    /**
     * Mutateur intitule.
     * @param intitule Le intitule à modifier
     */
    public void setIntitule(String intitule) {
        this.intitule = intitule;
    }

    /**
     * Accesseur description.
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Mutateur description.
     * Transforme le contenu de la description au cas où il y aurait des liens pour s'assurer qu'ils s'ouvrent dans une autre page.
     * @param description Le description à modifier
     */
    public void setDescription(String description) {
    	String[] monSplit = description.split("<a");
    	if(monSplit.length>1){
    		try{
    			String maDescModifie = monSplit[0];
    			// il y a un lien dans la description.
    			for(int i = 0;i<monSplit.length;i++){
    				if(i>0){
    					if(monSplit[i].contains("onclick=\"window.open(this.href); return false;")){
    						// Le onclick est déja présent. On le garde et on rajoute que le <a devant.
    						maDescModifie += "<a "+monSplit[i];
    					}else{
    						maDescModifie += "<a onclick=\"window.open(this.href); return false;\" "+monSplit[i]; 
    					}    					
    				}
    			}
    			this.description = maDescModifie;
    		}catch(Exception e){
    			this.description = description;
    			e.printStackTrace();
    		}
    	}else{
    		this.description = description;
    	}        
    }

    /**
     * Accesseur id.
     *
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * Mutateur id.
     *
     * @param id id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    
    /**
     * Accesseur files.
     * @return files
     */
    public List<FileUploadDTO> getFiles() {
        return files;
    }

    /**
     * Mutateur files.
     * @param files Le files à modifier
     */
    public void setFiles(List<FileUploadDTO> files) {
        this.files = files;
    }

    /**
     * Accesseur idSeance.
     * @return idSeance
     */
    public Integer getIdSeance() {
        return seance.getId();
    }

    /**
     * Mutateur idSeance.
     * @param idSeance Le idSeance à modifier
     */
    public void setIdSeance(Integer idSeance) {
        seance.setId(idSeance);
    }


    /**
     * Accesseur descriptionSansBaliseAbrege.
     * @return le descriptionSansBaliseAbrege
     */
    public String getDescriptionSansBaliseAbrege() {
        return descriptionSansBaliseAbrege;
    }

    /**
     * Mutateur de descriptionSansBaliseAbrege.
     * @param descriptionSansBaliseAbrege le descriptionSansBaliseAbrege à modifier.
     */
    public void setDescriptionSansBaliseAbrege(String descriptionSansBaliseAbrege) {
        this.descriptionSansBaliseAbrege = descriptionSansBaliseAbrege;
    }

    
    /**
     * Accesseur descriptionSansBalise.
     * @return descriptionSansBalise
     */
    public String getDescriptionSansBalise() {
        return descriptionSansBalise;
    }

    /**
     * Muttateur descriptionSansBalise.
     * @param descriptionSansBalise descriptionSansBalise
     */
    public void setDescriptionSansBalise(String descriptionSansBalise) {
        this.descriptionSansBalise = descriptionSansBalise;
    }

    /**
     * Accesseur de open.
     * @return le open
     */
    public Boolean getOpen() {
        return open;
    }

    /**
     * Mutateur de open.
     * @param open le open à modifier.
     */
    public void setOpen(Boolean open) {
        this.open = open;
    }

 

    /**
     * Accesseur de idClasse.
     * @return the idClasse
     */
    public Integer getIdClasse() {
        return idClasse;
    }

    /**
     * Mutateur de idClasse.
     * @param idClasse the idClasse to set
     */
    public void setIdClasse(Integer idClasse) {
        this.idClasse = idClasse;
    }

    /**
     * Accesseur idGroupe.
     * @return the idGroupe 
     */
    public Integer getIdGroupe() {
        return idGroupe;
    }

    /**
     * Mutateur de idGroupe.
     * @param idGroupe the idGroupe to set
     */
    public void setIdGroupe(Integer idGroupe) {
        this.idGroupe = idGroupe;
    }

    /**
     * Accesseur de idAncien.
     * @return the idAncien
     */
    public Integer getIdAncien() {
        return idAncien;
    }

    /**
     * Mutateur de idAncien.
     * @param idAncien the idAncien to set
     */
    public void setIdAncien(Integer idAncien) {
        this.idAncien = idAncien;
    }

    /**
     * Accesseur de pieceJointeSelectionne.
     * @return le pieceJointeSelectionne
     */
    public FileUploadDTO getPieceJointeSelectionne() {
        return pieceJointeSelectionne;
    }

    /**
     * Mutateur de pieceJointeSelectionne.
     * @param pieceJointeSelectionne le pieceJointeSelectionne à modifier.
     */
    public void setPieceJointeSelectionne(FileUploadDTO pieceJointeSelectionne) {
        this.pieceJointeSelectionne = pieceJointeSelectionne;
    }

    /**
     * Accesseur de dateSeance.
     * @return le dateSeance
     */
    public Date getDateSeance() {
        return seance.getDate();
    }

    /**
     * Mutateur de dateSeance.
     * @param dateSeance le dateSeance à modifier.
     */
    public void setDateSeance(Date dateSeance) {
        seance.setDate(dateSeance);
    }

    /**
     * Accesseur de nomEnseignant.
     * @return le nomEnseignant
     */
    public String getNomEnseignant() {
        return nomEnseignant;
    }

    /**
     * Mutateur de nomEnseignant.
     * @param nomEnseignant le nomEnseignant à modifier.
     */
    public void setNomEnseignant(String nomEnseignant) {
        this.nomEnseignant = nomEnseignant;
    }

    /**
     * Accesseur de civiliteEnseignant.
     * @return le civiliteEnseignant
     */
    public String getCiviliteEnseignant() {
        return civiliteEnseignant;
    }

    /**
     * Mutateur de civiliteEnseignant.
     * @param civiliteEnseignant le civiliteEnseignant à modifier.
     */
    public void setCiviliteEnseignant(String civiliteEnseignant) {
        this.civiliteEnseignant = civiliteEnseignant;
    }

    /**
     * Accesseur de designationClasse.
     * @return le designationClasse
     */
    public String getDesignationClasse() {
        return designationClasse;
    }

    /**
     * Mutateur de designationClasse.
     * @param designationClasse le designationClasse à modifier.
     */
    public void setDesignationClasse(String designationClasse) {
        this.designationClasse = designationClasse;
    }

    /**
     * Accesseur de designationGroupe.
     * @return le designationGroupe
     */
    public String getDesignationGroupe() {
        return designationGroupe;
    }

    /**
     * Mutateur de designationGroupe.
     * @param designationGroupe le designationGroupe à modifier.
     */
    public void setDesignationGroupe(String designationGroupe) {
        this.designationGroupe = designationGroupe;
    }

    /**
     * Accesseur de matiere.
     * @return le matiere
     */
    public String getMatiere() {
        return matiere;
    }

    /**
     * Mutateur de matiere.
     * @param matiere le matiere à modifier.
     */
    public void setMatiere(String matiere) {
        this.matiere = matiere;
    }

    /**
     * Accesseur de typeDevoirDTO {@link #typeDevoirDTO}.
     * Positionne par defaut un objet typeDevoirDTO pour que les controleur/form qui accede à la catégorie
     * de type de devoir ne soient pas obligé de faire des tests sur le null du type de devoir.
     * @return retourne typeDevoirDTO
     */
    public TypeDevoirDTO getTypeDevoirDTO() {
        if (typeDevoirDTO == null) {
            typeDevoirDTO = new TypeDevoirDTO();
        }
        return typeDevoirDTO;
    }

    /**
     * Mutateur de typeDevoirDTO {@link #typeDevoirDTO}.
     * @param typeDevoirDTO le typeDevoirDTO to set
     */
    public void setTypeDevoirDTO(TypeDevoirDTO typeDevoirDTO) {
        this.typeDevoirDTO = typeDevoirDTO;
    }

    /**
     * Accesseur de chargeTravail {@link #chargeTravail}.
     * @return retourne chargeTravail
     */
    public ChargeTravailDTO getChargeTravail() {
        return chargeTravail;
    }

    /**
     * Mutateur de chargeTravail {@link #chargeTravail}.
     * @param chargeTravail le chargeTravail to set
     */
    public void setChargeTravail(ChargeTravailDTO chargeTravail) {
        this.chargeTravail = chargeTravail;
    }

    /**
     * @return the seance
     */
    public SeanceDTO getSeance() {
        return seance;
    }

    /**
     * @param seance the seance to set
     */
    public void setSeance(SeanceDTO seance) {
        this.seance = seance;
    }

    /**
     * @return the descriptionHTML
     */
    public String getDescriptionHTML() {
        return descriptionHTML;
    }

    /**
     * @param descriptionHTML the descriptionHTML to set
     */
    public void setDescriptionHTML(String descriptionHTML) {
        this.descriptionHTML = descriptionHTML;
    }

    /**
     * @return the listeImages
     */
    public List<ImageDTO> getListeImages() {
        return listeImages;
    }

    /**
     * @param listeImages the listeImages to set
     */
    public void setListeImages(List<ImageDTO> listeImages) {
        this.listeImages = listeImages;
    }
    
    /**
     * Methode invoque cote IHM lors de la raz de la date de remise du devoir.
     */
    public void resetDateRemise() {
       this.dateRemise = null; 
    }
}
