package com.electrocomponents.model.data.mapping.castor.fieldhandler;

import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.mapping.ConfigurableFieldHandler;
import org.exolab.castor.mapping.GeneralizedFieldHandler;
import org.joda.time.DateTimeZone;

import com.electrocomponents.model.data.mapping.castor.type.DateTimeSimpleType;
import com.electrocomponents.model.domain.DateTime;

/**
 * <pre>
 * ************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author  : Yogesh Patil
 * Created : 17 Sep 2008 at 15:20:20
 *
 * ************************************************************************************************
 * Change History
 * ************************************************************************************************
 * Ref      * Who      * Date       * Description
 * ************************************************************************************************
 * *          *            *
 * ************************************************************************************************
 * </pre>
 */

/**
 * The field handler for the PmDateTime field.
 * @author Yogesh Patil.
 */
public class PmDateTimeHandler extends GeneralizedFieldHandler implements ConfigurableFieldHandler {

    /** Commons logging logger. */
    private static final Log LOG = LogFactory.getLog(PmDateTimeHandler.class);

    /**
     * {@inheritDoc}
     */
    @Override
    public Object convertUponGet(final Object arg0) {
        if (arg0 == null) {
            return null;
        }
        DateTime dateTime = (DateTime) arg0;
        DateTimeSimpleType simpleType = new DateTimeSimpleType();
        simpleType.setDateFormat(dateTime.getDateFormat());
        simpleType.setDateString(dateTime.getDateTimeString());
        simpleType.setTimezoneId(dateTime.getTimeZone().getID());
        Locale locale = dateTime.getLocale();
        if (locale != null) {
            if (locale.getCountry() != null) {
                simpleType.setLocaleCountry(dateTime.getLocale().getCountry());
            }
            if (locale.getLanguage() != null) {
                simpleType.setLocaleLanguage(locale.getLanguage());
            }
            if (locale.getVariant() != null) {
                simpleType.setLocaleVariant(locale.getVariant());
            }
        }
        return simpleType;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object convertUponSet(final Object arg0) {
        if (arg0 == null) {
            return null;
        }

        DateTime dateTime = null;
        try {
            DateTimeSimpleType str = (DateTimeSimpleType) arg0;
            dateTime = new DateTime(str.getDateString(), str.getDateFormat());
            if (str.getLocaleLanguage() != null && str.getLocaleCountry() != null && str.getLocaleVariant() != null) {
                dateTime.setJavaLocale(new Locale(str.getLocaleLanguage(), str.getLocaleCountry(), str.getLocaleVariant()));
            }
            dateTime.setTimeZone(DateTimeZone.forID(str.getTimezoneId()));
        } catch (Exception ex) {
            LOG.error("Exception in convertUponSet.", ex);
        }
        return dateTime;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Class getFieldType() {
        return DateTime.class;
    }

}
