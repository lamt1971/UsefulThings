package com.electrocomponents.servlet.filter;

import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;



public class ModifiableParamRequestWrapperTest {

    private HttpServletRequest mockRequest = null;
    private Map<String, String[]> requestParameterMap = null;
    private ModifiableParamRequestWrapper requestWrapper = null;
    
    private String FORMFIELD1 = "formField1";
    private String FORMFIELD2 = "formField2";
    private String FORMFIELD3 = "formField3";
    private String FORMFIELD4 = "formField4";

    private String FORMFIELD1_VALUES[] = new String[]{"formField1Content"};
    private String FORMFIELD1_MODIFIED_VALUES[] = new String[]{"modifiedField1Content"};
    private String FORMFIELD2_VALUES[] = new String[]{"formField2Content1", "formField2Content2", "formField2Content3"};
    private String FORMFIELD2_MODIFIED_VALUES[] = new String[]{"modifiedField2Content1", "modifiedField2Content2", "modifiedField2Content3"};
    private String FORMFIELD3_VALUES[] = null;
    private String FORMFIELD4_VALUES[] = new String[]{"formField4Content"};

    private String FORMFIELD1_VALUE = "formField1Content";
    private String FORMFIELD2_VALUE = "formField2Content1";
    private String FORMFIELD3_VALUE = null;
    private String FORMFIELD4_VALUE = "formField4Content";

    
    @Before
    public void setUp() throws Exception{
        mockRequest = Mockito.mock(HttpServletRequest.class);
        //set up wrapped httpRequest parameterMap with sets of key/value pairs
        requestParameterMap = new HashMap<>();
        requestParameterMap.put(FORMFIELD1, FORMFIELD1_VALUES);
        requestParameterMap.put(FORMFIELD2, FORMFIELD2_VALUES);
        requestParameterMap.put(FORMFIELD3, FORMFIELD3_VALUES);
        requestParameterMap.put(FORMFIELD4, FORMFIELD4_VALUES);
        
        Mockito.when(mockRequest.getParameterMap()).thenReturn(requestParameterMap);              
        requestWrapper = new ModifiableParamRequestWrapper(mockRequest);
    }
    
      
    @Test
    public void testGetParameterMap(){
        //verify that parameter map returned contains the original key/value pairs 
        Map<String,String[]> result = requestWrapper.getParameterMap();
    
        Assert.assertTrue(result.size()==4);
        Assert.assertEquals(Arrays.toString(FORMFIELD1_VALUES), Arrays.toString(result.get(FORMFIELD1)));      
        Assert.assertEquals(Arrays.toString(FORMFIELD2_VALUES), Arrays.toString(result.get(FORMFIELD2)));      
        Assert.assertEquals(Arrays.toString(FORMFIELD3_VALUES), Arrays.toString(result.get(FORMFIELD3)));      
        Assert.assertEquals(Arrays.toString(FORMFIELD4_VALUES), Arrays.toString(result.get(FORMFIELD4)));      
    }
    
    @Test
    public void testGetParameter(){
        //verify that the correct value is returned for the paramter name.
        Assert.assertEquals(FORMFIELD1_VALUE, requestWrapper.getParameter(FORMFIELD1));
        Assert.assertEquals(FORMFIELD2_VALUE, requestWrapper.getParameter(FORMFIELD2));
        Assert.assertEquals(FORMFIELD3_VALUE, requestWrapper.getParameter(FORMFIELD3));
        Assert.assertEquals(FORMFIELD4_VALUE, requestWrapper.getParameter(FORMFIELD4));
    }

    
    
    @Test
    public void testGetParameterNames(){
        Enumeration<String> names = requestWrapper.getParameterNames();
        
        String name = null;
        int i = 0;
        while(names.hasMoreElements()){
            name= names.nextElement();
            Assert.assertTrue(("formField1,formField2,formField3,formField4").indexOf(name)>-1);
            i++;
        }
        Assert.assertTrue(i==4);
    }

    @Test
    public void testModifiedParameters(){
        Map<String,String[]> modifiableMap = requestWrapper.getModifiableParameters();
        modifiableMap.put(FORMFIELD1, FORMFIELD1_MODIFIED_VALUES);
        modifiableMap.put(FORMFIELD2, FORMFIELD2_MODIFIED_VALUES);
   
        Map<String,String[]> result = requestWrapper.getParameterMap();
        Assert.assertTrue(result.size()==6);
        Assert.assertEquals(Arrays.toString(FORMFIELD1_MODIFIED_VALUES), Arrays.toString(result.get(FORMFIELD1)));      
        Assert.assertEquals(Arrays.toString(FORMFIELD2_MODIFIED_VALUES), Arrays.toString(result.get(FORMFIELD2)));      
    }

    @Test
    public void testGetParameterValuesWithUpdatedParameters(){
        Map<String,String[]> map = requestWrapper.getModifiableParameters();
        map.put(FORMFIELD1, FORMFIELD1_MODIFIED_VALUES);
        map.put(FORMFIELD2, FORMFIELD2_MODIFIED_VALUES);
      
        Assert.assertEquals(Arrays.toString(FORMFIELD1_MODIFIED_VALUES), Arrays.toString(requestWrapper.getParameterValues(FORMFIELD1)));      
        Assert.assertEquals(Arrays.toString(FORMFIELD2_MODIFIED_VALUES), Arrays.toString(requestWrapper.getParameterValues(FORMFIELD2)));      
        Assert.assertEquals(Arrays.toString(FORMFIELD3_VALUES), Arrays.toString(requestWrapper.getParameterValues(FORMFIELD3)));      
        Assert.assertEquals(Arrays.toString(FORMFIELD4_VALUES), Arrays.toString(requestWrapper.getParameterValues(FORMFIELD4)));      
    }
   
}
