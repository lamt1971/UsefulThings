/**
 * Helper class for Generating URL.
 */
package com.electrocomponents.commons.components.general.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.net.URLCodec;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.electrocomponents.model.data.catalogue.navigation.ImageEnum;
import com.electrocomponents.model.domain.searchzeroresult.ZeroResultLinkType;

/**
 * <pre>
 * ************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author  : Sandeep Kumar Jain
 * Created : 18 Mar 2011 at 10:00:09
 * ************************************************************************************************
 * </pre>
 */

/**
 * Helper class for URL Generation.
 * @author Sandeep Kumar Jain
 */
public final class UrlGenerationHelper {

    /** Constant for request parameter cm_mmc. */
    public static final String REQUEST_PARAM_CM_MMC = "cm_mmc";

    /** Constant for request parameter gclid. */
    public static final String REQUEST_PARAM_GCLID = "gclid";

    /** Log for logging errors etc. */
    private static final Log LOG = LogFactory.getLog(UrlGenerationHelper.class);

    /** Base Url for Catalogue. */
    private static final String CATALOGUE_BASE_URL = "/c/";

    /** Base Url for Catalogue. */
    private static final String LINECARD_BASE_URL = "/lc/";

    /** Base Url for Product. */
    private static final String PRODUCT_BASE_URL = "/p/";

    /** Base Url for Compare Products. */
    private static final String COMPARE_PRODUCTS_BASE_URL = "/cp/";

    /** Base Url for Compare Products. */
    private static final String COMPARE_SELECTED_PRODUCTS_BASE_URL = "/cps/";

    /** Base Url for Comapre Accessories. */
    private static final String COMPARE_ACCESSORIES_BASE_URL = "/cpac/";

    /** Base Url for Comapre related product. */
    private static final String COMPARE_RELATED_PRODUCT_BASE_URL = "/cpr/";

    /** Base Url for compare alternatives. */
    private static final String COMPARE_ALTERNATIVES_BASE_URL = "/cpal/";

    /** Base Url for compare discontinued. */
    private static final String COMPARE_DISCONTINUED_BASE_URL = "/cpd/";

    /** Base Url for compare out of stock. */
    private static final String COMPARE_OUT_OF_STOCK_BASE_URL = "/cpo/";

    /** Base Url for Offer. */
    private static final String OFFER_BASE_URL = "/o/";

    /** Base Url for brand shop. */
    private static final String BRAND_SHOP_BASE_URL = "/b/";

    /** Base Url for Our Brands. */
    private static final String OUR_BRANDS_BASE_URL = "/ob/";

    /** Base Url for our products. */
    private static final String OUR_PRODUCTS_BASE_URL = "/op/";

    /** Base Url for zero results. */
    private static final String ZERO_RESULTS_BASE_URL = "/zr/";

    /** Base Url for Shopping Cart. */
    private static final String SHOPPING_CART_BASE_URL = "/sc/";

    /** Base Url for Print Page. */
    private static final String PRINT_BASE_URL = "/pr/";

    /** Base Url for CheckOut. */
    private static final String CHECKOUT_BASE_URL = "/co/";

    /** The CheckOut module url. */
    private static final String CHECKOUT_MODULE_URL = "/co";

    /** The conversation id. */
    private static final String CID = "cid";

    /** Base Url for My Account. */
    private static final String MY_ACCOUNT_BASE_URL = "/ma/";

    /** Base Url for Large Image. */
    private static final String LARGE_IMAGE_BASE_URL = "/largeimages/";

    /** Base Url for Catalogue image. */
    private static final String CATALOGUE_IMAGE_BASE_URL = "/images/";

    /** Base Url for Catalogue image. */
    private static final String CATALOGUE_CAT_IMAGE_BASE_URL = "catimages/";

    /** Base Url for Catalogue image. */
    private static final String CATALOGUE_GENERAL_IMAGE_BASE_URL = "siteImages/general/";

    /** BZERO_SEARCH_RESULT_PARAM for zero results. */
    private static final String ZERO_SEARCH_RESULT_PARAM = "ossc=Zero";

    /** Search term Url parameter. */
    private static final String SEARCH_TERM_URL_PARAM = "searchTerm";

    /** Search type Url parameter. */
    private static final String SEARCH_TYPE_URL_PARAM = "searchType";

    /** Page offset Url parameter. */
    private static final String PAGE_OFFSET_URL_PARAM = "page-offset";

    /** Sort by= Url parameter. */
    private static final String SORT_BY_URL_PARAM = "sort-by";

    /** Applied dimensions Url parameter. */
    private static final String APPLIED_DIMENSIONS_URL_PARAM = "applied-dimensions";

    /** Relevancy Data Param. */
    private static final String RELEVANCY_DATA_PARAM = "relevancy-data";

    /** Applied dimension Id Url parameter. */
    private static final String APPLIED_DIMENSION_ID_URL_PARAM = "applied-dimension-id";

    /** Sort order Url parameter. */
    private static final String SORT_ORDER_URL_PARAM = "sort-order";

    /** View type Url parameter. */
    private static final String VIEW_TYPE_URL_PARAM = "view-type";

    /** View mode Url parameter. */
    private static final String VIEW_MODE = "viewMode";

    /** Sort option Url parameter. */
    private static final String SORT_OPTION_URL_PARAM = "sort-option";

    /** Family Id Url parameter. */
    private static final String PRODUCTS_URL_PARAM = "products";

    /** Locale Url parameter. */
    private static final String LOCALE_URL_PARAM = "locale";

    /** Origin Url parameter. */
    private static final String ORIGIN_URL_PARAM = "origin";

    /** New Products Url parameter. */
    private static final String NEW_PRODUCTS_URL_PARAM = "n";

    /** Lead Time Url parameter. */
    private static final String LEAD_TIME_URL_PARAM = "lt";

    /** Disabled Refinements Url parameter. */
    private static final String DISABLED_REFINEMENTS_URL_PARAM = "dr";

    /** Offers Url parameter. */
    private static final String OFFERS_URL_PARAM = "offerName";

    /** Production Pack Url parameter. */
    private static final String PRODUCTION_PACK_URL_PARAM = "pp";

    /** Footer Feedback Url. */
    private static final String FOOTER_FEEDBACK_URL = "/feedback.html";

    /** About RS Url. */
    private static final String ABOUT_RS_URL = "/generalDisplay.html?id=aboutRS";

    /** GENERAL_DISPLAY_BASE_URL. */
    private static final String GENERAL_DISPLAY_BASE_URL = "/generalDisplay.html?id=";

    /** Footer World Wide Url. */
    private static final String WORLD_WIDE_URL = "http://www.rs-components.com/index.html";

    /** Footer Corporate Group Url. */
    private static final String FOOTER_CORPORATE_GROUP_URL = "http://www.electrocomponents.com";

    /** Footer Conditions Sale Url. */
    private static final String FOOTER_CONDITIONS_SALE_URL = "/generalDisplay.html?id=aboutRS&file=conditions";

    /** Footer Website Terms Url. */
    private static final String FOOTER_WEBSITE_TERMS_URL = "/generalDisplay.html?id=aboutRS&file=terms";

    /** Footer Privacy Policy Url. */
    private static final String FOOTER_PRIVACY_POLICY_URL = "/generalDisplay.html?id=aboutRS&file=privacy";

    /** Footer register Review Data Act Title Url. */
    private static final String FOOTER_REGISTER_REVIEW_DATA_ACT_TITLE_URL = "/generalDisplay.html?id=aboutRS&file=datapolicy";

    /** Footer campaign Url. */
    private static final String FOOTER_CAMPAIGN_URL = "/generalDisplay.html?id=";

    /** Header trade counter Url. */
    private static final String HEADER_TRADE_COUNTERS_URL = "/generalDisplay.html?id=aboutRS&file=tc";

    /** ContactId request Url constant. */
    private static final String CONTACT_ID_REQUEST_URL = "/generalDisplay.html?id=myQuotesForm";

    /** Header my account Url. */
    private static final String HEADER_MY_ACCOUNT_URL = "/myaccount/myaccount.html";

    /** Header services Url. */
    private static final String HEADER_SERVICES_URL = "/generalDisplay.html?id=services";

    /** Header help Url. */
    private static final String HEADER_HELP_URL = "/generalDisplay.html?id=help";

    /** Cookie Policy Campaign Url. */
    private static final String COOKIE_POLICY_CAMPAIGN_URL = "/generalDisplay.html?id=aboutRS&file=cookiePolicy";

    /** Email Policy Campaign Url. */
    private static final String EMAIL_POLICY_CAMPAIGN_URL = "/generalDisplay.html?id=aboutRS/email-security";

    /** Header my Order Url. */
    private static final String HEADER_CART = "/cart/shoppingCart.html";

    /** Return order URL. */
    private static final String RETURN_ORDER = "/orderReturn.html";

    /** request invoice url. */
    private static final String REQUEST_INVOICE = "/myaccount/billingDocument.html";

    /** RS Logo Path Url. */
    private static final String RS_LOGO_PATH = "header/rs-logo_lrg.png";

    /** Logout Url Url. */
    private static final String LOGOUT_URL = "/logout.html";

    /** Label key for our brands. */
    public static final String LBL_KEY_OUR_BRANDS = "Cat_OurBrands";

    /** Label key for our products. */
    public static final String LBL_KEY_OUR_PRODUCTS = "Cat_AllProducts";

    /** Pivot Search Parameter for the terminal node page. */
    private static final String PIVOT_SEARCH_PARAM = "#applied-dimensions=";

    /** last attribute selected Url parameter. */
    private static final String LAST_ATTRIBUTE_SELECTED = "lastAttributeSelectedBlock";

    /** Large images Url. */
    private static final String LARGE_IMAGES_URL = "largeimages/";

    /** Images Url. */
    private static final String IMAGES_URL = "images/";

    /** Images 60 Url. */
    private static final String IMAGES_60_URL = "images60/";

    /** flags Images Url. */
    private static final String FLAGS_IMAGE_URL = "img/site/flags/";

    /** search Images Url. */
    private static final String SEARCH_IMAGE_URL = "img/site/search/";

    /** symbols Images Url. */
    private static final String SYMBOLS_IMAGES_URL = "img/site/symbols/";

    /** browse Images Url. */
    private static final String BROWSE_IMAGES_URL = "siteImages/browse/";

    /** Test Image Url. */
    private static final String TEST_IMAGES_URL = "test-image";

    /** Forward Slash used in URL construction. */
    private static final String FORWARD_SLASH = "/";

    /** Comma used in URL construction. */
    private static final String COMMA = ",";

    /** Ampersand used in URL construction. */
    private static final String AMPERSAND = "&";

    /** Root URL. */
    private static final String ROOT_URL = "/";

    /** EQUALS used in URL construction. */
    private static final String EQUALS = "=";

    /** QUESTION used in URL construction. */
    private static final String QUESTION_MARK = "?";

    /** Vertical bar '|' used in URL construction. */
    private static final String VERTICAL_BAR = "|";

    /** Base PM URL. */
    private static final String PM_URL = "/pm/";

    /** Page Hierarchy Id Url Parameter. */
    private static final String PAGE_HIERARCHY_ID_URL_PARAM = "h";

    /** Store offline URL. */
    private static final String STROE_OFFLINE_URL = "&storeOffline=N";

    /** Localhost URL. */
    private static final String LOCALHOST_URL = "localhost";

    /** Parameter separator /? */
    private static final String PARAM_SEPARATOR = FORWARD_SLASH + QUESTION_MARK;

    /** Brand Parameter. */
    private static final String BRAND_SERACH_TYPE = "Brand";

    /** Catalogue. */
    private static final String CATALOGUE_SERACH_TYPE = "CataloguePage";

    /** Constant for 'local' string. */
    private static final String LOCAL = "local";

    /** Constant for '#' character. */
    private static final String HASH = "#";

    /** Constant for Home URL. */
    private static final String HOME_URL = "/";

    /** Selected tab Url parameter. */
    private static final String LINE_CARD_SELECTED_TAB_URL_PARAM = "selected-tab";

    /** Header help Url. */
    private static final String PARTS_LIST_URL = "/cart/partsList.html?method=createPartsList&partsListFromShoppingCart=Y";

    /** Parts list details Url. */
    private static final String PARTS_LIST_DETAILS_URL = "/cart/partsList.html?method=displayPartsListDetails&partsListId=";

    /** Order option Url. */
    private static final String ORDER_PREFERENCES_URL = "/orderoptions/orderPreferences.html";

    /** My Accou Quotes Url. */
    private static final String MY_QUOTE_URL = "/quote/myQuotes.html";

    /** Authentication component of url. */
    private static final String AUTH_URL_COMPONENT = "/auth/";

    /** My Account url. */
    private static final String MY_ACCOUNT_URL = "ma";

    /** Open Account Url. */
    private static final String OPEN_ACCOUNT_URL = "/generalDisplay.html?id=openAccount";

    /** Redirect relevancy data param. */
    private static final String REDIRECT_RELEVANCY_DATA_PARAM = "redirect-relevancy-data";

    /** Update customer contact details url path. */
    private static final String UPDATE_CONTACT_DETAIL_URL =
            "/registration/registrationPageContactDetailAction.html?method=editUserDetails&mode=edit";

    /** View Quote Url. */
    private static final String QO_VIEW_QUOTE_URL = "/quote/redeemQuotes.html?method=showQuotesRedemptionByQuoteId&ecQuoteId=";

    /** View Product List Url. */
    private static final String QO_VIEW_PRODUCT_LIST_URL = "/mylists/manualQuotes.html?method=showEnquiryCreationPage&enquiryId=";

    /** View Product List Url. */
    private static final String QO_MATCHED_PARTS_URL = "/mylists/quoteOnline.html?method=showQuoteMatchingPageByQuoteId&ecQuoteId=";

    /** Constant for my quotes manual entry page url. */
    private static final String QO_QUOTES_MANUAL_ENTRY_URL = "/quote/manualQuotes.html?method=showEnquiryCreationPage&mode=new";

    /** Constant for my boms manual entry page url. */
    private static final String QO_BOMS_MANUAL_ENTRY_URL = "/mylists/manualQuotes.html?method=showEnquiryCreationPage&mode=new";

    /** Constant for my quotest produt details page url. */
    private static final String QO_QUOTES_PRODUCT_DETAILS_URL = "/quote/bomUpload.html?method=bomFileUploadAction";

    /** Constant for my boms product details page url. */
    private static final String QO_BOMS_PRODUCT_DETAILS_URL = "/mylists/bomUpload.html?method=bomFileUploadAction";

    /** Constant for my bom SCRUB page url. */
    private static final String QO_BOMS_SCRUB_URL = "/mylists/manualQuotes.html?method=scrubEnquiry&enquiryId=";

    /** parcel tracking url. */
    private static final String PARCEL_TRACKING_URL = "/ma/myaccount/trackyourorder/?refNumber=";

    /** Token for anchor. */
    private static final String ANCHOR_TOKEN = "#";

    /** My quotes anchor. */
    private static final String MY_QUOTE_ANCHOR = "mq";

    /** Identifier for TNS page. */
    private static final String PAGE_HIERARCHY_ID_FOR_TNS = "s";

    /** Identifier for TNF page. */
    private static final String PAGE_HIERARCHY_ID_FOR_TNF = "f";

    /** Java Info jar path. */
    private static final String JAVA_INFO_JAR_PATH = "applet/getJavaInfo.jar";

    /** Autocorrection Data Param. */
    private static final String AUTO_CORRECTION_DATA_PARAM = "autocorrected";

    /** Url parameter - to suppress the keyword redirect. */
    private static final String URL_PARAM_TO_SUPPRESS_KEYWORD_REDIRECT = "r=f";

    /** apply for credit account url. */
    private static final String CREDIT_ACCOUNT = "/generalDisplay.html?id=openAccount";

    /** display all parts list url. */
    private static final String DISPLAY_ALL_PART_LISTS = "/cart/partsList.html?method=displayAllPartsLists";

    /** The constant for mapping id - paymentId. */
    public static final String MAPPING_ID_ORDERREFERENCE = "orderPreferencesPageId";

    /** Authentication component of url. */
    private static final String AUTH_URL = "/auth";

    /** Header info zone Url. */
    private static final String HEADER_INFO_ZONE_URL = "/generalDisplay.html?id=infozone";

    /** Header New products linke URL. */
    private static final String HEADER_NEW_PRODUCTS_URL = "/generalDisplay.html?id=new";
  
    /** PDL server request url rsarticle. */
    private static final String PDL_REQUEST_URL_RS_ARTICLE = "rsarticle";

    /** PDL server request url files. */
    private static final String PDL_REQUEST_URL_FILES = "files";

    /** PDL server request url parameter api key. */
    private static final String PDL_REQUEST_URL_PARAMETER_API_KEY = "api_key";

    /** PDL server request url parameter api id. */
    private static final String PDL_REQUEST_URL_PARAMETER_API_ID = "api_id";

    /** The Constant SECURE_URL_PREFIX. Used to compare "/auth/" in the URL. */
    private static final String SECURE_URL_PREFIX = "/auth";

    /**
     * Prevent instances of this class being created.
     */
    private UrlGenerationHelper() {

    }

    /**
     * Method to get CatalogueBaseUrl.
     * @return catalogueBaseUrl String
     */
    public static String getCatalogueBaseUrl() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start/Finish getCatalogueBaseUrl.");
        }
        return CATALOGUE_BASE_URL;
    }

    /**
     * Method to get CatalogueBaseUrl.
     * @param contextPath as String
     * @return catalogueBaseUrl String
     */
    public static String getCatalogueBaseUrl(final String contextPath) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start getCatalogueBaseUrl.");
        }
        StringBuilder catalogueBaseUrlBuilder = new StringBuilder(contextPath);
        catalogueBaseUrlBuilder.append(CATALOGUE_BASE_URL);
        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish getCatalogueBaseUrl.");
        }
        return catalogueBaseUrlBuilder.toString();
    }

    /**
     * Method to generating LineCard base URL.
     * @param contextPath as String
     * @return lineCardBaseUrlBuilder String
     */
    public static String getLineCardBaseUrl(final String contextPath) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start getLineCardBaseUrl.");
        }
        final StringBuilder lineCardBaseUrlBuilder = new StringBuilder(contextPath);
        lineCardBaseUrlBuilder.append(LINECARD_BASE_URL);
        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish getLineCardBaseUrl.");
        }
        return lineCardBaseUrlBuilder.toString();
    }

    /**
     * Method to get CatalogueUrl.
     * @param contextPath as String
     * @param cataloguePath as String
     * @return catalogueUrl String
     */
    public static String getCatalogueUrl(final String contextPath, final String cataloguePath) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start getCatalogueUrl(1).");
        }
        StringBuilder stringBuilder = new StringBuilder();
        if (contextPath != null) {
            stringBuilder.append(contextPath);
        }
        stringBuilder.append(CATALOGUE_BASE_URL);
        if (cataloguePath != null && !"".equals(cataloguePath.trim())) {
            stringBuilder.append(cataloguePath);
            if (!cataloguePath.endsWith(FORWARD_SLASH)) {
                stringBuilder.append(FORWARD_SLASH);
            }
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish getCatalogueUrl(1).");
        }
        return stringBuilder.toString();
    }

    /**
     * Method to get CatalogueUrl for Linecard.
     * @param contextPath as String
     * @param cataloguePath as String
     * @param tabName as String
     * @return catalogueUrl String
     */
    public static String getLineCardCatalogueUrl(final String contextPath, final String cataloguePath, final String tabName) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start getCatalogueUrl(1).");
        }
        StringBuilder stringBuilder = new StringBuilder();
        if (contextPath != null) {
            stringBuilder.append(contextPath);
        }
        stringBuilder.append(LINECARD_BASE_URL);
        if (cataloguePath != null && !"".equals(cataloguePath.trim())) {
            stringBuilder.append(cataloguePath);
            if (!cataloguePath.endsWith(FORWARD_SLASH)) {
                stringBuilder.append(FORWARD_SLASH);
            }
        }
        if (tabName != null) {
            stringBuilder.append(QUESTION_MARK);
            stringBuilder.append(LINE_CARD_SELECTED_TAB_URL_PARAM);
            stringBuilder.append(EQUALS);
            stringBuilder.append(tabName);
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish getCatalogueUrl(1).");
        }
        return stringBuilder.toString();
    }

    /**
     * Method to get CatalogueUrl. Delegates to getCatalogueUrlWithParameters method passing nulls for the extra parameters.
     * @param contextPath as String
     * @param cataloguePath as String
     * @param searchTerm as String
     * @param pageOffset as Integer
     * @return catalogueUrl String
     */
    public static String getCatalogueUrl(final String contextPath, final String cataloguePath, final String searchTerm,
            final Integer pageOffset) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start getCatalogueUrl(2).");
        }
        String catalogueUrl =
                getCatalogueUrlWithParameters(contextPath, cataloguePath, searchTerm, null, pageOffset, null, null, null, null, null, null,
                        null, null, null, null, null);
        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish getCatalogueUrl(2).");
        }
        return catalogueUrl;
    }

    /**
     * Encode search term.
     * @param searchTerm as String
     * @return String containing encoded search term
     */
    private static String encodeSearchTerm(final String searchTerm) {
        String formatSearchTerm = null;
        if (searchTerm != null) {
            try {
                URLCodec urlCodec = new URLCodec();
                formatSearchTerm = (urlCodec.encode(searchTerm));
            } catch (EncoderException e) {
                LOG.warn("searchTerm - problem escaping: " + searchTerm, e);
            }
        }
        return formatSearchTerm;
    }

    /**
     * Method to get catalogueUrlWithParameters.
     * @param contextPath as String
     * @param cataloguePath as String
     * @param searchTerm as String
     * @param appliedDimensionId single appliedDimensionId.
     * @param searchType as String
     * @return catalogueUrlWithParameters String
     */
    public static String getCatalogueUrlWithParameters(final String contextPath, final String cataloguePath, final String searchTerm,
            final String appliedDimensionId, final String searchType) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start getCatalogueUrlWithParameters.");
        }

        List<String> appliedDimensionIds = null;
        if (!StringUtils.isBlank(appliedDimensionId)) {
            appliedDimensionIds = new ArrayList<String>();
            appliedDimensionIds.add(appliedDimensionId);
        }

        String catalogueUrlWithParameters =
                getCatalogueUrlWithParameters(contextPath, cataloguePath, searchTerm, appliedDimensionIds, 0, null, null, null, null, null,
                        searchType, null, null, null, null, null);

        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish getCatalogueUrlWithParameters.");
        }
        return catalogueUrlWithParameters;

    }

    /**
     * Method to get catalogueUrlWithParameters.
     * @param contextPath as String
     * @param cataloguePath as String
     * @param searchTerm as String
     * @param appliedDimensionId single appliedDimensionId.
     * @param searchType as String
     * @param productionPack as String
     * @param offerName as String
     * @param newProducts as String
     * @param leadTime as String
     * @param disabledRefinements as String
     * @return catalogueUrlWithParameters String
     */
    public static String getCatalogueUrlWithParameters(final String contextPath, final String cataloguePath, final String searchTerm,
            final String appliedDimensionId, final String searchType, final String productionPack, final String offerName,
            final String newProducts, final String leadTime, final String disabledRefinements) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start getCatalogueUrlWithParameters.");
        }

        List<String> appliedDimensionIds = null;
        if (!StringUtils.isBlank(appliedDimensionId)) {
            appliedDimensionIds = new ArrayList<String>();
            appliedDimensionIds.add(appliedDimensionId);
        }

        String catalogueUrlWithParameters =
                getCatalogueUrlWithParameters(contextPath, cataloguePath, searchTerm, appliedDimensionIds, 0, null, null, null, null, null,
                        searchType, productionPack, offerName, newProducts, leadTime, disabledRefinements);

        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish getCatalogueUrlWithParameters.");
        }
        return catalogueUrlWithParameters;

    }

    /**
     * Method to get catalogueUrlWithParameters.
     * @param contextPath as String
     * @param cataloguePath as String
     * @param searchTerm as String
     * @param appliedDimensionId single appliedDimensionId.
     * @param searchType as String
     * @return catalogueUrlWithParameters String
     */
    public static String getLineCardCatalogueUrlWithParameters(final String contextPath, final String cataloguePath,
            final String searchTerm, final String appliedDimensionId, final String searchType) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start getCatalogueUrlWithParameters.");
        }

        List<String> appliedDimensionIds = null;
        if (!StringUtils.isBlank(appliedDimensionId)) {
            appliedDimensionIds = new ArrayList<String>();
            appliedDimensionIds.add(appliedDimensionId);
        }

        String catalogueUrlWithParameters =
                getLineCardCatalogueUrlWithParameters(contextPath, cataloguePath, searchTerm, appliedDimensionIds, 0, null, null, null,
                        null, null, searchType);

        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish getCatalogueUrlWithParameters.");
        }
        return catalogueUrlWithParameters;

    }

    /**
     * Method to get catalogueUrlWithParameters.
     * @param contextPath as String
     * @param searchTerm as String
     * @return catalogueUrlWithParameters String
     */
    public static String getCatalogueUrlWithParameters(final String contextPath, final String searchTerm) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start getCatalogueUrlWithParameters.");
        }
        StringBuilder catalogueUrlWithParameters = new StringBuilder();
        catalogueUrlWithParameters.append(contextPath);
        catalogueUrlWithParameters.append(CATALOGUE_BASE_URL);
        if (!StringUtils.isBlank(searchTerm)) {
            catalogueUrlWithParameters.append(QUESTION_MARK).append(SEARCH_TERM_URL_PARAM).append(EQUALS).append(
                    encodeSearchTerm(searchTerm)).append(AMPERSAND);
        }
        catalogueUrlWithParameters.append(SEARCH_TYPE_URL_PARAM);
        catalogueUrlWithParameters.append(EQUALS);
        catalogueUrlWithParameters.append(CATALOGUE_SERACH_TYPE);

        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish getCatalogueUrlWithParameters.");
        }
        return catalogueUrlWithParameters.toString();
    }

    /**
     * Method to get CatalogueUrlWithHierarchy.
     * @param catalogueUrl as String
     * @param isTNS the parameter to identify TNS and TNF(if navigated by clicking brands in SR page).
     * @return CatalogueUrlWithHierarchy String
     */
    public static String getCatalogueUrlWithHierarchy(final String catalogueUrl, final boolean isTNS) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start getCatalogueUrlWithHierarchy.");
        }
        StringBuilder catalogueUrlWithHierarchy = new StringBuilder(catalogueUrl);
        if (isTNS) {
            catalogueUrlWithHierarchy.append(AMPERSAND).append(PAGE_HIERARCHY_ID_URL_PARAM).append(EQUALS)
                    .append(PAGE_HIERARCHY_ID_FOR_TNS);
        } else {
            /*
             * this parameter will be appended only if the page is TNF and navigated by clicking on brands link in SR page.
             */
            catalogueUrlWithHierarchy.append(AMPERSAND).append(PAGE_HIERARCHY_ID_URL_PARAM).append(EQUALS)
                    .append(PAGE_HIERARCHY_ID_FOR_TNF);
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish getCatalogueUrlWithHierarchy.");
        }
        return catalogueUrlWithHierarchy.toString();
    }

    /**
     * Method to get url for brand with context path and search term.
     * @param contextPath as String
     * @param searchTerm as String
     * @return brandUrl as String
     */
    public static String getBrandUrlWithParameters(final String contextPath, final String searchTerm) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start/Finish getBrandUrlWithParameters.");
        }
        return getBrandUrlWithContextAndParentPath(contextPath, null, searchTerm);
    }

    /**
     * Generates catalogue url by appending search parameters to the end of url.
     * @param contextPath as String
     * @param cataloguePath as String
     * @param searchTerm as String
     * @param appliedDimensionIds as List
     * @param pageOffset as Integer
     * @param sortBy as String
     * @param sortOrder as String
     * @param viewMode as String
     * @param sortOption as String
     * @param lastSelected as String
     * @param searchType as String
     * @param productionPack as String
     * @param offerName as String
     * @param newProducts as String
     * @param leadTime as String
     * @param disableRefinements as String
     * @return catalogueUrlWithParameters String
     */
    public static String getCatalogueUrlWithParameters(final String contextPath, final String cataloguePath, final String searchTerm,
            final List<String> appliedDimensionIds, final Integer pageOffset, final String sortBy, final String sortOrder,
            final String viewMode, final String sortOption, final String lastSelected, final String searchType,
            final String productionPack, final String offerName, final String newProducts, final String leadTime,
            final String disableRefinements) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start getCatalogueUrlWithParameters.");
        }

        StringBuilder catalogueUrlStringBuilder = new StringBuilder();
        String catalogueUrl = getCatalogueUrl(contextPath, cataloguePath);
        catalogueUrlStringBuilder.append(catalogueUrl);
        StringBuilder parametersStringBuilder = new StringBuilder();
        if (searchTerm != null && !"".equals(searchTerm.trim())) {
            parametersStringBuilder.append(SEARCH_TERM_URL_PARAM).append(EQUALS).append(encodeSearchTerm(searchTerm)).append(AMPERSAND);
        }
        if (pageOffset != null && pageOffset > 0) {
            parametersStringBuilder.append(PAGE_OFFSET_URL_PARAM).append(EQUALS).append(pageOffset).append(AMPERSAND);
        }
        if (sortBy != null && !"".equals(sortBy.trim())) {
            parametersStringBuilder.append(SORT_BY_URL_PARAM).append(EQUALS).append(encodeSearchTerm(sortBy)).append(AMPERSAND);
        }
        if (sortOrder != null && !"".equals(sortOrder.trim())) {
            parametersStringBuilder.append(SORT_ORDER_URL_PARAM).append(EQUALS).append(sortOrder).append(AMPERSAND);
        }
        if (viewMode != null && !"".equals(viewMode.trim())) {
            parametersStringBuilder.append(VIEW_TYPE_URL_PARAM).append(EQUALS).append(viewMode).append(AMPERSAND);
        }
        if (searchType != null && !"".equals(searchType.trim())) {
            parametersStringBuilder.append(SEARCH_TYPE_URL_PARAM).append(EQUALS).append(searchType).append(AMPERSAND);
        }
        if (appliedDimensionIds != null && !appliedDimensionIds.isEmpty()) {
            parametersStringBuilder.append(APPLIED_DIMENSIONS_URL_PARAM).append(EQUALS);
            for (int i = 0; i < appliedDimensionIds.size(); ++i) {
                parametersStringBuilder.append(appliedDimensionIds.get(i).toString());
                if (i < appliedDimensionIds.size() - 1) {
                    parametersStringBuilder.append(COMMA);
                }
            }
            parametersStringBuilder.append(AMPERSAND);
        }
        if (lastSelected != null && !"".equals(lastSelected.trim())) {
            parametersStringBuilder.append(LAST_ATTRIBUTE_SELECTED).append(EQUALS).append(lastSelected).append(AMPERSAND);
        }
        if (sortOption != null && !"".equals(sortOption.trim())) {
            parametersStringBuilder.append(SORT_OPTION_URL_PARAM).append(EQUALS).append(encodeSearchTerm(sortOption)).append(AMPERSAND);
        }
        if (productionPack != null && !"".equals(productionPack.trim())) {
            parametersStringBuilder.append(PRODUCTION_PACK_URL_PARAM).append(EQUALS).append(productionPack).append(AMPERSAND);
        }
        if (offerName != null && !"".equals(offerName.trim())) {
            parametersStringBuilder.append(OFFERS_URL_PARAM).append(EQUALS).append(offerName).append(AMPERSAND);
        }
        if (newProducts != null && !"".equals(newProducts.trim())) {
            parametersStringBuilder.append(NEW_PRODUCTS_URL_PARAM).append(EQUALS).append(newProducts).append(AMPERSAND);
        }
        if (leadTime != null && !"".equals(leadTime.trim())) {
            parametersStringBuilder.append(LEAD_TIME_URL_PARAM).append(EQUALS).append(leadTime).append(AMPERSAND);
        }
        if (disableRefinements != null && !"".equals(disableRefinements.trim())) {
            parametersStringBuilder.append(DISABLED_REFINEMENTS_URL_PARAM).append(EQUALS).append(disableRefinements).append(AMPERSAND);
        }

        /* Add '?' to the front of parameters and remove the '&' from the end of parameters if urlParameters exist */
        if (parametersStringBuilder.length() > 0) {
            String urlParameters = parametersStringBuilder.substring(0, parametersStringBuilder.length() - 1);
            catalogueUrlStringBuilder.append(QUESTION_MARK).append(urlParameters);
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish getCatalogueUrlWithParameters.");
        }
        return catalogueUrlStringBuilder.toString();
    }

    /**
     * Generates catalogue url by appending search parameters to the end of url.
     * @param contextPath as String
     * @param cataloguePath as String
     * @param searchTerm as String
     * @param appliedDimensionIds as List
     * @param pageOffset as Integer
     * @param sortBy as String
     * @param sortOrder as String
     * @param viewMode as String
     * @param sortOption as String
     * @param lastSelected as String
     * @param searchType as String
     * @return catalogueUrlWithParameters String
     */
    public static String getLineCardCatalogueUrlWithParameters(final String contextPath, final String cataloguePath,
            final String searchTerm, final List<String> appliedDimensionIds, final Integer pageOffset, final String sortBy,
            final String sortOrder, final String viewMode, final String sortOption, final String lastSelected, final String searchType) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start getCatalogueUrlWithParameters.");
        }

        StringBuilder catalogueUrlStringBuilder = new StringBuilder();
        String catalogueUrl = getLineCardCatalogueUrl(contextPath, cataloguePath, null);
        catalogueUrlStringBuilder.append(catalogueUrl);
        StringBuilder parametersStringBuilder = new StringBuilder();
        if (searchTerm != null && !"".equals(searchTerm.trim())) {
            parametersStringBuilder.append(SEARCH_TERM_URL_PARAM).append(EQUALS).append(encodeSearchTerm(searchTerm)).append(AMPERSAND);
        }
        if (pageOffset != null && pageOffset > 0) {
            parametersStringBuilder.append(PAGE_OFFSET_URL_PARAM).append(EQUALS).append(pageOffset).append(AMPERSAND);
        }
        if (sortBy != null && !"".equals(sortBy.trim())) {
            parametersStringBuilder.append(SORT_BY_URL_PARAM).append(EQUALS).append(encodeSearchTerm(sortBy)).append(AMPERSAND);
        }
        if (sortOrder != null && !"".equals(sortOrder.trim())) {
            parametersStringBuilder.append(SORT_ORDER_URL_PARAM).append(EQUALS).append(sortOrder).append(AMPERSAND);
        }
        if (viewMode != null && !"".equals(viewMode.trim())) {
            parametersStringBuilder.append(VIEW_TYPE_URL_PARAM).append(EQUALS).append(viewMode).append(AMPERSAND);
        }
        if (searchType != null && !"".equals(searchType.trim())) {
            parametersStringBuilder.append(SEARCH_TYPE_URL_PARAM).append(EQUALS).append(searchType).append(AMPERSAND);
        }
        if (appliedDimensionIds != null && !appliedDimensionIds.isEmpty()) {
            parametersStringBuilder.append(APPLIED_DIMENSIONS_URL_PARAM).append(EQUALS);
            for (int i = 0; i < appliedDimensionIds.size(); ++i) {
                parametersStringBuilder.append(appliedDimensionIds.get(i).toString());
                if (i < appliedDimensionIds.size() - 1) {
                    parametersStringBuilder.append(COMMA);
                }
            }
            parametersStringBuilder.append(AMPERSAND);
        }
        if (lastSelected != null && !"".equals(lastSelected.trim())) {
            parametersStringBuilder.append(LAST_ATTRIBUTE_SELECTED).append(EQUALS).append(lastSelected).append(AMPERSAND);
        }
        if (sortOption != null && !"".equals(sortOption.trim())) {
            parametersStringBuilder.append(SORT_OPTION_URL_PARAM).append(EQUALS).append(encodeSearchTerm(sortOption)).append(AMPERSAND);
        }

        /* Add '?' to the front of parameters and remove the '&' from the end of parameters if urlParameters exist */
        if (parametersStringBuilder.length() > 0) {
            String urlParameters = parametersStringBuilder.substring(0, parametersStringBuilder.length() - 1);
            catalogueUrlStringBuilder.append(QUESTION_MARK).append(urlParameters);
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish getCatalogueUrlWithParameters.");
        }
        return catalogueUrlStringBuilder.toString();
    }

    /**
     * Thie method constructs the SEO friendly URL for a given dynamic attribute.
     * @param contextPath the conext path
     * @param cataloguePath as String
     * @param appliedDimension as String
     * @param sortBy as String (RELEVANCY,Price LOW-HIGH,HIGH-LOW)
     * @param sortOrder as String (ASC/DES/DEFAULT)
     * @return the seo friendly URL.
     */
    public static String getSeoFriendlyUrlForDynamicAttribute(final String contextPath, final String cataloguePath,
            final String appliedDimension, final String sortBy, final String sortOrder) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start getSeoFriendlyUrlForDynamicAttribute.");
        }
        StringBuilder seoUrl = new StringBuilder();
        seoUrl.append(getCatalogueBaseUrl(contextPath));
        seoUrl.append(cataloguePath);
        seoUrl.append(FORWARD_SLASH);
        seoUrl.append(QUESTION_MARK);
        seoUrl.append(SORT_BY_URL_PARAM);
        seoUrl.append(EQUALS);
        seoUrl.append(sortBy);
        seoUrl.append(AMPERSAND);
        seoUrl.append(SORT_ORDER_URL_PARAM);
        seoUrl.append(EQUALS);
        seoUrl.append(sortOrder);
        seoUrl.append(AMPERSAND);
        seoUrl.append(APPLIED_DIMENSIONS_URL_PARAM);
        seoUrl.append(EQUALS);
        seoUrl.append(appliedDimension);
        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish getSeoFriendlyUrlForDynamicAttribute.");
        }
        return seoUrl.toString();
    }

    /**
     * Method to get productUrl.
     * @param contextPath as String
     * @param family as String
     * @param stockNumber as String
     * @return productUrl String
     */
    public static String getProductUrl(final String contextPath, final String family, final String stockNumber) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start getProductUrl(1).");
        }
        StringBuilder stringBuilder = new StringBuilder();
        if (contextPath != null) {
            stringBuilder.append(contextPath);
        }
        stringBuilder.append(PRODUCT_BASE_URL);
        if (family != null) {
            stringBuilder.append(family);
        } else {
            stringBuilder.append(PRODUCTS_URL_PARAM);
        }
        stringBuilder.append(FORWARD_SLASH);

        if (stockNumber != null && !"".equals(stockNumber.trim())) {
            stringBuilder.append(stockNumber).append(FORWARD_SLASH);
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish getProductUrl(1).");
        }
        return stringBuilder.toString();
    }

    /**
     * Method to get productUrl.
     * @param contextPath as String
     * @param family as String
     * @param stockNumber as String
     * @param searchTerm The search term
     * @return productUrl String
     */
    public static String getProductUrlSearchTerm(final String contextPath, final String family, final String stockNumber,
            final String searchTerm) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start getProductUrl(2).");
        }

        String productUrl = getProductUrl(contextPath, family, stockNumber);

        if (searchTerm != null && searchTerm.length() > 0) {
            if (productUrl.contains(PARAM_SEPARATOR)) {
                productUrl = productUrl + AMPERSAND + SEARCH_TERM_URL_PARAM + EQUALS + encodeSearchTerm(searchTerm);
            } else {
                productUrl = productUrl + QUESTION_MARK + SEARCH_TERM_URL_PARAM + EQUALS + encodeSearchTerm(searchTerm);
            }
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("Start getProductUrl(2).");
        }
        return productUrl;
    }

    /**
     * Method to get productUrl. The productUrl should be "/p/family/stockNumber/?origin=familyId|category value".
     * @param contextPath as String
     * @param family as String
     * @param stockNumber as String
     * @param familyId as String
     * @param category as String
     * @return productUrl String
     */
    public static String getProductUrl(final String contextPath, final String family, final String stockNumber, final String familyId,
            final String category) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start getProductUrl(3).");
        }

        String productUrl = getProductUrl(contextPath, family, stockNumber);

        StringBuilder builder = new StringBuilder(productUrl);
        builder.append(QUESTION_MARK);
        builder.append(ORIGIN_URL_PARAM);
        builder.append(EQUALS);
        builder.append(familyId);
        builder.append(VERTICAL_BAR);
        builder.append(category);

        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish getProductUrl(3).");
        }
        return builder.toString();
    }

    /**
     * Method to get productUrl.
     * @param contextPath as String
     * @param family as String
     * @param stockNumber as String
     * @param viewMode the viewMode
     * @return productUrl String
     */
    public static String getProductUrl(final String contextPath, final String family, final String stockNumber, final String viewMode) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start getProductUrl(4).");
        }
        String originalString = getProductUrl(contextPath, family, stockNumber);

        if (!StringUtils.isBlank(viewMode)) {
            originalString = originalString + QUESTION_MARK + VIEW_MODE + EQUALS + viewMode;
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish getProductUrl(4).");
        }
        return originalString;
    }

    /**
     * Method to get productUrl.
     * @param contextPath as String
     * @param family as String
     * @param stockNumber as String
     * @param searchTerm The search term
     * @param searchTermAlternative The alternative search term
     * @param relevancyData String containing relevancy data
     * @return productUrl String
     */
    public static String getProductUrlIncRelevancyData(final String contextPath, final String family, final String stockNumber,
            final String searchTerm, final String searchTermAlternative, final String relevancyData) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start getProductUrlIncRelevancy.");
        }

        StringBuilder productUrlIncRelevancy = new StringBuilder();
        productUrlIncRelevancy.append(getProductUrlSearchTerm(contextPath, family, stockNumber, searchTerm));

        if (searchTerm != null && searchTermAlternative != null && !searchTerm.trim().equalsIgnoreCase(searchTermAlternative.trim())) {
            productUrlIncRelevancy.append(AMPERSAND).append(AUTO_CORRECTION_DATA_PARAM).append(EQUALS).append("y");
        }

        productUrlIncRelevancy.append(AMPERSAND).append(RELEVANCY_DATA_PARAM).append(EQUALS).append(relevancyData);

        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish getProductUrlIncRelevancy.");
        }
        return productUrlIncRelevancy.toString();
    }

    /**
     * Method to get compareProductsUrl.
     * @param contextPath as String
     * @param stockNumbers as List
     * @return compareProductsUrl String
     */
    public static String getCompareProductsUrl(final String contextPath, final List<String> stockNumbers) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start getCompareProductsUrl.");
        }

        StringBuilder compareProductsUrl = new StringBuilder();
        if (!StringUtils.isBlank(contextPath)) {
            compareProductsUrl.append(contextPath);
        }
        compareProductsUrl.append(COMPARE_PRODUCTS_BASE_URL);
        if (stockNumbers != null && !stockNumbers.isEmpty()) {
            for (int i = 0; i < stockNumbers.size(); ++i) {
                compareProductsUrl.append(stockNumbers.get(i));
                if (i < stockNumbers.size() - 1) {
                    compareProductsUrl.append(COMMA);
                }
            }
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish getCompareProductsUrl.");
        }
        return compareProductsUrl.toString();
    }

    /**
     * Method to get compareAccessoriesUrl.
     * @param contextPath as String
     * @param stockNumber as String
     * @return compareAccessoriesUrl String
     */
    public static String getCompareAccessoriesUrl(final String contextPath, final String stockNumber) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start getCompareAccessoriesUrl.");
        }
        StringBuilder compareAccessoriesUrlBuilder = new StringBuilder(contextPath);
        compareAccessoriesUrlBuilder.append(COMPARE_ACCESSORIES_BASE_URL).append(stockNumber).append(FORWARD_SLASH);

        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish getCompareAccessoriesUrl.");
        }
        return compareAccessoriesUrlBuilder.toString();
    }

    /**
     * Method to get compareAccessoriesUrl.
     * @param contextPath as String
     * @param stockNumber as String
     * @return compareAccessoriesUrl String
     */
    public static String getCompareRelatedProductUrl(final String contextPath, final String stockNumber) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start getCompareRelatedProductUrl.");
        }
        StringBuilder compareAccessoriesUrlBuilder = new StringBuilder(contextPath);
        compareAccessoriesUrlBuilder.append(COMPARE_RELATED_PRODUCT_BASE_URL).append(stockNumber).append(FORWARD_SLASH);
        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish getCompareRelatedProductUrl.");
        }
        return compareAccessoriesUrlBuilder.toString();
    }

    /**
     * Method to get compareAlternativesUrl.
     * @param contextPath as String
     * @param stockNumber as String
     * @return compareAlternativesUrl String
     */
    public static String getCompareAlternativesUrl(final String contextPath, final String stockNumber) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start getCompareAlternativesUrl.");
        }
        StringBuilder compareAlternativesUrlBuilder = new StringBuilder(contextPath);
        compareAlternativesUrlBuilder.append(COMPARE_ALTERNATIVES_BASE_URL).append(stockNumber).append(FORWARD_SLASH);

        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish getCompareAlternativesUrl.");
        }
        return compareAlternativesUrlBuilder.toString();
    }

    /**
     * Method to get compareDiscontinuedUrl with out adding context path and adding search term.
     * @param stockNumber as String
     * @param searchTerm as String
     * @return compareDiscontinuedUrl String
     */
    public static String getCompareDiscontinuedWithRedirectUrl(final String stockNumber, final String searchTerm) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start getCompareDiscontinuedWithRedirectUrl.");
        }
        String compareDiscontinuedUrl = getCompareDiscontinuedUrl(null, stockNumber);
        if (!StringUtils.isBlank(searchTerm)) {
            StringBuilder compareDiscontinuedRedirectUrl = new StringBuilder();
            compareDiscontinuedRedirectUrl.append(compareDiscontinuedUrl);
            compareDiscontinuedRedirectUrl.append(QUESTION_MARK);
            compareDiscontinuedRedirectUrl.append(SEARCH_TERM_URL_PARAM).append(EQUALS).append(encodeSearchTerm(searchTerm));
            compareDiscontinuedUrl = compareDiscontinuedRedirectUrl.toString();
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish getCompareDiscontinuedWithRedirectUrl.");
        }
        return compareDiscontinuedUrl;
    }

    /**
     * Method to get compareDiscontinuedUrl.
     * @param contextPath as String
     * @param stockNumber as String
     * @return compareDiscontinuedUrl String
     */
    public static String getCompareDiscontinuedUrl(final String contextPath, final String stockNumber) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start getCompareDiscontinuedUrl.");
        }
        StringBuilder compareDiscontinuedUrlBuilder = new StringBuilder();
        if (contextPath != null) {
            compareDiscontinuedUrlBuilder.append(contextPath);
        }
        compareDiscontinuedUrlBuilder.append(COMPARE_DISCONTINUED_BASE_URL).append(stockNumber).append(FORWARD_SLASH);
        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish getCompareDiscontinuedUrl.");
        }
        return compareDiscontinuedUrlBuilder.toString();
    }

    /**
     * Method to get compareOutOfStockUrl with out adding context path and adding the search term.
     * @param stockNumber as String
     * @param searchTerm as String
     * @return compareOutOfStockUrl String
     */
    public static String getCompareOutOfStockRedirectUrl(final String stockNumber, final String searchTerm) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start getCompareOutOfStockRedirectUrl.");
        }
        String compareOutOfStockUrl = getCompareOutOfStockUrl(null, stockNumber);
        if (!StringUtils.isBlank(searchTerm)) {
            StringBuilder compareOutOfStockRedirectUrl = new StringBuilder();
            compareOutOfStockRedirectUrl.append(compareOutOfStockUrl);
            compareOutOfStockRedirectUrl.append(QUESTION_MARK);
            compareOutOfStockRedirectUrl.append(SEARCH_TERM_URL_PARAM).append(EQUALS).append(encodeSearchTerm(searchTerm));
            compareOutOfStockUrl = compareOutOfStockRedirectUrl.toString();
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish getCompareOutOfStockRedirectUrl.");
        }
        return compareOutOfStockUrl;
    }

    /**
     * Method to get compareOutOfStockUrl.
     * @param contextPath as String
     * @param stockNumber as String
     * @return compareOutOfStockUrl String
     */
    public static String getCompareOutOfStockUrl(final String contextPath, final String stockNumber) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start getCompareOutOfStockUrl.");
        }
        StringBuilder compareOutOfStockUrlBuilder = new StringBuilder();
        if (contextPath != null) {
            compareOutOfStockUrlBuilder.append(contextPath);
        }
        compareOutOfStockUrlBuilder.append(COMPARE_OUT_OF_STOCK_BASE_URL).append(stockNumber).append(FORWARD_SLASH);

        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish getCompareOutOfStockUrl.");
        }
        return compareOutOfStockUrlBuilder.toString();
    }

    /**
     * Method to get offerUrl.
     * @param locale as String
     * @param offerName as String
     * @return offerUrl as String
     */
    public static String getOfferUrl(final String locale, final String offerName) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start getOfferUrl.");
        }
        String originalString = OFFER_BASE_URL + offerName;

        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish getOfferUrl.");
        }
        return originalString;
    }

    /**
     * Method to get offerUrl.
     * @param contextPath as String
     * @param locale as String
     * @param offerName as String
     * @return offerUrl as String
     */
    public static String getOfferUrl(final String contextPath, final String locale, final String offerName) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start getOfferUrl(1).");
        }
        StringBuilder offerUrlBuilder = new StringBuilder(contextPath);
        offerUrlBuilder.append(OFFER_BASE_URL).append(offerName);

        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish getOfferUrl(1).");
        }
        return offerUrlBuilder.toString();
    }

    /**
     * Method to get brandShopUrl.
     * @param contextPath as String
     * @param brandName as String
     * @return brandShopUrl as String
     */
    public static String getBrandShopUrl(final String contextPath, final String brandName) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start getBrandShopUrl.");
        }
        StringBuilder brandShopUrlBuilder = new StringBuilder();
        brandShopUrlBuilder.append(contextPath).append(BRAND_SHOP_BASE_URL).append(brandName).append(FORWARD_SLASH);

        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish getBrandShopUrl.");
        }
        return brandShopUrlBuilder.toString();
    }

    /**
     * getOurBrandsUrl
     * A method to get the ourBrandsUrl with context path.
     * Calling code needs to do...
     * Locale locale = new Locale(locale);
     * String labelValue = LabelServiceLocator.getLocator().locate(locale).getLabelValue(UrlGenerationHelper.LBL_KEY_OUR_BRANDS, locale);
     * to obtain the required labelValue parameter
     * @param contextPath - can be null
     * @return labelValue
     */
    public static String getOurBrandsUrl(final String contextPath, String labelValue) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start getOurBrandsUrl(2)");
        }
        StringBuilder ourBrandsUrlBuilder = new StringBuilder();
        if (!StringUtils.isBlank(contextPath)) {
            ourBrandsUrlBuilder.append(contextPath);
        }
        ourBrandsUrlBuilder.append(OUR_BRANDS_BASE_URL);

        ourBrandsUrlBuilder.append(labelValue);
        ourBrandsUrlBuilder.append(FORWARD_SLASH);

        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish getOurBrandsUrl(2)");
        }
        return ourBrandsUrlBuilder.toString();
    }

    /**
     * Method to get ourBrandsUrlForLetter.
     * Calling code needs to do...
     * Locale locale = new Locale(locale);
     * String labelValue = LabelServiceLocator.getLocator().locate(locale).getLabelValue(UrlGenerationHelper.LBL_KEY_OUR_BRANDS, locale);
     * to obtain the required labelValue parameter
     * @param contextPath as String
     * @param labelValue as String
     * @param letter as String
     * @param appliedDimensionId dimensionId to be applied.
     * @return ourBrandsUrlForLetter as String
     */
    public static String getOurBrandsUrlForLetter(final String contextPath, final String labelValue, final String letter,
            final String appliedDimensionId) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start getOurBrandsUrlForLetter.");
        }

        /* Replacing '#' with 'local' for the brand url. */
        String letterForUrl = letter;
        if (HASH.equals(letterForUrl)) {
            letterForUrl = LOCAL;
        }
        StringBuilder ourBrandsUrlBuilder = new StringBuilder();
        if (contextPath != null) {
            ourBrandsUrlBuilder.append(contextPath);
        }
        ourBrandsUrlBuilder.append(OUR_BRANDS_BASE_URL);
        ourBrandsUrlBuilder.append(labelValue);
        ourBrandsUrlBuilder.append(FORWARD_SLASH);
        ourBrandsUrlBuilder.append(letterForUrl);
        ourBrandsUrlBuilder.append(FORWARD_SLASH);
        ourBrandsUrlBuilder.append(QUESTION_MARK);
        ourBrandsUrlBuilder.append(APPLIED_DIMENSIONS_URL_PARAM);
        ourBrandsUrlBuilder.append(EQUALS);
        ourBrandsUrlBuilder.append(appliedDimensionId);

        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish getOurBrandsUrlForLetter.");
        }
        return ourBrandsUrlBuilder.toString();
    }

    /**
     * Method to get ourProductsUrl.
     * Calling code needs to do...
     * Locale locale = new Locale(locale);
     * String labelValue = LabelServiceLocator.getLocator().locate(locale).getLabelValue(UrlGenerationHelper.LBL_KEY_OUR_PRODUCTS, locale);
     * to obtain the required labelValue parameter
     * @param contextPath as String
     * @param locale as String
     * @return ourProductsUrl as String
     */
    public static String getOurProductsUrl(final String contextPath, final String labelValue) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start getOurProductsUrl.");
        }
        StringBuilder ourProductsUrlBuilder = new StringBuilder(contextPath);
        ourProductsUrlBuilder.append(OUR_PRODUCTS_BASE_URL);
        ourProductsUrlBuilder.append(labelValue);
        ourProductsUrlBuilder.append(FORWARD_SLASH);

        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish getOurProductsUrl.");
        }
        return ourProductsUrlBuilder.toString();
    }

    /**
     * Method to get getOurProductsBaseUrl.
     * @return ourProductsUrl as String
     */
    public static String getOurProductsBaseUrl() {
        return OUR_PRODUCTS_BASE_URL;
    }

    /**
     * Method to get getOurBrandsBaseUrl.
     * @return OurBrandsBaseUrl as String
     */
    public static String getOurBrandsBaseUrl() {
        return OUR_BRANDS_BASE_URL;
    }

    /**
     * Method to get largeImageUrl.
     * @param imageRoot as String
     * @param imageName as String
     * @return largeImageUrl as String
     */
    public static String getLargeImageUrl(final String imageRoot, final String imageName) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start getLargeImageUrl.");
        }
        String originalString = LARGE_IMAGE_BASE_URL + imageRoot + FORWARD_SLASH + imageName;
        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish getLargeImageUrl.");
        }
        return originalString;
    }

    /**
     * Method to get Line Level Large Image Url.
     * @param imageRoot as String
     * @param imageName as String
     * @return largeImageUrl as String
     */
    public static String getLineLevelLargeImageUrl(final String imageRoot, final String imageName) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start getLineLevelLargeImageUrl.");
        }
        StringBuilder originalString = new StringBuilder(imageRoot).append(LARGE_IMAGE_BASE_URL).append(imageName);
        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish getLineLevelLargeImageUrl.");
        }
        return originalString.toString();
    }

    /**
     * Method to get Line Level Thumbnail Image Url.
     * @param imageRoot as String
     * @param imageName as String
     * @return thumbnailImageUrl as String
     */
    public static String getLineLevelThumbnailImageUrl(final String imageRoot, final String imageName) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start getLineLevelThumbnailImageUrl.");
        }

        StringBuilder imageUrlBuilder = new StringBuilder(imageRoot);
        imageUrlBuilder.append(FORWARD_SLASH);
        imageUrlBuilder.append(CATALOGUE_CAT_IMAGE_BASE_URL);
        imageUrlBuilder.append(imageName);

        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish getLineLevelThumbnailImageUrl.");
        }
        return imageUrlBuilder.toString();
    }

    /**
     * Method to get Line Level Image Url.
     * @param imageRoot as String
     * @param imageName as String
     * @return largeImageUrl as String
     */
    public static String getLineLevelImageUrl(final String imageRoot, final String imageName) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start getLineLevelImageUrl.");
        }
        StringBuilder originalString = new StringBuilder(imageRoot).append(CATALOGUE_IMAGE_BASE_URL).append(imageName);
        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish getLineLevelImageUrl.");
        }
        return originalString.toString();
    }

    /**
     * Method to get catalogueImageUrl.
     * @param imageRoot as String
     * @param imageName as String
     * @return catalogueImageUrl as String
     */
    public static String getCatalogueImageUrl(final String imageRoot, final String imageName) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start getCatalogueImageUrl.");
        }
        String originalString = CATALOGUE_IMAGE_BASE_URL + imageRoot + FORWARD_SLASH + imageName;
        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish getCatalogueImageUrl.");
        }
        return originalString;
    }

    /**
     * Builds up the url to the image.
     * @param imageRoot String containing the root image path
     * @param imageName String containing the name of the product
     * @param imageType ImageEnum contains the type of image i.e large, thumbnail, vfm, general, Search, Symbols, Browse Images.
     * @return String containing the url of the image
     * 
     * 
     * TODO: move this method out of this class so we don't need to ImageEnum.
     */
    public static String getImagePath(final String imageRoot, final String locale, final String imageName, final ImageEnum imageType) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start getImagePath");
        }
        String urlPath = null;
        StringBuilder urlStringBuilder = null;
        if (imageRoot != null && imageName != null) {
            urlStringBuilder = new StringBuilder(imageRoot);
            if (!imageRoot.endsWith(FORWARD_SLASH)) {
                urlStringBuilder.append(FORWARD_SLASH);
            }

            if (imageType.equals(ImageEnum.LARGE_IMAGE)) {
                urlStringBuilder.append(LARGE_IMAGES_URL).append(imageName);
            } else if (imageType.equals(ImageEnum.SIMPLE_IMAGE)) {
                urlStringBuilder.append(IMAGES_URL).append(imageName);
            } else if (imageType.equals(ImageEnum.SIMPLE_IMAGE_60)) {
                urlStringBuilder.append(IMAGES_60_URL).append(imageName);
            } else if (imageType.equals(ImageEnum.VFM_IMAGE)) {
                urlStringBuilder.append(locale).append(FORWARD_SLASH).append(FLAGS_IMAGE_URL).append(imageName);
            } else if (imageType.equals(ImageEnum.THUMB_NAIL_IMAGE)) {
                urlStringBuilder.append(CATALOGUE_CAT_IMAGE_BASE_URL).append(imageName);
            } else if (imageType.equals(ImageEnum.GENERAL_IMAGE)) {
                urlStringBuilder.append(CATALOGUE_GENERAL_IMAGE_BASE_URL).append(imageName);
            } else if (imageType.equals(ImageEnum.SEARCH_IMAGE)) {
                urlStringBuilder.append(locale).append(FORWARD_SLASH).append(SEARCH_IMAGE_URL).append(imageName);
            } else if (imageType.equals(ImageEnum.SYMBOLS_IMAGE)) {
                urlStringBuilder.append(locale).append(FORWARD_SLASH).append(SYMBOLS_IMAGES_URL).append(imageName);
            } else if (imageType.equals(ImageEnum.BROWSE_IMAGE)) {
                urlStringBuilder.append(BROWSE_IMAGES_URL).append(imageName);
            } else {
                urlStringBuilder.append(TEST_IMAGES_URL).append(imageName);
            }
        }
        if (urlStringBuilder != null) {
            urlPath = urlStringBuilder.toString();
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish getImagePath");
        }
        return urlPath;
    }

    /**
     * Method to get siteImageUrl.
     * @param site as String
     * @param imageRoot as String
     * @return siteImageUrl as String
     */
    public static String getSiteImageUrl(final String site, final String imageRoot) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start getSiteImageUrl.");
        }
        String originalString = CATALOGUE_IMAGE_BASE_URL + site + FORWARD_SLASH + imageRoot;
        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish getSiteImageUrl.");
        }
        return originalString;
    }

    /**
     * Method to get loginUrl.
     * @param contextPath as String
     * @param locale as String
     * @return loginUrl as String
     */
    public static String getLoginUrl(final String contextPath, final String locale) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start getLoginUrl.");
        }
        StringBuilder loginUrlBuilder = new StringBuilder();
        loginUrlBuilder.append(contextPath).append("/authHome.html?").append(LOCALE_URL_PARAM).append(EQUALS).append(locale);
        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish getLoginUrl.");
        }
        return loginUrlBuilder.toString();
    }

    /**
     * Method to get zeroResultsUrl.
     * @param searchTerm as String
     * @return zeroResultsUrl as String
     */
    public static String getZeroResultsUrl(final String searchTerm) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start getZeroResultsUrl(1).");
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(ZERO_RESULTS_BASE_URL);
        stringBuilder.append(QUESTION_MARK);
        stringBuilder.append(SEARCH_TERM_URL_PARAM);
        stringBuilder.append(EQUALS);

        String zeroResultsUrl = stringBuilder.append(encodeSearchTerm(searchTerm)).toString();

        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish getZeroResultsUrl(1).");
        }
        return zeroResultsUrl;
    }

    /**
     * Method to get zeroResultsUrl along with the Categeory (Coremetrics) URL parameter.
     * @param searchTerm as String
     * @param addAnalyticsParam as analyticsEnabled flag of session state.
     * @return zeroResultsUrl as String
     */
    public static String getZeroResultsUrl(final String searchTerm, final Boolean addAnalyticsParam) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start getZeroResultsUrl(2).");
        }

        String zeroResultsUrl = getZeroResultsUrl(searchTerm);

        if (addAnalyticsParam != null && addAnalyticsParam) {
            zeroResultsUrl += AMPERSAND + ZERO_SEARCH_RESULT_PARAM;
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish getZeroResultsUrl(2).");
        }
        return zeroResultsUrl;
    }

    /**
     * Method to get zeroResultsUrl along with the Categeory (Coremetrics) URL parameter.
     * @param searchTerm as String
     * @param addAnalyticsParam as analyticsEnabled flag of session state.
     * @param relevancyData String HexEnocded containing relevancy data
     * @return zeroResultsUrl as String
     */
    public static String getZeroResultsUrlIncRelevancyData(final String searchTerm, final Boolean addAnalyticsParam,
            final String relevancyData) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start getZeroResultsUrlIncRelevancyData.");
        }
        StringBuilder zeroResultsUrl = new StringBuilder();
        zeroResultsUrl.append(getZeroResultsUrl(searchTerm, addAnalyticsParam));
        zeroResultsUrl.append(AMPERSAND).append(RELEVANCY_DATA_PARAM).append(EQUALS).append(relevancyData);
        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish getZeroResultsUrlIncRelevancyData.");
        }
        return zeroResultsUrl.toString();
    }

    /**
     * Method to generate link for zero result programme base on link types(Product, Campaign, Brand or Search).
     * @param contextPath the context path
     * @param linkType the link type.
     * @param linkTo the link to.
     * @return link as String
     */
    public static String getZeroResultProgrammeUrl(final String contextPath, final String linkType, final String linkTo) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start getZeroResultLinkToUrl.");
        }

        StringBuilder builder = new StringBuilder();
        if (ZeroResultLinkType.PRODUCT.getLinkType().equalsIgnoreCase(linkType)) {
            builder.append(getProductUrl(contextPath, null, linkTo));
        } else if (ZeroResultLinkType.CAMPAIGN.getLinkType().equalsIgnoreCase(linkType)) {
            builder.append(getFooterCampaignUrl(contextPath, linkTo));
        } else if (ZeroResultLinkType.BRAND.getLinkType().equalsIgnoreCase(linkType)) {
            builder.append(contextPath);
            builder.append(CATALOGUE_BASE_URL);
            builder.append(QUESTION_MARK);
            builder.append(SEARCH_TERM_URL_PARAM);
            builder.append(EQUALS);
            builder.append(linkTo);
            builder.append(AMPERSAND);
            builder.append(SEARCH_TYPE_URL_PARAM);
            builder.append(EQUALS);
            builder.append(BRAND_SERACH_TYPE);
        } else if (ZeroResultLinkType.SEARCH.getLinkType().equalsIgnoreCase(linkType)) {
            builder.append(contextPath);
            builder.append(CATALOGUE_BASE_URL);
            builder.append(QUESTION_MARK);
            builder.append(SEARCH_TERM_URL_PARAM);
            builder.append(EQUALS);
            builder.append(linkTo);
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish getZeroResultLinkToUrl.");
        }
        return builder.toString();
    }

    /**
     * Method to generate link for tech notes links base on link types(Product, Campaign, Brand or Search).
     * @param contextPath the context path
     * @param linkType the link type.
     * @param linkTo the link to.
     * @return link as String
     * 
     * TODO: move this method out of here so we don't need the ZeroResultLinkType enum.
     */
    public static String getUrlForTechNotesLink(final String contextPath, final String linkType, final String linkTo) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start getUrlForTechNotesLink.");
        }
        StringBuilder builder = new StringBuilder();
        if (ZeroResultLinkType.PRODUCT.getLinkType().equalsIgnoreCase(linkType)) {
            builder.append(getProductUrl(contextPath, null, linkTo));
        } else if (ZeroResultLinkType.CAMPAIGN.getLinkType().equalsIgnoreCase(linkType)) {
            builder.append(getFooterCampaignUrl(contextPath, linkTo));
        } else if (ZeroResultLinkType.BRAND.getLinkType().equalsIgnoreCase(linkType)) {
            builder.append(contextPath);
            builder.append(CATALOGUE_BASE_URL);
            builder.append(QUESTION_MARK);
            builder.append(SEARCH_TERM_URL_PARAM);
            builder.append(EQUALS);
            builder.append(linkTo);
            builder.append(AMPERSAND);
            builder.append(SEARCH_TYPE_URL_PARAM);
            builder.append(EQUALS);
            builder.append(BRAND_SERACH_TYPE);
        } else if (ZeroResultLinkType.SEARCH.getLinkType().equalsIgnoreCase(linkType)) {
            builder.append(contextPath);
            builder.append(CATALOGUE_BASE_URL);
            builder.append(QUESTION_MARK);
            builder.append(SEARCH_TERM_URL_PARAM);
            builder.append(EQUALS);
            builder.append(linkTo);
        } else if (ZeroResultLinkType.NULL.getLinkType().equalsIgnoreCase(linkType)) {
            builder.append(contextPath);
            builder.append(linkTo);
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish getUrlForTechNotesLink.");
        }
        return builder.toString();
    }

    /**
     * Method to get ourProductsUrl.
     * @param bookPageDimensionId as String
     * @return ourProductsUrl as String
     */
    public static String getBrowseBookPageUrl(final String bookPageDimensionId) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start getBrowseBookPageUrl.");
        }
        StringBuilder originalString = new StringBuilder();
        originalString.append(CATALOGUE_BASE_URL);
        originalString.append(QUESTION_MARK);
        originalString.append(APPLIED_DIMENSION_ID_URL_PARAM);
        originalString.append(EQUALS);
        originalString.append(bookPageDimensionId);
        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish getBrowseBookPageUrl.");
        }
        return originalString.toString();
    }

    /**
     * Method to get feedbackUrl.
     * @param url as String
     * @param contextPath as String
     * @return feedbackUrl as String
     */
    public static String getFeedbackUrl(final String url, final String contextPath) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start getFeedbackUrl.");
        }
        StringBuilder heardAboutRSUrlBuilder = new StringBuilder(url);
        heardAboutRSUrlBuilder.append(contextPath);
        heardAboutRSUrlBuilder.append(FOOTER_FEEDBACK_URL);
        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish getFeedbackUrl.");
        }
        return heardAboutRSUrlBuilder.toString();
    }

    /**
     * Method to get heardAboutRSUrl.
     * @param contextPath as String
     * @return heardAboutRSUrl as String
     */
    public static String getAboutRSUrl(final String contextPath) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start getAboutRSUrl.");
        }
        StringBuilder heardAboutRSUrlBuilder = new StringBuilder();
        heardAboutRSUrlBuilder.append(contextPath);
        heardAboutRSUrlBuilder.append(ABOUT_RS_URL);
        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish getAboutRSUrl.");
        }
        return heardAboutRSUrlBuilder.toString();
    }

    /**
     * Method to get WorldWideUrl.
     * @param contextPath as String
     * @return WorldWideUrl as String
     */
    public static String getWorldWideUrl(final String contextPath) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start getWorldWideUrl.");
        }
        StringBuilder worldWideUrlBuilder = new StringBuilder();
        worldWideUrlBuilder.append(contextPath);
        worldWideUrlBuilder.append(WORLD_WIDE_URL);
        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish getWorldWideUrl.");
        }
        return worldWideUrlBuilder.toString();
    }

    /**
     * Method to get footerCorporateGroupUrl.
     * @param contextPath as String
     * @return footerCorporateGroupUrl as String
     */
    public static String getFooterCorporateGroupUrl(final String contextPath) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start getFooterCorporateGroupUrl.");
        }
        StringBuilder footerCorporateGroupUrlBuilder = new StringBuilder();
        footerCorporateGroupUrlBuilder.append(contextPath);
        footerCorporateGroupUrlBuilder.append(FOOTER_CORPORATE_GROUP_URL);
        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish getFooterCorporateGroupUrl.");
        }
        return footerCorporateGroupUrlBuilder.toString();
    }

    /**
     * Method to get footerConditionsSaleUrl.
     * @param contextPath as String
     * @return footerConditionsSaleUrl as String
     */
    public static String getFooterConditionsSaleUrl(final String contextPath) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start getFooterConditionsSaleUrl.");
        }
        StringBuilder footerConditionsSaleUrlBuilder = new StringBuilder();
        footerConditionsSaleUrlBuilder.append(contextPath);
        footerConditionsSaleUrlBuilder.append(FOOTER_CONDITIONS_SALE_URL);
        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish getFooterConditionsSaleUrl.");
        }
        return footerConditionsSaleUrlBuilder.toString();
    }

    /**
     * Method to get footerWebsiteTermsUrl.
     * @param contextPath as String
     * @return footerWebsiteTermsUrl as String
     */
    public static String getFooterWebsiteTermsUrl(final String contextPath) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start getFooterWebsiteTermsUrl.");
        }
        StringBuilder footerWebsiteTermsUrlBuilder = new StringBuilder();
        footerWebsiteTermsUrlBuilder.append(contextPath);
        footerWebsiteTermsUrlBuilder.append(FOOTER_WEBSITE_TERMS_URL);
        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish getFooterWebsiteTermsUrl.");
        }
        return footerWebsiteTermsUrlBuilder.toString();
    }

    /**
     * Method to get footerPrivacyPolicyUrl.
     * @param contextPath as String
     * @return footerPrivacyPolicyUrl as String
     */
    public static String getFooterPrivacyPolicyUrl(final String contextPath) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start getFooterPrivacyPolicyUrl.");
        }
        StringBuilder footerPrivacyPolicyUrlBuilder = new StringBuilder();
        footerPrivacyPolicyUrlBuilder.append(contextPath);
        footerPrivacyPolicyUrlBuilder.append(FOOTER_PRIVACY_POLICY_URL);
        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish getFooterPrivacyPolicyUrl.");
        }
        return footerPrivacyPolicyUrlBuilder.toString();
    }

    /**
     * Method to get registerReviewDataActTitleUrl.
     * @param contextPath as String
     * @return registerReviewDataActTitleUrl as String
     */
    public static String getRegisterReviewDataActTitleUrl(final String contextPath) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start getRegisterReviewDataActTitleUrl.");
        }
        StringBuilder registerReviewDataActTitleUrlBuilder = new StringBuilder();
        registerReviewDataActTitleUrlBuilder.append(contextPath);
        registerReviewDataActTitleUrlBuilder.append(FOOTER_REGISTER_REVIEW_DATA_ACT_TITLE_URL);
        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish getRegisterReviewDataActTitleUrl.");
        }
        return registerReviewDataActTitleUrlBuilder.toString();
    }

    /**
     * Method to get Root URL.
     * @param contextPath as String
     * @return ROOT_URL as String
     */
    public static String getRootUrl(final String contextPath) {
        StringBuilder rootUrlBuilder = new StringBuilder(contextPath);
        rootUrlBuilder.append(ROOT_URL);
        return rootUrlBuilder.toString();
    }

    /**
     * Method to get Home URL.
     * @return HOME_URL as String
     */
    public static String getHomeUrl() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start getHomePageUrl.");
        }
        StringBuilder rootUrlBuilder = new StringBuilder(HOME_URL);
        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish getHomePageUrl.");
        }
        return rootUrlBuilder.toString();
    }

    /**
     * Method to get footerCampaignUrl.
     * @param contextPath as String
     * @param id the id
     * @return footerCampaign1Url as String
     */
    public static String getFooterCampaignUrl(final String contextPath, final String id) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start getFooterCampaignUrl.");
        }
        StringBuilder footerCampaign1UrlBuilder = new StringBuilder();
        footerCampaign1UrlBuilder.append(contextPath);
        footerCampaign1UrlBuilder.append(FOOTER_CAMPAIGN_URL).append(id);
        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish getFooterCampaignUrl.");
        }
        return footerCampaign1UrlBuilder.toString();
    }

    /**
     * Method to get CookiePolicyCampaignUrl.
     * @param contextPath the  contextPath
     * @return footerCampaign1Url as String
     */
    public static String getCookiePolicyCampaignUrl(final String contextPath) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start getCookiePolicyCampaignUrl.");
        }
        StringBuilder cookiePolicyCampaignUrlBuilder = new StringBuilder();
        cookiePolicyCampaignUrlBuilder.append(contextPath);
        cookiePolicyCampaignUrlBuilder.append(COOKIE_POLICY_CAMPAIGN_URL);
        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish getCookiePolicyCampaignUrl.");
        }
        return cookiePolicyCampaignUrlBuilder.toString();
    }

    /**
     * Method to get EmailPolicyCampaignUrl.
     * @param contextPath the  contextPath
     * @return footerCampaign1Url as String
     */
    public static String getEmailPolicyCampaignUrl(final String contextPath) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start getEmailPolicyCampaignUrl.");
        }
        StringBuilder emailPolicyCampaignUrlBuilder = new StringBuilder();
        emailPolicyCampaignUrlBuilder.append(contextPath);
        emailPolicyCampaignUrlBuilder.append(EMAIL_POLICY_CAMPAIGN_URL);
        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish getEmailPolicyCampaignUrl.");
        }
        return emailPolicyCampaignUrlBuilder.toString();
    }

    /**
     * Method to get TradeCountersUrl.
     * @param contextPath as String
     * @return tradeCountersUrl as String
     */
    public static String getTradeCountersUrl(final String contextPath) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start getTradeCountersUrl.");
        }
        StringBuilder tradeCountersUrlBuilder = new StringBuilder();
        tradeCountersUrlBuilder.append(contextPath);
        tradeCountersUrlBuilder.append(HEADER_TRADE_COUNTERS_URL);
        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish getTradeCountersUrl.");
        }
        return tradeCountersUrlBuilder.toString();
    }

    /**
     * Method to get ServicesUrl.
     * @param contextPath as String
     * @return servicesUrl as String
     */
    public static String getServicesUrl(final String contextPath) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start getServicesUrl.");
        }
        StringBuilder servicesUrlBuilder = new StringBuilder();
        servicesUrlBuilder.append(contextPath);
        servicesUrlBuilder.append(HEADER_SERVICES_URL);
        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish getServicesUrl.");
        }
        return servicesUrlBuilder.toString();
    }

    /**
     * Method to get HelpUrl.
     * @param contextPath as String
     * @return helpUrl as String
     */
    public static String getHelpUrl(final String contextPath) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start getHelpUrl.");
        }
        StringBuilder helpUrlBuilder = new StringBuilder();
        helpUrlBuilder.append(contextPath).append(HEADER_HELP_URL);
        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish getHelpUrl.");
        }
        return helpUrlBuilder.toString();
    }

    /**
     * Method to get MyOrderUrl.
     * @param contextPath as String
     * @return myOrderUrl as String
     */
    public static String getMyOrderUrl(final String contextPath) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start getMyOrderUrl.");
        }
        StringBuilder myOrderUrlBuilder = new StringBuilder();
        myOrderUrlBuilder.append(contextPath).append(HEADER_CART);
        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish getMyOrderUrl.");
        }
        return myOrderUrlBuilder.toString();
    }

    /**
     * Method to get getMyQuoteUrl.
     * @param contextPath as String
     * @return myQuoteUrlBuilder as String
     */
    public static String getMyQuoteUrl(final String contextPath) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start getMyQuoteUrl.");
        }
        final StringBuilder myQuoteUrlBuilder = new StringBuilder();
        if (!StringUtils.isBlank(contextPath)) {
            myQuoteUrlBuilder.append(contextPath).append(MY_QUOTE_URL);
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish getMyQuoteUrl.");
        }
        return myQuoteUrlBuilder.toString();
    }

    /**
     * Method to get RsLogoPath.
     * @return rsLogoPath as String
     */
    public static String getRsLogoPath() {
        return RS_LOGO_PATH;
    }

    /**
     * Method to get LogoutUrl.
     * @param contextPath The Application source
     * @return logoutUrl as String
     */
    public static String getLogoutUrl(final String contextPath) {
        StringBuilder logoutUrlBuilder = new StringBuilder(contextPath);
        logoutUrlBuilder.append(LOGOUT_URL);
        return logoutUrlBuilder.toString();
    }

    /**
     * Refactored version of getPivotSearchUrl. Use productAncestors.get(productAncestors.size() - 1).getDimensionID()
     * to get the familyNodeDimensionId. If the productAncestors List is null or empty, pass null for the 
     * familtyNodeDimensionId.
     * @param contextPath
     * @param familyNodeDimensionId
     * @return
     */
    public static String getPivotSearchUrlForNodeDimensionId(final String contextPath, Long familyNodeDimensionId) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start getPivotSearchUrlForNodeDimensionId");
        }
        String pivotSearchUrl = null;
        StringBuilder pivotSearchUrlBuilder = null;
        pivotSearchUrlBuilder = new StringBuilder(contextPath);
        pivotSearchUrlBuilder.append(CATALOGUE_BASE_URL);
        if (familyNodeDimensionId != null) {
        	pivotSearchUrlBuilder.append(familyNodeDimensionId).append(FORWARD_SLASH);
        }
        pivotSearchUrlBuilder.append(PIVOT_SEARCH_PARAM);
        pivotSearchUrl = pivotSearchUrlBuilder.toString();

        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish getPivotSearchUrlForNodeDimensionId");
        }
        return pivotSearchUrl;
    }
    
    /**
     * Refactored version of getPivotSearchUrl. The hierarchyNames list should be populated from each productAncestors
     * object's hierarchyName. If the productAncestors list is null or empty, pass null (or an empty List) for hierarchyNames
     * @param contextPath
     * @param hierarchyNames
     * @return
     */
    public static String getPivotSearchUrlForHierarchyNames(final String contextPath, final List<String> hierarchyNames) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start getPivotSearchUrlForHierarchyNames");
        }
        String pivotSearchUrl = null;
        StringBuilder pivotSearchUrlBuilder = null;
        pivotSearchUrlBuilder = new StringBuilder(contextPath);
        pivotSearchUrlBuilder.append(CATALOGUE_BASE_URL);
        if (hierarchyNames != null) {
	        for (String hierarchyName : hierarchyNames) {
	        	pivotSearchUrlBuilder.append(hierarchyName).append(FORWARD_SLASH);
	        }
        }
        pivotSearchUrlBuilder.append(PIVOT_SEARCH_PARAM);
        pivotSearchUrl = pivotSearchUrlBuilder.toString();

        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish getPivotSearchUrlForHierarchyNames");
        }
        return pivotSearchUrl;
    }
    

    /**
     * This method returns true if the passed url contains any of the NCJ URL Pattern.
     * @param loginUrl loginUrl
     * @return boolean isNCJUrl
     */
    public static boolean isNCJUrl(final String loginUrl) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start isNCJUrl");
        }
        boolean isNCJUrl = false;
        if (loginUrl != null
                && (loginUrl.contains(CATALOGUE_BASE_URL) || loginUrl.contains(PRODUCT_BASE_URL)
                        || loginUrl.contains(COMPARE_PRODUCTS_BASE_URL) || loginUrl.contains(COMPARE_ACCESSORIES_BASE_URL)
                        || loginUrl.contains(COMPARE_ALTERNATIVES_BASE_URL) || loginUrl.contains(COMPARE_DISCONTINUED_BASE_URL)
                        || loginUrl.contains(COMPARE_OUT_OF_STOCK_BASE_URL) || loginUrl.contains(OFFER_BASE_URL)
                        || loginUrl.contains(BRAND_SHOP_BASE_URL) || loginUrl.contains(OUR_BRANDS_BASE_URL)
                        || loginUrl.contains(OUR_PRODUCTS_BASE_URL) || loginUrl.contains(ZERO_RESULTS_BASE_URL) || loginUrl
                            .contains(COMPARE_RELATED_PRODUCT_BASE_URL))) {
            isNCJUrl = true;
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish isNCJUrl");
        }
        return isNCJUrl;

    }

    /**
     * Method to get SearchTermUrl.
     * @param searchTerm The Search Term
     * @return searchTermUrl as String
     */
    public static String getSearchTermUrl(final String searchTerm) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start getSearchTermUrl.");
        }
        String searchTermUrl = "";
        if (!StringUtils.isBlank(searchTerm)) {
            StringBuilder urlBuilder = new StringBuilder();
            urlBuilder.append("/");
            urlBuilder.append("?");
            urlBuilder.append(SEARCH_TERM_URL_PARAM);
            urlBuilder.append("=");
            urlBuilder.append(encodeSearchTerm(searchTerm));
            searchTermUrl = urlBuilder.toString();

        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish getSearchTermUrl.");
        }
        return searchTermUrl;
    }

    /**
     * Method to generate the generalDisplay campaign Url for a given campaignId.
     * @param contextPath contextPath(web) as string.
     * @param campaignId campaignId.
     * @return generalDisplayUrl as String
     */
    public static String getGeneralDisplayUrl(final String contextPath, final String campaignId) {
        StringBuilder generalDisplayUrlBuilder = new StringBuilder(contextPath);
        generalDisplayUrlBuilder.append(GENERAL_DISPLAY_BASE_URL);
        generalDisplayUrlBuilder.append(campaignId);
        return generalDisplayUrlBuilder.toString();
    }

    /**
     * @return PM_URL PM_URL.
     */
    public static String getPMBaseUrl() {
        return PM_URL;
    }

    /**
     * @return STROE_OFFLINE_URL STROE_OFFLINE_URL.
     */
    public static String getStoreOfflineUrl() {
        return STROE_OFFLINE_URL;
    }

    /**
     * @return LOCALHOST_URL LOCALHOST_URL.
     */
    public static String getLocalhostUrl() {
        return LOCALHOST_URL;
    }

    /**
     * @return LOCALHOST_URL LOCALHOST_URL.
     * @param String locale string - String.valueOf(locale) or locale.toString()
     */
    public static String getLocaleParameterUrl(final String locale) {
        return QUESTION_MARK + LOCALE_URL_PARAM + EQUALS + locale;
    }

    /**
     * Method to get url for brand with context path and brand name.
     * @param brandName as String
     * @param contextPath as String
     * @return brandUrl as String
     */
    public static String getBrandUrlWithContextPath(final String contextPath, final String brandName) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start/Finish getBrandUrlWithContextPath.");
        }
        return getBrandUrlWithContextAndParentPath(contextPath, null, brandName);
    }

    /**
     * Method to get url for brand with parent, context path and brand name.
     * @param parentPath as String
     * @param brandName as String
     * @param contextPath as String
     * @return brandUrl as String
     */
    public static String getBrandUrlWithContextAndParentPath(final String contextPath, final String parentPath, final String brandName) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start getBrandUrlWithContextAndParentPath.");
        }
        StringBuilder brandUrl = new StringBuilder();
        if (!StringUtils.isBlank(contextPath)) {
            brandUrl.append(contextPath);
        }
        brandUrl.append(CATALOGUE_BASE_URL);

        if (!StringUtils.isBlank(parentPath)) {
            brandUrl.append(parentPath);
        }
        if (!StringUtils.isBlank(brandName)) {
            brandUrl.append(QUESTION_MARK).append(SEARCH_TERM_URL_PARAM).append(EQUALS).append(encodeSearchTerm(brandName)).append(
                    AMPERSAND);
        }
        brandUrl.append(SEARCH_TYPE_URL_PARAM);
        brandUrl.append(EQUALS);
        brandUrl.append(BRAND_SERACH_TYPE);

        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish getBrandUrlWithContextAndParentPath.");
        }
        return brandUrl.toString();
    }

    /**
     * Method to get productUrl.
     * @param contextPath as String
     * @param stockNumber as String
     * @param cmMmc the cmMmc
     * @param gclid the gclid
     * @return productUrl String
     */
    public static String getProductUrlWithAnalyticsParams(final String contextPath, final String stockNumber, final String cmMmc,
            final String gclid) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start getProductUrlWithAnalyticsParams.");
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getProductUrl(contextPath, null, stockNumber));

        if (!StringUtils.isBlank(cmMmc) || !StringUtils.isBlank(gclid)) {
            stringBuilder.append(getRequestParametersString(cmMmc, gclid, QUESTION_MARK));
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish getProductUrlWithAnalyticsParams.");
        }
        return stringBuilder.toString();
    }

    /**
     * Method to get productUrl.
     * @param contextPath as String
     * @param searchTerm as searchTerm
     * @param cmMmc the cmMmc
     * @param gclid the gclid
     * @return productUrl String
     */
    public static String getSearchProductUrlWithAnalyticsParams(final String contextPath, final String searchTerm, final String cmMmc,
            final String gclid) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start getSearchProductUrlWithAnalyticsParams.");
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(UrlGenerationHelper.getCatalogueBaseUrl(contextPath));
        if (stringBuilder.toString().endsWith(FORWARD_SLASH)) {
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        }
        stringBuilder.append(UrlGenerationHelper.getSearchTermUrl(searchTerm));
        if (!StringUtils.isBlank(cmMmc) || !StringUtils.isBlank(gclid)) {
            stringBuilder.append(getRequestParametersString(cmMmc, gclid, AMPERSAND));
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish getSearchProductUrlWithAnalyticsParams.");
        }
        return stringBuilder.toString();
    }

    /**
     * Method to get compareAlternativesUrl.
     * @param contextPath as String
     * @param stockNumber as String
     * @param cmMmc the cmMmc
     * @param gclid the gclid
     * @return compareAlternativesUrl String
     */
    public static String getCompareAlternativesUrlWithAnalyticsParams(final String contextPath, final String stockNumber,
            final String cmMmc, final String gclid) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start getCompareAlternativesUrlWithAnalyticsParams.");
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(UrlGenerationHelper.getCompareAlternativesUrl(contextPath, stockNumber));
        stringBuilder.append(FORWARD_SLASH);
        if (!StringUtils.isBlank(cmMmc) || !StringUtils.isBlank(gclid)) {
            stringBuilder.append(getRequestParametersString(cmMmc, gclid, QUESTION_MARK));
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish getCompareAlternativesUrlWithAnalyticsParams.");
        }
        return stringBuilder.toString();
    }

    /**
     * Method to get compareAlternativesUrl.
     * @param originalUrl as originalUrl
     * @param cmMmc the cmMmc
     * @param gclid the gclid
     * @return compareAlternativesUrl String
     */
    public static String appendAnalyticsParamsToUrl(final String originalUrl, final String cmMmc, final String gclid) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start appendAnalyticsParamsToUrl.");
        }
        StringBuilder stringBuilder = new StringBuilder(originalUrl);
        if (!StringUtils.isBlank(cmMmc) || !StringUtils.isBlank(gclid)) {
            stringBuilder.append(getRequestParametersString(cmMmc, gclid, AMPERSAND));
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish appendAnalyticsParamsToUrl.");
        }
        return stringBuilder.toString();
    }

    /**
     * @param cmMmc the cmMmc
     * @param gclid the gclid
     * @param startCharacter the startCharacter
     * @return request parameter url
     */
    private static String getRequestParametersString(final String cmMmc, final String gclid, final String startCharacter) {
        StringBuilder requestParametersString = new StringBuilder(startCharacter);
        if (!StringUtils.isBlank(cmMmc)) {
            requestParametersString.append(REQUEST_PARAM_CM_MMC).append(EQUALS).append(cmMmc).append(AMPERSAND);
        }
        if (!StringUtils.isBlank(gclid)) {
            requestParametersString.append(REQUEST_PARAM_GCLID).append(EQUALS).append(gclid);
        }
        if (requestParametersString.toString().endsWith(QUESTION_MARK) || requestParametersString.toString().endsWith(AMPERSAND)) {
            requestParametersString.deleteCharAt(requestParametersString.length() - 1);
        }
        return requestParametersString.toString();
    }

    /**
     * Method to get Print page url.
     * @param contextPath as String
     * @return ShoppingCartUrl as String
     */
    public static String getPrinUrl(final String contextPath) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start getPrinUrl(1).");
        }
        StringBuilder shoppingCartUrlBuilder = new StringBuilder();
        if (!StringUtils.isBlank(contextPath)) {
            shoppingCartUrlBuilder.append(contextPath);
        }
        shoppingCartUrlBuilder.append(PRINT_BASE_URL);
        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish getPrinUrl(1).");
        }
        return shoppingCartUrlBuilder.toString();
    }

    /**
     * Method to get ShoppingCart url.
     * @param secureUrl the secureUrl
     * @param contextPath as String
     * @param externalUrl the externalUrl
     * @param cid the cid
     * @return ShoppingCartUrl as String
     */
    public static String getSecuredCheckoutUrl(final String secureUrl, final String contextPath, final String externalUrl, final String cid) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start getSecuredCheckoutUrl.");
        }

        StringBuilder externalUrlToRedirect = new StringBuilder();
        externalUrlToRedirect.append(secureUrl);
        externalUrlToRedirect.append(contextPath);
        externalUrlToRedirect.append(CHECKOUT_MODULE_URL);
        externalUrlToRedirect.append(externalUrl);
        externalUrlToRedirect.append(QUESTION_MARK).append(CID).append(EQUALS).append(cid);

        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish getSecuredCheckoutUrl.");
        }
        return externalUrlToRedirect.toString();
    }

    /**
     * Method to get ShoppingCart url. Used for Progress bar functionality.
     * @param externalUrl the externalUrl
     * @param cid the cid
     * @return ShoppingCartUrl as String
     */
    public static String getSecuredCheckoutUrl(final String externalUrl, final String cid) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start getSecuredCheckoutUrl.");
        }

        StringBuilder externalUrlToRedirect = new StringBuilder();
        externalUrlToRedirect.append(CHECKOUT_MODULE_URL);
        externalUrlToRedirect.append(externalUrl);
        externalUrlToRedirect.append(QUESTION_MARK).append(CID).append(EQUALS).append(cid);

        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish getSecuredCheckoutUrl.");
        }
        return externalUrlToRedirect.toString();
    }

    /**
     * Method to get Checkout url.
     * @param contextPath as String
     * @return CheckoutUrl as String
     */
    public static String getCheckoutUrl(final String contextPath) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start getCheckoutUrl(1).");
        }
        StringBuilder checkoutUrlBuilder = new StringBuilder();
        if (!StringUtils.isBlank(contextPath)) {
            checkoutUrlBuilder.append(contextPath);
        }
        checkoutUrlBuilder.append(CHECKOUT_BASE_URL);
        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish getCheckoutUrl(1).");
        }
        return checkoutUrlBuilder.toString();
    }

    /**
     * Method to get MyAccount url.
     * @param contextPath as String
     * @param isNewMyAccountEnabled as boolean
     * @return myAccountUrl as String
     */
    public static String getMyAccountUrl(final String contextPath, final boolean isNewMyAccountEnabled) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start getMyAccountUrl.");
        }
        StringBuilder myAccountUrlBuilder = new StringBuilder();
        if (!StringUtils.isBlank(contextPath)) {
            myAccountUrlBuilder.append(contextPath);
        }
        if (isNewMyAccountEnabled) {
            myAccountUrlBuilder.append(MY_ACCOUNT_BASE_URL);
        } else {
            myAccountUrlBuilder.append(HEADER_MY_ACCOUNT_URL);
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish getMyAccountUrl.");
        }
        return myAccountUrlBuilder.toString();
    }

    /**
     * Method to get MyAccount url with quote anchor.
     * @param myAccountUrl as myAccountUrl
     * @return myAccountUrlWithQuoteAnchor as String
     */
    public static String getMyAccountUrlWithQuoteAnchor(final String myAccountUrl) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start getMyAccountUrlWithQuoteAnchor.");
        }

        StringBuilder urlBuilder = new StringBuilder(myAccountUrl);
        urlBuilder.append(ANCHOR_TOKEN);
        urlBuilder.append(MY_QUOTE_ANCHOR);

        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish getMyAccountUrlWithQuoteAnchor.");
        }
        return urlBuilder.toString();
    }

    /**
     * Method to get ShoppingCart url.
     * @param secureUrl the secureUrl
     * @param contextPath as String
     * @param externalUrl the externalUrl
     * @param cid the cid
     * @return ShoppingCartUrl as String
     */
    public static String getSecuredMyAccountUrl(final String secureUrl, final String contextPath, final String externalUrl, final String cid) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start getSecuredCheckoutUrl.");
        }

        StringBuilder externalUrlToRedirect = new StringBuilder();
        externalUrlToRedirect.append(secureUrl);
        externalUrlToRedirect.append(contextPath);
        externalUrlToRedirect.append(AUTH_URL_COMPONENT);
        externalUrlToRedirect.append(MY_ACCOUNT_URL);
        externalUrlToRedirect.append(externalUrl);
        externalUrlToRedirect.append(QUESTION_MARK).append(CID).append(EQUALS).append(cid);

        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish getSecuredCheckoutUrl.");
        }
        return externalUrlToRedirect.toString();
    }

    /**
     * Method to get Already Registered Email Redirect URL.
     * @return emailRedirectURL as string
     */
    public static String getAlreadyRegisteredEmailRedirectURL() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start getAlreadyRegisteredEmailRedirectURL.");
        }

        StringBuilder alreadyRegisteredEmailUrl = new StringBuilder();
        alreadyRegisteredEmailUrl.append(SHOPPING_CART_BASE_URL);

        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish getAlreadyRegisteredEmailRedirectURL.");
        }
        return alreadyRegisteredEmailUrl.toString();

    }

    /**
     * Method to get Multiple Account Match Redirect URL.
     * @param contextPath contextPath
     * @param locale locale
     * @return redirectUrl as a String
     */
    public static String getRegistrationURL(final String contextPath, final String locale) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start getRegistrationURL.");
        }

        StringBuilder registrationURL = new StringBuilder();
        registrationURL.append(contextPath).append("/registration/registrationPageOneAction.html?method=register");

        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish getRegistrationURL.");
        }
        return registrationURL.toString();
    }

    /**
     * Method to get PM REgistration Error URL.
     * @return pmerrorRedirectURL as string
     */
    public static String getPMErrorRedirectURL() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start getPMErrorRedirectURL.");
        }
        StringBuilder pmErrorRedirectURL = new StringBuilder();
        pmErrorRedirectURL.append(SHOPPING_CART_BASE_URL);

        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish getPMErrorRedirectURL.");
        }
        return pmErrorRedirectURL.toString();
    }

    /**
     * Method to get Parts List url.
     * @param contextPath as String
     * @return Parts List URL as String
     */
    public static String getPartsListUrl(final String contextPath) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start getPartsListUrl(1).");
        }
        StringBuilder partsListUrlBuilder = new StringBuilder(contextPath);
        partsListUrlBuilder.append(PARTS_LIST_URL);
        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish getPartsListUrl(1).");
        }
        return partsListUrlBuilder.toString();
    }

    /**
     * Method to get Parts List details url.
     * @param contextPath as String
     * @param partsListId as String
     * @return Parts List details URL as String
     */
    public static String getPartsListDetailsUrl(final String contextPath, final String partsListId) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start getPartsListDetailsUrl.");
        }
        StringBuilder partsListUrlBuilder = new StringBuilder(contextPath);
        partsListUrlBuilder.append(PARTS_LIST_DETAILS_URL);
        partsListUrlBuilder.append(partsListId);
        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish getPartsListDetailsUrl.");
        }
        return partsListUrlBuilder.toString();
    }

    /**
     * Method to get Parts List url.
     * @param contextPath as String
     * @return Parts List URL as String
     */
    public static String getOrderPreferencesUrl(final String contextPath) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start orderPreferencesUrlBuilder.");
        }
        StringBuilder orderPreferencesUrlBuilder = new StringBuilder(contextPath);
        orderPreferencesUrlBuilder.append(ORDER_PREFERENCES_URL);
        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish orderPreferencesUrlBuilder.");
        }
        return orderPreferencesUrlBuilder.toString();
    }

    /**
     * Method to get Open Account url.
     * @param contextPath as String
     * @return Parts List URL as String
     */
    public static String getOpenAccountUrl(final String contextPath) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start getOpenAccountUrl.");
        }
        StringBuilder openAccountUrlBuilder = new StringBuilder(contextPath);
        openAccountUrlBuilder.append(OPEN_ACCOUNT_URL);
        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish getOpenAccountUrl.");
        }
        return openAccountUrlBuilder.toString();
    }

    /**
     * Method to generate update contact details url.
     * @param contextPath as String
     * @return update contact details URL.
     */
    public static String getUpdateContactDetailUrl(final String contextPath) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start getUpdateContactDetailUrl.");
        }
        StringBuilder updateContactDetailUrlBuilder = new StringBuilder(contextPath);
        updateContactDetailUrlBuilder.append(UPDATE_CONTACT_DETAIL_URL);
        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish getUpdateContactDetailUrl.");
        }
        return updateContactDetailUrlBuilder.toString();

    }

    /**
     * Method to get redirectUrl.
     * @param nodePath as String
     * @return redirectUrl String
     */
    public static String getRedirectUrl(final String nodePath) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Start getRedirectUrl().");
        }

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(CATALOGUE_BASE_URL);

        if (nodePath != null && !"".equals(nodePath.trim())) {
            stringBuilder.append(nodePath);
            if (!nodePath.endsWith(FORWARD_SLASH)) {
                stringBuilder.append(FORWARD_SLASH);
            }
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish getRedirectUrl().");
        }
        return stringBuilder.toString();
    }

    /**
     * Method to get the Genral Display Redirect Url with relevancy Data.
     * @param redirectUrl the redirectUrl String
     * @param relevancyData the relevancy Data
     * @return redirectUrl String
     */
    public static String getGeneralDisplayUrlWithRelevancyData(final String redirectUrl, final String relevancyData) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start getGeneralDisplayUrlWithRelevancyData.");
        }
        StringBuilder redirectUrlWithRelevancy = new StringBuilder(redirectUrl);
        redirectUrlWithRelevancy.append(AMPERSAND).append(RELEVANCY_DATA_PARAM).append(EQUALS).append(relevancyData);
        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish getGeneralDisplayUrlWithRelevancyData.");
        }
        return redirectUrlWithRelevancy.toString();
    }

    /**
     * Method to get redirect url with relevency.
     * @param keyWordRedirect the keyWordRedirect to set.
     * @param relevancyData the relevancyData to set.
     * @param cmMmc the cm_mmc to set.
     * @param gclid the gclid to set.
     * @return redirect url with relivency.
     */
    public static String getRedirectUrlWithRelivency(final String keyWordRedirect, final String relevancyData, final String cmMmc,
            final String gclid) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start getRedirectUrlWithRelivency.");
        }

        StringBuilder redirectUrlWithRelevancy = new StringBuilder();
        String hashTagPartofUrl = null;

        // some of these the keyword redirect URL can contain a '#', we need to handle these '#' urls carefully other wise can corrupt
        // the Query string and can cause SVC errors. e.g : 100mm crank handler --> /c/enclosures-storage-material-handling/knobs-equip
        // ment-handles-hand-wheels/crank-handles/#esid=cl_4286700823,cl_4286688998,cl_4286689302,cl_4286691811,cl_4286691813,cl_4286691809
        // &applied-dimensions=4286579404
        if (!StringUtils.isBlank(keyWordRedirect) && keyWordRedirect.contains(HASH)) {

            String[] keyWordRedirectUrlParts = keyWordRedirect.split(HASH);
            redirectUrlWithRelevancy.append(keyWordRedirectUrlParts[0]);
            if (keyWordRedirectUrlParts.length == 2) {
                hashTagPartofUrl = keyWordRedirectUrlParts[1];
            }
        } else {
            redirectUrlWithRelevancy.append(keyWordRedirect);
        }

        if (!StringUtils.isBlank(relevancyData)) {
            if (redirectUrlWithRelevancy.toString().contains(QUESTION_MARK)) {
                redirectUrlWithRelevancy.append(AMPERSAND).append(REDIRECT_RELEVANCY_DATA_PARAM).append(EQUALS).append(relevancyData);
            } else {
                redirectUrlWithRelevancy.append(PARAM_SEPARATOR).append(REDIRECT_RELEVANCY_DATA_PARAM).append(EQUALS).append(relevancyData);
            }
            // this parameter is to avoid a loop(if the redirected URL has the same search term, it can cause loop).
            redirectUrlWithRelevancy.append(AMPERSAND).append(URL_PARAM_TO_SUPPRESS_KEYWORD_REDIRECT);
        }

        if (!StringUtils.isBlank(cmMmc)) {
            if (redirectUrlWithRelevancy.toString().contains(QUESTION_MARK)) {
                redirectUrlWithRelevancy.append(AMPERSAND).append(REQUEST_PARAM_CM_MMC).append(EQUALS).append(cmMmc);
            } else {
                redirectUrlWithRelevancy.append(PARAM_SEPARATOR).append(REQUEST_PARAM_CM_MMC).append(EQUALS).append(cmMmc);
            }
        }

        if (!StringUtils.isBlank(gclid)) {
            if (redirectUrlWithRelevancy.toString().contains(QUESTION_MARK)) {
                redirectUrlWithRelevancy.append(AMPERSAND).append(REQUEST_PARAM_GCLID).append(EQUALS).append(gclid);
            } else {
                redirectUrlWithRelevancy.append(PARAM_SEPARATOR).append(REQUEST_PARAM_GCLID).append(EQUALS).append(gclid);
            }
        }

        if (!StringUtils.isBlank(hashTagPartofUrl)) {
            redirectUrlWithRelevancy.append(HASH).append(hashTagPartofUrl);
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish getRedirectUrlWithRelivency.");
        }
        return redirectUrlWithRelevancy.toString();
    }

    /**
     * Method to get redirect url with relevency.
     * @param redirectUrl as String
     * @param relevancyData the relevancyData to set.
     * @return redirect url with relivency.
     */
    public static String getUpdatedRedirectUrlWithRelevancy(final String redirectUrl, final String relevancyData) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start getUpdatedRedirectUrlWithRelevancy.");
        }

        StringBuilder redirectUrlWithRelevancy = new StringBuilder(redirectUrl);
        if (!StringUtils.isBlank(relevancyData)) {
            if (redirectUrlWithRelevancy.toString().contains(QUESTION_MARK)) {
                redirectUrlWithRelevancy.append(AMPERSAND).append(REDIRECT_RELEVANCY_DATA_PARAM).append(EQUALS).append(relevancyData);
            } else {
                redirectUrlWithRelevancy.append(PARAM_SEPARATOR).append(REDIRECT_RELEVANCY_DATA_PARAM).append(EQUALS).append(relevancyData);
            }
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish getUpdatedRedirectUrlWithRelevancy.");
        }
        return redirectUrlWithRelevancy.toString();
    }

    /**
     * Creates url to view a quote. The
     * path to view a quote will be generated using {@code quoteId}.
     * The {@code contextPath} will only be included in the url if
     * not null and is not empty.
     * @param contextPath as String
     * @param quoteId as String, encrypted version of the quote id
     * @return String the url to view a quote is returned
     */
    public static String getViewQuoteUrl(final String contextPath, final String quoteId) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start getViewQuoteUrl.");
        }
        String url = null;
        StringBuilder viewQuoteUrl = new StringBuilder();

        if (!StringUtils.isBlank(contextPath)) {
            viewQuoteUrl.append(contextPath);
        }

        viewQuoteUrl.append(QO_VIEW_QUOTE_URL);
        viewQuoteUrl.append(quoteId);
        url = viewQuoteUrl.toString();

        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish getViewQuoteUrl.");
        }
        return url;
    }

    /**
     * Creates url to view a product list. The
     * path to view a product list will be generated using {@code enquiryId}.
     * The {@code contextPath} will only be included in the url if
     * not null and is not empty.
     * @param contextPath as String
     * @param enquiryId as String, encrypted version of the enquiry id
     * @return String the url to view a product list is returned
     */
    public static String getViewProductListUrl(final String contextPath, final String enquiryId) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start getViewProductListUrl.");
        }
        String url = null;
        StringBuilder viewProductListUrl = new StringBuilder();

        if (!StringUtils.isBlank(contextPath)) {
            viewProductListUrl.append(contextPath);
        }

        viewProductListUrl.append(QO_VIEW_PRODUCT_LIST_URL);
        viewProductListUrl.append(enquiryId);
        url = viewProductListUrl.toString();

        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish getViewProductListUrl.");
        }
        return url;
    }

    /**
     * Creates url to view initial matches list. The
     * path to view initial matches will be generated using {@code enquiryId}.
     * The {@code contextPath} will only be included in the url if
     * not null and is not empty.
     * @param contextPath as String
     * @param enquiryId as String, encrypted version of the enquiry id
     * @return String  the url to view initial matches is returned
     */
    public static String getViewInitialMatchesUrl(final String contextPath, final String enquiryId) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start getViewInitialMatchesUrl.");
        }
        String url = null;
        StringBuilder viewInitialMatchesUrl = new StringBuilder();

        if (!StringUtils.isBlank(contextPath)) {
            viewInitialMatchesUrl.append(contextPath);
        }

        viewInitialMatchesUrl.append(QO_MATCHED_PARTS_URL);
        viewInitialMatchesUrl.append(enquiryId);
        url = viewInitialMatchesUrl.toString();

        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish getViewInitialMatchesUrl.");
        }
        return url;
    }

    /**
     * Creates url to invoke bom scrubbing. The
     * path to bom scrubbing will be generated using {@code enquiryId}.
     * The {@code contextPath} will only be included in the url if
     * not null and is not empty.
     * @param contextPath as String
     * @param enquiryId as String, encrypted version of the enquiry id
     * @return String the url to bom scrubbing will be returned.
     */
    public static String getBomScrubUrl(final String contextPath, final String enquiryId) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start getBomScrubUrl.");
        }
        String url = null;
        StringBuilder viewInitialMatchesUrl = new StringBuilder();

        if (!StringUtils.isBlank(contextPath)) {
            viewInitialMatchesUrl.append(contextPath);
        }

        viewInitialMatchesUrl.append(QO_BOMS_SCRUB_URL);
        viewInitialMatchesUrl.append(enquiryId);
        url = viewInitialMatchesUrl.toString();

        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish getBomScrubUrl.");
        }
        return url;
    }

    /**
     * Method to get ContactIdRequestUrl.
     * @param contextPath as String
     * @return ContactIdRequestUrl as String
     */
    public static String getContactIdRequestUrl(final String contextPath) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start getContactIdRequestUrl.");
        }

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(contextPath);
        stringBuilder.append(CONTACT_ID_REQUEST_URL);

        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish getContactIdRequestUrl.");
        }
        return stringBuilder.toString();
    }

    /**
     * @return my quotes manual entry url.
     */
    public static String getMyQuotesManualEntryUrl() {
        return QO_QUOTES_MANUAL_ENTRY_URL;
    }

    /**
     * @return my boms manual entry url.
     */
    public static String getMyBomsManualEntryUrl() {
        return QO_BOMS_MANUAL_ENTRY_URL;
    }

    /**
     * @return Product Details url for MyQuotes
     */
    public static String getMyQuotesProductDeatilsUrl() {
        return QO_QUOTES_PRODUCT_DETAILS_URL;
    }

    /**
     * @return Product Details url for MyBoms
     */
    public static String getMyBomsProductDeatilsUrl() {
        return QO_BOMS_PRODUCT_DETAILS_URL;
    }

    /**
     * Method to get url of the java info jar file.
     * @param nonSecureUrl as String
     * @return javaInfoJarURL as String
     */
    public static String getJavaInfoJarURL(final String nonSecureUrl) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start getJavaInfoJarURL.");
        }
        // the path of the Java Info Jar file.
        StringBuilder javaInfoJarURL = new StringBuilder();
        javaInfoJarURL.append(nonSecureUrl);
        javaInfoJarURL.append(FORWARD_SLASH);
        javaInfoJarURL.append(JAVA_INFO_JAR_PATH);
        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish getJavaInfoJarURL.");
        }
        return javaInfoJarURL.toString();
    }

    /**
     * Builds up the url to the supplied site image.
     * @param siteImageRoot String containing the site image path
     * @param imageNamePath String containing the name of the product with folder name.
     * @return String containing the url of the image
     */
    public static String getSiteImageURL(final String siteImageRoot, final String imageNamePath) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start getSiteImageURL");
        }

        StringBuilder imagePath = new StringBuilder(siteImageRoot);
        imagePath.append(imageNamePath);

        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish getSiteImageURL");
        }
        return imagePath.toString();
    }

    /**
     * Method to get the brand url with Parameters.
     * @param contextPath the contextPath
     * @param brandShopName the brandShopName
     * @param brandShopPath the brandShopPath
     * @param hierarchyName the hierarchyName
     * @return brandUrl String
     */
    public static String getBrandShopUrlWithParameters(final String contextPath, final String brandShopName, final String brandShopPath,
            final String hierarchyName) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start getBrandShopUrlWithParameters");
        }

        StringBuilder brandShopUrlBuilder = new StringBuilder();
        brandShopUrlBuilder.append(contextPath);
        brandShopUrlBuilder.append(BRAND_SHOP_BASE_URL);
        brandShopUrlBuilder.append(brandShopName);
        brandShopUrlBuilder.append(FORWARD_SLASH);
        if (!StringUtils.isBlank(brandShopPath)) {
            brandShopUrlBuilder.append(brandShopPath);
            brandShopUrlBuilder.append(FORWARD_SLASH);
        }
        if (!StringUtils.isBlank(hierarchyName)) {
            brandShopUrlBuilder.append(hierarchyName);
            brandShopUrlBuilder.append(FORWARD_SLASH);
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("Start getBrandShopUrlWithParameters");
        }
        return brandShopUrlBuilder.toString();
    }

    /**
     * This method is used to get Parcel tracking url.
     * @param contextPath
     * @return
     */
    public static final String getParcleTrackingURL(final String contextPath) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start getParcleTrackingURL");
        }
        return contextPath + PARCEL_TRACKING_URL;
    }

    /**
     * This method is used to get return order url.
     * @param contextPath
     * @return
     */
    public static final String getReturnOrderURL(final String contextPath) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start getReturnOrderURL");
        }
        return contextPath + RETURN_ORDER;
    }

    /**
     * This method is used to get invoice url.
     * @param contextPath
     * @return
     */
    public static final String getRequestInvoiceURL(final String contextPath) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start getRequestInvoiceURL");
        }
        return contextPath + REQUEST_INVOICE;
    }

    /**
     * This method is used to get credit account url.
     * @param contextPath
     * @return
     */
    public static final String getCreditAccountURL(final String contextPath) {
        return contextPath + CREDIT_ACCOUNT;
    }

    /**
     * This method is used to get all part list url.
     * @param contextPath
     * @return
     */
    public static final String getAllPartListsURL(final String contextPath) {
        return contextPath + DISPLAY_ALL_PART_LISTS;
    }

    /**
     * This method is used to render my account page with a particular section open.
     * @param contextPath
     * @param openSection
     * @return
     */
    public static final String getMyAccountOpenSectionURL(String contextPath, String openSection) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start getMyAccountOpenSectionURL");
        }

        StringBuilder myAccountUrlBuilder = new StringBuilder();
        if (!StringUtils.isBlank(contextPath)) {
            myAccountUrlBuilder.append(contextPath);
        }
        myAccountUrlBuilder.append(HEADER_MY_ACCOUNT_URL);
        if (openSection != null && !openSection.isEmpty()) {
            myAccountUrlBuilder.append(QUESTION_MARK + "expandMyAccountPanel=" + openSection);
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish getMyAccountOpenSectionURL.");
        }
        return myAccountUrlBuilder.toString();
    }

    /**
     * Returns secured  url based on mapping id being passed.
     * @param mappingId id for external link page.
     * To obtain the externalUrl parameter use:
     * PrettyUrlCacheServiceLocator.getLocator().locate(locale).getExternalUrl(MAPPING_ID_ORDERREFERENCE, locale)
     * @param contextPath the contextPath.
     * @return externalUrl URL to be displayed to users.
     */
    public static String getOrderPreferenceUrl(final String contextPath, String externalUrl) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start getOrderPreferenceUrl");
        }
        final StringBuilder orderPrefUrl = new StringBuilder();
        final String myAccountBaseUrl = UrlGenerationHelper.getMyAccountUrl(null, Boolean.TRUE);
        orderPrefUrl.append(myAccountBaseUrl);
        orderPrefUrl.append(externalUrl.substring(1));
        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish getOrderPreferenceUrl");
        }
        return contextPath + AUTH_URL + orderPrefUrl.toString();
    }

    /**
     * Returns not you  url.
     * @param contextPath
     * @return
     */
    public static String getNotYouUrl(final String contextPath) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start getOrderPreferenceUrl");
        }
        return contextPath + "/forgetme.html";

    }

    /**
     * Method to get InfoZoneUrl.
     * @param contextPath as String
     * @return infoZoneUrl as String
     */
    public static String getInfoZoneUrl(final String contextPath) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start getInfoZoneUrl.");
        }
        StringBuilder tradeCountersUrlBuilder = new StringBuilder();
        tradeCountersUrlBuilder.append(contextPath);
        tradeCountersUrlBuilder.append(HEADER_INFO_ZONE_URL);
        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish getInfoZoneUrl.");
        }
        return tradeCountersUrlBuilder.toString();
    }

    /**
     * Method to get getNewProductsLinkUrl.
     * @param contextPath as String
     * @return newProductsLinkUrl as String
     */
    public static String getNewProductsLinkUrl(final String contextPath) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start getNewProductsLinkUrl.");
        }
        StringBuilder tradeCountersUrlBuilder = new StringBuilder();
        tradeCountersUrlBuilder.append(contextPath);
        tradeCountersUrlBuilder.append(HEADER_NEW_PRODUCTS_URL);
        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish getNewProductsLinkUrl.");
        }
        return tradeCountersUrlBuilder.toString();
    }

    /**
     * Get the url of the top Brand
     * @param contextPath context path of the top brand url
     * @param brandName brand name to be used in the url
     * @return the full url of the Top Brand
     */
    public static String getTopBrandUrl(final String contextPath, final String brandName) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start getTopBrandUrl.");
        }
        StringBuilder topBrandUrlBuilder = new StringBuilder();
        topBrandUrlBuilder.append(contextPath).append(BRAND_SHOP_BASE_URL).append(brandName);

        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish getTopBrandUrl.");
        }
        return topBrandUrlBuilder.toString();
    }

    /**
     * Get the url of advanced compare page.
     * @param contextPath context path of the top brand url
     * @return the full url of the advanced compare page url
     */
    public static String getCompareSelectedProductsPageURL(final String contextPath) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start getCompareSelectedProductsPageURL.");
        }
        StringBuilder topBrandUrlBuilder = new StringBuilder();
        topBrandUrlBuilder.append(contextPath).append(COMPARE_SELECTED_PRODUCTS_BASE_URL);

        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish getCompareSelectedProductsPageURL.");
        }
        return topBrandUrlBuilder.toString();
    }

    /**
     * This method populates PDL server URL to fetch documents for specified product number.
     * @param pdlServerBaseUrl as String
     * @param pdlRequestApiKey as String
     * @param pdlRequestApiId as String
     * @param apiVersionPath as String
     * @param productNumber as String
     * @return the request url of PDL server
     */
    public static String getPDLServerRequestUrlForProductNumber(final String pdlServerBaseUrl, final String pdlRequestApiKey,
            final String pdlRequestApiId, final String apiVersionPath, final String productNumber) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start getPDLServerRequestUrlForProductNumber.");
        }

        StringBuilder pdlServerRequestUrl = new StringBuilder(pdlServerBaseUrl);
        pdlServerRequestUrl.append(FORWARD_SLASH);
        pdlServerRequestUrl.append(apiVersionPath);
        pdlServerRequestUrl.append(FORWARD_SLASH);
        pdlServerRequestUrl.append(PDL_REQUEST_URL_RS_ARTICLE);
        pdlServerRequestUrl.append(FORWARD_SLASH);
        pdlServerRequestUrl.append(productNumber);
        pdlServerRequestUrl.append(FORWARD_SLASH);
        pdlServerRequestUrl.append(PDL_REQUEST_URL_FILES);
        pdlServerRequestUrl.append(QUESTION_MARK);
        pdlServerRequestUrl.append(PDL_REQUEST_URL_PARAMETER_API_KEY);
        pdlServerRequestUrl.append(EQUALS);
        pdlServerRequestUrl.append(pdlRequestApiKey);
        pdlServerRequestUrl.append(AMPERSAND);
        pdlServerRequestUrl.append(PDL_REQUEST_URL_PARAMETER_API_ID);
        pdlServerRequestUrl.append(EQUALS);
        pdlServerRequestUrl.append(pdlRequestApiId);

        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish getPDLServerRequestUrlForProductNumber.");
        }
        return pdlServerRequestUrl.toString();
    }

    /**
     * This method is used to populate url for L0 brand page to land on TN page.
     * @param contextPath
     * @param cataloguePath
     * @param urlParameterDTO
     * @return
     * 
     * TODO: move this out of here to remove the UrlParameterDTO dependency.
     */
    public static String getCatalogueUrlWithParameters(final String contextPath, final String cataloguePath,
            final UrlParameterDTO urlParameterDTO) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Start getCatalogueUrlWithParameters.");
        }

        // For Catalogue Url
        StringBuilder catalogueUrlStringBuilder = new StringBuilder();
        String catalogueUrl = getCatalogueUrl(contextPath, cataloguePath);

        // Append Catalogue Url
        catalogueUrlStringBuilder.append(catalogueUrl);

        // Null and empty check for urlParameterDTO fields and Construct Parameter Builder
        StringBuilder parametersStringBuilder = new StringBuilder();
        if (urlParameterDTO.getSearchTerm() != null && !"".equals(urlParameterDTO.getSearchTerm())) {
            parametersStringBuilder.append(SEARCH_TERM_URL_PARAM).append(EQUALS).append(encodeSearchTerm(urlParameterDTO.getSearchTerm()))
                    .append(AMPERSAND);
        }
        if (urlParameterDTO.getPageOffset() != null && urlParameterDTO.getPageOffset() > 0) {
            parametersStringBuilder.append(PAGE_OFFSET_URL_PARAM).append(EQUALS).append(urlParameterDTO.getPageOffset()).append(AMPERSAND);
        }
        if (urlParameterDTO.getSortBy() != null && !"".equals(urlParameterDTO.getSortBy())) {
            parametersStringBuilder.append(SORT_BY_URL_PARAM).append(EQUALS).append(encodeSearchTerm(urlParameterDTO.getSortBy())).append(
                    AMPERSAND);
        }
        if (urlParameterDTO.getSortOrder() != null && !"".equals(urlParameterDTO.getSortOrder().trim())) {
            parametersStringBuilder.append(SORT_ORDER_URL_PARAM).append(EQUALS).append(urlParameterDTO.getSortOrder()).append(AMPERSAND);
        }
        if (urlParameterDTO.getViewMode() != null && !"".equals(urlParameterDTO.getViewMode().trim())) {
            parametersStringBuilder.append(VIEW_TYPE_URL_PARAM).append(EQUALS).append(urlParameterDTO.getViewMode()).append(AMPERSAND);
        }
        if (urlParameterDTO.getSearchType() != null && !"".equals(urlParameterDTO.getSearchType().trim())) {
            parametersStringBuilder.append(SEARCH_TYPE_URL_PARAM).append(EQUALS).append(urlParameterDTO.getSearchType()).append(AMPERSAND);
        }
        if (urlParameterDTO.getLastSelected() != null && !"".equals(urlParameterDTO.getLastSelected().trim())) {
            parametersStringBuilder.append(LAST_ATTRIBUTE_SELECTED).append(EQUALS).append(urlParameterDTO.getLastSelected()).append(
                    AMPERSAND);
        }
        if (urlParameterDTO.getSortOption() != null && !"".equals(urlParameterDTO.getSortOption().trim())) {
            parametersStringBuilder.append(SORT_OPTION_URL_PARAM).append(EQUALS).append(encodeSearchTerm(urlParameterDTO.getSortOption()))
                    .append(AMPERSAND);
        }
        if (urlParameterDTO.getProductionPack() != null && !"".equals(urlParameterDTO.getProductionPack().trim())) {
            parametersStringBuilder.append(PRODUCTION_PACK_URL_PARAM).append(EQUALS).append(urlParameterDTO.getProductionPack()).append(
                    AMPERSAND);
        }
        if (urlParameterDTO.getOfferName() != null && !"".equals(urlParameterDTO.getOfferName().trim())) {
            parametersStringBuilder.append(OFFERS_URL_PARAM).append(EQUALS).append(urlParameterDTO.getOfferName()).append(AMPERSAND);
        }
        if (urlParameterDTO.getNewProducts() != null && !"".equals(urlParameterDTO.getNewProducts().trim())) {
            parametersStringBuilder.append(NEW_PRODUCTS_URL_PARAM).append(EQUALS).append(urlParameterDTO.getNewProducts())
                    .append(AMPERSAND);
        }
        if (urlParameterDTO.getLeadTime() != null && !"".equals(urlParameterDTO.getLeadTime())) {
            parametersStringBuilder.append(LEAD_TIME_URL_PARAM).append(EQUALS).append(urlParameterDTO.getLeadTime()).append(AMPERSAND);
        }
        if (urlParameterDTO.getDisabledRefinements() != null && !"".equals(urlParameterDTO.getDisabledRefinements().trim())) {
            parametersStringBuilder.append(DISABLED_REFINEMENTS_URL_PARAM).append(EQUALS).append(urlParameterDTO.getDisabledRefinements())
                    .append(AMPERSAND);
        }
        if (urlParameterDTO.getAppliedDimensionIds() != null && !urlParameterDTO.getAppliedDimensionIds().isEmpty()) {
            parametersStringBuilder.append(HASH).append(APPLIED_DIMENSIONS_URL_PARAM).append(EQUALS);
            for (int i = 0; i < urlParameterDTO.getAppliedDimensionIds().size(); ++i) {
                parametersStringBuilder.append(urlParameterDTO.getAppliedDimensionIds().get(i).toString());
                if (i < urlParameterDTO.getAppliedDimensionIds().size() - 1) {
                    parametersStringBuilder.append(COMMA);
                }
            }
            parametersStringBuilder.append(AMPERSAND);
        }

        /* Add '?' to the front of parameters and remove the '&' from the end of parameters if urlParameters exist */
        if (parametersStringBuilder.length() > 0) {
            String urlParameters = parametersStringBuilder.substring(0, parametersStringBuilder.length() - 1);
            catalogueUrlStringBuilder.append(QUESTION_MARK).append(urlParameters);
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish getCatalogueUrlWithParameters.");
        }
        return catalogueUrlStringBuilder.toString();

    }

    /**
     * Method to return canonical url string .
     * @param String original_request_uri
     * @param String nonSecureUrl
     * @return canonicalUrl.
     */

    public static String getLoginRedirectCanonicalUrl(final String originalRequestUri, final String nonSecureUrl) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Start getLoginRedirectCanonicalUrl.");
        }

        StringBuilder canonicalUrl = new StringBuilder();
        canonicalUrl.append(nonSecureUrl);
        String requested_url = originalRequestUri.replaceAll(SECURE_URL_PREFIX, "");
        canonicalUrl.append(requested_url);

        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish getLoginRedirectCanonicalUrl.");
        }

        return canonicalUrl.toString();
    }
}
