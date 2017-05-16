

package com.electrocomponents.servlet.filter;

import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;


/**
 * Based on <code>HttpRewriteWrappedRequest</code> that was written by Lincoln Baxter, III, author of the org.ocpsoft.rewrite rewrite-servlet
 * 
 * Wraps an <code>HttpServletRequest</code> so that parameters can be modified by a filter. 
 * Web containers like Tomcat have to adhere to Servlet Spec, especially when processing forwards & includes. This is achieved by the container implementation using http request wrappers.<br/>
 * The gotcha here is that the container will add a wrapper when it does an include or a forward to change the behaviour as the spec states. BUT it CANNOT add it at the front of the request wrappers. Instead, it adds it behind any custom wrappers. That is, behind all wrappers that the container has not put there.</br> 
 * </br>
 * For example a request without forward</br>
 * 1) catalina.RequestFacade</br>
 * 2) customRequestWrapperOne</br>
 * 3) customRequestWrapperTwo</br></br>
 *  
 * For example a request WITH forward</br>
 * 1) catalina.RequestFacade</br>
 * 2) catalina.DispatchWrapper***</br>
 * 3) customRequestWrapperOne</br>
 * 4) customRequestWrapperTwo</br>
 * <br/>
 * *** The catalina.DispatchWrapper changes the path methods (request uri, servlet path, query string etc) and adds parameters and attributes</br>
 * </br>
 * With this in mind, this custom HttpServletRequestWrapper will return parameters firstly from itself if they exist otherwise proxy to the wrapped Request. 
 * This will ensure that forwards and includes work correctly. 
 */
public class ModifiableParamRequestWrapper extends HttpServletRequestWrapper
  {
    private Map<String, String[]> modifiableParameters = null;
    private CompositeMap<String, String[]> allParameters;

    /**
     * Construct a wrapper for the original request.
     * 
     * @param request the original HttpServletRequest
     */
    public ModifiableParamRequestWrapper(HttpServletRequest request)
      {
        super(request);
        modifiableParameters = new TreeMap<String, String[]>();
      }


    @Override
    @SuppressWarnings("unchecked")
    public Map<String, String[]> getParameterMap()
    {
       allParameters = new CompositeMap<String, String[]>()
                .addDelegate(modifiableParameters)
                .addDelegate(super.getParameterMap());
       return Collections.unmodifiableMap(allParameters);
    }

    public Map<String, String[]> getModifiableParameters()
    {
       return modifiableParameters;
    }

    /*
     * HttpServletRequest overrides
     */

    @Override
    public String getParameter(final String name)
    {
       String[] strings = getParameterMap().get(name);
       if (strings != null && strings.length > 0)
       {
          return strings[0];
       }
       return super.getParameter(name);
    }

    @Override
    public Enumeration<String> getParameterNames()
    {
       return Collections.enumeration(getParameterMap().keySet());
    }

    @Override
    public String[] getParameterValues(final String name)
    {
       return getParameterMap().get(name);
    }
  }
