/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: jalopy.xml,v 1.1 2010/06/15 12:20:58 ent_breyton Exp $
 */

package org.crlr.metier.business;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.crlr.alimentation.DTO.EnseignantDTO;
import org.crlr.dto.ResultatDTO;
import org.crlr.dto.application.base.ArchiveSeanceDTO;
import org.crlr.dto.application.base.EnseignementDTO;
import org.crlr.dto.application.base.GroupesClassesDTO;
import org.crlr.dto.application.base.SeanceDTO;
import org.crlr.dto.application.base.TypeGroupe;
import org.crlr.dto.application.seance.RechercheVisaSeanceQO;
import org.crlr.dto.application.visa.DateListeVisaSeanceDTO;
import org.crlr.dto.application.visa.RechercheVisaQO;
import org.crlr.dto.application.visa.ResultatRechercheVisaSeanceDTO;
import org.crlr.dto.application.visa.TypeVisa;
import org.crlr.dto.application.visa.VisaDTO;
import org.crlr.dto.application.visa.VisaEnseignantDTO;
import org.crlr.dto.application.visa.VisaDTO.VisaProfil;
import org.crlr.exception.metier.MetierException;
import org.crlr.message.ConteneurMessage;
import org.crlr.message.Message;
import org.crlr.message.Message.Nature;
import org.crlr.metier.entity.ArchiveDevoirBean;
import org.crlr.metier.entity.ArchiveSeanceBean;
import org.crlr.metier.entity.ClasseBean;
import org.crlr.metier.entity.DevoirBean;
import org.crlr.metier.entity.EnseignementBean;
import org.crlr.metier.entity.EnseignementLibelleBean;
import org.crlr.metier.entity.GroupeBean;
import org.crlr.metier.entity.SeanceBean;
import org.crlr.metier.entity.SequenceBean;
import org.crlr.metier.entity.VisaBean;
import org.crlr.metier.entity.VisaSeanceBean;
import org.crlr.metier.utils.SchemaUtils;
import org.crlr.utils.Assert;
import org.crlr.utils.ObjectUtils;
import org.springframework.stereotype.Repository;

/**
 * DOCUMENTATION INCOMPLETE!
 *
 * @author $author$
 * @version $Revision: 1.1 $
  */
@Repository
public class VisaHibernateBusiness extends AbstractBusiness
    implements VisaHibernateBusinessService {
    
    
    
    

    /**  
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public ResultatDTO<List<VisaEnseignantDTO>> findListeVisaEnseignant(final List<EnseignantDTO> listeEnseignant) {
        
        Assert.isNotNull("listeEnseignant", listeEnseignant);
        
        final ResultatDTO<List<VisaEnseignantDTO>> resultat = new ResultatDTO<List<VisaEnseignantDTO>>();
        final List<VisaEnseignantDTO> listeVisaEnseignant = new ArrayList<VisaEnseignantDTO>();
        resultat.setValeurDTO(listeVisaEnseignant);
        
        final String schema = SchemaUtils.getDefaultSchema();
        
        if (!CollectionUtils.isEmpty(listeEnseignant)) {

            // construit la clause de liste des id enseignant
            String clauseIdEnseignant = "";
            for (final EnseignantDTO enseignant : listeEnseignant) {
                final String idEnseignant = enseignant.getId().toString();
                if (clauseIdEnseignant.isEmpty()) {
                    clauseIdEnseignant = "(";
                } else {
                    clauseIdEnseignant += ",";
                }
                clauseIdEnseignant += idEnseignant;
            }
            if (!clauseIdEnseignant.isEmpty()) {
                clauseIdEnseignant += ")";
            }
             
            // Requete de recherche entete enseignant (enseignant + dernier maj)
            final String requete = 
            "select " +
                "enseignant.id," + 
                "enseignant.nom, " +
                "enseignant.prenom, " +
                "visaDernierMaj.date_dernier_maj, " +
                "visaDernierMaj.id, " +
                "dernierVisa.profil, " +
                "dernierVisa.type_visa, " +
                "dernierVisa.date_visa, " +
                "coalesce(" +
                    "(select 1 where exists (" +
                        "select 1 from " + SchemaUtils.getTableAvecSchema(schema,"cahier_visa") + " visaRouge " +
                        "where visaRouge.id_enseignant = enseignant.id " +
                        "and visaRouge.date_dernier_maj is not null " +
                        "and visaRouge.date_visa is not null " +
                        "and visaRouge.date_dernier_maj > visaRouge.date_visa )), 0)" +
            "from " + 
                SchemaUtils.getTableAvecSchema(schema,"cahier_enseignant") + " enseignant, " +
                SchemaUtils.getTableAvecSchema(schema,"cahier_visa") + " visaDernierMaj, " +
                SchemaUtils.getTableAvecSchema(schema,"cahier_visa") + " dernierVisa " +
            "where " +
                "enseignant.id in " + clauseIdEnseignant + " and " +
                "visaDernierMaj.id_enseignant = enseignant.id  and " + 
                "(visaDernierMaj.id in ( " +
                    "select visa2.id " + 
                    "from " + SchemaUtils.getTableAvecSchema(schema,"cahier_visa") + " visa2 " +
                    "where  visa2.id_enseignant = visaDernierMaj.id_enseignant and visa2.date_dernier_maj is not null " +
                    "order by visa2.date_dernier_maj desc " +
                    "limit 1) or " +
                    "visaDernierMaj.id in ( " +
                    "select visa2.id " + 
                    "from " + SchemaUtils.getTableAvecSchema(schema,"cahier_visa") + " visa2 " +
                    "where  visa2.id_enseignant = visaDernierMaj.id_enseignant and visa2.date_dernier_maj is null " +
                    "and not exists (select 1 from " + SchemaUtils.getTableAvecSchema(schema,"cahier_visa") + " visanull " +
                                    "where visanull.id_enseignant = visa2.id_enseignant " +
                                    "and visanull.date_dernier_maj is not null)" +
                    "order by visa2.date_dernier_maj desc " +
                    "limit 1)" +
                ")  and " + 
                "dernierVisa.id_enseignant = enseignant.id  and " + 
                "(dernierVisa.id in ( " +
                    "select visa3.id " +
                    "from " + SchemaUtils.getTableAvecSchema(schema,"cahier_visa") + " visa3 " +
                    "where visa3.id_enseignant = dernierVisa.id_enseignant and visa3.date_visa is not null " +
                    "order by visa3.date_visa desc " +
                    "limit 1) or " +
                    "dernierVisa.id in ( " +
                    "select visa3.id " +
                    "from " + SchemaUtils.getTableAvecSchema(schema,"cahier_visa") + " visa3 " +
                    "where visa3.id_enseignant = dernierVisa.id_enseignant and visa3.date_visa is null " +
                    "and not exists (select 1 from " + SchemaUtils.getTableAvecSchema(schema,"cahier_visa") + " visanull " +
                                    "where visanull.id_enseignant = visa3.id_enseignant " +
                                    "and visanull.date_visa is not null)" +
                    "order by visa3.date_visa desc " +
                    "limit 1)" +
                    ")" + 
            "order by nom, prenom";
            final List<Object[]> liste = getEntityManager().createNativeQuery(requete).getResultList();

            // Construit la liste de resultat
            if (liste != null && !CollectionUtils.isEmpty(liste)) {
                for (final Object[] row : liste) {
                    final VisaEnseignantDTO visaEnseignant = new VisaEnseignantDTO();
                    final EnseignantDTO enseignant = new EnseignantDTO();
                    enseignant.setId((Integer) row[0]);
                    enseignant.setNom((String) row[1]);
                    enseignant.setPrenom((String) row[2]);
                    visaEnseignant.setEnseignant(enseignant);
                    visaEnseignant.setVraiOuFauxCollapse(true);
                    visaEnseignant.setDateDernierMaj((Date) row[3]);
                    visaEnseignant.setIdDernierMaj((Integer) row[4]);
                    final String typeVisaStr =  (String) row[6];
                    if (typeVisaStr != null) {
                        visaEnseignant.setDateDernierVisa((Date) row[7]);
                        visaEnseignant.setProfilDernierVisa(VisaProfil.valueOf((String) row[5]));
                        if (typeVisaStr.equals(TypeVisa.MEMO.name())) {
                            visaEnseignant.setTypeDernierVisa(TypeVisa.MEMO);
                        } else {
                            visaEnseignant.setTypeDernierVisa(TypeVisa.SIMPLE);
                        }
                    }
                    visaEnseignant.setVraiOuFauxExisteVisaPerime((Integer) row[8] == 1);
                    final List<VisaDTO> listeVisa = new ArrayList<VisaDTO>(); 
                    visaEnseignant.setListeVisa(listeVisa);
                    
                    listeVisaEnseignant.add(visaEnseignant);
                }
            }
        }
        
        return resultat;
    }
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public ResultatDTO<ArchiveSeanceDTO> findArchiveSeance(final Integer idSeance, final Integer idVisa) {
        
        // Requete qui charge la liste des visas pour un enseignant
        final String requete = 
        "select " + 
            "new map(" +
            "   visaArchive as visaArchive, " +
            "   sequence as sequence, " +
            "   enseignement as enseignement, " +
            "   classe as classe, " +
            "   groupe as groupe, " +
            "   enseignementLibelle as enseignementLibelle) " +
        "from " + 
                ArchiveSeanceBean.class.getName() + " visaArchive " +
                "INNER JOIN visaArchive.visa visa " +
                "INNER JOIN visa.listeVisaSeance visaSeance " +
                "LEFT  JOIN visaArchive.sequence sequence " +
                "LEFT  JOIN sequence.classe classe " + 
                "LEFT  JOIN sequence.groupe groupe " +
                "LEFT  JOIN sequence.enseignement enseignement " +
                "LEFT  JOIN sequence.enseignementLibelle enseignementLibelle " +
        " where " +
                " visaSeance.pk.idSeance = :idSeance and " +
                " visaSeance.pk.idSeance = visaArchive.idSeance " +
                " and visa.id = :idVisa";
            
        final Query query = getEntityManager().createQuery(requete);

        query.setParameter("idSeance", idSeance);
        query.setParameter("idVisa", idVisa);
        
        final List<Map<String, ?>> resultatQuery = query.getResultList();

        ResultatDTO<ArchiveSeanceDTO> resultat = new ResultatDTO<ArchiveSeanceDTO>();
        
        if (CollectionUtils.isEmpty(resultatQuery) || resultatQuery.size() != 1) {
            return resultat;
        }
        
        // Recupere l'objet seance archive et ses objets lies 
        ArchiveSeanceBean seancebean = (ArchiveSeanceBean) resultatQuery.get(0).get("visaArchive");
        SequenceBean sequencebean = (SequenceBean) resultatQuery.get(0).get("sequence");
        EnseignementBean enseignementbean = (EnseignementBean) resultatQuery.get(0).get("enseignement");
        EnseignementLibelleBean enseignementlibellebean = (EnseignementLibelleBean) resultatQuery.get(0).get("enseignementLibelle");
        GroupeBean groupebean = (GroupeBean) resultatQuery.get(0).get("groupe");
        ClasseBean classebean = (ClasseBean) resultatQuery.get(0).get("classe");

        // Construit le DTO qui va etre retourne
        ArchiveSeanceDTO dto = new ArchiveSeanceDTO();
        ObjectUtils.copyProperties(dto, seancebean);
        dto.setId(seancebean.getIdArchiveSeance());
        dto.setIdSeanceOriginale(idSeance);
        ObjectUtils.copyProperties(dto.getSequenceDTO(), sequencebean);
        
        ObjectUtils.copyProperties(dto.getSequenceDTO(), sequencebean);
        if (classebean != null) {
            ObjectUtils.copyProperties(dto.getSequenceDTO().getGroupesClassesDTO(), classebean);
        } else if (groupebean != null) {
            ObjectUtils.copyProperties(dto.getSequenceDTO().getGroupesClassesDTO(), groupebean);
        }
        if (enseignementlibellebean != null) {
            dto.getSequenceDTO().setDesignationEnseignement(enseignementlibellebean.getLibelle());
        } else {
            dto.getSequenceDTO().setDesignationEnseignement(enseignementbean.getDesignation());
        }
        resultat.setValeurDTO(dto);
        return resultat;
    }
       
    public ResultatDTO<Integer> updateVisaDateMaj(Integer idSeance, Date dateMaj) {
        String hqlUpdate = "update " + VisaBean.class.getName() +
                " visa set visa.dateMaj = :dateMaj " + 
                " where visa.id in (select visaInner.id from " + VisaBean.class.getName() + " visaInner " +
                 ", " + SeanceBean.class.getName() + " sea inner join sea.sequence seq where " +
                        " sea.id = :idSeance and " +
                        "seq.idEnseignant = visaInner.idEnseignant and " +
                        "(seq.idClasse = visaInner.idClasse or (seq.idClasse is null and visaInner.idClasse is null)) and " + 
                        "(seq.idGroupe = visaInner.idGroupe or (seq.idGroupe is null and visaInner.idGroupe is null)) and " +
                        "seq.idEnseignement = visaInner.idEnseignement)";
        
             
                
     int updatedEntities = getEntityManager().createQuery( hqlUpdate )
             .setParameter( "dateMaj", dateMaj )
             .setParameter( "idSeance", idSeance )
             .executeUpdate();
     
     ResultatDTO<Integer> resultat = new ResultatDTO<Integer>();
     resultat.setValeurDTO(updatedEntities);
     return resultat;
     
    }
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public ResultatDTO<List<VisaDTO>> findListeVisa(final RechercheVisaQO rechercheVisa) {
        Assert.isNotNull("rechercheVisa", rechercheVisa);
        final Integer idEnseignant = rechercheVisa.getIdEnseignant();
        final Integer idEtablissementi = rechercheVisa.getIdEtablissement();
        final VisaProfil profils = rechercheVisa.getProfil();
        
        Assert.isNotNull("idEnseignant", idEnseignant);
        Assert.isNotNull("idEtablissement", idEtablissementi);
        final ResultatDTO<List<VisaDTO>> resultat = new ResultatDTO<List<VisaDTO>>();
        final List<VisaDTO> listeVisa = new ArrayList<VisaDTO>();
        resultat.setValeurDTO(listeVisa);

        final String schema = SchemaUtils.getDefaultSchema();
        
        // Requete qui charge la liste des visas pour un enseignant
        final String requete = 
        "select " + 
            "classe.id as idClasse, " +
            "classe.code as codeClasse, " +
            "classe.designation as designationClasse, " +
            "groupe.id as idGroupe, " +
            "groupe.code as codeGroupe, " +
            "groupe.designation as designationGroupe, " +
            "enseignement.id as idEnseignement, " +
            "enseignement.designation as designationEnseignement, " +
            "enseignementEtablissement.libelle as libelleEnseignement, " +
            "visa.date_dernier_maj as dateMaj, " +
            "visa.profil as profil, " +
            "visa.type_visa as typeVisa, " +
            "visa.date_visa as dateVisa, " +
            "visa.id as idVisa," +
            "visa.id_etablissement as idEtablissement " +  
        "from " + 
            SchemaUtils.getTableAvecSchema(schema,"cahier_visa") + " visa " +  
            "INNER JOIN " + SchemaUtils.getTableAvecSchema(schema,"cahier_enseignement") + 
                " enseignement on (enseignement.id = visa.id_enseignement) " +
            "LEFT JOIN " + SchemaUtils.getTableAvecSchema(schema,"cahier_classe") + " classe on (classe.id = visa.id_classe) " +
            "LEFT JOIN " + SchemaUtils.getTableAvecSchema(schema,"cahier_groupe") + " groupe on (groupe.id = visa.id_groupe) " +
            "LEFT JOIN " + SchemaUtils.getTableAvecSchema(schema,"cahier_libelle_enseignement") + " enseignementEtablissement " + 
            "   on (enseignementEtablissement.id_enseignement = visa.id_enseignement and " +
            "       enseignementEtablissement.id_etablissement=visa.id_etablissement) " +
        "where " + 
            "visa.id_enseignant = ? and " +
            "visa.id_etablissement = ? and " +
            "(visa.profil = ? or visa.date_visa is not null)" +
        "order by  coalesce(classe.designation, groupe.designation) , " +
                  "coalesce(enseignement.designation, enseignementEtablissement.libelle) ";        

        final List<Object[]> liste = getEntityManager()
            .createNativeQuery(requete)
            .setParameter(1, idEnseignant)
            .setParameter(2, idEtablissementi)
            .setParameter(3, profils.toString())
            .getResultList();

        // Construit la liste de resultat
        if (liste != null && !CollectionUtils.isEmpty(liste)) {
            for (final Object[] row : liste) {
                final VisaDTO visa = new VisaDTO();
                
                final Integer idClasse = (Integer) row[0];
                final String codeClasse = (String) row[1];
                final String designationClasse = (String) row[2];
                final Integer idGroupe = (Integer) row[3];
                final String codeGroupe = (String) row[4];
                final String designationGroupe = (String) row[5];
                final Integer idEnseignement = (Integer) row[6];
                final String designationEnseignement = (String) row[7];
                final String libelleEnseignement = (String) row[8];
                final Date dateMaj = (Date) row[9];
                final VisaProfil profil = VisaProfil.valueOf((String) row[10]);
                final String typeVisa = (String) row[11];
                final Date dateVisa = (Date) row[12];
                final Integer idVisa = (Integer) row[13];
                final Integer idEtablissement = (Integer) row[14];
                
                final GroupesClassesDTO classeGroupe = new GroupesClassesDTO(
                        idClasse,codeClasse,designationClasse,
                        idGroupe,codeGroupe,designationGroupe);
                final EnseignementDTO enseignement = new EnseignementDTO();
                enseignement.setId(idEnseignement);
                enseignement.setIntitule(designationEnseignement);
                enseignement.setLibellePerso(libelleEnseignement);
                
                visa.setClasseGroupe(classeGroupe);
                visa.setDateMaj(dateMaj);
                visa.setDateVisa(dateVisa);
                visa.setEnseignementDTO(enseignement);
                visa.setProfil(profil);
                if (typeVisa != null && typeVisa.equals("MEMO")) {
                    visa.setTypeVisa(TypeVisa.MEMO);
                } else if (typeVisa != null && typeVisa.equals("SIMPLE")) {
                    visa.setTypeVisa(TypeVisa.SIMPLE);
                } else {
                    visa.setTypeVisa(null);
                }
                visa.setId(idVisa);
                visa.setIdEnseignant(idEnseignant);
                visa.setIdEtablissement(idEtablissement);
                visa.calculerEstPerime(dateMaj);
                
                listeVisa.add(visa);
            }
        }
        return resultat;
    }
    
    /**
     * {@inheritDoc}
     */
    public void saveVisa(final VisaDTO visa) {
        Assert.isNotNull("visa", visa);
        Assert.isNotNull("idVisa", visa.getIdVisa());
        Assert.isNotNull("Enseignement", visa.getEnseignementDTO());
        Assert.isNotNull("ClasseGroupe", visa.getClasseGroupe());
        Assert.isNotNull("TypeGroupe", visa.getClasseGroupe().getTypeGroupe());
        
        final String schema = SchemaUtils.getDefaultSchema();
        final Integer idVisa = visa.getIdVisa();
        
        VisaBean visaBean = getEntityManager().find(VisaBean.class, idVisa);
        
        if (null == visa.getTypeVisa()) {
            visaBean.setTypeVisa(null);
            visaBean.setDateMaj(null);
            visaBean.setDateVisa(null);
        } else {
            visaBean.setDateVisa(new Date());
            
            Assert.isTrue("profil ne peut pas être changé", StringUtils.equals(
                    visaBean.getProfil(), visa.getProfil().name()));
                
            
            
            visaBean.setProfil(visa.getProfil().name());
            visaBean.setTypeVisa(visa.getTypeVisa().name());
        }
        
        getEntityManager().flush();
        
        // Requete de maj
        /*
        String query = 
            "UPDATE " + SchemaUtils.getTableAvecSchema(schema,"cahier_visa") + " " +
            "SET " +
                "date_visa = " + setDateVisa + ", " +
                "profil = '" + visa.getProfil() + "', " + 
                "type_visa = " + setTypeVisa + " " +
            "WHERE id = ?";
        getEntityManager().createNativeQuery(query).setParameter(1, idVisa).executeUpdate();
*/
        String query = null;
        
        // On supprime le visa
        if (visa.getTypeVisa() == null) {
            
            //TODO et les archives ?
            
            // Supprime la partie association visa / seance
            query = 
                "DELETE FROM " + SchemaUtils.getTableAvecSchema(schema,"cahier_visa_seance") + " vs " +
                "WHERE vs.id_visa = ?";
            getEntityManager().createNativeQuery(query).setParameter(1, idVisa).executeUpdate();
            
        // On cree / maj le visa 
        } else {

            // Supprime toutes les association visa / seance des visa perime 
            /*
            query = 
                "DELETE FROM " + SchemaUtils.getTableAvecSchema(schema,"cahier_visa_seance") + " vs " +
                "USING " + SchemaUtils.getTableAvecSchema(schema,"cahier_seance") + " s " +
                "WHERE vs.id_visa = ? " +
                "AND vs.id_seance = s.id " +
                "AND s.date_maj > vs.date_visa ";
            getEntityManager().createNativeQuery(query).setParameter(1, idVisa).executeUpdate();
            */
            
            // Enregistre toutes les seances non visees ou modifiee depuis leur dernier visa
            final Integer idEnseignant = visa.getIdEnseignant();
            final Integer idEnseignement = visa.getEnseignementDTO().getId();
            final Integer idClasseGroupe = visa.getClasseGroupe().getId();
            final Integer idEtablissement = visa.getIdEtablissement();
            final TypeGroupe typeGroupe = visa.getClasseGroupe().getTypeGroupe();
                        
            // Clause where concernant la sequence 
            String clauseWhere = 
                "seq.id_enseignant=? and " + 
                "seq.id_enseignement = ? and " + 
                "seq.id_etablissement = ? and ";
            if (TypeGroupe.CLASSE.equals(typeGroupe)) {
                clauseWhere += "seq.id_classe = ? "; 
            } else {
                clauseWhere += "seq.id_groupe = ? ";
            }
            
            // Update les liens visa / seance existant qui sont perimes
            /*
            query = 
                "update " + SchemaUtils.getTableAvecSchema(schema,"cahier_visa_seance") + " vs " +
                "set date_visa = current_timestamp, " + 
                    "type_visa = " + setTypeVisa + " " + 
                "from " + SchemaUtils.getTableAvecSchema(schema,"cahier_seance") + " sea " +  
                "where vs.id_visa = " + idVisa + 
                " and sea.id = vs.id_seance " +  
                " and vs.id_visa = ? profil = '" + profil + "' " +
                " and vs.date_visa < sea.date_maj ";
                    
            getEntityManager().createNativeQuery(query)
            .executeUpdate();     */
            
            // cree les liens visa / seance pour toutes les seances correspondant au cahier du visa
            // qui n'existent pas encore
            query = 
                "insert into " + SchemaUtils.getTableAvecSchema(schema,"cahier_visa_seance") + " " + 
                "(id_visa, id_seance) " +
                "select " + 
                    "?, sea.id " +
                "from " +
                    SchemaUtils.getTableAvecSchema(schema,"cahier_seance") + " sea " + 
                    "inner join " + SchemaUtils.getTableAvecSchema(schema,"cahier_sequence") + " seq on (seq.id = sea.id_sequence) " +
                "where " + clauseWhere + 
                " and not exists ( " +
                        "select 1 " +
                        "from " + SchemaUtils.getTableAvecSchema(schema,"cahier_visa_seance") + " vs " +
                        "where vs.id_seance = sea.id " +
                        "and vs.id_visa = " + idVisa + ")";
            
            getEntityManager().createNativeQuery(query)
            .setParameter(1, idVisa)
            .setParameter(2, idEnseignant)
            .setParameter(3, idEnseignement)
            .setParameter(4, idEtablissement)
            .setParameter(5, idClasseGroupe)
            .executeUpdate();     
        }
    }
    
    //Des requêtes pour la suppression de séances archivées.  La clause 'where' sera remplacé par l'un des méthodes de supression.
    private static final String WHERE_CLAUSE_TOKEN = "WHERE_CLAUSE";
    private static final String SUPPRIMER_ARCHIVE_PJ_DEVOIR_QUERY = 
            "DELETE FROM " + SchemaUtils.getTableAvecSchema(SchemaUtils.getDefaultSchema(),"cahier_archive_piece_jointe_devoir") + " apj " +
                    " WHERE apj.id_archive_devoir IN (SELECT ad.id_archive_devoir from " + 
                    SchemaUtils.getTableAvecSchema(SchemaUtils.getDefaultSchema(),"cahier_archive_devoir") + " ad " +
                    "inner join " + SchemaUtils.getTableAvecSchema(SchemaUtils.getDefaultSchema(),"cahier_archive_seance") + " aSea " +
                    "ON ad.id_archive_seance = aSea.id_archive_seance WHERE " + WHERE_CLAUSE_TOKEN +
                    ")"; ;
                    
    private static final String SUPPRIMER_ARCHIVE_DEVOIR_QUERY = "DELETE FROM "
            + SchemaUtils.getTableAvecSchema(SchemaUtils.getDefaultSchema(),
                    "cahier_archive_devoir")
            + " ad "
            + " USING "
            + SchemaUtils.getTableAvecSchema(SchemaUtils.getDefaultSchema(),
                    "cahier_archive_seance") + " aSea "
            + "WHERE ad.id_archive_seance = aSea.id_archive_seance AND "
            + WHERE_CLAUSE_TOKEN;

    private static final String SUPPRIMER_ARCHIVE_PJ_SEANCE_QUERY = "DELETE FROM "
            + SchemaUtils.getTableAvecSchema(SchemaUtils.getDefaultSchema(),
                    "cahier_archive_piece_jointe_seance")
            + " pj "
            + "USING "
            + SchemaUtils.getTableAvecSchema(SchemaUtils.getDefaultSchema(),
                    "cahier_archive_seance")
            + " aSea "
            + "WHERE pj.id_archive_seance = aSea.id_archive_seance AND "
            + WHERE_CLAUSE_TOKEN;
    
    private static final String SUPRRIMER_ARCHIVE_SEANCES_QUERY = "DELETE FROM "
            + SchemaUtils.getTableAvecSchema(SchemaUtils.getDefaultSchema(),
                    "cahier_archive_seance")
            + " aSea "
            + "WHERE "
            + WHERE_CLAUSE_TOKEN;

    /**
     * {@inheritDoc}
     */
    public void supprimerArchiveVisa(final VisaDTO visa) {
        final Integer idVisa = visa.getIdVisa();
        
        // Supprime les association PJ / ArchiveDevoir
        String whereClause = "aSea.id_visa = ?";
        
        String query = SUPPRIMER_ARCHIVE_PJ_DEVOIR_QUERY.replace(WHERE_CLAUSE_TOKEN, whereClause);
                       
        getEntityManager().createNativeQuery(query).setParameter(1, idVisa).executeUpdate();
        
        // Supprime la partie devoir
        query = SUPPRIMER_ARCHIVE_DEVOIR_QUERY.replace(WHERE_CLAUSE_TOKEN, whereClause);
           
        getEntityManager().createNativeQuery(query).setParameter(1, idVisa).executeUpdate();

        // Supprime les association PJ / ArchiveSeance
        query = SUPPRIMER_ARCHIVE_PJ_SEANCE_QUERY.replace(WHERE_CLAUSE_TOKEN, whereClause);
        
       getEntityManager().createNativeQuery(query).setParameter(1, idVisa).executeUpdate();
       
       // Supprime les archives séances
       query = SUPRRIMER_ARCHIVE_SEANCES_QUERY.replace(WHERE_CLAUSE_TOKEN, whereClause);
               
       getEntityManager().createNativeQuery(query).setParameter(1, idVisa).executeUpdate();

        
    }
    
    /**
     * Suppression de l'archive.
     * @param seanceDTO une séance (pas une séance archivée) 
     */
    public void supprimerArchiveVisa(final SeanceDTO seanceDTO) {
        final Integer idSeance = seanceDTO.getId();
        
        // Supprime les association PJ / ArchiveDevoir
        String whereClause = "aSea.id_seance = ?";
        
        String query = SUPPRIMER_ARCHIVE_PJ_DEVOIR_QUERY.replace(WHERE_CLAUSE_TOKEN, whereClause);
                       
        getEntityManager().createNativeQuery(query).setParameter(1, idSeance).executeUpdate();
        
        // Supprime la partie devoir
        query = SUPPRIMER_ARCHIVE_DEVOIR_QUERY.replace(WHERE_CLAUSE_TOKEN, whereClause);
           
        getEntityManager().createNativeQuery(query).setParameter(1, idSeance).executeUpdate();

        // Supprime les association PJ / ArchiveSeance
        query = SUPPRIMER_ARCHIVE_PJ_SEANCE_QUERY.replace(WHERE_CLAUSE_TOKEN, whereClause);
        
       getEntityManager().createNativeQuery(query).setParameter(1, idSeance).executeUpdate();
       
       // Supprime les archives séances
       query = SUPRRIMER_ARCHIVE_SEANCES_QUERY.replace(WHERE_CLAUSE_TOKEN, whereClause);
               
       getEntityManager().createNativeQuery(query).setParameter(1, idSeance).executeUpdate();

    }
    
    /**
     * Supression du lien entre une séance et une visa pour qu'on puisse supprimer une séance.
     * @param seanceDTO séance
     */
    public void supprimerVisaSeance(final SeanceDTO seanceDTO) {
        String query = "DELETE FROM "
                + SchemaUtils.getTableAvecSchema(SchemaUtils.getDefaultSchema(),
                        "cahier_visa_seance")
                + " aSea "
                + "WHERE aSea.id_seance = ?";
        
        getEntityManager().createNativeQuery(query).setParameter(1, seanceDTO.getId()).executeUpdate();
    }
    
  
    
    /**
     * {@inheritDoc} 
     */
    @SuppressWarnings("unchecked")
    public void archiverVisa(final VisaDTO visa) {
        Assert.isNotNull("visa", visa);
        Assert.isNotNull("idVisa", visa.getIdVisa());
        Assert.isNotNull("Enseignement", visa.getEnseignementDTO());
        Assert.isNotNull("ClasseGroupe", visa.getClasseGroupe());
        Assert.isNotNull("TypeGroupe", visa.getClasseGroupe().getTypeGroupe());
        
        final String schema = SchemaUtils.getDefaultSchema();
        final Integer idEnseignant = visa.getIdEnseignant();
        final Integer idEnseignement = visa.getEnseignementDTO().getId();
        final Integer idClasseGroupe = visa.getClasseGroupe().getId();
        final Integer idEtablissement = visa.getIdEtablissement();
        final TypeGroupe typeGroupe = visa.getClasseGroupe().getTypeGroupe();
        
        // Clause where concernant la sequence 
        String clauseWhere = 
            "seq.id_enseignant=? and " + 
            "seq.id_enseignement = ? and " + 
            "seq.id_etablissement = ? and ";
        if (TypeGroupe.CLASSE.equals(typeGroupe)) {
            clauseWhere += "seq.id_classe = ? "; 
        } else {
            clauseWhere += "seq.id_groupe = ? ";
        }
        
        
        // Archive les objets SEANCE
        String query = 
            "insert into " + SchemaUtils.getTableAvecSchema(schema,"cahier_archive_seance") + " " + 
            "(id_archive_seance, id_seance, date_archive, id_visa, code, intitule, date, " +
            " heure_debut, minute_debut, heure_fin, minute_fin, " +
            " description, annotations, id_sequence, id_enseignant) " +
            "select " +
                "nextval('cahier_archive_seance'), sea.id, current_timestamp, " + visa.getIdVisa() + ", sea.code, sea.intitule, sea.date, " +
                "sea.heure_debut, sea.minute_debut, sea.heure_fin, sea.minute_fin, " +
                "sea.description, sea.annotations, sea.id_sequence, sea.id_enseignant " + 
            "from " + 
                SchemaUtils.getTableAvecSchema(schema,"cahier_seance sea") + " " +
                "inner join " + SchemaUtils.getTableAvecSchema(schema,"cahier_sequence") + " seq on (seq.id = sea.id_sequence) " +
            "where " + clauseWhere;
        getEntityManager().createNativeQuery(query)
            .setParameter(1, idEnseignant)
            .setParameter(2, idEnseignement)
            .setParameter(3, idEtablissement)
            .setParameter(4, idClasseGroupe)
            .executeUpdate();
        
        
        //Puisque il n'y a pas de lien dans la base entere devoir et archive devoir, il faut stocker l'association 
        Map<Integer, Integer> devoirVerArchiveDevoirId = new HashMap<Integer, Integer>();
        
        query = "select " + 
                "archSea.idArchiveSeance, dev as devoirBean " +
        " from " + DevoirBean.class.getName()
         + " dev " +
        "inner join dev.seance sea, " +
        " " + ArchiveSeanceBean.class.getName()  +
        " archSea " +                
    "where sea.id = archSea.idSeance and archSea.visa.id  = :idVisa " ; 
        
        List<Object[]> listeD = getEntityManager().createQuery(query)
                .setParameter("idVisa", visa.getIdVisa()).getResultList();
    
        final BaseHibernateBusiness baseHibernateBusiness =
                new BaseHibernateBusiness(this.getEntityManager());
        
     // archive les devoirs des seances
        for(Object[] row : listeD) {
            Integer idArchiveDevoir = baseHibernateBusiness.getIdInsertion("cahier_archive_devoir");
            Integer idArchiveSeance = (Integer) row[0];
            DevoirBean devoirBean = (DevoirBean) row[1];
            
            ArchiveDevoirBean archiveDevoirBean = new ArchiveDevoirBean();
            ObjectUtils.copyProperties(archiveDevoirBean, devoirBean);
            archiveDevoirBean.setIdArchiveDevoir(idArchiveDevoir);
            archiveDevoirBean.setIdArchiveSeance(idArchiveSeance);
            
            getEntityManager().persist(archiveDevoirBean);
            
            /*query = 
                    "insert into " + SchemaUtils.getTableAvecSchema(schema,"cahier_archive_devoir") + " " + 
                    "(id_archive_devoir, date_remise, code, intitule, description, id_type_devoir, id_archive_seance) " +
                            " values (?, ?, ?, ?, ?, ?, ?)";
            Query nativeQuery = getEntityManager().createNativeQuery(query);
            
            for(int i = 0; i < archiveDevoirChamps; ++i) {
                nativeQuery.setParameter(i + 1, row[i]);
            }
                    
            r = nativeQuery.executeUpdate();*/
            
            devoirVerArchiveDevoirId.put(devoirBean.getId(), idArchiveDevoir);
            log.info("devoir id {0} archive devoir id {1}", devoirBean.getId(), idArchiveDevoir);
        }
        
        getEntityManager().flush();
        
        // archive les pj devoir
        for(Integer devoirId : devoirVerArchiveDevoirId.keySet()) {
            Integer archiveDevoirId = devoirVerArchiveDevoirId.get(devoirId);
            
            query = 
                    "insert into " + SchemaUtils.getTableAvecSchema(schema,"cahier_archive_piece_jointe_devoir") + " " +
                    "(id_piece_jointe, id_archive_devoir) " +
                    "select " + 
                        "pj.id_piece_jointe, ? " +
                    "from " + 
                        SchemaUtils.getTableAvecSchema(schema,"cahier_piece_jointe_devoir") + " pj " +
                        "inner join " + SchemaUtils.getTableAvecSchema(schema,"cahier_devoir") + " dev on (dev.id = pj.id_devoir) " + 
                     
                        "where dev.id = ? "; 
                getEntityManager().createNativeQuery(query)
                        .setParameter(1, archiveDevoirId)
                .setParameter(2, devoirId)
                .executeUpdate();
        }
        
       
    
        // archive les pj seance
        query =
            "insert into " + SchemaUtils.getTableAvecSchema(schema,"cahier_archive_piece_jointe_seance") + " " + 
            "(id_piece_jointe, id_archive_seance) " +
            "select " + 
                "pj.id_piece_jointe, archSea.id_archive_seance " +
            "from " + 
                SchemaUtils.getTableAvecSchema(schema,"cahier_piece_jointe_seance") + " pj " + 
                "inner join " + SchemaUtils.getTableAvecSchema(schema,"cahier_seance") + " sea on (sea.id = pj.id_seance) " +
                "inner join " + SchemaUtils.getTableAvecSchema(schema,"cahier_archive_seance") + 
                " archSea on (sea.id = archSea.id_seance) " +               
                
            "where archSea.id_visa = ? "; 
        getEntityManager().createNativeQuery(query)
        .setParameter(1, visa.getIdVisa())
        .executeUpdate();
        
       
    
       
    }
    
    /**
     * Recupere dans la liste l'element correspondant a la date.
     * @param dateSeance la date de seance recherche
     * @param listeSeance la liste globale
     * @return l'element trouve.
     */
    private DateListeVisaSeanceDTO getDateListeVisaSeanceDTO(final Date dateSeance, final List<DateListeVisaSeanceDTO> listeSeance) {
        for (final DateListeVisaSeanceDTO date : listeSeance) {
            if (date.getDate().equals(dateSeance)) {
                return date;
            }
        }
        return null;
    }
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public ResultatDTO<List<DateListeVisaSeanceDTO>> findVisaSeance(RechercheVisaSeanceQO rechercheSeanceQO)
        throws MetierException {
        Assert.isNotNull("rechercheSeanceQO", rechercheSeanceQO);

        final ConteneurMessage conteneurMessage = new ConteneurMessage();

        //Paramètres de recherche
        final Integer idEtablissement = rechercheSeanceQO.getIdEtablissement();
        final Integer idEnseignement = rechercheSeanceQO.getIdEnseignement();
        final Integer idEnseignant = rechercheSeanceQO.getIdEnseignant();
        final Integer idClasseGroupe = rechercheSeanceQO.getIdClasseGroupe();
        final Integer idSequence = rechercheSeanceQO.getIdSequence();
        final TypeGroupe typeGroupe = rechercheSeanceQO.getTypeGroupe();

        final ResultatDTO<List<DateListeVisaSeanceDTO>> resultat = new ResultatDTO<List<DateListeVisaSeanceDTO>>();
            
        
        String requeteWHERE = "";

        if (idEnseignement != null) {
            requeteWHERE = requeteWHERE.concat(" AND SEQ.idEnseignement = :idEnseignement ");
        }
        if (idClasseGroupe != null) {
            if (typeGroupe  == (TypeGroupe.GROUPE)) {
                requeteWHERE = requeteWHERE.concat(" AND SEQ.idGroupe = :idGroupe ");
            } else if (typeGroupe  == (TypeGroupe.CLASSE)) {
                requeteWHERE = requeteWHERE.concat(" AND SEQ.idClasse = :idClasse ");
            }
        }

       
        
        if (idSequence != null) {
            requeteWHERE = requeteWHERE.concat(" AND SEQ.id = :idSequence ");
        } 
        
        //Filtrer sur l'etablissement
            
        requeteWHERE = requeteWHERE.concat(" AND (CLA.idEtablissement = :idEtabCla ");
        requeteWHERE = requeteWHERE.concat(" OR GRP.idEtablissement = :idEtabGrp) ");
    
        

        String requete = "";

        final String jointureVisaSeance =  
            " VISA_DIR.idEnseignant = SEQ.idEnseignant AND " +
            " VISA_DIR.profil = 'ENTDirecteur' AND " +
            " VISA_DIR.idEnseignement = SEQ.idEnseignement AND " + 
            " (VISA_DIR.idClasse = SEQ.idClasse OR (VISA_DIR.idClasse is null AND  SEQ.idClasse is null) ) AND " + 
            " (VISA_DIR.idGroupe = SEQ.idGroupe OR (VISA_DIR.idGroupe is null AND  SEQ.idGroupe is null) ) AND " +
            //" (VS_DIR.pk.idSeance = SEA.id OR  VS_DIR.pk.idSeance is null) AND " +  

            " VISA_INS.idEnseignant = SEQ.idEnseignant AND " +
            " VISA_INS.profil = 'ENTInspecteur' AND " +
            " VISA_INS.idEnseignement = SEQ.idEnseignement AND " + 
            " (VISA_INS.idClasse = SEQ.idClasse OR (VISA_INS.idClasse is null AND  SEQ.idClasse is null) ) AND " + 
            " (VISA_INS.idGroupe = SEQ.idGroupe OR (VISA_INS.idGroupe is null AND  SEQ.idGroupe is null) ) AND " ;
            //" (VS_INS.pk.idSeance = SEA.id OR  VS_INS.pk.idSeance is null) AND "; 
       
        
        final String existsVisaSqlQuery = "SELECT count(*) from "
                + VisaSeanceBean.class.getName()
                + " VS where VS.visa.id = :idVisa and VS.seance.id = :idSeance ";
        
            requete = "SELECT " + " new map(" + "SEA as seance, GRP.code as codeGroupe, " +
                      "VISA_DIR as visaDirecteur, VISA_INS as visaInspecteur," +
                      "GRP as groupe, " +
                      "CLA as classe, " +
                      "ENS.designation as designationEnseignement, ENS.id as idEnseignement, " +
                      "SEQ.description as designationSequence )" +
                      " FROM " + SequenceBean.class.getName() + " SEQ " +
                      " INNER JOIN SEQ.enseignement ENS " +
                      " LEFT JOIN SEQ.classe CLA " +
                      " LEFT JOIN SEQ.groupe GRP, " + SeanceBean.class.getName() + " SEA, " +
                      VisaBean.class.getName() + " as VISA_DIR, "  +
                      VisaBean.class.getName() + " as VISA_INS " +
                      " WHERE " + jointureVisaSeance + " SEQ.idEnseignant = :idEnseignant AND " +
                      " SEA.idSequence = SEQ.id " + requeteWHERE +
                      " ORDER BY SEA.date DESC, SEA.code DESC";
       // }

        final Query query = getEntityManager().createQuery(requete);

        query.setParameter("idEnseignant", idEnseignant);

        if (idEnseignement != null) {
            query.setParameter("idEnseignement", idEnseignement);
        }
        if (idClasseGroupe != null) {
            if (typeGroupe  == (TypeGroupe.GROUPE)) {
                query.setParameter("idGroupe", idClasseGroupe);
            } else if (typeGroupe  == (TypeGroupe.CLASSE)) {
                query.setParameter("idClasse", idClasseGroupe);
            }
        }
        if (idSequence != null) {
            query.setParameter("idSequence", idSequence);
        }
        

        if (idEtablissement == null) {
                ConteneurMessage cm = new ConteneurMessage();
                cm.add(new Message(Message.Code.ERREUR_INCONNUE.getId(), Nature.BLOQUANT));
                throw new MetierException(cm, "Erreur interne");
            }
        
        query.setParameter("idEtabGrp", idEtablissement);
        query.setParameter("idEtabCla", idEtablissement);
        

        final List<Map<String, ?>> resultatQuery = query.getResultList();
        final List<DateListeVisaSeanceDTO> listeResultat = new ArrayList<DateListeVisaSeanceDTO>();
        for (final Map<String, ?> result : resultatQuery) {
            final ResultatRechercheVisaSeanceDTO resultatRechercheVisaSeanceDTO =
                new ResultatRechercheVisaSeanceDTO();
            
            resultatRechercheVisaSeanceDTO.setProfil(rechercheSeanceQO.getProfil());
            
            final SeanceBean seance = (SeanceBean) result.get("seance");
            //final VisaSeanceBean visaSeanceDirecteur = (VisaSeanceBean) result.get("visaSeanceDirecteur");
            //final VisaSeanceBean visaSeanceInspecteur = (VisaSeanceBean) result.get("visaSeanceInspecteur");
            final VisaBean visaDirecteur = (VisaBean) result.get("visaDirecteur");
            final VisaBean visaInspecteur = (VisaBean) result.get("visaInspecteur");
            
            ObjectUtils.copyProperties(resultatRechercheVisaSeanceDTO, seance);
            
            resultatRechercheVisaSeanceDTO.getSequence().setIdEnseignant(seance.getIdEnseignant());
            resultatRechercheVisaSeanceDTO.getSequence().setId(seance.getIdSequence());
            resultatRechercheVisaSeanceDTO.getEnseignantDTO().setId(seance.getIdEnseignant());
                        
            resultatRechercheVisaSeanceDTO.setProfil(rechercheSeanceQO.getProfil());
            
            ObjectUtils.copyProperties(resultatRechercheVisaSeanceDTO.getVisaDirecteur(), visaDirecteur);
            ObjectUtils.copyProperties(resultatRechercheVisaSeanceDTO.getVisaInspecteur(), visaInspecteur);
            
            
            final Query existsVisaQuery = getEntityManager().createQuery(existsVisaSqlQuery);
            existsVisaQuery.setParameter("idSeance", seance.getId());
            existsVisaQuery.setParameter("idVisa", visaDirecteur.getId());
            boolean vraiOuFauxVisaSeanceExisteDirecteur = ( (Long)existsVisaQuery.getSingleResult() ) > 0;
            
            //Le cas où une séance est créee après le visa.  Le visa 'n'existe pas' pour la séance.
            if (!vraiOuFauxVisaSeanceExisteDirecteur) {
                resultatRechercheVisaSeanceDTO.getVisaDirecteur().setDateVisa(null);
                resultatRechercheVisaSeanceDTO.getVisaDirecteur().setTypeVisa(null);
            }
            
            
            existsVisaQuery.setParameter("idSeance", seance.getId());
            existsVisaQuery.setParameter("idVisa", visaInspecteur.getId());
            boolean vraiOuFauxVisaSeanceExisteInspecteur = ( (Long)existsVisaQuery.getSingleResult() ) > 0;

            if (!vraiOuFauxVisaSeanceExisteInspecteur) {
                resultatRechercheVisaSeanceDTO.getVisaInspecteur().setDateVisa(null);
                resultatRechercheVisaSeanceDTO.getVisaInspecteur().setTypeVisa(null);
            }
            query.setParameter("idEnseignant", idEnseignant);
            
            resultatRechercheVisaSeanceDTO.setVraiOuFauxVisaSeanceExisteDirecteur(vraiOuFauxVisaSeanceExisteDirecteur);
            resultatRechercheVisaSeanceDTO.setVraiOuFauxVisaSeanceExisteInspecteur(vraiOuFauxVisaSeanceExisteInspecteur);
            
            resultatRechercheVisaSeanceDTO.getVisaInspecteur().calculerEstPerime(resultatRechercheVisaSeanceDTO.getDateMaj());
            resultatRechercheVisaSeanceDTO.getVisaDirecteur().calculerEstPerime(resultatRechercheVisaSeanceDTO.getDateMaj());
            
            GroupesClassesDTO groupesClassesDTO = resultatRechercheVisaSeanceDTO.getSequenceDTO().getGroupesClassesDTO();
           
            GroupeBean groupeBean = (GroupeBean) result.get("groupe");
            ClasseBean classeBean = (ClasseBean) result.get("classe");
            
            if (groupeBean != null) {
                groupesClassesDTO.setTypeGroupe(TypeGroupe.GROUPE);
                ObjectUtils.copyProperties(groupesClassesDTO, groupeBean);
                
            } else if (classeBean != null) {
                groupesClassesDTO.setTypeGroupe(TypeGroupe.CLASSE);                
                ObjectUtils.copyProperties(groupesClassesDTO, classeBean);
            }
            
            //Propage la classe / groupe données aux visas aussi
            resultatRechercheVisaSeanceDTO.getVisaDirecteur().setClasseGroupe(groupesClassesDTO);
            resultatRechercheVisaSeanceDTO.getVisaInspecteur().setClasseGroupe(groupesClassesDTO);

            Integer idEnseignenment = (Integer) result.get("idEnseignement");
            resultatRechercheVisaSeanceDTO.getVisaDirecteur().getEnseignementDTO().setId(idEnseignenment);
            resultatRechercheVisaSeanceDTO.getVisaInspecteur().getEnseignementDTO().setId(idEnseignenment);
            
            
            resultatRechercheVisaSeanceDTO.getSequence().setDesignationEnseignement((String) result.get("designationEnseignement"));
            resultatRechercheVisaSeanceDTO.getSequence().setIdEnseignement(idEnseignenment);
            
            // Filtrage
            // One ne veut pas voir les seance non visees.
            if (BooleanUtils.isNotTrue(rechercheSeanceQO.getAfficheNonVisees())
                    && (BooleanUtils.isFalse(resultatRechercheVisaSeanceDTO
                            .getEstVisee()))) {
                continue;

            }

            // One ne veut pas voir les seance qui ont ete visees et ne sont pas perimées.
            if (BooleanUtils.isNotTrue(rechercheSeanceQO.getAfficheVisees())
                    && (BooleanUtils.isTrue(resultatRechercheVisaSeanceDTO
                            .getEstVisee()) && BooleanUtils
                            .isNotTrue(resultatRechercheVisaSeanceDTO
                                    .getAlerteDateMaj()))) {
                continue;
            }

            // One ne veut pas voir les seance qui sont perimees. 
            //n'Affiche pas les séances visés et perimées
            if (BooleanUtils.isNotTrue(rechercheSeanceQO.getAffichePerimees()) &&
                     (BooleanUtils.isTrue(resultatRechercheVisaSeanceDTO
                            .getEstVisee()) && BooleanUtils
                            .isTrue(resultatRechercheVisaSeanceDTO
                                    .getAlerteDateMaj()))) {
                continue;
            }
                
            
            DateListeVisaSeanceDTO dateListe = getDateListeVisaSeanceDTO(resultatRechercheVisaSeanceDTO.getDate(),listeResultat);
            if (dateListe == null) {
                dateListe = new DateListeVisaSeanceDTO();
                dateListe.setDate(resultatRechercheVisaSeanceDTO.getDate());
                listeResultat.add(dateListe);
            }
            final List<ResultatRechercheVisaSeanceDTO> listeSeanceDTO = dateListe.getListeVisaSeance(); 
            listeSeanceDTO.add(resultatRechercheVisaSeanceDTO);
        }

        if (listeResultat.size() == 0) {
            conteneurMessage.add(new Message(rechercheSeanceQO.getTypeReglesSeance().name(),
                                             Nature.INFORMATIF));
            resultat.setConteneurMessage(conteneurMessage);
            //throw new MetierException(conteneurMessage,
              //                        "Il n'existe aucun résultat pour votre recherche.");
        }
        resultat.setValeurDTO(listeResultat);
        return resultat;
    }
    
    /**
     * {@inheritDoc}
     */
    public void updateVisa(final Integer idVisa, final String profil, final String typeVisa, final String dateVisa) {
        final String schema = SchemaUtils.getDefaultSchema();
        
        // Requete de maj du visa chapeau
        final String query = 
            "UPDATE " + SchemaUtils.getTableAvecSchema(schema,"cahier_visa") + " " +
            "SET " +
                "date_visa = " + dateVisa + ", " +
                "profil = '" + profil + "', " + 
                "type_visa = '" + typeVisa + "' " +
            "WHERE id = ?";

        getEntityManager().createNativeQuery(query).setParameter(1, idVisa).executeUpdate();
    }
    
   
    
    
  
}
