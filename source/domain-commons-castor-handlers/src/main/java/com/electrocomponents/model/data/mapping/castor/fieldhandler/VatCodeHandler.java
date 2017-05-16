/**
 * com.electrocomponents.model.data.mapping.castor.fieldhandler.VatCodeHandler : VatCodeHandler.java
 * 11 Sep 2008 09:26:35
 * c0950078
 **/
package com.electrocomponents.model.data.mapping.castor.fieldhandler;

import org.exolab.castor.mapping.GeneralizedFieldHandler;

import com.electrocomponents.model.domain.VatCode;

/**
 * <pre>
 * ********************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author  : Yogesh Patil
 * Created : 11 Sep 2008 at 09:26:35
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
public class VatCodeHandler extends GeneralizedFieldHandler {

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
        VatCode vatCode = VatCode.valueOf((String) arg0);
        return vatCode;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Class getFieldType() {
        return VatCode.class;
    }

}
