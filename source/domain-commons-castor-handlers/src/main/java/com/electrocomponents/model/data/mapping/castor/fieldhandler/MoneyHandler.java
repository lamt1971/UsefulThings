package com.electrocomponents.model.data.mapping.castor.fieldhandler;

import org.exolab.castor.mapping.GeneralizedFieldHandler;

import com.electrocomponents.model.domain.Money;

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
 * A handler to convert the amount in string format to Money object for Castor.
 * @author Ganesh Raut
 */
public class MoneyHandler extends GeneralizedFieldHandler {

    /**
     * @param value the money object.
     * @return the money in string format.
     */
    @Override
    public Object convertUponGet(final Object value) {
        if (value == null) {
            return null;
        }
        Money money = (Money) value;
        return money.toString();
    }

    /**
     * @param value amount in string format.
     * @return the Money object constructed from the string amount.
     */
    @Override
    public Object convertUponSet(final Object value) {
        Money money = new Money((String) value);
        return money;
    }

    /**
     * @return the Money class.
     */
    @Override
    public Class getFieldType() {
        return Money.class;
    }
}
