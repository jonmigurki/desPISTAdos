package com.example.despistados;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    //Actividad que se encarga de crear la ventana principal de la aplicación

    Button entrar;
    Button registrar;
    EditText usuario;
    EditText contrasena;
    Context context;

    Button ajustes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        entrar = (Button) findViewById(R.id.btnEntrar);
        registrar = (Button) findViewById(R.id.btnRegistrar);
        usuario = (EditText) findViewById(R.id.txtUsuario);
        contrasena = (EditText) findViewById(R.id.txtContrasena);

        context = this.getApplicationContext();


        entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //Recogemos los datos
                String u = usuario.getText().toString();
                String c = contrasena.getText().toString();

                BD GestorDB = new BD(context, "BD", null, 1);
                SQLiteDatabase bd = GestorDB.getWritableDatabase();

                //Miramos en la BD
                Cursor cursor = bd.rawQuery("SELECT USUARIO FROM USUARIOS WHERE USUARIO = '" + u + "' AND CONTRASENA = '" + c + "'", null);

                int cursorCount = cursor.getCount();
                cursor.close();
                GestorDB.close();

                if (cursorCount > 0) {
                    Intent i = new Intent(MainActivity.this, Menu.class);
                    i.putExtra("usuario", u);
                    startActivity(i);
                    finish();
                } else {

                    String m = "";
                    if(String.valueOf(getResources().getConfiguration().locale).contains("es")){
                        m = "Has introducido mal algún campo. Vuelve a intentarlo.";
                    }else{
                        m = "You have written something wrong. Try it again.";
                    }

                    Toast.makeText(getApplicationContext(), m, Toast.LENGTH_SHORT).show();

                }


            }
        });


        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Nuevo formulario para rellenar usuario y contrasena

                Intent i = new Intent(MainActivity.this, Registro.class);
                startActivity(i);
                finish();
            }
        });



        ajustes = (Button) findViewById(R.id.btnAjustes);
        ajustes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, Ajustes.class);
                startActivity(i);
                finish();
            }
        });

    }



    /*
            Basado en el código extraído de Stack Overflow
            Pregunta: https://stackoverflow.com/questions/45729852/android-check-if-back-button-was-pressed
            Autor: https://stackoverflow.com/users/4586742/bob
            Modificado por Jon Miguel para adaptar las funcionalidades y mensajes deseados
        */

    public void onBackPressed() {

        String m1 = "";
        String m2 = "";
        String m3 = "";

        if(String.valueOf(getResources().getConfiguration().locale).contains("es")){
            m1 = "Salir";
            m2 = "¿Estás segur@ de que quieres salir?";
            m3 = "Sí";
        }else{
            m1 = "Exit";
            m2 = "Are you sure you want to exit?";
            m3 = "Yes";
        }


        AlertDialog.Builder alertdialog=new AlertDialog.Builder(this);
        alertdialog.setTitle(m1);
        alertdialog.setMessage(m2);
        alertdialog.setPositiveButton(m3, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MainActivity.super.onBackPressed();
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