package com.electrocomponents.commons.components.general.xml;

import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Copyright (c) Electrocomponents Plc.
 * Author         : e0180383
 * Creation Date  : 12-Feb-2007
 * Creation Time  : 11:06:06
 * IDE            : IntelliJ IDEA 5
 * *******************************************************************************************************************
 * Description :
 * Utility to standardise error reporting and "ServletFailure" Exception generation.
 * *******************************************************************************************************************
 * Change History
 * *******************************************************************************************************************
 * * Change   * Author   * Date         * Description
 * *******************************************************************************************************************
 * * New      * e0180383 *  12-Feb-2007 * New class created
 * *******************************************************************************************************************
 * * 966954   * UK158854  * 07/08/08    * Added temporarily disabled message.
 * ********************************************************************************************************
 * * 9.3      * E0161085  * 20/01/09    * Added additional messages to cover all circumstances.
 * ********************************************************************************************************
 *
 * @author e0180383
 */
public class SystemCodes {

    /**
     * No Sub Code.
     *
     */
    public static final int COMMON_NO_SUB_CODE = 0;

    /* COMMON CODES USED ACROSS ALL PROCESSING */

    /**
     * Operation Success.
     */
    public static final int COMMON_OPERATION_SUCCESS = 200;

    /**
     * Operation failure.
     */
    public static final int COMMON_OPERATION_FAILURE = 500;

    /**
     * Customer settings not found.
     *
     */
    public static final int COMMON_CUSTOMER_SETTINGS_NOT_FOUND = 501;

    /**
     * Incorrect Password.
     */
    public static final int COMMON_INCORRECT_PASSWORD = 502;

    /**
     * Customer settings disabled.
     */
    public static final int COMMON_CUSTOMER_SETTINGS_DISABLED = 503;

    /**
     * Vendor Not found.
     */
    public static final int COMMON_VENDOR_NOT_FOUND = 510;

    /**
     * Invalid Rules configured.
     */
    public static final int COMMON_INVALID_RULES_SETUP = 520;

    /**
     * Handler Not found.
     */
    public static final int COMMON_HANDLER_NOT_FOUND = 530;

    /**
     * Invalid Format.
     */
    public static final int COMMON_INVALID_FORMAT = 540;

    /**
     * XSLT Style sheet not found.
     */
    public static final int COMMON_XSLT_STYLESHEET_NOT_FOUND = 550;

    /**
     * XSLT Style sheet Transformation error.
     */
    public static final int COMMON_XSLT_TRANSFORMATION_ERROR = 551;

    /**
     * XPATH Rule error.
     */
    public static final int COMMON_XPATH_RULE_ERROR = 552;

    /**
     * Customer Tag Not found.
     */
    public static final int COMMON_CUSTOMER_TAG_NOT_FOUND = 570;

    /**
     * Unsupported Encoding Exception.
     */
    public static final int COMMON_UNSUPPORTED_ENCODING_EXCEPTION = 580;

    /**
     * Database Exception.
     */
    public static final int COMMON_DATABASE_EXCEPTION = 590;

    /* ORDER PROCESSING RELATED CODES */

    /**
     * Order already processed.
     */
    public static final int ORDER_ALREADY_PROCESSED = 600;

    /**
     * Order missing delivery contact name.
     */
    public static final int ORDER_MISSING_DELIVERY_CONTACT_NAME = 610;

    /**
     * Order missing delivery address line 1.
     */
    public static final int ORDER_MISSING_DELIVERY_ADDRESS_LINE_1 = 612;

    /**
     * Order missing delivery post code.
     */
    public static final int ORDER_MISSING_DELIVERY_POSTCODE = 613;

    /**
     * Sold To Not configured for customer.
     */
    public static final int ORDER_SOLDTO_NOT_CONFIGURED = 614;

    /**
     * Order missing delivery element.
     */
    public static final int ORDER_MISSING_DELIVERY_ELEMENT = 615;

    /**
     * Payment method has not been configured.
     */
    public static final int ORDER_PAYMENT_CONFIGURATION_ERROR = 620;

    /**
     * Payment details (e.g. credit card) missing.
     */
    public static final int ORDER_MISSING_PAYMENT_DETAILS = 621;

    /**
     * Invalid Stock Number.
     */
    public static final int ORDER_INVALID_STOCK_NBR = 630;

    /**
     * Invalid Quantity.
     */
    public static final int ORDER_INVALID_QUANTITY = 631;

    /**
     * Order missing Stock Number.
     */
    public static final int ORDER_MISSING_STOCK_NBR = 632;

    /**
     * Order missing Unit Price.
     */
    public static final int ORDER_MISSING_UNIT_PRICE = 633;

    /**
     * Error with decimal places in order.
     */
    public static final int ORDER_DEC_PLACES_ERROR = 634;

    /**
     * Line value calculation error.
     */
    public static final int ORDER_LINE_VALUE_CALCULATION_ERROR = 635;

    /**
     * Error creating row on staging table.
     */
    public static final int ORDER_ERROR_CREATING_STAGING = 640;

    /**
     * Error updating ERS Order ref.
     */
    public static final int ORDER_ERROR_UPDATING_ERS_ORD_REF = 641;

    /**
     * Order transmission error.
     */
    public static final int ORDER_TRANSMISSION_ERROR = 642;

    /**
     * Error in header details.
     */
    public static final int ORDER_ERROR_IN_HEADER_DETAILS_FOUND = 644;

    /* RESPONSE ERROR CODES */
    /**
     * Error creating order response.
     */
    public static final int RESPONSE_ERROR_CREATING_RESPONSE = 680;

    /* PUNCHOUT AUTHENTICATION PROCESSING RELATED CODES */

    /**
     * Authorisation Error not permitted.
     */
    public static final int AUTH_NOT_PERMITTED = 701;

    /**
     * Punchout userid not found.
     */
    public static final int PUNCHOUT_USERID_NOT_FOUND = 710;

    /**
     * Authorisation not found.
     */
    public static final int AUTH_SAVED_REQUISITION_NOT_FOUND = 730;

    /**
     * Shopping cart format error.
     */
    public static final int SHOPPING_CART_FORMAT_ERROR = 760;

    /**
     * Shopping Cart qunatity error.
     */
    public static final int SHOPPING_CART_QUANTITY_ERROR = 761;

    /**
     * Error decoding url.
     */
    public static final int PUNCHOUT_FAILED_TO_DECODE_URL = 770;

    /**
     * Punchout forwarding error.
     */
    public static final int PUNCHOUT_FORWARDING_ERROR = 780;

    /**
     * General undefined error.
     */
    public static final int COMMON_UNDEFINED_ERROR = 999;

    /**
     * Log4J Declaration.
    */
    private static Log log = LogFactory.getLog(SystemCodes.class);

    /**
     * Status Descriptions.
     */
    private static final HashMap statusDescriptions = initStatusDescs();

    /**
     * Init Status descriptions.
     * @return Hahsmap of status descriptions.
     */
    private static HashMap<Integer, String> initStatusDescs() {
        HashMap<Integer, String> hm = new HashMap<Integer, String>();

        hm.put(COMMON_OPERATION_SUCCESS, "OK");

        hm.put(COMMON_CUSTOMER_SETTINGS_NOT_FOUND, "The customer settings could not be found in the Database, please contact support");
        hm.put(COMMON_INCORRECT_PASSWORD, "The password / shared secret in your request was invalid, please contact support.");
        hm.put(COMMON_CUSTOMER_SETTINGS_DISABLED, "This account has been temporarily disabled, please contact support");
        hm.put(COMMON_VENDOR_NOT_FOUND, "The vendor could not be found for this customer.");
        hm.put(COMMON_INVALID_RULES_SETUP, "The customer rules configuration was invalid for this operation, please contact support.");
        hm.put(COMMON_HANDLER_NOT_FOUND, "The handler could not be found for this request.");
        hm.put(COMMON_INVALID_FORMAT, "The request could not be handled as its format is unrecognised.");
        hm.put(COMMON_XSLT_STYLESHEET_NOT_FOUND,
                "The system could not locate a XSLT stylesheet for this operation, please contact support.");
        hm.put(COMMON_XSLT_TRANSFORMATION_ERROR,
                "An unrecoverable error occurred during the XSLT transformation process, please contact support.");
        hm.put(COMMON_XPATH_RULE_ERROR, "The Xpath Rule has not bee configured correctly.");
        hm.put(COMMON_CUSTOMER_TAG_NOT_FOUND, "The customer xml tag has not been found.");
        hm.put(COMMON_UNSUPPORTED_ENCODING_EXCEPTION, "The reuqets contained an unsupported encoding.");
        hm.put(COMMON_DATABASE_EXCEPTION, "A Database error has occured.");

        hm.put(ORDER_ALREADY_PROCESSED, "This customer order reference has already been processed.");
        hm.put(ORDER_MISSING_DELIVERY_CONTACT_NAME, "The order XML did not specifiy a delivery contact name in the correct position.");
        hm.put(ORDER_MISSING_DELIVERY_ADDRESS_LINE_1, "The order XML did not specifiy a delivery address in the correct position.");
        hm.put(ORDER_MISSING_DELIVERY_POSTCODE, "The order XML did not specify a delivery address postal code in the correct position.");
        hm.put(ORDER_SOLDTO_NOT_CONFIGURED, "The system could not find a SoldTo account number for this order, please contact support.");
        hm.put(ORDER_PAYMENT_CONFIGURATION_ERROR, "Payment type has not been configured correctly");
        hm.put(ORDER_MISSING_PAYMENT_DETAILS, "Payment details are missing from the order");
        hm.put(ORDER_INVALID_STOCK_NBR, "The order could not be processed as it contained an invalid stock number.");
        hm.put(ORDER_MISSING_STOCK_NBR, "The product Number is missing for one or more lines.");
        hm.put(ORDER_INVALID_QUANTITY, "The order could not be processed as it contained an invalid quantity.");
        hm.put(ORDER_MISSING_UNIT_PRICE, "The unit price is missing for the stock number.");
        hm.put(ORDER_DEC_PLACES_ERROR, "Invalid number of decimal places.");
        hm.put(ORDER_LINE_VALUE_CALCULATION_ERROR, "Error calculating line value.");
        hm.put(ORDER_ERROR_CREATING_STAGING, "A fatal error occurred writing the order to the STAGING database, please contact support.");
        hm.put(ORDER_ERROR_UPDATING_ERS_ORD_REF, "A fatal error occurred updating the ERS order reference, please contact support.");
        hm.put(ORDER_TRANSMISSION_ERROR, "A fatal error has occured transferring the order.");
        hm.put(ORDER_ERROR_IN_HEADER_DETAILS_FOUND, "No /Header/To/Credential Elements could be found");
        hm.put(RESPONSE_ERROR_CREATING_RESPONSE, "Error creating the order response");

        hm.put(AUTH_NOT_PERMITTED, "You are not permitted PunchOut via the Electrocomponents eProcurement System. Please contact support");
        hm.put(PUNCHOUT_USERID_NOT_FOUND, "Userid is Mandatory");
        hm.put(AUTH_SAVED_REQUISITION_NOT_FOUND, "The saved requisition details were not found on the Database, please contact support.");
        hm.put(SHOPPING_CART_FORMAT_ERROR, "The Shopping Cart is not formatted correctly");
        hm.put(SHOPPING_CART_QUANTITY_ERROR, "The shopping cart quantity is not valid");
        hm.put(PUNCHOUT_FAILED_TO_DECODE_URL, "Failure to decode url");
        hm.put(PUNCHOUT_FORWARDING_ERROR, "Error forwarding to page");
        return hm;
    }

    /**
     * Return the standard status description for a given system status code.
     *
     * @param statusCode The system status we wish to retrieve the standard description for.
     * @return  The standard status description as a String.
     */
    public static String getStatusDesc(final int statusCode) {
        if (log.isDebugEnabled()) {
            log.debug("getStatusDesc : Method Start");
        }
        String statusDescription;
        statusDescription = (String) statusDescriptions.get(statusCode);
        if (statusDescription == null) {
            statusDescription = "UNDEFINED ERROR MESSAGE";
        }
        if (log.isDebugEnabled()) {
            log.debug("getStatusDesc : Method Finish");
        }
        return statusDescription;
    }

    /**
     * Return the standard status description for a given ServletFailure.
     *
     * @param  sf The ServletFailure we wish to retrieve the standard description for.
     * @return  The standard status description as a String.
     */
    public static String getStatusDesc(final ServletFailure sf) {
        if (log.isDebugEnabled()) {
            log.debug("getStatusDesc : Method Start");
        }
        String statusDescription;
        statusDescription = (String) statusDescriptions.get(sf.getStatusCode());
        if (statusDescription == null) {
            statusDescription = sf.getMessage();
        }
        if (log.isDebugEnabled()) {
            log.debug("getStatusDesc : Method Finish");
        }
        return statusDescription;
    }

    /**
     * Generate Servlet Failure.
     * @param statusCode The status code for the error.
     * @return ServletFailure.
     */
    public static ServletFailure generateServletFailure(final int statusCode) {
        return generateServletFailure(null, statusCode, COMMON_NO_SUB_CODE, (String) statusDescriptions.get(statusCode));
    }

    /**
     * Generate Servlet Failure.
     * @param originalException Original Exception.
     * @param statusCode Error Code.
     * @return ServletFailure.
     */
    public static ServletFailure generateServletFailure(final Exception originalException, final int statusCode) {
        return generateServletFailure(originalException, statusCode, COMMON_NO_SUB_CODE, (String) statusDescriptions.get(statusCode));
    }

    /**
     * Generate Servlet Failure.
     * @param statusCode Error Code.
     * @param statusSubCode Status Sub Code.
     * @return ServletFailure.
     */
    public static ServletFailure generateServletFailure(final int statusCode, final int statusSubCode) {
        return generateServletFailure(null, statusCode, statusSubCode, (String) statusDescriptions.get(statusCode));
    }

    /**
     * Generate Servlet Failure.
     * @param originalException Original Exception.
     * @param statusCode Error Code.
     * @param statusSubCode Status Sub Code.
     * @return ServletFailure.
     */
    public static ServletFailure generateServletFailure(final Exception originalException, final int statusCode, final int statusSubCode) {
        return generateServletFailure(originalException, statusCode, statusSubCode, (String) statusDescriptions.get(statusCode));
    }

    /**
     * Generate Servlet Failure.
     * @param statusCode Error Code.
     * @param supportDescription Support Description.
     * @return ServletFailure.
     */
    public static ServletFailure generateServletFailure(final int statusCode, final String supportDescription) {
        return generateServletFailure(null, statusCode, COMMON_NO_SUB_CODE, supportDescription);
    }

    /**
     * Generate Servlet Failure.
     * @param originalException Original Exception.
     * @param statusCode Error Code.
     * @param supportDescription Support Description.
     * @return ServletFailure.
     */
    public static ServletFailure generateServletFailure(final Exception originalException, final int statusCode,
            final String supportDescription) {
        return generateServletFailure(originalException, statusCode, COMMON_NO_SUB_CODE, supportDescription);
    }

    /**
     * Generate Servlet Failure.
     * @param originalException Original Exception.
     * @param statusCode Error Code.
     * @param statusSubCode Error Sub Code.
     * @param supportDescription Support Description.
     * @return Servlet Failure.
     */
    public static ServletFailure generateServletFailure(final Exception originalException, final int statusCode, final int statusSubCode,
            final String supportDescription) {
        if (log.isDebugEnabled()) {
            log.debug("generateServletFailure : Method Start");
        }

        String statusDescription = (String) statusDescriptions.get(statusCode);

        ServletFailure sf = new ServletFailure(originalException, supportDescription, statusCode, statusSubCode, statusDescription);

        if (log.isDebugEnabled()) {
            log.debug("generateServletFailure : Method Finish");
        }
        return sf;
    }
}
