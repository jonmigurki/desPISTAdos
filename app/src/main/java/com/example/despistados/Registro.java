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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Registro extends AppCompatActivity {

    EditText usuarioR, contrasenaR;
    Button btnRegistrarse;

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        usuarioR = (EditText) findViewById(R.id.txtUsuarioR);
        contrasenaR = (EditText) findViewById(R.id.txtContrasenaR);
        btnRegistrarse = (Button) findViewById(R.id.btnRegistrarse);

        context = this.getApplicationContext();


        /*
        PASOS:

        1- Verificar que ningún campo está vacío
        2- Comprobar que el usuario introducido está disponible
        3- Registrar en la base de datos
        4- Intent al menú
         */

        btnRegistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String m1 = "";
                String m2 = "";
                if(String.valueOf(getResources().getConfiguration().locale).equals("es_ES")) {
                    m1 = "Debes rellenar los dos campos";
                    m2 = "Ya existe un usuario con ese nombre. Elige otro nombre";
                }else{
                    m1 = "You must write in both fields";
                    m2 = "There's already another user with that name. Choose another one";
                }

                    if (usuarioR.getText().toString().equals("") || contrasenaR.getText().toString().equals("")) {

                    Toast.makeText(getApplicationContext(), m1, Toast.LENGTH_SHORT).show();

                } else {

                    BD GestorDB = new BD(context, "BD", null, 1);
                    SQLiteDatabase bd = GestorDB.getReadableDatabase();

                    //Miramos en la BD
                    Cursor cursor = bd.rawQuery("SELECT USUARIO FROM USUARIOS WHERE USUARIO = '" + usuarioR.getText().toString() + "'", null);

                    int cursorCount = cursor.getCount();
                    cursor.close();
                    GestorDB.close();

                    if (cursorCount > 0) {
                        //Hay algun usuario con ese nombre en la BD
                        Toast.makeText(getApplicationContext(), m2, Toast.LENGTH_SHORT).show();

                    } else {

                        //REALIZAMOS EL REGISTRO

                        GestorDB = new BD(context, "BD", null, 1);
                        bd = GestorDB.getWritableDatabase();

                        bd.execSQL("INSERT INTO USUARIOS(USUARIO, CONTRASENA, PUNTOS, MONEDAS) VALUES ('" + usuarioR.getText().toString() + "', '" + contrasenaR.getText().toString() + "', 0, 50)");

                        Intent i = new Intent(Registro.this, Menu.class);
                        i.putExtra("usuario", usuarioR.getText().toString());
                        startActivity(i);
                        finish();

                    }
                }
            }
        });



    }





    //Método que se encarga de visualizar un Dialog cuando el usuario le da al botón de atrás de su teléfono
    public void onBackPressed() {

        String texto1 = "";
        String texto2 = "";
        String texto3 = "";

        if(String.valueOf(getResources().getConfiguration().locale).equals("es_ES")){
            texto1 = "Salir";
            texto2 = "¿Estás segur@ de que quieres cerrar sesión?";
            texto3 = "Sí";
        }else{
            texto1 = "Exit";
            texto2 = "Are you sure you want to log out?";
            texto3 = "Yes";
        }

        AlertDialog.Builder alertdialog=new AlertDialog.Builder(this);
        alertdialog.setTitle(texto1);
        alertdialog.setMessage(texto2);
        alertdialog.setPositiveButton(texto3, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //    Menu.super.onBackPressed();
                Intent i = new Intent(Registro.this, MainActivity.class);
                startActivity(i);
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