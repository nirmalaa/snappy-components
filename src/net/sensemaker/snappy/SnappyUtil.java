/*
 * Created on Sep 27, 2008
 * By rob
 *
 * MIT Style License
 *
 * Copyright (c) 2008 Sensemaker Software Inc. All rights reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * 
 * ===========================================================================
 */
package net.sensemaker.snappy;




import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.logging.Logger;
import java.io.IOException;

import org.json.JSONObject;
import org.json.JSONArray;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.servlet.http.HttpServletRequest;




public class SnappyUtil {

	private static Logger log = Logger.getLogger(SnappyUtil.class.getName());

    
    public static Object getPropertyValue(String property, Object target) {
        Class clazz = target.getClass();
        property = Character.toUpperCase(property.charAt(0)) + property.substring(1);
        Method method = null;
        try {
            String methodName = "get" + property;
            method = clazz.getMethod(methodName);
            log.finest("Found [" + methodName + "]");
        } catch (NoSuchMethodException e) {
            try {
                String methodName = "is" + property;
                method = clazz.getMethod(methodName);
                log.finest("Found [" + methodName + "]");
            } catch (NoSuchMethodException e2) {
                throw new RuntimeException("Property " + property + " not found in " + clazz.getName());
            }
        }
        try {
            Object o = method.invoke(target, new Object[0]);
            log.finest("Returniong [" + o + "]");
            return o;
        } catch (Exception e) {
            throw new RuntimeException("Error getting property " + property + " from " + clazz.getName(), e);
        }

    }


    public static void setBooleanProperty(String property, Object target, boolean value){
        Class clazz = target.getClass();
        property = Character.toUpperCase(property.charAt(0)) + property.substring(1);
        Method method = null;
        String methodName = "set" + property;
        try{
            method = clazz.getMethod(methodName, Boolean.class);
        }catch(NoSuchMethodException e){
            throw new RuntimeException("Method " + methodName
                    + " not found in " + clazz.getName());
        }
        try{
            method.invoke(target, new Object[]{value});
        }catch(Exception e){
            throw new RuntimeException("Error setting property " + property
                    + " in " + clazz.getName(), e);
        }
    }

    public static boolean isNumber(Object object) {
        if (object == null) {
            return false;
        }
        String name = object.getClass().getName();
        String[] numbers = {"java.lang.Integer",
                "java.lang.Float",
                "java.lang.Integer",
                "java.lang.Long",
                "java.lang.Double",
                "java.math.BigDecimal"};
        for (int i = 0; i < numbers.length; i++) {
            if (numbers[i].equals(name)) return true;
        }
        return false;
    }

    public static String forJSON(String aText){
        final StringBuilder result = new StringBuilder();
        char[] ca = aText.toCharArray();

        for (int i = 0; i < ca.length; i++) {
            char character = ca[i];
           if (character == '>') {
                result.append("&Sgt;");
            } else if (character == '<') {
                result.append("&Slt;");
         
            } else {
                //the char is not a special one
                //add it to the result as is
                result.append(character);
            }

        }
        return result.toString();
    }

    public static String forHTML(String aText) {
        final StringBuilder result = new StringBuilder();
        char[] ca = aText.toCharArray();

        for (int i = 0; i < ca.length; i++) {
            char character = ca[i];
            if (character == '&') {// Must be first to escape previous escaped HTML
                result.append("&amp;");
            } else if (character == '>') {
                result.append("&gt;");
            } else if (character == '<') {
                result.append("&lt;");
                //} else if (character == '\"') {
                //  result.append("&quot;");
            } else if (character == '\'') {
                result.append("&#039;");
            } else if (character == '(') {
                result.append("&#040;");
            } else if (character == ')') {
                result.append("&#041;");
            } else if (character == '#') {
                result.append("&#035;");
            } else if (character == '%') {
                result.append("&#037;");
            } else if (character == ';') {
                result.append("&#059;");
            } else if (character == '+') {
                result.append("&#043;");
            } else if (character == '-') {
                result.append("&#045;");
            } else {
                //the char is not a special one
                //add it to the result as is
                result.append(character);
            }

        }
        return result.toString();
    }


    public static boolean jsonEquals(JSONObject a, JSONObject b){
        Iterator iter = a.sortedKeys();
        while(iter.hasNext()){
            String key = (String)iter.next();
            Object valueA = a.get(key);
            if(!b.has(key))return false;
            Object valueB = b.get(key);
            if(valueB == null)return false;
            if(!valueA.getClass().getName().equals(valueB.getClass().getName()))return false;
            if(valueA instanceof JSONObject){
                if(!jsonEquals((JSONObject)valueA, (JSONObject)valueB))return false;
            }
            if(valueA instanceof JSONArray){
                if(!jsonEquals((JSONArray)valueA, (JSONArray)valueB))return false;
            }
            if(!valueA.equals(valueB))return false;
        }
        return true;
    }

    public static boolean jsonEquals(JSONArray a, JSONArray b){
        for(int i = 0; i < a.length(); i++){
            Object valueA = a.get(i);
            Object valueB = b.get(i);
            if(!valueA.getClass().getName().equals(valueB.getClass().getName()))return false;
            if(valueA instanceof JSONObject){
                if(!jsonEquals((JSONObject)valueA, (JSONObject)valueB))return false;
            }
            if(valueA instanceof JSONArray){
                if(!jsonEquals((JSONArray)valueA, (JSONArray)valueB))return false;
            }
            if(!valueA.equals(valueB))return false;
        }
        return true;
    }

    public static void injectScript(FacesContext context, String scriptUri, String resourcePath)
        throws IOException
    {
        HttpServletRequest request = (HttpServletRequest)context.getExternalContext().getRequest();
        String contextPath = request.getContextPath();
        Map attributes = new HashMap();
        attributes.put("type","text/javascript");
        attributes.put("src", contextPath + resourcePath + scriptUri);
        addHeaderTag(context, "script", attributes);
    }

    public static void addHeaderTag(FacesContext context,
                                    String tag, Map attrbiutes)
             throws IOException
    {
        addHeaderTag(context.getViewRoot(), context, tag, attrbiutes);
    }

    private static void addHeaderTag(UIComponent uiComponent, FacesContext context,
                                     String tag, Map<String, String> attributes) throws IOException {
        Iterator iter = uiComponent.getChildren().iterator();
        if(attributes == null){
            attributes = new HashMap();
        }
        while(iter.hasNext()){
            UIComponent comp = (UIComponent)iter.next();
            if(comp instanceof com.icesoft.faces.component.UIXhtmlComponent){
                String tagName = ((com.icesoft.faces.component.UIXhtmlComponent)comp).getTag();
                if(tagName != null){
                    tagName = tagName.toLowerCase();
                    log.finest("Tag [" + tagName + "]");
                    if("body".equals(tagName)){
                        // Injection point
                        ResponseWriter rw = context.getResponseWriter();
                        rw.startElement(tag, comp);
                        Iterator attribs = attributes.entrySet().iterator();
                        while(attribs.hasNext()){
                        	Entry<String,String> entry =(Entry<String,String>)attribs.next(); 
                            rw.writeAttribute(entry.getKey(), entry.getValue(), null);
                        }
                        rw.endElement(tag);
                        log.finest("Injected");
                    }
                }
            }
            addHeaderTag(comp, context, tag, attributes);
        }
    }

}
