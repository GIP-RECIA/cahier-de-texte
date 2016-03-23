package org.crlr.web.application.control;

import java.io.Serializable;

import org.richfaces.model.TreeNodeImpl;

/**
 * @author G-CG34-FRMP
 *
 * @param <FichierDTOClasse> la classe associé avec le noeud dans l'arborescence RichFaces
 */
public abstract class AbstractFichierTreeNode<FichierDTOClasse> extends TreeNodeImpl implements Serializable {

    /**
     * @param noeud le noeud
     * @return le libéllé à affichier dans l'IHM.
     */
    abstract public String toLabel(FichierDTOClasse noeud);
    
    
    /**
     * 
     */
    private static final long serialVersionUID = 1901267675151843655L;
    private FichierDTOClasse fichierDTO;
    
    /** Identifiant unique du noeud. */
    private String key;

    /**
     * @param leaf vrai si un document / un fichier, faux pour un dossier.
     */
    public AbstractFichierTreeNode(boolean leaf) {
        super(leaf);
    }

    /**
     * @return the fichierDTO
     */
    public FichierDTOClasse getFichierDTO() {
        return fichierDTO;
    }

    /**
     * @param fichierDTO
     *            the fichierDTO to set
     */
    public void setFichierDTO(FichierDTOClasse fichierDTO) {
        this.fichierDTO = fichierDTO;
    }

    

    /**
     * Accesseur de key {@link #key}.
     * @return retourne key
     */
    public String getKey() {
        return key;
    }

    /**
     * Mutateur de key {@link #key}.
     * @param key le key to set
     */
    public void setKey(String key) {
        this.key = key;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        if (null != fichierDTO) {
            return toLabel(fichierDTO);
        } else {
            return super.toString();
        }
        
    }

    
}
