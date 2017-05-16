package com.electrocomponents.model.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/**
 * Instance of this class are IMMUTABLE and represent an amount of currency.
 * <p>
 * Values will be rounded to <b>3</b> decimal places using the <tt>RoundingMode.HALF_EVEN</tt> rounding rules.
 * <p>
 * <b>NB</b>. The formatted value of the amount will be cached. Added methods to store amount value for required precision number of digits.
 */
public final class Money implements Serializable {

    /** The number of digits after the decimal place. */
    public static final int SCALE = 3;

    /** */
    private static final long serialVersionUID = 7622751208407027694L;

    /** The value 100. */
    private static final BigDecimal ONE_HUNDRED = new BigDecimal(100);

    /**
     * The rounding mode to associate with the <tt>MathContext</tt> used to construct the <tt>BigDecimal</tt> instance and for use when
     * applying the scale.
     */
    private static final int ROUNDING_MODE = BigDecimal.ROUND_HALF_EVEN;

    /** The money amount. */
    private BigDecimal amount;

    /** The scale no of precision digits for current money value. */
    private Integer scale;

    /**
     * @param amount the amount
     */
    public Money(final double amount) {
        /*
         * Using the Double.toString() approach means we don't end up with weird values like 12.2129999999999992 instead of 12.213
         */
        this(new BigDecimal(new Double(amount).toString()));
    }

    /**
     * @param amount the decimal amount as a string
     */
    public Money(final String amount) {
        this(new BigDecimal(amount));
    }

    /**
     * @param amount {@link #amount

     */
    public Money(final BigDecimal amount) {
        this.amount = amount.setScale(SCALE, ROUNDING_MODE);
        this.scale = SCALE;
    }

    /**
     * @param amount the amount
     * @param scale no of precision digits.
     */
    public Money(final double amount, final int scale) {
        /*
         * Using the Double.toString() approach means we don't end up with weird values like 12.2129999999999992 instead of 12.213
         */
        this(new BigDecimal(new Double(amount).toString()), scale);
    }

    /**
     * @param amount the decimal amount as a string
     * @param scale no of precision digits.
     */
    public Money(final String amount, final int scale) {
        this(new BigDecimal(amount), scale);
    }

    /**
         * @param amount {@link #amount
         * @param scale no of precision digits.
         */
    public Money(final BigDecimal amount, final int scale) {
        this.amount = amount.setScale(scale, ROUNDING_MODE);
        this.scale = scale;
    }

    /**
     * The internal representation of the amount.
     * @return {@link #amount}
     */
    public BigDecimal getBigDecimalValue() {
        return this.amount;
    }

    /**
     * The returned value is rounded to one decimal place less than the more accurate internal representation.
     * @return the amount of money as a double
     */
    public double getAmount() {
        if (this.scale == SCALE) {
            return this.amount.setScale(SCALE - 1, ROUNDING_MODE).doubleValue();
        } else {
            return this.amount.setScale(this.scale, ROUNDING_MODE).doubleValue();
        }
    }

    /**
     * Although the value is held internally to 3 decimal places, the representation here is based on the NumberFormat instance associated
     * with the <tt>Locale</tt> and may be truncated and/or rounded based on the rules associated with the locale.
     * @return a string representation of the amount
     */
    public String toString() {
        return this.amount.toString();
    }

    /**
     * @param other the instance to compare against
     * @return true if equal
     */
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        boolean isEqual = false;
        if (other instanceof Money) {
            final Money otherAmount = (Money) other;
            isEqual = this.amount.equals(otherAmount.amount);
        }
        return isEqual;
    }

    /**
     * @return the hash code
     */
    public int hashCode() {
        return this.amount.hashCode();
    }

    /**
     * @param other the amount to add to this one
     * @return a new Money instance containing an amount equal to the sum of this instance and the supplied one.
     */
    public Money add(final Money other) {
        return new Money(this.amount.add(other.amount), this.scale);
    }

    /**
     * @param other the amount to subtract from this one
     * @return a new Money instance containing an amount equal to the amount in the supplied Money instance subtracted from this one
     */
    public Money subtract(final Money other) {
        return new Money(this.amount.subtract(other.amount), this.scale);
    }

    /**
     * @param percentage the percentage of this money amount to return
     * @return a <tt>Money</tt> instance representing the requested percentage of this amount.
     */
    public Money percent(final int percentage) {
        return percent(new BigDecimal(percentage));
    }

    /**
     * @param percentage the percentage of this money amount to return
     * @return a <tt>Money</tt> instance representing the requested percentage of this amount.
     */
    public Money percent(final BigDecimal percentage) {
        final BigDecimal percentAmount = amount.multiply(percentage.divide(ONE_HUNDRED));
        return new Money(percentAmount, this.scale);
    }

    /**
     * @param multiplicand the value with which to multiply this amount
     * @return a Money instance representing the result of multiplying this amount by the specified value
     */
    public Money multiply(final int multiplicand) {
        BigDecimal result = amount.multiply(new BigDecimal(multiplicand));
        return new Money(result, this.scale);
    }

    /**
     * This method will convert a locale specific money value to English Money value.
     * @param money a money value.
     * @param locale javalocale of money.
     * @return money The Money object.
     */
    public static Money parseMoney(final String money, final Locale locale) {
        DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols(locale);
        char groupingSeparator = decimalFormatSymbols.getGroupingSeparator();
        char decimalSeparator = decimalFormatSymbols.getDecimalSeparator();
        String amount = money;
        amount = amount.replaceAll("[" + groupingSeparator + "]", "");
        amount = amount.replace(decimalSeparator, '.');

        return new Money(amount);
    }

}
