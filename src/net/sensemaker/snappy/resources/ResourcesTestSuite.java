package net.sensemaker.snappy.resources;


import junit.framework.TestSuite;

public class ResourcesTestSuite {
	public static TestSuite suite() throws Exception {
        TestSuite suite = new TestSuite();
        suite.addTestSuite(CalendarTest.class);
        return suite;
    }
}
