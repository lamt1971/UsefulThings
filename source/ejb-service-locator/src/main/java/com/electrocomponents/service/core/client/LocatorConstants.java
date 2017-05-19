package com.electrocomponents.service.core.client;

/*
 *
 * ************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author  : <<<<user name>>>>
 * Created : 17 Jan 2008 at 14:17:42
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
 * Contants for All regions.
 */
public interface LocatorConstants {

    /** Constant for America region. */
    String REGION_NAME_AMER = "amer";

    /** Constant for Asic Pacific region. */
    String REGION_NAME_APAC = "apac";

    /** Constant for Europe and Middle East region. */
    String REGION_NAME_EMEA = "emea";

    /** Constant for global region. */
    String REGION_NAME_GLOBAL = "global";

    /** Constant for America region Host. */
    String REGION_HOSTS_AMER = "amerRegionHosts";

    /** Constant for Asic Pacific region Host. */
    String REGION_HOSTS_APAC = "apacRegionHosts";

    /** Constant for Europe and Middle East region Host. */
    String REGION_HOSTS_EMEA = "emeaRegionHosts";

    public static String APP_NAME_JNDI = "java:app/AppName";
    
    /** JNDI global context prefix. */
    public static String JNDI_GLOBAL_CONTEXT = "java:global/";
    
    /** JNDI global context prefix. */
    public static String JNDI_EJB_CONTEXT = "ejb:";
    
    /** EJB JNDI Name 'remote' interface view binding suffix. */
    public static String JNDI_EJB_REMOTE_BINDING_NAME_SUFFIX =  "Remote";
    
    /** EJB JNDI Name 'local' interface view binding suffix. */
    public static String JNDI_EJB_LOCAL_BINDING_NAME_SUFFIX =  "Local";
   

    // public static final String PROPFILE_KEY_ = "";
}
