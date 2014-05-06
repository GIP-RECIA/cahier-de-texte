package org.crlr.web.application.form;


/**
 * Formulaire de changement de profil.
 *
 */
public class ChangeProfilForm extends AbstractForm{
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;


    /** Type de profil. **/
    private String typeProfil;
    
    /**
     * Constructeur.
     */
    public ChangeProfilForm() {
        super();
    }

    /**
     * Accesseur typeProfil.
     * @return le typeProfil
     */
    public String getTypeProfil() {
        return typeProfil;
    }

    /**
     * Mutateur de typeProfil.
     * @param typeProfil le typeProfil à modifier.
     */
    public void setTypeProfil(String typeProfil) {
        this.typeProfil = typeProfil;
    }
}
