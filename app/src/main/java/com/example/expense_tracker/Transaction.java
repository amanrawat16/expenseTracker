package com.example.expense_tracker;

import com.google.firebase.database.Exclude;

public class Transaction {
    private String amount,type,documentId;

    public Transaction(){

    }

    public Transaction(String amount,String type){
        this.amount = amount;
        this.type = type;

    }
    @Exclude
    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getAmount() {
        return amount;
    }



    public String getType() {
        return type;
    }


}