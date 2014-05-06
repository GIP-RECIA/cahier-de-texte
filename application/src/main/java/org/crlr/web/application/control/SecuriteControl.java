/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: SecuriteControl.java,v 1.8 2009/06/19 06:49:27 ent_breyton Exp $
 */

package org.crlr.web.application.control;


import java.util.Set;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.crlr.dto.Outil;
import org.crlr.dto.ResultatDTO;
import org.crlr.dto.UserDTO;
import org.crlr.dto.application.base.ElevesParentDTO;
import org.crlr.dto.application.base.Profil;
import org.crlr.dto.application.base.TypeSkin;
import org.crlr.dto.application.base.UtilisateurDTO;
import org.crlr.dto.securite.AuthentificationQO;
import org.crlr.dto.securite.TypeAuthentification;
import org.crlr.exception.metier.MetierException;
import org.crlr.message.Message;
import org.crlr.services.ConfidentialiteService;
import org.crlr.services.PreferencesService;
import org.crlr.utils.Assert;
import org.crlr.utils.BooleanUtils;
import org.crlr.web.application.form.SecuriteForm;
import org.crlr.web.contexte.ContexteApplication;
import org.crlr.web.contexte.ContexteUtilisateur;
import org.crlr.web.contexte.utils.ContexteUtils;
import org.crlr.web.dto.TypePreferences;
import org.crlr.web.utils.CmisUtils;
import org.crlr.web.utils.FacesUtils;
import org.crlr.web.utils.MessageUtils;
import org.crlr.web.utils.NavigationUtils;
import org.jasig.cas.client.authentication.AttributePrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Gestion du formulaire d'authentification.
 *
 * @author breytond.
 */
@ManagedBean(name = "securite")
@RequestScoped
public class SecuriteControl extends AbstractControl<SecuriteForm> {
    
    protected final Logger log = LoggerFactory.getLogger(getClass());

    /** The contexte utilisateur. */
    @ManagedProperty(value = "#{contexteUtilisateur}")
    private ContexteUtilisateur contexteUtilisateur;

    /** confidentialiteService. */
    @ManagedProperty(value = "#{confidentialiteService}")
    private transient ConfidentialiteService confidentialiteService;

    /** Le service d'acces aux preferences de l'utilisateur. */
    @ManagedProperty(value = "#{preferencesService}")
    private transient PreferencesService preferencesService;
    
    /** The menu control. */
    @ManagedProperty(value = "#{menu}")
    private MenuControl menuControl;
    
    /** The skin control. */
    @ManagedProperty(value = "#{skin}")
    private SkinControl skinControl;
    
    
/**
     * Instantiates a new securite control.
     */
    public SecuriteControl() {
        super(new SecuriteForm());
    }

    /**
    * Injection du skinControl.
    * @param skinControl le skinControl à modifier.
    */
    public void setSkinControl(SkinControl skinControl) {
        this.skinControl = skinControl;
    }

    /**
     * Affecte contexte utilisateur.
     *
     * @param contexteUtilisateur the contexte utilisateur
     */
    public void setContexteUtilisateur(ContexteUtilisateur contexteUtilisateur) {
        this.contexteUtilisateur = contexteUtilisateur;
    }

    /**
     * Injection du confidentialiteService.
     *
     * @param confidentialiteService confidentialiteService
     */
    public void setConfidentialiteService(ConfidentialiteService confidentialiteService) {
        this.confidentialiteService = confidentialiteService;
    }

    /**
     * Mutateur de preferencesService {@link #preferencesService}.
     * @param preferencesService le preferencesService to set
     */
    public void setPreferencesService(PreferencesService preferencesService) {
        this.preferencesService = preferencesService;
    }

    /**
     * Injection du menu.
     *
     * @param menuControl contrôleur de menu.
     */
    public void setMenuControl(MenuControl menuControl) {
        this.menuControl = menuControl;
    }
    
    /**
     * Permet le déverrouillage ou la connexion de l'utilisateur.
     *
     * @return <code>connecter()</code> ou <code>deverrouiller()</code>
     */
    public String valider() {
        return this.connecter();
    }

    /**
     * Connexion de l'utilisateur. Cette méthode contacte le service
     * d'authentification pour vérifier l'identité de l'utilisateur.
     * @deprecated
     * @return <code>accueil</code>, <code>choixProfil</code>, ou <code>cibleKO()</code>
     */
    public String connecter() {
        Assert.isNotNull("contexteUtilisateur", contexteUtilisateur);
        final TypeAuthentification typeAuthentification = TypeAuthentification.LDAP;

        final AuthentificationQO criteres = new AuthentificationQO();
        criteres.setIdentifiant(form.getIdentifiant());
        criteres.setPassword(form.getMotDePasse());
        criteres.setTypeAuthentification(typeAuthentification);

        try {
            final ResultatDTO<UtilisateurDTO> resultat = confidentialiteService.initialisationAuthentification(criteres);
            initialiserContexteUtilisateur(resultat.getValeurDTO(), resultat.getValeurDTO().getUserDTO().getUid());
        } catch (MetierException e) {
            contexteUtilisateur.reset();
            return "ok";
        } finally {
            // le mot de passe est effacé
            form.setMotDePasse(null);
        }       

        log.info("Connexion de l'utilisateur via LDAP : {0}", form.getIdentifiant());

        // Acès aux autres applis
        return NavigationUtils.navigationVersOutil(Outil.ACCUEIL);
    }

    /**
     * Déconnexion de l'utilisateur. La session de l'utilisateur est invalidée.
     */
    public void deconnecter() {
        Assert.isNotNull("contexteUtilisateur", contexteUtilisateur);
        FacesUtils.deconnexionApplication(FacesContext.getCurrentInstance(), contexteUtilisateur);
    }

    /**
     * {@inheritDoc}
     */
    @PostConstruct
    public void onLoad() {
                
        final ExternalContext g = FacesContext.getCurrentInstance().getExternalContext();
        

        final AttributePrincipal principal = (AttributePrincipal) g.getUserPrincipal();

        final String casUser = principal.getName();
        
        //Authentification CAS permet de ne pas s'auhentifier en base.
        if (!org.apache.commons.lang.StringUtils.isEmpty(casUser)) {
            log.debug("Connexion effectuée via CAS : {}", casUser);
            initialiseEtRedirige(casUser);
        } else {
            log.debug("casUser est vide");
        }
        //Message du securityPhaseListener
        final Message message = (Message)g.getApplicationMap().get("Message");
        if (message != null) {
            g.getApplicationMap().remove("Message");
            MessageUtils.addMessage(message, this.getClass());
        }       
    }
    
  

    /**
     * Initialise le contexte utilisateur et redirection vers la page d'accueil.
     *
     * @param casUser uid de l'utilisateur CAS.
     */
    private void initialiseEtRedirige(final String casUser) {
        final TypeAuthentification typeAuthentification = TypeAuthentification.CAS;
        final AuthentificationQO criteres = new AuthentificationQO();
        criteres.setIdentifiant(casUser);
        criteres.setTypeAuthentification(typeAuthentification);
        
        final ContexteApplication contexteApplication = ContexteUtils.getContexteApplication();
        
        criteres.setEnvironnement(contexteApplication.getEnvironnement());
        criteres.setMapProfil(contexteApplication.getMapProfil());
        criteres.setGroupsADMCentral(contexteApplication.getGroupsADMCentral());
        criteres.setRegexpAdmLocal(contexteApplication.getRegexpAdmLocal());
        
        try {
            final ResultatDTO<UtilisateurDTO> resultat = confidentialiteService.initialisationAuthentification(criteres);
            initialiserContexteUtilisateur(resultat.getValeurDTO(), casUser);
            log.info("Connexion de l'utilisateur déjà effectuée via CAS : {}", casUser);
            final ContexteUtilisateur contexteUtilisateur = ContexteUtils.getContexteUtilisateur();
            final UtilisateurDTO utilisateurDTO = contexteUtilisateur.getUtilisateurDTO();
            
            if(contexteUtilisateur.getProfilPrefere() != null) {
            	                utilisateurDTO.setProfil(contexteUtilisateur.getProfilPrefere());
            	                log.debug("TEST : initialiseEtRedirige : profil <= profil prefere");
            }
            
            if (!BooleanUtils.isTrue(utilisateurDTO.getVraiOuFauxAdmCentral())) {
                
                // Redirige vers la page choisie comme preference par l'user
                if (Profil.DIRECTION_ETABLISSEMENT.equals(utilisateurDTO.getProfil())) {
                    FacesUtils.redirect("/ecrans/application/seances/seanceSemaine.xhtml");
                } else if (Profil.INSPECTION_ACADEMIQUE.equals(utilisateurDTO.getProfil())) {
                    FacesUtils.redirect("/ecrans/application/preferenceCahier.xhtml");
                } else if (Profil.AUTRE.equals(utilisateurDTO.getProfil()) ||
                        Profil.DOCUMENTALISTE.equals(utilisateurDTO.getProfil())) {
                    FacesUtils.redirect("/ecrans/application/seances/seanceSemaine.xhtml");
                } else { 
                    String preferencesUser = preferencesService.findUtilisateurPreferences(utilisateurDTO.getUserDTO().getUid());
                    if (StringUtils.isEmpty(preferencesUser)) {
                        if (Profil.ENSEIGNANT.equals(utilisateurDTO.getProfil())) {
                            preferencesUser = TypePreferences.EDTMENSUEL.name();
                        } else if (Profil.ELEVE.equals(utilisateurDTO.getProfil()) ||
                                   Profil.PARENT.equals(utilisateurDTO.getProfil())) {
                            preferencesUser = TypePreferences.DEVOIRS.name();
                        }
                    }
                    if (StringUtils.equals(preferencesUser, TypePreferences.EMPLOI.name())) {
                        FacesUtils.redirect("/ecrans/application/emploi/consoliderEmp.xhtml");
                    } else if (StringUtils.equals(preferencesUser, TypePreferences.DEVOIRS.name())) {
                        FacesUtils.redirect("/ecrans/application/devoirs/devoir.xhtml");
                    } else if (StringUtils.equals(preferencesUser, TypePreferences.SAISIR_EMP.name())) {
                        FacesUtils.redirect("/ecrans/application/seances/saisirSeance.xhtml");
                    } else if (StringUtils.equals(preferencesUser, TypePreferences.SEANCES.name())) {
                        FacesUtils.redirect("/ecrans/application/seances/seanceSemaine.xhtml");
                    } else if (StringUtils.equals(preferencesUser, TypePreferences.EDTMENSUEL.name())) {
                        FacesUtils.redirect("/ecrans/application/emploi/planningMensuel.xhtml"); 
                    } else {
                        FacesUtils.redirect("/ecrans/application/preference.xhtml");
                    }
                }
            } else {
                FacesUtils.redirect("/ecrans/application/preferenceCahier.xhtml");
            }
        } catch (MetierException e) {
            log.warn("{}", e);
            contexteUtilisateur.reset();         
        }
        
        //initialiserContexteUtilisateur(UserBouchonDTO.getUserDTO());
        //FacesUtils.redirect("/ecrans/start.xhtml");
    }   

    /**
     * Initialisation du contexte utilisateur.
     * 
     * @param utilisateurDTO utilisateurDTO
     * @param uidLdapCas uidLdapCas
     */
    private void initialiserContexteUtilisateur( 
            final UtilisateurDTO utilisateurDTO, final String uidLdapCas) {
        contexteUtilisateur.setUidLdapCas(uidLdapCas);
        contexteUtilisateur.setAuthentifie(true);
        
        // Verifie qu'on est pas en train de faire un mode de simulation
        Boolean modeSimu = false;
        if (contexteUtilisateur.getUtilisateurDTOOrigine()!=null) {
            
            // On a le meme UID que celui du directeur
            if (contexteUtilisateur.getUtilisateurDTOOrigine().getUserDTO().getUid().equals(uidLdapCas)) {
                modeSimu = true;
            } 
        }
        if (!modeSimu) {       
            contexteUtilisateur.setUtilisateurDTO(utilisateurDTO);
        }
        contexteUtilisateur.initDroit();
        
        
        utilisateurDTO.setTicketAlfresco(CmisUtils.getAlfrescoTicket());
        menuControl.init();
        
        // Selectionne le premier enfant par defaut
        if (Profil.PARENT.equals(utilisateurDTO.getProfil())) {
            final ResultatDTO<ElevesParentDTO> resultatDTO = confidentialiteService.findEleveDuParent(utilisateurDTO.getListeUidEnfant());
            final Set<UserDTO> listeEnfant = resultatDTO.getValeurDTO().getListeEnfant();
            if (!CollectionUtils.isEmpty(listeEnfant)) {
                final UserDTO enfant = listeEnfant.iterator().next();
                utilisateurDTO.getUserDTO().setIdentifiant(enfant.getIdentifiant());
            }
        }
        
        final TypeSkin typeSkin = utilisateurDTO.getTypeSkin();
        log.debug("TypeSkin {}", typeSkin);
        if (typeSkin != null) {
            skinControl.init(typeSkin);
        }
    }
 }
