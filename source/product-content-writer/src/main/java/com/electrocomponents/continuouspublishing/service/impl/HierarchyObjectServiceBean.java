package com.electrocomponents.continuouspublishing.service.impl;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.electrocomponents.continuouspublishing.service.interfaces.HierarchyObjectService;
import com.electrocomponents.continuouspublishing.service.interfaces.HierarchyObjectServiceLocal;
import com.electrocomponents.continuouspublishing.service.interfaces.HierarchyObjectServiceRemote;
import com.electrocomponents.model.data.linelevel.EcPublicationEntity;
import com.electrocomponents.model.data.linelevel.EcPublicationId;
import com.electrocomponents.model.domain.DateTime;
import com.electrocomponents.persistence.dao.DaoFactory;
import com.electrocomponents.persistence.dao.EcPublicationDaoLocal;
import com.electrocomponents.persistence.dao.jpa.EcPublicationDaoBean;

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
 * HeirarchyObjectServiceBean processes the Heirarchy message and saves the data in the related tables.
 * @author sanjay semwal
 */
@Stateless(name = HierarchyObjectService.SERVICE_NAME)
@Local(HierarchyObjectServiceLocal.class)
@Remote(HierarchyObjectServiceRemote.class)
public class HierarchyObjectServiceBean implements HierarchyObjectServiceRemote, HierarchyObjectServiceLocal {

    /** Logger. */
    private static final Log LOG = LogFactory.getLog(HierarchyObjectServiceBean.class);
    /**
     * Used to save If HierarchyName is Empty or Null .
     * Used in Procedure to sendEmail.  
     */
    private static final String HIERARCHY_NAME = "NO_HIERARCHY_NAME_AVAILABLE";

    /**
     * @param document Document created from XML message
     * @param jndiNameUsed identifies jndiname for a datasource This Method retrives all values for heirarchy entity from the document
     * object and saves the entities in the database tables
     */
    public void saveHeirarchyMessageObject(final Document document, final String jndiNameUsed) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start saveHeirarchyMessageObject.");
        }
        String publication = "";
        int seoCatNameMaxLength = 255;
        int seoMetaDesciptionMaxLength = 200;
        int seoTextMaxLength = 3500;
        int seoPageTitleMaxLength = 100;

        final EcPublicationEntity ecHeirarchyEntity = new EcPublicationEntity();
        final EcPublicationId heirarchyID = new EcPublicationId();
        NodeList list = document.getElementsByTagName("Publication");

        for (int i = 0; i < list.getLength(); i++) {
            final Element element = (Element) list.item(i);
            publication = element.getAttribute("ID");
            heirarchyID.setPublicationName(publication);
        }
        EcPublicationDaoBean heirarchyDaoBean = null;
        list = document.getElementsByTagName("Product");

        heirarchyDaoBean = DaoFactory.getInstance().getDao(EcPublicationDaoLocal.class, jndiNameUsed);
        for (int i = 0; i < list.getLength(); i++) {
            final Element element = (Element) list.item(i);
            final String heirachyid = element.getAttribute("ID");
            if (LOG.isDebugEnabled()) {
                LOG.debug("Processing Heirachy Id = " + heirachyid);
            }
            heirarchyID.setHierarchyID(heirachyid);
            final String parentHierarchyId = element.getAttribute("ParentID");
            if (parentHierarchyId == null || parentHierarchyId.equals("")) {
                EcPublicationEntity heirarchyEntity = (EcPublicationEntity) heirarchyDaoBean.findHeirarchy(heirachyid, publication);
                // LOG.info("heirarchyEntity****" + heirarchyEntity);
                if (heirarchyEntity != null) {
                    heirarchyEntity.setParentHierarchyId(null);
                    heirarchyEntity.setHierarchyName(HIERARCHY_NAME);
                    heirarchyEntity.setLastModifiedTime(new DateTime());
                    heirarchyDaoBean.makePersistent(heirarchyEntity);
                    // LOG.info("heirarchyEntity is not null");
                    return;
                } else {
                    return;
                }

            }
            ecHeirarchyEntity.setParentHierarchyId(parentHierarchyId);
            final String level = element.getAttribute("Level");
            ecHeirarchyEntity.setHierarchyLevel(level);

        }
        ecHeirarchyEntity.setSeoCatName("node_under_construction");

        list = document.getElementsByTagName("AssetCrossReference");

        for (int i = 0; i < list.getLength(); i++) {
            final Element element = (Element) list.item(i);
            final String imageId = element.getAttribute("AssetID");
            ecHeirarchyEntity.setImageId(imageId);

        }
        ecHeirarchyEntity.setShowAttribute('N');
        list = document.getElementsByTagName("ShowAttsOnWeb");

        for (int i = 0; i < list.getLength(); i++) {
            final Element element = (Element) list.item(i);
            final String showAttribute = element.getTextContent();
            if (showAttribute.equalsIgnoreCase("Y")) {
                ecHeirarchyEntity.setShowAttribute('Y');
            } else {
                ecHeirarchyEntity.setShowAttribute('N');
            }

        }

        ecHeirarchyEntity.setComponentChooserFlag('N');
        LOG.debug("CompChooserEntityValue = " + ecHeirarchyEntity.getComponentChooserFlag());
        list = document.getElementsByTagName("ReadyforComponentChooser");

        for (int i = 0; i < list.getLength(); i++) {
            final Element element = (Element) list.item(i);
            final String componentChooser = element.getTextContent();
            if (componentChooser.equalsIgnoreCase("Y")) {
                ecHeirarchyEntity.setComponentChooserFlag('Y');
            } else {
                ecHeirarchyEntity.setComponentChooserFlag('N');
            }

        }

        list = document.getElementsByTagName("Name");
        if (list.getLength() > 0) {
            for (int i = 0; i < list.getLength(); i++) {
                final Element element = (Element) list.item(i);
                final String heirarchyName = element.getTextContent();
                if (heirarchyName != null && !"".equals(heirarchyName)) {
                    ecHeirarchyEntity.setHierarchyName(heirarchyName);
                } else {
                    ecHeirarchyEntity.setHierarchyName(HIERARCHY_NAME);
                }
            }
        } else {
            ecHeirarchyEntity.setHierarchyName(HIERARCHY_NAME);
        }

        list = document.getElementsByTagName("Value");

        for (int i = 0; i < list.getLength(); i++) {
            final Element element = (Element) list.item(i);
            final String attributeId = element.getAttribute("AttributeID");

            if (attributeId.equalsIgnoreCase("Display Priority")) {
                try {
                    ecHeirarchyEntity.setDisplayPriority(new Integer(element.getTextContent()));
                } catch (NumberFormatException e) {
                    ecHeirarchyEntity.setDisplayPriority(null);
                }
            }

            if (attributeId.equalsIgnoreCase("SEO Category Name")) {

                StringBuilder seoCatName = new StringBuilder();
                seoCatName.append(element.getTextContent());
                if (seoCatName.length() == 0 || seoCatName.toString().equals("")) {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("SEO Category Name length was 0 setting default value");
                    }
                    seoCatName.append("node_under_construction");
                } else {
                    seoCatName.replace(0, seoCatName.length(), replaceExpected(seoCatName.toString()));
                    int seoCatNameLength = seoCatName.length();
                    seoCatName.replace(0, seoCatName.length(), stringToCleanString(seoCatName.toString()));
                    if (seoCatName.length() != seoCatNameLength || seoCatNameLength > seoCatNameMaxLength) {
                        if (LOG.isDebugEnabled()) {
                            LOG.debug("Corrected SEO Category Name value != match orignal length or > max length, setting default value. "
                                    + seoCatName);
                        }
                        seoCatName.replace(0, seoCatName.length(), "node_under_construction");
                    }
                }
                ecHeirarchyEntity.setSeoCatName(seoCatName.toString());
            }

            if (attributeId.equalsIgnoreCase("SEO Page Title")) {
                String seoPageTitle = element.getTextContent();
                if (seoPageTitle.length() > seoPageTitleMaxLength) {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("SEO Page Title was greater than " + seoPageTitleMaxLength + ", truncating");
                    }
                    seoPageTitle = seoPageTitle.substring(0, seoPageTitleMaxLength);
                    ecHeirarchyEntity.setSeoPageTitle(seoPageTitle);
                } else {
                    ecHeirarchyEntity.setSeoPageTitle(seoPageTitle);
                }
            }

            if (attributeId.equalsIgnoreCase("SEO Text")) {
                String seoText = element.getTextContent();
                if (seoText.length() > seoTextMaxLength) {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("SEO Text was greater than " + seoTextMaxLength + ", truncating");
                    }
                    seoText = seoText.substring(0, seoTextMaxLength);
                    ecHeirarchyEntity.setSeoText(seoText);
                } else {
                    ecHeirarchyEntity.setSeoText(seoText);
                }
            }

            if (attributeId.equalsIgnoreCase("SEO Metadata Description")) {
                String seoMetaDataDescription = element.getTextContent();
                if (seoMetaDataDescription.length() > seoMetaDesciptionMaxLength) {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("SEO Metadata Description was greater than " + seoMetaDesciptionMaxLength + ", truncating");
                    }
                    seoMetaDataDescription = seoMetaDataDescription.substring(0, seoMetaDesciptionMaxLength);
                    ecHeirarchyEntity.setSeoMetadataDescription(seoMetaDataDescription);
                } else {
                    ecHeirarchyEntity.setSeoMetadataDescription(seoMetaDataDescription);
                }
            }

        }
        ecHeirarchyEntity.setCreationTime(new DateTime());

        ecHeirarchyEntity.setLastModifiedTime(new DateTime());
        ecHeirarchyEntity.setId(heirarchyID);

        heirarchyDaoBean.makePersistent(ecHeirarchyEntity);
        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish saveHeirarchyMessageObject.");
        }
    }

    /**
     * @param string String The String to be cleaned of non standard characters
     * @return String with only standard a-z 0-9 characters in lowercase
     */
    public static String stringToCleanString(final String string) {
        String s = string.replaceAll("[^0-9A-Za-z_\\-\\s]", "");
        s = s.toLowerCase();
        return s;
    }

    /**
     * @param s String The String to be validated for special characters
     * @return String with replaced encoded characters
     */
    public static final String replaceExpected(final String s) {
        StringBuffer sb = new StringBuffer();
        int n = s.length();
        for (int i = 0; i < n; i++) {
            char c = s.charAt(i);
            switch (c) {
            case '/':
                sb.append("_");
                break;
            case '\\':
                sb.append("_");
                break;
            case ' ':
                sb.append("-");
                break;
            default:
                sb.append(c);
                break;
            }
        }
        return sb.toString();
    }
}
