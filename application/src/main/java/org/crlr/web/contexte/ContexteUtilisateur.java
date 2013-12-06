/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: ContexteUtilisateur.java,v 1.6 2010/03/09 14:29:13 ent_breyton Exp $
 */

package org.crlr.web.contexte;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.crlr.dto.Outil;
import org.crlr.dto.application.base.UtilisateurDTO;
import org.crlr.log.Log;
import org.crlr.log.LogFactory;
import org.crlr.utils.Assert;
import org.crlr.utils.PropertiesUtils;
import org.crlr.web.application.MenuNavigationItem;
import org.crlr.web.dto.TypeModeFonctionnement;

/**
 * Informations sur la session utilisateur courante. Ces informations sont mises à
 * jour par le contrôleur JSF {@link org.crlr.web.application.control.SecuriteControl}.
 *
 * @author breytond
 */
public class ContexteUtilisateur implements Serializable {
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The Constant ID. */
    public static final String ID = "contexteUtilisateur";

    /** The Constant MAX_NAVIGATION_ITEMS. */
    private static final int MAX_NAVIGATION_ITEMS = 6;

    /** L'utilisateur est-il authentifié ?. */
    private boolean authentifie;
    
    /** uid Unique provenant de cas ou du LDAP. */
    private String uidLdapCas;

    /**
     * Dto des informations de l'utilisateur.
     */
    private UtilisateurDTO utilisateurDTO;

    /** 
     * Infos de l'utilisateur qui s'est réellement connecté.
     * ce champ est renseigné lorsque le directeur est passé en mode simulation d'un eleve :
     * - ce champ contient les infos du directeur,
     * - les infos de l'eleve choisi sont stocké dans le champ utilisateurDTO 
     */
    private UtilisateurDTO utilisateurDTOOrigine;
    
    /**
     * Les logs.
     */
    private final static Log log = LogFactory.getLog(ContexteUtilisateur.class);

    /** The Constant DROIT_PROFIL. */
    private static final String DROIT_PROFIL_PROPERTIES = "/droit.properties";

    /**
     * Un map qui associe à un outil un type de fonctionnement. 
     */
    private Map<Outil, TypeModeFonctionnement> mapOutilMode;

    /**
     * L'outil en cours d'utilisation.
     */
    private Outil outil;

/**
     * Constructeur.
     */
    public ContexteUtilisateur() {
        utilisateurDTO = new UtilisateurDTO();
        mapOutilMode = new HashMap<Outil, TypeModeFonctionnement>();
    }

    /**
     * Initialisation des droits de l'utilisateur en fonction de son profil.
     */
    public void initDroit() {
        log.debug("Initialisation des droits");

        //lecture du fichier.
        final Properties props = PropertiesUtils.load(DROIT_PROFIL_PROPERTIES);

        // On renseigne les map de correspondance profil / outils
        final String outilsModes = props.getProperty(utilisateurDTO.getProfil().name());
        if (outilsModes != null) {
            final List<String> listeOutilModes = Arrays.asList(StringUtils.split(outilsModes, ","));

            for (final String outilModes : listeOutilModes) {
                final List<String> outilMode = Arrays.asList(StringUtils.split(outilModes, ":"));
                if (outilMode.size() == 2) {
                    mapOutilMode.put(Outil.valueOf(StringUtils.trimToEmpty(outilMode.get(0))),
                                     TypeModeFonctionnement.valueOf(StringUtils.trimToEmpty(outilMode.get(1))));
                }
            }
        }
    }

    /**
     * File de navigation conservant les outils utilisés (par conséquence l'outil
     * en cours).
     */
    private final List<MenuNavigationItem> navigation =
        new ArrayList<MenuNavigationItem>();

    /**
     * Checks if is authentifie.
     *
     * @return true, if is authentifie
     */
    public boolean isAuthentifie() {
        return authentifie;
    }

    /**
     * Affecte authentifie.
     *
     * @param authentifie the authentifie
     */
    public void setAuthentifie(boolean authentifie) {
        this.authentifie = authentifie;
    }

    /**
     * Retourne navigation.
     *
     * @return the navigation
     */
    public List<MenuNavigationItem> getNavigation() {
        return navigation;
    }

    /**
     * Reset.
     */
    public void reset() {
        utilisateurDTO = new UtilisateurDTO();
        authentifie = false;
        navigation.clear();
        outil = null;
        mapOutilMode.clear();
    }

    /**
     * Ajout d'une item de navigation dans la file de navigation.
     *
     * @param navigationItem the navigation item
     */
    public void addNavigationItem(MenuNavigationItem navigationItem) {
        Assert.isNotNull("navigation", navigation);
        findAndRemoveNavigationItem(navigationItem);
        navigation.add(navigationItem);
        if (navigation.size() > MAX_NAVIGATION_ITEMS) {
            navigation.remove(0);
        }
    }

    /**
     * Récupération de la derniere Item de navigation.
     *
     * @return the last navigation item
     */
    public MenuNavigationItem getLastNavigationItem() {
        Assert.isNotNull("navigation", navigation);
        if (navigation.size() >= 1) {
            return navigation.get(navigation.size() - 1);
        } else {
            return null;
        }
    }

    /**
     * Récupération de l'avant dernier item de navigation. Permet de revenir à
     * l'outil précédent l'outil courant.
     *
     * @return the before last navigation item
     */
    public MenuNavigationItem getBeforeLastNavigationItem() {
        if (navigation.size() < 2) {
            return getLastNavigationItem();
        } else {
            return navigation.get(navigation.size() - 2);
        }
    }

    /**
     * Récupération de l'avant dernier item de navigation. Permet de revenir à
     * l'outil précédent l'outil courant. Suppression du dernier item de navigation.
     *
     * @return l'item.
     */
    public MenuNavigationItem deleteAndBeforeLastNavigationItem() {
        if (navigation.size() < 2) {
            return getLastNavigationItem();
        } else {
            final MenuNavigationItem menuNavigationItem =
                navigation.get(navigation.size() - 2);
            navigation.remove(navigation.size() - 1);
            return menuNavigationItem;
        }
    }

    /**
     * Recherche et suppression d'une item de navigation associée à l'outil passé
     * en paramètre. Permet d'avoir un ensemble d'outils visités, donc sans doublons.
     *
     * @param navigationItem the navigation item
     */
    private void findAndRemoveNavigationItem(MenuNavigationItem navigationItem) {
        Assert.isNotNull("navigationItem", navigationItem);
        final ListIterator<MenuNavigationItem> iter = navigation.listIterator();
        while (iter.hasNext()) {
            final MenuNavigationItem nav = (MenuNavigationItem) iter.next();
            if (nav.equals(navigationItem)) {
                iter.remove();
                break;
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "ContexteUtilisateur[" + utilisateurDTO + ", authentifie=" + authentifie + "]";
    }

    /**
     * Accesseur utilisateurDTO.
     *
     * @return le utilisateurDTO.
     */
    public UtilisateurDTO getUtilisateurDTO() {
        return utilisateurDTO;
    }

    /**
     * Mutateur utilisateurDTO.
     *
     * @param utilisateurDTO le utilisateurDTO à modifier.
     */
    public void setUtilisateurDTO(UtilisateurDTO utilisateurDTO) {
        this.utilisateurDTO = utilisateurDTO;
    }

    /**
     * Accesseur mapOutilMode.
     *
     * @return le mapOutilMode.
     */
    public Map<Outil, TypeModeFonctionnement> getMapOutilMode() {
        return mapOutilMode;
    }

    /**
     * Accesseur outil.
     *
     * @return le outil.
     */
    public Outil getOutil() {
        return outil;
    }

    /**
     * Mutateur outil.
     *
     * @param outil le outil à modifier.
     */
    public void setOutil(Outil outil) {
        this.outil = outil;
    }

    /**
     * Accesseur uidLdapCas.
     * @return the uidLdapCas
     */
    public String getUidLdapCas() {
        return uidLdapCas;
    }

    /**
     * Mutateur uidLdapCas.
     * @param uidLdapCas the uidLdapCas to set
     */
    public void setUidLdapCas(String uidLdapCas) {
        this.uidLdapCas = uidLdapCas;
    }
    
    /**
     * Accesseur.
     * @return le nom de l'outil.
     */
    public String getNameOutil() {
        if (outil == null) {
            return Outil.ACCUEIL.name();
        }
        return outil.name();
    }

    /**
     * Accesseur de utilisateurDTOOrigine {@link #utilisateurDTOOrigine}.
     * @return retourne utilisateurDTOOrigine
     */
    public UtilisateurDTO getUtilisateurDTOOrigine() {
        return utilisateurDTOOrigine;
    }

    /**
     * Mutateur de utilisateurDTOOrigine {@link #utilisateurDTOOrigine}.
     * @param utilisateurDTOOrigine le utilisateurDTOOrigine to set
     */
    public void setUtilisateurDTOOrigine(UtilisateurDTO utilisateurDTOOrigine) {
        this.utilisateurDTOOrigine = utilisateurDTOOrigine;
    }
    
}
