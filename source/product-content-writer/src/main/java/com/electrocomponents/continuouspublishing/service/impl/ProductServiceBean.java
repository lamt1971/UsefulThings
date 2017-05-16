package com.electrocomponents.continuouspublishing.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.annotation.Resource;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.electrocomponents.commons.stringutils.HalfWidthToFullWidthConverter;
import com.electrocomponents.continuouspublishing.service.interfaces.ProductService;
import com.electrocomponents.continuouspublishing.service.interfaces.ProductServiceLocal;
import com.electrocomponents.continuouspublishing.service.interfaces.ProductServiceRemote;
import com.electrocomponents.continuouspublishing.utility.NameValueMapping;
import com.electrocomponents.model.data.linelevel.EcProductAttributesEntity;
import com.electrocomponents.model.data.linelevel.EcProductAttributesId;
import com.electrocomponents.model.data.linelevel.EcProductSearchAttributeEntity;
import com.electrocomponents.model.data.linelevel.EcProductSearchAttributeId;
import com.electrocomponents.model.data.linelevel.EcProductSymbolEntity;
import com.electrocomponents.model.data.linelevel.EcProductSymbolId;
import com.electrocomponents.model.data.linelevel.EcPublicationMappingEntity;
import com.electrocomponents.model.data.linelevel.EcPublicationMappingId;
import com.electrocomponents.model.data.linelevel.EcStorePublicationEntity;
import com.electrocomponents.model.data.product.EcProductEntity;
import com.electrocomponents.model.domain.DateTime;
import com.electrocomponents.model.domain.Locale;
import com.electrocomponents.model.domain.ProductNumber;
import com.electrocomponents.model.domain.Site;
import com.electrocomponents.model.domain.linelevel.EcStorePublication;
import com.electrocomponents.persistence.dao.DaoFactory;
import com.electrocomponents.persistence.dao.EcProductAttributesDao;
import com.electrocomponents.persistence.dao.EcProductSearchAttributeDao;
import com.electrocomponents.persistence.dao.EcProductSymbolDao;
import com.electrocomponents.persistence.dao.EcPublicationMappingDaoLocal;
import com.electrocomponents.persistence.dao.EcStorePublicationDao;
import com.electrocomponents.persistence.dao.jpa.EcProductAttributesDaoBean;
import com.electrocomponents.persistence.dao.jpa.EcProductSearchAttributeDaoBean;
import com.electrocomponents.persistence.dao.jpa.EcProductSymbolDaoBean;
import com.electrocomponents.persistence.dao.jpa.EcPublicationMappingDaoBean;
import com.electrocomponents.persistence.dao.jpa.EcStorePublicationDaoBean;
import com.electrocomponents.persistence.dao.jpa.productcontent.ProductDaoBean;
import com.electrocomponents.persistence.dao.productcontent.ProductDao;
import com.electrocomponents.service.core.client.ServiceLocator;
import com.electrocomponents.service.core.config.interfaces.PropertyService;

/*
 * ************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author  : Sanjay Semwal, Yogesh Patil
 * Created : 23 Aug 2007 at 14:13:00
 *
 * ************************************************************************************************
 * Change History
 * ************************************************************************************************
 * Ref      * Who      * Date       * Description
 * ************************************************************************************************
 *          *Yogesh Patil   *28 Apr 2008    * modified private method fillProductEntity to check for default
 *                                          * status value from ITC_CONTROL.
 *          *               *               *
 *          *Yogesh Patil   *30 Apr 2008    * Added methods:
 *                                          * saveProductMessageObjectWithPPK(Document,String)
 *                                          * saveProductMessageObject(Document,String,EcStorePublicationEntity)
 *                                          * Add product pack variant entries based on has_pack_variant value in
 *                                          * base product.
 * ************************************************************************************************
 */

/**
 * ProductServiceBean processes the product message and saves the data in the related tables.
 * @author sanjay semwal, Yogesh Patil
 */
@Stateless(name = ProductService.SERVICE_NAME)
@Local(ProductServiceLocal.class)
@Remote(ProductServiceRemote.class)
public class ProductServiceBean implements ProductServiceLocal, ProductServiceRemote {

    /** Logger. */
    private static final Log LOG = LogFactory.getLog(ProductServiceBean.class);

    /**
     * STEP_SYSTEM the name of the STEP_SYSTEM of type String used for comparing product data master system owner.
     */
    private static final String STEP_SYSTEM = "STEP";

    /**
     * PRODUCT_DATA_MASTER_PROPERTY the PRODUCT_DATA_MASTER_PROPERTY of type String.
     */
    private static final String LONG_DESCRIPTION_MASTER_SYSTEM = "PRODUCT_DATA_MASTER_SYSTEM";

    /**
     * FULLWIDTHPROPNAME the FULLWIDTHPROPNAME of type String.
     */
    private static final String FULLWIDTHPROPNAME = "CONVERT_TO_FULL_WIDTH";

    /**
     * String containing the separator used in the suffix list to seperate one suffix from another. Can be injected instead of hard-coding
     */
    private static final String PPK_SUFFIX_LIST_SEPERATOR = ",";

    /**
     * CWP_PRODUCT_MESSAGE the CWP_PRODUCT_MESSAGE of type String.
     */
    private static final String CWP_PRODUCT_MESSAGE = "CWP PRODUCT MESSAGE";

    /**
     * @param SEARCH_PRIMARY_KEYWORD_ATTRIBUTE_ID the SEARCH_PRIMARY_KEYWORD of type String.
     */
    private static final String SEARCH_PRIMARY_KEYWORD_ATTRIBUTE_ID = "SEARCH_PRIMARY_KEYWORD";

    /**
     * @param ATTRIBUTE_ID the ATTRIBUTE_ID of type String.
     */
    private static final String ATTRIBUTE_ID = "AttributeID";

    /***************************************************************************************************************************************
     * String containing values, which if matches with the attribute's value then that attribute is not inserted as well as all previous
     * values of such attribute for that product in the specifed catalogue are deleted.
     **************************************************************************************************************************************/
    @Resource(name = "att_value_block")
    private static String attValueBlock;
    
    
    /**
     * @param document Document object created out of XML message
     * @param productEntity product entity object mapped to bv_product This Method is a private service method which takes a Document object
     * and ECProductEntity as argument and populates the product entity with data retrived from the document object created from the product
     * xml message
     */
    private void fillProductEntity(final EcProductEntity productEntity, final Document document) {
        try {
            Locale productLocale = new Locale(productEntity.getSite().getSiteString().toLowerCase());
            PropertyService propertyService = ServiceLocator.locateLocalOrRemote(PropertyService.class, productLocale);

            boolean convertToFullWidth = propertyService.getBooleanProperty(FULLWIDTHPROPNAME, productLocale);

            final NodeList listValues = document.getElementsByTagName("Values");
            for (int j = 0; j < listValues.getLength(); j++) {
                final Element elementForValues = (Element) listValues.item(j);
                final NodeList listForValue = elementForValues.getElementsByTagName("Value");
                for (int k = 0; k < listForValue.getLength(); k++) {
                    final Element elementForValue = (Element) listForValue.item(k);
                    if (elementForValue.getAttribute("AttributeID").equals("PIM Build 100 Character Description") && elementForValue
                            .getTextContent() != null && !"".equals(elementForValue.getTextContent())) {
                        productEntity.setLongDescription(elementForValue.getTextContent());
                        if (convertToFullWidth) {
                            productEntity.setLongDescription(HalfWidthToFullWidthConverter.halfToFullWidthKatakana(productEntity
                                    .getLongDescription()));
                        }
                    }
                }
            }
        } catch (final Exception e) {
            LOG.error("Error while saving product Message in the Database tables", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * This Method is a private method which takes a Document object, publicationName, productId and productSymbolList as argument and
     * populates the productSymbolList with ECProductSymbolEntity's and  ecProductSearchAttributeEntityList with
     * EcProductSearchAttributeEntity's
     * retrieved from the value tags for few of the symbols and SEARCH_PRIMARY_KEYWORD coming in the
     * product xml message.
     * @param productId product number
     * @param publicationName publication name
     * @param document Document object created from xml message
     * @param productSymbolList arraylist of ProductSymbolEntity
     * @param ecProductSearchAttributeEntityList ArrayList of EcProductSearchAttributeEntity
     */
    private void fillListProductSymbolAndSearchAttibEntity(final String productId, final String publicationName, final Document document,
            final List<EcProductSymbolEntity> productSymbolList,
            final List<EcProductSearchAttributeEntity> ecProductSearchAttributeEntityList) {

        try {
            final NodeList listValues = document.getElementsByTagName("Values");
            for (int j = 0; j < listValues.getLength(); j++) {

                final Element elementForValues = (Element) listValues.item(j);
                final NodeList listForValue = elementForValues.getElementsByTagName("Value");
                for (int k = 0; k < listForValue.getLength(); k++) {
                    final Element elementForValue = (Element) listForValue.item(k);
                    if (elementForValue.getAttribute("AttributeID").equals("Design Change Symbol Flag")) {
                        final EcProductSymbolEntity productSymbolEntity = new EcProductSymbolEntity();
                        productSymbolEntity.setSymbolValue(elementForValue.getTextContent());
                        productSymbolEntity.setCreationTime(new DateTime());
                        productSymbolEntity.setLastModifiedTime(new DateTime());
                        final EcProductSymbolId productSymbolID = new EcProductSymbolId();
                        productSymbolID.setSymbolName(elementForValue.getAttribute("AttributeID"));
                        productSymbolID.setProductId(productId);
                        productSymbolID.setPublicationName(publicationName);
                        productSymbolEntity.setId(productSymbolID);
                        productSymbolList.add(productSymbolEntity);
                    } else if (elementForValue.getAttribute("AttributeID").equals("Chemical Substance Symbol Flag")) {
                        final EcProductSymbolEntity productSymbolEntity = new EcProductSymbolEntity();
                        productSymbolEntity.setSymbolValue(elementForValue.getTextContent());
                        productSymbolEntity.setCreationTime(new DateTime());
                        productSymbolEntity.setLastModifiedTime(new DateTime());
                        final EcProductSymbolId productSymbolID = new EcProductSymbolId();
                        productSymbolID.setSymbolName(elementForValue.getAttribute("AttributeID"));
                        productSymbolID.setProductId(productId);
                        productSymbolID.setPublicationName(publicationName);
                        productSymbolEntity.setId(productSymbolID);
                        productSymbolList.add(productSymbolEntity);
                    } else if (elementForValue.getAttribute("AttributeID").equals("Trade Counter Symbol Flag")) {
                        final EcProductSymbolEntity productSymbolEntity = new EcProductSymbolEntity();
                        productSymbolEntity.setSymbolValue(elementForValue.getTextContent());

                        productSymbolEntity.setCreationTime(new DateTime());
                        productSymbolEntity.setLastModifiedTime(new DateTime());
                        final EcProductSymbolId productSymbolID = new EcProductSymbolId();
                        productSymbolID.setProductId(productId);
                        productSymbolID.setSymbolName(elementForValue.getAttribute("AttributeID"));
                        productSymbolID.setPublicationName(publicationName);
                        productSymbolEntity.setId(productSymbolID);
                        productSymbolList.add(productSymbolEntity);
                    } else if (elementForValue.getAttribute("AttributeID").equals("BHN Symbol Flag")) {
                        final EcProductSymbolEntity productSymbolEntity = new EcProductSymbolEntity();
                        productSymbolEntity.setSymbolValue(elementForValue.getTextContent());

                        productSymbolEntity.setCreationTime(new DateTime());
                        productSymbolEntity.setLastModifiedTime(new DateTime());
                        final EcProductSymbolId productSymbolID = new EcProductSymbolId();
                        productSymbolID.setProductId(productId);
                        productSymbolID.setSymbolName(elementForValue.getAttribute("AttributeID"));
                        productSymbolID.setPublicationName(publicationName);
                        productSymbolEntity.setId(productSymbolID);
                        productSymbolList.add(productSymbolEntity);
                    } else if (elementForValue.getAttribute("AttributeID").equals("Licensed Symbol Flag")) {
                        final EcProductSymbolEntity productSymbolEntity = new EcProductSymbolEntity();
                        productSymbolEntity.setSymbolValue(elementForValue.getTextContent());

                        productSymbolEntity.setCreationTime(new DateTime());
                        productSymbolEntity.setLastModifiedTime(new DateTime());
                        final EcProductSymbolId productSymbolID = new EcProductSymbolId();
                        productSymbolID.setProductId(productId);
                        productSymbolID.setSymbolName(elementForValue.getAttribute("AttributeID"));
                        productSymbolID.setPublicationName(publicationName);
                        productSymbolEntity.setId(productSymbolID);
                        productSymbolList.add(productSymbolEntity);

                    } else if (elementForValue.getAttribute("AttributeID").equals("Discontinuation Symbol Flag")) {
                        final EcProductSymbolEntity productSymbolEntity = new EcProductSymbolEntity();
                        productSymbolEntity.setSymbolValue(elementForValue.getTextContent());

                        productSymbolEntity.setCreationTime(new DateTime());
                        productSymbolEntity.setLastModifiedTime(new DateTime());
                        final EcProductSymbolId productSymbolID = new EcProductSymbolId();
                        productSymbolID.setProductId(productId);
                        productSymbolID.setSymbolName(elementForValue.getAttribute("AttributeID"));
                        productSymbolID.setPublicationName(publicationName);
                        productSymbolEntity.setId(productSymbolID);
                        productSymbolList.add(productSymbolEntity);
                    } else if (elementForValue.getAttribute("AttributeID").equals("New Product Symbol Flag")) {
                        final EcProductSymbolEntity productSymbolEntity = new EcProductSymbolEntity();
                        productSymbolEntity.setSymbolValue(elementForValue.getTextContent());

                        productSymbolEntity.setCreationTime(new DateTime());
                        productSymbolEntity.setLastModifiedTime(new DateTime());
                        final EcProductSymbolId productSymbolID = new EcProductSymbolId();
                        productSymbolID.setProductId(productId);
                        productSymbolID.setSymbolName(elementForValue.getAttribute("AttributeID"));
                        productSymbolID.setPublicationName(publicationName);
                        productSymbolEntity.setId(productSymbolID);
                        productSymbolList.add(productSymbolEntity);

                    } else if (elementForValue.getAttribute("AttributeID").equals("Ozone Symbol Flag")) {
                        final EcProductSymbolEntity productSymbolEntity = new EcProductSymbolEntity();
                        productSymbolEntity.setSymbolValue(elementForValue.getTextContent());

                        productSymbolEntity.setCreationTime(new DateTime());
                        productSymbolEntity.setLastModifiedTime(new DateTime());
                        final EcProductSymbolId productSymbolID = new EcProductSymbolId();
                        productSymbolID.setProductId(productId);
                        productSymbolID.setSymbolName(elementForValue.getAttribute("AttributeID"));
                        productSymbolID.setPublicationName(publicationName);
                        productSymbolEntity.setId(productSymbolID);
                        productSymbolList.add(productSymbolEntity);
                        // newProductEntity.setIntroductionDate(introductionDate);
                    } else if (elementForValue.getAttribute("AttributeID").equals("Cailbration Symbol Flag")) {
                        final EcProductSymbolEntity productSymbolEntity = new EcProductSymbolEntity();
                        productSymbolEntity.setSymbolValue(elementForValue.getTextContent());

                        productSymbolEntity.setCreationTime(new DateTime());
                        productSymbolEntity.setLastModifiedTime(new DateTime());
                        final EcProductSymbolId productSymbolID = new EcProductSymbolId();
                        productSymbolID.setProductId(productId);
                        productSymbolID.setSymbolName(elementForValue.getAttribute("AttributeID"));
                        productSymbolID.setPublicationName(publicationName);
                        productSymbolEntity.setId(productSymbolID);
                        productSymbolList.add(productSymbolEntity);
                    } else if (elementForValue.getAttribute("AttributeID").equals("Warranty Flag")) {

                        final EcProductSymbolEntity productSymbolEntity = new EcProductSymbolEntity();
                        productSymbolEntity.setSymbolValue(elementForValue.getTextContent());

                        productSymbolEntity.setCreationTime(new DateTime());
                        productSymbolEntity.setLastModifiedTime(new DateTime());
                        final EcProductSymbolId productSymbolID = new EcProductSymbolId();
                        productSymbolID.setProductId(productId);
                        productSymbolID.setSymbolName(elementForValue.getAttribute("AttributeID"));
                        productSymbolID.setPublicationName(publicationName);
                        productSymbolEntity.setId(productSymbolID);
                        productSymbolList.add(productSymbolEntity);
                    } else if (elementForValue.getAttribute("AttributeID").equals("UK Plug Flag")) {
                        final EcProductSymbolEntity productSymbolEntity = new EcProductSymbolEntity();
                        productSymbolEntity.setSymbolValue(elementForValue.getTextContent());

                        productSymbolEntity.setCreationTime(new DateTime());
                        productSymbolEntity.setLastModifiedTime(new DateTime());
                        final EcProductSymbolId productSymbolID = new EcProductSymbolId();
                        productSymbolID.setProductId(productId);
                        productSymbolID.setSymbolName(elementForValue.getAttribute("AttributeID"));
                        productSymbolID.setPublicationName(publicationName);
                        productSymbolEntity.setId(productSymbolID);
                        productSymbolList.add(productSymbolEntity);
                    } else if (elementForValue.getAttribute(ATTRIBUTE_ID).equals(SEARCH_PRIMARY_KEYWORD_ATTRIBUTE_ID)) {
                        EcProductSearchAttributeEntity ecProductSearchAttributeEntity = new EcProductSearchAttributeEntity();
                        String attributeValue = elementForValue.getTextContent();
                        if (attributeValue != null) {
                            attributeValue = attributeValue.replaceAll("[\n]", "");
                        }

                        final EcProductSearchAttributeId ecProductSearchAttributeId = new EcProductSearchAttributeId();
                        ecProductSearchAttributeId.setProductId(productId);
                        ecProductSearchAttributeId.setPublicationName(publicationName);
                        ecProductSearchAttributeId.setAttributeName(SEARCH_PRIMARY_KEYWORD_ATTRIBUTE_ID);
                        ecProductSearchAttributeEntity.setId(ecProductSearchAttributeId);

                        ecProductSearchAttributeEntity.setAttributeValue(attributeValue);
                        ecProductSearchAttributeEntity.setCreationTime(new DateTime());
                        ecProductSearchAttributeEntity.setLastAmendedBy(CWP_PRODUCT_MESSAGE);
                        ecProductSearchAttributeEntity.setLastAmendedTime(new DateTime());

                        ecProductSearchAttributeEntityList.add(ecProductSearchAttributeEntity);

                    }
                }
            }
        } catch (final Exception e) {
            e.printStackTrace();
            LOG.error("Error while saving product Message in the Database tables");
            throw new RuntimeException();
        }
    }

    /**
     * @param document the message
     * @param jndiNameUsed jndi name of the data source to use The method processes the product attributes, while filtering out the special
     * values as passed in the attValueBlock from ejb-jar.xml file.
     */
    private void processAttributes(final Document document, final String jndiNameUsed) {
        EcProductAttributesDaoBean productAttributeDaoBean = DaoFactory.getInstance().getDao(EcProductAttributesDao.class, jndiNameUsed);
        NodeList list = document.getElementsByTagName("Publication");
        String publication = "";
        String productId = "";
        String heirachyId = "";

        for (int i = 0; i < list.getLength(); i++) {
            final Element element = (Element) list.item(i);
            publication = element.getAttribute("ID");

        }
        list = document.getElementsByTagName("Product");
        for (int i = 0; i < list.getLength(); i++) {
            final Element element = (Element) list.item(i);
            heirachyId = element.getAttribute("ParentID");
            productId = element.getAttribute("ID");

        }

        list = document.getElementsByTagName("ProductSpecificationAttributes");
        for (int i = 0; i < list.getLength(); i++) {
            final Element element = (Element) list.item(i);
            final NodeList list1 = element.getElementsByTagName("Values");
            for (int j = 0; j < list1.getLength(); j++) {
                final Element element1 = (Element) list1.item(j);
                final NodeList list2 = element1.getElementsByTagName("Value");
                for (int k = 0; k < list2.getLength(); k++) {

                    final Element element2 = (Element) list2.item(k);

                    String attributeValue = element2.getTextContent();
                    if (attributeValue != null) {
                        attributeValue = attributeValue.replaceAll("[\n]", "");
                    }
                    final boolean isAttributeValueBlocked = isAttributeValueBlocked(attributeValue);
                    if (!isAttributeValueBlocked) {
                        String attributeId = element2.getAttribute("AttributeID");
                        attributeId = attributeId.replaceAll("[(,)]", "");
                        String attributeName = element2.getAttribute("Name");
                        attributeName = attributeName.replaceAll("[(,)]", "");
                        final String unitName = element2.getAttribute("UnitName");
                        String unitID = element2.getAttribute("UnitID");

                        /** RP - Added code for attribute type * */
                        final String validationBaseType = element2.getAttribute("ValidationBaseType");

                        long displayPriority = 0;
                        if (attributeId.equalsIgnoreCase("NotImportant")) {
                            displayPriority = 9999;
                        } else {
                            displayPriority = new Long(element2.getAttribute("DisplayPriority")).longValue();
                        }

                        final EcProductAttributesEntity eProductAttributeEntity = new EcProductAttributesEntity();
                        final EcProductAttributesId productAttributeID = new EcProductAttributesId();
                        productAttributeID.setHierarchyID(heirachyId);
                        productAttributeID.setProductId(productId);
                        productAttributeID.setPublicationName(publication);

                        productAttributeID.setAttributeId(attributeId);
                        eProductAttributeEntity.setId(productAttributeID);
                        eProductAttributeEntity.setCreationTime(new DateTime());
                        eProductAttributeEntity.setLastModifiedTime(new DateTime());

                        eProductAttributeEntity.setAttributeName(attributeName);
                        eProductAttributeEntity.setDisplayPriority(displayPriority);
                        eProductAttributeEntity.setAttributeValue(attributeValue);
                        eProductAttributeEntity.setUnitName(unitName);
                        eProductAttributeEntity.setUnitId(unitID);
                        eProductAttributeEntity.setAttributeType(validationBaseType);

                        productAttributeDaoBean.getEntityManager().persist(eProductAttributeEntity);
                    }
                }
            }
        }
    }

    /**
     * Is the attribute value blocked.
     * PCR: Code to check for blocked attribute values. TO disable blocked value processing, remove
     * the att_value_block entry in ejb-jar.xml NO OTHER DEPENDECIES ON THIS CODE
     * @param attributeValue The attribute value to check.
     * @return Flag to indicate if the value is blocked
     */
    private boolean isAttributeValueBlocked(final String attributeValue) {
        boolean blocked = false;
        if ((attValueBlock != null) && (attributeValue != null)) {
            StringTokenizer tokenizer = new StringTokenizer(attValueBlock, PPK_SUFFIX_LIST_SEPERATOR);
            while (tokenizer.hasMoreElements()) {
                if (attributeValue.equalsIgnoreCase(tokenizer.nextToken())) {
                    blocked = true;
                    break;
                }
            }
        }
        return blocked;
    }

    /**
     * {@inheritDoc}
     */
    public boolean updateProduct(final Document productMessage, final String jndiName, final Site site) {
        NodeList list = productMessage.getElementsByTagName("Publication");
        String publication = "";
        String productId = "";
        String heirachyId = "";
        boolean isProductFound = false;

        for (int i = 0; i < list.getLength(); i++) {
            final Element element = (Element) list.item(i);
            publication = element.getAttribute("ID");
        }
        list = productMessage.getElementsByTagName("Product");
        for (int i = 0; i < list.getLength(); i++) {
            final Element element = (Element) list.item(i);
            heirachyId = element.getAttribute("ParentID");
            productId = element.getAttribute("ID");

        }

        ProductDaoBean productDaoBean = null;
        EcProductAttributesDaoBean productAttributeDaoBean = null;
        EcProductSymbolDaoBean productSymbolDaoBean = null;
        EcPublicationMappingDaoBean publicationMappingDaoBean = null;
        EcStorePublicationDaoBean publicationStoreDaoBean = null;

        EcProductSearchAttributeDaoBean ecProductSearchAttributeDaoBean = null;
        ecProductSearchAttributeDaoBean = DaoFactory.getInstance().getDao(EcProductSearchAttributeDao.class, jndiName);

        productDaoBean = DaoFactory.getInstance().getDao(ProductDao.class, jndiName);
        productAttributeDaoBean = DaoFactory.getInstance().getDao(EcProductAttributesDao.class, jndiName);
        productSymbolDaoBean = DaoFactory.getInstance().getDao(EcProductSymbolDao.class, jndiName);
        publicationMappingDaoBean = DaoFactory.getInstance().getDao(EcPublicationMappingDaoLocal.class, jndiName);
        publicationStoreDaoBean = DaoFactory.getInstance().getDao(EcStorePublicationDao.class, jndiName);

        final List<EcPublicationMappingEntity> ecMappingEntityList = new ArrayList<EcPublicationMappingEntity>();
        // TODO Need to process product symbol only if the product is already present in the database
        final List<EcProductSymbolEntity> productSymbolList = new ArrayList<EcProductSymbolEntity>();
        final List<EcProductSearchAttributeEntity> ecProductSearchAttributeEntityList = new ArrayList<EcProductSearchAttributeEntity>();
        fillListProductSymbolAndSearchAttibEntity(productId, publication, productMessage, productSymbolList,
                ecProductSearchAttributeEntityList);
        list = productMessage.getElementsByTagName("ClassificationReference");
        processClassificationReferenceMapping(list, ecMappingEntityList, productId, publication);

        list = productMessage.getElementsByTagName("AssetCrossReference");
        processAssetReferenceMapping(list, ecMappingEntityList, productId, publication);

        List<EcStorePublication> storePubList = publicationStoreDaoBean.getEcStorePublicationsByPublicationName(publication);
        if (storePubList.size() > 0) {
            for (int i = 0; i < storePubList.size(); i++) {

                final EcStorePublicationEntity eStorePublicationEntity = (EcStorePublicationEntity) storePubList.get(i);

                final Site publicationSite = eStorePublicationEntity.getId().getSite();

                EcProductEntity productEntity = (EcProductEntity) productDaoBean.findByGroupNumberAndSite(new ProductNumber(productId),
                        publicationSite, false);

                boolean useSTEPValue = true;
                Locale productLocale = new Locale(eStorePublicationEntity.getId().getSite().getSiteString().toLowerCase());

                if (productEntity != null) {
                    isProductFound = true;
                    try {
                        String currentDataMaster = ServiceLocator.locateLocalOrRemote(PropertyService.class, productLocale).getProperty(LONG_DESCRIPTION_MASTER_SYSTEM,
                                productLocale);
                        if (!STEP_SYSTEM.equalsIgnoreCase(currentDataMaster)) {
                            useSTEPValue = false;
                        }
                    } catch (Exception ex) {
                        if (LOG.isErrorEnabled()) {
                            LOG.error("Exception while doing property lookup. Assuming STEP to be data master.", ex);
                        }
                    }
                    // Check for useSTEPvalue here. isProductFound flag still needs to be set as rest of product message
                    // is supposed to get processed.
                    if (useSTEPValue) {
                        updateProductEntity(productEntity);
                        fillProductEntity(productEntity, productMessage);
                        productDaoBean.getEntityManager().merge(productEntity);

                        // If the product has Pack Variant then update the Long Description of production pack product
                        if (productEntity.getHasPackVariants() != null && productEntity.getHasPackVariants()) {
                            EcProductEntity prodPackProductEntity = (EcProductEntity) productDaoBean.findByGroupNumberAndSite(
                                    new ProductNumber(productId + "P"), publicationSite, false);

                            if (prodPackProductEntity != null) {
                                updateProductEntity(prodPackProductEntity);
                                prodPackProductEntity.setLongDescription(productEntity.getLongDescription());
                                productDaoBean.getEntityManager().merge(prodPackProductEntity);

                            } else {
                                LOG.info("Production Pack Product " + productId + "P not found for locale " + publicationSite);
                            }
                        }
                        productDaoBean.getEntityManager().flush();
                    }
                }
            }

            if (isProductFound) {
                productSymbolDaoBean.deleteFromTable(productId, publication);
                for (final EcProductSymbolEntity productSymbolEntity : productSymbolList) {
                    productSymbolDaoBean.getEntityManager().persist(productSymbolEntity);
                }

                ecProductSearchAttributeDaoBean.deleteFromTableEcProductSearchAttribute(productId, publication);
                if (ecProductSearchAttributeEntityList.size() > 0) {
                    ecProductSearchAttributeDaoBean.getEntityManager().persist(ecProductSearchAttributeEntityList.get(0));
                }
                if (ecProductSearchAttributeEntityList.size() > 1) {

                    LOG.warn("Multiple value Tags for SEARCH_PRIMARY_KEYWORD  found in XML for product number : " + "" + productId
                            + " for publication : " + publication);
                }

                productAttributeDaoBean.deleteForTableMessage(productId, heirachyId, publication);
                processAttributes(productMessage, jndiName);
                publicationMappingDaoBean.getEntityManager().flush();
                publicationMappingDaoBean.deleteForProductMessage(productId, publication, "PRODUCT");

                publicationMappingDaoBean.getEntityManager().flush();
                publicationMappingDaoBean.deleteForProductMessageForChildId(productId, publication, "PRODUCT");

                publicationMappingDaoBean.getEntityManager().flush();
                list = productMessage.getElementsByTagName("ProductCrossReference");

                processProductReferenceMapping(list, ecMappingEntityList, productId, publication);
                for (final EcPublicationMappingEntity ePublicationMappingEntity : ecMappingEntityList) {
                    publicationMappingDaoBean.getEntityManager().persist(ePublicationMappingEntity);
                }
            } else {
                LOG.warn("No Record found in EC_Product for product number : " + productId + " for publication : " + publication);
            }
        }
        return isProductFound;
    }

    /**
     * Update Last Modified By and Time of Product Entity.
     * @param productEntity as EcProductEntity
     */
    private void updateProductEntity(final EcProductEntity productEntity) {
        productEntity.setLastModifiedTime(new DateTime(new Date()));
        productEntity.setLastModifiedBy(CWP_PRODUCT_MESSAGE);
    }

    /**
     * This method processes the Asset Reference Mapping for the product.
     * @param list The list of xml elements.
     * @param mappings The publication mapping entities.
     * @param childId The child to use in publication mappings.
     * @param publication The name of the publication.
     */
    private void processAssetReferenceMapping(final NodeList list, final List<EcPublicationMappingEntity> mappings, final String childId,
            final String publication) {
        Map<EcPublicationMappingId, EcPublicationMappingEntity> map = new HashMap<EcPublicationMappingId, EcPublicationMappingEntity>();

        for (int i = 0; i < list.getLength(); i++) {
            final Element element = (Element) list.item(i);

            final String assetId = element.getAttribute("AssetID");
            final String type = element.getAttribute("Type");
            final EcPublicationMappingEntity ePublicationMappingEntity = new EcPublicationMappingEntity();
            final EcPublicationMappingId publicationMappingID = new EcPublicationMappingId();
            publicationMappingID.setPublicationName(publication);
            publicationMappingID.setChildId(assetId);

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

            } else if (type.equalsIgnoreCase("Animated Image")) {
                ePublicationMappingEntity.setDisplayPriority(NameValueMapping.PRIORITYVALUEFORANIMATEDIMAGE);

            }
            publicationMappingID.setParentId(childId);
            ePublicationMappingEntity.setCreationTime(new DateTime());
            ePublicationMappingEntity.setId(publicationMappingID);
            ePublicationMappingEntity.setType(type);
            ePublicationMappingEntity.setMessageType("PRODUCT");
            map.put(publicationMappingID, ePublicationMappingEntity);
        }
        mappings.addAll(map.values());
    }

    /**
     * This method processes the Product Reference Mapping for the product message.
     * @param list THe list of xml elements.
     * @param mappings The publication mapping entities.
     * @param childId The child id to use in publication mapping.
     * @param publication The name of the publication.
     */
    private void processProductReferenceMapping(final NodeList list, final List<EcPublicationMappingEntity> mappings, final String childId,
            final String publication) {
        for (int i = 0; i < list.getLength(); i++) {
            final Element element = (Element) list.item(i);
            final String productCrossReferenceId = element.getAttribute("ProductID");
            final String type = element.getAttribute("Type");

            final EcPublicationMappingEntity ePublicationMappingEntity = new EcPublicationMappingEntity();
            final EcPublicationMappingId publicationMappingID = new EcPublicationMappingId();
            publicationMappingID.setPublicationName(publication);
            publicationMappingID.setChildId(productCrossReferenceId);
            publicationMappingID.setParentId(childId);
            ePublicationMappingEntity.setCreationTime(new DateTime());
            ePublicationMappingEntity.setId(publicationMappingID);
            ePublicationMappingEntity.setType(type);
            ePublicationMappingEntity.setMessageType("PRODUCT");
            mappings.add(ePublicationMappingEntity);
        }
    }

    /**
     * This method processes the Classification Reference Mappings for the product message.
     * @param list The list of xml elements.
     * @param mappings The publication mapping entities.
     * @param childId The child id to use in publication mapping.
     * @param publication The name of the publication.
     */
    private void processClassificationReferenceMapping(final NodeList list, final List<EcPublicationMappingEntity> mappings,
            final String childId, final String publication) {
        for (int i = 0; i < list.getLength(); i++) {
            final Element element = (Element) list.item(i);
            final String classificationID = element.getAttribute("ID");
            final String type = element.getAttribute("UserTypeID");

            final EcPublicationMappingEntity ePublicationMappingEntity = new EcPublicationMappingEntity();
            final EcPublicationMappingId publicationMappingID = new EcPublicationMappingId();
            publicationMappingID.setParentId(classificationID);
            publicationMappingID.setChildId(childId);
            publicationMappingID.setPublicationName(publication);

            ePublicationMappingEntity.setId(publicationMappingID);
            ePublicationMappingEntity.setCreationTime(new DateTime());
            ePublicationMappingEntity.setType(type);
            ePublicationMappingEntity.setMessageType("PRODUCT");
            mappings.add(ePublicationMappingEntity);

        }
    }
}
