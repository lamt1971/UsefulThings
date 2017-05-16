package com.electrocomponents.model.domain.linelevel;

import java.util.Date;

/**
 * Domain interface for MessageAudit. Used by CWP for message auditing.
 * @author C0950079
 */
public interface MessageAudit {

    /**
     * @return the confirmationMessage
     */
    String getConfirmationMessage();

    /**
     * @param confirmationMessage the confirmationMessage to set
     */
    void setConfirmationMessage(final String confirmationMessage);

    /**
     * @return the createdTime
     */
    Date getCreatedTime();

    /**
     * @param createdTime the createdTime to set
     */
    void setCreatedTime(final Date createdTime);

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
     * @return the objectId
     */
    String getObjectId();

    /**
     * @param objectId the objectId to set
     */
    void setObjectId(final String objectId);

    /**
     * @return the oid
     */
    long getOid();

    /**
     * @param oid the oid to set
     */
    void setOid(final long oid);

    /**
     * @return the publicationName
     */
    String getPublicationName();

    /**
     * @param publicationName the publicationName to set
     */
    void setPublicationName(final String publicationName);

}
