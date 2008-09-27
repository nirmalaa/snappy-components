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
package net.sensemaker.snappy.test;

import net.sensemaker.snappy.calendar.SnappyCalendar;
import net.sensemaker.testframework.MockResponseWriter;
import net.sensemaker.testframework.ValueBindingUtil;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.shale.test.base.AbstractJsfTestCase;

public class SnappyCalendarTest extends AbstractJsfTestCase{

	
	public SnappyCalendarTest(String n){
		super(n);
	}
	
	public SnappyCalendarTest(){
		super(SnappyCalendarTest.class.getName());
	}
	
    public Class getComponentClass() {
        return SnappyCalendar.class;
    }
    
    public void testFirstRender() throws IOException
    {
    	
    	MockResponseWriter rw = new MockResponseWriter();
    	SnappyCalendar calendar = new SnappyCalendar();
    	calendar.setValue(new Date());
    	facesContext.setResponseWriter(rw);
        calendar.encodeEnd(facesContext);
        String id = calendar.getClientId(facesContext);
        assertTrue(rw.hasId(id));
        assertEquals("Text field not rendered", "text", (String)(rw.getElementById(id).getAttributes().get("type")));
        assertTrue("Button not rendered",rw.hasId(id + SnappyCalendar.BUTTON_ID) );
        assertTrue("Format Div not rendered", rw.hasId(id + SnappyCalendar.FORMAT_ID));
    }
    
    public void testFormater()  throws Exception
    {
    	// As value binding
    	CalendarTestBean ctb = new CalendarTestBean();
    	String formatString = "yyyy/MM/dd";
    	ctb.setFormat(formatString);
    	SnappyCalendar calendar = new SnappyCalendar();
    	
    	ValueBindingUtil.setValueBinding("#{test.format}", calendar, "format", facesContext, ctb);
    	MockResponseWriter rw = new MockResponseWriter();
    
    	Date d = new Date();
    	calendar.setValue(d);
    	facesContext.setResponseWriter(rw);
        calendar.encodeEnd(facesContext);
        String id = calendar.getClientId(facesContext);
        SimpleDateFormat sdf = new SimpleDateFormat(formatString);
        String result = (String)rw.getElementById(id).getAttributes().get("value");
        String expected = sdf.format(d);
        assertEquals("Date in wrong format", expected, result);
    }
    
    public void testCssProtection() throws Exception
    {
    	SnappyCalendar calendar = new SnappyCalendar();
    	String bad = "dd/MM/yyyy><";
    	String expected = "dd/MM/yyyy&gt;&lt;";
    	calendar.setFormat(bad);
    	String result = calendar.getFormat();
    	assertEquals("< > where not escaped", expected, result);
    }
    
}

