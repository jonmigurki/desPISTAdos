package com.example.despistados;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.view.View;

import androidx.annotation.Nullable;

public class BD extends SQLiteOpenHelper {


    public BD(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE USUARIOS ('ID' INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +     //Id 1, 2, 3, ...
                "'USUARIO' VARCHAR(255) NOT NULL, " +                                               //Nombre del usuario
                "'CONTRASENA' VARCHAR(255) NOT NULL, " +                                            //Contraseña del usuario
                "'PUNTOS' INT NOT NULL, " +                                                        //Puntos del usuario
                "'MONEDAS' INT NOT NULL, " +                                                        //Monedas del usuario
                "'LOGROS' TEXT)");                                                                  //JSON que contendrá todos los niveles ya resueltos

        /*
                { [ { categoria: 1, nivel: 1, pistas: 3, resuelto = "False"} ] , [ { categoria: 2, nivel: 1, pistas: 1 , resuelto = "True"} ] , ... , [ {} ] }
         */

        db.execSQL("INSERT INTO USUARIOS ('USUARIO', 'CONTRASENA', 'PUNTOS', 'MONEDAS') VALUES ('admin', '1234', 0, 50)");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
