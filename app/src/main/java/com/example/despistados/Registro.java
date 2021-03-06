package com.example.despistados;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
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

                if (usuarioR.getText().toString().equals("") || contrasenaR.getText().toString().equals("")) {

                    Toast.makeText(getApplicationContext(), "Debes rellenar los dos campos.", Toast.LENGTH_SHORT).show();

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
                        Toast.makeText(getApplicationContext(), "Ya existe un usuario con ese nombre. Elige otro nombre.", Toast.LENGTH_SHORT).show();

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
}