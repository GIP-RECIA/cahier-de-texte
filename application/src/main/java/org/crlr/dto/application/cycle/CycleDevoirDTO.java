/**
 * 
 */
package org.crlr.dto.application.cycle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.crlr.dto.application.devoir.DevoirDTO;
import org.crlr.dto.application.devoir.TypeDevoirDTO;
import org.crlr.utils.StringUtils;
import org.crlr.web.dto.FileUploadDTO;

/**
 * @author G-SAFIR-FRMP
 *
 */
public class CycleDevoirDTO implements Serializable {
    
    /** Serial. */
    private static final long serialVersionUID = 8311853233378169522L;

    /** Id du devoir. */
    private Integer id;
    
    /** Intitule. */
    private String intitule;
    
    /** Description. */
    private String description;
    
    /** Description au format HTML (integration image Latex). */
    private String descriptionHTML;
    
    /** Date de remise : choix parmi un enum. */
    private TypeDateRemise dateRemise;
    
    /** Type du devoir. */
    private TypeDevoirDTO typeDevoirDTO;

    /** La liste des pièces jointes du devoir. */
    private List<FileUploadDTO> listePieceJointe;
    
    
    /**
     * Constructeur par defaut.
     */
    public CycleDevoirDTO() {
        super();
        typeDevoirDTO = new TypeDevoirDTO();
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
     * Accesseur de description {@link #description}.
     * @return retourne description
     */
    public String getDescription() {
        return description;
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
     * Accesseur de dateRemise {@link #dateRemise}.
     * @return retourne dateRemise
     */
    public TypeDateRemise getDateRemise() {
        return dateRemise;
    }

    /**
     * Mutateur de dateRemise {@link #dateRemise}.
     * @param dateRemise le dateRemise to set
     */
    public void setDateRemise(TypeDateRemise dateRemise) {
        this.dateRemise = dateRemise;
    }

    /**
     * Accesseur de typeDevoirDTO {@link #typeDevoirDTO}.
     * @return retourne typeDevoirDTO
     */
    public TypeDevoirDTO getTypeDevoirDTO() {
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
     * Initialise un DevoirDTO a partir des champ de l'objet.
     * @return un DevoirDTO
     */
    public DevoirDTO initDevoirDTO() {
        final DevoirDTO devoirDTO = new DevoirDTO();
        devoirDTO.setDescription(getDescription());
        devoirDTO.setDescriptionHTML(getDescriptionHTML());
        devoirDTO.setIntitule(getIntitule());
        devoirDTO.setTypeDevoirDTO(getTypeDevoirDTO());
        
        final List<FileUploadDTO> listePj = new ArrayList<FileUploadDTO>();
        for (final FileUploadDTO pj : getListePieceJointe()) {
            listePj.add(pj);
        }
        devoirDTO.setFiles(listePj);
        return devoirDTO;
    }
}
