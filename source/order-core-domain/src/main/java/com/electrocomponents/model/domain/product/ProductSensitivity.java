package com.electrocomponents.model.domain.product;

import java.io.Serializable;

/*
 * ************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author  : Shruti Yadav
 * Created : 24 Sep 2007 at 10:59:14
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
 * A class showing the Sensitive Products.
 * @author Shruti Yadav
 */
public class ProductSensitivity implements Serializable {
    /** serialVersionUID. */
    private static final long serialVersionUID = -6583727087463032156L;

    /** productSensitivityType. */
    private ProductSensitivityType productSensitivityType;

    /** Customer Display Text. */
    private String customerDisplayText;

    /** Customer Display Text. */
    private String displayMoreInfoText;

    /**
     * Constructor.
     */
    public ProductSensitivity() {
    }

    /**
     * @return the serialVersionUID
     */
    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    /**
     * @return the customerDisplayText
     */
    public String getCustomerDisplayText() {
        return customerDisplayText;
    }

    /**
     * @param customerDisplayText the customerDisplayText to set
     */
    public void setCustomerDisplayText(final String customerDisplayText) {
        this.customerDisplayText = customerDisplayText;
    }

    /**
     * @return the displayMoreInfoText
     */
    public String getDisplayMoreInfoText() {
        return displayMoreInfoText;
    }

    /**
     * @param displayMoreInfoText the displayMoreInfoText to set
     */
    public void setDisplayMoreInfoText(final String displayMoreInfoText) {
        this.displayMoreInfoText = displayMoreInfoText;
    }

    /**
     * @return the productSensitivityType
     */
    public ProductSensitivityType getProductSensitivityType() {
        return productSensitivityType;
    }

    /**
     * @param productSensitivityType the productSensitivityType to set
     */
    public void setProductSensitivityType(final ProductSensitivityType productSensitivityType) {
        this.productSensitivityType = productSensitivityType;
    }

}
