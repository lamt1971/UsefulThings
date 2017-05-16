package com.electrocomponents.model.domain;

/*
 * ************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author  : Ed Ward
 * Created : 11 May 2007
 *
 * ************************************************************************************************
 * Change History
 * ************************************************************************************************
 * Ref      * Who      * Date       * Description
 * ************************************************************************************************
 *          *          *            *
 * ************************************************************************************************
 */

/**
 * Represents the Quantity of a product.
 * @author Ed Ward
 */

public final class Quantity extends Number implements Comparable<Quantity> {
    /** A useful constant. */
    public static final Quantity ZERO = new Quantity(0);

    /** A useful constant. */
    public static final Quantity ONE = new Quantity(1);

    /** */
    private static final long serialVersionUID = 1376650635102102737L;

    /** The quantity. */
    private int quantity;

    /**
     * @param quantity {@link #quantity}
     */
    public Quantity(final int quantity) {
        if (quantity >= 0) {
            this.quantity = quantity;
        } else {
            this.quantity = 0;
        }
    }

    /**
     * @param quantity {@link #quantity}
     */
    public Quantity(final String quantity) {
        if (quantity != null && !("").equals(quantity)) {
            this.quantity = Integer.parseInt(quantity);
        } else {
            this.quantity = 0;
        }
    }

    /**
     * @return {@link #quantity}
     */
    public int getQuantity() {
        return this.quantity;
    }

    /**
     * @return {@link Number#doubleValue()}
     */
    @Override
    public double doubleValue() {
        return (double) quantity;
    }

    /**
     * @return {@link Number#floatValue()}
     */
    @Override
    public float floatValue() {
        return quantity;
    }

    /**
     * @return {@link Number#intValue()}
     */
    @Override
    public int intValue() {
        return quantity;
    }

    /**
     * @return {@link Number#longValue()}
     */
    @Override
    public long longValue() {
        return (long) quantity;
    }

    /**
     * @param o other instance to compare to
     * @return the value <tt>0</tt> if this <tt>Quantity</tt> is equal to the argument <tt>Quantity</tt>, a value less than <tt>0</tt> if
     *         this <tt>Quantity</tt> is numerically less than the argument <tt>Quantity</tt>, and a value greater than <tt>0</tt> if this
     *         <tt>Integer</tt> is numerically greater than the argument <tt>Quantity</tt> (signed comparison).
     */
    public int compareTo(final Quantity o) {
        return this.quantity - o.quantity;
    }

    /**
     * @return a string representation of the number
     */
    public String toString() {
        return new StringBuffer().append(quantity).toString();
    }

    /**
     * @return the hash code
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + quantity;
        return result;
    }

    /**
     * @param obj the object to compare
     * @return <tt>true</tt> if the objects are equal
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
        final Quantity other = (Quantity) obj;
        if (quantity != other.quantity) {
            return false;
        }
        return true;
    }
}
