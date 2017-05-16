package com.electrocomponents.executor.exceptions;

/**
 *
 * <pre>
 * ************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author  : Bhavesh Kavia
 * Created : 21 Jan 2011 at 16:25:28
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
 * Exception thrown when the Command creation exceeds a maximum value.
 * @author Bhavesh Kavia
 */
public class EcCommandLimitException extends EcCommandException {

    /** The Serial version Id. */
    private static final long serialVersionUID = 8355794057921498074L;

    /**
     * The Exception Constructor.
     * @param message - The error message
     */
    public EcCommandLimitException(final String message) {
        super(message);
    }
}
