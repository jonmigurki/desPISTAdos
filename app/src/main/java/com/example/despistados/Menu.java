package com.example.despistados;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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

        //Cargamos las categorías
        String[] categorias = cargarCategorias();
        
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        //Establecemos el contexto de la aplicación
        context = this.getApplicationContext();

        //Accedemos al ListView y creamos el adaptador que visualizará las categorías cargadas
        ListView lista = (ListView) findViewById(R.id.lista1);
        AdaptadorCategorias eladap= new AdaptadorCategorias(getApplicationContext(),categorias);
        lista.setAdapter(eladap);

        //Obtenemos el usuario que se ha identificado
        String u = "";
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            u = extras.getString("usuario");
        }

        //Hacemos que se visualicen el nombre del usuario, sus puntos y sus monedas en la ventana
        usuario = (TextView) findViewById(R.id.txtIdentificado);
        usuario.setText(u);
        user = u;
        puntos = (TextView) findViewById(R.id.txtPuntos);
        monedas = (TextView) findViewById(R.id.txtMonedas);
        mostrarPuntosYMonedas();


        //Cuando el usuario seleccione una categoría, realizaremos un Intent explícito a una nueva ventana
        //para visualizarle los niveles disponibles en esa categoría. Además, necesitaremos pasarle el nombre
        //del usuario para recogerlo después y poder hacer los updates necesarios en la BD y guardar sus puntuaciones
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent i = new Intent(Menu.this, Nivel.class);
                i.putExtra("categoria", ((TextView)view.findViewById(R.id.etiqueta)).getText().toString());
                i.putExtra("usuario", user);

                startActivity(i);

            }
        });

    }



    //Método privado que se encarga de cargar las categorías leyendo el fichero de texto
    private String[] cargarCategorias() {

        ArrayList<String> lc = new ArrayList<String>();
        String linea;

        InputStream is = this.getResources().openRawResource(R.raw.data_es);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));

        String[] c = null;

        //Leemos la primera línea de data.txt
        try {
            linea = reader.readLine();

            c = linea.split(";");



        } catch (IOException e) {
            e.printStackTrace();
        }


        return c;

    }


    //Método privado que se encarga de mostrar los puntos y las monedas que el usuario tiene (se hace una consulta a la BD)
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


    //Método que se encarga de visualizar un Dialog cuando el usuario le da al botón de atrás de su teléfono
    public void onBackPressed() {

        AlertDialog.Builder alertdialog=new AlertDialog.Builder(this);
        alertdialog.setTitle("Salir");
        alertdialog.setMessage("¿Estás segur@ de que quieres cerrar sesión?");
        alertdialog.setPositiveButton("Sí", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
            //    Menu.super.onBackPressed();
                finish();
            }
        });

        alertdialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alertdialog.show();

    }



}