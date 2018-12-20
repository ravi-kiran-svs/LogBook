package com.example.ravikiran.logbook;

public class ID_Name_Bal_Object {

    private String Date;
    private String Name;
    private long Balance;

    private String Giver;
    private String Taker;

    ID_Name_Bal_Object(String name, long balance) {
        Date = "";
        Name = name;
        Balance = balance;
    }

    ID_Name_Bal_Object(String date, String name, long balance) {
        Date = date;
        Name = name;
        Balance = balance;
    }

    ID_Name_Bal_Object(String date, String giver, String taker, long balance) {
        Date = date;
        Giver = giver;
        Taker = taker;
        Balance = balance;
    }


    String getDate() {
        return Date;
    }

    String getName() {
        return Name;
    }

    long getBalance() {
        return Balance;
    }

    String getGiver() {
        return Giver;
    }

    String getTaker() {
        return Taker;
    }
}
