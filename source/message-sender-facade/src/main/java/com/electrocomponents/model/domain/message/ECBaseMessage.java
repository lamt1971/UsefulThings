package com.electrocomponents.model.domain.message;

import java.io.Serializable;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import com.electrocomponents.service.exception.MessageTransactionSafeServiceException;

/**
 *
 * <pre>
 * ************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author  : Raja Govindharaj
 * Created : 18 Oct 2010 at 12:58:23
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
 * The BaseECMessage is base class for ECMessage.
 * @author Raja Govindharaj
 */
public abstract class ECBaseMessage implements ECMessage, Serializable {

    /** Serial uid */
	private static final long serialVersionUID = 1430347268184311220L;

	/**
     * The Connection Factory JNDI Name.
     */
    private String connectionFactoryJNDIName;

    /**
     * The Destination JNDI Name.
     */
    private String destinationJNDIName;

    /**
     * Number Header Property (int, long, byte, short, float and double).
     */
    private Map<String, Object> headerMap = new ConcurrentHashMap<String, Object>();

    /**
     * Correlation ID.
     */
    private String correlationID;

    /**
     * Acknowledge Mode.
     */
    private Integer acknowledgeMode = AUTO_ACKNOWLEDGE;

    /**
     * The setter method to set connection factory JNDI name.
     * @param connectionFactoryJNDIName as Connection factory JNDI name.
     */
    public void setConnectionFactoryJNDIName(final String connectionFactoryJNDIName) {
        this.connectionFactoryJNDIName = connectionFactoryJNDIName;
    }

    /**
     * The getter method to get connection factory jndi name.
     * @return String Connection factory JNDI name.
     */
    public String getConnectionFactoryJNDIName() {
        return connectionFactoryJNDIName;
    }

    /**
     * The setter method to set destination JNDI name.
     * @param destinationJNDIName as destination JNDI name.
     */
    public void setDestinationJNDIName(final String destinationJNDIName) {
        this.destinationJNDIName = destinationJNDIName;
    }

    /**
     * The getter method to get destination JNDI name.
     * @return String Destination JNDI name..
     */
    public String getDestinationJNDIName() {
        return destinationJNDIName;
    }

    /**
     * The setter method to set number-value map message.
     * @param key as map key.
     * @param value is Number.
     */
    public void setNumberValueHeaderProp(final String key, final Number value) {
        validateDuplicateKeysAndAdd(key, value);
    }

    /**
     * The The getter method to get number-value map message.
     * @param key in Map key.
     * @return Number is Map value.
     */
    public Number getNumberValueHeaderProp(final String key) {
        Object object = getValueFromMap(key);
        Number number = null;
        if (object != null && Number.class.getName().equals(object.getClass().getName())) {
            number = (Number) object;
        }
        return number;
    }

    /**
     * The setter method to set boolean-value map message.
     * @param key as map key.
     * @param value is Boolean.
     */
    public void setBooleanValueHeaderProp(final String key, final Boolean value) {
        validateDuplicateKeysAndAdd(key, value);
    }

    /**
     * The The getter method to get boolean-value map message.
     * @param key in Map key.
     * @return Boolean is Map value.
     */
    public Boolean getBooleanValueHeaderProp(final String key) {
        Object object = getValueFromMap(key);
        Boolean boolvalue = Boolean.FALSE;
        if (object != null && Boolean.class.getName().equals(object.getClass().getName())) {
            boolvalue = (Boolean) object;
        }
        return boolvalue;
    }

    /**
     * The setter method to set String-value map message.
     * @param key as map key.
     * @param value is String.
     */
    public void setStringValueHeaderProp(final String key, final String value) {
        validateDuplicateKeysAndAdd(key, value);
    }

    /**
     * The The getter method to get string-value map message.
     * @param key in Map key.
     * @return String is Map value.
     */
    public String getStringValueHeaderProp(final String key) {
        Object object = getValueFromMap(key);
        String strVal = null;
        if (object != null && String.class.getName().equals(object.getClass().getName())) {
            strVal = (String) object;
        }
        return strVal;
    }

    /**
     * The setter method to set correlation ID.
     * @param correlationID as correlation ID for the message.
     */
    public void setCorrelationID(final String correlationID) {
        this.correlationID = correlationID;
    }

    /**
     * The getter method to get correlation ID.
     * @return String as correlation ID for the message.
     */
    public String getCorrelationID() {
        return correlationID;
    }

    /**
     * The setter method to set Acknowledge Mode.
     * @param acknowledgeMode as Acknowledge Mode.
     */
    public void setMessageAcknowledgeMode(final Integer acknowledgeMode) {
        this.acknowledgeMode = acknowledgeMode;
    }

    /**
     * The getter method to get Acknowledge mode.
     * @return Integer as Auto Acknowledge Mode.
     */
    public Integer getMessageAcknowledgeMode() {
        return acknowledgeMode;
    }

    /**
     * The getter method to get Map.
     * @return Map
     */
    public Map<String, Object> getHeaderMap() {
        return Collections.unmodifiableMap(this.headerMap);
    }

    /**
     * The getValueFromMap method return value from Object.
     * @param key is Map key
     * @return Object is value
     */
    private Object getValueFromMap(final String key) {
        Object object = null;
        if (key != null) {
            object = this.headerMap.get(key);
        }
        return object;
    }

    /**
     * The validateDuplicateKeysAndAdd is validate the key is duplicated.
     * @param key as Key of Map.
     * @param value is Object.
     */
    private void validateDuplicateKeysAndAdd(final String key, final Object value) {
        if (key != null) {
            if (this.headerMap.containsKey(key)) {
                throw new MessageTransactionSafeServiceException("Header Map keys is already exists : " + key);
            } else {
                this.headerMap.put(key, value);
            }
        }
    }
}
