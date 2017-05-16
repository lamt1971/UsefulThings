package com.electrocomponents.model.domain.message;

import java.io.Serializable;

/**
 *
 * <pre>
 * ************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author  : Raja Govindharaj
 * Created : 18 Oct 2010 at 09:22:01
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
 * The ECObjectMessage is formalised for Object Message that should be Serializable. The ECObjectMessage is declared as Final as this class
 * behaviour shouldn't be changed by any other class.
 * @author Raja Govindharaj
 */
public final class ECObjectMessage extends ECBaseMessage {

    /** Serial uid */
	private static final long serialVersionUID = 3670223657058666731L;

	/**
     * Object Message.
     */
    private Serializable object;

    /**
     * The setter method to set Serializable object message.
     * @param object as Serializable Object message.
     */
    public void setObjectMessage(final Serializable object) {
        this.object = object;
    }

    /**
     * The getter method to get a Serializable object message.
     * @return String as Serializable object message
     */
    public Serializable getObjectMessage() {
        return object;
    }
}
