package com.example.despistados;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Nivel extends AppCompatActivity {

    String usuario;
    String categoria;
    String num_categoria;

    TextView usuarioI, puntos, monedas;


    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nivel);

        usuarioI = (TextView) findViewById(R.id.txtIdentificado);
        usuarioI.setText(usuario);

        //Obtenemos el usuario identificado y la categoría seleccionada
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            usuario = extras.getString("usuario");
            categoria = extras.getString("categoria");
            num_categoria = extras.getString("num_categoria");
        }


        //Establecemos el contexto de la aplicación
        context = this.getApplicationContext();

        puntos = (TextView) findViewById(R.id.txtPuntos);
        monedas = (TextView) findViewById(R.id.txtMonedas);
        mostrarPuntosYMonedas();


        String [] niveles = cargarNiveles(categoria);

        Log.d("niveles", niveles[0] + niveles[1] + niveles[2]);


        //Accedemos al ListView y creamos el adaptador que visualizará los niveles cargados
        ListView lista = (ListView) findViewById(R.id.lista2);
        AdaptadorNiveles eladap= new AdaptadorNiveles(getApplicationContext(),niveles);
        lista.setAdapter(eladap);

        //Cuando el usuario pulse en un nivel concreto, deberá de llevarle a la actividad Adivinanza
        //donde tendrá que adivinar la palabra o frase. Tendré que pasarle el usuario identificado,
        //la categoría y el nivel seleccionado.
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(Nivel.this, Adivinanza.class);
                i.putExtra("usuario", usuario);
                i.putExtra("categoria", categoria);
                i.putExtra("num_categoria", num_categoria);

                //En nivel tendré que guardar el nombre del nivel, no el identificador
                //El View devuelve "Nivel X", necesito solamente sacar la X (el número) -> charAt(6)
                String v = ((TextView)view.findViewById(R.id.etiqueta)).getText().toString();
                String n = Character.toString(v.charAt(6));
                int numero = Integer.valueOf(n);
                i.putExtra("nivel", niveles[numero-1]);
                i.putExtra("num_nivel", String.valueOf(numero));

                startActivity(i);
               // finish();   //¿¿¿¿¿¿¿FINISH????????
            }
        });

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


    //Método privado que se encarga de mostrar los puntos y las monedas que el usuario tiene (se hace una consulta a la BD)
    private void mostrarPuntosYMonedas() {

        //Hacemos una consulta a la BD

        BD GestorDB = new BD (context, "BD", null, 1);
        SQLiteDatabase bd = GestorDB.getWritableDatabase();

        //Miramos en la BD
        Cursor cursor = bd.rawQuery("SELECT PUNTOS, MONEDAS FROM USUARIOS WHERE USUARIO = '" + usuario + "'", null);
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