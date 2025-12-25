package com.Model;

import com.exception.InsufficientBalanceException;

public class SavingsAccount extends Account {

    private double faizOrani = 0.05;

    public SavingsAccount(String hesapNo, double bakiye) {
        super(hesapNo, bakiye);
    }

    @Override
    public void paraCek(double miktar)
            throws InsufficientBalanceException {

        if (bakiye >= miktar) {
            bakiye -= miktar;
            islemler.add(new Transaction("Para Çekme (Vadeli)", miktar));
        } else {
            throw new InsufficientBalanceException("Yetersiz bakiye!");
        }
    }

    public void faizHesapla() {
        double faiz = bakiye * faizOrani;
        bakiye += faiz;
        islemler.add(new Transaction("Faiz", faiz));
    }

	public double getFaizOrani() {
		return faizOrani;
	}

	public void setFaizOrani(double faizOrani) {
		this.faizOrani = faizOrani;
	}

	public Object getAccountNumber() {
		// TODO Auto-generated method stub
		return null;
	}
}
//vadeli hesap 

