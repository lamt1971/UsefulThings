package com.electrocomponents.service.fileio.interfaces;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.electrocomponents.model.domain.Locale;
import com.electrocomponents.model.domain.order.FileType;
import com.electrocomponents.service.core.client.ServiceLocator;
import com.electrocomponents.service.core.usermessaging.support.ExcelEmailDTO;

import junit.framework.JUnit4TestAdapter;
import junit.framework.TestCase;

/**
 * <pre>
 * ************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author  : Ganesh Raut
 * Created : 13 Jan 2009 at 17:05:36
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
 * A test class to test the DocumentProcessorService.
 * @author Ganesh Raut
 */
public class DocumentProcessorServiceIT extends TestCase {

    /** Commons logging logger. */
    private static final Log LOG = LogFactory.getLog(DocumentProcessorServiceIT.class);

    /** Constant for the name of the application used to locate the service. */
    private static final String APPLICATION_NAME = "document-generation";

    /** Shape Permission name for Quotation portal. */
    private static final String QUOTE_PERMISSION_NAME = "Quotation Manager2";

    /** Excel test file name. */
    private static final String TEST_XLS_FILE_NAME =
            "src/test-integration/java/com/electrocomponents/service/fileio/interfaces/Enquiry_01.xls";

    /** Text test file name. */
    private static final String TEST_TEXT_FILE_NAME =
            "src/test-integration/java/com/electrocomponents/service/fileio/interfaces/Enquiry_01.txt";

    /**
     * @return test suite
     */
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(DocumentProcessorServiceIT.class);
    }

    /**
     * TTP: A test case to get the pdf file.
     */
    /*
     * public void testGetPdfAsByteArray() { Locale locale = new Locale("uk"); UserMessagingServiceHelper userMessagingServiceHelper = new
     * UserMessagingServiceHelper(); get the dummy Order object. Order order = constructDummyOrderObject(); convert the Order object to
     * OrderEmailObject. OrderEmailObject orderEmailObject = userMessagingServiceHelper.convertToOrderEmailObject(order);
     * DocumentProcessorService documentProcessorService = DocumentProcessorServiceLocator.getLocator().locate(locale); byte[]
     * pdfAsByteArray = documentProcessorService.getPdfAsByteArray(orderEmailObject, "WEB_ORDER_EMAIL_ATTACHMENT_XSLT", locale); File
     * pdfFile = null; try { pdfFile = new File("Order_" + new Date().getTime() + ".pdf"); FileOutputStream fileOutputStream = new
     * FileOutputStream(pdfFile); BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
     * bufferedOutputStream.write(pdfAsByteArray); bufferedOutputStream.close(); fileOutputStream.close(); if (pdfFile.exists()) {
     * LOG.info("Pdf file created successfully at - " + pdfFile.getAbsolutePath()); } else { LOG.error("Pdf file creation failed.. :("); } }
     * catch (Exception e) { e.printStackTrace(); } finally { if (pdfFile.exists()) { LOG.info("Deleting the test case created PDF file..");
     * pdfFile.delete(); } } }
     */

    /**
     * TTF: An invalid XSLT has been given to fail the test case.
     */
    /*
     * public void testGetPdfAsByteArray1() { Locale locale = new Locale("uk"); UserMessagingServiceHelper userMessagingServiceHelper = new
     * UserMessagingServiceHelper(); get the dummy Order object. Order order = constructDummyOrderObject(); convert the Order object to
     * OrderEmailObject. OrderEmailObject orderEmailObject = userMessagingServiceHelper.convertToOrderEmailObject(order);
     * DocumentProcessorService documentProcessorService = DocumentProcessorServiceLocator.getLocator().locate(locale); byte[]
     * pdfAsByteArray = documentProcessorService.getPdfAsByteArray(orderEmailObject, "WEB_ORDER_CONF_EMAIL_XSLT_TEST_DATA", locale); File
     * pdfFile = null; try { pdfFile = new File("Order_" + new Date().getTime() + ".pdf"); FileOutputStream fileOutputStream = new
     * FileOutputStream(pdfFile); BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream); if (pdfAsByteArray
     * != null) { bufferedOutputStream.write(pdfAsByteArray); } bufferedOutputStream.close(); fileOutputStream.close(); if
     * (pdfFile.exists()) { LOG.info("Pdf file created successfully at - " + pdfFile.getAbsolutePath()); } else {
     * LOG.error("Pdf file creation failed.. :("); } } catch (Exception e) { e.printStackTrace(); } finally { if (pdfFile.exists()) {
     * LOG.info("Deleting the test case created PDF file.."); pdfFile.delete(); } } }
     */

    /**
     * A test case to get the Excel file.
     */
    public void testGetExcelAsByteArray() {

        Locale locale = new Locale("uk");
        ExcelEmailDTO excelEmailDTO = constructExcelEmailDTO();
        DocumentProcessorService documentProcessorService = ServiceLocator.locateRemote(APPLICATION_NAME, DocumentProcessorService.class);
        byte[] excelAsByteArray = documentProcessorService.getExcelAsByteArray(excelEmailDTO, locale);
        File excelFile = null;
        try {
            excelFile = new File("Order_" + new Date().getTime() + ".xls");

            FileOutputStream fileOutputStream = new FileOutputStream(excelFile);
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
            bufferedOutputStream.write(excelAsByteArray);
            bufferedOutputStream.close();
            fileOutputStream.close();

            if (excelFile.exists()) {
                LOG.info("Excel file created successfully at - " + excelFile.getAbsolutePath());
            } else {
                LOG.error("Excel file creation failed.. :(");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (excelFile.exists()) {
                LOG.info("Deleting the test case created EXCEL file..");
                excelFile.delete();
            }
        }
    }

    /**
     * TTP: A test case to read XSL file data as list.
     */
    public void testGetFileDataAsList() {
        LOG.info("Method getFileDataAsList started.");

        Locale locale = new Locale("uk");
        try {
            /* read contents of an excel file. */
            File file = new File(TEST_XLS_FILE_NAME);
            byte[] fileData = getBytesFromFile(file);
            /* Call method to read file data. */
            DocumentProcessorService documentProcessorService = ServiceLocator.locateRemote(APPLICATION_NAME, DocumentProcessorService.class);
            Map<Integer, List<List<String>>> fileDataMap = documentProcessorService.getFileDataAsList(fileData, FileType.MS_EXCEL_2003);

            if (fileDataMap != null && fileDataMap.keySet().size() > 0) {
                for (Entry<Integer, List<List<String>>> fileDataMapEntry : fileDataMap.entrySet()) {
                    Integer maxColumnsToDisplay = (Integer) fileDataMapEntry.getKey();
                    List<List<String>> fileContentList = (List<List<String>>) fileDataMapEntry.getValue();
                    LOG.info("Max columns containing data in file: " + maxColumnsToDisplay);
                    LOG.info("File contents: " + fileContentList);
                }
            } else {
                LOG.error("Either the file is empty or there are errors reading the contents.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        LOG.info("Method getFileDataAsList finished.");
    }

    /**
     * TTP: A test case to read tab separated text file data as list.
     */
    public void testGetFileDataAsList1() {
        LOG.info("Method getFileDataAsList started.");

        Locale locale = new Locale("uk");
        try {
            /* read contents of an excel file. */
            File file = new File(TEST_TEXT_FILE_NAME);
            byte[] fileData = getBytesFromFile(file);
            /* Call method to read file data. */
            DocumentProcessorService documentProcessorService = ServiceLocator.locateRemote(APPLICATION_NAME,  DocumentProcessorService.class);
            Map<Integer, List<List<String>>> fileDataMap = documentProcessorService.getFileDataAsList(fileData, FileType.TEXT);

            if (fileDataMap != null && fileDataMap.keySet().size() > 0) {
                for (Entry<Integer, List<List<String>>> fileDataMapEntry : fileDataMap.entrySet()) {
                    Integer maxColumnsToDisplay = (Integer) fileDataMapEntry.getKey();
                    List<List<String>> fileContentList = (List<List<String>>) fileDataMapEntry.getValue();
                    LOG.info("Max columns containing data in file: " + maxColumnsToDisplay);
                    LOG.info("File contents: " + fileContentList);
                }
            } else {
                LOG.error("Either the file is empty or there are errors reading the contents.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        LOG.info("Method getFileDataAsList finished.");
    }

    /**
     * A test case to read file data as list with limited rows and cols.
     */
    public void testGetFileDataAsListWithLimitedRowsAndCols() {
        LOG.info("Method getFileDataAsListWithLimitedRowsAndCols started.");
        /* Max no of rows and cols to read from the file. */
        int maxRows = 10;
        int maxColumns = 10;
        Locale locale = new Locale("uk");
        try {
            /* read contents of an excel file. */
            File file = new File(TEST_XLS_FILE_NAME);
            byte[] fileData = getBytesFromFile(file);

            /* Call method to read file data. */
            DocumentProcessorService documentProcessorService = ServiceLocator.locateRemote(APPLICATION_NAME, DocumentProcessorService.class);
            Map<Integer, List<List<String>>> fileDataMap = documentProcessorService.getFileDataAsList(fileData, FileType.MS_EXCEL_2003,
                    maxRows, maxColumns);

            if (fileDataMap != null && fileDataMap.keySet().size() > 0) {
                for (Entry<Integer, List<List<String>>> fileDataMapEntry : fileDataMap.entrySet()) {
                    Integer maxColumnsToDisplay = (Integer) fileDataMapEntry.getKey();
                    List<List<String>> fileContentList = (List<List<String>>) fileDataMapEntry.getValue();
                    LOG.info("Max columns containing data in file: " + maxColumnsToDisplay);
                    LOG.info("File contents: " + fileContentList);
                }
            } else {
                LOG.error("Either the file is empty or there are errors reading the contents.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        LOG.info("Method getFileDataAsListWithLimitedRowsAndCols finished.");
    }

    /**
     * A test case to read file data as list with limited rows and cols and maximum cell char limit.
     */
    public void testGetFileDataAsListWithMaxRowsColsAndCellMaxChar() {
        LOG.info("Method getFileDataAsListWithLimitedRowsAndCols started.");
        /* Max no of rows and cols to read from the file. */
        int maxRows = 10;
        int maxColumns = 5;
        /* Max no of chars to load in a cell. */
        int cellMaxCharCount = 10;
        Locale locale = new Locale("uk");
        try {
            /* read contents of an excel file. */
            File file = new File(TEST_XLS_FILE_NAME);
            byte[] fileData = getBytesFromFile(file);

            /* Call method to read file data. */
            DocumentProcessorService documentProcessorService = ServiceLocator.locateRemote(APPLICATION_NAME, DocumentProcessorService.class);
            Map<Integer, List<List<String>>> fileDataMap = documentProcessorService.getFileDataAsList(fileData, FileType.MS_EXCEL_2003,
                    maxRows, maxColumns, cellMaxCharCount);

            if (fileDataMap != null && fileDataMap.keySet().size() > 0) {
                for (Entry<Integer, List<List<String>>> fileDataMapEntry : fileDataMap.entrySet()) {
                    Integer maxColumnsToDisplay = (Integer) fileDataMapEntry.getKey();
                    List<List<String>> fileContentList = (List<List<String>>) fileDataMapEntry.getValue();
                    LOG.info("Max columns containing data in file: " + maxColumnsToDisplay);
                    LOG.info("File contents: " + fileContentList);
                }
            } else {
                LOG.error("Either the file is empty or there are errors reading the contents.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        LOG.info("Method getFileDataAsListWithLimitedRowsAndCols finished.");
    }

    /**
     * A method to download the excel quotation.
     */
    /*
     * public void testGenerateQuotationReport() { Locale locale = new Locale("uk"); DocumentProcessorService documentProcessorService =
     * DocumentProcessorServiceLocator.getLocator().locate(locale); QuoteManagerService quoteManagerService =
     * QuoteManagerServiceLocator.getLocator().locate(locale); QuoteCoreService quoteCoreService =
     * QuoteCoreServiceLocator.getLocator().locate(locale); Long userId = 253L; Existing userId for which need to fetch quotation list.
     * ShapePermissionService shapePermissionService = ShapePermissionServiceLocator.getLocator().locate(); ShapePermission permission =
     * shapePermissionService.getPermissionByName(QUOTE_PERMISSION_NAME); Assert.assertNotNull(permission); ShapeUserGroupLocaleService
     * shapeUserGroupLocaleService = ShapeUserGroupLocaleServiceLocator.getLocator().locate(); List<Locale> userGroupLocales =
     * shapeUserGroupLocaleService.getUserGroupLocale(userId, permission.getPermissionId()); Assert.assertNotNull(userGroupLocales);
     * List<EcQuote> issuedQuoteList = quoteManagerService.getQuotationListByStatusForUser(QuoteStatus.QUOTE_ISSUED, userId, 5,
     * QuoteRecordType.QUOTE, userGroupLocales); Map<ExcelEmailDTO, ExcelEmailDTO> excelEmailDtoMap = new HashMap<ExcelEmailDTO,
     * ExcelEmailDTO>(); for (EcQuote ecQuote : issuedQuoteList) { EcQuote ecQuoteWithQuoteLines =
     * quoteCoreService.getQuoteById(ecQuote.getQuoteId(), QuoteConfigHelper.getConfigForFullQuote(), EcSystemId.WEB); ExcelEmailDTO
     * headerExcelEmailDTO = getQuoteHeaderDTOForExcelReport(ecQuoteWithQuoteLines); ExcelEmailDTO childExcelEmailDTO =
     * getExcelEmailDTOForExcelReport(ecQuoteWithQuoteLines); excelEmailDtoMap.put(headerExcelEmailDTO, childExcelEmailDTO); } byte[]
     * quotationExcelReportByteArray = documentProcessorService.getExcelAsByteArray(excelEmailDtoMap, locale); try { File pdfFile = new
     * File("D:\\QuotationExcelReport_" + new Date().getTime() + ".xls"); FileOutputStream fileOutputStream = new FileOutputStream(pdfFile);
     * BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
     * bufferedOutputStream.write(quotationExcelReportByteArray); bufferedOutputStream.close(); fileOutputStream.close(); if
     * (pdfFile.exists()) { LOG.info("Excel file created successfully at - " + pdfFile.getAbsolutePath()); } else {
     * LOG.error("Excel file creation failed.. :("); } } catch (Exception e) { e.printStackTrace(); } }
     */

    /**
     * Method to get Quotation data as ExcelEmailDTO.
     * @param ecQuoteForExcelEmailDTO the ecQuote to create ExcelEmailDTO
     * @return DTO Object.
     */
    /*
     * private ExcelEmailDTO getQuoteHeaderDTOForExcelReport(final EcQuote ecQuoteForExcelEmailDTO) { if (LOG.isDebugEnabled()) {
     * LOG.debug("Method Started"); } ExcelEmailDTO excelEmailDTO = new ExcelEmailDTO(); if (ecQuoteForExcelEmailDTO != null) { Map<String,
     * Integer> headerMap = new LinkedHashMap<String, Integer>(); headerMap.put("Quote Ref", 10); headerMap.put("Sold To", 15);
     * headerMap.put("Ship To", 10); headerMap.put("Issue Time", 15); headerMap.put("Goods Value Total", 20); headerMap.put("Margin Total",
     * 15); headerMap.put("Margin (%)", 13); headerMap.put("Order value Discount", 22); headerMap.put("Single Order Discount (%)", 24);
     * headerMap.put("Vat", 10); headerMap.put("Quote Total", 15); excelEmailDTO.setHeaderMap(headerMap); List<List<String>> elementList =
     * new ArrayList<List<String>>(); List<String> childList = new ArrayList<String>(); if (ecQuoteForExcelEmailDTO.getQuoteRef() != null) {
     * childList.add(ecQuoteForExcelEmailDTO.getQuoteRef()); } else { childList.add(" "); } if (ecQuoteForExcelEmailDTO.getSoldTo() != null)
     * { childList.add(ecQuoteForExcelEmailDTO.getSoldTo()); } else { childList.add(" "); } if (ecQuoteForExcelEmailDTO.getShipTo() != null)
     * { childList.add(ecQuoteForExcelEmailDTO.getShipTo()); } else { childList.add(" "); } if (ecQuoteForExcelEmailDTO.getQuoteIssueTime()
     * != null) { childList.add(ecQuoteForExcelEmailDTO.getQuoteIssueTime().getDateTimeString()); } else { childList.add(" "); } if
     * (ecQuoteForExcelEmailDTO.getQuoteGoodsValueTotal() != null) {
     * childList.add(ecQuoteForExcelEmailDTO.getQuoteGoodsValueTotal().toString()); } else { childList.add(" "); } if
     * (ecQuoteForExcelEmailDTO.getQuoteMarginTotal() != null) { childList.add(ecQuoteForExcelEmailDTO.getQuoteMarginTotal().toString()); }
     * else { childList.add(" "); } if (ecQuoteForExcelEmailDTO.getQuoteMarginPercentage() != null) {
     * childList.add(ecQuoteForExcelEmailDTO.getQuoteMarginPercentage().toString()); } else { childList.add(" "); } if
     * (ecQuoteForExcelEmailDTO.getOrderValueDiscount() != null) {
     * childList.add(ecQuoteForExcelEmailDTO.getOrderValueDiscount().toString()); } else { childList.add(" "); } if
     * (ecQuoteForExcelEmailDTO.getSingleOrderDiscount() != null) {
     * childList.add(ecQuoteForExcelEmailDTO.getSingleOrderDiscount().toString()); } else { childList.add(" "); } if
     * (ecQuoteForExcelEmailDTO.getQuotationVatTotal() != null) { childList.add(ecQuoteForExcelEmailDTO.getQuotationVatTotal().toString());
     * } else { childList.add(" "); } if (ecQuoteForExcelEmailDTO.getQuoteValueTotal() != null) {
     * childList.add(ecQuoteForExcelEmailDTO.getQuoteValueTotal().toString()); } else { childList.add(" "); } elementList.add(childList);
     * excelEmailDTO.setElementList(elementList); } if (LOG.isDebugEnabled()) { LOG.debug("Method Finished"); } return excelEmailDTO; }
     */

    /**
     * Method to get Quotation data as ExcelEmailDTO.
     * @param ecQuoteForExcelEmailDTO the ecQuote to create ExcelEmailDTO
     * @return DTO Object.
     */

    /*private ExcelEmailDTO getExcelEmailDTOForExcelReport(final EcQuote ecQuoteForExcelEmailDTO) {
        ExcelEmailDTO excelEmailDTO = new ExcelEmailDTO();
        if (LOG.isDebugEnabled()) {
            LOG.debug("Method getExcelEmailDto Started");
        }
        if (ecQuoteForExcelEmailDTO != null) {
            Map<String, Integer> headerMap = new LinkedHashMap<String, Integer>();
            headerMap.put("Item", 10);
            headerMap.put("CPN", 15);
            headerMap.put("Req'd Qty", 10);
            headerMap.put("Off'd Qty", 15);
            headerMap.put("OM", 20);
            headerMap.put("Price Each", 15);
            headerMap.put("Line Value", 13);
            headerMap.put("RS Aricle", 22);
            headerMap.put("Standard Availability", 24);
            headerMap.put("RoHS", 10);
            headerMap.put("NCNR", 15);
            headerMap.put("MPN", 20);
            headerMap.put("Manufacturer", 15);
            headerMap.put("Description", 15);
            headerMap.put("Pack Price", 12);
            headerMap.put("Price Unit", 10);
            headerMap.put("OM Pack Type", 15);
            headerMap.put("Supplier Delivery", 18);
            headerMap.put("Std Delivery", 18);
            excelEmailDTO.setHeaderMap(headerMap);
            excelEmailDTO.setElementList(getChildElementList(ecQuoteForExcelEmailDTO));
        } else {
            LOG.error("Quote is null");
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("Method Finished.");
        }
        return excelEmailDTO;
    }*/

    /**
     * @param ecQuoteForExcelEmailDTO ecQuoteForExcelEmailDTO
     * @return elementList
     */
    /*
     * private List<List<String>> getChildElementList(final EcQuote ecQuoteForExcelEmailDTO) { List<List<String>> elementList = new
     * ArrayList<List<String>>(); List<EcQuoteLine> quotationLineDetailList = ecQuoteForExcelEmailDTO.getEcQuoteLines();
     * Iterator<EcQuoteLine> iterator = quotationLineDetailList.iterator(); while (iterator.hasNext()) { EcQuoteLine ecQuoteLine =
     * (EcQuoteLine) iterator.next(); List<String> childList = new ArrayList<String>(); Col-1 : Item number.
     * childList.add(String.valueOf(ecQuoteLine.getLineNumber())); Col-2 : Customer Part number.
     * childList.add(ecQuoteLine.getCustomerLineRef()); Col-3 : Required Quantity. if (ecQuoteLine.getWorkingQuantity() != null) {
     * childList.add(ecQuoteLine.getWorkingQuantity().toString()); } else { childList.add(" "); } Col-4 : Offered Quantity. if
     * (ecQuoteLine.getOfferQuantity() != null) { childList.add(ecQuoteLine.getOfferQuantity().toString()); } else { childList.add(" "); }
     * Col-5 : Order multiple: Pack size. if (ecQuoteLine.getBidStatus() != null && BidStatus.BID.equals(ecQuoteLine.getBidStatus()) &&
     * ecQuoteLine.getEcArticleMatch() != null && ecQuoteLine.getEcArticleMatch().getEcRSArticle() != null) {
     * childList.add(String.valueOf(ecQuoteLine.getEcArticleMatch().getEcRSArticle().getPackSize())); } else if (ecQuoteLine.getBidStatus()
     * != null && BidStatus.BID.equals(ecQuoteLine.getBidStatus()) && ecQuoteLine.getEcArticleMatch() != null &&
     * ecQuoteLine.getEcArticleMatch().getEcRSArticle() == null && ecQuoteLine.getEcArticleMatch().getEcExtMatchArticle() != null) {
     * childList.add(String.valueOf(ecQuoteLine.getEcArticleMatch().getEcExtMatchArticle().getPackSize())); } else { childList.add(" "); }
     * Col-6 : Price Each. if (ecQuoteLine.getBidStatus() != null && BidStatus.BID.equals(ecQuoteLine.getBidStatus()) &&
     * ecQuoteLine.getEcArticleMatch() != null && ecQuoteLine.getEcArticleMatch().getEcRSArticle() != null) { if
     * (ecQuoteLine.getOfferPrice() != null) { if (ecQuoteLine.getEcArticleMatch().getEcRSArticle().isUnitPriced()) {
     * childList.add(ecQuoteLine.getOfferPrice().toString()); } else { childList.add(String.valueOf(ecQuoteLine.getOfferPrice().getAmount()
     * / ecQuoteLine.getEcArticleMatch().getEcRSArticle().getPackSize())); } } else { childList.add(" "); } } else if
     * (ecQuoteLine.getBidStatus() != null && BidStatus.BID.equals(ecQuoteLine.getBidStatus()) && ecQuoteLine.getEcArticleMatch() != null &&
     * ecQuoteLine.getEcArticleMatch().getEcRSArticle() == null) { if (ecQuoteLine.getOfferPrice() != null) {
     * childList.add(ecQuoteLine.getOfferPrice().toString()); } else { childList.add(" "); } } else { childList.add(" "); } Col-7 : Line
     * Value as Offer price * Offered Quantity. if (ecQuoteLine.getLineSellPriceAfterCustomerDiscount() != null) {
     * childList.add(ecQuoteLine.getLineSellPriceAfterCustomerDiscount().toString()); } else { childList.add(" "); } Col-8 : RS Article
     * number. if (ecQuoteLine.getBidStatus() != null && BidStatus.BID.equals(ecQuoteLine.getBidStatus()) && ecQuoteLine.getEcArticleMatch()
     * != null && ecQuoteLine.getEcArticleMatch().getEcRSArticle() != null) {
     * childList.add(ecQuoteLine.getEcArticleMatch().getEcRSArticle().getProductNumber().formatProductNumberToDisplay()); } else if
     * (ecQuoteLine.getBidStatus() != null && BidStatus.BID.equals(ecQuoteLine.getBidStatus()) && ecQuoteLine.getEcArticleMatch() != null &&
     * ecQuoteLine.getEcArticleMatch().getEcRSArticle() == null) { childList.add("On Demand"); } else { childList.add("No Bid"); } // TODO:
     * AvailabilityText is no more in the EcQuoteLine Col-9 : RS Delivery: Availability text. if (ecQuoteLine.getAvailability() != null) {
     * childList.add(ecQuoteLine.getAvailability()); } else { childList.add(" "); } Col-10: ROHS flag for selected product in quote line. if
     * (ecQuoteLine.getBidStatus() != null && BidStatus.BID.equals(ecQuoteLine.getBidStatus()) && ecQuoteLine.getEcArticleMatch() != null &&
     * ecQuoteLine.getEcArticleMatch().getEcRSArticle() != null && ecQuoteLine.getEcArticleMatch().getEcRSArticle().getRohsStatus() != null)
     * { if (RohsStatus.COMPLIANT.equals(ecQuoteLine.getEcArticleMatch().getEcRSArticle().getRohsStatus())) { childList.add("Yes"); } } else
     * if (ecQuoteLine.getBidStatus() != null && BidStatus.BID.equals(ecQuoteLine.getBidStatus()) && ecQuoteLine.getEcArticleMatch() != null
     * && ecQuoteLine.getEcArticleMatch().getEcAdhocArticle() != null && ecQuoteLine.getEcArticleMatch().getEcAdhocArticle().getRohsStatus()
     * != null) { if (RohsStatus.COMPLIANT.equals(ecQuoteLine.getEcArticleMatch().getEcAdhocArticle().getRohsStatus())) {
     * childList.add("Yes"); } else { childList.add(" "); } } else if (ecQuoteLine.getBidStatus() != null &&
     * BidStatus.BID.equals(ecQuoteLine.getBidStatus()) && ecQuoteLine.getEcArticleMatch() != null &&
     * ecQuoteLine.getEcArticleMatch().getEcExtMatchArticle() != null &&
     * ecQuoteLine.getEcArticleMatch().getEcExtMatchArticle().getRohsStatus() != null) { if
     * (RohsStatus.COMPLIANT.equals(ecQuoteLine.getEcArticleMatch().getEcExtMatchArticle().getRohsStatus())) { childList.add("Yes"); } else
     * { childList.add(" "); } } else if (ecQuoteLine.getBidStatus() != null && BidStatus.BID.equals(ecQuoteLine.getBidStatus()) &&
     * ecQuoteLine.getEcArticleMatch() != null && ecQuoteLine.getEcArticleMatch().getCompetitorArticle() != null &&
     * ecQuoteLine.getEcArticleMatch().getCompetitorArticle().getRohsStatus() != null) { if
     * (RohsStatus.COMPLIANT.equals(ecQuoteLine.getEcArticleMatch().getCompetitorArticle().getRohsStatus())) { childList.add("Yes"); } else
     * { childList.add(" "); } } else { childList.add(" "); } Col-11 : NCNR flag, Non cancellable non refundable: false for RS Products,
     * true otherwise. if (ecQuoteLine.getBidStatus() != null && BidStatus.BID.equals(ecQuoteLine.getBidStatus())) { if
     * (ecQuoteLine.getEcArticleMatch() != null && ecQuoteLine.getEcArticleMatch().getEcRSArticle() != null) { childList.add(" "); } else {
     * childList.add("Yes"); } } else { childList.add(" "); } Col-12 : Manufacturer part number of the selected product in the quote line.
     * Set manufacturer partNumber based on type of product selected in QuoteLine. String manufacturerPartNumber = null; if
     * (ecQuoteLine.getEcArticleMatch() != null && ecQuoteLine.getEcArticleMatch().getEcRSArticle() != null) { manufacturerPartNumber =
     * ecQuoteLine.getEcArticleMatch().getEcRSArticle().getManufacturerPartNumber(); } else if (ecQuoteLine.getEcArticleMatch() != null &&
     * ecQuoteLine.getEcArticleMatch().getEcExtMatchArticle() != null) { manufacturerPartNumber =
     * ecQuoteLine.getEcArticleMatch().getEcExtMatchArticle().getManufacturerPartNumber(); } else if (ecQuoteLine.getEcArticleMatch() !=
     * null && ecQuoteLine.getEcArticleMatch().getEcAdhocArticle() != null) { manufacturerPartNumber =
     * ecQuoteLine.getEcArticleMatch().getEcAdhocArticle().getManufacturerPartNbr(); } else if (ecQuoteLine.getEcArticleMatch() != null &&
     * ecQuoteLine.getEcArticleMatch().getCompetitorArticle() != null) { manufacturerPartNumber =
     * ecQuoteLine.getEcArticleMatch().getCompetitorArticle().getManufacturerPartNbr(); } childList.add(manufacturerPartNumber); Col-13 :
     * manufacturer name. Set manufacturer name based on type of product selected in QuoteLine. String manufacturerName = " "; if
     * (ecQuoteLine.getEcArticleMatch() != null && ecQuoteLine.getEcArticleMatch().getEcRSArticle() != null) { manufacturerName =
     * ecQuoteLine.getEcArticleMatch().getEcRSArticle().getManufacturerName(); } else if (ecQuoteLine.getEcArticleMatch() != null &&
     * ecQuoteLine.getEcArticleMatch().getEcExtMatchArticle() != null) { manufacturerName =
     * ecQuoteLine.getEcArticleMatch().getEcExtMatchArticle().getManufacturerName(); } else if (ecQuoteLine.getEcArticleMatch() != null &&
     * ecQuoteLine.getEcArticleMatch().getEcAdhocArticle() != null) { manufacturerName =
     * ecQuoteLine.getEcArticleMatch().getEcAdhocArticle().getManufacturerName(); } else if (ecQuoteLine.getEcArticleMatch() != null &&
     * ecQuoteLine.getEcArticleMatch().getCompetitorArticle() != null) { manufacturerName =
     * ecQuoteLine.getEcArticleMatch().getCompetitorArticle().getManufacturerName(); } childList.add(manufacturerName); Col-14 :
     * Description. Set description based on type of product selected in QuoteLine. String description = " "; if
     * (ecQuoteLine.getEcArticleMatch() != null && ecQuoteLine.getEcArticleMatch().getEcRSArticle() != null) { description =
     * ecQuoteLine.getEcArticleMatch().getEcRSArticle().getLongDescription(); } else if (ecQuoteLine.getEcArticleMatch() != null &&
     * ecQuoteLine.getEcArticleMatch().getEcExtMatchArticle() != null) { description =
     * ecQuoteLine.getEcArticleMatch().getEcExtMatchArticle().getDescription(); } else if (ecQuoteLine.getEcArticleMatch() != null &&
     * ecQuoteLine.getEcArticleMatch().getEcAdhocArticle() != null) { description =
     * ecQuoteLine.getEcArticleMatch().getEcAdhocArticle().getDescription(); } else if (ecQuoteLine.getEcArticleMatch() != null &&
     * ecQuoteLine.getEcArticleMatch().getCompetitorArticle() != null) { description =
     * ecQuoteLine.getEcArticleMatch().getCompetitorArticle().getDescription(); } childList.add(description); Col-15 : Offer price, Pack
     * Price. if (ecQuoteLine.getOfferPrice() != null) { childList.add(ecQuoteLine.getOfferPrice().toString()); } else { childList.add(" ");
     * } Col-16 : Price Unit Each/per pack. if (ecQuoteLine.getBidStatus() != null && BidStatus.BID.equals(ecQuoteLine.getBidStatus()) &&
     * ecQuoteLine.getEcArticleMatch() != null && ecQuoteLine.getEcArticleMatch().getEcRSArticle() != null) { if
     * (ecQuoteLine.getEcArticleMatch().getEcRSArticle().isUnitPriced()) { childList.add("Each"); } else { childList.add("per Pack"); } }
     * else if (ecQuoteLine.getBidStatus() != null && BidStatus.BID.equals(ecQuoteLine.getBidStatus()) && ecQuoteLine.getEcArticleMatch() !=
     * null && ecQuoteLine.getEcArticleMatch().getEcRSArticle() == null) { childList.add("Each"); } else { childList.add(" "); } Col-17 :
     * Unit display as Om Pack type. if (ecQuoteLine.getUnitDisplay() != null) { childList.add(ecQuoteLine.getUnitDisplay()); } else {
     * childList.add(" "); } // TODO: verify AvailabilityText is no more in the EcQuoteLine Col-18 : Supplier Delivery: Availability text.
     * if (ecQuoteLine.getAvailability() != null) { childList.add("Supplier"); } else { childList.add(" "); } // TODO: verify
     * AvailabilityText is no more in the EcQuoteLine Col-19 : Standard Delivery: Availability text. if (ecQuoteLine.getAvailability() !=
     * null) { childList.add("Standard"); } else { childList.add(" "); } elementList.add(childList); } return elementList; }
     */

    /**
     * Construts a dummy ExcelEmailDTO object for test cases.
     * @return ExcelEmailDTO object.
     */
    private ExcelEmailDTO constructExcelEmailDTO() {
        ExcelEmailDTO excelEmailDTO = new ExcelEmailDTO();

        Map<String, Integer> headerMap = new LinkedHashMap<String, Integer>();
        headerMap.put("Item Number", 15);
        headerMap.put("RS Part Number", 18);
        headerMap.put("Quantity", 10);
        headerMap.put("Customer Part Number", 23);
        headerMap.put("Manufacturer Name", 20);
        headerMap.put("Manufacturer Part Number", 25);
        headerMap.put("Line Price", 15);
        excelEmailDTO.setHeaderMap(headerMap);

        List<List<String>> elementList = new ArrayList<List<String>>();

        List<String> childList1 = new ArrayList<String>();
        childList1.add("1");
        childList1.add("0585242");
        childList1.add("1");
        childList1.add("RS-444444");
        childList1.add("Schneider - Partner Retail");
        childList1.add("SFH485");
        childList1.add("2.9");
        elementList.add(childList1);

        excelEmailDTO.setElementList(elementList);
        return excelEmailDTO;
    }

    /**
     * Construts a dummy Order object for test cases.
     * @return order object.
     */
    /*
     * private Order constructDummyOrderObject() { Locale locale = new Locale("uk"); ProductService productService =
     * ProductServiceLocator.getInstance().locate(locale); assertNotNull(productService); Order order = new Order();
     * order.setLocale(locale); order.setCreationTime(new DateTime()); order.setOrderDate(new DateTime()); Supplier Address. Plant plant =
     * new EcPlantEntity(); plant.setCompanyName("Rs Components"); plant.setAddressLine1("P.O. Box 99");
     * plant.setAddressLine2("Weldon Road"); plant.setAddressLine3("Corby"); plant.setAddressLine4("Northants");
     * plant.setAddressPostCode("NN17 9RS"); order.setPlant(plant); Delivery Address. AddressDTO customerAddress = new AddressDTO();
     * customerAddress.setCompanyName1("My Company"); customerAddress.setLine1("8"); customerAddress.setLine2("Thoresby Court");
     * customerAddress.setLine3("Corby"); customerAddress.setLine4("Northamptionshire"); customerAddress.setPostCode("NN18 0EL");
     * customerAddress.setCountry("United Kingdom"); order.setDeliveryAddress(customerAddress); Populating the Stocked Product Details.
     * List<OrderLineItem> orderLineItemList = new ArrayList<OrderLineItem>(); OrderLineItem orderLineItem1 = new OrderLineItem();
     * orderLineItem1.setProduct(productService.getProduct(new ProductNumber("444444"), locale)); orderLineItem1.setQuantity(new
     * Quantity(5)); orderLineItem1.setCustomerPartNumber("RS-444444"); orderLineItem1.setLineNumber(1); orderLineItem1.setPrices(new
     * LinePrices()); orderLineItem1.setLinePrice(new Money(new BigDecimal("20"))); OrderLineItem orderLineItem2 = new OrderLineItem();
     * orderLineItem2.setProduct(productService.getProduct(new ProductNumber("222222"), locale)); orderLineItem2.setQuantity(new
     * Quantity(6)); orderLineItem2.setCustomerPartNumber("RS-222222"); orderLineItem2.setLineNumber(2); orderLineItem2.setPrices(new
     * LinePrices()); orderLineItem2.setLinePrice(new Money(new BigDecimal("24.77"))); OrderLineItem orderLineItem3 = new OrderLineItem();
     * orderLineItem3.setProduct(productService.getProduct(new ProductNumber("2508747814"), locale)); orderLineItem3.setQuantity(new
     * Quantity(6)); orderLineItem3.setLineNumber(3); orderLineItem3.setPrices(new LinePrices()); orderLineItem3.setLinePrice(new Money(new
     * BigDecimal("24.77"))); OrderLineItem orderLineItem4 = new OrderLineItem(); orderLineItem4.setProduct(productService.getProduct(new
     * ProductNumber("4812087"), locale)); orderLineItem4.setQuantity(new Quantity(7)); orderLineItem4.setCustomerPartNumber("RS-4812087");
     * orderLineItem4.setLineNumber(4); orderLineItem4.setPrices(new LinePrices()); orderLineItem4.setLinePrice(new Money(new
     * BigDecimal("32.50"))); orderLineItemList.add(orderLineItem1); orderLineItemList.add(orderLineItem2);
     * orderLineItemList.add(orderLineItem3); orderLineItemList.add(orderLineItem4); order.setOrderLineItems(orderLineItemList); Setting Up
     * The Totals... order.setStockedGoodsTotal(new Money(new BigDecimal("20"))); EcDeliveryChargeEntity ecDeliveryChargeEntity = new
     * EcDeliveryChargeEntity(); ecDeliveryChargeEntity.setDeliveryCharge(new Money(new BigDecimal("5")));
     * ecDeliveryChargeEntity.setDespatchTypeKey("Next day for stocked products only");
     * order.setStockedDeliveryCharge(ecDeliveryChargeEntity); order.setStockedVatTotal(new Money(new BigDecimal("5")));
     * order.setStockedOrderTotal(new Money(new BigDecimal("30"))); order.setNonStockedGoodsTotal(new Money(new BigDecimal("20")));
     * EcDeliveryChargeEntity ecDeliveryCharge = new EcDeliveryChargeEntity(); ecDeliveryCharge.setDeliveryCharge(new Money(new
     * BigDecimal("5"))); order.setNonStockedDeliveryCharge(ecDeliveryCharge); order.setNonStockedVatTotal(new Money(new BigDecimal("5")));
     * order.setNonStockedOrderTotal(new Money(new BigDecimal("30"))); order.setOrderTotal(new Money(new BigDecimal("85.50")));
     * order.setStockedOrderReference("102167884"); order.setNonStockedOrderReference("102167884ABC"); User user = new
     * ItcUserProfileEntity(); user.setTitle(1); user.setFirstName("Ganesh"); user.setLastName("Raut");
     * user.setEmailAddress("Ganesh.Rault@rs-components.com"); user.setTelephoneNumber("01536 405494"); user.setEbsContact("72002629");
     * order.setUser(user); order.setPaymentCustomerOrderReference("Amex Ref RHolly"); order.setUserOrderName("My Order At 19:12:08-15:50");
     * order.setStockedCustomerOrderReference("19:12:2008-12:50"); order.setHeaderMediaCode("ABCDEFG");
     * order.setShipToCustomerAccountCode("500500500"); order.setOrderStockType(OrderStockType.MIXED_STOCK_ORDER);
     * order.setContactNbr("1234567"); return order; }
     */

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

        /*
         * You cannot create an array using a long type. It needs to be an int type. Before converting to an int type, check to ensure that
         * file is not loarger than Integer.MAX_VALUE;
         */
        if (length > Integer.MAX_VALUE) {
            return null;
        }
        /* Create the byte array to hold the data */
        byte[] bytes = new byte[(int) length];
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
        is.close();
        return bytes;
    }

}
