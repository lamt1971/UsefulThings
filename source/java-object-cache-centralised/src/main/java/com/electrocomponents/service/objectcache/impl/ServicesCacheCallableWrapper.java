package com.electrocomponents.service.objectcache.impl;

import java.util.concurrent.Callable;

import com.electrocomponents.model.domain.Locale;


/**
 * <pre>
 * ************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author  : Ganesh Raut
 * Created : 3 May 2012 at 12:29:31
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
 * @author Ganesh Raut
 */
public class ServicesCacheCallableWrapper implements Callable {

    /** serialVersionUID. */
    private static final long serialVersionUID = 8561803863823839699L;

    /** The callable request. */
    private Callable callable;

    /** The locale. */
    private Locale locale;

    /**
     * Constructor.
     * @param callable the callable
     * @param locale the locale
     */
    public ServicesCacheCallableWrapper(final Callable callable, final Locale locale) {
        this.callable = callable;
        this.locale = locale;
    }

    /**
     * @return the callable
     */
    public Callable getCallable() {
        return callable;
    }

    /**
     * @param callable the callable to set
     */
    public void setCallable(final Callable callable) {
        this.callable = callable;
    }

    /**
     * @return the locale
     */
    public Locale getLocale() {
        return locale;
    }

    /**
     * @param locale the locale to set
     */
    public void setLocale(final Locale locale) {
        this.locale = locale;
    }

    /**
     * A dummy call method.
     * @return nothing.
     * @throws Exception the Exception
     */
    public Object call() throws Exception {
        return null;
    }
}
