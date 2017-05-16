package com.electrocomponents.service.fileio.impl;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.URIResolver;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.DefaultConfigurationBuilder;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fop.apps.FOURIResolver;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.electrocomponents.commons.components.general.util.LabelParserUtility;
import com.electrocomponents.commons.components.general.util.MoneyParserUtility;
import com.electrocomponents.model.data.usermessaging.MailSettings;
import com.electrocomponents.model.domain.Locale;
import com.electrocomponents.model.domain.order.FileType;
import com.electrocomponents.service.core.client.JAXBSupportUtility;
import com.electrocomponents.service.core.client.ServiceLocator;
import com.electrocomponents.service.core.config.interfaces.LabelService;
import com.electrocomponents.service.core.config.interfaces.PropertyService;
import com.electrocomponents.service.core.usermessaging.interfaces.UserMessagingService;
import com.electrocomponents.service.core.usermessaging.support.ExcelEmailDTO;
import com.electrocomponents.service.core.usermessaging.support.InputType;
import com.electrocomponents.service.fileio.interfaces.DocumentProcessorService;
import com.electrocomponents.service.fileio.interfaces.DocumentProcessorServiceLocal;
import com.electrocomponents.service.fileio.interfaces.DocumentProcessorServiceRemote;

import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.format.VerticalAlignment;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

/**
 * <pre>
 * ************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author  : Ganesh Raut/Himanshu Goyal
 * Created : 13 Jan 2009 at 15:12:06
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
 * <pre>
 * A class for document processing (PDF, Excel, Delimeter separated text files).
 *      Create - PDF, Excel files
 *      Read - Excel, Text files (delimeter separated)
 * </pre>
 * @author Ganesh Raut/Himanshu Goyal
 */
@Stateless(name = DocumentProcessorService.SERVICE_NAME)
@Local(DocumentProcessorServiceLocal.class)
@Remote(DocumentProcessorServiceRemote.class)
public class DocumentProcessorServiceBean implements DocumentProcessorServiceLocal, DocumentProcessorServiceRemote {

    /** Commons logging logger. */
    private static final Log LOG = LogFactory.getLog(DocumentProcessorServiceBean.class);
    /** Property name for quote manager font config file. */
    private static final String QM_FONT_CONFIG_FILE = "QM_FONT_CONFIG_FILE";

    /** {@inheritDoc} */
    public byte[] getPdfAsByteArray(final Object inputContentObject, final String xsltPropertyName, final Locale locale) {
        return getPdfAsByteArray(inputContentObject, xsltPropertyName, locale, null, null);
    }

    /** {@inheritDoc} */
    public byte[] getPdfAsByteArray(final Object inputContentObject, final String xsltPropertyName, final Locale locale,
            final String fontConfigEnabled) {
        // If no parameter is passed for font file, QM will be used.
        return getPdfAsByteArray(inputContentObject, xsltPropertyName, locale, fontConfigEnabled, QM_FONT_CONFIG_FILE);
    }

    /** {@inheritDoc} */
    public byte[] getPdfAsByteArray(final Object inputContentObject, final String xsltPropertyName, final Locale locale,
            final String fontConfigEnabled, final String fontConfigFile) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Start getPdfAsByteArray.");
        }

        /* Convert the input object into xml format using jaxb. */
        String inputContentXML = JAXBSupportUtility.getInstance().convertObjectToXmlString(inputContentObject);

        boolean exceptionOccured = false;

        /* Setup the output stream. */
        ByteArrayOutputStream pdfOutputStream = new ByteArrayOutputStream();
        byte[] outputToReturn = null;

        try {

            PropertyService propertyService = ServiceLocator.locateLocalOrRemote(PropertyService.class, locale);
            String xsltForFop = propertyService.getLargeProperty(xsltPropertyName, locale);

            /* parsing the xslt and replacing the label names with the label values. */
            xsltForFop = LabelParserUtility.parseHTMLForLabels(xsltForFop, locale);

            /* Parse HTML content to search for money format place holders and replace them with the formatted values. */
            xsltForFop = MoneyParserUtility.parseHTMLForMoneyFormat(xsltForFop, locale);

            try {
                /* Configure the fopFactory & foUserAgent. */
                FopFactory fopFactory = FopFactory.newInstance();
                
                // supply a custom URI resolver to allow fonts to be loaded from the classpath
                ((FOURIResolver) fopFactory.getURIResolver()).setCustomURIResolver(new ClassPathURIResolver());
                InputStream fontConfigInputStream = null;
                String fontConfigFileString = null;
                if (fontConfigEnabled != null) {
                    Boolean checkFontConfig = propertyService.getBooleanProperty(fontConfigEnabled, locale);
                    if (checkFontConfig != null && checkFontConfig) {
                        fontConfigFileString = propertyService.getLargeProperty(fontConfigFile, locale);
                        if (fontConfigFileString != null && !(fontConfigFileString.trim().isEmpty())) {
                            try {
                                fontConfigInputStream = new ByteArrayInputStream(fontConfigFileString.getBytes("UTF-8"));
                            } catch (UnsupportedEncodingException e) {
                                LOG.error("Exception occured while writing file:" + e.getMessage());
                                fontConfigInputStream = null;
                            }
                            /* Set Fop to read user config file which uses user define fonts. */
                            if (fontConfigInputStream != null) {
                                DefaultConfigurationBuilder cfgBuilder = new DefaultConfigurationBuilder();
                                Configuration cfg = cfgBuilder.build(fontConfigInputStream);
                                fopFactory.setUserConfig(cfg);
                            }
                        }
                    }
                }
                FOUserAgent foUserAgent = fopFactory.newFOUserAgent();
                /* Construct fop with desired output format. */
                Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, foUserAgent, pdfOutputStream);

                /* Setup XSLT. */
                TransformerFactory factory = TransformerFactory.newInstance();
                StringReader xsltReader = new StringReader(xsltForFop);
                Transformer transformer = factory.newTransformer(new StreamSource(xsltReader));

                /* Setup input for XSLT transformation. */
                StringReader xmlReader = new StringReader(inputContentXML);
                Source src = new StreamSource(xmlReader);

                /* Resulting SAX events (the generated FO) must be piped through to FOP. */
                Result res = new SAXResult(fop.getDefaultHandler());

                /* Start XSLT transformation and FOP processing. */
                transformer.transform(src, res);

                outputToReturn = pdfOutputStream.toByteArray();
            } catch (TransformerConfigurationException tce) {
                exceptionOccured = true;
                String errorXSLMailContents =
                        "\n" + "XSLT ERROR : " + "\n\n" + tce.toString() + "\n\n" + "XSLT_PROPERTY_NAME : " + xsltPropertyName + "\n\n"
                                + "Locale : " + locale + "\n\n" + "Style Sheet :" + "\n\n" + xsltForFop + "\n\n\n" + "Input XML :" + "\n\n"
                                + inputContentXML;
                LOG.error("TransformerConfigurationException while XMLTransform : XSLT_PROPERTY_NAME : " + xsltPropertyName
                        + " :: Locale : " + locale + "::" + tce);
                try {
                    sendErrorEmail(errorXSLMailContents, locale);
                } catch (Exception exception) {
                    LOG.error("Exception while sending error email to a support email address " + exception);
                }

            } catch (TransformerException te) {
                exceptionOccured = true;
                String errorXSLMailContents =
                        "\n" + "XSLT ERROR : " + "\n\n" + te.toString() + "\n\n" + "XSLT_PROPERTY_NAME : " + xsltPropertyName + "\n\n"
                                + "Locale : " + locale + "\n\n" + "Style Sheet :" + "\n\n" + xsltForFop + "\n\n\n" + "Input XML :" + "\n\n"
                                + inputContentXML;
                LOG.error("TransformerException while XMLTransform : XSLT_PROPERTY_NAME : " + xsltPropertyName + " :: Locale : " + locale
                        + "::" + te);
                try {
                    sendErrorEmail(errorXSLMailContents, locale);
                } catch (Exception exception) {
                    LOG.error("Exception while sending error email to a support email address ", exception);
                }

            } finally {
                pdfOutputStream.close();
            }

        } catch (Exception e) {
            exceptionOccured = true;
            LOG.error("Exception while creating Pdf using FOP : ", e);
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish getPdfAsByteArray.");
        }
        if (exceptionOccured) {
            return null;
        }
        return outputToReturn;
    }

    /** {@inheritDoc} */
    public byte[] getExcelAsByteArray(final ExcelEmailDTO excelEmailDTO, final Locale locale) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start getExcelAsByteArray(1).");
        }

        /* Setup output. */
        ByteArrayOutputStream attachmentOutputStream = new ByteArrayOutputStream();
        LabelService labelService = ServiceLocator.locateLocalOrRemote(LabelService.class, locale);
        try {

            List<List<String>> elementList = excelEmailDTO.getElementList();
            Map<String, Integer> headerMap = excelEmailDTO.getHeaderMap();
            Set<String> headerNameList = headerMap.keySet();

            WritableWorkbook workbook = Workbook.createWorkbook(attachmentOutputStream);
            WritableSheet sheet = workbook.createSheet("First Sheet", 0);
            sheet.setRowView(0, 350);
            sheet.getSettings().setVerticalFreeze(1);

            WritableFont arial10BoldFont = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);
            WritableCellFormat arial10Boldformat = new WritableCellFormat(arial10BoldFont);
            arial10Boldformat.setAlignment(Alignment.CENTRE);
            arial10Boldformat.setBackground(Colour.GRAY_50);
            arial10Boldformat.setBorder(Border.ALL, BorderLineStyle.THIN);
            arial10Boldformat.setVerticalAlignment(VerticalAlignment.CENTRE);

            int row = 0;
            if (headerNameList != null && headerNameList.size() > 0) {
                Iterator<String> childListIterator = headerNameList.iterator();
                int column = 0;
                while (childListIterator.hasNext()) {
                    String cellvalue = (String) childListIterator.next();
                    sheet.setColumnView(column, headerMap.get(cellvalue));
                    jxl.write.Label item =
                            new jxl.write.Label(column++, row, labelService.getLabelValue(cellvalue, locale), arial10Boldformat);
                    sheet.addCell(item);
                }
                row++;
            }

            Iterator<List<String>> elementListIterator = elementList.iterator();
            while (elementListIterator.hasNext()) {
                List<String> childList = elementListIterator.next();
                Iterator<String> childListIterator = childList.iterator();
                int column = 0;
                while (childListIterator.hasNext()) {
                    String cellvalue = (String) childListIterator.next();
                    jxl.write.Label item = new jxl.write.Label(column++, row, cellvalue);
                    sheet.addCell(item);
                }
                row++;
                sheet.setRowView(row, 300);
            }

            workbook.write();
            workbook.close();

        } catch (Exception e) {
            LOG.error("Exception while creating excel file for Input = \n" + excelEmailDTO);
        }
        byte[] bs = attachmentOutputStream.toByteArray();
        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish getExcelAsByteArray(1).");
        }
        return bs;
    }

    /** {@inheritDoc} */
    public byte[] getExcelAsByteArray(final Map<ExcelEmailDTO, ExcelEmailDTO> excelEmailDtoMap, final Locale locale) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start getExcelAsByteArray(2).");
        }

        ByteArrayOutputStream attachmentOutputStream = new ByteArrayOutputStream();
        LabelService labelService = ServiceLocator.locateLocalOrRemote(LabelService.class, locale);
        try {

            WritableWorkbook workbook = Workbook.createWorkbook(attachmentOutputStream);
            WritableSheet sheet = workbook.createSheet("First Sheet", 0);

            // sheet.getSettings().setVerticalFreeze(1);
            sheet.getSettings().setShowGridLines(Boolean.TRUE);
            WritableFont arial10Font = new WritableFont(WritableFont.ARIAL, 10, WritableFont.NO_BOLD);
            WritableFont arial10BoldFont = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);
            WritableFont arial10BoldFontWhiteText =
                    new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE, Colour.WHITE);

            WritableCellFormat arial10BoldHeaderOneformat = new WritableCellFormat(arial10BoldFontWhiteText);
            arial10BoldHeaderOneformat.setAlignment(Alignment.LEFT);
            arial10BoldHeaderOneformat.setBackground(Colour.GRAY_80);
            arial10BoldHeaderOneformat.setVerticalAlignment(VerticalAlignment.CENTRE);
            arial10BoldHeaderOneformat.setBorder(Border.ALL, BorderLineStyle.THIN);
            arial10BoldHeaderOneformat.setWrap(Boolean.TRUE);

            WritableCellFormat arial10BoldHeaderTwoformat = new WritableCellFormat(arial10BoldFont);
            arial10BoldHeaderTwoformat.setAlignment(Alignment.LEFT);
            arial10BoldHeaderTwoformat.setBackground(Colour.GRAY_25);
            arial10BoldHeaderTwoformat.setVerticalAlignment(VerticalAlignment.CENTRE);
            arial10BoldHeaderTwoformat.setBorder(Border.ALL, BorderLineStyle.THIN);
            arial10BoldHeaderTwoformat.setWrap(Boolean.TRUE);

            WritableCellFormat arial10BoldFooterformat = new WritableCellFormat(arial10Font);
            arial10BoldFooterformat.setAlignment(Alignment.LEFT);
            arial10BoldFooterformat.setVerticalAlignment(VerticalAlignment.CENTRE);
            arial10BoldFooterformat.setBorder(Border.ALL, BorderLineStyle.THIN);
            arial10BoldFooterformat.setWrap(Boolean.TRUE);
            // Getting the top border
            Set<ExcelEmailDTO> keySet = excelEmailDtoMap.keySet();
            int column = 0, row = 2;

            for (ExcelEmailDTO headerExcelEmailDTO : keySet) {
                ExcelEmailDTO childExcelEmailDTO = excelEmailDtoMap.get(headerExcelEmailDTO);
                int count = 0;
                column = 0;
                sheet.setColumnView(column, 380);
                Map<String, Integer> headerMap = headerExcelEmailDTO.getHeaderMap();
                Set<String> headerNameList = headerMap.keySet();
                List<List<String>> headerElementList = headerExcelEmailDTO.getElementList();
                Iterator<List<String>> elementListIterator = headerElementList.iterator();
                if (headerNameList != null && headerNameList.size() > 0) {
                    Iterator<String> childListIterator = headerNameList.iterator();
                    sheet.setRowView(row, 400);
                    while (childListIterator.hasNext()) {
                        String cellvalue = (String) childListIterator.next();
                        // headerMap.get(cellvalue));
                        sheet.setColumnView(column, 30);
                        jxl.write.Label item =
                                new jxl.write.Label(column, row++, labelService.getLabelValue(cellvalue, locale),
                                        arial10BoldHeaderOneformat);
                        sheet.addCell(item);
                        count++;
                    }
                    column++;
                }
                sheet.setRowView(row, 400);
                while (elementListIterator.hasNext()) {
                    List<String> childList = elementListIterator.next();
                    Iterator<String> childListIterator = childList.iterator();
                    row -= count;
                    while (childListIterator.hasNext()) {
                        String cellvalue = (String) childListIterator.next();
                        sheet.setColumnView(column, 30);
                        jxl.write.Label item = new jxl.write.Label(column, row++, cellvalue, arial10BoldHeaderTwoformat);
                        sheet.addCell(item);
                    }
                    column++;
                }
                sheet.setRowView(row, 400);
                List<List<String>> childHeaderElementList = childExcelEmailDTO.getElementList();
                Iterator<List<String>> childElementListIterator = childHeaderElementList.iterator();
                while (childElementListIterator.hasNext()) {
                    List<String> childList = childElementListIterator.next();
                    Iterator<String> childListIterator = childList.iterator();
                    row -= count;
                    while (childListIterator.hasNext()) {
                        String cellvalue = (String) childListIterator.next();
                        sheet.setColumnView(column, 30);
                        jxl.write.Label item = new jxl.write.Label(column, row++, cellvalue, arial10BoldFooterformat);
                        sheet.addCell(item);
                    }
                    column++;
                    // sheet.setRowView(row, 300);
                }
                row++;
            }

            workbook.write();
            workbook.close();

        } catch (Exception e) {
            if (LOG.isErrorEnabled()) {
                LOG.error("Exception while creating excel file for Input = \n" + excelEmailDtoMap);
            }
        }
        byte[] bs = attachmentOutputStream.toByteArray();
        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish getExcelAsByteArray(2).");
        }
        return bs;
    }

    /** {@inheritDoc} */
    public Map<Integer, List<List<String>>> getFileDataAsList(final byte[] fileData, final FileType fileExtension) throws Exception {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start getFileDataAsList(1).");
        }
        /* Pass maxRows and maxColumns as -1 to load entire file list. */
        Map<Integer, List<List<String>>> map = getFileDataAsList(fileData, fileExtension, -1, -1, -1);
        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish getFileDataAsList(1).");
        }
        return map;
    }

    /** {@inheritDoc} */
    public Map<Integer, List<List<String>>> getFileDataAsList(final byte[] fileData, final FileType fileExtension, final int maxRows,
            final int maxColumns) throws Exception {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start getFileDataAsList(2).");
        }
        /* Pass maxRows and maxColumns as -1 to load entire file list. */
        Map<Integer, List<List<String>>> map = getFileDataAsList(fileData, fileExtension, maxRows, maxColumns, -1);
        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish getFileDataAsList(2).");
        }
        return map;
    }

    /** {@inheritDoc} */
    public Map<Integer, List<List<String>>> getFileDataAsList(final byte[] fileData, final FileType fileExtension, final int maxRows,
            final int maxColumns, final int cellMaxCharCount) throws Exception {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start getFileDataAsList(3).");
        }
        Map<Integer, List<List<String>>> fileContentMap = null;
        /* Check if the file data or file extension is not specified, return null. */
        if (fileData == null || fileExtension == null || StringUtils.isEmpty(fileExtension.getFileType())) {
            return null;
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("Reading file with extension: " + fileExtension.getFileType());
        }
        /* Check the file extension and accordingly call private methods to read file data. */
        if (FileType.TEXT.equals(fileExtension)) {
            fileContentMap = readTabSeparatedTextFile(fileData, maxRows, maxColumns, cellMaxCharCount);
        } else if (FileType.MS_EXCEL_2003.equals(fileExtension)) {
            fileContentMap = readExcelFile(fileData, maxRows, maxColumns, cellMaxCharCount);
        } else if (FileType.MS_EXCEL_2007.equals(fileExtension)) {
            fileContentMap = readExcelFile(fileData, maxRows, maxColumns, cellMaxCharCount);
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish getFileDataAsList(3).");
        }
        return fileContentMap;
    }

    /**
     * A method to read excel files and arrange them as a list of rows and columns. This method will return a map with key as "Max columns"
     * and value as the list of actual data. The parameters maxRows and maxColumns if passed values will restrict loading the collection
     * list to specific size.
     * @param fileData the fileData
     * @param maxRows maxRows required (-1 to load entire file)
     * @param maxColumns maxColumns required (-1 to load entire file)
     * @param cellMaxCharCount max no of chars allowed in a cell (-1 to load entire cell content)
     * @return Map of file data and max columns.
     * @throws Exception exception while reading excel file.
     */
    private Map<Integer, List<List<String>>> readExcelFile(final byte[] fileData, final int maxRows, final int maxColumns,
            final int cellMaxCharCount) throws Exception {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start readExcelFile.");
        }
        Map<Integer, List<List<String>>> fileContentMap = new HashMap<Integer, List<List<String>>>();
        List<List<String>> fileContentList = new ArrayList<List<String>>();

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(fileData);
        org.apache.poi.ss.usermodel.Workbook workBook = WorkbookFactory.create(byteArrayInputStream);
        if (LOG.isDebugEnabled()) {
            LOG.debug("numberOfSheets : " + workBook.getNumberOfSheets());
        }
        FormulaEvaluator formulaEvaluator = workBook.getCreationHelper().createFormulaEvaluator();
        Sheet sheet = workBook.getSheetAt(0);
        if (LOG.isDebugEnabled()) {
            LOG.debug("Max rows to read: " + maxRows);
        }
        int rowCount = 0;
        int startColumnIndex = -1;
        int endColumnIndex = -1;
        Map<Integer, Map<Integer, String>> rowMap = new LinkedHashMap<Integer, Map<Integer, String>>();
        Map<Integer, String> columnMap = null;
        if (sheet != null) {
            for (Row row : sheet) {
                boolean isRowContainsData = false;
                int cellCount = 0;
                columnMap = new HashMap<Integer, String>();
                for (Cell cell : row) {
                    /* Increment the cellCount counter and break if the max Cols are read. */
                    ++cellCount;
                    if (maxColumns > 0 && cellCount > maxColumns) {
                        break;
                    }
                    if (cell != null) {
                        String cellValue = "";

                        /* Get cell value as string irrespective of cell type. */
                        DataFormatter dataFormatter = new DataFormatter();
                        try {
                            cellValue = dataFormatter.formatCellValue(cell, formulaEvaluator);
                        } catch (RuntimeException e) {
                            cellValue = dataFormatter.formatCellValue(cell);
                        }
                        if (!org.apache.commons.lang.StringUtils.isEmpty(cellValue)) {
                            isRowContainsData = true;
                            /* If there is a max char limit for cell, then truncate extra chars. */
                            if (cellMaxCharCount > 0 && cellValue.length() > cellMaxCharCount) {
                                cellValue = cellValue.substring(0, cellMaxCharCount);
                            }
                            // Added this line for newer implementation.
                            columnMap.put(cell.getColumnIndex(), cellValue.trim());

                        }
                    }
                }
                if (isRowContainsData) {
                    /* Setting the start column Index. */
                    if (startColumnIndex < 0 || startColumnIndex > row.getFirstCellNum()) {
                        startColumnIndex = row.getFirstCellNum();
                    }
                    /* Setting the end column Index. */
                    if (endColumnIndex < 0 || endColumnIndex < row.getLastCellNum()) {
                        endColumnIndex = row.getLastCellNum();
                    }

                    /* Increment the rowCount counter and break if the max Rows are read. */
                    ++rowCount;
                    if (maxRows > 0 && rowCount > maxRows) {
                        break;
                    }
                    // Added this line for newer implementation.
                    rowMap.put(row.getRowNum(), columnMap);

                }

            }
        }
        /* Convert the data into matrix. */
        Map<Integer, String> tempColumnMap = null;
        for (Map.Entry<Integer, Map<Integer, String>> rowMapEntrySet : rowMap.entrySet()) {
            tempColumnMap = rowMapEntrySet.getValue();
            if (tempColumnMap != null && !tempColumnMap.isEmpty()) {
                List<String> rowData = new ArrayList<String>();
                for (int i = startColumnIndex; i < endColumnIndex; ++i) {
                    if (tempColumnMap.get(i) != null) {
                        rowData.add(tempColumnMap.get(i));
                    } else {
                        rowData.add("");
                    }
                }
                fileContentList.add(rowData);
            }

        }
        fileContentMap.put(endColumnIndex - startColumnIndex, fileContentList);
        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish readExcelFile.");
        }
        return fileContentMap;
    }

    /**
     * A method to read tab separated files and arrange them as a list of rows and columns. This method will return a map with key as "Max
     * columns" and value as the list of actual data. The parameters maxRows and maxColumns if passed values will restrict loading the
     * collection list to specific size.
     * @param fileData the fileData
     * @param maxRows maxRows required (-1 to load entire file)
     * @param maxColumns maxColumns required (-1 to load entire file)
     * @param cellMaxCharCount max no of chars allowed in a cell (-1 to load entire cell content)
     * @return Map of file data and max columns.
     * @throws Exception exception while reading tab separated file.
     */
    private Map<Integer, List<List<String>>> readTabSeparatedTextFile(final byte[] fileData, final int maxRows, final int maxColumns,
            final int cellMaxCharCount) throws Exception {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start readTabSeparatedTextFile.");
        }
        Map<Integer, List<List<String>>> fileContentMap = new HashMap<Integer, List<List<String>>>();
        List<List<String>> fileContentList = new ArrayList<List<String>>();

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(fileData);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(byteArrayInputStream, "UTF-8"));
        String row = "";
        int maxColumnsToDisplay = 0;
        int numberOfColumnsWithData = 0;
        int rowIndex = 0;
        while ((row = bufferedReader.readLine()) != null) {
            /* If exceed the max rows required, stop loading data in collection. */
            if (rowIndex++ == maxRows) {
                break;
            }
            String[] rowValues = row.split("\t");
            /* If exceed the max columns required, stop loading data in collection. */
            int numberOfColumns = (rowValues.length > maxColumns) ? maxColumns : rowValues.length;
            numberOfColumnsWithData = 0;
            List<String> rowDataList = new ArrayList<String>();
            boolean isRowContainsData = false;
            for (int i = 0; i < numberOfColumns; i++) {
                String cellValue = rowValues[i];
                if (cellValue != null && !"".equals(cellValue.trim())) {
                    isRowContainsData = true;
                    numberOfColumnsWithData++;
                    /* If there is a max char limit for cell, then truncate extra chars. */
                    if (cellMaxCharCount > 0 && cellValue.length() > cellMaxCharCount) {
                        cellValue = cellValue.substring(0, cellMaxCharCount);
                    }
                    rowDataList.add(cellValue.trim());
                } else {
                    rowDataList.add(cellValue);
                }
            }
            if (isRowContainsData) {
                fileContentList.add(rowDataList);
            }
            if (numberOfColumnsWithData > maxColumnsToDisplay) {
                maxColumnsToDisplay = numberOfColumnsWithData;
            }
        }
        fileContentMap.put(maxColumnsToDisplay, fileContentList);
        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish readTabSeparatedTextFile.");
        }
        return fileContentMap;
    }

    /**
     * This method sends the error details to support team via email.
     * @param errorXSLMailContents the error contents to send to support team.
     * @param locale the locale
     */
    private void sendErrorEmail(final String errorXSLMailContents, final Locale locale) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start sendErrorEmail.");
        }
        PropertyService propertyService = ServiceLocator.locateLocalOrRemote(PropertyService.class, locale);
        UserMessagingService userMessagingService = ServiceLocator.locateLocalOrRemote(UserMessagingService.class, locale);

        /* Get the To address for email. */
        String errorXSLtoAddress = propertyService.getProperty("ECC_SUPPORT_EMAIL", locale);

        /* From address for email. */
        String errorXSLfromAddress = "error@DocumentGeneratorService.com";
        /* Construct the subject for email. */
        String errorXSLEnv = propertyService.getProperty("ENVIRONMENT", locale);
        String errorXSLSubject = errorXSLEnv.toLowerCase() + " " + locale + " : " + "DocumentGenerator Service Error";

        /* Construct the mail setting object. */
        MailSettings mailSettings = new MailSettings();
        mailSettings.setFromAddress(errorXSLfromAddress);
        mailSettings.setToAddress(errorXSLtoAddress);
        mailSettings.setSubject(errorXSLSubject);
        mailSettings.setInlineInputType(InputType.TEXT);
        mailSettings.setLocale(locale);

        boolean emailSent = userMessagingService.sendEmail(errorXSLMailContents, mailSettings);
        if (emailSent) {
            LOG.info("Xslt Error email has been sent successfully..!");
        } else {
            LOG.error("Xslt Error email could not be sent.. :(");
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish sendErrorEmail.");
        }
    }
    
    /**
     * Custom URI resolver to allow fonts to be loaded from the classpath, rather than a physical file.
     * 
     * @author C0952298
     *
     */
    private static final class ClassPathURIResolver implements URIResolver {
        //Simple regex pattern to match custom font file URI in the form of 'file:///jboss/.../../../..ttf'
        private static final Pattern FONT_FILE_URI_PATTERN = Pattern.compile("^file:.*(/jboss/).*(ttf)$");
        @Override
        public Source resolve(String href, String base) throws TransformerException {
            // note we discard 'base' here.
            if (href != null && FONT_FILE_URI_PATTERN.matcher(href.toLowerCase()).matches()){
                String nameOnly = href; 
                // we just want the font name here without any path data:
                int lastPathSeparatorIndex = href.lastIndexOf('/');
                if (lastPathSeparatorIndex > -1) {
                    nameOnly = href.substring(lastPathSeparatorIndex);
                    LOG.info("loading font " + nameOnly + " from classpath instead of " + href);
                    return new StreamSource(Thread.currentThread().getContextClassLoader().getResourceAsStream(nameOnly));
                }
            }    
            return null;
        }
    }
}
