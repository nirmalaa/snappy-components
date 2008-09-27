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
package net.sensemaker.snappy;

import javax.faces.context.ResponseWriter;
import javax.faces.component.UIComponent;
import java.io.IOException;
import java.util.Iterator;


public class HTMLWriter {

    private HTMLElement element = new HTMLElement();

    public HTMLWriter() {

    }

  

    public void startElement(String name) {
        HTMLElement child = new HTMLElement(name, element);
        if (element != null) {
            element.addChild(child);
        }
        element = child;
    }

    public void endElement(String s) {
        endElement();
    }

    public void writeAttribute(String name, int i) {
        writeAttribute(name, "" + i);
    }

    public void writeAttribute(String name, String value) {
        element.addAttribute(name, value);
    }

    public void endElement() {
        if (element.getParent() != null) {
            element = element.getParent();
        }
    }


    public void writeText(String value) {
        HTMLElement child = new HTMLElement(value, true, element);
        element.addChild(child);
    }


    public void render(ResponseWriter writer, UIComponent componet) throws IOException {
        render(writer, componet, element);
    }

    public void render(ResponseWriter writer, UIComponent componet, HTMLElement element) throws IOException {
        if(element == null)return;
        if (element.isRoot()) {
            Iterator iter = element.getChildren().iterator();
            while (iter.hasNext()) {
                render(writer, componet, (HTMLElement) iter.next());
            }
        } else {
            if (element.isText())
                writer.writeText(element.getName(), null);
            else {
            	String name = element.getName();
                writer.startElement(name, componet);
                Iterator iter = element.getAttributes().keySet().iterator();
                while (iter.hasNext()) {
                    String key = (String) iter.next();
                    String value = (String) element.getAttributes().get(key);
                    writer.writeAttribute(key, value, null);
                }
                iter = element.getChildren().iterator();
                while (iter.hasNext()) {
                    render(writer, componet, (HTMLElement) iter.next());
                }
                writer.endElement(element.getName());
            }
        }

    }

    public String toString(){
        StringBuffer sb = new StringBuffer();
        toString(element, sb);
        return sb.toString();
    }

    private void toString(HTMLElement element, StringBuffer sb)
    {
         if (element.isRoot()) {
            Iterator iter = element.getChildren().iterator();
            while (iter.hasNext()) {
                toString((HTMLElement) iter.next(), sb);
            }
        } else {
            if (element.isText())
                sb.append(element.getName());
            else {

                sb.append("<").append(element.getName()).append(">");
                Iterator iter = element.getAttributes().keySet().iterator();
                while (iter.hasNext()) {
                    String key = (String) iter.next();
                    String value = (String) element.getAttributes().get(key);
                    sb.append(key).append("=\"").append(value).append("\" ");
                }
                sb.append(">");
                iter = element.getChildren().iterator();
                while (iter.hasNext()) {
                    toString((HTMLElement) iter.next(), sb);
                }
                sb.append("</").append(element.getName()).append(">");

            }
        }
    }

    

}
                                   