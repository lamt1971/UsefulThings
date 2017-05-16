package com.electrocomponents.model.domain;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.joda.time.DateTimeZone;

/**
 * <pre>
 * ************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author  : Ed Ward
 * Created : 1st Aug 2007
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
 * Represents a date and time. The type uses a {@link org.joda.time.DateTime } instance rather than a {@link Date} which has largely been
 * deprecated.
 * @author Ed Ward
 */
public class DateTime implements Serializable, Cloneable {

    /** */
    private static final long serialVersionUID = -7168565843589620103L;

    /** Offset to add for Trade Counter Delivery. */
    private static final int TC_OFFSET = 2;

    /** The Joda DateTime object. */
    private org.joda.time.DateTime jodaDateTime = new org.joda.time.DateTime();

    /** the Java Locale for date formatting. */
    private Locale javaLocale;

    /** a string defining the format of a getTimeString(). */
    private String dateFormat = "dd/MM/yyyy HH:mm";

    /** Defaults to the current date and time. */
    public DateTime() {
    }

    /**
     * @param date the initial date for this instance
     */
    public DateTime(final Date date) {
        this.jodaDateTime = this.jodaDateTime.withMillis(date.getTime());
    }

    /**
     * @param dateTime DateTime
     */
    public DateTime(final DateTime dateTime) {
        this.jodaDateTime = this.jodaDateTime.withMillis(dateTime.getTimeInMillis());
        this.setTimeZone(dateTime.getTimeZone());
        this.setJavaLocale(dateTime.getJavaLocale());
    }

    /**
     * @param timeInMillis the time in milliseconds
     */
    public DateTime(final long timeInMillis) {
        this.jodaDateTime = this.jodaDateTime.withMillis(timeInMillis);

    }

    /**
     * This constructors parameters are used to initialise a <tt>org.joda.time.DateTime</tt> instance. millisOfSecond is set to ZERO.
     * @param year the year
     * @param month the month
     * @param date the date
     * @param hourOfDay the hour
     * @param minute the minute
     * @param second the second
     */
    public DateTime(final int year, final int month, final int date, final int hourOfDay, final int minute, final int second) {
        this.jodaDateTime = this.jodaDateTime.withDate(year, month, date).withTime(hourOfDay, minute, second, 0);
    }

    /**
     * Create a org.joda.time.DateTime object from a date String and date format string.
     * @param dateString Date as a String
     * @param dateFormat date format as a String
     * @throws ParseException Exception
     */
    public DateTime(final String dateString, final String dateFormat) throws ParseException {
        setDate(dateString, dateFormat);
    }

    /**
     * @return a date instance corresponding to this date time
     */
    public Date getDate() {
        return jodaDateTime.toDate();
    }

    /**
     * @return the string representation of this instance
     */
    @Override
    public String toString() {
        return this.jodaDateTime.toDate().toString();
    }

    /**
     * Over-riding cloning.
     * @return DateTime the DateTime
     */
    public Object clone() {
        DateTime dateTime = null;
        try {
            dateTime = (DateTime) super.clone();
        } catch (CloneNotSupportedException e) {
            // this should never happen
            throw new InternalError(e.toString());
        }
        dateTime.jodaDateTime = jodaDateTime.toDateTime();
        return dateTime;
    }

    /**
     * @return a hash code for the object
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((jodaDateTime == null) ? 0 : jodaDateTime.hashCode());
        return result;
    }

    /**
     * @param obj the object to compare
     * @return <tt>true</tt> if the objects are equal
     */
    @Override
    public boolean equals(final Object obj) {
        try {
            return compareTo(obj) == 0;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Compares the time of two org.joda.time.DateTime objects.
     * @param obj a DateTime
     * @return the value 0 if the time represented by the argument is equal to the time represented by this jodaDateTime; a value less than
     * 0 if the time of this jodaDateTime is before the time represented by the argument; and a value greater than 0 if the time of this
     * jodaDateTime is after the time represented by the argument.
     * @throws Exception if obj is not a DateTime or the values in either this or obj are not valid.
     */
    public int compareTo(final Object obj) throws Exception {
        final DateTime other = (DateTime) obj;
        return jodaDateTime.compareTo(other.jodaDateTime);
    }

    /**
     * Returns whether this DateTime represents a time before the time represented by the specified Object.
     * @param when a DateTime
     * @return true if the time of this DateTime is before the time represented by when; false otherwise
     */
    public boolean before(final DateTime when) {
        try {
            return compareTo(when) < 0;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Returns whether this DateTime represents a time after the time represented by the specified Object.
     * @param when a DateTime
     * @return true if the time of this DateTime is after the time represented by when; false otherwise
     */
    public boolean after(final DateTime when) {
        try {
            return compareTo(when) > 0;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * set the time zone for this DateTime.
     * @param tz a DateTimeZone
     */
    public void setTimeZone(final DateTimeZone tz) {
        this.jodaDateTime = jodaDateTime.withZone(tz);
    }

    /**
     * get the time zone for this DateTime.
     * @return DateTimeZone for this
     */
    public DateTimeZone getTimeZone() {
        return jodaDateTime.getZone();
    }

    /**
     * set the time zone for this DateTime.
     * @param tzString a DateTimeZone id (string)
     */
    public void setTimeZone(final String tzString) {
        setTimeZone(DateTimeZone.forID(tzString));
    }

    /**
     * get a time zone from a string.
     * @param tzString a time zone string
     * @return DateTimeZone for this
     */
    public DateTimeZone getTimeZone(final String tzString) {
        return DateTimeZone.forID(tzString);
    }

    /**
     * set the Java locale by language and country.
     * @param language should be "en" etc.
     * @param country should be "UK" etc.
     */
    public void setLocale(final String language, final String country) {
        Locale lc = new Locale(language, country);
        if (lc != null) {
            javaLocale = lc;
        }
    }

    /**
     * get the Java locale.
     * @return Locale java locale for this
     */
    public Locale getLocale() {
        return this.javaLocale;
    }

    /**
     * set the Java locale by property value (i.e. JAVA_LOCALE)
     * @param javaLocaleString should be "en_UK" etc.
     */
    public void setLocale(final String javaLocaleString) {
        Locale lc = null;
        String[] arr = javaLocaleString.split("_");
        if (arr.length > 1) {
            lc = new Locale(arr[0], arr[0]);
        }
        if (lc != null) {
            javaLocale = lc;
        }
    }

    /**
     * @return the dateFormat
     */
    public String getDateFormat() {
        return dateFormat;
    }

    /**
     * @param dateFormat the dateFormat to set.
     */
    public void setDateFormat(final String dateFormat) {
        this.dateFormat = dateFormat;
    }

    /**
     * @return the javaLocale.
     */
    public Locale getJavaLocale() {
        return javaLocale;
    }

    /**
     * @param javaLocale the javaLocale to set.
     */
    public void setJavaLocale(final Locale javaLocale) {
        this.javaLocale = javaLocale;
    }

    /**
     * @param dateFormat dateFormat
     * @return a date time formatted according to dateFormat.
     */
    public String getDateTimeString(final String dateFormat) {
        if (this.javaLocale != null) {
            return jodaDateTime.toString(dateFormat, this.javaLocale);
        } else {
            return jodaDateTime.toString(dateFormat);
        }
    }

    /**
     * @return a date time formatted according to dateFormat.
     */
    public String getDateTimeString() {
        if (this.javaLocale != null) {
            return jodaDateTime.toString(dateFormat, this.javaLocale);
        } else {
            return jodaDateTime.toString(dateFormat);
        }
    }

    /**
     * Returns this DateTime's time value in milliseconds.
     * @return the current time as UTC milliseconds from the epoch.
     */
    public long getTimeInMillis() {
        return jodaDateTime.getMillis();
    }

    /**
     * Returns the day of the week for any given date.
     * @param dt DateTime
     * @return the day of the week (e.g Mon,Tue,Wed..)
     */
    public int getDayOfTheWeek(final DateTime dt) {
        return jodaDateTime.getDayOfWeek();
    }

    /**
     * Returns the first day of the month for any given date.
     * @param dt DateTime
     * @return the day of the week (e.g Mon,Tue,Wed..)
     */
    public int getDayOfTheMonth(final DateTime dt) {
        return jodaDateTime.getDayOfMonth();
    }

    /**
     * Set Last day to last of the month.
     * @param dayOfMonth dayOfMonth
     */
    public void setDayOfMonth(final int dayOfMonth) {
        this.jodaDateTime = this.jodaDateTime.withDayOfMonth(dayOfMonth);
    }

    /**
     * @return lastDayOfMonth. Fetch LastDay of the month.
     */
    public int getLastDayOfMonth() {
        return this.jodaDateTime.dayOfMonth().withMaximumValue().getDayOfMonth();
    }

    /**
     * @return fisrtDayOfMonth. Fetch first Day of the month.
     */
    public int getFirstDayOfMonth() {
        return this.jodaDateTime.dayOfMonth().withMinimumValue().getDayOfMonth();
    }

    /**
     * Returns year from a given date time.
     * @param dt DateTime
     * @return the year from a given date
     */
    public int getYear(final DateTime dt) {
        return jodaDateTime.getYear();
    }

    /**
     * Returns the date with a day added to the given datetime.
     */
    public void addDayToDate() {
        this.jodaDateTime = jodaDateTime.plusDays(1);
    }

    /**
     * Returns the date with a day added to the given datetime.
     */
    public void minusDayToDate() {
        minusDayToDate(1);
    }

    /**
     * Returns the date with the given number of days removed from datetime.
     * @param days int
     */
    public void minusDayToDate(final int days) {
        this.jodaDateTime = jodaDateTime.minusDays(days);
    }

    /**
     * Returns the date with the given number of months removed from datetime.
     * @param months int
     */
    public void minusMonthToDate(final int months) {
        this.jodaDateTime = jodaDateTime.minusMonths(months);
    }

    /**
     * Returns the date with the given number of days added.
     * @param days int
     */
    public void addDaysToDate(final int days) {
        this.jodaDateTime = jodaDateTime.plusDays(days);
    }

    /**
     * Sets the starting time of the day to 00:00:000.
     */
    public void setStartOfDay() {
        this.jodaDateTime = this.jodaDateTime.withTime(0, 0, 0, 0);
    }

    /**
     * Sets the ending time of the day to 24:00.
     */
    public void setEndOfDay() {
        this.jodaDateTime = this.jodaDateTime.withTime(23, 59, 59, 0);
    }

    /**
     * Add Offset Time of the day by +02:00.
     */
    public void addOffset() {
        this.jodaDateTime = this.jodaDateTime.plusHours(TC_OFFSET);

    }

    /**
     * Add Offset Time(minutes) of the day by the given value.
     * @param offsetInMin the number of minutes to add.
     */
    public void addOffsetMinutes(final int offsetInMin) {
        this.jodaDateTime = this.jodaDateTime.plusMinutes(offsetInMin);

    }

    /**
     * Sets the opening time of the day.
     * @param hours hours
     * @param minutes minutes
     */
    public void setOpeningOfDay(final int hours, final int minutes) {
        this.jodaDateTime = this.jodaDateTime.withTime(hours, minutes, 0, 0);
    }

    /**
     * Set the Date from a date String and date format string.
     * @param dateString Date as a String
     * @param dateFormat date format as a String
     * @throws ParseException Exception
     */
    public void setDate(final String dateString, final String dateFormat) throws ParseException {
        /* Modified to catch parse exception if the formatter is not able to parse the string into a valid date. */
        try {
            SimpleDateFormat dateFormater = new SimpleDateFormat(dateFormat);
            dateFormater.setLenient(false);
            Date parsedDate = dateFormater.parse(dateString);
            org.joda.time.DateTime parsedDateTime = new org.joda.time.DateTime(parsedDate);
            this.jodaDateTime =
                    this.jodaDateTime.withDate(parsedDateTime.getYear(), parsedDateTime.getMonthOfYear(), parsedDateTime.getDayOfMonth())
                            .withTime(parsedDateTime.getHourOfDay(), parsedDateTime.getMinuteOfHour(), parsedDateTime.getSecondOfMinute(),
                                    0);
        } catch (ParseException ex) {
            throw ex;
        }

    }

    /**
     * Returns the day of the month for any given date.
     * @return the day of the month
     */
    public int getDayOfTheMonth() {
        return jodaDateTime.getDayOfMonth();
    }

    /**
     * Returns month for any given date.
     * @return the month from a given date
     */
    public int getMonth() {
        return jodaDateTime.getMonthOfYear();
    }

    /**
     * Returns year for any given date.
     * @return the year from a given date
     */
    public int getYear() {
        return jodaDateTime.getYear();
    }

    /**
     * Returns XMLGregorianCalendar date for a given date.
     * @return the XMLGregorianCalendar from a given date
     * @throws DatatypeConfigurationException thrown from DatatypeFactory
     */
    public XMLGregorianCalendar dateToXML() throws DatatypeConfigurationException {
        XMLGregorianCalendar xmlGregorianCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar();
        xmlGregorianCalendar.setDay(getDayOfTheMonth());
        xmlGregorianCalendar.setMonth(getMonth());
        xmlGregorianCalendar.setYear(getYear());
        return xmlGregorianCalendar;
    }

    /**
     * Returns XMLGregorianCalendar date for a given date.
     * @return the XMLGregorianCalendar from a given date
     * @throws DatatypeConfigurationException thrown from DatatypeFactory
     */
    public XMLGregorianCalendar dateTimeToXML() throws DatatypeConfigurationException {
        XMLGregorianCalendar xmlGregorianCalendar = dateToXML();
        xmlGregorianCalendar.setHour(jodaDateTime.getHourOfDay());
        xmlGregorianCalendar.setMinute(jodaDateTime.getMinuteOfHour());
        xmlGregorianCalendar.setSecond(jodaDateTime.getSecondOfMinute());
        return xmlGregorianCalendar;
    }

    /**
     * Method returns the date of the coming day of the week relative to the current date.
     * @param dateTime as DateTime
     * @param comingdayOfTheWeek int
     * @return DateTime
     */
    public DateTime getDateOfComingDayOfTheweek(final DateTime dateTime, final int comingdayOfTheWeek) {
        int currentDayOfTheWeek = dateTime.getDayOfTheWeek(dateTime);
        if (comingdayOfTheWeek > currentDayOfTheWeek) {
            dateTime.addDaysToDate(comingdayOfTheWeek - currentDayOfTheWeek);
            return dateTime;
        } else if (currentDayOfTheWeek > comingdayOfTheWeek) {
            dateTime.addDaysToDate(7);
            dateTime.addDaysToDate(-(currentDayOfTheWeek - comingdayOfTheWeek));
            return dateTime;
        } else {
            dateTime.addDaysToDate(7);
            return dateTime;
        }

    }

    /**
     * Returns the date with months added/subtracted to the given date time.
     * @param months int
     */
    public void addMonthsToDate(final int months) {
        this.jodaDateTime = jodaDateTime.plusMonths(months);
    }

    /**
     * Gets the date in dd/mm/yyyy hh:mi:ss format.
     * @return a string date corresponding to this date time
     */
    public String getDateTimeInString() {
        return jodaDateTime.toString(dateFormat);
    }
}
