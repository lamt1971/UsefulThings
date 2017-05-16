/**
 *
 */
package com.electrocomponents.persistence.hibernate;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.usertype.UserType;

import com.electrocomponents.model.domain.order.MessagingTransportMethod;

/**
 * <pre>
 * ************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author  : C0950079
 * Created : 13 Feb 2010 at 15:57:23
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
 * Hibernate user type for mapping MessagingTransportMethod enum.
 * @author C0950079
 */
public class MessagingTransportMethodUserType implements UserType {

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
     * @param rs rs
     * @param names names
     * @param owner owner
     * @throws SQLException on error
     * @return the OrderProductType instance
     */
    public Object nullSafeGet(final ResultSet rs, final String[] names, SessionImplementor si, final Object owner) throws SQLException {
        final String messagingTransportMethodValue = rs.getString(names[0]);
        if (messagingTransportMethodValue != null) {
            return MessagingTransportMethod.value(messagingTransportMethodValue);
        } else {
            return null;
        }
    }

    /**
     * @param st st
     * @param value value
     * @param index index
     * @throws SQLException on error
     */
    public void nullSafeSet(final PreparedStatement st, final Object value, final int index, SessionImplementor si) throws SQLException {
        st.setString(index, value == null ? null : ((MessagingTransportMethod) value).getMessagingTransportMethod());
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
     * @return class
     */
    public Class returnedClass() {
        return MessagingTransportMethod.class;
    }

    /**
     * @return {@link #TYPES}
     */
    public int[] sqlTypes() {
        return TYPES;
    }

}
