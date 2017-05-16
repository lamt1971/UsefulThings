package com.electrocomponents.model.data.usermessaging;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * <pre>
 * ************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author  : <<<<user name>>>>
 * Created : 4 Jul 2014 at 13:07:08
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

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Labels")
public class AvailabilityLabels {

    /** A variable to hold label. */
    public String label;

    /** A variable to hold atpIcon. */
    public String atpIcon;

    /**
     * @return the label
     */
    public String getLabel() {
        return label;
    }

    /**
     * @param label the label to set
     */
    public void setLabel(final String label) {
        this.label = label;
    }

    /**
     * @return the atpIcon
     */
    public String getAtpIcon() {
        return atpIcon;
    }

    /**
     * @param atpIcon the atpIcon to set
     */
    public void setAtpIcon(final String atpIcon) {
        this.atpIcon = atpIcon;
    }

}
