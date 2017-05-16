package com.electrocomponents.persistence.dao.productcontent;

import javax.ejb.Local;

import com.electrocomponents.model.domain.ProductNumber;
import com.electrocomponents.model.domain.Site;
import com.electrocomponents.model.domain.product.Product;
import com.electrocomponents.persistence.daomk2.GenericDaoMk2;

/*
 * ************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author  : Ed Ward
 * Created : 1 Aug 2007 at 13:49:30
 *
 * ************************************************************************************************
 * Change History
 * ************************************************************************************************
 * Ref      * Who      * Date       * Description
 * ************************************************************************************************
 *          *          *            *
 * ************************************************************************************************
 */

/**
 * DAO for obtaining implementations of <tt>Product</tt>.
 * @author Ed Ward
 */
@Local
public interface ProductDao extends GenericDaoMk2<Product> {


    /**
     * This method finds product by GroupNumber and Site and returns Product. Used by ADaM Interface.
     * @param productNumber The group number of product to find.
     * @param site The site of the product.
     * @param isReadOnly Whether the result is read only.
     * @return The Product.
     */
    Product findByGroupNumberAndSite(final ProductNumber productNumber, final Site site, final Boolean isReadOnly);

}
