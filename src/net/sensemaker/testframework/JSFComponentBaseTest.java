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


import javax.faces.component.UIComponent;

import org.apache.shale.test.mock.MockFacesContext;

public abstract class JSFComponentBaseTest extends org.apache.shale.test.base.AbstractJsfTestCase {
    
    public abstract Class getComponentClass();
    
    public JSFComponentBaseTest(String name){
    	super(name);
    }

    public void testSaveRestoreState() throws Exception
    {
    	
        UIComponent comp = (UIComponent)getComponentClass().newInstance();
        Object o = comp.saveState(new MockFacesContext());
        Object[] oa = (Object[])o;
        comp.restoreState(new MockFacesContext(), oa);
    }

}
