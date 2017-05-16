package com.electrocomponents.messagerelay.process.persist;

import static org.mockito.Mockito.mock;

import java.util.HashMap;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.Test;

import com.electrocomponents.messagerelay.process.send.OB10HttpsSender;
import com.electrocomponents.messagerelay.support.BusinessMessage;

/**
 * Copyright (c) Electrocomponents Plc.
 * 
 * @author Ram Tripathi
 */

public class OB10HttpsSenderIT extends TestCase {

    /** Commons logging logger. */
    private static final Log LOG = LogFactory.getLog(OB10HttpsSenderIT.class);

    private BusinessMessage message = mock(BusinessMessage.class);

    private OB10HttpsSender sender;
    HashMap<String, String> configParam = new HashMap<String, String>();
    HashMap<String, String> stringPayload = new HashMap<String, String>();

    @Before
    protected void setUp() throws Exception {

        message = new BusinessMessage();
        sender = new OB10HttpsSender();
        stringPayload.put("OBEDocument", "<Invoice>Test Invoice </Invoice>");
        configParam.put("OB10HTTPSNumber", "AAA410579676");
        configParam.put("OB10HTTPSUser", "AAA410579676");
        configParam.put("OB10HTTPSPassword", "KoandsY2133");
        configParam.put("OB10HTTPSUrl", "http://transfer.ob10.com/server?request=send?directory=DX014");
        configParam.put("OB10_TEMPORARY_FILE_PATH", "./");
        configParam.put("OB10HTTPSFileName", "Invoice.xml");
        configParam.put(BusinessMessage.CONFIG_DISABLE_ROLLBACK_ON_ERROR, "TRUE");
        message.setConfigParameters(configParam);
    }

    /**
     * Tear Down Up the Class.
     * @see junit.framework.TestCase#tearDown()
     * @throws Exception Throw any Exception
     */
    protected void tearDown() throws Exception {
        super.tearDown();
        /* dateValidator = null; */
    }

    /**
     * Test OB10HttpsSender.processMessage(BusinessMessage) with valid credentials
     * 
     */
    @Test
    public void testProcessMessage() {
        /**
         * Call processMessage with configParam
         * 
         */
        BusinessMessage responceMessage = sender.processMessage(message);

        assertEquals("OK", responceMessage.getStringPayload("ResponseMessage"));

    }

    /**
     * Test OB10HttpsSender.processMessage(BusinessMessage) with Invalid OB10HTTPSNumbe (Ob10 username).
     * 
     */
    @Test
    public void testProcessMessage_Invalid_OB10HTTPSNumber() {
        /**
         * Set configParam configParam to invalid usernumber (UserId )
         * 
         */
        configParam.put("OB10HTTPSNumber", "ABCINVALIDUSER");

        message.setConfigParameters(configParam);

        BusinessMessage responceMessage = sender.processMessage(message);
        /**
         * Assert Unauthorized response.
         * 
         */
        assertEquals("Unauthorized  wrong Username Or password error    Response-  401", responceMessage
                .getStringPayload("ResponseMessage"));

    }

    /**
     * Test OB10HttpsSender.processMessage(BusinessMessage) with Invalid OB10HTTPSNumbe (Ob10 username).
     * 
     */
    public void testProcessMessage_Null_OB10HTTPSNumber() {
        /**
         * Set configParam to null
         * 
         */
        configParam.put("OB10HTTPSNumber", null);

        message.setConfigParameters(configParam);

        BusinessMessage responceMessage = sender.processMessage(message);
        /**
         * Assert Unauthorized response.
         * 
         */
        assertEquals("Unauthorized  wrong Username Or password error    Response-  401", responceMessage
                .getStringPayload("ResponseMessage"));

    }

    /**
     * Test OB10HttpsSender.processMessage(BusinessMessage) with Invalid OB10HTTPSPassword.
     * 
     */
    public void testProcessMessage_Null_OB10HTTPSPassword() {
        /**
         * Set configParam OB10HTTPSPassword to null
         * 
         */
        configParam.put("OB10HTTPSPassword", null);

        message.setConfigParameters(configParam);

        BusinessMessage responceMessage = sender.processMessage(message);
        /**
         * Assert Unauthorized response.
         * 
         */
        assertEquals("Unauthorized  wrong Username Or password error    Response-  401", responceMessage
                .getStringPayload("ResponseMessage"));

    }

    /**
     * Test OB10HttpsSender.processMessage(BusinessMessage) with Invalid OB10 Url.
     * 
     */
    public void testProcessMessage_Invalid_Url() {
        /**
         * Set configParam OB10HTTPSUrl to an invalid destination
         * 
         */

        configParam.put("OB10HTTPSUrl", "http://transfer.ob10.com/WrongDestination?request=send?directory=DX014");

        BusinessMessage responceMessage = sender.processMessage(message);
        /**
         * Assert Resource Not found response.
         * 
         */
        assertEquals("Resource Not found    Response-  404", responceMessage.getStringPayload("ResponseMessage"));

    }

    /**
     * Test OB10HttpsSender.processMessage(BusinessMessage) with null Url.
     * 
     */
    public void testProcessMessage_Null_Url() {
        /**
         * Set configParam OB10HTTPSUrl to null
         * 
         */

        configParam.put("OB10HTTPSUrl", null);

        BusinessMessage responceMessage = sender.processMessage(message);
        /**
         * Assert Https url is null response.
         * 
         */
        assertEquals("OB10 Https url is null  obeUrl -  null", responceMessage.getStringPayload("ResponseMessage"));

    }

    /**
     * Test OB10HttpsSender.processMessage(BusinessMessage) with null Url.
     * 
     */
    @Test
    public void testProcessInvoice() {

        BusinessMessage responceMessage = sender.processInvoice(message);
        /**
         * Assert response status OK.
         * 
         */
        assertEquals("OK", responceMessage.getStringPayload("ResponseMessage"));
    }

}
