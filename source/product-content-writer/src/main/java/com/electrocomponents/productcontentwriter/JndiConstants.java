package com.electrocomponents.productcontentwriter;

public final class JndiConstants {

    public static String EJB_MODULE_NAME = "product-content-writer";
    public static String ENTITY_MANAGER_JNDI_NAME_EMEA = "java:app/entitymanager/product-content-writer-entity-manager-europe";
    public static String ENTITY_MANAGER_JNDI_NAME_APAC = "java:app/entitymanager/product-content-writer-entity-manager-noneurope";

    private JndiConstants() {
    }
}
