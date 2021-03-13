package com.example.despistados;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class AdaptadorPistas extends BaseAdapter {

    private Context contexto;
    private LayoutInflater inflater;
    private String[] pistas;

    public AdaptadorPistas(Context applicationContext, String[] n) {
        contexto = applicationContext;
        pistas = n;

        inflater = (LayoutInflater) contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return pistas.length;
    }

    @Override
    public Object getItem(int position) {
        return pistas[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView=inflater.inflate(R.layout.fila_pista,null);
        TextView categoria = (TextView) convertView.findViewById(R.id.etiqueta);
        categoria.setText(pistas[position]);

        return convertView;
    }
}
