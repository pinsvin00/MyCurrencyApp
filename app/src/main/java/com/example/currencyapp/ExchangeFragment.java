package com.example.currencyapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

public class ExchangeFragment extends Fragment {

    Spinner spinner;
    ListView listView;
    ExchangeListAdapter listAdapter;

    CurrencyInfoProvider currencyInfoProvider;
    ExchangeFilterCriteria exchangeFilterCriteria;

    String selectedCurrencySymbol = "";

    Runnable onDataLoad;
    Handler handler;


    public ExchangeFragment() {
        // Required empty public constructor
    }

    public static ExchangeFragment newInstance(CurrencyInfoProvider currencyInfoProvider) {
        ExchangeFragment fragment = new ExchangeFragment();
        fragment.currencyInfoProvider = currencyInfoProvider;

        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        currencyInfoProvider = new CurrencyInfoProvider();

        handler = new Handler();
        Runnable onLoad = new Runnable() {
            @Override
            public void run() {
                ExchangeFragment.this.loadUi();
            }
        };

        currencyInfoProvider.loadExchange("USD", onLoad, handler);
    }

    public void loadUi() {

        View view = getView();
        assert view != null;

        Button btn = view.findViewById(R.id.searchButton);
        EditText editText = view.findViewById(R.id.currencyInput);


        CurrencySpinnerAdapter adapter = new CurrencySpinnerAdapter(view.getContext(), currencyInfoProvider.getCurrencies());
        spinner = view.findViewById(R.id.spinner);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedCurrencySymbol = currencyInfoProvider.currencies.get(i);
                listAdapter.selectedCurrency = selectedCurrencySymbol;
                System.out.println(selectedCurrencySymbol);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });


        listView = view.findViewById(R.id.currenciesChange);
        listAdapter = new ExchangeListAdapter(view.getContext(), currencyInfoProvider.filterByCriteria(this.exchangeFilterCriteria));
        listView.setAdapter(listAdapter);


        onDataLoad = new Runnable() {
            @Override
            public void run() {
                listAdapter.currencyExchange = currencyInfoProvider.filterByCriteria(exchangeFilterCriteria);
                listAdapter.notifyDataSetChanged();
                listView.invalidateViews();
            }
        };

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String amount = editText.getText().toString();
                listAdapter.amount = Integer.parseInt(amount);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println(selectedCurrencySymbol);
                        currencyInfoProvider.loadExchange(selectedCurrencySymbol, onDataLoad, handler);
                    }
                }).start();
            }
        });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_currency_list2, container, false);
    }
}