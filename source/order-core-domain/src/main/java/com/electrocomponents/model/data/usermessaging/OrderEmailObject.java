package com.electrocomponents.model.data.usermessaging;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

/*
 * ************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author  : Ganesh Raut
 * Created : 15 Aug 2007 at 16:09:44
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
 * A class created for EmailService. This OrderEmailObject will held the data from Order object for xml generation
 * @author Ganesh Raut
 */
@XmlRootElement
public class OrderEmailObject implements Serializable {

    /** Serializable id. */
    private static final long serialVersionUID = 2441466104920504500L;

    /** The locale. */
    private String locale;

    // Supplier's Address Related fields
    /** first part of the Company name of the supplier. */
    private String supplierCompanyName1;

    /** first part of the Company name of the supplier. */
    private String supplierCompanyName2;

    /** first line of the address of the supplier. */
    private String supplierLine1;

    /** second line of the address of the supplier. */
    private String supplierLine2;

    /** third line of the address of the supplier. */
    private String supplierLine3;

    /** forth line of the address of the supplier. */
    private String supplierLine4;

    /** Post code of the supplier. */
    private String supplierPostCode;

    /** Country name of the supplier. */
    private String supplierCountry;

    // Customer's Address Related fields
    /** first part of the Company name of the customer. */
    private String customerCompanyName1;

    /** first part of the Company name of the customer. */
    private String customerCompanyName2;

    /** first line of the address of the customer. */
    private String customerLine1;

    /** second line of the address of the customer. */
    private String customerLine2;

    /** third line of the address of the customer. */
    private String customerLine3;

    /** forth line of the address of the customer. */
    private String customerLine4;

    /** Post code of the customer. */
    private String customerPostCode;

    /** Country name of the customer. */
    private String customerCountry;

    // Invoice address details
    /** first part of the Company name of the invoice address. */
    private String invoiceAddrCompanyName1;

    /** first part of the Company name of the invoice address. */
    private String invoiceAddrCompanyName2;

    /** first line of the address of the invoice address. */
    private String invoiceAddrLine1;

    /** second line of the address of the invoice address. */
    private String invoiceAddrLine2;

    /** third line of the address of the invoice address. */
    private String invoiceAddrLine3;

    /** forth line of the address of the invoice address. */
    private String invoiceAddrLine4;

    /** Post code of the invoice address. */
    private String invoiceAddrPostCode;

    /** Country name of the invoice address. */
    private String invoiceAddrCountry;

    // Approver Details
    /** Name of the approver if for PM arrroval. */
    private String approverName;

    /** Value that represents this payment type on the target ERS. */
    private String paymentTypeErsCode;

    /** Value that represents this payment type on the target ERS. */
    private String ebsContact;

    /** The master order reference for the stocked portion of the order (if any). */
    private String stockedOrderReference;

    /** The master order reference for the stocked portion of the order (if any). */
    private String shipToSoldToNumber;

    /** The master order reference for the stocked portion of the order (if any). */
    private String nonStockedOrderReference;

    /** The name of the customer who placed the order. */
    private String orderPlacedBy;

    /** The contact telephone number of the customer who placed the order. */
    private String contactTelephoneNumber;

    /** The contact Email address of the customer who placed the order. */
    private String contactEmailAddress;

    /** The date of order. */
    private String orderDate;

    /** The type of delivery - Next Day / Sat am. */
    private String deliveryType;

    /** The customer order reference for the stocked portion of the order (if any). */
    private String orderNumber;

    /** The order name. */
    private String orderName;

    /** The cost centre associated with this order. */
    private String costCenter;

    /** The blanket order associated with this order. */
    private String blanketOrder;

    /** The customer order reference payment type (usually CreditC or PCard order ref). */
    private String customerReferenceNumber;

    /** Electrocomponents marketing publication media code information. */
    private String promotionCode;

    /** Electrocomponents marketing publication promotion code Description. */
    private String promotionCodeDescription;

    /** The order stock type type, e.g. S=Stocked, N=Non-stocked, M=Mixed. */
    private String orderStockType;

    /** The running total value of the stocked order. */
    private String stockedGoodsTotal;

    /** The delivery charge of the stocked order. */
    private String stockedDeliveryCharge;

    /** The VAT of the stocked order. */
    private String stockedVAT;

    /** The total amount of the stocked order. */
    private String stockedOrderTotal;

    /** The running total value of the stocked order. */
    private String nonStockedGoodsTotal;

    /** The delivery charge of the stocked order. */
    private String nonStockedDeliveryCharge;

    /** The VAT of the stocked order. */
    private String nonStockedVAT;

    /** The total amount of the stocked order. */
    private String nonStockedOrderTotal;

    /** The total amount of the order (All stocked and non-Stocked). */
    private String grandTotal;

    /** The list of line order objects for Stocked Products. */
    private List<LineOrderEmailObject> lineOrderEmailObjectList;

    /** Trade Counter collection time. */
    private String tcCollectionTime;

    /** Whether Order Email should display Customer pricing and Standard Pricing for Stocked Goods. */
    private boolean stockedGoodsShowValueForMoney = false;

    /** Whether Order Email should display Customer pricing and Standard Pricing for non Stocked Goods. */
    private boolean nonStockedGoodsShowValueForMoney = false;

    /** Order Stocked Cart Items discounted price. */
    private String stockedGoodsListDiscountPrice;

    /** Order Non Stocked Cart Items discounted price. */
    private String nonStockedGoodsListDiscountPrice;

    /** The forward order date on which order is delivered. */
    private String forwardOrderDate;

    /** The Order Type ,O - standard order,S - Scheduled order, F- Forward Order. */
    private String orderType;

    /** The site url. */
    private String siteUrl;

    /** The site image url. */
    private String siteImageUrl;

    /** The parent quote reference. */
    private String parentQuoteReference;

    /** payment Provider Image Name(like Alipay). */
    private String paymentProviderImageName;

    /** External system url to pay stocked order payment like Alipay. */
    private String stockedPaymentUrl;

    /** External system url to pay non stocked order payment like Alipay.. */
    private String nonStockedPaymentUrl;

    /** Indicates whether the Payment processor should be displayed on the email. */
    private boolean displayProviderImageToCustomer;

    /** Order discount total. */
    private String orderDiscountTotal;

    /** TAX number 1 (GUI Number). */
    private String guiNumber;

    /** TAX number 2 (Vat registration number). */
    private String vatRegistrationNumber;

    /** GTX Tax number. */
    private String gtsTaxNumber;

    /** VAT registered Bank name. */
    private String vatRegdBankName;

    /** VAT registered bank account number. */
    private String vatRegdBankAccountNumber;

    /** Flag to say if the customer is VAT Registered or not. */
    private Boolean vatCustomer;

    /** vat registered company name. */
    private String vatRegdCompanyName1;

    /** vat registered company alternate name. */
    private String vatRegdCompanyName2;

    /** The first line of the vat registered address. */
    private String vatRegdAddressLine1;

    /** The second line of the vat registered address. */
    private String vatRegdAddressLine2;

    /** The third line of the vat registered address. */
    private String vatRegdAddressLine3;

    /** The forth line of the vat registered address. */
    private String vatRegdAddressLine4;

    /** The post code of the vat registered address. */
    private String vatRegdAddressPostcode;

    /** The Country name of the vat registered address. */
    private String vatRegdAddressCountry;

    /** Field to capture user contact number. */
    private String vatCustomerContactNumber;

    /**
     * @return the paymentProviderImageName
     */
    public String getPaymentProviderImageName() {
        return paymentProviderImageName;
    }

    /**
     * @param paymentProviderImageName the paymentProviderImageName to set
     */
    public void setPaymentProviderImageName(final String paymentProviderImageName) {
        this.paymentProviderImageName = paymentProviderImageName;
    }

    /**
     * @return the approverName
     */
    public String getApproverName() {
        return approverName;
    }

    /**
     * @param approverName the approverName to set
     */
    public void setApproverName(final String approverName) {
        this.approverName = approverName;
    }

    /**
     * @return the orderDate
     */
    public String getOrderDate() {
        return orderDate;
    }

    /**
     * @param orderDate the orderDate to set
     */
    public void setOrderDate(final String orderDate) {
        this.orderDate = orderDate;
    }

    /**
     * @return the blanketOrder
     */
    public String getBlanketOrder() {
        return blanketOrder;
    }

    /**
     * @param blanketOrder the blanketOrder to set
     */
    public void setBlanketOrder(final String blanketOrder) {
        this.blanketOrder = blanketOrder;
    }

    /**
     * @return the contactEmailAddress
     */
    public String getContactEmailAddress() {
        return contactEmailAddress;
    }

    /**
     * @param contactEmailAddress the contactEmailAddress to set
     */
    public void setContactEmailAddress(final String contactEmailAddress) {
        this.contactEmailAddress = contactEmailAddress;
    }

    /**
     * @return the contactTelephoneNumber
     */
    public String getContactTelephoneNumber() {
        return contactTelephoneNumber;
    }

    /**
     * @param contactTelephoneNumber the contactTelephoneNumber to set
     */
    public void setContactTelephoneNumber(final String contactTelephoneNumber) {
        this.contactTelephoneNumber = contactTelephoneNumber;
    }

    /**
     * @return the costCenter
     */
    public String getCostCenter() {
        return costCenter;
    }

    /**
     * @param costCenter the costCenter to set
     */
    public void setCostCenter(final String costCenter) {
        this.costCenter = costCenter;
    }

    /**
     * @return the customerCompanyName1
     */
    public String getCustomerCompanyName1() {
        return customerCompanyName1;
    }

    /**
     * @param customerCompanyName1 the customerCompanyName1 to set
     */
    public void setCustomerCompanyName1(final String customerCompanyName1) {
        this.customerCompanyName1 = customerCompanyName1;
    }

    /**
     * @return the customerCompanyName2
     */
    public String getCustomerCompanyName2() {
        return customerCompanyName2;
    }

    /**
     * @param customerCompanyName2 the customerCompanyName2 to set
     */
    public void setCustomerCompanyName2(final String customerCompanyName2) {
        this.customerCompanyName2 = customerCompanyName2;
    }

    /**
     * @return the customerCountry
     */
    public String getCustomerCountry() {
        return customerCountry;
    }

    /**
     * @param customerCountry the customerCountry to set
     */
    public void setCustomerCountry(final String customerCountry) {
        this.customerCountry = customerCountry;
    }

    /**
     * @return the customerLine1
     */
    public String getCustomerLine1() {
        return customerLine1;
    }

    /**
     * @param customerLine1 the customerLine1 to set
     */
    public void setCustomerLine1(final String customerLine1) {
        this.customerLine1 = customerLine1;
    }

    /**
     * @return the customerLine2
     */
    public String getCustomerLine2() {
        return customerLine2;
    }

    /**
     * @param customerLine2 the customerLine2 to set
     */
    public void setCustomerLine2(final String customerLine2) {
        this.customerLine2 = customerLine2;
    }

    /**
     * @return the customerLine3
     */
    public String getCustomerLine3() {
        return customerLine3;
    }

    /**
     * @param customerLine3 the customerLine3 to set
     */
    public void setCustomerLine3(final String customerLine3) {
        this.customerLine3 = customerLine3;
    }

    /**
     * @return the customerLine4
     */
    public String getCustomerLine4() {
        return customerLine4;
    }

    /**
     * @param customerLine4 the customerLine4 to set
     */
    public void setCustomerLine4(final String customerLine4) {
        this.customerLine4 = customerLine4;
    }

    /**
     * @return the customerPostCode
     */
    public String getCustomerPostCode() {
        return customerPostCode;
    }

    /**
     * @param customerPostCode the customerPostCode to set
     */
    public void setCustomerPostCode(final String customerPostCode) {
        this.customerPostCode = customerPostCode;
    }

    /**
     * @return the customerReferenceNumber
     */
    public String getCustomerReferenceNumber() {
        return customerReferenceNumber;
    }

    /**
     * @param customerReferenceNumber the customerReferenceNumber to set
     */
    public void setCustomerReferenceNumber(final String customerReferenceNumber) {
        this.customerReferenceNumber = customerReferenceNumber;
    }

    /**
     * @return the deliveryType
     */
    public String getDeliveryType() {
        return deliveryType;
    }

    /**
     * @param deliveryType the deliveryType to set
     */
    public void setDeliveryType(final String deliveryType) {
        this.deliveryType = deliveryType;
    }

    /**
     * @return the ebsContact
     */
    public String getEbsContact() {
        return ebsContact;
    }

    /**
     * @param ebsContact the ebsContact to set
     */
    public void setEbsContact(final String ebsContact) {
        this.ebsContact = ebsContact;
    }

    /**
     * @return the grandTotal
     */
    public String getGrandTotal() {
        return grandTotal;
    }

    /**
     * @param grandTotal the grandTotal to set
     */
    public void setGrandTotal(final String grandTotal) {
        this.grandTotal = grandTotal;
    }

    /**
     * @return the lineOrderEmailObjectList
     */
    public List<LineOrderEmailObject> getLineOrderEmailObjectList() {
        return lineOrderEmailObjectList;
    }

    /**
     * @param lineOrderEmailObjectList the lineOrderEmailObjectList to set
     */
    public void setLineOrderEmailObjectList(final List<LineOrderEmailObject> lineOrderEmailObjectList) {
        this.lineOrderEmailObjectList = lineOrderEmailObjectList;
    }

    /**
     * @return the nonStockedOrderReference
     */
    public String getNonStockedOrderReference() {
        return nonStockedOrderReference;
    }

    /**
     * @param nonStockedOrderReference the nonStockedOrderReference to set
     */
    public void setNonStockedOrderReference(final String nonStockedOrderReference) {
        this.nonStockedOrderReference = nonStockedOrderReference;
    }

    // /**
    // * @return the orderDate
    // */
    // public DateTime getOrderDate() {
    // return orderDate;
    // }
    // /**
    // * @param orderDate the orderDate to set
    // */
    // public void setOrderDate(final DateTime orderDate) {
    // this.orderDate = orderDate;
    // }
    /**
     * @return the orderName
     */
    public String getOrderName() {
        return orderName;
    }

    /**
     * @param orderName the orderName to set
     */
    public void setOrderName(final String orderName) {
        this.orderName = orderName;
    }

    /**
     * @return the orderNumber
     */
    public String getOrderNumber() {
        return orderNumber;
    }

    /**
     * @param orderNumber the orderNumber to set
     */
    public void setOrderNumber(final String orderNumber) {
        this.orderNumber = orderNumber;
    }

    /**
     * @return the orderPlacedBy
     */
    public String getOrderPlacedBy() {
        return orderPlacedBy;
    }

    /**
     * @param orderPlacedBy the orderPlacedBy to set
     */
    public void setOrderPlacedBy(final String orderPlacedBy) {
        this.orderPlacedBy = orderPlacedBy;
    }

    /**
     * @return the paymentTypeErsCode
     */
    public String getPaymentTypeErsCode() {
        return paymentTypeErsCode;
    }

    /**
     * @param paymentTypeErsCode the paymentTypeErsCode to set
     */
    public void setPaymentTypeErsCode(final String paymentTypeErsCode) {
        this.paymentTypeErsCode = paymentTypeErsCode;
    }

    /**
     * @return the promotionCode
     */
    public String getPromotionCode() {
        return promotionCode;
    }

    /**
     * @param promotionCode the promotionCode to set
     */
    public void setPromotionCode(final String promotionCode) {
        this.promotionCode = promotionCode;
    }

    /**
     *
     * @return the promotion code description.
     */
    public String getPromotionCodeDescription() {
        return promotionCodeDescription;
    }

    /**
     *
     * @param promotionCodeDescription promotion code description.
     */
    public void setPromotionCodeDescription(final String promotionCodeDescription) {
        this.promotionCodeDescription = promotionCodeDescription;
    }

    /**
     * @return the orderStockType
     */
    public String getOrderStockType() {
        return orderStockType;
    }

    /**
     * @param orderStockType the orderStockType to set
     */
    public void setOrderStockType(final String orderStockType) {
        this.orderStockType = orderStockType;
    }

    /**
     * @return the shipToSoldToNumber
     */
    public String getShipToSoldToNumber() {
        return shipToSoldToNumber;
    }

    /**
     * @param shipToSoldToNumber the shipToSoldToNumber to set
     */
    public void setShipToSoldToNumber(final String shipToSoldToNumber) {
        this.shipToSoldToNumber = shipToSoldToNumber;
    }

    /**
     * @return the stockedOrderReference
     */
    public String getStockedOrderReference() {
        return stockedOrderReference;
    }

    /**
     * @param stockedOrderReference the stockedOrderReference to set
     */
    public void setStockedOrderReference(final String stockedOrderReference) {
        this.stockedOrderReference = stockedOrderReference;
    }

    /**
     * @return the supplierCompanyName1
     */
    public String getSupplierCompanyName1() {
        return supplierCompanyName1;
    }

    /**
     * @param supplierCompanyName1 the supplierCompanyName1 to set
     */
    public void setSupplierCompanyName1(final String supplierCompanyName1) {
        this.supplierCompanyName1 = supplierCompanyName1;
    }

    /**
     * @return the supplierCompanyName2
     */
    public String getSupplierCompanyName2() {
        return supplierCompanyName2;
    }

    /**
     * @param supplierCompanyName2 the supplierCompanyName2 to set
     */
    public void setSupplierCompanyName2(final String supplierCompanyName2) {
        this.supplierCompanyName2 = supplierCompanyName2;
    }

    /**
     * @return the supplierCountry
     */
    public String getSupplierCountry() {
        return supplierCountry;
    }

    /**
     * @param supplierCountry the supplierCountry to set
     */
    public void setSupplierCountry(final String supplierCountry) {
        this.supplierCountry = supplierCountry;
    }

    /**
     * @return the supplierLine1
     */
    public String getSupplierLine1() {
        return supplierLine1;
    }

    /**
     * @param supplierLine1 the supplierLine1 to set
     */
    public void setSupplierLine1(final String supplierLine1) {
        this.supplierLine1 = supplierLine1;
    }

    /**
     * @return the supplierLine2
     */
    public String getSupplierLine2() {
        return supplierLine2;
    }

    /**
     * @param supplierLine2 the supplierLine2 to set
     */
    public void setSupplierLine2(final String supplierLine2) {
        this.supplierLine2 = supplierLine2;
    }

    /**
     * @return the supplierLine3
     */
    public String getSupplierLine3() {
        return supplierLine3;
    }

    /**
     * @param supplierLine3 the supplierLine3 to set
     */
    public void setSupplierLine3(final String supplierLine3) {
        this.supplierLine3 = supplierLine3;
    }

    /**
     * @return the supplierLine4
     */
    public String getSupplierLine4() {
        return supplierLine4;
    }

    /**
     * @param supplierLine4 the supplierLine4 to set
     */
    public void setSupplierLine4(final String supplierLine4) {
        this.supplierLine4 = supplierLine4;
    }

    /**
     * @return the supplierPostCode
     */
    public String getSupplierPostCode() {
        return supplierPostCode;
    }

    /**
     * @param supplierPostCode the supplierPostCode to set
     */
    public void setSupplierPostCode(final String supplierPostCode) {
        this.supplierPostCode = supplierPostCode;
    }

    /**
     * @return the nonStockedDeliveryCharge
     */
    public String getNonStockedDeliveryCharge() {
        return nonStockedDeliveryCharge;
    }

    /**
     * @param nonStockedDeliveryCharge the nonStockedDeliveryCharge to set
     */
    public void setNonStockedDeliveryCharge(final String nonStockedDeliveryCharge) {
        this.nonStockedDeliveryCharge = nonStockedDeliveryCharge;
    }

    /**
     * @return the nonStockedOrderTotal
     */
    public String getNonStockedOrderTotal() {
        return nonStockedOrderTotal;
    }

    /**
     * @param nonStockedOrderTotal the nonStockedOrderTotal to set
     */
    public void setNonStockedOrderTotal(final String nonStockedOrderTotal) {
        this.nonStockedOrderTotal = nonStockedOrderTotal;
    }

    /**
     * @return the nonStockedGoodsTotal
     */
    public String getNonStockedGoodsTotal() {
        return nonStockedGoodsTotal;
    }

    /**
     * @param nonStockedGoodsTotal the nonStockedGoodsTotal to set
     */
    public void setNonStockedGoodsTotal(final String nonStockedGoodsTotal) {
        this.nonStockedGoodsTotal = nonStockedGoodsTotal;
    }

    /**
     * @return the nonStockedVAT
     */
    public String getNonStockedVAT() {
        return nonStockedVAT;
    }

    /**
     * @param nonStockedVAT the nonStockedVAT to set
     */
    public void setNonStockedVAT(final String nonStockedVAT) {
        this.nonStockedVAT = nonStockedVAT;
    }

    /**
     * @return the stockedDeliveryCharge
     */
    public String getStockedDeliveryCharge() {
        return stockedDeliveryCharge;
    }

    /**
     * @param stockedDeliveryCharge the stockedDeliveryCharge to set
     */
    public void setStockedDeliveryCharge(final String stockedDeliveryCharge) {
        this.stockedDeliveryCharge = stockedDeliveryCharge;
    }

    /**
     * @return the stockedOrderTotal
     */
    public String getStockedOrderTotal() {
        return stockedOrderTotal;
    }

    /**
     * @param stockedOrderTotal the stockedOrderTotal to set
     */
    public void setStockedOrderTotal(final String stockedOrderTotal) {
        this.stockedOrderTotal = stockedOrderTotal;
    }

    /**
     * @return the stockedGoodsTotal
     */
    public String getStockedGoodsTotal() {
        return stockedGoodsTotal;
    }

    /**
     * @param stockedGoodsTotal the stockedGoodsTotal to set
     */
    public void setStockedGoodsTotal(final String stockedGoodsTotal) {
        this.stockedGoodsTotal = stockedGoodsTotal;
    }

    /**
     * @return the stockedVAT
     */
    public String getStockedVAT() {
        return stockedVAT;
    }

    /**
     * @param stockedVAT the stockedVAT to set
     */
    public void setStockedVAT(final String stockedVAT) {
        this.stockedVAT = stockedVAT;
    }

    /**
     * @return the invoiceAddrCompanyName1
     */
    public String getInvoiceAddrCompanyName1() {
        return invoiceAddrCompanyName1;
    }

    /**
     * @param invoiceAddrCompanyName1 the invoiceAddrCompanyName1 to set
     */
    public void setInvoiceAddrCompanyName1(final String invoiceAddrCompanyName1) {
        this.invoiceAddrCompanyName1 = invoiceAddrCompanyName1;
    }

    /**
     * @return the invoiceAddrCompanyName2
     */
    public String getInvoiceAddrCompanyName2() {
        return invoiceAddrCompanyName2;
    }

    /**
     * @param invoiceAddrCompanyName2 the invoiceAddrCompanyName2 to set
     */
    public void setInvoiceAddrCompanyName2(final String invoiceAddrCompanyName2) {
        this.invoiceAddrCompanyName2 = invoiceAddrCompanyName2;
    }

    /**
     * @return the invoiceAddrCountry
     */
    public String getInvoiceAddrCountry() {
        return invoiceAddrCountry;
    }

    /**
     * @param invoiceAddrCountry the invoiceAddrCountry to set
     */
    public void setInvoiceAddrCountry(final String invoiceAddrCountry) {
        this.invoiceAddrCountry = invoiceAddrCountry;
    }

    /**
     * @return the invoiceAddrLine1
     */
    public String getInvoiceAddrLine1() {
        return invoiceAddrLine1;
    }

    /**
     * @param invoiceAddrLine1 the invoiceAddrLine1 to set
     */
    public void setInvoiceAddrLine1(final String invoiceAddrLine1) {
        this.invoiceAddrLine1 = invoiceAddrLine1;
    }

    /**
     * @return the invoiceAddrLine2
     */
    public String getInvoiceAddrLine2() {
        return invoiceAddrLine2;
    }

    /**
     * @param invoiceAddrLine2 the invoiceAddrLine2 to set
     */
    public void setInvoiceAddrLine2(final String invoiceAddrLine2) {
        this.invoiceAddrLine2 = invoiceAddrLine2;
    }

    /**
     * @return the invoiceAddrLine3
     */
    public String getInvoiceAddrLine3() {
        return invoiceAddrLine3;
    }

    /**
     * @param invoiceAddrLine3 the invoiceAddrLine3 to set
     */
    public void setInvoiceAddrLine3(final String invoiceAddrLine3) {
        this.invoiceAddrLine3 = invoiceAddrLine3;
    }

    /**
     * @return the invoiceAddrLine4
     */
    public String getInvoiceAddrLine4() {
        return invoiceAddrLine4;
    }

    /**
     * @param invoiceAddrLine4 the invoiceAddrLine4 to set
     */
    public void setInvoiceAddrLine4(final String invoiceAddrLine4) {
        this.invoiceAddrLine4 = invoiceAddrLine4;
    }

    /**
     * @return the invoiceAddrPostCode
     */
    public String getInvoiceAddrPostCode() {
        return invoiceAddrPostCode;
    }

    /**
     * @param invoiceAddrPostCode the invoiceAddrPostCode to set
     */
    public void setInvoiceAddrPostCode(final String invoiceAddrPostCode) {
        this.invoiceAddrPostCode = invoiceAddrPostCode;
    }

    /**
     * @return the tcCollectionTime
     */
    public String getTcCollectionTime() {
        return tcCollectionTime;
    }

    /**
     * @param tcCollectionTime the tcCollectionTime to set
     */
    public void setTcCollectionTime(final String tcCollectionTime) {
        this.tcCollectionTime = tcCollectionTime;
    }

    /**
     * @return the stockedGoodsShowValueForMoney
     */
    public boolean isStockedGoodsShowValueForMoney() {
        return stockedGoodsShowValueForMoney;
    }

    /**
     * @param stockedGoodsShowValueForMoney the stockedGoodsShowValueForMoney to set
     */
    public void setStockedGoodsShowValueForMoney(final boolean stockedGoodsShowValueForMoney) {
        this.stockedGoodsShowValueForMoney = stockedGoodsShowValueForMoney;
    }

    /**
     * @return the nonStockedGoodsShowValueForMoney
     */
    public boolean isNonStockedGoodsShowValueForMoney() {
        return nonStockedGoodsShowValueForMoney;
    }

    /**
     * @param nonStockedGoodsShowValueForMoney the nonStockedGoodsShowValueForMoney to set
     */
    public void setNonStockedGoodsShowValueForMoney(final boolean nonStockedGoodsShowValueForMoney) {
        this.nonStockedGoodsShowValueForMoney = nonStockedGoodsShowValueForMoney;
    }

    /**
     * @return the nonStockedGoodsListDiscountPrice
     */
    public String getNonStockedGoodsListDiscountPrice() {
        return nonStockedGoodsListDiscountPrice;
    }

    /**
     * @param nonStockedGoodsListDiscountPrice the nonStockedGoodsListDiscountPrice to set
     */
    public void setNonStockedGoodsListDiscountPrice(final String nonStockedGoodsListDiscountPrice) {
        this.nonStockedGoodsListDiscountPrice = nonStockedGoodsListDiscountPrice;
    }

    /**
     * @return the stockedGoodsListDiscountPrice
     */
    public String getStockedGoodsListDiscountPrice() {
        return stockedGoodsListDiscountPrice;
    }

    /**
     * @param stockedGoodsListDiscountPrice the stockedGoodsListDiscountPrice to set
     */
    public void setStockedGoodsListDiscountPrice(final String stockedGoodsListDiscountPrice) {
        this.stockedGoodsListDiscountPrice = stockedGoodsListDiscountPrice;
    }

    /**
     * @return forwardOrderDate
     */
    public String getForwardOrderDate() {
        return forwardOrderDate;
    }

    /**
     * @param forwardOrderDate String
     */
    public void setForwardOrderDate(final String forwardOrderDate) {
        this.forwardOrderDate = forwardOrderDate;
    }

    /**
     * @return the orderType
     */
    public String getOrderType() {
        return orderType;
    }

    /**
     * @param orderType the orderType to set
     */
    public void setOrderType(final String orderType) {
        this.orderType = orderType;
    }

    /**
     * @param locale The locale
     */
    public void setLocale(final String locale) {
        this.locale = locale;
    }

    /**
     * @return The locale
     */
    public String getLocale() {
        return locale;
    }

    /**
     * @return the siteImageUrl
     */
    public String getSiteImageUrl() {
        return siteImageUrl;
    }

    /**
     * @param siteImageUrl the siteImageUrl to set
     */
    public void setSiteImageUrl(final String siteImageUrl) {
        this.siteImageUrl = siteImageUrl;
    }

    /**
     * @return the siteUrl
     */
    public String getSiteUrl() {
        return siteUrl;
    }

    /**
     * @param siteUrl the siteUrl to set
     */
    public void setSiteUrl(final String siteUrl) {
        this.siteUrl = siteUrl;
    }

    /**
     * @return the parentQuoteReference
     */
    public final String getParentQuoteReference() {
        return parentQuoteReference;
    }

    /**
     * @param parentQuoteReference the parentQuoteReference to set
     */
    public final void setParentQuoteReference(final String parentQuoteReference) {
        this.parentQuoteReference = parentQuoteReference;
    }

    /**
     * @return External system url to pay stocked order payment like Alipay.
     */
    public String getStockedPaymentUrl() {
        return stockedPaymentUrl;
    }

    /**
     * @param stockedPaymentUrl External system url to pay stocked order payment like Alipay.
     */
    public void setStockedPaymentUrl(final String stockedPaymentUrl) {
        this.stockedPaymentUrl = stockedPaymentUrl;
    }

    /**
     * @return External system url to pay stocked order payment like Alipay.
     */
    public String getNonStockedPaymentUrl() {
        return nonStockedPaymentUrl;
    }

    /**
     * @param nonStockedPaymentUrl External system url to pay stocked order payment like Alipay.
     */
    public void setNonStockedPaymentUrl(final String nonStockedPaymentUrl) {
        this.nonStockedPaymentUrl = nonStockedPaymentUrl;
    }

    /**
     * @return the displayProviderImageToCustomer
     */
    public boolean isDisplayProviderImageToCustomer() {
        return displayProviderImageToCustomer;
    }

    /**
     * @param displayProviderImageToCustomer the displayProviderImageToCustomer to set
     */
    public void setDisplayProviderImageToCustomer(final boolean displayProviderImageToCustomer) {
        this.displayProviderImageToCustomer = displayProviderImageToCustomer;
    }

    /**
     * @return the orderDiscountTotal
     */
    public String getOrderDiscountTotal() {
        return orderDiscountTotal;
    }

    /**
     * @param orderDiscountTotal the orderDiscountTotal to set
     */
    public void setOrderDiscountTotal(final String orderDiscountTotal) {
        this.orderDiscountTotal = orderDiscountTotal;
    }

    /**
     * @param guiNumber the guiNumber to set
     */
    public void setGuiNumber(final String guiNumber) {
        this.guiNumber = guiNumber;
    }

    /**
     * @return the guiNumber
     */
    public String getGuiNumber() {
        return guiNumber;
    }

    /**
     * @param vatRegistrationNumber the vatRegistrationNumber to set
     */
    public void setVatRegistrationNumber(final String vatRegistrationNumber) {
        this.vatRegistrationNumber = vatRegistrationNumber;
    }

    /**
     * @return the vatRegistrationNumber
     */
    public String getVatRegistrationNumber() {
        return vatRegistrationNumber;
    }

    /**
     * @return the vatRegdBankName
     */
    public String getVatRegdBankName() {
        return vatRegdBankName;
    }

    /**
     * @param vatRegdBankName the vatRegdBankName to set
     */
    public void setVatRegdBankName(final String vatRegdBankName) {
        this.vatRegdBankName = vatRegdBankName;
    }

    /**
     * @return the vatRegdBankAccountNumber
     */
    public String getVatRegdBankAccountNumber() {
        return vatRegdBankAccountNumber;
    }

    /**
     * @param vatRegdBankAccountNumber the vatRegdBankAccountNumber to set
     */
    public void setVatRegdBankAccountNumber(final String vatRegdBankAccountNumber) {
        this.vatRegdBankAccountNumber = vatRegdBankAccountNumber;
    }

    /**
     * @return the vatCustomer
     */
    public Boolean getVatCustomer() {
        return vatCustomer;
    }

    /**
     * @param vatCustomer the vatCustomer to set
     */
    public void setVatCustomer(final Boolean vatCustomer) {
        this.vatCustomer = vatCustomer;
    }

    /**
     * @param gtsTaxNumber the gtsTaxNumber to set
     */
    public void setGtsTaxNumber(final String gtsTaxNumber) {
        this.gtsTaxNumber = gtsTaxNumber;
    }

    /**
     * @return the gtsTaxNumber
     */
    public String getGtsTaxNumber() {
        return gtsTaxNumber;
    }

    /**
     * @return the vatRegdCompanyName1
     */
    public String getVatRegdCompanyName1() {
        return vatRegdCompanyName1;
    }

    /**
     * @param vatRegdCompanyName1 the vatRegdCompanyName1 to set
     */
    public void setVatRegdCompanyName1(final String vatRegdCompanyName1) {
        this.vatRegdCompanyName1 = vatRegdCompanyName1;
    }

    /**
     * @return the vatRegdCompanyName2
     */
    public String getVatRegdCompanyName2() {
        return vatRegdCompanyName2;
    }

    /**
     * @param vatRegdCompanyName2 the vatRegdCompanyName2 to set
     */
    public void setVatRegdCompanyName2(final String vatRegdCompanyName2) {
        this.vatRegdCompanyName2 = vatRegdCompanyName2;
    }

    /**
     * @return the vatRegdAddressLine1
     */
    public String getVatRegdAddressLine1() {
        return vatRegdAddressLine1;
    }

    /**
     * @param vatRegdAddressLine1 the vatRegdAddressLine1 to set
     */
    public void setVatRegdAddressLine1(final String vatRegdAddressLine1) {
        this.vatRegdAddressLine1 = vatRegdAddressLine1;
    }

    /**
     * @return the vatRegdAddressLine2
     */
    public String getVatRegdAddressLine2() {
        return vatRegdAddressLine2;
    }

    /**
     * @param vatRegdAddressLine2 the vatRegdAddressLine2 to set
     */
    public void setVatRegdAddressLine2(final String vatRegdAddressLine2) {
        this.vatRegdAddressLine2 = vatRegdAddressLine2;
    }

    /**
     * @return the vatRegdAddressLine3
     */
    public String getVatRegdAddressLine3() {
        return vatRegdAddressLine3;
    }

    /**
     * @param vatRegdAddressLine3 the vatRegdAddressLine3 to set
     */
    public void setVatRegdAddressLine3(final String vatRegdAddressLine3) {
        this.vatRegdAddressLine3 = vatRegdAddressLine3;
    }

    /**
     * @return the vatRegdAddressLine4
     */
    public String getVatRegdAddressLine4() {
        return vatRegdAddressLine4;
    }

    /**
     * @param vatRegdAddressLine4 the vatRegdAddressLine4 to set
     */
    public void setVatRegdAddressLine4(final String vatRegdAddressLine4) {
        this.vatRegdAddressLine4 = vatRegdAddressLine4;
    }

    /**
     * @return the vatRegdAddressPostcode
     */
    public String getVatRegdAddressPostcode() {
        return vatRegdAddressPostcode;
    }

    /**
     * @param vatRegdAddressPostcode the vatRegdAddressPostcode to set
     */
    public void setVatRegdAddressPostcode(final String vatRegdAddressPostcode) {
        this.vatRegdAddressPostcode = vatRegdAddressPostcode;
    }

    /**
     * @return the vatRegdAddressCountry
     */
    public String getVatRegdAddressCountry() {
        return vatRegdAddressCountry;
    }

    /**
     * @param vatRegdAddressCountry the vatRegdAddressCountry to set
     */
    public void setVatRegdAddressCountry(final String vatRegdAddressCountry) {
        this.vatRegdAddressCountry = vatRegdAddressCountry;
    }

    /**
     * @param vatCustomerContactNumber the vatCustomerContactNumber to set
     */
    public void setVatCustomerContactNumber(final String vatCustomerContactNumber) {
        this.vatCustomerContactNumber = vatCustomerContactNumber;
    }

    /**
     * @return the vatCustomerContactNumber
     */
    public String getVatCustomerContactNumber() {
        return vatCustomerContactNumber;
    }
}
