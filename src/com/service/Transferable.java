package com.service;



import com.Model.Account;

import com.exception.InsufficientBalanceException;

public interface Transferable {

    void transfer(Account hedefHesap, double miktar)
            throws InsufficientBalanceException;
}

