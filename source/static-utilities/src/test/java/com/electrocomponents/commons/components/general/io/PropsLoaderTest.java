package com.electrocomponents.commons.components.general.io;

import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.Test;

import com.electrocomponents.commons.components.general.io.PropsLoader;

import junit.framework.Assert;

public class PropsLoaderTest {
    
    private static final Log LOG = LogFactory.getLog(PropsLoaderTest.class);

    private static final String TEST_PROP_FILENAME1 = "TestProperty.properties";
    
    private static final String TEST_PROP_FILENAME2 = "otherprops.properties";
    
    private static final String TEST_PROP_FALLBACK_FILENAME = "fallback.properties";
    
    private static final String TEST_PROP_MISSING_FILENAME = "SomeInvalidPropertiesFile.properties";
    
    @Before
    public void loadPropertiesIntoSystemProps() {
        try (InputStream resourceInputStream = PropsLoaderTest.class.getClassLoader().getResourceAsStream(TEST_PROP_FILENAME1)) {
            Properties props = new Properties();
            props.load(resourceInputStream);
            System.getProperties().putAll(props);
        } catch (Exception e) {
            LOG.error("Unable to load test properties1");
        }
        try (InputStream resourceInputStream = PropsLoaderTest.class.getClassLoader().getResourceAsStream(TEST_PROP_FILENAME2)) {
            Properties props = new Properties();
            props.load(resourceInputStream);
            System.getProperties().putAll(props);
        } catch (Exception e) {
            LOG.error("Unable to load test properties2");
        }
    }

    @Test(expected = RuntimeException.class)
    public void nullFilenameSpecified(){
        PropsLoader.loadPropertiesFileFromResource(null);
        Assert.fail("Should not have reached this point, call should have ended in RuntimeException");
    }

    @Test(expected = RuntimeException.class)
    public void emptyFilenameSpecified(){
        PropsLoader.loadPropertiesFileFromResource("");
        Assert.fail("Should not have reached this point, call should have ended in RuntimeException");
    }

    @Test(expected = RuntimeException.class)
    public void nonExistingFilenameSpecified(){
        PropsLoader.loadPropertiesFileFromResource(TEST_PROP_MISSING_FILENAME);
        Assert.fail("Should not have reached this point, call should have ended in RuntimeException");
    }
    
    @Test
    public void filenameSpecifiedPropertiesSuccessfullyLoaded(){
        Properties props = PropsLoader.loadPropertiesFileFromResource(TEST_PROP_FILENAME1);
        Assert.assertNotNull(props);
        Assert.assertEquals("ONE", props.getProperty("property.name.one"));
        Assert.assertEquals("TWO", props.getProperty("property.name.two"));
        
        Properties props2 = PropsLoader.loadPropertiesFileFromResource(TEST_PROP_FILENAME2);
        Assert.assertNotNull(props);
        Assert.assertEquals("other_one", props2.getProperty("property.name.one"));
        Assert.assertEquals("other_two", props2.getProperty("property.name.two"));
    }  
    
    @Test
    public void fallbackLoadingTest(){
        // fallback.properties hasn't been loaded, so should go through the old classloader mechanism and still work
        Properties props = PropsLoader.loadPropertiesFileFromResource(TEST_PROP_FALLBACK_FILENAME);
        Assert.assertNotNull(props);
        Assert.assertEquals("fallback_one", props.getProperty("property.name.one"));
        Assert.assertEquals("fallback_two", props.getProperty("property.name.two"));        
    }  
}
