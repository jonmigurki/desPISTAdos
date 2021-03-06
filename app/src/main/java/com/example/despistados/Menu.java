package com.example.despistados;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Menu extends AppCompatActivity {

 //   String[] categorias = new String[5];

    TextView usuario, puntos, monedas;

    Context context;

    String user;        //Variable global que guarda el nombre del usuario

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        String[] categorias = cargarCategorias();
        
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        context = this.getApplicationContext();

        ListView lista = (ListView) findViewById(R.id.lista1);
        AdaptadorListView eladap= new AdaptadorListView(getApplicationContext(),categorias);
        lista.setAdapter(eladap);

        String u = "";

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            u = extras.getString("usuario");
        }

        usuario = (TextView) findViewById(R.id.txtIdentificado);
        usuario.setText(u);
        user = u;

        puntos = (TextView) findViewById(R.id.txtPuntos);
        monedas = (TextView) findViewById(R.id.txtMonedas);

        mostrarPuntosYMonedas();

    }


    private String[] cargarCategorias() {

        ArrayList<String> lc = new ArrayList<String>();
        String linea;

        InputStream is = this.getResources().openRawResource(R.raw.data_ES);
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



    private void mostrarPuntosYMonedas() {

        //Hacemos una consulta a la BD

        BD GestorDB = new BD (context, "BD", null, 1);
        SQLiteDatabase bd = GestorDB.getWritableDatabase();

        //Miramos en la BD
        Cursor cursor = bd.rawQuery("SELECT PUNTOS, MONEDAS FROM USUARIOS WHERE USUARIO = '" + user + "'", null);
        if (cursor.moveToFirst()) {
            int p = cursor.getInt(cursor.getColumnIndex("PUNTOS"));
            int m = cursor.getInt(cursor.getColumnIndex("MONEDAS"));

            puntos.setText("Puntos: " + String.valueOf(p));
            monedas.setText("Monedas: " + String.valueOf(m));
        }
        cursor.close();
        GestorDB.close();






    }



}