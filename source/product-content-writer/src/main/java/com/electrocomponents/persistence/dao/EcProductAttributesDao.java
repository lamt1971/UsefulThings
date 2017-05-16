package com.electrocomponents.persistence.dao;

import java.util.List;

import javax.ejb.Local;

import com.electrocomponents.model.domain.ProductNumber;
import com.electrocomponents.model.domain.Site;
import com.electrocomponents.model.domain.linelevel.EcProductAttributes;
import com.electrocomponents.persistence.daomk2.GenericDaoMk2;

/**
 * <pre>
 * ************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author  : sanjay semwal
 * Created : 30 Aug 2007 at 13:49:30
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
 * DAO for obtaining implementations of <tt>ECPublicationMappingEntity</tt>.
 * @author sanjay semwal, Yogesh Patil
 */
@Local
public interface EcProductAttributesDao extends GenericDaoMk2<EcProductAttributes> {

    /**
     * @param productId productId
     * @param heirarchyId heirarchyId
     * @param publicationName publicationName
     */
    void deleteForTableMessage(final String productId, final String heirarchyId, final String publicationName);

    /**
     * @param productId productId
     * @param publicationName publicationName
     * @return list of EcProductAttributes
     */
    List<EcProductAttributes> getProductAttributeListByProdIdAndPublicationName(String productId, String publicationName);

    /**
     * @param productNumber The product number
     * @param site The site
     * @return list of EcProductAttributes
     */
    List<EcProductAttributes> getProductAttributeListByProductIdAndSite(ProductNumber productNumber, Site site);

    /**
     * @param productId productId
     * @param attributeId attribute id which is to be deleted
     * @return number of records deleted
     */
    Integer deleteAttributeForProductId(String productId, String attributeId);
}
