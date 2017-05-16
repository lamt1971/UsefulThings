package com.electrocomponents.messagerelay.synch;

import org.junit.Before;
import org.junit.Test;

import com.electrocomponents.messagerelay.process.persist.LoopBackProcessor;
import com.electrocomponents.messagerelay.support.BusinessMessage;
import com.electrocomponents.service.core.client.ServiceLocator;
import com.electrocomponents.service.notifications.sync.interfaces.SynchMessageRelay;

/**
 * @author Hemchandra Phirke
 */

public class SynchMessageRelayIT {

    private BusinessMessage testMsg;

    @Before
    public void setup() {
        testMsg = new BusinessMessage();
        testMsg.setProcessorClass(LoopBackProcessor.class.getCanonicalName());
        testMsg.setConfigParameter(BusinessMessage.CONFIG_TARGET_URL, "http://order.rs-components.com/InterfaceServlet");
        testMsg.setConfigParameter(BusinessMessage.CONFIG_TIMEOUT_MILLISECONDS, "5000");
        testMsg.addPostParameter("pcl_no", "021115115175");
        testMsg.addPostParameter("country", "GB");
    }

    @Test
    public void synchMessageRelay() {
        SynchMessageRelay interfaceref = ServiceLocator.locateRemote("message-relay-core", SynchMessageRelay.class);

        System.out.println("SynchMessageRelayTest : main : Outgoing Message =  \n\n" + testMsg.toString());

        BusinessMessage responseMsg = interfaceref.processMessage(testMsg);

        System.out.println("SynchMessageRelayTest : main : Response Message =  \n\n" + responseMsg.toString());
    }

}
