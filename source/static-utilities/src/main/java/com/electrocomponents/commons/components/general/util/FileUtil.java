package com.electrocomponents.commons.components.general.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.lang.StringUtils;

import com.electrocomponents.model.domain.order.FileType;

/**
 *
 * <pre>
 * ************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author  : Himanshu Goyal
 * Created : 21 Dec 2009 at 15:59:55
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
 * A util class for File operations.
 * @author Himanshu Goyal
 */
public final class FileUtil {

    /** Private constructor to avoid creating instances. */
    private FileUtil() {
        // Nothing to do
    }

    /**
     * A method to return the file type based on the file name.
     * @param fileName file name
     * @return File type
     */
    public static FileType getFileType(final String fileName) {
        FileType fileType = null;
        if (StringUtils.isNotBlank(fileName)) {
            /* Extract file extension. */
            int fileExtensionIndex = fileName.lastIndexOf(".");
            if (fileExtensionIndex != -1) {
                String fileExtension = fileName.substring(fileExtensionIndex + 1);
                if (StringUtils.isNotBlank(fileExtension)) {
                    if (fileExtension.equalsIgnoreCase("xls")) {
                        fileType = FileType.MS_EXCEL_2003;
                    } else if (fileExtension.equalsIgnoreCase("xlsx")) {
                        fileType = FileType.MS_EXCEL_2007;
                    } else if (fileExtension.equalsIgnoreCase("txt")) {
                        fileType = FileType.TEXT;
                    }
                }
            }
        }
        return fileType;
    }

    /**
     * Returns the contents of the file in a byte array.
     * @param file File this method should read
     * @return byte[] Returns a byte[] array of the contents of the file
     * @throws IOException IO Exception
     */
    public static byte[] getBytesFromFile(final File file) throws IOException {
        InputStream is = new FileInputStream(file);

        /* Get the size of the file. */
        long length = file.length();

        byte[] bytes = null;
        try {
            /*
             * You cannot create an array using a long type. It needs to be an int type. Before converting to an int type, check to ensure that
             * file is not loarger than Integer.MAX_VALUE;
             */
            if (length <= Integer.MAX_VALUE) {
                
                /* Create the byte array to hold the data */
                bytes = new byte[(int) length];
                /* Read in the bytes */
                int offset = 0;
                int numRead = 0;
                while ((offset < bytes.length)) {
                    numRead = is.read(bytes, offset, bytes.length - offset);
                    /* Break look if not enough data left to read. */
                    if (numRead < 0) {
                        break;
                    }
                    offset += numRead;
                }
                /* Ensure all the bytes have been read in */
                if (offset < bytes.length) {
                    throw new IOException("Could not completely read file " + file.getName());
                }
            }
        } finally {
            is.close();
        }
        return bytes;
    }
}
