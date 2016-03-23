package org.crlr.web.application.control;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;

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
    
        form.setProfil(utilisateurDTO.getProfil());
        
        List<SelectItem> listRoles = new ArrayList<SelectItem>();
        
        Set<Profil> profilsDisponibles = utilisateurDTO.getProfilsDisponibles();
        
        for (Profil profil : profilsDisponibles) {
			SelectItem item = new SelectItem(profil, profil.getRole());
			listRoles.add(item);
		}
        
        form.setProfilList(listRoles);
    }
            
    /**
     * Sauvegarde le profil choisi par l'utilisateur.
     * @return renvoie la chaine vide qui va permettre de naviguer vers l'écran par défaut.
     */
    public String saveProfil() {
        ContexteUtilisateur contexteUtilisateur = ContexteUtils.getContexteUtilisateur();
        UtilisateurDTO utilisateurDTO = contexteUtilisateur.getUtilisateurDTO();
        Profil profil = form.getProfil();
        log.debug("TEST CHANGEMENT PROFIL : {0}", profil.name());
      
        if(profil != null) {
            utilisateurDTO.setProfil(profil);
            contexteUtilisateur.setProfilPrefere(profil);
        }
        log.debug("NOUVEAU PROFIL : {0}", utilisateurDTO.getProfil().toString());
        log.debug("NOUVEAU PROFIL PREFERE : {0}", contexteUtilisateur.getProfilPrefere());
        return NavigationUtils.navigationVersOutil(Outil.ACCUEIL);
    }
    
}
