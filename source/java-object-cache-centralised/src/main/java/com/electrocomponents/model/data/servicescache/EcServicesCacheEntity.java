package com.electrocomponents.model.data.servicescache;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.electrocomponents.model.domain.DateTime;
import com.electrocomponents.model.domain.servicescache.EcServicesCache;

/**
 * <pre>
 * ************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author  : Rakesh Kumar
 * Created : 30 Apr 2012 at 14:22:10
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
 * An entity that represents a row of EC_SERVICE_CACHE table.
 * @author Rakesh Kumar
 */
@Entity
@Table(name = "EC_SERVICES_CACHE")
public class EcServicesCacheEntity implements Serializable, EcServicesCache {

    /** The serial version UID. */
    private static final long serialVersionUID = -996094559245463586L;

    /** The database identity. */
    @SequenceGenerator(sequenceName = "EC_SERVICES_CACHE_SEQ", name = "seqGen")
    @Id
    @Column(name = "SERVICES_CACHE_ID")
    @GeneratedValue(generator = "seqGen")
    private Long servicesCacheId;

    /** The Cache Access Path field. */
    @Column(name = "CACHE_ACCESS_PATH")
    private String cacheAccessPath;

    /** The Key field. */
    @Column(name = "CACHE_KEY")
    private String cacheKey;

    /** The Date when the record was last updated. */
    @org.hibernate.annotations.Type(type = "last_mod_time")
    @Column(name = "LAST_MOD_TIME")
    private DateTime lastModifiedTime;

    /** The Value field. */
    @Column(name = "VALUE")
    private byte[] value;

    /**
     * The getServicesCacheId is getter method.
     * @return servicesCacheId
     */
    public Long getServicesCacheId() {
        return servicesCacheId;
    }

    /**
    * The setServicesCacheId is Setter method.
     * @param servicesCacheId as services Cache Id
     */
    public void setServicesCacheId(final Long servicesCacheId) {
        this.servicesCacheId = servicesCacheId;
    }

    /**
     * The getCacheAccessPath is getter method.
     * @return cacheAccessPath Cache Access Path
     */
    public String getCacheAccessPath() {
        return cacheAccessPath;
    }

    /**
     * The setCacheAccessPath is Setter method.
     * @param cacheAccessPath Cache Access Path parameter
     */
    public void setCacheAccessPath(final String cacheAccessPath) {
        this.cacheAccessPath = cacheAccessPath;
    }

    /**
     * The getCacheKey is getter method.
     * @return cacheKey
     */
    public String getCacheKey() {
        return cacheKey;
    }

    /**
     * The setCacheKey is Setter method.
     * @param cacheKey Cache Key parameter
     */
    public void setCacheKey(final String cacheKey) {
        this.cacheKey = cacheKey;
    }

    /**
     * The getLastModifiedTime is getter method.
     * @return the lastModifiedTime
     */
    public DateTime getLastModifiedTime() {
        return lastModifiedTime;
    }

    /**
     * The setLastModifiedTime is Setter method.
     * @param lastModifiedTime last update DateTime parameter
     */
    public void setLastModifiedTime(final DateTime lastModifiedTime) {
        this.lastModifiedTime = lastModifiedTime;
    }

    /**
     * The getValue is getter method.
     * @return value
     */
    public byte[] getValue() {
        return value;
    }

    /**
     * The setValue is Setter method.
     * @param value Value parameter
     */
    public void setValue(final byte[] value) {
        this.value = value;
    }

    /**
     * @return hash code
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((servicesCacheId == null) ? 0 : servicesCacheId.hashCode());
        return result;
    }

    /**
     * @param obj the object to compare
     * @return <tt>true</tt> if equal to <tt>obj</tt>
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final EcServicesCacheEntity other = (EcServicesCacheEntity) obj;
        if (servicesCacheId == null) {
            if (other.servicesCacheId != null) {
                return false;
            }
        } else if (!servicesCacheId.equals(other.servicesCacheId)) {
            return false;
        }
        return true;
    }

}
