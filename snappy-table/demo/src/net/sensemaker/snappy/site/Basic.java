package net.sensemaker.snappy.site;

import java.util.List;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.math.BigDecimal;

import net.sensemaker.snappy.table.SnappyRowSelectionEvent;

/**
 * Copyright 2008 Sensemaker Software Inc.
 * User: rob
 * Date: May 10, 2008
 * Time: 1:11:13 PM
 */
public class Basic {

    private List invoices = new ArrayList();

    public Basic(){

        invoices = InvoiceData.buildData(100);
    }

    public List getInvoices() {
        return invoices;
    }
    
    public void selected(SnappyRowSelectionEvent srse){
    	System.err.println("Row is [" + srse.getTargetRow() + "] Col [" + srse.getTargetCol() + "]");
    }

    public void edit(){
        Iterator iter = invoices.iterator();
        while(iter.hasNext()){
            Invoice invoice = (Invoice)iter.next();
            if(invoice.getNumber() == number){
                System.err.println("Editing");
                invoice.setCustomer(customer);
                invoice.setAddress(address);
                invoice.setPaid(paid);
                invoice.setAmount(amount);
            }

        }

    }

    private int number;
    private String customer;
    private String address;
    private boolean paid;
    private BigDecimal amount;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
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
}
