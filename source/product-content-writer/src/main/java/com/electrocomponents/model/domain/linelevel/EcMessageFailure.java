package com.electrocomponents.model.domain.linelevel;

import java.util.Date;

/**
 * <pre>
 * ************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author  : C0950079
 * Created : 28 Oct 2010 at 14:43:14
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
 * TODO add class-level javadoc.
 * @author C0950079
 */
public interface EcMessageFailure {
    /**
     * @return the createdTime
     */
    Date getCreatedTime();

    /**
     * @param createdTime the createdTime to set
     */
    void setCreatedTime(final Date createdTime);

    /**
     * @return the errorMessage
     */
    String getErrorMessage();

    /**
     * @param errorMessage the errorMessage to set
     */
    void setErrorMessage(final String errorMessage);

    /**
     * @return the messageContent
     */
    String getMessageContent();

    /**
     * @param messageContent the messageContent to set
     */
    void setMessageContent(final String messageContent);

    /**
     * @return the messageType
     */
    String getMessageType();

    /**
     * @param messageType the messageType to set
     */
    void setMessageType(final String messageType);

    /**
     * @return the objectID
     */
    String getObjectID();

    /**
     * @param objectID the objectID to set
     */
    void setObjectID(final String objectID);

    /**
     * @return the oid
     */
    long getOid();

    /**
     * @param oid the oid to set
     */
    void setOid(final long oid);

    /**
     * @return the ationName
     */
    String getPublicationName();

    /**
     * @param publicationName the publicationName to set
     */
    void setPublicationName(final String publicationName);

}
