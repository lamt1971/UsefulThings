package com.electrocomponents.model.domain.invoice;

import java.io.Serializable;

/*
 * ************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author  : Sudha Ramasamy
 * Created : 18 Oct 2013
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
 * A domain model interface representing VAT Invoice Related Details.
 * @author Sudha Ramasamy
 */

public class VatRegisteredInformationTO implements Serializable {

    /** Serial Version UID.*/
    private static final long serialVersionUID = -5158734207210504197L;

    /** GTS Tax number.*/
    private String gtsTaxNumber;

    /** Registered Bank name.*/
    private String bankName;

    /** Registered Bank account number.*/
    private String bankAccountNumber;

    /** Flag to say if the customer is VAT Registered or not. */
    private boolean vatCustomer;

    /** Flag indicating whether to use existing delivery address. */
    private boolean useDeliveryAddress;

    /** Field to capture user contact number. */
    private String contactNumber;

    /**
     * @return the gtsTaxNumber
     */
    public String getGtsTaxNumber() {
        return gtsTaxNumber;
    }

    /**
     * @param gtsTaxNumber the gtsTaxNumber to set
     */
    public void setGtsTaxNumber(final String gtsTaxNumber) {
        this.gtsTaxNumber = gtsTaxNumber;
    }

    /**
     * @return the bankName
     */
    public String getBankName() {
        return bankName;
    }

    /**
     * @param bankName the bankName to set
     */
    public void setBankName(final String bankName) {
        this.bankName = bankName;
    }

    /**
     * @return the bankAccountNumber
     */
    public String getBankAccountNumber() {
        return bankAccountNumber;
    }

    /**
     * @param bankAccountNumber the bankAccountNumber to set
     */
    public void setBankAccountNumber(final String bankAccountNumber) {
        this.bankAccountNumber = bankAccountNumber;
    }

    /**
     * @return the vatCustomer
     */
    public boolean isVatCustomer() {
        return vatCustomer;
    }

    /**
     * @param vatCustomer the vatCustomer to set
     */
    public void setVatCustomer(final boolean vatCustomer) {
        this.vatCustomer = vatCustomer;
    }

    /**
     * @return the useDeliveryAddress
     */
    public boolean isUseDeliveryAddress() {
        return useDeliveryAddress;
    }

    /**
     * @param useDeliveryAddress the useDeliveryAddress to set
     */
    public void setUseDeliveryAddress(final boolean useDeliveryAddress) {
        this.useDeliveryAddress = useDeliveryAddress;
    }

    /**
     * @param contactNumber the contactNumber to set
     */
    public void setContactNumber(final String contactNumber) {
        this.contactNumber = contactNumber;
    }

    /**
     * @return the contactNumber
     */
    public String getContactNumber() {
        return contactNumber;
    }

}
