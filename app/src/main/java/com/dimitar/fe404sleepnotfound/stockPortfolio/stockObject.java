package com.dimitar.fe404sleepnotfound.stockPortfolio;

public class stockObject {

    private String name;
    private String symbol;
    private String price_open;
    private String amount;

    /**
     * Object that is used to represent the data that is in the user portfolio for a given stock
     *
     * @param name of stock
     * @param symbol of the stock
     * @param price_open price of the stock
     * @param amount amount of the stock that the user owns
     */
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
