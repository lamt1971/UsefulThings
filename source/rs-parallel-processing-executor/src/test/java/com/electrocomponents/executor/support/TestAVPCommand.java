package com.electrocomponents.executor.support;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.electrocomponents.executor.EcCommand;
import com.electrocomponents.model.domain.Locale;
import com.electrocomponents.service.exception.ServiceException;

/**
 * A Test AVP Command.
 * @author Bhavesh Kavia
 */
public class TestAVPCommand extends EcCommand {
    /** The Serial Version Id. */
    private static final long serialVersionUID = 6656352795961210870L;

    /** The Commons Logger. */
    private static final Log LOG = LogFactory.getLog(TestAVPCommand.class);

    /** Holds any Exception thrown during execution of TestAVPCommand. */
    private ServiceException error = null;

    /** Holds the EcCommandSpecification. */
    private final TestAVPCommandSpecification avpSpecification;

    /**
     * TestAVPCommand Constructor.
     * @param testAVPCommandSpecification - The TestAVPCommand Specification.
     */
    public TestAVPCommand(final TestAVPCommandSpecification testAVPCommandSpecification) {
        this.avpSpecification = testAVPCommandSpecification;
    }

    /** {@inheritDoc} */
    @Override
    public EcCommand execute() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start execute.");
        }

        Locale locale = avpSpecification.getLocale();
        String sessionId = avpSpecification.getSessionId();
        String pageId = avpSpecification.getPageId();
        LOG.info("Executing TestAVPCommand for sessionId[" + sessionId + "] PageId[" + pageId + "] and Locale[" + locale + "]");

        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish execute.");
        }
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public Throwable getError() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start getError.");
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish getError.");
        }
        return error;
    }

    /** {@inheritDoc} */
    @Override
    public Object getResult() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start getResult.");
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("Finish getResult.");
        }
        return "TestAVPResult_" + avpSpecification.getPageId();
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
