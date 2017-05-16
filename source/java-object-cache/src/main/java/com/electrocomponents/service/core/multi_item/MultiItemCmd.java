package com.electrocomponents.service.core.multi_item;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * <pre>
 * ************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author  : <<<<user name>>>>
 * Created : 1 Jun 2010 at 15:58:38
 *
 * ************************************************************************************************
 * Change History
 * ************************************************************************************************
 * Ref      * Who      * Date       * Description
 * ************************************************************************************************
 * *          *            *
 * ************************************************************************************************
 * </pre>
 */

/**
 * A way of using a single call on a service to retrieve multiple values (such as prices for multiple products in multiple quantities). The
 * intention is that some of the wanted information may already be cached, so only the items which have not been cached are added as
 * parameters to the call using addItem.
 * @param <ItemRequest> req
 * @param <ItemResult> resp
 * @author Andy Redhead
 */
public interface MultiItemCmd<ItemRequest, ItemResult> extends Callable {

    /**
     * @param setupProperties the setupProperties
     */
    void setUpMultiItemCmd(final Map<String, Object> setupProperties);

    /**
     * Adds the item.
     * @param itemRequest the item request
     */
    void addItem(ItemRequest itemRequest);

    /**
     * List the item request objects which have been added this command.
     * @return the list
     */
    List<ItemRequest> getItemRequestList();

    /**
     * Find the response for a given request item.
     * @param itemRequest req
     * @return ItemResult result
     */
    ItemResult findResponseForItemRequest(ItemRequest itemRequest);

}
