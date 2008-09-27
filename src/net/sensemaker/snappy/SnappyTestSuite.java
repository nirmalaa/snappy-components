package net.sensemaker.snappy;

import net.sensemaker.snappy.calendar.CalendarTestSuite;
import net.sensemaker.snappy.resources.ResourcesTestSuite;
import net.sensemaker.snappy.table.test.TableTestSuite;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class SnappyTestSuite extends TestCase {

	public SnappyTestSuite(String s){
		super(s);
	}
	
	public static Test suite() throws Exception {
        TestSuite suite = new TestSuite();      
        suite.addTest(ResourcesTestSuite.suite());
        suite.addTest(CalendarTestSuite.suite());
        suite.addTest(TableTestSuite.suite());
        return suite;
    }
}
