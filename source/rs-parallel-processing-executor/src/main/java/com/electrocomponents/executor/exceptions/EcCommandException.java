package com.electrocomponents.executor.exceptions;

import com.electrocomponents.model.domain.ApplicationSource;
import com.electrocomponents.service.exception.TransactionSafeServiceException;

/**
 *
 * <pre>
 * ************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author  : Gillian Boyd
 * Created : 7 Feb 2011 at 17:31:08
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
 * Exception thrown if error occurs when an exception occurs within the executor framework.
 * @author Gillian Boyd
 */
public class EcCommandException extends TransactionSafeServiceException {

    /** The Serial version Id. */
    private static final long serialVersionUID = -1814852513395932404L;

    /**
     * @param originalException is original Exception
     * @param applicationSource is source.
     */
    public EcCommandException(final Exception originalException, final ApplicationSource applicationSource) {
        super(originalException, applicationSource);
    }

    /**
     * @param message is source.
     */
    public EcCommandException(final String message) {
        super(message, ApplicationSource.RS_WEB_SITE);
    }

}
