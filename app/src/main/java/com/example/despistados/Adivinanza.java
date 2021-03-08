package com.example.despistados;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Button;

import java.util.HashMap;

public class Adivinanza extends AppCompatActivity {

    TextView puntos, monedas, categoria, nivel;
    Button atras, pista, resolver;

    Context context;

    String usuario, cat, niv, id_niv;

    //Pistas que el usuario ha utilizado -> al comienzo 1
    int pistasUtilizadas = 1;


    //Aquí guardaré las pistas -> [ 1 --- "Saga de películas basadas en 7 libros"
    //                              2 --- "..." ... ]
    HashMap<Integer, String> pistas = new HashMap<Integer, String>();


    /*Además, cada vez que abra una pista necesitaré guardarlo en la BD, para cuando salga y entre de nuevo
    Por lo tanto, nada más entrar en esta actividad, lo primero que tendré que hacer es comprobar que el
    usuario nunca haya entrado aquí. En la BD compruebo en el JSON si esta categoría y este nivel se encuentran;
    si se encuentran mirar el booleano (true si resuelto, false si no) y en el caso de que sea false, mirar
    cuántas pistas ha abierto hasta ahora. Después, en el ListView actualizo el número de pistas.

    https://stackoverflow.com/questions/4540754/how-do-you-dynamically-add-elements-to-a-listview-on-android

                                        adapter.add("pista");
    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adivinanza);


        context = this.getApplicationContext();

        puntos = (TextView) findViewById(R.id.txtPuntos1);
        monedas = (TextView) findViewById(R.id.txtMonedas1);
        categoria = (TextView) findViewById(R.id.txtCategoria);
        nivel = (TextView) findViewById(R.id.txtNivel);

        atras = (Button) findViewById(R.id.btnAtras);
        pista = (Button) findViewById(R.id.btnPista);
        resolver = (Button) findViewById(R.id.btnResolver);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            usuario = extras.getString("usuario");
            cat = extras.getString("categoria");
            niv = extras.getString("nivel");
            id_niv = extras.getString("id_nivel");
        }

        categoria.setText(cat.toString());
        nivel.setText("Nivel " + id_niv.toString());

        mostrarPuntosYMonedas();



        //Método que se encarga de comprobar cuántas pistas están abiertas en este nivel
        obtenerDatosUsuario();







        //Accedemos al ListView y creamos el adaptador que visualizará las categorías cargadas
        ListView lista = (ListView) findViewById(R.id.lista3);
        //En el adaptador le tengo que meter las pistas, PERO NO TODAS, sino LAS QUE ESTÁN ABIERTAS
        AdaptadorNiveles eladap= new AdaptadorNiveles(getApplicationContext(),XXXXXXXXXXXXXXXXXXXXXXXXXXXXXX);
        lista.setAdapter(eladap);

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


    private void obtenerDatosUsuario(){

        /*
        1- Si es la primera vez que entra: (SELECT a la BD y mirar que esta categoría y este nivel NO están metidxs en el JSON)

            -> Necesitamos hacer un UPDATE en la BD para meterle el siguiente JSON al usuario:
                { [ { categoria: 1, nivel: 1, pistas: 1, resuelto = "False" } ] }
           De esta manera, le indicamos que la primera pista ya ha sido descubierta

        2- Si no es la primera vez (en la BD este nivel y categoria para este usuario estan)

            2.1. Si resuelto=true --> un JDialog indicandole que ya lo ha resuelto con x pistas

            2.1. Si resuelto=false --> Miro por cuántas pistas va y se lo paso como parámetro al Adaptador para que actualice el ListView.
         */


    }


    private void obtenerPistas(){





    }


    private void anadirPista(){


    }


    private void comprobar(String res_usuario, String res){


    }


    private void resolver(){




    }



}