package net.sensemaker.snappy.table.test;

import junit.framework.TestCase;
import net.sensemaker.snappy.table.SnappyTableRenderBypassControl;

import org.json.JSONObject;

/**
 * Copyright 2008 Sensemaker Software Inc.
 * User: rob
 * Date: Jun 1, 2008
 * Time: 12:58:38 PM
 */
public class SnappyTableRenderBypassControlTest extends TestCase {


   private SnappyTableRenderBypassControl ctrl = new SnappyTableRenderBypassControl();
   private String statusA = "{\"columnCount\":6,\"maxPages\":20,\"selectedIndex\":-1,\"selectMultiple\":false,\"sortAscending\":false,\"sortColumn\":-1,\"columns\":\"[{\\\"column\\\": 0, \\\"sortDirection\\\": 0}, {\\\"column\\\": 1, \\\"sortDirection\\\": 0}, {\\\"column\\\": 2, \\\"sortDirection\\\": 0}, {\\\"column\\\": 3, \\\"sortDirection\\\": 0}, {\\\"column\\\": 4, \\\"sortDirection\\\": 0}, {\\\"column\\\": 5, \\\"sortDirection\\\": 0}]\",\"pageSize\":5,\"currentPage\":13,\"selectable\":false}";
   private String statusSortChange = "{\"columnCount\":6,\"maxPages\":20,\"selectedIndex\":-1,\"selectMultiple\":false,\"sortAscending\":true,\"sortColumn\":-1,\"columns\":\"[{\\\"column\\\": 0, \\\"sortDirection\\\": 0}, {\\\"column\\\": 1, \\\"sortDirection\\\": 0}, {\\\"column\\\": 2, \\\"sortDirection\\\": 0}, {\\\"column\\\": 3, \\\"sortDirection\\\": 0}, {\\\"column\\\": 4, \\\"sortDirection\\\": 0}, {\\\"column\\\": 5, \\\"sortDirection\\\": 0}]\",\"pageSize\":5,\"currentPage\":13,\"selectable\":false}";
   private String statusPageChange = "{\"columnCount\":6,\"maxPages\":20,\"selectedIndex\":-1,\"selectMultiple\":false,\"sortAscending\":false,\"sortColumn\":-1,\"columns\":\"[{\\\"column\\\": 0, \\\"sortDirection\\\": 0}, {\\\"column\\\": 1, \\\"sortDirection\\\": 0}, {\\\"column\\\": 2, \\\"sortDirection\\\": 0}, {\\\"column\\\": 3, \\\"sortDirection\\\": 0}, {\\\"column\\\": 4, \\\"sortDirection\\\": 0}, {\\\"column\\\": 5, \\\"sortDirection\\\": 0}]\",\"pageSize\":5,\"currentPage\":14,\"selectable\":false}";

   public SnappyTableRenderBypassControlTest(String s)
   {
	   super(s);
   }
   
   public SnappyTableRenderBypassControlTest(){
	   super(SnappyTableRenderBypassControlTest.class.getName());
   }
   
    public void testFirstPass() throws Exception
    {
        // First pass status chnage
        JSONObject currentStatus = new JSONObject(statusA);
        JSONObject lastStatus = new JSONObject();
        String currentEvent = "{evt:'yes'}";
        String lastEvent = "";
        assertTrue(
                ctrl.shouldBypass(lastStatus, currentStatus,
                        lastEvent, currentEvent));
    }

    public void testNoChange() throws Exception
    {
        // First pass status chnage
        JSONObject currentStatus = new JSONObject(statusA);
        JSONObject lastStatus = new JSONObject(statusA);
        String currentEvent = "{evt:'yes'}";
        String lastEvent = "{evt:'yes'}";
        assertTrue(
                !ctrl.shouldBypass(lastStatus, currentStatus,
                        lastEvent, currentEvent));
    }

    public void testSortChange() throws Exception
    {
        // First pass status chnage
        JSONObject currentStatus = new JSONObject(statusSortChange);
        JSONObject lastStatus = new JSONObject(statusA);
        String currentEvent = "{evt:'yes'}";
        String lastEvent = "{evt:'yes'}";
        assertTrue(
                ctrl.shouldBypass(lastStatus, currentStatus,
                        lastEvent, currentEvent));
    }

    public void testPageChange() throws Exception
    {
        // First pass status chnage
        JSONObject currentStatus = new JSONObject(statusPageChange);
        JSONObject lastStatus = new JSONObject(statusA);
        String currentEvent = "{evt:'yes'}";
        String lastEvent = "{evt:'yes'}";
        assertTrue(
                ctrl.shouldBypass(lastStatus, currentStatus,
                        lastEvent, currentEvent));
    }

}
