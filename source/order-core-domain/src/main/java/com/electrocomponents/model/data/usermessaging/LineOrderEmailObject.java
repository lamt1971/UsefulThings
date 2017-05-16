package com.electrocomponents.model.data.usermessaging;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlType;

/**
 * ************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author  : Ganesh Raut
 * Created : 15 Aug 2007 at 16:10:08
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
 * A class created for EmailService. This LineOrderEmailObject will held the data related to line order itoms from Order object for xml
 * generation
 * @author Ganesh Raut
 */
@XmlType
public class LineOrderEmailObject implements Serializable {

    /** serialVersionUID. */
    private static final long serialVersionUID = -1663803927066231962L;

    /** Rs stock number of the product. */
    private String rsStockNumber;

    /** Quantity of the ordered product. */
    private int productQuantity;

    /** Price of a single unit. */
    private String customerUnitPrice;

    /** List Price of a single unit. */
    private String listUnitPrice;

    /** Total price of a single type product. */
    private String customerLinePrice;

    /** Description of the ordered product. */
    private String description;

    /** Description of the ordered product. */
    private String weeCharge;

    /** Description of the ordered product. */
    private String environmentTax;

    /** Stock type of the ordered product.(True if Stocked / false if Non-Stocked). */
    private boolean isStockedProduct;

    /** Line level cost centre . */
    private String lineCostCentre;

    /** Line level part number. */
    private String linePartNumber;

    /** Boolean flag to check is on demand product. */
    private boolean isOnDemandProduct;

    /** isTradeCounter status. */
    private boolean isTradeCounter;

    /** List of availability labels. */
    List<AvailabilityLabels> availabilityLabels = new ArrayList<AvailabilityLabels>();

    /** Availability Message header. */
    private String availabilityMessageHeader;

    /**
     * @return the availabilityLabels
     */
    public List<AvailabilityLabels> getAvailabilityLabels() {
        return availabilityLabels;
    }

    /**
     * @param availabilityLabels the availabilityLabels to set
     */
    public void setAvailabilityLabels(final List<AvailabilityLabels> availabilityLabels) {
        this.availabilityLabels = availabilityLabels;
    }

    /**
     * Gets the line cost centre.
     * @return lineCostCentre
     */
    public String getLineCostCentre() {
        return lineCostCentre;
    }

    /**
     * Sets the line cost centre.
     * @param lineCostCentre lineCostCentre
     */
    public void setLineCostCentre(final String lineCostCentre) {
        this.lineCostCentre = lineCostCentre;
    }

    /**
     * Gets the environment tax.
     * @return the environmentTax
     */
    public String getEnvironmentTax() {
        return environmentTax;
    }

    /**
     * Sets the environment tax.
     * @param environmentTax the environmentTax to set
     */
    public void setEnvironmentTax(final String environmentTax) {
        this.environmentTax = environmentTax;
    }

    /**
     * Gets the wee charge.
     * @return the weeCharge
     */
    public String getWeeCharge() {
        return weeCharge;
    }

    /**
     * Sets the wee charge.
     * @param weeCharge the weeCharge to set
     */
    public void setWeeCharge(final String weeCharge) {
        this.weeCharge = weeCharge;
    }

    /**
     * Checks if is stocked product.
     * @return the isStockedProduct
     */
    public boolean isStockedProduct() {
        return isStockedProduct;
    }

    /**
     * Sets the stocked product.
     * @param isStockedProduct the isStockedProduct to set
     */
    public void setStockedProduct(final boolean isStockedProduct) {
        this.isStockedProduct = isStockedProduct;
    }

    /**
     * Gets the description.
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description.
     * @param description the description to set
     */
    public void setDescription(final String description) {
        this.description = description;
    }

    /**
     * Gets the customer line price.
     * @return the customerLinePrice
     */
    public String getCustomerLinePrice() {
        return customerLinePrice;
    }

    /**
     * Sets the customer line price.
     * @param customerLinePrice the customerLinePrice to set
     */
    public void setCustomerLinePrice(final String customerLinePrice) {
        this.customerLinePrice = customerLinePrice;
    }

    /**
     * Gets the product quantity.
     * @return the productQuantity
     */
    public int getProductQuantity() {
        return productQuantity;
    }

    /**
     * Sets the product quantity.
     * @param productQuantity the productQuantity to set
     */
    public void setProductQuantity(final int productQuantity) {
        this.productQuantity = productQuantity;
    }

    /**
     * Gets the rs stock number.
     * @return the rsStockNumber
     */
    public String getRsStockNumber() {
        return rsStockNumber;
    }

    /**
     * Sets the rs stock number.
     * @param rsStockNumber the rsStockNumber to set
     */
    public void setRsStockNumber(final String rsStockNumber) {
        this.rsStockNumber = rsStockNumber;
    }

    /**
     * Gets the list unit price.
     * @return the listUnitPrice
     */
    public String getListUnitPrice() {
        return listUnitPrice;
    }

    /**
     * Sets the list unit price.
     * @param listUnitPrice the listUnitPrice to set
     */
    public void setListUnitPrice(final String listUnitPrice) {
        this.listUnitPrice = listUnitPrice;
    }

    /**
     * Gets the customer unit price.
     * @return the customerUnitPrice
     */
    public String getCustomerUnitPrice() {
        return customerUnitPrice;
    }

    /**
     * Sets the customer unit price.
     * @param customerUnitPrice the customerUnitPrice to set
     */
    public void setCustomerUnitPrice(final String customerUnitPrice) {
        this.customerUnitPrice = customerUnitPrice;
    }

    /**
     * Gets the line part number.
     * @return The line part number
     */
    public String getLinePartNumber() {
        return linePartNumber;
    }

    /**
     * Sets the line part number.
     * @param linePartNumber The line part number
     */
    public void setLinePartNumber(final String linePartNumber) {
        this.linePartNumber = linePartNumber;
    }

    /**
     * Sets the checks if is on demand product.
     * @param isOnDemandProduct the new checks if is on demand product
     */
    public void setIsOnDemandProduct(final boolean isOnDemandProduct) {
        this.isOnDemandProduct = isOnDemandProduct;
    }

    /**
     * Gets the checks if is on demand product.
     * @return the checks if is on demand product
     */
    public boolean getIsOnDemandProduct() {
        return this.isOnDemandProduct;
    }

    /**
     * @return the isTradeCounter
     */
    public boolean getIsTradeCounter() {
        return isTradeCounter;
    }

    /**
     * @param isTradeCounter the isTradeCounter to set
     */
    public void setIsTradeCounter(final boolean isTradeCounter) {
        this.isTradeCounter = isTradeCounter;
    }

    /**
     * @return the availabilityMessageHeader
     */
    public String getAvailabilityMessageHeader() {
        return availabilityMessageHeader;
    }

    /**
     * @param availabilityMessageHeader the availabilityMessageHeader to set
     */
    public void setAvailabilityMessageHeader(final String availabilityMessageHeader) {
        this.availabilityMessageHeader = availabilityMessageHeader;
    }

}
