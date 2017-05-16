package com.electrocomponents.commons.components.general.util;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static com.electrocomponents.commons.components.general.util.JvmRouteUtil.SHORT_HOSTNAME_PROPERTY_NAME;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class JvmRouteUtilTest {

    private String oldSystemPropertyValue;

    @Before
    public void beforeEach() {
        oldSystemPropertyValue = System.setProperty(SHORT_HOSTNAME_PROPERTY_NAME, "HOST123");
    }

    @After
    public void afterEach() {
        if( oldSystemPropertyValue == null ) {
            System.clearProperty(SHORT_HOSTNAME_PROPERTY_NAME);
        } else {
            System.setProperty(SHORT_HOSTNAME_PROPERTY_NAME, oldSystemPropertyValue);
        }
    }

    @Test
    public void jvmRouteShouldUseShortHostnameSystemPropertyAsDefaultHostIdentifier() throws Exception {
        String jvmRoute = JvmRouteUtil.JVM_ROUTE;

        assertThat(jvmRoute, equalTo("123"));
    }

    @Test
    public void jvmRouteShouldDigitsInHostIdentifier() throws Exception {
        String hostIdentifier = "123HOST456";

        String jvmRoute = JvmRouteUtil.getJvmRoute(hostIdentifier);

        assertThat(jvmRoute, equalTo("123456"));
    }

    @Test
    public void jvmRouteShouldBeEmptyStringIfHostIdentifierDoesntContainDigits() throws Exception {
        String hostIdentifier = "ABC";

        String jvmRoute = JvmRouteUtil.getJvmRoute(hostIdentifier);

        assertThat(jvmRoute, equalTo(""));
    }

    @Test
    public void jvmRouteShouldBeNullIfHostIdentifierIsNull() throws Exception {
        String jvmRoute = JvmRouteUtil.getJvmRoute(null);

        assertThat(jvmRoute, equalTo(null));
    }

    @Test
    public void jvmRouteShouldBeEmptyStringIfHostIdentifierIsEmptyString() throws Exception {
        String jvmRoute = JvmRouteUtil.getJvmRoute("");

        assertThat(jvmRoute, equalTo(""));
    }
}
