package com.electrocomponents.service.notifications.sync.locator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.electrocomponents.messagerelay.JndiConstants;
import com.electrocomponents.service.notifications.sync.interfaces.SynchMessageRelay;

/**
 *<pre>
 * ************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author  : Raja Govindharaj
 * Created : 18 Oct 2010 at 11:25:22
 *
 * ************************************************************************************************
 * Change History
 * ************************************************************************************************
 * Ref      * Who      * Date       * Description
 * ************************************************************************************************
 *          *          *            *
 * ************************************************************************************************
 * </pre>
 */

/**
 * The SynchMessageRelayLocator is service locator class for SynchMessageRelay Service.
 * @author Hemchandra Phirke
 */
public final class SynchMessageRelayLocator extends BaseLocator<SynchMessageRelay> {

    /** A static instance of SynchMessageRelayLocator. */
    private static SynchMessageRelayLocator synchMessageRelayLocator = null;

    /** A static instance of Apache Common Log. */
    private static final Log LOG = LogFactory.getLog(SynchMessageRelayLocator.class);

    /** Private Constructor. */
    private SynchMessageRelayLocator() {
        setApplicationName(resolveApplicationName());
    }

    /**
     * The getLocator() method creates single instance of MessageProducerServiceLocator if one is not available before.
     * @return MessageProducerServiceLocator
     */
    public static SynchMessageRelayLocator getLocator() {

        if (LOG.isDebugEnabled()) {
            LOG.debug("MessageProducerServiceLocator.getLocator() is started");
        }

        if (synchMessageRelayLocator == null) {
            synchMessageRelayLocator = new SynchMessageRelayLocator();
            if (LOG.isDebugEnabled()) {
                LOG.debug("A new instance of MessageProducerServiceLocator is created");
            }
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("MessageProducerServiceLocator.getLocator() is finished");
        }

        return synchMessageRelayLocator;
    }

    /**
     * Locate and retrieve an instance of a SynchMessageRelay. This method will lookup for the bean in the local Application context.
     * Local application name is resolved by lookin up for java:app/AppName in local JNDI context.
     * @return A SynchMessageRelay that supports the specified locale.
     */
    public SynchMessageRelay locate() {
        String jndi = buildEjbJndiName(this.getApplicationName(), JndiConstants.EJB_MODULE_NAME, SynchMessageRelay.SERVICE_NAME,
                SynchMessageRelay.class.getCanonicalName());
        return super.locate(jndi, true);
    }

    /**
     * Locate and retrieve an instance of a SynchMessageRelay. This method will lookup for the bean in the local Application context.
     * Local application name is resolved by lookin up for java:app/AppName in local JNDI context.
     * @param APP_NAME Name of the application
     * @return A SynchMessageRelay that supports the specified locale.
     */
    public SynchMessageRelay locate(final String APP_NAME) {
        String jndi = buildEjbJndiName(APP_NAME, JndiConstants.EJB_MODULE_NAME, SynchMessageRelay.SERVICE_NAME, SynchMessageRelay.class
                .getCanonicalName());
        return super.locate(jndi, true);
    }
}
