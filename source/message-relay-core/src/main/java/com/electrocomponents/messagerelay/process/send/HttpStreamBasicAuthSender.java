package com.electrocomponents.messagerelay.process.send;

//import com.electrocomponents.commons.components.general.net.BasicAuthenticator;
import com.electrocomponents.commons.exception.ElectroException;
import com.electrocomponents.messagerelay.support.BusinessMessage;
import com.electrocomponents.messagerelay.utilities.BasicAuthenticator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.net.Authenticator;

/**
 * Copyright (c) Electrocomponents Plc.
 * Author         : e0180383
 * Creation Date  : 27-Jun-2006
 * Creation Time  : 10:40:10
 * IDE            : IntelliJ IDEA 5
 * *******************************************************************************************************************
 * Description :
 * Send a BusinessMessage to a remote host as a HTTP/S datastream and recieve and relay the response
 * back to the internal system if required. USE BASIC AUTHENTICATION WHEN CONNECTING TO THE TARGET HOST
 * *******************************************************************************************************************
 * Change History
 * *******************************************************************************************************************
 * * Change   * Author   * Date         * Description
 * *******************************************************************************************************************
 * * New      * e0161085 * 27-Jun-2006  * New class created
 * *******************************************************************************************************************
 *
 */
public class HttpStreamBasicAuthSender extends HttpStreamSender {

    /**
     * Log4J.
     */
    private static Log log = LogFactory.getLog(HttpStreamBasicAuthSender.class);

   /**
    * Setup BASIC AUTHENTICATION to allow system to authenticate with remote host.
    * @param outboundMessage The outbound BusinessMessage object.
    * @throws ElectroException Exception.
    */
    protected void setupAuthentication(final BusinessMessage outboundMessage) throws ElectroException {
       if (log.isDebugEnabled()) { log.debug("setupAuthentication : Method Start"); }

       BasicAuthenticator ba = new BasicAuthenticator();

       String username          = outboundMessage.getConfigParameter(BusinessMessage.CONFIG_USERNAME);
       String usernameEncScheme = outboundMessage.getConfigParameter(BusinessMessage.CONFIG_USERNAME_ENCODING);

       String password          = outboundMessage.getConfigParameter(BusinessMessage.CONFIG_PASSWORD);
       String passwordEncScheme = outboundMessage.getConfigParameter(BusinessMessage.CONFIG_PASSWORD_ENCODING);

       if (log.isDebugEnabled()) { log.debug("setupAuthentication : username = " + username); }
       if (log.isDebugEnabled()) { log.debug("setupAuthentication : username encoding scheme = " + usernameEncScheme); }
       if (log.isDebugEnabled()) { log.debug("setupAuthentication : password = " + password); }
       if (log.isDebugEnabled()) { log.debug("setupAuthentication : password encoding scheme = " + passwordEncScheme); }

       // Encode username and password id required...
       if (usernameEncScheme != null) {
         username = encode(username, usernameEncScheme);
       }
       if (passwordEncScheme != null) {
         password = encode(password, passwordEncScheme);
       }

       if (password != null) {
          char [] passwordBytes = new char[password.length()];
          password.getChars(0, password.length(), passwordBytes, 0);
          ba.setPswd(passwordBytes);
       }
       
       ba.setName(username);
       

       Authenticator.setDefault(ba);

       if (log.isDebugEnabled()) { log.debug("setupAuthentication : Method Finish"); }
    }

}
