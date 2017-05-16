package com.electrocomponents.persistence.dao.jpa;

import javax.ejb.Stateless;

import com.electrocomponents.model.data.linelevel.EcProductSearchAttributeEntity;
import com.electrocomponents.model.data.linelevel.EcProductSearchAttributeId;
import com.electrocomponents.model.domain.linelevel.EcProductSearchAttribute;
import com.electrocomponents.persistence.dao.EcProductSearchAttributeDao;
import com.electrocomponents.persistence.daomk2.GenericDaoMk2Jpa;
import com.electrocomponents.productcontentwriter.JndiConstants;

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
 * This DAO layer will be used to fetch records form bvuser.EC_PRODUCT_SRCH_ATTRIBUTE table which is snapshot of BVUSER.EC_LINK table.
 * @author Deepak Lathwal
 */
@Stateless
public class EcProductSearchAttributeDaoBean extends
        GenericDaoMk2Jpa<EcProductSearchAttribute, EcProductSearchAttributeEntity, EcProductSearchAttributeId> implements
        EcProductSearchAttributeDao {
    /**
     * Default constructor.
     */
    public EcProductSearchAttributeDaoBean() {
        super(JndiConstants.ENTITY_MANAGER_JNDI_NAME_EMEA);
    }

    /**
     * @param jndi jndi
     */
    public EcProductSearchAttributeDaoBean(final String jndi) {
        super(jndi);
    }

    /**
     * @param productId productId
     * @param publicationName publicationName
     * @return  the Integer
     */
    public Integer deleteFromTableEcProductSearchAttribute(final String productId, final String publicationName) {
        return _em.createNamedQuery("EcProductSearchAttributeEntity.deleteFromTableEcProductSearchAttribute").setParameter(
                "publicationName", publicationName).setParameter("productId", productId).executeUpdate();
    }
}
