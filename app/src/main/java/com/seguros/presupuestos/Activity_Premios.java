package com.seguros.presupuestos;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import java.util.HashMap;
import java.util.Map;

public class Activity_Premios extends AppCompatActivity {
    ProgressBar progressBar;
    Button bbuscar, bpago;
    EditText nro_doc;
    TextView premio;
    int Aplicacion_activa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__premios);
        progressBar     = (ProgressBar)findViewById(R.id.progressbar4);
        bbuscar         = (Button)     findViewById(R.id.bbuscar);
        bpago           = (Button)     findViewById(R.id.bpago);
        nro_doc         = (EditText)   findViewById(R.id.nro_doc);
        premio          = (TextView)   findViewById(R.id.premio);

        progressBar.setVisibility(View.GONE);
        premio.setText("0,00");
        this.setTitle("");
        bbuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Buscar_Premios();
            }
        });
        bpago.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),ActivityAyuda.class);
                i.putExtra("ayuda", "9");
                startActivity(i);
            }
        });

    }

    private void Buscar_Premios() {
        progressBar.setIndeterminate(true);
        progressBar.setVisibility(View.VISIBLE);
        Aplicacion_activa = 0;
        bbuscar.setEnabled(false);
        bpago.setEnabled(false);
        premio.setText("0,00");


        StringRequest stringRequest = new StringRequest(Request.Method.POST, UserFunctions.loginURL23,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        finalizar_datos(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                        bbuscar.setEnabled(true);
                        bpago.setEnabled(true);
                    }
                }){

            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("tag"           ,"3P197792S");
                params.put("tipodoc"       , "1");
                params.put("dni"           , nro_doc.getText().toString());
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

    //****************************************************************************************
    public void finalizar_datos(String response) {
        Aplicacion_activa = 0;

        try {
            JSONArray jsonObject = new JSONArray(response);
            JSONObject json      = new JSONObject(jsonObject.get(0).toString());
            Aplicacion_activa    = json.getInt("status");

            try
            {
                if (Aplicacion_activa == 1) // Exitoso
                {
                    String p = json.getString("premio");
                    //       p = String.format("%.02f", p);
                    premio.setText(p);
                }
                else
                {
                    Librerias.mostrar_error(Activity_Premios.this,2,"NO EXISTE INFORMACION RELACIONADA A ESTE DNI!!" );
                }


            }
            catch (Exception e)
            {
                Librerias.mostrar_error(Activity_Premios.this,2,"NO EXISTE INFORMACION RELACIONADA A ESTE DNI!!" );
                e.printStackTrace();
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Librerias.mostrar_error(Activity_Premios.this,2,"NO EXISTE INFORMACION RELACIONADA A ESTE DNI!!" );
        }
        progressBar.setVisibility(View.GONE);
        bbuscar.setEnabled(true);
        bpago.setEnabled(true);

    }

}
