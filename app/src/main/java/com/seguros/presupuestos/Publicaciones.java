package com.seguros.presupuestos;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.seguros.Actualizacion.UserFunctions;
import com.seguros.Cuentas.Cuentas;
import com.seguros.Datos.Datos;
import com.seguros.Datos.DatosBDTablas;

import org.json.JSONArray;
import org.json.JSONObject;

	public class Publicaciones extends Service {
	String ruta;		 
	    @Override
	    public IBinder onBind(Intent intent) {
	        // TODO Auto-generated method stub
	        return null;
	    }
	 
	    @Override
	    public int onStartCommand(Intent intent, int flags, int startId) {
	    	
		Librerias.ForzarVersion();	 
		
		if (Librerias.verificaConexion(Publicaciones.this))
		{
	//		  if (Datos.Esta_Activada(getApplicationContext()))
	//		  {
				  if (Datos.Esta_Logueada(getApplicationContext()))
				  {
					  if (Datos.Registro_Log(Publicaciones.this))
						  registrar_log();
				  }
		//	  }
			
		}
		
	    stopSelf();
	    return startId;
	    }
	 
/****************************************************************************************/
private void registrar_log() {
	int Aplicacion_activa = verificarActivo(getBaseContext());

	try 
	{
				DatosBDTablas db = new DatosBDTablas(getBaseContext());
				db.open(); 		
				if (Aplicacion_activa == 2)
		    	   db.BlanquearLogin();
	            db.close();
				
	       } 
		    catch (Exception e) 
	       {
	         e.printStackTrace();
	}	
		    
		//	getItemsControl("1");
		//	getItemsControl("2");
		    
	}

/****************************************************************************************/

@Override
public void onDestroy() {
	super.onDestroy();
}
/****************************************************************************************/
	    
@Override
public void onStart(Intent intent, int startId) {
	super.onStart(intent, startId);
}	    
/****************************************************************************************/
	    
public int verificarActivo(Context c) {
	    	boolean error = true;
	    	String id = Datos.Obtener_id_login(Publicaciones.this);

	    	Cuentas cuentas = new Cuentas();
	    	cuentas = Librerias.LeerCuentas(c);
	    	String idunico = Librerias.unico_ID(c);
	    	UserFunctions userFunction = new UserFunctions();
	    	JSONArray  arreglo = userFunction.VerificaUserActivo(id,cuentas, idunico, Librerias.getAndroidVersion(),getString(R.string.version));
	    	int resultado = 0;

	    	if (arreglo != null)
	    		{	
	    		try {
	    			int cantidad = 0;
	    			cantidad = arreglo.length();   
	    			int j = 0;   
	    			if (cantidad > 0) { 
	    			    JSONObject json = new JSONObject(arreglo.getJSONObject(j).toString());
	    				resultado = json.getInt("status");
	    			}		
	    		} catch (Exception e) {
	    			// TODO: handle exception
	    		}
	    			

	    			
	    	   }
	    	return resultado;
	    		}		    

/************************************************************************************/

	    public boolean getItemsControl(String producto) {
	    	boolean finalizo = false;	
	    	String idunico = Librerias.unico_ID(Publicaciones.this);
	    	UserFunctions userFunction = new UserFunctions();
	    	String id = Datos.Obtener_id_login(Publicaciones.this);
	    	JSONArray  arreglo = userFunction.ListaControl("Control",producto,id,idunico);

	    	if (arreglo != null)
	    		{	
	    			
	    			int cantidad = 0;
	    			cantidad = arreglo.length();   
	    			
	    			if (cantidad > 0) { 
	    				    try
	    				      {
	    					  	DatosBDTablas db = new DatosBDTablas(Publicaciones.this);
	    						db.open(); 
	    					    JSONObject json = new JSONObject(arreglo.getJSONObject(0).toString());
	    			    		String[] fecha = json.getString("fechaact").split("-");
	    			    		String fechaact = fecha[2] + "/" + fecha[1] + "/" + fecha[0];
	    			    		fecha = json.getString("fechavig").split("-");
	    			    		String fechavig = fecha[2] + "/" + fecha[1] + "/" + fecha[0];

	    						db.AgregarControl(2,Integer.valueOf(producto), fechaact, fechavig, 1);
	    						db.close();
	    						finalizo = true;
	    				     	 }
	    				 	    catch (Exception e) 
	    				        {
	    				          e.printStackTrace();
	    				        }     	 
	    			}
	    			
	    	   }
	    	return finalizo;
	    	}
/************************************************************************************/
	    
	}