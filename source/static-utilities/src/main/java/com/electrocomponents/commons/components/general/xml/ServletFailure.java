package com.electrocomponents.commons.components.general.xml;

/**
 * <pre>
 * ServletFailure
 * --------------
 * Copyright (c) RS Components.
 * User: Nick McKenna
 * Date: 12th June 2000
 * ********************************************************************************************************
 * Overview
 * --------
 * Represents the failure of the Servlet to process a request correctly. This exception is thrown only when
 * the failure of the servlet means that the request cannot be completed.
 * This class does not log to the event log as its methods are called.
 * ********************************************************************************************************
 * *          Change History                                                                              *
 * ********************************************************************************************************
 * * Number   * Who       * Date       * Description                                                      *
 * ********************************************************************************************************
 * * 38800    * e0180383  * 11/01/07   * Changes to support Sony / ASKUL PunchOut adoption.
 * ********************************************************************************************************
 * * 9.3      * e0161085  * 08/12/08   * Tidied Code.
 * ********************************************************************************************************
 *
 * </pre>
 */

public class ServletFailure extends Exception {

    /**
     * Serial ID.
     */
    private static final long serialVersionUID = -6248138134571934324L;

    /**
        *  The Original Exception that caused this exception to be thrown.
        */
    private Exception originalException;

    /**
     *  Additional information about the error that was thrown to be used by support.
     */
    private String message;

    // Status information used to responses to customer systems.
    /**
     * Status Code.
     */
    private int statusCode;

    /**
     * Status Sub Code.
     */
    private int statusSubCode;

    /**
     * Status Description.
     */
    private String statusDescription;

    /**
      * @param originalException Exception that caused the error to be raised.
      * @param message Additional information about the error to be used by support.
      * @param statusCode Predefined status code for the failure that has occurred.
      * @param statusSubCode Predefined status sub code for the failure that has occurred.
      * @param statusDescription Predefined customer facing desciption of the failure that has occurred.
      **/
    public ServletFailure(final Exception originalException, final String message, final int statusCode, final int statusSubCode,
            final String statusDescription) {
        this.originalException = originalException;
        this.message = message;
        this.statusCode = statusCode;
        this.statusSubCode = statusSubCode;
        this.statusDescription = statusDescription;
    }

    /**
     * @param message Additional information about the error to be used by support.
     * @param statusCode Predefined status code for the failure that has occurred.
     * @param statusSubCode Predefined status sub code for the failure that has occurred.
     * @param statusDescription Predefined customer facing desciption of the failure that has occurred.
     */
    public ServletFailure(final String message, final int statusCode, final int statusSubCode, final String statusDescription) {
        this.message = message;
        this.statusCode = statusCode;
        this.statusSubCode = statusSubCode;
        this.statusDescription = statusDescription;
    }

    /**
    * ServletFailure.
    * @param originalException Exception that caused the error to be raised.
    * @param message Additional information about the error
    */
    public ServletFailure(final Exception originalException, final String message) {
        this.originalException = originalException;
        this.message = message;
    }

    /**
     * ServletFailure.
     * @param  originalException Exception that caused the error to be raised.
     */
    public ServletFailure(final Exception originalException) {
        this.originalException = originalException;
    }

    /**
    *  ServletFailure.
    * @param message Message indicating the type of error that has occurred.
    */
    public ServletFailure(final String message) {
        this.message = message;
    }

    /**
     * Returns a short description of this error.
     *
     * @return The description of the error.
     */
    public String toString() {
        String err = "The request could not be processed for the following reason : ";

        if (message != null) {
            err += message;
        }
        if (originalException != null) {
            err += ", the original Exception was : " + originalException.getMessage();
        }
        return err;
    }

    /**
     * Get the Message.
     * @return The Message.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Set the Message.
     * @param message The message.
     */
    public void setMessage(final String message) {
        this.message = message;
    }

    /**
     * Get the Original Exception.
     * @return The original exception.
     */
    public Exception getOriginalException() {
        return originalException;
    }

    /**
     * Set the original exception.
     * @param originalException The exception.
     */
    public void setOriginalException(final Exception originalException) {
        this.originalException = originalException;
    }

    /**
     * Get the status code.
     * @return The status code.
     */
    public int getStatusCode() {
        return statusCode;
    }

    /**
     * Set the Status Code.
     * @param statusCode The status code.
     */
    public void setStatusCode(final int statusCode) {
        this.statusCode = statusCode;
    }

    /**
     * Get the Status description.
     * @return The status description.
     */
    public String getStatusDescription() {
        if (statusDescription != null) {
            return statusDescription;
        } else {
            return "Support Info : " + message;
        }
    }

    /**
     * Set the status description.
     * @param statusDescription The status description.
     */
    public void setStatusDescription(final String statusDescription) {
        this.statusDescription = statusDescription;
    }

    /**
     * Get the status code.
     * @return The status code.
     */
    public int getStatusSubCode() {
        return statusSubCode;
    }

    /**
     * Set the Status Sub Code.
     * @param statusSubCode The status sub code.
     */
    public void setStatusSubCode(final int statusSubCode) {
        this.statusSubCode = statusSubCode;
    }
}
