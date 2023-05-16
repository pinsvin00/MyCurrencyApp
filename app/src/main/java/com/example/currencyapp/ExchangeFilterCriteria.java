package com.example.currencyapp;

import java.util.ArrayList;
import java.util.List;

public class ExchangeFilterCriteria {
    public List<String> symbols;

    public ExchangeFilterCriteria() {
        this.symbols = new ArrayList<>();
    }

    public ExchangeFilterCriteria(List<String> symbols) {
        this.symbols = symbols;
    }


    public void addSymbolRequired(String symbol) {
        if (symbol.length() != 3) {
            return;
        }
        symbols.add(symbol);
    }


    @Override
    public String toString() {
        return "ExchangeFilterCriteria{" +
                "symbols=" + symbols +
                '}';
    }
}
