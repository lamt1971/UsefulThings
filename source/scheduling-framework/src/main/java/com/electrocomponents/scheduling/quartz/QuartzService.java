package com.electrocomponents.scheduling.quartz;

import java.util.Properties;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Scheduler;
import org.quartz.impl.StdSchedulerFactory;

/**
 * singleton implementation for configuring, starting a quartz scheduler instance and binding to JNDI.
 *  
 * <p> 
 * <b>Note:</b> The Scheduler instance bound to JNDI is not Serializable, so you will get a null reference back if you try to retrieve it from outside
 * the JBoss server in which it was bound.
 * </p>
 * 
 */
public class QuartzService  {
	
    /** default logger. */
    private static final Log LOG = LogFactory.getLog(QuartzService.class);
    
    /** component jndi context. */
    private static final String COMP_JNDI_CONTEXT="java:comp/env/";
    
    /** component jndi context. */
    private static final String ENV_PROPERTY_NAME="name";
    
    /** component jndi context. */
    private static final String ENV_PROPERTY_PROPERTIES_FILE = "quartzPropertiesFile";

    private StdSchedulerFactory schedulerFactory;

    private String name;

    private String propertiesFile;
    
    /** scheduler startup status.*/
    private boolean schedulerStarted = false;

    /*
     * default constructor.
     */

    public QuartzService() {
        // use PropertiesFile attribute
        propertiesFile = "";

        // default JNDI name for Scheduler
        name = "Quartz";
    }

    /*
     * Interface.
     */

    public void setName(String name) {
        this.name = name;
        
    }

    /**
     * returns quartz service jndi name.
     * @return
     */
    public String getName() {
        return name;
    }
    
 
    /**
     * set properties files.
     * @param propertiesFile propertiesFile.
     */
    public void setPropertiesFile(String propertiesFile) {
        this.propertiesFile = propertiesFile;
    }

    /**
     * returns the properties file name.
     * @return propertiesFile.
     */
    public String getPropertiesFile() {
        return propertiesFile;
    }
    
    /**
     * Create and start the scheduler. This *must* be referenced as a post-construct lifecycle-callback-method
     * in ejb-jar.xml where the subclass of this service is configured, otherwise the scheduler will never be 
     * created and never start. 
     * 
     * @throws Exception
     */
    public void createAndStartScheduler() throws Exception {
    	
    	//read component env variables and initialise the bean.
    	setAttributeValues();
    	
    	LOG.info("Create QuartzService(" + name + ")...");

        if (StringUtils.isBlank(propertiesFile)) {
        	LOG.error("Must specify a valid 'PropertiesFile'");
        }

        schedulerFactory = new StdSchedulerFactory();

        try {
	        Properties properties = new Properties();
	        properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(propertiesFile));
	        schedulerFactory.initialize(properties);
	        startScheduler();
	        schedulerStarted = true;
            
        } catch (Exception e) {
        	LOG.error("Failed to initialize Quartz Scheduler", e);
        }

        LOG.info("QuartzService(" + name + ") created.");
        
    }

    private void startScheduler() throws Exception {
    	LOG.info("Start Quartz Scheduler (" + name + ")...");

        try {
            Scheduler scheduler = schedulerFactory.getScheduler();
            scheduler.start();
        } catch (Exception e) {
        	LOG.error("Failed to start Quartz Scheduler", e);
        }

        LOG.info("QuartzService(" + name + ") started.");
    }
    
    private void setAttributeValues() {    	
    	InitialContext ic;
		try {
			ic = new InitialContext();
			
			this.setName((String)ic.lookup(COMP_JNDI_CONTEXT+ENV_PROPERTY_NAME));
			this.setPropertiesFile((String)ic.lookup(COMP_JNDI_CONTEXT+ENV_PROPERTY_PROPERTIES_FILE));
	    		    	
	    	ic.close();
		} catch (NamingException e) {
			LOG.error("Exception while reading the attribute values from component env context.",e);
		}    	
    }    
    
    /**
     * Stop and destroy the scheduler. This *must* be referenced as a pre-destroy lifecycle-callback-method
     * in ejb-jar.xml where the subclass of this service is configured, otherwise the scheduler will never be 
     * stopped or cleaned up. 
     * 
     * @throws Exception
     */
    public void stopScheduler() throws Exception {
    	LOG.info("Stop Quartz Scheduler(" + name + ")...");

        try {
        	if(schedulerStarted && schedulerFactory != null) {
        		Scheduler scheduler = schedulerFactory.getScheduler();
                scheduler.shutdown();	
        	}            
        } catch (Exception e) {
        	LOG.error("Failed to shutdown Quartz Scheduler (" + name + ")", e);
        }

        schedulerFactory = null;
        LOG.info("Quartz Scheduler(" + name + ") stopped.");
    }
    
}

