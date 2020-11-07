//******************************************************************************************************************************* 
package com.seguros.presupuestos;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.seguros.Actualizacion.MenuActual;
import com.seguros.Datos.Datos;
import com.seguros.Datos.DatosBDTablas;

public class Menu extends AppCompatActivity {
	ProgressBar progressBar;
	Button ImageButton01, ImageButton02;
	boolean datoscargados = false;

//******************************************************************************************************************************* 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);
        progressBar     = (ProgressBar)findViewById(R.id.progressBar1);
        ImageButton01   = (Button)findViewById(R.id.ImageButton01);
        ImageButton02   = (Button)findViewById(R.id.ImageButton02);
		this.setTitle("");
        datoscargados = false;
		CargarDatos itesmsrutas = new CargarDatos(Menu.this);
		itesmsrutas.execute();
        
}
    
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.opciones, menu);
        return true;
    }
	
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case android.R.id.home:
  	             finish();	
	             return true;	    
	        case R.id.mn_cierre:
	        	cerrar_sesion();
	            return true;
	        case R.id.mn_act:
	        	actualizarvalores();
	            return true;
			case R.id.mn_precios:
				Modificar_ListaPrecios();
				return true;
			case R.id.mn_adm:
				adm();
				return true;
	 //       case R.id.mn_salir:
	//        	salir();
	//            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}

	private void Modificar_ListaPrecios() {
		Intent i = new Intent(Menu.this,ActivityPrecios.class);
		startActivity(i);
	}

	private void cerrar_sesion() {
		DatosBDTablas db = new DatosBDTablas(getApplicationContext());
		db.open();
		db.AgregarClave("");
		db.LimpiarLogin();
		db.BlanquearLogin();
		db.close();
		Librerias.Grabar_Perfil(getApplication(),"0");
		finish();
	}

	private void adm() {
		Intent i = new Intent(Menu.this,Activacion2.class);
		startActivity(i);
	}

	private void salir() {
		if (datoscargados)
		finish();

	}

	private void login_users() {
	if (datoscargados)
	{
	//	  if (Datos.Esta_Activada(getApplicationContext()))
	//	  {
			  if (Datos.Esta_Logueada(getApplicationContext()))
			  {
				    Toast.makeText(getBaseContext(),"Atencion Ud. ya esta logueado en la Aplicacion!" ,Toast.LENGTH_LONG).show();
			  }
			  else
			  {
				    Intent i = new Intent(Menu.this,LoginApp.class); 
				    startActivity(i);
				  
			  }
	//	  }
	//	  else
	//	  {
	//		    Intent i = new Intent(Menu.this,Activacion.class);
	//		    startActivity(i);
	//	  }
	}
		
	}      
    
	private void actualizarvalores() {
		if (datoscargados)
		{
			
	//	  if (Datos.Esta_Activada(getApplicationContext()))
	//	  {
			  if (Datos.Esta_Logueada(getApplicationContext()))
			  {
				    Intent i = new Intent(Menu.this,MenuActual.class); 
				    startActivity(i);
			  }
			  else
			  {
				    Intent i = new Intent(Menu.this,LoginApp.class); 
				    startActivity(i);
				  
			  }
	//	  }
	//	  else
	//	  {
	//		    Intent i = new Intent(Menu.this,Activacion.class);
	//		    startActivity(i);
	//	  }
		}
		
	}      
    
  public void segvida(View v){
//	  if (Datos.Esta_Activada(getApplicationContext()))
//	  {
		  if (Datos.Esta_Logueada(getApplicationContext()))
		  {
			    Intent i = new Intent(Menu.this,ListServicios.class); 
			    startActivity(i);
		  }
		  else
		  {
			    Intent i = new Intent(Menu.this,LoginApp.class); 
			    startActivity(i);
			  
		  }
//	  }
//	  else
//	  {
//		    Intent i = new Intent(Menu.this,Activacion.class);
//		    startActivity(i);
//	  }
		  
  }  
  
  public void segsepelio(View v){
	  
//	  if (Datos.Esta_Activada(getApplicationContext()))
//	  {
		  if (Datos.Esta_Logueada(getApplicationContext()))
		  {
				DatosBDTablas db = new DatosBDTablas(getApplicationContext());
				db.open(); 
				db.LimpiarTmpPlanes();
				db.close();
			    Intent i = new Intent(Menu.this,Sepelio.class);
			    startActivity(i);
		  }
		  else
		  {
			    Intent i = new Intent(Menu.this,LoginApp.class); 
			    startActivity(i);
			  
		  }
	//  }
	//  else
	//  {
	//	    Intent i = new Intent(Menu.this,Activacion.class);
	//	    startActivity(i);
	//  }
  }

  /**************************************************************************************/        
  private class CargarDatos extends AsyncTask<String, Integer, Void> {
 	 Context c; 
 	 boolean errores = false;
 	 
 	public CargarDatos(Context c) {
 		this.c = c;
 		progressBar.setIndeterminate(true);
 	}


 	@Override
 	protected void onPreExecute() 
 	{
 		progressBar.setVisibility(View.VISIBLE);
 		errores = false;
 		ImageButton01.setEnabled(false);
 		ImageButton02.setEnabled(false);
 		datoscargados = false; 	
 	}

 	@Override
 	protected Void doInBackground(String... urls) 
 	{
 	 try 
 	   {
 		DatosBDTablas db = new DatosBDTablas(c);
 		db.open(); 
 		Cursor datos  = db.ConsultarServicios();
		int total    = datos.getCount();
		if (total > 0) 
			datos.close();
 		db.close();



 	   } 
 	    catch (Exception e) 
        {
          e.printStackTrace();
          errores = true;
        }
        return null;

 	}

 	@Override
 	protected void onPostExecute(Void unused) 
 	{
 	    try 
 	    {
 	     if (!errores)
 	     {
 	    	datoscargados = true;
 	     }
 	        
       
        } 
 	    catch (Exception e) 
        {
          e.printStackTrace();
        }
 		ImageButton01.setEnabled(true);
 		ImageButton02.setEnabled(true);
 		progressBar.setVisibility(View.GONE);

		if (!Librerias.Esta_Tarifa(c)){

			Librerias.Grabar_Tarifa(c);
		}

		if (Datos.Esta_Logueada(getApplicationContext()))
		{
			if (!Librerias.Esta_act_vida(Menu.this) || !Librerias.Esta_act_sep(Menu.this))
			{
				ActualizarPreios();
			}
		}
		else
		{
			Intent i = new Intent(Menu.this,LoginApp.class);
			startActivity(i);

		}



    }

 }
//******************************************************************************************************************************* 
private void ActualizarPreios() {
	AlertDialog.Builder MENSAJE = new AlertDialog.Builder(Menu.this);

	MENSAJE.setMessage("Es necesario Actualizar las Lista de Precios¿Desea continuar?")
			.setTitle("Hay Listas de Precios Nuevas!")
			.setCancelable(false)
			.setNegativeButton("Cancelar",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							dialog.cancel();
						}
					})
			.setPositiveButton("Aceptar",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {

							if (Datos.Esta_Logueada(Menu.this))
							{
								Intent i = new Intent(Menu.this,MenuActual.class);
								startActivity(i);
							}
							else
							{
								Intent i = new Intent(Menu.this,LoginApp.class);
								startActivity(i);

							}


						}
					});
	AlertDialog alert = MENSAJE.create();
	alert.show();
}

    	  

}
