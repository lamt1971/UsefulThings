package com.electrocomponents.model.domain.message;

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
 * The ECMapMessage is formalised for Map Message as key and value pairs.The key should be String type and value must be primitive type. The
 * ECMapMessage is declared as Final as this class behaviour shouldn't be changed by any other class.
 * @author Raja Govindharaj
 */
public final class ECMapMessage extends ECBaseMessage {

    /** Serial uid */
	private static final long serialVersionUID = 6081343434789561162L;

	/**
     * Number Map (int, long, byte, short, float and double).
     */
    private Map<String, Object> mapMessage = new ConcurrentHashMap<String, Object>();

    /**
     * The setter method to set number-value map message.
     * @param key as map key.
     * @param value is String.
     */
    public void setNumberValueMapMessage(final String key, final Number value) {
        validateDuplicateKeysAndAdd(key, value);
    }

    /**
     * The The getter method to get number-value map message.
     * @param key in Map key.
     * @return Number is Map value.
     */
    public Number getNumberValueMapMessage(final String key) {
        Object object = getValueFromMap(key);
        Number number = null;
        if (object != null && Number.class.getName().equals(object.getClass().getName())) {
            number = (Number) object;
        }
        return number;
    }

    /**
     * The setter method to set char-value map message.
     * @param key as map key.
     * @param value is String.
     */
    public void setCharValueMapMessage(final String key, final Character value) {
        validateDuplicateKeysAndAdd(key, value);
    }

    /**
     * The The getter method to get char-value map message.
     * @param key in Map key.
     * @return Character is Map value.
     */
    public Character getCharValueMapMessage(final String key) {
        Object object = getValueFromMap(key);
        Character charA = null;
        if (object != null && Character.class.getName().equals(object.getClass().getName())) {
            charA = (Character) object;
        }
        return charA;
    }

    /**
     * /** The setter method to set boolean-value map message.
     * @param key as map key.
     * @param value is String.
     */
    public void setBooleanValueMapMessage(final String key, final Boolean value) {
        validateDuplicateKeysAndAdd(key, value);
    }

    /**
     * The The getter method to get boolean-value map message.
     * @param key in Map key.
     * @return Boolean is Map value.
     */
    public Boolean getBooleanValueMapMessage(final String key) {
        Object object = getValueFromMap(key);
        Boolean boolvalue = null;
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
    public void setStringValueMapMessage(final String key, final String value) {
        validateDuplicateKeysAndAdd(key, value);
    }

    /**
     * The The getter method to get string-value map message.
     * @param key in Map key.
     * @return String is Map value.
     */
    public String getStringValueMapMessage(final String key) {
        Object object = getValueFromMap(key);
        String strVal = null;
        if (object != null && String.class.getName().equals(object.getClass().getName())) {
            strVal = (String) object;
        }
        return strVal;
    }

    /**
     * The setter method to set byte-array-value map message.
     * @param key as map key.
     * @param value is String.
     */
    public void setByteArrayValueMapMessage(final String key, final Byte[] value) {
        validateDuplicateKeysAndAdd(key, value);
    }

    /**
     * The The getter method to get byte array-value map message.
     * @param key in Map key.
     * @return Character is Map value.
     */
    public Byte[] getByteArrayValueMapMessage(final String key) {
        Object object = getValueFromMap(key);
        return (Byte[]) object;
    }

    /**
     * The getter method to get Map.
     * @return Map
     */
    public Map<String, Object> getMapMessage() {
        return Collections.unmodifiableMap(this.mapMessage);
    }

    /**
     * The validateDuplicateKeysAndAdd is validate the key is duplicated.
     * @param key as Key of Map.
     * @param value is Object.
     */
    private void validateDuplicateKeysAndAdd(final String key, final Object value) {
        if (key != null) {
            if (this.mapMessage.containsKey(key)) {
                throw new MessageTransactionSafeServiceException("Map Message keys is already exists : " + key);
            } else {
                this.mapMessage.put(key, value);
            }
        }
    }

    /**
     * The getValueFromMap method return value from Object.
     * @param key is Map key
     * @return Object is value
     */
    private Object getValueFromMap(final String key) {
        Object object = null;
        if (key != null) {
            object = this.mapMessage.get(key);
        }
        return object;
    }
}
