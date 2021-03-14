package com.example.despistados;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

public class Adivinanza extends AppCompatActivity {

    TextView puntos, monedas, categoria, nivel;
    Button atras, pista, resolver, comprobar;
    EditText respuesta;

    Context context;

    String usuario, cat, num_cat, niv, num_niv;

    //Pistas que el usuario ha utilizado -> al comienzo 1
    int pistasUtilizadas = 1;

    String[] pistasAbiertas;
    String[] pistas;

    int puntosUsuario;
    int monedasUsuario;

    boolean resuelto;


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
        comprobar = (Button) findViewById(R.id.btnComprobar);

        respuesta = (EditText) findViewById(R.id.txtRespuesta);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            usuario = extras.getString("usuario");
            cat = extras.getString("categoria");
            num_cat = extras.getString("num_categoria");
            niv = extras.getString("nivel");
            num_niv = extras.getString("num_nivel");
        }

        categoria.setText(cat.toString());
        nivel.setText("Nivel " + num_niv.toString());

        mostrarPuntosYMonedas();

        resuelto();     //COMPROBAMOS SI ESTE NIVEL ESTÁ RESUELTO O NO

        //Método que se encarga de comprobar cuántas pistas están abiertas en este nivel
        obtenerDatosUsuario();

        pistas = obtenerPistas();

        pistasAbiertas = actualizarListaPistas();

        //Accedemos al ListView y creamos el adaptador que visualizará las pistas cargadas
        ListView lista = (ListView) findViewById(R.id.lista3);
        AdaptadorPistas eladap= new AdaptadorPistas(getApplicationContext(),pistasAbiertas);
        lista.setAdapter(eladap);

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position==4){

                    ImageView image = new ImageView(Adivinanza.this);
                    String i = "imagen" + num_cat + num_niv;
                    int im = getResources().getIdentifier(i , "drawable", context.getPackageName());
                    image.setImageResource(im);

                    AlertDialog.Builder builder =
                            new AlertDialog.Builder(Adivinanza.this).
                                    setMessage("Imagen de la pista").
                                    setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    }).
                                    setView(image);
                    builder.create().show();


                }
            }
        });


        atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Adivinanza.this, Nivel.class);
                i.putExtra("categoria", cat);
                i.putExtra("usuario", usuario);
                i.putExtra("num_categoria", num_cat);

                startActivity(i);
                finish();
            }
        });


        resolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!resuelto) {
                    if (monedasUsuario >= 20) {
                        //Bajar teclado
                        View view = Adivinanza.this.getCurrentFocus();
                        if (view != null) {
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        }

                        //Mostramos el dialog indicando que el usuario ha fallado
                        AlertDialog.Builder alertdialog = new AlertDialog.Builder(Adivinanza.this);
                        alertdialog.setTitle("Resolver");
                        alertdialog.setMessage("¿Estás segur@ de que quieres resolver? Te costará 20 monedas.");
                        alertdialog.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                resolver();

                            }
                        });

                        alertdialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                        alertdialog.show();

                    } else {
                        Toast.makeText(getApplicationContext(), "No tienes monedas suficientes para poder resolver. " +
                                "Resuelve otros niveles para así conseguir más monedas", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "Este nivel ya está resuelto.", Toast.LENGTH_SHORT).show();
                }

            }
        });


        pista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!resuelto) {
                    if (pistasUtilizadas == 5) {
                        //Crear toast
                        Toast.makeText(getApplicationContext(), "Ya has abierto todas las pistas disponibles." +
                                "Piensa bien la respuesta, y si te rindes resuélvela pagando 20 monedas.", Toast.LENGTH_SHORT).show();
                    } else {
                        pistasUtilizadas++;
                        pistasAbiertas = actualizarListaPistas();
                        AdaptadorPistas adap = new AdaptadorPistas(getApplicationContext(), pistasAbiertas);
                        lista.setAdapter(adap);

                        //ACTUALIZAMOS LA BASE DE DATOS PARA GUARDAR EL NUMERO DE PISTAS UTILIZADAS
                        BD GestorDB = new BD(context, "BD", null, 1);
                        SQLiteDatabase bd = GestorDB.getWritableDatabase();

                        //Miramos en la BD
                        bd.execSQL("UPDATE LOGROS SET PISTAS=" + pistasUtilizadas + " WHERE USUARIO = '" + usuario + "' AND " +
                                "CATEGORIA = '" + num_cat + "' AND NIVEL = '" + num_niv + "'");

                    }
                }else{
                    Toast.makeText(getApplicationContext(), "Este nivel ya está resuelto.", Toast.LENGTH_SHORT).show();
                }


            }
        });


        comprobar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!resuelto) {
                    //Recogemos lo que el usuario ha escrito y comprobamos la respuesta
                    if (respuesta.getText().toString().equals("")) {
                        Toast.makeText(getApplicationContext(), "Debes escribir algo para comprobar la respuesta." +
                                "Pero asegúrate bien, que cada error te resta 2 puntos.", Toast.LENGTH_SHORT).show();
                    } else {

                        if (respuesta.getText().toString().equalsIgnoreCase(niv)) {

                            //Bajar teclado
                            View view = Adivinanza.this.getCurrentFocus();
                            if (view != null) {
                                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                            }

                            //Calculamos los puntos
                            int pm = calcularPuntosMonedas();

                            //Mostramos el dialog indicando que el usuario ha acertado
                            AlertDialog.Builder alertdialog = new AlertDialog.Builder(Adivinanza.this);
                            alertdialog.setTitle("HAS ACERTADO");
                            alertdialog.setMessage("Enhorabuena!! Has ganado " + pm + " puntos y " + pm + " monedas");
                            alertdialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });

                            alertdialog.setNeutralButton("Compartir", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent();
                                    intent.setAction(Intent.ACTION_SEND);
                                    intent.putExtra(Intent.EXTRA_TEXT, "He adivinado el *nivel " + num_niv + "* de la categoría *" + cat + "* en desPISTAdos " +
                                            "y me han dado *" + pm + " puntos y monedas*!! Intenta superarme xd!");
                                    intent.setType("text/plain");
                                    intent.setPackage("com.whatsapp");
                                    startActivity(intent);

                                        }
                                    });


                            alertdialog.show();

                            puntosUsuario = puntosUsuario + pm;
                            monedasUsuario = monedasUsuario + pm;

                            puntos.setText("Puntos: " + String.valueOf(puntosUsuario));
                            monedas.setText("Monedas: " + String.valueOf(monedasUsuario));

                            actualizarColorPuntos();

                            BD GestorDB = new BD(context, "BD", null, 1);
                            SQLiteDatabase bd = GestorDB.getWritableDatabase();
                            bd.execSQL("UPDATE LOGROS SET RESUELTO=1 WHERE USUARIO = '" + usuario + "' AND " +
                                    "CATEGORIA = '" + num_cat + "' AND NIVEL = '" + num_niv + "'");

                            bd.execSQL("UPDATE USUARIOS SET PUNTOS=" + puntosUsuario + " WHERE USUARIO='" + usuario + "'");
                            bd.execSQL("UPDATE USUARIOS SET MONEDAS=" + monedasUsuario + " WHERE USUARIO='" + usuario + "'");

                            resuelto();

                        } else {

                            //Bajar teclado
                            View view = Adivinanza.this.getCurrentFocus();
                            if (view != null) {
                                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                            }

                            //Mostramos el dialog indicando que el usuario ha fallado
                            AlertDialog.Builder alertdialog = new AlertDialog.Builder(Adivinanza.this);
                            alertdialog.setTitle("HAS FALLADO");
                            alertdialog.setMessage("Lo siento! Prueba con otra cosa. (Recuerda que debes escribir las tildes correctamente).");
                            alertdialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });

                            alertdialog.show();


                            puntosUsuario = puntosUsuario - 2;
                            puntos.setText("Puntos: " + String.valueOf(puntosUsuario));

                            actualizarColorPuntos();

                            BD GestorDB = new BD(context, "BD", null, 1);
                            SQLiteDatabase bd = GestorDB.getWritableDatabase();

                            bd.execSQL("UPDATE USUARIOS SET PUNTOS=" + puntosUsuario + " WHERE USUARIO='" + usuario + "'");


                        }

                    }
                }else{
                    Toast.makeText(getApplicationContext(), "Este nivel ya está resuelto.", Toast.LENGTH_SHORT).show();
                }
            }
        });




/*

        //Accedemos al ListView y creamos el adaptador que visualizará las categorías cargadas
        ListView lista = (ListView) findViewById(R.id.lista3);
        //En el adaptador le tengo que meter las pistas, PERO NO TODAS, sino LAS QUE ESTÁN ABIERTAS
        AdaptadorNiveles eladap= new AdaptadorNiveles(getApplicationContext(),XXXXXXXXXXXXXXXXXXXXXXXXXXXXXX);
        lista.setAdapter(eladap);
*/
    }

    private void actualizarColorPuntos(){

        if(puntosUsuario<0){
            puntos.setText("Puntos: " + String.valueOf(puntosUsuario));
            puntos.setTextColor(Color.RED);

        }else{
            puntos.setTextColor(Color.BLACK);
        }


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

            puntosUsuario = p;
            monedasUsuario = m;

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


        //1. Hacemos SELECT de BD para ver si este usuario, esta categoria y este nivel se encuentran
                //a) Si están --> Saco el número de pistas que ha abierto
                //b) Si no están --> Hago un INSERT para meter todos los datos

        Log.d("HE ENTRADO AQUI", "holaaa");

        BD GestorDB = new BD (context, "BD", null, 1);
        SQLiteDatabase bd = GestorDB.getWritableDatabase();

        //Miramos en la BD
        Cursor cursor = bd.rawQuery("SELECT PISTAS FROM LOGROS WHERE USUARIO = '" + usuario + "' AND " +
                "CATEGORIA = '" + num_cat + "' AND NIVEL = '" + num_niv + "'", null);

        int cursorCount = cursor.getCount();

        if(cursorCount==0){         //El usuario nunca ha entrado en este nivel de esta categoría, por lo que hacemos un INSERT

            bd.execSQL("INSERT INTO LOGROS ('USUARIO', 'CATEGORIA', 'NIVEL', 'RESUELTO', 'PISTAS') VALUES ('" + usuario + "', " + Integer.valueOf(num_cat) +
                    ", " + Integer.valueOf(num_niv) + ", 0, 1)");

            pistasUtilizadas = 1;


            Log.d("estoy aqui 1", String.valueOf(pistasUtilizadas));


        }else{

            if (cursor.moveToFirst()) {
                int pistas = cursor.getInt(cursor.getColumnIndex("PISTAS"));

                pistasUtilizadas = pistas;

                Log.d("estoy aqui 2", String.valueOf(pistasUtilizadas));
            }

        }





        cursor.close();
        GestorDB.close();




    }


    private String[] obtenerPistas(){

        InputStream is = this.getResources().openRawResource(R.raw.data_es);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));

        String[] p = new String[5];


        try {
            String linea = reader.readLine();

            boolean pistasEncontradas = false;

            while(!pistasEncontradas){

                linea = reader.readLine();

                if(linea.equals(niv)){
                    pistasEncontradas=true;

                }

            }

            for(int x = 0; x < 5; x++){

                linea = reader.readLine();

                String[] l = linea.split(";");

                p[x]=l[1];


            }

            Log.d("pistas", p[0].toString() + "   " + p[1].toString());
            Log.d("pistas", String.valueOf(p.length));



        } catch (IOException e) {
            e.printStackTrace();
        }


        return p;



    }

    private String[] actualizarListaPistas(){

        String[] p = new String[pistasUtilizadas];

        for(int x=0; x < pistasUtilizadas; x++){

            p[x]=pistas[x];


        }

        return p;

    }


    private int calcularPuntosMonedas(){

        switch(pistasUtilizadas) {

            case 1:
                return 10;

            case 2:
                return 5;

            case 3:
                return 3;

            case 4:
                return 2;

            case 5:
                return 1;

        }

        return 0;
    }





    private void resolver(){

        //Se le muestra un Dialog al usuario con la respuesta y se le restan 20 monedas

        //Bajar teclado
        View view = Adivinanza.this.getCurrentFocus();
        if(view!=null){
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        //Mostramos el dialog indicando que el usuario ha fallado
        AlertDialog.Builder alertdialog=new AlertDialog.Builder(Adivinanza.this);
        alertdialog.setTitle("La respuesta correcta era... " + niv);
        alertdialog.setMessage("Qué lástima que no hayas podido adivinarlo. Seguro que el próximo nivel lo acertarás a la primera :D");
        alertdialog.setPositiveButton("OK", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alertdialog.show();


        monedasUsuario = monedasUsuario-20;
        monedas.setText("Monedas: " + String.valueOf(monedasUsuario));


        BD GestorDB = new BD (context, "BD", null, 1);
        SQLiteDatabase bd = GestorDB.getWritableDatabase();

        bd.execSQL("UPDATE USUARIOS SET MONEDAS=" + monedasUsuario + " WHERE USUARIO='" + usuario + "'");

        bd.execSQL("UPDATE LOGROS SET RESUELTO=1 WHERE USUARIO = '" + usuario + "' AND " +
                "CATEGORIA = '" + num_cat + "' AND NIVEL = '" + num_niv + "'");


        resuelto();


    }


    private void resuelto(){


        //Hacemos una consulta a la BD

        BD GestorDB = new BD (context, "BD", null, 1);
        SQLiteDatabase bd = GestorDB.getWritableDatabase();

        //Miramos en la BD
        Cursor cursor = bd.rawQuery("SELECT RESUELTO FROM LOGROS WHERE USUARIO = '" + usuario + "' AND " +
                "CATEGORIA = '" + num_cat + "' AND NIVEL = '" + num_niv + "'", null);



        if (cursor.moveToFirst()) {
            int r = cursor.getInt(cursor.getColumnIndex("RESUELTO"));

            if(r==0){
                resuelto = false;
            }else{
                resuelto = true;
            }
        }

        cursor.close();
        GestorDB.close();


    }


}