package com.electrocomponents.model.domain.product;

/*
 * ************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author  : Stuart Sim
 * Created : 5 Sep 2007 at 16:55:05
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
 * Represents a Products "Sensitivity Status" for a given CustomerAccount or PunchOut Customer. DEFINITIONS ----------- BARRED : The product
 * CANNOT be purchased by this customer. RESTRICTED : The product CAN be purchased by this customer, but the customer should be warned that
 * Electrocomponents is not the preferred supplier for this type of Product. AVAILABLE : Indicates that the customer can be freely purchased
 * by the customer.
 * @author Stuart Sim
 */
public enum ProductSensitivityType {

    /** Indicates that the product CANNOT be purchased by this customer. */
    BARRED("B"),

    /**
     * Indicates that the product CAN be purchased by this customer, but the customer should be warned that Electrocomponents is not the
     * preferred supplier for this type of Product.
     */
    RESTRICTED("R"),

    /** Indicates that the customer can be freely purchased by the customer. */
    AVAILABLE("A");

    /** Holds the productSensitivityStatus. */
    private final String productSensitivityStatus;

    /**
     * @param productSensitivityStatus {@link #productSensitivityStatus}
     */
    private ProductSensitivityType(final String productSensitivityStatus) {
        this.productSensitivityStatus = productSensitivityStatus;
    }

    /**
     * @return {@link #productSensitivityStatus}
     */
    public String getProductSensitivityStatus() {
        return this.productSensitivityStatus;
    }

    /**
     * @param productSensitivityIn the value representing the enum in the database.
     * @return the enum associated with the supplied database value
     */
    public static ProductSensitivityType value(final String productSensitivityIn) {
        ProductSensitivityType ps = null;

        final ProductSensitivityType[] values = values();
        for (final ProductSensitivityType productSensitivity : values) {
            if (productSensitivityIn.equals(productSensitivity.productSensitivityStatus)) {
                ps = productSensitivity;
                break;
            }
        }
        return ps;
    }
}
