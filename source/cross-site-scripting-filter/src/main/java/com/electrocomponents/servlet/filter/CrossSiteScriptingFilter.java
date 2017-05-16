/*
 * $Revision$
 * $Date$
 *
 * Copyright (c) 2007 O'Reilly Media.  All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you
 * may not use this file except in compliance with the License. You may
 * obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package com.electrocomponents.servlet.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Based on BadInputValve that was written by Jason Brittain<br/>
 * Filters out bad user input from HTTP requests to avoid malicious attacks including Cross Site Scripting (XSS), SQL Injection, and HTML
 * Injection vulnerabilities, among others.
 * </br>
 * Converted into Servlet Filter for RS Usage</br> 
 * 1) Revised hashmaps to hold Pattern loaded at init() rather than compiling each pattern everytime!</br>  
 * 2) Added HttpServletWrapper implementation to wrap the original request whenever a parameter name/value has been deemed XSS content, this wrapper is then passed down the servlet chain.</br>
 * 
 * @see <code>com.electrocomponents.servlet.filter.ModifiableParamRequestWrapper</code> for further explanation.
 * 
 */
public class CrossSiteScriptingFilter implements Filter {
    //Filter Init Parameter
    private static final String FILTER_INIT_PARAM_ESCAPE_JAVA_SCRIPT = "escapeJavaScript";

    //Filter Init Parameter
    private static final String FILTER_INIT_PARAM_ESCAPE_ANGLE_BRACKETS = "escapeAngleBrackets";

    //Filter Init Parameter
    private static final String FILTER_INIT_PARAM_ESCAPE_QUOTES = "escapeQuotes";

    //Filter Init Parameter
    private static final String FILTER_INIT_PARAM_DENY = "deny";

    //Filter Init Parameter
    private static final String FILTER_INIT_PARAM_ALLOW = "allow";


    /**
     * Logger.
     */
    private static final Log LOG = LogFactory.getLog(CrossSiteScriptingFilter.class);

 
    /**
     * The flag that determines whether or not to escape quotes that are part of the request.
     */
    protected boolean escapeQuotes = false;

    /**
     * The flag that determines whether or not to escape angle brackets that are part of the request.
     */
    protected boolean escapeAngleBrackets = false;

    /**
     * The flag that determines whether or not to escape JavaScript function and object names that are part of the request.
     */
    protected boolean escapeJavaScript = false;

    /**
     * A substitution mapping (regular expression to match, replacement)
     * that is used to replace single quotes ('), double quotes ("), and
     * backticks (`) with escaped equivalents that can't be used for malicious
     * purposes.
     */
    protected HashMap<Pattern, String> quotesHashMap = new HashMap<>();

    /**
     * A substitution mapping (regular expression to match, replacement)
     * that is used to replace angle brackets (<>) with escaped
     * equivalents that can't be used for malicious purposes.
     */
    protected HashMap<Pattern, String> angleBracketsHashMap = new HashMap<>();

    /**
     * A substitution mapping (regular expression to match, replacement)
     * that is used to replace potentially dangerous Javascript function
     * calls with escaped equivalents that can't be used for malicious
     * purposes.
     */
    protected HashMap<Pattern, String> javascriptHashMap = new HashMap<>();

    /**
     * The comma-delimited set of <code>allow</code> expressions.
     */
    protected String allow = null;

    /**
     * The set of <code>allow</code> regular expressions we will evaluate.
     */
    protected Pattern allows[] = new Pattern[0];

    /**
     * The set of <code>deny</code> regular expressions we will evaluate.
     */
    protected Pattern denies[] = new Pattern[0];

    /**
     * The comma-delimited set of <code>deny</code> expressions.
     */
    protected String deny = null;

    
    /**
     * A Map of regular expressions used to filter the parameters.  The key
     * is the regular expression String to search for, and the value is the
     * regular expression String used to modify the parameter if the search
     * String is found.
     */
    protected HashMap<Pattern, String> parameterEscapes = new HashMap<>();

    /**
     * Gets the flag which determines whether this Valve will escape any quotes (both double and single quotes) that are part of the
     * request, before the request is performed.
     * @return boolean
     */
    protected boolean getEscapeQuotes() {

        return escapeQuotes;

    }


    /**
     * Gets the flag which determines whether this Valve will escape any angle brackets that are part of the request, before the request is
     * performed.
     * @return boolean
     */
    protected boolean getEscapeAngleBrackets() {

        return escapeAngleBrackets;

    }

    /**
     * Gets the flag which determines whether this Valve will escape any potentially dangerous references to JavaScript functions and
     * objects that are part of the request, before the request is performed.
     * @return boolean
     */
    protected boolean getEscapeJavaScript() {

        return escapeJavaScript;

    }


    
    
    /**
     * Sets the flag which determines whether this Filter will escape
     * any quotes (both double and single quotes) that are part of the
     * request, before the request is performed.
     *
     * @param escapeQuotes
     */
    protected void setEscapeQuotes(boolean escapeQuotes) {
        this.escapeQuotes = escapeQuotes;
        LOG.info("CrossSiteScriptingFilter escapeQuotes input : " + escapeQuotes);
        if (escapeQuotes) {
            // Escape all quotes.
            parameterEscapes.putAll(quotesHashMap);
        }
    }


    /**
     * Sets the flag which determines whether this Filter will escape
     * any angle brackets that are part of the request, before the
     * request is performed.
     *
     * @param escapeAngleBrackets
     */
    protected void setEscapeAngleBrackets(boolean escapeAngleBrackets) {
        this.escapeAngleBrackets = escapeAngleBrackets;
        LOG.info("CrossSiteScriptingFilter escapeAngleBrackets input : " + escapeAngleBrackets);
        if (escapeAngleBrackets) {
            // Escape all angle brackets.
            parameterEscapes.putAll(angleBracketsHashMap);
        }
    }


    /**
     * Sets the flag which determines whether this Filter will escape
     * any potentially dangerous references to Javascript functions
     * and objects that are part of the request, before the request is
     * performed.
     *
     * @param escapeJavaScript
     */
    protected void setEscapeJavaScript(boolean escapeJavaScript) {
        this.escapeJavaScript = escapeJavaScript;
        LOG.info("CrossSiteScriptingFilter escapeJavaScript input : " + escapeJavaScript);
        if (escapeJavaScript) {
            // Escape potentially dangerous Javascript method calls.
            parameterEscapes.putAll(javascriptHashMap);
        }
    }

    /**
     * Set the comma-delimited set of the <code>allow</code> expressions
     * configured for this Filter, if any.
     *
     * @param allow The new set of allow expressions
     */
    protected void setAllow(String allow) {
        this.allow = allow;
        LOG.info("CrossSiteScriptingFilter allow input : " + allow);
        allows = precalculate(allow);
    }


    /**
     * Set the comma-delimited set of the <code>deny</code> expressions
     * configured for this Filter, if any.
     *
     * @param deny The new set of deny expressions
     */
    protected void setDeny(String deny) {
        this.deny = deny;
        LOG.info("CrossSiteScriptingFilter deny input : " + deny);
        denies = precalculate(deny);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Populate the regex escape maps.
        LOG.info("CrossSiteScriptingFilter initialising...");
        quotesHashMap.put(Pattern.compile("\""), "&quot;");
        quotesHashMap.put(Pattern.compile("\'"), "&#39;");
        quotesHashMap.put(Pattern.compile("`"), "&#96;");
        angleBracketsHashMap.put(Pattern.compile("<"), "&lt;");
        angleBracketsHashMap.put(Pattern.compile(">"), "&gt;");
        javascriptHashMap.put(Pattern.compile("document(.*)\\.(.*)cookie"), "document&#46;&#99;ookie");
        javascriptHashMap.put(Pattern.compile("eval(\\s*)\\("), "eval&#40;");
        javascriptHashMap.put(Pattern.compile("setTimeout(\\s*)\\("), "setTimeout$1&#40;");
        javascriptHashMap.put(Pattern.compile("setInterval(\\s*)\\("), "setInterval$1&#40;");
        javascriptHashMap.put(Pattern.compile("execScript(\\s*)\\("), "exexScript$1&#40;");
        javascriptHashMap.put(Pattern.compile("(?i)javascript(?-i):"), "javascript&#58;");
               
        LOG.info("CrossSiteScriptingFilter loaded script patterns.");

        // Parse the Filter's init parameters.
        setAllow(filterConfig.getInitParameter(FILTER_INIT_PARAM_ALLOW));
        setDeny(filterConfig.getInitParameter(FILTER_INIT_PARAM_DENY));
        String initParam = filterConfig.getInitParameter(FILTER_INIT_PARAM_ESCAPE_QUOTES);
        if (initParam != null) {
            setEscapeQuotes(Boolean.parseBoolean(initParam));
        }
        initParam = filterConfig.getInitParameter(FILTER_INIT_PARAM_ESCAPE_ANGLE_BRACKETS);
        if (initParam != null) {
            setEscapeAngleBrackets(Boolean.parseBoolean(initParam));
        }
        initParam = filterConfig.getInitParameter(FILTER_INIT_PARAM_ESCAPE_JAVA_SCRIPT);
        if (initParam != null) {
            setEscapeJavaScript(Boolean.parseBoolean(initParam));
        }

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
    
        // Skip filtering for non-HTTP requests and responses.
        if (!(request instanceof HttpServletRequest) || !(response instanceof HttpServletResponse)) {
            filterChain.doFilter(request, response);
            return;
        }

        // Only let requests through based on the allows and denies.
        if (processAllowsAndDenies(request, response)) {

            // Filter the input for potentially dangerous JavaScript
            // code so that bad user input is cleaned out of the request         
            ModifiableParamRequestWrapper wrappedRequest = new ModifiableParamRequestWrapper((HttpServletRequest)request);
            boolean XSSDetected = filterParametersAndReturnXSSDetectedStatus(wrappedRequest);
            
            // Perform the request, only use the wrapper request if XSS has been detected to limit reduce as much 'noise' as possible.
            if (XSSDetected){
                filterChain.doFilter(wrappedRequest, response);
            }else{
                filterChain.doFilter(request, response);                
            }
        }
    }
    
    /**
     * Return a text representation of this object.
     */
    @Override
    public String toString() {
        return "CrossSiteScriptingFilter";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void destroy() {
    }
 
    
    
    /**
     * Uses the functionality of the (abstract) RequestFilterValve to stop requests that contain forbidden string patterns in parameter
     * names and parameter values.
     * @param request The servlet request to be processed
     * @param response The servlet response to be created
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet error occurs
     * @return false if the request is forbidden, true otherwise.
     */
    protected boolean processAllowsAndDenies(final ServletRequest request, final ServletResponse response) throws IOException, ServletException {

        Map<String,String[]> paramMap = request.getParameterMap();
        // Loop through the list of parameters.
        Iterator<String> y = paramMap.keySet().iterator();
        while (y.hasNext()) {
            String name = y.next();
            String[] values = request.getParameterValues(name);

            // See if the name contains a forbidden pattern.
            if (!checkAllowsAndDenies(name, response)) {
                return false;
            }

            // Check the parameter's values for the pattern.
            if (values != null) {
                int valuesLength = values.length;
                for (int i = 0; i < valuesLength; i++) {
                    String value = values[i];
                    if (!checkAllowsAndDenies(value, response)) {
                        return false;
                    }
                }
            }
        }

        // No parameter caused a deny. The request should continue.
        return true;

    }

    /**
     * Perform the filtering that has been configured for this filter, matching against the specified request property. If the request is
     * allowed to proceed, this method returns true. Otherwise, this method sends a Forbidden error response page, and returns false. <br>
     * <br>
     * This method borrows heavily from RequestFilterValve.process( ), only this method has a boolean return type and doesn't call getNext(
     * ).invoke(request, response).
     * @param property The request property on which to filter
     * @param response The servlet response to be processed
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet error occurs
     * @return true if the request is still allowed to proceed.
     */
    protected boolean checkAllowsAndDenies(final String property, final ServletResponse response) throws IOException, ServletException {

        // If there were no denies and no allows, process the request.
        if (denies.length == 0 && allows.length == 0) {
            return true;
        }

        // Check the deny patterns, if any
        int deniesLength = denies.length;
        for (int i = 0; i < deniesLength; i++) {
            Matcher m = denies[i].matcher(property);
            if (m.find()) {
                if (response instanceof HttpServletResponse) {
                    ((HttpServletResponse)response).sendError(HttpServletResponse.SC_FORBIDDEN);
                    return false;
                }
            }
        }

        // Check the allow patterns, if any
        int allowsLength = allows.length;
        for (int i = 0; i < allowsLength; i++) {
            Matcher m = allows[i].matcher(property);
            if (m.find()) {
                return true;
            }
        }

        // Allow if denies specified but not allows
        if (denies.length > 0 && allows.length == 0) {
            return true;
        }

        // Otherwise, deny the request.
        if (response instanceof HttpServletResponse) {
            ((HttpServletResponse)response).sendError(HttpServletResponse.SC_FORBIDDEN);
        }
        return false;

    }

    /**
     * Filters all existing parameters for potentially dangerous content, and escapes any if they are found and add it to the wrapper's modifiable parameter map. 
     * @param request The Request that contains the parameters.
     * @return true if at least one parameter name or value has been filtered. 
     */
    protected boolean filterParametersAndReturnXSSDetectedStatus(final ModifiableParamRequestWrapper request) {
        //parameters from the ModifiableParamRequestWrapper and those from its wrapped request 
        Map<String, String[]> paramMap = request.getParameterMap();
        
        boolean XSSValuesDetected = false;
        // Loop through each of the substitution patterns.
        Iterator<Pattern> escapesIterator = parameterEscapes.keySet().iterator();
        while (escapesIterator.hasNext()) {
            Pattern pattern = escapesIterator.next();

            // Loop through the list of parameters.
            Enumeration<String> paramNames = request.getParameterNames();
            while(paramNames.hasMoreElements()){
                String name = paramNames.nextElement();
                String[] values = paramMap.get(name);
                // See if the name contains the pattern.
                boolean nameMatch;
                Matcher matcher = pattern.matcher(name);
                nameMatch = matcher.find();
                if (nameMatch) {
                    // The parameter's name matched a pattern, so we
                    // fix it by modifying the name, adding the parameter
                    // back as the new name.
                    // Add this back to the modifiable parameter map. Methods to retrieve parameters via the ModifiableParamRequestWrapper will always return values from the modifiable parameter map first. 
                    String newName = matcher.replaceAll((String) parameterEscapes.get(pattern));
                    request.getModifiableParameters().put(newName, values);
                    LOG.warn("Parameter name " + name + " matched pattern \"" + (String) parameterEscapes.get(pattern) + "\".  Remote addr: " + ((HttpServletRequest) request).getRemoteAddr());
                }
                // Check the parameter's values for the pattern.
                if (values != null) {
                    boolean atLeastOneMatch = false;
                    int valuesLength = values.length;
                    for (int j = 0; j < valuesLength; j++) {
                        String value = values[j];
                        boolean valueMatch;
                        matcher = pattern.matcher(value);
                        valueMatch = matcher.find();
                        if (valueMatch) {
                            // The value matched, so we modify the value
                            // and then set it back into the array.
                            values[j] = matcher.replaceAll((String) parameterEscapes.get(pattern));;
                            atLeastOneMatch = true;
                            XSSValuesDetected = true;
                            LOG.warn("Parameter \"" + name + "\"'s value \"" + value + "\" matched pattern \"" + (String) parameterEscapes.get(pattern)
                                    + "\".  Remote addr: " + ((HttpServletRequest) request).getRemoteAddr());
                        }
                    }
                    if (atLeastOneMatch){
                        //one match occurred, add this back to the modifiable parameter map. Methods to retrieve parameters via the ModifiableParamRequestWrapper will always return values from the modifiable parameter map first. 
                        LOG.warn("Parameter \"" + name + " matched detected, adding new value back to wrapped parameter map");
                        request.getModifiableParameters().put(name, values);
                    }
                }
            }
        }
        return XSSValuesDetected;
    }
    


    /**
     * Based on precalculate method from tomcat's RequestFilterValue. 
     * Return an array of regular expression objects initialised from the
     * specified argument, which must be <code>null</code> or a
     * comma-delimited list of regular expression patterns.
     *
     * @param list The comma-separated list of patterns
     *
     * @exception IllegalArgumentException if one of the patterns has
     *  invalid syntax
     */
    protected Pattern[] precalculate(String list) {

        if (list == null)
            return (new Pattern[0]);
        list = list.trim();
        if (list.length() < 1)
            return (new Pattern[0]);
        list += ",";

        ArrayList<Pattern> reList = new ArrayList<Pattern>();
        while (list.length() > 0) {
            int comma = list.indexOf(',');
            if (comma < 0)
                break;
            String pattern = list.substring(0, comma).trim();
            try {
                reList.add(Pattern.compile(pattern));
            } catch (PatternSyntaxException e) {
                IllegalArgumentException iae = new IllegalArgumentException("Syntax error in request filter pattern" + pattern);
                iae.initCause(e);
                throw iae;
            }
            list = list.substring(comma + 1);
        }

        Pattern reArray[] = new Pattern[reList.size()];
        return ((Pattern[]) reList.toArray(reArray));
    }


}
