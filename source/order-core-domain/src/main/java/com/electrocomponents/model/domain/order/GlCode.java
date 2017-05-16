package com.electrocomponents.model.domain.order;

import com.electrocomponents.model.domain.DateTime;

/**
 * <pre>
 * ************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author  : Vaibhav Dhopte
 * Created : 17 Oct 2008 at 11:38:12
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
 * GlCode Domain Model.
 * @author Vaibhav Dhopte
 */
public interface GlCode {

    /**
     * @return the id
     */
    long getId();

    /**
     * @param id the id to set
     */
    void setId(final long id);

    /**
     * @return the userId
     */
    long getUserId();

    /**
     * @param userId the userId to set
     */
    void setUserId(final long userId);

    /**
     * @return the description
     */
    String getDescription();

    /**
     * @param description the description to set
     */
    void setDescription(final String description);

    /**
     * @return the glCode
     */
    String getGlCode();

    /**
     * @param glCode the glCode to set
     */
    void setGlCode(final String glCode);

    /**
     * @return the creationTime
     */
    DateTime getCreationTime();

    /**
     * @param creationTime the creationTime to set
     */
    void setCreationTime(final DateTime creationTime);

    /**
     * @return the lastModifiedTime
     */
    DateTime getLastModifiedTime();

    /**
     * @param lastModifiedTime the lastModifiedTime to set
     */
    void setLastModifiedTime(final DateTime lastModifiedTime);

    /**
     * @return the isPmGlCode
     */
    boolean isPmGlCode();

    /**
     * @param isPmGlCode the isPmGlCode to set
     */
    void setPmGlCode(final boolean isPmGlCode);
}
