/**
 * 
 */
package com.electrocomponents.messagerelay.process.persist;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
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
 * @author e0161085
 *
 */
public class MessageSBSWriter extends MessageProcessor {

    /**
      * Log4J.
      */
    private static Log log = LogFactory.getLog(MessageSBSWriter.class);

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
        String text = "";
        String tmpLocale = null;

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

            EprocEinvoicingService us = ServiceLocator.locateLocalOrRemote(EprocEinvoicingService.class, new Locale(tmpLocale));
            if (log.isDebugEnabled()) {
                log.debug("updateDocumentStatus() : Got Locator");
            }

            EinvHeader einvHeader = us.getEinvHeaderEntityById(einvHeaderId);

            if (log.isDebugEnabled()) {
                log.debug("updateDocumentStatus() : setting  Status");
                log.debug("updateDocumentStatus() : Payload is " + payLoad);
            }
            if (payLoad != null && payLoad.equals("Asynchronous transaction completed")) {
                if (log.isDebugEnabled()) {
                    log.debug("updateDocumentStatus() : Payload is ok");
                }
                // Set the status to indicate the message has been transferred
                einvHeader.setStatus(EprocTransferStatus.DONE);
                text = "Done";
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
        documentDate = message.getConfigParameter("DocumentDate");
        documentType = message.getConfigParameter("DocumentType");
        customerIdentifier = message.getConfigParameter("CustomerIdentifier");
        accountNumber = message.getConfigParameter("AccountNumber");

        if (log.isDebugEnabled()) {
            log.debug("retrieveConfigurationParameters() : Locale = " + locale);
            log.debug("retrieveConfigurationParameters() : DocumentNbr = " + documentNbr);
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
        payLoad = message.getStringPayload(BusinessMessage.PAYLOAD_RESPONSE_MESSAGE);
        if (StringUtils.isNotBlank(payLoad)) {
            payLoad = payLoad.trim();
        }
        if (log.isDebugEnabled()) {
            log.debug("retrievePayLoad() : returning " + payLoad);
            log.debug("retrievePayLoad() : Finished");
        }
    }

}
