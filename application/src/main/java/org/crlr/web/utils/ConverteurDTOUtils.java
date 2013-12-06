/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: ConverteurDTOUtils.java,v 1.12 2010/04/19 13:35:00 ent_breyton Exp $
 */

package org.crlr.web.utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.crlr.dto.application.base.TypeJour;
import org.crlr.dto.application.devoir.DetailJourDTO;
import org.crlr.utils.DateUtils;
import org.crlr.utils.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.crlr.web.application.form.AbstractForm;
import org.crlr.web.dto.JoursDTO;

/**
 * convertWeekCalendar.
 *
 * @author breytond.
 * @version $Revision: 1.12 $
  */
public final class ConverteurDTOUtils {
/**
     * The Constructor.
     */
    private ConverteurDTOUtils() {
    }

    /**
     * Effectue la conversion.
     *
     * @param liste la liste
     *
     * @return la liste convertie.
     */
    public static List<JoursDTO> convertWeekCalendar(List<DetailJourDTO> liste) {
        final Map<Integer, DetailJourDTO> mapLundi =
            new HashMap<Integer, DetailJourDTO>();
        final Map<Integer, DetailJourDTO> mapMardi =
            new HashMap<Integer, DetailJourDTO>();
        final Map<Integer, DetailJourDTO> mapMercredi =
            new HashMap<Integer, DetailJourDTO>();
        final Map<Integer, DetailJourDTO> mapJeudi =
            new HashMap<Integer, DetailJourDTO>();
        final Map<Integer, DetailJourDTO> mapVendredi =
            new HashMap<Integer, DetailJourDTO>();
        final Map<Integer, DetailJourDTO> mapSamedi =
            new HashMap<Integer, DetailJourDTO>();
        final Map<Integer, DetailJourDTO> mapDimanche =
            new HashMap<Integer, DetailJourDTO>();
        
        
        for (final DetailJourDTO detailJourDTO : liste) {
            if(AbstractForm.ZERO.equals(detailJourDTO.getMinuteDebutSeance())) {
                detailJourDTO.setHeureSeance(detailJourDTO.getHeureSeance()+"0");
            }
            detailJourDTO.setClasse(StringUtils.abbreviate(detailJourDTO.getClasse(), 13));            
            detailJourDTO.setCodeSeance(StringUtils.abbreviate(detailJourDTO.getCodeSeance(), 13));
            if (StringUtils.isEmpty(detailJourDTO.getIntituleDevoir())) {
                detailJourDTO.setIntituleDevoir("Travail pour le " + DateUtils.format(detailJourDTO.getDate(), "dd/MM/yyyy"));
            } else {
                detailJourDTO.setIntituleDevoir(detailJourDTO.getIntituleDevoir());
            }
            detailJourDTO.setIntituleSeance(detailJourDTO.getIntituleSeance());
            /* Mantis 39548 : augmentation de la taille qui passe de 13 à 18. */
            detailJourDTO.setIntituleSequence(StringUtils.abbreviate(detailJourDTO.getIntituleSequence(), 13));
            /* Mantis 39548 : augmentation de la taille qui passe de 13 à 18. */
            detailJourDTO.setMatiere(StringUtils.abbreviate(detailJourDTO.getMatiere(), 13));            
            detailJourDTO.setGroupe(StringUtils.abbreviate(detailJourDTO.getGroupe(), 13));
            /* Mantis 39548 : augmentation de la taille qui passe de 11 à 18. */
            detailJourDTO.setNom(StringUtils.abbreviate(detailJourDTO.getNom(), 18));            
           
            switch (DateUtils.getChamp(detailJourDTO.getDate(), Calendar.DAY_OF_WEEK)) {
                case Calendar.MONDAY:
                    mapLundi.put(mapLundi.size() + 1, detailJourDTO);
                    break;
                case Calendar.TUESDAY:
                    mapMardi.put(mapMardi.size() + 1, detailJourDTO);
                    break;
                case Calendar.WEDNESDAY:
                    mapMercredi.put(mapMercredi.size() + 1, detailJourDTO);
                    break;
                case Calendar.THURSDAY:
                    mapJeudi.put(mapJeudi.size() + 1, detailJourDTO);
                    break;
                case Calendar.FRIDAY:
                    mapVendredi.put(mapVendredi.size() + 1, detailJourDTO);
                    break;
                case Calendar.SATURDAY:
                    mapSamedi.put(mapSamedi.size() + 1, detailJourDTO);
                    break;
                case Calendar.SUNDAY:
                    mapDimanche.put(mapDimanche.size() + 1, detailJourDTO);
                    break;
                default:
                    break;
            }
        }

        int cptNbLigne = 0;

        final int cptDetailLundi = mapLundi.size();
        final int cptDetailMardi = mapMardi.size();
        final int cptDetailMercredi = mapMercredi.size();
        final int cptDetailJeudi = mapJeudi.size();
        final int cptDetailVendredi = mapVendredi.size();
        final int cptDetailSamedi = mapSamedi.size();
        final int cptDetailDimanche = mapDimanche.size();

        cptNbLigne = obtenirLePlusGrand(cptDetailLundi, cptDetailMardi);
        cptNbLigne = obtenirLePlusGrand(cptNbLigne, cptDetailMercredi);
        cptNbLigne = obtenirLePlusGrand(cptNbLigne, cptDetailJeudi);
        cptNbLigne = obtenirLePlusGrand(cptNbLigne, cptDetailVendredi);
        cptNbLigne = obtenirLePlusGrand(cptNbLigne, cptDetailSamedi);
        cptNbLigne = obtenirLePlusGrand(cptNbLigne, cptDetailDimanche);

        final List<JoursDTO> listeConvertie = new ArrayList<JoursDTO>(cptNbLigne);

        for (int l = 1; l <= cptNbLigne; l++) {
            final JoursDTO joursDTO = new JoursDTO();
            joursDTO.getMap()
                    .put(TypeJour.LUNDI.name(),
                         (DetailJourDTO) ObjectUtils.defaultIfNull(mapLundi.get(l),
                                                                   new DetailJourDTO()));
            joursDTO.getMap()
                    .put(TypeJour.MARDI.name(),
                         (DetailJourDTO) ObjectUtils.defaultIfNull(mapMardi.get(l),
                                                                   new DetailJourDTO()));
            joursDTO.getMap()
                    .put(TypeJour.MERCREDI.name(),
                         (DetailJourDTO) ObjectUtils.defaultIfNull(mapMercredi.get(l),
                                                                   new DetailJourDTO()));
            joursDTO.getMap()
                    .put(TypeJour.JEUDI.name(),
                         (DetailJourDTO) ObjectUtils.defaultIfNull(mapJeudi.get(l),
                                                                   new DetailJourDTO()));
            joursDTO.getMap()
                    .put(TypeJour.VENDREDI.name(),
                         (DetailJourDTO) ObjectUtils.defaultIfNull(mapVendredi.get(l),
                                                                   new DetailJourDTO()));
            joursDTO.getMap()
                    .put(TypeJour.SAMEDI.name(),
                         (DetailJourDTO) ObjectUtils.defaultIfNull(mapSamedi.get(l),
                                                                   new DetailJourDTO()));
            joursDTO.getMap()
                    .put(TypeJour.DIMANCHE.name(),
                         (DetailJourDTO) ObjectUtils.defaultIfNull(mapDimanche.get(l),
                                                                   new DetailJourDTO()));

            listeConvertie.add(joursDTO);
        }

        return listeConvertie;
    }    
    
    /**
     * Permet de disposer de l'entier le plus grand.
     * @param cpt1 l'entier 1
     * @param cpt2 l'entier 2
     * @return l'entier le plus grand.
     */
    private static int obtenirLePlusGrand(final int cpt1, final int cpt2) {
        final int cpt;

        if (cpt1 > cpt2) {
            cpt = cpt1;
        } else {
            cpt = cpt2;
        }

        return cpt;
    }   
}
