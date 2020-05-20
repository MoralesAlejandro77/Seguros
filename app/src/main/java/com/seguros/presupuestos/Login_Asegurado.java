package com.seguros.presupuestos;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
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
import com.seguros.Cocherias.BuscarAsegurado;
import com.seguros.Datos.Datos;

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

public class Login_Asegurado extends AppCompatActivity {
ProgressBar progressBar;
Spinner tipo_doc;
EditText nro_doc, password;
Button blogin, bpromotores, bcontactos, bprestadores, botreg;
int Aplicacion_activa;
int resultado = 0;
String usuario;
String version_and;
TextView olvidarclave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_login_aseg);
        progressBar   = (ProgressBar)findViewById(R.id.progressbar3);
        tipo_doc      = (Spinner)findViewById(R.id.tipo_doc);
        nro_doc       = (EditText) findViewById(R.id.nro_doc);
        password      = (EditText) findViewById(R.id.password);
        olvidarclave  = (TextView) findViewById(R.id.olvidarclave);



        blogin        = (Button) findViewById(R.id.blogin);
        bpromotores   = (Button) findViewById(R.id.bpromotores);
        bcontactos    = (Button) findViewById(R.id.bcontactos);
        bprestadores  = (Button) findViewById(R.id.bprestadores);
     //   botreg        = (Button) findViewById(R.id.botreg);


        this.setTitle("");
        progressBar.setVisibility(View.GONE);
        resultado = 0;
        version_and = Librerias.getAndroidVersion_new(getApplicationContext());

        bpromotores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AccesoSistema();
            }
        });
        bprestadores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AccesoEmpresas();
            }
        });

        bcontactos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),MenuContactos.class);
                startActivity(i);

            }
        });

     /*   botreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Registrar_asegurado();

            }
        });*/



        blogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Librerias.verificaConexion(getApplicationContext()))
                {
                    LoginUser();
                }
                else
                    Toast.makeText(getBaseContext(),"No es posible establecer la conexion con el Servidor!" ,Toast.LENGTH_LONG).show();


            }
        });

        olvidarclave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Login_Asegurado.this,Clave_adm.class);
                startActivity(i);
            }
        });
    }
//****************************************************************************************

    private void Registrar_asegurado() {
        Intent i = new Intent(getApplicationContext(),Registro_Asegurado.class);
        startActivity(i);

    }
 //****************************************************************************************

    private void LoginUser() {
    boolean error;

        error = false;

        if (!Librerias.es_entero_valido(nro_doc.getText().toString()))
        {
            error = true;
            Librerias.mostrar_error(Login_Asegurado.this,2,"Debe ingresar el Numero de Documento!");
            nro_doc.requestFocus();
        }

        if (!error && password.getText().toString().isEmpty())
        {
            error = true;
            Librerias.mostrar_error(Login_Asegurado.this,2,"Debe ingresar el Password para continuar!");
            password.requestFocus();
        }

      if (!error)  {
          LoginUsuario();
      }
    }

    //**********************************************************************************
    public void AccesoEmpresas(){
        if (Datos.Esta_Logueadaemp(getApplicationContext()))
        {

            Intent i = new Intent(getApplicationContext(), BuscarAsegurado.class);
            startActivity(i);

        }
        else
        {
            Intent i = new Intent(getApplicationContext(),LoginApp.class);
            i.putExtra("producto", "600");
            i.putExtra("actual", "0");
            i.putExtra("tipoacceso", "2");
            startActivityForResult(i, 600);
        }
    }
    //**********************************************************************************
    public void AccesoSistema(){
        if (Datos.Esta_Logueada(getApplicationContext()))
        {
            Intent i = new Intent(getApplicationContext(),Menu.class);
            startActivity(i);
        }
        else
        {
            Intent i = new Intent(getApplicationContext(),LoginApp.class);
            i.putExtra("producto", "700");
            i.putExtra("actual", "0");
            i.putExtra("tipoacceso", "1");
            startActivityForResult(i, 700);
        }
    }
    /*************************************************************************************************************/
    private void LoginUsuario(){

        preparar();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UserFunctions.loginURL15,
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
                        blogin.setEnabled(true);
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
        blogin.setEnabled(false);
        usuario = "";
    }
    /*************************************************************************************************************/
    public Map<String,String> preparar_Parametros() {
        SharedPreferences Preferences;
        Preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        Map<String,String> params = new HashMap<String, String>();
        params.put("tipodoc"     , tipo_doc.getSelectedItem().toString());
        params.put("dni"         , nro_doc.getText().toString());
        params.put("clave"       , password.getText().toString());
        params.put("version"     , version_and);
        params.put("app"         , getString(R.string.version));
        params.put("tag"         ,"3P197792S");
        params.put("token"       , Preferences.getString("token", ""));
        return params;
    }
    /*************************************************************************************************************/
    public void finalizar(String response) {
        Aplicacion_activa = 0;

        try {
            JSONArray jsonObject = new JSONArray(response);
            JSONObject valor     = new JSONObject(jsonObject.get(0).toString());
            Aplicacion_activa    = valor.getInt("status");
            usuario              = valor.getString("nombre");

            try
            {
                if (Aplicacion_activa == 1)
                {
                    Librerias.Registrar_asegurado(getApplication());
                    Librerias.Registrar_asegurado_id(getApplication(), (int) tipo_doc.getSelectedItemId(), nro_doc.getText().toString());

                    Librerias.mostrar_error(Login_Asegurado.this,1, "Ud. ha sido identificado con Exito!!!! " + usuario);
                    finish();
                    Intent i = new Intent(getApplicationContext(),MenuAsegurado.class);
                    startActivity(i);


                }
                else
                {
                    Librerias.mostrar_error(Login_Asegurado.this,2,"Los datos ingresados son incorrectos, intente nuevamente..!");
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
        blogin.setEnabled(true);

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
    /*************************************************************************************************************/


//public boolean onCreateOptionsMenu(android.view.Menu menu) {
//    getMenuInflater().inflate(R.menu.usuario, menu);
//    return true;
//}
//*******************************************************************************************************************************

/*    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.blanqeo:
                Intent i = new Intent(Login_Asegurado.this,Clave_adm.class);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }*/

}
