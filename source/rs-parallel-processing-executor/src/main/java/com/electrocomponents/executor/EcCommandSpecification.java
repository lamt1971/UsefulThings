package com.electrocomponents.executor;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.electrocomponents.executor.exceptions.EcCommandNotValidException;
import com.electrocomponents.model.domain.Locale;

/**
 * <pre>
 * ************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author  : Berkan Kurtoglu
 * Created : 18 Jan 2011 at 12:46:07
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
 * EcCommandSpecification is a base class for specific EcCommandSpecification implementations and contains default EcCommand properties.
 * Specific EcCommandSpecification implementations have to extend this base class and implement the abstract methods which are buildCommand
 * and validate. EcCommandSpecification is used to keep mandatory properties of EcCommand which are going to be used for initialisation and
 * execution of specific EcCommand implementations. EcCommandSpecification also provides validateAll method to validate all properties of
 * specific implementations.
 * @author Berkan Kurtoglu
 */
public abstract class EcCommandSpecification implements Serializable {

    /** The serialVersionUID. */
    private static final long serialVersionUID = -5979883743302162272L;

    /** commons logger. */
    private static final Log LOG = LogFactory.getLog(EcCommandSpecification.class);

    /** The Http Session Id. */
    private String sessionId;

    /** The Page Coordinator Identification. */
    private String pageId;

    /** The Command role to group commands. */
    private String commandRole;

    /** The Class Name of Specific Command. */
    private String commandType;

    /** The Identifier of Specific Locale. */
    private Locale locale;
    
    private String requestUrl;

    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    /**
     * Parameterised Constructor.
     * @param sessionId String
     * @param pageId String
     * @param commandRole String
     * @param commandType String
     * @param locale Locale
     */
    public EcCommandSpecification(final String sessionId, final String pageId, final String commandRole, final String commandType,
            final Locale locale) {
        this.sessionId = sessionId;
        this.pageId = pageId;
        this.commandRole = commandRole;
        this.commandType = commandType;
        this.locale = locale;
    }

    /**
     * Gets the session id.
     * @return the sessionId as a string
     */
    public String getSessionId() {
        return sessionId;
    }

    /**
     * Gets the page id.
     * @return the pageId as a string
     */
    public String getPageId() {
        return pageId;
    }

    /**
     * Gets the command role.
     * @return the commandRole as a string
     */
    public String getCommandRole() {
        return commandRole;
    }

    /**
     * Gets the command type.
     * @return the commandType as a string
     */
    public String getCommandType() {
        return commandType;
    }

    /**
     * Gets the locale.
     * @return the locale as a Locale
     */
    public Locale getLocale() {
        return locale;
    }

    /**
     * Validates mandatory EcCommand properties and throws EcCommandNotValidException if properties are not valid. Calls validate method of
     * specific EcCommandSpecification to validate the specific properties. Exception of subclass is merged and throw only one exception for
     * all properties.
     * @throws EcCommandNotValidException the exception
     */
    public final void validateAll() throws EcCommandNotValidException {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start validateAll");
        }

        StringBuilder builder = new StringBuilder();

        if (StringUtils.isBlank(this.pageId)) {
            builder.append("Page Id cannot be null/empty");
        }
        if (StringUtils.isBlank(this.sessionId)) {
            builder.append("\nSession Id cannot be null/empty");
        }
        if (StringUtils.isBlank(this.commandRole)) {
            builder.append("\nCommand Role cannot be null/empty");
        }
        if (StringUtils.isBlank(this.commandType.toString())) {
            builder.append("\nCommand Type Id cannot be null/empty");
        }
        if (this.locale == null) {
            builder.append("\nLocale cannot be null/empty");
        }

        try {
            validate();
        } catch (EcCommandNotValidException e) {
            builder.append("\nSubclass Validation Error Messages : " + e.getMessage() + " Supprting Messages:" + e.getSupportMessage());
        }

        String builderExceptionMessage = builder.toString();

        if (StringUtils.isNotEmpty(builderExceptionMessage)) {
            LOG.error(builderExceptionMessage);
            throw new EcCommandNotValidException(builderExceptionMessage);
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("End validateAll");
        }
    }

    /**
     * Abstract method to validate properties of specific EcCommandSpecification. Need to be implemented by specific EcCommandSpecification.
     * Throws EcCommandNotValidException exception if specific properties are not valid.
     * @throws EcCommandNotValidException the exception
     */
    public abstract void validate() throws EcCommandNotValidException;

    /**
     * Creates specific EcCommand from specific EcCommandSpecification. Need to be implemented by specific EcCommandSpecification.
     * @return EcCommand the Specific EcCommand
     */
    public abstract EcCommand buildCommand();
}
