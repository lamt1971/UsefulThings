package com.electrocomponents.model.domain.order;

import com.electrocomponents.model.domain.DateTime;
import com.electrocomponents.model.domain.paymentmethod.PaymentProcessor;

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
 * PaymentMethod Domain Model to used in OrderPad options Currently All the fields may not be in use.
 */
public interface PaymentMethod {
    /** @return (@link #id) */
    long getId();

    /** @param id (@link #id) */
    void setId(long id);

    /** @return (@link #accountId) */
    long getAccountId();

    /** @param accountId (@link #accountId) */
    void setAccountId(long accountId);

    /** @return ersCode */
    String getPaymentErsCode();

    /** @param ersCode ErsCode. */
    void setPaymentErsCode(String ersCode);

    /** @return (@link #addCountry) */
    String getAddCountry();

    /** @param addCountry (@link #addCountry) */
    void setAddCountry(String addCountry);

    /** @return (@link #addLine1) */
    String getAddLine1();

    /** @param addLine1 (@link #addLine1) */
    void setAddLine1(String addLine1);

    /** @return (@link #addLine2) */
    String getAddLine2();

    /** @param addLine2 (@link #addLine2) */
    void setAddLine2(String addLine2);

    /** @return (@link #addLine3) */
    String getAddLine3();

    /** @param addLine3 (@link #addLine3) */
    void setAddLine3(String addLine3);

    /** @return (@link #addLine4) */
    String getAddLine4();

    /** @param addLine4 (@link #addLine4) */
    void setAddLine4(String addLine4);

    /** @return {@link #addPostCode} */
    String getAddPostCode();

    /** @param addPostCode (@link #addPostCode) */
    void setAddPostCode(String addPostCode);

    /** @return (@link #address) */
    String getAddress();

    /** @param address (@link #address) */
    void setAddress(String address);

    /** @return (@link #address2) */
    String getAddress2();

    /** @param address2 (@link #address2) */
    void setAddress2(String address2);

    /** @return cardNumer */
    String getCardNumber();

    /** @param cardNumber */
    void setCardNumber(String cardNumber);

    /** @return companyName */
    String getCompanyName();

    /** @param companyName */
    void setCompanyName(String companyName);

    /** @return country */
    String getCountry();

    /** @param country */
    void setCountry(String country);

    /** @return creationTime */
    DateTime getCreationTime();

    /** @param creationTime */
    void setCreationTime(DateTime creationTime);

    /** @return deleted */
    long getDeleted();

    /** @param deleted */
    void setDeleted(long deleted);

    /** @return email */
    String getEmail();

    /** @param email */
    void setEmail(String email);

    /** @return expirationDate */
    DateTime getExpirationDate();

    /** @param expirationDate */
    void setExpirationDate(DateTime expirationDate);

    /** @return fax */
    String getFax();

    /** @param fax */
    void setFax(String fax);

    /** @return firstName */
    String getFirstName();

    /** @param firstName */
    void setFirstName(String firstName);

    /** @return ItcCardHolder */
    String getItcCardHolder();

    /** @param itcCardHolder */
    void setItcCardHolder(String itcCardHolder);

    /** @return ItcCardName */
    String getItcCardName();

    /** @param itcCardName */
    void setItcCardName(String itcCardName);

    /** @return ItcCorpCard */
    String getItcCorpCard();

    /** @param itcCorpCard */
    void setItcCorpCard(String itcCorpCard);

    /** @return ItcCostCentre */
    String getItcCostCentre();

    /** @param itcCostCentre */
    void setItcCostCentre(String itcCostCentre);

    /**
     * @return ItcCustRef
     */
    String getItcCustRef();

    /**
     * @param itcCustRef itcCustRef
     */
    void setItcCustRef(String itcCustRef);

    /**
     * @return ItcIssueNum
     */
    Long getItcIssueNum();

    /**
     * @param itcIssueNum itcIssueNum
     */
    void setItcIssueNum(Long itcIssueNum);

    /**
     * @return ItcLast4dig
     */
    String getItcLast4dig();

    /**
     * @param itcLast4dig itcLast4dig
     */
    void setItcLast4dig(String itcLast4dig);

    /**
     * @return ItcSecCode
     */
    String getItcSecCode();

    /**
     * @param itcSecCode itcSecCode
     */
    void setItcSecCode(String itcSecCode);

    /**
     * @return lastModifiedTime
     */
    DateTime getLastModifiedTime();

    /**
     * @param lastModifiedTime lastModifiedTime
     */
    void setLastModifiedTime(DateTime lastModifiedTime);

    /**
     * @return LastName
     */
    String getLastName();

    /**
     * @param lastName lastName
     */
    void setLastName(String lastName);

    /**
     * @return MiddleName
     */
    String getMiddleName();

    /**
     * @param middleName middleName
     */
    void setMiddleName(String middleName);

    /**
     * @return paymentName
     */
    String getPaymentName();

    /**
     * @return Card friendly name
     */
    String getCardFriendlyName();

    /**
     * @param cardFriendlyName Card Friendly name
     */
    void setCardFriendlyName(String cardFriendlyName);

    /**
     * @param paymentName paymentName
     */
    void setPaymentName(String paymentName);

    /**
     * @return PaymentTypeId
     */
    long getPaymentTypeId();

    /**
     * @param paymentTypeId paymentTypeId
     */
    void setPaymentTypeId(long paymentTypeId);

    /**
     * @return phone
     */
    String getPhone();

    /**
     * @param phone phone
     */
    void setPhone(String phone);

    /**
     * @return purchaseLimit
     */
    long getPurchaseLimit();

    /**
     * @param purchaseLimit purchaseLimit
     */
    void setPurchaseLimit(long purchaseLimit);

    /**
     * @return rsAccId
     */
    String getRsAccId();

    /**
     * @param rsAccId rsAccId
     */
    void setRsAccId(String rsAccId);

    /** @param status */
    void setStatus(long status);

    /** @return status status */
    long getStatus();

    /** @return storeId */
    long getStoreId();

    /** @param storeId */
    void setStoreId(long storeId);

    /** @return type */
    String getType();

    /** @param type */
    void setType(String type);

    /** @return validDate */
    DateTime getValidDate();

    /** @param validDate */
    void setValidDate(DateTime validDate);

    /** @return description. */
    String getDescription();

    /** @param description description. */
    void setDescription(String description);

    /**
     * @return the paymentProcessor
     */
    PaymentProcessor getPaymentProcessor();

    /**
     * @param paymentProcessor the paymentProcessor to set
     */
    void setPaymentProcessor(final PaymentProcessor paymentProcessor);

    /**
     * @return the imageName
     */
    String getImageName();

    /**
     * @param imageName the imageName to set
     */
    void setImageName(final String imageName);

    /**
     * @return boolean true if card authorisation is enabled in payment type.
     */
    boolean isPaymentAuthoriseEnabled();

    /**
     * @param isPaymentAuthoriseEnabled boolean true if card authorisation is enabled in payment type.
     */
    void setPaymentAuthoriseEnabled(final boolean isPaymentAuthoriseEnabled);

    /**
     * @return boolean true if payment capture is enabled in payment type.
     */
    boolean isPaymentCaptureEnabled();

    /**
     * @param isPaymentCaptureEnabled boolean true if payment capture is enabled in payment type.
     */
    void setPaymentCaptureEnabled(final boolean isPaymentCaptureEnabled);

    /**
     * Get altText.
     * @return the altText.
     */
    String getAltText();

    /**
     * Set altText.
     * @param altText the altText to set.
     */
    void setAltText(final String altText);

    /**
     * Get Card Logo Image Id.
     * @return the cardLogoImageId.
     */
    String getCardLogoImageId();

    /**
     * Set Card Logo Image Id.
     * @param cardLogoImageId the cardLogoImageId to set.
     */
    void setCardLogoImageId(final String cardLogoImageId);

}
