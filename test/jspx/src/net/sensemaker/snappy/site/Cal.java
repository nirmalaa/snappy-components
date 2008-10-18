package net.sensemaker.snappy.site;

import java.util.Date;

/**
 * Copyright 2008 Sensemaker Software Inc.
 * User: rob
 * Date: Jul 20, 2008
 * Time: 12:03:47 PM
 */
public class Cal {


    private Date value = new Date();
    private Date valueLink = new Date();
    private Date valueLinkLocale = new Date();
    private Date nullDate;

    public Date getValue() {
        return value;
    }

    public void setValue(Date value) {
        this.value = value;
    }

    public Date getValueLink() {
        return valueLink;
    }

    public void setValueLink(Date valueLink) {
        this.valueLink = valueLink;
    }

    public void setValueLinkLocale(Date date)
    {
    	this.valueLinkLocale = date;
    }
    
    public Date getValueLinkLocale()
    {
    	return valueLinkLocale;
    }

    public Date getNullDate()
    {
    	return nullDate;
    }
    
    public void setNullDate(Date d)
    {
    	nullDate = d;
    }
    
    public void set(){}
}
