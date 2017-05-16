package com.electrocomponents.service.core.multi_item;

/**
 * <pre>
 * ************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author  : Andy Redhead
 * Created : 1 Jun 2010 at 10:35:15
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
 * Implementations provide the information necessary to store an item in an expected location within a tree cache. A tree cache needs both a
 * path into the cache and a key against which an individual item is to be stored.
 * @param <ItemRequest>
 * @author Andy Redhead
 */
public interface CacheLocationProvider<ItemRequest> {

    /**
     * What path should be used to store an item in a tree cache.
     * @param itemReq the itemReq
     * @return the path to use
     */
    String cachePath(ItemRequest itemReq);

    /**
     * What key should be used to store an item in a tree cache.
     * @param itemReq the itemReq
     * @return the key to use
     */
    Object cacheKey(ItemRequest itemReq);

}
