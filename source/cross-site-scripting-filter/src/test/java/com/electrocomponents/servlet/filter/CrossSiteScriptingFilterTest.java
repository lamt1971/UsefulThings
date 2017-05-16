package com.electrocomponents.servlet.filter;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class CrossSiteScriptingFilterTest {

    private HttpServletRequest mockRequest = null;
    private HttpServletResponse mockResponse = null;
    private FilterChain mockFilterChain = null;
    private CrossSiteScriptingFilter filter = null;
    private Map<String, String[]> requestParameterMap;
    private FilterConfig mockFilterConfig;

    private String FORMFIELD1 = "formField1";
    private String FORMFIELD2 = "formField2";
    private String FORMFIELD3 = "formField3";
    private String FORMFIELD4 = "formField4";
    private String FORMFIELD5 = "formField5";

    private String FORMFIELD1_VALUES[] = new String[] { "formField1Content" };
    private String FORMFIELD2_VALUES[] = new String[] { "formField2Content1", "formField2Content2", "formField2Content3" };
    private String FORMFIELD3_VALUES[] = null;
    private String FORMFIELD4_VALUES[] = new String[] { "formField4Content" };
    private String FORMFIELD5_VALUES[] = new String[] { "formField5'formField5'formField5" };

    @Before
    public void setUp() throws Exception {
        mockRequest = Mockito.mock(HttpServletRequest.class);
        Mockito.when(mockRequest.getRemoteAddr()).thenReturn("127.0.0.1");

        mockFilterConfig = Mockito.mock(FilterConfig.class);

        mockResponse = Mockito.mock(HttpServletResponse.class);        
        Mockito.doNothing().when(mockResponse).sendError(Mockito.anyInt());
        
        filter = new CrossSiteScriptingFilter();

    }

    /*
     * set up standard filter config
     */
    private void setupFilterConfig(){
        Mockito.when(mockFilterConfig.getInitParameter("allow")).thenReturn("");
        Mockito.when(mockFilterConfig.getInitParameter("deny")).thenReturn("\\x00,\\x04,\\x08");
        Mockito.when(mockFilterConfig.getInitParameter("escapeAngleBrackets")).thenReturn("true");
        Mockito.when(mockFilterConfig.getInitParameter("escapeJavaScript")).thenReturn("true");
        Mockito.when(mockFilterConfig.getInitParameter("escapeQuotes")).thenReturn("true");        
    }

    
    /**
     * set up standard request parameters 
     */
    private void setupRequestParameterMap(){
        // set up wrapped httpRequest parameterMap with sets of key/value pairs
        requestParameterMap = new HashMap<>();
        requestParameterMap.put(FORMFIELD1, FORMFIELD1_VALUES);
        requestParameterMap.put(FORMFIELD2, FORMFIELD2_VALUES);
        requestParameterMap.put(FORMFIELD3, FORMFIELD3_VALUES);
        requestParameterMap.put(FORMFIELD4, FORMFIELD4_VALUES);
        Mockito.when(mockRequest.getParameterMap()).thenReturn(requestParameterMap);        
        Mockito.when(mockRequest.getParameterValues(Mockito.anyString())).thenAnswer(new Answer<String[]>() {
            @Override
            public String[] answer(InvocationOnMock invocation) throws Throwable {
                return requestParameterMap.get(invocation.getArgumentAt(0, String.class));
            }            
        });        
        
    }

    /**
     * set up request parameters using known XSS input values. 
     * Parameter Value are single inputs. 
     */
    private void setupXSSRequestParameterMap(){
        // set up wrapped httpRequest parameterMap with sets of key/value pairs
        requestParameterMap = new HashMap<>();
        KnownXSSTestData[] testInputs = KnownXSSTestData.values();
        for (KnownXSSTestData input : testInputs) {
            requestParameterMap.put(input.name(), new String[]{input.getInput()});            
        }
        
        Mockito.when(mockRequest.getParameterMap()).thenReturn(requestParameterMap);        
        Mockito.when(mockRequest.getParameterValues(Mockito.anyString())).thenAnswer(new Answer<String[]>() {
            @Override
            public String[] answer(InvocationOnMock invocation) throws Throwable {
                return requestParameterMap.get(invocation.getArgumentAt(0, String.class));
            }            
        });        
        
    }

    /**
     * set up request parameters using mixtures of safe inputs and known XSS inputs.
     * Parameter Values are mixtures of single entry and mutliple entries
     */
    private void setupXSSRequestParameterMapValues(){
        // set up wrapped httpRequest parameterMap with sets of key/value pairs
        requestParameterMap = new HashMap<>();
        requestParameterMap.put(FORMFIELD1, FORMFIELD1_VALUES);
        requestParameterMap.put(FORMFIELD2, FORMFIELD2_VALUES);
        requestParameterMap.put(FORMFIELD3, new String[]{"formField1Content", KnownXSSTestData.LOCATOR.getInput(), KnownXSSTestData.NO_FILTER_INVASION.getInput()});
        requestParameterMap.put(FORMFIELD4, new String[]{KnownXSSTestData.LOCATOR.getInput(), KnownXSSTestData.NO_FILTER_INVASION.getInput(), "formField4Content3",  "formField4Content4"});
        Mockito.when(mockRequest.getParameterMap()).thenReturn(requestParameterMap);        
        Mockito.when(mockRequest.getParameterValues(Mockito.anyString())).thenAnswer(new Answer<String[]>() {
            @Override
            public String[] answer(InvocationOnMock invocation) throws Throwable {
                return requestParameterMap.get(invocation.getArgumentAt(0, String.class));
            }            
        });        
        
    }
    

    /**
     * set up request parameters with names and values using mixtures of safe inputs and known XSS inputs.
     * Parameter Values are mixtures of single entry and mutliple entries
     */
    private void setupRequestParameterMapWithXSSNamesAndValues(){
        // set up wrapped httpRequest parameterMap with sets of key/value pairs
        requestParameterMap = new HashMap<>();
        requestParameterMap.put(FORMFIELD1, FORMFIELD1_VALUES);
        requestParameterMap.put(FORMFIELD2, FORMFIELD2_VALUES);
        requestParameterMap.put(FORMFIELD3, new String[]{"formField1Content", KnownXSSTestData.LOCATOR.getInput(), KnownXSSTestData.NO_FILTER_INVASION.getInput()});
        requestParameterMap.put(FORMFIELD4, new String[]{KnownXSSTestData.LOCATOR.getInput(), KnownXSSTestData.NO_FILTER_INVASION.getInput(), "formField4Content3",  "formField4Content4"});
        requestParameterMap.put("<" + FORMFIELD1, FORMFIELD1_VALUES);
        requestParameterMap.put(FORMFIELD1 + ">", FORMFIELD1_VALUES);
        Mockito.when(mockRequest.getParameterMap()).thenReturn(requestParameterMap);        
        Mockito.when(mockRequest.getParameterValues(Mockito.anyString())).thenAnswer(new Answer<String[]>() {
            @Override
            public String[] answer(InvocationOnMock invocation) throws Throwable {
                return requestParameterMap.get(invocation.getArgumentAt(0, String.class));
            }            
        });        
        
    }

    /**
     * Test filter init when no regex given for deny and allow.
     */
    @Test
    public void testFilterNullRegexInitialisation() {
        try {
            
            mockFilterConfig = Mockito.mock(FilterConfig.class);
            Mockito.when(mockFilterConfig.getInitParameter("allow")).thenReturn(null);
            Mockito.when(mockFilterConfig.getInitParameter("deny")).thenReturn(null);
            Mockito.when(mockFilterConfig.getInitParameter("escapeAngleBrackets")).thenReturn("false");
            Mockito.when(mockFilterConfig.getInitParameter("escapeJavaScript")).thenReturn("false");
            Mockito.when(mockFilterConfig.getInitParameter("escapeQuotes")).thenReturn("false");
            filter.init(mockFilterConfig);

            Assert.assertFalse(filter.escapeAngleBrackets);
            Assert.assertFalse(filter.escapeJavaScript);
            Assert.assertFalse(filter.escapeQuotes);
            Assert.assertTrue(filter.allows.length == 0);
            Assert.assertTrue(filter.denies.length == 0);
        } catch (Exception e) {
            Assert.fail("Unexpected error!!" + e.getMessage());
        }

    }

    /**
     * Test filter init when only single deny is specified.
     */
    @Test
    public void testFilterNullAndSingleRegexInitialisation() {
        try {
            
            mockFilterConfig = Mockito.mock(FilterConfig.class);
            Mockito.when(mockFilterConfig.getInitParameter("allow")).thenReturn(null);
            Mockito.when(mockFilterConfig.getInitParameter("deny")).thenReturn("\\x00");
            Mockito.when(mockFilterConfig.getInitParameter("escapeAngleBrackets")).thenReturn("false");
            Mockito.when(mockFilterConfig.getInitParameter("escapeJavaScript")).thenReturn("false");
            Mockito.when(mockFilterConfig.getInitParameter("escapeQuotes")).thenReturn("false");
            filter.init(mockFilterConfig);

            Assert.assertFalse(filter.escapeAngleBrackets);
            Assert.assertFalse(filter.escapeJavaScript);
            Assert.assertFalse(filter.escapeQuotes);
            Assert.assertTrue(filter.allows.length == 0);
            Assert.assertTrue(filter.denies.length == 1);
        } catch (Exception e) {
            Assert.fail("Unexpected error!!" + e.getMessage());
        }

    }

    /**
     * Test that invalid regex patterns throws error
     */
    @Test(expected = IllegalArgumentException.class) 
    public void testFilterInvalidRegexInitialisation() {
        try {
            mockFilterConfig = Mockito.mock(FilterConfig.class);
            Mockito.when(mockFilterConfig.getInitParameter("allow")).thenReturn(null);
            Mockito.when(mockFilterConfig.getInitParameter("deny")).thenReturn("[^^$//\\/]][");
            Mockito.when(mockFilterConfig.getInitParameter("escapeAngleBrackets")).thenReturn("false");
            Mockito.when(mockFilterConfig.getInitParameter("escapeJavaScript")).thenReturn("false");
            Mockito.when(mockFilterConfig.getInitParameter("escapeQuotes")).thenReturn("false");
            filter.init(mockFilterConfig);
        } catch (ServletException e) {
            Assert.fail("Unexpected error!!" + e.getMessage());
        }
    }

    /**
     * Test filter init with real world configuration
     */
    @Test
    public void testFilterRealWorldInitialisation() {
        try {
            setupFilterConfig();
            filter.init(mockFilterConfig);

            Assert.assertTrue(filter.escapeAngleBrackets);
            Assert.assertTrue(filter.escapeJavaScript);
            Assert.assertTrue(filter.escapeQuotes);
            Assert.assertTrue(filter.allows.length == 0);
            Assert.assertTrue(filter.denies.length == 3);
        } catch (Exception e) {
            Assert.fail("Unexpected error!!" + e.getMessage());
        }

    }

    /**
     * Test Deny routine allows safe inputs through
     */
    @Test
    public void testCheckDenyAcceptsNoneThreateningCharacters() {
        try {
            setupFilterConfig();
            
            filter.init(mockFilterConfig);
            
            Assert.assertTrue(filter.checkAllowsAndDenies("aValidPropertyName", mockResponse));
            Assert.assertTrue(filter.checkAllowsAndDenies("111111", mockResponse));
            Assert.assertTrue(filter.checkAllowsAndDenies("", mockResponse));
            Assert.assertTrue(filter.checkAllowsAndDenies("   ", mockResponse));
            Assert.assertTrue(filter.checkAllowsAndDenies("!\"£$%^&*()_+}{POIUYTREWQ~@:LKJHGFDSA?><MNBVCXZ|", mockResponse));
            Assert.assertTrue(filter.checkAllowsAndDenies("\uD83D\uDE05", mockResponse));
        } catch (Exception e) {
            Assert.fail("Unexpected error1!!" + e.getMessage());
        }

    }

    /**
     * Test Deny routine rejects unsafe null characters
     */
    @Test
    public void testCheckDenyRejectsNullCharacters() {
        try {
            setupFilterConfig();
            
            filter.init(mockFilterConfig);
            
            Assert.assertFalse(filter.checkAllowsAndDenies("\u0000", mockResponse));
            Assert.assertFalse(filter.checkAllowsAndDenies(" \u0000", mockResponse));
            Assert.assertFalse(filter.checkAllowsAndDenies(" \u0000 ", mockResponse));
            Assert.assertFalse(filter.checkAllowsAndDenies("aaa\u0000", mockResponse));
            Assert.assertFalse(filter.checkAllowsAndDenies("aaa\u0000aaa", mockResponse));
            Assert.assertFalse(filter.checkAllowsAndDenies("\u0000aaa", mockResponse));
        } catch (Exception e) {
            Assert.fail("Unexpected error1!!" + e.getMessage());
        }

    }

    /**
     * Test Deny routine rejects unsafe End of Transmission characters
     */
    @Test
    public void testCheckDenyRejectsEOTCharacters() {
        try {
            setupFilterConfig();

            filter.init(mockFilterConfig);
            
            Assert.assertFalse(filter.checkAllowsAndDenies("\u0004", mockResponse));
            Assert.assertFalse(filter.checkAllowsAndDenies(" \u0004", mockResponse));
            Assert.assertFalse(filter.checkAllowsAndDenies(" \u0004 ", mockResponse));
            Assert.assertFalse(filter.checkAllowsAndDenies("aaa\u0004", mockResponse));
            Assert.assertFalse(filter.checkAllowsAndDenies("aaa\u0004aaa", mockResponse));
            Assert.assertFalse(filter.checkAllowsAndDenies("\u0004aaa", mockResponse));                        
        } catch (Exception e) {
            Assert.fail("Unexpected error1!!" + e.getMessage());
        }

    }


    /**
     * Test Deny routine rejects unsafe backspace characters
     */
    @Test
    public void testDenyRejectsBackSpaceCharacters() {
        try {
            setupFilterConfig();

            filter.init(mockFilterConfig);

            Assert.assertFalse(filter.checkAllowsAndDenies("\u0008", mockResponse));
            Assert.assertFalse(filter.checkAllowsAndDenies(" \u0008", mockResponse));
            Assert.assertFalse(filter.checkAllowsAndDenies(" \u0008 ", mockResponse));
            Assert.assertFalse(filter.checkAllowsAndDenies("aaa\u0008", mockResponse));
            Assert.assertFalse(filter.checkAllowsAndDenies("aaa\u0008aaa", mockResponse));
            Assert.assertFalse(filter.checkAllowsAndDenies("\u0008aaa", mockResponse));
        } catch (Exception e) {
            Assert.fail("Unexpected error1!!" + e.getMessage());
        }

    }

    /**
     * Test Allow Deny routines allows all through when not configured
     */
    @Test
    public void testInactiveAllowsAndDenies() {
        try {
            setupFilterConfig();
            Mockito.when(mockFilterConfig.getInitParameter("deny")).thenReturn("");

            filter.init(mockFilterConfig);
            
            Assert.assertTrue(filter.checkAllowsAndDenies("validPropertyName", mockResponse));
            Assert.assertTrue(filter.checkAllowsAndDenies("\u0000", mockResponse));
            Assert.assertTrue(filter.checkAllowsAndDenies("\u0004", mockResponse));
            Assert.assertTrue(filter.checkAllowsAndDenies("\u0008", mockResponse));
        } catch (Exception e) {
            Assert.fail("Unexpected error1!!" + e.getMessage());
        }

    }


    /**
     * Test Allow routine lets through valid inputs
     */
    @Test
    public void testAllowsPasses() {
        try {
            setupFilterConfig();
            Mockito.when(mockFilterConfig.getInitParameter("allow")).thenReturn("[abc]");
            Mockito.when(mockFilterConfig.getInitParameter("deny")).thenReturn("");

            filter.init(mockFilterConfig);

            Assert.assertTrue(filter.checkAllowsAndDenies("   a", mockResponse));
            Assert.assertTrue(filter.checkAllowsAndDenies("   a ", mockResponse));
            Assert.assertTrue(filter.checkAllowsAndDenies("a", mockResponse));
            Assert.assertTrue(filter.checkAllowsAndDenies("   aa", mockResponse));
            Assert.assertTrue(filter.checkAllowsAndDenies("   aa ", mockResponse));
            Assert.assertTrue(filter.checkAllowsAndDenies("aa", mockResponse));
            Assert.assertTrue(filter.checkAllowsAndDenies("   aacacdccdcacc", mockResponse));
        } catch (Exception e) {
            Assert.fail("Unexpected error1!!" + e.getMessage());
        }

    }

    /**
     * Test Allow routine rejects unmatched characters
     */
    @Test
    public void testAllowsRejects() {
        try {
            setupFilterConfig();
            Mockito.when(mockFilterConfig.getInitParameter("allow")).thenReturn("[abc]");
            Mockito.when(mockFilterConfig.getInitParameter("deny")).thenReturn("");
            
            filter.init(mockFilterConfig);          
            
            Assert.assertFalse(filter.checkAllowsAndDenies("   dddd    ", mockResponse));
            Mockito.verify(mockResponse, Mockito.times(1)).sendError(HttpServletResponse.SC_FORBIDDEN);
        } catch (Exception e) {
            Assert.fail("Unexpected error1!!" + e.getMessage());
        }

    }

    /**
     * Test FilterParameters routine using valid safe inputs
     */
    @Test
    public void testFilterParametersWithoutQuoteEscapeUsingValidInputs() {
        try {
            setupFilterConfig();
            Mockito.when(mockFilterConfig.getInitParameter("escapeQuotes")).thenReturn("false");
            setupRequestParameterMap();
            requestParameterMap.put(FORMFIELD5, FORMFIELD5_VALUES);
            ModifiableParamRequestWrapper wrappedRequest =  Mockito.spy(new ModifiableParamRequestWrapper(mockRequest));

            filter.init(mockFilterConfig);          
            boolean XSSDetected = filter.filterParametersAndReturnXSSDetectedStatus(wrappedRequest);
            
            Assert.assertFalse(XSSDetected);
            Assert.assertEquals(FORMFIELD1_VALUES[0], wrappedRequest.getParameter(FORMFIELD1));
            Assert.assertEquals(FORMFIELD1_VALUES[0], wrappedRequest.getParameterValues(FORMFIELD1)[0]);
            Assert.assertEquals(FORMFIELD2_VALUES[0], wrappedRequest.getParameter(FORMFIELD2));
            Assert.assertEquals(FORMFIELD2_VALUES[0], wrappedRequest.getParameterValues(FORMFIELD2)[0]);
            Assert.assertNull(wrappedRequest.getParameter(FORMFIELD3));
            Assert.assertNull(wrappedRequest.getParameterValues(FORMFIELD3));
            Assert.assertEquals(FORMFIELD4_VALUES[0], wrappedRequest.getParameter(FORMFIELD4));
            Assert.assertEquals(FORMFIELD4_VALUES[0], wrappedRequest.getParameterValues(FORMFIELD4)[0]);
            Assert.assertEquals(FORMFIELD5_VALUES[0], wrappedRequest.getParameter(FORMFIELD5));
            Assert.assertEquals(FORMFIELD5_VALUES[0], wrappedRequest.getParameterValues(FORMFIELD5)[0]);
        } catch (Exception e) {
            Assert.fail("Unexpected error1!!" + e.getMessage());
        }

    }


    /**
     * Test FilterParameters routine using valid safe inputs
     */
    @Test
    public void testFilterParametersUsingValidInputs() {
        try {
            setupFilterConfig();
            setupRequestParameterMap();
            ModifiableParamRequestWrapper wrappedRequest =  Mockito.spy(new ModifiableParamRequestWrapper(mockRequest));

            filter.init(mockFilterConfig);          
            boolean XSSDetected = filter.filterParametersAndReturnXSSDetectedStatus(wrappedRequest);
            
            Assert.assertFalse(XSSDetected);
            Assert.assertEquals(FORMFIELD1_VALUES[0], wrappedRequest.getParameter(FORMFIELD1));
            Assert.assertEquals(FORMFIELD1_VALUES[0], wrappedRequest.getParameterValues(FORMFIELD1)[0]);
            Assert.assertEquals(FORMFIELD2_VALUES[0], wrappedRequest.getParameter(FORMFIELD2));
            Assert.assertEquals(FORMFIELD2_VALUES[0], wrappedRequest.getParameterValues(FORMFIELD2)[0]);
            Assert.assertNull(wrappedRequest.getParameter(FORMFIELD3));
            Assert.assertNull(wrappedRequest.getParameterValues(FORMFIELD3));
            Assert.assertEquals(FORMFIELD4_VALUES[0], wrappedRequest.getParameter(FORMFIELD4));
            Assert.assertEquals(FORMFIELD4_VALUES[0], wrappedRequest.getParameterValues(FORMFIELD4)[0]);
        } catch (Exception e) {
            Assert.fail("Unexpected error1!!" + e.getMessage());
        }

    }
    
    /**
     * Test FilterParameters routine using unsafe known XSS inputs
     */
    @Test
    public void testFilterParametersUsingXSSInputValue() {
        try {
            setupFilterConfig();
            setupXSSRequestParameterMap();
            ModifiableParamRequestWrapper wrappedRequest =  Mockito.spy(new ModifiableParamRequestWrapper(mockRequest));

            filter.init(mockFilterConfig);          
            boolean XSSDetected = filter.filterParametersAndReturnXSSDetectedStatus(wrappedRequest);
            
            Assert.assertTrue(XSSDetected);
            Assert.assertEquals(KnownXSSTestData.LOCATOR.getFilteredOutput(), wrappedRequest.getParameter(KnownXSSTestData.LOCATOR.name()));
            Assert.assertEquals(KnownXSSTestData.NO_FILTER_INVASION.getFilteredOutput(), wrappedRequest.getParameter(KnownXSSTestData.NO_FILTER_INVASION.name()));
            Assert.assertEquals(KnownXSSTestData.IMAGE_USING_JAVASCRIPT_DIRECTIVE.getFilteredOutput(), wrappedRequest.getParameter(KnownXSSTestData.IMAGE_USING_JAVASCRIPT_DIRECTIVE.name()));
            Assert.assertEquals(KnownXSSTestData.NO_QUOTE_NO_SEMI_COLON.getFilteredOutput(), wrappedRequest.getParameter(KnownXSSTestData.NO_QUOTE_NO_SEMI_COLON.name()));
            Assert.assertEquals(KnownXSSTestData.CASE_INSENSITIVE.getFilteredOutput(), wrappedRequest.getParameter(KnownXSSTestData.CASE_INSENSITIVE.name()));
            Assert.assertEquals(KnownXSSTestData.MALFORMED_A_TAGS.getFilteredOutput(), wrappedRequest.getParameter(KnownXSSTestData.MALFORMED_A_TAGS.name()));
            Assert.assertEquals(KnownXSSTestData.MALFORMED_A_TAGS_NO_QUOTES.getFilteredOutput(), wrappedRequest.getParameter(KnownXSSTestData.MALFORMED_A_TAGS_NO_QUOTES.name()));
            Assert.assertEquals(KnownXSSTestData.MALFORMED_IMG_TAGS.getFilteredOutput(), wrappedRequest.getParameter(KnownXSSTestData.MALFORMED_IMG_TAGS.name()));
            Assert.assertEquals(KnownXSSTestData.FROM_CHARCODE.getFilteredOutput(), wrappedRequest.getParameter(KnownXSSTestData.FROM_CHARCODE.name()));
            Assert.assertEquals(KnownXSSTestData.DEFAULT_SRC_TAG_BY_LEAVING_IT_EMPTY.getFilteredOutput(), wrappedRequest.getParameter(KnownXSSTestData.DEFAULT_SRC_TAG_BY_LEAVING_IT_EMPTY.name()));
            Assert.assertEquals(KnownXSSTestData.DEFAULT_SRC_TAG_BY_LEAVING_IT_OUT_ENTIRELY.getFilteredOutput(), wrappedRequest.getParameter(KnownXSSTestData.DEFAULT_SRC_TAG_BY_LEAVING_IT_OUT_ENTIRELY.name()));
            Assert.assertEquals(KnownXSSTestData.ON_ERROR_ALERT.getFilteredOutput(), wrappedRequest.getParameter(KnownXSSTestData.ON_ERROR_ALERT.name()));
            Assert.assertEquals(KnownXSSTestData.ON_ERROR_ALERT_ENCODE.getFilteredOutput(), wrappedRequest.getParameter(KnownXSSTestData.ON_ERROR_ALERT_ENCODE.name()));
            Assert.assertEquals(KnownXSSTestData.HTML_DECIMAL_REFERENCE.getFilteredOutput(), wrappedRequest.getParameter(KnownXSSTestData.HTML_DECIMAL_REFERENCE.name()));
        } catch (Exception e) {
            Assert.fail("Unexpected error1!!" + e.getMessage());
        }

    }

    /**
     * Test FilterParameters routine using unsafe known XSS inputs with multiple values
     */
    @Test
    public void testFilterParametersUsingXSSMultipleInputValues() {
        try {
            setupFilterConfig();
            setupXSSRequestParameterMapValues();
            ModifiableParamRequestWrapper wrappedRequest =  Mockito.spy(new ModifiableParamRequestWrapper(mockRequest));

            filter.init(mockFilterConfig);          
            boolean XSSDetected = filter.filterParametersAndReturnXSSDetectedStatus(wrappedRequest);
            
            Assert.assertTrue(XSSDetected);
            Assert.assertEquals(FORMFIELD1_VALUES[0], wrappedRequest.getParameterValues(FORMFIELD1)[0]);
            Assert.assertEquals(FORMFIELD2_VALUES[0], wrappedRequest.getParameterValues(FORMFIELD2)[0]);
            Assert.assertEquals("formField1Content", wrappedRequest.getParameterValues(FORMFIELD3)[0]);
            Assert.assertEquals(KnownXSSTestData.LOCATOR.getFilteredOutput(), wrappedRequest.getParameterValues(FORMFIELD3)[1]);
            Assert.assertEquals(KnownXSSTestData.NO_FILTER_INVASION.getFilteredOutput(), wrappedRequest.getParameterValues(FORMFIELD3)[2]);
            Assert.assertEquals(KnownXSSTestData.LOCATOR.getFilteredOutput(), wrappedRequest.getParameterValues(FORMFIELD4)[0]);
            Assert.assertEquals(KnownXSSTestData.NO_FILTER_INVASION.getFilteredOutput(), wrappedRequest.getParameterValues(FORMFIELD4)[1]);
            Assert.assertEquals("formField4Content3", wrappedRequest.getParameterValues(FORMFIELD4)[2]);
            Assert.assertEquals("formField4Content4", wrappedRequest.getParameterValues(FORMFIELD4)[3]);
        } catch (Exception e) {
            Assert.fail("Unexpected error1!!" + e.getMessage());
        }

    }
    

    /**
     * Test FilterParameters routine using unsafe known XSS inputs for Names and Values (single and multiple)
     */
    @Test
    public void testFilterParametersUsingXSSNamesAndValues() {
        try {
            setupFilterConfig();
            setupRequestParameterMapWithXSSNamesAndValues();
            ModifiableParamRequestWrapper wrappedRequest =  Mockito.spy(new ModifiableParamRequestWrapper(mockRequest));

            filter.init(mockFilterConfig);          
            boolean XSSDetected = filter.filterParametersAndReturnXSSDetectedStatus(wrappedRequest);
            
            Assert.assertTrue(XSSDetected);                        
            Assert.assertEquals(FORMFIELD1_VALUES[0], wrappedRequest.getParameterValues(FORMFIELD1)[0]);
            Assert.assertEquals(FORMFIELD2_VALUES[0], wrappedRequest.getParameterValues(FORMFIELD2)[0]);
            Assert.assertEquals(KnownXSSTestData.LOCATOR.getFilteredOutput(), wrappedRequest.getParameterValues(FORMFIELD3)[1]);
            Assert.assertEquals(KnownXSSTestData.NO_FILTER_INVASION.getFilteredOutput(), wrappedRequest.getParameterValues(FORMFIELD3)[2]);
            Assert.assertEquals(KnownXSSTestData.LOCATOR.getFilteredOutput(), wrappedRequest.getParameterValues(FORMFIELD4)[0]);
            Assert.assertEquals(KnownXSSTestData.NO_FILTER_INVASION.getFilteredOutput(), wrappedRequest.getParameterValues(FORMFIELD4)[1]);
            Assert.assertEquals("formField4Content3", wrappedRequest.getParameterValues(FORMFIELD4)[2]);
            Assert.assertEquals("formField4Content4", wrappedRequest.getParameterValues(FORMFIELD4)[3]);
            Assert.assertEquals(FORMFIELD1_VALUES[0], wrappedRequest.getParameterValues("&lt;" + FORMFIELD1)[0]);
            Assert.assertEquals(FORMFIELD1_VALUES[0], wrappedRequest.getParameterValues(FORMFIELD1 + "&gt;")[0]);

        } catch (Exception e) {
            Assert.fail("Unexpected error1!!" + e.getMessage());
        }

    }
    
    /**
     * Test doFilter using valid request parameters
     */
    @Test
    public void testDoFilterUsingValidInputs(){
        try {
            setupFilterConfig();
            setupRequestParameterMap();
            mockFilterChain = Mockito.mock(FilterChain.class);
            Mockito.doNothing().when(mockFilterChain).doFilter(Mockito.any(ServletRequest.class), Mockito.any(ServletResponse.class));
            
            filter.init(mockFilterConfig);          
            filter.doFilter(mockRequest, mockResponse, mockFilterChain);
            
            Mockito.verify(mockFilterChain, Mockito.times(1)).doFilter(Mockito.any(ServletRequest.class), Mockito.any(ServletResponse.class));
            
            
        } catch (Exception e) {
            Assert.fail("Unexpected error1!!" + e.getMessage());
        }        
    }

    /**
     * Test doFilter using valid and invalid request parameters
     */
    @Test
    public void testDoFilterUsingValidAndInvalidInputs(){
        try {
            setupFilterConfig();
            setupRequestParameterMapWithXSSNamesAndValues();
            mockFilterChain = Mockito.mock(FilterChain.class);
            Mockito.doNothing().when(mockFilterChain).doFilter(Mockito.any(ServletRequest.class), Mockito.any(ServletResponse.class));
            
            filter.init(mockFilterConfig);          
            filter.doFilter(mockRequest, mockResponse, mockFilterChain);
            
            Mockito.verify(mockFilterChain, Mockito.times(1)).doFilter(Mockito.any(ModifiableParamRequestWrapper.class), Mockito.any(ServletResponse.class));
            
        } catch (Exception e) {
            Assert.fail("Unexpected error1!!" + e.getMessage());
        }        
    }
    
    /**
     * Test doFilter using invalid null character property name
     */
    @Test
    public void testDoFilterUsingDeniedNullCharacter(){
        try {
            setupFilterConfig();
            setupRequestParameterMap();
            mockFilterChain = Mockito.mock(FilterChain.class);
            Mockito.doNothing().when(mockFilterChain).doFilter(Mockito.any(ServletRequest.class), Mockito.any(ServletResponse.class));
            requestParameterMap.put("\u0000", new String[]{"This property will cause a FORBIDDEN"});
            
            filter.init(mockFilterConfig);          
            filter.doFilter(mockRequest, mockResponse, mockFilterChain);
            
            Mockito.verify(mockResponse, Mockito.times(1)).sendError(HttpServletResponse.SC_FORBIDDEN);
            
            
        } catch (Exception e) {
            Assert.fail("Unexpected error1!!" + e.getMessage());
        }        
    }
    
    /**
     * Test doFilter using invalid EOT character property name
     */
    @Test
    public void testDoFilterUsingDeniedEOTCharacter(){
        try {
            setupFilterConfig();
            setupRequestParameterMap();
            mockFilterChain = Mockito.mock(FilterChain.class);
            Mockito.doNothing().when(mockFilterChain).doFilter(Mockito.any(ServletRequest.class), Mockito.any(ServletResponse.class));
            requestParameterMap.put("\u0004", new String[]{"This property will cause a FORBIDDEN"});
            
            filter.init(mockFilterConfig);          
            filter.doFilter(mockRequest, mockResponse, mockFilterChain);
            
            Mockito.verify(mockResponse, Mockito.times(1)).sendError(HttpServletResponse.SC_FORBIDDEN);
            
            
        } catch (Exception e) {
            Assert.fail("Unexpected error1!!" + e.getMessage());
        }        
    }

    /**
     * Test doFilter using invalid null character property name
     */
    @Test
    public void testDoFilterUsingDeniedBackspaceCharacter(){
        try {
            setupFilterConfig();
            setupRequestParameterMap();
            mockFilterChain = Mockito.mock(FilterChain.class);
            Mockito.doNothing().when(mockFilterChain).doFilter(Mockito.any(ServletRequest.class), Mockito.any(ServletResponse.class));
            requestParameterMap.put("\u0008", new String[]{"This property will cause a FORBIDDEN"});
            
            filter.init(mockFilterConfig);          
            filter.doFilter(mockRequest, mockResponse, mockFilterChain);
            
            Mockito.verify(mockResponse, Mockito.times(1)).sendError(HttpServletResponse.SC_FORBIDDEN);
            
            
        } catch (Exception e) {
            Assert.fail("Unexpected error1!!" + e.getMessage());
        }        
    }

    /**
     * Enum to hold known XSS input string and their expected filtered output. <br/> 
     * A subset of known XSS inputs were taken from OWASP Cheat Sheet reference for Unit Testing 
     *
     */
    private enum KnownXSSTestData {

        LOCATOR("'';!--\"<XSS>=&{()}", "&#39;&#39;;!--&quot;&lt;XSS&gt;=&{()}"),
        NO_FILTER_INVASION("<SCRIPT SRC=http://xss.rocks/xss.js></SCRIPT>", "&lt;SCRIPT SRC=http://xss.rocks/xss.js&gt;&lt;/SCRIPT&gt;"),
        IMAGE_USING_JAVASCRIPT_DIRECTIVE("<IMG SRC=\"javascript:alert('XSS');\">", "&lt;IMG SRC=&quot;javascript&#58;alert(&#39;XSS&#39;);&quot;&gt;"),
        NO_QUOTE_NO_SEMI_COLON("<IMG SRC=javascript:alert('XSS')>", "&lt;IMG SRC=javascript&#58;alert(&#39;XSS&#39;)&gt;"),
        CASE_INSENSITIVE("<IMG SRC=JaVaScRiPt:alert('XSS')>", "&lt;IMG SRC=javascript&#58;alert(&#39;XSS&#39;)&gt;"),
        HTML_ENTITIES("<IMG SRC=javascript:alert(\"XSS\")>", "&lt;IMG SRC=javascript&#58;alert(&quot;XSS&quot;)&gt;"),
        GRAVE_ACCENT_OBFUSCATION("<IMG SRC=`javascript:alert(\"RSnake says, 'XSS'\")`>", "&lt;IMG SRC=&#96;javascript&#58;alert(&quot;RSnake says, &#39;XSS&#39;&quot;)&#96;&gt;"),
        MALFORMED_A_TAGS("<a onmouseover=\"alert(document.cookie)\">xxs link</a>", "&lt;a onmouseover=&quot;alert(document&#46;&#99;ookie)&quot;&gt;xxs link&lt;/a&gt;"),
        MALFORMED_A_TAGS_NO_QUOTES("<a onmouseover=alert(document.cookie)>xxs link</a>", "&lt;a onmouseover=alert(document&#46;&#99;ookie)&gt;xxs link&lt;/a&gt;"),
        MALFORMED_IMG_TAGS("<IMG \"\"\"><SCRIPT>alert(\"XSS\")</SCRIPT>\">", "&lt;IMG &quot;&quot;&quot;&gt;&lt;SCRIPT&gt;alert(&quot;XSS&quot;)&lt;/SCRIPT&gt;&quot;&gt;"),
        FROM_CHARCODE("<IMG SRC=javascript:alert(String.fromCharCode(88,83,83))>", "&lt;IMG SRC=javascript&#58;alert(String.fromCharCode(88,83,83))&gt;"),
        DEFAULT_SRC_TAG_BY_LEAVING_IT_EMPTY("<IMG SRC=# onmouseover=\"alert('xxs')\">", "&lt;IMG SRC=# onmouseover=&quot;alert(&#39;xxs&#39;)&quot;&gt;"), 
        DEFAULT_SRC_TAG_BY_LEAVING_IT_OUT_ENTIRELY("<IMG onmouseover=\"alert('xxs')\">", "&lt;IMG onmouseover=&quot;alert(&#39;xxs&#39;)&quot;&gt;"), 
        ON_ERROR_ALERT("<IMG SRC=/ onerror=\"alert(String.fromCharCode(88,83,83))\"></img>", "&lt;IMG SRC=/ onerror=&quot;alert(String.fromCharCode(88,83,83))&quot;&gt;&lt;/img&gt;"),
        ON_ERROR_ALERT_ENCODE("<img src=x onerror=\"&#0000106&#0000097&#0000118&#0000097&#0000115&#0000099&#0000114&#0000105&#0000112&#0000116&#0000058&#0000097&#0000108&#0000101&#0000114&#0000116&#0000040&#0000039&#0000088&#0000083&#0000083&#0000039&#0000041\">", 
                "&lt;img src=x onerror=&quot;&#0000106&#0000097&#0000118&#0000097&#0000115&#0000099&#0000114&#0000105&#0000112&#0000116&#0000058&#0000097&#0000108&#0000101&#0000114&#0000116&#0000040&#0000039&#0000088&#0000083&#0000083&#0000039&#0000041&quot;&gt;"),
        HTML_DECIMAL_REFERENCE("<IMG SRC=&#106;&#97;&#118;&#97;&#115;&#99;&#114;&#105;&#112;&#116;&#58;&#97;&#108;&#101;&#114;&#116;&#40;&#39;&#88;&#83;&#83;&#39;&#41;>", "&lt;IMG SRC=&#106;&#97;&#118;&#97;&#115;&#99;&#114;&#105;&#112;&#116;&#58;&#97;&#108;&#101;&#114;&#116;&#40;&#39;&#88;&#83;&#83;&#39;&#41;&gt;"),
        
        ;
        private String input;
        private String filteredOutput;
        
        private KnownXSSTestData(String input, String filteredOutput) {
            this.input = input;
            this.filteredOutput = filteredOutput;
        }
        
        public String getInput() {
            return input;
        }
        public String getFilteredOutput() {
            return filteredOutput;
        }
        
        
    }
}
