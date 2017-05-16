package com.electrocomponents.continuouspublishing.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.electrocomponents.continuouspublishing.service.interfaces.InfoObjectService;
import com.electrocomponents.continuouspublishing.service.interfaces.InfoObjectServiceLocal;
import com.electrocomponents.continuouspublishing.service.interfaces.InfoObjectServiceRemote;
import com.electrocomponents.continuouspublishing.utility.NameValueMapping;
import com.electrocomponents.model.data.linelevel.EcInfoObjectEntity;
import com.electrocomponents.model.data.linelevel.EcInfoObjectId;
import com.electrocomponents.model.data.linelevel.EcInfoTextEntity;
import com.electrocomponents.model.data.linelevel.EcInfoTextId;
import com.electrocomponents.model.data.linelevel.EcPublicationMappingEntity;
import com.electrocomponents.model.data.linelevel.EcPublicationMappingId;
import com.electrocomponents.model.domain.DateTime;
import com.electrocomponents.persistence.dao.DaoFactory;
import com.electrocomponents.persistence.dao.EcInfoObjectDaoLocal;
import com.electrocomponents.persistence.dao.EcInfoTextDaoLocal;
import com.electrocomponents.persistence.dao.EcPublicationMappingDaoLocal;
import com.electrocomponents.persistence.dao.jpa.EcInfoObjectDaoBean;
import com.electrocomponents.persistence.dao.jpa.EcInfoTextDaoBean;
import com.electrocomponents.persistence.dao.jpa.EcPublicationMappingDaoBean;

/*
 * ************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author  : Sanjay Semwal
 * Created : 3rd sep 2007 at 16:13:00
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
 * InfoObjectServiceBean processes the info message and saves the data in the related tables.
 * @author sanjay semwal
 */
@Stateless(name = InfoObjectService.SERVICE_NAME)
@Local(InfoObjectServiceLocal.class)
@Remote(InfoObjectServiceRemote.class)
public class InfoObjectServiceBean implements InfoObjectServiceLocal, InfoObjectServiceRemote {

    /** Logger. */
    private static final Log LOG = LogFactory.getLog(InfoObjectServiceBean.class);

    /**
     * @param document Document created from XML message
     * @param jndiNameUsed identifies jndiname for a datasource This Method is a service method which takes a Document object as argument
     * retrives all values for InfoMessage entity from the document object and related entities and saves the entities in the database
     * tables
     */
    public final void saveInfoMessageObject(final Document document, final String jndiNameUsed) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start saveInfoMessageObject.");
        }

        String publication = "";
        String infoObjectId = "";
        String parentId = "";
        String parentObjectType = "";
        final EcInfoObjectEntity infoEntity = new EcInfoObjectEntity();
        final EcInfoObjectId infoID = new EcInfoObjectId();

        NodeList list = document.getElementsByTagName("Publication");

        for (int i = 0; i < list.getLength(); i++) {
            final Element element = (Element) list.item(i);
            publication = element.getAttribute("ID");
            infoID.setPublicationName(publication);
        }

        final EcInfoObjectDaoBean infoDaoBean = DaoFactory.getInstance().getDao(EcInfoObjectDaoLocal.class, jndiNameUsed);

        final EcPublicationMappingDaoBean publicationMappingDaoBean = DaoFactory.getInstance().getDao(EcPublicationMappingDaoLocal.class,
                jndiNameUsed);
        final EcInfoTextDaoBean infoTextDaoBean = DaoFactory.getInstance().getDao(EcInfoTextDaoLocal.class, jndiNameUsed);
        boolean flagNoProductTag = false;
        list = document.getElementsByTagName("Product");
        for (int i = 0; i < list.getLength(); i++) {
            final Element element = (Element) list.item(i);
            infoObjectId = element.getAttribute("ID");
            infoID.setInfoObjectId(infoObjectId);
            flagNoProductTag = true;
        }
        if (infoID.getInfoObjectId() == null || infoID.getInfoObjectId().equalsIgnoreCase("")) {
            infoID.setInfoObjectId(" ");
        }

        list = document.getElementsByTagName("ClassificationReference");

        if (list.getLength() == 0) {
            EcInfoObjectEntity ecInfoEntity = (EcInfoObjectEntity) infoDaoBean.findInfoObject(infoObjectId, publication);
            if (ecInfoEntity != null) {
                ecInfoEntity.setLastModifiedTime(new DateTime());
                infoDaoBean.deleteFromTableECPublicationMappingForChild(infoObjectId, publication, "INFO");
                infoDaoBean.deleteFromTableECPublicationMapping(infoObjectId, publication, "INFO");
                infoDaoBean.makePersistent(ecInfoEntity);
                return;
            } else {
                return;
            }

        }

        for (int i = 0; i < list.getLength(); i++) {

            final Element element = (Element) list.item(i);
            parentObjectType = element.getAttribute("UserTypeID");
            infoEntity.setInfoObjectType(parentObjectType);
            parentId = element.getAttribute("ID");
        }
        infoEntity.setCreationTime(new DateTime());
        infoEntity.setLastModifiedTime(new DateTime());
        infoEntity.setId(infoID);
        list = document.getElementsByTagName("Name");
        for (int j = 0; j < list.getLength(); j++) {
            final Element nameElement = (Element) list.item(j);
            if (nameElement.getParentNode().getNodeName().equalsIgnoreCase("product")) {
                infoEntity.setName(nameElement.getTextContent());
            }

        }
        Map<EcPublicationMappingId, EcPublicationMappingEntity> map = new HashMap<EcPublicationMappingId, EcPublicationMappingEntity>();
        if (flagNoProductTag) {

            list = document.getElementsByTagName("AssetCrossReference");

            for (int i = 0; i < list.getLength(); i++) {
                final Element element = (Element) list.item(i);
                final String assetId = element.getAttribute("AssetID");
                final String type = element.getAttribute("Type");
                final EcPublicationMappingEntity ePublicationMappingEntity = new EcPublicationMappingEntity();
                final EcPublicationMappingId publicationMappingIDForAsset = new EcPublicationMappingId();
                publicationMappingIDForAsset.setPublicationName(publication);
                publicationMappingIDForAsset.setChildId(assetId);
                publicationMappingIDForAsset.setParentId(parentId);
                ePublicationMappingEntity.setCreationTime(new DateTime());
                // ePublicationMappingEntity.setLastModifTime(new Date());
                ePublicationMappingEntity.setId(publicationMappingIDForAsset);
                if (type.equalsIgnoreCase("Primary Image")) {
                    ePublicationMappingEntity.setDisplayPriority(NameValueMapping.PRIORITYVALUEFORPRIMARYIMAGE);

                } else if (type.equalsIgnoreCase("Vendor Logo")) {
                    ePublicationMappingEntity.setDisplayPriority(NameValueMapping.PRIORITYVALUEFORVENDORLOGO);

                } else if (type.equalsIgnoreCase("Symbol")) {
                    ePublicationMappingEntity.setDisplayPriority(NameValueMapping.PRIORITYVALUEFORSYMBOL);
                } else if (type.equalsIgnoreCase("Line Drawing")) {
                    ePublicationMappingEntity.setDisplayPriority(NameValueMapping.PRIORITYVALUEFORLINEDRAWING);

                } else if (type.equalsIgnoreCase("Colour Photograph")) {
                    ePublicationMappingEntity.setDisplayPriority(NameValueMapping.PRIORITYVALUEFORCOLORPHOTOGRAPH);

                }
                ePublicationMappingEntity.setMessageType("INFO");
                ePublicationMappingEntity.setType(type);
                map.put(publicationMappingIDForAsset, ePublicationMappingEntity);
            }
            Collection<EcPublicationMappingEntity> collection = map.values();
            final List<EcInfoTextEntity> infoTextEntityList = new ArrayList<EcInfoTextEntity>();
            list = document.getElementsByTagName("RepeatedElement");
            for (int i = 0; i < list.getLength(); i++) {
                final Element element = (Element) list.item(i);
                final NodeList list1 = element.getElementsByTagName("Value");
                for (int j = 0; j < list1.getLength(); j++) {
                    final EcInfoTextId infoID1 = new EcInfoTextId();
                    final Element elementValue = (Element) list1.item(j);
                    final EcInfoTextEntity infoTextEntity = new EcInfoTextEntity();
                    infoID1.setPublicationName(publication);
                    infoID1.setInfoTextId(infoObjectId);
                    infoTextEntity.setCreationTime(new DateTime());
                    infoTextEntity.setLastModifiedTime(new DateTime());
                    infoID1.setDisplayPriority(elementValue.getAttribute("DisplayPriority"));
                    final String infoType = elementValue.getAttribute("Type");
                    infoID1.setInfoObjectType(infoType);
                    final String content = elementValue.getTextContent();
                    infoTextEntity.setContent(content);
                    infoTextEntity.setId(infoID1);
                    infoTextEntityList.add(infoTextEntity);
                }
            }

            list = document.getElementsByTagName("Values");
            for (int i = 0; i < list.getLength(); i++) {
                final Element element = (Element) list.item(i);
                final NodeList valueList = element.getElementsByTagName("Value");
                for (int j = 0; j < valueList.getLength(); j++) {

                    final Element elementValue = (Element) valueList.item(j);
                    if (elementValue.getAttribute("AttributeID").equalsIgnoreCase("Heading")) {
                        infoEntity.setHeadingText(elementValue.getTextContent());
                    } else if (elementValue.getAttribute("AttributeID").equalsIgnoreCase("Intro")) {
                        infoEntity.setIntrotext(elementValue.getTextContent());
                    } else if (elementValue.getAttribute("AttributeID").equalsIgnoreCase("Note")) {
                        infoEntity.setNoteText(elementValue.getTextContent());
                    } else if (elementValue.getAttribute("AttributeID").equalsIgnoreCase("Caution")) {
                        infoEntity.setCaution(elementValue.getTextContent());
                    } else if (elementValue.getAttribute("AttributeID").equalsIgnoreCase("Standards")) {

                        infoEntity.setStandards(elementValue.getTextContent());
                    } else if (elementValue.getAttribute("AttributeID").equalsIgnoreCase("Warning")) {
                        infoEntity.setWarning(elementValue.getTextContent());
                    } else if (elementValue.getAttribute("AttributeID").equalsIgnoreCase("Licensed")) {
                        infoEntity.setLicensed(elementValue.getTextContent());
                    } else if (elementValue.getAttribute("AttributeID").equalsIgnoreCase("Supplied with")) {
                        infoEntity.setSuppliedWith(elementValue.getTextContent());
                    } else if (elementValue.getAttribute("AttributeID").equalsIgnoreCase("Contents")) {
                        infoEntity.setContentText(elementValue.getTextContent());
                    } else if (elementValue.getAttribute("AttributeID").equalsIgnoreCase("Trade Mark")) {
                        infoEntity.setTradeMark(elementValue.getTextContent());
                    } else if (elementValue.getAttribute("AttributeID").equalsIgnoreCase("Approvals")) {
                        infoEntity.setApprovals(elementValue.getTextContent());
                    }
                }
            }

            infoDaoBean.deleteFromTableECPublicationMapping(parentId, publication, "INFO");
            infoDaoBean.deleteFromTableECPublicationMappingForChild(infoObjectId, publication, "INFO");
            infoDaoBean.deleteFromTableECINFOTEXT(infoObjectId, publication);
            EcPublicationMappingEntity publicationMappingEntity = new EcPublicationMappingEntity();
            EcPublicationMappingId publicationMappingID = new EcPublicationMappingId();
            publicationMappingID.setChildId(infoObjectId);
            publicationMappingID.setParentId(parentId);
            publicationMappingID.setPublicationName(publication);
            publicationMappingEntity.setId(publicationMappingID);
            publicationMappingEntity.setType(parentObjectType);
            publicationMappingEntity.setCreationTime(new DateTime());
            publicationMappingEntity.setMessageType("INFO");
            publicationMappingDaoBean.getEntityManager().persist(publicationMappingEntity);

            for (final EcPublicationMappingEntity ePublicationMapping : collection) {
                publicationMappingDaoBean.getEntityManager().persist(ePublicationMapping);
            }

            for (final EcInfoTextEntity ecInfoTextEntity : infoTextEntityList) {

                infoTextDaoBean.getEntityManager().persist(ecInfoTextEntity);

            }
        }
        infoDaoBean.makePersistent(infoEntity);
        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish saveInfoMessageObject.");
        }

    }

}
