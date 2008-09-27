package net.sensemaker.snappy.site;

import java.util.*;
import java.math.BigDecimal;

/**
 * Copyright 2008 Sensemaker Software Inc.
 * User: rob
 * Date: May 11, 2008
 * Time: 2:11:50 PM
 */
public class InvoiceData {

    public static List buildData(int size){
        Customer[] customers = new Customer[]{
            new Customer("ACME Inc", "127 First ST"),
            new Customer("Very \"Good\" Show", "28913 Broadway"),
            new Customer("Fancy Shoes", "5019 12th ST NW"),
            new Customer("Bob's Pizza", "44 5th ave SW"),
            new Customer("Sweet Bakery", "12 Glamorgan Place"),
            new Customer("Zed<s> Hotrods", "834 Dearfoot Trail")
        };

        Random random = new Random();
        List result = new ArrayList();
        int day = 0;
        for(int i = 0 ; i < size; i++){
            int invoiceId = 1000 + i;
            Customer customer = customers[random.nextInt(customers.length)];
            boolean paid = random.nextBoolean();
            double price = (random.nextInt(100) + 100) * random.nextDouble();
            
            result.add(new Invoice(customer.name, customer.address,
                    invoiceId, paid, new BigDecimal(price).setScale(2, BigDecimal.ROUND_HALF_UP),d(2,++day),
                    style(random.nextInt(4))
                  
            ));
        }
        return result;
    }

    private static Date d(int month, int day){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -1);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        return calendar.getTime();
    }

    private static class Customer{
        private String name;
        private String address;

        private Customer(String name, String address) {
            this.name = name;
            this.address = address;
        }
    }

    private static String style(int i){
        switch(i){
            case 0:
            case 1:
                return null;
            case 2:
                return "yellowRow";
            case 3:
                return "greenRow";
        }
        return null;
    }
}
