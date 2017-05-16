package com.electrocomponents.commons.cache;

import com.electrocomponents.model.domain.Locale;
import com.electrocomponents.model.domain.Site;

/**
 * <pre>
 * ************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author  : Raja Govindharaj
 * Created : 21 Jul 2011 at 12:25:05
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
 * The class provides contract to sub-classes to provide the implementation.
 * @author Raja Govindharaj
 */
public interface CachePath extends CachePathConstant {

    /**
     * The method generates cache path for application.
     * @param paths is Cache PATH.
     * @return String.
     */
    String generateApplicationPath(final String... paths);

    /**
     * The method will generate publication path by appending PUBLICATION + path + publicationName. The publicationName is added at last.
     * @param path is generated cache path.
     * @param publicationName is Publication Name.
     * @return String
     */
    String generatePublicationPath(final String path, final String publicationName);

    /**
     * The method construct path like PUBLICATION + prefixPath + publicationName + suffixPath.
     * @param prefixPath is prefix or affix path is appended after PUBLICATION.
     * @param publicationName is Publication Name.
     * @param suffixPath is appended after Publication Name.
     * @return String
     */
    String generatePublicationPath(final String prefixPath, final String publicationName, final String suffixPath);

    /**
     * The method will generate site path by appending SITE + path + siteName. The siteName is added at last.
     * @param path is generated cache path.
     * @param siteName is Site Name.
     * @return String
     */
    String generateSitePath(final String path, final Site siteName);

    /**
     * The method construct path like SITE + prefixPath + siteName + suffixPath.
     * @param prefixPath is prefix or affix path is appended after SITE.
     * @param siteName is Site Name.
     * @param suffixPath is appended after Site Name.
     * @return String
     */
    String generateSitePath(final String prefixPath, final Site siteName, final String suffixPath);

    /**
     * The method will generate locale path by appending LOCALE + path + localeName. The localeName is added at last.
     * @param path is generated cache path.
     * @param localeName is Locale Name.
     * @return String
     */
    String generateLocalePath(final String path, final Locale localeName);

    /**
     * The method constructs path like LOCALE + prefixPath + localeName + suffixPath.
     * @param prefixPath is prefix or affix path is appended after LOCALE.
     * @param localeName is Locale Name.
     * @param suffixPath is appended after Locale Name.
     * @return String
     */
    String generateLocalePath(final String prefixPath, final Locale localeName, final String suffixPath);

    /**
     * The method constructs path with PATH_SEPARATOR if its not in path.
     * @param paths is PATH.
     * @return String
     */
    String constructPath(final String... paths);
}
