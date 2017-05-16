package com.electrocomponents.service.core.validation;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.electrocomponents.model.domain.ApplicationSource;
import com.electrocomponents.service.exception.ServiceException;

public class ValidationUtility {
	
    private static final Log LOG = LogFactory.getLog(ValidationUtility.class);

	/**
     * Mandatory Validation for all type of parameters. Throws {@link ServiceException} if validation fails.
     * @param fieldName the name of the field
     * @param fieldValue the field Value for which validation has to be done.
     * @param optional classname for the calling class - this is only used in error logging
     */
    public static void validateMandatoryParameter(final String fieldName, final Object fieldValue, 
    		final String className) {

        if (fieldValue == null || "".equals(fieldValue)) {
            String errMsg = "Validation Error in " + className + "" + " - The " + fieldName + " you are passing is null or empty.";
            LOG.fatal(errMsg);
            throw new ServiceException(errMsg, ApplicationSource.RS_WEB_SITE);
        }

        // If the field is a String and the String "null" is being passed in reject.
        if (fieldValue instanceof String && "null".equalsIgnoreCase((String) fieldValue)) {
            String errMsg = "The " + fieldName + " you are passing has a value of the String \"null\".";
            LOG.fatal(errMsg);
            throw new ServiceException(errMsg, ApplicationSource.RS_WEB_SITE);
        }
    }
}
