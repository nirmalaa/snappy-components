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
import net.sensemaker.snappy.SnappyUtil;

public class SnappyTableRenderBypassControl {


    // This miht sem lame, but remeber that I spend an hour finding a very obvious bug here
    public boolean shouldBypass(JSONObject lastStatus, JSONObject currentStatus,
                                String lastEvent, String currentEvent
                                ) {


        boolean result = false;
        boolean statusEqual =  SnappyUtil.jsonEquals(currentStatus,lastStatus);
        boolean eventEqual = currentEvent.equals(lastEvent);

        if (statusEqual && eventEqual) {

            result = false;

        } else {
            result = true;

        }

        return result;
    }
}
/*
{
 "columnCount": 6,
 "columns": "[
 {\"column\": 0, \"sortDirection\": 0}, {\"column\": 1, \"sortDirection\": 0}, {\"column\": 2, \"sortDirection\": 0}, {\"column\": 3, \"sortDirection\": 0}, {\"column\": 4, \"sortDirection\": 0}, {\"column\": 5, \"sortDirection\": 0}]",
 "currentPage": 0,
 "maxPages": 20,
 "pageSize": 5,
 "selectMultiple": false,
 "selectable": true,
 "selectedIndex": 1,
 "sortAscending": false,
 "sortColumn": -1
}



*/