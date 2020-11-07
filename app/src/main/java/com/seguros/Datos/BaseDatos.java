package com.seguros.Datos;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
/***********************************************************************************************************************/	
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

public class BaseDatos extends SQLiteOpenHelper {
//**************************************************************************************************    
public static final String TAG = "IDATOS";   
public static final String packageName          = "Android.Seguros.Datos";
public static final int DATABASE_VERSION        = 10;
public static final String DATABASE_NAME        = "BDSeguros";
public static final String TABLA_SERVICIOS      = "Servicios";
public static final String TABLA_PRIMAS         = "Primas";
public static final String TABLA_PLANES         = "Planes";
public static final String TABLA_PLANES_TMP     = "tmpPlanes";
public static final String TABLA_PARAMETROS     = "parametros";
public static final String TABLA_CONTROL        = "control";
public static final String TABLA_GASTOS         = "gastos";
public static final String TABLA_CAPITAL        = "capital";
public static final String TABLA_LOG            = "log";
public static final String TABLA_TARIFAS        = "Tarifas";
//public static final String TABLA_Complejidad    = "Complejidad";

private Context contexto;
//**************************************************************************************************    
public static final String crearTablaServicios = "create table if not exists "  
	  + " Servicios (codigo integer primary key, descripcion text,  tipo_base integer, porc real,"  
	  + " valor_base real, edad_max integer, lectura integer, tipocalculo integer, tipo_mes integer );";
//**************************************************************************************************    
public static final String crearTablaPrimas = "create table if not exists "  
		  + " Primas (codigo integer, edad integer,  "  
		  + " edad_max integer, porc real, valor real, primary key (codigo, edad));";
//**************************************************************************************************    
public static final String crearTablaPlanes = "create table if not exists "  
		  + " Planes (tarifa integer, plan text,  servicio integer, edadi integer, edadf integer,"
		  + " valor real, lectura integer , primary key (tarifa, plan, servicio, edadi));";
	//**************************************************************************************************    
public static final String crearTablaTmpPlanes = "create table if not exists "  
		  + " tmpPlanes (tarifa integer, codigo integer, fechanac text,  edad integer, luto real, parcela real,"
		  + " sepelio real, prima real , primary key (tarifa, codigo));";
	//**************************************************************************************************    
public static final String crearTablaParametros = "create table if not exists "  
		  + " parametros (codigo integer  primary key, clave text,  activado integer, login integer, id integer, fecha_act text, fecha_login text );" ;

	//**************************************************************************************************    
public static final String crearTablaControl = "create table if not exists "  
		  + " control (codigo integer  primary key, fechainst text,  fechavigencia text, activado integer);" ;

	//**************************************************************************************************    
public static final String crearTablaGastos = "create table if not exists "  
		  + " gastos (codigo integer  primary key, importe float);" ;

	//**************************************************************************************************    
public static final String crearTablaCapital = "create table if not exists "  
		  + " capital (codigo integer  primary key, importe float);" ;

	//**************************************************************************************************    
public static final String crearTablaLog = "create table if not exists "  
		  + " log (fecha text primary key);" ;

//**************************************************************************************************
    public static final String crearTablaTarifas = "create table if not exists "
            + " Tarifas (codigo integer primary key, descripcion text,  tipo integer );";
//**************************************************************************************************
    /*
public static final String crearTablaComplejidad = "create table if not exists "
        + " Planes (codigo integer, descripcion text,  sexo integer, edadi integer, edadf integer,"
        + " valor real, valor2 real , primary key (codigo, sexo, edadi));";

     */
//**************************************************************************************************


  public BaseDatos(Context contexto) {
                super(contexto, DATABASE_NAME, null, DATABASE_VERSION);
        }
        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.i(TAG, "Creando datos!");
            db.execSQL(crearTablaServicios);
            db.execSQL(crearTablaPrimas);
            db.execSQL(crearTablaPlanes);
            db.execSQL(crearTablaTmpPlanes);
            db.execSQL(crearTablaParametros);
            db.execSQL(crearTablaControl);
            db.execSQL(crearTablaGastos);
            db.execSQL(crearTablaCapital);
            db.execSQL(crearTablaLog);
            db.execSQL(crearTablaTarifas);
//            db.execSQL(crearTablaComplejidad);


        }   
//**************************************************************************************************    
@Override
public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
                Log.w(TAG, "Upgrading database from version " + oldVersion
                                + " to " + newVersion + ", which will destroy all old data");
    /*
                db.execSQL("DROP TABLE IF EXISTS " + TABLA_SERVICIOS);
                db.execSQL("DROP TABLE IF EXISTS " + TABLA_PRIMAS);
                db.execSQL("DROP TABLE IF EXISTS " + TABLA_PLANES);
                db.execSQL("DROP TABLE IF EXISTS " + TABLA_PLANES_TMP);
                db.execSQL("DROP TABLE IF EXISTS " + TABLA_PARAMETROS);
                db.execSQL("DROP TABLE IF EXISTS " + TABLA_CONTROL);
                db.execSQL("DROP TABLE IF EXISTS " + TABLA_GASTOS);
                db.execSQL("DROP TABLE IF EXISTS " + TABLA_CAPITAL);
                db.execSQL("DROP TABLE IF EXISTS " + TABLA_LOG);
                db.execSQL("DROP TABLE IF EXISTS " + TABLA_TARIFAS)
                db.execSQL("DROP TABLE IF EXISTS " + TABLA_Complejidad)
                */

        switch(oldVersion) {
        case 9:
            // db.execSQL(DATABASE_CREATE_color);
            // we want both updates, so no break statement here...
        case 10:
     //       db.execSQL(crearTablaComplejidad);
        }
        onCreate(db);
        }
//********************************************************************************  

}        