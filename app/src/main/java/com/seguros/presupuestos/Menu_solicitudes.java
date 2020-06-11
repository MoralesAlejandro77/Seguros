package com.seguros.presupuestos;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.seguros.Actualizacion.UserFunctions;
import com.seguros.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Menu_solicitudes extends Activity {
    ProgressBar progressBar;
    Button opcion_misproductores, opcion_solicitud_poliza, opcion_solicitud_pago_dia;
    int Aplicacion_activa;
    String Titulo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_solicitudes);

        opcion_misproductores          = (Button)findViewById(R.id.opcion_misproductores);
        opcion_solicitud_poliza        = (Button)findViewById(R.id.opcion_solicitud_poliza);
        opcion_solicitud_pago_dia      = (Button)findViewById(R.id.opcion_solicitud_pago_dia);
        progressBar                    = (ProgressBar) findViewById(R.id.progressBar4);

        this.setTitle("");
        progressBar.setVisibility(View.GONE);
        Titulo = "";
        opcion_misproductores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            /*    Intent i = new Intent(getApplicationContext(),OtrosServicios.class);
                i.putExtra("tipo"  , "1");
                i.putExtra("titulo", "SOLICITUD DE MIS PRODUCTORES");
                startActivity(i);*/
                Titulo = "SOLICITUD DE MIS PRODUCTORES";
                Informa_Novedad();
            }
        });


        opcion_solicitud_poliza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
/*                Intent i = new Intent(getApplicationContext(),OtrosServicios.class);
                i.putExtra("tipo"  , "3");
                i.putExtra("titulo", "SOLICITUD DE POLIZA");
                startActivity(i);*/
                Titulo = "SOLICITUD DE POLIZA";
                Informa_Novedad();

            }
        });

        opcion_solicitud_pago_dia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Intent i = new Intent(getApplicationContext(),OtrosServicios.class);
                i.putExtra("tipo"  , "4");
                i.putExtra("titulo", "SOLICITUD PAGO AL DIA");
                startActivity(i);*/
                Titulo = "SOLICITUD PAGO AL DIA";
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
                        opcion_misproductores.setEnabled(true);
                        opcion_solicitud_poliza.setEnabled(true);
                        opcion_solicitud_pago_dia.setEnabled(true);
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
        opcion_misproductores.setEnabled(false);
        opcion_solicitud_poliza.setEnabled(false);
        opcion_solicitud_pago_dia.setEnabled(false);
    }
    /*************************************************************************************************************/
    public Map<String,String> preparar_Parametros() {
        Map<String,String> params = new HashMap<String, String>();
        params.put("tag"           ,"3P197792S");
        params.put("tipodoc"       , String.valueOf(Librerias.Leer_tipodoc(getApplicationContext())));
        params.put("dni"           , Librerias.Leer_dni(getApplicationContext()));
        params.put("comentario"    , "");
        params.put("titulo"        , Titulo);
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

                    Intent i = new Intent(getApplicationContext(),Solicitud_ok.class);
                    startActivity(i);


                }
                else
                {
                    Librerias.mostrar_error(Menu_solicitudes.this,2,"SE HA PRODUCIDO UN ERROR  ");
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
        opcion_misproductores.setEnabled(true);
        opcion_solicitud_poliza.setEnabled(true);
        opcion_solicitud_pago_dia.setEnabled(true);


    }
//*******************************************************************************************************************************

}
