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

public class MainActivity extends AppCompatActivity {

    Button entrar;
    Button registrar;
    EditText usuario;
    EditText contrasena;
    Context context;

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

                BD GestorDB = new BD (context, "BD", null, 1);
                SQLiteDatabase bd = GestorDB.getWritableDatabase();

                //Miramos en la BD
                Cursor cursor = bd.rawQuery("SELECT USUARIO FROM USUARIOS WHERE USUARIO = '" + u + "' AND CONTRASENA = '" + c + "'", null);

                int cursorCount = cursor.getCount();
                cursor.close();
                GestorDB.close();

                if (cursorCount > 0) {
                    Intent i = new Intent(MainActivity.this, Menu.class);
                    startActivity(i);
                }else{
                    Toast.makeText(getApplicationContext(), "Has introducido mal algún campo. Vuelve a intentarlo.", Toast.LENGTH_SHORT).show();

                }



            }
        });


        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Nuevo formulario para rellenar usuario y contrasena

                Intent i = new Intent(MainActivity.this, Registro.class);
                startActivity(i);

            }
        });

    }
}