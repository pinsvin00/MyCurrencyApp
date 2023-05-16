package com.example.currencyapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class DeletableAdapter extends ArrayAdapter {

    List<String> itemsList;

    public DeletableAdapter(Context ctx, ArrayList<String> list) {
        super(ctx, 0, list);
        this.itemsList = list;
    }


    public static int getImageId(String resName, Class<?> c) {
        try {
            Field idField = c.getDeclaredField(resName);
            return idField.getInt(idField);
        } catch (Exception e) {
            return -1;
        }
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_currency, parent, false);
            LinearLayout layout = convertView.findViewById(R.id.currencyLinear);

            String name = this.itemsList.get(position);

            TextView textView = layout.findViewById(R.id.currencyName);
            textView.setText(name);


            ImageView imageView = layout.findViewById(R.id.currencyCountryFlag);
            imageView.setImageResource(getImageId(name.toLowerCase() , R.drawable.class));

            Button button = new Button(getContext());

            button.setText("USUŃ");

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemsList.remove(position);
                    DeletableAdapter.this.notifyDataSetChanged();
                }
            });

            layout.addView(button);

        return convertView;
    }

    public void update(ArrayList<CurrencyExchange> filterByCriteria) {
        this.itemsList.clear();
        this.itemsList.addAll(
                filterByCriteria.stream().map(c -> c.currencyName).collect(Collectors.toList()
                ));

        this.notifyDataSetChanged();
    }
}
