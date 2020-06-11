package com.seguros.presupuestos;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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

public class Menu_asegurados_1 extends AppCompatActivity {

    Button opcion_contactenos,  opcion_cambiardatos,
           opcion_solicitudes,
            opcion_contrato,  opcion_pago_online, opcion_cerrarsesion;

    ProgressBar progressBar;
    int Aplicacion_activa;
    String Titulo;

    ImageView siguiente;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_asegurados_1);

        opcion_pago_online     = (Button)findViewById(R.id.opcion_pago_online);
        opcion_contrato        = (Button)findViewById(R.id.opcion_contrato);
        opcion_cambiardatos    = (Button)findViewById(R.id.opcion_cambiardatos);
        opcion_solicitudes     = (Button)findViewById(R.id.opcion_solicitudes);
        opcion_cerrarsesion    = (Button)findViewById(R.id.opcion_cerrarsesion);
        opcion_contactenos     = (Button)findViewById(R.id.opcion_contactenos);
        progressBar            = (ProgressBar) findViewById(R.id.progressBar5);

        this.setTitle("");
        progressBar.setVisibility(View.GONE);
        Titulo = "";

    //    siguiente              = (ImageView) findViewById(R.id.siguiente);


        opcion_pago_online.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),ActivityAyuda.class);
                i.putExtra("ayuda", "9");
                startActivity(i);
            }
        });

   /*     siguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),Menu_asegurados_2.class);
                startActivity(i);
            }
        });*/


        opcion_contrato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            /*    Intent i = new Intent(getApplicationContext(),OtrosServicios.class);
                i.putExtra("tipo"  , "5");
                i.putExtra("titulo", "SOLICITUD DE CONTRATAR SEGURO");
                startActivity(i);*/

                Titulo = "SOLICITUD DE CONTRATAR SEGURO";
                Informa_Novedad();

            }
        });

        opcion_cambiardatos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),Menu_cambiar_datos.class);
                startActivity(i);
            }
        });

        opcion_contactenos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),MenuContactos.class);

                startActivity(i);
            }
        });
        opcion_solicitudes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),Menu_solicitudes.class);

                startActivity(i);
            }
        });

        opcion_cerrarsesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               cerrar_sesion();
            }
        });

    }
//*****************************************
    private void cerrar_sesion() {
        Librerias.Cerrar_sesion(getApplication());
        finish();
        Librerias.mostrar_error(Menu_asegurados_1.this,1, "Su sesion ha sido cerrada correctamente!");

        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
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
                        opcion_contactenos.setEnabled(true);
                        opcion_cambiardatos.setEnabled(true);
                        opcion_solicitudes.setEnabled(true);
                        opcion_contrato.setEnabled(true);
                        opcion_pago_online.setEnabled(true);
                        opcion_cerrarsesion.setEnabled(true);
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
        opcion_contactenos.setEnabled(false);
        opcion_cambiardatos.setEnabled(false);
        opcion_solicitudes.setEnabled(false);
        opcion_contrato.setEnabled(false);
        opcion_pago_online.setEnabled(false);
        opcion_cerrarsesion.setEnabled(false);
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
                    Librerias.mostrar_error(Menu_asegurados_1.this,2,"SE HA PRODUCIDO UN ERROR  ");
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
        opcion_contactenos.setEnabled(true);
        opcion_cambiardatos.setEnabled(true);
        opcion_solicitudes.setEnabled(true);
        opcion_contrato.setEnabled(true);
        opcion_pago_online.setEnabled(true);
        opcion_cerrarsesion.setEnabled(true);


    }
//*******************************************************************************************************************************

}
