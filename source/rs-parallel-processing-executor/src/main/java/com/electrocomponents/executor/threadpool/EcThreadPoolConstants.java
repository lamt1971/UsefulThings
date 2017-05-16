package com.electrocomponents.executor.threadpool;

/**
 *
 * <pre>
 * ************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author  : Sharma Jampani
 * Created : 1 Aug 2013 at 14:09:35
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
 * This interface is created to hold all the constants related to Thread pool Implementation.
 * @author c0950667
 */
public interface EcThreadPoolConstants {

    /**
     * Constant for EC_THREAD_POOL_MAX.
     */
    String EC_THREAD_POOL_MAX = "ec.thread.pool.max";

    /**
     * Constant for EC_THREAD_CONNECTION_TIMEOUT.
     */
    String EC_THREAD_CONNECTION_TIMEOUT = "ec.thread.connection.timeout";

    /** Property (Jboss run.bat/run.conf) to check application scope thread pool is enabled at JVM Level.*/
    String EC_THREAD_RESOURCE_POOL_ENABLE = "ec_thread_resource_pool_enable";

    /**
     * Constant for EXECUTOR_QUEUE_DEPTH_MAX.
     */
    String EXECUTOR_QUEUE_DEPTH_MAX = "executor.queue.depth.max";

    /**
     * Constant for EC_EXECUTOR_QUEUE_DEPTH_MAX.
     */
    String EC_EXECUTOR_QUEUE_DEPTH_MAX = "ec.executor.queue.depth.max";

    /**
     * Constant for EC_EXECUTOR_QUEUE_BLOCKING_TIMEOUT_MS.
     */
    String EC_EXECUTOR_QUEUE_BLOCKING_TIMEOUT_MS = "ec.executor.queue.blocking.timeout.ms";

    /**
     * Constant for EC_EXECUTOR_RESULT_TIMEOUT_MS.
     */
    String EC_EXECUTOR_RESULT_TIMEOUT_MS = "ec.executor.result.timeout.ms";

    /**
     * Constant for EC_EXECUTOR_ASYNC_ROOT_CACHE_PATH.
     */
    String EC_EXECUTOR_ASYNC_ROOT_CACHE_PATH = "ec.executor.async.root.cache.path";

    /**
     * Constant for EC_EXECUTOR_COMMAND_CREATION_MAX.
     */
    String EC_EXECUTOR_COMMAND_CREATION_MAX = "ec.executor.command.creation.max";

    /**
     * Constant for EC_EXECUTOR_PAGE_CREATION_MAX.
     */
    String EC_EXECUTOR_PAGE_CREATION_MAX = "ec.executor.page.creation.max";

    /**
     * Constant for EXECUTOR_MAX_THREAD_POOL_SIZE.
     */
    String EXECUTOR_MAX_THREAD_POOL_SIZE = "executor.max.thread.pool.size";

    /**
     * Constant for EC_EXECUTOR_MAX_THREAD_POOL_SIZE.
     */
    String EC_EXECUTOR_MAX_THREAD_POOL_SIZE = "ec.executor.max.thread.pool.size";

    /**
     * Constant for EXECUTOR_CORE_THREAD_POOL_SIZE .
     */
    String EXECUTOR_CORE_THREAD_POOL_SIZE = "executor.core.thread.pool.size";

    /**
     * Constant for EC_EXECUTOR_CORE_THREAD_POOL_SIZE.
     */
    String EC_EXECUTOR_CORE_THREAD_POOL_SIZE = "ec.executor.core.thread.pool.size";

    /**
     *  Constant for EC_EXECUTOR_IDLE_TIMEOUT_MS.
     */
    String EC_EXECUTOR_THREAD_IDLE_TIMEOUT_MS = "ec.executor.thread.idle.timeout.ms";

    /**
     *  Constant for EXECUTOR_IDLE_TIMEOUT_MS.
     */
    String EXECUTOR_THREAD_IDLE_TIMEOUT_MS = "executor.thread.idle.timeout.ms";

    /**
     *  Constant for EXECUTOR_ALLOW_CORE_THREAD_TIMEOUT.
     */
    String EXECUTOR_ALLOW_CORE_THREAD_TIMEOUT = "executor.allow.core.thread.timeout";

    /**
     *  Constant for EC_EXECUTOR_ALLOW_CORE_THREAD_TIMEOUT.
     */
    String EC_EXECUTOR_ALLOW_CORE_THREAD_TIMEOUT = "ec.executor.allow.core.thread.timeout";

    /**
     *  Constant for Thread pool monitoring.
     */
    String EC_THREAD_POOL_USAGE_MONITORING = "ec.thread.pool.usage.monitoring";

}
