package com.dimitar.fe404sleepnotfound.foreignExchange.foreignExchangeFragments;

/**
 * Custom object for sending a currency ticker and the type of recycler view that was selected from
 *
 * @author Jamroa
 */
public class CurrencySelect {

    private String currency;
    private String type;

    public CurrencySelect(String currency, String type) {
        this.currency = currency;
        this.type = type;
    }

    public String getCurrency() {
        return currency;
    }

    public String getType() {
        return type;
    }
}
