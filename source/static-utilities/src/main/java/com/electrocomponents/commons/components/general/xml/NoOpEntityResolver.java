package com.electrocomponents.commons.components.general.xml;

import java.io.StringReader;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;

/**
 * RSJDomUtils
 * -----------
 * Copyright (c) RS Components.
 * User: UK161085 (Michael Reilly)
 * Date: 18/11/2004
 * ********************************************************************************************************
 * Overview
 * --------
 * This class is required to turn off DTD validation for xml.
 * ********************************************************************************************************
 * <P>
 * ********************************************************************************************************
 * *          Change History                                                                              *
 * ********************************************************************************************************
 * * Number  * Who       * Date       * Description                                                       *
 * ********************************************************************************************************
 * * N/A     * UK180383  * 30/10/2006 * New development
 * *********************************************************************************************************
 * */

public class NoOpEntityResolver implements EntityResolver {

    /**
     * Resolve the Entity.
     * @param publicId - The public id of the entity.
     * @param systemId - The system id.
     * @return Input Source object.
     */
    public InputSource resolveEntity(final String publicId, final String systemId) {
        return new InputSource(new StringReader(""));
    }
}
