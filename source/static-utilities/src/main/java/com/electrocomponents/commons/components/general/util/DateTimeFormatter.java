package com.electrocomponents.commons.components.general.util;

import java.util.Calendar;

import javax.xml.datatype.XMLGregorianCalendar;

import com.electrocomponents.model.domain.DateTime;

/**
 * @author C0951983
 *
 */
public class DateTimeFormatter {

    /**
     * This method converts the xmlGregorianCalendar object to DateTime object.
     * @param xmlGregorianCalendar the xmlGregorianCalendar to convert to DateTime
     * @return DateTime object.
     */
    public static DateTime getDateTimeFromXMLGregorianCalendar(final XMLGregorianCalendar xmlGregorianCalendar) {
        if (xmlGregorianCalendar!=null) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(xmlGregorianCalendar.getYear(), xmlGregorianCalendar.getMonth() - 1, xmlGregorianCalendar.getDay());
            DateTime dateTime = new DateTime(calendar.getTime());
            return dateTime;
        } else {
            return null;
        }
    }
}
