package com.electrocomponents.commons.cache;

/**
 * provides list of cache names supported by this Object Cache module.
 * calling code should use one of these cache names for predictable caching behaviour.
 *
 */
public enum CacheName {

    /**
     * Cache name for caching web-labels.
     */
    CACHE_NAME_WEB_LABELS("web-labels"),

    /**
     * Cache name for caching system configuration (properties, large properties).
     */
    CACHE_NAME_SYSTEM_CONFIGURATION("system-configuration"),

    /**
     * Cache name for caching system configuration web app (server lists).
     */
    CACHE_NAME_SYSTEM_CONFIGURATION_WEB_APP("system-configuration-web-app"),

    /**
     * Cache name for reference-data (Sort Lists, UI selection options etc).
     */
    CACHE_NAME_REFERENCE_DATA("reference-data"),

    /**
     * Cache name for caching ui-page-capability-configuration.
     */
    CACHE_NAME_UI_PAGE_CAPABILITY_CONFIGURATION("ui-page-capability-configuration"),

    /**
     * Cache name for caching ui-selection-options.
     */
    CACHE_NAME_UI_SELECTION_OPTIONS("ui-selection-options"),

    /**
     * Cache name for caching price service responses.
     */
    CACHE_NAME_PRICE("price"),

    /**
     * Cache name for caching availability service response.
     */
    CACHE_NAME_AVAILABILITY("availability"),

    /**
     * Cache name for caching cost price service response.
     */
    CACHE_NAME_COST_PRICE("cost-price"),

    /**
     * Cache name for caching customer-validation-rules.
     */
    CACHE_NAME_CUSTOMER_VALIDATION_RULES("customer-validation-rules"),

    /**
     * Cache name for caching delivery-options metadata.
     */
    CACHE_NAME_DELIVERY_OPTIONS("delivery-options"),
    
    /**
     * Cache name for caching delivery-options metadata.
     */
    CACHE_NAME_CUSTOMER_DELIVERY_OPTIONS("customer-delivery-options"),

    /**
     * Cache name for caching FAQs Content.
     */
    CACHE_NAME_FAQ_CONTENT("faq-content"),

    /**
     * Cache name for caching brand content.
     */
    CACHE_NAME_BRAND_CONTENT("brand-content"),

    /**
     * Cache name for caching static page content (general display pages).
     */
    CACHE_NAME_STATIC_PAGE_CONTENT_WRAPPER("static-page-content-wrapper"),

    /**
     * Cache name for caching mapping configuration between internal/technical URLs to external urls (SEO firendly, user friendly).
     */
    CACHE_NAME_WEB_PAGE_URL_MAPPING("web-page-url-mapping"),

    /**
     * Cache name for Catalogue search browse meta data/config.
     */
    CACHE_NAME_CATALOGUE_SEARCH_BROWSE_FACADE("catalogue-search-browse-facade"),
    
    /**
     * Cache name for Catalogue SEO URL Mapping. caches SEO url to endeca dimension id mapping. 
     */
    CACHE_NAME_CATALOGUE_SEO_URL_MAPPING("catalogue-seo-url-mapping"),

    /**
     * Cache name for caching article-information.
     */
    CACHE_NAME_ARTICLE_INFORMATION("article-information"),
    
    /**
     * Cache name for caching product-associations (product and its accessories association).
     */
    CACHE_NAME_PRODUCT_ASSOCIATIONS("product-associations"),

    /**
     * Cache name for caching marketing-product-campaigns config.
     */
    CACHE_NAME_MARKETING_PRODUCT_CAMPAIGNS("marketing-product-campaigns"),

    /**
     * Cache name for  marketing-product-association-configuration purpose.
     */
    CACHE_NAME_MARKETING_PRODUCT_ASSOCIATION_CONFIGURATION("marketing-product-association-configuration"),

    /**
     * Cache name for user-agent-utilities purpose.
     */
    CACHE_NAME_USER_AGENT_UTILITIES("user-agent-utilities"),

    /**
     * Cache name for enquiries quotes.
     */
    CACHE_NAME_ENQUIRIES_QUOTES("enquiries-quotes"),

    /**
     * Cache name for predictive search.
     */
    CACHE_NAME_PREDICTIVE_SEARCH("predictive-search"),
	
	/**
     * Cache name for predictive search.
     */
    CACHE_NAME_RS_PARALLEL_PROCESSING_EXECUTOR("rs-parallel-processing-executor"),
    
    /**
     * Cache name for caching html-page-fragments.
     */
    CACHE_NAME_HTML_PAGE_FRAGMENT("html-page-fragment"),

    /**
     * Cache name for caching site config.
     */
    CACHE_NAME_SITE_CONFIG("site-config");

    /** The value representing the cache name. */
    private String cacheName;

    /**
     * @param value {@link #value}
     */
    private CacheName(final String cacheName) {
        this.cacheName = cacheName;
    }

    /**
     * @return {@link #value}
     */
    public String getCacheName() {
        return this.cacheName;
    }

}
