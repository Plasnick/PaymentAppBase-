package com.techelevator.tenmo.dao;

public interface AccountDao {

    public double getBalance (String userName);

    public double subtractFromAccount(double Amount, String userName);
    public double addToAccount(double Amount, String userName);
    public int audit(double amount, String sendingUser, String receivingUser);
    //public createAccount() {}
}
