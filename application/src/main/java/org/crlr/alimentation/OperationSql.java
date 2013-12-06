/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: OperationSql.java,v 1.26 2010/06/08 10:51:06 ent_breyton Exp $
 */

package org.crlr.alimentation;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.persistence.NoResultException;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.crlr.alimentation.DTO.EleveDTO;
import org.crlr.alimentation.DTO.EnseignantDTO;
import org.crlr.alimentation.DTO.InspecteurDTO;
import org.crlr.dto.application.base.GenericDTO;
import org.crlr.log.Log;
import org.crlr.log.LogFactory;
import org.crlr.metier.business.AbstractBusiness;
import org.crlr.metier.business.BaseHibernateBusiness;
import org.crlr.metier.entity.AnneeScolaireBean;
import org.crlr.metier.entity.ClasseBean;
import org.crlr.metier.entity.EleveBean;
import org.crlr.metier.entity.ElevesClassesBean;
import org.crlr.metier.entity.ElevesGroupesBean;
import org.crlr.metier.entity.EnseignantBean;
import org.crlr.metier.entity.EnseignantsClassesBean;
import org.crlr.metier.entity.EnseignantsEnseignementsBean;
import org.crlr.metier.entity.EnseignantsGroupesBean;
import org.crlr.metier.entity.EnseignementBean;
import org.crlr.metier.entity.EtablissementBean;
import org.crlr.metier.entity.EtablissementsElevesBean;
import org.crlr.metier.entity.EtablissementsEnseignantsBean;
import org.crlr.metier.entity.GroupeBean;
import org.crlr.metier.entity.GroupesClassesBean;
import org.crlr.metier.entity.InspecteurBean;
import org.crlr.metier.entity.TypeDevoirBean;
import org.crlr.metier.utils.SchemaUtils;
import org.crlr.utils.Assert;
import org.crlr.utils.DateUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Classe permettant d'effectuer des opérations sur la base de données du module
 * Cahier de Texte.
 *
 * @author breytond.
 * @version $Revision: 1.26 $
 */
@Component
@Transactional(
        readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class
    )
public class OperationSql extends AbstractBusiness implements OperationSqlService {
    /** L'id de l'année scolaire inserée au debut. */
    private int idAnneeScol;

    /** Fichier de log pour les cas d'erreur. */
    private static final Log log = LogFactory.getLog(Alimentation.class);
    
    /**
     * {@inheritDoc}
     */
    public void addAnneeScol(String exercice, Date dateRentree, Date dateSortie, List<GenericDTO<Date, Date>> periodes) {
        final AnneeScolaireBean anneeScolaireBean = new AnneeScolaireBean();

        anneeScolaireBean.setDateRentree(dateRentree);
        anneeScolaireBean.setDateSortie(dateSortie);
        anneeScolaireBean.setExercice(exercice);

        if (!CollectionUtils.isEmpty(periodes)){
            final StringBuffer periodeScolaire = new StringBuffer();
            for (GenericDTO<Date, Date> dates : periodes){
                periodeScolaire.append(DateUtils.format(dates.getValeur1()));
                periodeScolaire.append(":");
                periodeScolaire.append(DateUtils.format(dates.getValeur2()));
                periodeScolaire.append("|");
            }
            anneeScolaireBean.setPeriodeScolaire(periodeScolaire.toString());  
        }

        final BaseHibernateBusiness baseHibernateBusiness =
            new BaseHibernateBusiness(this.getEntityManager());
        final Integer id =
            baseHibernateBusiness.getIdInsertion("cahier_annee_scolaire");
        anneeScolaireBean.setId(id);

        getEntityManager().persist(anneeScolaireBean);
        getEntityManager().flush();

        idAnneeScol = id;
    }
    
    /**
     * {@inheritDoc}
     */
    public void addEtablissements(List<EtablissementBean> etablissements, String[] typesDevoir) {
        for (EtablissementBean etablissementDTO : etablissements) {
            final BaseHibernateBusiness baseHibernateBusiness =
                new BaseHibernateBusiness(this.getEntityManager());
            final Integer idEtablissement =
                baseHibernateBusiness.getIdInsertion("cahier_etablissement");

            etablissementDTO.setId(idEtablissement);
            etablissementDTO.setFractionnement(1);
            getEntityManager().persist(etablissementDTO);
            
            addTypeDevoir(idEtablissement, typesDevoir);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void addEnseignants(List<EnseignantDTO> enseignants) {
        for (EnseignantDTO enseignantDTO : enseignants) {
            if (enseignantDTO == null){
                //On n'intégre pas les utilisateurs inactifs.
                continue;
            }
            
            // Insertion de l'enseignant
            final BaseHibernateBusiness baseHibernateBusiness =
                new BaseHibernateBusiness(this.getEntityManager());
            final Integer idEnseignant =
                baseHibernateBusiness.getIdInsertion("cahier_enseignant");

            final EnseignantBean enseignant = enseignantDTO.getEnsBean();
            enseignant.setId(idEnseignant);

            getEntityManager().persist(enseignant);
                       
            
            // la relation avec les etablissement
            boolean auMoinsUnEtab = false;
            for (String enseignement : enseignantDTO.getEnseignements()){

                final String dnSiren = enseignement;
                final String siren = extractSirenEtablissement(dnSiren);

                int etabid = 0;

                etabid = etablissementFindBySiren(siren);

                // L'etablissement n'a pas été trouvé
                // on passe à l'enseignement suivant
                if (etabid == 0)  {
                    log.info("L'etablissement "+siren+"n'a pu être trouvé dans la BD");
                    log.info("L'enseignant "+ enseignantDTO.getEnsBean().getUid() +" ne sera donc pas inseré dans cet établissement");
                    log.info("---------------------------------------------------------");
                    continue;
                }

                //on ne persiste que si besoin
                if (! existEnseignantEtablissement(idEnseignant, etabid)){
                    final EtablissementsEnseignantsBean etablissementsEnseignantsBean =
                        new EtablissementsEnseignantsBean();
                    etablissementsEnseignantsBean.setIdEnseignant(idEnseignant);
                    etablissementsEnseignantsBean.setIdEtablissement(etabid);
                    getEntityManager().persist(etablissementsEnseignantsBean);
                    auMoinsUnEtab = true;
                }

                //création des enseignements
                // traite la chaine de charectre de l'enseignement issu de
                // l'annuaire LDAP
                
                final List<String> tabEnseignement = Arrays.asList(StringUtils.split(enseignement, "\\$"));
                if (tabEnseignement.size()==2) {
                    final String libelleEnseignement = tabEnseignement.get(1);
                    int idEnseignement = findEnseignementByLibelle(libelleEnseignement);
                    
                    if (idEnseignement == 0) {
                        idEnseignement = createEnseignement(libelleEnseignement);

                        // La jointure avec enseignant - comme on vient de creer l'enseignement on la cree toujours
                        final EnseignantsEnseignementsBean enseignantsEnseignementsBean =
                            new EnseignantsEnseignementsBean();
                        enseignantsEnseignementsBean.setIdEnseignement(idEnseignement);
                        enseignantsEnseignementsBean.setIdEnseignant(idEnseignant);
                        enseignantsEnseignementsBean.setIdEtablissement(etabid);
                        getEntityManager().persist(enseignantsEnseignementsBean);
                    } else if (!existEnseignantEnseignement(idEnseignant,
                            idEnseignement, etabid)) {
                        // La jointure avec enseignant - seulement si celle ci n'existe pas deja 
                        //ie si l'enseignant enseigne cet enseignement dans 2 etablissement
                        //ou en cas de passage d'année scolaire
                        final EnseignantsEnseignementsBean enseignantsEnseignementsBean =
                            new EnseignantsEnseignementsBean();
                        enseignantsEnseignementsBean.setIdEnseignement(idEnseignement);
                        enseignantsEnseignementsBean.setIdEnseignant(idEnseignant);
                        enseignantsEnseignementsBean.setIdEtablissement(etabid);
                        getEntityManager().persist(enseignantsEnseignementsBean);
                    }
                }
            }
            if (! auMoinsUnEtab){
                 log.info("L'enseignant "+ enseignantDTO.getEnsBean().getUid() +" n'a aucune matière rattachée à un établissement");
                 log.info("---------------------------------------------------------");
            }

            // les classes
            for (String classe : enseignantDTO.getClasses()) {
                int id = findClasseByDesignation(classe);

                // Si la classe n'existe pas deja
                // on supose qu'il n'y a pas de doublons dans la liste des
                // classes de l'enseignant
                if (id == 0) {
                    id = createClasse(classe, 0);
                    if (id == 0){
                        // on a pas pu creer la classe car l'etablissement n'existe pas dans la BD
                        //on passe à la classe suivante
                        log.info("L'etablissement "+classe+" n'a pu être trouvé dans la BD");
                        log.info("La classe "+ classe +" ne sera donc pas inseré ");
                        log.info("et l'enseignant "+ enseignant.getUid() +" ne sera donc pas relié à cette classe ");
                        log.info("---------------------------------------------------------");
                        continue;
                    }
                }

                // La jointure avec enseignant
                final EnseignantsClassesBean enseignantsClassesBean =
                    new EnseignantsClassesBean();
                enseignantsClassesBean.setIdClasse(id);
                enseignantsClassesBean.setIdEnseignant(idEnseignant);
                getEntityManager().persist(enseignantsClassesBean);
            }

            // les groupes
            for (String groupe : enseignantDTO.getGroupes()) {
                int id = findGroupeByDesignation(groupe);

                // Si le groupe n'existe pas deja
                // on supose qu'il n'y a pas de doublons dans la liste des
                // groupes de l'enseignant
                if (id == 0) {
                    id = createGroupe(groupe, 0);
                    if (id == 0){
                        // on a pas pu creer le groupe car l'etablissement n'existe pas dans la BD
                        //on passe au groupe suivante
                        log.info("L'etablissement "+extractSirenClasse(groupe)+"n'a pu être trouvé dans la BD");
                        log.info("Le groupe "+ groupe +" ne sera donc pas inseré ");
                        log.info("et l'enseignant "+ enseignant.getUid() +" ne sera donc pas relié à ce groupe ");
                        log.info("---------------------------------------------------------");
                        continue;
                    }
                }

                // la jointure entre les groupes et les classes
                // on a besoin uniquement des élèves
                // joinGroupClasses(id, enseignantDTO.getClasses());

                // La jointure avec enseignant
                final EnseignantsGroupesBean enseignantsGroupesBean =
                    new EnseignantsGroupesBean();
                enseignantsGroupesBean.setIdGroupe(id);
                enseignantsGroupesBean.setIdEnseignant(idEnseignant);
                getEntityManager().persist(enseignantsGroupesBean);
            }           
        }
        getEntityManager().flush();
    }
    
    /**
     * {@inheritDoc}
     */
    public void updateEnseignants(List<EnseignantDTO> enseignants) {
        for (EnseignantDTO enseignantDTO : enseignants) {
            // Recuperer l'enseignant       
            final EnseignantBean enseignant = findEnseignant(enseignantDTO.getEnsBean().getUid());
            final Integer idEnseignant = enseignant.getId();

            // On ne met à jours que la relation avec les etab.
            //les classes et groupes ont deja ete inserrée
            for (String enseignement : enseignantDTO.getEnseignements()){
                
                final String dnSiren = enseignement;
                final String siren = extractSirenEtablissement(dnSiren);

                int etabid = 0;

                etabid = etablissementFindBySiren(siren);

                // L'etablissement n'a pas été trouvé
                // on passe à l'enseignement suivant
                if (etabid == 0)  {
                    log.info("L'etablissement "+siren+"n'a pu être trouvé dans la BD");
                    log.info("L'enseignant "+ enseignantDTO.getEnsBean().getUid() +" ne sera donc pas inseré dans cet établissement");
                    log.info("---------------------------------------------------------");
                    continue;
                }
               
                //on ne persiste que si besoin
                if (! existEnseignantEtablissement(idEnseignant, etabid)){
                    final EtablissementsEnseignantsBean etablissementsEnseignantsBean =
                         new EtablissementsEnseignantsBean();
                     etablissementsEnseignantsBean.setIdEnseignant(idEnseignant);
                     etablissementsEnseignantsBean.setIdEtablissement(etabid);
                     getEntityManager().persist(etablissementsEnseignantsBean);
                }
            }

        }
        getEntityManager().flush();
    }

    /**
     * {@inheritDoc}
     */
    public void addEleves(List<EleveDTO> eleves) {
        for (EleveDTO eleveDTO : eleves) {
            if (eleveDTO == null){
                //On n'intégre pas les utilisateurs inactifs.
                continue;
            }
            // l'eleve
            final BaseHibernateBusiness baseHibernateBusiness =
                new BaseHibernateBusiness(this.getEntityManager());
            final Integer idEleve =
                baseHibernateBusiness.getIdInsertion("cahier_eleve");

            final EleveBean eleve = eleveDTO.getElBean();
            eleve.setId(idEleve);

            // la relation avec l'etablissement
            final String dnSiren = eleveDTO.getEtablissementDNSiren();
            final String siren = extractSirenEtablissement(dnSiren);

            int etabid = 0;

            etabid = etablissementFindBySiren(siren);
            
            log.info("Eleve siren {0} etab {1} id {2}", siren, etabid, idEleve);
            if (etabid == 0) {
                continue;
            }

            getEntityManager().persist(eleve);

            final EtablissementsElevesBean etablissementsElevesBean =
                new EtablissementsElevesBean();
            etablissementsElevesBean.setIdEleve(idEleve);
            etablissementsElevesBean.setIdEtablissement(etabid);
            getEntityManager().persist(etablissementsElevesBean);

            // les classes
            for (String classe : eleveDTO.getClasses()) {
                
                if (StringUtils.trimToNull(classe) == null) {
                    log.info( "Erreur classe éléve");
                    continue;
                }
                
                if (classe.indexOf("$") == -1) {
                    log.info( "Erreur classe éléve");
                    continue;
                }
                
                
                
                int id = findClasseByDesignation(classe);
                
                log.info("class {0} id {1}", classe, id);

                // Si la classe n'existe pas deja
                // on supose qu'il n'y a pas de doublons dans la liste des
                // classes de l'eleve
                if (id == 0) {
                    id = createClasse(classe, 0);
                    if (id == 0){
                        //ce cas est theoriquement impossible
                        log.info("Cas etrange lors de l'insertion de " + eleve.getUid());
                        log.info("Problème lors de la creation de la classe "+ classe);
                        log.info("---------------------------------------------------------");
                        continue;
                    }
                }

                // La jointure avec eleve
                final ElevesClassesBean elevesClassesBean = new ElevesClassesBean();
                elevesClassesBean.setIdClasse(id);
                elevesClassesBean.setIdEleve(idEleve);
                getEntityManager().persist(elevesClassesBean);
            }

            // les groupes
            for (String groupe : eleveDTO.getGroupes()) {
                int id = findGroupeByDesignation(groupe);

                // Si le groupe n'existe pas deja
                // on supose qu'il n'y a pas de doublons dans la liste des
                // groupes de l'enseignant
                if (id == 0) {
                    id = createGroupe(groupe, 0);
                    if (id == 0){
                        //ce cas est theoriquement impossible
                        log.info("Cas etrange lors de l'insertion de " + eleve.getUid());
                        log.info("Problème lors de la creation du groupe "+ groupe);
                        log.info("---------------------------------------------------------");
                        continue;
                    }
                }

                // la jointure entre les groupes et les classes
                joinGroupClasses(id, eleveDTO.getClasses());

                // La jointure avec enseignant
                final ElevesGroupesBean elevesGroupesBean = new ElevesGroupesBean();
                elevesGroupesBean.setIdGroupe(id);
                elevesGroupesBean.setIdEleve(idEleve);
                getEntityManager().persist(elevesGroupesBean);
            }
        }
    }
    
    /**
     * Ajout de l'inspecteur en BDD.
     * @param inspecteurs une liste d'inspecteurs à ajouter.
     */
    public void addInspecteurs(List<InspecteurDTO> inspecteurs) {
        for (InspecteurDTO inspecteurDTO : inspecteurs) {
            if (inspecteurDTO == null){
                //On n'intégre pas les utilisateurs inactifs.
                continue;
            }
            final BaseHibernateBusiness baseHibernateBusiness =
                new BaseHibernateBusiness(this.getEntityManager());
            final Integer idInspecteur =
                baseHibernateBusiness.getIdInsertion("cahier_inspecteur");

            final InspecteurBean inspecteur = inspecteurDTO.getInsBean();
            inspecteur.setId(idInspecteur);
            getEntityManager().persist(inspecteur);
        }
    }

    /**
     * Cree la jointure entre un groupe et une liste de classes.
     *
     * @param idGroupe l'id du groupe
     * @param classes une liste de nom de classes telles qu'extraites de l'annuaire (ie
     *        des dn)
     */
    private void joinGroupClasses(int idGroupe, List<String> classes) {
        // la jointure avec les classes
        for (String classe : classes) {
            final int idClasse = findClasseByDesignation(classe);

            // Si la classe n'existe pas deja
            // ca n'arrive jamais
            if (idClasse == 0) {
                log.info("Classe n'existe pas");
                return;
            }
            final Boolean existeDeja = existClasseGroupe(idClasse, idGroupe);
            if (!existeDeja) {
                // La jointure avec le groupe
                final GroupesClassesBean groupesClassesBean = new GroupesClassesBean();
                groupesClassesBean.setIdClasse(idClasse);
                groupesClassesBean.setIdGroupe(idGroupe);
                getEntityManager().persist(groupesClassesBean);
            }
        }
    }

    /**
     * Recherche si une entrée recensant la jointure entre ce groupe et cette
     * classe existe.
     *
     * @param idClasse l'id de la classe
     * @param idGroupe l'id du groupe
     *
     * @return vrai sss cette jointure existe
     */
    @Transactional(readOnly = true)
    private Boolean existClasseGroupe(int idClasse, int idGroupe) {
        final String query =
            "SELECT count(*) FROM " + GroupesClassesBean.class.getName() +
            " e WHERE e.pk.idGroupe = :id_groupe AND e.pk.idClasse = :id_classe";

        final Long id =
            (Long) getEntityManager().createQuery(query)
                       .setParameter("id_groupe", idGroupe)
                       .setParameter("id_classe", idClasse).getSingleResult();
        final Long zero = 0L;
        return !(zero.equals(id));
    }

    /**
     * Recherche si une entrée recensant la jointure entre un enseignement et un
     * enseignant existe.
     *
     * @param idEnseignant l'id de l'enseignant
     * @param idEnseignement l'id de l'enseignement
     * @param idEtablissement l'id de l'établissement
     *
     * @return vrai sss cette jointure existe
     */
    @Transactional(readOnly = true)
    private Boolean existEnseignantEnseignement(int idEnseignant, int idEnseignement, int idEtablissement) {
        final String query =
            "SELECT count(*) FROM " + EnseignantsEnseignementsBean.class.getName() +
            " e WHERE e.pk.idEnseignant = :id_enseignant AND " +
            " e.pk.idEnseignement = :id_enseignement AND e.pk.idEtablissement = :idEtablissement";

        final Long id =
            (Long) getEntityManager().createQuery(query)
                       .setParameter("id_enseignant", idEnseignant)
                       .setParameter("id_enseignement", idEnseignement)
                       .setParameter("idEtablissement", idEtablissement).getSingleResult();
        final Long zero = 0L;
        return !(zero.equals(id));
    }
    
    /**
     * Recherche si une entrée recensant la jointure entre un etablissement et un
     * enseignant existe.
     *
     * @param idEnseignant l'id de l'enseignant
     * @param idEtablissement l'id de l'etablissement
     *
     * @return vrai sss cette jointure existe
     */
    @Transactional(readOnly = true)
    private Boolean existEnseignantEtablissement(int idEnseignant, int idEtablissement) {
        final String query =
            "SELECT count(*) FROM " + EtablissementsEnseignantsBean.class.getName() +
            " e WHERE e.pk.idEnseignant = :id_enseignant AND e.pk.idEtablissement = :id_etablissement";

        final Long id =
            (Long) getEntityManager().createQuery(query)
                       .setParameter("id_enseignant", idEnseignant)
                       .setParameter("id_etablissement", idEtablissement).getSingleResult();
        final Long zero = 0L;
        return !(zero.equals(id));
    }

    /**
     * Recherche l'id d'un groupe.
     *
     * @param groupe La designation du groupe (ie un dn)
     *
     * @return l'id du groupe ou bien 0 s'il n'existe pas
     */
    @Transactional(readOnly = true)
    private int findGroupeByDesignation(String groupe) {
        final String query =
            "SELECT e.id FROM " + GroupeBean.class.getName() +
            " e WHERE e.designation = :designation and e.idEtablissement = :idEtablissement and e.idAnneeScolaire = :annee";
        Integer id;
        try {
            id = (Integer) getEntityManager().createQuery(query)
                               .setParameter("designation", extractDesignation(groupe))
                               .setParameter("idEtablissement", extractIdEtab(groupe))
                               .setParameter("annee", idAnneeScol).getSingleResult();
        } catch (NoResultException e) {
            id = 0;
        }
        return id;
    }

    /**
     * Recherche l'id d'un enseignement par libelle.
     *
     * @param libelle Le libelle de l'enseignement recherché issu de l'annuaire
     *
     * @return l'id de l'enseignement ou bien 0 s'il n'existe pas
     */
    @Transactional(readOnly = true)
    private int findEnseignementByLibelle(String libelle) {
        final String query =
            "SELECT e.id FROM " + EnseignementBean.class.getName() +
            " e WHERE e.designation = :libelle AND trim(to_char(id,'9999999999')) = code";
        Integer id;
        try {
            id = (Integer) getEntityManager().createQuery(query).setParameter("libelle", libelle)
                               .getSingleResult();
        } catch (NoResultException e) {
            id = 0;
        }
        return id;
    }
    
    
    /**
     * Recherche l'id d'une classe.
     *
     * @param classe La designation de la classe (ie un dn)
     *
     * @return l'id de la classe ou bien 0 si elle n'existe pas
     */
    @Transactional(readOnly = true)
    private int findClasseByDesignation(String classe) {
        final String query =
            "SELECT e.id FROM " + ClasseBean.class.getName() +
            " e WHERE e.designation = :designation and e.idEtablissement = :idEtablissement and e.idAnneeScolaire = :annee";

        int id;
        try {
            id = (Integer) getEntityManager().createQuery(query)
                               .setParameter("designation", extractDesignation(classe))
                               .setParameter("idEtablissement", extractIdEtab(classe))
                               .setParameter("annee", idAnneeScol).getSingleResult();
        } catch (NoResultException e) {
            id = 0;
        }
        return id;
    }

    /**
     * Recherche l'id d'un établissement.
     *
     * @param siren Le siren de l'établissement
     *
     * @return L'id de l'établissement ou bien 0 s'il n'existe pas
     */
    @Transactional(readOnly = true)
    private int etablissementFindBySiren(String siren) {
        final String query =
            "SELECT e.id FROM " + EtablissementBean.class.getName() +
            " e WHERE e.code = :code";

        int id;
        try {
            id = (Integer) getEntityManager().createQuery(query)
                               .setParameter("code", siren).getSingleResult();
        } catch (NoResultException e) {
            id = 0;
        }

        return id;
    }

    /**
     * Insere la classe dans la BD.
     *
     * @param classe le nom de la classe tel qu'extrait de l'annuaire (ie un dn)
     * @param etabid l'id de l'etablissement de la classe
     *
     * @return l'id de la nouvelle entrée
     */
    private int createClasse(String classe, int etabid) {
        final ClasseBean classeBean = new ClasseBean();

        classeBean.setDesignation(extractDesignation(classe));
        
        if (StringUtils.length(classeBean.getDesignation()) > 50) {
            log.info("Designation trop longue : [{0}]", classeBean.getDesignation());
            return 0;
        }
        
        if (etabid == 0) {
            final int idEtab = extractIdEtab(classe);
            if (idEtab == 0) {
                //si on ne le trouve pas, on a un problème
                return 0;
            }
            classeBean.setIdEtablissement(idEtab);
        } else {
            classeBean.setIdEtablissement(etabid);
        }

        final BaseHibernateBusiness baseHibernateBusiness =
            new BaseHibernateBusiness(this.getEntityManager());
        final int id = baseHibernateBusiness.getIdInsertion("cahier_classe");

        classeBean.setCode("CLA" + id);

        classeBean.setId(id);
        classeBean.setIdAnneeScolaire(idAnneeScol);

        getEntityManager().persist(classeBean);

        return id;
    }

    /**
     * Insere la classe dans la BD.
     *
     * @param groupe le nom du groupe tel qu'extrait de l'annuaire (ie un dn)
     * @param etabid l'etablissement du groupe
     *
     * @return l'id du nouveau groupe
     */
    private int createGroupe(String groupe, int etabid) {
        final GroupeBean groupeBean = new GroupeBean();

        groupeBean.setDesignation(extractDesignation(groupe));
        
        if (StringUtils.length(groupeBean.getDesignation()) > 50) {
            log.info("Designation trop longue : [{0}]", groupeBean.getDesignation());
            return 0;
        }
        if (etabid == 0) {
            final int idEtab = extractIdEtab(groupe);
            if (idEtab == 0) {
                //si on ne le trouve pas, on a un problème
                return 0;
            }
            groupeBean.setIdEtablissement(idEtab);
        } else {
            groupeBean.setIdEtablissement(etabid);
        }

        final BaseHibernateBusiness baseHibernateBusiness =
            new BaseHibernateBusiness(this.getEntityManager());
        final int id = baseHibernateBusiness.getIdInsertion("cahier_groupe");

        groupeBean.setCode("GRP" + id);

        groupeBean.setId(id);
        groupeBean.setIdAnneeScolaire(idAnneeScol);

        getEntityManager().persist(groupeBean);

        return id;
    }

    /**
     * Extrait la designation d'un dn de classe ou groupes, 
     * ENTStructureSIREN=19340011600011$2GT10,ou=structures,dc=example,dc=org
     * renvoit 2GT10.
     *
     * @param dn le dn à traiter
     *
     * @return la designation utilisée dans la BD
     */
    private String extractDesignation(String dn) {
        final int beginIndex = dn.indexOf("$") +1;

        final String nomEntite = dn.substring(beginIndex);

        return nomEntite;
    }

    /**
     * Extrait le siren d'un établissement
     * ENTStructureSIREN=19340011600011,ou=structures,dc=example,dc=org
     * renvoit 19340011600011.
     * 
     *
     * @param dn le dn à traiter
     *
     * @return le siren
     */
    private String extractSirenEtablissement(String dn) {
        final int endIndex = dn.indexOf(",ou=");

        final String siren = dn.substring(dn.indexOf("ENTStructureSIREN=") + 18, endIndex);

        return siren;
    }
    
    /**
     * Extrait le siren d'un dn de classe ou groupes 
     * 19340011600011$2GT10
     * renvoit 19340011600011.
     * 
     *
     * @param dn le dn à traiter
     *
     * @return le siren
     */
    private String extractSirenClasse(String dn) {
        
        final int endIndex = dn.indexOf("$");
        try {
        final String siren = dn.substring(0, endIndex);

        return siren;
        } catch (StringIndexOutOfBoundsException ex) {
            log.error(ex, "ex");
            return null;
        }
    }
    
    /**
     * Extrait l'id de l'etablissement d'un dn de classe ou groupes, 
     * ENTStructureSIREN=19340011600011,ou=structures,dc=example,dc=org$2GT10
     * renvoit l'id de l'etablissement 19340011600011.
     * 
     * @param groupe le groupe.
     * @return l'id dans la BD (ou 0 si l'etab existe pas)
     */
    private int extractIdEtab(String groupe) {
        final String siren = extractSirenClasse(groupe);
        final int idEtab = etablissementFindBySiren(siren);
        return idEtab;
    }

    /**
     * Insere l'enseignement dans la BD.
     *
     * @param designation la description associée à cet enseignement issue de l'annuaire
     *        LDAP
     *
     * @return l'id du nouvel enseignement
     */
    private int createEnseignement(String designation) {
        final EnseignementBean enseignementBean = new EnseignementBean();

        final BaseHibernateBusiness baseHibernateBusiness =
            new BaseHibernateBusiness(this.getEntityManager());
        final int id =
            baseHibernateBusiness.getIdInsertion("cahier_enseignement");

        enseignementBean.setCode(Integer.toString(id));
        enseignementBean.setDesignation(designation);
        enseignementBean.setId(id);

        getEntityManager().persist(enseignementBean);

        return id;
    }
    
    /**
     * Recherche un enseignant à partir de son uid.
     * @param uid l'uid de l'enseignant
     * @return l'enseignant
     */
    @SuppressWarnings("unchecked")
    private EnseignantBean findEnseignant(String uid) {
        final EnseignantBean enseignant;

        final String query =
            "SELECT e FROM " + EnseignantBean.class.getName() + " e WHERE e.uid = :uid";

        final List<EnseignantBean> liste =
            getEntityManager().createQuery(query).setParameter("uid", uid).getResultList();

        if (!CollectionUtils.isEmpty(liste)) {
            enseignant = liste.get(0);
        } else {
            enseignant = null;
        }

        return enseignant;
    }

    /**
     * Methode pour ajouter les types de devoir dans la base de donnée.
     * @param idEtablissement l'identifiant de l'établissement.
     * @param typesDevoir La liste des types de devoirs.
     */
    private void addTypeDevoir(final Integer idEtablissement, final String[] typesDevoir) {
        final BaseHibernateBusiness baseHibernateBusiness =
            new BaseHibernateBusiness(this.getEntityManager());
        for (String typeDevoir : typesDevoir) {
            final TypeDevoirBean typeDevoirBean = new TypeDevoirBean();
            final int id =
                baseHibernateBusiness.getIdInsertion("cahier_type_devoir");

            typeDevoirBean.setCode("TD" + id);
            typeDevoirBean.setLibelle(typeDevoir);
            typeDevoirBean.setId(id);
            typeDevoirBean.setIdEtablissement(idEtablissement);
            typeDevoirBean.setCategorie("NORMAL");

            getEntityManager().persist(typeDevoirBean);
        }
        getEntityManager().flush();
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public String findExerciceAnneeScolaireArchive() {
        final String query =
            "SELECT a.exercice FROM " 
            + AnneeScolaireBean.class.getName() +
            " a ORDER BY a.id DESC";

        final List<String> liste =
            getEntityManager().createQuery(query)       
            .getResultList();
        
        if (!CollectionUtils.isEmpty(liste)) {
            return liste.get(0);
        } else {
            log.info("Erreur de recherche de l'exercice à archiver.");
            return null;
        }
    }
    
    

    /**
     * {@inheritDoc}
     */
    public void renameSchemaPourArchive(final String schemaArchive) {
        Assert.isNotNull("schemaArchive", schemaArchive);
       final String nativeQueryUpdateSchema = 
           "ALTER SCHEMA " + SchemaUtils.getDefaultSchema() + " RENAME TO \"" + schemaArchive + "\"";
       
       getEntityManager().createNativeQuery(nativeQueryUpdateSchema).executeUpdate();
       getEntityManager().flush();        
    }
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public void saveListeAnneeScolaireSurSchemaCourantDeArchive(final String schemaArchive) {
        Assert.isNotNull("schemaArchive", schemaArchive);
        
        final BaseHibernateBusiness baseHibernateBusiness =
            new BaseHibernateBusiness(this.getEntityManager());

        if (baseHibernateBusiness.existSchemaIntoDataBase(schemaArchive)) {

            final String nativeQuery = 
                "SELECT a.exercice, a.date_rentree, a.date_sortie FROM \"" + 
                schemaArchive + "\".cahier_annee_scolaire a ORDER BY a.id ASC";


            final List<Object[]> liste = 
                getEntityManager().createNativeQuery(nativeQuery).getResultList();

            for (final Object[] obj : liste) {
                addAnneeScol((String)obj[0], (Date)obj[1], (Date)obj[2], null);            
            }
        } else {
            log.info("Aucune récupération d'année scolaire précédente, puisque aucune archive n'est encore présente");        
        }
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public boolean existEtablissement() {
        final String query =
            "SELECT 1 FROM " + EtablissementBean.class.getName() + " e";
        final List<Integer> list = getEntityManager().createQuery(query).getResultList();
        return !CollectionUtils.isEmpty(list);
         

    }

    
}
