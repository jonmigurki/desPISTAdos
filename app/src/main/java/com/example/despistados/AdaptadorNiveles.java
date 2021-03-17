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
    private String idioma;

    public AdaptadorNiveles(Context applicationContext, String[] n, String i) {
        contexto = applicationContext;
        niveles = n;
        idioma = i;

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
        convertView=inflater.inflate(R.layout.fila_categoria_nivel,null);
        TextView categoria = (TextView) convertView.findViewById(R.id.etiqueta);

        if(idioma.equals("español")){
            categoria.setText("Nivel " + String.valueOf(position + 1));
        }else{
            categoria.setText("Level " + String.valueOf(position + 1));
        }


            return convertView;
    }
}
