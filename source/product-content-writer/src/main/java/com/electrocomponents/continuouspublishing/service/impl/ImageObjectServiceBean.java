package com.electrocomponents.continuouspublishing.service.impl;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.electrocomponents.continuouspublishing.service.interfaces.ImageObjectService;
import com.electrocomponents.continuouspublishing.service.interfaces.ImageObjectServiceLocal;
import com.electrocomponents.continuouspublishing.service.interfaces.ImageObjectServiceRemote;
import com.electrocomponents.model.data.linelevel.EcImageEntity;
import com.electrocomponents.model.data.linelevel.EcImageId;
import com.electrocomponents.model.domain.DateTime;
import com.electrocomponents.persistence.dao.DaoFactory;
import com.electrocomponents.persistence.dao.EcImageDaoLocal;
import com.electrocomponents.persistence.dao.jpa.EcImageDaoBean;

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
 * ImageObjectServiceBean processes the image message and saves the data in the related tables.
 * @author sanjay semwal
 */
@Stateless(name = ImageObjectService.SERVICE_NAME)
@Local(ImageObjectServiceLocal.class)
@Remote(ImageObjectServiceRemote.class)
public class ImageObjectServiceBean implements ImageObjectServiceLocal, ImageObjectServiceRemote {

    /** Logger. */
    private static final Log LOG = LogFactory.getLog(ImageObjectServiceBean.class);

    /**
     * @param document Document created from XML message
     * @param jndiNameUsed identifies jndiname for a datasource This Method is a service method which takes a Document object as argument
     * retrives all values for image entity from the document object and saves the entities in the database table.
     */
    public final void saveImageMessageObject(final Document document, final String jndiNameUsed) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Start saveImageMessageObject.");
        }

        String publication = "";
        final EcImageEntity imageEntity = new EcImageEntity();
        NodeList list = document.getElementsByTagName("Publication");
        EcImageId imageID = new EcImageId();
        for (int i = 0; i < list.getLength(); i++) {
            final Element element = (Element) list.item(i);
            publication = element.getAttribute("ID");
            imageID.setPublicationName(publication);

        }

        final EcImageDaoBean imageDaoBean = DaoFactory.getInstance().getDao(EcImageDaoLocal.class, jndiNameUsed);

        list = document.getElementsByTagName("Asset");

        for (int i = 0; i < list.getLength(); i++) {
            final Element element = (Element) list.item(i);
            final String imageId = element.getAttribute("ID");
            imageID.setImageId(imageId);

        }
        imageEntity.setId(imageID);
        list = document.getElementsByTagName("Values");

        for (int i = 0; i < list.getLength(); i++) {
            final Element element = (Element) list.item(i);
            final NodeList listOfValue = element.getElementsByTagName("Value");
            for (int j = 0; j < listOfValue.getLength(); j++) {
                final Element elementValue = (Element) listOfValue.item(j);
                final String attrId = elementValue.getAttribute("AttributeID");
                if (attrId.equals("asset.extension")) {
                    final String format = elementValue.getTextContent();
                    imageEntity.setImageFormat(format);
                } else if (attrId.equals("asset.pixel-width")) {
                    final String width = elementValue.getTextContent();
                    imageEntity.setImageWidth(new Integer(width));
                } else if (attrId.equals("asset.pixel-height")) {
                    final String height = elementValue.getTextContent();
                    imageEntity.setImageHeight(new Integer(height));
                }
            }
        }
        list = document.getElementsByTagName("Name");

        for (int i = 0; i < list.getLength(); i++) {
            final Element element = (Element) list.item(i);
            final String imageName = element.getTextContent();

            imageEntity.setImageName(imageName);

        }
        imageEntity.setCreationTime(new DateTime());

        imageDaoBean.makePersistent(imageEntity);
        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish saveImageMessageObject.");
        }

    }
}
