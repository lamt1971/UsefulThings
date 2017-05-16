package com.electrocomponents.model.domain.message;

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
 * The ECTextMessage is formalised for Text (String) Message. The ECTextMessage is declared as Final as this class behaviour shouldn't be
 * changed by any other class.
 * @author Raja Govindharaj
 */
public final class ECTextMessage extends ECBaseMessage {

    /** Serial uid */
	private static final long serialVersionUID = -6497536671445883910L;

	/**
     * Text Message.
     */
    private String textMessage;

    /**
     * The setter method to set string message.
     * @param textMessage as String message.
     */
    public void setTextMessage(final String textMessage) {
        this.textMessage = textMessage;
    }

    /**
     * The getter method to get a string message.
     * @return String as text string message
     */
    public String getTextMessage() {
        return textMessage;
    }
}
