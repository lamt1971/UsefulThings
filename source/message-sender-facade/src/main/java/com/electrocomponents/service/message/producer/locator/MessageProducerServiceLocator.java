package com.electrocomponents.service.message.producer.locator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.electrocomponents.model.domain.Locale;
import com.electrocomponents.service.core.client.BaseLocator;
import com.electrocomponents.service.message.producer.interfaces.MessageProducerService;

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
 * The MessageProducerServiceLocator is service locator class for MessageProducer Service.
 * @author Raja Govindharaj
 */
public final class MessageProducerServiceLocator extends BaseLocator<MessageProducerService> {

    /** A static instance of MessageProducerServiceLocator. */
    private static MessageProducerServiceLocator messageProducerServiceLocator = null;

    /** A static instance of Apache Common Log. */
    private static final Log LOG = LogFactory.getLog(MessageProducerServiceLocator.class);

    /** Allow override of application name for integration tests */
    private String appName = null;

    /** Private Constructor. */
    private MessageProducerServiceLocator() {
        setApplicationName(resolveApplicationName());
    }

    /**
     * The getLocator() method creates single instance of MessageProducerServiceLocator if one is not available before.
     * @return MessageProducerServiceLocator
     */
    public static synchronized MessageProducerServiceLocator getLocator() {

        if (LOG.isDebugEnabled()) {
            LOG.debug("MessageProducerServiceLocator.getLocator() is started");
        }

        if (messageProducerServiceLocator == null) {
            messageProducerServiceLocator = new MessageProducerServiceLocator();
            if (LOG.isDebugEnabled()) {
                LOG.debug("A new instance of MessageProducerServiceLocator is created");
            }
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("MessageProducerServiceLocator.getLocator() is finished");
        }

        return messageProducerServiceLocator;
    }

    /**
     * ONLY TO BE USED FOR INTEGRATION TESTS
     * Singleton to return the instance of the locator.
     * @return The messageProducerServiceLocator instance..
     */
    public static synchronized MessageProducerServiceLocator getLocator(String appName) {

        if (messageProducerServiceLocator == null) {
            messageProducerServiceLocator = new MessageProducerServiceLocator();
            messageProducerServiceLocator.appName = appName;
        }
        return messageProducerServiceLocator;
    }

    /**
     * Locate and retrieve an instance of a MessageProducerService. This method will lookup for the bean in the local Application context.
     * Local application name is resolved by lookin up for java:app/AppName in local JNDI context.
     * @param locale The locale we wish to retrieve a MessageProducerService for.
     * @return A MessageProducerService that supports the specified locale.
     */
    public MessageProducerService locate(final Locale locale) {
        String jndi = buildEjbJndiName(this.getApplicationName(), MessageProducerService.EJB_MODULE_NAME, MessageProducerService.SERVICE_NAME,
                MessageProducerService.class.getCanonicalName());
        return super.locate(jndi, locale, true);
    }

    /**
     * Locate and retrieve an instance of a MessageProducerService. This method will lookup for the bean in the local Application context.
     * Local application name is resolved by lookin up for java:app/AppName in local JNDI context.
     * @param locale The locale we wish to retrieve a MessageProducerService for.
     * @return A MessageProducerService that supports the specified locale.
     */
    public MessageProducerService locate() {
        String jndi = buildEjbJndiName(this.getApplicationName(), MessageProducerService.EJB_MODULE_NAME, MessageProducerService.SERVICE_NAME,
                MessageProducerService.class.getCanonicalName());
        return super.locate(jndi, true);
    }

    /**
     * Locate and retrieve an instance of a MessageProducerService. This method will lookup for the bean in the local Application context.
     * Local application name is resolved by lookin up for java:app/AppName in local JNDI context.
     * @param appName   The name of the application.
     * @return A MessageProducerService that supports the specified locale.
     */
    public MessageProducerService locate(final String appName) {
        String jndi = buildEjbJndiName(appName, MessageProducerService.EJB_MODULE_NAME, MessageProducerService.SERVICE_NAME,
                MessageProducerService.class.getCanonicalName());
        return super.locate(jndi, true);
    }

    /**
     * @return the application name
     */
    public String getApplicationName() {
        if (this.appName == null) {
            return super.getApplicationName();
        }
        return this.appName;
    }
}
