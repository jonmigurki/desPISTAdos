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

public class Nivel extends AppCompatActivity {

    String usuario;
    String categoria;

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nivel);

        //Obtenemos el usuario identificado y la categoría seleccionada
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            usuario = extras.getString("usuario");
            categoria = extras.getString("categoria");
        }

        String [] niveles = cargarNiveles(categoria);

        Log.d("niveles", niveles[0] + niveles[1] + niveles[2]);

        //Establecemos el contexto de la aplicación
        context = this.getApplicationContext();

        //Accedemos al ListView y creamos el adaptador que visualizará las categorías cargadas
        ListView lista = (ListView) findViewById(R.id.lista2);
        AdaptadorNiveles eladap= new AdaptadorNiveles(getApplicationContext(),niveles);
        lista.setAdapter(eladap);

    }


    private String[] cargarNiveles(String categoria) {

        InputStream is = this.getResources().openRawResource(R.raw.data_es);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));

        String[] niveles = null;

        //Leemos la primera línea de data.txt

        try {
            String linea = reader.readLine();

            boolean nivelEncontrado = false;

            while(!nivelEncontrado){

                linea = reader.readLine();

                if(linea.equals(categoria)){
                    nivelEncontrado=true;
                    String n = reader.readLine();
                    niveles = n.split(";");
                }



            }



        } catch (IOException e) {
            e.printStackTrace();
        }


        return niveles;

    }



}