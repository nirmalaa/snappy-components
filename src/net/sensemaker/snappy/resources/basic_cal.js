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
sp_calendar = {
    calendars:[],
    slideing:[],
    listeners:[],
    DIV_ID:'calDiv',
    LINK_ID:'link',
    display:function(inputId){
        var date = sp_calendar.calendars[inputId];
        if(!date){
            date = new Date();
            date.setDate(1);
            sp_calendar.calendars[inputId] = date;
        }
        sp_calendar.show(inputId, date);
        
    },
    back:function(inputId,event){
       var date = sp_calendar.calendars[inputId];
       var y = date.getFullYear();
       var m = date.getMonth();
        if(event.shiftKey){
            y--;
        }else{
            m--;
            if(m < 0){
                m = 11;
                y--;
            }
        }
        date = new Date(y,m,1);

        sp_calendar.calendars[inputId] = date;
        sp_calendar.render(inputId, date);
    },
    forward:function(inputId, event){
       var date = sp_calendar.calendars[inputId];
       var y = date.getFullYear();
       var m = date.getMonth();

        if(event.shiftKey){
            y++;
        }else{

            m++;
            if(m > 11){
                m = 0;
                y++;
            }
        }
        date = new Date(y,m,1);
        sp_calendar.calendars[inputId] = date;
        sp_calendar.render(inputId, date);
    },
    show:function(inputId, date){
        var id = inputId + sp_calendar.DIV_ID;
        var div = $(id);
        var slideDown = true;
        if(div){
            if(div.style.display!='none'){
                slideDown = false;
                //div.style.display='none';
                //return;
            }
            //div.style.display='block';
        }else{
            var targetId = inputId;
            var linkId = inputId + sp_calendar.LINK_ID;
            if($(linkId)){targetId = linkId;}
           var d = Position.cumulativeOffset($(targetId));
           div = document.createElement("div");
           div.className = $(inputId).className; //'snappyCalendar';
           div.style.position='absolute';
           div.style.display='none';
           div.style.top = (d[1]+25) +'';
           div.style.left = (d[0]) + '';
           div.id = id;
           document.body.appendChild(div);
           var tempDiv = document.createElement('div');
           tempDiv.id = inputId + '_iefixDiv';
           tempDiv.innerHTML =  
         	       '<iframe id="' + inputId + '_iefix" '+
         	       'style="display:none;position:absolute;filter:progid:DXImageTransform.Microsoft.Alpha(opacity=0);" ' +
         	       'src="javascript:false;" frameborder="0" scrolling="no"></iframe>';
           document.body.appendChild(tempDiv);
           
           ieFix = $(inputId + '_ieFix');
           if(ieFix != null){
        	   ieFix.style.top = div.style.top;
        	   ieFix.style.left = div.style.left; 
        	   ieFix.style.width = div.style.width; 
        	   ieFix.style.height = div.style.height; 
        	   ieFix.style.display = 'block';
        	   ieFix.style.zIndex = 1;
           }
        }
        //div.style.display='block';
        var ieFix = $(inputId + '_ieFix');
        
        div.style.zIndex = 2;
        
        sp_calendar.parse(inputId);
        if(slideDown){
        	
            sp_calendar.slideing[inputId] = false;
            Effect.SlideDown(div,{duration:0.25});
            $(inputId + '_iefixDiv').style.display='block';
        }else{
        	
            if(!sp_calendar.slideing[inputId]){
                sp_calendar.slideing[inputId] = true;
            
                Effect.SlideUp(div,{duration:0.25});
                $(inputId + '_iefixDiv').style.display='none';
            }
        }
    },
    render:function(inputId, date){
        var id = inputId + sp_calendar.DIV_ID;
        var div = $(id);

        sp_calendar.renderCal(div, inputId, date);
    },
    dow:function(i,ar){
        return ar[i];
    },
   
    pop:function(inputId, dateString, event){
        var format = sp_calendar.format(inputId);
        var dateArr = dateString.split(":");
        var m = (parseInt(dateArr[1])+1);
        var d = (parseInt(dateArr[2]));
        if(m < 10)m = '0' + m;
        if(d < 10)d = '0' + d;
        format = format.replace('yyyy', dateArr[0]);
        format = format.replace('MM', m);
        format = format.replace('dd', d);
        $(inputId).value = format;
        var linkId = inputId + sp_calendar.LINK_ID;
        var link = $(linkId);
        if(link){
            link.innerHTML = format;
        }
        var id = inputId + sp_calendar.DIV_ID;
        var div = $(id);
        if(!event){

        }else{
            if(sp_calendar.slideing[inputId])return;                
            sp_calendar.slideing[inputId] = true;
            var element = event.element();

            new Effect.Highlight(element,{startcolor:'#fffc00',endcolor:'#ffffff',duration:0.4,afterFinish:
                    function(){
                    Effect.SlideUp(div, {delay:0.1,duration:0.25})}});
        }
        var flagsId = inputId + "flags";
        var flags = $(flagsId);
        if(flags.innerHTML=='true'){

                sp_calendar.form($(inputId)).submit();

          
        }
    },
    form:function(element){
        var nodeName = element.nodeName.toUpperCase();
        if(nodeName == 'FORM'){return element;}
        if(element.parentNode == null){return null;}
        return sp_calendar.form(element.parentNode);
    },
    parse_date:function(value, format){
        var month = format.indexOf("MM");
        var day = format.indexOf("dd");
        var year = format.indexOf("yyyy");
        var seperators = new Array();
        var valueA = value.split('');
        var i =0;
        for(i=0;i<valueA.length;i++){
            if(isNaN(parseInt(valueA[i]))){
                // assume 1 char seperators
                // assume character is not a number
                    seperators.push(i);
            }
        }
        var iMonth;
        var iDay;
        var iYear;
        var newValue = '';
        if(value.length < format.length){
                    // Add Leading zeros
                    var lastSep = 0;
                    for(i=0;i<seperators.length;i++){
                        // assume first char is not a seperator
                        var x = parseInt(value.substring(lastSep, seperators[i]));
                        if(x <10){
                            newValue += '0' + x + value.charAt(seperators[i]);
                        }else{
                            newValue += x + value.charAt(seperators[i]);
                        }
                        lastSep = seperators[i] + 1;
                    }
                    newValue += value.substring(lastSep);
                    value = newValue;
                }

                // assume less then year 10,000, less then 99 months, less then 99 days
                var s = value.substring(month,(month+2));
                iMonth = parseInt(s,10);
                iDay = parseInt(value.substring(day,(day+2)),10);
                iYear = parseInt(value.substring(year,(year+4)),10);
                iMonth--;
                var result = new Date(iYear, iMonth, 1);
           return result;
    },
    parse:function(inputId){
        var format = sp_calendar.format(inputId);
        var settings = sp_calendar.get(inputId);
        
        // Assume MM dd yyyy are supplied
        var month = format.indexOf("MM");
        var day = format.indexOf("dd");
        var year = format.indexOf("yyyy");
        var value = $(inputId).value;
        var seperators = new Array();
        var valueA = value.split('');
        var i =0;
        for(i=0;i<valueA.length;i++){
            if(isNaN(parseInt(valueA[i]))){
                // assume 1 char seperators
                // assume character is not a number
                seperators.push(i);
            }
        }
        var iMonth;
        var iDay;
        var iYear;
        var newValue = '';
        if(value.length < format.length){
            // Add Leading zeros
            var lastSep = 0;
            for(i=0;i<seperators.length;i++){
                // assume first char is not a seperator
                var x = parseInt(value.substring(lastSep, seperators[i]));
                if(x <10){
                    newValue += '0' + x + value.charAt(seperators[i]);
                }else{
                    newValue += x + value.charAt(seperators[i]);
                }
                lastSep = seperators[i] + 1;
            }
            newValue += value.substring(lastSep);
            value = newValue;
        }

        // assume less then year 10,000, less then 99 months, less then 99 days
        var s = value.substring(month,(month+2));
        iMonth = parseInt(s,10);
        iDay = parseInt(value.substring(day,(day+2)),10);
        iYear = parseInt(value.substring(year,(year+4)),10);
        iMonth--;
        var result = new Date(iYear, iMonth, 1);

        sp_calendar.calendars[inputId] = result;
        sp_calendar.render(inputId, result);

    },
    format:function(inputId){
        return $(inputId + 'format').innerHTML;
    },
    renderCal:function(target,inputId, date){
        //todo proper cleanup
        //var SDOW = 1; //Monday
        var listeners = sp_calendar.listeners[inputId];
        if(listeners != null){
           var i = 0;
            for(i=0;i<listeners.length;i++){
                var l = listeners[i];
                Event.stopObserving(l.element, l.event, l.listener);
                //listeners.push({element:e,event:a,listener:events[a]});
            }
        }
        // Load JSON Config
        var settings = sp_calendar.get(inputId);
        var SDOW = settings.startDay;
        listeners = new Array();
        target.innerHTML = '';
        sp_calendar.listeners[inputId] = listeners;

        var ac = function(p,c){p.appendChild(c);};
        var be = sp_calendar.buildElement;
        //ac(target, be('div',{},'HELLO'));
        //return;
        target.className='snappyCalendar';

        var m = date.getMonth();
        var y = date.getFullYear();
        
        var cH = 'header';// Header
        var cM = 'table';// Table class
        var cDW = 'dayOfWeek'; // Day of week header
        var cD = 'dayCell'; // Day cell
        var brdr = 1;
        //var mn=['January','February','March','April','May','June','July','August','September','October','November','December'];
        var mn = settings.months;
        var daysOfWeek = settings.daysOfWeek;
        var dim=[31,0,31,30,31,30,31,31,30,31,30,31];
        var oD = new Date(y, m, 1);
        oD.od=oD.getDay()+1;

        var todaydate=new Date();

        var scanfortoday=(y==todaydate.getFullYear() && m==todaydate.getMonth())? todaydate.getDate() : 0 //DD added

        dim[1]=(((oD.getFullYear()%100!=0)&&(oD.getFullYear()%4==0))||(oD.getFullYear()%400==0))?29:28;

        var table = be('table',{
        	'cols':'7'
        	,'className':cM
        	,'cellPadding':'0'
        	,'cellSpacing':'0'
        	,'border':brdr       	
        	});
        var tbody = be('tbody',{});
        ac(table,tbody);
        var headerRow = be('tr',{'align':'center'});

        ac(tbody,headerRow);

        var backButton = be('td',{'className':'back'},'&lt;',{
            'click':function(event){
                sp_calendar.back(''+inputId, event);
            }},inputId);
        ac(headerRow, backButton);
        var header = be('td', {'colSpan':'5','align':'center','className':cH},mn[m]+' - '+y);
        ac(headerRow, header);
        var fwdButton = be('td',{'className':'fwd'},'&gt;',{
            'click':function(event){
                sp_calendar.forward(''+inputId, event);
            }
        },inputId);
        ac(headerRow, fwdButton);
        var row = be('tr',{'align':'center'});
        s = SDOW;
        var i =0;
        for(i=0;i<7;i++){
            ac(row, be('td', {'className':cDW}, sp_calendar.dow(s,daysOfWeek)));
            s++
            if(s>6)s=0;
        }
        tbody.appendChild(row);
        var colorNext = true;
        row = be('tr',{'align':'center'});
        for(i=1;i<=42;i++){
            var tc = cD;

           /* if(((i)%7==0)&&(i<=36)){
                tc = 'weekend';
                colorNext = true;
            }else{
                if(i==0 || colorNext){
                    tc = 'weekend';
                    colorNext = false;
                }
            }*/
            var x = '&nbsp';
            var dayOfWeek = oD.od - SDOW;
            var valueOfDay = i - dayOfWeek;
            if(valueOfDay>=0&&i-dayOfWeek<dim[m]){
			    x = i-oD.od+1+SDOW;
                var colorDate = new Date(date.getFullYear(),date.getMonth(),x);
                if(colorDate.getDay() == 0 ||colorDate.getDay() ==6){
                   tc = 'weekend';
                }
            }
            //var x=((i-oD.od>=0)&&(i-oD.od<dim[m]))? i-oD.od+1 : '&nbsp;';
            if (x==scanfortoday)
                tc = 'today';

            var events = null;
            if(x != '&nbsp;'){
                var id = y +":"+ m +":"+ x;
                var buildFF = function(aId, pId){
                    return function(event){sp_calendar.pop(aId,pId, event);};
                }
                events= {'click':buildFF(inputId, id)};
            }
            var dc = be('td', {'id':id,'className':tc},x,events,inputId);
            ac(row,dc);
            if(((i)%7==0)&&(i<36)){
                ac(tbody,row);
                row = be('tr',{'align':'center'});
            }
        }
        ac(tbody,row);
        //ac(target,table);
        target.appendChild(table);
       
    },
    buildElement:function(name, attrbiutes, innerHTML,events,inputId){
    	var e = document.createElement(name);
        e = $(e);
        var a = null;
    	for(a in attrbiutes){
    		e[a] = attrbiutes[a];
    	}
    	if(innerHTML){
    		e.innerHTML = innerHTML;
    	}
        var listeners = sp_calendar.listeners[inputId];
        if(events){
            for(a in events){
                e.observe(a,events[a],true);
                //Event.stopObserving(o.td, 'mouseover',o.mov);
                listeners.push({element:e,event:a,listener:events[a]});
            }
        }
         return e;
	}
    ,get:function(rootId){
            var id = rootId + "STATUSDIV";
            var field = $(id);
            return eval('(' + field.innerHTML + ')');
    }
}


