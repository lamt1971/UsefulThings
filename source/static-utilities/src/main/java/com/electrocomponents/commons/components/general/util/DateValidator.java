/**
 *
 */
package com.electrocomponents.commons.components.general.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.electrocomponents.model.domain.DateTime;
import com.electrocomponents.model.domain.DateValidationStatus;

/**
 * <pre>
 * ************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author  : Sanjay Zende
 * Created : 16 Dec 2009 at 09:17:50
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
 * This class is responsible for the date validation operations.
 * @author s.zende
 */
public final class DateValidator {

    /** Commons Logger. */
    private static final Log LOG = LogFactory.getLog(DateValidator.class);

    /** The singleton instance of DateValidator. */
    private static DateValidator dateValidator;

    /**
     * Simple date format used for date formatting.
     */
    private static final String DATE_FORMAT = "dd/MM/yyyy";

    /** Constructor. */
    private DateValidator() {

    }

    /**
     * @return the instance of DateValidator
     */
    public static DateValidator getInstance() {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Method Start");
        }
        if (dateValidator == null) {
            dateValidator = new DateValidator();
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("Method Finish");
        }

        return dateValidator;
    }

    /**
     * This method validates start and end date string for the date range search functionality. start date and end date strings should not
     * be null or empty.Start date and end date should be able to parse into valid date format. start date and end date should not be after
     * current date. And start date should not be after end date.
     * @param startDateStr startDate string
     * @param endDateStr end date string
     * @return true for the valid date range or else false
     */
    public DateValidationStatus validateDateRange(final String startDateStr, final String endDateStr) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Method Start");
        }

        DateValidationStatus dateValidationStatus = new DateValidationStatus();
        Boolean isDateRangeValid = Boolean.TRUE;
        DateTime currentDate = new DateTime();
        DateTime startDate = null;
        DateTime endDate = null;

        try {
            if (startDateStr != null && !startDateStr.trim().equals("")) {
                startDate = new DateTime(startDateStr.trim(), DATE_FORMAT);
                dateValidationStatus.setStartDateInValidDateFormat(Boolean.TRUE);
            } else {
                isDateRangeValid = Boolean.FALSE;
                dateValidationStatus.setEmptyStartDate(Boolean.TRUE);
            }
            if (endDateStr != null && !endDateStr.trim().equals("")) {
                endDate = new DateTime(endDateStr.trim(), DATE_FORMAT);
                dateValidationStatus.setEndDateInValidDateFormat(Boolean.TRUE);
            } else {
                isDateRangeValid = Boolean.FALSE;
                dateValidationStatus.setEmptyEndDate(Boolean.TRUE);
            }
            if (startDate != null && endDate != null) {
                if (startDate.after(currentDate)) {
                    isDateRangeValid = Boolean.FALSE;
                    dateValidationStatus.setStartDateInFuture(Boolean.TRUE);
                }
                if (endDate.after(currentDate)) {
                    isDateRangeValid = Boolean.FALSE;
                    dateValidationStatus.setEndDateInFuture(Boolean.TRUE);
                }
                if (startDate.after(endDate)) {
                    isDateRangeValid = Boolean.FALSE;
                    dateValidationStatus.setStartDateAfterEndDate(Boolean.TRUE);
                }
            }
        } catch (Exception ex) {
            isDateRangeValid = Boolean.FALSE;
            if (LOG.isDebugEnabled()) {
                LOG.debug("Formatter is not able to parse the string into a valid date.");
            }
        }
        if (isDateRangeValid) {
            dateValidationStatus.setValidDateRange(Boolean.TRUE);
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("Method Finish");
        }
        return dateValidationStatus;
    }
}
