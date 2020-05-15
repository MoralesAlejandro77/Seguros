package com.seguros.presupuestos;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class OtrosServicios extends AppCompatActivity {
TextView titulo;
ProgressBar progressBar;
Button bgrabar;
EditText comentario;
int Aplicacion_activa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otros_servicios);
        titulo          = (TextView) findViewById(R.id.xtitulo);
        comentario      = (EditText) findViewById(R.id.comentario);
        bgrabar         = (Button) findViewById(R.id.benviar);
        progressBar     = (ProgressBar)findViewById(R.id.progressBar);

        titulo.setText(this.getIntent().getExtras().getString("titulo"));
        progressBar.setVisibility(View.GONE);

        bgrabar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Informa_Novedad();
            }
        });
    }
/*************************************************************************************************************/
    private void Informa_Novedad(){

        preparar();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UserFunctions.loginURL20,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        finalizar(response);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),error.toString(), Toast.LENGTH_LONG).show();
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
    }
    /*************************************************************************************************************/
    public Map<String,String> preparar_Parametros() {
        Map<String,String> params = new HashMap<String, String>();
        params.put("tag"           ,"3P197792S");
        params.put("tipodoc"       , String.valueOf(Librerias.Leer_tipodoc(getApplicationContext())));
        params.put("dni"           , Librerias.Leer_dni(getApplicationContext()));
        params.put("comentario"    , comentario.getText().toString());
        params.put("titulo"        , titulo.getText().toString());
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

                    Librerias.mostrar_error(OtrosServicios.this,1, "Su solicitud esta siendo procesada!!, nos pondremos en contacto a la brevedad!!");
                    finish();

                }
                else
                {
                    Librerias.mostrar_error(OtrosServicios.this,2,"SE HA PRODUCIDO UN ERROR  ");
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
