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


import net.sensemaker.snappy.HTMLWriter;


import javax.faces.component.UIComponentBase;

import javax.faces.el.MethodBinding;
import javax.faces.el.ValueBinding;
import javax.faces.event.FacesEvent;
import javax.faces.context.FacesContext;
import java.io.IOException;

import java.util.*;
import java.util.logging.Logger;




public class SnappyTable extends UIComponentBase {

	private static Logger log = Logger.getLogger(SnappyTable.class.getName());

     /*
        Internal use variables
      */
     String[] rowStyleClasses;
     HTMLWriter tableWriter = null;
     int sortColumn = -1;
     boolean sortAscending;
     int selectedIndex = -1;
     boolean controlTop = true;
     boolean skipTableRender = false;
     String lastEvent;
     String lastStatus;

     int extraStart;
     int extraEnd;

     List sortedIndex = new ArrayList();
     private MethodBinding selectionListener;

    SnappyTableRenderBypassControl bypassCtrl = new SnappyTableRenderBypassControl();
    TableEncoder tableEncoder = null;
    
    public SnappyTable(){
        this(new SnappyTableRenderBypassControl());
    }
    
   

    public SnappyTable(SnappyTableRenderBypassControl bypass){
        this.bypassCtrl = bypass;
        tableEncoder = new TableEncoder(this, new ColumnValueEncoder());
        
    }


    public String getFamily() {
        return "net.sensemaker.snappy.Table";

    }


    public void decode(FacesContext context) {
        super.decode(context);
        TableRequestDecoder tableRequestDecoder = new TableRequestDecoder();
        tableRequestDecoder.decode(context,this);
    }




    public void encodeEnd(FacesContext context) throws IOException {
        tableEncoder.encode(context);
    }


    int maxPages = 0;

    public int getMaxPages() {
        return maxPages;
    }

    public void setMaxPages(int maxPages) {
        this.maxPages = maxPages;
    }

    int columnCount;

    public int getColumnCount() {
        return columnCount;
    }

    public void setColumnCount(int columnCount) {
        this.columnCount = columnCount;
    }




    public void setSelectionListener(String methodBidning) {
        selectionListener = getFacesContext().getApplication()
                .createMethodBinding(methodBidning, new Class[]{SnappyRowSelectionEvent.class});
    }

    public void broadcast(FacesEvent event) {
        if (event instanceof SnappyRowSelectionEvent && selectionListener != null) {
            selectionListener.invoke(getFacesContext(), new Object[]{event});
        }
        super.broadcast(event);
    }








    public Object saveState(FacesContext context){
        Object[] result = new Object[2];
        result[0] = null;//TODO
        result[1] = super.saveState(context);
        return result;
    }

    public void restoreState(FacesContext context, Object o){
        Object[] oa = (Object[])o;
        //snappyTable = (SnappyTableOld)oa[0];   TODO
        super.restoreState(context, oa[1]);
    }


    /*
        User defined variables
     */
    private int pageSize = 5;

    public int getPageSize() {
        ValueBinding vb = getValueBinding("pageSize");
        if (vb != null) {
            return ((Integer) vb.getValue(getFacesContext())).intValue();
        }
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    boolean sortable = false;

    public boolean isSortable() {
        return sortable;
    }

    public void setSortable(boolean sortable) {
        this.sortable = sortable;
    }

    String rowClasses = "oddRow,evenRow";

    public String getRowClasses() {
        ValueBinding vb = getValueBinding("rowClasses");
        if(vb != null){
            return ((String)vb.getValue(getFacesContext()));
        }
        return rowClasses;
    }

    public void setRowClasses(String rowClasses) {
        this.rowClasses = rowClasses;
    }

    private String pageControlClass = "snappyTablePage";

    public String getPageControlClass() {
        ValueBinding vb = getValueBinding("pageControlClass");
        if(vb != null){
            return ((String)vb.getValue(getFacesContext()));
        }
        return pageControlClass;
    }

    public void setPageControlClass(String pageControlClass) {
        this.pageControlClass = pageControlClass;
    }

    String styleClass = "snappyDefault";

    public String getStyleClass() {
        ValueBinding vb = getValueBinding("styleClass");
        if(vb != null){
            return ((String)vb.getValue(getFacesContext()));
        }
        return styleClass;
    }

    public void setStyleClass(String styleClass) {
        this.styleClass = styleClass;
    }






    boolean selectable = false;

    public boolean isSelectable() {
        return selectable;
    }

    public void setSelectable(boolean selectable) {
        this.selectable = selectable;
    }

    boolean selectMultiple = false;

    public boolean isSelectMultiple() {
        return selectMultiple;
    }

    public void setSelectMultiple(boolean selectMultiple) {
        this.selectMultiple = selectMultiple;
    }

   
    boolean chunked;

    public boolean isChunked() {
        ValueBinding vb = getValueBinding("chunked");
        if(vb != null)return (Boolean)vb.getValue(getFacesContext());
        return chunked;
    }

    public void setChunked(boolean chunked) {
        this.chunked = chunked;
    }

    private List value;
    // Value binding only property
    public List getValue() {
        ValueBinding vb = getValueBinding("value");
        if(vb != null)return (List)vb.getValue(getFacesContext());
        return value;
    }
    
    public void setValue(List l)
    {
    	value = l;
    }


     int currentPage = 0;

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

   
    private String selectedClass;

    public void setSelectedClass(String selectedClass) {
        this.selectedClass = selectedClass;
    }

    public String getSelectedClass() {
    	
        return selectedClass;
    }                                                            

    String var;

    public String getVar() {
        return var;
    }

    public void setVar(String var) {
        this.var = var;
    }

    int chunkSize = 10;

    public int getChunkSize(){
        ValueBinding vb = getValueBinding("chunkSize");
        if(vb != null){
            return (Integer)vb.getValue(getFacesContext());
        }
        log.finest("Returning [" + chunkSize + "]");
        return chunkSize;
    }

    public void setChunkSize(int i){
        chunkSize = i;
    }
// END USER PROPERTIES
    
    
     void initRowStyleClasses() {
        if (rowClasses != null) {

            StringTokenizer st = new StringTokenizer(rowClasses, ",");
            int count = st.countTokens();
            if (count == 2) {
                rowStyleClasses = new String[]{
                        st.nextToken(),
                        st.nextToken()
                };
            }
        }
    }
     public boolean isSelected() {
     	ValueBinding vb = getValueBinding("selectedValue");
     	if(vb == null)return false;
     	return (Boolean)vb.getValue(getFacesContext());
     }

     public void setSelected(boolean b) {
       	ValueBinding vb = getValueBinding("selectedValue");
     	if(vb == null)return;
     	vb.setValue(getFacesContext(), b);  
     }


    public String genStatusId(String tableId) {
        String id = tableId + "SPSTATUS";
        return id;
    }

    String genEventId(String tableId) {
        String id = tableId + "SPEVENT";
        return id;
    }

    String genSortId(String tableId){
        String id = tableId + "SPSORT";
        return id;
    }


   /* boolean isRowSelected(Object row) {
        if (selectedProperty != null) {
            Object o = SnappyUtil.getPropertyValue(selectedProperty, row);
            if (o == null) {
                throw new RuntimeException("The styleClassProperty " +
                        selectedProperty + " of class " + row.getClass().getName() + " must not be null");
            }
            if (!(o instanceof Boolean)) {
                throw new RuntimeException("The selectedProperty " +
                        selectedProperty + " of class " + row.getClass().getName() + " must be Boolean and not "
                        + o.getClass().getName());
            }
            boolean result = ((Boolean) o).booleanValue();

            return result;
        }
        return false;
    }*/

    public String getRowStyleClass(){
    	ValueBinding vb = getValueBinding("rowClassValue");
    	if(vb == null)return null;
    	return (String)vb.getValue(getFacesContext());
    }

    void setRowSelected(int rowNumber, boolean selected) {
        List list = getValue();
        if (rowNumber >= list.size()) {
            throw new RuntimeException("Row Number " + rowNumber +
                    " does not exist in value with size " + list.size());
        }
        Object row = list.get(rowNumber);
        getFacesContext().getExternalContext().getRequestMap().put(getVar(), row);      
        setSelected(selected);
    }

    Object getRow(int i){
        List value = getValue();
        if(sortedIndex.size() <= i)return value.get(i);
        int index = ((Integer)sortedIndex.get(i)).intValue();
        return value.get(index);
    }





    int getFirstBlockEnd() {
        int firstBlockEnd = getChunkSize() * getPageSize();
        return firstBlockEnd;
    }

    int getLastBlockStart() {
        List value = getValue();
        int firstBlockEnd = getChunkSize()* getPageSize();
        int rowsOver = value.size() % getPageSize();
        int lastBlockStart = value.size() - firstBlockEnd + rowsOver;
        return lastBlockStart;
    }


     
}
