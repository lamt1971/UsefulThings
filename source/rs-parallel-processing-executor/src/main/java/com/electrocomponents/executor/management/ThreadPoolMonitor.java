package com.electrocomponents.executor.management;

import java.lang.management.LockInfo;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
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
import javax.management.ObjectName;
import javax.management.ReflectionException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.electrocomponents.executor.threadpool.EcThreadPool;
import com.electrocomponents.executor.threadpool.EcThreadPoolFactory;
import com.electrocomponents.executor.threadpool.EcThreadUsage;

/**
 * <pre>
 * ************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author  : Michael Reilly
 * Created : 20 Jun 2013 at 10:24:15
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
 * Thread Pool Moonitor.
 * Used to monitor the status of threads in the pool,
 * with details of the Active, free and dead threads.
 * @author Michael Reilly
 */
@Singleton
@Startup
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
public class ThreadPoolMonitor implements ThreadPoolMonitorMBean {

    /** Commons logger. */
    private static final Log LOG = LogFactory.getLog(ThreadPoolMonitor.class);

    /**
     * Number of Active Threads.
     */
    private int activeThreadCount;

    /**
     * Number of Dead Threads.
     */
    private int deadThreadCount;

    /**
     * Number of Free Threads.
     */
    private int freeThreadCount;

    /**
     * Maximum number of threads.
     */
    private int maxThreadCount;

    /**
     * Dead Lock Counter.
     */
    private int deadLockCounter;

    /**
     * Flag to change the font if the max usage time is greater than 30 minutes.
     */
    private long changeFontTime = 1800000;

    /**
     * Attribute Map.
     */
    private ConcurrentHashMap<String, Long> attributesMap = new ConcurrentHashMap<String, Long>();
    
    @PostConstruct
    public void registerAsMBean(){
   	 MBeanServer mbeanServer = ManagementFactory.getPlatformMBeanServer();
        try {
       	 ObjectName mbeanName = new ObjectName("electro.rsweb:monitor-target=threadpool-monitor-service");
       	 mbeanServer.registerMBean(this, mbeanName);

        } catch (Throwable e) {
            String errorMsg = "Problem registering ThreadPoolMonitor with name : electro.rsweb:monitor-target=threadpool-monitor-service";
            LOG.fatal(errorMsg + e);
        }
   }

    /**
     * Find the list of deadlocked threads ids and retrieve
     * key data.
     * @return The Deadlocked data as a string.
     */
    public String printDeadLockInfo() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start printDeadLockInfo");
        }

        long[] deadLockthreadIds;
        attributesMap.put("deadLockCounter", 0L);
        StringBuilder sb = new StringBuilder();
        sb.append("<h3>" + "Dead Lock Information" + "</h3>");
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();

        if (threadMXBean.isSynchronizerUsageSupported()) {
            deadLockthreadIds = threadMXBean.findDeadlockedThreads();
        } else {
            deadLockthreadIds = threadMXBean.findMonitorDeadlockedThreads();
        }

        if (deadLockthreadIds != null && deadLockthreadIds.length > 0) {
            attributesMap.put("deadLockCounter", Long.valueOf(deadLockthreadIds.length));

            ThreadInfo[] threadInfos = threadMXBean.getThreadInfo(deadLockthreadIds, true, true);
            getThreadDetails(threadInfos, sb);
        } else {
            if (LOG.isInfoEnabled()) {
                sb.append("<table border=1>");
                sb.append("<tr>");
                sb.append("<td>");
                sb.append("<h4>" + "No DeadLock Threads Detected!!" + "</h4>");
                sb.append("</td>");
                sb.append("</tr>");
                sb.append("</table>");
            }
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish printDeadLockInfo");
        }

        return sb.toString();

    }

    /**
     * Get the details on the thread, including name, owner, etc.
     * @param threadInfos ThreadInfo Object Array.
     * @param sb the String Builder
     */
    private void getThreadDetails(final ThreadInfo[] threadInfos, final StringBuilder sb) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start getThreadDetails");
        }

        sb.append("<table border=1>");
        sb.append("<tr>");
        sb.append("<th>" + "Thread ID" + "</th>");
        sb.append("<th>" + "Thread Name" + "</th>");
        sb.append("<th>" + "Lock Name" + "</th>");
        sb.append("<th>" + "Locking Class Name" + "</th>");
        sb.append("<th>" + "Lock Owner Name" + "</th>");
        sb.append("<th>" + "Lock Owner ID" + "</th>");
        sb.append("<th>" + "Wait Time" + "</th>");
        sb.append("<th>" + "Blocked Time" + "</th>");
        sb.append("</tr>");
        
        for (ThreadInfo threadInfo : threadInfos) {
            LockInfo lockInfo = threadInfo.getLockInfo();
            sb.append("<tr>");
            sb.append("<td>" + threadInfo.getThreadId() + "</td>");
            sb.append("<td>" + threadInfo.getThreadName() + "</td>");
            sb.append("<td>" + threadInfo.getLockName() + "</td>");
            sb.append("<td>" + lockInfo.getClassName() + "</td>");
            sb.append("<td>" + threadInfo.getLockOwnerName() + "</td>");
            sb.append("<td>" + threadInfo.getLockOwnerId() + "</td>");
            sb.append("<td>" + threadInfo.getWaitedTime() + "</td>");
            sb.append("<td>" + threadInfo.getBlockedTime() + "</td>");
            sb.append("</tr>");
        }

        sb.append("</table>");

        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish getThreadDetails");
        }

    }

    /**
     * Method to populate the Thread Pool Data.
     */
    private void generateThreadPoolData() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start generateThreadPoolDate");
        }
        
        EcThreadPool ecThreadPool = EcThreadPoolFactory.getInstance().getThreadPool();

        activeThreadCount = ecThreadPool.getActiveThreadCount();
        deadThreadCount = ecThreadPool.getDeadThreadCount();
        freeThreadCount = ecThreadPool.getFreeThreadCount();
        maxThreadCount = ecThreadPool.getMaxThread();

        attributesMap.put("maxThreadCount", Long.valueOf(maxThreadCount));
        attributesMap.put("activeThreadCount", Long.valueOf(activeThreadCount));
        attributesMap.put("deadThreadCount", Long.valueOf(deadThreadCount));
        attributesMap.put("freeThreadCount", Long.valueOf(freeThreadCount));
        
        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish generateThreadPoolDate");
        }
    }

    /**
     * Generate the Thread Pool Data.
     * @return A string containing the details of the Thread Pool.
     */
    public String printThreadUsage() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start printThreadUsage");
        }
        
        StringBuilder sb = new StringBuilder();

        if (!ThreadConfiguration.isThreadUsageMonitored()) {
            sb.append("<table border=1><tr><td>Thread usage monitoring is DISABLED</td></tr></table>");
            return sb.toString();
        }

        EcThreadPool ecThreadPool = EcThreadPoolFactory.getInstance().getThreadPool();

        // Populating the counters dynamically.
        generateThreadPoolData();

        // Printing the Dead Lock Thread Information
        String deadLocakInfo = printDeadLockInfo();
        sb.append(deadLocakInfo);

        // Populating the Thread Counter Details table.
        populateThreadCountDetails(sb);

        List<EcThreadUsage> ecThreadUsages = ecThreadPool.getThreadsUsage();
        List<EcThreadUsage> deadThreadList = ecThreadPool.getDeadThreadsUsage();

        // Populating the Thread Usage Details table.
        populateThreadUsageDetails(sb, ecThreadUsages, false);

        if (deadThreadList != null && deadThreadList.size() > 0) {
            populateThreadUsageDetails(sb, deadThreadList, true);
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish printThreadUsage");
        }

        return sb.toString();

    }

    /**
    * Callback Methods destroy.
    */
    public void destroy() {
        LOG.info("ThreadPoolMonitorMBean Destroyed");
    }

    /**
     * Callback Methods start.
     * @throws Exception the Exception
     */
    protected void start() throws Exception {
        LOG.info("ThreadPoolMonitorMBean Started");
    }

    /**
     * Callback Methods start.
     * @throws Exception the Exception
     */
    protected void create() throws Exception {
        LOG.info("ThreadPoolMonitorMBean Started");
    }

    /**
     * Callback Methods stop.
     */
    public void stop() {
        LOG.info("ThreadPoolMonitorMBean Stopped");
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
        return printThreadUsage();
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
        generateThreadPoolData();
        return attributesMap.get(attribute);
    }

    /**
     * The setAttribute method sets the attribute name and value in Map.
     * @param attribute the attribute
     * @throws MBeanException the MBeanException
     * @throws AttributeNotFoundException the AttributeNotFoundException
     */
    public void setAttribute(final Attribute attribute) throws MBeanException, AttributeNotFoundException {
        String value = String.valueOf(attribute.getValue());
        attributesMap.put(attribute.getName(), new Long(Long.valueOf(value)));
    }

    /**
     * The setAttributes sets the AttributeList into Map.
     * @param list as AttributeList.
     * @return list as AttributeList.
     */
    public AttributeList setAttributes(final AttributeList list) {
        Iterator<Object> iterator = list.iterator();
        while (iterator.hasNext()) {
            Object object = iterator.next();
            if (object != null && Attribute.class.equals(object.getClass())) {
                Attribute attribute = (Attribute) object;
                String value = String.valueOf(attribute.getValue());
                attributesMap.put(attribute.getName(), new Long(Long.valueOf(value)));
            }
        }
        return list;
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
            String value = attributesMap.get(name).toString();
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
            if (key.contains("Default")) {
                MBeanAttributeInfo attributeInfo =
                        new MBeanAttributeInfo(key, String.class.getName(), key + " is attribute for JMX MBean", true, true, false);
                attributeInfos[i++] = attributeInfo;
            } else {
                MBeanAttributeInfo attributeInfo =
                        new MBeanAttributeInfo(key, String.class.getName(), key + " is attribute for JMX MBean", true, false, false);
                attributeInfos[i++] = attributeInfo;
            }
        }
        MBeanOperationInfo[] opers = new MBeanOperationInfo[1];
        opers[0] = new MBeanOperationInfo("printThreadUsage", "Prints Thread Usage", null, "String", MBeanOperationInfo.ACTION_INFO);
        return new MBeanInfo(this.getClass().getName(), "Thread Pool Monitor MBean", attributeInfos, null, opers, null);
    }

    /**
     * Method used to populate the Thread Count Information table.
     * @param sb the StringBuilder
     */
    private void populateThreadCountDetails(final StringBuilder sb) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start populateThreadCountDetails");
        }
        sb.append("<h3>" + "Thread Count Information" + "</h3>");
        sb.append("<table border=1>");
        sb.append("<tr>");
        sb.append("<th>" + "Maximum Thread Count" + "</th>");
        sb.append("<th>" + "Active Thread Count" + "</th>");
        sb.append("<th>" + "Dead Thread Count" + "</th>");
        sb.append("<th>" + "Free Thread Count" + "</th>");
        sb.append("</tr>");
        sb.append("<tr>");
        sb.append("<td>" + maxThreadCount + "</td>");
        sb.append("<td>" + activeThreadCount + "</td>");
        sb.append("<td>" + deadThreadCount + "</td>");
        sb.append("<td>" + freeThreadCount + "</td>");
        sb.append("</tr>");
        sb.append("</table>");
        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish populateThreadCountDetails");
        }
    }

    /**
     * Method used to populate the Thread Usage Details table.
     * @param sb the StringBuilder
     * @param threadUsageList List of Thread Usages
     * @param isDeadThreadList boolean isDeadThreadList
     */
    private void populateThreadUsageDetails(final StringBuilder sb, final List<EcThreadUsage> threadUsageList,
            final boolean isDeadThreadList) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start populateThreadUsageDetails");
        }

        if (isDeadThreadList) {
            sb.append("<h3>" + "Dead Thread Information" + "</h3>");
        } else {
            sb.append("<h3>" + "Thread Usage Information" + "</h3>");
        }

        sb.append("<table border=1>");
        sb.append("<tr>");
        sb.append("<th>" + "Thread Name" + "</th>");
        sb.append("<th>" + "Current Session Id" + "</th>");
        sb.append("<th>" + "Current Allocated Time(ms)" + "</th>");
        sb.append("<th>" + "Currently in Use Since(ms)" + "</th>");
        sb.append("<th>" + "Thread Borrowed By" + "</th>");
        sb.append("<th>" + "Maximum Usage Time(ms)" + "</th>");
        sb.append("<th>" + "Maximun Used Thread Allocated Date" + "</th>");
        sb.append("<th>" + "Maximun Used Thread Release Date" + "</th>");
        sb.append("<th>" + "Thread Borrowed by (when Beyond Max Usage)" + "</th>");
        sb.append("<th>" + "Session Id (when Beyond Max Usage)" + "</th>");
        sb.append("</tr>");
        if (threadUsageList != null && !"".equals(threadUsageList)) {

            for (EcThreadUsage ecThreadUsage : threadUsageList) {
                sb.append("<tr>");
                sb.append("<td>" + ecThreadUsage.getThreadName() + "</td>");
                if (ecThreadUsage.getSessionId() != null && !ecThreadUsage.getSessionId().trim().isEmpty()) {
                    sb.append("<td>" + ecThreadUsage.getSessionId() + "</td>");
                } else {
                    sb.append("<td>" + "Thread not allocated to any Session." + "</td>");
                }
                sb.append("<td>" + ecThreadUsage.getAllocatedTimeInMillis() + "</td>");
                if (ecThreadUsage.getAllocatedTimeInMillis() > 0) {
                    sb.append("<td>" + (System.currentTimeMillis() - ecThreadUsage.getAllocatedTimeInMillis()) + "</td>");
                    sb.append("<td>" + ecThreadUsage.getBorrowerThreadName() + "</td>");
                } else {
                    sb.append("<td>" + "N/A" + "</td>");
                    sb.append("<td>" + "N/A" + "</td>");
                }

                if (ecThreadUsage.getMaxUsageTimeInMillis() > changeFontTime) {
                    sb.append("<td>" + "<span style=" + "background-color:red;" + "color:white;>" + ecThreadUsage.getMaxUsageTimeInMillis()
                            + "</span>" + "</td>");
                } else {
                    if (ecThreadUsage.getMaxUsageTimeInMillis() > 0) {
                        sb.append("<td>" + ecThreadUsage.getMaxUsageTimeInMillis() + "</td>");
                    } else {
                        sb.append("<td>" + "N/A" + "</td>");
                    }
                }
                if (ecThreadUsage.getAllocatedDate() > 0 && ecThreadUsage.getReleasedDate() > 0) {
                    DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                    Date date = new Date(ecThreadUsage.getAllocatedDate());
                    sb.append("<td>" + formatter.format(date) + "</td>");
                    date = new Date(ecThreadUsage.getReleasedDate());
                    sb.append("<td>" + formatter.format(date) + "</td>");
                    sb.append("<td>" + ecThreadUsage.getThreadBorrowedByBeyondMaxUsage() + "</td>");
                } else {
                    sb.append("<td>" + "N/A" + "</td>");
                    sb.append("<td>" + "N/A" + "</td>");
                    sb.append("<td>" + "N/A" + "</td>");
                }

                if (ecThreadUsage.getSessionIdBeyongMaxUsage() != null && !ecThreadUsage.getSessionIdBeyongMaxUsage().trim().isEmpty()) {
                    sb.append("<td>" + ecThreadUsage.getSessionIdBeyongMaxUsage() + "</td>");
                } else {
                    sb.append("<td>" + "N/A" + "</td>");
                }
                sb.append("</tr>");
            }

        }
        sb.append("</table>");
        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish populateThreadUsageDetails");
        }
    }

    /**
     * @return the activeThreadCount
     */
    public int getActiveThreadCount() {
        return activeThreadCount;
    }

    /**
     * @return the deadThreadCount
     */
    public int getDeadThreadCount() {
        return deadThreadCount;
    }

    /**
     * @param deadThreadCount the deadThreadCount to set
     */
    public void setDeadThreadCount(final int deadThreadCount) {
        this.deadThreadCount = deadThreadCount;
    }

    /**
     * @return the freeThreadCount
     */
    public int getFreeThreadCount() {
        return freeThreadCount;
    }

    /**
     * @param freeThreadCount the freeThreadCount to set
     */
    public void setFreeThreadCount(final int freeThreadCount) {
        this.freeThreadCount = freeThreadCount;
    }

    /**
     * @return the maxThreadCount
     */
    public int getMaxThreadCount() {
        return maxThreadCount;
    }

    /**
     * @param maxThreadCount the maxThreadCount to set
     */
    public void setMaxThreadCount(final int maxThreadCount) {
        this.maxThreadCount = maxThreadCount;
    }

    /**
     * @return the deadLockCounter
     */
    public int getDeadLockCounter() {
        return deadLockCounter;
    }

    /**
     * @param deadLockCounter the deadLockCounter to set
     */
    public void setDeadLockCounter(final int deadLockCounter) {
        this.deadLockCounter = deadLockCounter;
    }

}
