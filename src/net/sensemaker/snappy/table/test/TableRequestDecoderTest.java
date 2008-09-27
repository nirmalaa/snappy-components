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
package net.sensemaker.snappy.table.test;

import java.util.ArrayList;
import java.util.List;

import net.sensemaker.snappy.table.ColumnValueEncoder;
import net.sensemaker.snappy.table.SnappyTable;
import net.sensemaker.snappy.table.TableEncoder;
import net.sensemaker.snappy.table.TableRequestDecoder;
import net.sensemaker.testframework.MockResponseWriter;


import org.json.JSONObject;

public class TableRequestDecoderTest extends BaseTableTest{
	
	public TableRequestDecoderTest(String s)
	{
		super(s);
	}
	
	
	/*
	 * Actions to test
	 * currentPage: Page that is currently displayed
	 * row , selected : A table row was selected
	 * 
	 */
	public void testCurrentPageChange() throws Exception
	{
		SnappyTable table = new SnappyTable();
		table.setVar("row");
		List value = new ArrayList();
		for(int i = 0 ; i < 100; i++)
		{
			value.add(""+i);
		}
		table.setValue(value);
		TableRequestDecoder tableRequestDecoder = new TableRequestDecoder();
		ColumnValueEncoder columnValueEncoder = new ColumnValueEncoder();
		TableEncoder tableEncoder = new TableEncoder(table,columnValueEncoder);
		String event = "";
		String sort = "";
		JSONObject json  = new JSONObject();
		json.put("currentPage", 1);
		json.put("maxPages", 10);
		json.put("selectedIndex","-1");
		
		MockResponseWriter writerA = new MockResponseWriter();
		facesContext.setResponseWriter(writerA);
		facesContext.getExternalContext().getRequestParameterMap().put(table.genStatusId(table.getClientId(facesContext)), json.toString());
		facesContext.getExternalContext().getRequestMap().put("status", json.toString());
		tableRequestDecoder.decode(json.toString(), event, sort, facesContext, table);
		assertEquals(table.getCurrentPage(), json.getInt("currentPage"));
		tableEncoder.encode(facesContext);
		assertTrue("Empty after first pass",!writerA.isEmpty());
		
		json.put("currentPage", 2);
		MockResponseWriter writerB = new MockResponseWriter();
		facesContext.setResponseWriter(writerB);
		tableRequestDecoder.decode(json.toString(), event, sort, facesContext, table);
		assertEquals(table.getCurrentPage(), json.getInt("currentPage"));
		tableEncoder.encode(facesContext);
		assertTrue("Empty after second pass",!writerB.isEmpty());
		assertNotNull(writerA.toString());
		assertNotNull(writerB.toString());
		
		assertTrue("Writers are not Equal", writerA.equals(writerB));
	}
	
	/*
	 * Test to make sure when nothing changes that extra rows are not renderer.
	 */
	public void testCurrentPageNoChange() throws Exception
	{
		
	}
	
	/*
	 * Test refresh should send down updates 
	 */
	
	/*
	 * Test that when table data changes updates are sent 
	 * but only for the json data affected
	 */
}
