/**
 * com.rs.DateFieldHandler : DateHandler.java
 * 20 Jun 2008 11:28:14
 * c0950078
 **/
package com.electrocomponents.model.data.mapping.castor.fieldhandler;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.mapping.ConfigurableFieldHandler;
import org.exolab.castor.mapping.GeneralizedFieldHandler;
import org.exolab.castor.mapping.ValidityException;

/**
 * <pre>
 * ********************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author  : Yogesh Patil
 * Created : 20 Jun 2008 at 11:28:14
 *
 * ********************************************************************************************************
 *
 *
 * ********************************************************************************************************
 * @author Yogesh Patil
 * ********************************************************************************************************
 * * Change History
 * ********************************************************************************************************
 * * Ref      * Who                 * Date       * Description
 * ********************************************************************************************************
 * </pre>
 **/
public class DateHandler extends GeneralizedFieldHandler implements ConfigurableFieldHandler {

    /** The logger for this class. */
    private static final Log LOG = LogFactory.getLog(DateHandler.class);

    /** The date format in which the string will be present. */
    private String format;

    /**
     * {@inheritDoc}
     */
    @Override
    public Object convertUponGet(final Object obj) {
        return obj.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object convertUponSet(final Object obj) {
        String str = (String) obj;
        if (str.indexOf('T') >= 0) {
            str = str.replaceAll("T", "");
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        Date date = null;
        try {
            date = dateFormat.parse(str);
        } catch (ParseException pex) {
            // TODO There is nothing signicficant to do here for now..
            if (LOG.isErrorEnabled()) {
                LOG.error("Parse Exception in DateHandler ConvertUponSet method.", pex);
            }
        }
        return date;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Class getFieldType() {
        return Date.class;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setConfiguration(final Properties properties) throws ValidityException {
        this.format = properties.getProperty("dateformat");
    }
}
