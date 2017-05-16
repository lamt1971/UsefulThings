package com.electrocomponents.messagerelay.utilities;

import java.net.Authenticator;
import java.net.PasswordAuthentication;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Copyright (c) Electrocomponents Plc.
 * Author         : e0161085
 * Creation Date  : 26-Aug-2010
 * Creation Time  : 10:40:10
 * *******************************************************************************************************************
 * Description :
 * Carry out Password Authenication.
 * This used to be in the Commom Components package, but look like it has long been removed.
 * *******************************************************************************************************************
 * Change History
 * *******************************************************************************************************************
 * * Change   * Author   * Date         * Description
 * *******************************************************************************************************************
 * * New      * e0161085 * 26-Aug-2010  * Recreated Class.
 * *******************************************************************************************************************
 *
 * @author e0180383
 */
public class BasicAuthenticator extends Authenticator {

    /**
     * Log4J.
     */
    private static Log log = LogFactory.getLog(BasicAuthenticator.class);

    /**
     * Name.
     */
    private String name;

    /**
     * Password.
     */
    private char[] pswd;

    /**
     * Default Constructor.
     */
    public BasicAuthenticator() {
        name = null;
    }

    /**
     * Get Password Authentication.
     * @return Password Authenication.
     */
    public PasswordAuthentication getPasswordAuthentication() {
        if (log.isDebugEnabled()) {
            log.debug("getPasswordAuthentication : Method Start");
        }

        PasswordAuthentication passwordauthentication = new PasswordAuthentication(name, pswd);
        String s = getRequestingPrompt();
        String s1 = getRequestingHost();
        java.net.InetAddress inetaddress = getRequestingSite();
        int i = getRequestingPort();
        String s2 = getRequestingProtocol();
        String s3 = getRequestingScheme();
        if (log.isDebugEnabled()) {
            log.debug((new StringBuilder()).append("getPasswordAuthentication : promptString : ").append(s).toString());
            log.debug((new StringBuilder()).append("getPasswordAuthentication : hostname : ").append(s1).toString());
            log.debug((new StringBuilder()).append("getPasswordAuthentication : ipaddr : ").append(inetaddress).toString());
            log.debug((new StringBuilder()).append("getPasswordAuthentication : port : ").append(i).toString());
            log.debug((new StringBuilder()).append("getPasswordAuthentication : protocol : ").append(s2).toString());
            log.debug((new StringBuilder()).append("getPasswordAuthentication : scheme : ").append(s3).toString());
            log.debug("getPasswordAuthentication : Method Finish");
        }

        return passwordauthentication;
    }

    /**
     * set the Name.
     * @param name  The Name.
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * Set the Password.
     * @param pswd The Password.
     */
    public void setPswd(final char pswd[]) {
        this.pswd = pswd;
    }
}
