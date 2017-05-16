package com.electrocomponents.executor.exceptions;

/**
 * <pre>
 * ************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author  : Berkan Kurtoglu
 * Created : 118 Jan 2011 at 13:18:36
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
 * An exception class to be thrown when command specification parameters are not valid.
 * @author Berkan Kurtoglu
 */

public class EcCommandNotValidException extends EcCommandException {

    /** serialVersionUID. */
    private static final long serialVersionUID = -6686827012492987422L;

    /**
     * @param errorMessage the errorMessage
     */
    public EcCommandNotValidException(final String errorMessage) {
        super(errorMessage);
    }

}
