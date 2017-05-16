package com.electrocomponents.service.objectcache.client;

import java.io.IOException;
import java.util.Properties;

import javax.management.JMX;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import com.electrocomponents.commons.cache.CacheName;
import com.electrocomponents.service.objectcache.interfaces.CacheManagerMXBean;

/**
 * POC client to read cache sizes and clear them.
 * 
 * @author C0952298
 *
 */
public class ObjectCacheClient {
   
    private static final String PROPERTIES_FILE = "/servers.properties";
    private static Properties properties = new Properties();
    
    private static final String LIST_CACHES_COMMAND = "list_caches";
    private static final String LIST_ENTRIES_COMMAND = "entries";
    private static final String CLEAR_COMMAND = "clear";
    private static final String EVICT_COMMAND = "evict";
    private static final String EXISTS_COMMAND = "exists";
    private static final String DETAILS_COMMAND = "details";
    
    static {
        try {
            properties.load(ObjectCacheClient.class.getResourceAsStream(PROPERTIES_FILE));
        } catch (IOException e) {
            System.out.println("Unable to load properties: " + e.getMessage());
        }
    }

    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            printUsageAndExit();
        } 
        String clusterId = args[0];
        String operation = args[1];
        String specifiedCacheName;
        String appName;
        String key;
        String clusterMembersStr = properties.getProperty(clusterId);
        if (clusterMembersStr == null || clusterMembersStr.length() == 0) {
            System.out.println("No cluster members defined for clusterId " + clusterId);
            return;
        }
        String[] clusterMembers = clusterMembersStr.split(" ");
        switch(operation) {
            case CLEAR_COMMAND: 
                if (args.length < 3) {
                    printUsageAndExit();
                } 
                specifiedCacheName = args[2];
                for (String hostname : clusterMembers) {
                    CacheManagerMXBean mbeanProxy = getMBeanProxy(hostname);
                    System.out.println("\nResults for server " + hostname);
                    System.out.println(specifiedCacheName +" contains : " + mbeanProxy.getCacheRegionSize(specifiedCacheName) + " items");  
                    int deletedCount = mbeanProxy.clearRegion(specifiedCacheName);
                    System.out.println("Deleted " + deletedCount + " items");  
                }
                break;
            case DETAILS_COMMAND:
                if (args.length == 5) {
                    appName = args[2];
                    specifiedCacheName = args[3];
                    key = args[4];
                    for (String hostname : clusterMembers) {
                        CacheManagerMXBean mbeanProxy = getMBeanProxy(hostname);
                        System.out.println("\nResults for server " + hostname);
                        System.out.println(mbeanProxy.getValueInfo(appName, specifiedCacheName, key));  
                    }
                } else if (args.length == 4) {
                    specifiedCacheName = args[2];
                    key = args[3];
                    for (String hostname : clusterMembers) {
                        CacheManagerMXBean mbeanProxy = getMBeanProxy(hostname);
                        System.out.println("\nResults for server " + hostname);
                        System.out.println(mbeanProxy.getValueInfo(specifiedCacheName, key));  
                    }
                } else {
                    printUsageAndExit();
                }
                break;
            case EVICT_COMMAND:
                if (args.length == 5) {
                    appName = args[2];
                    specifiedCacheName = args[3];
                    key = args[4];
                    for (String hostname : clusterMembers) {
                        CacheManagerMXBean mbeanProxy = getMBeanProxy(hostname);
                        System.out.println("\nResults for server " + hostname);
                        System.out.println("Evicting key " + key + " from application " + appName + " zone of cache " + specifiedCacheName);
                        mbeanProxy.evict(appName, specifiedCacheName, key);  
                    }
                } else if (args.length == 4) {
                    specifiedCacheName = args[2];
                    key = args[3];
                    for (String hostname : clusterMembers) {
                        CacheManagerMXBean mbeanProxy = getMBeanProxy(hostname);
                        System.out.println("\nResults for server " + hostname);
                        System.out.println("Evicting key " + key + " from cache " + specifiedCacheName);
                        mbeanProxy.evict(specifiedCacheName, key);  
                    }
                } else {
                    printUsageAndExit();
                }
                break;
            case EXISTS_COMMAND:
                if (args.length == 5) {
                    appName = args[2];
                    specifiedCacheName = args[3];
                    key = args[4];
                    for (String hostname : clusterMembers) {
                        CacheManagerMXBean mbeanProxy = getMBeanProxy(hostname);
                        System.out.println("\nResults for server " + hostname);
                        System.out.println(mbeanProxy.keyExists(appName, specifiedCacheName, key));  
                    }
                } else if (args.length == 4) {
                    specifiedCacheName = args[3];
                    key = args[3];
                    for (String hostname : clusterMembers) {
                        CacheManagerMXBean mbeanProxy = getMBeanProxy(hostname);
                        System.out.println("\nResults for server " + hostname);
                        System.out.println(mbeanProxy.keyExists(specifiedCacheName, key));  
                    }
                } else {
                    printUsageAndExit();
                }                
                break;
            case LIST_CACHES_COMMAND:
                for (String hostname : clusterMembers) {
                    CacheManagerMXBean mbeanProxy = getMBeanProxy(hostname);
                    System.out.println("\nResults for server " + hostname);
                    for (CacheName cacheName : CacheName.values()) {
                        System.out.println(cacheName.getCacheName() +" contains : " + mbeanProxy.getCacheRegionSize(cacheName.getCacheName()) + " items");         
                    }
                }
                break;
            case LIST_ENTRIES_COMMAND:
                if (args.length < 3) {
                    printUsageAndExit();
                } 
                specifiedCacheName = args[2];
                String regex = args.length > 3 ? args[3] : null;
                for (String hostname : clusterMembers) {
                    CacheManagerMXBean mbeanProxy = getMBeanProxy(hostname);
                    System.out.println("\nResults for server " + hostname);
                    System.out.println(mbeanProxy.getKeysAndValues(specifiedCacheName, regex));
                }
                break;
            default:
               printUsageAndExit();
        }
    }
    
    private static CacheManagerMXBean getMBeanProxy(String hostname) throws IOException, MalformedObjectNameException {        
        JMXServiceURL url = new JMXServiceURL("service:jmx:remoting-jmx://" + hostname + ":4447");
        JMXConnector jmxc = JMXConnectorFactory.connect(url, null);
        MBeanServerConnection mbsc = jmxc.getMBeanServerConnection();
        ObjectName mbeanName = new ObjectName("jimtest:type=JimsCacheManager");
        return JMX.newMBeanProxy(mbsc, mbeanName, CacheManagerMXBean.class, true);
    }
    
    private static void printUsageAndExit() {
        System.out.println("Usage: \n clusterId operation [additional args]\n e.g. " +
                "predictive-search-fmo-static1-emea list\n or " +
                "predictive-search-fmo-static1-emea clear system-configuration or " +
                "predictive-search-fmo-static1-emea evict predictive-search system-configuration hello_world_label" +
                "predictive-search-fmo-static1-emea details predictive-search system-configuration hello_world_label" +
                "predictive-search-fmo-static1-emea exists predictive-search system-configuration hello_world_label" +
                "predictive-search-fmo-static1-emea entries predictive-search system-configuration .*uk.*");
        System.exit(1);
    }
}
