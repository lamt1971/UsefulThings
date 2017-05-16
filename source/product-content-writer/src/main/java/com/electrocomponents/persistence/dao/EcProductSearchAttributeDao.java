package com.electrocomponents.persistence.dao;

import javax.ejb.Local;
import com.electrocomponents.model.domain.linelevel.EcProductSearchAttribute;
import com.electrocomponents.persistence.daomk2.GenericDaoMk2;


/**
 * <pre>
 * ************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author  : Deepak Lathwal
 * Created : 7 Nov 2013 at 17:18:25
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
 * DAO for obtaining implementations of <tt>EcProductSearchAttribute</tt>.
 * @author Deepak Lathwal
 */
@Local
public interface EcProductSearchAttributeDao extends GenericDaoMk2<EcProductSearchAttribute> {

    /**
     * @param productId the stock number of the product to be retrieved.
     * @param publicationName the broad vision store identifier used to help locate the product instance.
     * @return Integer as count of Deletion.
     */
    Integer deleteFromTableEcProductSearchAttribute(final String productId, final String publicationName);
}
