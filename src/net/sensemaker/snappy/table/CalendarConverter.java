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

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Logger;

public class CalendarConverter implements ColumnConverter{

    private DateFormat dateFormat = DateFormat.getDateTimeInstance();
    private static Logger log = Logger.getLogger(CalendarConverter.class.getName());
    
    
    public String toHtml(Object o) {
        if(o == null)return"";
        if(o instanceof Calendar){
            return dateFormat.format(((Calendar)o).getTime());
        }
        if(o instanceof Date){
            return dateFormat.format((Date)o);
        }
        if(o instanceof Long){
            return dateFormat.format(new Date((Long)o));
        }
        
        log.finest("CLass [" + o.getClass() + "]");
        return o.toString();
    }

    public String toSort(Object o) {
        if(o == null)return"";
        if(o instanceof Calendar){
            return ((Calendar)o).getTime().getTime() + "";
        }
        if(o instanceof Date){
            return ((Date)o).getTime() + "";
        }
        if(o instanceof Long){
            return ((Long)o).longValue() + "";
        }
        //System.err.println("Sort CLass [" + o.getClass() + "]");
        return o.toString();
    }

    public String getAlign(){
        return null;
    }

    public String getVAlign(){
        return null;
    }

    public String toPopulate(Object o){
        return toHtml(o);
    }
}
