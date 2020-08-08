package com.saving;

public abstract  class Savings {
    String cusNo;
    String cusName;
    double deposit;
    int years;
    String savingType;

    Double[][] interestRates;

    public Savings(String customerNo, String name, double amount, int noofyears, String typeofSaving)
    {
        cusNo = customerNo;
        cusName = name;
        deposit = amount;
        years = noofyears;
        savingType = typeofSaving;

        interestRates  = new Double[years][4];

    }

    public abstract void EnableTable();
}
