package net.sensemaker.snappy.site;

import net.sensemaker.snappy.table.*;

/**
 * Copyright 2008 Sensemaker Software Inc.
 * User: rob
 * Date: Jun 13, 2008
 * Time: 6:00:45 PM
 */
public class BooleanConverter implements ColumnTransformer {

    public String toHtml(Object o) {
        if (Boolean.parseBoolean(o.toString())) {
            return "<img src='/true.gif'/ alt='true'>";
        }
        return "<img src='/false.gif' alt='true'>";
    }

    public String toSort(Object o) {
        return o.toString();
    }


    public String getAlign() {
        return "right";
    }

    public String getVAlign() {
        return null;
    }

    public String toPopulate(Object o) {
        Boolean b = (Boolean) o;
        return b.toString();
    }
}
