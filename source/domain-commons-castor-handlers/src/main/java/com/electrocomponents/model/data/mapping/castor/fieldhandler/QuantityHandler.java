package com.electrocomponents.model.data.mapping.castor.fieldhandler;

import org.exolab.castor.mapping.GeneralizedFieldHandler;

import com.electrocomponents.model.domain.Quantity;

/**
 * <pre>
 * ************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author  : Ganesh Raut
 * Created : 20 Jun 2008 at 14:51:00
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
 * A handler to convert Integer to Quantity object for Castor.
 * @author Ganesh Raut
 */
public class QuantityHandler extends GeneralizedFieldHandler {

    /**
     * @param value the value.
     * @return the integer value of the quantity.
     */
    @Override
    public Object convertUponGet(final Object value) {
        if (value == null) {
            return null;
        }
        Quantity quantity = (Quantity) value;
        return quantity.getQuantity();
    }

    /**
     * @param value the value.
     * @return the Quantity object.
     */
    @Override
    public Object convertUponSet(final Object value) {
        Quantity quantity = new Quantity((Integer) value);
        return quantity;
    }

    /**
     * @return the field type.
     */
    @Override
    public Class getFieldType() {
        return Quantity.class;
    }
}
