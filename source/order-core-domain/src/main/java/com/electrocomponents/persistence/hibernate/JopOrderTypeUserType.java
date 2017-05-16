package com.electrocomponents.persistence.hibernate;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.usertype.UserType;

import com.electrocomponents.model.domain.jop.JopOrderType;

/**
 * <pre>
 * ************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author  : Bharat Bonde
 * Created : 2009-07-20
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
 * A hibernate custom user type for mapping values.
 */
public class JopOrderTypeUserType implements UserType {

    /** The SQL types of the columns mapped by this type. */
    private static final int[] TYPES = new int[] { Types.VARCHAR };

    /**
     * @param cached cached
     * @param owner owner
     * @return cached
     */
    public Object assemble(final Serializable cached, final Object owner) {
        return cached;
    }

    /**
     * @param value value
     * @return value
     */
    public Object deepCopy(final Object value) {
        return value;
    }

    /**
     * @param value value
     * @return value cast to Serializable
     */
    public Serializable disassemble(final Object value) {
        return (Serializable) value;
    }

    /**
     * @param x x
     * @param y y
     * @return true if x and y are equal
     */
    public boolean equals(final Object x, final Object y) {
        if (x == y) {
            return true;
        }

        if (x == null || y == null) {
            return false;
        }

        return x.equals(y);
    }

    /**
     * @param x x
     * @return the hash code
     */
    public int hashCode(final Object x) {
        return x.hashCode();
    }

    /**
     * @return false
     */
    public boolean isMutable() {
        return false;
    }

    /**
     * @param rs ResultSet
     * @param names names
     * @param owner owner
     * @throws SQLException on error
     * @return the RSUserType instance
     */
    public Object nullSafeGet(final ResultSet rs, final String[] names, SessionImplementor si, final Object owner) throws SQLException {
        final String userType = rs.getString(names[0]);
        return userType == null ? null : JopOrderType.value(userType);
    }

    /**
     * @param st PreparedStatement
     * @param value value
     * @param index index
     * @throws SQLException on error
     */
    public void nullSafeSet(final PreparedStatement st, final Object value, final int index, SessionImplementor si) throws SQLException {
        st.setString(index, value == null ? null : ((JopOrderType) value).getJopOrderType());
    }

    /**
     * @param original original
     * @param target target
     * @param owner owner
     * @return original
     */
    public Object replace(final Object original, final Object target, final Object owner) {
        return original;
    }

    /**
     * @return OrderType.class
     */
    public Class returnedClass() {
        return JopOrderType.class;
    }

    /**
     * @return {@link #TYPES}
     */
    public int[] sqlTypes() {
        return TYPES;
    }
}
