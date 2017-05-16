package com.electrocomponents.persistence.dao.jpa;

import java.util.List;

import javax.ejb.Stateless;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.electrocomponents.model.data.config.BvOidRegistry;
import com.electrocomponents.model.data.config.BvOidRegistryEntity;
import com.electrocomponents.model.domain.ApplicationSource;
import com.electrocomponents.persistence.dao.BvOidRegistryDao;
import com.electrocomponents.persistence.daomk2.GenericDaoMk2Jpa;
import com.electrocomponents.productcontentwriter.JndiConstants;
import com.electrocomponents.service.exception.ServiceException;

/**
 * An implementation class for BvOidRegistryDao.
 * @see BvOidRegistryDao
 * @author Ganesh Raut
 */
@Stateless
public class BvOidRegistryDaoBean extends GenericDaoMk2Jpa<BvOidRegistry, BvOidRegistryEntity, Long> implements BvOidRegistryDao {

    /** Commons logging logger. */
    private static final Log LOG = LogFactory.getLog(BvOidRegistryDaoBean.class);

    /** Default Constructor. */
    public BvOidRegistryDaoBean() {
        super(JndiConstants.ENTITY_MANAGER_JNDI_NAME_EMEA);
    }

    /**
     * @param jndi jndi
     */
    public BvOidRegistryDaoBean(final String jndi) {
        super(jndi);
    }

    /** {@inheritDoc} */
    public long getOId(final int systemId) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start getOId.");
        }

        List<BvOidRegistry> bvOidRegistry = null;
        bvOidRegistry = _em.createNamedQuery("BvOidRegistry.GenerateOIdProc").setParameter("cId", systemId).getResultList();
        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish getOId.");
        }
        if (bvOidRegistry != null && bvOidRegistry.size() > 0) {
            return bvOidRegistry.get(0).getOId();
        }
        throw new ServiceException("Problem while generating the sequence number in Oid service.", ApplicationSource.RS_WEB_SITE);
    }
}
