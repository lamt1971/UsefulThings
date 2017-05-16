package com.electrocomponents.messagerelay.process.persist;

import java.io.ByteArrayInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;

import com.electrocomponents.commons.components.general.xml.NoOpEntityResolver;
import com.electrocomponents.commons.exception.ElectroException;
import com.electrocomponents.messagerelay.process.MessageProcessor;
import com.electrocomponents.messagerelay.support.BusinessMessage;
import com.electrocomponents.model.domain.DateTime;
import com.electrocomponents.model.domain.notifications.EcomNotificationDetails;
import com.electrocomponents.model.domain.notifications.EcomNotificationSettings;
import com.electrocomponents.model.domain.notifications.enumeration.NotificationMessageStatusEnum;
import com.electrocomponents.model.domain.notifications.enumeration.NotificationMessageTypeEnum;
import com.electrocomponents.model.domain.notifications.enumeration.NotificationResponseTypeEnum;
import com.electrocomponents.service.core.client.ServiceLocator;
import com.electrocomponents.service.notifications.interfaces.EcomNotificationDetailsService;
import com.electrocomponents.service.notifications.interfaces.EcomNotificationSettingsService;
import com.electrocomponents.service.notifications.support.NotificationFailureEmailSenderUtility;

/**
 * @author C0951407
 *
 */
public class EcomNotificationResponseProcessor extends MessageProcessor {

    /** Logger. */
    private static final Log LOG = LogFactory.getLog(EcomNotificationResponseProcessor.class);

    /** Notification Id String Constant. */
    public static final String NOTIFICATION_ID = "notificationId";

    /** Ecom Config Id String Constant. */
    public static final String ECOM_CONFIG_ID = "ecomConfigId";

    /** Last amended by - SYSTEM. */
    public static final String AMENDED_BY_SYSTEM = "SYSTEM";

    /** String constant for Success Response Code - 200. */
    public static final String SUCCESS_RESPONSE_CODE = "200";

    /**
     * Method to process notification message response.
     * @param message as BusinessMessage
     * @return response message
     * @throws ElectroException as ElectroException
     */
    public BusinessMessage processMessage(final BusinessMessage message) throws ElectroException {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start processMessage.");
        }
        BusinessMessage responseMessage = new BusinessMessage();
        String notificationId = message.getConfigParameter(NOTIFICATION_ID);
        String ecomConfigId = message.getConfigParameter(ECOM_CONFIG_ID);
        String messageType = message.getConfigParameter(BusinessMessage.CONFIG_TRANSACTION_TYPE);
        String messagePayload = message.getStringPayload(BusinessMessage.PAYLOAD_RESPONSE_MESSAGE);
        String responseCode = message.getStringPayload(BusinessMessage.PAYLOAD_RESPONSE_CODE);
        String responseErrorStream = message.getStringPayload(BusinessMessage.PAYLOAD_RESPONSE_ERROR_STREAM);

        // Add response message in Audit table.
        auditResponseMessage(notificationId, messagePayload, responseCode, responseErrorStream);

        // Adding debug statements -
        if (LOG.isDebugEnabled()) {
            LOG.debug("Response message from Client - " + message.toString());
            LOG.debug("Response Code from Client - " + responseCode);
            LOG.debug("Response Error Stream from Client - " + responseErrorStream);
            LOG.debug("Response message Payload from Client - " + messagePayload);
        }

        if (StringUtils.isNotBlank(notificationId) && StringUtils.isNotBlank(ecomConfigId)
                && StringUtils.isNotBlank(messageType)) {
            if (StringUtils.isNotBlank(responseCode) && (SUCCESS_RESPONSE_CODE).equals(responseCode)) {
                parseMessageAndUpdateStatus(notificationId, ecomConfigId, messageType, messagePayload);
            } else {
                // Retry if response code is other than 200.
                updateNotificationDetailsStatus(notificationId, NotificationMessageStatusEnum.READY_TO_SEND_RETRY, true);
            }
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish processMessage.");
        }
        return responseMessage;
    }

    /**
     * Method to parse message payload and update the notification details status accordingly.
     * @param notificationId as String
     * @param ecomConfigId as String
     * @param messageType as String
     * @param messagePayload as String
     */
    public void parseMessageAndUpdateStatus(final String notificationId, final String ecomConfigId, final String messageType,
            final String messagePayload) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start parseMessageAndUpdateStatus.");
        }
        EcomNotificationSettingsService ecomNotificationSettingsService = ServiceLocator.locateLocal(EcomNotificationSettingsService.class);
        EcomNotificationSettings notificationSettings =
                ecomNotificationSettingsService.getEcomNotificationSettingsByConfigIdAndMsgType(Long.valueOf(ecomConfigId),
                        NotificationMessageTypeEnum.value(messageType));

        String responseValue = notificationSettings.getResponseValue();
        NotificationResponseTypeEnum responseType = notificationSettings.getResponseType();
        boolean isSuccess = true;
        if (responseType != null) {
            if (NotificationResponseTypeEnum.TEXT.getValue().equals(responseType.getValue())) {
                isSuccess = parseTextResponse(messagePayload, responseValue);
            } else if (NotificationResponseTypeEnum.XML_PATH.getValue().equals(responseType.getValue())) {
                isSuccess = parseXMLResponse(messagePayload, responseValue, notificationSettings.getResponseXMLPath());
            }
        }
        if (isSuccess) {
            updateNotificationDetailsStatus(notificationId, NotificationMessageStatusEnum.COMPLETED, false);
        } else {
            updateNotificationDetailsStatus(notificationId, NotificationMessageStatusEnum.CUSTOMER_PROCESSING_ERROR, false);
            // Send notification failure email to user.
            NotificationFailureEmailSenderUtility.sendFailureEmail(Long.valueOf(notificationId));
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish parseMessageAndUpdateStatus.");
        }
    }

    /**
     * Method to parse text message payload response.
     * @param messagePayload as String
     * @param responseValue as String
     * @return true if response is OK.
     */
    private boolean parseTextResponse(final String messagePayload, final String responseValue) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start parseTextResponse.");
        }
        if (StringUtils.isNotBlank(messagePayload) && StringUtils.isNotBlank(responseValue)) {
            if (messagePayload.contains(responseValue)) {
                return true;
            }
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish parseTextResponse.");
        }
        return false;
    }

    /**
     * Method to parse xml message payload response.
     * @param messagePayload as String
     * @param responseValue as String
     * @param responseXMLPath as String
     * @return true if response is success.
     */
    private boolean parseXMLResponse(final String messagePayload, final String responseValue, final String responseXMLPath) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start parseXMLResponse.");
        }
        if (StringUtils.isNotBlank(messagePayload) && StringUtils.isNotBlank(responseValue)
                && StringUtils.isNotBlank(responseXMLPath)) {

            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = null;
            try {
                builder = builderFactory.newDocumentBuilder();
                // Added the following to ensure that the DTD is not validated.
                // Even if the Validation is turned off, the parser still looks for the DTD and will
                // fail if not found.
                builder.setEntityResolver(new NoOpEntityResolver());
                Document xmlDocument = builder.parse(new ByteArrayInputStream(messagePayload.getBytes()));
                XPath xPath = XPathFactory.newInstance().newXPath();
                String confirmationValue = xPath.compile(responseXMLPath).evaluate(xmlDocument);
                if (StringUtils.isNotBlank(confirmationValue) && (confirmationValue).contains(responseValue)) {
                    return true;
                }
            } catch (Exception e) {
                LOG.error("Error occurred while parsing XML Response : " + e.getMessage(), e);
            }
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish parseXMLResponse.");
        }
        return false;
    }

    /**
     * Method to update status of notification details record.
     * @param notificationId as String
     * @param messageStatus as NotificationMessageStatusEnum
     * @param increamentRetry as boolean
     */
    public void updateNotificationDetailsStatus(final String notificationId, final NotificationMessageStatusEnum messageStatus,
            final boolean increamentRetry) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start updateNotificationDetailsStatus.");
        }
        DateTime currentDate = new DateTime();
        EcomNotificationDetailsService ecomNotificationDetailsService = ServiceLocator.locateLocal(EcomNotificationDetailsService.class);
        EcomNotificationDetails ecomNotificationDetails =
                ecomNotificationDetailsService.getNotificationDetailsById(Long.valueOf(notificationId));
        ecomNotificationDetails.setStatus(messageStatus);
        ecomNotificationDetails.setLastAmendedBy(AMENDED_BY_SYSTEM);
        ecomNotificationDetails.setLastAmendedDate(currentDate);
        ecomNotificationDetails.setStatusLastChanged(currentDate);
        if (increamentRetry) {
            Integer retryNumber = ecomNotificationDetails.getRetryNumber();
            if (retryNumber == null) {
                retryNumber = Integer.valueOf(0);
            }
            ecomNotificationDetails.setRetryNumber(retryNumber + 1);
        }
        ecomNotificationDetailsService.saveOrUpdateEcomNotificationDetails(ecomNotificationDetails);
        // Add entry in audit table if notification details status is other than KEY_DATA_RETRIEVED.
        ecomNotificationDetailsService.updateNotificationDetailsAudit(ecomNotificationDetails, ecomNotificationDetails.getStatus()
                .getValue());
        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish updateNotificationDetailsStatus.");
        }
    }

    /**
     * Add the response message in Audit table.
     * @param notificationId as String
     * @param messagePayload as String
     * @param responseCode as String
     * @param responseErrorStream as String
     */
    private void auditResponseMessage(final String notificationId, final String messagePayload, final String responseCode,
            final String responseErrorStream) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start updateAuditDetails.");
        }
        EcomNotificationDetailsService ecomNotificationDetailsService = ServiceLocator.locateLocal(EcomNotificationDetailsService.class);
        EcomNotificationDetails ecomNotificationDetails =
                ecomNotificationDetailsService.getNotificationDetailsById(Long.valueOf(notificationId));
        StringBuilder auditText = new StringBuilder();
        auditText.append("Response Code - " + responseCode);
        auditText.append("\nResponse Message - " + messagePayload);
        auditText.append("\nResponse Error Stream - " + responseErrorStream);
        ecomNotificationDetailsService.updateNotificationDetailsAudit(ecomNotificationDetails, auditText.toString());

        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish updateAuditDetails.");
        }
    }
}
