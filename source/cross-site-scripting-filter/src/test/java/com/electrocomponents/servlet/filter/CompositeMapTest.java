package com.electrocomponents.servlet.filter;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;



public class CompositeMapTest {

    private Map<String, String[]> requestParameterMap = null;
    private Map<String, String[]> modifiableParameterMap = null;
    private CompositeMap<String, String[]> compositeMap = null;
    
    private String FORMFIELD1 = "formField1";
    private String FORMFIELD2 = "formField2";
    private String FORMFIELD3 = "formField3";
    private String FORMFIELD4 = "formField4";
    private String FORMFIELD5 = "formField5";

    private String FORMFIELD1_VALUE = "formField1Content";
    private String FORMFIELD2_VALUE = "formField2Content1";
    private String FORMFIELD3_VALUE = null;
    private String FORMFIELD4_VALUE = "formField4Content";
    private String FORMFIELD5_VALUE = "formField5Content";

    private String FORMFIELD1_VALUES[] = new String[]{FORMFIELD1_VALUE};
    private String FORMFIELD1_MODIFIED_VALUES[] = new String[]{"modified" + FORMFIELD1_VALUE};
    private String FORMFIELD2_VALUES[] = new String[]{FORMFIELD2_VALUE, FORMFIELD2_VALUE + "2", FORMFIELD2_VALUE + "3"};
    private String FORMFIELD2_MODIFIED_VALUES[] = new String[]{"modified" + FORMFIELD2_VALUE, "modified" + FORMFIELD2_VALUE + "2", "modified" + FORMFIELD2_VALUE + "3"};
    private String FORMFIELD3_VALUES[] = null;
    private String FORMFIELD4_VALUES[] = new String[]{FORMFIELD4_VALUE};
    private String FORMFIELD5_VALUES[] = new String[]{FORMFIELD5_VALUE};


    
    @Before
    public void setUp(){
        requestParameterMap = new HashMap<>();
        modifiableParameterMap = new HashMap<>();     
    }
    
    public void setUpRequestParameterMap(){
        //set up wrapped httpRequest parameterMap with sets of key/value pairs
        requestParameterMap.put(FORMFIELD1, FORMFIELD1_VALUES);
        requestParameterMap.put(FORMFIELD2, FORMFIELD2_VALUES);
        requestParameterMap.put(FORMFIELD3, FORMFIELD3_VALUES);
        requestParameterMap.put(FORMFIELD4, FORMFIELD4_VALUES);        
    }

    public void setUpModifiableParameterMap(){
        //set up wrapped httpRequest parameterMap with sets of key/value pairs
        modifiableParameterMap.put(FORMFIELD1, FORMFIELD1_MODIFIED_VALUES);
        modifiableParameterMap.put(FORMFIELD2, FORMFIELD2_MODIFIED_VALUES);
        modifiableParameterMap.put(FORMFIELD5, FORMFIELD5_VALUES);
    }
    
    @Test
    public void testUnsupportedMethods(){
        compositeMap = new CompositeMap<String, String[]>().addDelegate(requestParameterMap).addDelegate(modifiableParameterMap);        
        try{
            compositeMap.put(FORMFIELD1, FORMFIELD1_VALUES);
        }catch(UnsupportedOperationException e){
        }catch(Exception e){
            Assert.fail("unexpected exception");
        }
        try{
            compositeMap.putAll(modifiableParameterMap);
        }catch(UnsupportedOperationException e){
        }catch(Exception e){
            Assert.fail("unexpected exception");
        }
        try{
            compositeMap.remove(FORMFIELD1);
        }catch(UnsupportedOperationException e){
        }catch(Exception e){
            Assert.fail("unexpected exception");
        }
    }
         
    @Test
    public void testQueryMethodsUsingEmptyMaps(){
        compositeMap = new CompositeMap<String, String[]>().addDelegate(requestParameterMap).addDelegate(modifiableParameterMap);        
       
        Assert.assertFalse(compositeMap.containsKey(FORMFIELD1));
        Assert.assertFalse(compositeMap.containsValue(FORMFIELD1_VALUES));
        Assert.assertNull(compositeMap.get(FORMFIELD1));
        Assert.assertTrue(compositeMap.isEmpty());
        Assert.assertTrue(compositeMap.entrySet().isEmpty());
        Assert.assertTrue(compositeMap.keySet().isEmpty());
        Assert.assertTrue(compositeMap.values().isEmpty());
        Assert.assertEquals(0, compositeMap.size());
    }

    
    @Test
    public void testQueryMethodsUsingNoneEmptyMaps(){
       setUpRequestParameterMap();
       setUpModifiableParameterMap();
       compositeMap = new CompositeMap<String, String[]>().addDelegate(requestParameterMap).addDelegate(modifiableParameterMap);        
       
       Assert.assertTrue(compositeMap.containsKey(FORMFIELD1));
       Assert.assertTrue(compositeMap.containsKey(FORMFIELD5));
       Assert.assertFalse(compositeMap.containsKey("UNKNOWN"));
       
       Assert.assertTrue(compositeMap.containsValue(FORMFIELD1_VALUES));
       Assert.assertTrue(compositeMap.containsValue(FORMFIELD5_VALUES));
       Assert.assertFalse(compositeMap.containsValue(new String[]{"UNKOWN"}));
       
       Assert.assertNotNull(compositeMap.get(FORMFIELD1));
       Assert.assertNotNull(compositeMap.get(FORMFIELD5));
       Assert.assertNull(compositeMap.get("UNKNOWN"));

       Assert.assertFalse(compositeMap.isEmpty());
       Assert.assertEquals(7, compositeMap.entrySet().size());
       Assert.assertEquals(5, compositeMap.keySet().size());
       Assert.assertEquals(7, compositeMap.values().size());
       Assert.assertEquals(7, compositeMap.size());
    }
    
    @Test
    public void testClearUsingNoneEmptyMaps(){
       setUpRequestParameterMap();
       setUpModifiableParameterMap();
       compositeMap = new CompositeMap<String, String[]>().addDelegate(requestParameterMap).addDelegate(modifiableParameterMap);        
       compositeMap.clear();

       Assert.assertFalse(compositeMap.containsKey(FORMFIELD1));
       Assert.assertFalse(compositeMap.containsValue(FORMFIELD1_VALUES));
       Assert.assertNull(compositeMap.get(FORMFIELD1));
       Assert.assertTrue(compositeMap.isEmpty());
       Assert.assertTrue(compositeMap.entrySet().isEmpty());
       Assert.assertTrue(compositeMap.keySet().isEmpty());
       Assert.assertTrue(compositeMap.values().isEmpty());
       Assert.assertEquals(0, compositeMap.size());
    }



}
