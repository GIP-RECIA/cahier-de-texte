/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: DateConverter.java,v 1.1 2009/03/17 16:30:44 ent_breyton Exp $
 */

package org.crlr.facelets.ext;

import org.crlr.log.Log;
import org.crlr.log.LogFactory;

import org.crlr.utils.DateUtils;
import org.crlr.utils.ObjectUtils;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Calendar;
import java.util.Date;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

/**
 * Convertisseur JSF d'objets {@link Date}. Le format de conversion peut être
 * spécifié avec la propriété {@link #pattern}. Par défaut, le format de conversion est
 * <code>dd/MM/yyyy</code>.
 *
 * @author breytond
 */
public class DateConverter implements Converter, Serializable {
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The log. */
    protected final transient Log log = LogFactory.getLog(getClass());

    /** The Constant CONVERTER_ID. */
    public static final String CONVERTER_ID = "si@drh.converter.dateConverter";

    /** The Constant DEFAULT_PATTERN. */
    public static final String DEFAULT_PATTERN = "dd/MM/yyyy";

    /** The pattern. */
    private String pattern = DEFAULT_PATTERN;

    /** The Constant MAX_YEAR. */
    private static final int MAX_YEAR = 2070;

    /** The Constant MIN_YEAR. */
    private static final int MIN_YEAR = 1900;

/**
     * The Constructor.
     */
    public DateConverter() {
        log.debug("Constructeur de DateConverter");
    }

    /**
     * {@inheritDoc}
     */
    public Object getAsObject(FacesContext contexte, UIComponent component, String strDate)
                       throws ConverterException {
        if (StringUtils.isEmpty(strDate)) {
            return null;
        }

        // On réexécute le code js du formatage de la date
        // en pensant peut etre que le code js n'a pas
        // pu s'exécuter ce qui est le cas lorsque l'on
        // a un "supporter" sur un champ
        final String strDatePreFormatee = preFormatDate(strDate);

        final DateFormat sdf = createDateFormat(pattern);

        final Date date;
        try {
            date = sdf.parse(strDatePreFormatee);
            if (DateUtils.getChamp(date, Calendar.YEAR) > MAX_YEAR) {
                // Manque les dépendances vers siad-contexte et siad-web
                // on ne pourra pas fabriquer ici de message à partir
                // d'un fichier de properties
                final FacesMessage message = new FacesMessage();
                message.setSeverity(FacesMessage.SEVERITY_ERROR);
                message.setSummary("Erreur de conversion");
                message.setSummary("La date '" + strDate +
                                   "' est incorrecte, l'année dépasse " + MAX_YEAR);
                throw new ConverterException(message);
            }
            if (DateUtils.getChamp(date, Calendar.YEAR) < MIN_YEAR) {
                final FacesMessage message = new FacesMessage();
                message.setSeverity(FacesMessage.SEVERITY_ERROR);
                message.setSummary("Erreur de conversion");
                message.setSummary("La date '" + org.crlr.utils.StringUtils.stripSpaces(strDate) +
                                   "' est incorrecte, l'année est inférieure à " +
                                   MIN_YEAR);
                throw new ConverterException(message);
            }
        } catch (final ParseException e) {
            final FacesMessage message = new FacesMessage();
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
            message.setSummary("Erreur de conversion");
            message.setSummary("La date " + strDate + " a un format incorrect");
            throw new ConverterException(message);
        }
        return date;
    }

    /**
     * Préformatage de la date tel qu'il est fait js.
     *
     * @param strDate the str date
     *
     * @return the string
     */
    private String preFormatDate(String strDate) {
        log.debug("Préformatage de la date");
        if (!strDate.matches("[0-9]{6}") && !strDate.matches("[0-9]{8}") &&
                !strDate.matches("[0-9]{2}/[0-9]{2}/[0-9]{2}") &&
                !strDate.matches("[0-9]{2}/[0-9]{2}/[0-9]{4}")) {
            return strDate;
        }

        log.debug("Un préformatage est à prévoir");

        // Suppression des slashes possibles
        String strDatePreFormatee = "";
        for (int j = 0; j < strDate.length(); j++) {
            final char car = strDate.charAt(j);
            if (car != '/') {
                strDatePreFormatee += car;
            }
        }

        final String jour = strDatePreFormatee.substring(0, 2);
        final String mois = strDatePreFormatee.substring(2, 4);
        String annee = strDatePreFormatee.substring(4);

        // passage de l'annee en 4 caracteres
        if (annee.length() == 2) {
            if (annee.charAt(0) >= '3') {
                annee = "19" + annee;
            } else {
                annee = "20" + annee;
            }
        }

        strDatePreFormatee = jour + "/" + mois + "/" + annee;

        return strDatePreFormatee;
    }

    /**
     * {@inheritDoc}
     */
    public String getAsString(FacesContext contexte, UIComponent component, Object obj)
                       throws ConverterException {
        if (obj == null) {
            // throw new ConverterException("Date à convertir vide");
            return "";
        }
        final DateFormat sdf = createDateFormat(pattern);
        return sdf.format((Date) obj);
    }

    /**
     * Retourne pattern.
     *
     * @return the pattern
     */
    public String getPattern() {
        return pattern;
    }

    /**
     * Affecte pattern.
     *
     * @param pattern the pattern
     */
    public void setPattern(String pattern) {
        this.pattern = ObjectUtils.defaultIfNull(StringUtils.trimToNull(pattern),
                                                 DEFAULT_PATTERN);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "DateConverter[pattern=" + pattern + "]";
    }

    /**
     * Creates the date format.
     *
     * @param pattern the pattern
     *
     * @return the date format
     */
    private DateFormat createDateFormat(String pattern) {
        final SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        sdf.setLenient(false);
        return sdf;
    }
}
