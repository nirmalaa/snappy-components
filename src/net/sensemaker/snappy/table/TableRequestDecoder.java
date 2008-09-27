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

import org.json.JSONObject;

import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import java.util.StringTokenizer;
import java.util.logging.Logger;

public class TableRequestDecoder {

	private static Logger log = Logger.getLogger(TableRequestDecoder.class.getName());
	
    void decode(FacesContext context, SnappyTable table) {
        if(table.getValue() == null)return; //TODO WTF is going on?
        String status =
                (String) context
                        .getExternalContext()
                        .getRequestParameterMap()
                        .get(table.genStatusId(table.getClientId(context)));
        String event =
                (String) context
                        .getExternalContext()
                        .getRequestParameterMap()
                        .get(table.genEventId(table.getClientId(context)));

        String sort =
                (String) context
                        .getExternalContext()
                        .getRequestParameterMap()
                        .get(table.genSortId(table.getClientId(context)));
        SnappyTableEvent snappyTableEvent = decode(status, event, sort,context,table);

        if (snappyTableEvent != null) {
            fireEvent(snappyTableEvent.getRow(), snappyTableEvent.getCol(),table);
        }
    }

    void fireEvent(int row, int col, SnappyTable table) {
        SnappyRowSelectionEvent sEvt = new SnappyRowSelectionEvent(table);
        sEvt.setTargetRow(row);
        sEvt.setTargetCol(col);
        sEvt.setPhaseId(PhaseId.APPLY_REQUEST_VALUES);
        table.queueEvent(sEvt);
    }

    public SnappyTableEvent decode(String status,
    		String event, 
    		String sort, 
    		FacesContext context, 
    		SnappyTable table) {
          if(status == null)return null;
          if(sort != null)
              sort = sort.trim();
          table.sortedIndex.clear();
          try{
              StringTokenizer st = new StringTokenizer(sort, ",");
              while(st.hasMoreTokens()){
                  table.sortedIndex.add(Integer.parseInt(st.nextToken().trim()));
              }
          }catch(Exception e){
              e.printStackTrace();
          }
          JSONObject json = null;
          try {
          /**
           * NOTE: We don't want to rerneder the table if it is alreay properly
           * displayed. In order to do this it is nessisary to set certain ingore
           * cases where even though the logic will detect a table change it will
           * not rerender. The flag for this is skipTableRender
           */
              JSONObject lastStatusJson = new JSONObject();
              if (table.lastStatus != null)
                  lastStatusJson = new JSONObject(table.lastStatus);
              json = new JSONObject(status);
              table.skipTableRender = table.bypassCtrl.shouldBypass(lastStatusJson, json, table.lastEvent, event);
              if(json.has("moreRows")){
                  int moreRows = json.getInt("moreRows");
                  int halfBlock = (table.getChunkSize() *table.getPageSize());
                  int start  = moreRows - halfBlock;
                  int end = moreRows + halfBlock;
                  int firstBlockEnd = table.getFirstBlockEnd();
                  int lastBlockStart= table.getLastBlockStart();
                  if(start <= firstBlockEnd){
                      table.extraStart = firstBlockEnd;
                  }else{
                      table.extraStart = start;
                  }
                  if(end >= lastBlockStart){
                      table.extraEnd = lastBlockStart;
                  }else{
                      table.extraEnd = end;
                  }

              }
              table.lastStatus = status;
          } catch (Exception e) {
              e.printStackTrace();
          }

          int currentPage = json.getInt("currentPage");

          table.currentPage = currentPage;
          //table.setPagei = json.getInt("pageSize");
          table.maxPages = json.getInt("maxPages");
          int lastSelectedIndex = table.selectedIndex;
          table.selectedIndex = json.getInt("selectedIndex");


          if (event != null && event.trim().length() > 0 && !event.equals(table.lastEvent)) {
              table.lastEvent = event;
              json = new JSONObject(event);
              boolean selected = json.getBoolean("selected");
              log.finest("Event Selected [" + selected + "]" + "Row " + json.getInt("row") + "Multiple " + table.isSelectMultiple());
              if (!table.isSelectMultiple()) {
                  if (selected && lastSelectedIndex != -1) {
                      table.setRowSelected(lastSelectedIndex, false);
                  } else {
                      if (!selected) {
                          table.selectedIndex = -1;
                      }
                  }
              }
              table.setRowSelected(json.getInt("row"), selected);

              
              return new SnappyTableEvent(json.getInt("row"), json.getInt("col"));

          }

          return null;
      }


}
