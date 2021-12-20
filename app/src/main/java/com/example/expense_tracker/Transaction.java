package com.example.expense_tracker;

public class Transaction {
    private String amount,credit,debit;

    public Transaction(){

    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCredit() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }

    public String getDebit() {
        return debit;
    }

    public void setDebit(String debit) {
        this.debit = debit;
    }

    public Transaction(String amount, String credit, String debit){
        this.amount = amount;
        this.credit = credit;
        this.debit = debit;
    }
} 