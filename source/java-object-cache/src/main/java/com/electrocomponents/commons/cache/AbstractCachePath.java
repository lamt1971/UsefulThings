package com.electrocomponents.commons.cache;

import org.apache.commons.lang.StringUtils;

import com.electrocomponents.model.domain.Locale;
import com.electrocomponents.model.domain.Site;

/**
 * <pre>
 * ************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author  : Raja Govindharaj
 * Created : 25 Jul 2011 at 10:30:06
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
 * The abstract class provides support implementation for CachePath Impl.
 * @author Raja Govindharaj
 */
public abstract class AbstractCachePath implements CachePath {

    /** {@inheritDoc} */
    public String constructPath(final String... paths) {
        StringBuilder builder = new StringBuilder();
        if (null != paths) {
            for (String path : paths) {
            	if (StringUtils.isNotBlank(path)) {
                    if (!path.startsWith(PATH_SEPARATOR)) {
                        builder.append(PATH_SEPARATOR);
                    }
                    builder.append(path);
                }
            }
        }
        return builder.toString();
    }

    /** {@inheritDoc} */
    public String generateApplicationPath(final String... paths) {
        StringBuilder builder = new StringBuilder(PATH_SEPARATOR);
        builder.append(getRootPath());
        builder.append(PATH_SEPARATOR);
        builder.append(APPLICATION);
        if (null != paths) {
            for (String path : paths) {
            	if (StringUtils.isNotBlank(path)) {
                    if (!path.startsWith(PATH_SEPARATOR)) {
                        builder.append(PATH_SEPARATOR);
                    }
                    builder.append(path);
                }
            }
        }

        return builder.toString();
    }

    /** {@inheritDoc} */
    public String generatePublicationPath(final String path, final String publicationName) {
        StringBuilder builder = new StringBuilder(PATH_SEPARATOR);
        builder.append(getRootPath());
        builder.append(PATH_SEPARATOR);
        builder.append(PUBLICATION);
        if (StringUtils.isNotBlank(path)) {
            if (!path.startsWith(PATH_SEPARATOR)) {
                builder.append(PATH_SEPARATOR);
            }
            builder.append(path);

            if (!path.endsWith(PATH_SEPARATOR)) {
                builder.append(PATH_SEPARATOR);
            }
            if (StringUtils.isNotBlank(publicationName)) {
                builder.append(publicationName);
            }

        }
        return builder.toString();
    }

    /** {@inheritDoc} */
    public String generatePublicationPath(final String prefixPath, final String publicationName, final String suffixPath) {
        StringBuilder builder = new StringBuilder(PATH_SEPARATOR);
        builder.append(getRootPath());
        builder.append(PATH_SEPARATOR);
        builder.append(PUBLICATION);
        if (StringUtils.isNotBlank(prefixPath)) {
            if (!prefixPath.startsWith(PATH_SEPARATOR)) {
                builder.append(PATH_SEPARATOR);
            }
            builder.append(prefixPath);

            if (!prefixPath.endsWith(PATH_SEPARATOR)) {
                builder.append(PATH_SEPARATOR);
            }
            if (StringUtils.isNotBlank(publicationName)) {
                builder.append(publicationName);
            }
            if (StringUtils.isNotBlank(suffixPath)) {
                if (!suffixPath.startsWith(PATH_SEPARATOR)) {
                    builder.append(PATH_SEPARATOR);
                }
                builder.append(suffixPath);
            }

        }
        return builder.toString();
    }

    /** {@inheritDoc} */
    public String generateSitePath(final String path, final Site siteName) {
        StringBuilder builder = new StringBuilder(PATH_SEPARATOR);
        builder.append(getRootPath());
        builder.append(PATH_SEPARATOR);
        builder.append(SITE);
        if (StringUtils.isNotBlank(path)) {
            if (!path.startsWith(PATH_SEPARATOR)) {
                builder.append(PATH_SEPARATOR);
            }
            builder.append(path);

            if (!path.endsWith(PATH_SEPARATOR)) {
                builder.append(PATH_SEPARATOR);
            }
            if (null != siteName) {
                builder.append(siteName.getSiteString());
            }

        }
        return builder.toString();
    }

    /** {@inheritDoc} */
    public String generateSitePath(final String prefixPath, final Site siteName, final String suffixPath) {
        StringBuilder builder = new StringBuilder(PATH_SEPARATOR);
        builder.append(getRootPath());
        builder.append(PATH_SEPARATOR);
        builder.append(SITE);
        if (StringUtils.isNotBlank(prefixPath)) {
            if (!prefixPath.startsWith(PATH_SEPARATOR)) {
                builder.append(PATH_SEPARATOR);
            }
            builder.append(prefixPath);

            if (!prefixPath.endsWith(PATH_SEPARATOR)) {
                builder.append(PATH_SEPARATOR);
            }
            if (null != siteName) {
                builder.append(siteName.getSiteString());
            }
            if (StringUtils.isNotBlank(suffixPath)) {
                if (!suffixPath.startsWith(PATH_SEPARATOR)) {
                    builder.append(PATH_SEPARATOR);
                }
                builder.append(suffixPath);
            }

        }
        return builder.toString();
    }

    /** {@inheritDoc} */
    public String generateLocalePath(final String path, final Locale localeName) {
        StringBuilder builder = new StringBuilder(PATH_SEPARATOR);
        builder.append(getRootPath());
        builder.append(PATH_SEPARATOR);
        builder.append(LOCALE);
        if (StringUtils.isNotBlank(path)) {
            if (!path.startsWith(PATH_SEPARATOR)) {
                builder.append(PATH_SEPARATOR);
            }
            builder.append(path);

            if (!path.endsWith(PATH_SEPARATOR)) {
                builder.append(PATH_SEPARATOR);
            }
            if (null != localeName) {
                builder.append(localeName.getLocaleString());
            }

        }
        return builder.toString();
    }

    /** {@inheritDoc} */
    public String generateLocalePath(final String prefixPath, final Locale localeName, final String suffixPath) {
        StringBuilder builder = new StringBuilder(PATH_SEPARATOR);
        builder.append(getRootPath());
        builder.append(PATH_SEPARATOR);
        builder.append(LOCALE);
        if (StringUtils.isNotBlank(prefixPath)) {
            if (!prefixPath.startsWith(PATH_SEPARATOR)) {
                builder.append(PATH_SEPARATOR);
            }
            builder.append(prefixPath);

            if (!prefixPath.endsWith(PATH_SEPARATOR)) {
                builder.append(PATH_SEPARATOR);
            }
            if (null != localeName) {
                builder.append(localeName.getLocaleString());
            }
            if (StringUtils.isNotBlank(suffixPath)) {
                if (!suffixPath.startsWith(PATH_SEPARATOR)) {
                    builder.append(PATH_SEPARATOR);
                }
                builder.append(suffixPath);
            }

        }
        return builder.toString();
    }

    /**
     * The method returns ROOT Path as specific implementation.
     * @return String.
     */
    public abstract String getRootPath();

}
