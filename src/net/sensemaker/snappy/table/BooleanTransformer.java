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

public class BooleanTransformer implements ColumnTransformer {

    public String toHtml(Object o) {
        if(o == null)return "";
        if(o instanceof Boolean){
            if(((Boolean)o).booleanValue()){
                //return "<span style=\"font-family: Code2000, TITUS Cyberbit Basic, Doulos SIL, Chrysanthi Unicode, Bitstream Cyberbit, Bitstream CyberBase, Thryomanes, Gentium, GentiumAlt, Visual Geez Unicode, Lucida Grande, Arial Unicode MS, Microsoft Sans Serif, Lucida Sans Unicode;\">&#10004;</span>";
                return "Y";
                //return "<span>YY</span>";
            }
            return "N";
        }
        return o.toString();
    }

    public String toSort(Object o) {
        if(o == null)return"";
        return o.toString();
    }

    public String getAlign(){
        return "right";
    }

    public String getVAlign(){
        return null;
    }

    public String toPopulate(Object o){
        Boolean b = (Boolean)o;
        return b.toString();
    }
}



