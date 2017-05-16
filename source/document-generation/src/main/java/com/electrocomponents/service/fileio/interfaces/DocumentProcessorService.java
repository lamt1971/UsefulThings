package com.electrocomponents.service.fileio.interfaces;

import java.util.List;
import java.util.Map;

import com.electrocomponents.model.domain.Locale;
import com.electrocomponents.model.domain.order.FileType;
import com.electrocomponents.service.core.usermessaging.support.ExcelEmailDTO;

/**
 * <pre>
 * ************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author  : Ganesh Raut
 * Created : 13 Jan 2009 at 15:00:16
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
 * A service interface to create pdf & excel files.
 * @author Ganesh Raut
 */
public interface DocumentProcessorService {

    String SERVICE_NAME = "DocumentProcessorService";
    
    String EJB_MODULE_NAME = "document-generation";

    /**
     * This method constructs the pdf file using apache FOP.
     * @param inputContentObject the input object to construct the pdf file.
     * @param xsltPropertyName The property name to retrive the style sheet from db to construct pdf.
     * @param locale The locale to access large property
     * @return pdf file in byte[] format
     */
    byte[] getPdfAsByteArray(final Object inputContentObject, final String xsltPropertyName, final Locale locale);

    /**
     * This method constructs the pdf file using apache FOP.
     * @param inputContentObject the input object to construct the pdf file.
     * @param xsltPropertyName The property name to retrive the style sheet from db to construct pdf.
     * @param locale The locale to access large property
     * @param fontConfigFile This is userConfig file to set font. Pass null if want to use default fonts of system.
     * @return pdf file in byte[] format
     */
    byte[] getPdfAsByteArray(final Object inputContentObject, final String xsltPropertyName, final Locale locale,
            final String fontConfigFile);

    /**
    * This method constructs the pdf file using apache FOP.
     * @param inputContentObject the input object to construct the pdf file.
     * @param xsltPropertyName The property name to retrive the style sheet from db to construct pdf.
     * @param locale The locale to access large property
     * @param fontConfigEnabled This is userConfig file to set font. Pass null if want to use default fonts of system.
     * @param fontConfigFile This is font file
     * @return pdf file in byte[] format
     */
    byte[] getPdfAsByteArray(final Object inputContentObject, final String xsltPropertyName, final Locale locale,
            final String fontConfigEnabled, final String fontConfigFile);

    /**
     * getExcelAsByteArray(1).
     * This method constructs the excel attachment in output stream format using JExcel API.
     * @param excelEmailDTO the excelEmailDTO
     * @param locale Locale
     * @return excel file in byte[] format
     */
    byte[] getExcelAsByteArray(final ExcelEmailDTO excelEmailDTO, final Locale locale);

    /**
     * getExcelAsByteArray(2).
     * This method constructs the excel attachment in output stream format using JExcel API.
     * @param excelEmailDtoMap the excelEmailDtoMap
     * @param locale Locale
     * @return excel file in byte[] format
     */
    byte[] getExcelAsByteArray(final Map<ExcelEmailDTO, ExcelEmailDTO> excelEmailDtoMap, final Locale locale);

    /**
     * getFileDataAsList(1).
     * A method to read file data and then accordingly return a map as: Key: Max Columns to be displayed based on the available data in
     * file. Value: list of data arranged in rows and columns.
     * @param fileData byte array stream for file
     * @param fileExtension file extension
     * @return Map containing max columns & data in file.
     * @throws Exception exception while reading file.
     */
    Map<Integer, List<List<String>>> getFileDataAsList(final byte[] fileData, final FileType fileExtension) throws Exception;

    /**
     * getFileDataAsList(2).
     * A method to read file data and then accordingly return a map as: Key: Max Columns to be displayed based on the available data in
     * file. Value: list of data arranged in rows and columns.
     * @param fileData byte array stream for file
     * @param fileExtension file extension
     * @param maxRows maxRows required (-1 to load entire file)
     * @param maxColumns maxColumns required (-1 to load entire file)
     * @return Map containing max columns & data in file.
     * @throws Exception exception while reading file.
     */
    Map<Integer, List<List<String>>> getFileDataAsList(final byte[] fileData, final FileType fileExtension, final int maxRows,
            final int maxColumns) throws Exception;

    /**
     * getFileDataAsList(3).
     * A method to read file data and then accordingly return a map as: Key: Max Columns to be displayed based on the available data in
     * file. Value: list of data arranged in rows and columns.
     * @param fileData byte array stream for file
     * @param fileExtension file extension
     * @param maxRows maxRows required (-1 to load entire file)
     * @param maxColumns maxColumns required (-1 to load entire file)
     * @param cellMaxCharCount max number of chars to allow in a cell, truncate extra bit (-1 to load entire cell content)
     * @return Map containing max columns & data in file.
     * @throws Exception exception while reading file.
     */
    Map<Integer, List<List<String>>> getFileDataAsList(final byte[] fileData, final FileType fileExtension, final int maxRows,
            final int maxColumns, final int cellMaxCharCount) throws Exception;
    
}
