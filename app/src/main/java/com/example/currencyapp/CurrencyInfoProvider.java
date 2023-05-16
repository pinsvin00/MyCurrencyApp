package com.example.currencyapp;

import static java.lang.String.format;

import android.os.Handler;
import android.util.Pair;

import com.github.mikephil.charting.data.Entry;
import com.google.gson.Gson;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;

public class CurrencyInfoProvider {

    public ArrayList<String> currencies;
    private CurrencyPackage currencyPackage;
    private CurrencyHistoryPackage history;

    private String base;
    private String symbol;


    public ArrayList<Pair<String, Entry>> getHistoryAsEntries() throws ParseException {
        ArrayList<Pair<String, Entry>> entries = new ArrayList<>();

        SimpleDateFormat parser = new SimpleDateFormat("yyyy-mm-dd");
        int i = 0;
        Long first = null;
        for(String rating : history.rates.keySet()) {
            Entry entry = new Entry();
            Map<String, Float> data = history.rates.get(rating);


            entry.setX(i);
            entry.setY( data.get(symbol) );

            i++;
            entries.add( new Pair<>(rating, entry) );
        }

        return entries;
    }

    public ArrayList<CurrencyExchange> filterByCriteria(ExchangeFilterCriteria criteria) {

        ArrayList<CurrencyExchange> exchanges = currencyPackage.getAsExchangeArray();
        System.out.println(currencyPackage);
        if(criteria.symbols.size() == 0) {
            return exchanges;
        }


        return new ArrayList<CurrencyExchange>(exchanges.stream().filter(el -> criteria.symbols.contains(el.currencyName)).
                collect(Collectors.toList()));
    }

    public void fetchHistory(String base, String symbol, String start, String end , Handler handler, Runnable onLoad){

        this.base = base;
        this.symbol = symbol;

        new Thread(new Runnable() {
            @Override
            public void run() {
                InputStream in =null;
                int responseCode = -1;
                try{

                    String fullUrl = String.format(
                            "https://api.exchangerate.host/timeseries?start_date=%s&end_date=%s&symbols=%s&base=%s",
                            start, end,
                            symbol, base
                    );


                    URL url = new URL(fullUrl);
                    HttpURLConnection con = (HttpURLConnection)url.openConnection();
                    con.setDoInput(true);
                    con.connect();
                    responseCode = con.getResponseCode();
                    if(responseCode == HttpURLConnection.HTTP_OK)
                    {
                        //download
                        in = con.getInputStream();

                        Reader reader = new InputStreamReader(in, StandardCharsets.UTF_8);
                        CurrencyInfoProvider.this.history = new Gson().fromJson(reader, CurrencyHistoryPackage.class);

                        handler.post(onLoad);

                    }

                }
                catch(Exception ex) {
                    ex.printStackTrace(System.err);
                }
            }
        }).start();
    }

    public void loadExchange(String currency , Runnable onLoad, Handler handler) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                InputStream in =null;
                int responseCode = -1;
                try{

                    System.out.println(currency);
                    String urlStr = "https://v6.exchangerate-api.com/v6/fa9ad460ce948df78abe27e2/latest/" +  currency;

                    URL url = new URL(urlStr);
                    HttpURLConnection con = (HttpURLConnection)url.openConnection();
                    con.setDoInput(true);
                    con.connect();
                    responseCode = con.getResponseCode();
                    if(responseCode == HttpURLConnection.HTTP_OK)
                    {
                        //download
                        in = con.getInputStream();

                        Reader reader = new InputStreamReader(in, StandardCharsets.UTF_8);
                        CurrencyInfoProvider.this.currencyPackage = new Gson().fromJson(reader, CurrencyPackage.class);

                        handler.post(onLoad);

                    }

                }
                catch(Exception ex) {
                    ex.printStackTrace(System.err);
                }
            }
        }).start();
    }


    public ArrayList<String> getCurrencies() {
        currencies = new ArrayList<>();
        currencies.addAll(currencyPackage.conversionRate.keySet());

        return currencies;
    }

    public void setCurrencies(ArrayList<String> currencies) {
        this.currencies = currencies;
    }

    public CurrencyPackage loadExchange() {
        return currencyPackage;
    }

    public void setCurrencyPackage(CurrencyPackage currencyPackage) {
        this.currencyPackage = currencyPackage;
    }

}
