package net.sensemaker.snappy.site;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

/**
 * Copyright 2008 Sensemaker Software Inc.
 * User: rob
 * Date: May 11, 2008
 * Time: 10:58:45 AM
 */
public class Invoice {


    private String customer;
    private String address;
    private int number;
    private boolean paid;
    private BigDecimal amount;
    private Date date;
    private boolean selected;
    private String style;


    public Invoice(String customer, String address, int number, boolean paid,
                   BigDecimal amount, Date date, String style) {
        this.customer = customer;
        this.address = address;
        this.number = number;
        this.paid = paid;
        this.amount = amount;
        this.date = date;
        this.style = style;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
    	System.err.println("Selelected Set [" + selected + "]");
        this.selected = selected;
    }

    public String getStyle() {
        return paid?null:"unpaidRow";
    }
}

