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

import javax.faces.webapp.UIComponentTag;
import javax.faces.component.UIComponent;
import javax.faces.el.ValueBinding;
import javax.faces.context.FacesContext;

public class SnappyCalendarTag extends UIComponentTag {

    private String value;
    private String format;
    private String styleClass;
    private String buttonValue;
    private String link;
    private String autoSubmit;
    private String startDay;

    private String months = null;
    private String daysOfWeek = null;

    public SnappyCalendarTag() {
        super();
    }

    public String getComponentType() {
        return "net.sensemaker.snappy.Calendar";
    }

    public String getRendererType() {
        return null;
    }


    public void setValue(String value) {
        this.value = value;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public void setStyleClass(String styleClass) {
        this.styleClass = styleClass;
    }

    public void setButtonValue(String buttonValue) {
        this.buttonValue = buttonValue;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setAutoSubmit(String autoSubmit) {
        this.autoSubmit = autoSubmit;
    }

    public void setStartDay(String startDay) {
        this.startDay = startDay;
    }

    public void setMonths(String months) {
        this.months = months;
    }

    public void setDaysOfWeek(String daysOfWeek) {
        this.daysOfWeek = daysOfWeek;
    }

    protected void setProperties(UIComponent uiComponent) {
        SnappyCalendar calendar = (SnappyCalendar)uiComponent;
        if (value != null) {
            if (isValueReference(value)) {
                ValueBinding vb =  FacesContext.getCurrentInstance()
                        .getApplication().createValueBinding(value);
                uiComponent.setValueBinding("value", vb);
            } else {
                calendar.setValue(value);
            }
        }
        if (format != null) {
            if (isValueReference(format)) {
                ValueBinding vb =  FacesContext.getCurrentInstance()
                        .getApplication().createValueBinding(format);
                uiComponent.setValueBinding("format", vb);
            } else {
                calendar.setFormat(format);
            }
        }
        if (styleClass != null) {
            if (isValueReference(styleClass)) {
                ValueBinding vb =  FacesContext.getCurrentInstance()
                        .getApplication().createValueBinding(styleClass);
                uiComponent.setValueBinding("styleClass", vb);
            } else {
                calendar.setStyleClass(styleClass);
            }
        }
        if(buttonValue != null){
           if (isValueReference(buttonValue)) {
                ValueBinding vb =  FacesContext.getCurrentInstance()
                        .getApplication().createValueBinding(buttonValue);
                uiComponent.setValueBinding("buttonValue", vb);
            } else {
                calendar.setButtonValue(buttonValue);
            }
        }
        if(link != null){
            if(isValueReference(link)){
                ValueBinding vb = FacesContext.getCurrentInstance()
                        .getApplication().createValueBinding(link);
                uiComponent.setValueBinding("link", vb);
            }else{
                calendar.setLink(Boolean.parseBoolean(link));
            }
        }
        if(autoSubmit != null){
            if(isValueReference(autoSubmit)){
                ValueBinding vb = FacesContext.getCurrentInstance()
                        .getApplication().createValueBinding(autoSubmit);
                uiComponent.setValueBinding("autoSubmit", vb);
            }else{
                calendar.setAutoSubmit(Boolean.parseBoolean(autoSubmit));
            }
        }

        if(startDay != null){
            if(isValueReference(startDay)){
                ValueBinding vb = FacesContext.getCurrentInstance()
                        .getApplication().createValueBinding(startDay);
                uiComponent.setValueBinding("startDay", vb);
            }else{
                calendar.setStartDay(Integer.parseInt(startDay));
            }
        }
        if(months != null){
            if(isValueReference(months)){
                ValueBinding vb = FacesContext.getCurrentInstance()
                        .getApplication().createValueBinding(months);
                uiComponent.setValueBinding("months", vb);
            }else{
                calendar.setMonths(months);
            }
        }
        if(daysOfWeek != null){
            if(isValueReference(daysOfWeek)){
                ValueBinding vb = FacesContext.getCurrentInstance()
                        .getApplication().createValueBinding(daysOfWeek);
                uiComponent.setValueBinding("daysOfWeek", vb);
            }else{
                calendar.setDaysOfWeek(daysOfWeek);
            }
        }

    }
}
