/**
 *
 */
package com.electrocomponents.continuouspublishing.service.impl;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.electrocomponents.continuouspublishing.service.interfaces.OIdService;
import com.electrocomponents.continuouspublishing.service.interfaces.OIdServiceLocal;
import com.electrocomponents.continuouspublishing.service.interfaces.OIdServiceRemote;
import com.electrocomponents.persistence.dao.BvOidRegistryDao;
import com.electrocomponents.persistence.dao.DaoFactory;

/*
 * ************************************************************************************************ Copyright (c) Electrocomponents Plc.
 * Author : Sanjay Semwal Created : 23 Aug 2007 at 14:13:00
 * ************************************************************************************************ Change History
 * ************************************************************************************************ Ref * Who * Date * Description
 * ************************************************************************************************ * * *
 * ************************************************************************************************
 */

/**
 * A service implementation for generating Oid.
 * @see OIdService
 * @author sanjay semwal
 */
@Stateless(name = OIdService.SERVICE_NAME)
@Local(OIdServiceLocal.class)
@Remote(OIdServiceRemote.class)
public class OIdServiceBean implements OIdServiceLocal, OIdServiceRemote {

    /** Logger. */
    private static final Log LOG = LogFactory.getLog(OIdServiceBean.class);

    /**
     * @param jndiName jndi
     * @return the generated OId
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public long generateOId(final String jndiName) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start generateOId.");
        }

        final BvOidRegistryDao dao = DaoFactory.getInstance().getDao(BvOidRegistryDao.class, jndiName);
        final long oId = dao.getOId(1);

        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish generateOId.");
        }
        return oId;
    }
}
