package com.electrocomponents.commons.components.general.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

public final class StockNumberValidatorUtil {

    /** NON_CATALOGUE_PRODUCT_PREFIX. */
    public static final String NON_CATALOGUE_PRODUCT_PREFIX = "25";

    /** Private constructor to avoid creating instances. */
    private StockNumberValidatorUtil() {
        // Nothing to do
    }

    /**
     * @param rawSN rawSN
     * @return string
     */
    public static boolean checkSN(final String rawSN) {
        if (StringUtils.isBlank(rawSN)) {
            return false;
        }
        String fmtSN = rawSN.toUpperCase().replaceAll("[^0-9A-Z]", "");
        int length = 10 - NON_CATALOGUE_PRODUCT_PREFIX.length();
        Pattern p = Pattern.compile(NON_CATALOGUE_PRODUCT_PREFIX + "[0-9]{" + length + "}");

        Matcher m = p.matcher(fmtSN.toUpperCase());
        return checkSN(fmtSN, m.matches());
    }

    /**
     * @param rawSN rawSN
     * @param isNonCatalogue isNonCatalogue
     * @return boolean
     */
    public static boolean checkSN(final String rawSN, final boolean isNonCatalogue) {
        if (StringUtils.isBlank(rawSN)) {
            return false;
        }
        int[] checkArray = { 1, 5, 1, 9, 7, 3, 1, 6 };
        int iLen = checkArray.length;
        // remove any alpha chars
        String fmtSN = rawSN.replaceAll("\\D", "");
        // set the check number
        int checkValue = 0;
        if (isNonCatalogue) {
            // the check value is offset by 3 and we need to remove the prefix
            // checkValue = 3;
            fmtSN = fmtSN.substring(NON_CATALOGUE_PRODUCT_PREFIX.length());
        }
        // pad the raw sn to the right length
        if (fmtSN.length() < 9) {
            // must be a least 9 characters long
            fmtSN = ("000000000" + fmtSN).substring(fmtSN.length());
        }

        // loop and do the multiplication
        for (int i = 0; i < iLen; i++) {
            checkValue += (checkArray[i] * Integer.parseInt(fmtSN.substring(i, i + 1), 10));
        }
        String checkString = Integer.toString(checkValue);
        String testString = fmtSN.substring(fmtSN.length() - 1);
        if ((testString.equals(checkString.substring(checkString.length() - 1)))) {
            return true;
        }

        checkString = Integer.toString(checkValue + 7);
        if ((testString.equals(checkString.substring(checkString.length() - 1)))) {
            return true;
        }

        checkString = Integer.toString(checkValue + 3);
        if ((testString.equals(checkString.substring(checkString.length() - 1)))) {
            return true;
        }

        return false;
    }
}
