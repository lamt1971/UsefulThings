package com.electrocomponents.model.data.mapping.castor.fieldhandler;

import org.exolab.castor.mapping.GeneralizedFieldHandler;

import com.electrocomponents.model.domain.Locale;

/**
 * <pre>
 * ************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author  : Ian Collington
 * Created : 29 Sep 2008 at 17:30
 *
 * ************************************************************************************************
 * Change History
 * ************************************************************************************************
 * Ref      * Who      * Date       * Description
 * ************************************************************************************************
 *          *          *            *
 * ************************************************************************************************
 * </pre>
 */

/**
 * A handler to convert String to Locale object for Castor.
 * @author Ian Collington
 */
public class LocaleHandler extends GeneralizedFieldHandler {
    /**
     * @param arg0 The object to convert
     * @return The converted object
     */
    @Override
    public Object convertUponGet(final Object arg0) {
        Locale locale = (Locale) arg0;
        if (locale != null) {
            return locale.toString();
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object convertUponSet(final Object arg0) {
        String param = (String) arg0;
        if (param != null) {
            return new Locale(param);
        }
        return null;
    }

    /**
     * @return the Locale class.
     */
    @Override
    public Class getFieldType() {
        return Locale.class;
    }

}
