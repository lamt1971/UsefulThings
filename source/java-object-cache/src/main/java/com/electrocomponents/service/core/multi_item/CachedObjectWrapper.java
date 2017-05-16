package com.electrocomponents.service.core.multi_item;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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
 * CachedObjectWrapper class implementation for Cached Object Wrapper.
 * @param <ItemRequest>
 * @param <ItemResult>
 * @author Andy Redhead
 */

public class CachedObjectWrapper<ItemRequest, ItemResult> {

    /** Logger for CachedObjectWrapper. */
    private static final Log LOG = LogFactory.getLog(CachedObjectWrapper.class);

    /** Future List for MultiItemCmd.*/
    Future<MultiItemCmd<ItemRequest, ItemResult>> _cmd;

    /** ItemRequest instance.*/
    ItemRequest _req;

    /** ItemResult instance.*/
    ItemResult _result;

    /**
     * Constructor method for CachedObjectWrapper.
     * @param cmd the Future.
     * @param req the ItemRequest.
     */
    public CachedObjectWrapper(final Future<MultiItemCmd<ItemRequest, ItemResult>> cmd, final ItemRequest req) {
        _cmd = cmd;
        _req = req;
    }

    /**
     * Method to return ItemResult.
     * @return ItemResult
     */
    public ItemResult getValue() {
        ItemResult value = null;
        try {
            MultiItemCmd<ItemRequest, ItemResult> cmd = _cmd.get();
            if (cmd != null) {
                value = cmd.findResponseForItemRequest(_req);
            }
        } catch (InterruptedException e) {
            LOG.warn("getValue - interrupted exception", e);
        } catch (ExecutionException e) {
            LOG.warn("getValue - excution exception", e);
        }
        return value;
    }

}
