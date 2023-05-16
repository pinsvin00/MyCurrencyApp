package com.example.currencyapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ExchangeListAdapter extends ArrayAdapter {
    public int amount;
    public String selectedCurrency;
    ArrayList<CurrencyExchange> currencyExchange;

    public ExchangeListAdapter(Context ctx, ArrayList<CurrencyExchange> list) {
        super(ctx, 0, list);
        this.currencyExchange = list;
    }


    public void update(List<CurrencyExchange> newlist) {
        currencyExchange.clear();
        currencyExchange.addAll(newlist);
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_currency_list, parent, false);
        this.loadCurrencyDataForView(convertView, position);

        return convertView;
    }

    public static int getImageId(String resName, Class<?> c) {
        try {
            Field idField = c.getDeclaredField(resName);
            return idField.getInt(idField);
        } catch (Exception e) {
            return R.drawable.cny;
        }
    }


    protected void loadCurrencyDataForView(View view, int position) {

        ImageView iv = view.findViewById(R.id.currencyCountryFlag);
        TextView tv = view.findViewById(R.id.currencyName);
        String currency = currencyExchange.get(position).currencyName;
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ExchangeHistoryActivity.class);
                intent.putExtra("symbol", currency);
                intent.putExtra("base", selectedCurrency);
                getContext().startActivity(intent);
            }
        });


        int id = getImageId(currency.toLowerCase() , R.drawable.class);
        if(id != -1) {
            iv.setImageResource(id);
        }
        tv.setText(currency.toUpperCase(Locale.ROOT));

        TextView from = view.findViewById(R.id.currencyExchangeFrom);
        TextView to = view.findViewById(R.id.currencyExchangeTo);

        CurrencyExchange exchange = currencyExchange.get(position);

        Double sellPrice = (1.0/exchange.price) * amount;

        from.setText(String.format("%.2f", exchange.price * amount));
        to.setText( String.format("%.2f", sellPrice) );
    }

}
