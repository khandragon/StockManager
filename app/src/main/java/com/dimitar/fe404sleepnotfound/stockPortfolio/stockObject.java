package com.dimitar.fe404sleepnotfound.stockPortfolio;

public class stockObject {

    private String name;
    private String symbol;
    private String price_open;
    private String amount;


    public stockObject(String name, String symbol, String price_open, String amount){
        this.name = name;
        this.symbol = symbol;
        this.price_open = price_open;
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getPrice_open() {
        return price_open;
    }

    public String getAmount() {
        return amount;
    }
}
