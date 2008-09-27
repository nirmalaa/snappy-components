package net.sensemaker.snappy.test;

import java.util.List;
import java.util.ArrayList;

/**
 * <<Class summary>>
 *
 * @author Robert Mayhew &lt;&gt;
 * @version $Rev$
 */
public final class TestBean {


    private List list = new ArrayList();
    /**
     * 
     */
    public TestBean() {
       for(int i = 0; i < 100;i++){
        list.add(new TestRow(i));
       }



    }

	public String getValue(){
		return "VALUE";
	}

    public List getList(){
        return list;
    }
    
}
