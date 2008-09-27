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

import java.util.Iterator;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;


public class HTMLElement {

	private static Logger log = Logger.getLogger(HTMLElement.class.getName());
    private String name;
    private Map attributes = new HashMap();
    private boolean text;
    private List children = new ArrayList();
    private HTMLElement parent;
    private boolean root;
    public HTMLElement(){
        root = true;

    }



    public HTMLElement(String name, HTMLElement parent){
        this.name = name;
        this.parent = parent;
    }

    public HTMLElement(String name, boolean text, HTMLElement parent){
        if(text){
            this.name = name;
            this.text = text;
        }
        this.parent = parent;
    }

    public void addAttribute(String name, String value){
        attributes.put(name, value);
    }

    public void addChild(HTMLElement element){
        children.add(element);
    }

    public String getName() {
        return name;
    }

    public Map getAttributes() {
        return attributes;
    }

    public boolean isText() {
        return text;
    }

    public List getChildren() {
        return children;
    }

    public HTMLElement getParent() {
        return parent;
    }

    public boolean isRoot() {
        return root;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAttributes(Map attributes) {
        this.attributes = attributes;
    }

    public void setText(boolean text) {
        this.text = text;
    }

    public void setChildren(List children) {
        this.children = children;
    }

    public void setParent(HTMLElement parent) {
        this.parent = parent;
    }

    public void setRoot(boolean root) {
        this.root = root;
    }
    
    public boolean equals(Object o)
    {
    	if(o==null)return false;
    	if(!(o instanceof HTMLElement))return false;
    	HTMLElement e = (HTMLElement)o;
    	if(name == null){
    		if(e.name != null)return false;
    	}else
    		if(!name.equals(e.name)){
    			log.finest("Expeceted [" + name + "] got [" + e.name + "]");
    			return false;
    		}
    	if(text != e.text){
    		log.finest("Not Text");
    		return false;}
    	Iterator iter = attributes.keySet().iterator();
    	while(iter.hasNext())
    	{
    		String key = (String)iter.next();
    		String value = (String)attributes.get(key);
    		String eVal = (String)e.attributes.get(key);
    		if(!value.equals(eVal))
    			{
    			log.finest("Attrbiutes key " + key + " " +value + " != " + eVal);
    			return false;
    			
    			}
    	}
    	iter = children.iterator();
    	Iterator eIter = e.children.iterator();
    	while(iter.hasNext()){
    		if(!eIter.hasNext())return false;
    		HTMLElement child = (HTMLElement)iter.next();
    		HTMLElement eChild = (HTMLElement)eIter.next();
    		if(!child.equals(eChild))return false;
    	}
    	return true;
    }
    
    public int hashCode()
    {
    	int hash = 1;
    	hash = hash * 31 * name.hashCode();
    	hash = hash * 21 * attributes.hashCode();
    	return hash;
    }
}
