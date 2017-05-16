package com.electrocomponents.service.notifications.support;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.electrocomponents.model.data.usermessaging.MailSettings;
import com.electrocomponents.model.domain.Locale;
import com.electrocomponents.model.domain.eprocurement.EcomCustomerSettings;
import com.electrocomponents.model.domain.notifications.EcomNotificationDetails;
import com.electrocomponents.model.domain.notifications.EcomNotificationSettings;
import com.electrocomponents.model.domain.notifications.enumeration.NotificationMessageTypeEnum;
import com.electrocomponents.service.core.client.ServiceLocator;
import com.electrocomponents.service.core.config.interfaces.LabelService;
import com.electrocomponents.service.core.config.interfaces.PropertyService;
import com.electrocomponents.service.core.usermessaging.interfaces.UserMessagingService;
import com.electrocomponents.service.core.usermessaging.support.InputType;
import com.electrocomponents.service.notifications.interfaces.EcomNotificationDetailsService;
import com.electrocomponents.service.notifications.interfaces.EcomNotificationSettingsService;

/**
 * Utility class to send failure notification email to user.
 * @author C0951407
 *
 */
public final class NotificationFailureEmailSenderUtility {

    /** Logger. */
    private static final Log LOG = LogFactory.getLog(NotificationFailureEmailSenderUtility.class);

    /** String constant for email subject. */
    private static final String EMAIL_SUBJECT = "enotificationFailureEmailSubject";

    /** String constant for email subject. */
    private static final String EMAIL_BODY = "enotificationFailureEmailBody";

    /** Place Holder to insert MESSAGETYPE into Label Value. */
    private static final String LBL_PLACE_HOLDER_MESSAGETYPE_VALUE = "##MESSAGETYPE##";

    /** Place Holder to insert NOTIFICATIONID into Label Value. */
    private static final String LBL_PLACE_HOLDER_NOTIFICATIONID_VALUE = "##NOTIFICATIONID##";

    /** Place Holder to insert RSORDERREF into Label Value. */
    private static final String LBL_PLACE_HOLDER_RSORDERREF_VALUE = "##RSORDERREF##";

    /** Place Holder to insert STATUS into Label Value. */
    private static final String LBL_PLACE_HOLDER_STATUS_VALUE = "##STATUS##";

    /**
     * private constructor.
     */
    private NotificationFailureEmailSenderUtility() {
        // Line added to suppress checkstyle warning.
    }

    /**
     * Method to send ASN / POR message via email to customer.
     * @param notificationId as String
     */
    public static void sendFailureEmail(final Long notificationId) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start sendFailureEmail.");
        }
        final EcomNotificationDetailsService ecomNotificationDetailsService = ServiceLocator.locateLocal(EcomNotificationDetailsService.class);
        final EcomNotificationDetails ecomNotificationDetails = ecomNotificationDetailsService.getNotificationDetailsById(notificationId);
        final Locale locale = new Locale(ecomNotificationDetails.getLocale());
        final UserMessagingService userMessagingService = ServiceLocator.locateLocalOrRemote(UserMessagingService.class, locale);
        final LabelService labelService = ServiceLocator.locateLocalOrRemote(LabelService.class, locale);
        EcomNotificationSettingsService ecomNotificationSettingsService = ServiceLocator.locateLocal(EcomNotificationSettingsService.class);

        EcomCustomerSettings customerSettings = ecomNotificationDetails.getEcomCustomerSettings();
        NotificationMessageTypeEnum messageType = ecomNotificationDetails.getMessageType();

        if (customerSettings != null && messageType != null) {
            EcomNotificationSettings ecomNotificationSettings =
                    ecomNotificationSettingsService.getEcomNotificationSettingsByConfigIdAndMsgType(customerSettings.getConfigId(),
                            messageType);
            if (ecomNotificationSettings != null) {
                String failureEmailId = ecomNotificationSettings.getFailureNotificationEmail();
                if (StringUtils.isNotBlank(failureEmailId)) {
                    final MailSettings mailSettings = getMailSettings(failureEmailId, locale, labelService);
                    final Map<String, String> params = new HashMap<String, String>();
                    params.put(LBL_PLACE_HOLDER_MESSAGETYPE_VALUE, ecomNotificationDetails.getMessageType().getValue());
                    params.put(LBL_PLACE_HOLDER_NOTIFICATIONID_VALUE, String.valueOf(ecomNotificationDetails.getId()));
                    params.put(LBL_PLACE_HOLDER_RSORDERREF_VALUE, ecomNotificationDetails.getRsOrderReference());
                    params.put(LBL_PLACE_HOLDER_STATUS_VALUE, ecomNotificationDetails.getStatus().getValue());
                    final String messageBody = labelService.getLabelValue(EMAIL_BODY, locale, params);
                    userMessagingService.sendEmail(messageBody, mailSettings);
                }
            }
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish sendFailureEmail.");
        }
    }

    /**
     * Method to get mail settings.
     * @param toEmailAddress as String
     * @param locale as Locale
     * @param labelService as LabelService
     * @return mailSettings as MailSettings
     */
    public static MailSettings getMailSettings(final String toEmailAddress, final Locale locale, final LabelService labelService) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start getMailSettings.");
        }
        final MailSettings mailSettings = new MailSettings();
        final PropertyService ps = ServiceLocator.locateLocalOrRemote(PropertyService.class, locale);
        final String fromAddress = ps.getProperty("EURO_ITC_EMAIL_ADDR", locale);
        mailSettings.setFromAddress(fromAddress);
        mailSettings.setToAddress(toEmailAddress);
        mailSettings.setSubject(labelService.getLabelValue(EMAIL_SUBJECT, locale));
        mailSettings.setLocale(locale);
        mailSettings.setInlineInputType(InputType.TEXT);
        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish getMailSettings.");
        }
        return mailSettings;
    }
}
