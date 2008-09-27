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
package net.sensemaker.snappy.table;

import javax.faces.webapp.UIComponentTag;
import javax.faces.component.UIComponent;
import javax.faces.el.ValueBinding;
import javax.faces.context.FacesContext;

public class SnappyTableTag extends UIComponentTag {

    private String value;
    private String selectionListener;
    private String selectedValue;
    private String sortable;
    private String selectable;
    private String selectMultiple;
    private String rowClasses;
    private String pageSize;
    private String styleClass;
    private String rowStyleValue;
    private String selectedClass;
    private String chunked;
    private String var;
    private String chunkSize;
    private String rowClassValue;
    private String pageControlClass;
    private String currentPage;

    public SnappyTableTag() {
        super();

    }

    public String getComponentType() {
        return "net.sensemaker.snappy.Table";
    }

    public String getRendererType() {
        return null;
    }

    protected void setProperties(UIComponent uiComponent) {
        super.setProperties(uiComponent);
        SnappyTable snappyTable = (SnappyTable) uiComponent;
        if (value != null) {
            if (isValueReference(value)) {
                ValueBinding vb = FacesContext.getCurrentInstance()
                        .getApplication().createValueBinding(value);
                uiComponent.setValueBinding("value", vb);
            }
        }
        if (selectionListener != null) {
            if (isValueReference(selectionListener)) {
                //TODO Clean up names
                snappyTable.setSelectionListener(selectionListener);
            } else {
                throw new RuntimeException("SelectionListener must be a method binding");
            }
        }
        if (selectedValue != null) {
            if (isValueReference(selectedValue)) {
            	ValueBinding vb = FacesContext.getCurrentInstance()
                .getApplication().createValueBinding(selectedValue);
            	uiComponent.setValueBinding("selectedValue", vb);
            } else {
            	 throw new RuntimeException("SeltectedProperty must not be a value binding");
                
            }
        }
        if (rowStyleValue!= null) {
            if (isValueReference(rowStyleValue)) {
            	ValueBinding vb = FacesContext.getCurrentInstance()
                .getApplication().createValueBinding(rowStyleValue);
            	uiComponent.setValueBinding("rowStyleValue", vb);
            } else {
            	throw new RuntimeException("rowStyleValue must be a value binding");            }                                      
        }
        if (sortable != null) {
            if (isValueReference(sortable)) {
                throw new RuntimeException("Sortable must not be a value binding");
            } else {
                snappyTable.setSortable(Boolean.parseBoolean(sortable));
            }
        }
        if (selectable != null) {
            if (isValueReference(selectable)) {
                throw new RuntimeException("Selectable must not be a value binding");
            } else {
                snappyTable.setSelectable(Boolean.parseBoolean(selectable));
            }
        }
        if (selectMultiple != null) {
            if (isValueReference(selectMultiple)) {
                throw new RuntimeException("SelectMultiple must not be a value binding");
            } else {
                snappyTable.setSelectMultiple(Boolean.parseBoolean(selectMultiple));
            }
        }
        if (rowClasses != null) {
            if (isValueReference(rowClasses)) {
                ValueBinding vb = FacesContext.getCurrentInstance()
                        .getApplication().createValueBinding(rowClasses);
                uiComponent.setValueBinding("rowClasses", vb);
            } else {
                snappyTable.setRowClasses(rowClasses);
            }
        }
        if( pageSize != null){
             if (isValueReference(pageSize)) {
                ValueBinding vb = FacesContext.getCurrentInstance()
                        .getApplication().createValueBinding(pageSize);
                uiComponent.setValueBinding("pageSize", vb);
            } else {
                snappyTable.setPageSize(Integer.parseInt(pageSize));
            }
        }
        if(styleClass != null){
            if (isValueReference(styleClass)) {
                ValueBinding vb = FacesContext.getCurrentInstance()
                        .getApplication().createValueBinding(styleClass);
                uiComponent.setValueBinding("styleClass", vb);
            } else {
                snappyTable.setStyleClass(styleClass);
            }
        }
        if(selectedClass != null){
            if (isValueReference(selectedClass)) {
                ValueBinding vb = FacesContext.getCurrentInstance()
                        .getApplication().createValueBinding(styleClass);
                uiComponent.setValueBinding("selectedCLass", vb);
            } else {
                snappyTable.setSelectedClass(selectedClass);
            }
        }
        if(chunked != null){
            if (isValueReference(chunked)) {
                ValueBinding vb = FacesContext.getCurrentInstance()
                        .getApplication().createValueBinding(chunked);
                uiComponent.setValueBinding("chunked", vb);
            } else {
                snappyTable.setChunked(Boolean.parseBoolean(chunked));
            }
        }
        if(var != null){
            snappyTable.setVar(var);
        }
        if(chunkSize != null){
            if (isValueReference(chunkSize)) {
                ValueBinding vb = FacesContext.getCurrentInstance()
                        .getApplication().createValueBinding(chunkSize);
                uiComponent.setValueBinding("chunkSize", vb);
            } else {
                snappyTable.setChunkSize(Integer.parseInt(chunkSize));
            }
        }
        if(rowClassValue != null){
        	if (isValueReference(rowClassValue)) {
                ValueBinding vb = FacesContext.getCurrentInstance()
                        .getApplication().createValueBinding(rowClassValue);
                uiComponent.setValueBinding("rowClassValue", vb);
            } else {
                throw new RuntimeException("rowClassValue must be a value expression");
            }
        }
        if(pageControlClass != null)
        {
        	if(isValueReference(pageControlClass))
        	{
        		ValueBinding vb = FacesContext.getCurrentInstance()
        			.getApplication().createValueBinding(pageControlClass);
        		uiComponent.setValueBinding("pageControlClass",vb);
        	}else{
        		snappyTable.setPageControlClass(pageControlClass);
        	}
        }
        if(currentPage != null)
        {
        	if(isValueReference(currentPage))
        	{
        		ValueBinding vb  = FacesContext.getCurrentInstance()
        			.getApplication().createValueBinding("currentPage");
        		uiComponent.setValueBinding("currentPage", vb);
        	}else{
        		snappyTable.setCurrentPage(Integer.parseInt(currentPage));
        	}
        }
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setSelectionListener(String selectionListener) {
        this.selectionListener = selectionListener;
    }

    public void setSelectedValue(String selectedProperty) {
        this.selectedValue = selectedProperty;
    }

    public void setSortable(String sortable) {
        this.sortable = sortable;
    }

    public void setSelectable(String selectable) {
        this.selectable = selectable;
    }

    public String getSelectMultiple() {
        return selectMultiple;
    }

    public void setSelectMultiple(String selectMultiple) {
        this.selectMultiple = selectMultiple;
    }

    public void setRowClasses(String rowClasses) {
        this.rowClasses = rowClasses;
    }

    public void setStyleClass(String styleClass) {
        this.styleClass = styleClass;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }

    public void setRowClassValue(String rowClassValue) {
        this.rowClassValue = rowClassValue;
    }
    
    public void setRowStyleValue(String rowStyleValue) {
        this.rowStyleValue = rowStyleValue;
    }   

    public void setSelectedClass(String selectedClass) {
        this.selectedClass = selectedClass;
    }

    public void setChunked(String chunked) {
        this.chunked = chunked;
    }

    public void setVar(String var) {
        this.var = var;
    }

    public void setChunkSize(String chunkSize) {
        this.chunkSize = chunkSize;
    }
    
    public void setPageControlClass(String pageControlClass)
    {
    	this.pageControlClass = pageControlClass;
    }
    
    public void setCurrentPage(String currentPage)
    {
    	this.currentPage = currentPage;
    }
}