/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: jalopy.xml,v 1.1 2010/06/15 12:20:58 ent_breyton Exp $
 */

package org.crlr.metier.business;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;

import org.apache.commons.lang.StringUtils;
import org.crlr.dto.Environnement;
import org.crlr.dto.UserDTO;
import org.crlr.dto.application.base.GenericDTO;
import org.crlr.dto.application.base.Profil;
import org.crlr.dto.application.base.TypeSkin;
import org.crlr.dto.application.base.UtilisateurDTO;
import org.crlr.exception.base.CrlrRuntimeException;
import org.crlr.utils.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.DistinguishedName;
import org.springframework.ldap.core.LdapTemplate;

/**
 * Métier LDAP.
 *
 * @author breytond.
 */
public class LdapBusiness implements LdapBusinessService, InitializingBean {
    /** Connexion au LDAP. */
    private LdapTemplate ldapTemplate;

    /** La branche personne. */
    private String branchePersonne;

    /** La branche structure. */
    private String brancheStructure;
    
    /** Injection du nom de l'attribut LDAP représentant le profil. */
    private String nomProfil;

    /** Map CG/SKIN. */
    private static Map<String, TypeSkin> mapCgSkin = new HashMap<String, TypeSkin>();
    
    /** Logger */
    protected final Logger log = LoggerFactory.getLogger(getClass());

    /**
     * Methode appellée lors de l'initialisation de la classe.
     *
     * @throws Exception .
     */
    public void afterPropertiesSet() throws Exception {
        if (this.branchePersonne == null) {
            throw new CrlrRuntimeException("Echec de l'initialisation du LdapBusiness : La configuration " +
                                           "de 'ldap.branchePersonne' n'a pas été renseignée");
        }

        if (this.brancheStructure == null) {
            throw new CrlrRuntimeException("Echec de l'initialisation du LdapBusiness : La configuration " +
                                           "de 'ldap.brancheStructure' n'a pas été renseignée");
        }

        mapCgSkin.put("CG48", TypeSkin.CG48);
        mapCgSkin.put("CG34", TypeSkin.CG34);
        mapCgSkin.put("CG11", TypeSkin.CG11);
        mapCgSkin.put("CG66", TypeSkin.CG66);
        mapCgSkin.put("CG30", TypeSkin.CG30);
    }

/**
     * Constructeur. 
     */
    public LdapBusiness() {
    }

    /**
     * {@inheritDoc}
     */
    public UtilisateurDTO getUser(final String id, final Environnement environnement,
                                  final Map<String, Profil> mapProfil,
                                  final List<String> grougsADMCentral,
                                  final String regexADMLocal)
                           throws Exception {       
        Assert.isNotNull("uid", id);
        Assert.isNotNull("environnement", environnement);
        
        if (Environnement.CRC.equals(environnement) && StringUtils.isEmpty(nomProfil)) {
            throw new CrlrRuntimeException("Echec de l'initialisation du LdapBusiness : La configuration " +
                                           "de 'ldap.nomProfil' n'a pas été renseignée");
        }
        
        final DistinguishedName dn = new DistinguishedName();
        dn.add("ou", branchePersonne);
        
        log.debug("Recherche de l'utilisateur avec le filtre uid={}", id);

        return (UtilisateurDTO) ldapTemplate.search(dn, "uid=" + id,
                                                    new EntryAttributesMapperUtilisateurDTO(environnement,
                                                                                            mapProfil,
                                                                                            grougsADMCentral,
                                                                                            regexADMLocal, nomProfil))
                                            .get(0);
    }

    /**
     * {@inheritDoc}
     *
     * @deprecated
     */
    public UtilisateurDTO getUser(final String login, final String motDePasse)
                           throws Exception {
        Assert.isNotNull("login", login);
        Assert.isNotNull("motDePasse", motDePasse);
        final DistinguishedName dn = new DistinguishedName();
        dn.add("ou", "personnes");
        final String filter =
            "(&(ENTPersonLogin=" + login + ")(EntPersonPassword=" + motDePasse + "))";

        return (UtilisateurDTO) ldapTemplate.search(dn, filter,
                                                    new EntryAttributesMapperUtilisateurDTO(null,
                                                                                            null,
                                                                                            null,
                                                                                            null, null))
                                            .get(0);
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public Map<String, String> findSirenByUAI(Set<String> uais) {
        Assert.isNotNull("uais", uais);
        Assert.isNotEmpty("uais", uais);
        final DistinguishedName dn = new DistinguishedName();
        dn.add("ou", brancheStructure);

        String filter = "(|";
        for (String uai : uais) {
            filter += ("(ENTStructureUAI=" + uai + ")");
        }
        filter += ")";

        final List<GenericDTO<String, String>> listeUAISiren =
            (List<GenericDTO<String, String>>) ldapTemplate.search(dn, filter,
                                                                   new AttributesMapper() {
                    public Object mapFromAttributes(Attributes attrs)
                                             throws NamingException {
                        final GenericDTO<String, String> genericDTO =
                                                                   new GenericDTO<String, String>((String) attrs.get("ENTStructureUAI")
                                                                                                                .get(),
                                                                                                  (String) attrs.get("ENTStructureSIREN")
                                                                                                                .get());
                        return genericDTO;
                    }
                });
        final Map<String, String> map = new HashMap<String, String>();
        for (GenericDTO<String, String> genericDTO : listeUAISiren) {
            map.put(genericDTO.getValeur1(), genericDTO.getValeur2());
        }
        return map;
    }

    /**
     * Retourne le siren de l'établissement.
     *
     * @param filtre : le filtre ldap à appliquer.
     *
     * @return le siren.
     */
    public String getSirenEtablissement(String filtre) {
        final DistinguishedName dn = new DistinguishedName();
        dn.add("ou", "structures");
        final String siren =
            (String) ldapTemplate.search(dn, filtre,
                                         new EntryAttributesMapperSirenEtablissement())
                                 .get(0);
        return siren;
    }
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public Set<String> getAutoriteParentale(String uidParent,
            Set<String> uidsEnfant) {
        final DistinguishedName dn = new DistinguishedName();
        dn.add("ou", branchePersonne);
        
        String filter = "(&(ENTEleveAutoriteParentale=uid="+uidParent+",*)(|";
        for (String uidEnfant : uidsEnfant) {
            filter += ("(uid=" + uidEnfant + ")");
        }
        filter += "))";
        
        final List<String> uidsEnfantFiltre =
            (List<String>) ldapTemplate.search(dn, filter,
                    new AttributesMapper() {
                public Object mapFromAttributes(Attributes attrs) throws NamingException {
                    final String uid = (String) attrs.get("uid").get();
                    return uid;
                }
            });
        return new HashSet<String>(uidsEnfantFiltre);
    }


    /**
     * Mapper qui renvoit le siren de l'établissement à partir des attributs.
     */
    private class EntryAttributesMapperSirenEtablissement implements AttributesMapper {
        /**
         * Méthode de mappage pour retourner le string siren.
         *
         * @param atts : les attributs obtenus du ldap.
         *
         * @return siren recherché.
         *
         * @throws NamingException exception.
         */
        public String mapFromAttributes(Attributes atts)
                                 throws NamingException {
            final String siren = atts.get("ENTStructureSIREN").get().toString();
            return siren;
        }
    }

    /**
     * Mapper UserDTO.
     */
    private class EntryAttributesMapperUtilisateurDTO implements AttributesMapper {
        /** Environnement d'exécution. */
        private Environnement environnement;
        private Map<String, Profil> mapProfil;
        private List<String> grougsADMCentral;
        private String regexADMLocal;
        private String nomProfil;

        /**
         * Mapper.
         * @param environnement l'environnement d'exécution.
         * @param mapProfil la map des profils.
         * @param groupsADMCentral .
         * @param regexADMLocal .
         * @param nomProfil .
         */
        public EntryAttributesMapperUtilisateurDTO(final Environnement environnement,
                                                   final Map<String, Profil> mapProfil,
                                                   final List<String> groupsADMCentral,
                                                   final String regexADMLocal, final String nomProfil) {
            this.environnement = environnement;
            this.mapProfil = mapProfil;
            this.grougsADMCentral = groupsADMCentral;
            this.regexADMLocal = regexADMLocal;
            this.nomProfil = nomProfil;
        }

        /**
         * Mapping Attributs LDAP / Objet Java.
         *
         * @param attrs attributs.
         *
         * @return le dto.
         *
         * @throws NamingException l'exception.
         */
        public UtilisateurDTO mapFromAttributes(final Attributes attrs)
                                         throws NamingException {
            
            final UserDTO userDTO = new UserDTO();

            userDTO.setUid(attrs.get("uid").get().toString());
            log.debug("Mappage de l'utilisateur avec l'uid uid={}", userDTO.getUid());
            userDTO.setNom(attrs.get("sn").get().toString());
            userDTO.setPrenom(attrs.get("givenName").get().toString());

            final UtilisateurDTO utilisateurDTO = new UtilisateurDTO();
            utilisateurDTO.setUserDTO(userDTO);

            // Traitement de la civilité.
            String civilite = "";

            final Attribute attPersonalTitle = attrs.get("personalTitle");
            if (attPersonalTitle != null) {
                civilite = StringUtils.trimToEmpty((String) attPersonalTitle.get());
            }

            if (StringUtils.isEmpty(civilite)) {
                final Attribute attTitle = attrs.get("title");
                if (attTitle != null) {
                    civilite = StringUtils.trimToEmpty((String) attTitle.get());
                }
            }

            utilisateurDTO.setCivilite(civilite);

            log.debug("Gestion de skin");
            
            //gestion des valeurs propres à un environnement.
            switch (environnement) {
                case CRLR:
                    gestionProfilCRLR(attrs, utilisateurDTO, mapProfil);
                    // Traitement des skins
                    gestionSkinsCrlr(attrs, utilisateurDTO);
                    break;
                case CRC:
                    gestionProfilCRC(attrs, utilisateurDTO);
                    //skin statique
                    utilisateurDTO.setTypeSkin(TypeSkin.CRC);
                    break;
                case Aquitaine:
                    gestionProfilCRC(attrs, utilisateurDTO);
                    log.debug("Mettre skin de l'aquitaine");
                  //skin statique
                    utilisateurDTO.setTypeSkin(TypeSkin.AQUITAINE);
                    break;
                    
                default:
                    throw new CrlrRuntimeException("L'environnement : {0} n'est pas pris en charge",
                                                   environnement.name());
            }

            log.debug("Fin du mappage de l'utilisateur avec l'uid uid={}", userDTO.getUid());
            return utilisateurDTO;
        }

        /**
         * Gestion des profils du CRLR.
         *
         * @param attrs les attributs LDAP.
         * @param utilisateurDTO le dto de résultat.
         *
         * @throws NamingException exception.
         */
        private void gestionSkinsCrlr(final Attributes attrs,
                                      final UtilisateurDTO utilisateurDTO)
                               throws NamingException {
            final Attribute attColl = attrs.get("ENTPersonCollectivite");
            if (attColl != null) {
                final String collectivite =
                    StringUtils.trimToNull((String) attColl.get());
                if (collectivite != null) {
                    utilisateurDTO.setTypeSkin(mapCgSkin.get(collectivite));
                }
            }
        }
        
        
        /**
         * Traiter si un compte a des délégations pour tout l'ENT pour le Cahier.
         * 
         * Administration de ressources 
         * 
         * Attribute :  ENTPersonAdminRessource
         * @param attrs .
         * @param utilisateurDTO .
         * @throws NamingException ex
         */
        private void gestionCRLRAdminRessourceENT(final Attributes attrs,
                final UtilisateurDTO utilisateurDTO) throws NamingException {
          //traitement de l'administrateur de ressources
            final Set<String> listeSirenRess = new HashSet<String>();

            Boolean vraiOuFauxAdminRessEnt = false;

            final Attribute attAdmRes = attrs.get("ENTPersonAdminRessource");
            int attAdmResSize = (attAdmRes == null) ? 0 : attAdmRes.size();
            
            for (int i = 0; i < attAdmResSize; i++) {
                final String tmpAdmRes =
                    StringUtils.trimToNull((String) attAdmRes.get(i));
                
                if (StringUtils.startsWith(tmpAdmRes, "CAHIER")) {
                    if (StringUtils.equals("CAHIER", tmpAdmRes)) {
                        //Adm ressource ent            
                        vraiOuFauxAdminRessEnt = true;
                        break;
                    } else {
                        //adm ress etablissement                            }
                        final String sirenEtab = tmpAdmRes.split("\\$")[1];
                        listeSirenRess.add(sirenEtab);
                    }
                }
                
            }
            
            utilisateurDTO.setVraiOuFauxAdmRessourceENT(vraiOuFauxAdminRessEnt);
            utilisateurDTO.setAdminRessourceSiren(listeSirenRess);
        }

        /**
         * Gestion des profils du CRLR.
         *
         * @param attrs les attributs LDAP.
         * @param utilisateurDTO le dto de résultat.
         * @param mapProfil DOCUMENT ME!
         *
         * @throws NamingException .
         */
        private void gestionProfilCRLR(final Attributes attrs,
                                       final UtilisateurDTO utilisateurDTO,
                                       final Map<String, Profil> mapProfil)
                                throws NamingException {
            //gestion des profils
            Profil profilIndividu = null;
            final Attribute attributObjectClass = attrs.get("objectClass");
            for (Entry<String, Profil> entry : mapProfil.entrySet()) {
                if (attributObjectClass.contains(entry.getKey())) {
                    profilIndividu = entry.getValue();
                    break;
                }
            }

            //administrateur central 
            Boolean vraiOuFauxAdmCentral = false;

            if (profilIndividu == null) {
                //Dorénavant suite à l'ajout de l'interface d'admin, de nombreux profils peuvent y accéder.
                profilIndividu = Profil.AUTRE;

                //administrateur central est forcement un profil autre. */
                if ("ADM00000".equals(utilisateurDTO.getUserDTO().getUid())) {
                    vraiOuFauxAdmCentral = true;
                }
            }

            utilisateurDTO.setProfil(profilIndividu);
            utilisateurDTO.setVraiOuFauxAdmCentral(vraiOuFauxAdmCentral);

            //gestion des enfants du profil parent
            if (Profil.PARENT.equals(profilIndividu)) {
                utilisateurDTO.setListeUidEnfant(extractListeEnfants(attrs));
            }

            //Traitement du Siren de l'établissement
            final Attribute attEtab = attrs.get("ENTPersonStructRattach");

            String sirenEtablissement = null;

            if (attEtab != null) {
                final String dnEtablissement =
                    StringUtils.trimToNull((String) attEtab.get());

                if (dnEtablissement != null) {
                    final String debutDnEtab = "ENTStructureSIREN=";
                    int dnEtabDebutIndex = dnEtablissement.indexOf(debutDnEtab);

                    if (dnEtabDebutIndex != -1) {
                        dnEtabDebutIndex += debutDnEtab.length();
                        final int dnEtabFinIndex =
                            dnEtablissement.indexOf(",", dnEtabDebutIndex);
                        if (dnEtabFinIndex != -1) {
                            sirenEtablissement = dnEtablissement.substring(dnEtabDebutIndex,
                                                                           dnEtabFinIndex);
                        }
                    }
                }
            }

            utilisateurDTO.setSirenEtablissement(sirenEtablissement);

            //Traitement des Sirens des établissement pour les enseignants
            if (Profil.ENSEIGNANT.equals(profilIndividu) ||
                    Profil.DIRECTION_ETABLISSEMENT.equals(profilIndividu) ||
                    Profil.AUTRE.equals(profilIndividu) ||
                    Profil.DOCUMENTALISTE.equals(profilIndividu)) {
                final Attribute attEtabs = attrs.get("ENTPersonCentresInteret");
                final Set<String> sirensEtablissement = new HashSet<String>();

                if (attEtabs != null) {
                    for (int i = 0; i < attEtabs.size(); i++) {
                        final String tmpDnCentreInteret =
                            StringUtils.trimToNull((String) attEtabs.get(i));
                        if (tmpDnCentreInteret != null) {
                            final String[] centreInteret =
                                tmpDnCentreInteret.split("\\$");
                            if ((centreInteret.length == 2) &&
                                    centreInteret[1].equals("Fonction")) {
                                final String dnEtablissement = centreInteret[0];
                                if (dnEtablissement != null) {
                                    final String debutDnEtab = "ENTStructureSIREN=";
                                    int dnEtabDebutIndex =
                                        dnEtablissement.indexOf(debutDnEtab);

                                    if (dnEtabDebutIndex != -1) {
                                        dnEtabDebutIndex += debutDnEtab.length();
                                        final int dnEtabFinIndex =
                                            dnEtablissement.indexOf(",", dnEtabDebutIndex);
                                        if (dnEtabFinIndex != -1) {
                                            sirensEtablissement.add(dnEtablissement.substring(dnEtabDebutIndex,
                                                                                              dnEtabFinIndex));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                utilisateurDTO.setSirensEtablissement(sirensEtablissement);
            }

            gestionCRLRAdminRessourceENT(attrs, utilisateurDTO);

            // Traitement de l'administrateur local
            final Set<String> listeSirenAdm = new HashSet<String>();
            final Attribute attAdmLocal = attrs.get("ENTPersonAdmin");
            if (attAdmLocal != null) {
                for (int i = 0; i < attAdmLocal.size(); i++) {
                    final String dnadmin = (String) attAdmLocal.get(i);
                    listeSirenAdm.add(StringUtils.split(dnadmin, ",")[0]
                                                 .replaceFirst("cn=", "")
                                                 .replaceFirst("\\$Admin", ""));
                }
            }

            utilisateurDTO.setAdminLocalSiren(listeSirenAdm);
        }

        /**
         * Gestion des profils du CRC.
         *
         * @param attrs les attributs LDAP.
         * @param utilisateurDTO le dto de résultat.
         * @param mapProfil DOCUMENT ME!
         * @param groupsADMCentral DOCUMENT ME!
         * @param regexpADMLocal DOCUMENT ME!
         *
         * @throws NamingException .
         */
        private void gestionProfilCRC(final Attributes attrs,
                                      final UtilisateurDTO utilisateurDTO)
                               throws NamingException {
            final Attribute passwordAtt = attrs.get("userPassword");
            final String password = passwordAtt.get(0).toString();
            if (password.startsWith("{SCRIPT}")) {
                throw new CrlrRuntimeException("L'utilisateur : {0} n'est pas actif",
                                               utilisateurDTO.getUserDTO().getUid());
            }
            
            final Attribute attributProfil = attrs.get(this.nomProfil);

            //ESCOPersonProfils = {Directeur,Enseignant,...} 
            if (attributProfil != null) {
                for (int i = 0; i < attributProfil.size(); i++) {
                    final String profilName = (String) attributProfil.get(i);
                    for (Entry<String, Profil> entry : mapProfil.entrySet()) {
                        if (profilName.equals(entry.getKey())) {
                            utilisateurDTO.setProfil(entry.getValue());
                        }
                    }
                }
            }

            //gestion des enfants du profil parent
            if (Profil.PARENT.equals(utilisateurDTO.getProfil())) {
                utilisateurDTO.setListeUidEnfant(extractListeEnfants(attrs));
            }

            //Gestion des droits administratifs
            Boolean vraiOuFauxAdmCentral = false;
            final Set<String> uaiAdmin = new HashSet<String>();

            final Attribute attributMemberOf = attrs.get("isMemberOf");
            int attributMemberOfSize = attributMemberOf == null ? 0 : attributMemberOf.size();
            for (int i = 0; i < attributMemberOfSize; i++) {
                final String attMember = (String) attributMemberOf.get(i);
                if (this.grougsADMCentral.contains(attMember)) {
                    vraiOuFauxAdmCentral = true;
                    break;
                } else if (attMember.matches(this.regexADMLocal)) {
                    final String[] tabMember = attMember.split(":");
                    final String etab = tabMember[tabMember.length - 1];
                    final String[] tabEtab = etab.split("_");
                    if (tabEtab.length > 1) {
                        final String uai = tabEtab[tabEtab.length - 1]; // La chaine se termine tjs par le _UAI
                        uaiAdmin.add(uai);
                    }
                }
            }
            utilisateurDTO.setAdminLocalSiren(uaiAdmin);
            utilisateurDTO.setVraiOuFauxAdmCentral(vraiOuFauxAdmCentral);

            utilisateurDTO.setVraiOuFauxAdmRessourceENT(false);

            if (utilisateurDTO.getProfil() == null) {
                //Un contrôle des utilisateurs non autorisés est fait dans ConfidentialiteFacade.
                utilisateurDTO.setProfil(Profil.AUTRE);
            }

            //Traitement de l'uai de l'établissement de rattachement
            final Attribute attUAICourant = attrs.get("ESCOUAICourant");
            String uaiEtab = null;
            if (attUAICourant != null) {
                uaiEtab = StringUtils.trimToNull((String) attUAICourant.get());
            }
            utilisateurDTO.setSirenEtablissement(uaiEtab);

            //Traitement de l'uai de l'établissement de rattachement
            final Attribute attUAIS = attrs.get("ESCOUAI");
            final Set<String> uaisEtab = new HashSet<String>();
            if (attUAIS != null) {
                for (int i = 0; i < attUAIS.size(); i++) {
                    final String attUAI = (String) attUAIS.get(i);
                    uaisEtab.add(attUAI);
                }
            }
            utilisateurDTO.setSirensEtablissement(uaisEtab);
        }

        /**
         * Extrait la liste des uids des enfants d'un parent.
         *
         * @param attrs les attributs du parent.
         *
         * @return la liste des uids des enfants.
         *
         * @throws NamingException une exception si l'attribut pour les enfants n'est pas
         *         présent.
         */
        private Set<String> extractListeEnfants(final Attributes attrs)
                                         throws NamingException {
            final Attribute attPersRel = attrs.get("ENTAuxPersRelEleveEleve");
            final Set<String> listeUidEnfant = new HashSet<String>();
            if (attPersRel != null) {
                for (int i = 0; i < attPersRel.size(); i++) {
                    final String dnEnfant =
                        StringUtils.trimToNull((String) attPersRel.get(i));
                    final String[] dnEnfantTableau = dnEnfant.split(",")[0].split("=");
                    if (dnEnfantTableau.length > 1) {
                        listeUidEnfant.add(dnEnfantTableau[1]);
                    }
                }
            }
            return listeUidEnfant;
        }
    }

    
    /**
     * Positionne le service LdapTemplate.
     *
     * @param ldapTemplate ldapTemplate.
     */
    public void setLdapTemplate(final LdapTemplate ldapTemplate) {
        this.ldapTemplate = ldapTemplate;
    }

    /**
     * Mutateur de branchePersonne.
     *
     * @param branchePersonne le branchePersonne à modifier.
     */
    public void setBranchePersonne(String branchePersonne) {
        this.branchePersonne = branchePersonne;
    }

    /**
     * Mutateur de brancheStructure.
     *
     * @param brancheStructure le brancheStructure à modifier.
     */
    public void setBrancheStructure(String brancheStructure) {
        this.brancheStructure = brancheStructure;
    }
    
    /**
     * Mutateur nomProfil.
     * @param nomProfil le nomProfil à modifier.
     */
    public void setNomProfil(String nomProfil) {
        this.nomProfil = nomProfil;
    }

    
}
