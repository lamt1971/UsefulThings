package com.electrocomponents.model.domain;

/**
 * <pre>
 * ************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author  : Sanjay Zende
 * Created : 18 Dec 2009 at 10:26:04
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
 * Class to give date range validation for the date range search functionality like search orders by start date and end date.
 * @author s.zende
 */
public class DateValidationStatus {

    /**
     * Flag to check if start date is null or empty.
     */
    private boolean isEmptyStartDate;

    /**
     * Flag to check if end date is null or empty.
     */
    private boolean isEmptyEndDate;

    /**
     * Flag to check if start date in future.
     */
    private boolean isStartDateInFuture;

    /**
     * Flag to check if end date in future.
     */
    private boolean isEndDateInFuture;

    /**
     * Flag to check if start date after end date.
     */
    private boolean isStartDateAfterEndDate;

    /**
     * Flag to check if start date in valid date format.
     */
    private boolean isStartDateInValidDateFormat;

    /**
     * Flag to check if end date is in valid date format.
     */
    private boolean isEndDateInValidDateFormat;

    /**
     * Flag to check if end date is in valid date format.
     */
    private boolean isValidDateRange;

    /**
     * @return isEmptyStartDate
     */
    public boolean isEmptyStartDate() {
        return isEmptyStartDate;
    }

    /**
     * @param isEmptyStartDate flag to check if start date is null or empty.
     */
    public void setEmptyStartDate(final boolean isEmptyStartDate) {
        this.isEmptyStartDate = isEmptyStartDate;
    }

    /**
     * @return isEmptyEndDate
     */
    public boolean isEmptyEndDate() {
        return isEmptyEndDate;
    }

    /**
     * @param isEmptyEndDate flag to check if end date is null or empty.
     */
    public void setEmptyEndDate(final boolean isEmptyEndDate) {
        this.isEmptyEndDate = isEmptyEndDate;
    }

    /**
     * @return isStartDateInFuture
     */
    public boolean isStartDateInFuture() {
        return isStartDateInFuture;
    }

    /**
     * @param isStartDateInFuture flag to check if start date in future.
     */
    public void setStartDateInFuture(final boolean isStartDateInFuture) {
        this.isStartDateInFuture = isStartDateInFuture;
    }

    /**
     * @return isEndDateInFuture
     */
    public boolean isEndDateInFuture() {
        return isEndDateInFuture;
    }

    /**
     * @param isEndDateInFuture flag to check if start date in future.
     */
    public void setEndDateInFuture(final boolean isEndDateInFuture) {
        this.isEndDateInFuture = isEndDateInFuture;
    }

    /**
     * @return isStartDateAfterEndDate
     */
    public boolean isStartDateAfterEndDate() {
        return isStartDateAfterEndDate;
    }

    /**
     * @param isStartDateAfterEndDate flag to check if start date after end date.
     */
    public void setStartDateAfterEndDate(final boolean isStartDateAfterEndDate) {
        this.isStartDateAfterEndDate = isStartDateAfterEndDate;
    }

    /**
     * @return isStartDateInValidDateFormat
     */
    public boolean isStartDateInValidDateFormat() {
        return isStartDateInValidDateFormat;
    }

    /**
     * @param isStartDateInValidDateFormat flag to check if start date in valid date format.
     */
    public void setStartDateInValidDateFormat(final boolean isStartDateInValidDateFormat) {
        this.isStartDateInValidDateFormat = isStartDateInValidDateFormat;
    }

    /**
     * @return isEndDateInValidDateFormat
     */
    public boolean isEndDateInValidDateFormat() {
        return isEndDateInValidDateFormat;
    }

    /**
     * @param isEndDateInValidDateFormat flag to check if end date is in valid date format.
     */
    public void setEndDateInValidDateFormat(final boolean isEndDateInValidDateFormat) {
        this.isEndDateInValidDateFormat = isEndDateInValidDateFormat;
    }

    /**
     * @return isValidDateRange
     */
    public boolean isValidDateRange() {
        return isValidDateRange;
    }

    /**
     * @param isValidDateRange flag to check if there is any validation error.
     */
    public void setValidDateRange(final boolean isValidDateRange) {
        this.isValidDateRange = isValidDateRange;
    }

    /**
     * Resets the DateValidationStatus flags to default.
     */
    public void clear() {
        this.isEmptyStartDate = Boolean.FALSE;
        this.isEmptyEndDate = Boolean.FALSE;
        this.isStartDateInFuture = Boolean.FALSE;
        this.isEndDateInFuture = Boolean.FALSE;
        this.isStartDateAfterEndDate = Boolean.FALSE;
        this.isStartDateInValidDateFormat = Boolean.FALSE;
        this.isEndDateInValidDateFormat = Boolean.FALSE;
        this.isValidDateRange = Boolean.FALSE;
    }
}
