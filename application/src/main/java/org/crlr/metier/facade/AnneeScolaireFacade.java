/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: AnneeScolaireFacade.java,v 1.8 2010/06/04 07:25:50 ent_breyton Exp $
 */

package org.crlr.metier.facade;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.crlr.dto.ResultatDTO;
import org.crlr.dto.application.admin.DatePeriodeQO;
import org.crlr.dto.application.admin.GenerateurDTO;
import org.crlr.dto.application.base.AnneeScolaireDTO;
import org.crlr.dto.application.base.DateAnneeScolaireQO;
import org.crlr.dto.application.base.GenericDTO;
import org.crlr.dto.application.base.OuvertureQO;
import org.crlr.dto.application.base.PeriodeVacanceQO;
import org.crlr.dto.application.base.TypeReglesAdmin;
import org.crlr.exception.metier.MetierException;
import org.crlr.message.ConteneurMessage;
import org.crlr.message.Message;
import org.crlr.metier.business.AnneeScolaireHibernateBusinessService;
import org.crlr.utils.Assert;
import org.crlr.utils.DateUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * AnneeScolaireFacade.
 *
 * @author $author$
 * @version $Revision: 1.8 $
 */
@Component
@Transactional(
        readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class
    )
public class AnneeScolaireFacade implements AnneeScolaireFacadeService {
    /** DOCUMENTATION INCOMPLETE! */
    @Autowired
    private AnneeScolaireHibernateBusinessService anneeScolaireHibernateBusinessService;

    /**
     * Mutateur anneeScolaireHibernateBusinessService.
     * @param anneeScolaireHibernateBusinessService Le anneeScolaireHibernateBusinessService à modifier
     */
    public void setAnneeScolaireHibernateBusinessService(
            AnneeScolaireHibernateBusinessService anneeScolaireHibernateBusinessService) {
        this.anneeScolaireHibernateBusinessService = anneeScolaireHibernateBusinessService;
    }

    /**
     * {@inheritDoc}
     */
    public ResultatDTO<List<AnneeScolaireDTO>> findListeAnneeScolaire()
            throws MetierException {
        return this.anneeScolaireHibernateBusinessService.findListeAnneeScolaire(true);
    }
    
    /**
     * {@inheritDoc}
     */
    public ResultatDTO<Integer> saveAnneeScolaire(final DateAnneeScolaireQO dateAnneeScolaireQO) 
            throws MetierException {
        Assert.isNotNull("dateAnneeScolaireQO", dateAnneeScolaireQO);
        final Date dateDebut = dateAnneeScolaireQO.getDateRentree();
        final Date dateFin = dateAnneeScolaireQO.getDateSortie();
        final ConteneurMessage cm = new ConteneurMessage();
        if (dateDebut == null) {
            cm.add(new Message(TypeReglesAdmin.ADMIN_04.name()));
        }
        
        if (dateFin == null) {
            cm.add(new Message(TypeReglesAdmin.ADMIN_05.name()));
        }
        
        if (cm.contientMessageBloquant()) {
            throw new MetierException(cm, "Echec de la sauvegarde de l'année scolaire.");
        }
        
        if (DateUtils.lessOrEquals(dateFin, dateDebut)) {
            cm.add(new Message(TypeReglesAdmin.ADMIN_06.name()));
        }
        
        if (cm.contientMessageBloquant()) {
            throw new MetierException(cm, "Echec de la sauvegarde de l'année scolaire.");
        }
        
        final Date dateDuJour = DateUtils.getAujourdhui();
        final Integer annee = DateUtils.getYear(dateDuJour);
        
        final Integer mois = DateUtils.getChamp(dateDuJour, Calendar.MONTH);
        final Integer anneeDebut = (mois >= Calendar.JANUARY && mois < Calendar.JULY) ? annee -1 : annee;
        
        final Date dateDebutTheorique = DateUtils.creer(anneeDebut, Calendar.JUNE, 1);
        final Date dateDebutFinTheorique = DateUtils.creer(anneeDebut, Calendar.SEPTEMBER, 30);
        final Date dateFinTheorique = DateUtils.creer(anneeDebut+1, Calendar.JUNE, 1);
        final Date dateFinFinTheorique = DateUtils.creer(anneeDebut+1, Calendar.SEPTEMBER, 30);
        
        //contrôle de l'année
        if (!DateUtils.isBetween(dateDebut, dateDebutTheorique, dateDebutFinTheorique)) {
            cm.add(new Message(TypeReglesAdmin.ADMIN_19.name(), 
                    "début", DateUtils.format(dateDebutTheorique), DateUtils.format(dateDebutFinTheorique)));
        } else if (!DateUtils.isBetween(dateFin, dateFinTheorique, dateFinFinTheorique)) {
            cm.add(new Message(TypeReglesAdmin.ADMIN_19.name(), "fin", 
                    DateUtils.format(dateFinTheorique), DateUtils.format(dateFinFinTheorique)));
        }
     
        
        if (cm.contientMessageBloquant()) {
            throw new MetierException(cm, "Echec de la sauvegarde de l'année scolaire.");
        }
        
        return this.anneeScolaireHibernateBusinessService.saveAnneeScolaire(dateAnneeScolaireQO);
    }
    
    /**
     * {@inheritDoc}
     */
    public ResultatDTO<Integer> savePeriodeVacance(final PeriodeVacanceQO periodeVacanceQO) 
    throws MetierException {
        return this.anneeScolaireHibernateBusinessService.savePeriodeVacance(periodeVacanceQO);
    }
    
    /**
     * {@inheritDoc}
     */
    public void checkDatesPeriode(final DatePeriodeQO datePeriodeQO)
    throws MetierException {
        Assert.isNotNull("datePeriodeQO", datePeriodeQO);
        final Date dateRentreeSco = datePeriodeQO.getDateRentree();
        final Date dateSortieSco = datePeriodeQO.getDateSortie();
        Assert.isNotNull("dateRentreeSco", dateRentreeSco);
        Assert.isNotNull("dateSortieSco", dateSortieSco);
        
        final ConteneurMessage cm = new ConteneurMessage();
        final Date dateDebut = datePeriodeQO.getDateDebutPlage();
        final Date dateFin = datePeriodeQO.getDateFinPlage();
        if (dateDebut == null || dateFin == null) {
            if (dateDebut == null) {
                cm.add(new Message(TypeReglesAdmin.ADMIN_01.name(), "début"));
            } else if (dateFin == null) {
                cm.add(new Message(TypeReglesAdmin.ADMIN_01.name(), "fin"));
            }
        } else {
            //La date de fin est au moins égale à la date de début + 2 jours.
            final Date dateFinMinimal = DateUtils.ajouter(dateFin, Calendar.DATE, -2);
            
            if (dateFin.before(dateDebut)) {
                cm.add(new Message(TypeReglesAdmin.ADMIN_00.name()));
            } else if (dateDebut.after(dateFinMinimal)) {
                cm.add(new Message(TypeReglesAdmin.ADMIN_08.name()));
               //contrôle des dates de la plage vis-à-vis de l'année scolaire.
            } else if (!DateUtils.isBetween(dateDebut, dateRentreeSco, dateSortieSco)) {            
                cm.add(new Message(TypeReglesAdmin.ADMIN_07.name(), "début"));
            } else if (!DateUtils.isBetween(dateFin, dateRentreeSco, dateSortieSco)) {
                cm.add(new Message(TypeReglesAdmin.ADMIN_07.name(), "fin"));
            } else {
                final String chainePlageDateExistante = datePeriodeQO.getPlageExistante();
                //si c'est la première insertion d'une plage, aucun test est nécéssaire.
                if (!StringUtils.isEmpty(chainePlageDateExistante)) {
                    final List<GenericDTO<Date, Date>> listePlageExistante = 
                        GenerateurDTO.generateListePeriodeVancanceFromDb(chainePlageDateExistante);                    

                    for (final GenericDTO<Date, Date> dto : listePlageExistante) {
                        if (DateUtils.isBetweenDateMaxNonCompris(dateDebut, dto.getValeur1(), dto.getValeur2())) {
                            cm.add(new Message(TypeReglesAdmin.ADMIN_02.name(), 
                                    "début", DateUtils.format(dateDebut), DateUtils.format(dto.getValeur1()), DateUtils.format(dto.getValeur2())));
                            break;
                        } else if (DateUtils.isBetweenDateMinNonCompris(dateFin, dto.getValeur1(), dto.getValeur2())) {
                            cm.add(new Message(TypeReglesAdmin.ADMIN_02.name(), 
                                    "fin", DateUtils.format(dateFin), DateUtils.format(dto.getValeur1()), DateUtils.format(dto.getValeur2())));
                            break;
                        } else if (DateUtils.isBetweenDateMaxNonCompris(dto.getValeur1(), dateDebut, dateFin) ||
                                DateUtils.isBetweenDateMinNonCompris(dto.getValeur2(), dateDebut, dateFin)) {
                            cm.add(new Message(TypeReglesAdmin.ADMIN_03.name(), 
                                    DateUtils.format(dateDebut), DateUtils.format(dateFin), 
                                    DateUtils.format(dto.getValeur1()), DateUtils.format(dto.getValeur2())));
                            break;
                        }
                    }
                }
            }
        }

        if (cm.contientMessageBloquant()) {
            throw new MetierException(cm, "Echec de contrôle de la nouvelle plage de jours chomés.");
        }
    }

    /**
     * {@inheritDoc}
     */
    public ResultatDTO<Integer> saveOuvertureENT(OuvertureQO ouvertureQO) throws MetierException {       
        return this.anneeScolaireHibernateBusinessService.saveOuvertureENT(ouvertureQO);
    }
}
