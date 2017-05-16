package com.electrocomponents.continuouspublishing.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.electrocomponents.continuouspublishing.service.interfaces.TableObjectService;
import com.electrocomponents.continuouspublishing.service.interfaces.TableObjectServiceLocal;
import com.electrocomponents.continuouspublishing.service.interfaces.TableObjectServiceRemote;
import com.electrocomponents.model.data.linelevel.EcPublicationMappingEntity;
import com.electrocomponents.model.data.linelevel.EcPublicationMappingId;
import com.electrocomponents.model.data.linelevel.EcTableObjectEntity;
import com.electrocomponents.model.data.linelevel.EcTableObjectId;
import com.electrocomponents.model.domain.DateTime;
import com.electrocomponents.persistence.dao.DaoFactory;
import com.electrocomponents.persistence.dao.EcPublicationMappingDaoLocal;
import com.electrocomponents.persistence.dao.EcTableObjectDaoLocal;
import com.electrocomponents.persistence.dao.jpa.EcPublicationMappingDaoBean;
import com.electrocomponents.persistence.dao.jpa.EcTableObjectDaoBean;

/*
 * ************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author  : Sanjay Semwal
 * Created : 3rd Sep 2007 at 14:13:00
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
 * TableObjectServiceBean processes the table message and saves the data in the related tables.
 * @author sanjay semwal
 */
@Stateless(name = TableObjectService.SERVICE_NAME)
@Local(TableObjectServiceLocal.class)
@Remote(TableObjectServiceRemote.class)
public class TableObjectServiceBean implements TableObjectServiceLocal, TableObjectServiceRemote {

    /** Logger. */
    private static final Log LOG = LogFactory.getLog(TableObjectServiceBean.class);

    /**
     * This Method is a service method which takes a Document object as argument retrives all values for tableobject and related entities
     * and saves the entities in the database tables.
     * @param document Document created from XML message
     * @param jndiNameUsed identifies jndiname for a datasource
     */
    public final void saveTableObjectMessageObject(final Document document, final String jndiNameUsed) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start saveTableObjectMessageObject.");
        }
        final List<EcTableObjectEntity> tablelist = new ArrayList<EcTableObjectEntity>();

        final EcTableObjectEntity tableEntity = new EcTableObjectEntity();

        String publication = "";
        String tableObjectId = "";
        String tableName = "";
        String numberOfRows = "0";
        String numberOfColumns = "0";

        NodeList list = document.getElementsByTagName("Publication");

        for (int i = 0; i < list.getLength(); i++) {
            final Element element = (Element) list.item(i);
            publication = element.getAttribute("ID");
        }

        final EcTableObjectDaoBean tableDaoBean = DaoFactory.getInstance().getDao(EcTableObjectDaoLocal.class, jndiNameUsed);
        final EcPublicationMappingDaoBean publicationMappingDaoBean = DaoFactory.getInstance().getDao(EcPublicationMappingDaoLocal.class,
                jndiNameUsed);
        list = document.getElementsByTagName("Name");
        for (int i = 0; i < list.getLength(); i++) {
            final Element element = (Element) list.item(i);
            tableName = element.getTextContent();
        }
        list = document.getElementsByTagName("ContentTable");
        for (int i = 0; i < list.getLength(); i++) {
            final Element element = (Element) list.item(i);
            tableObjectId = element.getAttribute("ID");
            numberOfColumns = element.getAttribute("NoOfColumns");
            numberOfRows = element.getAttribute("NoOfRows");
            // LOG.info("TableObject Id = " + tableObjectId);
        }
        list = document.getElementsByTagName("value");
        for (int i = 0; i < list.getLength(); i++) {

            final EcTableObjectId tableID = new EcTableObjectId();
            tableID.settableObjectId(tableObjectId);
            tableID.setPublicationName(publication);
            tableEntity.setNbrOfColumns(new Integer(numberOfColumns));
            tableEntity.setNbrOfRows(new Integer(numberOfRows));
            tableEntity.setName(tableName);
            tableEntity.setId(tableID);
            tableEntity.setCreationTime(new DateTime());
            final Element element = (Element) list.item(i);
            final String contentData = element.getTextContent();
            tableEntity.setContentData(contentData);
            tablelist.add(tableEntity);

        }
        final List<EcPublicationMappingEntity> eList = new ArrayList<EcPublicationMappingEntity>();
        boolean flagForDelinkTableObject = false;

        list = document.getElementsByTagName("ClassificationReference");
        for (int i = 0; i < list.getLength(); i++) {
            final Element element = (Element) list.item(i);
            final String classificationID = element.getAttribute("ClassificationID");
            if (classificationID == null || classificationID.equals("")) {
                flagForDelinkTableObject = true;
                break;
            }

            final EcPublicationMappingEntity ePublicationMappingEntity = new EcPublicationMappingEntity();
            final EcPublicationMappingId publicationMappingID = new EcPublicationMappingId();
            publicationMappingID.setParentId(classificationID);
            publicationMappingID.setChildId(tableObjectId);
            publicationMappingID.setPublicationName(publication);
            ePublicationMappingEntity.setId(publicationMappingID);
            ePublicationMappingEntity.setDisplayPriority(1);
            ePublicationMappingEntity.setCreationTime(new DateTime());
            ePublicationMappingEntity.setType("Table Object");
            ePublicationMappingEntity.setMessageType("TABLE");
            eList.add(ePublicationMappingEntity);
        }

        if (!flagForDelinkTableObject) {
            for (final EcTableObjectEntity eTableEntity : tablelist) {
                tableDaoBean.makePersistent(eTableEntity);
            }

            publicationMappingDaoBean.deleteForTableMessage(tableObjectId, publication, "TABLE");
            for (final EcPublicationMappingEntity ecPublicationMappingEntity : eList) {
                publicationMappingDaoBean.makePersistent(ecPublicationMappingEntity);
            }
        } else {
            publicationMappingDaoBean.deleteForTableMessage(tableObjectId, publication, "TABLE");
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish saveTableObjectMessageObject.");
        }
    }
}
