package com.electrocomponents.executor.management;

import java.lang.management.ManagementFactory;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.AttributeNotFoundException;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanServer;
import javax.management.MBeanServerInvocationHandler;
import javax.management.ObjectName;
import javax.management.ReflectionException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.electrocomponents.executor.threadpool.EcCustomThreadPoolImpl;
import com.electrocomponents.executor.threadpool.EcThreadPoolConstants;

/**
 * <pre>
 * ************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author  : Sharma Jampani
 * Created : 20 Jun 2013 at 10:27:27
 *
 * ************************************************************************************************
 * Change History
 * ************************************************************************************************
 * Ref      * Who      * Date       * Description
 * ************************************************************************************************
 *          *          *            *
 * ************************************************************************************************
 * </pre>
 */

/**
 * The ThreadConfiguration class is used to get the thread configuration and connection timeout details.
 * @author Sharma Jampani
 */
@Singleton
@Startup
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
public class ThreadConfiguration implements ThreadConfigurationMBean {

    /** Logger. */
    private static final Log LOG = LogFactory.getLog(ThreadConfiguration.class);

    /**
     * Attribute Map.
     */
    private ConcurrentHashMap<String, String> attributesMap = new ConcurrentHashMap<String, String>();

    /**
    * Reference for ThreadPoolMonitorMBean.
    */
    @EJB
    private ThreadPoolMonitorMBean threadPoolMonitorMBean;

    /**
     * Empty Constructor.
     */
    public ThreadConfiguration() {
       

    }

    /**
     * Method used to Initialise the application level thread pool configuration with give maxThread and connectionTimeOut values.
     */
    @PostConstruct
    public void create() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start 'create' of ThreadConfigurationMBean");
        }

        boolean isSuccess = false;
        boolean jvmLevelapplicationThreadPoolEnabled = isJvmLevelApplicationThreadPoolEnabled();
        int maxThread = getMaxThread();
        int connectionTimeOut = getConnectionTimeout();

        if (!jvmLevelapplicationThreadPoolEnabled || maxThread <= 0 || connectionTimeOut < 0) {
            if (LOG.isWarnEnabled()) {
                LOG.warn("The JVM Level Application Thread Pool Enabled Falg is :: " + jvmLevelapplicationThreadPoolEnabled
                        + " and the Max Thread is ::" + maxThread + " and Connection Timeout is::" + connectionTimeOut
                        + " Thread Pool Cannot be Created!!!!");
            }
        }

        if (jvmLevelapplicationThreadPoolEnabled && maxThread > 0) {
            isSuccess = EcCustomThreadPoolImpl.getInstance().create(maxThread, connectionTimeOut);
            LOG.info("Parameters to initialise thread poll are Max Thread: " + maxThread + ", Connection TimeOut: " + connectionTimeOut);
        }
        if (isSuccess) {
            LOG.info("ThreadConfigurationMBean : Pool initialised.");
            threadPoolMonitorMBean.printThreadUsage();
        } else {
            LOG.error("ThreadConfigurationMBean : Unable to initialise Pool");
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish 'create' of ThreadConfigurationMBean");
        }
        
        registerAsMBean();
    }
    
    private void registerAsMBean(){
    	 MBeanServer mbeanServer = ManagementFactory.getPlatformMBeanServer();
         try {
        	 ObjectName mbeanName = new ObjectName("electro.rsweb:monitor-target=threadconfiguration-service");
        	 mbeanServer.registerMBean(this, mbeanName);

         } catch (Throwable e) {
        	 String errorMsg = "Problem registering ThreadConfigurationMBean with name : electro.rsweb:monitor-target=threadconfiguration-service";
             LOG.fatal(errorMsg + e);
         }
    }

    /**
     * The getAttribute returns attribute from Map.
     * @param attribute String attribute.
     * @throws MBeanException the MBeanException
     * @throws AttributeNotFoundException the AttributeNotFoundException
     * @throws ReflectionException the ReflectionException
     * @return Object as object.
     */
    public Object getAttribute(final String attribute) throws AttributeNotFoundException, MBeanException, ReflectionException {
        return attributesMap.get(attribute);
    }

    /**
     * The invoke clear and object and create again.
     * @param actionName as Action Name.
     * @param params as Object Array.
     * @param signature as Signature.
     * @return Object as object.
     * @throws MBeanException the MBeanException
     * @throws ReflectionException the ReflectionException
     */
    public Object invoke(final String actionName, final Object[] params, final String[] signature) throws MBeanException,
            ReflectionException {
        try {
            attributesMap.clear();
            create();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

    /**
     * The getAttributes returns AttributeList from Map.
     * @param names as attribute names.
     * @return Object as object.
     */
    public AttributeList getAttributes(final String[] names) {
        Set<String> keySet = attributesMap.keySet();
        AttributeList list = new AttributeList();
        for (String name : keySet) {
            String value = attributesMap.get(name);
            if (value != null) {
                list.add(new Attribute(name, value));
            }
        }
        return list;
    }

    /**
     * The getMBeanInfo method return MBean Info.
     * @return MBeanInfo as MBeanInfo
     */
    public MBeanInfo getMBeanInfo() {
        Set<String> keySet = attributesMap.keySet();
        MBeanAttributeInfo[] attributeInfos = new MBeanAttributeInfo[keySet.size()];
        int i = 0;
        for (String key : keySet) {
            MBeanAttributeInfo attributeInfo =
                    new MBeanAttributeInfo(key, String.class.getName(), key + " is attribute for JMX MBean", true, false, false);
            attributeInfos[i++] = attributeInfo;
        }
        MBeanOperationInfo[] opers = new MBeanOperationInfo[1];
        opers[0] = new MBeanOperationInfo("create", "Reload MBean Config XML", null, "void", MBeanOperationInfo.ACTION);
        return new MBeanInfo(this.getClass().getName(), "Thread Configuration MBean", attributeInfos, null, opers, null);
    }

    /**
     * Callback Methods destroy.
     */
    @PreDestroy
    public void destroy() {
        LOG.info("Thread Configuration Destroyed");
        EcCustomThreadPoolImpl.getInstance().shutdown();
    }

    /**
     * The list method will display the current state of all member variable in list.
     * @return String.
     */
    public String list() {
        return "";
    }

    @Override
    public int getConnectionTimeout() {
        int connectionTimeout = -1;
        String connectionTimeoutProperty = System.getProperty(EcThreadPoolConstants.EC_THREAD_CONNECTION_TIMEOUT);
        if (StringUtils.isNotBlank(connectionTimeoutProperty)) {
            try {
                connectionTimeout = Integer.valueOf(connectionTimeoutProperty).intValue();
            } catch (NumberFormatException e) {
                LOG.error("Cannot convert value: " + connectionTimeoutProperty, e);
            }
        }
        return connectionTimeout;
    }

    @Override
    public int getMaxThread() {
        int maxThread = -1;
        String maxThreadProperty = System.getProperty(EcThreadPoolConstants.EC_THREAD_POOL_MAX);
        if (StringUtils.isNotBlank(maxThreadProperty)) {
            try {
                maxThread = Integer.valueOf(maxThreadProperty).intValue();
            } catch (NumberFormatException e) {
                LOG.error("Cannot convert value: " + maxThreadProperty, e);
            }
        }
        return maxThread;
    }

    /**
     * Method to check whether the Thread Pool has been Enabled at JVM Level.
     * @return threadPoolEnabled boolean
     */
    private boolean isJvmLevelApplicationThreadPoolEnabled() {
        boolean threadPoolEnabled = false;

        String jvmLevelApplicationThreadPoolEnabled = System.getProperty(EcThreadPoolConstants.EC_THREAD_RESOURCE_POOL_ENABLE);
        if (StringUtils.isNotBlank(jvmLevelApplicationThreadPoolEnabled)) {
            threadPoolEnabled = Boolean.valueOf("Y".equalsIgnoreCase(jvmLevelApplicationThreadPoolEnabled));
        }
        return threadPoolEnabled;
    }

    /**
     * Method to check whether the Thread Pool Usage Monitoring has been Enabled at JVM Level.
     * @return isThreadUsageMonitored boolean
     */
    public static boolean isThreadUsageMonitored() {
        boolean isThreadUsageMonitored = false;

        String jvmLevelThreadPoolUsageEnabled = System.getProperty(EcThreadPoolConstants.EC_THREAD_POOL_USAGE_MONITORING);
        if (StringUtils.isNotBlank(jvmLevelThreadPoolUsageEnabled)) {
            isThreadUsageMonitored = Boolean.valueOf("Y".equalsIgnoreCase(jvmLevelThreadPoolUsageEnabled));
        }
        return isThreadUsageMonitored;
    }

}
