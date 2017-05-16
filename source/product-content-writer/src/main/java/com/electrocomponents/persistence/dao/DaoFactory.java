package com.electrocomponents.persistence.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.electrocomponents.persistence.dao.jpa.BvOidRegistryDaoBean;
import com.electrocomponents.persistence.dao.jpa.EcImageDaoBean;
import com.electrocomponents.persistence.dao.jpa.EcInfoObjectDaoBean;
import com.electrocomponents.persistence.dao.jpa.EcInfoTextDaoBean;
import com.electrocomponents.persistence.dao.jpa.EcMessageAuditDaoBean;
import com.electrocomponents.persistence.dao.jpa.EcMessageFailureDaoBean;
import com.electrocomponents.persistence.dao.jpa.EcProductAttributesDaoBean;
import com.electrocomponents.persistence.dao.jpa.EcProductSearchAttributeDaoBean;
import com.electrocomponents.persistence.dao.jpa.EcProductSymbolDaoBean;
import com.electrocomponents.persistence.dao.jpa.EcPublicationDaoBean;
import com.electrocomponents.persistence.dao.jpa.EcPublicationMappingDaoBean;
import com.electrocomponents.persistence.dao.jpa.EcStorePublicationDaoBean;
import com.electrocomponents.persistence.dao.jpa.EcTableObjectDaoBean;
import com.electrocomponents.persistence.dao.jpa.productcontent.ProductDaoBean;
import com.electrocomponents.persistence.dao.productcontent.ProductDao;

/**
 * ************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author  : Ed Ward
 * Created : May 2007
 *
 * ************************************************************************************************
 * Change History
 * ************************************************************************************************
 * Ref      * Who      * Date       * Description
 * ************************************************************************************************
 * GEN-924  * E0161085 * 20/07/2010 * Removed reference to EcomCGCustomer.
 * ************************************************************************************************
 * GEN-1302 * E0161085 * 23/09/2010 * Removed stacks of Eproc Stuff :-)
 * ************************************************************************************************
 */

/**
 * This factory is intended for use by classes that need to instantiate DAO instances directly (rather than ideally having the instances
 * 'dependency injected'). Use of this factory decouples classes' dependency on any particular implementation of the DAO.
 * @author Ed Ward
 */
public abstract class DaoFactory {

    /** Commons logging logger. */
    private static final Log LOG = LogFactory.getLog(DaoFactory.class);

    /** The singleton instance. */
    private static DaoFactory instance;

    static {
        instance = new DaoFactoryBean();
    }

    /** */
    DaoFactory() {
    }

    /**
     * getDao(2).
     * @param <T> the type of the DAO factory
     * @param daoClass the type of the DAO factory required
     * @param jndi the jndi name
     * @return the DAO factory
     */
    public abstract <T> T getDao(final Class daoClass, final String jndi);

    /**
     * @return {@link #instance}
     */
    public static DaoFactory getInstance() {
        return instance;
    }

    /**
     * The non-test implementation.
     */
    public static class DaoFactoryBean extends DaoFactory {

        /**
         * @param <T> the type of the DAO factory
         * @param daoClass the type of the DAO factory required
         * @param jndi the jndi name
         * @return the DAO factory
         */
        @SuppressWarnings("unchecked")
        public <T> T getDao(final Class daoClass, final String jndi) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Start getDao(2).");
            }
            T dao = null;
            if (EcImageDaoLocal.class.equals(daoClass)) {
                dao = (T) new EcImageDaoBean(jndi);
            } else if (EcInfoObjectDaoLocal.class.equals(daoClass)) {
                dao = (T) new EcInfoObjectDaoBean(jndi);
            } else if (EcPublicationDaoLocal.class.equals(daoClass)) {
                dao = (T) new EcPublicationDaoBean(jndi);
            } else if (EcPublicationMappingDaoLocal.class.equals(daoClass)) {
                dao = (T) new EcPublicationMappingDaoBean(jndi);
            } else if (EcTableObjectDaoLocal.class.equals(daoClass)) {
                dao = (T) new EcTableObjectDaoBean(jndi);
            } else if (ProductDao.class.equals(daoClass)) {
                dao = (T) new ProductDaoBean(jndi);
            } else if (EcProductAttributesDao.class.equals(daoClass)) {
                dao = (T) new EcProductAttributesDaoBean(jndi);
            } else if (EcProductSymbolDao.class.equals(daoClass)) {
                dao = (T) new EcProductSymbolDaoBean(jndi);
            } else if (EcInfoTextDaoLocal.class.equals(daoClass)) {
                dao = (T) new EcInfoTextDaoBean(jndi);
            } else if (EcMessageFailureDao.class.equals(daoClass)) {
                dao = (T) new EcMessageFailureDaoBean(jndi);
            } else if (EcMessageAuditDao.class.equals(daoClass)) {
                dao = (T) new EcMessageAuditDaoBean(jndi);
            } else if (BvOidRegistryDao.class.equals(daoClass)) {
                dao = (T) new BvOidRegistryDaoBean(jndi);
            } else if (EcProductSearchAttributeDao.class.equals(daoClass)) {
                dao = (T) new EcProductSearchAttributeDaoBean(jndi);
            } else if (EcStorePublicationDao.class.equals(daoClass)) {
                dao = (T) new EcStorePublicationDaoBean(jndi);
            }

            if (dao == null) {
                final String errorMsg = "failed to retrieve DAO for class " + daoClass.toString() + " For JNDI Name : " + jndi;
                if (LOG.isErrorEnabled()) {
                    LOG.error(errorMsg);
                }
                throw new NullPointerException(errorMsg);
            }
            if (LOG.isDebugEnabled()) {
                LOG.debug("Finish getDao(2).");
            }
            return dao;
        }
    }
}
