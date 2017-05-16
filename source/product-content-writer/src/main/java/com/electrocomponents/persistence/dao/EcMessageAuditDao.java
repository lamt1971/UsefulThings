package com.electrocomponents.persistence.dao;

import javax.ejb.Local;

import com.electrocomponents.model.domain.linelevel.MessageAudit;
import com.electrocomponents.persistence.daomk2.GenericDaoMk2;

/*
 * ************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author  : Ganesh Raut
 * Created : 9 Oct 2007 at 15:04:21
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
 * DAO for obtaining implementations of <tt>EcMessageAuditEntity</tt>.
 * @author Ganesh Raut
 */
@Local
public interface EcMessageAuditDao extends GenericDaoMk2<MessageAudit> {

    /**
     * @param publicationName publicationName
     * @param days days
     * @return the condition number for a message processed after the passed in date.
     */
    String checkForProcessedMessage(final String publicationName, final String days);

}
