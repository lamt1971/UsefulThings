package com.electrocomponents.persistence.dao;

import javax.ejb.Local;

import com.electrocomponents.model.data.config.BvOidRegistry;
import com.electrocomponents.persistence.daomk2.GenericDaoMk2;

/**<pre>
 * ************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author  : Ganesh Raut
 * Created : 31 Jul 2007 at 12:51:58
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
 * DAO for obtaining BvOidRegistry instances.
 * @author Ganesh Raut
 */
@Local
public interface BvOidRegistryDao extends GenericDaoMk2<BvOidRegistry> {

    /**
     * calls a stored procedure to generate OId. You should pass 31 for the general sequence. For Continuous Publishing you need to pass 1
     * as parameter.
     * @param systemId the systemId using which sequence number has to be generated.
     * @return the generated OId
     */
    long getOId(int systemId);
}
