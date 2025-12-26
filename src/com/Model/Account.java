package com.Model;



import com.service.Transferable;
import com.exception.InsufficientBalanceException;
import java.util.ArrayList;
import java.util.List;

public abstract class Account implements Transferable {

    private String hesapNumarasi;
    protected double bakiye;
    protected List<Transaction> islemler;

    public Account(String hesapNumarasi, double bakiye) {
        this.hesapNumarasi = hesapNumarasi;
        this.bakiye = bakiye;
        this.islemler = new ArrayList<>();
    }

    public double getBakiye() {
        return bakiye;
    }

    public void paraYatir(double miktar) {
        bakiye += miktar;
        islemler.add(new Transaction("Para Yatırma", miktar));
    }

    public abstract void paraCek(double miktar)
            throws InsufficientBalanceException;

    @Override
    public void transfer(Account hedef, double miktar)
            throws InsufficientBalanceException {

        paraCek(miktar);
        hedef.paraYatir(miktar);
        islemler.add(new Transaction("Havale", miktar));
    }

    public void islemleriGoster() {
        islemler.forEach(System.out::println);
    }
}
//abstract classtır 
