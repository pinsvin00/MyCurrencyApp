package com.example.currencyapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;


public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    CurrencyInfoProvider provider;


    Runnable onLoad;
    Handler handler;


    ExchangeFragment exchangeFragment;
    CriteriaFragment criteriaFragment;
    Button viewButton;
    FragmentTransaction ft;

    boolean isOnCriteriaView = true;

    protected void loadUi() {
        setContentView(R.layout.activity_main);

        viewButton = findViewById(R.id.viewButton);
        viewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                ft = getSupportFragmentManager().beginTransaction();
                ft.setCustomAnimations(
                        R.anim.slide_in,
                        R.anim.slide_out
                );

                if(isOnCriteriaView) {
                    ft.replace(R.id.fragmentLayout, exchangeFragment);
                    exchangeFragment.exchangeFilterCriteria = criteriaFragment.criteria;
                }
                else {
                    ft.replace(R.id.fragmentLayout, criteriaFragment);
                }


                ft.commit();

                isOnCriteriaView = !isOnCriteriaView;
            }
        });


        criteriaFragment = CriteriaFragment.newInstance(provider);
        exchangeFragment = ExchangeFragment.newInstance(provider);

        ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(
                R.anim.slide_in,
                R.anim.slide_out
        );

        ft.replace(R.id.fragmentLayout, criteriaFragment);
        ft.commit();



    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        handler = new Handler();
        onLoad = new Runnable() {
            @Override
            public void run() {
                MainActivity.this.loadUi();
            }
        };
        System.out.println("Hello world!");
        provider = new CurrencyInfoProvider();
        provider.loadExchange("USD", onLoad, handler);

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }
}