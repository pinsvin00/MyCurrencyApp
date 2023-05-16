package com.example.currencyapp;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.HashMap;

public class CurrencyPackage {
    @SerializedName("conversion_rates")
    public HashMap<String, Double> conversionRate;
    public String result;
    public String base_code;

    public ArrayList<CurrencyExchange> getAsExchangeArray() {
        ArrayList<CurrencyExchange> ex = new ArrayList<>();

        for(String key : conversionRate.keySet()) {
            ex.add( new CurrencyExchange(key, conversionRate.get(key) ) );
        }

        return ex;
    }

    @Override
    public String toString() {
        return "CurrencyPackage{" +
                "conversionRate=" + conversionRate +
                ", result='" + result + '\'' +
                ", base_code='" + base_code + '\'' +
                '}';
    }
}
