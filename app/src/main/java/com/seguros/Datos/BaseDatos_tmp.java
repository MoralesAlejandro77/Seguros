package com.seguros.Datos;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
/**********************************************************************************************************************/	
/*
 * Programador: Ing. Alejandro Morales
 * Producciones Ale77
 * Fecha de ultima Actualizacion 06-03-2015
 * Version : 1.4
 * Modulo: Presupuestador Seguro de Vida y Sepelio
 * Empresa : TRES PROVINCIAS SEGUROS - MENDOZA
 * 
 */
/***********************************************************************************************************************/	

public class BaseDatos_tmp extends SQLiteOpenHelper {
//**************************************************************************************************    
public static final String TAG = "IDATOS";   
public static final String packageName      = "Android.Cocheria.Datos";
public static final int DATABASE_VERSION    = 1;
public static final String DATABASE_NAME    = "BDCocherias";
public static final String TABLA_FAMILIA  = "Familia";

private Context contexto;
//**************************************************************************************************
public static final String crearTablaFlia = "create table if not exists "
		  + " Familia (dni text, edad text,  "
		  + " fechanac text, nombre text, primary key (dni));";
//**************************************************************************************************    

  public BaseDatos_tmp(Context contexto) {
                super(contexto, DATABASE_NAME, null, DATABASE_VERSION);
        }
        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.i(TAG, "Creando datos!");
            db.execSQL(crearTablaFlia);

        }   
//**************************************************************************************************    
@Override
public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
                Log.w(TAG, "Upgrading database from version " + oldVersion
                                + " to " + newVersion + ", which will destroy all old data");

                db.execSQL(crearTablaFlia);
                onCreate(db);
        }
//********************************************************************************  

}        