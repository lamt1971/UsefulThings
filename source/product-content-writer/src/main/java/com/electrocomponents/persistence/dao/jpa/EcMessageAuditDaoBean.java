package com.electrocomponents.persistence.dao.jpa;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.electrocomponents.model.data.linelevel.EcMessageAuditEntity;
import com.electrocomponents.model.domain.ApplicationSource;
import com.electrocomponents.model.domain.linelevel.MessageAudit;
import com.electrocomponents.persistence.dao.EcMessageAuditDao;
import com.electrocomponents.persistence.daomk2.GenericDaoMk2Jpa;
import com.electrocomponents.productcontentwriter.JndiConstants;
import com.electrocomponents.service.exception.ServiceException;

/*
 * ************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author  : Ganesh Raut
 * Created : 9 Oct 2007 at 15:45:38
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
 * TODO add class-level javadoc.
 * @author Ganesh Raut
 */
@Stateless
public class EcMessageAuditDaoBean extends GenericDaoMk2Jpa<MessageAudit, EcMessageAuditEntity, Long> implements EcMessageAuditDao {

    /** Logger. */
    private static final Log LOG = LogFactory.getLog(EcMessageAuditDaoBean.class);

    /** Default constructor. */
    public EcMessageAuditDaoBean() {
        super(JndiConstants.ENTITY_MANAGER_JNDI_NAME_EMEA);
    };

    /**
     * @param jndi jndi
     */
    public EcMessageAuditDaoBean(final String jndi) {
        super(jndi);
    }

    /**
     * * {@inheritDoc}
     * @return
     */
    public String checkForProcessedMessage(final String publicationName, final String days) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start checkForProcessedMessage.");
        }

        long ndays = Long.parseLong(days);
        String id = null;

        try {
            Query qry = _em.createNamedQuery("EcMessageAuditEntity.checkForProcessedMessage");
            qry.setParameter("publicationName", publicationName);
            qry.setParameter("days", ndays);
            qry.setHint("org.hibernate.readOnly", true);
            id = (String) qry.getSingleResult();
        } catch (NoResultException nre) {
            LOG.warn("NoResultException in checkForProcessedMessage.");
        } catch (Exception e) {
            LOG.warn("An error occurred retrieving message after date ", e);
            throw new ServiceException("An error occurred retrieving message after date ", e, ApplicationSource.RS_WEB_SITE);
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish checkForProcessedMessage.");
        }
        return id;
    }
}
