package com.example.despistados;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Menu extends AppCompatActivity {

 //   String[] categorias = new String[5];

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        String[] categorias = cargarCategorias();
        
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        ListView lista = (ListView) findViewById(R.id.lista1);
        AdaptadorListView eladap= new AdaptadorListView(getApplicationContext(),categorias);
        lista.setAdapter(eladap);
    }



    public String[] cargarCategorias() {

        ArrayList<String> lc = new ArrayList<String>();
        String linea;

        InputStream is = this.getResources().openRawResource(R.raw.data);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));

        String[] c = null;

        //Leemos la primera línea de data.txt
        try {
            linea = reader.readLine();

            c = linea.split(";");



        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.d("holaa", "golaa");
        Log.d("categorias", c[0] + c[1] + c[2] + c[3]);

        return c;

    }


}