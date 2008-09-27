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
sp.table={
        testPage:0,
        cellListeners: {}
};
sp.table.first=function(tableId){
            //try{                                   
                var status =  sp.status.get(tableId);
                status.currentPage = 0;
                sp.status.set(tableId,status);
                sp.table.render(tableId, status);
            //}catch(e){
            //    sp.log("Error in first",e);
            //}
            return false;
        }
sp.table.last=function(tableId){
           //try{
                var status =  sp.status.get(tableId);
                status.currentPage = status.maxPages-1;
                sp.status.set(tableId,status);
                sp.table.render(tableId, status);
            //}catch(e){
            //    sp.log("Error in last",e);
            //}
            return false;
        };
sp.table.next=function(tableId){
           //try{
            var startTime = new Date().getTime();
            var status =  sp.status.get(tableId);
            //sp.time("NEXT Got Status",startTime);

              // sp.log("Current Page [" +status.currentPage + "] of [" + status.maxPages + "]" );
            if(status.currentPage < (status.maxPages-1)){
                   status.currentPage++;
                  // sp.log("Page Set to[" +status.currentPage + "] of [" + status.maxPages + "]" );

            }else{
                  // sp.log("Page not changed. [" +status.currentPage + "] of [" + status.maxPages + "]" );

            }
            //sp.time("NEXT added Page",startTime);
                        
            sp.status.set(tableId,status);
            //sp.time("NEXT Status set",startTime);
            sp.table.render(tableId, status);
            //sp.time("NEXT Render complete",startTime);
            //}catch(e){
            ///    sp.log("Error in next",e);
            //}
            return false;
        };
sp.table.previous=function(tableId){
            //try{
                var status =  sp.status.get(tableId);
                status.currentPage--;
               if(status.currentPage < 0){
                   status.currentPage = 0;
               }
                sp.status.set(tableId,status);
                sp.table.render(tableId, status);
            //}catch(e){
            //    sp.log("Error in previous", e);
            //}
            return false;
        };

sp.table.render=function(tableId, status){
            if(!status){
                status =  sp.status.get(tableId);
            }
            var startTime = new Date().getTime();
            //sp.time("Render Start",startTime);
            var page = status.currentPage;
            var start = page * status.pageSize;
            var end = start + status.pageSize;
            //sp.time("R:1",startTime);
            var style = sp.table.getStyle(tableId);

            var tBody = $(tableId + "BODY");
            if(!tBody){
                sp.log("Table Body [" + tableId + "BODY" + "] Not found");
            }
            sp.table.removeCellListeners(tableId);
            var cell = tBody;
            if ( cell.hasChildNodes() )
            {
                while ( cell.childNodes.length >= 1 )
                {
                    cell.removeChild( cell.firstChild );
                }
            }
            var row = 0;
            var renderedRow = 0;
            //sp.time("R:2",startTime);
            for(row=start;row<end;row++){
                var rowStartTime = new Date().getTime();
                //sp.time("R:L:0-BEAT-CHECK",rowStartTime);
                //sp.time("R:L:0-ROW-START",rowStartTime);
                var id = tableId + "DATAROW" + row;
                //sp.time("R:L:0-ID Built",rowStartTime);

                var dataDiv = $(id);
                //sp.time("R:L:0-Got Element",rowStartTime);

                if(!dataDiv){                                        
                    sp.log("Data Row [" + id + "] not found");
                    if(row < status.listSize){
                        // Get more data from the server
                        sp.table.getMoreRows(tableId, status, row);
                    }
                    break;
                }
                // I might need to attach all at once to avoid
                // renereding the entire page
                var tr = document.createElement('tr');
                //sp.time("R:L:0-Created Row",rowStartTime);
                var jsonRow = null;
                try{
                	jsonRow = eval('(' + dataDiv.innerHTML + ')');
                }catch(e){
                	alert('Error in [' + dataDiv.innerHTML + "]");
                	throw e;
                }
                tr.className = jsonRow.defaultClass;

                //sp.time("R:L:0-Set Style",rowStartTime);

                ////sp.time("R:L:1",startTime);
                renderedRow++;
                // TODO: Row color / select on or off / selected need to be stored in JSON
                //sp.log("Row HTML [" + dataDiv.innerHTML + "]");
                //sp.time("R:L:1:JSON-START", rowStartTime);

                //sp.time("R:L:1:JSON-END", rowStartTime);
                var selectedClass = jsonRow.selectedClass;
                ////sp.time("R:Selected [" + jsonRow.selected + "]",startTime);
                if(status.selectMultiple){
                    if((sp.isTrue(jsonRow.selected)) && selectedClass != null && selectedClass != ''){
                        tr.className = selectedClass;
                    }
                }else{
                    if(status.selectedIndex == jsonRow.listIndex){
                        tr.className = selectedClass; 
                    }
                }
                //sp.time("R:L:2-set classname",rowStartTime);
                var cells = jsonRow.row;
                var c = 0;
                for(c=0;c<cells.length;c++){
                    ////sp.time("R:L:L:0",startTime);
                    var td = document.createElement('td');
                    //sp.time("R:L:L:1",rowStartTime);
                    if(status.selectable){
                        //sp.log("R:L:L:2",rowStartTime);
                        sp.table.addCellListeners(tableId, row, c, td);
                        //sp.log("R:L:L:3",rowStartTime);

                    }
                    //sp.time("R:L:L:4",rowStartTime);
                    ////sp.time("R:L:L:3",startTime);
                    //TODO: Get all of the attributes of the cell as well
                    var json = cells[c];
                    //sp.time("R:L:L:5",rowStartTime);
                    td.innerHTML = sp.table.rebuildHTML(json.value);
                    //sp.time("R:L:L:6",rowStartTime);
                    td.align=json.align;
                    
                    td.valign=json.vAlign;
                    td.className = json.columnClass;
                    //sp.time("R:L:L:7", rowStartTime);
                    tr.appendChild(td);
                    ////sp.time("R:L:L:7-TD appeneded", rowStartTime);
                }
                ////sp.time("R:L:4",rowStartTime);
                tBody.appendChild(tr);
                ////sp.time("R:L:5-ROW-END",rowStartTime);
            }
            ////sp.time("R:3",startTime);
            sp.table.renderFooter(tableId, status);
            //sp.time("Render End",startTime);

            ////sp.time("R:4-Render Complete", startTime);
        }

 sp.table.getMoreRows=function(tableId, status, row){
            var tBody = $(tableId + "BODY");
            var tr = document.createElement("tr");
            var td = document.createElement("td");
            td.colSpan=status.columnCount;
            td.rowSpan=status.pageSize;
            status.moreRows = row;
            td.vAlign='center';
            td.innerHTML='...LOADING...';
            tr.appendChild(td);
            tBody.appendChild(tr);
            sp.status.set(tableId,status);
            setTimeout("sp.table.render('" + tableId + "');", 100);
            var form = sp.form(tBody);
            iceSubmit(form, tBody, null);
        };

sp.table.rebuildHTML=function(escaped, includeGtLt){

            var result = escaped.replace(/\&amp\;/g, '&');;
            if(includeGtLt){
                result = result.replace(/\&lt\;/g, '<');
                result = result.replace(/\&gt\;/g, '>');
            }
            result = result.replace(/\&Slt\;/g, '<');
            result = result.replace(/\&Sgt\;/g, '>');
            result = result.replace(/\&039\;/g, '\'');
            result = result.replace(/\&\#039\;/g, '\'');
            result = result.replace(/\&040\;/g, '(');
            result = result.replace(/\&041\;/g, ')');
            result = result.replace(/\&035\;/g, '#');
            result = result.replace(/\&037\;/g, '%');
            result = result.replace(/\&059\;/g, ';');
            result = result.replace(/\&043\;/g, '+');
            result = result.replace(/\&045\;/g, '-');
            //result = result.replace(/\&amp\;/g, '&');
             sp.log("before [" + escaped + "] after [" + result + "]");
            return result;
        };
 sp.table.escapeForJson=function(html){
        	html = html+'';
        	var result = html.replace(/\'/g,"\\'");
        	return result;
}
 
 sp.table.addCellListeners=function(tableId, row, cell, td){
            var mov =  function(event){
                    sp.table.mouseover(tableId, row, cell, event.target);
                };
            var mou = function(event){
                    sp.table.mouseout(tableId, row, cell, event.target);
                };
            var mc = function(event){
                    sp.table.click(tableId, row, cell, event.target, event.shiftKey, event.ctrlKey);
                };
            td = $(td);

            td.observe('mouseover',
                mov,true);

            td.observe("mouseout",
                mou,true);

            td.observe("click",
                mc,true);
            var listenerArray = sp.table.cellListeners[tableId];
            if(!listenerArray){
                listenerArray = new Array();
                sp.table.cellListeners[tableId] = listenerArray;
            }
            listenerArray[listenerArray.length] = {
                td:td,
                mov:mov,
                mou:mou,
                mc:mc
            };
        };

sp.table.removeCellListeners=function(tableId){
            var listenerArray = sp.table.cellListeners[tableId];
            if(!listenerArray)return;
            var i = 0;
            for(i=0;i<listenerArray.length;i++){
                var o = listenerArray[i];
                Event.stopObserving(o.td, 'mouseover',o.mov);
                Event.stopObserving(o.td, 'mouseout', o.mou);
                Event.stopObserving(o.td, 'click', o.mc);
            }
            sp.table.cellListeners[tableId] = new Array();
        }
        

sp.table.renderFooter=function(tableId, status){
            var span = $(tableId + "PAGE");
            span.innerHTML = (status.currentPage+1) + " / " + status.maxPages;
        }

sp.table.toggleSort=function(tableId, columnIndex){
            // &darr; &uarr;
            // Clear all columns sort index
            var headerRow = $(tableId + "HEADERROW");
            var i = 0;
            var setHeader = function(headerIndex, headerValue){
                var he =
                $( tableId
                                + "TH" + headerIndex
                                + "DIR");
                if(he){
                    he.innerHTML = headerValue;
                }
                //sp.log(he.innerHTML);
            }

            var status = sp.status.get(tableId);
            for(i=0; i < status.columnCount; i++){
                if(i == columnIndex){
                    if(i == status.sortColumn){
                        if(status.sortAscending){
                          setHeader(i, "&uarr;");
                        }else{
                            setHeader(i, "&darr;");
                        }
                        status.sortAscending = !status.sortAscending;
                    }else{
                        setHeader(i, "&darr;");
                        status.sortColumn = columnIndex;
                        status.sortAscending = true;
                    }
                }else{
                    setHeader(i, "");
                }
            }

            

            //TODO: Use a sortable value to handle things like calendars etc
            // Build up a sortable array
            var array = new Array();
            for(i=0;i<(status.maxPages*status.pageSize);i++){
                var dataDiv = $(tableId + "DATAROW" + i);
                if(!dataDiv){
                    break;
                }else{
                    var json = dataDiv.innerHTML;
                    var jsonRow = eval('(' + json + ')');
                    var cells = jsonRow.row;
                    var cell = cells[columnIndex];
                    array[array.length] = {                           
                        value:cell.value,
                        sort:cell.sort,
                        index:i,
                        row:json,
                        listIndex:jsonRow.listIndex
                    }
                }
            }
            // Sort
            array.sort(function(a,b){
                var sa = a.sort
                var sb = b.sort;
                if(!isNaN(sa)){
                    sa = parseFloat(sa);
                    sb = parseFloat(sb);
                }
                if(sa == sb)return 0;
                if(sa < sb)return status.sortAscending?-1:1;
                return status.sortAscending?1:-1;
            });
            var sortList = "";
            // Put the data back
            for(i=0;i<array.length;i++){
                var rowDataDiv = $(tableId + "DATAROW" + i);
                if(rowDataDiv){
                    rowDataDiv.innerHTML = array[i].row;
                    sortList += array[i].listIndex + ", ";
                }
            }
            $(tableId + "SPSORT").value = sortList;
            sp.status.set(tableId, status);
            // Redraw the current page
            sp.table.render(tableId, status);
        };

 sp.table.mouseover=function(tableId, row,cell, td){
            //sp.log("OVER");
            if(td.parentNode){
                $(tableId + "ROWTEMP").value = td.parentNode.className;
                td.parentNode.className = 'mouseOver';
            }
        };
 sp.table.mouseout=function(tableId, row, cell, td){
            //sp.log("OUT");
            if(td.parentNode){
                td.parentNode.className = $(tableId + "ROWTEMP").value;
            }
        }

 sp.table.click=function(tableId, row, col, td, shiftKey, ctrlKey){
        	if(shiftKey)alert('Shift key');
            var id = tableId + "DATAROW" + row;
            var dataDiv = $(id);
            var json = eval("(" + dataDiv.innerHTML + ")");
            var cells = json.row;
            var listIndex = json.listIndex;
            var i = 0;
            for(i = 0; i < cells.length; i++){
                var jsonCell = cells[i];
                //sp.log("CLICK Row " + row + " index " + json.listIndex );
                if (jsonCell.populate) {
                    //sp.log("Populate [" + jsonCell.populate + "] with [" + jsonCell.value + "]");
                    var ele = $(jsonCell.populate);
                    if (ele) {
                        if (ele.nodeName == 'INPUT') {
                            if (ele.type == 'checkbox') {
                                ele.checked = sp.isTrue(jsonCell.populateValue);
                            } else {
                                ele.value = sp.table.rebuildHTML(jsonCell.populateValue, true);
                            }
                        } else if (ele.nodeName == 'TEXTAREA') {
                            ele.value = sp.table.rebuildHTML(jsonCell.populateValue, true);
                        } else {
                            ele.value = jsonCell.populateValue;
                        }
                        //TODO Render innerHTML of output text
                    }
                }
            }
            var status =  sp.status.get(tableId);

            var selected = !(sp.isTrue(json.selected));
            if(!(status.selectMultiple == true)){
            	console.log("Select Single");
                if(status.selectedIndex == json.listIndex){
                    selected = false;
                    status.selectedIndex = -1;
                }else{
                    status.selectedIndex = json.listIndex;
                    selected = true;
                }
                sp.status.set(tableId, status, true);
            }else{
            	console.log("Select multiple");
                json.selected = selected;
                var jsonString = sp.table.jsonToString(json);
                dataDiv.innerHTML = jsonString;
            }
            sp.table.render(tableId, status);
            // Fire Event to the server
            var eventTxtId  = tableId + "SPEVENT";
            var eventTxt = $(eventTxtId);
            eventTxt.value = "{'row':" + listIndex + ",'col':" + col + ", 'selected':" + selected + "}";
            sp.ev.submitSoon(eventTxtId);
        };
 sp.table.getStyle=function(tableId){
           return eval("(" + $(tableId + 'STYLE').innerHTML +")");
        };
        
sp.table.jsonToString=function(jsonObj){
            var x;
            var y;
            var i = 0;
            var result = "{";
            for (x in jsonObj) {
                if(typeof(jsonObj[x]) != 'function'){
                    if(x == 'row'){
                        result += "row:[";
                        var row = jsonObj[x];
                        for(i=0;i<row.length;i++){
                            result += "{";
                            var cell = row[i];
                            for(y in cell){
                                if(typeof(cell[y]) != 'function'){
                                    result += y + ":'" +  sp.table.escapeForJson(cell[y]) + "',";
                                }
                            }
                            result += "},";
                        }
                        result += "],";
                    }else{
                        result += x + ":'" + sp.table.escapeForJson(jsonObj[x]) + "',";
                    }
                }
            }
            result += "d:true}";
            return result;
        };
    
 sp.status = {};
 sp.status.get=function(rootId){
            var id = rootId + "STATUSDIV";
            var field = $(id);
            return eval('(' + field.innerHTML + ')');
        };
 sp.status.set=function(rootId, status, skipSubmit){
            var id = rootId + "SPSTATUS";
            var field = $(id);

            var s = JSON.stringify(status);
            field.value = s;
            // Send it up
            if(!skipSubmit){
                sp.ev.submitSoon(id);
            }
            $(rootId + "STATUSDIV").innerHTML = s;
        }

   
 sp.isTrue=function(b){
        return b+"" == 'true';
 };
    
 sp.ev={
		 waitingSubmits:new Array()
 };
 sp.ev.loop=function() {

        };
 sp.ev.start=function() {

        };
 sp.ev.add=function(call) {

        };
 sp.ev.remove=function(call) {


        };
 sp.ev.submitSoon=function(targetId){
            var now = new Date().getTime();
            var submit = null;
            for(var i =0; i < sp.ev.waitingSubmits.length; i++){
                var s = sp.ev.waitingSubmits[i];
                if(s.targetId == targetId){
                    submit = s;
                    break;
                }
            }
            // User might be rapidly clicking. Don't send a submit until its clear they are done.
            var waitTime = 600; //0.6 sec seams about right
            if(!submit){
                submit = new Object();
                submit.fireTime = now + waitTime;
                submit.fired = false;
                submit.targetId = targetId;
                sp.ev.waitingSubmits[sp.ev.waitingSubmits.length] = submit;
            }else{
                submit.fireTime = now + waitTime;
                submit.fired = false;
            }
            setTimeout("sp.ev.fireSubmit()",50);

        };
sp.ev.fireSubmit=function(){
            var now = new Date().getTime();
            var waiting = false;
            for(var i =0; i < sp.ev.waitingSubmits.length; i++){
                var submit = sp.ev.waitingSubmits[i];
                if(!submit.fired && now > submit.fireTime){
                    submit.fired = true;
                    var target = $(submit.targetId);
                    if(!target)return;
                    var form = sp.form(target);
                    iceSubmit(form, target, null);
                }else{
                    if(!submit.fired)
                        waiting = true;
                }
            }
            if(waiting)
                setTimeout("sp.ev.fireSubmit()",50);
        };

sp.form=function(element){
        var nodeName = element.nodeName.toUpperCase();
        if(nodeName == 'FORM'){return element;}
        if(element.parentNode == null){return null;}
        return sp.form(element.parentNode);
    };
sp.log=function(s){
        //window.logger.debug(s);
    };
sp.time=function(s, start){
        var now = new Date().getTime();
        window.logger.debug(s + ">" + (now - start));
    };


/*
    http://www.JSON.org/json2.js
    2008-04-29

    Public Domain.

    NO WARRANTY EXPRESSED OR IMPLIED. USE AT YOUR OWN RISK.

    See http://www.JSON.org/js.html

    This file creates a global JSON object containing two methods: stringify
    and parse.


        JSON.stringify(value, replacer, space, linebreak)
            value       any JavaScript value, usually an object or array.

            replacer    an optional parameter that determines how object
                        values are stringified for objects without a toJSON
                        method. It can be a function or an array.

            space       an optional parameter that specifies the indentation
                        of nested structures. If it is omitted, the text will
                        be packed without extra whitespace. If it is a number,
                        it will specify the number of spaces to indent at each
                        level. If it is a string (such as '\t' or '&nbsp;'),
                        it contains the characters used to indent at each level.

            linebreak   an optional parameter that specifies the text used to
                        break lines, such as '<br>' or '\r\n'. It is used with
                        the space parameter. The default is '\n'.

            This method produces a JSON text from a JavaScript value.

            When an object value is found, if the object contains a toJSON
            method, its toJSON method will be called and the result will be
            stringified. A toJSON method does not serialize: it returns the
            value represented by the name/value pair that should be serialized,
            or undefined if nothing should be serialized. The toJSON method
            will be passed the key associated with the value, and this will be
            bound to the object holding the key.

            For example, this would serialize Dates as ISO strings.

                Date.prototype.toJSON = function (key) {
                    function f(n) {
                        // Format integers to have at least two digits.
                        return n < 10 ? '0' + n : n;
                    }

                    return this.getUTCFullYear()   + '-' +
                         f(this.getUTCMonth() + 1) + '-' +
                         f(this.getUTCDate())      + 'T' +
                         f(this.getUTCHours())     + ':' +
                         f(this.getUTCMinutes())   + ':' +
                         f(this.getUTCSeconds())   + 'Z';
                };

            You can provide an optional replacer method. It will be passed the
            key and value of each member, with this bound to the containing
            object. The value that is returned from your method will be
            serialized. If your method returns undefined, then the member will
            be excluded from the serialization.

            If the replacer parameter is an array, then it will be used to
            select the members to be serialized. It filters the results such
            that only members with keys listed in the replacer array are
            stringified.

            Values that do not have JSON representaions, such as undefined or
            functions, will not be serialized. Such values in objects will be
            dropped; in arrays they will be replaced with null. You can use
            a replacer function to replace those with JSON values.
            JSON.stringify(undefined) returns undefined.

            The optional space parameter produces a stringification of the
            value that is filled with line breaks and indentation to make it
            easier to read.

            If the space parameter is a non-empty string, then that string will
            be used for indentation. If the space parameter is a number, then
            then indentation will be that many spaces.

            Example:

            text = JSON.stringify(['e', {pluribus: 'unum'}]);
            // text is '["e",{"pluribus":"unum"}]'


            text = JSON.stringify(['e', {pluribus: 'unum'}], null, '\t');
            // text is '[\n\t"e",\n\t{\n\t\t"pluribus": "unum"\n\t}\n]'

            text = JSON.stringify([new Date()], function (key, value) {
                return this[key] instanceof Date ?
                    'Date(' + this[key] + ')' : value;
            });
            // text is '["Date(---current time---)"]'


        JSON.parse(text, reviver)
            This method parses a JSON text to produce an object or array.
            It can throw a SyntaxError exception.

            The optional reviver parameter is a function that can filter and
            transform the results. It receives each of the keys and values,
            and its return value is used instead of the original value.
            If it returns what it received, then the structure is not modified.
            If it returns undefined then the member is deleted.

            Example:

            // Parse the text. Values that look like ISO date strings will
            // be converted to Date objects.

            myData = JSON.parse(text, function (key, value) {
                var a;
                if (typeof value === 'string') {
                    a =
/^(\d{4})-(\d{2})-(\d{2})T(\d{2}):(\d{2}):(\d{2}(?:\.\d*)?)Z$/.exec(value);
                    if (a) {
                        return new Date(Date.UTC(+a[1], +a[2] - 1, +a[3], +a[4],
                            +a[5], +a[6]));
                    }
                }
                return value;
            });

            myData = JSON.parse('["Date(09/09/2001)"]', function (key, value) {
                var d;
                if (typeof value === 'string' &&
                        value.slice(0, 5) === 'Date(' &&
                        value.slice(-1) === ')') {
                    d = new Date(value.slice(5, -1));
                    if (d) {
                        return d;
                    }
                }
                return value;
            });


    This is a reference implementation. You are free to copy, modify, or
    redistribute.

    This code should be minified before deployment.
    See http://javascript.crockford.com/jsmin.html

    USE YOUR OWN COPY. IT IS EXTREMELY UNWISE TO LOAD THIRD PARTY
    CODE INTO YOUR PAGES.
*/

/*jslint evil: true */

/*global JSON */

/*members "", "\b", "\t", "\n", "\f", "\r", "\"", JSON, "\\", call,
    charCodeAt, floor, getUTCDate, getUTCFullYear, getUTCHours,
    getUTCMinutes, getUTCMonth, getUTCSeconds, hasOwnProperty, join, length,
    parse, propertyIsEnumerable, prototype, push, quote, replace, stringify,
    test, toJSON, toString
*/

if (!this.JSON) {

// Create a JSON object only if one does not already exist. We create the
// object in a closure to avoid global variables.

    JSON = function () {

        function f(n) {
            // Format integers to have at least two digits.
            return n < 10 ? '0' + n : n;
        }

        Date.prototype.toJSON = function (key) {

            return this.getUTCFullYear()   + '-' +
                 f(this.getUTCMonth() + 1) + '-' +
                 f(this.getUTCDate())      + 'T' +
                 f(this.getUTCHours())     + ':' +
                 f(this.getUTCMinutes())   + ':' +
                 f(this.getUTCSeconds())   + 'Z';
        };

        var escapeable = /["\\\x00-\x1f\x7f-\x9f]/g,
            gap,
            indent,
            breaker,
            meta = {    // table of character substitutions
                '\b': '\\b',
                '\t': '\\t',
                '\n': '\\n',
                '\f': '\\f',
                '\r': '\\r',
                '"' : '\\"',
                '\\': '\\\\'
            },
            rep;


        function quote(string) {

// If the string contains no control characters, no quote characters, and no
// backslash characters, then we can safely slap some quotes around it.
// Otherwise we must also replace the offending characters with safe escape
// sequences.

            return escapeable.test(string) ?
                '"' + string.replace(escapeable, function (a) {
                    var c = meta[a];
                    if (typeof c === 'string') {
                        return c;
                    }
                    c = a.charCodeAt();
                    return '\\u00' + Math.floor(c / 16).toString(16) +
                                               (c % 16).toString(16);
                }) + '"' :
                '"' + string + '"';
        }


        function str(key, holder) {

// Produce a string from holder[key].

            var i,          // The loop counter.
                k,          // The member key.
                v,          // The member value.
                length,
                mind = gap,
                partial,
                value = holder[key];

// If the value has a toJSON method, call it to obtain a replacement value.

            if (value && typeof value === 'object' &&
                    typeof value.toJSON === 'function') {
                value = value.toJSON(key);
            }

// If we were called with a replacer function, then call the replacer to
// obtain a replacement value.

            if (typeof rep === 'function') {
                value = rep.call(holder, key, value);
            }

// What happens next depends on the value's type.

            switch (typeof value) {
            case 'string':
                return quote(value);

            case 'number':

// JSON numbers must be finite. Encode non-finite numbers as null.

                return isFinite(value) ? String(value) : 'null';

            case 'boolean':
            case 'null':

// If the value is a boolean or null, convert it to a string. Note:
// typeof null does not produce 'null'. The case is included here in
// the remote chance that this gets fixed someday.

                return String(value);

// If the type is 'object', we might be dealing with an object or an array or
// null.

            case 'object':

// Due to a specification blunder in ECMAScript, typeof null is 'object',
// so watch out for that case.

                if (!value) {
                    return 'null';
                }

// Make an array to hold the partial results of stringifying this object value.

                gap += indent;
                partial = [];

// If the object has a dontEnum length property, we'll treat it as an array.

                if (typeof value.length === 'number' &&
                        !(value.propertyIsEnumerable('length'))) {

// The object is an array. Stringify every element. Use null as a placeholder
// for non-JSON values.

                    length = value.length;
                    for (i = 0; i < length; i += 1) {
                        partial[i] = str(i, value) || 'null';
                    }

// Join all of the elements together, separated with commas, and wrap them in
// brackets.

                    v = partial.length === 0 ? '[]' :
                        gap ? '[' + breaker + gap +
                                partial.join(',' + breaker + gap) + breaker +
                                    mind + ']' :
                              '[' + partial.join(',') + ']';
                    gap = mind;
                    return v;
                }

// If the replacer is an array, use it to select the members to be stringified.

                if (rep && typeof rep === 'object') {
                    length = rep.length;
                    for (i = 0; i < length; i += 1) {
                        k = rep[i];
                        if (typeof k === 'string') {
                            v = str(k, value, rep);
                            if (v) {
                                partial.push(quote(k) + (gap ? ': ' : ':') + v);
                            }
                        }
                    }
                } else {

// Otherwise, iterate through all of the keys in the object.

                    for (k in value) {
                        if (Object.hasOwnProperty.call(value, k)) {
                            v = str(k, value, rep);
                            if (v) {
                                partial.push(quote(k) + (gap ? ': ' : ':') + v);
                            }
                        }
                    }
                }

// Join all of the member texts together, separated with commas,
// and wrap them in braces.

                v = partial.length === 0 ? '{}' :
                    gap ? '{' + breaker + gap +
                            partial.join(',' + breaker + gap) + breaker +
                            mind + '}' :
                          '{' + partial.join(',') + '}';
                gap = mind;
                return v;
            }
        }


// Return the JSON object containing the stringify, parse, and quote methods.

        return {
            stringify: function (value, replacer, space, linebreak) {

// The stringify method takes a value and an optional replacer, and an optional
// space parameter, and returns a JSON text. The replacer can be a function
// that can replace values, or an array of strings that will select the keys.
// A default replacer method can be provided. Use of the space parameter can
// produce text that is more easily readable.

                var i;
                gap = '';
                indent = '';
                if (space) {

// If the space parameter is a number, make an indent string containing that
// many spaces.

                    if (typeof space === 'number') {
                        for (i = 0; i < space; i += 1) {
                            indent += ' ';
                        }

// If the space parameter is a string, it will be used as the indent string.

                    } else if (typeof space === 'string') {
                        indent = space;
                    }
                    breaker = typeof linebreak !== 'string' ? '\n' : linebreak;
                }

// If there is a replacer, it must be a function or an array.
// Otherwise, throw an error.

                rep = replacer;
                if (replacer && typeof replacer !== 'function' &&
                        (typeof replacer !== 'object' ||
                         typeof replacer.length !== 'number')) {
                    throw new Error('JSON.stringify');
                }

// Make a fake root object containing our value under the key of ''.
// Return the result of stringifying the value.

                return str('', {'': value});
            },


            parse: function (text, reviver) {

// The parse method takes a text and an optional reviver function, and returns
// a JavaScript value if the text is a valid JSON text.

                var j;

                function walk(holder, key) {

// The walk method is used to recursively walk the resulting structure so
// that modifications can be made.

                    var k, v, value = holder[key];
                    if (value && typeof value === 'object') {
                        for (k in value) {
                            if (Object.hasOwnProperty.call(value, k)) {
                                v = walk(value, k);
                                if (v !== undefined) {
                                    value[k] = v;
                                } else {
                                    delete value[k];
                                }
                            }
                        }
                    }
                    return reviver.call(holder, key, value);
                }


// Parsing happens in three stages. In the first stage, we run the text against
// regular expressions that look for non-JSON patterns. We are especially
// concerned with '()' and 'new' because they can cause invocation, and '='
// because it can cause mutation. But just to be safe, we want to reject all
// unexpected forms.

// We split the first stage into 4 regexp operations in order to work around
// crippling inefficiencies in IE's and Safari's regexp engines. First we
// replace all backslash pairs with '@' (a non-JSON character). Second, we
// replace all simple value tokens with ']' characters. Third, we delete all
// open brackets that follow a colon or comma or that begin the text. Finally,
// we look to see that the remaining characters are only whitespace or ']' or
// ',' or ':' or '{' or '}'. If that is so, then the text is safe for eval.

                if (/^[\],:{}\s]*$/.test(text.replace(/\\["\\\/bfnrtu]/g, '@').
replace(/"[^"\\\n\r]*"|true|false|null|-?\d+(?:\.\d*)?(?:[eE][+\-]?\d+)?/g, ']').
replace(/(?:^|:|,)(?:\s*\[)+/g, ''))) {

// In the second stage we use the eval function to compile the text into a
// JavaScript structure. The '{' operator is subject to a syntactic ambiguity
// in JavaScript: it can begin a block or an object literal. We wrap the text
// in parens to eliminate the ambiguity.

                    j = eval('(' + text + ')');

// In the optional third stage, we recursively walk the new structure, passing
// each name/value pair to a reviver function for possible transformation.

                    return typeof reviver === 'function' ?
                        walk({'': j}, '') : j;
                }

// If the text is not JSON parseable, then a SyntaxError is thrown.

                throw new SyntaxError('JSON.parse');
            }
        };
    }();
}

