/**
 *
 */
package com.electrocomponents.model.data.config;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/*
 * ************************************************************************************************ Copyright (c)
 * Electrocomponents Plc. Author : Ganesh Raut Created : 31 Jul 2007 at 15:25:26
 * ************************************************************************************************ Change History
 * ************************************************************************************************ Ref * Who * Date *
 * Description ************************************************************************************************ * * *
 * ************************************************************************************************
 */

/**
 * An entity representing a row in the BV_OID_REGISTRY table.
 * @author Ganesh Raut
 */
@Entity
@Table(name = "BV_OID_REGISTRY")
public class BvOidRegistryEntity implements BvOidRegistry {

    /** The database identity. */
    @Column(name = "SYSTEM_ID")
    @Id
    private long systemId;

    /** The Oid. */
    @Column(name = "NEXT_OID")
    private long oId;

    /** @return the oId */
    public long getOId() {
        return oId;
    }

    /** @param id the oId to set */
    public void setOId(final long id) {
        oId = id;
    }

    /** @return the systemId */
    public long getSystemId() {
        return systemId;
    }

    /** @param systemId the systemId to set */
    public void setSystemId(final long systemId) {
        this.systemId = systemId;
    }
}
