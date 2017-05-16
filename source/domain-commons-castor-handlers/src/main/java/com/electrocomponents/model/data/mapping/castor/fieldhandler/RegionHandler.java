package com.electrocomponents.model.data.mapping.castor.fieldhandler;

import org.exolab.castor.mapping.GeneralizedFieldHandler;

import com.electrocomponents.model.domain.Region;

/**
 * <pre>
 * ************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author  : Neeraj Singh
 * Created : 01 Apr 2009 at 12:22:37
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
 * TODO add class-level javadoc.
 * @author Neeraj Singh
 */
public class RegionHandler extends GeneralizedFieldHandler {

    /**
     * {@inheritDoc}
     */
    @Override
    public Object convertUponGet(final Object arg0) {
        Region region = (Region) arg0;
        if (region != null) {
            return region.toString();
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
            return Region.valueOf(param);
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Class getFieldType() {
        return Region.class;
    }

}
