package org.crlr.web.application.control;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.crlr.dto.Outil;
import org.crlr.dto.application.base.AnneeScolaireDTO;
import org.crlr.dto.application.base.PreferencesQO;
import org.crlr.dto.application.base.Profil;
import org.crlr.dto.application.base.UtilisateurDTO;
import org.crlr.dto.application.sequence.RechercheSequenceQO;
import org.crlr.dto.application.sequence.SaveSequenceSimplifieeQO;
import org.crlr.exception.metier.MetierException;
import org.crlr.services.PreferencesService;
import org.crlr.services.SequenceService;
import org.apache.commons.lang.StringUtils;
import org.crlr.web.application.MenuAction;
import org.crlr.web.application.form.ChangeProfilForm;
import org.crlr.web.contexte.utils.ContexteUtils;
import org.crlr.web.contexte.ContexteUtilisateur;
import org.crlr.web.utils.NavigationUtils;
import org.crlr.web.dto.TypePreferences;

/**
 * ChangeProfilControl.
 *
 */
@ManagedBean(name="changeProfil")
@ViewScoped
public class ChangeProfilControl extends AbstractControl<ChangeProfilForm> {


    /**
     * Constructeur.
     */
    public ChangeProfilControl() {
        super(new ChangeProfilForm());
    }
    
    /**
     * Onload.
     */
    @PostConstruct
    public void onLoad() {
        // Charge les preference de l'utilisateur
        final UtilisateurDTO utilisateurDTO =
            ContexteUtils.getContexteUtilisateur().getUtilisateurDTO();
        form.setTypeProfil(utilisateurDTO.getProfil().toString());
    }
            
    /**
     * Sauvegarde le profil choisi par l'utilisateur.
     * @return renvoie la chaine vide qui va permettre de naviguer vers l'écran par défaut.
     */
    public String saveProfil() {
        ContexteUtilisateur contexteUtilisateur = ContexteUtils.getContexteUtilisateur();
        UtilisateurDTO utilisateurDTO = contexteUtilisateur.getUtilisateurDTO();
        String profilSelectionne = form.getTypeProfil();
        log.debug("TEST CHANGEMENT PROFIL : {0}", form.getTypeProfil());
        Profil profilFinal = null;
        if("ENSEIGNANT".equals(profilSelectionne)) {
            profilFinal = Profil.ENSEIGNANT;
        } else if("DIRECTION_ETABLISSEMENT".equals(profilSelectionne)) {
            profilFinal = Profil.DIRECTION_ETABLISSEMENT;
        }
        
        if(profilFinal != null) {
            utilisateurDTO.setProfil(profilFinal);
            contexteUtilisateur.setProfilPrefere(profilFinal);
        }
        log.debug("NOUVEAU PROFIL : {0}", utilisateurDTO.getProfil().toString());
        log.debug("NOUVEAU PROFIL PREFERE : {0}", contexteUtilisateur.getProfilPrefere());
        return NavigationUtils.navigationVersOutil(Outil.ACCUEIL);
    }
    
}
