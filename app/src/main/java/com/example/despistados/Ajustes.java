package com.example.despistados;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.Locale;

public class Ajustes extends AppCompatActivity {

    Button cambiarIdioma, atras;

    Locale locale;
    Configuration config = new Configuration();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajustes);


        cambiarIdioma = (Button) findViewById(R.id.btnIdioma);
        atras = (Button) findViewById(R.id.btnAtras2);


        cambiarIdioma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder b = new AlertDialog.Builder(Ajustes.this.getApplicationContext());
                b.setTitle("Cambiar idioma");

                String[] idiomas = {"Español", "English"};
                b.setItems(idiomas, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                        switch(which){
                            case 0:
                                locale = new Locale("es");
                                config.locale =locale;
                                break;
                            case 1:
                                locale = new Locale("en");
                                config.locale =locale;
                                break;

                        }
                        
                        getResources().updateConfiguration(config, null);
                        Intent refresh = new Intent(Ajustes.this, Ajustes.class);
                        startActivity(refresh);
                        finish();
                    }

                });

                b.show();





            }
        });


        atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(Ajustes.this, MainActivity.class);
                startActivity(i);
            }
        });


    }
}