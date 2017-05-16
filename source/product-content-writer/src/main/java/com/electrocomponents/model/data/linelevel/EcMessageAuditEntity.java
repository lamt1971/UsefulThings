package com.electrocomponents.model.data.linelevel;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.electrocomponents.model.domain.linelevel.MessageAudit;

/*
 * ************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author  : Ganesh Raut
 * Created : 9 Oct 2007 at 12:35:26
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
 * An entity representing a row in the EC_MESSAGE_AUDIT table.
 * @author Ganesh Raut
 */
@Entity
@Table(name = "EC_MESSAGE_AUDIT")
public class EcMessageAuditEntity implements Serializable, MessageAudit {

    /** serialVersionUID. */
    private static final long serialVersionUID = 6663928215849954682L;

    /** The oid. */
    @SequenceGenerator(sequenceName = "EC_MESSAGE_AUDIT_SEQ", name = "seqGen")
    @Id
    @GeneratedValue(generator = "seqGen")
    @Column(name = "OID")
    private long oid;

    /** The confirmationMessage. */
    @Column(name = "CONFIRMATION_MESSAGE", length = 100)
    private String confirmationMessage;

    /** The messageContent. */
    @Column(name = "MESSAGE", length = 100)
    private String messageContent;

    /** The createdTime. */
    @Column(name = "CREATION_TIME", length = 100)
    private Date createdTime;

    /** The PUBLICATION NAME. */
    @Column(name = "PUBLICATION_NAME", length = 100)
    private String publicationName;

    /** The object ID . */
    @Column(name = "OBJECT_ID")
    private String objectId;

    /** The message type. */
    @Column(name = "MESSAGE_TYPE", length = 100)
    private String messageType;

    /**
     */
    public EcMessageAuditEntity() {
    }

    /**
     * @return the confirmationMessage
     */
    public String getConfirmationMessage() {
        return confirmationMessage;
    }

    /**
     * @param confirmationMessage the confirmationMessage to set
     */
    public void setConfirmationMessage(final String confirmationMessage) {
        this.confirmationMessage = confirmationMessage;
    }

    /**
     * @return the createdTime
     */
    public Date getCreatedTime() {
        return createdTime;
    }

    /**
     * @param createdTime the createdTime to set
     */
    public void setCreatedTime(final Date createdTime) {
        this.createdTime = createdTime;
    }

    /**
     * @return the messageContent
     */
    public String getMessageContent() {
        return messageContent;
    }

    /**
     * @param messageContent the messageContent to set
     */
    public void setMessageContent(final String messageContent) {
        this.messageContent = messageContent;
    }

    /**
     * @return the messageType
     */
    public String getMessageType() {
        return messageType;
    }

    /**
     * @param messageType the messageType to set
     */
    public void setMessageType(final String messageType) {
        this.messageType = messageType;
    }

    /**
     * @return the objectId
     */
    public String getObjectId() {
        return objectId;
    }

    /**
     * @param objectId the objectId to set
     */
    public void setObjectId(final String objectId) {
        this.objectId = objectId;
    }

    /**
     * @return the oid
     */
    public long getOid() {
        return oid;
    }

    /**
     * @param oid the oid to set
     */
    public void setOid(final long oid) {
        this.oid = oid;
    }

    /**
     * @return the publicationName
     */
    public String getPublicationName() {
        return publicationName;
    }

    /**
     * @param publicationName the publicationName to set
     */
    public void setPublicationName(final String publicationName) {
        this.publicationName = publicationName;
    }

    /**
     * @return hash code
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (oid ^ (oid >>> 32));
        return result;
    }

    /**
     * @param obj the object to compare
     * @return <tt>true</tt> if equal to <tt>obj</tt>
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final EcMessageAuditEntity other = (EcMessageAuditEntity) obj;
        if (oid != other.oid) {
            return false;
        }
        return true;

    }
}
