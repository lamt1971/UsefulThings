package com.electrocomponents.commons.components.general.util;

public class JvmRouteUtil {

    static final String SHORT_HOSTNAME_PROPERTY_NAME = "short_hostname";

    public static final String JVM_ROUTE = getJvmRoute(System.getProperty(SHORT_HOSTNAME_PROPERTY_NAME));

    public static String getJvmRoute(String hostIdentifier) {
        return stripNonDigits(hostIdentifier);
    }

    private static String stripNonDigits(final String input){
        if (input == null) {
            return null;
        }
        return input.replaceAll("\\D+", "");
    }
}
