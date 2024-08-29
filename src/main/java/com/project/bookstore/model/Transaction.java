package com.project.bookstore.model;


public class Transaction {
    private int txId;
    private String bookName;
    private int price;
    private int quantity;
    private int total;
    private int cashUsed;
    private int cashChange;
    private String dateTime;

    public Transaction() {}

    public Transaction(String bookName, int price, int quantity, int total, int cashUsed, int cashChange, String dateTime) {
        this.bookName = bookName;
        this.price = price;
        this.quantity = quantity;
        this.total = (int) total;
        this.cashUsed = cashUsed;
        this.cashChange = cashChange;
        this.dateTime = dateTime;
    }

    public int getTxId() {
        return txId;
    }

    public void setTxId(int txId) {
        this.txId = txId;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getCashUsed() {
        return cashUsed;
    }

    public void setCashUsed(int cashUsed) {
        this.cashUsed = cashUsed;
    }

    public int getCashChange() {
        return cashChange;
    }

    public void setCashChange(int cashChange) {
        this.cashChange = cashChange;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
}