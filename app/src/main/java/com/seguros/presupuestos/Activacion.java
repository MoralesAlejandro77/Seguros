//******************************************************************************************************************************* 
package com.seguros.presupuestos;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.seguros.Actualizacion.UserFunctions;
import com.seguros.Datos.DatosBDTablas;

import org.json.JSONArray;
import org.json.JSONObject;

public class Activacion extends FragmentActivity  {
	EditText pass;
	Button boton;
	int opcion;
	ProgressBar progressBar;
	int Aplicacion_activa;

//******************************************************************************************************************************* 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acceso_clave);
        pass          = (EditText)findViewById(R.id.txtpass);
        boton         = (Button)findViewById(R.id.button1);
        progressBar   = (ProgressBar)findViewById(R.id.progressBar1);

        boton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Verifica_clave();
					
			}
		});
        
}

   



protected void Verifica_clave() {
	boolean error = false;

	if (pass.getText().toString().length()==0)
	{
	mostrar_error(2,"La Clave No puede ser nula!");
	error = true;
	}

	if (!error)
	{
		ActivarAplicacion itesmsrutas = new ActivarAplicacion(Activacion.this,progressBar);
		itesmsrutas.execute();
		
	}
	}





//******************************************************************************************************************************* 
public void mostrar_error(int tipo, String mensaje) {
		
	    LayoutInflater inflater = getLayoutInflater();
	    View layout = inflater.inflate(R.layout.customtoast, (ViewGroup) findViewById(R.id.toast_layout_root));
	 
	    TextView  text     = (TextView) layout.findViewById(R.id.toastText);
	    ImageView iconimag = (ImageView) layout.findViewById(R.id.toastImage);
	    text.setText(mensaje);
	    iconimag.setImageResource(0);
	    
	    if (tipo==1) //info
	       iconimag.setImageResource(R.drawable.info);
	    
	    if (tipo==2) //error
		       iconimag.setImageResource(R.drawable.error);

	    Toast t = new Toast(getApplicationContext());
		    t.setDuration(Toast.LENGTH_LONG);
		    t.setView(layout);
		    t.show();		
	}	
/*************************************************************************************************************/

private class ActivarAplicacion extends AsyncTask<String, Integer, Void> {
	 Context c; 
	 boolean errores = false;
	 
	public ActivarAplicacion(Context c, ProgressBar progressBar) {
		this.c = c;
		progressBar.setIndeterminate(true);
	}


	@Override
	protected void onPreExecute() 
	{
		progressBar.setVisibility(View.VISIBLE);
		errores = false;
		Aplicacion_activa = 0;
		
	}

	@Override
	protected Void doInBackground(String... urls) 
	{
	 try 
	   {
		 Aplicacion_activa = getItems();

		 
	   
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
			if (Aplicacion_activa == 1)
			{
				DatosBDTablas db = new DatosBDTablas(c);
				db.open(); 		
	    		db.RegistraActivacion();
                db.close();
				
				mostrar_error(1, "La Aplicacion esta activada correctamente, vuelva a ingresar!!!!");
				finish();
			}
			else
			{
				mostrar_error(2,"La Clave ingresada es Incorrecta!");
			}
       } 
	    catch (Exception e) 
       {
         e.printStackTrace();
       }
		progressBar.setVisibility(View.GONE);
   }		

}

/*************************************************************************************************************/
public int getItems() {
UserFunctions userFunction = new UserFunctions();
String idunico = Librerias.unico_ID(Activacion.this);
JSONArray  arreglo = userFunction.ActivarAplicacion(pass.getText().toString(),idunico);
int resultado = 0;

if (arreglo == null)
{
	    Toast.makeText(getBaseContext(),"No es posible consultar los Datos!" ,Toast.LENGTH_LONG).show();
}    
else
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
}
