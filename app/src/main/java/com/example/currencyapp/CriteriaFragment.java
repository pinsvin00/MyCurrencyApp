package com.example.currencyapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class CriteriaFragment extends Fragment {
    DeletableAdapter criteriaAdapter;
    ListView criteriaList;
    ExchangeFilterCriteria criteria;
    private CurrencyInfoProvider provider;

    public CriteriaFragment() {
        // Required empty public constructor
    }

    public static CriteriaFragment newInstance(CurrencyInfoProvider provider) {
        CriteriaFragment fragment = new CriteriaFragment();
        fragment.provider = provider;
        fragment.criteria = new ExchangeFilterCriteria();

        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.loadUi();
    }


    public void loadUi() {
        View view = getView();

        criteriaList = view.findViewById(R.id.currentCriteria);

        EditText searchField = view.findViewById(R.id.currencySymbol);
        searchField.setImeActionLabel("ime", KeyEvent.KEYCODE_ENTER);

        if(criteria == null) return;

        criteriaAdapter = new DeletableAdapter(view.getContext(), (ArrayList<String>) criteria.symbols);
        criteriaList.setAdapter(criteriaAdapter);

        Button acceptButton = view.findViewById(R.id.accept);
        acceptButton.setText("DODAJ WALUTĘ");
        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                criteria.addSymbolRequired(searchField.getText().toString());
                criteriaAdapter.update(
                        CriteriaFragment.this.provider.filterByCriteria(criteria))
                ;

                searchField.setText("");
            }
        });


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_currency_criteria, container, false);
    }
}