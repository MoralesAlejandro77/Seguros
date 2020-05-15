//******************************************************************************************************************************* 
package com.seguros.presupuestos;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
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
import com.seguros.Actualizacion.MenuActual;
import com.seguros.Actualizacion.UserFunctions;
import com.seguros.Cuentas.Cuentas;
import com.seguros.Datos.Datos;
import com.seguros.Datos.DatosBDTablas;

public class LoginApp extends FragmentActivity  {
	private static final int MY_PERMISSIONS_REQUEST_INTERNET = 101;
	private static final int MY_PERMISSIONS_REQUEST_CONTACTS = 102;
	EditText pass, id;
	Button boton;
	int opcion;
	ProgressBar progressBar;
	int Aplicacion_activa;
	String usuario = "";
	int resultado = 0,tipoacceso = 0, respuesta = 0;
	int actual    = 0;
	boolean permiso = false;
	int pperfil;

	//*******************************************************************************************************************************
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        id            = (EditText)findViewById(R.id.id);
        pass          = (EditText)findViewById(R.id.txtpass);
        boton         = (Button)findViewById(R.id.button1);
        progressBar   = (ProgressBar)findViewById(R.id.progressBar1);
		progressBar.setVisibility(View.GONE);
		try {
	        resultado = Integer.valueOf(this.getIntent().getExtras().getString("producto"));
			tipoacceso = Integer.valueOf(this.getIntent().getExtras().getString("tipoacceso"));



		} catch (Exception e) {
			resultado = 0;
		}
        
		try {
			actual = Integer.valueOf(this.getIntent().getExtras().getString("actual"));
			
		} catch (Exception e) {
			actual = 0;
		}
        
        boton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Verifica_clave();
					
			}
		});
        
        pperfil = 0;
		DatosBDTablas db = new DatosBDTablas(getApplicationContext());
		db.open(); 		
		Cursor datos  = db.ConsultarClave();
		
		int total    = datos.getCount();
		if (total > 0) 
		{ 
			datos.moveToFirst();
			if (datos.getInt(4) > 0)
			id.setText(String.valueOf(datos.getInt(4)));
            datos.close();
		}
		db.close();	           
               
}
//*******************************************************************************************************************************
protected void Verifica_clave() {
	boolean error = false;
	if (id.getText().toString().length()==0)
		{
			Librerias.mostrar_error(LoginApp.this,2,"Debe indicar el Id del Usuario!!");
		error = true;
		}

	if (pass.getText().toString().length()==0)
	{
		Librerias.mostrar_error(LoginApp.this,2,"La Clave No puede ser nula!");
	error = true;
	}

	if (!error)
	{

		permiso = false;
		if (Build.VERSION.SDK_INT >= 23)
		{
			if (ContextCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.READ_PHONE_STATE)!= PackageManager.PERMISSION_GRANTED) {

				if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_PHONE_STATE)) {
					/*********************************************************/
					Toast.makeText(getBaseContext(),"Se necesita este permiso para poder completar la operacion!" ,Toast.LENGTH_LONG).show();
					ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, MY_PERMISSIONS_REQUEST_INTERNET);
					/*************************************************************/
				} else {

					Toast.makeText(getBaseContext(),"Se necesita este permiso para poder completar la operacion!" ,Toast.LENGTH_LONG).show();
					ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, MY_PERMISSIONS_REQUEST_INTERNET);

				}
			}
			else
				permiso = true;
		}
		else
			permiso = true;

		if (permiso){
			if (Librerias.verificaConexion(this.getApplicationContext()))
			{
				LoginUser();
			}
			else
				Toast.makeText(getBaseContext(),"No es posible establecer la conexion con el Servidor!" ,Toast.LENGTH_LONG).show();

		}

		
	}
	}
//*******************************************************************************************************************************
	private void LoginUser() {
		boolean permiso = false;
		if (Build.VERSION.SDK_INT >= 23)
		{
			if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_CONTACTS)!= PackageManager.PERMISSION_GRANTED) {

				if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_CONTACTS)) {
					/*********************************************************/
					Toast.makeText(getBaseContext(),"Se necesita este permiso para poder completar la operacion!" ,Toast.LENGTH_LONG).show();
					ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, MY_PERMISSIONS_REQUEST_CONTACTS);
					/*************************************************************/
				} else {

					Toast.makeText(getBaseContext(),"Se necesita este permiso para poder completar la operacion!" ,Toast.LENGTH_LONG).show();
					ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, MY_PERMISSIONS_REQUEST_CONTACTS);

				}
			}
			else
				permiso = true;
		}
		else
			permiso = true;


		if (permiso){
			LoginUsuario();
		}
	}
//*******************************************************************************************************************************
	@Override
	public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
		switch (requestCode) {
			case MY_PERMISSIONS_REQUEST_INTERNET: {
				// If request is cancelled, the result arrays are empty.
				if (grantResults.length > 0
						&& grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					permiso = true;

				} else {

				}
				return;
			}
		}
	}

//*******************************************************************************************************************************

	private void LoginUsuario(){

		preparar();
		StringRequest stringRequest = new StringRequest(Request.Method.POST, UserFunctions.loginURL3,
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
						progressBar.setVisibility(View.GONE);
						boton.setEnabled(true);
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
		Cuentas cuentas = new Cuentas();
		cuentas = Librerias.LeerCuentas(LoginApp.this);
		String idunico = Librerias.unico_ID(LoginApp.this);

		Map<String,String> params = new HashMap<String, String>();
		params.put("clave"      , pass.getText().toString());
		params.put("id"         , id.getText().toString());
		params.put("idunico"    , idunico);
		params.put("cuenta"     , cuentas.getNombre());
		params.put("version"    , Librerias.getAndroidVersion());
		params.put("tag"        ,"3P197792S");
		params.put("app"        , getString(R.string.version));
		return params;
	}
	/*************************************************************************************************************/
	public void finalizar(String response) {

		respuesta         = 0;

		try {
			JSONArray jsonObject = new JSONArray(response);
			JSONObject valor     = new JSONObject(jsonObject.get(0).toString());
			Aplicacion_activa    = valor.getInt("status");
			usuario              = valor.getString("nombre");
			pperfil              = Integer.valueOf(valor.getString("perfil"));


			if (tipoacceso == 1 && Aplicacion_activa != 0 &&(pperfil == 0 || pperfil == 2))
				Aplicacion_activa = 3;

			if (tipoacceso == 2 && Aplicacion_activa != 0 &&(pperfil == 0 || pperfil == 1))
				Aplicacion_activa = 3;


			try
			{
				DatosBDTablas db = new DatosBDTablas(getApplicationContext());
				db.open();
				if (Aplicacion_activa == 1)
				{
					if (resultado==700||resultado==600)
						db.AgregarClave("");
					db.RegistraLogin(Integer.valueOf(id.getText().toString()));
					if (resultado==600 ){
						Librerias.Grabar_Perfil(getApplication(),String.valueOf(pperfil));
						if (!Datos.Esta_Logueada(getApplicationContext()))
							db.RegistraEmpresa(Integer.valueOf(id.getText().toString()));
					}



					Intent resultData = new Intent();
					setResult(resultado,resultData);

					if (actual != 1)
						Librerias.mostrar_error(LoginApp.this,1, "Ud. ha sido identificado con Exito!!!! " + usuario);
					finish();
				}
				else
				{
					if (Aplicacion_activa == 3){
						//	db.BlanquearLogin();
						Librerias.mostrar_error(LoginApp.this,2,"No tiene permisos para acceder a esta opcion..!");
					}else {
						//	db.BlanquearLogin();
						Librerias.mostrar_error(LoginApp.this,2,"Los datos ingresados son incorrectos, intente nuevamente..!");

					}


				}
				db.close();

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
