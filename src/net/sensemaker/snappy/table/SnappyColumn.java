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

import javax.faces.component.UIComponentBase;
import javax.faces.component.ValueHolder;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.el.ValueBinding;
import java.util.Date;
import java.util.Calendar;
import java.util.HashMap;
import java.util.logging.Logger;

public class SnappyColumn extends UIComponentBase implements ValueHolder {

	private static Logger log = Logger.getLogger(SnappyColumn.class.getName());

    public SnappyColumn(){
        initBaseTransformers();
    }

    public String getFamily() {
        return "net.sensemaker.snappy.Table";
    }


    private String header;
    private Object value;
    private int sortDirection;
    private String headerClass;
    private String columnClass;
    private String populate;
    private String transformer;
    private String transformerOptions;
    private ColumnTransformer columnTransformer;

    public static final int SORT_NONE = 0;
    public static final int SORT_ASSC = 1;
    public static final int SORT_DESC = 2;

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public Object getValue() {
        ValueBinding vb = getValueBinding("value");
        if(vb != null)return vb.getValue(getFacesContext());
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public int getSortDirection() {
        return sortDirection;
    }

    public void setSortDirection(int sortDirection) {
        this.sortDirection = sortDirection;
    }

    public String getHeaderClass() {
        return headerClass;
    }

    public void setHeaderClass(String headerClass) {
        this.headerClass = headerClass;
    }

    public String getColumnClass() {
        return columnClass;
    }

    public void setColumnClass(String columnClass) {
        this.columnClass = columnClass;
    }

    public String getPopulate() {
        return populate;
    }

    public void setPopulate(String populate) {
        this.populate = populate;
    }

    public String getTransformer() {
        return transformer;
    }

    public void setTransformer(String transformer) {
        this.transformer = transformer;
    }

    public String convertToHtml(Object o){
        checkTransformer(o);
        return columnTransformer.toHtml(o);
    }

    public String convertToSort(Object o){
        checkTransformer(o);
        return columnTransformer.toSort(o);
    }

    public String getTransformerOptions() {
        return transformerOptions;
    }

    public void setTransformerOptions(String transformerOptions) {
        this.transformerOptions = transformerOptions;
    }

    private void checkTransformer(Object o){
        if(columnTransformer == null){
            if(transformer == null){
                // Have to guess
                columnTransformer = guessTransformer(o);
            }else{
                columnTransformer = buildTransformer(transformer);

            }
        }
    }

    public String getAlign(Object o){
        checkTransformer(o);
        return columnTransformer.getAlign();
    }

    public String getVAlign(Object o){
        checkTransformer(o);
        return columnTransformer.getVAlign();
    }

    public String toPopulate(Object o){
        checkTransformer(o);
        return columnTransformer.toPopulate(o);
    }

    private ColumnTransformer guessTransformer(Object o){
        if(o == null)return new StringTransformer();
        if(o instanceof Date || o instanceof Calendar){
            return new CalendarTransformer();
        }
        if( o instanceof Boolean){
            return new BooleanTransformer();
        }
        return new StringTransformer();
    }

    private HashMap<String, String> baseTransformers = new HashMap<String,String>();

    private void initBaseTransformers()
    {
        baseTransformers.put("checkbox", "net.sensemaker.snappy.table.CheckboxTransformer");
    }

    private ColumnTransformer buildTransformer(String transformer){
        String baseClass = baseTransformers.get(transformer);
        if(baseClass != null)transformer = baseClass;
        try{
            Class clazz = Class.forName(transformer);
            Object o = clazz.newInstance();
            return (ColumnTransformer)o;
        }catch(ClassNotFoundException e){
            log.finest("Can't find the transformer class [" + transformer + "]. I'll just return the plain string converted instead.");
            return new StringTransformer();
        }catch(InstantiationException e){
            throw new RuntimeException("Exception creating the [" + transformer + "] class. I tried, but then it blew up. Exception follows", e);
        }catch(IllegalAccessException e){
            throw new RuntimeException("Illegal Access Exception creating the transformer [" + transformer + "] class.",e);
        }

    }

    public Object getLocalValue() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }


    private Converter converter;

    public Converter getConverter() {
        return converter;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void setConverter(Converter converter) {
        this.converter = converter; 
    }

    public void encodeChildren(FacesContext ctx){

    }

    public boolean getRendersChildren(){
        return true;
    }


}
