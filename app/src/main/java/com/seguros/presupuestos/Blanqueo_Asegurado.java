package com.seguros.presupuestos;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
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
import com.seguros.Cuentas.Cuentas;
import com.seguros.Datos.Datos;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Blanqueo_Asegurado extends AppCompatActivity {
ProgressBar progressBar;
Spinner tipo_doc;
EditText nro_doc, password1, password2, clavep;
Button bgrabar;
int Aplicacion_activa;
int resultado = 0;
String usuario;
String version_and;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_blanq_aseg);
        progressBar   = (ProgressBar)findViewById(R.id.progressbar3);
        tipo_doc      = (Spinner)findViewById(R.id.tipo_doc);
        nro_doc       = (EditText) findViewById(R.id.nro_doc);
        password1     = (EditText) findViewById(R.id.password1);
        password2     = (EditText) findViewById(R.id.password2);
        clavep        = (EditText) findViewById(R.id.clavep);


        bgrabar        = (Button) findViewById(R.id.blogin);


        this.setTitle("");

        progressBar.setVisibility(View.GONE);
        resultado = 0;
        version_and = Librerias.getAndroidVersion_new(getApplicationContext());


        bgrabar.setOnClickListener(new View.OnClickListener() {
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
        
//**************************************************************************************
    }
 //****************************************************************************************
    private void LoginUser() {
    boolean error;

        error = false;

        if (!Librerias.es_entero_valido(nro_doc.getText().toString()))
        {
            error = true;
            Librerias.mostrar_error(Blanqueo_Asegurado.this,2,"Debe ingresar el Numero de Documento!");
            nro_doc.requestFocus();
        }

        if (!error && clavep.getText().toString().isEmpty())
        {
            error = true;
            Librerias.mostrar_error(Blanqueo_Asegurado.this,2,"Debe ingresar la Clave Provisoria!");
            clavep.requestFocus();
        }

        if (!error && password1.getText().toString().isEmpty())
        {
            error = true;
            Librerias.mostrar_error(Blanqueo_Asegurado.this,2,"Debe ingresar el Password para continuar!");
            password1.requestFocus();
        }

        if (!error && password2.getText().toString().isEmpty())
        {
            error = true;
            Librerias.mostrar_error(Blanqueo_Asegurado.this,2,"Debe ingresar el Password para continuar!");
            password2.requestFocus();
        }

        if (!error && !password1.getText().toString().equals(password2.getText().toString()))
        {
            error = true;
            Librerias.mostrar_error(Blanqueo_Asegurado.this,2,"ATENCION, No coinciden las Claves indicados!");
            password1.requestFocus();
        }

      if (!error)  {
          Blanquaruser();
      }
    }
    /*************************************************************************************************************/
    private void Blanquaruser(){

        preparar();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UserFunctions.loginURL22,
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
                        bgrabar.setEnabled(true);
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
        bgrabar.setEnabled(false);
        usuario = "";
    }
    /*************************************************************************************************************/
    public Map<String,String> preparar_Parametros() {
        SharedPreferences Preferences;
        Preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        Map<String,String> params = new HashMap<String, String>();
        params.put("tipodoc"     , tipo_doc.getSelectedItem().toString());
        params.put("dni"         , nro_doc.getText().toString());
        params.put("clave"       , password1.getText().toString());
        params.put("clavep"      , clavep.getText().toString());
        params.put("version"     , version_and);
        params.put("tag"         ,"3P197792S");
        params.put("app"         , getString(R.string.version));
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

                    Librerias.mostrar_error(Blanqueo_Asegurado.this,1, "Ud. ha sido identificado con Exito!!!! " + usuario);

                    Intent resultData = new Intent();
                    setResult(40,resultData);
                    finish();

                }
                else
                {
                    Librerias.mostrar_error(Blanqueo_Asegurado.this,2,"Los datos ingresados son incorrectos, intente nuevamente..!");
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
        bgrabar.setEnabled(true);

    }
//*******************************************************************************************************************************

}
