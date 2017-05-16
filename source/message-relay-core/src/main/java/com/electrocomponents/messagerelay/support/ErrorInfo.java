package com.electrocomponents.messagerelay.support;

import java.io.Serializable;

/**
 * Copyright (c) Electrocomponents Plc.
 * Author         : e0180383
 * Creation Date  : 21-Jun-2006
 * Creation Time  : 10:56:14
 * IDE            : IntelliJ IDEA 5
 * *******************************************************************************************************************
 * Description :
 * A simple POJO object to represent an error / exception on the remote system.
 * *******************************************************************************************************************
 * Change History
 * *******************************************************************************************************************
 * * Change   * Author   * Date         * Description
 * *******************************************************************************************************************
 * * New      * e0180383 *  21-Jun-2006 * New class created
 * *******************************************************************************************************************
 * * 10.1ELS  * e0161085 *  07-Jul-2009 * Tidied Code.
 * *******************************************************************************************************************
 *
 * @author e0180383
 */
public class ErrorInfo implements Serializable {

    /**
     * Error.
     */
    public static final String ERROR     = "ERROR";
    /**
     * Fatal.
     */
    public static final String FATAL     = "FATAL";
    /**
     * HTTP RC.
     */
    public static final String HTTP_RC   = "HTTP_RC";

    /**
     * Error Type.
     */
    private String errorType;
    /**
     * Error Generating class.
     */
    private String errorGeneratingClass;
    /**
     * Error Message.
     */
    private String errorMessage;

    /**
     *  Error Info.
     * @param errorType Error Type.
     * @param errorGeneratingClass Error Class.
     * @param errorMessage Error Message.
     */
    public ErrorInfo(final String errorType, final Object errorGeneratingClass, final String errorMessage) {
      this.errorType = errorType;
      this.errorGeneratingClass = errorGeneratingClass.getClass().getName();
      this.errorMessage = errorMessage;
    }



    /**
     * Get Error Generating Class.
     * @return The class.
     */
    public String getErrorGeneratingClass() {
        return errorGeneratingClass;
    }

    /**
     * Set the Error Generating Class.
     * @param errorGeneratingClass The class.
     */
    public void setErrorGeneratingClass(final Object errorGeneratingClass) {
        this.errorGeneratingClass = errorGeneratingClass.getClass().getName();
    }

    /**
     * Get the Error Message.
     * @return The Error message.
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * Set the Error Message.
     * @param errorMessage The Error message.
     */
    public void setErrorMessage(final String errorMessage) {
        this.errorMessage = errorMessage;
    }

    /**
     * Get the Error Type.
     * @return The Error Type.
     */
    public String getErrorType() {
        return errorType;
    }

    /**
     * Set the Error Type.
     * @param errorType The Error Type.
     */
    public void setErrorType(final String errorType) {
        this.errorType = errorType;
    }

    /**
     * Equals.
     * @param o The Object.
     * @return True if equals.
     */
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final ErrorInfo errorInfo = (ErrorInfo) o;

        if (errorGeneratingClass != null ? !errorGeneratingClass.equals(errorInfo.errorGeneratingClass) : errorInfo.errorGeneratingClass != null) return false;
        if (errorMessage != null ? !errorMessage.equals(errorInfo.errorMessage) : errorInfo.errorMessage != null) return false;
        return !(errorType != null ? !errorType.equals(errorInfo.errorType) : errorInfo.errorType != null);

    }

    /**
     * Hashcode.
     * @return Hashcode.
     */
    public int hashCode() {
        int result;
        result = (errorType != null ? errorType.hashCode() : 0);
        result = 29 * result + (errorGeneratingClass != null ? errorGeneratingClass.hashCode() : 0);
        result = 29 * result + (errorMessage != null ? errorMessage.hashCode() : 0);
        return result;
    }

}

