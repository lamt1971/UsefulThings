package com.electrocomponents.persistence.dao.jpa;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.electrocomponents.model.data.linelevel.EcPublicationEntity;
import com.electrocomponents.model.data.linelevel.EcPublicationId;
import com.electrocomponents.model.domain.linelevel.EcPublication;
import com.electrocomponents.persistence.dao.EcPublicationDaoLocal;
import com.electrocomponents.persistence.daomk2.GenericDaoMk2Jpa;
import com.electrocomponents.productcontentwriter.JndiConstants;

/**
 * <pre>
 * ************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author  : Sridhar Katla.
 * Created : 10 Sep 2007 at 15:39:08
 *
 * ************************************************************************************************
 * Change History
 * ************************************************************************************************
 * Ref      * Who      * Date       * Description
 * ************************************************************************************************
 *          * UK307495 * 30-04-2009 * Added getHeirarchyForEprocSensitiveProducts method for eProc
 * ************************************************************************************************
 * </pre>
 */

/**
 * @author Sridhar Katla.
 */
@Stateless
public class EcPublicationDaoBean extends GenericDaoMk2Jpa<EcPublication, EcPublicationEntity, EcPublicationId> implements
        EcPublicationDaoLocal {

    /** Logger. */
    private static final Log LOG = LogFactory.getLog(EcPublicationDaoBean.class);

    /**
     * Public default constructor.
     */
    public EcPublicationDaoBean() {
        super(JndiConstants.ENTITY_MANAGER_JNDI_NAME_EMEA);
    }

    /**
     * @param jndi jndi
     */
    public EcPublicationDaoBean(final String jndi) {
        super(jndi);
    }

    /** {@inheritDoc} */
    public EcPublication findHeirarchy(final String hierarchyId, final String publicationName) {
        EcPublication ecPublication = null;
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start findHeirarchy.");
        }

        try {
            ecPublication = (EcPublication) _em.createNamedQuery("EcPublication.findHierarchy").setParameter("hierarchyId", hierarchyId)
                    .setParameter("publicationName", publicationName).getSingleResult();
        } catch (final NoResultException nre) {
            StringBuilder message = new StringBuilder();
            message.append("No publication record found with heirachyId=").append(hierarchyId);
            message.append("and publicationName=").append(publicationName);
            LOG.debug(message);
        } catch (final Exception e) {
            StringBuilder error = new StringBuilder();
            error.append("Failed to retrieve EcPublication with heirachyId=").append(hierarchyId);
            error.append("and publicationName=").append(publicationName);
            LOG.error(error.toString(), e);
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish findHeirarchy.");
        }

        return ecPublication;
    }

    /** {@inheritDoc} */
    @SuppressWarnings("unchecked")
    public List<EcPublication> getBooksDisplayOrder(final String hierarchyLevel, final Integer storeId) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start getBooksDisplayOrder.");
        }

        List<EcPublication> entities = _em.createNamedQuery("EcPublication.findByHierarchyLevel").setParameter("hierarchyLevel",
                hierarchyLevel).setParameter("storeId", storeId).getResultList();

        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish getBooksDisplayOrder.");
        }
        return entities;
    }

    /** {@inheritDoc} */
    @SuppressWarnings("unchecked")
    public List<EcPublication> getHeirarchyForProduct(final String heirachyId, final String publicationName) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start getHeirarchyForProduct.");
        }

        List<EcPublication> entities = _em.createNamedQuery("EcPublication.getHeirarchyForProduct").setParameter("heirachyId", heirachyId)
                .setParameter("publicationName", publicationName).getResultList();

        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish getHeirarchyForProduct.");
        }
        return entities;
    }

    /** {@inheritDoc} */
    @SuppressWarnings("unchecked")
    public List<EcPublication> getHeirarchyForEprocSensitiveProducts(final String publicationName) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start getHeirarchyForEprocSensitiveProducts.");
        }

        List<EcPublication> entities = _em.createNamedQuery("EcPublication.getHeirarchyForEprocSensitiveProducts").setParameter(
                "publicationName", publicationName).getResultList();

        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish getHeirarchyForEprocSensitiveProducts.");
        }
        return entities;
    }

}
