package com.Model;



import java.time.LocalDateTime;

public class Transaction {

    private String islemTuru;
    private double miktar;
    private LocalDateTime tarih;

    public Transaction(String islemTuru, double miktar) {
        this.islemTuru = islemTuru;
        this.miktar = miktar;
        this.tarih = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return tarih + " | " + islemTuru + " | " + miktar + " TL";
    }
}

//tarih ve saati belirtir

