package com.electrocomponents.model.domain.order;

import com.electrocomponents.model.domain.DateTime;

/**
 *
 * ************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author  : Mohan Muddana
 * Created : 12 Jul 2007 at 10:42:29
 *
 * ************************************************************************************************
 * Change History
 * ************************************************************************************************
 * Ref      * Who      * Date       * Description
 * ************************************************************************************************
 *          *          *            *
 * ************************************************************************************************
 */

/**
 * BlanketOrders Domain Model used for OrderPad Options. To be used in OrderOptions
 * @author Mohan Muddana
 */
public interface BlanketOrder extends Comparable {

    /** @return id OID */
    long getId();

    /** @param id oid */
    void setId(long id);

    /** @return blanketOrderReference */
    String getBlanketOrderReference();

    /** @param blanketOrderReference */
    void setBlanketOrderReference(final String blanketOrderReference);

    /** @return ownerId */
    long getOwnerId();

    /** @param ownerId */
    void setOwnerId(final long ownerId);

    /** @return storeId */
    long getStoreId();

    /** @param storeId */
    void setStoreId(final long storeId);

    /** @return creationTime) */
    DateTime getCreationTime();

    /** @param creationTime */
    void setCreationTime(final DateTime creationTime);

    /** @return deleted */
    long getDeleted();

    /** @param deleted */
    void setDeleted(final long deleted);

    /** @return endDateTime */
    DateTime getEndDate();

    /** @param endDate */
    void setEndDate(final DateTime endDate);

    /** @return lastModifiedTime */
    DateTime getLastModifiedTime();

    /** @param lastModifiedTime */
    void setLastModifiedTime(final DateTime lastModifiedTime);

    /** @return getOwnerType */
    String getOwnerType();

    /** @param ownerType */
    void setOwnerType(final String ownerType);

    /** @return startDate */
    DateTime getStartDate();

    /** @param startDate startDate */
    void setStartDate(final DateTime startDate);

    /** @return getStatus */
    long getStatus();

    /** @param status getStatus */
    void setStatus(final long status);

    /**
     * @return value In the Table this value is #float but we user #Float to avoid parsing exceptions.
     */
    Float getValue();

    /** @param value */
    void setValue(final Float value);

    /**
     * Compares 2 blanket orders for sorting.
     * @param o2 another blanket order
     * @return <1 if this < o2, >1 if this>o2
     */
    int compareTo(final Object o2);

    /**
     * @return remainingBudget
     */
    Double getRemainingBudget();

    /**
     * @param remainingBudget remainingBudget
     */
    void setRemainingBudget(final Double remainingBudget);

}
