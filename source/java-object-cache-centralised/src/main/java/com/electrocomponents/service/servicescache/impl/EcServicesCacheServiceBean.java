package com.electrocomponents.service.servicescache.impl;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.electrocomponents.model.domain.servicescache.EcServicesCache;
import com.electrocomponents.persistence.dao.EcServicesCacheDao;
import com.electrocomponents.service.core.validation.ValidationUtility;
import com.electrocomponents.service.servicescache.interfaces.EcServicesCacheService;
import com.electrocomponents.service.servicescache.interfaces.EcServicesCacheServiceLocal;
import com.electrocomponents.service.servicescache.interfaces.EcServicesCacheServiceRemote;

/**
 * <pre>
 * ************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author  : Rakesh Kumar
 * Created : 1 May 2012 at 10:48:08
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
 * An implementation class for EcCacheService interface.
 * @author Rakesh Kumar
 */
@Stateless(name=EcServicesCacheServiceBean.SERVICE_NAME)
@Local(EcServicesCacheServiceLocal.class)
@Remote(EcServicesCacheServiceRemote.class)
public class EcServicesCacheServiceBean implements EcServicesCacheServiceLocal, EcServicesCacheServiceRemote {

    /** Commons logging logger. */
    private static final Log LOG = LogFactory.getLog(EcServicesCacheServiceBean.class);
    /**
     * EcServicesCacheDao instance injected as stateless EJB.
     */
    @EJB
    private EcServicesCacheDao ecServicesCacheDao;

    @Override
    public EcServicesCache saveServicesCache(final EcServicesCache ecServicesCache) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start saveServicesCache");
        }

        /* Validating the mandatory parameters. */
        ValidationUtility.validateMandatoryParameter("EcServicesCache", ecServicesCache,EcServicesCacheServiceBean.class.getName());

        EcServicesCache eServicesCache = ecServicesCacheDao.saveEcServicesCache(ecServicesCache);
        if (LOG.isDebugEnabled()) {
            LOG.debug("End saveServicesCache");
        }
        return eServicesCache;
    }

    @Override
    public EcServicesCache getServicesCache(final String cacheAccessPath, final String cacheKey) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start saveServicesCache");
        }

        /* Validating the mandatory parameters. */
        ValidationUtility.validateMandatoryParameter("cacheAccessPath", cacheAccessPath,EcServicesCacheServiceBean.class.getName());
        ValidationUtility.validateMandatoryParameter("cacheKey", cacheKey,EcServicesCacheServiceBean.class.getName());

        EcServicesCache eServicesCache = ecServicesCacheDao.getEcServicesCache(cacheAccessPath, cacheKey);

        if (LOG.isDebugEnabled()) {
            LOG.debug("End saveServicesCache");
        }
        return eServicesCache;
    }

    @Override
    public Boolean deleteServicesCache(final EcServicesCache ecServicesCache) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start deleteServicesCache");
        }

        /* Validating the mandatory parameters. */
        ValidationUtility.validateMandatoryParameter("EcServicesCache", ecServicesCache,EcServicesCacheServiceBean.class.getName());

        Boolean deletionFlag = ecServicesCacheDao.deleteEcServicesCache(ecServicesCache);

        if (LOG.isDebugEnabled()) {
            LOG.debug("End deleteServicesCache");
        }
        return deletionFlag;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getInstance(final Class ecServicesCache) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start getInstance.");
        }

        T t = null;
        if (EcServicesCache.class.getName().equals(ecServicesCache.getName())) {
            t = (T) ecServicesCacheDao.newInstance();
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish getInstance.");
        }
        return t;
    }
}
