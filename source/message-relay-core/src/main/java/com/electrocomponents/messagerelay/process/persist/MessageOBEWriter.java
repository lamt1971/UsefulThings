package com.electrocomponents.messagerelay.process.persist;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.electrocomponents.messagerelay.process.MessageProcessor;
import com.electrocomponents.messagerelay.support.BusinessMessage;
import com.electrocomponents.model.domain.DateTime;
import com.electrocomponents.model.domain.EprocTransferStatus;
import com.electrocomponents.model.domain.Locale;
import com.electrocomponents.model.domain.eprocurement.EinvHeader;
import com.electrocomponents.model.domain.eprocurement.EinvHeaderIdTO;
import com.electrocomponents.service.core.client.ServiceLocator;
import com.electrocomponents.service.eprocurement.einvoicing.interfaces.EprocEinvoicingService;

/**
 * <pre>
 * Copyright (c) RS Components.
 * Created by IntelliJ IDEA.
 * User: UK161085
 * Date: 31-May-2006
 * Time: 10:46:54
 * ********************************************************************************************************
 * MessageOBEWriter
 * Process the response from the OBESender class.
 * There are two statuses that should be processed :-
 * ok    -  In which case set the document status on einv_header to "Transferred"
 * other - This should be an error message as to why the process was unable to send the document
 * <p/>
 * ********************************************************************************************************
 *
 * @author UK161085
 *         ********************************************************************************************************
 *         *             Change History                                                                           *
 *         ********************************************************************************************************
 *         * Number   * Who         * Date       * Description                                                    *
 *         ********************************************************************************************************
 *         * New      * UK161085    * 27/06/06  * New Class
 *         ********************************************************************************************************
 *         * N/A      * UK161085    * 05/09/06  * Changed status to wait ack went invoice sent.
 *         ********************************************************************************************************
 *         * 10.1     * UK161085    * 28/05/09  * Used services to update the data using services.
 *         ********************************************************************************************************
 *         * GEN-1228 * e0161085    * 26/08/10 * Tidied Code.
 *         ********************************************************************************************************
 *         * GEN-2894 * e0161085    * 30/06/11 * Pick up the locale from the message to select correct locator.
 *         ********************************************************************************************************
 *         </pre>
 */
public class MessageOBEWriter extends MessageProcessor {

    /**
    * Log4J.
    */
    private static Log log = LogFactory.getLog(MessageOBEWriter.class);

    // Message Header Parameters
    /**
     * Locale.
     */
    private String locale;
    /**
     * Document Nbr.
     */
    private String documentNbr;
    /**
     * Document Date.
     */
    private String documentDate;
    /**
     * Document Type.
     */
    private String documentType;
    /**
     * Customer Identifier.
     */
    private String customerIdentifier;
    /**
     * Account Number.
     */
    private String accountNumber;
    /**
     * OBE File Name.
     */
    private String OBEFileName;
    /**
     * Payload.
     */
    private String payLoad;

    /**
     * Process the message.
     * @param responseMsg The message.
     * @return The Business Message.
     */
    public BusinessMessage processMessage(final BusinessMessage responseMsg) {
        if (log.isDebugEnabled()) {
            log.debug("processMessage() : Started");
        }

        // retrieve the Configuration Parameters
        if (log.isDebugEnabled()) {
            log.debug("processMessage() : Reading Config parameters");
        }
        retrieveConfigurationParameters(responseMsg);

        // retrieve the Payload
        if (log.isDebugEnabled()) {
            log.debug("processMessage() : Reading payload");
        }
        retrievePayLoad(responseMsg);

        // Update the Document status
        if (log.isDebugEnabled()) {
            log.debug("processMessage() : Updating Status");
        }
        updateDocumentStatus();

        if (log.isDebugEnabled()) {
            log.debug("processMessage() : Finished");
        }
        return responseMsg;
    }

    /**
     * Update the document status on the einv Header table.
     * This should only be carried out from the internal side of the firewall.
     */
    public void updateDocumentStatus() {
        if (log.isDebugEnabled()) {
            log.debug("updateDocumentStatus() : Started");
        }

        // Convert the date into a date object
        Date docDate = formatDate(documentDate);
        String status;
        String text = "";
        String tmpLocale = null;

        if (payLoad.equals("OK")) {
            // Set the status to indicate the message has been transferred
            status = "wait ack";
        } else {
            status = "error";
        }
        if (log.isDebugEnabled()) {
            log.debug("updateDocumentStatus() : Calling Locator");
        }
        try {

            EinvHeaderIdTO einvHeaderId = new EinvHeaderIdTO();
            einvHeaderId.setDocumentNumber(documentNbr);
            einvHeaderId.setLocale(locale);
            einvHeaderId.setDocumentType(documentType);
            einvHeaderId.setDocumentDate(docDate);

            if (locale != null && locale.equalsIgnoreCase("FR")) {
                tmpLocale = "f1";
            } else {
                if (locale != null) {
                    tmpLocale = locale.toLowerCase();
                } else {
                    tmpLocale = "uk";
                }

            }

            // EprocEinvoicingService us = locator.locate(new Locale("uk"));
            EprocEinvoicingService us = ServiceLocator.locateLocalOrRemote(EprocEinvoicingService.class, new Locale(tmpLocale));
            if (log.isDebugEnabled()) {
                log.debug("updateDocumentStatus() : Got Locator");
            }

            EinvHeader einvHeader = us.getEinvHeaderEntityById(einvHeaderId);

            if (log.isDebugEnabled()) {
                log.debug("updateDocumentStatus() : setting  Status");
            }
            if (payLoad.equals("OK")) {
                if (log.isDebugEnabled()) {
                    log.debug("updateDocumentStatus() : Payload is ok");
                }
                // Set the status to indicate the message has been transferred
                einvHeader.setStatus(EprocTransferStatus.WAIT_ACKNOWLEDGE);
                text = "Sent";
                einvHeader.setStatusErrorText(text);
            } else {
                if (log.isDebugEnabled()) {
                    log.debug("updateDocumentStatus() : Payload is error");
                }
                einvHeader.setStatus(EprocTransferStatus.ERROR);
                text = payLoad;
                einvHeader.setStatusErrorText(text);
            }

            if (log.isDebugEnabled()) {
                log.debug("updateDocumentStatus() : About to save change");
            }
            einvHeader.setStatusChangedDate(new DateTime());
            us.updateEinvHeaderEntity(einvHeader, text);
            if (log.isDebugEnabled()) {
                log.debug("updateDocumentStatus() : change saved.");
            }
        } catch (Exception e) {

            if (log.isDebugEnabled()) {
                log.error("updateDocumentStatus() : Error " + e.getMessage());
            }
        }

        if (log.isDebugEnabled()) {
            log.debug("updateDocumentStatus() : Finished");
        }
    }

    /**
     * Reformat the date.
     * The JMS MEssage queue stores this as YYYY-MM-DD.
     * We need to reformat it to a Date object.
     *
     * @param dateToConvert Date to convert.
     * @return Date object containing the date.
     */

    public Date formatDate(final String dateToConvert) {
        if (log.isDebugEnabled()) {
            log.debug("formatDate() :Started");
        }

        Date docDate = null;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            docDate = df.parse(dateToConvert);
        } catch (ParseException e) {
            if (log.isDebugEnabled()) {
                log.error("formatDate() : Unable to format date " + e.getMessage());
            }
        }

        if (log.isDebugEnabled()) {
            log.debug("formatDate() :Finished");
        }
        return docDate;
    }

    /**
     * Retrieve the configuration parameters from the message.
     * These hold the key information required to update the status on
     * einv_header and to create a line in the audit table,
     * einv_transfer_audit
     * @param message The message.
     */
    private void retrieveConfigurationParameters(final BusinessMessage message) {
        if (log.isDebugEnabled()) {
            log.debug("retrieveConfigurationParameters() : Started");
        }

        locale = message.getConfigParameter("Locale");
        documentNbr = message.getConfigParameter("DocumentNbr");
        OBEFileName = message.getConfigParameter("OBEFileName");
        documentDate = message.getConfigParameter("DocumentDate");
        documentType = message.getConfigParameter("DocumentType");
        customerIdentifier = message.getConfigParameter("CustomerIdentifier");
        accountNumber = message.getConfigParameter("AccountNumber");

        if (log.isDebugEnabled()) {
            log.debug("retrieveConfigurationParameters() : Locale = " + locale);
            log.debug("retrieveConfigurationParameters() : DocumentNbr = " + documentNbr);
            log.debug("retrieveConfigurationParameters() : OBEFileName = " + OBEFileName);
            log.debug("retrieveConfigurationParameters() : documentDate = " + documentDate);
            log.debug("retrieveConfigurationParameters() : documentType = " + documentType);
            log.debug("retrieveConfigurationParameters() : customerIdentifier = " + customerIdentifier);
            log.debug("retrieveConfigurationParameters() : accountNumber = " + accountNumber);
            log.debug("retrieveConfigurationParameters() : Finished");
        }
    }

    /**
     * retrievePayLoad
     * Pull off the document text from the message.
     * This shoudl either be the word "OK", or an error message !
     *
     * @param message The message.
     */
    private void retrievePayLoad(final BusinessMessage message) {
        if (log.isDebugEnabled()) {
            log.debug("retrievePayLoad() : Started");
        }

        payLoad = message.getStringPayload("ResponseMessage");

        if (log.isDebugEnabled()) {
            log.debug("retrievePayLoad() : returning " + payLoad);
            log.debug("retrievePayLoad() : Finished");
        }
    }

}
