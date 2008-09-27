package net.sensemaker.snappy.resources;

import junit.framework.TestCase;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.text.SimpleDateFormat;

/**
 * Copyright 2008 Sensemaker Software Inc.
 * User: rob
 * Date: Jul 31, 2008
 * Time: 10:22:18 PM
 */
public class CalendarTest extends TestCase {

    public CalendarTest(){
        super(CalendarTest.class.getName());
    }
    
    public CalendarTest(String s)
    {
    	super(s);
    }

    @SuppressWarnings("deprecation")
	public void testParse() throws Exception {
        Context cx = Context.enter();
        BufferedReader in = null;
        
        try {
            Scriptable scope = cx.initStandardObjects();
            InputStream stream =
                    this.getClass().getClassLoader().getResourceAsStream("net/sensemaker/snappy/resources/snappy.js");
            in = new BufferedReader(new InputStreamReader(stream));
            StringBuffer sb= new StringBuffer();
           
            while (in.ready()) {
                sb.append( in.readLine()).append("\n");
            }


            cx.evaluateString(scope, sb.toString(), "<cmd>", 1, null);
            String script = "sp_calendar.parse_date('06/07/1973','MM/dd/yyyy').getTime();";
           cx.evaluateString(scope, script, "<cmd>", 1, null);


        } finally {
        	in.close();
            Context.exit();
        }

    }

    @SuppressWarnings("deprecation")
	public void testSupportedDateFormats() throws Exception {
        String[] formats = new String[]{"MM/dd/yyyy", "dd/MM/yyyy", "yyyy/dd/MM", "yyyy/MM/dd", "MM:dd:yyyy"};
        Context cx = Context.enter();
        BufferedReader in = null;
        try {
            Scriptable scope = cx.initStandardObjects();
            InputStream stream =
                    this.getClass().getClassLoader().getResourceAsStream("net/sensemaker/snappy/resources/snappy.js");
            in = new BufferedReader(new InputStreamReader(stream));
            String s = "";

            while (in.ready()) {
                s += in.readLine() + "\n";
            }

            cx.evaluateString(scope, s, "<cmd>", 1, null);


            for (String format : formats) {
                SimpleDateFormat df = new SimpleDateFormat(format);
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.MONTH, 0);
                cal.set(Calendar.DAY_OF_MONTH, 1);
                int year = cal.get(Calendar.YEAR);
                year++;
                Calendar end = (Calendar) cal.clone();
              end.set(Calendar.YEAR, year);
                while (cal.before(end)) {
                    String value = df.format(cal.getTime());
                    String script = "sp_calendar.parse_date('" + value + "','" + format + "').getTime();";
                    Object result = cx.evaluateString(scope, script, "<cmd>", 1, null);
                    Calendar testResult = Calendar.getInstance();
                    testResult.setTimeInMillis(((Double) result).longValue());
                    assertEquals("Day must be 1", 1, testResult.get(Calendar.DAY_OF_MONTH));
                    assertEquals("Month wrong", cal.get(Calendar.MONTH), testResult.get(Calendar.MONTH));
                    assertEquals("Year is wrong", cal.get(Calendar.YEAR), testResult.get(Calendar.YEAR));
                    cal.add(Calendar.DAY_OF_YEAR, 1);
                }
            }
        } finally {
        	in.close();
            Context.exit();
        }
    }


}
