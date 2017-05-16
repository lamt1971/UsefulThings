package com.electrocomponents.model.domain.message;

import java.util.Map;

import javax.jms.QueueSession;

/**
 *
 * <pre>
 * ************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author  : Raja Govindharaj
 * Created : 18 Oct 2010 at 09:20:59
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
 * The ECMessage is a contract to MessageDTO.
 * @author Raja Govindharaj
 */
public interface ECMessage {

    /*
     * Re-used QueueSession's acknowledge properties here as because client of ECMessage is not allowed to use any of JMS classes.
     */

    /**
     * AUTO_ACKNOWLEDGE.
     */
    int AUTO_ACKNOWLEDGE = QueueSession.AUTO_ACKNOWLEDGE;

    /**
     * CLIENT_ACKNOWLEDGE.
     */
    int CLIENT_ACKNOWLEDGE = QueueSession.CLIENT_ACKNOWLEDGE;

    /**
     * DUPS_OK_ACKNOWLEDGE.
     */
    int DUPS_OK_ACKNOWLEDGE = QueueSession.DUPS_OK_ACKNOWLEDGE;

    /**
     * SESSION_TRANSACTED.
     */
    int SESSION_TRANSACTED = QueueSession.SESSION_TRANSACTED;

    /**
     * The setter method to set connection factory JNDI name.
     * @param connectionFactoryJNDIName as Connection factory JNDI name.
     */
    void setConnectionFactoryJNDIName(final String connectionFactoryJNDIName);

    /**
     * The getter method to get connection factory jndi name.
     * @return String Connection factory JNDI name.
     */
    String getConnectionFactoryJNDIName();

    /**
     * The setter method to set destination JNDI name.
     * @param destinationJNDIName as destination JNDI name.
     */
    void setDestinationJNDIName(final String destinationJNDIName);

    /**
     * The getter method to get destination JNDI name.
     * @return String Destination JNDI name..
     */
    String getDestinationJNDIName();

    /**
     * The setter method to set number-value map message.
     * @param key as map key.
     * @param value is Number.
     */
    void setNumberValueHeaderProp(final String key, final Number value);

    /**
     * The The getter method to get number-value map message.
     * @param key in Map key.
     * @return Number is Map value.
     */
    Number getNumberValueHeaderProp(final String key);

    /**
     * The setter method to set boolean-value map message.
     * @param key as map key.
     * @param value is Boolean.
     */
    void setBooleanValueHeaderProp(final String key, final Boolean value);

    /**
     * The The getter method to get Boolean-value map message.
     * @param key in Map key.
     * @return Boolean is Map value.
     */
    Boolean getBooleanValueHeaderProp(final String key);

    /**
     * The setter method to set String-value map message.
     * @param key as map key.
     * @param value is String.
     */
    void setStringValueHeaderProp(final String key, final String value);

    /**
     * The The getter method to get string-value map message.
     * @param key in Map key.
     * @return String is Map value.
     */
    String getStringValueHeaderProp(final String key);

    /**
     * The getter method to get Map.
     * @return Map
     */
    Map<String, Object> getHeaderMap();

    /**
     * The setter method to set correlation ID.
     * @param correlationID as correlation ID for the message.
     */
    void setCorrelationID(final String correlationID);

    /**
     * The getter method to get correlation ID.
     * @return String as correlation ID for the message.
     */
    String getCorrelationID();

    /**
     * The setter method to set Acknowledge Mode.
     * @param acknowledgeMode as Acknowledge Mode.
     */
    void setMessageAcknowledgeMode(final Integer acknowledgeMode);

    /**
     * The getter method to get Acknowledge mode.
     * @return Integer as Auto Acknowledge Mode.
     */
    Integer getMessageAcknowledgeMode();

}
