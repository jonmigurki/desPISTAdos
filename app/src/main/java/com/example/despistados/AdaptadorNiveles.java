package com.example.despistados;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class AdaptadorNiveles extends BaseAdapter {

    private Context contexto;
    private LayoutInflater inflater;
    private String[] niveles;

    public AdaptadorNiveles(Context applicationContext, String[] n) {
        contexto = applicationContext;
        niveles = n;

        inflater = (LayoutInflater) contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return niveles.length;
    }

    @Override
    public Object getItem(int position) {
        return niveles[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView=inflater.inflate(R.layout.fila,null);
        TextView categoria = (TextView) convertView.findViewById(R.id.etiqueta);
        categoria.setText("Nivel " + String.valueOf(position + 1));

        return convertView;
    }
}
