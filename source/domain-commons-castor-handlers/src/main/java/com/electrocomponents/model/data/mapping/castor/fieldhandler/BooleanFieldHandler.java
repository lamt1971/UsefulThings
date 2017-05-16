/**
 * com.electrocomponents.model.data.mapping.castor.fieldhandler.BooleanFieldHandler : BooleanFieldHandler.java
 * 3 Sep 2008 16:32:51
 * c0950078
 **/
package com.electrocomponents.model.data.mapping.castor.fieldhandler;

import org.exolab.castor.mapping.GeneralizedFieldHandler;

/**
 * <pre>
 * ********************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author  : Yogesh Patil
 * Created : 3 Sep 2008 at 16:32:51
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
 * *          Mark Glover           27/03/2009   Amended convertUponSet method to deal with Y/N fields
 * *                                             as well as true/false
 * ********************************************************************************************************
 * </pre>
 **/
public class BooleanFieldHandler extends GeneralizedFieldHandler {

    /**
     * {@inheritDoc}
     */
    @Override
    public Object convertUponGet(final Object arg0) {
        return arg0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object convertUponSet(final Object arg0) {
        if (arg0 == null) {
            return null;
        }
        String value = (String) arg0;
        if (value.trim().length() == 0) {
            return null;
        }
        /** to map Y/N values to boolean fields */
        if (value.equalsIgnoreCase("Y")) {
            value = "true";
        }
        return Boolean.valueOf(value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Class getFieldType() {
        return Boolean.class;
    }

}
