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
package net.sensemaker.testframework;

import net.sensemaker.snappy.HTMLElement;

import javax.faces.context.ResponseWriter;
import javax.faces.component.UIComponent;
import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;

public class MockResponseWriter extends ResponseWriter {

    private HTMLElement element = new HTMLElement();
    private HTMLElement root = element;


    public String getContentType() {
        return null;
    }

    public String getCharacterEncoding() {
        return null;
    }

    public void flush() throws IOException {

    }

    public void startDocument() throws IOException {

    }

    public void endDocument() throws IOException {

    }

    public void startElement(String s, UIComponent uiComponent) throws IOException {
         HTMLElement child = new HTMLElement(s, element);
        if (element != null) {
            element.addChild(child);
        }
        element = child;
    }

    public void endElement(String s) throws IOException {
        if (element.getParent() != null) {
            element = element.getParent();
        }
    }

    public void writeAttribute(String s, Object o, String s1) throws IOException {
         element.addAttribute(s, o.toString());
    }

    public void writeURIAttribute(String s, Object o, String s1) throws IOException {

    }

    public void writeComment(Object o) throws IOException {

    }

    public void writeText(Object o, String s) throws IOException {
        HTMLElement child = new HTMLElement(o.toString(), true, element);
        element.addChild(child);
    }

    public void writeText(char[] chars, int i, int i1) throws IOException {

    }

    public ResponseWriter cloneWithWriter(Writer writer) {
        return null;
    }

    public void write(char[] chars, int i, int i1) throws IOException {

    }

    public void close() throws IOException {
        
    }

    
    public int hashCode()
    {
    	int hash = 1;
    	hash = hash * 31 + element.hashCode();
    	return hash;
    }
    /*
     * Test methods
     */
    
    public boolean hasId(String id){
        return getElementById(id, root) != null;
    }

    public HTMLElement getElementById(String id){
        return getElementById(id, root);
    }
    
    public boolean isEmpty(){
    	return root.getChildren().isEmpty();
    }

    private HTMLElement getElementById(String id, HTMLElement p){
        if(p.getAttributes().containsKey("id")){
            if(id.equals(p.getAttributes().get("id"))){
                return p;
            }
        }
        Iterator iter = p.getChildren().iterator();
        while(iter.hasNext()){
            HTMLElement c = (HTMLElement)iter.next();
            HTMLElement r = getElementById(id, c);
            if(r != null)return r;
        }
        return null;
    }
   
    public boolean equals(Object o)
    {
    	if(o == null)return false;
    	if(!(o instanceof MockResponseWriter)){
    		return false;
    	}
    	MockResponseWriter e = (MockResponseWriter)o;
    	return root.equals(e.root);
    }
    
    
    @Override
    public String toString()
    {
    	StringBuffer sb = new StringBuffer();
    	toString(root, sb);
    	return sb.toString();
    }
    
    private void toString(HTMLElement ele, StringBuffer sb){
    	
    	sb.append("<").append(ele.getName());
    	Iterator atts = ele.getAttributes().keySet().iterator();
    	while(atts.hasNext()){
    		String a = (String)atts.next();
    		String v = (String)ele.getAttributes().get(a);
    		sb.append(" ").append(a).append("=\"").append(v).append("\"");
    	}
    	if(ele.getChildren().size() == 0){
    		sb.append("/>");
    	}else{
    		sb.append(">");
    		Iterator kids = ele.getChildren().iterator();
    		while(kids.hasNext()){
    			HTMLElement kid = (HTMLElement)kids.next();
    			if(kid.isText()){
    				sb.append(kid.getName());
    			}else{
    				toString(kid, sb);
    			}
    		}
    		sb.append("</").append(ele.getName()).append(">");
    	}
    }
}
