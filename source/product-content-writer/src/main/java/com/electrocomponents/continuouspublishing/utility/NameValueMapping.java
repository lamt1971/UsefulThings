package com.electrocomponents.continuouspublishing.utility;

/*
 * ************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author  : Sanjay Semwal
 * Created : 3rd Sep 2007 at 16:20:00
 *
 * ************************************************************************************************
 * Change History
 * ************************************************************************************************
 * Ref      * Who      * Date       * Description
 * ************************************************************************************************
 *          *          *            *
 * ************************************************************************************************
 */

/**
 * NameValueMapping is an interface which has constants defined used while saving product and info object messages.
 * @see ProductServiceBean
 * @see InfoObjectServiceBean
 * @author sanjay semwal
 */
public interface NameValueMapping {

    /** value for PRIORITYVALUEFORPRIMARYIMAGE constant. */
    int PRIORITYVALUEFORPRIMARYIMAGE = 1;

    /** value for PRIORITYVALUEFORSYMBOL constant. */

    int PRIORITYVALUEFORSYMBOL = 9;

    /** value for PRIORITYVALUEFORCOLORPHOTOGRAPH constant. */
    int PRIORITYVALUEFORCOLORPHOTOGRAPH = 2;

    /** value for PRIORITYVALUEFORVENDORLOGO constant. */
    int PRIORITYVALUEFORVENDORLOGO = 9;

    /** value for PRIORITYVALUEFORCONTENTTABLE constant. */
    int PRIORITYVALUEFORCONTENTTABLE = 9;

    /** value for PRIORITYVALUEFORLINEDRAWING constant. */
    int PRIORITYVALUEFORLINEDRAWING = 3;

    /** value for PRIORITYVALUEFORANIMATEDIMAGE constant. */
    int PRIORITYVALUEFORANIMATEDIMAGE = 4;

    /**
     * Region name for Europe Locales/Publications.
     */
    String EUROPE_REGION = "EUROPE";

    /**
     * Region name for Non Europe Locales/Publications.
     */
    String NON_EUROPE_REGION = "NONEUROPE";

    /**
     * Region name for International.
     */
    String INTERNATIONAL_REGION = "INTERNATIONAL";

    /**
     * Entity manager's JNDI name for Europe.
     */
    String ENTITY_MANAGER_JNDI_EUROPE = "java:entityManagerEurope";

    /**
     * Entity manager's JNDI name for Non Europe.
     */
    String ENTITY_MANAGER_JNDI_NON_EUROPE = "java:entityManagerNonEurope";

    /**
     * Root document element for Product Messages.
     */
    String ROOT_DOCUMENT_ELEMENT_PRODUCT_MESSAGE = "ECC-ProductMessage";

    /**
     * Root document element for HierarchyMessage.
     */
    String ROOT_DOCUMENT_ELEMENT_HIERARCHY_MESSAGE = "ECC-HierarchyMessage";

    /**
     * Root document element for Info Messages.
     */
    String ROOT_DOCUMENT_ELEMENT_INFO_MESSAGE = "ECC-InfoMessage";

    /**
     * Root document element for Image Messages.
     */
    String ROOT_DOCUMENT_ELEMENT_IMAGE_MESSAGE = "ECC-ImageMessage";

    /**
     * Root document element for Table content Messages.
     */
    String ROOT_DOCUMENT_ELEMENT_TABLE_MESSAGE = "ECC-TableMessage";

    /**
     * Message Audit type used for Product messages.
     */
    String MESSAGE_AUDIT_TYPE_PRODUCT = "Product";

    /**
     * Message Audit type used for TABLE messages.
     */
    String MESSAGE_AUDIT_TYPE_TABLE = "table";

    /**
     * Message Audit type used for IMAGE messages.
     */
    String MESSAGE_AUDIT_TYPE_IMAGE = "image";

    /**
     * Message Audit type used for INFO messages.
     */
    String MESSAGE_AUDIT_TYPE_INFO = "info";

    /**
     * Message Audit type used for hierarchy messages.
     */
    String MESSAGE_AUDIT_TYPE_HIERARCHY = "heirarchy";

}
