package com.electrocomponents.model.domain.order;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Transient;

import com.electrocomponents.model.domain.Money;
import com.electrocomponents.model.domain.Quantity;
import com.electrocomponents.model.domain.availability.Availability;
import com.electrocomponents.model.domain.pricing.LinePrices;
import com.electrocomponents.model.domain.product.PackType;
import com.electrocomponents.model.domain.product.Product;
import com.electrocomponents.model.domain.product.ProductSensitivity;

/*
 * ************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author  : Ed Ward
 * Created : 19 Jul 2007 at 10:53:45
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
 * Represents a single item in an order.
 * @see Order
 * @author Ed Ward
 */
public class OrderLineItem implements Serializable {

    /** The SVID. */
    private static final long serialVersionUID = -2585901818061664512L;

    /** The order line number. */
    private long lineNumber;

    /**
     * The order line number / reference on the customer system used by eProcurement for eInvoicing line matching.
     */
    private String customerLineNumber;

    /** The product. */
    private Product product;

    /** The quantity. */
    private Quantity quantity;

    /** The customer specific prices / line prices based on availability of sold-to account. */
    private LinePrices prices;

    /** The list prices associated with this order line. */
    private LinePrices listLinePrices;

    /** The availability of the product. */
    private Availability availability;

    /** The line level cost centre (if present). */
    private String lineCostCentre;

    /** The PurchasingManager line level cost centre (if present). */
    private String pmLineCostCentre;

    /** The line level Gl Code (if present). */
    private String lineGlCode;

    /** Customer entered part number for this product line. */
    private String customerPartNumber;

    /** Electrocomponents marketing publication media code information. */
    private String lineMediaCode;

    /** An electronic address ID required in some markets (e.g. Danish Government). */
    private String lineElectronicAddressId;

    /** True if the product is sensitive (e.g. for PunchOut customers). */
    private boolean sensitiveProduct;

    /** For sensitive Products.. */
    @Transient
    private ProductSensitivity productSensitivity;

    /** The PM amend state. */
    private String pmAmendState;

    /** The pack type of the product. i.e. STANDARD, BULK, CALIBRATED, PRODUCTION_PACKED. */
    private PackType packType;

    /** Flag to differentiate between pm and user cost centre. */
    private boolean isPmLineLevelCostCentre = false;

    /** Flag to differentiate between pm and user gl code. */
    private boolean isPmLineLevelGlCode = false;

    /** List of Item Drops. */
    private Set<OrderLineItemDrop> lineItemDrops;

    /**
     * This contains site information priced or not.
     */
    private boolean unPriced;

    /**
     * Unique id.
     */
    private String uniqueId;

    /**
     * Quote line id used only for quote orders.
     */
    private String lineReference;

    /**
     * Flag to differentiate between pm and web cost centres.
     */
    private boolean isSelectedPmCostCentre;

    /**
     * Flag to differentiate between pm and web gl code.
     */
    private boolean isSelectedPmGLCode;

    /**
     * @return the unPriced
     */
    public boolean isUnPriced() {
        return unPriced;
    }

    /**
     * @param unPriced the unPriced to set
     */
    public void setUnPriced(final boolean unPriced) {
        this.unPriced = unPriced;
    }

    /**
     * @return the lineItemDrops
     */
    public Set<OrderLineItemDrop> getLineItemDrops() {
        return lineItemDrops;
    }

    /**
     * @param lineItemDrops the lineItemDrops to set
     */
    public void setLineItemDrops(final Set<OrderLineItemDrop> lineItemDrops) {
        this.lineItemDrops = lineItemDrops;
    }

    /**
     * @return isPmLineLevelCostCentre
     */
    public boolean isPmLineLevelCostCentre() {
        return isPmLineLevelCostCentre;
    }

    /**
     * @param isPmLineLevelCostCentre flag to differentiate between pm and user cost centre
     */
    public void setPmLineLevelCostCentre(final boolean isPmLineLevelCostCentre) {
        this.isPmLineLevelCostCentre = isPmLineLevelCostCentre;
    }

    /**
     * @return isPmLineLevelGlCode
     */
    public boolean isPmLineLevelGlCode() {
        return isPmLineLevelGlCode;
    }

    /**
     * @param isPmLineLevelGlCode flag to differentiate between pm and user gl code.
     */
    public void setPmLineLevelGlCode(final boolean isPmLineLevelGlCode) {
        this.isPmLineLevelGlCode = isPmLineLevelGlCode;
    }

    /**
     * @return lineGlCode line level Gl Code
     */
    public String getLineGlCode() {
        return lineGlCode;
    }

    /**
     * @param lineGlCode line level Gl Code
     */
    public void setLineGlCode(final String lineGlCode) {
        this.lineGlCode = lineGlCode;
    }

    /**
     * Check if this OrderLineItem is priced.
     * @return true if the line is priced, false if unpriced (i.e. is unPriced flag is true).
     */
    public boolean isPricedOrderLineItem() {
        if (product == null || product.isUnPriced()) {
            return false;
        }
        return true;
    }

    /**
     * @return {@link #unitPrice}
     */
    public Money getUnitPrice() {
        if (this.prices != null) {
            return this.prices.getUnitPrice();
        } else {
            return null;
        }
    }

    /**
     * @param unitPrice the price associated with this order line
     */
    public void setUnitPrice(final Money unitPrice) {
        if (this.prices != null) {
            this.prices.setUnitPrice(unitPrice);
        }
    }

    /**
     * @return {@link #linePrice}
     */
    public Money getLinePrice() {
        if (this.prices != null) {
            return this.prices.getLinePrice();
        } else {
            return null;
        }
    }

    /**
     * @param linePrice the price for this order line.
     */
    public void setLinePrice(final Money linePrice) {
        if (this.prices != null) {
            this.prices.setLinePrice(linePrice);
        }
    }

    /**
     * @return {@link #unitPrice}
     */
    public Money getListUnitPrice() {
        if (this.listLinePrices != null) {
            return this.listLinePrices.getUnitPrice();
        } else {
            return null;
        }
    }

    /**
     * @param unitPrice the price associated with this order line
     */
    public void setListUnitPrice(final Money unitPrice) {
        if (this.listLinePrices != null) {
            this.listLinePrices.setUnitPrice(unitPrice);
        }
    }

    /**
     * @return {@link #linePrice}
     */
    public Money getListLinePrice() {
        if (this.listLinePrices != null) {
            return this.listLinePrices.getLinePrice();
        } else {
            return null;
        }
    }

    /**
     * @param linePrice the price for this order line.
     */
    public void setListLinePrice(final Money linePrice) {
        if (this.listLinePrices != null) {
            this.listLinePrices.setLinePrice(linePrice);
        }
    }

    /**
     * @return the prices
     */
    public LinePrices getPrices() {
        return prices;
    }

    /**
     * @param prices the prices to set
     */
    public void setPrices(final LinePrices prices) {
        this.prices = prices;
    }

    /**
     * @return the listLinePrices
     */
    public LinePrices getListLinePrices() {
        return listLinePrices;
    }

    /**
     * @param listLinePrices the listLinePrices to set
     */
    public void setListLinePrices(final LinePrices listLinePrices) {
        this.listLinePrices = listLinePrices;
    }

    /**
     * @return {@link #product}
     */
    public Product getProduct() {
        return this.product;
    }

    /**
     * @param product {@link #product}
     */
    public void setProduct(final Product product) {
        this.product = product;
    }

    /**
     * @return the quantity being ordered
     */
    public Quantity getQuantity() {
        return this.quantity;
    }

    /**
     * @param quantity {@link #quantity}
     */
    public void setQuantity(final Quantity quantity) {
        this.quantity = quantity;
    }

    /**
     * @return the availability of the product / qty being ordered.
     */
    public Availability getAvailability() {
        return availability;
    }

    /**
     * @param availability {@link #availability}
     */
    public void setAvailability(final Availability availability) {
        this.availability = availability;
    }

    /**
     * @return the customerPartNumber
     */
    public String getCustomerPartNumber() {
        return customerPartNumber;
    }

    /**
     * @param customerPartNumber the customerPartNumber to set
     */
    public void setCustomerPartNumber(final String customerPartNumber) {
        this.customerPartNumber = customerPartNumber;
    }

    /**
     * @return the lineCostCentre
     */
    public String getLineCostCentre() {
        return lineCostCentre;
    }

    /**
     * @param lineCostCentre the lineCostCentre to set
     */
    public void setLineCostCentre(final String lineCostCentre) {
        this.lineCostCentre = lineCostCentre;
    }

    /**
     * @return the lineElectronicAddressId
     */
    public String getLineElectronicAddressId() {
        return lineElectronicAddressId;
    }

    /**
     * @param lineElectronicAddressId the lineElectronicAddressId to set
     */
    public void setLineElectronicAddressId(final String lineElectronicAddressId) {
        this.lineElectronicAddressId = lineElectronicAddressId;
    }

    /**
     * @return the lineMediaCode
     */
    public String getLineMediaCode() {
        return lineMediaCode;
    }

    /**
     * @param lineMediaCode the lineMediaCode to set
     */
    public void setLineMediaCode(final String lineMediaCode) {
        this.lineMediaCode = lineMediaCode;
    }

    /**
     * @return the lineNumber
     */
    public long getLineNumber() {
        return lineNumber;
    }

    /**
     * @param lineNumber the lineNumber to set
     */
    public void setLineNumber(final long lineNumber) {
        this.lineNumber = lineNumber;
    }

    /**
     * @return the sensitiveProduct
     */
    public boolean isSensitiveProduct() {
        return sensitiveProduct;
    }

    /**
     * @param sensitiveProduct the sensitiveProduct to set
     */
    public void setSensitiveProduct(final boolean sensitiveProduct) {
        this.sensitiveProduct = sensitiveProduct;
    }

    /**
     * @return the productSensitivity
     */
    public ProductSensitivity getProductSensitivity() {
        return productSensitivity;
    }

    /**
     * @param productSensitivity the productSensitivity to set
     */
    public void setProductSensitivity(final ProductSensitivity productSensitivity) {
        this.productSensitivity = productSensitivity;
    }

    /**
     * @return the customerLineNumber
     */
    public String getCustomerLineNumber() {
        return customerLineNumber;
    }

    /**
     * @param customerLineNumber the customerLineNumber to set
     */
    public void setCustomerLineNumber(final String customerLineNumber) {
        this.customerLineNumber = customerLineNumber;
    }

    /**
     * @return the pmAmendState
     */
    public String getPmAmendState() {
        return pmAmendState;
    }

    /**
     * @param pmAmendState the pmAmendState to set
     */
    public void setPmAmendState(final String pmAmendState) {
        this.pmAmendState = pmAmendState;
    }

    /**
     * @return the packType
     */
    public PackType getPackType() {
        return packType;
    }

    /**
     * @param packType the packType to set
     */
    public void setPackType(final PackType packType) {
        this.packType = packType;
    }

    /**
     * @return The PurchasingManger line level cost centre.
     */
    public String getPmLineCostCentre() {
        return pmLineCostCentre;
    }

    /**
     * @param pmLineCostCentre The PurchasingManager line level cost centre.
     */
    public void setPmLineCostCentre(final String pmLineCostCentre) {
        this.pmLineCostCentre = pmLineCostCentre;
    }

    /**
     *
     * @return The uniqueId contains basketLineId
     */
    public String getUniqueId() {
        return uniqueId;
    }

    /**
     *
     * @param uniqueId the uniqueId to set.
     */
    public void setUniqueId(final String uniqueId) {
        this.uniqueId = uniqueId;
    }

    /**
     * @return the quoteLineReference
     */
    public String getLineReference() {
        return lineReference;
    }

    /**
     * @param lineReference the quoteLineReference to set
     */
    public void setLineReference(final String lineReference) {
        this.lineReference = lineReference;
    }

    /**
     * @return the isSelectedPmCostCentre
     */
    public boolean isSelectedPmCostCentre() {
        return isSelectedPmCostCentre;
    }

    /**
     * @param isSelectedPmCostCentre the isSelectedPmCostCentre to set
     */
    public void setSelectedPmCostCentre(final boolean isSelectedPmCostCentre) {
        this.isSelectedPmCostCentre = isSelectedPmCostCentre;
    }

    /**
     * @return the isSelectedPmGLCode
     */
    public boolean isSelectedPmGLCode() {
        return isSelectedPmGLCode;
    }

    /**
     * @param isSelectedPmGLCode the isSelectedPmGLCode to set
     */
    public void setSelectedPmGLCode(final boolean isSelectedPmGLCode) {
        this.isSelectedPmGLCode = isSelectedPmGLCode;
    }

}
