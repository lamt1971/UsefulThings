package com.electrocomponents.model.data.mapping.castor.fieldhandler;

import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.mapping.ConfigurableFieldHandler;
import org.exolab.castor.mapping.GeneralizedFieldHandler;
import org.exolab.castor.mapping.ValidityException;

import com.electrocomponents.model.domain.DateTime;

/**
 * <pre>
 * ************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author  : Ganesh Raut
 * Created : 20 Jun 2008 at 15:22:51
 *
 * ************************************************************************************************
 * Change History
 * ************************************************************************************************
 * Ref      * Who      * Date       * Description
 * ************************************************************************************************
 *          * C0950237 *30 Apr 2009 * handles the null/empty value
 * ************************************************************************************************
 * </pre>
 */

/**
 * A handler to convert String to DateTime object for Castor.
 */
public class DateTimeHandler extends GeneralizedFieldHandler implements ConfigurableFieldHandler {

    /** Commons logging logger. */
    private static final Log LOG = LogFactory.getLog(DateTimeHandler.class);

    /** The date format specifyed in mapping file. */
    private String dateFormat;

    /**
     * @param value the DateTime object.
     * @return the date time in string format.
     */
    @Override
    public Object convertUponGet(final Object value) {
        DateTime dateTime = (DateTime) value;
        return (dateTime != null ? dateTime.getDateTimeString(dateFormat) : "null");
    }

    /**
     * @param value the date time value in string format.
     * @return the DateTime object.
     */
    @Override
    public Object convertUponSet(final Object value) {
        DateTime dateTime = null;
        if (value == null || "".equals(value) || "null".equals(value)) {
            return null;
        } else {
            try {
                String string = (String) value;
                string = string.replaceAll("T", "");
                dateTime = new DateTime(string, dateFormat);
            } catch (Exception e) {
                LOG.error("Exception in convertUponSet.", e);
            }
            return dateTime;
        }
    }

    /**
     * @return the DateTime class.
     */
    @Override
    public Class<DateTime> getFieldType() {
        return DateTime.class;
    }

    /**
     * Sets the parameters passed in through the mapping file.
     * @param properties the properties to set.
     * @throws ValidityException ValidityException
     */
    @Override
    public void setConfiguration(final Properties properties) throws ValidityException {
        this.dateFormat = properties.getProperty("dateformat");
    }
}
