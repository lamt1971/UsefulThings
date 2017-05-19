package com.electrocomponents.service.core.client;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import junit.framework.TestCase;

public class TestLoadProperties extends TestCase { 
	
	 /** Logger. */
    private static final Log LOG = LogFactory.getLog(TestLoadProperties.class);

	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}
	
	public void testLoadProperties() {
		
		Map<String, Object> supportedLocales = LoadProperties.loadSupportedLocales();
		
		assertNotNull(supportedLocales);
		
		LOG.info(supportedLocales);
		
	}
	

	
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}
}
