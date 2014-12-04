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

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

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
     * @param utiliseDateSeance vrai pour la date séance faux pour la date de remise de devoir
     *
     * @return la liste convertie.
     */
    public static List<JoursDTO> convertWeekCalendar(List<DetailJourDTO> liste, boolean utiliseDateSeance) {
    	return convertWeekCalendar(liste, utiliseDateSeance, true);
    }
    
    public static List<JoursDTO> convertWeekCalendar(List<DetailJourDTO> liste, boolean utiliseDateSeance, boolean withCoupe) {
        
        
        List<Map<Integer, DetailJourDTO>> mapJours = Lists.newArrayList();
        for(int i = 0; i < 7; ++i) {
            mapJours.add(new HashMap<Integer, DetailJourDTO>());
        }
        
        for (final DetailJourDTO detailJourDTO : liste) {
            if(AbstractForm.ZERO.equals(detailJourDTO.getSeance().getMinuteDebut())) {
                detailJourDTO.setHeureSeance(detailJourDTO.getHeureSeance()+"0");
            }
             if (StringUtils.isEmpty(detailJourDTO.getIntitule())) {
                detailJourDTO.setIntitule("Travail pour le " + DateUtils.format(detailJourDTO.getDateRemise(), "dd/MM/yyyy"));
            } else {
                detailJourDTO.setIntitule(detailJourDTO.getIntitule());
            }
            
             if (withCoupe) {
            detailJourDTO.getGroupesClassesDTO().setDesignation(StringUtils.abbreviate(detailJourDTO.getGroupesClassesDTO().getDesignation(), 13));            
            detailJourDTO.getSeance().setCode(StringUtils.abbreviate(detailJourDTO.getSeance().getCode(), 13));
           
             
            //detailJourDTO.setIntituleSeance(detailJourDTO.getIntituleSeance());
            /* Mantis 39548 : augmentation de la taille qui passe de 13 à 18. */
            detailJourDTO.getSeance().getSequence().setIntitule(StringUtils.abbreviate(detailJourDTO.getSeance().getSequence().getIntitule(), 13));
            /* Mantis 39548 : augmentation de la taille qui passe de 13 à 18. */
            detailJourDTO.setMatiere(StringUtils.abbreviate(detailJourDTO.getMatiere(), 13));            

            /* Mantis 39548 : augmentation de la taille qui passe de 11 à 18. */
            detailJourDTO.setNom(StringUtils.abbreviate(detailJourDTO.getNom(), 18));            
             }
            int jour = DateUtils.getChamp(utiliseDateSeance ? 
                    detailJourDTO.getDateSeance() : detailJourDTO.getDateRemise(), Calendar.DAY_OF_WEEK); 

            Preconditions.checkArgument(jour >= 1 && jour <= 7);
            
            Map<Integer, DetailJourDTO> map = mapJours.get(jour-1);
            map.put(map.size()+1, detailJourDTO);
            
        }

        int cptNbLigne = 0;

        for(int i = 0; i < mapJours.size(); ++i)
        {
            cptNbLigne = Math.max(cptNbLigne, mapJours.get(i).size());
        }
        

        final List<JoursDTO> listeConvertie = new ArrayList<JoursDTO>(cptNbLigne);

        for (int l = 1; l <= cptNbLigne; l++) {
            final JoursDTO joursDTO = new JoursDTO();
            joursDTO.getMap()
                    .put(TypeJour.LUNDI.name(),
                         (DetailJourDTO) ObjectUtils.defaultIfNull(mapJours.get(Calendar.MONDAY-1).get(l),
                                                                   new DetailJourDTO()));
            joursDTO.getMap()
                    .put(TypeJour.MARDI.name(),
                         (DetailJourDTO) ObjectUtils.defaultIfNull(mapJours.get(Calendar.TUESDAY-1).get(l),
                                                                   new DetailJourDTO()));
            joursDTO.getMap()
                    .put(TypeJour.MERCREDI.name(),
                         (DetailJourDTO) ObjectUtils.defaultIfNull(mapJours.get(Calendar.WEDNESDAY-1).get(l),
                                                                   new DetailJourDTO()));
            joursDTO.getMap()
                    .put(TypeJour.JEUDI.name(),
                         (DetailJourDTO) ObjectUtils.defaultIfNull(mapJours.get(Calendar.THURSDAY-1).get(l),
                                                                   new DetailJourDTO()));
            joursDTO.getMap()
                    .put(TypeJour.VENDREDI.name(),
                         (DetailJourDTO) ObjectUtils.defaultIfNull(mapJours.get(Calendar.FRIDAY-1).get(l),
                                                                   new DetailJourDTO()));
            joursDTO.getMap()
                    .put(TypeJour.SAMEDI.name(),
                         (DetailJourDTO) ObjectUtils.defaultIfNull(mapJours.get(Calendar.SATURDAY-1).get(l),
                                                                   new DetailJourDTO()));
            joursDTO.getMap()
                    .put(TypeJour.DIMANCHE.name(),
                         (DetailJourDTO) ObjectUtils.defaultIfNull(mapJours.get(Calendar.SUNDAY-1).get(l),
                                                                   new DetailJourDTO()));

            listeConvertie.add(joursDTO);
        }

        return listeConvertie;
    }    
    
    
}
