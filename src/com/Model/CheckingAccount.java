package com.Model;

import com.exception.InsufficientBalanceException;

public class CheckingAccount extends Account {

    public CheckingAccount(String hesapNo, double bakiye) {
        super(hesapNo, bakiye);
        this.bakiye = 1000; // Kredili limit burada set ediliyor
    }

    @Override
    public void paraCek(double miktar) throws InsufficientBalanceException {
        if (bakiye + bakiye >= miktar) {
            bakiye -= miktar;
            islemler.add(new Transaction("Para Çekme (Vadesiz)", miktar));
        } else {
            throw new InsufficientBalanceException("Yetersiz bakiye!");
        }
    }
}

