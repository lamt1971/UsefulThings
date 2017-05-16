package com.electrocomponents.model.domain.order;

import java.io.Serializable;

import com.electrocomponents.model.domain.DateTime;
import com.electrocomponents.model.domain.Quantity;

/**
 * <pre>
 * ************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author  : Vaibhav Dhopte
 * Created : 21 Jul 2009 at 09:21:27
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
 * Represents a single order line item scheduled order drop data.
 * @author Vaibhav Dhopte
 */
public class OrderLineItemDrop implements Serializable {

    /** The SVID. */
    private static final long serialVersionUID = -5657340464780417163L;

    /** The Product number. */
    private Long ecProductId;

    /** Scheduled order line item drop date. */
    private DateTime dropDate;

    /** Scheduled order line item drop quantity. */
    private Quantity dropQuantity;

    /** Scheduled order line item drop date error. */
    private String dropDateError;

    /** Scheduled order line item drop quantity error. */
    private String dropQuantityError;

    /** Scheduled order line item drop associated index.e.g. drop '1','2'etc */
    private int dropIndex;

    /**
     * @return the dropDate
     */
    public DateTime getDropDate() {
        return dropDate;
    }

    /**
     * @param dropDate the dropDate to set
     */
    public void setDropDate(final DateTime dropDate) {
        this.dropDate = dropDate;
    }

    /**
     * @return the dropQuantity
     */
    public Quantity getDropQuantity() {
        return dropQuantity;
    }

    /**
     * @param dropQuantity the dropQuantity to set
     */
    public void setDropQuantity(final Quantity dropQuantity) {
        this.dropQuantity = dropQuantity;
    }

    /**
     * @return the ecProductId
     */
    public Long getEcProductId() {
        return ecProductId;
    }

    /**
     * @param ecProductId the ecProductId to set
     */
    public void setEcProductId(final Long ecProductId) {
        this.ecProductId = ecProductId;
    }

    /**
     * @return the dropDateError
     */
    public String getDropDateError() {
        return dropDateError;
    }

    /**
     * @param dropDateError the dropDateError to set
     */
    public void setDropDateError(final String dropDateError) {
        this.dropDateError = dropDateError;
    }

    /**
     * @return the dropQuantityError
     */
    public String getDropQuantityError() {
        return dropQuantityError;
    }

    /**
     * @return the dropIndex
     */
    public int getDropIndex() {
        return dropIndex;
    }

    /**
     * @param dropIndex the dropIndex to set
     */
    public void setDropIndex(final int dropIndex) {
        this.dropIndex = dropIndex;
    }

    /**
     * @param dropQuantityError the dropQuantityError to set
     */
    public void setDropQuantityError(final String dropQuantityError) {
        this.dropQuantityError = dropQuantityError;
    }

    /**
     * Returns hash code.
     * @return int
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((dropDate == null) ? 0 : dropDate.hashCode());
        result = prime * result + ((ecProductId == null) ? 0 : ecProductId.hashCode());
        return result;
    }

    /**
     * @param obj Object
     * @return boolean
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        OrderLineItemDrop other = (OrderLineItemDrop) obj;
        if (dropDate == null) {
            if (other.dropDate == null) {
                return true;
            }
            if (other.dropDate != null) {
                return false;
            }
        } else if (!dropDate.equals(other.dropDate)) {
            return false;
        }
        if (ecProductId == null) {
            if (other.ecProductId != null) {
                return false;
            }
        } else if (!ecProductId.equals(other.ecProductId)) {
            return false;
        }
        return true;
    }
}
