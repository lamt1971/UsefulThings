package com.electrocomponents.messagerelay.process.send;

import com.electrocomponents.commons.exception.ElectroException;
import com.electrocomponents.messagerelay.support.BusinessMessage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.security.Security;

/**
 * Copyright (c) Electrocomponents Plc.
 * Author         : e0180383
 * Creation Date  : 14-Jul-2006
 * Creation Time  : 16:44:42
 * IDE            : IntelliJ IDEA 5
 * *******************************************************************************************************************
 * Description :
 * Send a BusinessMessage to a remote host as a HTTP/S datastream and recieve and relay the response
 * back to the internal system if required. USE CLIENT-CERT AUTHENTICATION WHEN CONNECTING TO THE TARGET HOST
 * *******************************************************************************************************************
 * Change History
 * *******************************************************************************************************************
 * * Change   * Author   * Date         * Description
 * *******************************************************************************************************************
 * * New      * e0180383 *  14-Jul-2006 * New class created
 * *******************************************************************************************************************
 * * GEN-1228 * e0161085 *  26-Aug-2010 * Tidied Code.
 * *******************************************************************************************************************
 *
 * @author e0180383
 */
public class HttpStreamClientCertAuthSender extends HttpStreamSender {

    /**
     * Log4J.
     */
    private static Log log = LogFactory.getLog(HttpStreamClientCertAuthSender.class);

    // TODO THIS CLASS HAS NOT BEEN TESTED - IT IS UNUSED AT THE TIME OF WRITING!!

  /**
  * Setup CLIENT CERT authentication to allow system to authenticate with remote host.
  * @param outboundMessage The outbound BusinessMessage object.
  * @throws ElectroException Exception.
  */
    protected void setupAuthentication(final BusinessMessage outboundMessage) throws ElectroException {
       if (log.isDebugEnabled()) { log.debug("setupAuthentication : Method Start"); }

       // set up property for client certificate handshake
       Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
       // set new property for client key store
       System.setProperty("javax.net.ssl.keyStore", outboundMessage.getConfigParameter(BusinessMessage.CONFIG_CLIENTCERT_AUTH_KEYSTORE));
       System.setProperty("javax.net.ssl.keyStoreType", "jks");
       System.setProperty("javax.net.ssl.keyStorePassword", outboundMessage.getConfigParameter(BusinessMessage.CONFIG_CLIENTCERT_AUTH_KS_PASSWORD));
       System.setProperty("javax.net.debug", "all");
       if (outboundMessage.getConfigParameter(BusinessMessage.CONFIG_CLIENTCERT_AUTH_TRUSTSTORE) != null) {
          System.setProperty("javax.net.ssl.trustStore", outboundMessage.getConfigParameter(BusinessMessage.CONFIG_CLIENTCERT_AUTH_TRUSTSTORE));
       }

       if (log.isDebugEnabled()) { log.debug("setupAuthentication : Method Finish"); }
    }


}
