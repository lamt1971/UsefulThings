package com.electrocomponents.persistence.dao;

import java.util.List;

import javax.ejb.Local;

import com.electrocomponents.model.domain.linelevel.EcPublication;
import com.electrocomponents.persistence.daomk2.GenericDaoMk2;

/**
 * <pre>
 * ************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author  : Sridhar Katla
 * Created : 10 Sep 2007 at 15:30:20
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
 * @author Sridhar Katla.
 */
@Local
public interface EcPublicationDao extends GenericDaoMk2<EcPublication> {

    /**
     * Returns an instance of {@link EcPublication} or null if no record found.
     * @param hierarchyId the id of the hierarchy e.g. PSF_421427
     * @param publicationName the publicationName e.g. GB
     * @return BlanketOrderEntity
     */
    EcPublication findHeirarchy(String hierarchyId, String publicationName);

    /**
     * @param hierarchyLevel Hierarchy Level for the book
     * @param storeId Store Id
     * @return the publication entities in the display order.
     */
    List<EcPublication> getBooksDisplayOrder(final String hierarchyLevel, final Integer storeId);

    /**
     * @param heirachyId the heirachyId
     * @param publicationName the publicationName
     * @return hierarchy list
     */
    List<EcPublication> getHeirarchyForProduct(final String heirachyId, final String publicationName);

    /**
     * @param publicationName The Publication Name
     * @return hierarchy list
     */
    List<EcPublication> getHeirarchyForEprocSensitiveProducts(final String publicationName);

}
