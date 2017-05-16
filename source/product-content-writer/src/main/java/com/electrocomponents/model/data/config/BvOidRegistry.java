package com.electrocomponents.model.data.config;

/**
 * <pre>
 * ************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author  : Vijay Swarnkar
 * Created : 24 Sep 2010 at 12:51:40
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
 * An interface for BvOidRegistryEntity.
 * @author Vijay Swarnkar
 */
public interface BvOidRegistry {

    /** @return the oId */
    long getOId();

    /** @param id the oId to set */
    void setOId(final long id);

    /** @return the systemId */
    long getSystemId();

    /** @param systemId the systemId to set */
    void setSystemId(final long systemId);

}
