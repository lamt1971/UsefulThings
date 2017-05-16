package com.electrocomponents.persistence.dao.jpa;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.PersistenceException;

import com.electrocomponents.model.data.linelevel.EcProductAttributesEntity;
import com.electrocomponents.model.data.linelevel.EcProductAttributesId;
import com.electrocomponents.model.domain.ProductNumber;
import com.electrocomponents.model.domain.Site;
import com.electrocomponents.model.domain.linelevel.EcProductAttributes;
import com.electrocomponents.persistence.dao.EcProductAttributesDao;
import com.electrocomponents.persistence.daomk2.GenericDaoMk2Jpa;
import com.electrocomponents.productcontentwriter.JndiConstants;

/**
 * <pre>
 * ************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author  : Ganesh Raut
 * Created : 9 Oct 2007 at 16:10:01
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
 * @author Ganesh Raut
 */
@Stateless
public class EcProductAttributesDaoBean extends GenericDaoMk2Jpa<EcProductAttributes, EcProductAttributesEntity, EcProductAttributesId>
        implements EcProductAttributesDao {

    /**
     * Default Constructor.
     */
    public EcProductAttributesDaoBean() {
        super(JndiConstants.ENTITY_MANAGER_JNDI_NAME_EMEA);
    }

    /**
     * @param jndi jndi
     */
    public EcProductAttributesDaoBean(final String jndi) {
        super(jndi);
    }

    /**
     * {@inheritDoc}
     */
    public void deleteForTableMessage(final String productId, final String heirarchyId, final String publicationName) {
        try {
            /**
             * Need to delete all product attributes, and not just those by hierarchy getEntityManager().createQuery( "delete from
             * EcProductAttributesEntity entity where entity.id.publicationName='" + publicationName + "' and entity.id.hierarchyID='" +
             * heirarchyId + "'" + " and entity.id.productId='" + productId + "'").executeUpdate();
             */
            _em.createQuery("delete from EcProductAttributesEntity entity where entity.id.publicationName='" + publicationName
                    + "' and entity.id.productId='" + productId + "'").executeUpdate();

        } catch (final PersistenceException e) {
            // ignore
        }
    }

    /** {@inheritDoc} */
    @SuppressWarnings("unchecked")
    public List<EcProductAttributes> getProductAttributeListByProdIdAndPublicationName(final String productId,
            final String publicationName) {

        List<EcProductAttributes> ecProductAttributesList = _em.createQuery(
                "from EcProductAttributesEntity entity where entity.id.publicationName='" + publicationName + "' and entity.id.productId='"
                        + productId + "' order by entity.displayPriority").getResultList();
        return ecProductAttributesList;
    }

    /** {@inheritDoc} */
    public Integer deleteAttributeForProductId(final String productId, final String attributeId) {
        return _em.createNamedQuery("EcProductAttributesEntity.deleteFilteredAttributes").setParameter("productId", productId).setParameter(
                "attributeId", attributeId).executeUpdate();

    }

    /** {@inheritDoc} */
    public List<EcProductAttributes> getProductAttributeListByProductIdAndSite(final ProductNumber productNumber, final Site site) {
        return _em.createNamedQuery("EcProductAttributesEntity.getAttributesByProductIdAndSite").setParameter("productNumber", productNumber
                .getRawProductNumber()).setParameter("site", site).getResultList();
    }
}
