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
import net.sensemaker.snappy.SnappyUtil;

import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.Converter;
import java.io.IOException;
import java.util.List;
import java.util.Iterator;
import java.util.logging.Logger;



import org.json.JSONObject;
import org.json.JSONArray;

public class TableEncoder {


    private SnappyTable table = null;
    private ColumnValueEncoder columnValueEncoder;
    private static Logger log = Logger.getLogger(TableEncoder.class.getName());


    public TableEncoder(SnappyTable table,ColumnValueEncoder columnValueEncoder)
    {
       this.table = table;
       this.columnValueEncoder = columnValueEncoder;
    }


    public void encode(FacesContext context) throws IOException
    {

        try {
            if(table.getValue() == null){
                log.finest("Table Value is null returning");
                return;
            }
            
            String status =
                    (String) context
                            .getExternalContext()
                            .getRequestParameterMap()
                            .get(table.genStatusId(table.getClientId(context)));

            String tableId = table.getClientId(context);
            // TODO Just pass snappyColumns
            HTMLWriter[] writers = encodeEnd(status, tableId, table.getChildren(), context);

            ResponseWriter responseWriter = context.getResponseWriter();
            for (int i = 0; i < writers.length; i++) {
                writers[i].render(responseWriter, table);
            }

        } catch (Exception e) {
           
        }
    }

    public HTMLWriter[] encodeEnd(String status, String tableId, List children, FacesContext context) throws IOException {
          try {

              if (status == null) {
            	  log.finest("Status is null");
                  // Only skip refresh for updated from this table.
                  // For everyone else bake a fresh copy.
                  table.tableWriter = null;
              }

              //ResponseWriter tableWriter = context.getResponseWriter();
              //if (tableWriter == null) {
              table.initRowStyleClasses();
              HTMLWriter newTableWriter = new HTMLWriter();
              //HTMLWriter javascriptWriter = new HTMLWriter();
              // Render javascript code (If not already done);
              //renderJavascript(javascriptWriter);
              renderTable(newTableWriter, children, tableId,context);
              renderEvent(newTableWriter, tableId);
              renderSort(newTableWriter, tableId);


              //}
              // Rerender if anything is different.
              if (table.tableWriter == null) {
            	  log.finest("Rendering new from null");
                  table.tableWriter = newTableWriter;
              } else if (!(table.tableWriter.toString().equals(newTableWriter.toString()))) {
            	  log.finest("Table not equal");
                  if (!table.skipTableRender) {
                	  log.finest("Rendering new");
                      table.tableWriter = newTableWriter;
                  } else {
                	  log.finest("Rendering old");
                  }
              }

              HTMLWriter jsonWriter = new HTMLWriter();
              renderJson(jsonWriter, tableId, children,context);
              renderStatus(jsonWriter, tableId, children);

              // Render JSON data
              return new HTMLWriter[]{
                      //javascriptWriter,
                      table.tableWriter,
                      jsonWriter
              };

              // put it back to detect other submits
              //tableWriter = newTableWriter;
          } catch (Exception e) {
        	  e.printStackTrace();
              //log.error("Error enciding table",e);
          }
          return null;
      }

      private void renderTable(HTMLWriter tableWriter, List children, String tableId, FacesContext context)  throws IOException{
          tableWriter.startElement("table");
          // Force rerender of entire table
          // This will avoid getting into trouble with strange DOM conficts from
          // local updates.
          tableWriter.writeAttribute("rerender", "this" + System.currentTimeMillis());
          tableWriter.writeAttribute("width", "100%");
          tableWriter.writeAttribute("class", table.getStyleClass());
          tableWriter.writeAttribute("id", tableId);

          table.columnCount = 0;
          Iterator iter = children.iterator();

          while (iter.hasNext()) {
              Object o = iter.next();
              if (o instanceof SnappyColumn) {
                  //SnappyColumn column = (SnappyColumn) o;
                  table.columnCount++;
              }
          }
          int defaultWidth = 100;
          if (table.columnCount > 0)
              defaultWidth = 100 / table.columnCount;

          tableWriter.startElement("thead");
          if (table.controlTop) {
              renderPageControl(tableWriter, tableId);

          }

          tableWriter.startElement("tr");
          tableWriter.writeAttribute("id", tableId + "HEADERROW");

          iter = children.iterator();
          int columnIndex = 0;
          while (iter.hasNext()) {
              Object o = iter.next();
              if (o instanceof SnappyColumn) {
                  SnappyColumn column = (SnappyColumn) o;

                  tableWriter.startElement("th");

                  if (column.getHeaderClass() == null && column.getColumnClass() == null)
                      tableWriter.writeAttribute("width", defaultWidth + "%");

                  if (column.getHeader() != null) {

                      if (table.sortable) {
                          tableWriter.startElement("a");
                          tableWriter.writeAttribute("href",
                                  "#");
                          tableWriter.writeAttribute("onmouseup",
                                  "javascript:sp.table.toggleSort('" +
                                          tableId + "'," + columnIndex + ");");
                      }
                      tableWriter.startElement("span");
                      tableWriter.writeText(column.getHeader());
                      tableWriter.endElement("span");
                      if (table.sortable) {
                          tableWriter.startElement("span");
                          tableWriter.writeAttribute("id", tableId
                                  + "TH" + columnIndex
                                  + "DIR");
                          if (columnIndex == table.sortColumn) {
                              if (table.sortAscending) {
                                  tableWriter.writeText("&darr;");
                              } else {
                                  tableWriter.writeText("&uarr;");
                              }
                          }

                          tableWriter.endElement("span");
                          tableWriter.endElement("a");
                      }
                  }
                  tableWriter.endElement("th");

                  columnIndex++;
              }
          }
          tableWriter.endElement("tr");
          tableWriter.endElement("thead");
          tableWriter.startElement("tbody");
          tableWriter.writeAttribute("id", tableId + "BODY");
          renderPage(tableWriter, tableId,children,context);
          tableWriter.endElement("tbody");
          if (!table.controlTop) {
              tableWriter.startElement("tfoot");
              renderPageControl(tableWriter, tableId);
              tableWriter.endElement("tfoot");
          }
          tableWriter.endElement("table");
      }

    private void renderPage(HTMLWriter writer, String tableId, List children,FacesContext context)  throws IOException{

        int startIndex = table.getCurrentPage() * table.getPageSize();
        int endIndex = startIndex + table.getPageSize();

        List list = table.getValue();
        if (list != null) {
            for (int i = startIndex; i < endIndex; i++) {
                if (i < list.size()) {
                    writer.startElement("tr");
                    String selectedClass = null;
                    Object row = table.getRow(i);
                    context.getExternalContext().getRequestMap().put(table.var, row);
                    if (table.isSelected()) {
                        selectedClass = table.getSelectedClass();

                    }
                    String defaultClass = writeRowDefaultClass(i, row);
                    String styleClass = defaultClass;
                    if (selectedClass != null)
                        styleClass = selectedClass;

                    if (styleClass != null) {
                        writer.writeAttribute("class", styleClass);
                    }
                    Iterator iter = children.iterator();
                    int cell = 0;
                    while (iter.hasNext()) {
                        Object o = iter.next();
                        if(o instanceof SnappyColumn){
                            SnappyColumn column = (SnappyColumn) o;

                            Converter jsfConverter = column.getConverter();

                            writer.startElement("td");
                            if (table.selectable) {
                                writer.writeAttribute("onmouseover", "sp.table.mouseover('" + tableId + "'," + i + "," + cell + ", this);");
                                writer.writeAttribute("onmouseout", "sp.table.mouseout('" + tableId + "'," + i + "," + cell + ", this);");
                                writer.writeAttribute("onclick", "sp.table.click('" + tableId + "'," + i + "," + cell + ", this);");
                            }
                            
                            //RGDM
                            Object cellVal = columnValueEncoder.encode(column, context);

                            String html = column.convertToHtml(cellVal);
                            if(jsfConverter != null){
                                html = jsfConverter.getAsString(
                                        FacesContext.getCurrentInstance(),
                                        column,
                                        cellVal);
                            }
                            String align = column.getAlign(cellVal);
                            String vAlign = column.getVAlign(cellVal);
                            if (align != null) {
                                writer.writeAttribute("align", align);
                            } else {
                                writer.writeAttribute("align", "left");
                            }
                            if (vAlign != null) {
                                writer.writeAttribute("valign", vAlign);
                            }
                            if (column.getColumnClass() == null) {

                            } else {
                                writer.writeAttribute("class", column.getColumnClass());
                            }
                            writer.writeText(html);
                            writer.endElement("td");
                            cell++;
                        }
                    }
                    writer.endElement("tr");
                }
            }
        }
    }

    private String writeRowDefaultClass(int rowNumber, Object row) {
        String clazz = table.getRowStyleClass();
        if(clazz != null)return clazz;
        if (table.rowStyleClasses != null) {
            if (rowNumber % 2 == 0) {
                //even
                clazz = table.rowStyleClasses[1];
            } else {
                //odd
                clazz = table.rowStyleClasses[0];
            }
        }

        return clazz;
    }

    private void renderJson(HTMLWriter writer, String tableId, List children, FacesContext context) throws IOException{

        writer.startElement("div");
        writer.writeAttribute("id", tableId + "DATA");
        writer.writeAttribute("style", "display:none;");
        List list = table.getValue();
        //int rowIndex = 0;
        long renderTime = System.currentTimeMillis();
        if (list != null) {
            int firstBlockEnd = table.getFirstBlockEnd();
            int lastBlockStart = table.getLastBlockStart();

            //Iterator rows = value.iterator();
            //JSONObject json = new JSONObject();
            //JSONArray jsonRows = new JSONArray();
            for(int rowIndex = 0; rowIndex < list.size(); rowIndex++){
                boolean ok = false;
                if(rowIndex < firstBlockEnd)ok=true;
                if(rowIndex >= lastBlockStart)ok = true;
                if(!table.isChunked())ok=true;// Render all for unchunked
                if(!ok){


                    continue;
                }else{

                }

                renderJsonRow(writer, tableId, children, renderTime, rowIndex,context);
            }
            writer.startElement("div");
            writer.writeAttribute("id", tableId + "EXTRA-DATA");
            for(int rowIndex = table.extraStart; rowIndex < table.extraEnd; rowIndex++){
            	log.finest("Rendering Extra" + rowIndex) ;
                renderJsonRow(writer, tableId, children, renderTime, rowIndex, context);
            }
            writer.endElement();

        }

        writer.startElement("div");
        writer.writeAttribute("id", tableId + "STYLE");
        JSONObject json = new JSONObject();
        if (table.rowStyleClasses != null) {
            json.put("oddRow", table.rowStyleClasses[0]);
            json.put("evenRow", table.rowStyleClasses[1]);
        }
        writer.writeText(json.toString());
        writer.endElement("div");
        writer.startElement("input");
        writer.writeAttribute("id", tableId + "ROWTEMP");
        writer.endElement("input");
        writer.startElement("input");
        writer.writeAttribute("id", tableId + "SORTTEMP");
        writer.endElement("input");

        writer.endElement("div");

    }

    private void renderJsonRow(HTMLWriter writer, String tableId, List children, long renderTime, int rowIndex, FacesContext context) throws IOException{
        Object row = table.getRow(rowIndex);
        context.getExternalContext().getRequestMap().put(table.var, row);

        Iterator iter = children.iterator();
        JSONArray jsonArray = new JSONArray();

        while (iter.hasNext()) {

            Object o = iter.next();
            if( o instanceof SnappyColumn){
                SnappyColumn column = (SnappyColumn) o;

                Object cellVal = columnValueEncoder.encode(column, context);
                JSONObject cell = new JSONObject();
                String string = column.convertToHtml(cellVal);
                log.finest("Rendering [" + string + "]");
                cell.put("value", SnappyUtil.forJSON(string));
                String sort = SnappyUtil.forHTML(column.convertToSort(cellVal));
                log.finest("Sort [" + sort + "]");
                cell.put("sort", sort);

                cell.put("columnClass", column.getColumnClass());
                String align = column.getAlign(cellVal);
                String vAlign = column.getVAlign(cellVal);

                if (align != null)
                    cell.put("align", align);
                else
                    cell.put("align", "left");
                if (vAlign != null)
                    cell.put("valign", vAlign);
                else
                    cell.put("valign", "center");
                if (column.getPopulate() != null) {
                    cell.put("populate", column.getPopulate());
                    cell.put("populateValue", SnappyUtil.forJSON(column.toPopulate(cellVal)));
                }

                jsonArray.put(cell);
            }
        }
        // Javascript is very fast to eval and slow to stringify.
        // So add the row as a string
        writer.startElement("div");
        writer.writeAttribute("id", tableId + "DATAROW" + rowIndex);
        JSONObject jsonRow = new JSONObject();
        jsonRow.put("row", jsonArray);
        boolean selected = table.isSelected();
        jsonRow.put("selected", selected);
        jsonRow.put("listIndex", rowIndex);
        //jsonRow.put("rendered", renderTime); // This can't be here it will cause a fresh rerender each time


        jsonRow.put("selectedClass", table.getSelectedClass());
        String defaultClass = writeRowDefaultClass(rowIndex, row);
        jsonRow.put("defaultClass",  defaultClass);
        writer.writeText(jsonRow.toString());
        writer.endElement("div");
    }


    private void renderPageControl(HTMLWriter writer, String tableId) {
        String footerId = tableId + "SPFOOT";
        if (table.getValue() == null) {
        	log.finest("List for tableId [" + tableId + "] is null.");
        }
        int size = table.getValue().size();
        int max = size / table.getPageSize();
        if (size % table.getPageSize() > 0) {
            max++;
        }
        max++;
        table.maxPages = max - 1;
        // << < 1 / PAGES > >>
        writer.startElement("tr");
        writer.startElement("td");
        writer.writeAttribute("id", footerId);

        writer.writeAttribute("colspan", table.columnCount);
        writer.writeAttribute("align", "center");
        writer.startElement("table");
        writer.writeAttribute("class", "pageControl");
        writer.startElement("tr");
        writer.startElement("td");


        writer.writeAttribute("class", "footLeft");
        renderLink(writer, "<<", "sp.table.first('" + tableId + "')");
        writer.writeText("&nbsp;");
        renderLink(writer, "<", "sp.table.previous('" + tableId + "')");
        writer.endElement("td");

        writer.startElement("td");
        int width = 20;
        String s = "" + table.maxPages;
        width += width * s.length();
        writer.writeAttribute("width", width);
        writer.writeAttribute("align", "center");
        writer.startElement("span");
        writer.writeAttribute("id", tableId + "PAGE");
        writer.writeAttribute("class", table.getPageControlClass());
        String msg = (table.currentPage + 1) + " / " + (max - 1);
        writer.writeText(msg);
        writer.endElement("span");
        writer.endElement("td");

        writer.startElement("td");

        renderLink(writer, ">", "sp.table.next('" + tableId + "')");
        writer.writeText("&nbsp;");
        renderLink(writer, ">>", "sp.table.last('" + tableId + "')");
        writer.endElement("td");
        writer.endElement("tr");
        writer.endElement("table");

        writer.endElement("td");
        writer.endElement("tr");
    }

    private void renderLink(HTMLWriter writer, String label, String action) {
        writer.startElement("a");
        writer.writeAttribute("onmouseup", action);
        writer.writeAttribute("href", "#");
        writer.writeText(label);
        writer.endElement("a");
    }
    private void renderStatus(HTMLWriter writer, String tableId, List children) {
           String id = table.genStatusId(tableId);

           JSONObject json = new JSONObject();
           json.put("currentPage", table.getCurrentPage());
           json.put("pageSize", table.getPageSize());
           json.put("maxPages", table.getMaxPages());
           json.put("columnCount", table.getColumnCount());
           json.put("sortColumn", table.sortColumn);
           json.put("sortAscending", table.sortAscending);
           json.put("selectable", table.selectable);
           json.put("selectMultiple", table.selectMultiple);
           json.put("selectedIndex", table.selectedIndex);
           json.put("listSize", table.getValue().size());


           String val = json.toString();
           writer.startElement("div");
           writer.writeAttribute("style", "display:none;");
           writer.writeAttribute("id", tableId + "STATUSDIV");
           writer.writeText(val);
           writer.endElement("div");
           writer.startElement("input");
           writer.writeAttribute("type", "hidden");
           writer.writeAttribute("name", id);
           writer.writeAttribute("id", id);


           writer.writeAttribute("value", val);
           writer.endElement("input");
       }

       private void renderEvent(HTMLWriter writer, String tableId) {
           String id = table.genEventId(tableId);

           writer.startElement("input");
           writer.writeAttribute("type", "hidden");
           writer.writeAttribute("name", id);
           writer.writeAttribute("id", id);
           writer.endElement("input");
       }

       private void renderSort(HTMLWriter writer, String tableId){
          String id = table.genSortId(tableId);

           writer.startElement("input");
           writer.writeAttribute("type", "hidden");
           writer.writeAttribute("name", id);
           writer.writeAttribute("id", id);
           writer.endElement("input");
       }

}
