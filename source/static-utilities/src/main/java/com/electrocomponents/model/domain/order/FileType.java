/**
 * Package defining order domain objects and enums.
 */
package com.electrocomponents.model.domain.order;

/**
 * <pre>
 * ************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author  : Himanshu Goyal.
 * Created : 19 Nov 2009 at 15:54:12
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
 * Enum to specify file type (Text, Excel etc).
 * @author Himanshu Goyal.
 */
public enum FileType {

    /** Excel file extensions for file version <= 2003. */
    MS_EXCEL_2003("xls"),

    /** Excel file extensions for file version 2007. */
    MS_EXCEL_2007("xlsx"),

    /** Text file. */
    TEXT("txt");

    /** The value representing the enum in the database. */
    private String value;

    /**
     * @param value {@link #value}
     */
    private FileType(final String value) {
        this.value = value;
    }

    /**
     * @return {@link #value}
     */
    public String getFileType() {
        return this.value;
    }

    /**
     * @param value File type
     * @return the enum associated with the supplied value
     */
    public static FileType value(final String value) {
        FileType fileType = null;
        for (final FileType ft : values()) {
            if (value.equals(ft.value)) {
                fileType = ft;
                break;
            }
        }
        return fileType;
    }
}
