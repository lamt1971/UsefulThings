package com.electrocomponents.persistence.dao.jpa;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.electrocomponents.model.data.linelevel.EcStorePublicationEntity;
import com.electrocomponents.model.data.linelevel.EcStorePublicationId;
import com.electrocomponents.model.domain.Site;
import com.electrocomponents.model.domain.linelevel.EcStorePublication;
import com.electrocomponents.persistence.dao.EcStorePublicationDao;
import com.electrocomponents.persistence.daomk2.GenericDaoMk2Jpa;
import com.electrocomponents.productcontentwriter.JndiConstants;

/*
 * ************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author  : Ganesh Raut
 * Created : 9 Oct 2007 at 16:27:06
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
 * TODO -- Duplicated DAO Bean. Same DAoBean is in site-language-publication-mapping module. may need look at merging and reusing that module.
 * @author Ganesh Raut
 */
@Stateless
public class EcStorePublicationDaoBean extends GenericDaoMk2Jpa<EcStorePublication, EcStorePublicationEntity, EcStorePublicationId>
        implements EcStorePublicationDao {

    /** Logger. */
    private static final Log LOG = LogFactory.getLog(EcStorePublicationDaoBean.class);

    /**
     * Default constructor.
     */
    public EcStorePublicationDaoBean() {
        super(JndiConstants.ENTITY_MANAGER_JNDI_NAME_EMEA);
    }

    /**
     * @param jndi jndi
     */
    public EcStorePublicationDaoBean(final String jndi) {
        super(jndi);
    }

    /**
     * @param publicationName publicationName
     * @return list
     */
    @SuppressWarnings("unchecked")
    public List<EcStorePublication> getEcStorePublicationsByPublicationName(final String publicationName) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start getEcStorePublicationsByPublicationName.");
        }

        Query qry = _em.createNamedQuery("EcStorePublicationEntity.findStorePublicationByPublicationName");
        qry.setParameter("publicationName", publicationName);
        // Prevent any changes made to Entity being automatically persisted + improves performance..
        qry.setHint("org.hibernate.readOnly", new Boolean(true));

        List storePublications = qry.getResultList();

        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish getEcStorePublicationsByPublicationName.");
        }
        return storePublications;
    }

    /** {@inheritDoc}. */
    public EcStorePublication getEcStorePublicationByStoreId(final Long storeId) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start getEcStorePublicationByStoreId.");
        }
        List<EcStorePublication> ecStorePublicationList = _em.createQuery("from EcStorePublicationEntity p where p.id.storeId='" + storeId
                + "'").getResultList();

        EcStorePublication ecStorePublication = null;
        if (ecStorePublicationList != null && ecStorePublicationList.size() > 0) {
            ecStorePublication = ecStorePublicationList.get(0);
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish getEcStorePublicationByStoreId.");
        }
        return ecStorePublication;
    }

    /**
     * fetches ecStorePublication for a given site.
     * @param site site.
     * @return EcStorePublicationEntity
     */
    public EcStorePublication getEcStorePublicationsBySite(final Site site) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start getEcStorePublicationsBySite.");
        }
        Query qry = _em.createNamedQuery("EcStorePublicationEntity.findStorePublicationBySite");
        qry.setParameter("site", site);
        // Prevent any changes made to Entity being automatically persisted + improves performance..
        qry.setHint("org.hibernate.readOnly", new Boolean(true));

        // Enable the query cache for Publication lookups...
        qry.setHint("org.hibernate.cacheable", true);
        qry.setHint("org.hibernate.cacheRegion", "query.core.config.publicationmapping");

        EcStorePublication storePublication = null;
        try {
            storePublication = (EcStorePublicationEntity) qry.getSingleResult();
        } catch (NoResultException e) {
            LOG.warn("There are zero Mappings for the Site:  " + site);
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish getEcStorePublicationsBySite.");
        }
        return storePublication;
    }
}
