package com.electrocomponents.model.domain.order;

import java.io.Serializable;
import java.util.List;

import com.electrocomponents.model.domain.Currency;
import com.electrocomponents.model.domain.DateTime;
import com.electrocomponents.model.domain.Locale;
import com.electrocomponents.model.domain.Money;
import com.electrocomponents.model.domain.Plant;
import com.electrocomponents.model.domain.Site;
import com.electrocomponents.model.domain.account.Address;
import com.electrocomponents.model.domain.account.CustomerAccount;
import com.electrocomponents.model.domain.account.DeliveryCharge;
import com.electrocomponents.model.domain.config.Country;
import com.electrocomponents.model.domain.discount.MethodOfPlacement;
import com.electrocomponents.model.domain.invoice.TaxNumberTO;
import com.electrocomponents.model.domain.invoice.VatRegisteredInformationTO;
import com.electrocomponents.model.domain.user.User;

/*
 * ************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author  : Ed Ward / Stuart Sim
 * Created : 19 Jul 2007 at 10:53:07
 *
 * ************************************************************************************************
 * Change History
 * ************************************************************************************************
 * Ref      * Who      * Date       * Description
 * ************************************************************************************************
 *          * E0161085 * 27/07/2011 * Added Transaction ID.
 * ************************************************************************************************
 */

/**
 * Represents an order or quote for a set of product items / quantities.
 * @see OrderLineItem
 * @author Ed Ward / Stuart Sim
 */
public class Order implements Serializable {

    /** The SVID. */
    private static final long serialVersionUID = 323297163302041875L;

    /** The date the order was created. */
    private DateTime creationTime;

    /** The date the order was last amended. */
    private DateTime lastModTime;

    /** Placement Date. */
    private DateTime orderDate;

    /** forward date. */
    private DateTime forwardDate;

    /** The web user placing the order. */
    private User user;

    /** The source of the order: Mobile/web/Eproc/PmUser. */
    private EcSystemId ecSystemId;

    
    /** The web locale assocaiated with the opco fulfilling the order. */
    private Locale locale;

    /** The plant within the locale associated with the order. */
    private Plant plant;

    /** The site associated with the opco fulfilling the order. e.g. BE */
    private Site site;

    /** Despatch Type. */
    private DespatchType despatchType;

    /** true if an order is priced, false if one or more Order elements are unpriced. */
    private boolean pricedOrder = true;

    /** Despatch Type. */
    private Currency currency;

    /**
     * Flag to indicate whether a copy of the order should be e-mailed to the order placer. This flag is sent to PurchasingManager to tell
     * PM whether the e-mail should be sent or not. This flag is not (currently) used by the web.
     */
    private Boolean emailOrderCopy = false;

    /** GENERAL ORDER HEADER INFO * */
    /** **************************** */

    /** The order placers general name for the order. */
    private String userOrderName;

    /** The order placers general name for the order. */
    private String userOrderPassword;

    /** The blanket order associated with this order. */
    private BlanketOrder headerBlanketOrder;

    /** Flag to differentiate between pm and user blanket order. */
    private boolean isPmBlanketOrder = false;

    /** The cost centre associated with this order. */
    private CostCentre headerCostCentre;

    /** Flag to differentiate between pm and user cost centre. */
    private boolean isPmCostCentre = false;

    /** The cost centre associated with this order. */
    private GlCode headerGlCode;

    /** Flag to differentiate between pm and user gl code. */
    private boolean isPmGlCode = false;

    /** Electrocomponents marketing publication media code information. */
    private String headerMediaCode;

    /** Promo Code Description. */
    private String promoCodeDescription;

    /** An electronic address ID required in some markets (e.g. Danish Government). */
    private String headerElectronicAddressId;

    /** A unique number representing an Order. */
    private String orderId;

    /** Customer Trade Counter Message. */
    private String customerTradeCounterMessage;

    /** Delivery Recipient Name. */
    private String deliveryRecipientName;

    /** Delivery Recipient PhoneNumber. */
    private String deliveryRecipientPhoneNumber;

    /** Order Placed Flag.. */
    private Boolean orderPlacedFlag;

    /** ACCOUNT AND PAYMENT INFO * */
    /** *************************** */

    /** The SAP contact number or the order placer (if available). */
    private String contactNbr;

    /** The master customer account ("SoldTo" in SAP opocs, account in ROW locales). */
    /** the electonic address id required flag is embedded in this object. */
    private CustomerAccount customerAccount;

    /** The Delivery (shipTo) customer account (only used in SAP opcos). */
    private String shipToCustomerAccountCode;

    /** The Delivery (shipTo) address. */
    private Address deliveryAddress;

    /** The Delivery address id. */
    private String deliveryAddressId;

    /** The Invoice (billTo) customer account (only used in SAP opcos). */
    private String billToCustomerAccountCode;

    /** The Invoice (billTo) address. */
    private Address invoiceAddress;

    /** PAYMENT INFORMATION * */
    /** ********************** */

    /** The Payment Method. */
    private PaymentMethod payment;

    /** The customer order reference payment type (usually CreditC or PCard order ref. */
    private String paymentCustomerOrderReference;

    /** The cost centre for this payment type (usually CreditC or PCard cost centres. */
    private String paymentCostCentre;

    /** The paymentName for this payment. */
    private String paymentName;

    /** PURCHASING MANAGER INFORMATION * */
    /** ******************************** */

    /** The Purchasing Manager Info. */
    private PurchasingManagerOrderInfo purchasingManagerOrderInfo;

    /** STOCKED ORDER LINES TOTALS * */
    /** ***************************** */

    /** Total value of the stocked product lines in the order. */
    private Money stockedGoodsTotal;

    /** Total list value of the stocked product lines in the order. */
    private Money stockedGoodsListTotal;

    /** Total value of order discount applied to the stocked product lines in the order. */
    private Money stockedGoodsOrderDiscountTotal;

    /** The delivery charge applicable to the stocked product portion of this order. */
    private DeliveryCharge stockedDeliveryCharge;

    /** Total value of the VAT payable on the stocked product lines in the order. */
    private Money stockedVatTotal;

    /** The total value of the stocked product portion of the order, including VAT and delivery charges. */
    private Money stockedOrderTotal;

    /** NON-STOCKED ORDER LINES TOTALS * */
    /** ********************************* */

    /** Total value of the non-stocked product lines in the order. */
    private Money nonStockedGoodsTotal;

    /** Total list value of the non-stocked product lines in the order. */
    private Money nonStockedGoodsListTotal;

    /** Total value of order discount applied to the non-stocked product lines in the order. */
    private Money nonStockedGoodsOrderDiscountTotal;

    /** The delivery charge applicable to the non-stocked product portion of this order. */
    private DeliveryCharge nonStockedDeliveryCharge;

    /** Total value of the VAT payable on the non-stocked product lines in the order. */
    private Money nonStockedVatTotal;

    /** The total value of the non-stocked product portion of the order, including VAT and delivery charges. */
    private Money nonStockedOrderTotal;

    /** The total value of the order, including VAT and delivery charges. */
    private Money orderTotal;

    /** The running order total excluding VAT and delivery charges. */
    private Money goodsTotal;

    /**
     * Transaction order total is the amount which paid by user using cyber source payment.
     */
    private Money transactionOrderTotal;

    /** ERS INFORMATION * */
    /** ****************** */

    /** The order MOP, e.g. web, eprocurement etc. */
    @org.hibernate.annotations.Type(type = "method_of_placement")
    private MethodOfPlacement methodOfPlacement;

    /** The order type, e.g. O=order Q=quote etc. */
    private OrderType orderType;

    /** The order stock type type, e.g. S=Stocked, N=Non-stocked, M=Mixed. */
    private OrderStockType orderStockType;

    /** The country (OPCO) that the order has been placed against. */
    private Country ersCountry;

    /** A password that is matched against a predefined value on the ERS. */
    private OrderPasswordMatch ersOrderPasswordMatch;

    /** STOCKED ORDER INFORMATION * */
    /** **************************** */

    /** The master order reference for the stocked portion of the order (if any). */
    private String stockedOrderReference;

    /** The customer order reference for the stocked portion of the order (if any). */
    private String stockedCustomerOrderReference;

    /** The number of stocked OrderLineItems in the order (if any). */
    private Integer stockedNbrOrderLines;

    /** The transfer status for the non-stocked portion of the order (if any). */
    private OrderTransferStatus stockedOrderTransferStatus;

    /** NON-STOCKED ORDER INFORMATION * */
    /** ******************************** */

    /** The master order reference for the stocked portion of the order (if any). */
    private String nonStockedOrderReference;

    /** The customer order reference for the non-stocked portion of the order (if any). */
    private String nonStockedCustomerOrderReference;

    /** The number of non-stocked OrderLineItems in the order (if any). */
    private Integer nonStockedNbrOrderLines;

    /** The transfer status for the non-stocked portion of the order (if any). */
    private OrderTransferStatus nonStockedOrderTransferStatus;

    /** EPROCUREMENT ORDER INFORMATION * */
    /** ********************************* */

    /** The eProcurement system vendor / format for this order. */
    private String vendor;

    /** The eProcurement customer ID for this order. */
    private String customer;

    /** Free text area for customer instructions - eProcurement only. */
    private String customerFreeText;

    /** Generals */

    /** The STORE_ID. */
    private Integer storeId;

    /** ORDER LINE ITEMS * */
    /** ******************* */
    /** A Linked List of product items and quantities to be ordered. */
    private List<OrderLineItem> orderLineItems;

    /** The site url. */
    private String siteUrl;

    /** The site image url. */
    private String siteImageUrl;

    /** ENHANCMENT MESSAGING STARTS HERE * */
    /**
     * Transport type either EMAIL or SMS.
     */
    private MessagingTransportMethod transportType;

    /**
     * Notification type either NONE or ORDER_AND_DISPATCH.
     */
    private MessagingRequirement notificationType;

    /**
     * Notification address is email address if transportType = 'EMAIL' or phone number if transportType = 'SMS'.
     */
    private String notificationAddress;

    /** External system url to pay stocked order payment like Alipay. */
    private String stockedPaymentUrl;

    /** External system url to pay non stocked order payment like Alipay.. */
    private String nonStockedPaymentUrl;

    /** THIRD PARTY PAYMENT / AUTHENTICATION STARTS HERE * */
    /**
     * Third Party Transaction ID.
     */
    private String transactionId;

    /** Parent quote reference. */
    private String parentQuoteReference;

    /**
     * Order Number.
     */
    private String orderNumber;

    /**
     * Single order discount only available for quotes order.
     */
    private Double singleOrderDiscountPercent;

    /**
     *  Flag to identify whether order request is been processed or not.
     */
    private Boolean orderRequestProcessed = Boolean.FALSE;

    /** Vat invoice Address details. */
    private Address vatRegisteredAddress;

    /** Vat invoice reg. number and bank details. */
    private VatRegisteredInformationTO vatRegisteredInformationTO;

    /** VAT invoice tax number details. */
    private TaxNumberTO taxNumberTO;

    /** Customer IP address. */
    private String customerIpAddress;
    /**
     * @return the transportType
     */
    public MessagingTransportMethod getTransportType() {
        return transportType;
    }

    /**
     * @param transportType the transportType to set
     */
    public void setTransportType(final MessagingTransportMethod transportType) {
        this.transportType = transportType;
    }

    /**
     * @return the notificationType
     */
    public MessagingRequirement getNotificationType() {
        return notificationType;
    }

    /**
     * @param notificationType the notificationType to set
     */
    public void setNotificationType(final MessagingRequirement notificationType) {
        this.notificationType = notificationType;
    }

    /**
     * @return the notificationAddress
     */
    public String getNotificationAddress() {
        return notificationAddress;
    }

    /**
     * @param notificationAddress the notificationAddress to set
     */
    public void setNotificationAddress(final String notificationAddress) {
        this.notificationAddress = notificationAddress;
    }

    /**
     * @return forwardDate.
     */
    public DateTime getForwardDate() {
        return forwardDate;
    }

    /**
     * @param forwardDate the forwardDate to set
     */
    public void setForwardDate(final DateTime forwardDate) {
        this.forwardDate = forwardDate;
    }

    /**
     * @return isPmBlanketOrder
     */
    public boolean isPmBlanketOrder() {
        return isPmBlanketOrder;
    }

    /**
     * @param isPmBlanketOrder flag to differentiate between pm and user blanket order
     */
    public void setPmBlanketOrder(final boolean isPmBlanketOrder) {
        this.isPmBlanketOrder = isPmBlanketOrder;
    }

    /**
     * @return isPmCostCentre
     */
    public boolean isPmCostCentre() {
        return isPmCostCentre;
    }

    /**
     * @param isPmCostCentre flag to differentiate between pm and user cost centre
     */
    public void setPmCostCentre(final boolean isPmCostCentre) {
        this.isPmCostCentre = isPmCostCentre;
    }

    /**
     * @return isPmGlCode
     */
    public boolean isPmGlCode() {
        return isPmGlCode;
    }

    /**
     * @param isPmGlCode flag to differentiate between pm and user gl code
     */
    public void setPmGlCode(final boolean isPmGlCode) {
        this.isPmGlCode = isPmGlCode;
    }

    /**
     * @return headerGlCode Gl Code
     */
    public GlCode getHeaderGlCode() {
        return headerGlCode;
    }

    /**
     * @param headerGlCode Gl Code
     */
    public void setHeaderGlCode(final GlCode headerGlCode) {
        this.headerGlCode = headerGlCode;
    }

    /**
     * @return the customerFreeText
     */
    public String getCustomerFreeText() {
        return customerFreeText;
    }

    /**
     * @param customerFreeText the customerFreeText to set
     */
    public void setCustomerFreeText(final String customerFreeText) {
        this.customerFreeText = customerFreeText;
    }

    /**
     * @return the billToCustomerAccountCode
     */
    public String getBillToCustomerAccountCode() {
        return billToCustomerAccountCode;
    }

    /**
     * @param billToCustomerAccountCode the billToCustomerAccountCode to set
     */
    public void setBillToCustomerAccountCode(final String billToCustomerAccountCode) {
        this.billToCustomerAccountCode = billToCustomerAccountCode;
    }

    /**
     * @return the contactNbr
     */
    public String getContactNbr() {
        return contactNbr;
    }

    /**
     * @param contactNbr the contactNbr to set
     */
    public void setContactNbr(final String contactNbr) {
        this.contactNbr = contactNbr;
    }

    /**
     * @return the creationTime
     */
    public DateTime getCreationTime() {
        return creationTime;
    }

    /**
     * @param creationTime the creationTime to set
     */
    public void setCreationTime(final DateTime creationTime) {
        this.creationTime = creationTime;
    }

    /**
     * @return the orderDate
     */
    public DateTime getOrderDate() {
        return orderDate;
    }

    /**
     * @param orderDate the orderDate to set
     */
    public void setOrderDate(final DateTime orderDate) {
        this.orderDate = orderDate;
    }

    /**
     * @return the customer.
     */
    public String getCustomer() {
        return customer;
    }

    /**
     * @param customer the customer to set
     */
    public void setCustomer(final String customer) {
        this.customer = customer;
    }

    /**
     * @return the customerAccount
     */
    public CustomerAccount getCustomerAccount() {
        return customerAccount;
    }

    /**
     * @param customerAccount the customerAccount to set
     */
    public void setCustomerAccount(final CustomerAccount customerAccount) {
        this.customerAccount = customerAccount;
    }

    /**
     * @return the deliveryAddress
     */
    public Address getDeliveryAddress() {
        return deliveryAddress;
    }

    /**
     * @param deliveryAddress the deliveryAddress to set
     */
    public void setDeliveryAddress(final Address deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    /**
     * @return the electronicAddressId
     */
    public String getHeaderElectronicAddressId() {
        return headerElectronicAddressId;
    }

    /**
     * @param headerElectronicAddressId the headerElectronicAddressId to set
     */
    public void setHeaderElectronicAddressId(final String headerElectronicAddressId) {
        this.headerElectronicAddressId = headerElectronicAddressId;
    }

    /**
     * @return the ersCountry
     */
    public Country getErsCountry() {
        return ersCountry;
    }

    /**
     * @param ersCountry the ersCountry to set
     */
    public void setErsCountry(final Country ersCountry) {
        this.ersCountry = ersCountry;
    }

    /**
     * @return the methodOfPlacement
     */
    public MethodOfPlacement getMethodOfPlacement() {
        return methodOfPlacement;
    }

    /**
     * @param methodOfPlacement the methodOfPlacement to set
     */
    public void setMethodOfPlacement(final MethodOfPlacement methodOfPlacement) {
        this.methodOfPlacement = methodOfPlacement;
    }

    /**
     * @return the ersOrderPasswordMatch
     */
    public OrderPasswordMatch getErsOrderPasswordMatch() {
        return ersOrderPasswordMatch;
    }

    /**
     * @param ersOrderPasswordMatch the ersOrderPasswordMatch to set
     */
    public void setErsOrderPasswordMatch(final OrderPasswordMatch ersOrderPasswordMatch) {
        this.ersOrderPasswordMatch = ersOrderPasswordMatch;
    }

    /**
     * @return the headerBlanketOrder
     */
    public BlanketOrder getHeaderBlanketOrder() {
        return headerBlanketOrder;
    }

    /**
     * @param headerBlanketOrder the headerBlanketOrder to set
     */
    public void setHeaderBlanketOrder(final BlanketOrder headerBlanketOrder) {
        this.headerBlanketOrder = headerBlanketOrder;
    }

    /**
     * @return the headerCostCentre
     */
    public CostCentre getHeaderCostCentre() {
        return headerCostCentre;
    }

    /**
     * @param headerCostCentre the headerCostCentre to set
     */
    public void setHeaderCostCentre(final CostCentre headerCostCentre) {
        this.headerCostCentre = headerCostCentre;
    }

    /**
     * @return the invoiceAddress
     */
    public Address getInvoiceAddress() {
        return invoiceAddress;
    }

    /**
     * @param invoiceAddress the invoiceAddress to set
     */
    public void setInvoiceAddress(final Address invoiceAddress) {
        this.invoiceAddress = invoiceAddress;
    }

    /**
     * @return the lastModTime
     */
    public DateTime getLastModTime() {
        return lastModTime;
    }

    /**
     * @param lastModTime the lastModTime to set
     */
    public void setLastModTime(final DateTime lastModTime) {
        this.lastModTime = lastModTime;
    }

    /**
     * @return the locale
     */
    public Locale getLocale() {
        return locale;
    }

    /**
     * @param locale the locale to set
     */
    public void setLocale(final Locale locale) {
        this.locale = locale;
    }

    /**
     * @return the mediaCode
     */
    public String getHeaderMediaCode() {
        return headerMediaCode;
    }

    /**
     * @param headerMediaCode the headerMediaCode to set.
     */
    public void setHeaderMediaCode(final String headerMediaCode) {
        this.headerMediaCode = headerMediaCode;
    }

    /**
     * @return the nonStockedCustomerOrderReference
     */
    public String getNonStockedCustomerOrderReference() {
        return nonStockedCustomerOrderReference;
    }

    /**
     * @param nonStockedCustomerOrderReference the nonStockedCustomerOrderReference to set
     */
    public void setNonStockedCustomerOrderReference(final String nonStockedCustomerOrderReference) {
        this.nonStockedCustomerOrderReference = nonStockedCustomerOrderReference;
    }

    /**
     * @return the nonStockedDeliveryCharge
     */
    public DeliveryCharge getNonStockedDeliveryCharge() {
        return nonStockedDeliveryCharge;
    }

    /**
     * @param nonStockedDeliveryCharge the nonStockedDeliveryCharge to set
     */
    public void setNonStockedDeliveryCharge(final DeliveryCharge nonStockedDeliveryCharge) {
        this.nonStockedDeliveryCharge = nonStockedDeliveryCharge;
    }

    /**
     * @return the nonStockedGoodsOrderDiscountTotal
     */
    public Money getNonStockedGoodsOrderDiscountTotal() {
        return nonStockedGoodsOrderDiscountTotal;
    }

    /**
     * @param nonStockedGoodsOrderDiscountTotal the nonStockedGoodsOrderDiscountTotal to set
     */
    public void setNonStockedGoodsOrderDiscountTotal(final Money nonStockedGoodsOrderDiscountTotal) {
        this.nonStockedGoodsOrderDiscountTotal = nonStockedGoodsOrderDiscountTotal;
    }

    /**
     * @return the nonStockedGoodsTotal
     */
    public Money getNonStockedGoodsTotal() {
        return nonStockedGoodsTotal;
    }

    /**
     * @param nonStockedGoodsTotal the nonStockedGoodsTotal to set
     */
    public void setNonStockedGoodsTotal(final Money nonStockedGoodsTotal) {
        this.nonStockedGoodsTotal = nonStockedGoodsTotal;
    }

    /**
     * @return the nonStockedGoodsListTotal
     */
    public Money getNonStockedGoodsListTotal() {
        return nonStockedGoodsListTotal;
    }

    /**
     * @param nonStockedGoodsListTotal the nonStockedGoodsListTotal to set
     */
    public void setNonStockedGoodsListTotal(final Money nonStockedGoodsListTotal) {
        this.nonStockedGoodsListTotal = nonStockedGoodsListTotal;
    }

    /**
     * @return the nonStockedNbrOrderLines
     */
    public Integer getNonStockedNbrOrderLines() {
        return nonStockedNbrOrderLines;
    }

    /**
     * @param nonStockedNbrOrderLines the nonStockedNbrOrderLines to set
     */
    public void setNonStockedNbrOrderLines(final Integer nonStockedNbrOrderLines) {
        this.nonStockedNbrOrderLines = nonStockedNbrOrderLines;
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

    /**
     * @return the nonStockedOrderTotal
     */
    public Money getNonStockedOrderTotal() {
        return nonStockedOrderTotal;
    }

    /**
     * @param nonStockedOrderTotal the nonStockedOrderTotal to set
     */
    public void setNonStockedOrderTotal(final Money nonStockedOrderTotal) {
        this.nonStockedOrderTotal = nonStockedOrderTotal;
    }

    /**
     * @return the orderTotal
     */
    public Money getOrderTotal() {
        return orderTotal;
    }

    /**
     * @param orderTotal the orderTotal to set
     */
    public void setOrderTotal(final Money orderTotal) {
        this.orderTotal = orderTotal;
    }

    /**
     * @return the nonStockedOrderTransferStatus
     */
    public OrderTransferStatus getNonStockedOrderTransferStatus() {
        return nonStockedOrderTransferStatus;
    }

    /**
     * @param nonStockedOrderTransferStatus the nonStockedOrderTransferStatus to set
     */
    public void setNonStockedOrderTransferStatus(final OrderTransferStatus nonStockedOrderTransferStatus) {
        this.nonStockedOrderTransferStatus = nonStockedOrderTransferStatus;
    }

    /**
     * @return the nonStockedVatTotal
     */
    public Money getNonStockedVatTotal() {
        return nonStockedVatTotal;
    }

    /**
     * @param nonStockedVatTotal the nonStockedVatTotal to set
     */
    public void setNonStockedVatTotal(final Money nonStockedVatTotal) {
        this.nonStockedVatTotal = nonStockedVatTotal;
    }

    /**
     * @return the orderLineItems
     */
    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }

    /**
     * @param orderLineItems the orderLineItems to set
     */
    public void setOrderLineItems(final List<OrderLineItem> orderLineItems) {
        this.orderLineItems = orderLineItems;
    }

    /**
     * @return the orderStockType
     */
    public OrderStockType getOrderStockType() {
        return orderStockType;
    }

    /**
     * @param orderStockType the orderStockType to set
     */
    public void setOrderStockType(final OrderStockType orderStockType) {
        this.orderStockType = orderStockType;
    }

    /**
     * @return the orderType
     */
    public OrderType getOrderType() {
        return orderType;
    }

    /**
     * @param orderType the orderType to set
     */
    public void setOrderType(final OrderType orderType) {
        this.orderType = orderType;
    }

    /**
     * @return the payment
     */
    public PaymentMethod getPayment() {
        return payment;
    }

    /**
     * @param payment the payment to set
     */
    public void setPayment(final PaymentMethod payment) {
        this.payment = payment;
    }

    /**
     * @return the paymentCostCentre
     */
    public String getPaymentCostCentre() {
        return paymentCostCentre;
    }

    /**
     * @param paymentCostCentre the paymentCostCentre to set
     */
    public void setPaymentCostCentre(final String paymentCostCentre) {
        this.paymentCostCentre = paymentCostCentre;
    }

    /**
     * @return the purchasingManagerOrderInfo
     */
    public PurchasingManagerOrderInfo getPurchasingManagerOrderInfo() {
        return purchasingManagerOrderInfo;
    }

    /**
     * @param purchasingManagerOrderInfo the purchasingManagerOrderInfo to set
     */
    public void setPurchasingManagerOrderInfo(final PurchasingManagerOrderInfo purchasingManagerOrderInfo) {
        this.purchasingManagerOrderInfo = purchasingManagerOrderInfo;
    }

    /**
     * @return the paymentCustomerOrderReference
     */
    public String getPaymentCustomerOrderReference() {
        return paymentCustomerOrderReference;
    }

    /**
     * @param paymentCustomerOrderReference the paymentCustomerOrderReference to set
     */
    public void setPaymentCustomerOrderReference(final String paymentCustomerOrderReference) {
        this.paymentCustomerOrderReference = paymentCustomerOrderReference;
    }

    /**
     * @return the plant
     */
    public Plant getPlant() {
        return plant;
    }

    /**
     * @param plant the plant to set
     */
    public void setPlant(final Plant plant) {
        this.plant = plant;
    }

    /**
     * @return the shipToCustomerAccountCode
     */
    public String getShipToCustomerAccountCode() {
        return shipToCustomerAccountCode;
    }

    /**
     * @param shipToCustomerAccountCode the shipToCustomerAccountCode to set
     */
    public void setShipToCustomerAccountCode(final String shipToCustomerAccountCode) {
        this.shipToCustomerAccountCode = shipToCustomerAccountCode;
    }

    /**
     * @return the stockedCustomerOrderReference
     */
    public String getStockedCustomerOrderReference() {
        return stockedCustomerOrderReference;
    }

    /**
     * @param stockedCustomerOrderReference the stockedCustomerOrderReference to set
     */
    public void setStockedCustomerOrderReference(final String stockedCustomerOrderReference) {
        this.stockedCustomerOrderReference = stockedCustomerOrderReference;
    }

    /**
     * @return the stockedDeliveryCharge
     */
    public DeliveryCharge getStockedDeliveryCharge() {
        return stockedDeliveryCharge;
    }

    /**
     * @param stockedDeliveryCharge the stockedDeliveryCharge to set
     */
    public void setStockedDeliveryCharge(final DeliveryCharge stockedDeliveryCharge) {
        this.stockedDeliveryCharge = stockedDeliveryCharge;
    }

    /**
     * @return the stockedGoodsOrderDiscountTotal
     */
    public Money getStockedGoodsOrderDiscountTotal() {
        return stockedGoodsOrderDiscountTotal;
    }

    /**
     * @param stockedGoodsOrderDiscountTotal the stockedGoodsOrderDiscountTotal to set
     */
    public void setStockedGoodsOrderDiscountTotal(final Money stockedGoodsOrderDiscountTotal) {
        this.stockedGoodsOrderDiscountTotal = stockedGoodsOrderDiscountTotal;
    }

    /**
     * @return the stockedGoodsTotal
     */
    public Money getStockedGoodsTotal() {
        return stockedGoodsTotal;
    }

    /**
     * @param stockedGoodsTotal the stockedGoodsTotal to set
     */
    public void setStockedGoodsTotal(final Money stockedGoodsTotal) {
        this.stockedGoodsTotal = stockedGoodsTotal;
    }

    /**
     * @return the stockedGoodsListTotal
     */
    public Money getStockedGoodsListTotal() {
        return stockedGoodsListTotal;
    }

    /**
     * @param stockedGoodsListTotal the stockedGoodsListTotal to set
     */
    public void setStockedGoodsListTotal(final Money stockedGoodsListTotal) {
        this.stockedGoodsListTotal = stockedGoodsListTotal;
    }

    /**
     * @return the stockedNbrOrderLines
     */
    public Integer getStockedNbrOrderLines() {
        return stockedNbrOrderLines;
    }

    /**
     * @param stockedNbrOrderLines the stockedNbrOrderLines to set
     */
    public void setStockedNbrOrderLines(final Integer stockedNbrOrderLines) {
        this.stockedNbrOrderLines = stockedNbrOrderLines;
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
     * @return the stockedOrderTotal
     */
    public Money getStockedOrderTotal() {
        return stockedOrderTotal;
    }

    /**
     * @param stockedOrderTotal the stockedOrderTotal to set
     */
    public void setStockedOrderTotal(final Money stockedOrderTotal) {
        this.stockedOrderTotal = stockedOrderTotal;
    }

    /**
     * @return the stockedOrderTransferStatus
     */
    public OrderTransferStatus getStockedOrderTransferStatus() {
        return stockedOrderTransferStatus;
    }

    /**
     * @param stockedOrderTransferStatus the stockedOrderTransferStatus to set
     */
    public void setStockedOrderTransferStatus(final OrderTransferStatus stockedOrderTransferStatus) {
        this.stockedOrderTransferStatus = stockedOrderTransferStatus;
    }

    /**
     * @return the stockedVatTotal
     */
    public Money getStockedVatTotal() {
        return stockedVatTotal;
    }

    /**
     * @param stockedVatTotal the stockedVatTotal to set
     */
    public void setStockedVatTotal(final Money stockedVatTotal) {
        this.stockedVatTotal = stockedVatTotal;
    }

    /**
     * @return the user
     */
    public User getUser() {
        return user;
    }

    /**
     * @param user the user to set
     */
    public void setUser(final User user) {
        this.user = user;
    }

    /**
     * @return the ecSystemId
     */
    public final EcSystemId getEcSystemId() {
        return ecSystemId;
    }

    /**
     * @param ecSystemId the ecSystemId to set
     */
    public final void setEcSystemId(final EcSystemId ecSystemId) {
        this.ecSystemId = ecSystemId;
    }

    /**
     * @return the userOrderName
     */
    public String getUserOrderName() {
        return userOrderName;
    }

    /**
     * @param userOrderName the userOrderName to set
     */
    public void setUserOrderName(final String userOrderName) {
        this.userOrderName = userOrderName;
    }

    /**
     * @return the vendor
     */
    public String getVendor() {
        return vendor;
    }

    /**
     * @param vendor the vendor to set
     */
    public void setVendor(final String vendor) {
        this.vendor = vendor;
    }

    /**
     * @return the site
     */
    public Site getSite() {
        return site;
    }

    /**
     * @param site the site to set
     */
    public void setSite(final Site site) {
        this.site = site;
    }

    /**
     * @return the orderId
     */
    public String getOrderId() {
        return orderId;
    }

    /**
     * @param orderId the orderId to set
     */
    public void setOrderId(final String orderId) {
        this.orderId = orderId;
    }

    /**
     * @return the customerTradeCounterMessage
     */
    public String getCustomerTradeCounterMessage() {
        return customerTradeCounterMessage;
    }

    /**
     * @param customerTradeCounterMessage the customerTradeCounterMessage to set
     */
    public void setCustomerTradeCounterMessage(final String customerTradeCounterMessage) {
        this.customerTradeCounterMessage = customerTradeCounterMessage;
    }

    /**
     * @return the deliveryRecipientName
     */
    public String getDeliveryRecipientName() {
        return deliveryRecipientName;
    }

    /**
     * @param deliveryRecipientName the deliveryRecipientName to set
     */
    public void setDeliveryRecipientName(final String deliveryRecipientName) {
        this.deliveryRecipientName = deliveryRecipientName;
    }

    /**
     * @return the deliveryRecipientPhoneNumber
     */
    public String getDeliveryRecipientPhoneNumber() {
        return deliveryRecipientPhoneNumber;
    }

    /**
     * @param deliveryRecipientPhoneNumber the deliveryRecipientPhoneNumber to set
     */
    public void setDeliveryRecipientPhoneNumber(final String deliveryRecipientPhoneNumber) {
        this.deliveryRecipientPhoneNumber = deliveryRecipientPhoneNumber;
    }

    /**
     * @return the despatchType
     */
    public DespatchType getDespatchType() {
        return despatchType;
    }

    /**
     * @param despatchType the despatchType to set
     */
    public void setDespatchType(final DespatchType despatchType) {
        this.despatchType = despatchType;
    }

    /**
     * @return the orderPlacedFlag
     */
    public Boolean getOrderPlacedFlag() {
        return orderPlacedFlag;
    }

    /**
     * @param orderPlacedFlag the orderPlacedFlag to set
     */
    public void setOrderPlacedFlag(final Boolean orderPlacedFlag) {
        this.orderPlacedFlag = orderPlacedFlag;
    }

    /**
     * @return the storeId
     */
    public Integer getStoreId() {
        return storeId;
    }

    /**
     * @param storeId the storeId to set
     */
    public void setStoreId(final Integer storeId) {
        this.storeId = storeId;
    }

    /**
     * @return the currency
     */
    public Currency getCurrency() {
        return currency;
    }

    /**
     * @param currency the currency to set
     */
    public void setCurrency(final Currency currency) {
        this.currency = currency;
    }

    /**
     * @return the pricedOrder
     */
    public boolean isPricedOrder() {
        return pricedOrder;
    }

    /**
     * @param pricedOrder the pricedOrder to set
     */
    public void setPricedOrder(final boolean pricedOrder) {
        this.pricedOrder = pricedOrder;
    }

    /**
     * @return the userOrderPassword
     */
    public String getUserOrderPassword() {
        return userOrderPassword;
    }

    /**
     * @param userOrderPassword the userOrderPassword to set
     */
    public void setUserOrderPassword(final String userOrderPassword) {
        this.userOrderPassword = userOrderPassword;
    }

    /**
     * @return Flag indicating whether an e-mail copy of the order is required.
     */
    public Boolean getEmailOrderCopy() {
        return emailOrderCopy;
    }

    /**
     * @param emailOrderCopy Flag indicating whether an e-mail copy of the order is required.
     */
    public void setEmailOrderCopy(final Boolean emailOrderCopy) {
        this.emailOrderCopy = emailOrderCopy;
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
     * @return the transactionId.
     */
    public String getTransactionId() {
        return transactionId;
    }

    /**
     * @param transactionId the transactionId to set.
     */
    public void setTransactionId(final String transactionId) {
        this.transactionId = transactionId;
    }

    /**
     * @return order number
     */
    public String getOrderNumber() {
        return orderNumber;
    }

    /**
     * @param orderNumber order number
     */
    public void setOrderNumber(final String orderNumber) {
        this.orderNumber = orderNumber;
    }

    /**
     * @return order total paid using third party like cyber source
     */
    public Money getTransactionOrderTotal() {
        return transactionOrderTotal;
    }

    /**
     * @param transactionOrderTotal order total paid using third party like cyber source
     */
    public void setTransactionOrderTotal(final Money transactionOrderTotal) {
        this.transactionOrderTotal = transactionOrderTotal;
    }

    /**
     * @return the parentQuoteReference
     */
    public String getParentQuoteReference() {
        return parentQuoteReference;
    }

    /**
     * @param parentQuoteReference the parentQuoteReference to set
     */
    public void setParentQuoteReference(final String parentQuoteReference) {
        this.parentQuoteReference = parentQuoteReference;
    }

    /**
     * @return the goodsTotal.
     */
    public Money getGoodsTotal() {
        return goodsTotal;
    }

    /**
     * @param goodsTotal the goodsTotal to set
     */
    public void setGoodsTotal(final Money goodsTotal) {
        this.goodsTotal = goodsTotal;
    }

    /**
     * @return the singleOrderDiscountPercent
     */
    public Double getSingleOrderDiscountPercent() {
        return singleOrderDiscountPercent;
    }

    /**
     * @param singleOrderDiscountPercent the singleOrderDiscountPercent to set
     */
    public void setSingleOrderDiscountPercent(final Double singleOrderDiscountPercent) {
        this.singleOrderDiscountPercent = singleOrderDiscountPercent;
    }

    /**
     * @return the promoCodeDescription
     */
    public String getPromoCodeDescription() {
        return promoCodeDescription;
    }

    /**
     * @param promoCodeDescription the promoCodeDescription to set
     */
    public void setPromoCodeDescription(final String promoCodeDescription) {
        this.promoCodeDescription = promoCodeDescription;
    }

    /**
     * @return is order request is processed or not.
     */
    public Boolean getOrderRequestProcessed() {
        return orderRequestProcessed;
    }

    /**
     * Set flag "TRUE" on order request is placed successfully.
     * @param orderRequestProcessed orderRequestProcessed.
     */
    public void setOrderRequestProcessed(final Boolean orderRequestProcessed) {
        this.orderRequestProcessed = orderRequestProcessed;
    }

    /**
     * @return the vatRegisteredAddress
     */
    public Address getVatRegisteredAddress() {
        return vatRegisteredAddress;
    }

    /**
     * @param vatRegisteredAddress the vatRegisteredAddress to set
     */
    public void setVatRegisteredAddress(final Address vatRegisteredAddress) {
        this.vatRegisteredAddress = vatRegisteredAddress;
    }

    /**
     * @return the vatRegisteredInformationTO
     */
    public VatRegisteredInformationTO getVatRegisteredInformationTO() {
        return vatRegisteredInformationTO;
    }

    /**
     * @param vatRegisteredInformationTO the vatRegisteredInformationTO to set
     */
    public void setVatRegisteredInformationTO(final VatRegisteredInformationTO vatRegisteredInformationTO) {
        this.vatRegisteredInformationTO = vatRegisteredInformationTO;
    }

    /**
     * @param taxNumberTO the taxNumberTO to set
     */
    public void setTaxNumberTO(final TaxNumberTO taxNumberTO) {
        this.taxNumberTO = taxNumberTO;
    }

    /**
     * @return the taxNumberTO
     */
    public TaxNumberTO getTaxNumberTO() {
        return taxNumberTO;
    }

    /**
     * @return the paymentName
     */
    public final String getPaymentName() {
        return paymentName;
    }

    /**
     * @param paymentName the paymentName to set
     */
    public final void setPaymentName(final String paymentName) {
        this.paymentName = paymentName;
    }

    /**
     * @return the deliveryAddressId
     */
    public final String getDeliveryAddressId() {
        return deliveryAddressId;
    }

    /**
     * @param deliveryAddressId the deliveryAddressId to set
     */
    public final void setDeliveryAddressId(final String deliveryAddressId) {
        this.deliveryAddressId = deliveryAddressId;
    }

	/**
	 * @return the customerIpAddress
	 */
	public String getCustomerIpAddress() {
		return customerIpAddress;
	}

	/**
	 * @param customerIpAddress as String to set
	 */
	public void setCustomerIpAddress(String customerIpAddress) {
		this.customerIpAddress = customerIpAddress;
	}

}
