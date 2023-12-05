package com.seguros.presupuestos;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
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

public class Modifica_Otros extends AppCompatActivity {
ProgressBar progressBar;
RadioButton opcion1_no, opcion1_si, opcion2_no, opcion2_si, opcion3_no, opcion3_si;
Button bgrabar;


int Aplicacion_activa;
int resultado = 0;
String usuario, estado, version_and;
String sopcion1, sopcion2, sopcion3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_mod_otros);
        progressBar     = (ProgressBar)findViewById(R.id.progressbar3);

        opcion1_no         = (RadioButton)findViewById(R.id.opcion1_no);
        opcion1_si         = (RadioButton)findViewById(R.id.opcion1_si);
        opcion2_no         = (RadioButton)findViewById(R.id.opcion2_no);
        opcion2_si         = (RadioButton)findViewById(R.id.opcion2_si);
        opcion3_no         = (RadioButton)findViewById(R.id.opcion3_no);
        opcion3_si         = (RadioButton)findViewById(R.id.opcion3_si);

        bgrabar         = (Button)   findViewById(R.id.bgrabar);

        progressBar.setVisibility(View.GONE);

        bgrabar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ModificarUSuario();
            }
        });
        estado    = "";
        resultado = 0;
        this.setTitle("");
        version_and = Librerias.getAndroidVersion_new(getApplicationContext());
        Consultar_datos();
    }
//****************************************************************************************
private void Consultar_datos(){
    progressBar.setIndeterminate(true);
    progressBar.setVisibility(View.VISIBLE);
    Aplicacion_activa = 0;
    bgrabar.setEnabled(false);

    StringRequest stringRequest = new StringRequest(Request.Method.POST, UserFunctions.loginURL36,
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
                    bgrabar.setEnabled(true);
                }
            }){

        @Override
        protected Map<String,String> getParams(){
            Map<String,String> params = new HashMap<String, String>();
            params.put("tag"           ,"3P197792S");
            params.put("tipodoc"       , String.valueOf(Librerias.Leer_tipodoc(getApplicationContext())));
            params.put("dni"           , Librerias.Leer_dni(getApplicationContext()));
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

    sopcion1 = "";
    sopcion2 = "";
    sopcion3 = "";

    try {
            JSONArray jsonObject = new JSONArray(response);
            JSONObject json      = new JSONObject(jsonObject.get(0).toString());
            Aplicacion_activa    = json.getInt("status");




            sopcion1             = json.getString("op1_polexp");
            sopcion2             = json.getString("op2_sujob");
            sopcion3             = json.getString("op3_fatca");

    //    Librerias.mostrar_error(Modifica_Otros.this,2,"DATOS CARGADOS :  " + sopcion1 + " " + sopcion2 + " "+ sopcion3 );


            try
            {
                if (Aplicacion_activa == 1) // Exitoso
                {
                    MostrarDatos(getApplicationContext());
                }
                else
                {
                    Librerias.mostrar_error(Modifica_Otros.this,2,"SE HA PRODUCIDO UN ERROR AL LEER DATOS PREVIAMENTE CARGADOS  " );
                }


            }
            catch (Exception e)
            {
                Librerias.mostrar_error(Modifica_Otros.this,2,"ERROR 1 :  " + e.getMessage());
                e.printStackTrace();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        Librerias.mostrar_error(Modifica_Otros.this,2,"ERROR 2 :  " + e.getMessage());
        }
        progressBar.setVisibility(View.GONE);
        bgrabar.setEnabled(true);

    }


//*******************************************************************************************************************************

private void ModificarUSuario(){

        preparar();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UserFunctions.loginURL35,
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
    }
    /*************************************************************************************************************/
    public Map<String,String> preparar_Parametros() {

        sopcion1 = "";
        sopcion2 = "";
        sopcion3 = "";

        if (opcion1_no.isChecked())
            sopcion1 = "NO";

        if (opcion1_si.isChecked())
            sopcion1 = "SI";

        if (opcion2_no.isChecked())
            sopcion2 = "NO";

        if (opcion2_si.isChecked())
            sopcion2 = "SI";

        if (opcion3_no.isChecked())
            sopcion3 = "NO";

        if (opcion3_si.isChecked())
            sopcion3 = "SI";


        Map<String,String> params = new HashMap<String, String>();
        params.put("tag"           ,"3P197792S");
        params.put("tipodoc"       , String.valueOf(Librerias.Leer_tipodoc(getApplicationContext())));
        params.put("dni"           , Librerias.Leer_dni(getApplicationContext()));
        params.put("opcion1"       , sopcion1);
        params.put("opcion2"       , sopcion2);
        params.put("opcion3"       , sopcion3);
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
                    Librerias.mostrar_error(Modifica_Otros.this,1, "La actualizacion de datos fue realizada Exitosamente!!!!");
                    finish();

                }
                else
                {
                    Librerias.mostrar_error(Modifica_Otros.this,2,"SE HA PRODUCIDO UN ERROR : " + estado);
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
    private void MostrarDatos(Context c) {
        opcion1_no.setChecked(false);
        opcion1_si.setChecked(false);
        opcion2_no.setChecked(false);
        opcion2_si.setChecked(false);
        opcion3_no.setChecked(false);
        opcion3_si.setChecked(false);

        if (sopcion1.equals("NO")){
            opcion1_no.setChecked(true);
        }
        if (sopcion1.equals("SI")){
            opcion1_si.setChecked(true);
        }

        if (sopcion2.equals("NO")){
            opcion2_no.setChecked(true);
        }
        if (sopcion2.equals("SI")){
            opcion2_si.setChecked(true);
        }

        if (sopcion3.equals("NO")){
            opcion3_no.setChecked(true);
        }
        if (sopcion3.equals("SI")){
            opcion3_si.setChecked(true);
        }



    }
    /*************************************************************************************************************/
    public void setSpinText(Spinner spin, String text)
    {
        for(int i= 0; i < spin.getAdapter().getCount(); i++)
        {
            if(spin.getAdapter().getItem(i).toString().contains(text))
            {
                spin.setSelection(i);
            }
        }

    }
/*************************************************************************************************************/

}
