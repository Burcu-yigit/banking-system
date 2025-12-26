package com.Model;



import com.exception.InsufficientBalanceException;

public class CheckingAccount extends Account {

    private double eksiLimit = 1000;

    public CheckingAccount(String hesapNo, double bakiye) {
        super(hesapNo, bakiye);
    }

    @Override
    public void paraCek(double miktar)
            throws InsufficientBalanceException {

        if (bakiye + eksiLimit >= miktar) {
            bakiye -= miktar;
            islemler.add(new Transaction("Para Çekme (Vadesiz)", miktar));
        } else {
            throw new InsufficientBalanceException("Limit aşıldı!");
        }
    }
}

