package com.electrocomponents.persistence.dao.jpa;

import java.util.List;

import javax.ejb.Stateless;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.electrocomponents.commons.cache.CentralisedCacheJndiConstants;
import com.electrocomponents.model.data.servicescache.EcServicesCacheEntity;
import com.electrocomponents.model.domain.servicescache.EcServicesCache;
import com.electrocomponents.persistence.dao.EcServicesCacheDao;
import com.electrocomponents.persistence.daomk2.GenericDaoMk2Jpa;

/**
 * <pre>
 * ************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author  : Rakesh Kumar
 * Created : 30 Apr 2012 at 17:50:26
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
 * A DAO Class for CRUD operations related to the EcServicesCacheEntity .
 * @author Rakesh Kumar
 */
@Stateless
public class EcServicesCacheDaoBean extends GenericDaoMk2Jpa<EcServicesCache, EcServicesCacheEntity, Long> implements EcServicesCacheDao {

    /** Commons logging logger. */
    private static final Log LOG = LogFactory.getLog(EcServicesCacheDaoBean.class);

    /**
     * default constructor.
     */
    public EcServicesCacheDaoBean() {
        super(CentralisedCacheJndiConstants.ENTITY_MANAGER);
    }

    @SuppressWarnings("unchecked")
    @Override
    public EcServicesCache getEcServicesCache(final String cacheAccessPath, final String cacheKey) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start getEcServicesCache");
        }
        EcServicesCache servicesCache = null;
        try {
            List<EcServicesCache> servicesCacheList =
                    _em.createNamedQuery("EcServicesCacheEntity.findEcServicesCacheEntity").setParameter("cacheKey", cacheKey)
                            .setParameter("cacheAccessPath", cacheAccessPath).getResultList();
            if (servicesCacheList!=null && servicesCacheList.isEmpty()==false) {
                servicesCache = servicesCacheList.get(0);
            }
        } catch (Exception exception) {
            LOG.error("Failed To Retrive EcServicesCache. Exception : " + exception);
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("End getEcServicesCache");
        }
        return servicesCache;
    }

    @Override
    public EcServicesCache saveEcServicesCache(final EcServicesCache ecServicesCache) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start saveEcServicesCache");
        }

        EcServicesCache servicesCache = null;
        try {
            EcServicesCache servicesCacheToUpdate = getEcServicesCache(ecServicesCache.getCacheAccessPath(), ecServicesCache.getCacheKey());
            if (servicesCacheToUpdate != null) {
                servicesCacheToUpdate.setValue(ecServicesCache.getValue());
                servicesCache = makePersistent(servicesCacheToUpdate);
            } else {
                servicesCache = makePersistent(ecServicesCache);
            }
        } catch (Exception exception) {
            LOG.error("Failed To Save EcServicesCache. Exception : " + exception);
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("End saveEcServicesCache");
        }
        return servicesCache;
    }

    @Override
    public boolean deleteEcServicesCache(final EcServicesCache ecServicesCache) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start deleteEcServicesCache");
        }
        boolean deletionFlag = false;
        try {
            EcServicesCache servicesCacheToDelete = getEcServicesCache(ecServicesCache.getCacheAccessPath(), ecServicesCache.getCacheKey());
            if (servicesCacheToDelete != null) {
                makeTransient(servicesCacheToDelete);
                deletionFlag = true;
            }
        } catch (Exception e) {
            LOG.error("Failed To Delete EcServicesCache.");
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("End deleteEcServicesCache");
        }
        return deletionFlag;
    }

}
