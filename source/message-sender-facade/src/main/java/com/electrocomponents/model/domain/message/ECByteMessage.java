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
 * The ECByteMessage is formalised for Byte Message. The ECByteMessage is declared as Final as this class behaviour shouldn't be changed by
 * any other class.
 * @author Raja Govindharaj
 */
public final class ECByteMessage extends ECBaseMessage {

    /** Serial uid */
	private static final long serialVersionUID = -3681038558214367371L;

	/**
     * Byte Message.
     */
    private byte[] bytesMessage;

    /**
     * The setter method to set Byte[] message.
     * @param bytesMessage as Byte[] message.
     */
    public void setBytesMessage(final byte[] bytesMessage) {
        this.bytesMessage = bytesMessage;
    }

    /**
     * The getter method to get a Byte[] message.
     * @return String as Byte[]message
     */
    public byte[] getBytesMessage() {
        return bytesMessage;
    }
}
