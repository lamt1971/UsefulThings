package com.electrocomponents.persistence.dao;

import java.util.List;

import javax.ejb.Local;

import com.electrocomponents.model.domain.linelevel.EcProductSymbol;
import com.electrocomponents.persistence.daomk2.GenericDaoMk2;

/**
 * <pre>
 * ************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author  : sanjay semwal
 * Created : 05 Sep 2007 at 09:09:30
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
 * DAO for obtaining implementations of <tt>ECProductSymbolEntity</tt>.
 * @author sanjay semwal
 */
@Local
public interface EcProductSymbolDao extends GenericDaoMk2<EcProductSymbol> {

    /**
     * @param productId the stock number of the product to be retrieved.
     * @param publicationName the broad vision store identifier used to help locate the product instance.
     */
    void deleteFromTable(String productId, String publicationName);

    /**
     * @param productId productId
     * @param publicationName publicationName
     * @return List of EcProductSymbolEntity
     */
    List<EcProductSymbol> getProductSymbolsByProdIdAndPublicationName(final String productId, final String publicationName);
}
