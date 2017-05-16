package com.electrocomponents.service.core.usermessaging.support;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * <pre>
 * ************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author  : Ganesh Raut
 * Created : 5 Jan 2009 at 15:36:30
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
 * A data transfer object for generating excel file for email attachment.
 * @author Ganesh Raut
 */
public class ExcelEmailDTO implements Serializable {

    /** serialVersionUID. */
    private static final long serialVersionUID = -1058446601755009820L;

    /** A hash map holding column name as a key and width as a value. */
    private Map<String, Integer> headerMap = new LinkedHashMap<String, Integer>();

    /** Child element list. */
    private List<List<String>> elementList = new ArrayList<List<String>>();

    /**
     * @return the headerMap
     */
    public Map<String, Integer> getHeaderMap() {
        return headerMap;
    }

    /**
     * @param headerMap the headerMap to set
     */
    public void setHeaderMap(final Map<String, Integer> headerMap) {
        this.headerMap = headerMap;
    }

    /**
     * @return the elementList
     */
    public List<List<String>> getElementList() {
        return elementList;
    }

    /**
     * @param elementList the elementList to set
     */
    public void setElementList(final List<List<String>> elementList) {
        this.elementList = elementList;
    }
}
