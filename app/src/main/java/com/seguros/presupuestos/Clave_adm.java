package com.seguros.presupuestos;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Clave_adm extends AppCompatActivity {
Button bblanqueo, breingreso;
ProgressBar progressBar;
    Spinner tipo_doc;
    EditText nro_doc;
    int Aplicacion_activa;
    String version_and;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clave_adm);

        bblanqueo        = (Button) findViewById(R.id.bblanqueo);
        breingreso       = (Button) findViewById(R.id.breingreso);
        progressBar      = (ProgressBar) findViewById(R.id.progressBar3);
        tipo_doc         = (Spinner)findViewById(R.id.tipo_doc);
        nro_doc          = (EditText) findViewById(R.id.nro_doc);
        this.setTitle("");


        progressBar.setVisibility(View.GONE);
        version_and = Librerias.getAndroidVersion_new(getApplicationContext());
        bblanqueo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean error;

                error = false;

                if (!Librerias.es_entero_valido(nro_doc.getText().toString()))
                {
                    error = true;
                    Librerias.mostrar_error(Clave_adm.this,2,"Debe ingresar el Numero de Documento!");
                    nro_doc.requestFocus();
                }
                if (!error){
                    SolicitarCodigo();
                }
            }
        });

        breingreso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),Blanqueo_Asegurado.class);
                startActivityForResult(i, 40);
            }
        });

    }

    /*************************************************************************************************************/
    private void SolicitarCodigo(){

        preparar();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UserFunctions.loginURL21,
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
                        bblanqueo.setEnabled(true);
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
        bblanqueo.setEnabled(false);
    }
    /*************************************************************************************************************/
    public Map<String,String> preparar_Parametros() {
        Map<String,String> params = new HashMap<String, String>();
        params.put("tag"           ,"3P197792S");
        params.put("tipodoc"       , String.valueOf((int) tipo_doc.getSelectedItemId()));
        params.put("dni"           ,  nro_doc.getText().toString());
        params.put("version"     , version_and);
        params.put("app"         , getString(R.string.version));
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

                    Librerias.mostrar_error(Clave_adm.this,1, "Le enviamos un correo con su CLAVE Provisoria!!");
                    finish();

                }
                else
                {
                    Librerias.mostrar_error(Clave_adm.this,2,"SE HA PRODUCIDO UN ERROR  ");
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
        bblanqueo.setEnabled(true);

    }
//*******************************************************************************************************************************
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 40)
        {
            if (Librerias.Esta_registrado_asegurado(getApplicationContext()))
            {
                finish();
                Intent i = new Intent(getApplicationContext(), MenuAsegurado.class);
                startActivity(i);
            }


        }

    }
//*******************************************************************************************************************************

}
