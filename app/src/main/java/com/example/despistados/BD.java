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

        db.execSQL("CREATE TABLE USUARIOS ('ID' INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, 'USUARIO' VARCHAR(255) NOT NULL, 'CONTRASENA' VARCHAR(255) NOT NULL)");

        db.execSQL("INSERT INTO USUARIOS ('USUARIO', 'CONTRASENA') VALUES ('admin', '1234')");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
