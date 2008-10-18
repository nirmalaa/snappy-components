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
package net.sensemaker.snappy.calendar;

import net.sensemaker.snappy.HTMLWriter;


import javax.faces.component.ValueHolder;
import javax.faces.component.UIComponentBase;
import javax.faces.convert.Converter;
import javax.faces.el.ValueBinding;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.text.ParseException;

import java.util.Date;

import java.util.StringTokenizer;
import java.util.logging.Logger;


import org.json.JSONObject;
import org.json.JSONArray;


public class SnappyCalendar extends UIComponentBase implements ValueHolder {

	private static final Logger log = Logger.getLogger(SnappyCalendar.class.getName());
    private Object value;
    private String format ;
    private String styleClass = "snappyCalendar";
    private boolean evalMode = true;// TODO When read to ship make a parameter
    private String buttonValue = "...";
    public static final String BUTTON_ID = "btn";
    public static final String FORMAT_ID = "format";
    public static final String FLAGS = "flags";
    private boolean link = false;
    private boolean autoSubmit = false;
    private SnappyCalendarLocale locale = new SnappyCalendarFrenchLocale();
    private int startDay = 0;
    private String months = "January,February,March,April,May,June,July,August,September,October,November,December";
    private String daysOfWeek = "Sun,Mon,Tue,Wed,Thu,Fri,Sat";


    public String getFamily() {
         return "net.sensemaker.snappy.Calendar";
    }

    public Object getLocalValue() {
        return null;
    }

    public String getStyleClass() {
        ValueBinding vb = this.getValueBinding("styleClass");
        if(vb == null)return styleClass;
        return (String)vb.getValue(this.getFacesContext());
    }

    public void setStyleClass(String styleClass) {
        this.styleClass = styleClass;
    }

    public Object getValue() {
        if(value != null)return value;
        ValueBinding vb = this.getValueBinding("value");
        if(vb == null)return null;
        return vb.getValue(this.getFacesContext());
    }

    public void setValue(Object o) {
        this.value = o;
    }

    public boolean isLink() {
        ValueBinding vb = this.getValueBinding("link");
        if(vb == null)return link;
        return ((Boolean)vb.getValue(this.getFacesContext())).booleanValue();
    }

    public void setLink(boolean link) {
        this.link = link;
    }

    public boolean isAutoSubmit() {
        ValueBinding vb = this.getValueBinding("autoSubmit");
        if(vb == null)return autoSubmit;
        return ((Boolean)vb.getValue(this.getFacesContext())).booleanValue();
    }

    public void setAutoSubmit(boolean autoSubmit) {
        this.autoSubmit = autoSubmit;
    }

    public String getFormat() {
    	String result = null;
        if(format != null){
        	result =  format;
        }else{
        	ValueBinding vb = this.getValueBinding("format");
        	if(vb == null){
        		result =  "MM/dd/yyyy";
        	}else{
        		result = (String)vb.getValue(this.getFacesContext());
        	}
        }
        if(result != null){
        	// Escape > < chars to prevent a cross site script attack
        	result = result.replaceAll("\\<", "&lt;");
        	result = result.replaceAll("\\>", "&gt;");
        }
    	return result;
    }

    public int getStartDay() {
        ValueBinding vb = getValueBinding("startDay");
        if(vb != null)return (Integer)vb.getValue(getFacesContext());
        return startDay;
    }

    public void setStartDay(int startDay) {
        this.startDay = startDay;
    }

    public String getMonths() {
        ValueBinding vb = getValueBinding("months");
        if(vb != null)return (String)vb.getValue(getFacesContext());
        return months;
    }

    public void setMonths(String months) {
        this.months = months;
    }

    public String getDaysOfWeek() {
        ValueBinding vb = getValueBinding("daysOfWeek");
        if(vb != null)return (String)vb.getValue(getFacesContext());
        return daysOfWeek;
    }

    public void setDaysOfWeek(String daysOfWeek) {
        this.daysOfWeek = daysOfWeek;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getButtonValue() {
        ValueBinding vb = this.getValueBinding("buttonValue");
        if(vb == null)return buttonValue;
        return (String)vb.getValue(this.getFacesContext());
    }

    public void setButtonValue(String buttonValue) {
        this.buttonValue = buttonValue;
    }

    public Converter getConverter() {
        return null;
    }

    public void setConverter(Converter converter) {

    }

    public boolean isEvalMode() {
        return evalMode;
    }

    public void setEvalMode(boolean evalMode) {
        this.evalMode = evalMode;
    }

    public void decode(FacesContext context){
               String fieldId =  getClientId(context);
            String value = (String)context.getExternalContext().getRequestParameterMap().get(fieldId);
            if(value != null){
                if(value.trim().length() > 0){
                      SimpleDateFormat sdf = new SimpleDateFormat(getFormat());
                        try{
                            Date d = sdf.parse(value);
                            ValueBinding vb = this.getValueBinding("value");
                            if(vb != null){
                                vb.setValue(context, d);
                            }else{
                                setValue(d);
                            }
                        }catch(ParseException e){
                            e.printStackTrace();
                        }
                }
            }
     }

    public void encodeEnd(FacesContext context) throws IOException {
        super.encodeEnd(context);
       
        HTMLWriter hw = new HTMLWriter();
        if(evalMode){
            //include JS files directly on the page
            try{
            //renderJavascript(hw);
            renderCss(hw);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        
        renderTextfield(hw, context);

        //renderJavascript(hw);
        ResponseWriter responseWriter = context.getResponseWriter();
        log.finest(responseWriter.getCharacterEncoding());
        renderConfig(context,responseWriter);
        hw.render(responseWriter, this);
    }

    private void renderTextfield(HTMLWriter w, FacesContext context) throws IOException
    {
        String fieldId =  getClientId(context);
        String buttonId = fieldId + "btn";
        String formatId = fieldId + "format";
        String linkId = fieldId + "link";
        w.startElement("input");
        if(isLink()){
            w.writeAttribute("type", "hidden");
        }else{
            w.writeAttribute("type", "text");
        }
        w.writeAttribute("id", fieldId);
        w.writeAttribute("name", fieldId);
        w.writeAttribute("class", styleClass);


        w.writeAttribute("onkeyup","sp_calendar.parse('" + fieldId + "');");
        w.writeAttribute("onchange","sp_calendar.parse('" + fieldId + "');");

        SimpleDateFormat sdf = new SimpleDateFormat(getFormat());
        if(getValue() != null)
        	w.writeAttribute("value", sdf.format(getValue()));
        w.endElement("input");
        if(isLink()){
            w.startElement("a");
            w.writeAttribute("id",linkId);
            w.writeAttribute("class","link");
            w.writeAttribute("href", "#");
            w.writeAttribute("onclick","sp_calendar.display('" + fieldId + "');return false;");
            if(getValue() != null)
            	w.writeText(sdf.format(getValue()));
            
            w.endElement("a");
        }else{
            w.startElement("input");
            w.writeAttribute("id", buttonId);
            w.writeAttribute("class", styleClass);

            if(getButtonValue() != null){
                w.writeAttribute("value", getButtonValue());
            }
            w.writeAttribute("type", "button");
            w.writeAttribute("onclick", "sp_calendar.display('" + fieldId + "');");
            w.endElement("input");
        }
        w.startElement("div");
        w.writeAttribute("id", formatId);
        w.writeAttribute("style", "display:none;");
        w.writeText(getFormat());
        w.endElement("div");
        w.startElement("div");
        w.writeAttribute("id", fieldId + FLAGS);
        w.writeAttribute("style", "display:none;");
        w.writeText(isAutoSubmit()+"");
        w.endElement("div");
//        w.startElement("script");
//        w.writeAttribute("type","text/javascript");
//        w.writeText("Calendar.setup({\n" +
//                "        inputField     :    \"" + fieldId + "\",      // id of the input field\n" +
//                "        ifFormat       :    \"%m/%d/%Y %I:%M %p\",       // format of the input field\n" +
//                "        showsTime      :    true,            // will display a time selector\n" +
//                "        button         :    \"" + buttonId + "\",   // trigger for the calendar (button ID)\n" +
//                "        singleClick    :    false,           // double-click mode\n" +
//                "        step           :    1,                // show all years in drop-down boxes (instead of every other year as default)\n" +
//                "        align          :    \"Tl\"" +
//                "    });");
//        w.endElement("script");
        /*
        <script type="text/javascript">
    Calendar.setup({
        inputField     :    "f_date_b",      // id of the input field
        ifFormat       :    "%m/%d/%Y %I:%M %p",       // format of the input field
        showsTime      :    true,            // will display a time selector
        button         :    "f_trigger_b",   // trigger for the calendar (button ID)
        singleClick    :    false,           // double-click mode
        step           :    1                // show all years in drop-down boxes (instead of every other year as default)
    });
</script>
         */
    }

    /*private void renderJavascript(HTMLWriter writer) throws IOException {

        InputStream stream =
                this.getClass().getClassLoader().getResourceAsStream("net/sensemaker/snappy/resources/basic_cal.js");
        BufferedReader in = null;
        try{
        	in = new BufferedReader(new InputStreamReader(stream));
        	writer.startElement("script");
        	writer.writeAttribute("type", "text/javascript");
        	// writer.writeAttribute("src", "/jspx/snappyresources/basic_cal.js");
        	while (in.ready()) {
        		writer.writeText(in.readLine() + '\n');
        	}
        	writer.endElement("script");
        }finally
        {
        	if(in != null)
        		in.close();
        }
    }*/
    private void renderCss(HTMLWriter writer) throws IOException {

        InputStream stream =
                this.getClass().getClassLoader().getResourceAsStream("net/sensemaker/snappy/resources/snappy.css");
        
        BufferedReader in = null;
        try{
        	in = new BufferedReader(new InputStreamReader(stream));
        	writer.startElement("style");
        	while (in.ready()) {
        		writer.writeText(in.readLine() + '\n');
        	}
        	writer.endElement("style");
        }finally{
        	if(in != null)
        	{
        		in.close();
        	}
        }
    }
    


    private void renderConfig(FacesContext context, ResponseWriter rw) throws IOException
    {
        JSONArray months = new JSONArray();
        log.finest("Months [" + locale.months() + "]");
        StringTokenizer st = new StringTokenizer(this.months, ",");
        while(st.hasMoreTokens()){
            
            months.put(st.nextToken());

        }

        JSONArray daysOfWeek = new JSONArray();
        st = new StringTokenizer(this.daysOfWeek, ",");
        while(st.hasMoreTokens()){
            daysOfWeek.put(st.nextToken());

        }

        rw.startElement("div",this);
        rw.writeAttribute("id", getClientId(context) + "STATUSDIV",null);
        rw.writeAttribute("style", "display:none;",null);
        JSONObject json = new JSONObject();
        json.put("startDay", this.startDay);
        json.put("months", months);
        json.put("daysOfWeek", daysOfWeek);
        log.finest("JSON [" + json.toString() + "]");
        rw.writeText(json.toString(),null);
        rw.endElement("div");

    }
}
