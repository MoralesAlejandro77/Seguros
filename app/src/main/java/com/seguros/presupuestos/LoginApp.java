//******************************************************************************************************************************* 
package com.seguros.presupuestos;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.security.ProviderInstaller;
import com.seguros.Actualizacion.UserFunctions;
import com.seguros.Cuentas.Cuentas;
import com.seguros.Datos.Datos;
import com.seguros.Datos.DatosBDTablas;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class LoginApp extends Activity {
	private static final int MY_PERMISSIONS_REQUEST_INTERNET = 101;
	private static final int MY_PERMISSIONS_REQUEST_CONTACTS = 102;
	EditText pass, id;
	Button boton, botonb;
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
		botonb        = (Button)findViewById(R.id.bblanquear);

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

		botonb.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Blanquear_clave();
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
	/*	Cuentas cuentas = new Cuentas();
		cuentas = Librerias.LeerCuentas(LoginApp.this);
		String nombrecuenta =  cuentas.getNombre();
		String idunico      = Librerias.unico_ID(LoginApp.this);*/
		String idunico      = "";
		String nombrecuenta =  "";

		Map<String,String> params = new HashMap<String, String>();
		params.put("clave"      , pass.getText().toString());
		params.put("id"         , id.getText().toString());
		params.put("idunico"    , idunico);
		params.put("cuenta"     , nombrecuenta);
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
@SuppressLint("TrulyRandom")
public static void handleSSLHandshake() {
    try {
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }

            @Override
            public void checkClientTrusted(X509Certificate[] certs, String authType) {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] certs, String authType) {
            }
        }};

        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String arg0, SSLSession arg1) {
                return true;
            }
        });
    } catch (Exception ignored) {
    }
}

    private void updateAndroidSecurityProvider() { try { ProviderInstaller.installIfNeeded(this); } catch (Exception e) { e.getMessage(); } }

    /*************************************************************************************************************/
    private void Blanquear_clave(){

        preparar2();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UserFunctions.loginURL21,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        finalizar_blanqueo(response);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                        botonb.setEnabled(true);
                    }
                }){

            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = preparar_blanqueo();
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
    public void preparar2() {
        progressBar.setIndeterminate(true);
        progressBar.setVisibility(View.VISIBLE);
        Aplicacion_activa = 0;
        botonb.setEnabled(false);
    }
    /*************************************************************************************************************/
    public Map<String,String> preparar_blanqueo() {
        Map<String,String> params = new HashMap<String, String>();
        params.put("tag"           ,"3P197792S");
   //     params.put("tipodoc"       , String.valueOf((int) tipo_doc.getSelectedItemId()));
  //      params.put("dni"           ,  nro_doc.getText().toString());
  //      params.put("version"     , version_and);
        params.put("app"         , getString(R.string.version));
        return params;
    }
    /*************************************************************************************************************/
    public void finalizar_blanqueo(String response) {
        Aplicacion_activa = 0;

        try {
            JSONArray jsonObject = new JSONArray(response);
            JSONObject valor     = new JSONObject(jsonObject.get(0).toString());
            Aplicacion_activa    = valor.getInt("status");

            try
            {
                if (Aplicacion_activa == 1) // Exitoso
                {

                    Librerias.mostrar_error(LoginApp.this,1, "Le enviamos un correo con su CLAVE Provisoria!!");
                    finish();

                }
                else
                {
                    Librerias.mostrar_error(LoginApp.this,2,"SE HA PRODUCIDO UN ERROR  ");
                }


            }
            catch (Exception e)
            {
                Librerias.mostrar_error(LoginApp.this,2,"SE HA PRODUCIDO UN ERROR  " + e.toString());
                e.printStackTrace();
            }

        } catch (JSONException e) {
            Librerias.mostrar_error(LoginApp.this,2,"SE HA PRODUCIDO UN ERROR  " + e.toString());
            e.printStackTrace();
        }
        progressBar.setVisibility(View.GONE);
        botonb.setEnabled(true);

    }
//*******************************************************************************************************************************

}
