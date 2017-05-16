package com.electrocomponents.continuouspublishing.exception;

import javax.ejb.ApplicationException;

/*
 * ************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author  : Sanjay Semwal
 * Created : 3rd Oct 2007 at 11:56:00
 *
 * ************************************************************************************************
 * Change History
 * ************************************************************************************************
 * Ref      * Who      * Date       * Description
 * ************************************************************************************************
 *          *          *            *
 * ************************************************************************************************
 */

/**
 * AuditLogException is an application exception class.
 * @author sanjay semwal
 */
@ApplicationException(rollback = false)
public class AuditLogException extends Exception {
    /**
     * serialVersionUID for serialization.
     */
    private static final long serialVersionUID = 1L;

    /**
     * @param cause The cause of the exception.
     */
    public AuditLogException(final Exception cause) {
        super(cause);
    }
}
