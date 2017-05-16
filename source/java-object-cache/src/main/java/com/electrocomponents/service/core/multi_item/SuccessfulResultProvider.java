package com.electrocomponents.service.core.multi_item;

/**
 * <pre>
 * ************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author  : <<<<user name>>>>
 * Created : 1 Jun 2010 at 10:39:01
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
 * Interface for SuccessfulResultProvider.
 * @param <ItemResult>
 * @author Andy Redhead
 */
public interface SuccessfulResultProvider<ItemResult> {

    /**
     * Was the call for this item ok.
     * @param iresult iresult.
     * @return true if the call was ok
     */
    boolean successfullResult(final ItemResult iresult);

}
