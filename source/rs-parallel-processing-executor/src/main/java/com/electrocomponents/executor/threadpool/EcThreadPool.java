package com.electrocomponents.executor.threadpool;

import java.util.List;

/**
 * <pre>
 * ************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author  : Michael Reilly
 * Created : 19 Jun 2013 at 10:42:08
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
 * The EC Thread Pool Interface.
 * @author Michael Reilly
 */
public interface EcThreadPool {

    /**
     * Create new threads and change the status to Wait State prior to
     * putting the thread into the pool.
     * The name of the thread being created should have the following format :
     * EC_APP_TH_<jvm.route>_<SEQ>
     * Where.....
     * EC_APP_TH is static string to identify the application thread and
     * jvm.route is unique ID per JBoss Application Server and
     * SEQ is unique ID among threads in the pool. i.e. It may be a index number 1,2,3,...,N
     * This method should be invoked only once per application startup.
     * @param maxThread Maximum Number of Threads that should be created.
     * @param connectionTimeout The Connection Time Out.
     * @return True if the thread has been created ok. False if there is an issue.
     */
    boolean create(int maxThread, int connectionTimeout);

    /**
     * Set all threads in the pool to be terminated.
     * @return True if shutdown is sucessful.
     */
    boolean shutdown();

    /**
     * Get the number of threads in the Active Thread Pool.
     * @return The number of active Threads.
     */
    int getActiveThreadCount();

    /**
     * Get the configured Connection timeout.
     * @return The Connection Timeout.
     */
    int getConnectionTimeout();

    /**
     *  The list of Ec Thread Usage Instances.
     * @return The list of Ec Thread Usage Instances.
     */
    List<EcThreadUsage> getThreadsUsage();

    /**
     * Get the configured maximum number of threads.
     * @return The configured maximum number of threads.
     */
    int getMaxThread();

    /**
     * Get the number of threads which have died.
     * @return The number of dead threads.
     */
    int getDeadThreadCount();

    /**
     * Get the number of Free Threads.
     * @return The number of free threads.
     */
    int getFreeThreadCount();

    /**
     * The method returns the thread from free thread pool.
     * It moves thread from free pool to active pool and also assign the first runnable task
     * to it and also awake the thread by signaling it.
     * It should also assign current time in millisecond into EcThread.allocatedTimeInMills.
     * if thread is unavailable in the free pool, it should put requested thread to wait with connection timeout.
     * After timeout, it should check again the thread availability in free pool and return thread if available
     * as mentioned above, otherwise return NULL
     * @param firstTask The name of the first task.
     * @param sessionId The Session ID.
     * @return A free thread.
     */
    EcThread getThread(Runnable firstTask, String sessionId);

    /**
     * The method accept the runnable thread and check it is alive or not.
     * If alive, add the thread from active pool to free pool.
     * If not alive, create new thread with dead thread name and add it into the free pool.
     * It also increment the dead thread count.
     * The thread should be in await state whenever returned into free pool.
     * It also makes sure that associated first runnable task is removed from thread before moving into free pool.
     * It also calculate usage time in millisecond by performing the Calculation between current
     * millisecond and EcThread.allocatedTimeInMills which will be stored in EcThreadUsage
     * instance for the thread and also update the session id,...etc.
     * if calculated time usage is higher than current value in EcThreadUsage.maxUsageTimeInMillis.
     * @param runnable The runnable thread.
     */
    void releaseThread(Runnable runnable);

    /**
     * Method to get the Thread Pool Status.
     * @return The Thread Pool Status
     */
    String getThreadPoolStatus();

    /**
    * Returns the list of EcThreadUsage instances which are dead.
    * @return retuns the list of Dead Thread information
    */
    List<EcThreadUsage> getDeadThreadsUsage();

    /**
     * This method checks if the thread pool is creaed.
     * @return boolean is pool created.
     */
    boolean isPoolCreated();

}
