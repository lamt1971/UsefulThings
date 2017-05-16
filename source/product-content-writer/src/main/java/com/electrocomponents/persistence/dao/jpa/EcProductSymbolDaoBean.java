package com.electrocomponents.persistence.dao.jpa;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.PersistenceException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.electrocomponents.model.data.linelevel.EcProductSymbolEntity;
import com.electrocomponents.model.data.linelevel.EcProductSymbolId;
import com.electrocomponents.model.domain.linelevel.EcProductSymbol;
import com.electrocomponents.persistence.dao.EcProductSymbolDao;
import com.electrocomponents.persistence.daomk2.GenericDaoMk2Jpa;
import com.electrocomponents.productcontentwriter.JndiConstants;

/**
 * <pre>
 * ************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author  : Ganesh Raut
 * Created : 9 Oct 2007 at 16:19:14
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
public class EcProductSymbolDaoBean extends GenericDaoMk2Jpa<EcProductSymbol, EcProductSymbolEntity, EcProductSymbolId> implements
        EcProductSymbolDao {

    /** Logger. */
    private static final Log LOG = LogFactory.getLog(EcProductSymbolDaoBean.class);

    /**
     * Public default constructor.
     */
    public EcProductSymbolDaoBean() {
        super(JndiConstants.ENTITY_MANAGER_JNDI_NAME_EMEA);
    }

    /**
     * @param jndi jndi
     */
    public EcProductSymbolDaoBean(final String jndi) {
        super(jndi);
    }

    /**
     * @param productId productId
     * @param publicationName publicationName
     */
    public void deleteFromTable(final String productId, final String publicationName) {
        try {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Start deleteFromTable.");
            }
            _em.createQuery("delete from EcProductSymbolEntity entity where entity.id.publicationName='" + publicationName
                    + "' and entity.id.productId='" + productId + "'").executeUpdate();

            if (LOG.isDebugEnabled()) {
                LOG.debug("Finish deleteFromTable.");
            }
        } catch (final PersistenceException e) {
            StringBuilder error = new StringBuilder();
            error.append("Exception cannot delete EcProductSymbolEntity with productId=").append(productId);
            error.append(", publicationName=").append(publicationName);
            LOG.error(error.toString(), e);
        }
    }

    /** {@inheritDoc}. */
    public List<EcProductSymbol> getProductSymbolsByProdIdAndPublicationName(final String productId, final String publicationName) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Start getProductSymbolsByProdIdAndPublicationName.");
        }
        List<EcProductSymbol> ecProductSymbolList = _em.createQuery("from EcProductSymbolEntity entity where entity.id.publicationName='"
                + publicationName + "' and entity.id.productId='" + productId + "'").getResultList();
        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish getProductSymbolsByProdIdAndPublicationName.");
        }
        return ecProductSymbolList;
    }
}
