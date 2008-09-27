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

public class SnappyColumnTag extends UIComponentTag {

    private String header;
    private String value;
    private String populate;
    private String headerClass;
    private String columnClass;
    private String transformer;
    private String transformerOptions;
    

    public SnappyColumnTag() {
        super();

    }

    public String getComponentType() {
        return "net.sensemaker.snappy.Column";
    }

    public String getRendererType() {
        return null;
    }

    protected void setProperties(UIComponent uiComponent) {
        super.setProperties(uiComponent);
        SnappyColumn column = (SnappyColumn)uiComponent;

        if (header != null) {
            if (isValueReference(header)) {
                ValueBinding vb =  FacesContext.getCurrentInstance().getApplication().createValueBinding(header);
                uiComponent.setValueBinding("header", vb);
            } else {
                column.setHeader(header);
            }
        }
        if (value != null) {
            if (isValueReference(value)) {
                ValueBinding vb =  FacesContext.getCurrentInstance().getApplication().createValueBinding(value);
                uiComponent.setValueBinding("value", vb);
            } else {
                column.setValue(value);
            }
        }
        if (populate != null) {
            if (isValueReference(populate)) {
                throw new RuntimeException("Populate must be a string");
            } else {
                column.setPopulate(populate);
            }
        }
        if (headerClass != null) {
            if (isValueReference(headerClass)) {
                throw new RuntimeException("headerClass must be a string");
            } else {
                column.setHeaderClass(headerClass);
            }
        }
        if (columnClass != null) {
            if (isValueReference(columnClass)) {
                throw new RuntimeException("columnClass must be a string");
            } else {
                column.setColumnClass(columnClass);
            }
        }
        if (transformer != null) {
            if (isValueReference(transformer)) {
                throw new RuntimeException("transformer must be a string");
            } else {
                column.setTransformer(transformer);
            }
        }
        if (transformerOptions != null) {
            if (isValueReference(transformerOptions)) {
                //TODO Validate json
                throw new RuntimeException("transformerOptions must be a string");
            } else {
                column.setTransformerOptions(transformerOptions);
            }
        }
    }


    public void setHeader(String header) {
        this.header = header;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setPopulate(String populate) {
        this.populate = populate;
    }

    public void setHeaderClass(String headerClass) {
        this.headerClass = headerClass;
    }

    public void setColumnClass(String columnClass) {
        this.columnClass = columnClass;
    }

    public void setTransformer(String transformer) {
        this.transformer = transformer;
    }

    public void setTransformerOptions(String transformerOptions) {
        this.transformerOptions = transformerOptions;
    }
}