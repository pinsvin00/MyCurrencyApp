package com.example.currencyapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.ExecutorService;

public class CurrencySpinnerAdapter extends ArrayAdapter {

    ArrayList<String> currencyTagList;

    public CurrencySpinnerAdapter(Context ctx, ArrayList<String> list) {
        super(ctx, 0, list);
        this.currencyTagList = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_currency, parent, false);
        }

        this.loadCurrencyDataForView(convertView, position);

        return convertView;
    }




    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return this.getView(position, convertView, parent);
    }

    public static int getImageId(String resName, Class<?> c) {
        try {
            Field idField = c.getDeclaredField(resName);
            return idField.getInt(idField);
        } catch (Exception e) {
            return -1;
        }
    }


    protected void loadCurrencyDataForView(View view, int position) {
        ImageView iv = view.findViewById(R.id.currencyCountryFlag);
        TextView tv = view.findViewById(R.id.currencyName);

        String currency = this.currencyTagList.get(position);

        int id = getImageId(currency.toLowerCase() , R.drawable.class);
        if(id != -1) {
            iv.setImageResource(id);
        }
        tv.setText(currency.toUpperCase(Locale.ROOT));

    }


}
