package com.electrocomponents.executor.support;

import com.electrocomponents.executor.EcCommand;
import com.electrocomponents.executor.EcCommandSpecification;
import com.electrocomponents.executor.exceptions.EcCommandNotValidException;
import com.electrocomponents.model.domain.Locale;

/**
 *
 * <pre>
 * ************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author  : Bhavesh Kavia
 * Created : 25 Jan 2011 at 11:18:48
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
 * Test AVP Command Specification.
 * @author Bhavesh Kavia
 */
public class TestAVPCommandSpecification extends EcCommandSpecification {
    /** The Version Id. */
    private static final long serialVersionUID = -1162130023089519666L;

    /**
     * @param sessionId - The Session Id
     * @param pageId - The Page Id
     * @param commandRole - The Command Role
     * @param commandType - The Command Type
     * @param locale - The Locale
     */
    public TestAVPCommandSpecification(final String sessionId, final String pageId, final String commandRole, final String commandType,
            final Locale locale) {
        super(sessionId, pageId, commandRole, commandType, locale);
    }

    /** {@inheritDoc} */
    @Override
    public EcCommand buildCommand() {
        return new TestAVPCommand(this);
    }

    /** {@inheritDoc} */
    @Override
    public void validate() throws EcCommandNotValidException {
        // No implementation.

    }

}
