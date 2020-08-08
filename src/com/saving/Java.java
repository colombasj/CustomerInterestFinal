package com.saving;

public class Java extends Savings implements Compunt_Interest{

    public Java(String customerNo, String name, double amount, int noofyears, String typeofSaving) {
        super(customerNo, name, amount, noofyears, typeofSaving);
    }
    public void EnableTable(){
        generateTable();
    }

    @Override
    public void generateTable() {
        Double startingamt = super.deposit;
        for(int i=0; i<super.years; i++)
        {
            Double interest = (startingamt * 10)/100 ;
            Double endingValue = startingamt + interest;

            super.interestRates[i][0] =Double.valueOf(i+1);

            super.interestRates[i][1] = startingamt ;
            super.interestRates[i][2] =  interest  ;
            super.interestRates[i][3] = endingValue;

            startingamt = endingValue;
        }
    }
}
