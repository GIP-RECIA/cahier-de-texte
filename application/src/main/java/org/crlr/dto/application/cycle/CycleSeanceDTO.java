package org.crlr.dto.application.cycle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.crlr.alimentation.DTO.EnseignantDTO;
import org.crlr.dto.application.base.UtilisateurDTO;
import org.crlr.utils.StringUtils;
import org.crlr.web.contexte.utils.ContexteUtils;
import org.crlr.web.dto.FileUploadDTO;

/**
 * Seance creee dans une sequence pedagogique.
 * @author G-SAFIR-FRMP
 */
public class CycleSeanceDTO  implements Serializable {

    /** Serial. */
    private static final long serialVersionUID = -4688476089398143494L;

    /** Identifiant. */
    private Integer id;

    /** Id du Cycle. */
    private Integer idCycle;

    /** Enseignant createur de la seance pedagogique. */
    private EnseignantDTO enseignantDTO;

    /** Libelle de l'enseignement. */
    private String enseignement;

    /** Intitule. */
    private String intitule;
    
    /** Objectif. */
    private String objectif;
    
    /** Description. */
    private String description;
    
    /** Description au format HTML. */
    private String descriptionHTML;
    
    /** Annotations personnelles. */
    private String annotations;

    /** Annotations personnelles au format HTML. */
    private String annotationsHTML;
    
    /** Annotations visible ? */
    private Boolean annotationsVisible;
    
    /** Indice (numero d'ordre de la seance. */
    private Integer indice;
    
    /** Liste des devoirs. */
    private List<CycleDevoirDTO> listeCycleDevoir;
    
    /** Liste des piece jointe. */
   private List<FileUploadDTO> listePieceJointe;

   /**
    * Constructeur par defaut.
    */
    public CycleSeanceDTO() {
        super();
        listeCycleDevoir = new ArrayList<CycleDevoirDTO>();
        listePieceJointe = new ArrayList<FileUploadDTO>();
    }

/**
 * Accesseur de id {@link #id}.
 * @return retourne id
 */
public Integer getId() {
    return id;
}

/**
 * Mutateur de id {@link #id}.
 * @param id le id to set
 */
public void setId(Integer id) {
    this.id = id;
}



/**
 * Accesseur de idCycle {@link #idCycle}.
 * @return retourne idCycle
 */
public Integer getIdCycle() {
    return idCycle;
}

/**
 * Mutateur de idCycle {@link #idCycle}.
 * @param idCycle le idCycle to set
 */
public void setIdCycle(Integer idCycle) {
    this.idCycle = idCycle;
}

/**
 * Accesseur de enseignantDTO {@link #enseignantDTO}.
 * @return retourne enseignantDTO
 */
public EnseignantDTO getEnseignantDTO() {
    return enseignantDTO;
}

/**
 * Mutateur de enseignantDTO {@link #enseignantDTO}.
 * @param enseignantDTO le enseignantDTO to set
 */
public void setEnseignantDTO(EnseignantDTO enseignantDTO) {
    this.enseignantDTO = enseignantDTO;
}

/**
 * Accesseur de enseignement {@link #enseignement}.
 * @return retourne enseignement
 */
public String getEnseignement() {
    return enseignement;
}

/**
 * Mutateur de enseignement {@link #enseignement}.
 * @param enseignement le enseignement to set
 */
public void setEnseignement(String enseignement) {
    this.enseignement = enseignement;
}

/**
 * Accesseur de intitule {@link #intitule}.
 * @return retourne intitule
 */
public String getIntitule() {
    return intitule;
}

/**
 * Mutateur de intitule {@link #intitule}.
 * @param intitule le intitule to set
 */
public void setIntitule(String intitule) {
    this.intitule = intitule;
}

/**
 * Accesseur de objectif {@link #objectif}.
 * @return retourne objectif
 */
public String getObjectif() {
    return objectif;
}
/**
 * Accesseur de objectif {@link #objectif} en version courte pour etre affichée
 * dans une liste.
 * @return retourne objectif
 */
public String getObjectifCourt() {
    return StringUtils.truncateHTMLString(objectif.replaceAll("\n","<br/>"), 100);
}

/**
 * Mutateur de objectif {@link #objectif}.
 * Transforme le contenu de la description au cas où il y aurait des liens pour s'assurer qu'ils s'ouvrent dans une autre page.
 * @param objectif le objectif to set
 */
public void setObjectif(String objectif) {
    this.objectif = StringUtils.protegeLienDansTexteHtml(objectif);
}


/**
 * Accesseur de description {@link #description}.
 * @return retourne description
 */
public String getDescription() {
    return description;
}

/**
 * Accesseur de description {@link #description} en version courte sans HTML.
 * Les descriptions contenant des balises img posent pb si la ciscion intervient dans la balise (autofermente)
 * Notemment cela arrive sur les champs latex qui sont transformes en img.   
 * @return retourne description 
 */
public String getDescriptionCourt() {
    return org.crlr.utils.StringUtils.generateDescriptionSansBaliseAbrege(description); 
}


/**
 * Mutateur de description {@link #description}.
 * Transforme le contenu de la description au cas où il y aurait des liens pour s'assurer qu'ils s'ouvrent dans une autre page.
 * @param description le description to set
 */
public void setDescription(String description) {
    this.description = StringUtils.protegeLienDansTexteHtml(description);
}

/**
 * Accesseur de descriptionHTML {@link #descriptionHTML}.
 * @return retourne descriptionHTML
 */
public String getDescriptionHTML() {
    return descriptionHTML;
}

/**
 * Mutateur de descriptionHTML {@link #descriptionHTML}.
 * @param descriptionHTML le descriptionHTML to set
 */
public void setDescriptionHTML(String descriptionHTML) {
    this.descriptionHTML = descriptionHTML;
}

/**
 * Accesseur de annotations {@link #annotations}.
 * @return retourne annotations
 */
public String getAnnotations() {
    return annotations;
}

/**
 * Mutateur de annotations {@link #annotations}.
 * Transforme le contenu de la description au cas où il y aurait des liens pour s'assurer qu'ils s'ouvrent dans une autre page.
 * @param annotations le annotations to set
 */
public void setAnnotations(String annotations) {
    this.annotations = StringUtils.protegeLienDansTexteHtml(annotations);
}

/**
 * Accesseur de annotationsHTML {@link #annotationsHTML}.
 * @return retourne annotationsHTML
 */
public String getAnnotationsHTML() {
    return annotationsHTML;
}

/**
 * Mutateur de annotationsHTML {@link #annotationsHTML}.
 * @param annotationsHTML le annotationsHTML to set
 */
public void setAnnotationsHTML(String annotationsHTML) {
    this.annotationsHTML = annotationsHTML;
}

/**
 * Accesseur de annotationsVisible {@link #annotationsVisible}.
 * @return retourne annotationsVisible
 */
public Boolean getAnnotationsVisible() {
    return annotationsVisible;
}

/**
 * Mutateur de annotationsVisible {@link #annotationsVisible}.
 * @param annotationsVisible le annotationsVisible to set
 */
public void setAnnotationsVisible(Boolean annotationsVisible) {
    this.annotationsVisible = annotationsVisible;
}

/**
 * Accesseur de listeCycleDevoir {@link #listeCycleDevoir}.
 * @return retourne listeCycleDevoir
 */
public List<CycleDevoirDTO> getListeCycleDevoir() {
    return listeCycleDevoir;
}

/**
 * Mutateur de listeCycleDevoir {@link #listeCycleDevoir}.
 * @param listeCycleDevoir le listeCycleDevoir to set
 */
public void setListeCycleDevoir(List<CycleDevoirDTO> listeCycleDevoir) {
    this.listeCycleDevoir = listeCycleDevoir;
}

/**
 * Accesseur de listePieceJointe {@link #listePieceJointe}.
 * @return retourne listePieceJointe
 */
public List<FileUploadDTO> getListePieceJointe() {
    return listePieceJointe;
}

/**
 * Mutateur de listePieceJointe {@link #listePieceJointe}.
 * @param listePieceJointe le listePieceJointe to set
 */
public void setListePieceJointe(List<FileUploadDTO> listePieceJointe) {
    this.listePieceJointe = listePieceJointe;
}

/**
 * Accesseur de indice {@link #indice}.
 * @return retourne indice
 */
public Integer getIndice() {
    return indice;
}

/**
 * Mutateur de indice {@link #indice}.
 * @param indice le indice to set
 */
public void setIndice(Integer indice) {
    this.indice = indice;
}

/**
 * Verifie si l'utilisateur connecte est en lecture seul ou en modif sur cette seance.
 * Utilisée uniquement au niveau des IHM  
 * @return true / false
 */
public Boolean getReadOnly() {
    final UtilisateurDTO utilisateurDTO = ContexteUtils.getContexteUtilisateur().getUtilisateurDTO();
    return !(this.enseignantDTO==null || utilisateurDTO.getUserDTO().getIdentifiant().equals(this.enseignantDTO.getId()));
}
   
}
