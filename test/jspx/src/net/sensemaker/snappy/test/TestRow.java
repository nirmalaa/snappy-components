package net.sensemaker.snappy.test;

import java.util.Random;

/**
 * Copyright 2008 Sensemaker Software Inc.
 * User: rob
 * Date: Apr 30, 2008
 * Time: 9:46:35 AM
 */
public class TestRow {

    private int row = 0;
    private String name;
    private int age = 34;
    private String city;
    private static Random rnd = new Random();
    public TestRow(int row) {
        this.row = row;
        this.name = randomString();
        age = rnd.nextInt(100)+1;
        city = randomString();
        city = "<script>alert('" + city + "');</script>";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    private String randomString(){
        String result = "";
        for(int i = 0; i < 10;i++){
            result += (char)(rnd.nextInt(26) + 65);
        }
        return result;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }
}
