package com.electrocomponents.commons.components.general.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Copyright (c) Electrocomponents Plc. Author : Andy Holyoak Creation Date : 2000 Creation Time : 16:11:40 IDE : IntelliJ IDEA 5
 * ******************************************************************************************************************* Description : Utility
 * to write files to disk
 * ******************************************************************************************************************* Change History
 * ******************************************************************************************************************* * Change * Author *
 * Date * Description ******************************************************************************************************************* *
 * New * Andy H * 2000 * New class created
 * *******************************************************************************************************************
 * @author Andy H
 */

public class FileControl {

    /** logger. */
    private static Log log = LogFactory.getLog(FileControl.class);
    
    private FileControl() {}

    /**
     * Attempt to open a file with the given filename, if this already exists append a sequence nbr and retry.
     * @param fileName The full path / filename we wish to write to disk
     * @return A FileWriter on the filename we supplied.
     * @throws IOException IOException
     */
    public static FileWriter openFile(final String fileName) throws IOException {
        String tempFileName = fileName;

        if (log.isDebugEnabled()) {
            log.debug("openFile : Method Start");
        }

        java.io.FileWriter fw = null;
        File f = null;

        // Try to create the file with the name passed in
        f = new File(tempFileName);
        int a = 0;

        if (f.exists()) {
            // If the file exists loop around appending numbers on the end until the file is ok
            // Try 10 numbers and then raise an exception

            while (f.exists() && a < 10) {
                f = new File(tempFileName + "." + a);
            }

            if (a > 9) {
                String error = "openFile : Unable to create file";
                log.fatal(error);
            } else {
                tempFileName = tempFileName + "-" + a;
            }
        }

        fw = new java.io.FileWriter(tempFileName);

        if (log.isDebugEnabled()) {
            log.debug("openFile : Method End");
        }
        return fw;
    }

    /**
     * Close the supplied FileWriter.
     * @param fw The FileWriter we wish to close.
     * @throws IOException IOException
     */
    public static void closeFile(final java.io.FileWriter fw) throws IOException {
        if (log.isDebugEnabled()) {
            log.debug("closeFile : Methd Start");
        }

        fw.close();

        if (log.isDebugEnabled()) {
            log.debug("closeFile : Methd End");
        }
    }

    /**
     * Write content to a file.
     * @param fw The handle to the file on the disk
     * @param data The String data to be written to disk.
     */
    public static void writeDetails(final FileWriter fw, final String data) {
        if (log.isDebugEnabled()) {
            log.debug("writeDetails : Methd Start");
        }

        PrintWriter pw = new PrintWriter(fw);

        pw.println(data);
        pw.close();

        if (pw.checkError()) {
            String error = "writeDetails : Failed to write details";
            log.fatal(error);
        }

        if (log.isDebugEnabled()) {
            log.debug("writeDetails : Method End");
        }
    }
    
    /**
     * Read a data file in to a string.
     * @param inFileName The name of the file. (including the directory).
     * @return The contents of the file as a string.
     */
    public static String readFile(final String inFileName) {
        if (log.isDebugEnabled()) { log.debug("readFile : Method Start"); }

        BufferedReader br = null;
        String data = "";
        try {

            br = new BufferedReader(new FileReader(inFileName));
            String line = br.readLine();
            while (line!=null) {
                data = data + line + "\n";
                line = br.readLine();
            }

        } catch (FileNotFoundException e) {
            log.error("readFile : File not found : " + inFileName);
        } catch (IOException e) {
            log.error("readFile : IO Exception ocurred reading file : " + e);
        } finally {
            try {
                // closing br will close its nested FileReader
                br.close();
            } catch (IOException e) {
                log.error("readFile : IO Exception ocurred closing file : " + e);
            }
        }
        if (log.isDebugEnabled()) { log.debug("readFile : Method End"); }
        return data;
    }
}
