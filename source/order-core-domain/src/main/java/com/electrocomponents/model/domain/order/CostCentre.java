package com.electrocomponents.model.domain.order;

import com.electrocomponents.model.domain.DateTime;

/*
 * ************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author  : Mohan Muddana
 * Created : 12 Jul 2007 at 12:06:10
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
 * CostCentre Domain Model for CostCentres for the OrderPad Options.
 */
public interface CostCentre extends Comparable {

    /** @return getId which is OID */
    long getId();

    /** @param id (@link #oid) */
    void setId(final long id);

    /** @return costCentreId costCentreId */
    String getCostCentreId();

    /** @param costCentreId */
    void setCostCentreId(final String costCentreId);

    /** @return (@link #creationTime) */
    DateTime getCreationTime();

    /** @param creationTime (@link #creationTime) */
    void setCreationTime(final DateTime creationTime);

    /** @return (@link #deleted) */
    long getDeleted();

    /** @param deleted (@link #deleted) */
    void setDeleted(final long deleted);

    /** @return friendlyName */
    String getFriendlyName();

    /** @param friendlyName (@link #friendlyName) */
    void setFriendlyName(final String friendlyName);

    /** @return lastModifiedTime */
    DateTime getLastModifiedTime();

    /** @param lastModifiedTime (@link #lastModifiedTime); */
    void setLastModifiedTime(final DateTime lastModifiedTime);

    /** @return ownerType */
    String getOwnerType();

    /** @param ownerType (@link #ownerType) */
    void setOwnerType(final String ownerType);

    /** @return status */
    long getStatus();

    /** @param status (@link #status) */
    void setStatus(final long status);

    /**
     * Compares 2 cost centers for sorting.
     * @param o2 another cost centre to compare
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

    /**
     * @return ownerId.
     */
    long getOwnerId();

    /**
     * @param ownerId ownerId
     */
    void setOwnerId(final long ownerId);

    /**
     * @return storeId.
     */
    long getStoreId();

    /**
     * @param storeId storeId
     */
    void setStoreId(final long storeId);

    /**
     * @return the pmCostCentre
     */
    boolean isPmCostCentre();

    /**
     * @param pmCostCentre the pmCostCentre to set
     */
    void setPmCostCentre(final boolean pmCostCentre);

}
