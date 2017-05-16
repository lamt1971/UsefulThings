package com.electrocomponents.model.domain.invoice;

import java.io.Serializable;

/**
 * A domain model interface representing TaxNumbers for VAT Invoice.
 * @author Sudha Ramasamy
 */

public class TaxNumberTO implements Serializable {

    /** Serial Version UID.*/
    private static final long serialVersionUID = 7162346918111731234L;

    /** Tax Number 1 (GUI number).*/
    private String guiNumber;

    /** Tax Number 2 (VAT registration number).*/
    private String vatRegistrationNumber;

    /** TAX number 3 (Registered Company ID). */
    private String regdCompanyId;

    /**
     * @return the guiNumber
     */
    public String getGuiNumber() {
        return guiNumber;
    }

    /**
     * @param guiNumber the guiNumber to set
     */
    public void setGuiNumber(final String guiNumber) {
        this.guiNumber = guiNumber;
    }

    /**
     * @return the vatRegistrationNumber
     */
    public String getVatRegistrationNumber() {
        return vatRegistrationNumber;
    }

    /**
     * @param vatRegistrationNumber the vatRegistrationNumber to set
     */
    public void setVatRegistrationNumber(final String vatRegistrationNumber) {
        this.vatRegistrationNumber = vatRegistrationNumber;
    }

    /**
     * @return the regdCompanyId
     */
    public String getRegdCompanyId() {
        return regdCompanyId;
    }

    /**
     * @param regdCompanyId the regdCompanyId to set
     */
    public void setRegdCompanyId(final String regdCompanyId) {
        this.regdCompanyId = regdCompanyId;
    }

}
