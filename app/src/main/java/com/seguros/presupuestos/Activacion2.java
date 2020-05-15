//******************************************************************************************************************************* 
package com.seguros.presupuestos;

import android.content.Context;
import android.content.Intent;
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

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.seguros.Actualizacion.UserFunctions;
import com.seguros.Datos.DatosBDTablas;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Activacion2 extends FragmentActivity  {
	EditText pass;
	Button boton;
	int opcion;
	ProgressBar progressBar;
	int Aplicacion_activa;

//******************************************************************************************************************************* 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acceso_clave2);
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
		Librerias.mostrar_error(Activacion2.this,2,"La Clave No puede ser nula!");

	error = true;
	}

	if (!error)
	{
		Verificar_Activacion();
		
	}
	}





//******************************************************************************************************************************* 
	private void Verificar_Activacion(){

		preparar();
		StringRequest stringRequest = new StringRequest(Request.Method.POST, UserFunctions.loginURL2,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						finalizar(response);

					}
				},
				new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_LONG).show();
					}
				}){

			@Override
			protected Map<String,String> getParams(){
				Map<String,String> params = preparar_Parametros();
				return params;
			}

		};
		stringRequest.setRetryPolicy(new DefaultRetryPolicy(
				0,
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

		RequestQueue requestQueue = Volley.newRequestQueue(this);
		requestQueue.add(stringRequest);
	}
	/*************************************************************************************************************/
	public void preparar() {
		progressBar.setIndeterminate(true);
		progressBar.setVisibility(View.VISIBLE);
		Aplicacion_activa = 0;
		boton.setEnabled(false);
	}
	/*************************************************************************************************************/
	public Map<String,String> preparar_Parametros() {
		String idunico = Librerias.unico_ID(Activacion2.this);
		Map<String,String> params = new HashMap<String, String>();
		params.put("clave"     , pass.getText().toString());
		params.put("id"         , idunico);
		return params;
	}
	/*************************************************************************************************************/
	public void finalizar(String response) {
		Aplicacion_activa = 0;

		try {
			JSONArray jsonObject = new JSONArray(response);
			JSONObject valor     = new JSONObject(jsonObject.get(0).toString());
			Aplicacion_activa    = valor.getInt("status");

			try
			{

				if (Aplicacion_activa == 1) // Exitoso
				{
					finish();
					Intent i = new Intent(Activacion2.this,ListaVendedores.class);
					startActivity(i);

				}
				else
				{
					Librerias.mostrar_error(Activacion2.this,2,"La Clave ingresada es Incorrecta!");
				}


			}
			catch (Exception e)
			{
				e.printStackTrace();
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
		progressBar.setVisibility(View.GONE);
		boton.setEnabled(true);

	}
//*******************************************************************************************************************************
}
