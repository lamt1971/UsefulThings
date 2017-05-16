package com.electrocomponents.service.exception;

import javax.ejb.ApplicationException;

/**
 * <pre>
 * ************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author  : Vijay Swarnkar
 * Created : 20 Jan 2011 at 14:32:01
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
 * Cache path not found exception to be thrown when no results found at the cache path / cache path does not exists.
 * @author Vijay Swarnkar
 */
@ApplicationException(rollback = false)
public class CachePathNotFoundException extends RuntimeException {

    /** Serial version ID. */
    private static final long serialVersionUID = -1445229692888970746L;

    /**
     * @param originalException the original exception
     */
    public CachePathNotFoundException(final Exception originalException) {
        super(originalException);
    }

    /**
     * @param supportMessage the original supportMessage
     */
    public CachePathNotFoundException(final String supportMessage) {
        super(supportMessage);
    }

}
