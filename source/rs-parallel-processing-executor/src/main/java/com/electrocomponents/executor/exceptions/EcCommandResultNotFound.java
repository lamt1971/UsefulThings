package com.electrocomponents.executor.exceptions;

/**
 *
 * <pre>
 * ************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author  : Bhavesh Kavia
 * Created : 21 Jan 2011 at 16:30:33
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
 * Exception used to indicate that no result was found.
 * @author Bhavesh Kavia
 */
public class EcCommandResultNotFound extends EcCommandException {

    /** The Serial version Id. */
    private static final long serialVersionUID = -6142360911000634760L;

    /**
     * The Exception Constructor.
     * @param message - The error message
     */
    public EcCommandResultNotFound(final String message) {
        super(message);
    }
}
