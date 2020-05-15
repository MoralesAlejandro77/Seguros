/***********************************************************************************************************************/	
package com.seguros.Datos;


/***********************************************************************************************************************/	
/*
 * Programador: Ing. Alejandro Morales
 * Producciones Ale77
 * Modulo: Presupuestador Seguro de Vida y Sepelio
 * Empresa : TRES PROVINCIAS SEGUROS - MENDOZA
 * 
 */
/***********************************************************************************************************************/	


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;
/***********************************************************************************************************************/	
/**
 *
 * @author ALE77
 */
/***********************************************************************************************************************/	
public class DatosBDTablas_tmp {
private BaseDatos_tmp dbHelper;
private SQLiteDatabase db;
private final Context contexto;
public static final String TAG = "DATOS";   

//**************************************************************************************
public DatosBDTablas_tmp(Context contexto) 
{
this.contexto = contexto;
}
//**************************************************************************************
public SQLiteDatabase open() throws SQLException 
{
dbHelper = new BaseDatos_tmp(contexto);
db = dbHelper.getWritableDatabase();
return db;
}
//**************************************************************************************
public void close() 
{
dbHelper.close();
}
//**************************************************************************************
public long AgregarFamilia(String dni, String edad, String fechanac, String nombre)
{
	ContentValues registro = new ContentValues();
	registro.put("dni"     , dni);
	registro.put("edad", edad);
	registro.put("fechanac"  , fechanac);
	registro.put("nombre"       , nombre);

	
	return db.insert(BaseDatos_tmp.TABLA_FAMILIA, null, registro);
}
//**************************************************************************************
public Cursor obtenerFamilia() throws SQLException {
Cursor c=db.rawQuery("SELECT dni as _id, edad, fechanac, nombre  FROM Familia", null);
return c;
}
//**************************************************************************************

public int LimpiarTablaflia()
{
	return db.delete(BaseDatos_tmp.TABLA_FAMILIA, "", null);
}
//**************************************************************************************

}
    