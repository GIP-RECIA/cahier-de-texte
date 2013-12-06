/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: jalopy.xml,v 1.1 2010/06/15 12:20:58 ent_breyton Exp $
 */

package org.crlr.metier.business;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.crlr.alimentation.DTO.EnseignantDTO;
import org.crlr.dto.ResultatDTO;
import org.crlr.dto.application.base.EnseignantsClasseGroupeQO;
import org.crlr.dto.application.base.GroupesClassesDTO;
import org.crlr.dto.application.base.RechercheGroupeClassePopupQO;
import org.crlr.dto.application.base.TypeGroupe;
import org.crlr.exception.metier.MetierException;
import org.crlr.exception.metier.MetierRuntimeException;
import org.crlr.message.ConteneurMessage;
import org.crlr.metier.entity.ClasseBean;
import org.crlr.metier.entity.ElevesClassesBean;
import org.crlr.metier.entity.EnseignantsClassesBean;
import org.crlr.metier.entity.EnseignantsEnseignementsBean;
import org.crlr.metier.entity.OuvertureInspecteurBean;
import org.crlr.metier.utils.SchemaUtils;
import org.crlr.utils.Assert;
import org.crlr.utils.ComparateurUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

/**
 * ClasseHibernateBusiness.
 *
 * @author vibertd
 * @version $Revision: 1.28 $
 */
@Repository
public class ClasseHibernateBusiness extends AbstractBusiness
    implements ClasseHibernateBusinessService {
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public ClasseBean find(Integer id) {
        final ClasseBean classe;

        final String query =
            "SELECT c FROM " + ClasseBean.class.getName() + " c WHERE c.id = :id";

        final List<ClasseBean> liste =
            getEntityManager().createQuery(query).setParameter("id", id).getResultList();

        if (!CollectionUtils.isEmpty(liste)) {
            classe = liste.get(0);
        } else {
            classe = null;
        }

        return classe;
    }

    /**
     * {@inheritDoc}
     */
    public Integer save(ClasseBean classe) throws MetierException {
        getEntityManager().persist(classe);
        getEntityManager().flush();

        return classe.getId();
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public ResultatDTO<List<GroupesClassesDTO>> findClassePopup(RechercheGroupeClassePopupQO rechercheGroupeClassePopupQO)
        throws MetierException {
        final ResultatDTO<List<GroupesClassesDTO>> classePopup =
            new ResultatDTO<List<GroupesClassesDTO>>();

        Assert.isNotNull("rechercheGroupeClassePopupQO", rechercheGroupeClassePopupQO);

        final Integer idInspecteur = rechercheGroupeClassePopupQO.getIdInspecteur();
        final Integer idEnseignant = rechercheGroupeClassePopupQO.getIdEnseignant();
        final Integer idEtablissement = rechercheGroupeClassePopupQO.getIdEtablissement();
        final Integer idAnneeScolaire = rechercheGroupeClassePopupQO.getIdAnneeScolaire();

        final String schema =
            SchemaUtils.getSchemaCourantOuArchive(rechercheGroupeClassePopupQO.getArchive(),
                                                  rechercheGroupeClassePopupQO.getExerciceScolaire());

        String requete = "";
        if (idInspecteur != null) {
            requete = "SELECT DISTINCT C.id, C.code, C.designation " + " FROM " +
                      SchemaUtils.getTableAvecSchema(schema, "cahier_enseignant_classe") +
                      " EC INNER JOIN " +
                      SchemaUtils.getTableAvecSchema(schema, "cahier_classe") +
                      " C ON (C.id = EC.id_classe)," +
                      SchemaUtils.getTableAvecSchema(schema, "cahier_ouverture_inspecteur") +
                      " OUV " + " WHERE " + " OUV.id_etablissement =? AND" +
                      " OUV.id_inspecteur =? AND" + " C.id_etablissement =? AND " +
                      " C.id_annee_scolaire =? AND ";
            if (idEnseignant != null) {
                requete += 
                      " EC.id_enseignant = ? AND ";
            }
            requete +=" EC.id_enseignant = OUV.id_enseignant " + 
                      " ORDER BY C.id ASC";
        } else if (idEnseignant != null) {
            requete = "SELECT DISTINCT C.id, C.code, C.designation " + " FROM " +
                      SchemaUtils.getTableAvecSchema(schema, "cahier_enseignant_classe") +
                      " EC INNER JOIN " +
                      SchemaUtils.getTableAvecSchema(schema, "cahier_classe") +
                      " C ON (C.id = EC.id_classe)" + " WHERE " +
                      " C.id_etablissement =? AND " + " C.id_annee_scolaire =? AND " +
                      " EC.id_enseignant =? " + " ORDER BY C.id ASC";
        } else {
            requete = "SELECT DISTINCT C.id, C.code, C.designation " + " FROM " +
                      SchemaUtils.getTableAvecSchema(schema, "cahier_classe") + " C " +
                      " WHERE " + " C.id_etablissement =? " + 
                      " ORDER BY C.id ASC";
        }

        final Query query = getEntityManager().createNativeQuery(requete);

        if (idInspecteur != null) {
            query.setParameter(1, idEtablissement);
            query.setParameter(2, idInspecteur);
            query.setParameter(3, idEtablissement);
            query.setParameter(4, idAnneeScolaire);
            if (idEnseignant != null) {
                query.setParameter(5, idEnseignant);
            }
        } else if (idEnseignant != null) {
            query.setParameter(1, idEtablissement);
            query.setParameter(2, idAnneeScolaire);
            query.setParameter(3, idEnseignant);
        } else {
            query.setParameter(1, idEtablissement);
        }

        final List<Object[]> liste = query.getResultList();
        final List<GroupesClassesDTO> resultat = new ArrayList<GroupesClassesDTO>();

        for (final Object[] classe : liste) {
            final GroupesClassesDTO classeDTO = new GroupesClassesDTO();
            classeDTO.setId((Integer) classe[0]);
            classeDTO.setCode((String) classe[1]);
            classeDTO.setIntitule((String) classe[2]);
            classeDTO.setTypeGroupe(TypeGroupe.CLASSE);
            resultat.add(classeDTO);
        }
        classePopup.setValeurDTO(ComparateurUtils.sort(resultat, "intitule"));

        return classePopup;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public Integer exist(Integer id) throws MetierException {
        final Integer idClasse;

        final String query =
            "SELECT c.id FROM " + ClasseBean.class.getName() + " c WHERE c.id = :id";

        final List<Integer> liste =
            getEntityManager().createQuery(query).setParameter("id", id).getResultList();

        if (!CollectionUtils.isEmpty(liste)) {
            idClasse = liste.get(0);
        } else {
            idClasse = null;
        }

        return idClasse;
    }

    /**
     * {@inheritDoc}
     */
    public GroupesClassesDTO findClasse(Integer idClasse) {
        final ClasseBean classeBean = this.find(idClasse);
        if (classeBean != null) {
            final GroupesClassesDTO groupesClassesDTO = new GroupesClassesDTO();

            org.crlr.utils.ObjectUtils.copyProperties(groupesClassesDTO, classeBean);
            
            groupesClassesDTO.setIntitule(classeBean.getDesignation()); 
            groupesClassesDTO.setTypeGroupe(TypeGroupe.CLASSE);

            return groupesClassesDTO;
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public Integer findByCode(final String code, final Boolean archive,
                              final String exercice)
                       throws MetierException {
        Assert.isNotNull("code", code);
        final Integer id;

        final String schema = SchemaUtils.getSchemaCourantOuArchive(archive, exercice);

        final String query =
            "SELECT c.id FROM " +
            SchemaUtils.getTableAvecSchema(schema, "cahier_classe") +
            " c WHERE c.code =?";

        final List<Integer> liste =
            getEntityManager().createNativeQuery(query).setParameter(1, code)
                .getResultList();

        if (!CollectionUtils.isEmpty(liste)) {
            id = liste.get(0);
        } else {
            id = null;
        }

        return id;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public boolean checkDroitClasse(final Integer idEnseignant, final Integer idClasse,
                                    final Boolean archive, final String exercice)
                             throws MetierException {
        boolean verif = false;

        final String schema = SchemaUtils.getSchemaCourantOuArchive(archive, exercice);

        final String query =
            "SELECT 1 FROM " +
            SchemaUtils.getTableAvecSchema(schema, "cahier_enseignant_classe") + " EC " +
            " WHERE EC.id_enseignant =? AND " + " EC.id_classe =?";

        final List<Integer> listeEnseignantsClasses =
            getEntityManager().createNativeQuery(query).setParameter(1, idEnseignant)
                .setParameter(2, idClasse).getResultList();

        if (listeEnseignantsClasses.size() > 0) {
            verif = true;
        }

        return verif;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public boolean checkDroitClasseInspecteur(final Integer idInspecteur,
                                              final Integer idClasse,
                                              final Boolean archive,
                                              final String exercice,
                                              final Integer idEtablissement)
                                       throws MetierException {
        boolean verif = false;

        final String schema = SchemaUtils.getSchemaCourantOuArchive(archive, exercice);

        final String query =
            "SELECT 1 FROM " +
            SchemaUtils.getTableAvecSchema(schema, "cahier_enseignant_classe") + " EC, " +
            SchemaUtils.getTableAvecSchema(schema, "cahier_ouverture_inspecteur") +
            " OUV " + " WHERE EC.id_enseignant =OUV.id_enseignant AND " +
            " EC.id_classe = ? AND " + 
            " OUV.id_inspecteur = ? AND " +
            " OUV.id_etablissement = ?";

        final List<Integer> listeEnseignantsClasses =
            getEntityManager().createNativeQuery(query).setParameter(1, idClasse)
                .setParameter(2, idInspecteur).setParameter(3, idEtablissement)
                .getResultList();

        if (listeEnseignantsClasses.size() > 0) {
            verif = true;
        }

        return verif;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public boolean checkDroitClasseDirection(final Integer idEtablissement,
                                             final Integer idClasse,
                                             final Boolean archive, final String exercice)
                                      throws MetierException {
        boolean verif = false;

        final String schema = SchemaUtils.getSchemaCourantOuArchive(archive, exercice);

        final String query =
            "SELECT 1 FROM " + SchemaUtils.getTableAvecSchema(schema, "cahier_classe") +
            " C " + " WHERE C.id_etablissement =? AND " + " C.id =?";

        final List<Integer> listeEnseignantsClasses =
            getEntityManager().createNativeQuery(query).setParameter(1, idEtablissement)
                .setParameter(2, idClasse).getResultList();

        if (listeEnseignantsClasses.size() > 0) {
            verif = true;
        }

        return verif;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public List<ClasseBean> findClassesEleve(Integer idEleve)
                                      throws MetierException {
        final List<ClasseBean> listeClasseBean = new ArrayList<ClasseBean>();

        final String requete =
            "SELECT " + " new map(EC.pk.idClasse as idClasse) " + " FROM " +
            ElevesClassesBean.class.getName() + " EC " + " WHERE " +
            " EC.pk.idEleve = :idEleve";

        final Query query = getEntityManager().createQuery(requete);
        final List<Map<String, ?>> resultatQuery =
            query.setParameter("idEleve", idEleve).getResultList();

        for (Map<String, ?> result : resultatQuery) {
            final ClasseBean classeBean = new ClasseBean();
            classeBean.setId((Integer) result.get("idClasse"));
            listeClasseBean.add(classeBean);
        }

        return listeClasseBean;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public Integer findIdClasseEleve(Integer idEleve) {
        Assert.isNotNull("idEleve", idEleve);

        final Integer idClasse;

        final String query =
            "SELECT ec.pk.idClasse FROM " + ElevesClassesBean.class.getName() +
            " ec WHERE ec.pk.idEleve = :id";

        final List<Integer> liste =
            getEntityManager().createQuery(query).setParameter("id", idEleve)
                .getResultList();

        if (!CollectionUtils.isEmpty(liste)) {
            idClasse = liste.get(0);
        } else {
            throw new MetierRuntimeException(new ConteneurMessage(),
                                             "Cet élève n'est rattaché à aucune classe");
        }

        return idClasse;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public List<EnseignantDTO> findEnseignantsClasse(final EnseignantsClasseGroupeQO enseignantsClasseGroupeQO) {
        Assert.isNotNull("enseignantsClasseGroupeQO", enseignantsClasseGroupeQO);

        final Integer idClasse = (Integer) enseignantsClasseGroupeQO.getIdGroupeClasse();
        Assert.isNotNull("idClasse", idClasse);
        Assert.isNotNull("etablissement", enseignantsClasseGroupeQO.getIdEtablissement());
        
        final List<EnseignantDTO> listeEnseignant =
            new ArrayList<EnseignantDTO>();

        
        final Integer idInspecteur = enseignantsClasseGroupeQO.getIdInspecteur();
        
        final String requete ;
        
        if (idInspecteur != null){
            requete = "SELECT " +
            " new map(EC.pk.idEnseignant as idEnseignant, E.civilite as civ, E.nom as nom, E.prenom as prenom) " +
            " FROM " + EnseignantsClassesBean.class.getName() +
            " EC INNER JOIN EC.enseignant E , " +
            OuvertureInspecteurBean.class.getName() + " OUV " +
            " WHERE " +
            " OUV.inspecteur.id = :idInspecteur AND " +
            " OUV.etablissement.id = :idEtablissement AND " +
            " OUV.enseignant.id = E.id AND " +
            " EC.pk.idClasse = :idClasse";
        } else {
            requete = "SELECT " +
            " new map(EC.pk.idEnseignant as idEnseignant, E.civilite as civ, E.nom as nom, E.prenom as prenom) " +
            " FROM " + EnseignantsClassesBean.class.getName() +
            " EC INNER JOIN EC.enseignant E " + " WHERE " +
            " EC.pk.idClasse = :idClasse";
        }
            

        final Query queryEnseignant = getEntityManager().createQuery(requete).setParameter("idClasse", idClasse);
        if (idInspecteur != null){
            queryEnseignant.setParameter("idInspecteur", enseignantsClasseGroupeQO.getIdInspecteur());
            queryEnseignant.setParameter("idEtablissement", enseignantsClasseGroupeQO.getIdEtablissement());
        }
        
        final List<Map<String, ?>> resultatQuery =
            queryEnseignant.getResultList();

        //recherche des enseignants de l'enseignement si besoin.
        final Integer idEnseignement =
            (Integer) enseignantsClasseGroupeQO.getIdEnseignement();
        final Boolean checkEnseignant;
        final List<Integer> listeIdEnseignant;

        if (idEnseignement != null) {
            final String query =
                "SELECT ee.pk.idEnseignant FROM " +
                EnseignantsEnseignementsBean.class.getName() +
                " ee WHERE ee.pk.idEtablissement=:idEtab AND ee.pk.idEnseignement=:idEnseignement";

            listeIdEnseignant = getEntityManager().createQuery(query)
                                    .setParameter("idEtab",
                                                  enseignantsClasseGroupeQO.getIdEtablissement())
                                    .setParameter("idEnseignement", idEnseignement)
                                    .getResultList();

            checkEnseignant = false;
        } else {
            listeIdEnseignant = new ArrayList<Integer>();
            checkEnseignant = true;
        }

        for (Map<String, ?> result : resultatQuery) {
            final Integer idEnseignant = (Integer) result.get("idEnseignant");

            if (checkEnseignant || listeIdEnseignant.contains(idEnseignant)) {
                final EnseignantDTO enseignantDTO =
                    new EnseignantDTO();
                enseignantDTO.setId(idEnseignant);
                enseignantDTO.setCivilite(StringUtils.trimToEmpty((String) result.get("civ")));
                enseignantDTO.setNom(StringUtils.trimToEmpty((String) result.get("nom")));
                enseignantDTO.setPrenom(StringUtils.trimToEmpty((String) result.get("prenom")));
                
                listeEnseignant.add(enseignantDTO);
            }
        }

        return listeEnseignant;
    }
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public int findIdClasseByCodeAndEtab(String codeClasse, int idEtab){
        List<Integer> idClasse = new ArrayList<Integer>();
        final String requete = "SELECT id FROM " + ClasseBean.class.getName() + " WHERE designation = :codeClasse " +
        " AND id_etablissement = :idEtab";
        idClasse = getEntityManager().createQuery(requete).setParameter("codeClasse", codeClasse)
            .setParameter("idEtab", idEtab).getResultList();
        if(! CollectionUtils.isEmpty(idClasse)){
        return idClasse.get(0);
        }else{
            return 0;
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public List<Integer> findListeClassePourEtab(Integer idEtab){
        final String requete = "SELECT distinct id FROM " + ClasseBean.class.getName() + " WHERE id_etablissement = :idEtab";
        final List<Integer> lesIdClasse = getEntityManager().createQuery(requete)
            .setParameter("idEtab", idEtab).getResultList();
        return lesIdClasse;
    }
    
}
