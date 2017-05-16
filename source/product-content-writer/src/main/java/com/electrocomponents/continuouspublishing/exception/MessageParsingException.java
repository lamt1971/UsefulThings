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
 *          *Yogesh Patil *20-May-2008  * Modified class to support custom exception message and
 *                                      * passing the root exception.
 *          *Yogesh Patil *17-Jun-2008  * Fixed checkstyle errors.
 * ************************************************************************************************
 */

/**
 * MessageParsingException is an application exception class.
 * @author sanjay semwal
 */
@ApplicationException(rollback = false)
public class MessageParsingException extends Exception {

   /**
     * serialVersionUID for serialization.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The default constructor.
     */
    public MessageParsingException() {
        super();
    }

    /**
     * The constructor which initializes with exception message.
     * @param msg the message to set.
     */
    public MessageParsingException(final String msg) {
        super(msg);
    }

    /**
     * The constructor which initializes with exception message and the root cause exception.
     * @param msg the message to set.
     * @param t the root exception.
     */
    public MessageParsingException(final String msg, final Throwable t) {
        super(msg, t);
    }

}
