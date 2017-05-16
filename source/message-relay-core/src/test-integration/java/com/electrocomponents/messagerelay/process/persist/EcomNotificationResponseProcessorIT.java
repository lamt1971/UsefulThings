package com.electrocomponents.messagerelay.process.persist;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import com.electrocomponents.model.domain.notifications.enumeration.NotificationMessageStatusEnum;
import com.electrocomponents.model.domain.notifications.enumeration.NotificationMessageTypeEnum;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;

/**
 * Test class to test Ecom notification response processor.
 * @author C0951407
 *
 */
public class EcomNotificationResponseProcessorIT extends TestCase {

    /** Common Logger instance. */
    private static final Log LOG = LogFactory.getLog(EcomNotificationResponseProcessorIT.class);

    /** notificationDetailsService. */
    private static EcomNotificationResponseProcessor ecomNotificationResponseProcessor = null;

    /**
     * A method to setup data required for test case.
     */
    @BeforeClass
    public void setUp() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start setUp.");
        }
        
        //
        try {
            System.setProperty("java.naming.factory.initial", "com.electrocomponents.messagerelay.process.persist.LocalInitialContextFactory");
            Context initContext = new InitialContext();
            LOG.info("Looked up AppName : " + (String)initContext.lookup("java:app/AppName"));
        } catch (NamingException e) {
            LOG.error(e);
            throw new AssertionFailedError("Naming Exception occurred, unable to bind appName JNDI context.");
        }
        
        
        if (ecomNotificationResponseProcessor == null) {
            ecomNotificationResponseProcessor = new EcomNotificationResponseProcessor();
            assertNotNull(ecomNotificationResponseProcessor);
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish setUp.");
        }
    }

    /**
     * A method to clean data set up for test case.
     */
    @AfterClass
    public void tearDown() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start tearDown.");
        }
        ecomNotificationResponseProcessor = null;
        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish tearDown.");
        }
    }

    /**
     * Test method to parse message payload (Text) and update status accordingly.
     */
    public void testParseTextMessageAndUpdateStatus() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start testParseTextMessageAndUpdateStatus.");
        }
        String notificationId = "145";
        String ecomConfigId = "940";
        String messageType = NotificationMessageTypeEnum.PURCHASE_ORDER_RESPONSE.getValue();
        String messagePayload = "OK";
        ecomNotificationResponseProcessor.parseMessageAndUpdateStatus(notificationId, ecomConfigId, messageType, messagePayload);

        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish testParseTextMessageAndUpdateStatus.");
        }
    }

    /**
     * Test method to parse message payload (XML) and update status accordingly.
     */
    public void testParseXMLMessageAndUpdateStatus() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start testParseXMLMessageAndUpdateStatus.");
        }
        String notificationId = "31";
        String ecomConfigId = "1399";
        String messageType = NotificationMessageTypeEnum.PURCHASE_ORDER_RESPONSE.getValue();
        StringBuilder messagePayload = new StringBuilder();
        messagePayload
                .append("<?xml version=\"1.0\" encoding=\"UTF-8\"?><!DOCTYPE cXML SYSTEM \"http://xml.cxml.org/schemas/cXML/1.2.024/Fulfill.dtd\"><cXML timestamp=\"2014-06-02T08:53:53-07:00\" payloadID=\"1401724433373-8633436477781194695@216.109.111.67\"><Response><Status code=\"200\" text=\"SUccess\">Error:The Sender doesn&apos;t have authority to send on behalf of the From credential  Please contact support with the Error Reference Number: ANERR-50000000000000000097796776 for more details</Status></Response></cXML>");
        ecomNotificationResponseProcessor.parseMessageAndUpdateStatus(notificationId, ecomConfigId, messageType, messagePayload.toString());

        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish testParseXMLMessageAndUpdateStatus.");
        }
    }

    /**
     * Test method to update status of notification details record.
     */
    public void testUpdateNotificationDetailsStatus() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start testUpdateNotificationDetailsStatus.");
        }
        String notificationId = "145";
        boolean increamentRetry = true;
        NotificationMessageStatusEnum messageStatus = NotificationMessageStatusEnum.READY_TO_SEND_RETRY;
        ecomNotificationResponseProcessor.updateNotificationDetailsStatus(notificationId, messageStatus, increamentRetry);
        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish testUpdateNotificationDetailsStatus.");
        }
    }
}
