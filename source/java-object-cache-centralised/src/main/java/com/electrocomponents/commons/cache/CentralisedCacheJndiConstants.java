package com.electrocomponents.commons.cache;

/**
 * JNDI constants.
 */
public final class CentralisedCacheJndiConstants {

    private CentralisedCacheJndiConstants(){};
    
    /**
     * The EJB module name.
     */
	public static final String EJB_MODULE_NAME = "java-object-cache-centralised";
    
    /** EntityManager JNDI name.*/
	public static final String ENTITY_MANAGER ="java:app/entitymanager/java-object-cache-centralised-entity-manager"; 

}
