package com.electrocomponents.persistence.dao.jpa.productcontent;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.electrocomponents.model.data.product.EcProductEntity;
import com.electrocomponents.model.domain.ProductNumber;
import com.electrocomponents.model.domain.Site;
import com.electrocomponents.model.domain.product.Product;
import com.electrocomponents.persistence.dao.productcontent.ProductDao;
import com.electrocomponents.persistence.daomk2.GenericDaoMk2Jpa;
import com.electrocomponents.productcontentwriter.JndiConstants;

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
 * @see ProductDao
 */
@Stateless
public class ProductDaoBean extends GenericDaoMk2Jpa<Product, EcProductEntity, Long> implements ProductDao {
    /** Commons logging logger. */
    private static final Log LOG = LogFactory.getLog(ProductDaoBean.class);

    /**
     * Public default constructor.
     */
    public ProductDaoBean() {
        super(JndiConstants.ENTITY_MANAGER_JNDI_NAME_EMEA);
    }

    /**
     * @param jndi jndi
     */
    public ProductDaoBean(final String jndi) {
        super(jndi);
    }

    /**
     * {@inheritDoc}
     */
    public Product findByGroupNumberAndSite(final ProductNumber productNumber, final Site site, final Boolean isReadOnly) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start findByGroupNumberAndSite.");
        }

        Product prod = null;

        try {
            Query qry = _em.createNamedQuery("EcProductEntity.findByGroupNumberAndSite");
            qry.setParameter("productNumber", productNumber);
            qry.setParameter("site", site);
            List list = qry.getResultList();
            if (list.size() > 0) {
                prod = (Product) list.get(0);
            } else {
                LOG.warn("No results when retrieving product " + productNumber + " for Site " + site);
            }
        } catch (Exception e) {
            LOG.warn("An error occurred retrieving product " + productNumber + " for Site " + site, e);
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish findByGroupNumberAndSite.");
        }
        return prod;
    }
}
