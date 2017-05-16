package com.electrocomponents.service.message.producer.interfaces;

import com.electrocomponents.model.domain.message.ECMessage;

/**
 * All imports go here
 */

/**
 * <pre>
 * ************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author  : Raja Govindharaj
 * Created : 10 Feb 2010 at 11:08:00
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
 * The MessageProducer is an interface to Message Producer Service.
 * @author Raja Govindharaj
 */
public interface MessageProducerService {

    public static final String SERVICE_NAME = "MessageProducerService";
    
    String EJB_MODULE_NAME = "message-sender-facade";

    /**
     *  @Deprecated, Deprecated, use : sendMessage(final ECMessage message, final boolean transacted) instead
     */
    @Deprecated
    Boolean sendMessage(final ECMessage message);

    /**
     * The sendMessage method will accept the Message object and will identify message attributes and construct a JMS Message to publish
     * into producer.
     * @param message as parameter of ECMessage.
     * @param transacted If true then message will be sent in a new or existing transaction
     * @return Boolean is meant to notify whether message has been published successfully or not. if returns true, message successfully
     *         sent. if false, message sent failed.
     */
    Boolean sendMessage(final ECMessage message, final boolean transacted);
}
