package com.electrocomponents.persistence.dao;

import java.util.List;

import javax.ejb.Local;

import com.electrocomponents.model.domain.Site;
import com.electrocomponents.model.domain.linelevel.EcStorePublication;
import com.electrocomponents.persistence.daomk2.GenericDaoMk2;

/*
 * ************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author  : sanjay semwal
 * Created : 31 Aug 2007 at 11:45:30
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
 * DAO for obtaining implementations of <tt>ECStorePublicationEntity</tt>.
 * @author sanjay semwal
 */
@Local
public interface EcStorePublicationDao extends GenericDaoMk2<EcStorePublication> {

    /**
     * @param publicationName publicationName
     * @return list
     */
    List<EcStorePublication> getEcStorePublicationsByPublicationName(final String publicationName);

    /**
     * @param storeId storeId
     * @return list
     */
    EcStorePublication getEcStorePublicationByStoreId(Long storeId);

    /**
     * fetches ecStorePublication for a given site.
     * @param site site.
     * @return EcStorePublicationEntity
     */
    EcStorePublication getEcStorePublicationsBySite(Site site);

}
